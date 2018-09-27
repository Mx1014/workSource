package com.everhomes.enterprise;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationWorkPlaces;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.forum.Attachment;
import com.everhomes.group.Group;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.Organization;
import com.everhomes.rest.enterprise.EnterpriseAddressStatus;
import com.everhomes.rest.enterprise.EnterpriseCommunityMapStatus;
import com.everhomes.rest.enterprise.EnterpriseCommunityMapType;
import com.everhomes.rest.enterprise.EnterpriseCommunityType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhEnterpriseAddressesRecord;
import com.everhomes.server.schema.tables.records.EhEnterpriseAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhEnterpriseCommunityMapRecord;
import com.everhomes.server.schema.tables.records.EhEnterpriseDetailsRecord;
import com.everhomes.server.schema.tables.records.EhGroupsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.techpark.expansion.EnterpriseDetail;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class EnterpriseProviderImpl implements EnterpriseProvider {
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private GroupProvider groupProvider;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired 
    private SequenceProvider sequenceProvider;
    
    @Autowired
    private AddressProvider addressProvider;
    
    //TODO enterprise field
    @Override
    public void createEnterprise(Enterprise enterprise) {
        
        //TODO for forum
//        enterprise.setDiscriminator(GroupDiscriminator.ENTERPRISE.getCode());
//        this.groupProvider.createGroup(enterprise);
    }
    
    @Override
    public Enterprise findEnterpriseById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, id));
        EhGroupsDao dao = new EhGroupsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), Enterprise.class);
    }
    
    @Override
    public void updateEnterprise(Enterprise enterprise) {
//        this.groupProvider.updateGroup(enterprise);
    }
    
    @Override
    public Enterprise getEnterpriseById(Long id) {
        Group g = this.groupProvider.findGroupById(id);
        return ConvertHelper.convert(g, Enterprise.class);
    }
    
    @Override
    public void deleteEnterpriseById(Long id) {
        this.groupProvider.deleteGroup(id);
    }
    
    public List<Group> queryGroupsWithOk(CrossShardListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        final List<Group> groups = new ArrayList<>();
        
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhGroups.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            
            locator.setShardIterator(shardIterator);
        }
        
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhGroupsRecord> query = context.selectQuery(Tables.EH_GROUPS);

            if(callback != null)
                callback.buildCondition(locator, query);
                
            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_GROUPS.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_GROUPS.ID.asc());
            query.addLimit(count - groups.size() + 1);
            
            query.fetch().map((r) -> {
                groups.add(ConvertHelper.convert(r, Group.class));
                return null;
            });
            
            if(groups.size() > count) {
                return AfterAction.done;
            }
           
            return AfterAction.next;
        });
        
        if(groups.size() > count) {
            //Bigger than origin, so has more data
            groups.remove(groups.size() - 1);
            locator.setAnchor(groups.get(groups.size() - 1).getId());            
        } else {
            locator.setAnchor(null);
        }
        
        return groups;
    }
    
    @Override
    public List<Enterprise> queryEnterprises(CrossShardListingLocator locator, int count, ListingQueryBuilderCallback callback, Enterprise enterprise) {
        List<Group> groups = this.queryGroupsWithOk(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                if(callback != null)
                    callback.buildCondition(locator, query);
                query.addConditions(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.ENTERPRISE.getCode()));
                if(null != enterprise){
                	if(!StringUtils.isEmpty(enterprise.getName())){
                		query.addConditions(Tables.EH_GROUPS.NAME.like(enterprise.getName() + "%"));
                	}
                }
                return query;
            }
            
        });
        
        List<Enterprise> ents = new ArrayList<Enterprise>();
        for(Group g : groups) {
            ents.add(ConvertHelper.convert(g, Enterprise.class));
        }
        return ents;
    }
    
    @Override
    public List<Enterprise> listEnterprises(CrossShardListingLocator locator, int count) {
        return this.queryEnterprises(locator, count, null, null);
    }
    
    @Override
    public List<Enterprise> listEnterprisesByName(CrossShardListingLocator locator, int count, Enterprise enterprise) {
        return this.queryEnterprises(locator, count, null, enterprise);
    }
    
    @Override
    public void createEnterpriseCommunity(Long creatorId, EnterpriseCommunity ec) {
        //TODO for forum
        ec.setCommunityType(EnterpriseCommunityType.Enterprise.getCode());
        this.communityProvider.createCommunity(creatorId, ec);
    }
    
    @Override
    public EnterpriseCommunity getEnterpriseCommunityById(Long id) {
        Community c = this.communityProvider.findCommunityById(id);
        if(c.getCommunityType() == EnterpriseCommunityType.Enterprise.getCode()) {
            return ConvertHelper.convert(c, EnterpriseCommunity.class);    
        }
        return null;
    }
    
    @Override
    public void createEnterpriseCommunityMap(EnterpriseCommunityMap ec) {
    	long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseCommunityMap.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, ec.getCommunityId()));
        ec.setId(id);
        ec.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        ec.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseCommunityMapDao dao = new EhEnterpriseCommunityMapDao(context.configuration());
        dao.insert(ec);
    }
    
    @Override
    public void updateEnterpriseCommunityMap(EnterpriseCommunityMap ec) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, ec.getCommunityId()));
        ec.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseCommunityMapDao dao = new EhEnterpriseCommunityMapDao(context.configuration());
        ec.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(ec);        
    }
    
    @Override
    public void deleteEnterpriseCommunityMapById(EnterpriseCommunityMap ec) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, ec.getCommunityId()));
        ec.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseCommunityMapDao dao = new EhEnterpriseCommunityMapDao(context.configuration());
        dao.delete(ec);        
    }
    
    @Override
    public EnterpriseCommunityMap getEnterpriseCommunityMapById(Long id) {
        EnterpriseCommunityMap[] result = new EnterpriseCommunityMap[1];
        
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null, 
            (DSLContext context, Object reducingContext) -> {
                result[0] = context.select().from(Tables.EH_ENTERPRISE_COMMUNITY_MAP)
                    .where(Tables.EH_ENTERPRISE_COMMUNITY_MAP.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, EnterpriseCommunityMap.class);
                    });

                if (result[0] != null) {
                    return false;
                } else {
                    return true;
                }
            });
        
        return result[0];
    }
    
    @Override
    public List<EnterpriseCommunityMap> queryEnterpriseMapByCommunityId(ListingLocator locator, Long comunityId
            , int count, ListingQueryBuilderCallback queryBuilderCallback) {
    	final List<EnterpriseCommunityMap> contacts = new ArrayList<EnterpriseCommunityMap>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class, comunityId));
        count = count + 1;
        SelectQuery<EhEnterpriseCommunityMapRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_COMMUNITY_MAP);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
 
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_ID.lt(locator.getAnchor()));
        }
        query.addGroupBy(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_ID);
        query.addOrderBy(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_ID.desc());
        query.addLimit(count - contacts.size());
        query.fetch().map((r) -> {
        	 contacts.add(ConvertHelper.convert(r, EnterpriseCommunityMap.class));
             return null;
        });
        locator.setAnchor(null);
        if(contacts.size() >= count) {
        	contacts.remove(contacts.size() - 1);
            locator.setAnchor(contacts.get(contacts.size() - 1).getMemberId());
        }
        
        return contacts;
    }
    
    @Override
    public EnterpriseCommunityMap findEnterpriseCommunityByEnterpriseId(Long communityId, Long enterpriseId) {
        ListingLocator locator = new ListingLocator();
        List<EnterpriseCommunityMap> enterprises = this.queryEnterpriseMapByCommunityId(locator, communityId, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.COMMUNITY_ID.eq(communityId));
                query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_ID.eq(enterpriseId));
                query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_TYPE.eq(EnterpriseCommunityMapType.Enterprise.getCode()));
                query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_STATUS.ne(EnterpriseCommunityMapStatus.INACTIVE.getCode()));
                return query;
            }
            
        });
        
        if (null != enterprises && enterprises.size() > 0) {
            return enterprises.get(0);
        }
        
        return null;
    }
    
    @Override
    public List<EnterpriseCommunityMap> queryEnterpriseCommunityMap(CrossShardListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback) {
        final List<EnterpriseCommunityMap> contacts = new ArrayList<EnterpriseCommunityMap>();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhGroups.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            
            locator.setShardIterator(shardIterator);
        }
        
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhEnterpriseCommunityMapRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_COMMUNITY_MAP);

            if(queryBuilderCallback != null)
                queryBuilderCallback.buildCondition(locator, query);
                
            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_ENTERPRISE_COMMUNITY_MAP.ID.asc());
            query.addLimit(count - contacts.size());
            
            query.fetch().map((r) -> {
                contacts.add(ConvertHelper.convert(r, EnterpriseCommunityMap.class));
                return null;
            });
           
            if(contacts.size() >= count) {
                locator.setAnchor(contacts.get(contacts.size() - 1).getId());
                return AfterAction.done;
            }
            return AfterAction.next;
 
        });
        return contacts;
    }
    
    //Use join
    @Override
    public List<Enterprise> queryEnterpriseByPhone(String phone) {
        final List<Enterprise> enterprises = new ArrayList<>();
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhGroups.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhGroupsRecord> query = context.selectQuery(Tables.EH_GROUPS);     
            SelectConditionStep<Record1<Long>> step2 = context.select(Tables.EH_ENTERPRISE_CONTACTS.ENTERPRISE_ID)
                    .from(Tables.EH_ENTERPRISE_CONTACTS).join(Tables.EH_ENTERPRISE_CONTACT_ENTRIES)
                    .on(Tables.EH_ENTERPRISE_CONTACTS.ID.eq(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.CONTACT_ID))
                    .where(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ENTRY_VALUE.eq(phone));
            query.addConditions(Tables.EH_GROUPS.ID.in(step2));
            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_GROUPS.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_GROUPS.ID.asc());
            
            query.fetch().map((r) -> {
                enterprises.add(ConvertHelper.convert(r, Enterprise.class));
                return null;
            });
           
            return AfterAction.next;
        });
        
        return enterprises;
    }

	@Override
	public void createEnterpriseAttachment(EnterpriseAttachment attachment) {

		assert(attachment.getEnterpriseId() != null);
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizations.class, attachment.getEnterpriseId()));
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseAttachments.class));
        attachment.setId(id);
        
        EhEnterpriseAttachmentsDao dao = new EhEnterpriseAttachmentsDao(context.configuration());
        dao.insert(attachment);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterpriseAttachments.class, null);
	}

	@Override
	public void createEnterpriseAddress(EnterpriseAddress enterpriseAddr) {

		assert(enterpriseAddr.getEnterpriseId() != null);
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizations.class, enterpriseAddr.getEnterpriseId()));
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseAddresses.class));
        enterpriseAddr.setId(id);
        
        EhEnterpriseAddressesDao dao = new EhEnterpriseAddressesDao(context.configuration());
        dao.insert(enterpriseAddr);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterpriseAddresses.class, null);		
	}
	
	@Override
    public List<EnterpriseAttachment> listEnterpriseAttachments(long enterpriseId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, enterpriseId));
        
        return context.selectFrom(Tables.EH_ENTERPRISE_ATTACHMENTS)
            .where(Tables.EH_ENTERPRISE_ATTACHMENTS.ENTERPRISE_ID.eq(enterpriseId))
            .fetch()
            .map((r)-> { return ConvertHelper.convert(r, EnterpriseAttachment.class); } );
    }

	@Override
	public void populateEnterpriseAttachments(Enterprise enterprise) {

		if(enterprise == null) {
            return;
        } else {
            List<Enterprise> enterprises = new ArrayList<Enterprise>();
            enterprises.add(enterprise);
            
            populateEnterpriseAttachments(enterprises);
        }
	}

	@Override
	public void populateEnterpriseAttachments(List<Enterprise> enterprises) {

		if(enterprises == null || enterprises.size() == 0) {
            return;
        }
            
        final List<Long> enterpriseIds = new ArrayList<Long>();
        final Map<Long, Enterprise> mapEnterprises = new HashMap<Long, Enterprise>();
        
        for(Enterprise enterprise: enterprises) {
        	enterpriseIds.add(enterprise.getId());
        	mapEnterprises.put(enterprise.getId(), enterprise);
        }

        // 平台1.0.0版本更新，已不支持getContentShards()接口，经与kelven讨论目前没有用到多shard，
        // 故先暂时去掉，若后面需要支持多shard再思考解决办法 by lqs 20180516
        //List<Integer> shards = this.shardingProvider.getContentShards(EhGroups.class, enterpriseIds);
        //this.dbProvider.mapReduce(shards, AccessSpec.readOnlyWith(EhGroups.class), null, (DSLContext context, Object reducingContext) -> {
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhEnterpriseAttachmentsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_ATTACHMENTS);
            query.addConditions(Tables.EH_ENTERPRISE_ATTACHMENTS.ENTERPRISE_ID.in(enterpriseIds));
            query.fetch().map((EhEnterpriseAttachmentsRecord record) -> {
            	Enterprise enterprise = mapEnterprises.get(record.getEnterpriseId());
                assert(enterprise != null);
                enterprise.getAttachments().add(ConvertHelper.convert(record, EnterpriseAttachment.class));
            
                return null;
            });
            return true;
        });
	}

	@Override
	public void populateEnterpriseAddresses(Enterprise enterprise) {
		if(enterprise == null) {
            return;
        } else {
            List<Enterprise> enterprises = new ArrayList<Enterprise>();
            enterprises.add(enterprise);
            
            populateEnterpriseAddresses(enterprises);
        }
		
	}

	@Override
	public void populateEnterpriseAddresses(List<Enterprise> enterprises) {

		if(enterprises == null || enterprises.size() == 0) {
            return;
        }
            
		final List<Long> enterpriseIds = new ArrayList<Long>();
        final Map<Long, Enterprise> mapEnterprises = new HashMap<Long, Enterprise>();
        
        for(Enterprise enterprise: enterprises) {
        	enterpriseIds.add(enterprise.getId());
        	mapEnterprises.put(enterprise.getId(), enterprise);
        }

        // 平台1.0.0版本更新，已不支持getContentShards()接口，经与kelven讨论目前没有用到多shard，
        // 故先暂时去掉，若后面需要支持多shard再思考解决办法 by lqs 20180516
        //List<Integer> shards = this.shardingProvider.getContentShards(EhGroups.class, enterpriseIds);
        //this.dbProvider.mapReduce(shards, AccessSpec.readOnlyWith(EhGroups.class), null, (DSLContext context, Object reducingContext) -> {
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhEnterpriseAddressesRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_ADDRESSES);
            query.addConditions(Tables.EH_ENTERPRISE_ADDRESSES.ENTERPRISE_ID.in(enterpriseIds));
            query.fetch().map((EhEnterpriseAddressesRecord record) -> {
            	Enterprise enterprise = mapEnterprises.get(record.getEnterpriseId());
                assert(enterprise != null);
                Address addr = this.addressProvider.findAddressById(record.getAddressId());
                enterprise.getAddress().add(addr);
            
                return null;
            });
            return true;
        });
	}

	@Override
	public Enterprise findEnterpriseByAddressId(long addressId) {
		final Enterprise[] result = new Enterprise[1];
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), result, 
				(DSLContext context, Object reducingContext) -> {
					List<Enterprise> list = context.select().from(Tables.EH_GROUPS).leftOuterJoin(Tables.EH_ENTERPRISE_ADDRESSES)
							.on(Tables.EH_GROUPS.ID.eq(Tables.EH_ENTERPRISE_ADDRESSES.ENTERPRISE_ID))
							.where(Tables.EH_ENTERPRISE_ADDRESSES.ADDRESS_ID.eq(addressId))
							.and(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.ENTERPRISE.getCode()))
							.fetch().map((r) -> {
								return ConvertHelper.convert(r, Enterprise.class);
							});

					if(list != null && !list.isEmpty()){
						result[0] = list.get(0);
						return false;
					}

					return true;
				});

		return result[0];
	}

	@Override
	public Boolean isExistInEnterpriseAddresses(long enterpriseId,
			long addressId) {
		
		List<Integer> addr = new ArrayList<Integer>();
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class, enterpriseId), null, 
				(DSLContext context, Object reducingContext) -> {
					SelectQuery<EhEnterpriseAddressesRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_ADDRESSES);
					query.addConditions(Tables.EH_ENTERPRISE_ADDRESSES.ENTERPRISE_ID.eq(enterpriseId));
					query.addConditions(Tables.EH_ENTERPRISE_ADDRESSES.ADDRESS_ID.eq(addressId));
					query.addConditions(Tables.EH_ENTERPRISE_ADDRESSES.STATUS.ne(EnterpriseAddressStatus.INACTIVE.getCode()));
					List<EhEnterpriseAddressesRecord> r = query.fetch().map((EhEnterpriseAddressesRecord record) -> {
		            	return record;
					});
					
					if(r != null && !r.isEmpty()) {
						addr.add(1);
					}

					return true;
				});
		
		
		return !addr.isEmpty();
	}

	@Override
	public void deleteEnterpriseAttachmentsByEnterpriseId(long enterpriseId) {

		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhOrganizations.class, enterpriseId), null, 
				(DSLContext context, Object reducingContext) -> {
					SelectQuery<EhEnterpriseAttachmentsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_ATTACHMENTS);
					query.addConditions(Tables.EH_ENTERPRISE_ATTACHMENTS.ENTERPRISE_ID.eq(enterpriseId));
		            query.fetch().map((EhEnterpriseAttachmentsRecord record) -> {
		            	deleteEnterpriseAttachmentsById(record.getId());
		            	return null;
					});

					return true;
				});

		
	}
	
	@Override
	public void deleteEnterpriseAddressByEnterpriseId(long enterpriseId) {
		
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhOrganizations.class, enterpriseId), null, 
				(DSLContext context, Object reducingContext) -> {
					SelectQuery<EhEnterpriseAddressesRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_ADDRESSES);
					query.addConditions(Tables.EH_ENTERPRISE_ADDRESSES.ENTERPRISE_ID.eq(enterpriseId));
		            query.fetch().map((EhEnterpriseAddressesRecord record) -> {
		            	this.deleteEnterpriseAttachmentsByEnterpriseId(enterpriseId);
		            	return null;
					});

					return true;
				});

		
	}

    private void deleteEnterpriseAttachmentsById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class));
        EhEnterpriseAttachmentsDao dao = new EhEnterpriseAttachmentsDao(context.configuration());
        dao.deleteById(id);        
    }

	@Override
	public List<EnterpriseAddress> findEnterpriseAddressByEnterpriseId(
			Long enterpriseId) {
		
		List<EnterpriseAddress> ea = new ArrayList<EnterpriseAddress>();
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class, enterpriseId), null, 
				(DSLContext context, Object reducingContext) -> {
					SelectQuery<EhEnterpriseAddressesRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_ADDRESSES);
					query.addConditions(Tables.EH_ENTERPRISE_ADDRESSES.ENTERPRISE_ID.eq(enterpriseId));
					query.addConditions(Tables.EH_ENTERPRISE_ADDRESSES.STATUS.ne(EnterpriseAddressStatus.INACTIVE.getCode()));
					query.fetch().map((EhEnterpriseAddressesRecord record) -> {
						ea.add(ConvertHelper.convert(record, EnterpriseAddress.class));
		            	return null;
					});
					
					return true;
				});
		return ea;
	}

	@Override
	public void deleteEnterpriseAddress(EnterpriseAddress enterpriseAddr) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, enterpriseAddr.getEnterpriseId()));
		enterpriseAddr.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseAddressesDao dao = new EhEnterpriseAddressesDao(context.configuration());
        dao.delete(enterpriseAddr); 		
	}
	
	@Override
	public void deleteEnterpriseAddressById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizations.class, id));
        EhEnterpriseAddressesDao dao = new EhEnterpriseAddressesDao(context.configuration());
        dao.deleteById(id);		
	}

	@Override
	public void updateEnterpriseAddress(EnterpriseAddress ea) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, ea.getEnterpriseId()));
		ea.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseAddressesDao dao = new EhEnterpriseAddressesDao(context.configuration());
        ea.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(ea);
		
	}

	@Override
	public EnterpriseAddress findEnterpriseAddressByAddressId(Long addressId) {
		final EnterpriseAddress[] result = new EnterpriseAddress[1];

        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhEnterpriseAddresses.class), result, 
            (DSLContext context, Object reducingContext) -> {
           	 context.select().from(Tables.EH_ENTERPRISE_ADDRESSES)
       		 .where(Tables.EH_ENTERPRISE_ADDRESSES.ADDRESS_ID.eq(addressId))
       		 .fetch().map(r ->{
       			return result[0] = ConvertHelper.convert(r,EnterpriseAddress.class);
       		});
           	 return true;
       				 
            });
        
        return result[0];
	}

	@Override
	public void createEnterpriseDetail(EnterpriseDetail enterpriseDetail) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EnterpriseDetail.class));
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizations.class, enterpriseDetail.getEnterpriseId()));
		EhEnterpriseDetailsDao dao = new EhEnterpriseDetailsDao(context.configuration());
		enterpriseDetail.setId(id);
		dao.insert(enterpriseDetail);
	}
	
	@Override
	public void updateEnterpriseDetail(EnterpriseDetail enterpriseDetail) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizations.class, enterpriseDetail.getEnterpriseId()));
		EhEnterpriseDetailsDao dao = new EhEnterpriseDetailsDao(context.configuration());
		dao.update(enterpriseDetail);
	}
	
	@Override
	public EnterpriseDetail findEnterpriseDetailByEnterpriseId(Long enterpriseId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhOrganizations.class));
		SelectQuery<EhEnterpriseDetailsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_DETAILS);
		query.addConditions(Tables.EH_ENTERPRISE_DETAILS.ENTERPRISE_ID.eq(enterpriseId));
		List<EnterpriseDetail> enterpriseDetails = new ArrayList<EnterpriseDetail>();
		query.fetch().map(r ->{
			enterpriseDetails.add(ConvertHelper.convert(r,EnterpriseDetail.class));
   			return null;
   		});
		if(0 == enterpriseDetails.size()){
			return null;
		}
		return enterpriseDetails.get(0);
	}

	@Override
	public EnterpriseAddress findEnterpriseAddressByEnterpriseIdAndAddressId(Long enterpriseId, Long addressId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Record record = context.select().from(Tables.EH_ENTERPRISE_ADDRESSES)
			.where(Tables.EH_ENTERPRISE_ADDRESSES.ENTERPRISE_ID.eq(enterpriseId))
			.and(Tables.EH_ENTERPRISE_ADDRESSES.ADDRESS_ID.eq(addressId))
			.fetchOne();
		if (record != null) {
			return ConvertHelper.convert(record, EnterpriseAddress.class);
		}
		return null;
	}

    /**
     * 向eh_enterprise_community_map表中持久化数据
     * @param enterpriseCommunityMap
     */
    @Override
	public void insertIntoEnterpriseCommunityMap(EnterpriseCommunityMap enterpriseCommunityMap){
        //获取上下文
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        //获取一个最大的id值
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseCommunityMap.class));
        enterpriseCommunityMap.setId(id);
        enterpriseCommunityMap.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        enterpriseCommunityMap.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseCommunityMapDao dao = new EhEnterpriseCommunityMapDao(context.configuration());
        dao.insert(enterpriseCommunityMap);
    }

    /**
     * 向eh_organization_community_requests表中添加数据
     * @param organizationCommunityRequest
     */
    @Override
    public void insertIntoOrganizationCommunityRequest(OrganizationCommunityRequest organizationCommunityRequest){
        //获取上下文
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        //获取一个最大的id值
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizationCommunityRequests.class));
        organizationCommunityRequest.setId(id);
        organizationCommunityRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        organizationCommunityRequest.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhOrganizationCommunityRequestsDao dao = new EhOrganizationCommunityRequestsDao(context.configuration());
        dao.insert(organizationCommunityRequest);
    }

    /**
     * 根据组织ID和项目Id来删除该项目下面的公司
     * @param organizationWorkPlaces
     */
    @Override
    public void deleteEnterpriseByOrgIdAndCommunityId(OrganizationWorkPlaces organizationWorkPlaces){
        //获取上下文
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        context.delete(Tables.EH_ORGANIZATION_WORKPLACES).where(Tables.EH_ORGANIZATION_WORKPLACES.COMMUNITY_ID.eq(organizationWorkPlaces.getCommunityId()))
                .and(Tables.EH_ORGANIZATION_WORKPLACES.ORGANIZATION_ID.eq(organizationWorkPlaces.getOrganizationId())).execute();
    }

    /**
     * 根据组织ID和项目Id来删除该项目下面的公司
     * @param enterpriseCommunityMap
     */
    @Override
    public void deleteEnterpriseFromEnterpriseCommunityMapByOrgIdAndCommunityId(EnterpriseCommunityMap enterpriseCommunityMap){
        //获取上下文
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        context.delete(Tables.EH_ENTERPRISE_COMMUNITY_MAP).where(Tables.EH_ENTERPRISE_COMMUNITY_MAP.COMMUNITY_ID.eq(enterpriseCommunityMap.getCommunityId()))
                .and(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_ID.eq(enterpriseCommunityMap.getMemberId())).execute();
    }
	
}
