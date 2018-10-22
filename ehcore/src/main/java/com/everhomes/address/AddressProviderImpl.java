// @formatter:off
package com.everhomes.address;

import com.everhomes.asset.AddressIdAndName;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.namespace.Namespace;
import com.everhomes.naming.NameMapper;
import com.everhomes.openapi.ContractBuildingMapping;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.organization.pm.CommunityPmOwner;
import com.everhomes.rest.address.*;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.community.BuildingAdminStatus;
import com.everhomes.rest.community.ListApartmentsInCommunityCommand;
import com.everhomes.rest.organization.OrganizationAddressStatus;
import com.everhomes.rest.organization.pm.reportForm.ApartmentReportFormDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhOrganizationAddressMappings;
import com.everhomes.server.schema.tables.EhPaymentBills;
import com.everhomes.server.schema.tables.daos.EhAddressArrangementDao;
import com.everhomes.server.schema.tables.daos.EhAddressAttachmentsDao;
import com.everhomes.server.schema.tables.daos.EhAddressesDao;
import com.everhomes.server.schema.tables.daos.EhContractBuildingMappingsDao;
import com.everhomes.server.schema.tables.pojos.EhAddressArrangement;
import com.everhomes.server.schema.tables.pojos.EhAddressAttachments;
import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.server.schema.tables.pojos.EhContractBuildingMappings;
import com.everhomes.server.schema.tables.records.EhAddressesRecord;
import com.everhomes.server.schema.tables.records.EhContractBuildingMappingsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;
import com.everhomes.util.RecordHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record6;
import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.SelectQuery;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;

@Component
public class AddressProviderImpl implements AddressProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressProviderImpl.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private ContractProvider contractProvider;
    
    @Override
    public void createAddress(Address address) {
        // 平台1.0.0版本更新主表ID获取方式 by lqs 20180516
        long id = this.dbProvider.allocPojoRecordId(EhAddresses.class);
        //long id = shardingProvider.allocShardableContentId(EhAddresses.class).second(); 

        address.setId(id); 
        address.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime())); 
        address.setUuid(UUID.randomUUID().toString());
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddresses.class, id)); 

        EhAddressesDao dao = new EhAddressesDao(context.configuration()); 
        dao.insert(address); 

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhAddresses.class, null); 
        
    }
    
    @Override
    public void createAddress2(Address address) {
    	long startTime = System.currentTimeMillis();
        // 平台1.0.0版本更新主表ID获取方式 by lqs 20180516
    	long id = this.dbProvider.allocPojoRecordId(EhAddresses.class);
        //long id = shardingProvider.allocShardableContentId(EhAddresses.class).second(); 

        address.setId(id); 
        address.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime())); 
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddresses.class, id)); 

        EhAddressesDao dao = new EhAddressesDao(context.configuration()); 
        dao.insert(address); 

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhAddresses.class, null); 
        long endTime = System.currentTimeMillis();
		LOGGER.info("successed insert one record.time=" + (endTime - startTime));
    }

    @Override
    public long createAddress3(Address address) {
        // 平台1.0.0版本更新主表ID获取方式 by lqs 20180516
        long id = this.dbProvider.allocPojoRecordId(EhAddresses.class);

        address.setId(id);
        address.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        address.setUuid(UUID.randomUUID().toString());
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddresses.class, id));

        EhAddressesDao dao = new EhAddressesDao(context.configuration());
        dao.insert(address);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhAddresses.class, null);

        return address.getId();
    }

    @Caching(evict = { @CacheEvict(value="Address", key="#address.id"),
            @CacheEvict(value="Apartment", key="{#address.communityId, #address.buildingName, #address.apartmentName}") })
    @Override
    public void updateAddress(Address address) {
        assert(address.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddresses.class, address.getId()));
        EhAddressesDao dao = new EhAddressesDao(context.configuration());
        dao.update(address);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAddresses.class, address.getId());
    }

    @Caching(evict = { @CacheEvict(value="Address", key="#address.id"),
            @CacheEvict(value="Apartment", key="{#address.communityId, #address.buildingName, #address.apartmentName}") })
    @Override
    public void deleteAddress(Address address) {
        assert(address.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddresses.class, address.getId()));
        EhAddressesDao dao = new EhAddressesDao(context.configuration());
        dao.deleteById(address.getId());
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAddresses.class, address.getId());
    }

    @Override
    public void deleteAddressById(long id) {
        // work around limitation posted by Spring proxy based injection
        AddressProvider self = PlatformContext.getComponent(AddressProvider.class);
        Address address = self.findAddressById(id);
        if(address != null)
            self.deleteAddress(address);
    }

    /**
     * 根据addressId来查询eh_addresses表中对应的楼栋和门牌信息
     * @param id
     * @return
     */
    @Cacheable(value="Address", key="#id",unless="#result==null")
    @Override
    public Address findAddressById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddresses.class, id));
        EhAddressesDao dao = new EhAddressesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), Address.class);
    }


    @Override
    public Address findGroupAddress(Long groupId) {
        final Address[] result = new Address[1];

        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null,
                (DSLContext context, Object reducingContext) -> {

                    List<Address> list = context.select(Tables.EH_ADDRESSES.fields()).from(Tables.EH_ADDRESSES)
                            .join(Tables.EH_GROUPS).on(Tables.EH_ADDRESSES.ID.eq(Tables.EH_GROUPS.INTEGRAL_TAG1))
                            .where(Tables.EH_GROUPS.ID.eq(groupId))
                            .fetch().map((r) -> RecordHelper.convert(r, Address.class));

                    if(list != null && !list.isEmpty()){
                        result[0] = list.get(0);
                        return false;
                    }

                    return true;
                });

        return result[0];
    }
    
    @Cacheable(value="Apartment", key="{#communityId, #buildingName, #apartmentName}" ,unless="#result==null")
    @Override
    public Address findApartmentAddress(Integer namespaceId, long communityId, String buildingName, String apartmentName) {
        final Address[] result = new Address[1];
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null, 
               (DSLContext context, Object reducingContext) -> {

             List<Address> list = context.select().from(Tables.EH_ADDRESSES)
                .where(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId))
                .and(Tables.EH_ADDRESSES.BUILDING_NAME.eq(buildingName))
                .and(Tables.EH_ADDRESSES.APARTMENT_NAME.eq(apartmentName))
                .and(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
                .fetch().map((r) -> {
                   return ConvertHelper.convert(r, Address.class); 
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
    public List<Address> queryAddress(CrossShardListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback) {
    	int namespaceId = (UserContext.current().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getNamespaceId();
        final List<Address> addresses = new ArrayList<>();
        
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhAddresses.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            
            locator.setShardIterator(shardIterator);
        }
        
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhAddressesRecord> query = context.selectQuery(Tables.EH_ADDRESSES);

            if(queryBuilderCallback != null)
                queryBuilderCallback.buildCondition(locator, query);
                
            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_ADDRESSES.ID.gt(locator.getAnchor()));
            
            query.addConditions(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId));
            query.addOrderBy(Tables.EH_ADDRESSES.ID.asc());
            query.addLimit(count - addresses.size());
            
            query.fetch().map((r) -> {
                addresses.add(ConvertHelper.convert(r, Address.class));
                return null;
            });
           
            if(addresses.size() >= count) {
                locator.setAnchor(addresses.get(addresses.size() - 1).getId());
                return AfterAction.done;
            }
            return AfterAction.next;
        });

        if(addresses.size() > 0) {
            locator.setAnchor(addresses.get(addresses.size() - 1).getId());
        }
        
        return addresses;
    }

    @Override
    public List<ApartmentDTO> listApartmentsByBuildingName(long communityId, String buildingName , int offset , int size) {
    	int namespaceId = UserContext.getCurrentNamespaceId(null);
        List<ApartmentDTO> results = new ArrayList<>();
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    
                    context.select(Tables.EH_ADDRESSES.ID,Tables.EH_ADDRESSES.APARTMENT_NAME, Tables.EH_ADDRESSES.ADDRESS)
                        .from(Tables.EH_ADDRESSES)
                        .where(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(communityId)
                        .and(Tables.EH_ADDRESSES.BUILDING_NAME.equal(buildingName)))
                        .and(Tables.EH_ADDRESSES.STATUS.equal(AddressAdminStatus.ACTIVE.getCode()))
                        .and(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
                        //.limit(size).offset(offset)
                        .fetch().map((r) -> {
                            ApartmentDTO apartment = new ApartmentDTO();
                            apartment.setAddressId(r.getValue(Tables.EH_ADDRESSES.ID));
                            apartment.setApartmentName(r.getValue(Tables.EH_ADDRESSES.APARTMENT_NAME));
                            apartment.setAddress(r.getValue(Tables.EH_ADDRESSES.ADDRESS));
                            results.add(apartment);
                            return null;
                        });
                    
                return true;
            });
        return results;
    }
    
    @Override
    public int countApartmentsByBuildingName(long communityId, String buildingName) {
    	int namespaceId = (UserContext.current().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getNamespaceId();
        final Integer[] count = new Integer[1];
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    count[0] = context.selectCount().from(Tables.EH_ADDRESSES)
                            .leftOuterJoin(Tables.EH_GROUPS)
                            .on(Tables.EH_ADDRESSES.ID.eq(Tables.EH_GROUPS.INTEGRAL_TAG1))
                            .where(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(communityId))
                                    .and(Tables.EH_ADDRESSES.STATUS.equal(AddressAdminStatus.ACTIVE.getCode()))
                                    .and(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
                                    .and(Tables.EH_GROUPS.MEMBER_COUNT.greaterThan(0L))
                    .fetchOneInto(Integer.class);
                    return true;
                });
        
        return count[0];
    }
    
    //@Cacheable(value="Address", key="#id",unless="#result==null")
    @Override
    public Address findAddressByUuid(String uuid) {
        final Address[] addresses = new Address[1];
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null, 
                (DSLContext context, Object reducingContext)-> {
                   context.select().from(Tables.EH_ADDRESSES)
                   .where(Tables.EH_ADDRESSES.UUID.eq(uuid))
                   .fetch().map(r ->{
                       addresses[0] = ConvertHelper.convert(r, Address.class);
                       return null;
                   });
                   return true; 
                });
        return addresses[0];
    }

    @Override
    public Address findAddressByRegionAndAddress(Long cityId, Long areaId, String address) {
    	int namespaceId = (UserContext.current().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getNamespaceId();
        final Address[] addresses = new Address[1];
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null, 
                (DSLContext context, Object reducingContext)-> {
                   context.select().from(Tables.EH_ADDRESSES)
                   .where(Tables.EH_ADDRESSES.CITY_ID.eq(cityId)
                           .and(Tables.EH_ADDRESSES.AREA_ID.eq(areaId))
                           .and(Tables.EH_ADDRESSES.ADDRESS.eq(address))
                           .and(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId)))
                   .fetch().map(r ->{
                       addresses[0] = ConvertHelper.convert(r, Address.class);
                       return null;
                   });
                   return true; 
                });
        return addresses[0];
    }

	@Override
	public Address findAddressByAddress(String address) {
		int namespaceId = (UserContext.current().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getNamespaceId();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Record r = context.select().from(Tables.EH_ADDRESSES).where(Tables.EH_ADDRESSES.ADDRESS.eq(address))
				.and(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId)).fetchOne();
		if(r != null)
			return ConvertHelper.convert(r, Address.class);
		return null;
	}
	
	 @Override
	 public Address findAddressByCommunityAndAddress(Long cityId, Long areaId, Long communityId, String addressName) {
		 int namespaceId = UserContext.getCurrentNamespaceId();
		 List<Address> addresses = new ArrayList<Address>();
	        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null, 
	                (DSLContext context, Object reducingContext)-> {
	                   context.select().from(Tables.EH_ADDRESSES)
	                   .where(Tables.EH_ADDRESSES.CITY_ID.eq(cityId)
	                           .and(Tables.EH_ADDRESSES.AREA_ID.eq(areaId))
	                           .and(Tables.EH_ADDRESSES.ADDRESS.eq(addressName))
	                           .and(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId)))
	                           .and(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
	                   .fetch().map(r ->{
	                	   addresses.add(ConvertHelper.convert(r, Address.class));
	                       return null;
	                   });
	                   return true; 
	                });
	        if(0 == addresses.size())
	        	return null;
	        return addresses.get(0);
	  }

    @Override
    public List<Address> listAddressByIds(Integer namespaceId, List<Long> ids) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ADDRESSES)
                .where(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ADDRESSES.ID.in(ids))
                .and(Tables.EH_ADDRESSES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
                .fetchInto(Address.class);
    }

    @Override
    public List<Address> listAddressOnlyByIds(List<Long> ids) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ADDRESSES)
                .where(Tables.EH_ADDRESSES.ID.in(ids))
                .and(Tables.EH_ADDRESSES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
                .fetchInto(Address.class);
    }

    @Override
    public List<AddressDTO> listAddressByBuildingName(Integer namespaceId, Long communityId, String buildingName) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ADDRESSES)
                .where(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId))
                .and(Tables.EH_ADDRESSES.BUILDING_NAME.like(DSL.concat("%", buildingName, "%")).or(Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME.like(DSL.concat("%", buildingName, "%"))))
                .fetchInto(AddressDTO.class);
    }

	@Override
	public Address findAddressByBuildingApartmentName(Integer namespaceId, Long communityId, String buildingName, String apartmentName) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectConditionStep<Record> step = context.select().from(Tables.EH_ADDRESSES)
	        .where(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
	        .and(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId))
	        .and(Tables.EH_ADDRESSES.APARTMENT_NAME.eq(apartmentName))
			.and(Tables.EH_ADDRESSES.BUILDING_NAME.eq(buildingName));
		
	    Record record = step.fetchAny();

        LOGGER.debug("findAddressByBuildingApartmentName, sql=" + step.getSQL());
        LOGGER.debug("findAddressByBuildingApartmentName, bindValues=" + step.getBindValues());
		if (record != null) {
			return ConvertHelper.convert(record, Address.class);
		}
		return null;
	}
	
    @Override
    public Address findActiveAddressByBuildingApartmentName(Integer namespaceId, Long communityId, String buildingName, String apartmentName) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record> step = context.select().from(Tables.EH_ADDRESSES)
                .where(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId))
                .and(Tables.EH_ADDRESSES.APARTMENT_NAME.eq(apartmentName))
                .and(Tables.EH_ADDRESSES.BUILDING_NAME.eq(buildingName))
                .and(Tables.EH_ADDRESSES.STATUS.eq(AddressAdminStatus.ACTIVE.getCode()));

        Record record = step.fetchAny();

        LOGGER.debug("findAddressByBuildingApartmentName, sql=" + step.getSQL());
        LOGGER.debug("findAddressByBuildingApartmentName, bindValues=" + step.getBindValues());
        if (record != null) {
            return ConvertHelper.convert(record, Address.class);
        }
        return null;
    }

    @Override
    public List<ApartmentAbstractDTO> listAddressByBuildingApartmentName(Integer namespaceId, Long communityId,
                String buildingName, String apartmentName, Byte livingStatus, CrossShardListingLocator locator, int count) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<ApartmentAbstractDTO> addresses = new ArrayList<>();
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_ADDRESSES);
        if(locator.getAnchor() != null)
            query.addConditions(Tables.EH_ADDRESSES.ID.gt(locator.getAnchor()));

        query.addConditions(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_ADDRESSES.STATUS.equal(AddressAdminStatus.ACTIVE.getCode()));
        if(StringUtils.isNotBlank(buildingName)) {
            query.addConditions(Tables.EH_ADDRESSES.BUILDING_NAME.equal(buildingName)
                    .or(Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME.equal(buildingName)));
        }
        if(StringUtils.isNotBlank(apartmentName)) {
            query.addConditions(Tables.EH_ADDRESSES.APARTMENT_NAME.like("%" + apartmentName + "%"));
        }
        //按状态筛选
        if(livingStatus != null) {
            query.addJoin(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS
                    , Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ADDRESS_ID.eq(Tables.EH_ADDRESSES.ID));
            query.addConditions(Tables.EH_ADDRESSES.LIVING_STATUS.eq(livingStatus)
                 .or(Tables.EH_ADDRESSES.LIVING_STATUS.isNull()
                 .and(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.LIVING_STATUS.eq(livingStatus))));
            //数据重复问题，by-djm
            query.addConditions(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.COMMUNITY_ID));
        }

        query.addOrderBy(Tables.EH_ADDRESSES.ID.asc());
        query.addLimit(count - addresses.size());

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("listAddressByBuildingApartmentName, sql=" + query.getSQL());
            LOGGER.debug("listAddressByBuildingApartmentName, bindValues=" + query.getBindValues());
        }

        query.fetch().map((r) -> {
            ApartmentAbstractDTO dto = new ApartmentAbstractDTO();
            dto.setId(r.getValue(Tables.EH_ADDRESSES.ID));
            dto.setBuildingName(r.getValue(Tables.EH_ADDRESSES.BUILDING_NAME));
            dto.setChargeArea(r.getValue(Tables.EH_ADDRESSES.CHARGE_AREA));
            dto.setLivingStatus(r.getValue(Tables.EH_ADDRESSES.LIVING_STATUS));
            dto.setName(r.getValue(Tables.EH_ADDRESSES.APARTMENT_NAME));
            dto.setOrientation(r.getValue(Tables.EH_ADDRESSES.ORIENTATION));
            dto.setIsFutureApartment(r.getValue(Tables.EH_ADDRESSES.IS_FUTURE_APARTMENT));
            addresses.add(dto);
            return null;
        });
        return addresses;
    }

    @Override
	public List<Address> listAddressByNamespaceType(Integer namespaceId, Long communityId, String namespaceType) {
		return dbProvider.getDslContext(AccessSpec.readOnly()).select().from(Tables.EH_ADDRESSES)
	        .where(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
	        .and(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId))
	        .and(Tables.EH_ADDRESSES.NAMESPACE_ADDRESS_TYPE.eq(namespaceType))
	        .fetch()
	        .map(r->ConvertHelper.convert(r, Address.class));
	}

    @Override
    public List<Address> listAddressByNamespaceType(Integer namespaceId, String namespaceType) {
        return dbProvider.getDslContext(AccessSpec.readOnly()).select().from(Tables.EH_ADDRESSES)
                .where(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ADDRESSES.NAMESPACE_ADDRESS_TYPE.eq(namespaceType))
                .fetch()
                .map(r->ConvertHelper.convert(r, Address.class));
    }

    @Override
	public Map<Byte, Integer> countApartmentByLivingStatus(Long communityId) {
		Map<Byte, Integer> map = new HashMap<>();
		dbProvider.getDslContext(AccessSpec.readOnly())
			.select(Tables.EH_ADDRESSES.LIVING_STATUS, DSL.count())
			.from(Tables.EH_ADDRESSES)
			.where(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId))
			.and(Tables.EH_ADDRESSES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
			.groupBy(Tables.EH_ADDRESSES.LIVING_STATUS)
			.fetch()
			.map(r->{
				map.put(r.getValue(Tables.EH_ADDRESSES.LIVING_STATUS), r.getValue(DSL.count()));
				return null;
			});
		return map;
	}

	@Override
	public Integer countApartment(Long communityId) {
		return dbProvider.getDslContext(AccessSpec.readOnly())
			.select(DSL.count())
			.from(Tables.EH_ADDRESSES)
			.where(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId))
			.and(Tables.EH_ADDRESSES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
			.fetch()
			.get(0)
			.getValue(DSL.count());
	}

	@Override
	public void updateOrganizationOwnerAddress(Long addressId) {
		dbProvider.getDslContext(AccessSpec.readWrite())
			.delete(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
			.where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ADDRESS_ID.eq(addressId))
			.execute();
	}

	@Override
	public void updateOrganizationAddress(Long addressId) {
		dbProvider.getDslContext(AccessSpec.readWrite())
			.update(Tables.EH_ORGANIZATION_ADDRESSES)
			.set(Tables.EH_ORGANIZATION_ADDRESSES.STATUS, OrganizationAddressStatus.INACTIVE.getCode())
			.where(Tables.EH_ORGANIZATION_ADDRESSES.ADDRESS_ID.eq(addressId))
			.execute();
	}

	@Override
	public void updateOrganizationAddressMapping(Long addressId) {
		dbProvider.getDslContext(AccessSpec.readWrite())
			.delete(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS)
			.where(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ADDRESS_ID.eq(addressId))
			.execute();
	}

    @Override
    public Address findAddressByNamespaceTypeAndName(String namespaceType, String namespaceToken) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhAddresses.class));
        List<Address> addresses = new ArrayList<>();
        SelectQuery<EhAddressesRecord> query = context.selectQuery(Tables.EH_ADDRESSES);
        query.addConditions(Tables.EH_ADDRESSES.NAMESPACE_ADDRESS_TYPE.eq(namespaceType));
        query.addConditions(Tables.EH_ADDRESSES.NAMESPACE_ADDRESS_TOKEN.eq(namespaceToken));
        query.fetch().map(r ->{
            addresses.add(ConvertHelper.convert(r, Address.class));
            return null;
        });
        if(addresses == null || addresses.size() == 0) {
            return null;
        }
        return addresses.get(0);
    }

    @Override
    public List<Long> listThirdPartRelatedAddresses(String namespaceType, List<String> addressIds) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhAddresses.class));
        return  context.select(Tables.EH_ADDRESSES.ID).from(Tables.EH_ADDRESSES)
                .where(Tables.EH_ADDRESSES.NAMESPACE_ADDRESS_TYPE.eq(namespaceType))
                .and(Tables.EH_ADDRESSES.NAMESPACE_ADDRESS_TOKEN.in(addressIds))
                .fetchInto(Long.class);
    }

    public List<AddressIdAndName> findAddressByPossibleName(Integer currentNamespaceId, Long ownerId, String buildingName, String apartmentName) {
        List<AddressIdAndName> list = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhAddresses r = Tables.EH_ADDRESSES.as("r");
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(r);
        query.addSelect(r.ID,r.BUILDING_NAME,r.APARTMENT_NAME);
        if (buildingName != null && buildingName.trim().length()>0){
            query.addConditions(r.BUILDING_NAME.eq(buildingName));
        }
        if (apartmentName != null && apartmentName.trim().length()>0){
            query.addConditions(r.APARTMENT_NAME.eq(apartmentName));
        }
        query.addConditions(r.NAMESPACE_ID.eq(currentNamespaceId));
        query.addConditions(r.COMMUNITY_ID.eq(ownerId));

        try {
            query.fetch()
                    .map(f -> {
                        AddressIdAndName ian = new AddressIdAndName();
                        ian.setApartmentName(f.getValue(r.APARTMENT_NAME));
                        ian.setAddressId(f.getValue(r.ID));
                        ian.setBuildingName(f.getValue(r.BUILDING_NAME));
                        list.add(ian);
                        return null;
                    });
        } catch (DataAccessException e) {

        }

        return list;
    }


    @Override
    public List<GetApartmentNameByBuildingNameDTO> getApartmentNameByBuildingName(String buildingName, Long communityId, Integer currentNamespaceId) {
        List<GetApartmentNameByBuildingNameDTO> list = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhAddresses t = Tables.EH_ADDRESSES.as("t");
        context.select(t.ID,t.APARTMENT_NAME)
                .from(t)
                .where(t.NAMESPACE_ID.eq(currentNamespaceId))
                .and(t.COMMUNITY_ID.eq(communityId))
                .and(t.BUILDING_NAME.eq(buildingName))
                .fetch()
                .map(r -> {
                    GetApartmentNameByBuildingNameDTO dto = new GetApartmentNameByBuildingNameDTO();
                    dto.setAddressId(r.getValue(t.ID));
                    dto.setApartmentName(r.getValue(t.APARTMENT_NAME));
                    list.add(dto);
                    return null;
                });
        return list;
    }

    @Override
    public void createAddressAttachment(AddressAttachment attachment) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAddressAttachments.class));

        attachment.setId(id);
        attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        LOGGER.info("createAddressAttachment: " + attachment);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddressAttachments.class, id));
        EhAddressAttachmentsDao dao = new EhAddressAttachmentsDao(context.configuration());
        dao.insert(attachment);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhAddressAttachments.class, id);

    }

    @Override
    public void deleteApartmentAttachment(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhAddressAttachmentsDao dao = new EhAddressAttachmentsDao(context.configuration());
        dao.deleteById(id);
    }

    @Override
    public AddressAttachment findByAddressAttachmentId(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhAddressAttachments.class, id));
        EhAddressAttachmentsDao dao = new EhAddressAttachmentsDao(context.configuration());
        EhAddressAttachments result = dao.findById(id);
        if (result == null) {
            return null;
        }
        return ConvertHelper.convert(result, AddressAttachment.class);
    }

    @Override
    public List<AddressAttachment> listAddressAttachments(Long addressId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ADDRESS_ATTACHMENTS)
                .where(Tables.EH_ADDRESS_ATTACHMENTS.ADDRESS_ID.eq(addressId))
                .fetchInto(AddressAttachment.class);
    }

    @Override
    public void updateAddressAttachment(AddressAttachment attachment) {
        assert(attachment.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddressAttachments.class, attachment.getId()));
        EhAddressAttachmentsDao dao = new EhAddressAttachmentsDao(context.configuration());
        dao.update(attachment);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAddressAttachments.class, attachment.getId());
    }

    @Override
    public String findLastVersionByNamespace(Integer namespaceId, Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhAddressesRecord> query = context.selectQuery(Tables.EH_ADDRESSES);
        query.addConditions(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ADDRESSES.STATUS.eq(CommonStatus.ACTIVE.getCode()));
        query.addOrderBy(Tables.EH_ADDRESSES.VERSION.desc());
        query.addLimit(1);
        List<Address> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, Address.class));
            return null;
        });
        if(result == null || result.size() == 0) {
            return null;
        }

        return result.get(0).getVersion();
    }
/**
     * 根据门牌地址集合addressIds进行批量删除门牌地址
     * @param addressIds
     */
    @Override
    public void betchDisclaimAddress(List<Long> addressIds){
        //获取上下文
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        context.delete(Tables.EH_ADDRESSES).where(Tables.EH_ADDRESSES.ID.in(addressIds)).execute();
    }
	@Override
	public List<ContractBuildingMapping> findContractBuildingMappingByAddressId(Long addressId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<ContractBuildingMapping> list = new ArrayList<>();
		
		context.select()
				.from(Tables.EH_CONTRACT_BUILDING_MAPPINGS)
				.where(Tables.EH_CONTRACT_BUILDING_MAPPINGS.ADDRESS_ID.eq(addressId))
				.fetch()
				.map(r->{
					ContractBuildingMapping convert = ConvertHelper.convert(r, ContractBuildingMapping.class);
					list.add(convert);
					return null;
				});
		return list;
	}

	@Override
    public ContractBuildingMapping findContractBuildingMappingByContractId(Long contractId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());


        List<ContractBuildingMapping> mappings = new ArrayList<>();
        SelectQuery<EhContractBuildingMappingsRecord> query = context.selectQuery(Tables.EH_CONTRACT_BUILDING_MAPPINGS);
        query.addConditions(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_ID.eq(contractId));
        query.addOrderBy(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CREATE_TIME.desc());
        query.fetch().map(r ->{
            mappings.add(ConvertHelper.convert(r, ContractBuildingMapping.class));
            return null;
        });
        if(mappings == null || mappings.size() == 0) {
            return null;
        }
        return mappings.get(0);

    }


    @Override
	public void updateContractBuildingMapping(ContractBuildingMapping contractBuildingMapping) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractBuildingMappings.class, contractBuildingMapping.getId()));
        
        EhContractBuildingMappingsDao dao = new EhContractBuildingMappingsDao(context.configuration());
        dao.update(contractBuildingMapping);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhContractBuildingMappings.class, contractBuildingMapping.getId());
	}

    @Override
    public String getAddressNameById(Long addressId) {
        return this.dbProvider.getDslContext(AccessSpec.readOnly()).select(Tables.EH_ADDRESSES.ADDRESS)
                .from(Tables.EH_ADDRESSES).where(Tables.EH_ADDRESSES.ID.eq(addressId))
                .fetchOne(Tables.EH_ADDRESSES.ADDRESS);
    }

    @Override
    public int changeAddressLivingStatus(Long addressId, Byte status) {
//        return this.dbProvider.getDslContext(AccessSpec.readWrite()).update(Tables.EH_ADDRESSES)
//                .set(Tables.EH_ADDRESSES.LIVING_STATUS, status.getCode())
//                .where(Tables.EH_ADDRESSES.ID.eq(addressId))
//                .execute();
        LOGGER.info("address living status xfesijfisejsj");
        return this.dbProvider.getDslContext(AccessSpec.readWrite()).update(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS)
                .set(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.LIVING_STATUS, status)
                .where(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ADDRESS_ID.eq(addressId))
                .execute();
    }

//    @Override
//    public Byte getAddressLivingStatus(Long addressId) {
//        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
//        Byte aByte = context.select(Tables.EH_ADDRESSES.LIVING_STATUS)
//                .from(Tables.EH_ADDRESSES)
//                .where(Tables.EH_ADDRESSES.ID.eq(addressId))
//                .fetchOne(Tables.EH_ADDRESSES.LIVING_STATUS);
//        if(aByte == null){
//            aByte = context.select(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.LIVING_STATUS)
//                    .from(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS)
//                    .where(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ADDRESS_ID.eq(addressId))
//                    .fetchOne(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.LIVING_STATUS);
//        }
//        if(aByte == null){
//            aByte = 1;
//        }
//        return aByte;
//    }

	@Override
	public Byte getAddressLivingStatus(Long addressId, String addressName) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Byte aByte = context.select(Tables.EH_ADDRESSES.LIVING_STATUS)
                .from(Tables.EH_ADDRESSES)
                .where(Tables.EH_ADDRESSES.ID.eq(addressId))
                .and(Tables.EH_ADDRESSES.ADDRESS.eq(addressName))
                .fetchOne(Tables.EH_ADDRESSES.LIVING_STATUS);
        if(aByte == null){
            aByte = context.select(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.LIVING_STATUS)
                    .from(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS)
                    .where(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ADDRESS_ID.eq(addressId))
                    .and(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ORGANIZATION_ADDRESS.eq(addressName))
                    .fetchOne(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.LIVING_STATUS);
        }
        if(aByte == null){
            aByte = 1;
        }
        return aByte;
	}

	@Override
	public int changeAddressLivingStatus(Long addressId, String addressName, byte status) {
		LOGGER.info("address living status xfesijfisejsj");
        return this.dbProvider.getDslContext(AccessSpec.readWrite()).update(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS)
                .set(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.LIVING_STATUS, status)
                .where(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ADDRESS_ID.eq(addressId))
                .and(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ORGANIZATION_ADDRESS.eq(addressName))
                .execute();
	}

	@Override
	public Address findNotInactiveAddressByBuildingApartmentName(Integer namespaceId, Long communityId,String buildingName, String apartmentName) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectConditionStep<Record> step = context.select().from(Tables.EH_ADDRESSES)
	        .where(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
	        .and(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId))
	        .and(Tables.EH_ADDRESSES.APARTMENT_NAME.eq(apartmentName))
			.and(Tables.EH_ADDRESSES.BUILDING_NAME.eq(buildingName))
			.and(Tables.EH_ADDRESSES.STATUS.ne((byte)0));
			
	    Record record = step.fetchAny();

        LOGGER.debug("findAddressByBuildingApartmentName, sql=" + step.getSQL());
        LOGGER.debug("findAddressByBuildingApartmentName, bindValues=" + step.getBindValues());
		if (record != null) {
			return ConvertHelper.convert(record, Address.class);
		}
		return null;
	}

	    /**
     * 根据buildingName和CommunityId来查询eh_addersses表中的门牌的数量
     * @param buildingName
     * @param communityId
     * @return
     */
    public int getApartmentCountByBuildNameAndCommunityId(String buildingName,Long communityId){
        //获取上下文
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        int apartmentCount = context.selectCount().from(Tables.EH_ADDRESSES)
                .where(Tables.EH_ADDRESSES.BUILDING_NAME.eq(buildingName))
                .and(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId))
                .fetchAnyInto(Integer.class);
        return apartmentCount;

    }


    /**
     * 根据id的集合来批量的查询eh_addresses表中信息
     * @param ids
     * @return
     */
    @Override
    public List<Address> findAddressByIds(List<Long> ids,Integer namespaceId){
        //获取上下文
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<Address> addressList = context.select().from(Tables.EH_ADDRESSES)
                .where(Tables.EH_ADDRESSES.ID.in(ids))
                .and(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
                .fetchInto(Address.class);
        return addressList;
    }

	@Override
	public void createAddressArrangement(AddressArrangement arrangement) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(AddressArrangement.class));

		arrangement.setId(id);
		arrangement.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        LOGGER.info("createAddressArrangement: " + arrangement);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(AddressArrangement.class, id));
        EhAddressArrangementDao dao = new EhAddressArrangementDao(context.configuration());
        dao.insert(arrangement);

        DaoHelper.publishDaoAction(DaoAction.CREATE, AddressArrangement.class, id);
	}

	@Override
	public AddressArrangement findActiveAddressArrangementByAddressId(Long addressId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return context.select()
					.from(Tables.EH_ADDRESS_ARRANGEMENT)
					.where(Tables.EH_ADDRESS_ARRANGEMENT.ADDRESS_ID.eq(addressId))
					.and(Tables.EH_ADDRESS_ARRANGEMENT.STATUS.eq(AddressArrangementStatus.ACTIVE.getCode()))
					.fetchOneInto(AddressArrangement.class);
	}

	@Override
	public void deleteAddressArrangement(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		context.update(Tables.EH_ADDRESS_ARRANGEMENT)
				.set(Tables.EH_ADDRESS_ARRANGEMENT.STATUS,AddressArrangementStatus.INACTIVE.getCode())
				.where(Tables.EH_ADDRESS_ARRANGEMENT.ID.eq(id))
				.execute();
	}

	@Override
	public AddressArrangement findAddressArrangementById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(AddressArrangement.class));
        EhAddressArrangementDao dao = new EhAddressArrangementDao(context.configuration());
        EhAddressArrangement record = dao.findById(id);
        if (record != null) {
			return ConvertHelper.convert(record, AddressArrangement.class);
		}
        return null;
	}

	@Override
	public AddressArrangement findActiveAddressArrangementByTargetId(Long addressId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return context.select()
					.from(Tables.EH_ADDRESS_ARRANGEMENT)
					.where(Tables.EH_ADDRESS_ARRANGEMENT.TARGET_ID.like(DSL.concat("%", addressId.toString(), "%")))
					.and(Tables.EH_ADDRESS_ARRANGEMENT.STATUS.eq(AddressArrangementStatus.ACTIVE.getCode()))
					.fetchOneInto(AddressArrangement.class);
	}

	@Override
	public String findApartmentNameById(long addressId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        String apartmentName = context.select(Tables.EH_ADDRESSES.APARTMENT_NAME)
					                .from(Tables.EH_ADDRESSES)
					                .where(Tables.EH_ADDRESSES.ID.eq(addressId))
					                .fetchOne(Tables.EH_ADDRESSES.APARTMENT_NAME);
		return apartmentName;
	}

	@Override
	public Byte findArrangementOperationTypeByAddressId(Long addressId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Byte operationType = context.select(Tables.EH_ADDRESS_ARRANGEMENT.OPERATION_TYPE)
					                .from(Tables.EH_ADDRESS_ARRANGEMENT)
					                .where(Tables.EH_ADDRESS_ARRANGEMENT.ADDRESS_ID.eq(addressId))
					                .and(Tables.EH_ADDRESS_ARRANGEMENT.STATUS.eq(AddressArrangementStatus.ACTIVE.getCode()))
					                .fetchOne(Tables.EH_ADDRESS_ARRANGEMENT.OPERATION_TYPE);
		return operationType;
	}

	@Override
	public List<AddressArrangement> listActiveAddressArrangementToday(Timestamp today) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		List<AddressArrangement> result = new ArrayList<>();
		context.select()
				.from(Tables.EH_ADDRESS_ARRANGEMENT)
				.where(Tables.EH_ADDRESS_ARRANGEMENT.STATUS.equal(AddressArrangementStatus.ACTIVE.getCode()))
				.and(Tables.EH_ADDRESS_ARRANGEMENT.OPERATION_FLAG.eq((byte) 0))
				.and(Tables.EH_ADDRESS_ARRANGEMENT.DATE_BEGIN.eq(today))
				.fetch()
				.map(r->{
					AddressArrangement convert = ConvertHelper.convert(r, AddressArrangement.class);
					result.add(convert);
					return null;
				});
		return result;
	}

	@Override
	public void updateAddressArrangement(AddressArrangement arrangement) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(AddressArrangement.class));
        EhAddressArrangementDao dao = new EhAddressArrangementDao(context.configuration());
        dao.update(arrangement);
	}

	@Override
	public List<AddressArrangement> findActiveAddressArrangementByOriginalIdV2(Long addressId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return context.select()
					.from(Tables.EH_ADDRESS_ARRANGEMENT)
					.where(Tables.EH_ADDRESS_ARRANGEMENT.ORIGINAL_ID.like(DSL.concat("%", addressId.toString(), "%")))
					.and(Tables.EH_ADDRESS_ARRANGEMENT.STATUS.eq(AddressArrangementStatus.ACTIVE.getCode()))
					.fetchInto(AddressArrangement.class);
	}

	@Override
	public List<AddressArrangement> findActiveAddressArrangementByTargetIdV2(Long addressId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return context.select()
					.from(Tables.EH_ADDRESS_ARRANGEMENT)
					.where(Tables.EH_ADDRESS_ARRANGEMENT.TARGET_ID.like(DSL.concat("%", addressId.toString(), "%")))
					.and(Tables.EH_ADDRESS_ARRANGEMENT.STATUS.eq(AddressArrangementStatus.ACTIVE.getCode()))
					.fetchInto(AddressArrangement.class);
	}

	@Override
	public Integer countApartmentNumberByBuildingName(Long communityId, String buildingName) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return  context.selectCount()
				.from(Tables.EH_ADDRESSES)
				.where(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId))
				.and(Tables.EH_ADDRESSES.BUILDING_NAME.eq(buildingName))
				.and(Tables.EH_ADDRESSES.STATUS.eq((BuildingAdminStatus.ACTIVE.getCode())))
				.and(Tables.EH_ADDRESSES.IS_FUTURE_APARTMENT.eq((byte)0))
				.fetchOneInto(Integer.class);
	}

	@Override
	public Integer countRelatedEnterpriseCustomerNumber(Long communityId,Long buildingId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		List<Long> addressIds = context.select(Tables.EH_ADDRESSES.ID)
										.from(Tables.EH_ADDRESSES)
										.where(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId))
										.and(Tables.EH_ADDRESSES.BUILDING_ID.eq(buildingId))
										.and(Tables.EH_ADDRESSES.STATUS.eq(BuildingAdminStatus.ACTIVE.getCode()))
										.fetchInto(Long.class);

		List<Long> customerIds = context.selectDistinct(Tables.EH_CUSTOMER_ENTRY_INFOS.CUSTOMER_ID)
										.from(Tables.EH_CUSTOMER_ENTRY_INFOS)
										.where(Tables.EH_CUSTOMER_ENTRY_INFOS.ADDRESS_ID.in(addressIds))
										.and(Tables.EH_CUSTOMER_ENTRY_INFOS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
										.fetchInto(Long.class);

		List<EnterpriseCustomer> enterpriseCustomers = context.select()
												  .from(Tables.EH_ENTERPRISE_CUSTOMERS)
												  .where(Tables.EH_ENTERPRISE_CUSTOMERS.ID.in(customerIds))
							                      .and(Tables.EH_ENTERPRISE_CUSTOMERS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
											      .fetchInto(EnterpriseCustomer.class);

		if (enterpriseCustomers != null && enterpriseCustomers.size() > 0) {
			return enterpriseCustomers.size();
		}
		return 0;
	}

	@Override
	public Integer countRelatedOrganizationOwnerNumber(Long communityId, Long buildingId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		List<Long> addressIds = context.select(Tables.EH_ADDRESSES.ID)
										.from(Tables.EH_ADDRESSES)
										.where(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId))
										.and(Tables.EH_ADDRESSES.BUILDING_ID.eq(buildingId))
										.and(Tables.EH_ADDRESSES.STATUS.eq(BuildingAdminStatus.ACTIVE.getCode()))
										.fetchInto(Long.class);

		List<Long> organizationOwnerIds = context.selectDistinct(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ORGANIZATION_OWNER_ID)
												.from(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
												.where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ADDRESS_ID.in(addressIds))
												.fetchInto(Long.class);

		List<CommunityPmOwner> owners = context.select()
												.from(Tables.EH_ORGANIZATION_OWNERS)
												.where(Tables.EH_ORGANIZATION_OWNERS.ID.in(organizationOwnerIds))
												.and(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq((byte)1))
												.fetchInto(CommunityPmOwner.class);

		if (owners != null && owners.size() > 0) {
			return owners.size();
		}
		return 0;
	}

	@Override
	public List<Address> findActiveAddressByCommunityId(Long communityId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return context.select()
						.from(Tables.EH_ADDRESSES)
						.where(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId))
						.and(Tables.EH_ADDRESSES.STATUS.eq(AddressAdminStatus.ACTIVE.getCode()))
						.and(Tables.EH_ADDRESSES.IS_FUTURE_APARTMENT.eq((byte)0))
						.fetchInto(Address.class);
	}

	@Override
	public List<Address> findActiveAddressByBuildingNameAndCommunityId(String buildingName,Long communityId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return context.select()
						.from(Tables.EH_ADDRESSES)
						.where(Tables.EH_ADDRESSES.BUILDING_NAME.eq(buildingName))
						.and(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId))
						.and(Tables.EH_ADDRESSES.STATUS.eq(AddressAdminStatus.ACTIVE.getCode()))
						.and(Tables.EH_ADDRESSES.IS_FUTURE_APARTMENT.eq((byte)0))
						.fetchInto(Address.class);
	}

	@Override
	public List<Address> listApartmentsInCommunity(ListApartmentsInCommunityCommand cmd) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<Record> query = context.selectQuery();
		query.addFrom(Tables.EH_ADDRESSES);
		query.addConditions(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(cmd.getNamespaceId()));
		query.addConditions(Tables.EH_ADDRESSES.STATUS.eq(AddressAdminStatus.ACTIVE.getCode()));
		//不能是未来资产
		query.addConditions(Tables.EH_ADDRESSES.IS_FUTURE_APARTMENT.eq((byte)0));
		query.addOrderBy(Tables.EH_ADDRESSES.ID.asc());

		if (cmd.getCommunityId() != null) {
			query.addConditions(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(cmd.getCommunityId()));
		}
		if (cmd.getCommunityIds() != null && cmd.getCommunityIds().size() > 0) {
			query.addConditions(Tables.EH_ADDRESSES.COMMUNITY_ID.in(cmd.getCommunityIds()));
		}
		if (!org.springframework.util.StringUtils.isEmpty(cmd.getBuildingName())) {
			query.addConditions(Tables.EH_ADDRESSES.BUILDING_NAME.eq(cmd.getBuildingName()));
		}
		if (!org.springframework.util.StringUtils.isEmpty(cmd.getFloorNumber())) {
			query.addConditions(Tables.EH_ADDRESSES.APARTMENT_FLOOR.eq(cmd.getFloorNumber()));
		}
		if (!org.springframework.util.StringUtils.isEmpty(cmd.getKeyword())) {
			query.addConditions(Tables.EH_ADDRESSES.BUILDING_NAME.like("%" + cmd.getKeyword() + "%")
								.or(Tables.EH_ADDRESSES.APARTMENT_NAME.like("%" + cmd.getKeyword() + "%")));
		}
		if (cmd.getAreaSizeFrom() != null) {
			query.addConditions(Tables.EH_ADDRESSES.AREA_SIZE.ge(cmd.getAreaSizeFrom()));
		}
		if (cmd.getAreaSizeTo() != null) {
			query.addConditions(Tables.EH_ADDRESSES.AREA_SIZE.le(cmd.getAreaSizeTo()));
		}
		if (cmd.getRentAreaFrom() != null) {
			query.addConditions(Tables.EH_ADDRESSES.RENT_AREA.ge(cmd.getRentAreaFrom()));
		}
		if (cmd.getRentAreaTo() != null) {
			query.addConditions(Tables.EH_ADDRESSES.RENT_AREA.le(cmd.getRentAreaTo()));
		}
		if (cmd.getChargeAreaFrom() != null) {
			query.addConditions(Tables.EH_ADDRESSES.CHARGE_AREA.ge(cmd.getChargeAreaFrom()));
		}
		if (cmd.getChargeAreaTo() != null) {
			query.addConditions(Tables.EH_ADDRESSES.CHARGE_AREA.le(cmd.getChargeAreaTo()));
		}
		if (cmd.getFreeAreaFrom() != null) {
			query.addConditions(Tables.EH_ADDRESSES.FREE_AREA.ge(cmd.getFreeAreaFrom()));
		}
		if (cmd.getFreeAreaTo() != null) {
			query.addConditions(Tables.EH_ADDRESSES.FREE_AREA.le(cmd.getFreeAreaTo()));
		}
		return query.fetchInto(Address.class);
	}

	@Override
	public Byte getAddressLivingStatusByAddressId(Long addressId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Byte aByte = context.select(Tables.EH_ADDRESSES.LIVING_STATUS)
                .from(Tables.EH_ADDRESSES)
                .where(Tables.EH_ADDRESSES.ID.eq(addressId))
                .fetchOne(Tables.EH_ADDRESSES.LIVING_STATUS);
        if(aByte == null){
            aByte = context.select(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.LIVING_STATUS)
                    .from(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS)
                    .where(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ADDRESS_ID.eq(addressId))
                    .fetchOne(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.LIVING_STATUS);
        }
        if(aByte == null){
            aByte = 1;
        }
        return aByte;
	}

	@Override
	public List<Address> findActiveApartmentsByBuildingId(Long buildingId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return  context.select()
				       .from(Tables.EH_ADDRESSES)
				       .where(Tables.EH_ADDRESSES.BUILDING_ID.eq(buildingId))
				       .and(Tables.EH_ADDRESSES.STATUS.eq(AddressAdminStatus.ACTIVE.getCode()))
				       .and(Tables.EH_ADDRESSES.IS_FUTURE_APARTMENT.eq((byte)0))
				       .fetchInto(Address.class);
	}

	//SELECT COUNT(*) FROM eh_addresses WHERE namespace_id!=0 AND `status`=2;
	@Override
	public int getTotalApartmentCount() {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return  context.selectCount()
				       .from(Tables.EH_ADDRESSES)
				       .where(Tables.EH_ADDRESSES.NAMESPACE_ID.ne(0))
				       .and(Tables.EH_ADDRESSES.STATUS.eq(AddressAdminStatus.ACTIVE.getCode()))
				       .and(Tables.EH_ADDRESSES.IS_FUTURE_APARTMENT.eq((byte)0))
				       .fetchAnyInto(Integer.class);
	}

//SELECT
//	a.id,
//	a.community_id,
//	a.community_name,
//	a.building_id,
//	a.building_name,
//	a.status,
//	b.living_status
//FROM
//	eh_addresses a
//LEFT JOIN eh_organization_address_mappings b ON a.id = b.address_id
//WHERE
//	a.namespace_id != 0
//AND
//	a.`status`=2
//ORDER BY
//	a.community_id,a.building_id
//LIMIT 5000;
	@Override
	public List<ApartmentReportFormDTO> findActiveApartments(int startIndex, int pageSize) {
		List<ApartmentReportFormDTO> result = new ArrayList<>();
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		com.everhomes.server.schema.tables.EhAddresses a = Tables.EH_ADDRESSES.as("a");
		EhOrganizationAddressMappings b = Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.as("b");
		
		context.select(a.ID,a.COMMUNITY_ID,a.COMMUNITY_NAME,a.BUILDING_ID,a.BUILDING_NAME,b.LIVING_STATUS)
			   .from(a)
			   .leftOuterJoin(b)
			   .on(a.ID.eq(b.ADDRESS_ID))
			   .where(a.NAMESPACE_ID.ne(0))
			   .and(a.STATUS.eq(AddressAdminStatus.ACTIVE.getCode()))
			   .orderBy(a.COMMUNITY_ID,a.BUILDING_ID)
			   .limit(startIndex, pageSize)
			   .fetch()
			   .forEach(r->{
				   ApartmentReportFormDTO dto = new ApartmentReportFormDTO();
				   dto.setId(r.getValue(a.ID));
				   dto.setCommunityId(r.getValue(a.COMMUNITY_ID));
				   dto.setCommunityName(r.getValue(a.COMMUNITY_NAME));
				   dto.setBuildingId(r.getValue(a.BUILDING_ID));
				   dto.setBuildingName(r.getValue(a.BUILDING_NAME));
				   dto.setLivingStatus(r.getValue(b.LIVING_STATUS));
				   result.add(dto);
			   });
		return result;
	}
}
