// @formatter:off
package com.everhomes.enterprise;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Table;

import org.elasticsearch.common.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Operator;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.group.GroupMember;
import com.everhomes.group.IterateGroupMemberCallback;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.recommend.Recommendation;
import com.everhomes.recommend.RecommendationRecordMapper;
import com.everhomes.rest.acl.RoleConstants;
import com.everhomes.rest.enterprise.EnterpriseContactEntryType;
import com.everhomes.rest.enterprise.EnterpriseGroupMemberStatus;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.techpark.company.ContactType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterpriseContactsDao;
import com.everhomes.server.schema.tables.daos.EhEnterpriseContactEntriesDao;
import com.everhomes.server.schema.tables.daos.EhEnterpriseContactGroupsDao;
import com.everhomes.server.schema.tables.daos.EhEnterpriseContactGroupMembersDao;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseContactEntries;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseContactGroupMembers;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseContactGroups;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseContacts;
import com.everhomes.server.schema.tables.pojos.EhGroupMembers;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.server.schema.tables.records.EhEnterpriseContactGroupsRecord;
import com.everhomes.server.schema.tables.records.EhEnterpriseContactsRecord;
import com.everhomes.server.schema.tables.records.EhEnterpriseContactEntriesRecord;
import com.everhomes.server.schema.tables.records.EhEnterpriseContactGroupMembersRecord;
import com.everhomes.server.schema.tables.records.EhRecommendationsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;
import com.everhomes.videoconf.VideoConfProvider;

@Component
public class EnterpriseContactProviderImpl implements EnterpriseContactProvider {
    @Autowired
    private DbProvider dbProvider;
   
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;
    
    @Autowired
    private VideoConfProvider vcProvider;
    
    // TODO for cache. member of eh_groups partition
    @Override
    public Long createContact(EnterpriseContact contact) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseContacts.class));
        //long id = this.shardingProvider.allocShardableContentId(EhGroups.class).second();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, contact.getEnterpriseId()));
        contact.setId(id);
        //Default approving state
        if(contact.getStatus() == null) {
            contact.setStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());    
        }
        
        contact.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseContactsDao dao = new EhEnterpriseContactsDao(context.configuration());
        dao.insert(contact);
        return id;
    }
    
    @Override
    public void updateContact(EnterpriseContact contact) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, contact.getEnterpriseId()));
        EhEnterpriseContactsDao dao = new EhEnterpriseContactsDao(context.configuration());
        dao.update(contact);
    }
    
    @Override
    public void deleteContactById(EnterpriseContact contact) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, contact.getEnterpriseId()));
        EhEnterpriseContactsDao dao = new EhEnterpriseContactsDao(context.configuration());
        dao.deleteById(contact.getId());        
    }
    
    @Override
    public EnterpriseContact getContactById(Long id) {
        EnterpriseContact[] result = new EnterpriseContact[1];
        
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
            (DSLContext context, Object reducingContext) -> {
                result[0] = context.select().from(Tables.EH_ENTERPRISE_CONTACTS)
                    .where(Tables.EH_ENTERPRISE_CONTACTS.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, EnterpriseContact.class);
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
    public EnterpriseContact queryContactByUserId(Long enterpriseId, Long userId) {
        ListingLocator locator = new ListingLocator();
        int count = 1;
        
        List<EnterpriseContact> contacts = this.queryContactByEnterpriseId(locator, enterpriseId, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.USER_ID.eq(userId));
                query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.ENTERPRISE_ID.eq(enterpriseId));
                query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.STATUS.ne(GroupMemberStatus.INACTIVE.getCode()));
                return query;
            }
            
        });
        
        if(contacts != null && contacts.size() > 0) {
            return contacts.get(0);
        }
        return null;
    }
    
    @Override
    public EnterpriseContact queryContactByUserId(Long userId) {
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
    	SelectQuery<EhEnterpriseContactsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACTS);
    	query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.USER_ID.eq(userId));
        query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.STATUS.ne(GroupMemberStatus.INACTIVE.getCode()));
        
        Result<EhEnterpriseContactsRecord> r= query.fetch();
        
        if(null != r && r.size() > 0){
        	return ConvertHelper.convert(r.get(0), EnterpriseContact.class);
        }
        return null;
    }
    
    @Override
    public List<EnterpriseContact> queryContactByEnterpriseId(ListingLocator locator, Long enterpriseId
            , int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, enterpriseId));
 
        SelectQuery<EhEnterpriseContactsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACTS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
 
        query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.ENTERPRISE_ID.eq(enterpriseId));
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.ID.gt(locator.getAnchor()));
            }
        
        //query.addOrderBy(Tables.EH_ENTERPRISE_CONTACTS.CREATE_TIME.desc());
        query.addLimit(count);
        return query.fetch().map((r) -> {
            return ConvertHelper.convert(r, EnterpriseContact.class);
        });
    }
    
    @Override
    public List<EnterpriseContact> listContactByEnterpriseId(ListingLocator locator, Long enterpriseId, int count,String keyWord) {
        return this.queryContactByEnterpriseId(locator, enterpriseId, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
            	List<Byte> conditionList = new ArrayList<Byte>();
            	conditionList.add(GroupMemberStatus.ACTIVE.getCode());
            	conditionList.add(GroupMemberStatus.WAITING_FOR_ACCEPTANCE.getCode());
                query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.STATUS.in(conditionList));
            	if (null!= keyWord)
            		 query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.NAME.like("%"+keyWord+"%").or(Tables.EH_ENTERPRISE_CONTACTS.NICK_NAME.like("%"+keyWord+"%")).or(Tables.EH_ENTERPRISE_CONTACTS.STRING_TAG1.like("%"+keyWord+"%")));
                return query;
            }
            
        });
    }
    
    @Override
    public List<EnterpriseContact> queryContacts(CrossShardListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback) {
        final List<EnterpriseContact> contacts = new ArrayList<EnterpriseContact>();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhGroups.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            
            locator.setShardIterator(shardIterator);
        }
        
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhEnterpriseContactsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACTS);

            if(queryBuilderCallback != null)
                queryBuilderCallback.buildCondition(locator, query);
                
            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_ENTERPRISE_CONTACTS.ID.asc());
            query.addLimit(count - contacts.size());
            
            query.fetch().map((r) -> {
                contacts.add(ConvertHelper.convert(r, EnterpriseContact.class));
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
    
    @Override
    public List<EnterpriseContact> queryEnterpriseContactByPhone(String phone) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        
        final List<EnterpriseContact> contacts = new ArrayList<EnterpriseContact>();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhGroups.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            
            locator.setShardIterator(shardIterator);
        }
        
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhEnterpriseContactsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACTS);
            query.addJoin(Tables.EH_ENTERPRISE_CONTACT_ENTRIES
                    , Tables.EH_ENTERPRISE_CONTACT_ENTRIES.CONTACT_ID.eq(Tables.EH_ENTERPRISE_CONTACTS.ID));
            query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ENTRY_TYPE.eq(EnterpriseContactEntryType.Mobile.getCode()));
            query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ENTRY_VALUE.eq(phone));
            System.out.println(query.toString());
            List<EhEnterpriseContactsRecord> records = query.fetch().map(new EnterpriseContactRecordMapper());
            records.stream().map((r) -> {
                contacts.add(ConvertHelper.convert(r, EnterpriseContact.class));
                return null;
            }).collect(Collectors.toList());
            return AfterAction.next;
        });
        return contacts;
    }

    @Override
    public List<EnterpriseContact> queryEnterpriseContactByKeyword(String keyword) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        
        final List<EnterpriseContact> contacts = new ArrayList<EnterpriseContact>();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhGroups.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            
            locator.setShardIterator(shardIterator);
        }
        
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhEnterpriseContactsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACTS); 
            if(!StringUtils.isEmpty(keyword)){
            query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.NAME.like("%"+keyword+"%"));
            }
            System.out.println(query.toString());
            List<EhEnterpriseContactsRecord> records = query.fetch().map(new EnterpriseContactRecordMapper());
            records.stream().map((r) -> {
                contacts.add(ConvertHelper.convert(r, EnterpriseContact.class));
                return null;
            }).collect(Collectors.toList());
            return AfterAction.next;
        });
        return contacts;
    }
    
    @Override
    public void iterateEnterpriseContacts(int count, ListingQueryBuilderCallback queryBuilderCallback, 
        IterateEnterpriseContactCallback callback) {
        if(count <= 0 || callback == null) {
            return;
        }
        
        List<EnterpriseContact> contactList = null;
        int maxIndex = 0; // Max index of group list in loop
        CrossShardListingLocator locator = new CrossShardListingLocator();
        Long pageAnchor = null;
        do {
            locator.setAnchor(pageAnchor);
            
            contactList = queryContacts(locator, count + 1, queryBuilderCallback);
            
            if(contactList == null || contactList.size() == 0) {
                break;
            } else {
                // if there are still more records in db
                if(contactList.size() > count) {
                    maxIndex = contactList.size() - 2;
                    pageAnchor = contactList.get(contactList.size() - 2).getId();
                } else {
                    // no more record in db
                    maxIndex = contactList.size() - 1;
                    pageAnchor = null;
                }

                for(int i = 0; i <= maxIndex; i++) {
                    callback.process(contactList.get(i));
                }
            }
        } while (pageAnchor != null);
    }
    
    @Override
    public void createContactEntry(EnterpriseContactEntry entry) {
        Long enterpriseId = entry.getEnterpriseId();
        if(enterpriseId == null) {
            EnterpriseContact ec = this.getContactById(entry.getContactId());
            enterpriseId = ec.getEnterpriseId();
        }
        entry.setEnterpriseId(enterpriseId);
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseContactEntries.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, entry.getEnterpriseId()));
        entry.setId(id);
        entry.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseContactEntriesDao dao = new EhEnterpriseContactEntriesDao(context.configuration());
        dao.insert(entry);
    }
    
    @Override
    public void updateContactEntry(EnterpriseContactEntry entry) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, entry.getEnterpriseId()));
        entry.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseContactEntriesDao dao = new EhEnterpriseContactEntriesDao(context.configuration());
        dao.update(entry);        
    }
    
    @Override
    public void deleteContactEntry(EnterpriseContactEntry entry) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, entry.getEnterpriseId()));
        entry.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseContactEntriesDao dao = new EhEnterpriseContactEntriesDao(context.configuration());
        dao.delete(entry);        
    }
    
    @Override
    public EnterpriseContactEntry getContactEntryById(Long id) {
        EnterpriseContactEntry[] result = new EnterpriseContactEntry[1];
        
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
            (DSLContext context, Object reducingContext) -> {
                result[0] = context.select().from(Tables.EH_ENTERPRISE_CONTACT_ENTRIES)
                    .where(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, EnterpriseContactEntry.class);
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
    public List<EnterpriseContactEntry> queryEnterpriseContactEntries(byte entryType, String entryValue) {
        List<EnterpriseContactEntry> results = new ArrayList<EnterpriseContactEntry>();
        
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class),
            results, (DSLContext context, Object reducingContext) -> {
                SelectQuery<EhEnterpriseContactEntriesRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACT_ENTRIES);
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ENTRY_TYPE.eq(entryType));
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ENTRY_VALUE.eq(entryValue));
                query.fetch().map((r) -> {
                    results.add(ConvertHelper.convert(r, EnterpriseContactEntry.class));
                    return null;
                });

                return true;
            });
        
        return results;
    }
     
    
    @Override
    public List<EnterpriseContactEntry> queryContactEntryByEnterpriseId(ListingLocator locator, Long enterpriseId
            , int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, enterpriseId));
 
        SelectQuery<EhEnterpriseContactEntriesRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACT_ENTRIES);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
         

        query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ENTERPRISE_ID.eq(enterpriseId));
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ID.gt(locator.getAnchor()));
            }
        
        //query.addOrderBy(Tables.EH_ENTERPRISE_CONTACTS.CREATE_TIME.desc());
        query.addLimit(count);
        return query.fetch().map((r) -> {
            return ConvertHelper.convert(r, EnterpriseContactEntry.class);
        });
    }

    @Override
    public List<EnterpriseContactEntry> queryContactEntryByEnterpriseIdAndPhone(ListingLocator locator, Long enterpriseId
            , String phoneString , ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, enterpriseId));
 
        SelectQuery<EhEnterpriseContactEntriesRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACT_ENTRIES);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
        

        if(null!=locator && null != locator.getAnchor() ) {
            query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ID.gt(locator.getAnchor()));
            }
        
        query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ENTERPRISE_ID.eq(enterpriseId));
        query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ENTRY_TYPE.eq(ContactType.MOBILE.getCode()));
        query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ENTRY_VALUE.eq(phoneString));
        
        //query.addOrderBy(Tables.EH_ENTERPRISE_CONTACTS.CREATE_TIME.desc());
     
        return query.fetch().map((r) -> {
            return ConvertHelper.convert(r, EnterpriseContactEntry.class);
        });
    }
    @Override
    public List<EnterpriseContactEntry> queryContactEntryByContactId(ListingLocator locator , Integer count,  EnterpriseContact contact) {
    	return this.queryContactEntryByEnterpriseId(locator, contact.getEnterpriseId(), count, new ListingQueryBuilderCallback() {

			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ENTERPRISE_ID.eq(contact.getEnterpriseId()));
				query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.CONTACT_ID.eq(contact.getId()));
				return query;
			}
    		
    	});
    }
    
    @Override
    public List<EnterpriseContactEntry> queryContactEntryByContactId(EnterpriseContact contact) {
        ListingLocator locator = new ListingLocator();
        return this.queryContactEntryByEnterpriseId(locator, contact.getEnterpriseId(), 100, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ENTERPRISE_ID.eq(contact.getEnterpriseId()));
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.CONTACT_ID.eq(contact.getId()));
                return query;
            }
        });
    }

    @Override
    public List<EnterpriseContactEntry> queryContactEntryByContactId(EnterpriseContact contact,Byte contactType ) {
        ListingLocator locator = new ListingLocator();
        return this.queryContactEntryByEnterpriseId(locator, contact.getEnterpriseId(), 100, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
            	 query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ENTRY_TYPE.eq(contactType));
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ENTERPRISE_ID.eq(contact.getEnterpriseId()));
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.CONTACT_ID.eq(contact.getId()));
                return query;
            }
        });
    } 

    @Override
    public List<EnterpriseContactEntry> queryContactEntries(CrossShardListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback) {
        final List<EnterpriseContactEntry> contacts = new ArrayList<EnterpriseContactEntry>();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhGroups.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            
            locator.setShardIterator(shardIterator);
        }
        
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhEnterpriseContactEntriesRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACT_ENTRIES);

            if(queryBuilderCallback != null)
                queryBuilderCallback.buildCondition(locator, query);
                
            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ID.asc());
            query.addLimit(count - contacts.size());
            
            query.fetch().map((r) -> {
                contacts.add(ConvertHelper.convert(r, EnterpriseContactEntry.class));
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
    
    /**
     * 通过手机号查询某一家公司下面的通讯录
     */
    @Override
    public  EnterpriseContactEntry getEnterpriseContactEntryByPhone(Long enterpriseId, String value) {
        ListingLocator locator = new ListingLocator();
        List<EnterpriseContactEntry>  entries = this.queryContactEntryByEnterpriseId(locator, enterpriseId, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ENTRY_VALUE.eq(value));
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ENTERPRISE_ID.eq(enterpriseId));
                return query;
            }
            
        });
        
        if(entries != null && entries.size() > 0) {
            return entries.get(0);
        }
        
        return null;
    }
    
    @Override
    public void createContactGroup(EnterpriseContactGroup group) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseContactGroups.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, group.getEnterpriseId()));
        group.setId(id);
        group.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseContactGroupsDao dao = new EhEnterpriseContactGroupsDao(context.configuration());
        dao.insert(group);
    }
    
    @Override
    public void updateContactGroup(EnterpriseContactGroup group) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, group.getEnterpriseId()));
        group.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseContactGroupsDao dao = new EhEnterpriseContactGroupsDao(context.configuration());
        dao.update(group);        
    }
    
    @Override
    public void deleteContactGroup(EnterpriseContactGroup group) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, group.getEnterpriseId()));
        group.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseContactGroupsDao dao = new EhEnterpriseContactGroupsDao(context.configuration());
        dao.delete(group);        
    }
    
    @Override
    public EnterpriseContactGroup getContactGroupById(Long id) {
        EnterpriseContactGroup[] result = new EnterpriseContactGroup[1];
        
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
            (DSLContext context, Object reducingContext) -> {
                result[0] = context.select().from(Tables.EH_ENTERPRISE_CONTACT_GROUPS)
                    .where(Tables.EH_ENTERPRISE_CONTACT_GROUPS.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, EnterpriseContactGroup.class);
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
    public List<EnterpriseContactGroup> queryContactGroupByEnterpriseId(ListingLocator locator, Long enterpriseId
            , int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, enterpriseId));
 
        SelectQuery<EhEnterpriseContactGroupsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACT_GROUPS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUPS.ENTERPRISE_ID.eq(enterpriseId));
        if(null!=locator && null != locator.getAnchor() ) {
            query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUPS.ID.gt(locator.getAnchor()));
            }
        
        //query.addOrderBy(Tables.EH_ENTERPRISE_CONTACTS.CREATE_TIME.desc());
        query.addLimit(count);
        return query.fetch().map((r) -> {
            return ConvertHelper.convert(r, EnterpriseContactGroup.class);
        });
    }
    
    @Override
    public EnterpriseContactGroup getContactGroupByName(Long enterpriseId, String group) {
        ListingLocator locator = new ListingLocator();
        List<EnterpriseContactGroup> groups = this.queryContactGroupByEnterpriseId(locator, enterpriseId, 1
                , new ListingQueryBuilderCallback() {

                    @Override
                    public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                            SelectQuery<? extends Record> query) {
                        query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUPS.ENTERPRISE_ID.eq(enterpriseId));
                        query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUPS.NAME.eq(group));
                        return query;
                    }
            
        });
        
        if(groups != null && groups.size() > 0) {
            return groups.get(0);
        }
        
        return null;
    }
    
    @Override
    public List<EnterpriseContactGroup> queryContactGroups(CrossShardListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback) {
        final List<EnterpriseContactGroup> contacts = new ArrayList<EnterpriseContactGroup>();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhGroups.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            
            locator.setShardIterator(shardIterator);
        }
        
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhEnterpriseContactGroupsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACT_GROUPS);

            if(queryBuilderCallback != null)
                queryBuilderCallback.buildCondition(locator, query);
                
            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUPS.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_ENTERPRISE_CONTACT_GROUPS.ID.asc());
            query.addLimit(count - contacts.size());
            
            query.fetch().map((r) -> {
                contacts.add(ConvertHelper.convert(r, EnterpriseContactGroup.class));
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
    
    @Override
    public void createContactGroupMember(EnterpriseContactGroupMember member) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseContactGroupMembers.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, member.getEnterpriseId()));
        member.setId(id);
        member.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseContactGroupMembersDao dao = new EhEnterpriseContactGroupMembersDao(context.configuration());
        dao.insert(member);
    }
    
    @Override
    public void updateContactGroupMember(EnterpriseContactGroupMember member) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, member.getEnterpriseId()));
        member.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseContactGroupMembersDao dao = new EhEnterpriseContactGroupMembersDao(context.configuration());
        dao.update(member);        
    }
    
    @Override
    public void deleteContactGroupMember(EnterpriseContactGroupMember member) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, member.getEnterpriseId()));
        member.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseContactGroupMembersDao dao = new EhEnterpriseContactGroupMembersDao(context.configuration());
        dao.delete(member);        
    }
    
    @Override
    public EnterpriseContactGroupMember getContactGroupMemberById(Long id) {
        EnterpriseContactGroupMember[] result = new EnterpriseContactGroupMember[1];
        
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
            (DSLContext context, Object reducingContext) -> {
                result[0] = context.select().from(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS)
                    .where(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, EnterpriseContactGroupMember.class);
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
    public List<EnterpriseContactGroupMember> queryContactGroupMemberByEnterpriseId(ListingLocator locator, Long enterpriseId
            , int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, enterpriseId));
 
        SelectQuery<EhEnterpriseContactGroupMembersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.ENTERPRISE_ID.eq(enterpriseId));
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.ID.gt(locator.getAnchor()));
            }
        
        //query.addOrderBy(Tables.EH_ENTERPRISE_CONTACTS.CREATE_TIME.desc());
        query.addLimit(count);
        return query.fetch().map((r) -> {
            return ConvertHelper.convert(r, EnterpriseContactGroupMember.class);
        });
    }

    @Override
    public  List<EnterpriseContactGroupMember> listContactGroupMemberByContactGroupId(Long enterpriseId, Long groupId) {
        ListingLocator locator = new ListingLocator();
        
        List<EnterpriseContactGroupMember> members = this.queryContactGroupMemberByEnterpriseId(locator, enterpriseId, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.ENTERPRISE_ID.eq(enterpriseId));
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.CONTACT_GROUP_ID.eq(groupId));
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.CONTACT_STATUS.ne(EnterpriseGroupMemberStatus.INACTIVE.getCode()));
                return query;
            }
            
        });
        
        if(null != members && members.size() > 0) {
            return members;
        }
        
        return null;
    }
    @Override
    public EnterpriseContactGroupMember getContactGroupMemberByContactId(Long enterpriseId, Long contactId, Long groupId) {
        ListingLocator locator = new ListingLocator();
        
        List<EnterpriseContactGroupMember> members = this.queryContactGroupMemberByEnterpriseId(locator, enterpriseId, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.ENTERPRISE_ID.eq(enterpriseId));
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.CONTACT_ID.eq(contactId));
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.CONTACT_GROUP_ID.eq(groupId));
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.CONTACT_STATUS.ne(EnterpriseGroupMemberStatus.INACTIVE.getCode()));
                return query;
            }
            
        });
        
        if(null != members && members.size() > 0) {
            return members.get(0);
        }
        
        return null;
    }
    
    @Override
    public EnterpriseContactGroupMember getContactGroupMemberByContactId(Long enterpriseId, Long contactId) {
        ListingLocator locator = new ListingLocator();
        
        List<EnterpriseContactGroupMember> members = this.queryContactGroupMemberByEnterpriseId(locator, enterpriseId, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.ENTERPRISE_ID.eq(enterpriseId));
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.CONTACT_ID.eq(contactId));
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.CONTACT_STATUS.ne(EnterpriseGroupMemberStatus.INACTIVE.getCode()));
                return query;
            }
            
        });
        
        //Now, the contact only has a parent group 
//        Collections.sort(members, new Comparator<EnterpriseContactGroupMember>() {
//            @Override
//            public int compareTo(EnterpriseContactGroupMember o1, EnterpriseContactGroupMember o2) {
//            }
//            
//        });
        
        if(null != members && members.size() > 0) {
            return members.get(0);
        }
        
        return null;
    }
    
    @Override
    public List<EnterpriseContactGroupMember> queryContactGroupMembers(CrossShardListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback) {
        final List<EnterpriseContactGroupMember> contacts = new ArrayList<EnterpriseContactGroupMember>();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhGroups.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            
            locator.setShardIterator(shardIterator);
        }
        
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhEnterpriseContactGroupMembersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS);

            if(queryBuilderCallback != null)
                queryBuilderCallback.buildCondition(locator, query);
                
            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.ID.asc());
            query.addLimit(count - contacts.size());
            
            query.fetch().map((r) -> {
                contacts.add(ConvertHelper.convert(r, EnterpriseContactGroupMember.class));
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

	@Override
	public List<EnterpriseContact> listContactRequestByEnterpriseId(
			ListingLocator locator, Long enterpriseId, int count,String keyWord) {
		 return this.queryContactByEnterpriseId(locator, enterpriseId, count, new ListingQueryBuilderCallback() {

	            @Override
	            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
	                    SelectQuery<? extends Record> query) {
	            	List<Byte> conditionList = new ArrayList<Byte>();
	            	conditionList.add(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
	            	conditionList.add(GroupMemberStatus.INACTIVE.getCode());
	                query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.STATUS.in(conditionList));
	                if (null!=keyWord)
	                	 query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.NAME.like("%"+keyWord+"%").or(Tables.EH_ENTERPRISE_CONTACTS.NICK_NAME.like("%"+keyWord+"%")).or(Tables.EH_ENTERPRISE_CONTACTS.STRING_TAG1.like("%"+keyWord+"%")));
	                return query;
	            }
	            
	        });
	}

	@Override
	public EnterpriseContact queryContactById(Long contactId) {
		 ListingLocator locator = new ListingLocator();
	        int count = 1;
	        
	        List<EnterpriseContact> contacts = this.queryContactById(locator, contactId, count, new ListingQueryBuilderCallback() {

	            @Override
	            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
	                    SelectQuery<? extends Record> query) {
	                query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.ID.eq(contactId)); 
	                query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.STATUS.ne(GroupMemberStatus.INACTIVE.getCode()));
	                return query;
	            }
	            
	        });
	        
	        if(contacts != null && contacts.size() > 0) {
	            return contacts.get(0);
	        }
	        return null;
	}

	private List<EnterpriseContact> queryContactById(ListingLocator locator,
			Long contactId, int count,
			ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, contactId));
        
        SelectQuery<EhEnterpriseContactsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACTS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
 
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.ID.gt(locator.getAnchor()));
            }
        
        //query.addOrderBy(Tables.EH_ENTERPRISE_CONTACTS.CREATE_TIME.desc());
        query.addLimit(count);
        return query.fetch().map((r) -> {
            return ConvertHelper.convert(r, EnterpriseContact.class);
        });
	}

	@Override
	public List<EnterpriseContactGroupMember> queryContactGroupMemberByEnterpriseIdAndContactId(
			ListingLocator locator, Long enterpriseId, Long contactId,
			ListingQueryBuilderCallback queryBuilderCallback) {
		 DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, enterpriseId));
		 
	        SelectQuery<EhEnterpriseContactGroupMembersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS);
	        if(queryBuilderCallback != null)
	            queryBuilderCallback.buildCondition(locator, query);

	        if(null!=locator && null != locator.getAnchor() ) {
	            query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUPS.ID.gt(locator.getAnchor()));
	            }
	        
	        
	        //query.addOrderBy(Tables.EH_ENTERPRISE_CONTACTS.CREATE_TIME.desc());
	        query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.CONTACT_ID.eq(contactId));
	        return query.fetch().map((r) -> {
	            return ConvertHelper.convert(r, EnterpriseContactGroupMember.class);
	        });
	}

	@Override

	public List<EnterpriseContactGroup> listContactGroupsByEnterpriseId(
			ListingLocator locator, Long enterpriseId, int count) { 
		 return this.queryContactGroupByEnterpriseId(locator, enterpriseId, count, null );
	}

	@Override
	public EnterpriseContactGroup queryContactGroupById(Long Id) { 
		EnterpriseContactGroup[] result = new EnterpriseContactGroup[1];
	        
	        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
	            (DSLContext context, Object reducingContext) -> {
	                result[0] = context.select().from(Tables.EH_ENTERPRISE_CONTACT_GROUPS)
	                    .where(Tables.EH_ENTERPRISE_CONTACT_GROUPS.ID.eq(Id))
	                    .fetchAny().map((r) -> {
	                        return ConvertHelper.convert(r, EnterpriseContactGroup.class);
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
	public List<EnterpriseContactGroup> queryContactGroupByPath(
			Long enterpriseId, Long groupId) {
		List<EnterpriseContactGroup> groups = this.queryContactGroupByEnterpriseId(null, enterpriseId, 1
                , new ListingQueryBuilderCallback() {

                    @Override
                    public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                            SelectQuery<? extends Record> query) {
                        query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUPS.ENTERPRISE_ID.eq(enterpriseId));
                        query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUPS.PATH.like("%"+groupId+"%"));
                        return query;
                    }
            
        });
        
		return groups;
	}

	public List<Long> deleteContactByEnterpriseId(Long enterpriseId) {

		List<Long> contactIds = new ArrayList<Long>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, enterpriseId));
		 
        SelectQuery<EhEnterpriseContactsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACTS);
        query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.ENTERPRISE_ID.eq(enterpriseId));
 
        
        query.fetch().map((r) -> {
        	EnterpriseContact ec = ConvertHelper.convert(r, EnterpriseContact.class);
        	contactIds.add(ec.getId());
        	this.deleteContactById(ec);
        	return null;
        });
        
        return contactIds;
	}

	@Override
	public void deleteContactEntryByContactId(List<Long> contactIds) {
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
		 
        SelectQuery<EhEnterpriseContactEntriesRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACT_ENTRIES);
        query.addConditions(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.CONTACT_ID.in(contactIds));
        
        
        query.fetch().map((r) -> {
        	EnterpriseContactEntry ece = ConvertHelper.convert(r, EnterpriseContactEntry.class);
        	this.deleteContactEntry(ece);
        	return null;
        });
	}

	@Override
	public EnterpriseContact queryEnterpriseContactor(Long enterpriseId) {
		ListingLocator locator = new ListingLocator();
        int count = 1;
        
        List<EnterpriseContact> contacts = this.queryContactByEnterpriseId(locator, enterpriseId, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.ENTERPRISE_ID.eq(enterpriseId));
                query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.ROLE.eq(RoleConstants.ResourceAdmin));
                query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.STATUS.ne(GroupMemberStatus.INACTIVE.getCode()));
                return query;
            }
            
        });
        
        if(contacts != null && contacts.size() > 0) {
            return contacts.get(0);
        }
        return null;
	}

	@Override
	public List<EnterpriseContact> queryEnterpriseContactByKeywordAndGroupId(
			Long enterpriseId,String keyword, Long enterpriseGroupId) {
		// TODO Auto-generated method stub
		CrossShardListingLocator locator = new CrossShardListingLocator();
		List<Long> contactIds = new ArrayList<Long>();
		if(enterpriseGroupId!=null ){
			List<EnterpriseContactGroupMember> groupMembers = this.listContactGroupMemberByContactGroupId(enterpriseId, enterpriseGroupId);
	       
	        for(EnterpriseContactGroupMember member : groupMembers ){
	        	contactIds.add(member.getContactId());
	        }
        }
		final List<EnterpriseContact> contacts = new ArrayList<EnterpriseContact>();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhGroups.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            
            locator.setShardIterator(shardIterator);
        }
        
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhEnterpriseContactsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACTS); 
            if(!StringUtils.isEmpty(keyword)){
            	query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.NAME.like("%"+keyword+"%"));
            }
            if(enterpriseGroupId!=null ){
            	 query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.ID.in(contactIds));
            }
            if(null!=enterpriseId){
            	query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.ENTERPRISE_ID.in(enterpriseId));
            }
            List<EhEnterpriseContactsRecord> records = query.fetch().map(new EnterpriseContactRecordMapper());
            records.stream().map((r) -> {
                contacts.add(ConvertHelper.convert(r, EnterpriseContact.class));
                return null;
            }).collect(Collectors.toList());
            return AfterAction.next;
        });
        return contacts;
	}

	@Override
	public List<EnterpriseContact> queryContactByEnterpriseId(Long enterpriseId, String keyword) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, enterpriseId));
		 
        SelectQuery<EhEnterpriseContactsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACTS);

        query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.ENTERPRISE_ID.eq(enterpriseId));
        query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.USER_ID.ne(0L));
        
        if(keyword != null) {
        	Condition con = Tables.EH_ENTERPRISE_CONTACTS.NAME.like(keyword + "%");
        	con = con.or(Tables.EH_ENTERPRISE_CONTACTS.STRING_TAG1.like(keyword + "%"));
        	
        	query.addConditions(con);
        }
        
        query.addOrderBy(Tables.EH_ENTERPRISE_CONTACTS.USER_ID);
        
        return query.fetch().map((r) -> {
            return ConvertHelper.convert(r, EnterpriseContact.class);
        });
	}
//    @Override
//    public List<EnterpriseContact> queryEnterpriseContactWithJoin() {
//        EnterpriseContactRecordMapper
//    }

	@Override
	public List<EnterpriseContact> queryContact(
			CrossShardListingLocator locator, int count) {
		final List<EnterpriseContact> contacts = new ArrayList<EnterpriseContact>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
		
//		List<Long> userIds = vcProvider.findUsersByEnterpriseId(null);
		 
        SelectQuery<EhEnterpriseContactsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACTS);
 
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.ID.gt(locator.getAnchor()));
        }

        query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.USER_ID.ne(0L));
        query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.STATUS.eq((byte) 3));
//        query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.USER_ID.notIn(userIds));
        query.addLimit(count);
        
        query.fetch().map((r) -> {
            contacts.add(ConvertHelper.convert(r, EnterpriseContact.class));
            return null;
        });
       
        if(contacts.size() >= count) {
            locator.setAnchor(contacts.get(contacts.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return contacts;
        
	}

	@Override
	public List<Long> queryUserIds() {
		List<Long> userIds = new ArrayList<Long>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
		 
        SelectQuery<EhEnterpriseContactsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CONTACTS);
        query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.STATUS.eq(GroupMemberStatus.ACTIVE.getCode()));
 
        
        query.fetch().map((r) -> {
        	EnterpriseContact ec = ConvertHelper.convert(r, EnterpriseContact.class);
        	userIds.add(ec.getUserId());
        	return null;
        });
        
        return userIds;
	}
}
