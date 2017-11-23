package com.everhomes.user.admin;


import com.everhomes.address.Address;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.namespace.Namespace;
import com.everhomes.region.Region;
import com.everhomes.rest.address.CommunitySummaryDTO;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAddressesDao;
import com.everhomes.server.schema.tables.daos.EhUserIdentifiersDao;
import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhUserIdentifiers;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.server.schema.tables.records.EhUserIdentifiersRecord;
import com.everhomes.server.schema.tables.records.EhUsersRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.UserAdminProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;
import org.jooq.*;
import org.jooq.impl.DefaultRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserAdminProviderImpl implements UserAdminProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private ContentServerService contentServerService;
    
    @Autowired
    private ShardingProvider shardingProvider;

    @Override
    public List<UserIdentifier> listUserIdentifiers(List<Long> uids) {
        List<UserIdentifier> identifiers = new ArrayList<UserIdentifier>(uids.size());
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUserIdentifiers.class),null,
                (context, obj) -> {
                    if (identifiers.size() >= uids.size())
                        return false;
                    EhUserIdentifiersDao dao = new EhUserIdentifiersDao(context.configuration());
                    identifiers.addAll(dao.fetchByOwnerUid(uids.toArray(new Long[uids.size()])).stream()
                            .map(r -> ConvertHelper.convert(r, UserIdentifier.class)).collect(Collectors.toList()));
                    return true;
                });
        return identifiers;
    }

    @Override
    public List<UserInfo> listRegisterByOrder(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback callback) {
        List<UserInfo> userInfos = new ArrayList<UserInfo>();
        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhUsers.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhUsersRecord> query = context.selectQuery(Tables.EH_USERS);
            if (callback != null)
                callback.buildCondition(locator, query);

            if (locator.getAnchor() != null)
                query.addConditions(Tables.EH_USERS.ID.lt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_USERS.CREATE_TIME.desc());
            query.addLimit(count - userInfos.size());

            query.fetch().map((r) -> {
                userInfos.add(ConvertHelper.convert(r, UserInfo.class));
                return null;
            });

            if (userInfos.size() >= count) {
                locator.setAnchor(userInfos.get(userInfos.size() - 1).getId());
                return AfterAction.done;
            }
            return AfterAction.next;
        });

        if (userInfos.size() > 0) {
            locator.setAnchor(userInfos.get(userInfos.size() - 1).getId());
        }

        return userInfos;
    }

    @Override
    public List<UserIdentifier> listUserIdentifiersByOrder(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback callback, Condition... conditons) {
        List<UserIdentifier> userIdentifiers = new ArrayList<UserIdentifier>();
        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhUserIdentifiers.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhUserIdentifiersRecord> query = context.selectQuery(Tables.EH_USER_IDENTIFIERS);
            if (callback != null)
                callback.buildCondition(locator, query);

            if (locator.getAnchor() != null)
                query.addConditions(Tables.EH_USER_IDENTIFIERS.ID.lt(locator.getAnchor()));
            if (conditons != null) {
                query.addConditions(conditons);
            }
            query.addOrderBy(Tables.EH_USER_IDENTIFIERS.NOTIFY_TIME.desc());
            query.addOrderBy(Tables.EH_USER_IDENTIFIERS.CREATE_TIME.desc());
            query.addLimit(count - userIdentifiers.size());

            query.fetch().map((r) -> {
                userIdentifiers.add(ConvertHelper.convert(r, UserIdentifier.class));
                return null;
            });

            if (userIdentifiers.size() >= count) {
                locator.setAnchor(userIdentifiers.get(userIdentifiers.size() - 1).getId());
                return AfterAction.done;
            }
            return AfterAction.next;
        });

        if (userIdentifiers.size() > 0) {
            locator.setAnchor(userIdentifiers.get(userIdentifiers.size() - 1).getId());
        }

        return userIdentifiers;
    }

    @Override
    public List<UserIdentifier> listVetsByOrder(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback callback) {
        // vet number like 1234578743,start with 12
        return listUserIdentifiersByOrder(locator, count, callback,
                Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.like("12%"));
    }

    @Override
    public List<UserIdentifier> listAllVerifyCode(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback callback) {
        return listUserIdentifiersByOrder(locator, count, callback,
                Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TYPE.eq(IdentifierType.MOBILE.getCode()));
    }

	@Override
	public List<UserIdentifier> listIdentifierTokenByType(String start, Byte type) {
		List<UserIdentifier> userIdentifiers = new ArrayList<UserIdentifier>();
		 dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (DSLContext context, Object reducingContext) -> {
	            context.select().from(Tables.EH_USER_IDENTIFIERS).where(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.like(start + "%"))
	            .and(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TYPE.eq(type))
	            .fetch().map((r) -> {
	                userIdentifiers.add(ConvertHelper.convert(r, UserIdentifier.class));
	                return null;
	            });;
	            return true;
	        });
		return userIdentifiers;
	}
    
    @Override
    public Long getRegionIdByCityName(String cityName) {
    	int namespaceId = (UserContext.current().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getNamespaceId();
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_REGIONS);
		selectStep.where(Tables.EH_REGIONS.NAME.like(cityName+"%"))
		.and(Tables.EH_REGIONS.NAMESPACE_ID.eq(namespaceId));
		List<Region> regions= selectStep.fetch().map(
				new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
				);
		if(null == regions || regions.isEmpty()){
			return null;
		}
    	return regions.get(0).getId();
    }
    
    
    
    @Override
    public Long getRegionIdByAreaName(Long cityId, String areaName) {
    	int namespaceId = (UserContext.current().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getNamespaceId();
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_REGIONS);
		selectStep.where(Tables.EH_REGIONS.PARENT_ID.eq(cityId))
		.and(Tables.EH_REGIONS.NAME.like(areaName+"%"))
		.and(Tables.EH_REGIONS.NAMESPACE_ID.eq(namespaceId));
		List<Region> regions= selectStep.fetch().map(
				new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
				);
		if(null == regions || regions.isEmpty()){
			return null;
		}
    	return regions.get(0).getId();
    }
    
    @Override
    public Long getCommunitiesIdByName(Long cityId, Long areaId, String name) {
    	int namespaceId = (UserContext.current().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getNamespaceId();
    	List<CommunitySummaryDTO> results = new ArrayList<>();
    	this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null, 
                (DSLContext context, Object reducingContext)-> {
                context.select().from(Tables.EH_COMMUNITIES)
                        .where(Tables.EH_COMMUNITIES.CITY_ID.eq(cityId))
                        .and(Tables.EH_COMMUNITIES.AREA_ID.eq(areaId))
                        .and(Tables.EH_COMMUNITIES.NAME.like(name+"%"))
                        .and(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId))
                        .fetch().map((r) -> {
                        results.add(ConvertHelper.convert(r, CommunitySummaryDTO.class));
                        return null;
                    });
                return true;
            });
    	if(null == results || results.isEmpty()){
			return null;
		}
    	return results.get(0).getId();
    }
    
    @Override
    public List<Address> getAddressByApartments(String buildingName,
    		String apartment_name) {
    	int namespaceId = (UserContext.current().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getNamespaceId();
    	List<Address> addresses = new ArrayList<Address>();
    	 this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null, 
                 (DSLContext context, Object reducingContext) -> {
               context.select().from(Tables.EH_ADDRESSES)
                  .where(Tables.EH_ADDRESSES.BUILDING_NAME.eq(buildingName))
                  .and(Tables.EH_ADDRESSES.APARTMENT_NAME.eq(apartment_name))
                  .and(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
                  .fetch().map((r) -> {
                	  addresses.add(ConvertHelper.convert(r, Address.class)); 
                	  return null;
                  });
              return true;
          });
    	return addresses;
    }
    
    
    @Override
    public Address createAddress(Address address) {
        long id = shardingProvider.allocShardableContentId(EhAddresses.class).second(); 

        address.setId(id); 
        address.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime())); 
        address.setUuid(UUID.randomUUID().toString());
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddresses.class, id)); 

        EhAddressesDao dao = new EhAddressesDao(context.configuration()); 
        dao.insert(address); 

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhAddresses.class, null); 
        return address;
    }
}
