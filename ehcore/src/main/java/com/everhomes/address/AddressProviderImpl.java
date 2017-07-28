// @formatter:off
package com.everhomes.address;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.address.AddressAdminStatus;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.address.ApartmentDTO;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.organization.OrganizationAddressStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAddressesDao;
import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.server.schema.tables.records.EhAddressesRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class AddressProviderImpl implements AddressProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressProviderImpl.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private SequenceProvider sequnceProvider;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Override
    public void createAddress(Address address) {
        long id = shardingProvider.allocShardableContentId(EhAddresses.class).second(); 

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
        long id = shardingProvider.allocShardableContentId(EhAddresses.class).second(); 

        address.setId(id); 
        address.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime())); 
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddresses.class, id)); 

        EhAddressesDao dao = new EhAddressesDao(context.configuration()); 
        dao.insert(address); 

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhAddresses.class, null); 
        long endTime = System.currentTimeMillis();
		LOGGER.info("successed insert one record.time=" + (endTime - startTime));
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

    @Cacheable(value="Address", key="#id",unless="#result==null")
    @Override
    public Address findAddressById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddresses.class, id));
        EhAddressesDao dao = new EhAddressesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), Address.class);
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
	    
		if (record != null) {
			return ConvertHelper.convert(record, Address.class);
		}
		return null;
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
    
}
