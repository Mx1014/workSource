// @formatter:off
package com.everhomes.address;

import com.everhomes.asset.AddressIdAndName;
import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.community.Building;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityDataInfo;
import com.everhomes.community.CommunityGeoPoint;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.core.AppConfig;
import com.everhomes.customer.CustomerEntryInfo;
import com.everhomes.customer.CustomerService;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.enterprise.Enterprise;
import com.everhomes.enterprise.EnterpriseProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.Family;
import com.everhomes.family.FamilyProvider;
import com.everhomes.family.FamilyService;
import com.everhomes.family.FamilyUtils;
import com.everhomes.group.Group;
import com.everhomes.group.GroupAdminStatus;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceResource;
import com.everhomes.namespace.NamespaceResourceProvider;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractBuildingMapping;
import com.everhomes.openapi.ContractBuildingMappingProvider;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.organization.ImportFileService;
import com.everhomes.organization.ImportFileTask;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.organization.pm.CommunityPmContact;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.address.AddressAdminStatus;
import com.everhomes.rest.address.AddressArrangementDTO;
import com.everhomes.rest.address.AddressArrangementStatus;
import com.everhomes.rest.address.AddressArrangementTemplateCode;
import com.everhomes.rest.address.AddressArrangementType;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.address.AddressLivingStatus;
import com.everhomes.rest.address.AddressServiceErrorCode;
import com.everhomes.rest.address.ApartmentAttachmentDTO;
import com.everhomes.rest.address.ApartmentDTO;
import com.everhomes.rest.address.ApartmentFloorDTO;
import com.everhomes.rest.address.ArrangementApartmentDTO;
import com.everhomes.rest.address.ArrangementOperationFlag;
import com.everhomes.rest.address.BuildingDTO;
import com.everhomes.rest.address.ClaimAddressCommand;
import com.everhomes.rest.address.ClaimedAddressInfo;
import com.everhomes.rest.address.CommunityAdminStatus;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.address.CommunitySummaryDTO;
import com.everhomes.rest.address.CreateAddressArrangementCommand;
import com.everhomes.rest.address.CreateServiceAddressCommand;
import com.everhomes.rest.address.DeleteAddressArrangementCommand;
import com.everhomes.rest.address.DeleteApartmentAttachmentCommand;
import com.everhomes.rest.address.DeleteServiceAddressCommand;
import com.everhomes.rest.address.DisclaimAddressCommand;
import com.everhomes.rest.address.DownloadApartmentAttachmentCommand;
import com.everhomes.rest.address.ExcuteAddressArrangementCommand;
import com.everhomes.rest.address.ExportApartmentsInBuildingDTO;
import com.everhomes.rest.address.GetApartmentByBuildingApartmentNameCommand;
import com.everhomes.rest.address.GetApartmentNameByBuildingNameCommand;
import com.everhomes.rest.address.GetApartmentNameByBuildingNameDTO;
import com.everhomes.rest.address.GetHistoryApartmentCommand;
import com.everhomes.rest.address.HistoryApartmentDTO;
import com.everhomes.rest.address.ImportApartmentDataDTO;
import com.everhomes.rest.address.ListAddressArrangementCommand;
import com.everhomes.rest.address.ListAddressByKeywordCommand;
import com.everhomes.rest.address.ListAddressByKeywordCommandResponse;
import com.everhomes.rest.address.ListAddressCommand;
import com.everhomes.rest.address.ListApartmentAttachmentsCommand;
import com.everhomes.rest.address.ListApartmentByBuildingNameCommand;
import com.everhomes.rest.address.ListApartmentByBuildingNameCommandResponse;
import com.everhomes.rest.address.ListApartmentFloorCommand;
import com.everhomes.rest.address.ListBuildingByKeywordCommand;
import com.everhomes.rest.address.ListCommunityByKeywordCommand;
import com.everhomes.rest.address.ListNearbyCommunityCommand;
import com.everhomes.rest.address.ListNearbyMixCommunitiesCommand;
import com.everhomes.rest.address.ListNearbyMixCommunitiesCommandResponse;
import com.everhomes.rest.address.ListNearbyMixCommunitiesCommandV2Response;
import com.everhomes.rest.address.ListPropApartmentsByKeywordCommand;
import com.everhomes.rest.address.SearchCommunityCommand;
import com.everhomes.rest.address.SuggestCommunityCommand;
import com.everhomes.rest.address.SuggestCommunityDTO;
import com.everhomes.rest.address.UpdateAddressArrangementCommand;
import com.everhomes.rest.address.UploadApartmentAttachmentCommand;
import com.everhomes.rest.address.admin.CorrectAddressAdminCommand;
import com.everhomes.rest.address.admin.ImportAddressCommand;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.community.CommunityDoc;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.community.ListApartmentEnterpriseCustomersCommand;
import com.everhomes.rest.contract.ContractDTO;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.customer.CustomerEntryInfoDTO;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.customer.ListCustomerEntryInfosCommand;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.family.LeaveFamilyCommand;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.namespace.NamespaceResourceType;
import com.everhomes.rest.openapi.UserServiceAddressDTO;
import com.everhomes.rest.organization.ImportFileResultLog;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.organization.ImportFileTaskType;
import com.everhomes.rest.organization.OrganizationCommunityDTO;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.organization.OrganizationOwnerDTO;
import com.everhomes.rest.organization.OrganizationServiceErrorCode;
import com.everhomes.rest.organization.pm.AddressMappingStatus;
import com.everhomes.rest.organization.pm.ListOrganizationOwnersByAddressCommand;
import com.everhomes.rest.organization.pm.OrganizationOwnerAddressAuthType;
import com.everhomes.rest.region.RegionAdminStatus;
import com.everhomes.rest.region.RegionScope;
import com.everhomes.rest.region.RegionServiceErrorCode;
import com.everhomes.rest.ui.user.SceneDTO;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.search.CommunitySearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhActivities;
import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.server.schema.tables.pojos.EhBuildings;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserGroupHistory;
import com.everhomes.user.UserGroupHistoryProvider;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.user.UserServiceAddress;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.FileHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.PinYinHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import com.everhomes.util.file.DataFileHandler;
import com.everhomes.util.file.DataProcessConstants;
import com.everhomes.varField.FieldService;
import com.everhomes.varField.ScopeFieldItem;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record11;
import org.jooq.Record2;
import org.jooq.Record5;
import org.jooq.SelectConditionStep;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.everhomes.util.RuntimeErrorException.errorWith;

@Component
public class AddressServiceImpl implements AddressService, LocalBusSubscriber, ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressServiceImpl.class);

    private static final String ADMIN_IMPORT_DATA_SEPERATOR = "admin.import.data.seperator";
    private static final String ADMIN_IMPORT_ADDRESS_ALLOW_MAX_COUNT = "admin.import.address.allow.max.count";

    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private ImportFileService importFileService;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private LocalBus localBus;

    @Autowired
    private AddressProvider addressProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

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

    @Autowired
    private EnterpriseProvider enterpriseProvider;

    @Autowired
    private UserGroupHistoryProvider userGroupHistoryProvider;

    @Autowired
    private PropertyMgrService propertyMgrService;

    @Autowired
    private UserService userService;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private NamespaceResourceProvider namespaceResourceProvider;

    @Autowired
    private ContractBuildingMappingProvider contractBuildingMappingProvider;

    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private EnterpriseCustomerProvider enterpriseCustomerProvider;

    @Autowired
    private FieldService fieldService;
    
    @Autowired
	private LocaleTemplateService localeTemplateService;
    
    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private CustomerService customerService;
    
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    public void setup() {
        localBus.subscribe(DaoHelper.getDaoActionPublishSubject(DaoAction.CREATE, EhCommunities.class, null), this);
        localBus.subscribe(DaoHelper.getDaoActionPublishSubject(DaoAction.MODIFY, EhCommunities.class, null), this);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            setup();
        }
    }
    
    public Action onLocalBusMessage(Object sender, String subject, Object args, String subscriptionPath) {
        // TODO monitor changeEnergyMeter notifications for cache invalidation
        return Action.none;
    }

    @Override
    public SuggestCommunityDTO suggestCommunity(SuggestCommunityCommand cmd) {
        int namespaceId = (UserContext.current().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getNamespaceId();
        if (cmd.getRegionId() == null || cmd.getName() == null || cmd.getName().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter, regionId and name should not be null or empty");

        Region region = this.regionProvider.findRegionById(cmd.getRegionId());
        if (region == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid regionId parameter, could not find the region");
        Community community = new Community();
        byte scopeCode = region.getScopeCode();
        if (scopeCode == RegionScope.AREA.getCode()) {
            Region city = this.regionProvider.findRegionById(region.getParentId());
            if (city == null)
                throw RuntimeErrorException.errorWith(RegionServiceErrorCode.SCOPE, RegionServiceErrorCode.ERROR_REGION_NOT_EXIST,
                        "Invalid cityId parameter, could not find the region");
            community.setCityId(city.getId());
            community.setCityName(city.getName());
            community.setAreaId(region.getId());
            community.setAreaName(region.getName());
        } else if (scopeCode == RegionScope.CITY.getCode()) {
            community.setCityId(region.getId());
            community.setCityName(region.getName());
            community.setAreaId(0L);
            community.setAreaName("");
        } else {
            throw RuntimeErrorException.errorWith(RegionServiceErrorCode.SCOPE, RegionServiceErrorCode.ERROR_REGION_NOT_EXIST,
                    "Invalid regionId parameter, could not find the region");
        }

        community.setName(cmd.getName());
        community.setAddress(cmd.getAddress());

        User user = UserContext.current().getUser();
        community.setCreatorUid(user.getId());
        community.setStatus(CommunityAdminStatus.CONFIRMING.getCode());
        community.setNamespaceId(namespaceId);

        this.dbProvider.execute((TransactionStatus status) -> {
            this.communityProvider.createCommunity(user.getId(), community);
            if (cmd.getLatitude() != null && cmd.getLongitude() != null) {
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

        SuggestCommunityDTO dto = ConvertHelper.convert(community, SuggestCommunityDTO.class);
        dto.setRegionId(region.getId());
        dto.setRegionName(region.getName());
        return dto;
    }

    @Override
    public Tuple<Integer, List<CommunitySummaryDTO>> listSuggestedCommunities() {
        final List<CommunitySummaryDTO> results = new ArrayList<>();

        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null,
                (DSLContext context, Object reducingContext) -> {

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
        int namespaceId = UserContext.getCurrentNamespaceId();
        final List<CommunityDTO> results = new ArrayList<>();

        if (cmd.getCityId() == null && cmd.getLatigtue() == null && cmd.getLongitude() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter, need at least one valid value in one of these parameters: latitude, longitude, cityId");

        if (cmd.getLatigtue() == null || cmd.getLongitude() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter, latitude and longitude have to be both specified or neigher");

        // TODO, return all communities only to test our REST response for now
//        CommunityDTO testDto = ConvertHelper.convert(communityProvider.findCommunityById(240111044331051304l), CommunityDTO.class);
//        results.add(testDto);
        
        ListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(null);
        List<Community> coms = this.communityProvider.listCommunitiesByKeyWord(locator, 100, null, namespaceId, cmd.getCommunityType());
        for(Community r : coms) {
            CommunityDTO community = ConvertHelper.convert(r, CommunityDTO.class);
            if(cmd.getCityId() != null && !cmd.getCityId().equals(community.getCityId())) {
                continue;
            }
            
            if (null == cmd.getCommunityType()) {
                results.add(community);
            } else {
                if (cmd.getCommunityType().equals(community.getCommunityType())) {
                    results.add(community);
                }
            }
        }

        /* List<CommunityGeoPoint> pointList = this.communityProvider.findCommunityGeoPointByGeoHash(cmd.getLatigtue(), cmd.getLongitude(), 6);
        List<Long> communityIds = getAllCommunityIds(pointList);
        
        //select active community by xiongying 20160516
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null,
                (DSLContext context, Object reducingContext) -> {

                    context.select().from(Tables.EH_COMMUNITIES)
                            .where(Tables.EH_COMMUNITIES.ID.in(communityIds))
                            .and(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId))
                            .and(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()))
                            .fetch().map((r) -> {

                        CommunityDTO community = ConvertHelper.convert(r, CommunityDTO.class);
                        if (null == cmd.getCommunityType()) {
                            results.add(community);
                        } else {
                            if (cmd.getCommunityType().equals(community.getCommunityType())) {
                                results.add(community);
                            }
                        }

                        return null;
                    });

                    return true;
                }); */
        
        for(CommunityDTO result : results) {
            if(result.getName() != null && result.getName().length() > 1) {
                result.setFirstLatterOfName(PinYinHelper.getCapitalInitial(PinYinHelper.getPinYin(result.getName().substring(0, 1))));    
            }
            
        }


        return new Tuple<>(ErrorCodes.SUCCESS, results);
    }

    private List<Long> getAllCommunityIds(List<CommunityGeoPoint> pointList) {
        if (pointList == null || pointList.isEmpty())
            return new ArrayList<Long>();
        List<Long> communityIds = new ArrayList<Long>(pointList.size());
        for (CommunityGeoPoint c : pointList) {
            communityIds.add(c.getCommunityId());
        }
        return communityIds;
    }

    /**
     *  TODO: Pagination across partitions needs to be pre-fetched and cached
     */
    @Override
    public Tuple<Integer, List<CommunityDTO>> listCommunitiesByKeyword(ListCommunityByKeywordCommand cmd) {
        int namespaceId = (UserContext.current().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getNamespaceId();
        //
        // TODO, should be integrating with keyword search server
        //
        if (cmd.getKeyword() == null || cmd.getKeyword().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid keyword parameter");

        final List<CommunityDTO> results = new ArrayList<>();
        final long pageSize = this.configurationProvider.getIntValue("pagination.page.size",
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE);
        long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset(), pageSize);
        Tuple<Integer, Long> targetShard = new Tuple<>(0, offset);
        if (offset > 0) {
            final List<Long> countsInShards = new ArrayList<>();
            this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null,
                    (DSLContext context, Object reducingContext) -> {

                        Long count = context.selectCount().from(Tables.EH_COMMUNITIES)
                                .where(Tables.EH_COMMUNITIES.NAME.like(cmd.getKeyword() + "%"))
                                .or(Tables.EH_COMMUNITIES.ALIAS_NAME.like(cmd.getKeyword() + "%"))
                                .and(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId))
                                .fetchOne(0, Long.class);

                        countsInShards.add(count);
                        return true;
                    });

            targetShard = PaginationHelper.offsetFallsAt(countsInShards, offset);
        }
        if (targetShard.first() < 0)
            return new Tuple<>(ErrorCodes.SUCCESS, results);

        final int[] currentShard = new int[1];
        currentShard[0] = 0;
        final Tuple<Integer, Long> fallingShard = targetShard;

        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), currentShard,
                (DSLContext context, Object reducingContext) -> {
                    int[] current = (int[]) reducingContext;
                    if (current[0] < fallingShard.first()) {
                        current[0] += 1;
                        return true;
                    }

                    long off = 0;
                    if (current[0] == fallingShard.first())
                        off = fallingShard.second();

                    context.select().from(Tables.EH_COMMUNITIES)
                            .where(Tables.EH_COMMUNITIES.NAME.like(cmd.getKeyword() + "%"))
                            .or(Tables.EH_COMMUNITIES.ALIAS_NAME.like(cmd.getKeyword() + "%"))
                            .and(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId))
                            .limit((int) pageSize).offset((int) off)
                            .fetch().map((r) -> {
                        CommunityDTO community = ConvertHelper.convert(r, CommunityDTO.class);
                        if (results.size() <= pageSize + 1)
                            results.add(community);
                        return null;
                    });

                    return true;
                });

        if (results.size() > pageSize) {
            results.remove(results.size() - 1);
            return new Tuple<>(ErrorCodes.SUCCESS_MORE_DATA, results);
        }

        return new Tuple<>(ErrorCodes.SUCCESS, results);
    }

    @Override
    public Tuple<Integer, List<BuildingDTO>> listBuildingsByKeyword(ListBuildingByKeywordCommand cmd) {
        if (cmd.getCommunityId() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid communityId or keyword parameter");

        if (cmd.getKeyword() == null)
            cmd.setKeyword("");
        ;
        List<BuildingDTO> results = new ArrayList<BuildingDTO>();
        long startTime = System.currentTimeMillis();
        int namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        //从地址表中查会导致初始化时没有门牌的楼栋永远无法添加门牌，现改为从楼栋表中取 bug16081 add by xiongying20170925
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhBuildings.class), null,
                (DSLContext context, Object reducingContext) -> {

                    String likeVal = "%" + cmd.getKeyword() + "%";

//                    context.selectDistinct(Tables.EH_ADDRESSES.BUILDING_NAME, Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME)
//                            .from(Tables.EH_ADDRESSES)
//                            .where(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(cmd.getCommunityId())
//                                    .and(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
//                                    .and(Tables.EH_ADDRESSES.BUILDING_NAME.like(likeVal)
//                                            .or(Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME.like(likeVal))
//                                    ))
//                            .and(Tables.EH_ADDRESSES.STATUS.equal(AddressAdminStatus.ACTIVE.getCode()))
//                            .fetch().map((r) -> {
//                        BuildingDTO building = new BuildingDTO();
//                        building.setBuildingName(r.getValue(Tables.EH_ADDRESSES.BUILDING_NAME));
//                        building.setBuildingAliasName(r.getValue(Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME));
//                        results.add(building);
//                        return null;
//                    });
                    // 添加日志方便调试，在深圳湾环境，当导入门牌数据时，若楼栋“创投大厦”已经存在，则会自动创建出
                    // 一个“创投大厦chuang xintou”新楼栋，需要打日志来确认为什么会这样 by lqs 20170822
                    SelectQuery<Record2<String, String>> query = context.selectDistinct(Tables.EH_BUILDINGS.NAME, Tables.EH_BUILDINGS.ALIAS_NAME)
                            .from(Tables.EH_BUILDINGS)
                            .where(Tables.EH_BUILDINGS.COMMUNITY_ID.equal(cmd.getCommunityId())
                                    .and(Tables.EH_BUILDINGS.NAMESPACE_ID.eq(namespaceId))
                                    .and(Tables.EH_BUILDINGS.NAME.like(likeVal)
                                            .or(Tables.EH_BUILDINGS.ALIAS_NAME.like(likeVal))
                                    ))
                            .and(Tables.EH_BUILDINGS.STATUS.equal(AddressAdminStatus.ACTIVE.getCode()))
                            .orderBy(Tables.EH_BUILDINGS.DEFAULT_ORDER.desc()).getQuery();

                    query.fetch().map((r) -> {
                        BuildingDTO building = new BuildingDTO();
                        building.setBuildingName(r.getValue(Tables.EH_BUILDINGS.NAME));
                        building.setBuildingAliasName(r.getValue(Tables.EH_BUILDINGS.ALIAS_NAME));
                        results.add(building);
                        return null;
                    });

                    if(LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Query buildings by keyword, sql=" + query.getSQL());
                        LOGGER.debug("Query buildings by keyword, bindValues=" + query.getBindValues());
                    }


                    return true;
                });
//        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null,
//                (DSLContext context, Object reducingContext) -> {
//
//                    String likeVal = "%" + cmd.getKeyword() + "%";
//                    context.selectDistinct(Tables.EH_ADDRESSES.BUILDING_NAME, Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME)
//                            .from(Tables.EH_ADDRESSES)
//                            .where(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(cmd.getCommunityId())
//                                    .and(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
//                                    .and(Tables.EH_ADDRESSES.BUILDING_NAME.like(likeVal)
//                                            .or(Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME.like(likeVal))
//                                    ))
//                            .and(Tables.EH_ADDRESSES.STATUS.equal(AddressAdminStatus.ACTIVE.getCode()))
//                            .fetch().map((r) -> {
//                        BuildingDTO building = new BuildingDTO();
//                        building.setBuildingName(r.getValue(Tables.EH_ADDRESSES.BUILDING_NAME));
//                        building.setBuildingAliasName(r.getValue(Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME));
//                        results.add(building);
//                        return null;
//                    });
//
//                    return true;
//                });
        long endTime = System.currentTimeMillis();
        LOGGER.info("List buildings by keyword,keyword=" + cmd.getKeyword() + ",elapse=" + (endTime - startTime));
        return new Tuple<Integer, List<BuildingDTO>>(ErrorCodes.SUCCESS, results);
    }


    @Override
    public Tuple<Integer, List<BuildingDTO>> listBuildingsByKeywordForBusiness(ListBuildingByKeywordCommand cmd) {
        if (cmd.getCommunityId() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid communityId or keyword parameter");

        if (cmd.getKeyword() == null)
            cmd.setKeyword("");
        ;
        List<BuildingDTO> results = new ArrayList<BuildingDTO>();
        long startTime = System.currentTimeMillis();
        int namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null,
                (DSLContext context, Object reducingContext) -> {

                    String likeVal = "%" + cmd.getKeyword() + "%";
                    context.selectDistinct(Tables.EH_ADDRESSES.BUILDING_NAME, Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME, Tables.EH_ADDRESSES.BUSINESS_BUILDING_NAME)
                            .from(Tables.EH_ADDRESSES)
                            .where(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(cmd.getCommunityId())
                                    .and(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
                                    .and(Tables.EH_ADDRESSES.BUILDING_NAME.like(likeVal)
                                            .or(Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME.like(likeVal))
                                            .or(Tables.EH_ADDRESSES.BUSINESS_BUILDING_NAME.like(likeVal))
                                    ))
                            .and(Tables.EH_ADDRESSES.STATUS.equal(AddressAdminStatus.ACTIVE.getCode()))
                            .fetch().map((r) -> {
                        BuildingDTO building = new BuildingDTO();
                        building.setBuildingName(r.getValue(Tables.EH_ADDRESSES.BUILDING_NAME));
                        building.setBuildingAliasName(r.getValue(Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME));
                        building.setBusinessBuildingnName(r.getValue(Tables.EH_ADDRESSES.BUSINESS_BUILDING_NAME));
                        results.add(building);
                        return null;
                    });

                    return true;
                });
        long endTime = System.currentTimeMillis();
        LOGGER.info("List buildings by keyword,keyword=" + cmd.getKeyword() + ",elapse=" + (endTime - startTime));
        return new Tuple<Integer, List<BuildingDTO>>(ErrorCodes.SUCCESS, results);
    }

    @Override
    public Tuple<Integer, List<ApartmentFloorDTO>> listApartmentFloor(ListApartmentFloorCommand cmd) {
        if (cmd.getCommunityId() == null || cmd.getBuildingName() == null || cmd.getBuildingName().isEmpty()) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid communityId parameter");
        }
        if (cmd.getBuildingName() == null || cmd.getBuildingName().isEmpty()) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid buildingName parameter");
        }

        if (cmd.getKeyword() == null) {
            cmd.setKeyword("");
        }

        List<ApartmentFloorDTO> results = new ArrayList<ApartmentFloorDTO>();
        String likeVal = "%" + cmd.getKeyword() + "%";
        long startTime = System.currentTimeMillis();
        int namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null,
                (DSLContext context, Object reducingContext) -> {
                    context.selectDistinct(Tables.EH_ADDRESSES.APARTMENT_FLOOR)
                            .from(Tables.EH_ADDRESSES)
                            .where(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(cmd.getCommunityId())
                                    .and(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
                                    .and(Tables.EH_ADDRESSES.BUILDING_NAME.equal(cmd.getBuildingName())
                                            .or(Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME.equal(cmd.getBuildingName()))))
                            .and(Tables.EH_ADDRESSES.APARTMENT_FLOOR.like(likeVal))
                            .and(Tables.EH_ADDRESSES.STATUS.equal(AddressAdminStatus.ACTIVE.getCode()))
                            .and(Tables.EH_ADDRESSES.APARTMENT_FLOOR.isNotNull())
                            .and(Tables.EH_ADDRESSES.APARTMENT_FLOOR.trim().ne(""))
                            .orderBy(Tables.EH_ADDRESSES.APARTMENT_FLOOR.asc())
                            .fetch().map((r) -> {
                        ApartmentFloorDTO floor = new ApartmentFloorDTO();
                        floor.setApartmentFloor(r.getValue(Tables.EH_ADDRESSES.APARTMENT_FLOOR));
                        results.add(floor);
                        return null;
                    });

                    return true;
                });
        long endTime = System.currentTimeMillis();
        LOGGER.info("List apartmentFloor by keyword,keyword=" + cmd.getKeyword() + ",elapse=" + (endTime - startTime));
        return new Tuple<Integer, List<ApartmentFloorDTO>>(ErrorCodes.SUCCESS, results);
    }

    @Override
    public Tuple<Integer, List<ApartmentFloorDTO>> listApartmentFloorForBusiness(ListApartmentFloorCommand cmd) {
        if (cmd.getCommunityId() == null || cmd.getBuildingName() == null || cmd.getBuildingName().isEmpty()) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid communityId parameter");
        }
        if (cmd.getBuildingName() == null || cmd.getBuildingName().isEmpty()) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid buildingName parameter");
        }

        if (cmd.getKeyword() == null) {
            cmd.setKeyword("");
        }

        List<ApartmentFloorDTO> results = new ArrayList<ApartmentFloorDTO>();
        String likeVal = "%" + cmd.getKeyword() + "%";
        long startTime = System.currentTimeMillis();
        int namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null,
                (DSLContext context, Object reducingContext) -> {
                    context.selectDistinct(Tables.EH_ADDRESSES.APARTMENT_FLOOR)
                            .from(Tables.EH_ADDRESSES)
                            .where(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(cmd.getCommunityId())
                                    .and(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
                                    .and(Tables.EH_ADDRESSES.BUILDING_NAME.equal(cmd.getBuildingName())
                                            .or(Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME.equal(cmd.getBuildingName()))
                                            .or(Tables.EH_ADDRESSES.BUSINESS_BUILDING_NAME.equal(cmd.getBuildingName()))))
                            .and(Tables.EH_ADDRESSES.APARTMENT_FLOOR.like(likeVal))
                            .and(Tables.EH_ADDRESSES.STATUS.equal(AddressAdminStatus.ACTIVE.getCode()))
                            .and(Tables.EH_ADDRESSES.APARTMENT_FLOOR.isNotNull())
                            .and(Tables.EH_ADDRESSES.APARTMENT_FLOOR.trim().ne(""))
                            .orderBy(Tables.EH_ADDRESSES.APARTMENT_FLOOR.asc())
                            .fetch().map((r) -> {
                        ApartmentFloorDTO floor = new ApartmentFloorDTO();
                        floor.setApartmentFloor(r.getValue(Tables.EH_ADDRESSES.APARTMENT_FLOOR));
                        results.add(floor);
                        return null;
                    });

                    return true;
                });
        long endTime = System.currentTimeMillis();
        LOGGER.info("List apartmentFloor by keyword,keyword=" + cmd.getKeyword() + ",elapse=" + (endTime - startTime));
        return new Tuple<Integer, List<ApartmentFloorDTO>>(ErrorCodes.SUCCESS, results);
    }

    @Override
    public Tuple<Integer, List<ApartmentDTO>> listApartmentsByKeyword(ListPropApartmentsByKeywordCommand cmd) {
        if (cmd.getCommunityId() == null || cmd.getBuildingName() == null || cmd.getBuildingName().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid communityId, buildingName or keyword parameter");
        if (cmd.getKeyword() == null)
            cmd.setKeyword("");
        List<ApartmentDTO> results = new ArrayList<>();
        String likeVal = "%" + cmd.getKeyword() + "%";
        long startTime = System.currentTimeMillis();
        int namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null,
                (DSLContext context, Object reducingContext) -> {

                    SelectConditionStep<Record11<Long, String, Double, Double,Double, Double,String, Byte, String, String, String>> selectSql =
                            context.selectDistinct(Tables.EH_ADDRESSES.ID, Tables.EH_ADDRESSES.APARTMENT_NAME,
                            						Tables.EH_ADDRESSES.AREA_SIZE,Tables.EH_ADDRESSES.FREE_AREA,
                            						Tables.EH_ADDRESSES.CHARGE_AREA,Tables.EH_ADDRESSES.RENT_AREA,
                            						Tables.EH_ADDRESSES.APARTMENT_FLOOR, Tables.EH_ADDRESSES.LIVING_STATUS,
                            						Tables.EH_ADDRESSES.ORIENTATION,Tables.EH_ADDRESSES.NAMESPACE_ADDRESS_TOKEN,
                            						Tables.EH_ADDRESSES.NAMESPACE_ADDRESS_TYPE
                            						)
                                    .from(Tables.EH_ADDRESSES)
                                    .where(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(cmd.getCommunityId())
                                            .and(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
                                            .and(Tables.EH_ADDRESSES.BUILDING_NAME.equal(cmd.getBuildingName())
                                            .or(Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME.equal(cmd.getBuildingName()))
                                            .or(Tables.EH_ADDRESSES.BUILDING_ID.equal(cmd.getBuildingId()))
                                            )
                                            .and(Tables.EH_ADDRESSES.APARTMENT_NAME.like(likeVal))
                                            .and(Tables.EH_ADDRESSES.STATUS.equal(AddressAdminStatus.ACTIVE.getCode())))
                    						//不能显示未来房源
                    						.and(Tables.EH_ADDRESSES.IS_FUTURE_APARTMENT.equal((byte) 0));
                    if (cmd.getApartmentFloor() != null && !cmd.getApartmentFloor().isEmpty()) {
                        selectSql = selectSql.and(Tables.EH_ADDRESSES.APARTMENT_FLOOR.eq(cmd.getApartmentFloor()));
                    }
                    // living status没有在java文件和数据库中维护；
                    // 此处获得的living status不是直接返回前的living status，address表的living status并不是唯一性的资产的‘居住状态’的记录者，
                    // EH_ORGANIZATION_ADDRESS_MAPPINGS中的living status才是真的living status <- by wentian
//                    if(cmd.getLivingStatus() != null){
//                        selectSql = selectSql.and(Tables.EH_ADDRESSES.LIVING_STATUS.eq(cmd.getLivingStatus()));
//                    }

                    selectSql.fetch().map((r) -> {
                        ApartmentDTO apartment = new ApartmentDTO();
                        apartment.setAddressId(r.getValue(Tables.EH_ADDRESSES.ID));
                        apartment.setApartmentName(r.getValue(Tables.EH_ADDRESSES.APARTMENT_NAME));
                        apartment.setAreaSize(r.getValue(Tables.EH_ADDRESSES.AREA_SIZE));
                        apartment.setRentArea(r.getValue(Tables.EH_ADDRESSES.RENT_AREA));
                        apartment.setChargeArea(r.getValue(Tables.EH_ADDRESSES.CHARGE_AREA));
                        apartment.setFreeArea(r.getValue(Tables.EH_ADDRESSES.FREE_AREA));
                        apartment.setApartmentFloor(r.getValue(Tables.EH_ADDRESSES.APARTMENT_FLOOR));
                        apartment.setLivingStatus(r.getValue(Tables.EH_ADDRESSES.LIVING_STATUS));
                        apartment.setOrientation(r.getValue(Tables.EH_ADDRESSES.ORIENTATION));
                        apartment.setNamespaceAddressToken(r.getValue(Tables.EH_ADDRESSES.NAMESPACE_ADDRESS_TOKEN));
                        apartment.setNamespaceAddressType(r.getValue(Tables.EH_ADDRESSES.NAMESPACE_ADDRESS_TYPE));
                        results.add(apartment);
                        return null;
                    });
                    return true;
                });
        Collections.sort(results);
        long endTime = System.currentTimeMillis();
        LOGGER.info("List apartments by keyword,keyword=" + cmd.getKeyword() + ",elapse=" + (endTime - startTime));
        return new Tuple<Integer, List<ApartmentDTO>>(ErrorCodes.SUCCESS, results);
    }

    @Override
    public Tuple<Integer, List<ApartmentDTO>> listApartmentsByKeywordForBusiness(ListPropApartmentsByKeywordCommand cmd) {
        if (cmd.getCommunityId() == null || cmd.getBuildingName() == null || cmd.getBuildingName().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid communityId, buildingName or keyword parameter");
        if (cmd.getKeyword() == null)
            cmd.setKeyword("");
        List<ApartmentDTO> results = new ArrayList<>();
        String likeVal = "%" + cmd.getKeyword() + "%";
        long startTime = System.currentTimeMillis();
        int namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null,
                (DSLContext context, Object reducingContext) -> {

                    SelectConditionStep<Record5<Long, String, Double, String, String>> selectSql =
                            context.selectDistinct(Tables.EH_ADDRESSES.ID, Tables.EH_ADDRESSES.APARTMENT_NAME, Tables.EH_ADDRESSES.AREA_SIZE, Tables.EH_ADDRESSES.BUSINESS_APARTMENT_NAME, Tables.EH_ADDRESSES.APARTMENT_FLOOR)
                                    .from(Tables.EH_ADDRESSES)
                                    .where(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(cmd.getCommunityId())
                                            .and(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
                                            .and(Tables.EH_ADDRESSES.BUILDING_NAME.equal(cmd.getBuildingName())
                                                    .or(Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME.equal(cmd.getBuildingName()))
                                                    .or(Tables.EH_ADDRESSES.BUSINESS_BUILDING_NAME.equal(cmd.getBuildingName())))
                                            .and(Tables.EH_ADDRESSES.APARTMENT_NAME.like(likeVal).or(Tables.EH_ADDRESSES.BUSINESS_APARTMENT_NAME.like(likeVal)))
                                            .and(Tables.EH_ADDRESSES.STATUS.equal(AddressAdminStatus.ACTIVE.getCode())));

                    if (cmd.getApartmentFloor() != null && !cmd.getApartmentFloor().isEmpty()) {
                        selectSql = selectSql.and(Tables.EH_ADDRESSES.APARTMENT_FLOOR.eq(cmd.getApartmentFloor()));
                    }

                    LOGGER.debug("listApartmentsByKeywordForBusiness sql = :",selectSql.getSQL());
                    LOGGER.debug("listApartmentsByKeywordForBusiness bindValues = :",selectSql.getBindValues());
                    selectSql.fetch().map((r) -> {
                        ApartmentDTO apartment = new ApartmentDTO();
                        apartment.setAddressId(r.getValue(Tables.EH_ADDRESSES.ID));
                        apartment.setApartmentName(r.getValue(Tables.EH_ADDRESSES.APARTMENT_NAME));
                        apartment.setBusinessApartmentName(r.getValue(Tables.EH_ADDRESSES.BUSINESS_APARTMENT_NAME));
                        apartment.setAreaSize(r.getValue(Tables.EH_ADDRESSES.AREA_SIZE));
                        apartment.setApartmentFloor(r.getValue(Tables.EH_ADDRESSES.APARTMENT_FLOOR));
                        results.add(apartment);
                        return null;
                    });

                    return true;
                });
        Collections.sort(results);
        long endTime = System.currentTimeMillis();
        LOGGER.info("List apartments by keyword,keyword=" + cmd.getKeyword() + ",elapse=" + (endTime - startTime));
        return new Tuple<Integer, List<ApartmentDTO>>(ErrorCodes.SUCCESS, results);
    }

    @Override
    public ListAddressByKeywordCommandResponse listAddressByKeyword(ListAddressByKeywordCommand cmd) {
        if (cmd.getCommunityId() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid communityId  or keyword parameter");

        List<AddressDTO> results = new ArrayList<>();
        final long pageSize = cmd.getPageSize() == null ? this.configurationProvider.getIntValue("pagination.page.size",
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();
        long pageOffset = cmd.getPageOffset() == null ? 1L : cmd.getPageOffset();
        long offset = PaginationHelper.offsetFromPageOffset(pageOffset, pageSize);
        Tuple<Integer, Long> targetShard = new Tuple<>(0, offset);
        // String likeVal = "%" + cmd.getKeyword() + "%";
        int namespaceId = UserContext.getCurrentNamespaceId(null);
        if (offset > 0) {
            final List<Long> countsInShards = new ArrayList<>();
            this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class),
                    null, (DSLContext context, Object reducingContext) -> {

                SelectQuery<Record1<Integer>> query = context.selectCount().from(Tables.EH_ADDRESSES)
                        .where(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(cmd.getCommunityId()))
                        .and(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
                        .and(Tables.EH_ADDRESSES.STATUS.equal(AddressAdminStatus.ACTIVE.getCode()))
                        .getQuery();

                if (cmd.getKeyword() != null && cmd.getKeyword().trim().length() > 0) {
                    query.addConditions(Tables.EH_ADDRESSES.APARTMENT_NAME.like(DSL.concat("%", cmd.getKeyword(), "%")));
                }
                Long count = query.fetchOne(0, Long.class);

                countsInShards.add(count);
                return true;
            });

            targetShard = PaginationHelper.offsetFallsAt(countsInShards, offset);
        }
        ListAddressByKeywordCommandResponse response = new ListAddressByKeywordCommandResponse();
        if (targetShard.first() < 0) {
            return response;
        }

        final int[] currentShard = new int[1];
        currentShard[0] = 0;
        final Tuple<Integer, Long> fallingShard = targetShard;

        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class),
                currentShard, (DSLContext context, Object reducingContext) -> {

            int[] current = (int[]) reducingContext;
            if (current[0] < fallingShard.first()) {
                current[0] += 1;
                return true;
            }

            long off = 0;
            if (current[0] == fallingShard.first()) {
                off = fallingShard.second();
            }

            SelectQuery<Record> query = context.select().from(Tables.EH_ADDRESSES)
                    .where(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(cmd.getCommunityId()))
                    .and(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
                    .and(Tables.EH_ADDRESSES.STATUS.equal(AddressAdminStatus.ACTIVE.getCode()))
                    .getQuery();

            if (cmd.getKeyword() != null && cmd.getKeyword().trim().length() > 0) {
                query.addConditions(Tables.EH_ADDRESSES.APARTMENT_NAME.like(DSL.concat("%", cmd.getKeyword(), "%")));
            }

            query.addLimit((int)off, (int)pageSize);
            List<AddressDTO> addressDTOS = query.fetchInto(AddressDTO.class);
            results.addAll(addressDTOS);

            /*context.select().from(Tables.EH_ADDRESSES)
            .where(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(cmd.getCommunityId()))
            // .and(Tables.EH_ADDRESSES.APARTMENT_NAME.like(likeVal))
            .and(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
            .and(Tables.EH_ADDRESSES.STATUS.equal(AddressAdminStatus.ACTIVE.getCode()))
            .limit((int) pageSize).offset((int) off)
            .fetch().map((r) -> {
                results.add(ConvertHelper.convert(r, AddressDTO.class));
                return null;
            });*/
            return true;
        });

        if (results.size() >= pageSize) {
            response.setNextPageOffset((int) pageOffset + 1);
        }
        response.setRequests(results);
        return response;
    }


    @Override
    public ClaimedAddressInfo claimAddress(ClaimAddressCommand cmd) {
        if (cmd.getCommunityId() == null || cmd.getBuildingName() == null || cmd.getBuildingName().isEmpty()
                || cmd.getApartmentName() == null || cmd.getApartmentName().isEmpty()) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid communityId, buildingName or appartmentName parameter");
        }

        if (cmd.getReplacedAddressId() == null) {
            return processNewAddressClaim(cmd);
        } else {
            ClaimedAddressInfo info = processNewAddressClaim(cmd);

            DisclaimAddressCommand disclaimCmd = new DisclaimAddressCommand();
            disclaimCmd.setAddressId(cmd.getReplacedAddressId());
            disclaimAddress(disclaimCmd);
            return info;
        }
    }

    @Override
    public FamilyDTO claimAddressV2(ClaimAddressCommand cmd) {
        if (cmd.getCommunityId() == null || cmd.getBuildingName() == null || cmd.getBuildingName().isEmpty()
                || cmd.getApartmentName() == null || cmd.getApartmentName().isEmpty()) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid communityId, buildingName or appartmentName parameter");
        }

        if (cmd.getHistoryId() != null) {
            UserGroupHistory history = this.userGroupHistoryProvider.getHistoryById(cmd.getHistoryId());
            if (null != history) {
                this.userGroupHistoryProvider.deleteUserGroupHistory(history);
            }
        }

        FamilyDTO familyDTO = processNewAddressClaimV2(cmd);
        autoApproveMember(familyDTO.getCommunityId(), familyDTO.getId(), familyDTO.getAddressId());

        if (cmd.getReplacedAddressId() != null) {
            DisclaimAddressCommand disclaimCmd = new DisclaimAddressCommand();
            disclaimCmd.setAddressId(cmd.getReplacedAddressId());
            disclaimAddress(disclaimCmd);
        }
        return familyDTO;
    }

    private void autoApproveMember(Long communityId, Long groupId, Long addressId) {
        if (communityId != null && groupId != null) {
            propertyMgrService.autoApprovalGroupMember(UserContext.current().getUser().getId(), communityId, groupId, addressId);
        }
    }

    public void disclaimAddress(DisclaimAddressCommand cmd) {
        if (cmd.getAddressId() == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid addressId parameter");
        }
        Address address = this.addressProvider.findAddressById(cmd.getAddressId());
        if (address == null) {
            throw RuntimeErrorException.errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_ADDRESS_NOT_EXIST,
                    "Invalid addressId parameter,address is not found");
        }

//        long uid = UserContext.current().getUser().getId();
//        List<UserGroup> userGroups = this.userProvide.listUserGroups(uid, GroupDiscriminator.FAMILY.getCode());
//        userGroups = userGroups.stream().filter((userGroup)-> {
//            Group group = this.groupProvider.findGroupById(userGroup.getGroupId());
//            if(group != null){
//                return group.getIntegralTag1().longValue() == cmd.getApartmentId().longValue();
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
        if (family == null) {
            throw RuntimeErrorException.errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_ADDRESS_NOT_EXIST,
                    "Invalid addressId parameter,address is not found");
        }
        LeaveFamilyCommand leaveCmd = new LeaveFamilyCommand();
        leaveCmd.setId(family.getId());
        familyService.leave(leaveCmd, null);

        // 修改用户对应的客户资料认证状态  add by xq.tian  20160923
        propertyMgrService.updateOrganizationOwnerAddressAuthType(null, family.getCommunityId(), family.getAddressId(),
                OrganizationOwnerAddressAuthType.INACTIVE);
    }

    private ClaimedAddressInfo processNewAddressClaim(ClaimAddressCommand cmd) {
        Address address = this.getOrCreateAddress(cmd);
        Family family = this.familyService.getOrCreatefamily(address, null);

        ClaimedAddressInfo info = new ClaimedAddressInfo();
        info.setAddressId(address.getId());
        info.setFullAddress(address.getAddress());
        info.setUserCount((int) family.getMemberCount().longValue());
        return info;
    }

    private FamilyDTO processNewAddressClaimV2(ClaimAddressCommand cmd) {

        Address address = this.getOrCreateAddress(cmd);
        Family family = this.familyService.getOrCreatefamily(address, null);
        FamilyDTO familyDTO = ConvertHelper.convert(family, FamilyDTO.class);
        long communityId = family.getIntegralTag2();
        Community community = this.communityProvider.findCommunityById(communityId);
        if (community != null) {

            familyDTO.setCommunityId(communityId);
            familyDTO.setCommunityName(community.getName());
            familyDTO.setCityId(community.getCityId());
            familyDTO.setCityName(community.getCityName());

            familyDTO.setAreaId(community.getAreaId());
            familyDTO.setAreaName(community.getAreaName());
            familyDTO.setCommunityType(community.getCommunityType());
        }
        familyDTO.setMembershipStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
        if (address != null) {
            familyDTO.setBuildingName(address.getBuildingName());
            familyDTO.setApartmentName(address.getApartmentName());
            familyDTO.setAddressStatus(address.getStatus());
            String addrStr = FamilyUtils.joinDisplayName(community.getCityName(), community.getAreaName(), community.getName(),
                    address.getBuildingName(), address.getApartmentName());
            familyDTO.setDisplayName(addrStr);
            familyDTO.setAddress(addrStr);
        }
        return familyDTO;
    }

    private Address getOrCreateAddress(ClaimAddressCommand cmd) {
        Address address = null;

        Integer namespaceId = UserContext.getCurrentNamespaceId(UserContext.current().getNamespaceId());
        if (null == address) {
            address = this.addressProvider.findApartmentAddress(namespaceId, cmd.getCommunityId(),
                    cmd.getBuildingName(), cmd.getApartmentName());
        }

        Community community = this.communityProvider.findCommunityById(cmd.getCommunityId());
        if (community == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid communityId");
        User user = UserContext.current().getUser();
        long userId = user.getId();
        if (address == null) {
            // optimize with double-lock pattern 
            Tuple<Address, Boolean> result = this.coordinationProvider
                    .getNamedLock(CoordinationLocks.CREATE_ADDRESS.getCode()).enter(() -> {
                        long lqStartTime = System.currentTimeMillis();
                        Address addr = this.addressProvider.findApartmentAddress(namespaceId, cmd.getCommunityId(),
                                cmd.getBuildingName(), cmd.getApartmentName());
                        long lqEndTime = System.currentTimeMillis();
                        LOGGER.info("find address ,in the lock,elapse=" + (lqEndTime - lqStartTime));
                        long lcStartTime = System.currentTimeMillis();
                        if (addr == null) {
                            addr = new Address();
                            addr.setCityId(community.getCityId());
                            addr.setCityName(community.getCityName());
                            addr.setAreaId(community.getAreaId());
                            addr.setAreaName(community.getAreaName());
                            addr.setCommunityId(cmd.getCommunityId());
                            addr.setBuildingName(cmd.getBuildingName());
                            addr.setApartmentName(cmd.getApartmentName());
                            addr.setAddress(joinAddrStr(cmd.getBuildingName(), cmd.getApartmentName()));
                            addr.setApartmentFloor(parserApartmentFloor(cmd.getApartmentName()));
                            addr.setStatus(AddressAdminStatus.CONFIRMING.getCode());
                            addr.setCreatorUid(userId);
                            addr.setNamespaceId(namespaceId);
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
        if (cmd == null || cmd.getCommunityId() == 0)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid communityId parameter");

        final List<Address> results = new ArrayList<>();
        final long pageSize = cmd.getPageSize() == null ? this.configurationProvider.getIntValue("pagination.page.size",
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();
        long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset(), pageSize);
        int namespaceId = (UserContext.current().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getNamespaceId();
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null,
                (DSLContext context, Object reducingContext) -> {

                    context.select().from(Tables.EH_ADDRESSES)
                            .where(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(cmd.getCommunityId()))
                            .and(Tables.EH_ADDRESSES.NAMESPACE_ID.eq(namespaceId))
                            .limit((int) pageSize).offset((int) offset)
                            .fetch().map((r) -> {
                        results.add(ConvertHelper.convert(r, Address.class));
                        return null;
                    });

                    return true;
                });

        return new Tuple<>(ErrorCodes.SUCCESS, results);
    }


    private String joinAddrStr(String buildingName, String apartName) {
        boolean isFirst = true;
        StringBuilder strBuilder = new StringBuilder();
        if (!StringUtils.isEmpty(buildingName)) {
            if (!isFirst) {
                strBuilder.append("-");
            }
            strBuilder.append(buildingName);
            isFirst = false;
        }

        if (!StringUtils.isEmpty(apartName)) {
            if (!isFirst) {
                strBuilder.append("-");
            }
            strBuilder.append(apartName);
            isFirst = false;
        }
        return strBuilder.toString();
    }

    @Override
    public void correctAddress(CorrectAddressAdminCommand cmd) {

        if (cmd.getCommunityId() == null || cmd.getAddressId() == null
                || cmd.getBuildingName() == null || cmd.getApartmentName() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid communityId or addressId or buildingName or apratment parameter");
        Address address = this.addressProvider.findAddressById(cmd.getAddressId());
        if (address == null) {
            LOGGER.error("Invalid addressId parameter,address is not found.addressId=" + cmd.getAddressId());
            throw RuntimeErrorException.errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_ADDRESS_NOT_EXIST,
                    "Address is not found.");
        }
        User user = UserContext.current().getUser();
        long userId = user.getId();
        long startTime = System.currentTimeMillis();
        Address addr = this.addressProvider.findApartmentAddress(UserContext.getCurrentNamespaceId(UserContext.current().getNamespaceId()), cmd.getCommunityId(), cmd.getBuildingName(), cmd.getApartmentName());
        this.dbProvider.execute((TransactionStatus status) -> {
            Family family = this.familyProvider.findFamilyByAddressId(cmd.getAddressId());
            if (family == null) {
                LOGGER.error("Family is not found with addressId=" + cmd.getAddressId());
                return null;
            }
            long addrId = 0L;
            Group group = ConvertHelper.convert(family, Group.class);
            if (addr == null || addr.getId().longValue() == address.getId().longValue()) {
                address.setBuildingName(cmd.getBuildingName());
                address.setApartmentName(cmd.getApartmentName());
                address.setAddress(joinAddrStr(cmd.getBuildingName(), cmd.getApartmentName()));
                address.setApartmentFloor(parserApartmentFloor(cmd.getApartmentName()));
                address.setStatus(AddressAdminStatus.ACTIVE.getCode());
                address.setOperatorUid(userId);
                address.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                this.addressProvider.updateAddress(address);

                addrId = address.getId();
                Community community = this.communityProvider.findCommunityById(cmd.getCommunityId());
                if (community != null) {
                    community.setAptCount(community.getAptCount() + 1);
                    this.communityProvider.updateCommunity(community);
                }

            } else {
                //this.familyService.approveMembersByFamily(family);
                this.addressProvider.deleteAddress(address);
                addrId = addr.getId();
            }
            group.setIntegralTag1(addrId);
            this.groupProvider.updateGroup(group);

            return null;
        });
        long endTime = System.currentTimeMillis();
        LOGGER.info("Correct address elapse=" + (endTime - startTime));

    }

    private String parserApartmentFloor(String apartmentName) {
        if (StringUtils.isEmpty(apartmentName)) return null;

        if (apartmentName.length() <= 2)
            return "1";
        else if (apartmentName.length() > 2 &&
                apartmentName.startsWith(apartmentName.substring(0, apartmentName.length() - 2))) {
            return apartmentName.substring(0, apartmentName.length() - 2);
        }
        return null;
    }

    /**
     * 搜索小区
     * @param cmd
     * @return
     */
    @Override
    public List<CommunityDoc> searchCommunities(SearchCommunityCommand cmd) {
        /*if (cmd.getKeyword() == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid keyword paramter.");
        }*/
        int pageNum = cmd.getPageOffset() == null ? 1 : cmd.getPageOffset();
        final int pageSize = cmd.getPageSize() == null ? this.configurationProvider.getIntValue("pagination.page.size",
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();

        return communitySearcher.searchDocs(cmd.getKeyword(), cmd.getCommunityType(), cmd.getCityId(), cmd.getRegionId(), pageNum - 1, pageSize);
    }

    private void checkUserPrivilege(long userId, long communityId) {
        boolean flag = false;
        if (userId == 1) //超级管理员
            return;
        List<FamilyDTO> familydtos = this.familyProvider.getUserFamiliesByUserId(userId);
        if (familydtos == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                    "User has not living in community.");
        }
        for (FamilyDTO family : familydtos) {
            if (family.getCommunityId().longValue() == communityId) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                    "User has not living in community.");
        }

    }

    @Override
    public ListApartmentByBuildingNameCommandResponse listApartmentsByBuildingName(ListApartmentByBuildingNameCommand cmd) {
        if (cmd.getCommunityId() == null || cmd.getBuildingName() == null || cmd.getBuildingName().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid communityId, buildingName parameter");

        User user = UserContext.current().getUser();
        long userId = cmd.getUserId()==null?user.getId():cmd.getUserId();

        checkUserPrivilege(userId, cmd.getCommunityId());

        List<ApartmentDTO> results = new ArrayList<ApartmentDTO>();
        int pageOffset = cmd.getPageOffset() == null ? 1 : cmd.getPageOffset();
        int pageSize = cmd.getPageSize() == null ? this.configurationProvider.getIntValue("pagination.page.size",
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, (long) pageSize);
        ListApartmentByBuildingNameCommandResponse response = new ListApartmentByBuildingNameCommandResponse();

        int totalCount = this.addressProvider.countApartmentsByBuildingName(cmd.getCommunityId(), cmd.getBuildingName());
//        if (totalCount == 0) return response;

        List<ApartmentDTO> list = this.addressProvider.listApartmentsByBuildingName(cmd.getCommunityId(),
                cmd.getBuildingName(), offset, pageSize);
        list.stream().map((r) -> {
            Family family = this.familyProvider.findFamilyByAddressId(r.getAddressId());
            if (family != null && family.getMemberCount() > 0) {
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

    @Override
    public ListApartmentByBuildingNameCommandResponse listCommunityApartmentsByBuildingName(ListApartmentByBuildingNameCommand cmd) {
        if (cmd.getCommunityId() == null || cmd.getBuildingName() == null || cmd.getBuildingName().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid communityId, buildingName parameter");

        int pageOffset = cmd.getPageOffset() == null ? 1 : cmd.getPageOffset();
        int pageSize = cmd.getPageSize() == null ? this.configurationProvider.getIntValue("pagination.page.size",
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, (long) pageSize);
        ListApartmentByBuildingNameCommandResponse response = new ListApartmentByBuildingNameCommandResponse();

        List<ApartmentDTO> list = this.addressProvider.listApartmentsByBuildingName(cmd.getCommunityId(),
                cmd.getBuildingName(), offset, pageSize);
        response.setApartmentList(list);
        return response;
    }

    private static void sortApartment(List<ApartmentDTO> results) {
        if (results == null || results.isEmpty())
            return;
        String regexNum = "^\\d+$";
        String regexNumChar = "^(\\d+)([a-zA-Z]+)$";
        String regexNumCharNum = "^\\d+[A-Za-z]+$";
        Pattern patternNum = Pattern.compile(regexNum);
        Pattern patternNumChar = Pattern.compile(regexNumChar);
        Pattern patternNumCharNum = Pattern.compile(regexNumCharNum);
        String apartmentName = results.get(0).getApartmentName();
        try {
            if (patternNum.matcher(apartmentName).matches()) {
                results.sort(new Comparator<ApartmentDTO>() {
                    @Override
                    public int compare(ApartmentDTO o1, ApartmentDTO o2) {
                        return Integer.valueOf(o1.getApartmentName()) - Integer.valueOf(o2.getApartmentName());
                    }
                });
            } else if (patternNumChar.matcher(apartmentName).matches()) {
                results.sort(new Comparator<ApartmentDTO>() {
                    @Override
                    public int compare(ApartmentDTO o1, ApartmentDTO o2) {

                        String o1_num = getGroupValue(o1.getApartmentName(), 1);
                        String o2_num = getGroupValue(o2.getApartmentName(), 1);
                        if ((Integer.valueOf(o1_num) - Integer.valueOf(o2_num)) == 0) {
                            String o1_c = getGroupValue(o1.getApartmentName(), 2);
                            String o2_c = getGroupValue(o2.getApartmentName(), 2);
                            return o1_c.compareTo(o2_c);
                        }
                        return Integer.valueOf(o1_num) - Integer.valueOf(o2_num);
                    }
                });

            } else if (patternNumCharNum.matcher(apartmentName).matches()) {

            }

        } catch (NumberFormatException e) {
            LOGGER.error("Number format is error." + e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Format apartment is error." + e);
        }

    }

    private static String getGroupValue(String apartmentName, int index) {
        Matcher m = Pattern.compile("^(\\d+)([a-zA-Z]+)$").matcher(apartmentName);
        m.find();
        return m.group(index);
    }

    @Override
    public UserServiceAddressDTO createServiceAddress(CreateServiceAddressCommand cmd) {
        if (cmd.getRegionId() == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid regionId, regionId parameter");
        }
        if (cmd.getAddress() == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid address, address parameter");
        }
        if (cmd.getContactName() == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid contactName, contactName parameter");
        }
        if (cmd.getContactToken() == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid contactToken, contactToken parameter");
        }
        Region region = this.regionProvider.findRegionById(cmd.getRegionId());
        if (region == null) {
            throw RuntimeErrorException.errorWith(RegionServiceErrorCode.SCOPE, RegionServiceErrorCode.ERROR_REGION_NOT_EXIST,
                    "Region is not found");
        }
        long cityId = 0;
        String cityName = null;
        long areaId = 0;
        String areaName = null;
        if (region.getScopeCode().byteValue() == RegionScope.CITY.getCode()) {
            cityId = region.getId();
            cityName = region.getName();
        } else if (region.getScopeCode().byteValue() == RegionScope.AREA.getCode()) {
            Region city = this.regionProvider.findRegionById(region.getParentId());
            if (city == null)
                throw RuntimeErrorException.errorWith(RegionServiceErrorCode.SCOPE, RegionServiceErrorCode.ERROR_REGION_NOT_EXIST,
                        "Region is not found");
            cityId = city.getId();
            cityName = city.getName();
            areaId = region.getId();
            areaName = region.getName();
        }

        User user = UserContext.current().getUser();
        long userId = user.getId();

        Address address = this.addressProvider.findAddressByRegionAndAddress(cityId, areaId, cmd.getAddress());

        if (address == null) {
            final long fcityId = cityId;
            final long fareaId = areaId;
            final String fcityName = cityName;
            final String fareaName = areaName;
            // optimize with double-lock pattern 
            Tuple<Address, Boolean> result = this.coordinationProvider
                    .getNamedLock(CoordinationLocks.CREATE_ADDRESS.getCode()).enter(() -> {
                        Address addr = this.addressProvider.findAddressByRegionAndAddress(fcityId, fareaId, cmd.getAddress());
                        ;
                        long lcStartTime = System.currentTimeMillis();
                        if (addr == null) {
                            addr = new Address();
                            addr.setCityId(fcityId);
                            addr.setCityName(fcityName);
                            addr.setAreaId(fareaId);
                            addr.setAreaName(fareaName);
                            addr.setCommunityId(cmd.getCommunityId());
                            addr.setAddress(cmd.getAddress());
                            addr.setAddressAlias(cmd.getAddress());
                            addr.setStatus(AddressAdminStatus.ACTIVE.getCode());
                            addr.setCreatorUid(userId);
                            this.addressProvider.createAddress(addr);

                        }
                        long lcEndTime = System.currentTimeMillis();
                        LOGGER.info("create address in the lock,elapse=" + (lcEndTime - lcStartTime));
                        return addr;
                    });

            address = result.first();
        }

        //insert user_service_addresses
        UserServiceAddress serviceAddress = new UserServiceAddress();
        serviceAddress.setAddressId(address.getId());
        serviceAddress.setOwnerUid(userId);
        serviceAddress.setCreatorUid(userId);
        serviceAddress.setContactName(cmd.getContactName());
        serviceAddress.setContactToken(cmd.getContactToken());
        serviceAddress.setContactType(cmd.getContactType());
        serviceAddress.setStatus((byte) 2);

        this.userActivityProvider.addUserServiceAddress(serviceAddress);

        UserServiceAddressDTO dto = new UserServiceAddressDTO();
        dto.setId(serviceAddress.getId());
        dto.setAddress(cmd.getAddress());
        dto.setCity(cityName);
        dto.setArea(areaName);
        dto.setUserName(cmd.getContactName());
        dto.setCallPhone(cmd.getContactToken());
        return dto;
    }


    @Override
    public void deleteServiceAddress(DeleteServiceAddressCommand cmd) {
        if (cmd.getId() == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid address id, id parameter");
        }
        Address address = this.addressProvider.findAddressById(cmd.getId());
        if (address == null) {
            throw RuntimeErrorException.errorWith(AddressServiceErrorCode.SCOPE, AddressServiceErrorCode.ERROR_ADDRESS_NOT_EXIST,
                    "Address is not exists");
        }
        User user = UserContext.current().getUser();

        this.userActivityProvider.deleteUserServieAddress(address.getId(), user.getId());
        this.addressProvider.deleteAddress(address);
    }

    @Override
    public void importCommunityInfos(MultipartFile[] files) {
        long startTime = System.currentTimeMillis();
        User user = UserContext.current().getUser();
        List<Region> cityList = regionProvider.listRegions(UserContext.getCurrentNamespaceId(UserContext.current().getNamespaceId()), RegionScope.CITY, RegionAdminStatus.ACTIVE, null);
        List<Region> areaList = regionProvider.listRegions(UserContext.getCurrentNamespaceId(UserContext.current().getNamespaceId()), RegionScope.AREA, RegionAdminStatus.ACTIVE, null);

        try {
            String seperator = configurationProvider.getValue(ADMIN_IMPORT_DATA_SEPERATOR, "#@");
            List<String[]> dataList = FileHelper.getDataArrayByInputStream(files[0].getInputStream(), seperator);
            List<CommunityDataInfo> communityList = DataFileHandler.getComunityDataByFile(dataList, cityList, areaList, user);

            if (communityList != null && communityList.size() > 0) {
                //事务原子操作。1,导入小区和小区点经纬度  2,导入小区物业条目
                txImportCommunity(user, communityList);
            } else {
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE,
                        "can not to import the community.parse file error.");
            }
            long endTime = System.currentTimeMillis();
            LOGGER.info("success to import address ,elapse=" + (endTime - startTime));
        } catch (IOException e) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE,
                    "can not to import the community.io error.");
        }
    }

    private void txImportCommunity(User user, List<CommunityDataInfo> communityList) {

        // 导入小区信息 和小区坐标
        importCommunityInfo(user, communityList);

        // 导入小区物业条目
        importCommunityProperty(user, communityList);

        //生成小区时，创建小区的索引
        List<Community> communities = createCommunities(communityList);
        communitySearcher.bulkUpdate(communities);

    }

    private List<Community> createCommunities(List<CommunityDataInfo> communityList) {
        List<Community> communities = new ArrayList<Community>();
        if (communityList != null && communityList.size() > 0) {
            for (CommunityDataInfo communityDataInfo : communityList) {
                Community community = DataFileHandler.getCommunityByData(communityDataInfo);
                communities.add(community);
            }
        }
        return communities;
    }

    private void importCommunityProperty(User user, List<CommunityDataInfo> communityList) {
        for (CommunityDataInfo communityDataInfo : communityList) {
            List<CommunityPmContact> contacts = DataFileHandler.getPropItemsByData(user, communityDataInfo);
            if (contacts != null && contacts.size() > 0) {
                for (CommunityPmContact communityPmContact : contacts) {
                    propertyMgrProvider.createPropContact(communityPmContact);
                }
            }
        }
    }

    private void importCommunityInfo(User user, List<CommunityDataInfo> communityList) {
        this.dbProvider.execute((TransactionStatus status) -> {
            for (int i = 0; i < communityList.size(); i++) {
                CommunityDataInfo communityDataInfo = communityList.get(i);
                Community community = DataFileHandler.getCommunityByData(communityDataInfo);
                CommunityGeoPoint communityGeoPoint = DataFileHandler.getCommunityPosByData(communityDataInfo);
                if (!isCommunityExit(community.getAreaId(), community.getName())) {
                    communityProvider.createCommunity(user.getId(), community);
                    communityGeoPoint.setCommunityId(community.getId());
                    communityProvider.createCommunityGeoPoint(communityGeoPoint);
                    communityDataInfo.setId(community.getId());
                } else {
                    LOGGER.error("community  duplicate .community = " + community);
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE,
                            "the community is existed.");
                }
            }
            return null;
        });

    }

    // 判断小区是否重复 : 和数据库中已存在的小区比较 重复返回true 不重复返回false
    public boolean isCommunityExit(long areaId, String communityName) {
        Community community = communityProvider.findCommunityByAreaIdAndName(areaId, communityName);
        if (community != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void importAddressInfos(MultipartFile[] files) {
        long startTime = System.currentTimeMillis();

        User user = UserContext.current().getUser();
        List<Region> cityList = regionProvider.listRegions(UserContext.getCurrentNamespaceId(UserContext.current().getNamespaceId()), RegionScope.CITY, RegionAdminStatus.ACTIVE, null);
        List<Region> areaList = regionProvider.listRegions(UserContext.getCurrentNamespaceId(UserContext.current().getNamespaceId()), RegionScope.AREA, RegionAdminStatus.ACTIVE, null);


        try {
            String seperator = configurationProvider.getValue(ADMIN_IMPORT_DATA_SEPERATOR, "#@");
            List<String[]> dataList = FileHelper.getDataArrayByInputStream(files[0].getInputStream(), seperator);

            if (dataList != null && dataList.size() > 0) {
                //事务原子操作。1,导入小区和小区点经纬度  2,导入小区物业条目
                txImportAddress(user, cityList, areaList, dataList);
            } else {
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

    private void txImportAddress(User user, List<Region> cityList, List<Region> areaList, List<String[]> dataList) {
        List<Address> addressList = new ArrayList<Address>();
        int maxCount = configurationProvider.getIntValue(ADMIN_IMPORT_ADDRESS_ALLOW_MAX_COUNT, 0);

        // 缓存list 匹配查找小区时 先从缓存list匹配小区 如果没有，再去数据库查找。然后加入到缓存list
        List<Community> bufferCommunityList = new ArrayList<Community>(2048);
        // 缓存map 查询小区公寓总数时 先从map中取小区id对应的总数 如果没有再去数据库查找。然后加入到缓存map 确保
        // 每个小区的总数只查一次。
        Map<Long, Integer> bufferAptCountMap = new HashMap<Long, Integer>();
        // 查询小区公寓的标记状态 先从map中取小区id对应的以前公寓标记状态 如果是未标记状态 则先将以前所有小区标记
        // 可能冲突重复状态 然后加入到缓存map
        Map<Long, Boolean> flagMap = new HashMap<Long, Boolean>();// 未标记状态。
        // 每个小区只需要操作一次
        for (String[] rowDatas : dataList) {
            if (rowDatas.length == DataProcessConstants.DATA_LENGTH_ZISE_APARTMENT) {
                AddressDataInfo address = DataFileHandler.convertAddressDataByRow(rowDatas);
                Community community = getCommunityByList(address, bufferCommunityList, cityList, areaList);
                CommunityGeoPoint communityGeoPoint = communityProvider.findCommunityGeoPointById(community.getId());
                Address ne = DataFileHandler.convertNEDBModelDataInfo(community, communityGeoPoint, address, user);

                // 先查询该小区已存在的公寓总数。新增的小区公寓 理论上应该最多只可能有很少量的用户填写的公寓门牌号。
                // 总数如果小于正常值（在系统配置项中配置）。1：标记已存在的公寓
                // 为可能重复。需要人工去重。只进行一次操作 2：添加公寓ne
                // 总数如果大于正常值。肯定有问题： 第一，可能一部分公寓已经导入。 第二。小区公寓以前导过。
                // 则不属于新增操作。移至补充公寓。不进行数据库操作。抛出异常给据数组审核。
                long communityId = community.getId();
                int aptCount = getAptCountByCommunityId(community, bufferAptCountMap);
                if (aptCount <= maxCount) {
                    if (aptCount != 0) {
                        if (flagMap.containsKey(communityId)) {
                            // 标记已存在的公寓 为可能重复
                            // 标记公寓： 应该是每一小区 只标记一次 map《id，flag》
                            // 1:map中包含该小区且 标记为false
                            // 标记并将false设置为true
                            // 2：map中 不包含该小区 标记并存入map中
                            if (!flagMap.get(communityId)) {
//								updateAptConflictByCommunityId(communityId);
                                flagMap.put(communityId, true);
                            }
                        } else {
//							updateAptConflictByCommunityId(communityId);
                            flagMap.put(communityId, true);
                        }
                    }
                    addressList.add(ne);
                } else {
                    LOGGER.error("community apt is too much.community = " + community + ",aptCount = " + aptCount);
                    throw new RuntimeErrorException(community.getName() + "已经有 " + aptCount + "个公寓了。请请检查是否重复导入，并与后台人员联系核实。");
                }
            } else {
                LOGGER.error("data format is not correct.rowDatas = " + Arrays.toString(rowDatas));
                throw new RuntimeErrorException("公寓文件格式不正确。每行共" + DataProcessConstants.DATA_LENGTH_ZISE_APARTMENT + "项。数据 = " + Arrays.toString(rowDatas));
            }
        }

        txImportAddressList(addressList);

        //处理导入公寓后的小区公寓数。
        if (bufferCommunityList != null && bufferCommunityList.size() > 0) {
            for (Community community : bufferCommunityList) {
                long communityId = community.getId();
//				int aptCount = countAptByCommunityId(communityId);
//				communityService.updateCommunityAptcountByIdAndCount(aptCount, communityId);
            }
        }

    }

    public void txImportAddressList(List<Address> addressList) {
        this.dbProvider.execute((TransactionStatus status) -> {
            long startTime = System.currentTimeMillis();
            LOGGER.info("======count=" + addressList.size() + ",startTime=" + startTime);
//			for (Address address : addressList) {
//				addressProvider.createAddress(address);
//			}
            Long stime = System.currentTimeMillis();
            Long etime = System.currentTimeMillis();
            for (int i = 0; i < addressList.size(); i++) {
                if (i % 100 == 0 && i != 0) {
                    addressProvider.createAddress2(addressList.get(i));
                    etime = System.currentTimeMillis();
                    LOGGER.info("$$ successed to insert " + (i / 100) + "个100行。time=======" + (etime - stime));
                    stime = etime;
                } else {
                    addressProvider.createAddress(addressList.get(i));
                }
            }
            long endTime = System.currentTimeMillis();
            LOGGER.info("success.======count=" + addressList.size() + ",totalTime=" + (endTime - startTime));
            return null;
        });
    }

    public Community getCommunityByList(AddressDataInfo address, List<Community> bufferCommunityList,
                                        List<Region> cityList, List<Region> areaList) {

        Region city = DataFileHandler.getCityByData(address.getCityName(), cityList);
        if (city == null) {
            throw new RuntimeErrorException("数据文件的城市名字不正确。小区 = " + address.getCityName() + "\t"
                    + address.getAreaName() + "\t" + address.getCommunityName());
        }

        Region area = DataFileHandler.getAreaByData(city.getId(), address.getAreaName(), areaList);
        if (area == null) {
            throw new RuntimeErrorException("数据文件的区县名字不正确。小区 = " + address.getCityName() + "\t"
                    + address.getAreaName() + "\t" + address.getCommunityName());
        }
        address.setAreaId(area.getId());
        Community community = null;
        if (address != null) {
            Long areaId = address.getAreaId();
            String communityName = address.getCommunityName();
            for (Community bufferCommunity : bufferCommunityList) {
                if (areaId == bufferCommunity.getAreaId() && communityName.equals(bufferCommunity.getName())) {
                    community = bufferCommunity;
                    break;
                }
            }
            if (community == null) {
                try {
                    community = communityProvider.findCommunityByAreaIdAndName(areaId, communityName);
                } catch (Exception e) {
                    LOGGER.error("community duplicate . areaId = " + areaId + " , communityName =" + communityName, e);
                    throw new RuntimeErrorException("小区重复 . 区县id = " + areaId + " , 区县名字 =" + area.getName()
                            + " , 小区名字 =" + communityName);
                }
                if (community == null) {
                    throw new RuntimeErrorException("数据文件的小区未找到。小区 = " + address.getAreaName() + "\t"
                            + address.getCommunityName());
                }
                bufferCommunityList.add(community);
            }
        }
        return community;
    }

    public int getAptCountByCommunityId(Community community, Map<Long, Integer> map) {
        int count = 0;
        Long communityId = community.getId();
        if (map.containsKey(communityId)) {
            count = map.get(communityId);
        } else {
            count = community.getAptCount() == null ? 0 : community.getAptCount();  //countAptByCommunityId(communityId);
            map.put(communityId, count);
        }
        return count;
    }

    @Override
    public List<ApartmentDTO> listUnassignedApartmentsByBuildingName(
            ListApartmentByBuildingNameCommand cmd) {

        List<ApartmentDTO> results = new ArrayList<ApartmentDTO>();
        int pageOffset = cmd.getPageOffset() == null ? 1 : cmd.getPageOffset();
        int pageSize = cmd.getPageSize() == null ? this.configurationProvider.getIntValue("pagination.page.size",
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, (long) pageSize);

        List<ApartmentDTO> list = this.addressProvider.listApartmentsByBuildingName(cmd.getCommunityId(),
                cmd.getBuildingName(), offset, pageSize);

        list.stream().map((r) -> {
            r.setLivingStatus(AddressLivingStatus.INACTIVE.getCode());
            Family family = this.familyProvider.findFamilyByAddressId(r.getAddressId());
            if (family != null && family.getMemberCount() > 0) {
                r.setLivingStatus(AddressLivingStatus.ACTIVE.getCode());
                r.setFamilyId(family.getId());
            }

            Enterprise enterprise = this.enterpriseProvider.findEnterpriseByAddressId(r.getAddressId());
            if (enterprise != null && enterprise.getId() != null) {
                r.setLivingStatus(AddressLivingStatus.ACTIVE.getCode());
            }

            if (r.getLivingStatus() == AddressLivingStatus.INACTIVE.getCode()) {
                results.add(r);
            }
            return null;
        }).collect(Collectors.toList());
        sortApartment(results);
        return results;
    }

    @Override
    public void importParkAddressData(ImportAddressCommand cmd,
                                      MultipartFile[] files) {
        long communityId = cmd.getCommunityId();

        if (org.springframework.util.StringUtils.isEmpty(files) || files.length == 0) {
            LOGGER.error("File content is empty。");
            throw new RuntimeErrorException("File content is empty。");
        }

        try {
            List list = PropMrgOwnerHandler.processorExcel(files[0].getInputStream());

            List<String[]> datas = this.convertToParkDatas(list);

            Community community = communityProvider.findCommunityById(communityId);

            this.batchAddAddresses(community, datas);

            updateCommunityAptCount(community);
        } catch (Exception e) {
            throw new RuntimeErrorException("File parsing error", e);
        }

    }

    @Override
	public Object importParkAddressData(ImportAddressCommand cmd, MultipartFile file) {
    	Long userId = UserContext.current().getUser().getId();
    	ImportFileTask task = new ImportFileTask();
		try {
			//解析excel
			List resultList = PropMrgOwnerHandler.processorExcel(file.getInputStream());

			if(null == resultList || resultList.isEmpty()){
				LOGGER.error("File content is empty.userId="+userId);
				throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_FILE_IS_EMPTY,
						"File content is empty");
			}
			task.setOwnerType(EntityType.COMMUNITY.getCode());
			task.setOwnerId(cmd.getCommunityId());
			task.setType(ImportFileTaskType.APARTMENT.getCode());
			task.setCreatorUid(userId);
			task = importFileService.executeTask(() -> {
				ImportFileResponse response = new ImportFileResponse();
				List<ImportApartmentDataDTO> datas = handleImportApartmentData(resultList);
				if(datas.size() > 0){
					//设置导出报错的结果excel的标题
					response.setTitle(datas.get(0));
					datas.remove(0);
				}
				List<ImportFileResultLog<ImportApartmentDataDTO>> results = importApartment(datas, userId, cmd);
				response.setTotalCount((long)datas.size());
				response.setFailCount((long)results.size());
				response.setLogs(results);
				return response;
			}, task);

		} catch (IOException e) {
			LOGGER.error("File can not be resolved...");
			e.printStackTrace();
		}
		return ConvertHelper.convert(task, ImportFileTaskDTO.class);
	}

    @Override
    public List<AddressIdAndName> findAddressByPossibleName(Integer currentNamespaceId, Long ownerId, String buildingName, String apartmentName) {
        return addressProvider.findAddressByPossibleName( currentNamespaceId,  ownerId,  buildingName,  apartmentName);
    }

    @Override
    public List<GetApartmentNameByBuildingNameDTO> getApartmentNameByBuildingName(GetApartmentNameByBuildingNameCommand cmd) {
        return addressProvider.getApartmentNameByBuildingName(cmd.getBuildingName(), cmd.getCommunityId(), cmd.getNamespaceId() == null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId());
    }
    @Override
    public ListNearbyMixCommunitiesCommandV2Response listNearbyMixCommunitiesV2(ListNearbyMixCommunitiesCommand cmd) {
        ListNearbyMixCommunitiesCommandV2Response resp = new ListNearbyMixCommunitiesCommandV2Response();

        if (cmd.getLatigtue() == null || cmd.getLongitude() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter, latitude and longitude have to be both specified or neigher");


        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0L);

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        ListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        int namespaceId = (UserContext.current().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getNamespaceId();
        if (namespaceId == 0) {
            ListNearbyMixCommunitiesCommandResponse resp_1 = listMixCommunitiesByDistance(cmd, locator, pageSize);
            resp.setDtos(resp_1.getDtos());
            return resp;
        } else {
            List<CommunityDTO> dots = new ArrayList<>();
            List<CommunityDTO> resudentials = new ArrayList<>();
            List<CommunityDTO> commercials = new ArrayList<>();
            if(cmd.getCommunityType() != null){
                dots = this.communityProvider.listCommunitiesByNamespaceId(cmd.getCommunityType(), namespaceId, locator, pageSize);
            }else{
                resudentials = this.communityProvider.listCommunitiesByNamespaceId(CommunityType.RESIDENTIAL.getCode(), namespaceId, locator, pageSize);
                commercials = this.communityProvider.listCommunitiesByNamespaceId(CommunityType.COMMERCIAL.getCode(), namespaceId, locator, pageSize);
            }

            if (dots != null)
                resp.setDtos(dots);
            if (resudentials != null)
                resp.setResudentials(resudentials);
            if (commercials != null)
                resp.setCommercials(commercials);

            return resp;
        }
    }

    @Override
    public ListNearbyMixCommunitiesCommandV2Response listPopularCommunitiesWithType(ListNearbyMixCommunitiesCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        ListingLocator locator = new ListingLocator();
        Integer pageSize = 40;
        if (cmd.getPageSize() != null) {
            pageSize = cmd.getPageSize();
        }
        List<Community> communities = new ArrayList<>();
        if (null != cmd.getCommunityType()) {
            communities = communityProvider.listCommunities(namespaceId, locator, 1000, new ListingQueryBuilderCallback() {
                @Override
                public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                    query.addConditions(Tables.EH_COMMUNITIES.COMMUNITY_TYPE.eq(cmd.getCommunityType()));
                    return query;
                }
            });
        } else {
            communities = communityProvider.listCommunities(namespaceId, locator, 1000, null);
        }

        List<CommunityDTO> dtos = communities.stream().map(r -> {
            Integer communityUserCount = 0;
            if (r.getCommunityType() == CommunityType.RESIDENTIAL.getCode()) {
                List<Group> groups = groupProvider.listGroupByCommunityId(r.getId(), (loc, query) -> {
                    Condition c = Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode());
                    query.addConditions(c);
                    return query;
                });

                List<Long> groupIds = new ArrayList<Long>();
                for (Group group : groups) {
                    groupIds.add(group.getId());
                }

                CrossShardListingLocator locator_g = new CrossShardListingLocator();
                List<GroupMember> groupMembers = groupProvider.listGroupMemberByGroupIds(groupIds, locator_g, null, (loc, query) -> {
                    Condition c = Tables.EH_GROUP_MEMBERS.MEMBER_TYPE.eq(EntityType.USER.getCode());
                    c = c.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.ne(GroupMemberStatus.INACTIVE.getCode()));
                    query.addConditions(c);
                    query.addGroupBy(Tables.EH_GROUP_MEMBERS.MEMBER_ID);
                    return query;
                });

                communityUserCount = groupMembers.size();

            } else {
                communityUserCount = organizationProvider.countUserOrganization(namespaceId, r.getId(), null);
            }

            CommunityDTO dto = ConvertHelper.convert(r, CommunityDTO.class);
            dto.setCommunityUserCount(communityUserCount);
            return dto;
        }).collect(Collectors.toList());

        if (dtos != null && dtos.size() > 0) {
            ListNearbyMixCommunitiesCommandV2Response response = new ListNearbyMixCommunitiesCommandV2Response();
            Collections.sort(dtos, new Comparator<CommunityDTO>() {
                public int compare(CommunityDTO arg0, CommunityDTO arg1) {
                    return arg1.getCommunityUserCount().compareTo(arg0.getCommunityUserCount());
                }
            });

            if (pageSize < dtos.size()) {
                dtos = dtos.subList(0, pageSize);
            }
            response.setDtos(dtos);
            return response;
        }
        return null;
    }

    private List<ImportFileResultLog<ImportApartmentDataDTO>> importApartment(List<ImportApartmentDataDTO> datas,Long userId, ImportAddressCommand cmd) {
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		Building building = communityProvider.findBuildingById(cmd.getBuildingId());

		List<ImportFileResultLog<ImportApartmentDataDTO>> errorLogs = new ArrayList<>(); 
		for (ImportApartmentDataDTO data : datas) {
			ImportFileResultLog<ImportApartmentDataDTO> log = new ImportFileResultLog<>(AddressServiceErrorCode.SCOPE);
			//校验excel填写内容
//			if (StringUtils.isEmpty(data.getBuildingName())) {
//				log.setData(data);
//				log.setErrorLog("building name is null");
//				log.setCode(AddressServiceErrorCode.ERROR_BUILDING_NAME_EMPTY);
//				errorLogs.add(log);
//				continue;
//			}
			
			if (StringUtils.isEmpty(data.getApartmentName())) {
				log.setData(data);
				log.setErrorLog("apartment name is null");
				log.setCode(AddressServiceErrorCode.ERROR_APARTMENT_NAME_EMPTY);
				errorLogs.add(log);
				continue;
			}
			if (StringUtils.isEmpty(data.getStatus())) {
				log.setData(data);
				log.setErrorLog("apartmentStatus is null");
				log.setCode(AddressServiceErrorCode.ERROR_APARTMENT_STATUS_EMPTY);
				errorLogs.add(log);
				continue;
			}
			
			if (StringUtils.isNotEmpty(data.getStatus())) {
				Byte livingStatus = AddressMappingStatus.fromDesc(data.getStatus()).getCode();
				if (livingStatus==-1) {
					log.setData(data);
					log.setErrorLog("apartmentStatus is error");
					log.setCode(AddressServiceErrorCode.ERROR_APARTMENT_STATUS_TYPE);
					errorLogs.add(log);
					continue;
				}
			}
            /*Address address = addressProvider.findActiveAddressByBuildingApartmentName(community.getNamespaceId(), community.getId(), data.getBuildingName(), data.getApartmentName());
			if(address != null) {
                log.setData(data);
                log.setErrorLog("apartment name is exist in building");
                log.setCode(AddressServiceErrorCode.ERROR_EXISTS_APARTMENT_NAME);
                errorLogs.add(log);
                continue;
            }*/
			
            double areaSize = 0;
            double chargeArea = 0;
            double rentArea = 0;
            double freeArea = 0;
            
			if (StringUtils.isNotEmpty(data.getAreaSize())) {
				try {
                    areaSize = Double.parseDouble(data.getAreaSize());
                    if (areaSize<0) {
                    	log.setData(data);
    					log.setErrorLog("area size should be greater than zero");
    					log.setCode(AddressServiceErrorCode.ERROR_AREA_SIZE_LESS_THAN_ZERO);
    					errorLogs.add(log);
    					continue;
					}
				} catch (Exception e) {
					log.setData(data);
					log.setErrorLog("area size is not number");
					log.setCode(AddressServiceErrorCode.ERROR_AREA_SIZE_NOT_NUMBER);
					errorLogs.add(log);
					continue;
				}
			}

            if (StringUtils.isNotEmpty(data.getChargeArea())) {
                try {
                    chargeArea = Double.parseDouble(data.getChargeArea());
                    if (chargeArea<0) {
                    	log.setData(data);
    					log.setErrorLog("charge area should be greater than zero");
    					log.setCode(AddressServiceErrorCode.ERROR_CHARGE_AREA_LESS_THAN_ZERO);
    					errorLogs.add(log);
    					continue;
					}
                } catch (Exception e) {
                    log.setData(data);
                    log.setErrorLog("charge area is not number");
                    log.setCode(AddressServiceErrorCode.ERROR_CHARGE_AREA_NOT_NUMBER);
                    errorLogs.add(log);
                    continue;
                }
            }

            if (StringUtils.isNotEmpty(data.getRentArea())) {
                try {
                    rentArea = Double.parseDouble(data.getRentArea());
                    if (rentArea<0) {
                    	log.setData(data);
    					log.setErrorLog("rent area should be greater than zero");
    					log.setCode(AddressServiceErrorCode.ERROR_RENT_AREA_LESS_THAN_ZERO);
    					errorLogs.add(log);
    					continue;
					}
                } catch (Exception e) {
                    log.setData(data);
                    log.setErrorLog("rent area is not number");
                    log.setCode(AddressServiceErrorCode.ERROR_RENT_AREA_NOT_NUMBER);
                    errorLogs.add(log);
                    continue;
                }
            }

//            if (StringUtils.isNotEmpty(data.getSharedArea())) {
//                try {
//                    shareArea = Double.parseDouble(data.getSharedArea());
//                } catch (Exception e) {
//                    log.setData(data);
//                    log.setErrorLog("share area is not number");
//                    log.setCode(AddressServiceErrorCode.ERROR_SHARE_AREA_NOT_NUMBER);
//                    errorLogs.add(log);
//                    continue;
//                }
//            }
            if (StringUtils.isNotEmpty(data.getFreeArea())) {
                try {
                    freeArea = Double.parseDouble(data.getFreeArea());
                    if (freeArea<0) {
                    	log.setData(data);
    					log.setErrorLog("free area should be greater than zero");
    					log.setCode(AddressServiceErrorCode.ERROR_FREE_AREA_LESS_THAN_ZERO);
    					errorLogs.add(log);
    					continue;
					}
                } catch (Exception e) {
                    log.setData(data);
                    log.setErrorLog("free area is not number");
                    log.setCode(AddressServiceErrorCode.ERROR_FREE_AREA_NOT_NUMBER);
                    errorLogs.add(log);
                    continue;
                }
            }
            
            if (StringUtils.isNotEmpty(data.getOrientation()) && (data.getOrientation()).length()>20) {
            	log.setData(data);
				log.setErrorLog("orientation lenth error");
				log.setCode(AddressServiceErrorCode.ERROR_APARTMENT_ORIENTATION_LENTH);
				errorLogs.add(log);
				continue;
            }
            
            //正则校验数字
//        	if (StringUtils.isNotEmpty(data.getApartmentFloor())) {
//    			String reg = "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";
//    			if(!Pattern.compile(reg).matcher(data.getApartmentFloor()).find()){
//    				log.setData(data);
//    				log.setErrorLog("FloorNumber format is error");
//    				log.setCode(AddressServiceErrorCode.ERROR_FLOORNUMBER_FORMAT);
//    				errorLogs.add(log);
//    				continue;
//    			}
//    		}
            if (StringUtils.isNotEmpty(data.getApartmentFloor())) {
    			try {
    				int apartmentFloor = Integer.parseInt(data.getApartmentFloor());
    				if (building.getFloorNumber() != null) {
						if (apartmentFloor <= 0 || apartmentFloor > building.getFloorNumber().intValue()) {
							log.setData(data);
		    				log.setErrorLog("FloorNumber is out of range");
		    				log.setCode(AddressServiceErrorCode.ERROR_FLOORNUMBER_OUT_OF_RANGE);
		    				errorLogs.add(log);
		    				continue;
						}
					}
				} catch (Exception e) {
					log.setData(data);
    				log.setErrorLog("FloorNumber format is error");
    				log.setCode(AddressServiceErrorCode.ERROR_FLOORNUMBER_FORMAT);
    				errorLogs.add(log);
    				continue;
				}
    		}
            //TODO 可能得增加对NamespaceAddressType和NamespaceAddressToken字段的校验 by tangcen
			importApartment(community, building, data, areaSize, chargeArea, rentArea, freeArea);
		}
        updateCommunityAptCount(community);
        
		return errorLogs;
	}
	
	private void importApartment(Community community, Building building, ImportApartmentDataDTO data, double areaSize, double chargeArea, double rentArea, double freeArea) {
		Long organizationId = findOrganizationByCommunity(community);

//        if (null == building) {
//            building = new Building();
//            // 补充域空间信息，以免一直查到的都是左邻域的楼栋，从而产生重新 by lqs 20161110
//            building.setNamespaceId(community.getNamespaceId());
//            building.setCommunityId(community.getId());
//            building.setName(data.getBuildingName());
//            building.setOperatorUid(UserContext.current().getUser().getId());
//            building = communityProvider.createBuilding(building.getOperatorUid(), building);
//            if(LOGGER.isDebugEnabled()) {
//                LOGGER.debug("Add new building, communityId={}, buildingName={}, building={}", community.getId(), data.getBuildingName(), building);
//            }
//        }

        //Address address = addressProvider.findAddressByCommunityAndAddress(community.getCityId(), community.getAreaId(), community.getId(), building.getName() + "-" + data.getApartmentName());
        Address address = addressProvider.findActiveAddressByBuildingApartmentName(community.getNamespaceId(), community.getId(), building.getName(), data.getApartmentName());

        if (address == null) {
        	address = new Address();
            address.setCommunityId(community.getId());
            address.setCommunityName(community.getName());
            address.setCityId(community.getCityId());
            address.setCityName(community.getCityName());
            address.setAreaId(community.getAreaId());
            address.setAreaName(community.getAreaName());
            address.setBuildingId(building.getId());
            address.setBuildingName(building.getName());
            address.setApartmentName(data.getApartmentName());
            address.setAreaSize(areaSize);
            address.setFreeArea(freeArea);
            address.setChargeArea(chargeArea);
            address.setRentArea(rentArea);
            address.setAddress(building.getName() + "-" + data.getApartmentName());
            address.setNamespaceAddressType(data.getNamespaceAddressType());
            address.setNamespaceAddressToken(data.getNamespaceAddressToken());
            address.setStatus(AddressAdminStatus.ACTIVE.getCode());
            address.setNamespaceId(community.getNamespaceId());
            address.setOrientation(data.getOrientation());
            address.setIsFutureApartment((byte)0);
            if (StringUtils.isNotBlank(data.getApartmentFloor())) {
            	address.setApartmentFloor(data.getApartmentFloor());
			}else {
				address.setApartmentFloor(null);
			}
        	addressProvider.createAddress(address);
        	//导入房源后，需要更新相应楼宇和园区的面积
            addAreaInBuildingAndCommunity(community, building, address);
		}else {
			//为了兼顾以前的历史数据，需要为这些数据添加CommunityName和BuildingId
			address.setCommunityId(community.getId());
            address.setCommunityName(community.getName());
            address.setBuildingId(building.getId());
            address.setBuildingName(building.getName());
			
			Double oldAreaSize = address.getAreaSize() == null ? 0.0 : address.getAreaSize();
            Double oldFreeArea = address.getFreeArea() == null ? 0.0 : address.getFreeArea();
            Double oldChargeArea = address.getChargeArea() == null ? 0.0 : address.getChargeArea();
            Double oldRentArea = address.getRentArea() == null ? 0.0 : address.getRentArea();
            
            address.setAreaSize(areaSize);
            address.setFreeArea(freeArea);
            address.setChargeArea(chargeArea);
            address.setRentArea(rentArea);
            address.setNamespaceAddressType(data.getNamespaceAddressType());
            address.setNamespaceAddressToken(data.getNamespaceAddressToken());
            address.setStatus(AddressAdminStatus.ACTIVE.getCode());
            address.setOrientation(data.getOrientation());
            if (StringUtils.isNotBlank(data.getApartmentFloor())) {
            	address.setApartmentFloor(data.getApartmentFloor());
			}else {
				address.setApartmentFloor(null);
			}
            
            addressProvider.updateAddress(address);
            //导入房源后，需要更新相应楼宇和园区的面积
            updateAreaInBuildingAndCommunity(community, building, address,oldAreaSize,oldFreeArea,oldChargeArea,oldRentArea);
		}
        Byte livingStatus = AddressMappingStatus.fromDesc(data.getStatus()).getCode();
        insertOrganizationAddressMapping(organizationId, community, address, livingStatus);
	}
	
	private void updateAreaInBuildingAndCommunity(Community community, Building building, Address address,
			Double oldAddressAreaSize, Double oldAddressFreeArea, Double oldAddressChargeArea, Double oldAddressRentArea) {
		
   	 	Double buildingAreaSize = building.getAreaSize() == null ? 0.0 : building.getAreaSize();
        building.setAreaSize(buildingAreaSize - oldAddressAreaSize + address.getAreaSize());
        Double communityAreaSize = community.getAreaSize() == null ? 0.0 : community.getAreaSize();
        community.setAreaSize(communityAreaSize - oldAddressAreaSize + address.getAreaSize());
            
        Double buildingRentArea = building.getRentArea() == null ? 0.0 : building.getRentArea();
        building.setRentArea(buildingRentArea - oldAddressRentArea + address.getRentArea());
        Double communityRentArea = community.getRentArea() == null ? 0.0 : community.getRentArea();
        community.setRentArea(communityRentArea - oldAddressRentArea + address.getRentArea());

        Double buildingChargeArea = building.getChargeArea() == null ? 0.0 : building.getChargeArea();
        building.setChargeArea(buildingChargeArea - oldAddressChargeArea + address.getChargeArea());
        Double communityChargeArea = community.getChargeArea() == null ? 0.0 : community.getChargeArea();
        community.setChargeArea(communityChargeArea - oldAddressChargeArea + address.getChargeArea());

        Double buildingFreeArea = building.getFreeArea() == null ? 0.0 : building.getFreeArea();
        building.setFreeArea(buildingFreeArea - oldAddressFreeArea + address.getFreeArea());
        Double communityFreeArea = community.getFreeArea() == null ? 0.0 : community.getFreeArea();
        community.setFreeArea(communityFreeArea - oldAddressFreeArea + address.getFreeArea());

        communityProvider.updateBuilding(building);
        communityProvider.updateCommunity(community);
	}

	private void addAreaInBuildingAndCommunity(Community community, Building building, Address address){
		
        Double buildingAreaSize = building.getAreaSize() == null ? 0.0 : building.getAreaSize();
        building.setAreaSize(buildingAreaSize + address.getAreaSize());
        Double communityAreaSize = community.getAreaSize() == null ? 0.0 : community.getAreaSize();
        community.setAreaSize(communityAreaSize + address.getAreaSize());
        
        Double buildingRentArea = building.getRentArea() == null ? 0.0 : building.getRentArea();
        building.setRentArea(buildingRentArea + address.getRentArea());
        Double communityRentArea = community.getRentArea() == null ? 0.0 : community.getRentArea();
        community.setRentArea(communityRentArea + address.getRentArea());
        
        Double buildingChargeArea = building.getChargeArea() == null ? 0.0 : building.getChargeArea();
        building.setChargeArea(buildingChargeArea + address.getChargeArea());
        Double communityChargeArea = community.getChargeArea() == null ? 0.0 : community.getChargeArea();
        community.setChargeArea(communityChargeArea + address.getChargeArea());
       
        Double buildingFreeArea = building.getFreeArea() == null ? 0.0 : building.getFreeArea();
        building.setFreeArea(buildingFreeArea + address.getFreeArea());
        Double communityFreeArea = community.getFreeArea() == null ? 0.0 : community.getFreeArea();
        community.setFreeArea(communityFreeArea + address.getFreeArea());
        
        communityProvider.updateBuilding(building);
        communityProvider.updateCommunity(community);
	}
	
	private List<ImportApartmentDataDTO> handleImportApartmentData(List resultList) {
		List<ImportApartmentDataDTO> list = new ArrayList<>();
		for(int i = 1; i < resultList.size(); i++) {
            RowResult r = (RowResult) resultList.get(i);
			if (StringUtils.isNotBlank(r.getA()) || StringUtils.isNotBlank(r.getB()) || StringUtils.isNotBlank(r.getC()) 
					|| StringUtils.isNotBlank(r.getD()) || StringUtils.isNotBlank(r.getE()) || StringUtils.isNotBlank(r.getF())
					|| StringUtils.isNotBlank(r.getG()) || StringUtils.isNotBlank(r.getH()) || StringUtils.isNotBlank(r.getI())
					|| StringUtils.isNotBlank(r.getJ())) {
				ImportApartmentDataDTO data = new ImportApartmentDataDTO();
				data.setApartmentName(trim(r.getA()));
				data.setStatus(trim(r.getB()));
				data.setApartmentFloor(trim(r.getC()));
                data.setAreaSize(trim(r.getD()));
                data.setRentArea(trim(r.getE()));
                data.setFreeArea(trim(r.getF()));
                data.setChargeArea(trim(r.getG()));
                data.setOrientation(trim(r.getH()));
                //加上来源第三方和在第三方的唯一标识 没有则不填 by xiongying20170814
                data.setNamespaceAddressType(trim(r.getI()));
                data.setNamespaceAddressToken(trim(r.getJ()));
				list.add(data);
			}
		}
		return list;
	}
	
	private String trim(String string) {
		return string == null ? "" : string.trim();
	}
	
	private void updateCommunityAptCount(Community community) {
        if (community != null) {
            Integer count = addressProvider.countApartment(community.getId());
            if (community.getAptCount() == null || community.getAptCount().intValue() != count.intValue()) {
                community.setAptCount(count);
                communityProvider.updateCommunity(community);
            }
        }
    }

    @Override
    public void importAddressData(MultipartFile[] files) {

        if (org.springframework.util.StringUtils.isEmpty(files) || files.length == 0) {
            LOGGER.error("File content is empty.");
            throw new RuntimeErrorException("File content is empty。");
        }

        try {
            List list = PropMrgOwnerHandler.processorExcel(files[0].getInputStream());

            Map<String, List<String[]>> dataMap = this.convertToAddrDatas(list);


            for (String key : dataMap.keySet()) {
                List<String[]> datas = dataMap.get(key);
                String[] str = datas.get(0);
                String cityName = str[3];
                String areaName = str[4];
                String communityName = str[5];
                Long cityId = regionProvider.findRegionByPath("/" + cityName).getId();
                if (null == cityId) {
                    LOGGER.error("The city does not exist。 data = " + key);
                    continue;
                }
                Long areaId = regionProvider.findRegionByPath("/" + cityName + "/" + areaName).getId();
                if (null == areaId) {
                    LOGGER.error("The area does not exist。 data = " + key);
                    continue;
                }
                List<Community> communitys = communityProvider.findCommunitiesByNameCityIdAreaId(communityName, cityId, areaId);

                if (null == communitys || 0 == communitys.size()) {
                    LOGGER.error("The community does not exist。 data = " + key);
                    continue;
                }
                Community community = communitys.get(0);

                this.batchAddAddresses(community, datas);
            }


        } catch (Exception e) {
            throw new RuntimeErrorException("File parsing error");
        }

    }

    private Long findOrganizationByCommunity(Community community) {
        if (community != null) {
            List<OrganizationCommunityDTO> list = organizationProvider.findOrganizationCommunityByCommunityId(community.getId());
            if (list != null && !list.isEmpty()) {
                return list.get(0).getOrganizationId();
            }
        }
        return null;
    }

    private void batchAddAddresses(Community community, List<String[]> datas) {
        Long organizationId = findOrganizationByCommunity(community);
        for (String[] arr : datas) {

            if (org.springframework.util.StringUtils.isEmpty(arr[0])
                    || org.springframework.util.StringUtils.isEmpty(arr[1])) {
                LOGGER.error("Data is not complete or empty. data = " + arr[0] + "|" + arr[1] + "|" + arr[2]);
                continue;
            }

            Building building = communityProvider.findBuildingByCommunityIdAndName(community.getId(), arr[0]);

            if (null == building) {
                building = new Building();
                // 补充域空间信息，以免一直查到的都是左邻域的楼栋，从而产生重新 by lqs 20161110
                building.setNamespaceId(community.getNamespaceId());
                building.setCommunityId(community.getId());
                building.setName(arr[0]);
                building.setOperatorUid(UserContext.current().getUser().getId());
                building = communityProvider.createBuilding(building.getOperatorUid(), building);
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Add new building, communityId={}, buildingName={}, building={}", community.getId(), arr[0], building);
                }
            }

            double areaSize = 0;
            try {
                areaSize = Double.parseDouble(arr[2]);
            } catch (Exception e) {
            }

            Address address = new Address();
            address.setCommunityId(community.getId());
            address.setCityId(community.getCityId());
            address.setCityName(community.getCityName());
            address.setAreaId(community.getAreaId());
            address.setAreaName(community.getAreaName());
            address.setBuildingName(building.getName());
            address.setApartmentName(arr[1]);
            address.setAreaSize(areaSize);
            address.setAddress(building.getName() + "-" + arr[1]);
            address.setStatus(AddressAdminStatus.ACTIVE.getCode());
            address.setNamespaceId(community.getNamespaceId());

            Address addr = addressProvider.findAddressByCommunityAndAddress(address.getCityId(), address.getAreaId(), address.getCommunityId(), address.getAddress());

            if (null != addr) {
                LOGGER.error("Data already exists. data = " + arr[0] + "|" + arr[1] + "|" + arr[2]);
                insertOrganizationAddressMapping(organizationId, community, addr, AddressMappingStatus.DEFAULT.getCode());
                continue;
            }
            addressProvider.createAddress(address);
            insertOrganizationAddressMapping(organizationId, community, address, AddressMappingStatus.DEFAULT.getCode());
        }
    }

    private void insertOrganizationAddressMapping(Long organizationId, Community community, Address address, Byte livingStatus) {
        if (organizationId != null && community != null && address != null) {
            CommunityAddressMapping communityAddressMapping = organizationProvider.findOrganizationAddressMapping(organizationId, community.getId(), address.getId());
            if (communityAddressMapping == null) {
                communityAddressMapping = new CommunityAddressMapping();
                communityAddressMapping.setOrganizationId(organizationId);
                communityAddressMapping.setCommunityId(community.getId());
                communityAddressMapping.setAddressId(address.getId());
                communityAddressMapping.setOrganizationAddress(address.getAddress());
                communityAddressMapping.setLivingStatus(livingStatus);
                communityAddressMapping.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                communityAddressMapping.setUpdateTime(communityAddressMapping.getCreateTime());
                organizationProvider.createOrganizationAddressMapping(communityAddressMapping);
            }else {
				communityAddressMapping.setLivingStatus(livingStatus);
				communityAddressMapping.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				organizationProvider.updateOrganizationAddressMapping(communityAddressMapping);
			}
        }
    }


    private Map<String, List<String[]>> convertToAddrDatas(List list) {
        Map<String, List<String[]>> map = new HashMap<String, List<String[]>>();
        String key = "";
        List<String[]> values = null;
        boolean firstRow = true;
        for (Object o : list) {
            if (firstRow) {
                firstRow = false;
                continue;
            }
            RowResult r = (RowResult) o;

            if (null == r.getA() || "".equals(r.getA()) || "null".equals(r.getA())
                    || null == r.getB() || "".equals(r.getB()) || "null".equals(r.getB())
                    || null == r.getC() || "".equals(r.getC()) || "null".equals(r.getC()))
                continue;
            key = r.getA() + r.getB() + r.getC();
            String[] value = new String[6];
            value[3] = r.getA();
            value[4] = r.getB();
            value[5] = r.getC();
            value[0] = r.getD();
            value[1] = r.getE();
            value[2] = r.getF();
            if (null == map.get(key)) {
                values = new ArrayList<String[]>();
                values.add(value);
                map.put(key, values);
            } else {
                map.get(key).add(value);
            }

        }
        return map;
    }

    private List<String[]> convertToParkDatas(List list) {
        List<String[]> result = new ArrayList<String[]>();
        boolean firstRow = true;
        for (Object o : list) {
            if (firstRow) {
                firstRow = false;
                continue;
            }
            RowResult r = (RowResult) o;
            String[] s = new String[3];
            s[0] = r.getA();
            s[1] = r.getB();
            s[2] = r.getC();
            result.add(s);
        }
        return result;
    }

    @Override
    public ListNearbyMixCommunitiesCommandResponse listNearbyMixCommunities(
            ListNearbyMixCommunitiesCommand cmd) {
        ListNearbyMixCommunitiesCommandResponse resp = new ListNearbyMixCommunitiesCommandResponse();
        List<CommunityDTO> results = new ArrayList<>();

        if (cmd.getLatigtue() == null || cmd.getLongitude() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter, latitude and longitude have to be both specified or neigher");


        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0L);

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        ListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        int namespaceId = (UserContext.current().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getNamespaceId();
        if (namespaceId == 0) {
            resp = listMixCommunitiesByDistance(cmd, locator, pageSize);
            return resp;
        } else {
           // results = this.communityProvider.listCommunitiesByNamespaceId(cmd.getCommunityType(), namespaceId, locator, pageSize + 1);
            //update by huanglm ,#22488【iOS&安卓】左上角地址切换不是按照距离最近来排序的 
        	List<Community> communities = this.listMixCommunitiesByDistanceWithNamespaceId(cmd, locator, pageSize);
        	//issue-33013(部分解决),communities可能为空，会导致app报开小差，需要做非空判断
        	if (communities!=null) {
        		communities.stream().map(r->{
    				CommunityDTO dto = ConvertHelper.convert(r,CommunityDTO.class);					
    				results.add(dto);
    				return null;
    			}).collect(Collectors.toList());
			}
        	
            if (results != null && results.size() > pageSize) {
                results.remove(results.size() - 1);
                resp.setNextPageAnchor(results.get(results.size() - 1).getId());
            }
            if (results != null) {
                resp.setDtos(results);
            }

            return resp;
        }

    }

    @Override
    public ListNearbyMixCommunitiesCommandResponse listMixCommunitiesByDistance(ListNearbyMixCommunitiesCommand cmd
            , ListingLocator locator, int pageSize) {
        ListNearbyMixCommunitiesCommandResponse resp = new ListNearbyMixCommunitiesCommandResponse();
        List<CommunityDTO> results = new ArrayList<>();

        List<CommunityGeoPoint> pointList = this.communityProvider.findCommunityGeoPointByGeoHash(cmd.getLatigtue(), cmd.getLongitude(), 5);
        List<Long> communityIds = getAllCommunityIds(pointList);

        int namespaceId = UserContext.getCurrentNamespaceId();

        if (cmd.getCommunityType() == null) {
            if (cmd.getPageAnchor() != 0) {
                Community anchorCommunity = this.communityProvider.findCommunityById(cmd.getPageAnchor());
                if (anchorCommunity != null && anchorCommunity.getCommunityType() == CommunityType.COMMERCIAL.getCode()) {
                    //园区 Anchor
                    List<CommunityDTO> commercials = this.communityProvider.listCommunitiesByType(namespaceId, communityIds,
                            CommunityType.COMMERCIAL.getCode(), locator, pageSize + 1);
                    if (commercials != null) {
                        results.addAll(commercials);
                    }

                    if (commercials == null || (commercials != null && commercials.size() <= pageSize)) {
                        int count = pageSize;
                        if (commercials != null) {
                            count = pageSize - commercials.size();
                        }
                        //小区  从0开始
                        ListingLocator residentialsLocator = new CrossShardListingLocator();
                        residentialsLocator.setAnchor(0L);
                        List<CommunityDTO> residentials = this.communityProvider.listCommunitiesByType(namespaceId, communityIds,
                                CommunityType.RESIDENTIAL.getCode(), residentialsLocator, count + 1);
                        if (residentials != null) {
                            results.addAll(residentials);
                        }
                    }

                } else {
                    //小区 Anchor
                    List<CommunityDTO> residentials = this.communityProvider.listCommunitiesByType(namespaceId, communityIds,
                            CommunityType.RESIDENTIAL.getCode(), locator, pageSize + 1);
                    if (residentials != null) {
                        results.addAll(residentials);
                    }
                }
            } else {
                //园区  从0开始
                List<CommunityDTO> commercials = this.communityProvider.listCommunitiesByType(namespaceId, communityIds,
                        CommunityType.COMMERCIAL.getCode(), locator, pageSize + 1);
                if (commercials != null) {
                    results.addAll(commercials);
                }

                if (commercials == null || (commercials != null && commercials.size() <= pageSize)) {
                    int count = pageSize;
                    if (commercials != null) {
                        count = pageSize - commercials.size();
                    }
                    //小区  从0开始
                    List<CommunityDTO> residentials = this.communityProvider.listCommunitiesByType(namespaceId, communityIds,
                            CommunityType.RESIDENTIAL.getCode(), locator, count + 1);
                    if (residentials != null) {
                        results.addAll(residentials);
                    }
                }
            }
        } else if (cmd.getCommunityType() != null && cmd.getCommunityType().equals(CommunityType.COMMERCIAL.getCode())) {
            List<CommunityDTO> commercials = this.communityProvider.listCommunitiesByType(namespaceId, communityIds,
                    CommunityType.COMMERCIAL.getCode(), locator, pageSize + 1);
            if (commercials != null) {
                results.addAll(commercials);
            }
        } else if (cmd.getCommunityType() != null && cmd.getCommunityType().equals(CommunityType.RESIDENTIAL.getCode())) {
            List<CommunityDTO> residentials = this.communityProvider.listCommunitiesByType(namespaceId, communityIds,
                    CommunityType.RESIDENTIAL.getCode(), locator, pageSize + 1);
            if (residentials != null) {
                results.addAll(residentials);
            }
        }


        if (results != null && results.size() > pageSize) {
            results.remove(results.size() - 1);
            resp.setNextPageAnchor(results.get(results.size() - 1).getId());
        }
        if (results != null) {
            resp.setDtos(results);
        }

        return resp;
    }

    @Override
    public List<AddressDTO> listAddressByBuildingName(ListApartmentByBuildingNameCommand cmd) {
        return addressProvider.listAddressByBuildingName(UserContext.getCurrentNamespaceId(), cmd.getCommunityId(), cmd.getBuildingName());
    }

    @Override
    public AddressDTO getApartmentByBuildingApartmentName(GetApartmentByBuildingApartmentNameCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Address address = addressProvider.findAddressByBuildingApartmentName(namespaceId, cmd.getCommunityId(),
                cmd.getBuildingName(), cmd.getApartmentName());
        return ConvertHelper.convert(address, AddressDTO.class);

    }


    @Override
    public List<Community> listMixCommunitiesByDistanceWithNamespaceId(ListNearbyMixCommunitiesCommand cmd, ListingLocator locator, int pageSize){
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Long userId = UserContext.current().getUser().getId();
        List<Long> communityIds = null;
        List<NamespaceResource> resources = namespaceResourceProvider.listResourceByNamespaceOrderByDefaultOrder(namespaceId, NamespaceResourceType.COMMUNITY);
        if(resources != null && resources.size() > 0){
            communityIds = resources.stream().map(NamespaceResource::getResourceId).collect(Collectors.toList());
        }
        List<SceneDTO> sceneList = new ArrayList<SceneDTO>();
        
        if(communityIds != null){
            List<CommunityGeoPoint> pointList = this.communityProvider.listCommunityGeoPointByGeoHashInCommunities(cmd.getLatigtue(), cmd.getLongitude(), 5, communityIds);
            if(pointList != null && pointList.size() > 0){
                List<Community> communities_after = pointList.stream().map(r->{
                    return this.communityProvider.findCommunityById(r.getCommunityId());
                }).collect(Collectors.toList());
                return communities_after;
            }
        }
        return null;
    }

    @Override
    public void deleteApartmentAttachment(DeleteApartmentAttachmentCommand cmd) {
        addressProvider.deleteApartmentAttachment(cmd.getId());
    }

    @Override
    public List<ApartmentAttachmentDTO> listApartmentAttachments(ListApartmentAttachmentsCommand cmd) {
        List<AddressAttachment> attachmentList = addressProvider.listAddressAttachments(cmd.getAddressId());

        return attachmentList.stream().map(r -> {
            ApartmentAttachmentDTO dto = ConvertHelper.convert(r, ApartmentAttachmentDTO.class);
            dto.setContentUrl(contentServerService.parserUri(r.getContentUri(), EhActivities.class.getSimpleName(), r.getAddressId()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public ApartmentAttachmentDTO uploadApartmentAttachment(UploadApartmentAttachmentCommand cmd) {
        AddressAttachment attachment = new AddressAttachment();
        attachment.setAddressId(cmd.getAddressId());
        attachment.setContentUri(cmd.getContentUri());
        attachment.setName(cmd.getAttachmentName());
        ContentServerResource resource = contentServerService.findResourceByUri(cmd.getContentUri());
        Integer size = resource.getResourceSize();
        attachment.setFileSize(size);
        attachment.setCreatorUid(UserContext.currentUserId());
        addressProvider.createAddressAttachment(attachment);

        ApartmentAttachmentDTO dto = ConvertHelper.convert(attachment, ApartmentAttachmentDTO.class);
        dto.setContentUrl(contentServerService.parserUri(attachment.getContentUri(), EhActivities.class.getSimpleName(), attachment.getAddressId()));
        return dto;
    }

    @Override
    public void downloadApartmentAttachment(DownloadApartmentAttachmentCommand cmd) {
        if(cmd.getAttachmentId() == null || cmd.getAddressId() == null) {
            return ;
        }

        AddressAttachment attachment = addressProvider.findByAddressAttachmentId(cmd.getAttachmentId());
        if(attachment == null) {
            LOGGER.error("handle address attachment error ,the address attachment does not exsit.cmd={}", cmd);
            throw RuntimeErrorException.errorWith(AddressServiceErrorCode.SCOPE,
                    AddressServiceErrorCode.ERROR_INVALID_ADDRESS_ATTACHMENT_ID, "invalid address attachment id " + cmd.getAttachmentId());
        }

        attachment.setDownloadCount(attachment.getDownloadCount() + 1);
        addressProvider.updateAddressAttachment(attachment);
    }

    @Override
    public List<EnterpriseCustomerDTO> listApartmentEnterpriseCustomers(ListApartmentEnterpriseCustomersCommand cmd) {
        List<EnterpriseCustomerDTO> dtos = new ArrayList<>();
//    3.在门牌管理页面中新增企业客户信息tab页，该门牌关联的企业信息来源于3处，详见!新增企业客户信息tab.png!：
//    a) 企业客户基本信息中关联了楼栋门牌的企业
//    b) 入驻信息tab页中关联了楼栋门牌的企业
//    c) 合同中关联了楼栋门牌的企业；
        Set<Long> customerIds = new HashSet<>();
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());

        List<ContractBuildingMapping> mappings = contractBuildingMappingProvider.listByAddress(cmd.getAddressId());
        if(mappings != null && mappings.size() > 0) {
            mappings.forEach(mapping -> {
                Contract contract = contractProvider.findContractById(mapping.getContractId());
                if (contract != null) {
                	if(ContractStatus.ACTIVE.equals(ContractStatus.fromStatus(contract.getStatus()))
                            && now.after(contract.getContractStartDate()) && now.before(contract.getContractEndDate())
                            && CustomerType.ENTERPRISE.equals(CustomerType.fromStatus(contract.getCustomerType()))) {
                        customerIds.add(contract.getCustomerId());
                    }
				}
            });
        }

        List<CustomerEntryInfo> entryInfos = enterpriseCustomerProvider.listAddressEntryInfos(cmd.getAddressId());
        if(entryInfos != null && entryInfos.size() > 0) {
            entryInfos.forEach(entryInfo -> {
                if(CustomerType.ENTERPRISE.equals(CustomerType.fromStatus(entryInfo.getCustomerType()))) {
                    customerIds.add(entryInfo.getCustomerId());
                }
            });
        }

        if(customerIds != null && customerIds.size() > 0) {
            List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomers(customerIds);
            if(customers != null && customers.size() > 0) {
                customers.forEach(customer -> {
                    EnterpriseCustomerDTO dto = ConvertHelper.convert(customer, EnterpriseCustomerDTO.class);
                    ScopeFieldItem categoryItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getCommunityId(), customer.getCategoryItemId());
                    if(categoryItem != null) {
                        dto.setCategoryItemName(categoryItem.getItemDisplayName());
                    }
                    ScopeFieldItem levelItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getCommunityId(), customer.getLevelItemId());
                    if(levelItem != null) {
                        dto.setLevelItemName(levelItem.getItemDisplayName());
                    }
                    ScopeFieldItem entryStatusItem  = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getCommunityId(), customer.getEntryStatusItemId());
                    if(entryStatusItem != null) {
                        dto.setLevelItemName(entryStatusItem.getItemDisplayName());
                    }
                    ListCustomerEntryInfosCommand command1 = new ListCustomerEntryInfosCommand();
                    command1.setCommunityId(customer.getCommunityId());
                    command1.setCustomerId(customer.getId());
                    List<CustomerEntryInfoDTO> entryInfoDTOList = customerService.listCustomerEntryInfosWithoutAuth(command1);
                    entryInfoDTOList = removeDuplicatedEntryInfo(entryInfoDTOList);
                    if (entryInfoDTOList != null && entryInfoDTOList.size() > 0) {
                        entryInfoDTOList = entryInfoDTOList.stream().peek((e) -> e.setAddressName(e.getBuilding() + "/" + e.getApartment())).collect(Collectors.toList());
                        dto.setEntryInfos(entryInfoDTOList);
                    }
                    ListServiceModuleAdministratorsCommand command = new ListServiceModuleAdministratorsCommand();
                    command.setOrganizationId(customer.getOrganizationId());
                    command.setCustomerId(customer.getId());
                    command.setNamespaceId(UserContext.getCurrentNamespaceId());
                    command.setCommunityId(customer.getCommunityId());
                    List<OrganizationContactDTO> admins = customerService.listOrganizationAdmin(command);
                    dto.setEnterpriseAdmins(admins);
                    dtos.add(dto);
                });
            }
        }
        return dtos;
    }

    private List<CustomerEntryInfoDTO> removeDuplicatedEntryInfo(List<CustomerEntryInfoDTO> entryInfos) {
        Map<Long, CustomerEntryInfoDTO> map = new HashMap<>();
        if (entryInfos != null && entryInfos.size() > 0) {
            entryInfos.forEach((e) -> {
                if (e.getAddressId() != null) {
                    Address address = addressProvider.findAddressById(e.getAddressId());
                    if (address != null) {
                        map.putIfAbsent(e.getAddressId(), e);
                    }
                }
            });
            return new ArrayList<>(map.values());
        }
        return null;
    }

	@Override
	public void createAddressArrangement(CreateAddressArrangementCommand cmd) {
		if (cmd.getOperationType() == AddressArrangementType.SPLIT.getCode()) {
			createSplitArrangement(cmd);
		}else if (cmd.getOperationType() == AddressArrangementType.MERGE.getCode()) {
			createMergeArrangement(cmd);
		}
	}
	
	private void createSplitArrangement(CreateAddressArrangementCommand cmd) {
		Address targetAddress = addressProvider.findAddressById(cmd.getAddressId());
		List<String> targetIds = new ArrayList<>();
		List<String> originalIds = new ArrayList<>();
		originalIds.add(cmd.getAddressId().toString());
		List<ArrangementApartmentDTO> apartments = cmd.getApartments();
		for (ArrangementApartmentDTO apartment : apartments) {
			Address address = new Address();
			address.setAreaSize(apartment.getAreaSize());
			address.setRentArea(apartment.getRentArea());
			address.setFreeArea(apartment.getFreeArea());
			address.setChargeArea(apartment.getChargeArea());
			address.setApartmentName(apartment.getApartmentName());
			address.setStatus(AddressAdminStatus.ACTIVE.getCode());
			//设置成为未来资产
			address.setIsFutureApartment((byte) 1);
			address.setNamespaceId(targetAddress.getNamespaceId());
			address.setCommunityId(targetAddress.getCommunityId());
			address.setCityName(targetAddress.getCityName());
			address.setCityId(targetAddress.getCityId());
			address.setAreaId(targetAddress.getAreaId());
			address.setAreaName(targetAddress.getAreaName());
			address.setApartmentFloor(targetAddress.getApartmentFloor());
			address.setBuildingName(targetAddress.getBuildingName());
			address.setAddress(address.getBuildingName() + "-" + address.getApartmentName());
			
			Long addressId = addressProvider.createAddress3(address);
			targetIds.add(addressId.toString());
			//设置房源的具体状态
			createOrganizationAddressMapping(addressId,address.getAddress(),cmd.getOrganizationId(),cmd.getCommunityId());
		}
		AddressArrangement arrangement = ConvertHelper.convert(cmd, AddressArrangement.class);
		String targetId = StringHelper.toJsonString(targetIds);
		arrangement.setTargetId(targetId);
		String originalId = StringHelper.toJsonString(originalIds);
		arrangement.setOriginalId(originalId);
		arrangement.setDateBegin(formatTime(cmd.getDateBegin()));
		arrangement.setOperationFlag(ArrangementOperationFlag.NOT_EXCUTED.getCode());
		arrangement.setStatus(AddressArrangementStatus.ACTIVE.getCode());
		User user = UserContext.current().getUser();
		arrangement.setCreatorUid(user.getId());
		addressProvider.createAddressArrangement(arrangement);
	}	
	
	private void createMergeArrangement(CreateAddressArrangementCommand cmd) {
		Address targetAddress = addressProvider.findAddressById(cmd.getAddressId());
		List<String> targetIds = new ArrayList<>();
		List<String> originalIds = new ArrayList<>();
		originalIds.add(targetAddress.getId().toString());
		
		Address address = new Address();
		address.setStatus(AddressAdminStatus.ACTIVE.getCode());
		//设置成为未来资产
		address.setIsFutureApartment((byte) 1);
		address.setNamespaceId(targetAddress.getNamespaceId());
		address.setCommunityId(targetAddress.getCommunityId());
		address.setCityName(targetAddress.getCityName());
		address.setCityId(targetAddress.getCityId());
		address.setAreaId(targetAddress.getAreaId());
		address.setAreaName(targetAddress.getAreaName());
		address.setApartmentFloor(targetAddress.getApartmentFloor());
		address.setBuildingName(targetAddress.getBuildingName());
		String newInChinese = localeTemplateService.getLocaleTemplateString(AddressArrangementTemplateCode.SCOPE,
																		AddressArrangementTemplateCode.ADDRESS_ARRANGEMENT_NEW_IN_CHINESE, 
																		UserContext.current().getUser().getLocale(), null, "");
		address.setApartmentName(targetAddress.getApartmentName()+"-"+newInChinese);
		address.setAddress(address.getBuildingName() + "-" + address.getApartmentName());
		
		address.setAreaSize(targetAddress.getAreaSize());
		//address.setChargeArea(targetAddress.getChargeArea());
		//address.setRentArea(targetAddress.getRentArea());
		//address.setFreeArea(targetAddress.getFreeArea());
		
		List<ArrangementApartmentDTO> apartments = cmd.getApartments();
		for (ArrangementApartmentDTO apartment : apartments) {
			//address.setRentArea((address.getRentArea()!=null ? address.getRentArea() : (double)0) + (apartment.getRentArea()!=null ? apartment.getRentArea() : (double)0));
			//address.setFreeArea((address.getFreeArea()!=null ? address.getFreeArea() : (double)0) + (apartment.getFreeArea()!=null ? apartment.getFreeArea() : (double)0));
			//address.setChargeArea((address.getChargeArea()!=null ? address.getChargeArea():(double)0) + (apartment.getChargeArea()!=null ? apartment.getChargeArea() : (double)0));
			address.setAreaSize((address.getAreaSize()!=null ? address.getAreaSize() : (double)0) + (apartment.getAreaSize()!=null ? apartment.getAreaSize() : (double)0));
			originalIds.add(apartment.getAddressId().toString());
		}
		//合并后的房源，在租面积等于0，可招租面积等于建筑面积，收费面积等于建筑面积
		address.setRentArea(0.0);
		address.setFreeArea(address.getAreaSize());
		address.setChargeArea(address.getAreaSize());
		
		Long newAddressId = addressProvider.createAddress3(address);
		targetIds.add(newAddressId.toString());
		//设置房源的具体状态
		createOrganizationAddressMapping(newAddressId,address.getAddress(),cmd.getOrganizationId(),cmd.getCommunityId());
		
		AddressArrangement arrangement = ConvertHelper.convert(cmd, AddressArrangement.class);
		String targetId = StringHelper.toJsonString(targetIds);
		arrangement.setTargetId(targetId);
		String originalId = StringHelper.toJsonString(originalIds);
		arrangement.setOriginalId(originalId);
		arrangement.setDateBegin(formatTime(cmd.getDateBegin()));
		arrangement.setOperationFlag(ArrangementOperationFlag.NOT_EXCUTED.getCode());
		arrangement.setStatus(AddressArrangementStatus.ACTIVE.getCode());
		User user = UserContext.current().getUser();
		arrangement.setCreatorUid(user.getId());
		addressProvider.createAddressArrangement(arrangement);
	}
	
	//添加房源、小区、机构的对应关系，设置房源的具体状态存在eh_organization_address_mappings表中
	private void createOrganizationAddressMapping(Long addressId,String address,Long organizationId, Long communityId) {
		CommunityAddressMapping communityAddressMapping = new CommunityAddressMapping();
        communityAddressMapping.setOrganizationId(organizationId);
        communityAddressMapping.setCommunityId(communityId);
        communityAddressMapping.setAddressId(addressId);
        communityAddressMapping.setOrganizationAddress(address);
        communityAddressMapping.setLivingStatus(AddressMappingStatus.FREE.getCode());
        communityAddressMapping.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        communityAddressMapping.setUpdateTime(communityAddressMapping.getCreateTime());
        organizationProvider.createOrganizationAddressMapping(communityAddressMapping);
	}
	
	private Timestamp formatTime(Long time){
		Timestamp result = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String format = simpleDateFormat.format(time);
		try {
			Date today = simpleDateFormat.parse(format);
			result = new Timestamp(today.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public AddressArrangementDTO listAddressArrangement(ListAddressArrangementCommand cmd) {
		AddressArrangement arrangement = null;
		//因为用的是like匹配，所以需要对数据进一步过滤
		List<AddressArrangement> arrangements = addressProvider.findActiveAddressArrangementByOriginalIdV2(cmd.getAddressId());
		for (AddressArrangement addressArrangement : arrangements) {
			List<String> originalIds = (List<String>)StringHelper.fromJsonString(addressArrangement.getOriginalId(), ArrayList.class); 
			if (originalIds.contains(cmd.getAddressId().toString())) {
				arrangement = addressArrangement;
				break;
			}
		}
		AddressArrangementDTO dto = null;
		if (arrangement != null) {
			dto = ConvertHelper.convert(arrangement, AddressArrangementDTO.class);
			dto.setOrganizationId(cmd.getOrganizationId());
			dto.setDateBegin(arrangement.getDateBegin().getTime());
			
			Address address = addressProvider.findAddressById(arrangement.getAddressId());
			ArrangementApartmentDTO baseApartment = ConvertHelper.convert(address, ArrangementApartmentDTO.class);
			baseApartment.setAddressId(address.getId());
			dto.setBaseApartment(baseApartment);
			
			if (arrangement.getOperationType() == AddressArrangementType.SPLIT.getCode()) {
				//获取未来房源（即被拆分产生的房源）
				List<ArrangementApartmentDTO> targetApartments = generateTargetApartments(arrangement);
				dto.setApartments(targetApartments);
				generateApartmentRelatedInfo(dto,targetApartments);
			}else if (arrangement.getOperationType() == AddressArrangementType.MERGE.getCode()) {
				//获取当下房源（即参与合并的房源）
				List<ArrangementApartmentDTO> OriginalApartments = generateOriginalApartments(arrangement);
				dto.setApartments(OriginalApartments);
				List<ArrangementApartmentDTO> targetApartments = generateTargetApartments(arrangement);
				generateApartmentRelatedInfo(dto,targetApartments);
			}
			
		}
		return dto;
	}
	
	//获取未来房源
	private List<ArrangementApartmentDTO> generateTargetApartments(AddressArrangement arrangement) {
		List<ArrangementApartmentDTO> results = new ArrayList<>();
		List<String> targetIds = (List<String>)StringHelper.fromJsonString(arrangement.getTargetId(), ArrayList.class);
		for (String addressId : targetIds) {
			Address address = addressProvider.findAddressById(Long.parseLong(addressId));
			ArrangementApartmentDTO dto = ConvertHelper.convert(address, ArrangementApartmentDTO.class);
			dto.setAddressId(address.getId());
			results.add(dto);
		}
		return results;
	}
	
	//获取当下房源
	private List<ArrangementApartmentDTO> generateOriginalApartments(AddressArrangement arrangement) {
		List<ArrangementApartmentDTO> results = new ArrayList<>();
		List<String> originalIds = (List<String>)StringHelper.fromJsonString(arrangement.getOriginalId(), ArrayList.class);
		originalIds.remove(arrangement.getAddressId().toString());
		for (String addressId : originalIds) {
			Address address = addressProvider.findAddressById(Long.parseLong(addressId));
			ArrangementApartmentDTO dto = ConvertHelper.convert(address, ArrangementApartmentDTO.class);
			dto.setAddressId(address.getId());
			results.add(dto);
		}
		return results;
	}
	
	//需要显示的是所有房源关联的企业客户，个人客户，和合同信息
	private void generateApartmentRelatedInfo(AddressArrangementDTO dto,List<ArrangementApartmentDTO> apartments){
		List<EnterpriseCustomerDTO> enterpriseCustomersList = new ArrayList<>();
	    List<OrganizationOwnerDTO> individualCustomersList = new ArrayList<>();
	    List<ContractDTO> contractsList = new ArrayList<>();
		for(ArrangementApartmentDTO apartment:apartments){
			//企业客户
			ListApartmentEnterpriseCustomersCommand listApartmentEnterpriseCustomersCommand= new ListApartmentEnterpriseCustomersCommand();
			listApartmentEnterpriseCustomersCommand.setAddressId(apartment.getAddressId());
			List<EnterpriseCustomerDTO> enterpriseCustomers = this.listApartmentEnterpriseCustomers(listApartmentEnterpriseCustomersCommand);
			enterpriseCustomersList.addAll(enterpriseCustomers);
			//个人客户
			ListOrganizationOwnersByAddressCommand listOrganizationOwnersByAddressCommand = new ListOrganizationOwnersByAddressCommand();
			listOrganizationOwnersByAddressCommand.setAddressId(apartment.getAddressId());
			listOrganizationOwnersByAddressCommand.setOrganizationId(dto.getOrganizationId());
			List<OrganizationOwnerDTO> individualCustomers = propertyMgrService.listOrganizationOwnersByAddress(listOrganizationOwnersByAddressCommand);
			individualCustomersList.addAll(individualCustomers);
			//合同信息
			List<Contract> contracts = contractProvider.listContractsByAddressId(apartment.getAddressId());
			List<ContractDTO> contractDTOs = contracts.stream().map(contract -> {
				ContractDTO contractDTO = ConvertHelper.convert(contract, ContractDTO.class);
				return contractDTO;
			}).collect(Collectors.toList());
			contractsList.addAll(contractDTOs);
		}
		dto.setEnterpriseCustomers(enterpriseCustomersList);
		dto.setIndividualCustomers(individualCustomersList);
		dto.setContracts(contractsList);
	}

	@Override
	public void updateAddressArrangement(UpdateAddressArrangementCommand cmd) {
		AddressArrangement arrangement = addressProvider.findAddressArrangementById(cmd.getId());
		if (arrangement == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "the addressArrangement does not exist!");
		}
		if (cmd.getDateBegin()!=null && formatTime(cmd.getDateBegin()) != arrangement.getDateBegin()) {
			arrangement.setDateBegin(formatTime(cmd.getDateBegin()));
			arrangement.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			User user = UserContext.current().getUser();
			arrangement.setUpdateUid(user.getId());
			addressProvider.updateAddressArrangement(arrangement);
		}
		
		List<ArrangementApartmentDTO> apartments = cmd.getApartments();
		if (apartments!=null) {
			for (ArrangementApartmentDTO dto : apartments) {
				Address address = addressProvider.findAddressById(dto.getAddressId());
				if (address == null) {
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
		                    "the address does not exist!");
				}
				if (dto.getApartmentName() != null && !dto.getApartmentName().equals(address.getApartmentName())) {
					address.setApartmentName(dto.getApartmentName());
					address.setAddress(address.getBuildingName() + "-" + address.getApartmentName());
				}
				address.setAreaSize(dto.getAreaSize());
				address.setChargeArea(dto.getChargeArea());
				address.setFreeArea(dto.getFreeArea());
				address.setRentArea(dto.getRentArea());
				addressProvider.updateAddress(address);
			}
		}
	}

	@Override
	public void deleteAddressArrangement(DeleteAddressArrangementCommand cmd) {
		AddressArrangement arrangement = addressProvider.findAddressArrangementById(cmd.getId());
		List<String> targetIds = (List<String>)StringHelper.fromJsonString(arrangement.getTargetId(), ArrayList.class);
		//删除未来房源
		for (String addressId : targetIds) {
			Address futureAddress = addressProvider.findAddressById(Long.parseLong(addressId));
			futureAddress.setStatus(AddressAdminStatus.INACTIVE.getCode());
			addressProvider.updateAddress(futureAddress);
		}
		addressProvider.deleteAddressArrangement(cmd.getId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HistoryApartmentDTO> getHistoryApartment(GetHistoryApartmentCommand cmd) {
		if (cmd.getAddressId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "the addressId should not be null!");
		}
		List<HistoryApartmentDTO> results = new ArrayList<>();
		AddressArrangement arrangement = null;
		List<AddressArrangement> arrangements = addressProvider.findActiveAddressArrangementByTargetIdV2(cmd.getAddressId());
		//因为用的是like匹配，所以需要对数据进一步过滤
		for (AddressArrangement addressArrangement : arrangements) {
			List<String> targetIds = (List<String>)StringHelper.fromJsonString(addressArrangement.getTargetId(), ArrayList.class); 
			if (targetIds.contains(cmd.getAddressId().toString())) {
				arrangement = addressArrangement;
				break;
			}
		}
		if (arrangement != null) {
			List<String> targetIds = (List<String>)StringHelper.fromJsonString(arrangement.getTargetId(), ArrayList.class);
			List<String> originalIds = (List<String>)StringHelper.fromJsonString(arrangement.getOriginalId(), ArrayList.class);
			String remark = null;
			HashMap<String, String> data = new HashMap<>();
			StringBuffer targetIdsStringBuffer= new StringBuffer();
			StringBuffer originalIdsStringBuffer = new StringBuffer();
			for (String addressId : targetIds) {
				String apartmentName = addressProvider.findApartmentNameById(Long.parseLong(addressId));
				targetIdsStringBuffer.append(apartmentName).append(",");
			}
			for (String addressId : originalIds) {
				String apartmentName = addressProvider.findApartmentNameById(Long.parseLong(addressId));
				originalIdsStringBuffer.append(apartmentName).append(",");
			}
			String targetIdsString = targetIdsStringBuffer.toString().length() > 0 
										? targetIdsStringBuffer.toString().substring(0,targetIdsStringBuffer.toString().length() -1) 
										: targetIdsStringBuffer.toString();
			String originalIdsString = originalIdsStringBuffer.toString().length() > 0 
										? originalIdsStringBuffer.toString().substring(0,originalIdsStringBuffer.toString().length() -1) 
										: originalIdsStringBuffer.toString();
			data.put("targetIds", targetIdsString);
			data.put("originalIds", originalIdsString);
			if (arrangement.getOperationType() == AddressArrangementType.SPLIT.getCode()) {
				//需要获取特定的语句：如101被拆分成101A,101B
	            remark = localeTemplateService.getLocaleTemplateString(AddressArrangementTemplateCode.SCOPE,
	            													AddressArrangementTemplateCode.ADDRESS_ARRANGEMENT_SPLIT , 
	            													UserContext.current().getUser().getLocale(), 
	            													data, "");
			}else if (arrangement.getOperationType() == AddressArrangementType.MERGE.getCode()) {
				//需要获取特定的语句：如101,102,103被合并成101
				remark = localeTemplateService.getLocaleTemplateString(AddressArrangementTemplateCode.SCOPE, 
																	AddressArrangementTemplateCode.ADDRESS_ARRANGEMENT_MERGE , 
																	UserContext.current().getUser().getLocale(), 
																	data, "");
			}
			for (String addressId : originalIds) {
				Address address = addressProvider.findAddressById(Long.parseLong(addressId));
				HistoryApartmentDTO dto = ConvertHelper.convert(address, HistoryApartmentDTO.class);
				dto.setAddressId(address.getId());
				dto.setDateBegin(arrangement.getDateBegin().getTime());
				dto.setRemark(remark);
				results.add(dto);
			}
		}
		return results;
	}
	
	@Scheduled(cron = "0 10 0 * * ?")
	//@Scheduled(cron = "0 */1 * * * ?")
	@Override
    public void excuteAddressArrangementOnTime() {
        LOGGER.debug("start excuting addressArrangement!");
		try {
			Calendar calendar = Calendar.getInstance();
			Date time = calendar.getTime();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String format = simpleDateFormat.format(time);
			Date today = simpleDateFormat.parse(format);
			if(RunningFlag.fromCode(scheduleProvider.getRunningFlag())==RunningFlag.TRUE) {
	            coordinationProvider.getNamedLock(CoordinationLocks.EXCUTE_ADDRESS_ARRANGEMENT.getCode()).tryEnter(() -> {
	            	List<AddressArrangement> list = addressProvider.listActiveAddressArrangementToday(new Timestamp(today.getTime()));
	        		for (AddressArrangement arrangement : list) {
	        			arrangement.setOperationFlag((byte) 1);
	        			addressProvider.updateAddressArrangement(arrangement);
	        			//原始房源失效
	        			List<String> originalIds = (List<String>)StringHelper.fromJsonString(arrangement.getOriginalId(), ArrayList.class);
	        			for (String addressId : originalIds) {
	        				Address address = addressProvider.findAddressById(Long.parseLong(addressId));
	        				address.setStatus(AddressAdminStatus.INACTIVE.getCode());
	        				addressProvider.updateAddress(address);
	        				//删除个人客户及企业客户的关联关系
	        		        addressProvider.updateOrganizationAddressMapping(address.getId());
	        		        addressProvider.updateOrganizationAddress(address.getId());
	        		        //addressProvider.updateOrganizationOwnerAddress(address.getId());
	        		        //enterpriseCustomerProvider.deleteCustomerEntryInfoByAddessId(address.getId());
	        		        subBuildingAndCommunityArea(address);
	        			}
	        			//未来房源成为正式房源
	        			List<String> targetIds = (List<String>)StringHelper.fromJsonString(arrangement.getTargetId(), ArrayList.class);
	        			for (String addressId : targetIds) {
	        				Address address = addressProvider.findAddressById(Long.parseLong(addressId));
	        				address.setIsFutureApartment((byte) 0);
	        				addressProvider.updateAddress(address);
	        				addBuildingAndCommunityArea(address);
	        			}
	        		}
	            });
			}
			LOGGER.debug("addressArrangement excuted!");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void subBuildingAndCommunityArea(Address address){
		Community community = checkCommunity(address.getCommunityId());
        Building building = communityProvider.findBuildingByCommunityIdAndName(address.getCommunityId(), address.getBuildingName());
        if (address.getAreaSize() != null) {
            Double buildingAreaSize = building.getAreaSize() == null ? 0.0 : building.getAreaSize();
            building.setAreaSize(buildingAreaSize - address.getAreaSize());

            Double communityAreaSize = community.getAreaSize() == null ? 0.0 : community.getAreaSize();
            community.setAreaSize(communityAreaSize - address.getAreaSize());
        }
        if (address.getRentArea() != null) {
            Double buildingRentArea = building.getRentArea() == null ? 0.0 : building.getRentArea();
            building.setRentArea(buildingRentArea - address.getRentArea());

            Double communityRentArea = community.getRentArea() == null ? 0.0 : community.getRentArea();
            community.setRentArea(communityRentArea - address.getRentArea());
        }
        if (address.getSharedArea() != null) {
            Double buildingSharedArea = building.getSharedArea() == null ? 0.0 : building.getSharedArea();
            building.setSharedArea(buildingSharedArea - address.getSharedArea());

            Double communitySharedArea = community.getSharedArea() == null ? 0.0 : community.getSharedArea();
            community.setSharedArea(communitySharedArea - address.getSharedArea());
        }
//        if (address.getBuildArea() != null) {
//            Double buildingBuildArea = building.getBuildArea() == null ? 0.0 : building.getBuildArea();
//            building.setBuildArea(buildingBuildArea - address.getBuildArea());
//
//            Double communityBuildArea = community.getBuildArea() == null ? 0.0 : community.getBuildArea();
//            community.setBuildArea(communityBuildArea - address.getBuildArea());
//        }
        if (address.getChargeArea() != null) {
            Double buildingChargeArea = building.getChargeArea() == null ? 0.0 : building.getChargeArea();
            building.setChargeArea(buildingChargeArea - address.getChargeArea());

            Double communityChargeArea = community.getChargeArea() == null ? 0.0 : community.getChargeArea();
            community.setChargeArea(communityChargeArea - address.getChargeArea());
        }
        if (address.getFreeArea() != null) {
            Double buildingFreeArea = building.getFreeArea() == null ? 0.0 : building.getFreeArea();
            building.setFreeArea(buildingFreeArea - address.getFreeArea());

            Double communityFreeArea = community.getFreeArea() == null ? 0.0 : community.getFreeArea();
            community.setFreeArea(communityFreeArea - address.getFreeArea());
        }
        communityProvider.updateBuilding(building);
        communityProvider.updateCommunity(community);
	}
	
	private void addBuildingAndCommunityArea(Address address){
		Community community = checkCommunity(address.getCommunityId());
        Building building = communityProvider.findBuildingByCommunityIdAndName(address.getCommunityId(), address.getBuildingName());
        if (address.getAreaSize() != null) {
            Double buildingAreaSize = building.getAreaSize() == null ? 0.0 : building.getAreaSize();
            building.setAreaSize(buildingAreaSize + address.getAreaSize());

            Double communityAreaSize = community.getAreaSize() == null ? 0.0 : community.getAreaSize();
            community.setAreaSize(communityAreaSize + address.getAreaSize());
        }
        if (address.getRentArea() != null) {
            Double buildingRentArea = building.getRentArea() == null ? 0.0 : building.getRentArea();
            building.setRentArea(buildingRentArea + address.getRentArea());

            Double communityRentArea = community.getRentArea() == null ? 0.0 : community.getRentArea();
            community.setRentArea(communityRentArea + address.getRentArea());
        }
        if (address.getSharedArea() != null) {
            Double buildingSharedArea = building.getSharedArea() == null ? 0.0 : building.getSharedArea();
            building.setSharedArea(buildingSharedArea + address.getSharedArea());

            Double communitySharedArea = community.getSharedArea() == null ? 0.0 : community.getSharedArea();
            community.setSharedArea(communitySharedArea + address.getSharedArea());
        }
//        if (address.getBuildArea() != null) {
//            Double buildingBuildArea = building.getBuildArea() == null ? 0.0 : building.getBuildArea();
//            building.setBuildArea(buildingBuildArea + address.getBuildArea());
//
//            Double communityBuildArea = community.getBuildArea() == null ? 0.0 : community.getBuildArea();
//            community.setBuildArea(communityBuildArea + address.getBuildArea());
//        }
        if (address.getChargeArea() != null) {
            Double buildingChargeArea = building.getChargeArea() == null ? 0.0 : building.getChargeArea();
            building.setChargeArea(buildingChargeArea + address.getChargeArea());

            Double communityChargeArea = community.getChargeArea() == null ? 0.0 : community.getChargeArea();
            community.setChargeArea(communityChargeArea + address.getChargeArea());
        }
        if (address.getFreeArea() != null) {
            Double buildingFreeArea = building.getFreeArea() == null ? 0.0 : building.getFreeArea();
            building.setFreeArea(buildingFreeArea + address.getFreeArea());

            Double communityFreeArea = community.getFreeArea() == null ? 0.0 : community.getFreeArea();
            community.setFreeArea(communityFreeArea + address.getFreeArea());
        }
        communityProvider.updateBuilding(building);
        communityProvider.updateCommunity(community);
	}
	
	 private Community checkCommunity(Long communityId) {
	        Community community = communityProvider.findCommunityById(communityId);
	        if (community == null) {
	            LOGGER.error("Unable to find the community");
	            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
	                    "Unable to find the community.");
	        }
	        return community;
	    }
	
	@Override
	public void excuteAddressArrangement(ExcuteAddressArrangementCommand cmd){
		AddressArrangement arrangement = addressProvider.findAddressArrangementById(cmd.getId());
		arrangement.setOperationFlag((byte) 1);
		addressProvider.updateAddressArrangement(arrangement);
		//原始房源失效
		List<String> originalIds = (List<String>)StringHelper.fromJsonString(arrangement.getOriginalId(), ArrayList.class);
		for (String addressId : originalIds) {
			Address address = addressProvider.findAddressById(Long.parseLong(addressId));
			address.setStatus(AddressAdminStatus.INACTIVE.getCode());
			addressProvider.updateAddress(address);
			//删除个人客户及企业客户的关联关系
	        addressProvider.updateOrganizationAddressMapping(address.getId());
	        addressProvider.updateOrganizationAddress(address.getId());
	        //addressProvider.updateOrganizationOwnerAddress(address.getId());
	        //enterpriseCustomerProvider.deleteCustomerEntryInfoByAddessId(address.getId());
	        subBuildingAndCommunityArea(address);
		}
		//未来房源成为正式房源
		List<String> targetIds = (List<String>)StringHelper.fromJsonString(arrangement.getTargetId(), ArrayList.class);
		for (String addressId : targetIds) {
			Address address = addressProvider.findAddressById(Long.parseLong(addressId));
			address.setIsFutureApartment((byte) 0);
			addressProvider.updateAddress(address);
			addBuildingAndCommunityArea(address);
		}
	}

	@Override
	public void exportApartmentsInBuilding(ListPropApartmentsByKeywordCommand cmd, HttpServletResponse response) {
		List<ApartmentDTO> aptList = this.listApartmentsByKeyword(cmd).second();
		if (aptList != null && aptList.size()>0) {
			String fileName = null;
			if (cmd.getCommunityId() != null) {
				Community community = communityProvider.findCommunityById(cmd.getCommunityId());
				fileName = String.format("房源信息_%s_%s", community.getName(),cmd.getBuildingName());
			}else {
				fileName = String.format("房源信息_%s", cmd.getBuildingName());
			}
			ExcelUtils excelUtils = new ExcelUtils(response, fileName, "房源信息");
			excelUtils = excelUtils.setNeedSequenceColumn(false);
			List<ExportApartmentsInBuildingDTO> data = aptList.stream().map(r->{
				ExportApartmentsInBuildingDTO dto = ConvertHelper.convert(r, ExportApartmentsInBuildingDTO.class);
				Byte livingStatus = addressProvider.getAddressLivingStatusByAddressId(r.getAddressId());
				dto.setLivingStatus(AddressMappingStatus.fromCode(livingStatus).getDesc());
				if (dto.getAreaSize()!=null) {
					dto.setAreaSize(doubleRoundHalfUp(dto.getAreaSize(),2));
				}
				if(dto.getRentArea()!=null){
					dto.setRentArea(doubleRoundHalfUp(dto.getRentArea(),2));
				}
				if(dto.getFreeArea()!=null){
					dto.setFreeArea(doubleRoundHalfUp(dto.getFreeArea(),2));
				}
				if(dto.getChargeArea()!=null){
					dto.setChargeArea(doubleRoundHalfUp(dto.getChargeArea(),2));
				}
				return dto;
			}).collect(Collectors.toList());
			
			List<ExportApartmentsInBuildingDTO> filterData = filterExportApartmentsInBuildingDTO(data,cmd);
			
			String[] propertyNames = {"apartmentName", "livingStatus", "apartmentFloor", "areaSize", "rentArea", "freeArea", "chargeArea", "orientation", "namespaceAddressType", "namespaceAddressToken"};
			String[] titleNames = {"*房源", "*状态", "楼层", "建筑面积", "在租面积", "可招租面积", "收费面积", "朝向","第三方来源", "第三方标识"};
			int[] titleSizes = {20, 20, 20, 20, 20, 20, 20, 20, 20, 20};
			excelUtils.writeExcel(propertyNames, titleNames, titleSizes, filterData);
		} else {
			throw errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_NO_DATA,
					"no data");
		}
	}
		
	private List<ExportApartmentsInBuildingDTO> filterExportApartmentsInBuildingDTO(List<ExportApartmentsInBuildingDTO> data,ListPropApartmentsByKeywordCommand cmd){
		List<ExportApartmentsInBuildingDTO>  filterData = new ArrayList<>();
		for(ExportApartmentsInBuildingDTO dto : data){
			filterData.add(dto);
		    //按状态筛选
			if(cmd.getLivingStatus() != null && AddressMappingStatus.fromDesc(dto.getLivingStatus()).getCode() != cmd.getLivingStatus()){
				filterData.remove(dto);
                continue;
			}
			//按楼层筛选
			if (!org.springframework.util.StringUtils.isEmpty(cmd.getApartmentFloor())){
				if (dto.getApartmentFloor()==null || !dto.getApartmentFloor().equals(cmd.getApartmentFloor())) {
					filterData.remove(dto);
	                continue;
				}
			}
			//按建筑面积筛选
			if (cmd.getAreaSizeFrom() != null) {
				if (dto.getAreaSize()==null || dto.getAreaSize().doubleValue() < cmd.getAreaSizeFrom().doubleValue()) {
					filterData.remove(dto);
	                continue;
				}
			}
			if (cmd.getAreaSizeTo() != null) {
				if (dto.getAreaSize()==null || dto.getAreaSize().doubleValue() > cmd.getAreaSizeTo().doubleValue()) {
					filterData.remove(dto);
	                continue;
				}
			}
			//按在租面积筛选
			if (cmd.getRentAreaFrom() != null) {
				if (dto.getRentArea()==null || dto.getRentArea().doubleValue() < cmd.getRentAreaFrom().doubleValue()) {
					filterData.remove(dto);
	                continue;
				}
			}
			if (cmd.getRentAreaTo() != null) {
				if (dto.getRentArea()==null || dto.getRentArea().doubleValue() > cmd.getRentAreaTo().doubleValue()) {
					filterData.remove(dto);
	                continue;
				}
			}
			//按收费面积筛选
			if (cmd.getChargeAreaFrom() != null) {
				if (dto.getChargeArea()==null || dto.getChargeArea().doubleValue() < cmd.getChargeAreaFrom().doubleValue()) {
					filterData.remove(dto);
	                continue;
				}
			}
			if (cmd.getChargeAreaTo() != null ) {
				if (dto.getChargeArea()==null || dto.getChargeArea().doubleValue() > cmd.getChargeAreaTo().doubleValue()) {
					filterData.remove(dto);
	                continue;
				}
			}
			//按可招租面积筛选
			if (cmd.getFreeAreaFrom() != null) {
				if (dto.getFreeArea()==null || dto.getFreeArea().doubleValue() < cmd.getFreeAreaFrom().doubleValue()) {
					filterData.remove(dto);
	                continue;
				}
			}
			if (cmd.getFreeAreaTo() != null) {
				if (dto.getFreeArea()==null || dto.getFreeArea().doubleValue() > cmd.getFreeAreaTo().doubleValue()) {
					filterData.remove(dto);
	                continue;
				}
			}
		}
		return filterData;
	}	
	
	//四舍五入截断double类型数据
	private double doubleRoundHalfUp(double input,int scale){
		BigDecimal digit = new BigDecimal(input); 
		return digit.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
		
}
