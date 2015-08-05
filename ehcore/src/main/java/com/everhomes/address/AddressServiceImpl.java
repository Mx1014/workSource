// @formatter:off
package com.everhomes.address;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.core.joran.conditional.ElseAction;

import com.everhomes.address.admin.CorrectAddressAdminCommand;
import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityDataInfo;
import com.everhomes.community.CommunityDoc;
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
import com.everhomes.family.FamilyDTO;
import com.everhomes.family.FamilyProvider;
import com.everhomes.family.FamilyService;
import com.everhomes.family.LeaveFamilyCommand;
import com.everhomes.group.Group;
import com.everhomes.group.GroupProvider;
import com.everhomes.organization.pm.CommunityPmContact;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.region.Region;
import com.everhomes.region.RegionAdminStatus;
import com.everhomes.region.RegionProvider;
import com.everhomes.region.RegionScope;
import com.everhomes.region.RegionServiceErrorCode;
import com.everhomes.search.CommunitySearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserServiceAddress;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.FileHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import com.everhomes.util.file.DataFileHandler;
import com.everhomes.util.file.DataProcessConstants;

@Component
public class AddressServiceImpl implements AddressService, LocalBusSubscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressServiceImpl.class);
    
    private static final String ADMIN_IMPORT_DATA_SEPERATOR = "admin.import.data.seperator";
    private static final String ADMIN_IMPORT_ADDRESS_ALLOW_MAX_COUNT = "admin.import.address.allow.max.count";
    
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
    
    @Autowired
    private CommunitySearcher communitySearcher;
    
    @Autowired
    private FamilyService familyService;
    
    @Autowired
    private PropertyMgrProvider propertyMgrProvider;
    
    @Autowired
    private UserActivityProvider userActivityProvider;
    
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
        
        User user = UserContext.current().getUser();
        community.setCreatorUid(user.getId());
        community.setStatus(CommunityAdminStatus.CONFIRMING.getCode());

        this.dbProvider.execute((TransactionStatus status) ->  {
            this.communityProvider.createCommunity(user.getId(), community);
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
        
        List<CommunityGeoPoint> pointList = this.communityProvider.findCommunityGeoPointByGeoHash(cmd.getLatigtue(),cmd.getLongitude());
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
        if(cmd.getCommunityId() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid communityId or keyword parameter");
        
        if(cmd.getKeyword() == null)
            cmd.setKeyword("");;
        List<BuildingDTO> results = new ArrayList<BuildingDTO>();
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    
                    String likeVal = "%" + cmd.getKeyword() + "%";
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
    public Tuple<Integer, List<ApartmentDTO>> listApartmentsByKeyword(ListPropApartmentsByKeywordCommand cmd) {
        if(cmd.getCommunityId() == null || cmd.getBuildingName() == null || cmd.getBuildingName().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid communityId, buildingName or keyword parameter");
        if(cmd.getKeyword() == null)
            cmd.setKeyword("");
        List<ApartmentDTO> results = new ArrayList<>();
        String likeVal = "%" + cmd.getKeyword() + "%";
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    
                    context.selectDistinct(Tables.EH_ADDRESSES.ID,Tables.EH_ADDRESSES.APARTMENT_NAME)
                        .from(Tables.EH_ADDRESSES)
                        .where(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(cmd.getCommunityId())
                        .and(Tables.EH_ADDRESSES.BUILDING_NAME.equal(cmd.getBuildingName())
                                .or(Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME.equal(cmd.getBuildingName()))))
                        .and(Tables.EH_ADDRESSES.APARTMENT_NAME.like(likeVal))
                        .fetch().map((r) -> {
                            ApartmentDTO apartment = new ApartmentDTO();
                            apartment.setAddressId(r.getValue(Tables.EH_ADDRESSES.ID));
                            apartment.setApartmentName(r.getValue(Tables.EH_ADDRESSES.APARTMENT_NAME));
                            results.add(apartment);
                            return null;
                        });
                    
                return true;
            });
        
        return new Tuple<Integer, List<ApartmentDTO>>(ErrorCodes.SUCCESS, results);
    }
    
    @Override
    public ListAddressByKeywordCommandResponse listAddressByKeyword(ListAddressByKeywordCommand cmd) {
        if(cmd.getCommunityId() == null || cmd.getKeyword() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid communityId  or keyword parameter");

        List<AddressDTO> results = new ArrayList<>();
        final long pageSize = cmd.getPageSize() == null ? this.configurationProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();
        long pageOffset = cmd.getPageOffset() == null ? 1L : cmd.getPageOffset();
        long offset = PaginationHelper.offsetFromPageOffset(pageOffset, pageSize);
        Tuple<Integer, Long> targetShard = new Tuple<>(0, offset);
        String likeVal = "%" + cmd.getKeyword() + "%";
        if(offset > 0) {
            final List<Long> countsInShards = new ArrayList<>();
            this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null, 
                    (DSLContext context, Object reducingContext)-> {
                        
                    Long count = context.selectCount().from(Tables.EH_ADDRESSES)
                            .where(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(cmd.getCommunityId()))
                            .and(Tables.EH_ADDRESSES.APARTMENT_NAME.like(likeVal))
                            .and(Tables.EH_ADDRESSES.STATUS.equal(AddressAdminStatus.ACTIVE.getCode()))
                            .fetchOne(0, Long.class);
                    
                    countsInShards.add(count);
                    return true;
                });
            
            targetShard = PaginationHelper.offsetFallsAt(countsInShards, offset);
        }
        ListAddressByKeywordCommandResponse response = new ListAddressByKeywordCommandResponse();
        if(targetShard.first() < 0)
            return response;

        final int[] currentShard = new int[1];
        currentShard[0] = 0;
        final Tuple<Integer, Long> fallingShard = targetShard;
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), currentShard, 
                (DSLContext context, Object reducingContext)-> {
                    
                    int[] current = (int[])reducingContext;
                    if(current[0] < fallingShard.first()) {
                        current[0] += 1;
                        return true;
                    }
                    
                    long off = 0;
                    if(current[0] == fallingShard.first())
                        off = fallingShard.second();
                    
                    context.select().from(Tables.EH_ADDRESSES)
                    .where(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(cmd.getCommunityId()))
                    .and(Tables.EH_ADDRESSES.APARTMENT_NAME.like(likeVal))
                    .and(Tables.EH_ADDRESSES.STATUS.equal(AddressAdminStatus.ACTIVE.getCode()))
                    .limit((int)pageSize).offset((int)off)
                    .fetch().map((r) -> {
                        results.add(ConvertHelper.convert(r, AddressDTO.class));
                        return null;
                    });
                return true;
            });

        if(results.size() >= pageSize) {
            response.setNextPageOffset((int)pageOffset + 1);
        }
        response.setRequests(results);
        return response;
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
        if(cmd.getAddressId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid addressId parameter");
        }
        Address address = this.addressProvider.findAddressById(cmd.getAddressId());
        if(address == null){
            throw RuntimeErrorException.errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_ADDRESS_NOT_EXIST, 
                    "Invalid addressId parameter,address is not found");
        }

//        long uid = UserContext.current().getUser().getId();
//        List<UserGroup> userGroups = this.userProvide.listUserGroups(uid, GroupDiscriminator.FAMILY.getCode());
//        userGroups = userGroups.stream().filter((userGroup)-> {
//            Group group = this.groupProvider.findGroupById(userGroup.getGroupId());
//            if(group != null){
//                return group.getIntegralTag1().longValue() == cmd.getAddressId().longValue();
//            }
//            return false;
//        }).collect(Collectors.toList());
//        
//        if(userGroups.size() > 0) {
//            LeaveFamilyCommand leaveCmd = new LeaveFamilyCommand();
//            leaveCmd.setId(userGroups.get(0).getGroupId());
//            familyService.leave(leaveCmd);
//            //this.familyProvider.leaveFamilyAtAddress(address, userGroups.get(0));
//        }
        Family family = this.familyProvider.findFamilyByAddressId(cmd.getAddressId());
        if(family == null){
            throw RuntimeErrorException.errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_ADDRESS_NOT_EXIST, 
                    "Invalid addressId parameter,address is not found");
        }
        LeaveFamilyCommand leaveCmd = new LeaveFamilyCommand();
        leaveCmd.setId(family.getId());
        familyService.leave(leaveCmd);
    }
   
    private ClaimedAddressInfo processNewAddressClaim(ClaimAddressCommand cmd) {
        Address address = this.getOrCreateAddress(cmd);
        Family family = this.familyService.getOrCreatefamily(address);
        
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
        User user = UserContext.current().getUser();
        long userId = user.getId();
        if(address == null) {
            // optimize with double-lock pattern 
            Tuple<Address, Boolean> result = this.coordinationProvider
                     .getNamedLock(CoordinationLocks.CREATE_ADDRESS.getCode()).enter(()-> {
                long lqStartTime = System.currentTimeMillis();
                Address addr = this.addressProvider.findApartmentAddress(cmd.getCommunityId(), 
                                 cmd.getBuildingName(), cmd.getApartmentName());
                long lqEndTime = System.currentTimeMillis();
                LOGGER.info("find address ,in the lock,elapse=" + (lqEndTime - lqStartTime));
                long lcStartTime = System.currentTimeMillis();
                if(addr == null) {
                     addr = new Address();
                     addr.setCityId(community.getCityId());
                     addr.setCityName(community.getCityName());
                     addr.setAreaId(community.getAreaId());
                     addr.setAreaName(community.getAreaName());
                     addr.setCommunityId(cmd.getCommunityId());
                     addr.setBuildingName(cmd.getBuildingName());
                     addr.setApartmentName(cmd.getApartmentName());
                     addr.setAddress(joinAddrStr(cmd.getBuildingName(),cmd.getApartmentName()));
                     addr.setApartmentFloor(parserApartmentFloor(cmd.getApartmentName()));
                     addr.setStatus(AddressAdminStatus.CONFIRMING.getCode());
                     addr.setCreatorUid(userId);
                     this.addressProvider.createAddress(addr);
                }
                long lcEndTime = System.currentTimeMillis();
                LOGGER.info("create address in the lock,elapse=" + (lcEndTime - lcStartTime));
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
           strBuilder.append(apartName);
           isFirst = false;
        }
        return strBuilder.toString();
    }

    @Override
    public void correctAddress(CorrectAddressAdminCommand cmd) {
        
        if(cmd.getCommunityId() == null || cmd.getAddressId() == null 
                || cmd.getBuildingName() == null || cmd.getApartmentName() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid communityId or addressId or buildingName or apratment parameter");
        Address address = this.addressProvider.findAddressById(cmd.getAddressId());
        if(address == null){
            LOGGER.error("Invalid addressId parameter,address is not found.addressId=" + cmd.getAddressId());
            throw RuntimeErrorException.errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_ADDRESS_NOT_EXIST, 
                    "Address is not found.");
        }
        User user = UserContext.current().getUser();
        long userId = user.getId();
        long startTime = System.currentTimeMillis();
        Address addr = this.addressProvider.findApartmentAddress(cmd.getCommunityId(), cmd.getBuildingName(), cmd.getApartmentName());
        this.dbProvider.execute((TransactionStatus status) -> {
            if(addr == null || addr.getId().longValue() == address.getId().longValue()){
                address.setBuildingName(cmd.getBuildingName());
                address.setApartmentName(cmd.getApartmentName());
                address.setAddress(joinAddrStr(cmd.getBuildingName(),cmd.getApartmentName()));
                address.setApartmentFloor(parserApartmentFloor(cmd.getApartmentName()));
                address.setStatus(AddressAdminStatus.ACTIVE.getCode());
                address.setOperatorUid(userId);
                address.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                this.addressProvider.updateAddress(address);
                //approve members of this address
//                Family family = this.familyProvider.findFamilyByAddressId(cmd.getAddressId());
//                if(family != null)
//                    this.familyService.approveMembersByFamily(family);
                Community community = this.communityProvider.findCommunityById(cmd.getCommunityId());
                if(community != null){
                    community.setAptCount(community.getAptCount() + 1);
                    this.communityProvider.updateCommunity(community);
                }
                
            }else {
                Family family = this.familyProvider.findFamilyByAddressId(cmd.getAddressId());
                if(family == null){
                    LOGGER.error("Family is not found with addressId=" + cmd.getAddressId());
                    return null;
                }
                Group group = ConvertHelper.convert(family, Group.class); 
                group.setIntegralTag1(addr.getId());
                this.groupProvider.updateGroup(group);
                //this.familyService.approveMembersByFamily(family);
                this.addressProvider.deleteAddress(address);
            }
            return null;
        });
        long endTime = System.currentTimeMillis();
        LOGGER.info("Correct address elapse=" + (endTime - startTime));
        
    }
    
    private String parserApartmentFloor(String apartmentName){
        if(StringUtils.isEmpty(apartmentName)) return null;

        if(apartmentName.length() <= 2)
            return "1";
        else if(apartmentName.length() > 2 && 
                apartmentName.startsWith(apartmentName.substring(0, apartmentName.length() - 2))){
            return apartmentName.substring(0, apartmentName.length() - 2);
        }
        return null;
    }

    @Override
    public List<CommunityDoc> searchCommunities(SearchCommunityCommand cmd) {
        if(cmd.getKeyword() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid keyword paramter.");
        }
        if(cmd.getCityId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid cityId paramter.");
        }
        int pageNum = cmd.getPageOffset() == null ? 1: cmd.getPageOffset();
        final int pageSize = cmd.getPageSize() == null ? this.configurationProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();
        
        return communitySearcher.searchDocs(cmd.getKeyword(), cmd.getCityId(), pageNum - 1, pageSize);
    }

    private void checkUserPrivilege(long userId, long communityId) {
        boolean flag = false;
        List<FamilyDTO> familydtos = this.familyProvider.getUserFamiliesByUserId(userId);
        if(familydtos == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                    "User has not living in community.");
        }
        for (FamilyDTO family : familydtos) {
            if(family.getCommunityId().longValue() == communityId){
                flag = true;
                break;
            }
        }
        if(!flag){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                    "User has not living in community.");
        }
        
    }
    @Override
    public ListApartmentByBuildingNameCommandResponse listApartmentsByBuildingName(ListApartmentByBuildingNameCommand cmd) {
        if(cmd.getCommunityId() == null || cmd.getBuildingName() == null || cmd.getBuildingName().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid communityId, buildingName parameter");
        
        User user = UserContext.current().getUser();
        long userId = user.getId();
        
        checkUserPrivilege(userId,cmd.getCommunityId());
        
        List<ApartmentDTO> results = new ArrayList<ApartmentDTO>();
        int pageOffset = cmd.getPageOffset() == null ? 1 : cmd.getPageOffset();
        int pageSize = cmd.getPageSize() == null ? this.configurationProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();
        int offset = (int) PaginationHelper.offsetFromPageOffset((long)pageOffset, (long)pageSize);
        ListApartmentByBuildingNameCommandResponse response = new ListApartmentByBuildingNameCommandResponse();
        //修改为整个小区有人住门牌数
        int totalCount = this.addressProvider.countApartmentsByBuildingName(cmd.getCommunityId(), cmd.getBuildingName());
        if(totalCount == 0) return response;
        
        List<ApartmentDTO> list = this.addressProvider.listApartmentsByBuildingName(cmd.getCommunityId(), 
                cmd.getBuildingName() , offset , pageSize);
        list.stream().map((r) ->{
            Family family = this.familyProvider.findFamilyByAddressId(r.getAddressId());
            if(family != null && family.getMemberCount() > 0){
                r.setLivingStatus(AddressLivingStatus.ACTIVE.getCode());
                r.setFamilyId(family.getId());
            }
            results.add(r);
            return null;
        }).collect(Collectors.toList());
        response.setApartmentLivingCount(totalCount);
        sortApartment(results);
        response.setApartmentList(results);
        return response;
    }
    
    private static void sortApartment(List<ApartmentDTO> results){
        if(results == null || results.isEmpty())
            return;
        String regexNum = "^\\d+$";
        String regexNumChar = "^(\\d+)([a-zA-Z]+)$";
        String regexNumCharNum = "^\\d+[A-Za-z]+$";
        Pattern patternNum =  Pattern.compile(regexNum);
        Pattern patternNumChar =  Pattern.compile(regexNumChar);
        Pattern patternNumCharNum =  Pattern.compile(regexNumCharNum);
        String apartmentName = results.get(0).getApartmentName();
        try{
            if(patternNum.matcher(apartmentName).matches()){
                results.sort(new Comparator<ApartmentDTO>() {
                    @Override
                    public int compare(ApartmentDTO o1, ApartmentDTO o2) {
                        return Integer.valueOf(o1.getApartmentName()) - Integer.valueOf(o2.getApartmentName());
                    }
                });
            }else if(patternNumChar.matcher(apartmentName).matches()){
                results.sort(new Comparator<ApartmentDTO>() {
                    @Override
                    public int compare(ApartmentDTO o1, ApartmentDTO o2) {
                        
                        String o1_num = getGroupValue(o1.getApartmentName(),1);
                        String o2_num = getGroupValue(o2.getApartmentName(),1);
                        if((Integer.valueOf(o1_num) - Integer.valueOf(o2_num)) == 0){
                            String o1_c = getGroupValue(o1.getApartmentName(),2);
                            String o2_c = getGroupValue(o2.getApartmentName(),2);
                            return o1_c.compareTo(o2_c);
                        }
                        return Integer.valueOf(o1_num) - Integer.valueOf(o2_num);
                    }
                });
                
            }else if(patternNumCharNum.matcher(apartmentName).matches()){
                
            }
            
        }catch(NumberFormatException e){
           System.out.println("Number format is error."+e.getMessage());
        }catch(Exception e){
            System.out.println("Format apartment is error."+e);
        }
        
    }
    
    private static String getGroupValue(String apartmentName,int index){
        Matcher m =  Pattern.compile("^(\\d+)([a-zA-Z]+)$").matcher(apartmentName);
        m.find();
        return m.group(index);
    }
    
    @Override
    public AddressDTO createServiceAddress(CreateServiceAddressCommand cmd) {
        if(cmd.getCityId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid cityId, cityId parameter");
        }
        if(cmd.getAreaId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid areaId, areaId parameter");
        }
        if(cmd.getAddress() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid address, address parameter");
        }
        Region city = this.regionProvider.findRegionById(cmd.getCityId());
        if(city == null){
            throw RuntimeErrorException.errorWith(RegionServiceErrorCode.SCOPE, RegionServiceErrorCode.ERROR_REGION_NOT_EXIST, 
                    "City is not found");
        }
        Region area = this.regionProvider.findRegionById(cmd.getAreaId());
        if(area == null){
            throw RuntimeErrorException.errorWith(RegionServiceErrorCode.SCOPE, RegionServiceErrorCode.ERROR_REGION_NOT_EXIST, 
                    "Area is not found");
        }
        User user = UserContext.current().getUser();
        long userId = user.getId();
        
        Address address = this.addressProvider.findAddressByRegionAndAddress(cmd.getCityId(),cmd.getAreaId(),cmd.getAddress());
        
        if(address == null) {
            // optimize with double-lock pattern 
            Tuple<Address, Boolean> result = this.coordinationProvider
                     .getNamedLock(CoordinationLocks.CREATE_ADDRESS.getCode()).enter(()-> {
                Address addr = this.addressProvider.findAddressByRegionAndAddress(cmd.getCityId(),cmd.getAreaId(),cmd.getAddress());;
                long lcStartTime = System.currentTimeMillis();
                if(addr == null) {
                     addr = new Address();
                     addr.setCityId(cmd.getCityId());
                     addr.setCityName(city.getName());
                     addr.setAreaId(cmd.getAreaId());
                     addr.setAreaName(area.getName());
                     addr.setAddress(cmd.getAddress());
                     addr.setAddressAlias(cmd.getAddress());
                     addr.setStatus(AddressAdminStatus.ACTIVE.getCode());
                     addr.setCreatorUid(userId);
                     this.addressProvider.createAddress(addr);
                     
                     //insert user_service_addresses
                     UserServiceAddress serviceAddress = new UserServiceAddress();
                     serviceAddress.setAddressId(addr.getId());
                     serviceAddress.setOwnerUid(userId);;
                     this.userActivityProvider.addUserServiceAddress(serviceAddress);
                }
                long lcEndTime = System.currentTimeMillis();
                LOGGER.info("create address in the lock,elapse=" + (lcEndTime - lcStartTime));
                return addr;
             });
             
             address = result.first();
         }
         return ConvertHelper.convert(address, AddressDTO.class);
    }
    

	@Override
	public void importCommunityInfos(MultipartFile[] files) {
		long startTime = System.currentTimeMillis();
		User user  = UserContext.current().getUser();
		List<Region> cityList = regionProvider.listRegions(RegionScope.CITY, RegionAdminStatus.ACTIVE, null);
		List<Region> areaList = regionProvider.listRegions(RegionScope.AREA, RegionAdminStatus.ACTIVE, null);
		
		try {
			String seperator = configurationProvider.getValue(ADMIN_IMPORT_DATA_SEPERATOR, "#@");
			List<String[]> dataList =FileHelper.getDataArrayByInputStream(files[0].getInputStream(), seperator);
			List<CommunityDataInfo> communityList = DataFileHandler.getComunityDataByFile(dataList, cityList, areaList, user);
			
			if(communityList != null && communityList.size() > 0){
				//事务原子操作。1,导入小区和小区点经纬度  2,导入小区物业条目 
				txImportCommunity(user, communityList);
			}
			else{
				 throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE, 
			                "can not to import the community.parse file error.");
			}
			long endTime = System.currentTimeMillis();
            LOGGER.info("success to import address ,elapse=" + (endTime - startTime));
		}  catch (IOException e) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE, 
	                "can not to import the community.io error.");
		}
	}

	private void txImportCommunity(User user,List<CommunityDataInfo> communityList) {
	
		// 导入小区信息 和小区坐标
		importCommunityInfo(user,communityList);

		// 导入小区物业条目
		importCommunityProperty(user, communityList);
		
		//生成小区时，创建小区的索引
		List<Community> communities = createCommunities(communityList);
		communitySearcher.bulkUpdate(communities);
			
	}

	private List<Community> createCommunities(List<CommunityDataInfo> communityList)
	{
		List<Community> communities = new ArrayList<Community>();
		if(communityList != null && communityList.size() > 0){
			for (CommunityDataInfo communityDataInfo : communityList) {
				Community community = DataFileHandler.getCommunityByData(communityDataInfo);
				communities.add(community);
			}
		}
		return communities;
	}
	
	private void importCommunityProperty(User user,List<CommunityDataInfo> communityList) {
		for (CommunityDataInfo communityDataInfo : communityList) {
			List<CommunityPmContact> contacts = DataFileHandler.getPropItemsByData(user, communityDataInfo);
			if(contacts != null && contacts.size() > 0){
				for (CommunityPmContact communityPmContact : contacts) {
					propertyMgrProvider.createPropContact(communityPmContact);
				}
			}
		}
	}

	private void importCommunityInfo(User user,List<CommunityDataInfo> communityList) {
		this.dbProvider.execute((TransactionStatus status) ->  {
			for (int i = 0; i < communityList.size(); i++)
			{
				CommunityDataInfo communityDataInfo = communityList.get(i);
				Community community = DataFileHandler.getCommunityByData(communityDataInfo);
				CommunityGeoPoint communityGeoPoint = DataFileHandler.getCommunityPosByData(communityDataInfo);
				if (!isCommunityExit(community.getAreaId(), community.getName()))
				{
					communityProvider.createCommunity(user.getId(), community);
					communityGeoPoint.setCommunityId(community.getId());
					communityProvider.createCommunityGeoPoint(communityGeoPoint);
					communityDataInfo.setId(community.getId());
				} else
				{
					LOGGER.error("community  duplicate .community = " + community);
					 throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE, 
			                    "the community is existed.");
				}
			}
		  return null;
        });
		
	}
	
	// 判断小区是否重复 : 和数据库中已存在的小区比较 重复返回true 不重复返回false
	public boolean isCommunityExit(long areaId, String communityName)
	{
		Community community = communityProvider.findCommunityByAreaIdAndName(areaId, communityName);
		if (community != null)
		{
			return true;
		} else
		{
			return false;
		}
	}

	@Override
	public void importAddressInfos(MultipartFile[] files) {
		long startTime =  System.currentTimeMillis();
		
		User user  = UserContext.current().getUser();
		List<Region> cityList = regionProvider.listRegions(RegionScope.CITY, RegionAdminStatus.ACTIVE, null);
		List<Region> areaList = regionProvider.listRegions(RegionScope.AREA, RegionAdminStatus.ACTIVE, null);
		
	
		try {
			String seperator = configurationProvider.getValue(ADMIN_IMPORT_DATA_SEPERATOR, "#@");
			List<String[]> dataList =FileHelper.getDataArrayByInputStream(files[0].getInputStream(), seperator);
			
			if(dataList != null && dataList.size() > 0){
				//事务原子操作。1,导入小区和小区点经纬度  2,导入小区物业条目 
				txImportAddress(user,cityList,areaList,dataList);
			}
			else{
				 throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE, 
			                "can not to import the community.parse file error.");
			}
			long endTime = System.currentTimeMillis();
            LOGGER.info("success to import address ,elapse=" + (endTime - startTime));
		} catch (IOException e) {
			 throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE, 
		                "can not to import the community.IOException.");
		}
		
		
	}

	private void txImportAddress(User user, List<Region> cityList,List<Region> areaList, List<String[]> dataList) {
		List<Address> addressList = new ArrayList<Address>();
		int maxCount =  configurationProvider.getIntValue(ADMIN_IMPORT_ADDRESS_ALLOW_MAX_COUNT, 0);

		// 缓存list 匹配查找小区时 先从缓存list匹配小区 如果没有，再去数据库查找。然后加入到缓存list
		List<Community> bufferCommunityList = new ArrayList<Community>(2048);
		// 缓存map 查询小区公寓总数时 先从map中取小区id对应的总数 如果没有再去数据库查找。然后加入到缓存map 确保
		// 每个小区的总数只查一次。
		Map<Long, Integer> bufferAptCountMap = new HashMap<Long, Integer>();
		// 查询小区公寓的标记状态 先从map中取小区id对应的以前公寓标记状态 如果是未标记状态 则先将以前所有小区标记
		// 可能冲突重复状态 然后加入到缓存map
		Map<Long, Boolean> flagMap = new HashMap<Long, Boolean>();// 未标记状态。
																	// 每个小区只需要操作一次
		for (String[] rowDatas : dataList)
		{
			if (rowDatas.length == DataProcessConstants.DATA_LENGTH_ZISE_APARTMENT)
			{
				AddressDataInfo address = DataFileHandler.convertAddressDataByRow(rowDatas);
				Community community = getCommunityByList(address, bufferCommunityList, cityList, areaList);
				CommunityGeoPoint communityGeoPoint  = communityProvider.findCommunityGeoPointById(community.getId());
				Address ne = DataFileHandler.convertNEDBModelDataInfo(community, communityGeoPoint,address, user);

				// 先查询该小区已存在的公寓总数。新增的小区公寓 理论上应该最多只可能有很少量的用户填写的公寓门牌号。
				// 总数如果小于正常值（在系统配置项中配置）。1：标记已存在的公寓
				// 为可能重复。需要人工去重。只进行一次操作 2：添加公寓ne
				// 总数如果大于正常值。肯定有问题： 第一，可能一部分公寓已经导入。 第二。小区公寓以前导过。
				// 则不属于新增操作。移至补充公寓。不进行数据库操作。抛出异常给据数组审核。
				long communityId = community.getId();
				int aptCount = getAptCountByCommunityId(community, bufferAptCountMap);
				if (aptCount <= maxCount)
				{
					if (aptCount != 0)
					{
						if (flagMap.containsKey(communityId))
						{
							// 标记已存在的公寓 为可能重复
							// 标记公寓： 应该是每一小区 只标记一次 map《id，flag》
							// 1:map中包含该小区且 标记为false
							// 标记并将false设置为true
							// 2：map中 不包含该小区 标记并存入map中
							if (!flagMap.get(communityId))
							{
//								updateAptConflictByCommunityId(communityId);
								flagMap.put(communityId, true);
							}
						} else
						{
//							updateAptConflictByCommunityId(communityId);
							flagMap.put(communityId, true);
						}
					}
					addressList.add(ne);
				} else
				{
					LOGGER.error("community apt is too much.community = " + community + ",aptCount = " + aptCount);
					throw new RuntimeErrorException(community.getName() + "已经有 " + aptCount + "个公寓了。请请检查是否重复导入，并与后台人员联系核实。");
				}
			} else
			{
				LOGGER.error("data format is not correct.rowDatas = " + Arrays.toString(rowDatas));
				throw new RuntimeErrorException("公寓文件格式不正确。每行共"+ DataProcessConstants.DATA_LENGTH_ZISE_APARTMENT +"项。数据 = " + Arrays.toString(rowDatas));
			}
		}
		
		txImportAddressList(addressList);
		
		//处理导入公寓后的小区公寓数。
		if(bufferCommunityList != null && bufferCommunityList.size() > 0)
		{
			for (Community community : bufferCommunityList)
			{
				long communityId = community.getId();
//				int aptCount = countAptByCommunityId(communityId);
//				communityService.updateCommunityAptcountByIdAndCount(aptCount, communityId);
			}
		}
		
	}

	public void txImportAddressList(List<Address> addressList) {
		this.dbProvider.execute((TransactionStatus status) ->  {
			long startTime = System.currentTimeMillis();
			LOGGER.info("======count=" + addressList.size() +",startTime=" + startTime);
//			for (Address address : addressList) {
//				addressProvider.createAddress(address);
//			}
			Long stime = System.currentTimeMillis();
			Long etime = System.currentTimeMillis();
			for (int i = 0; i < addressList.size(); i++) {
				if(i%100 == 0 && i!= 0){
					addressProvider.createAddress2(addressList.get(i));
					etime = System.currentTimeMillis();
					LOGGER.info("$$ successed to insert " +(i/100)+"个100行。time=======" + (etime-stime));
					stime = etime;
				}
				else{
					addressProvider.createAddress(addressList.get(i));
				}
			}
			long endTime = System.currentTimeMillis();
			LOGGER.info("success.======count=" + addressList.size() +",totalTime=" + (endTime - startTime));
		    return null;
        });
	}

	public Community getCommunityByList(AddressDataInfo address, List<Community> bufferCommunityList,
			List<Region> cityList, List<Region> areaList) 
	{

		Region city = DataFileHandler.getCityByData(address.getCityName(), cityList);
		if (city == null)
		{
			throw new RuntimeErrorException("数据文件的城市名字不正确。小区 = " + address.getCityName() + "\t"
					+ address.getAreaName() + "\t" + address.getCommunityName());
		}

		Region area = DataFileHandler.getAreaByData(city.getId(), address.getAreaName(), areaList);
		if (area == null)
		{
			throw new RuntimeErrorException("数据文件的区县名字不正确。小区 = " + address.getCityName() + "\t"
					+ address.getAreaName() + "\t" + address.getCommunityName());
		}
		address.setAreaId(area.getId());
		Community community = null;
		if (address != null)
		{
			Long areaId = address.getAreaId();
			String communityName = address.getCommunityName();
			for (Community bufferCommunity : bufferCommunityList)
			{
				if (areaId == bufferCommunity.getAreaId() && communityName.equals(bufferCommunity.getName()))
				{
					community = bufferCommunity;
					break;
				}
			}
			if (community == null)
			{
				try
				{
					community = communityProvider.findCommunityByAreaIdAndName(areaId, communityName);
				} catch (Exception e)
				{
					LOGGER.error("community duplicate . areaId = " + areaId + " , communityName =" + communityName, e);
					throw new RuntimeErrorException("小区重复 . 区县id = " + areaId + " , 区县名字 =" + area.getName()
							+ " , 小区名字 =" + communityName);
				}
				if (community == null)
				{
					throw new RuntimeErrorException("数据文件的小区未找到。小区 = " + address.getAreaName() + "\t"
							+ address.getCommunityName());
				}
				bufferCommunityList.add(community);
			}
		}
		return community;
	}
	
	public int getAptCountByCommunityId(Community community, Map<Long, Integer> map)
	{
		int count = 0;
		Long communityId = community.getId();
		if (map.containsKey(communityId))
		{
			count = map.get(communityId);
		} else
		{
			count =  community.getAptCount()==null ?0:community.getAptCount();  //countAptByCommunityId(communityId);
			map.put(communityId, count);
		}
		return count;
	}


}
