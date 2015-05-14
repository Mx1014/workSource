// @formatter:off
package com.everhomes.address;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityGeoPoint;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.core.AppConfig;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.family.Family;
import com.everhomes.family.FamilyProvider;
import com.everhomes.group.Group;
import com.everhomes.group.GroupDiscriminator;
import com.everhomes.group.GroupProvider;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserGroup;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;

@Component
public class AddressServiceImpl implements AddressService, LocalBusSubscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressServiceImpl.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private LocalBus localBus;
    
    @Autowired
    private AddressProvider addressProvider;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    private RegionProvider regionProvider;
    
    @Autowired
    private FamilyProvider familyProvider;
    
    @Autowired
    private CoordinationProvider coordinationProvider;
   
    @Autowired
    private GroupProvider groupProvider;
    
    @Autowired
    private UserProvider userProvide;
    
    @PostConstruct
    public void setup() {
        localBus.subscribe(DaoHelper.getDaoActionPublishSubject(DaoAction.CREATE, EhCommunities.class, null), this);
        localBus.subscribe(DaoHelper.getDaoActionPublishSubject(DaoAction.MODIFY, EhCommunities.class, null), this);
    }
    
    public Action onLocalBusMessage(Object sender, String subject, Object args, String subscriptionPath) {
        // TODO monitor change notifications for cache invalidation
        return Action.none;
    }
    
    @Override
    public CommunitySummaryDTO suggestCommunity(SuggestCommunityCommand cmd) {
        if(cmd.getCityId() == null || cmd.getAreaId() == null || cmd.getName() == null || cmd.getName().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid parameter, cityId, areaId and name should not be null or empty");
        
        Region city = this.regionProvider.findRegionById(cmd.getCityId());
        if(city == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid cityId parameter, could not find the city");
        
        Region area = this.regionProvider.findRegionById(cmd.getAreaId());
        if(area == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid areaId parameter, could not find the ");
        
        Community community = new Community();
        community.setCityId(cmd.getCityId());
        community.setCityName(city.getName());
        community.setAreaId(cmd.getAreaId());
        community.setAreaName(area.getName());
        community.setName(cmd.getName());
        community.setAddress(cmd.getAddress());
        
        UserContext user = UserContext.current();
        community.setCreatorUid(user.getUser().getId());
        community.setStatus(CommunityAdminStatus.CONFIRMING.getCode());

        this.dbProvider.execute((TransactionStatus status) ->  {
            this.communityProvider.createCommunity(community);
            if(cmd.getLatitude() != null && cmd.getLongitude() != null) {
                CommunityGeoPoint point = new CommunityGeoPoint();
                point.setCommunityId(community.getId());
                point.setDescription("central");
                point.setLatitude(cmd.getLatitude());
                point.setLongitude(cmd.getLongitude());
                String geoHash = GeoHashUtils.encode(cmd.getLatitude(), cmd.getLongitude());
                point.setGeohash(geoHash);
                this.communityProvider.createCommunityGeoPoint(point);
            }
            return null;
        });
        
        return ConvertHelper.convert(community, CommunitySummaryDTO.class);
    }
    
    @Override
    public Tuple<Integer, List<CommunitySummaryDTO>> listSuggestedCommunities() {
        final List<CommunitySummaryDTO> results = new ArrayList<>();
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    
                Long uid = UserContext.current().getUser().getId();
                context.select().from(Tables.EH_COMMUNITIES)
                    .where(Tables.EH_COMMUNITIES.CREATOR_UID.eq(uid))
                    .fetch().map((r) -> {
                        results.add(ConvertHelper.convert(r, CommunitySummaryDTO.class));
                        return null;
                    });
                
                return true;
            });

        
        return new Tuple<Integer, List<CommunitySummaryDTO>>(ErrorCodes.SUCCESS, results);
    }
    
    @Override
    public Tuple<Integer, List<CommunityDTO>> listNearbyCommunities(ListNearbyCommunityCommand cmd) {
        final List<CommunityDTO> results = new ArrayList<>();

        if(cmd.getCityId() == null && cmd.getLatigtue() == null && cmd.getLongitude() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid parameter, need at least one valid value in one of these parameters: latitude, longitude, cityId");
        
        if(cmd.getLatigtue() == null || cmd.getLongitude() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid parameter, latitude and longitude have to be both specified or neigher");

        // TODO, return all communities only to test our REST response for now
        
        String geoHash = GeoHashUtils.encode(cmd.getLatigtue(), cmd.getLongitude());
        List<CommunityGeoPoint> pointList = this.communityProvider.findCommunityGeoPointByGeoHash(geoHash.substring(0, 5));
        List<Long> communityIds = getAllCommunityIds(pointList);
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null, 
            (DSLContext context, Object reducingContext)-> {
            
            context.select().from(Tables.EH_COMMUNITIES)
                .where(Tables.EH_COMMUNITIES.ID.in(communityIds))
                .fetch().map((r) -> {
                CommunityDTO community = ConvertHelper.convert(r, CommunityDTO.class);
                results.add(community);
                
                return null;
            });
                
            return true;
        });
        
        
        return new Tuple<>(ErrorCodes.SUCCESS, results);
    }
    
    private List<Long> getAllCommunityIds(List<CommunityGeoPoint> pointList){
        if(pointList == null || pointList.isEmpty())
            return new ArrayList<Long>();
        List<Long> communityIds= new ArrayList<Long>(pointList.size());
        for(CommunityGeoPoint c : pointList){
            communityIds.add(c.getCommunityId());
        }
        return communityIds;
    }
    /**
     *  TODO: Pagination across partitions needs to be pre-fetched and cached
     */
    @Override
    public Tuple<Integer, List<CommunityDTO>> listCommunitiesByKeyword(ListCommunityByKeywordCommand cmd) {
        //
        // TODO, should be integrating with keyword search server
        //
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid keyword parameter");
            
        final List<CommunityDTO> results = new ArrayList<>();
        final long pageSize = this.configurationProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE);
        long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset(), pageSize);
        Tuple<Integer, Long> targetShard = new Tuple<>(0, offset);
        if(offset > 0) {
            final List<Long> countsInShards = new ArrayList<>();
            this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null, 
                    (DSLContext context, Object reducingContext)-> {
                        
                    Long count = context.selectCount().from(Tables.EH_COMMUNITIES)
                            .where(Tables.EH_COMMUNITIES.NAME.like(cmd.getKeyword() + "%"))
                            .or(Tables.EH_COMMUNITIES.ALIAS_NAME.like(cmd.getKeyword() + "%"))
                            .fetchOne(0, Long.class);
                    
                    countsInShards.add(count);
                    return true;
                });
            
            targetShard = PaginationHelper.offsetFallsAt(countsInShards, offset);
        }
        if(targetShard.first() < 0)
            return new Tuple<>(ErrorCodes.SUCCESS, results);

        final int[] currentShard = new int[1];
        currentShard[0] = 0;
        final Tuple<Integer, Long> fallingShard = targetShard;
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), currentShard, 
            (DSLContext context, Object reducingContext)-> {
                int[] current = (int[])reducingContext;
                if(current[0] < fallingShard.first()) {
                    current[0] += 1;
                    return true;
                }
                
                long off = 0;
                if(current[0] == fallingShard.first())
                    off = fallingShard.second();
                
                context.select().from(Tables.EH_COMMUNITIES)
                    .where(Tables.EH_COMMUNITIES.NAME.like(cmd.getKeyword() + "%"))
                    .or(Tables.EH_COMMUNITIES.ALIAS_NAME.like(cmd.getKeyword() + "%"))
                    .limit((int)pageSize).offset((int)off)
                    .fetch().map((r) -> {
                        CommunityDTO community = ConvertHelper.convert(r, CommunityDTO.class);
                        if(results.size() <= pageSize + 1)
                            results.add(community);
                        return null;
                });
                
            return true;
        });
        
        if(results.size() > pageSize) {
            results.remove(results.size() - 1);
            return new Tuple<>(ErrorCodes.SUCCESS_MORE_DATA, results);
        }
        
        return new Tuple<>(ErrorCodes.SUCCESS, results);
    }

    @Override
    public Tuple<Integer, List<BuildingDTO>> listBuildingsByKeyword(ListBuildingByKeywordCommand cmd) {
        if(cmd.getCommunityId() == null || cmd.getKeyword() == null || cmd.getKeyword().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid communityId or keyword parameter");
        
        List<BuildingDTO> results = new ArrayList<BuildingDTO>();
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    
                    String likeVal = cmd.getKeyword() + "%";
                    context.selectDistinct(Tables.EH_ADDRESSES.BUILDING_NAME, Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME)
                        .from(Tables.EH_ADDRESSES)
                        .where(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(cmd.getCommunityId())
                        .and(Tables.EH_ADDRESSES.BUILDING_NAME.like(likeVal)
                                .or(Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME.like(likeVal))))        
                        .fetch().map((r) -> {
                            BuildingDTO building = new BuildingDTO();
                            building.setBuildingName(r.getValue(Tables.EH_ADDRESSES.BUILDING_NAME));
                            building.setBuildingAliasName(r.getValue(Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME));
                            results.add(building);
                            return null;
                        });
                    
                return true;
            });
        
        return new Tuple<Integer, List<BuildingDTO>>(ErrorCodes.SUCCESS, results);
    }

    @Override
    public Tuple<Integer, List<ApartmentDTO>> listApartmentsByKeyword(ListApartmentByKeywordCommand cmd) {
        if(cmd.getCommunityId() == null || cmd.getKeyword() == null ||
             cmd.getBuildingName() == null || cmd.getBuildingName().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid communityId, buildingName or keyword parameter");
        
        List<ApartmentDTO> results = new ArrayList<>();
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    
                    context.selectDistinct(Tables.EH_ADDRESSES.APARTMENT_NAME)
                        .from(Tables.EH_ADDRESSES)
                        .where(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(cmd.getCommunityId())
                        .and(Tables.EH_ADDRESSES.BUILDING_NAME.equal(cmd.getBuildingName())
                                .or(Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME.equal(cmd.getBuildingName()))))
                        .and(Tables.EH_ADDRESSES.APARTMENT_NAME.like(cmd.getKeyword() + "%"))
                        .fetch().map((r) -> {
                            ApartmentDTO apartment = new ApartmentDTO();
                            apartment.setApartmentName(r.getValue(Tables.EH_ADDRESSES.APARTMENT_NAME));
                            results.add(apartment);
                            return null;
                        });
                    
                return true;
            });
        
        return new Tuple<Integer, List<ApartmentDTO>>(ErrorCodes.SUCCESS, results);
    }
    
    @Override
    public ClaimedAddressInfo claimAddress(ClaimAddressCommand cmd) {
        if(cmd.getCommunityId() == null || cmd.getBuildingName() == null || cmd.getBuildingName().isEmpty()
            || cmd.getApartmentName() == null || cmd.getApartmentName().isEmpty()) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid communityId, buildingName or appartmentName parameter");
        }
        
        if(cmd.getReplacedAddressId() == null) {
            return processNewAddressClaim(cmd);
        } else {
            ClaimedAddressInfo info = processNewAddressClaim(cmd);
            
            DisclaimAddressCommand disclaimCmd = new DisclaimAddressCommand();
            disclaimCmd.setAddressId(cmd.getReplacedAddressId());
            disclaimAddress(disclaimCmd);
            return info;
        }
    }
    
    public void disclaimAddress(DisclaimAddressCommand cmd) {
        Address address = this.addressProvider.findAddressById(cmd.getAddressId());
        if(address == null)
            return;

        long uid = UserContext.current().getUser().getId();
        List<UserGroup> userGroups = this.userProvide.listUserGroups(uid, GroupDiscriminator.FAMILY.getCode());
        userGroups = userGroups.stream().filter((userGroup)-> {
            Group group = this.groupProvider.findGroupById(userGroup.getGroupId());
            if(group != null){
                return group.getIntegralTag1().longValue() == cmd.getAddressId().longValue();
            }
            return false;
        }).collect(Collectors.toList());
        
        if(userGroups.size() > 0) {
            this.familyProvider.leaveFamilyAtAddress(address, userGroups.get(0));
        }
    }
   
    private ClaimedAddressInfo processNewAddressClaim(ClaimAddressCommand cmd) {
        Address address = this.getOrCreateAddress(cmd);
        Family family = this.familyProvider.getOrCreatefamily(address);
        
        ClaimedAddressInfo info = new ClaimedAddressInfo();
        info.setAddressId(address.getId());
        info.setFullAddress(address.getAddress());
        info.setUserCount((int)family.getMemberCount().longValue());
        
        return info;
    }
    
    private Address getOrCreateAddress(ClaimAddressCommand cmd) {
        Address address = this.addressProvider.findApartmentAddress(cmd.getCommunityId(), 
                cmd.getBuildingName(), cmd.getApartmentName());
        
        Community community = this.communityProvider.findCommunityById(cmd.getCommunityId());
        if(community == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid communityId");
        
        if(address == null) {
            // optimize with double-lock pattern 
            Tuple<Address, Boolean> result = this.coordinationProvider
                     .getNamedLock(CoordinationLocks.CREATE_ADDRESS.getCode()).enter(()-> {
                         
                Address addr = this.addressProvider.findApartmentAddress(cmd.getCommunityId(), 
                                 cmd.getBuildingName(), cmd.getApartmentName());
                if(addr == null) {
                     addr = new Address();
                     addr.setCityId(community.getCityId());
                     addr.setCommunityId(cmd.getCommunityId());
                     addr.setBuildingName(cmd.getBuildingName());
                     addr.setApartmentName(cmd.getApartmentName());
                     addr.setAddress(joinAddrStr(cmd.getBuildingName(),cmd.getApartmentName()));
                     addr.setStatus(AddressAdminStatus.CONFIRMING.getCode());
                     this.addressProvider.createAddress(addr);
                }
                return addr;
             });
             
             address = result.first();
         }
         
         return address;
    }

    @Override
    public Tuple<Integer, List<Address>> listAddressByCommunityId(ListAddressCommand cmd) {
        if(cmd == null || cmd.getCommunityId() == 0)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid communityId parameter");
            
        final List<Address> results = new ArrayList<>();
        final long pageSize = cmd.getPageSize() == null ? this.configurationProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();
        long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset(), pageSize);
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
            (DSLContext context, Object reducingContext)-> {
                
                context.select().from(Tables.EH_ADDRESSES)
                    .where(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(cmd.getCommunityId()))
                    .limit((int)pageSize).offset((int)offset)
                    .fetch().map((r) -> {
                        results.add(ConvertHelper.convert(r, Address.class)); 
                        return null;
                });
                
            return true;
        });
        
        return new Tuple<>(ErrorCodes.SUCCESS, results);
    }

    private String joinAddrStr(String buildingName, String apartName){
        boolean isFirst = true;
        StringBuilder strBuilder = new StringBuilder();
        if (!StringUtils.isEmpty(buildingName)){
             if (!isFirst){
                 strBuilder.append("-");
            }
            strBuilder.append(buildingName);
            isFirst = false;
        }

        if (!StringUtils.isEmpty(apartName)){
            if (!isFirst){
                strBuilder.append("-");
           }
           strBuilder.append(buildingName);
           isFirst = false;
        }
        return strBuilder.toString();
    }

}
