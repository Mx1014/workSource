// @formatter:off
package com.everhomes.community;

import com.everhomes.acl.Acl;
import com.everhomes.acl.AclProvider;
import com.everhomes.acl.AclRoleDescriptor;
import com.everhomes.acl.RoleAssignment;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.acl.ServiceModuleAppAuthorization;
import com.everhomes.acl.ServiceModuleAppAuthorizationProvider;
import com.everhomes.acl.ServiceModuleAppAuthorizationService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.asset.AssetService;
import com.everhomes.building.BuildingProvider;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.configuration.Configurations;
import com.everhomes.configuration.ConfigurationsProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.FamilyProvider;
import com.everhomes.filedownload.TaskService;
import com.everhomes.forum.Forum;
import com.everhomes.forum.ForumProvider;
import com.everhomes.group.Group;
import com.everhomes.group.GroupAdminStatus;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupMemberLog;
import com.everhomes.group.GroupMemberLogProvider;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleTemplate;
import com.everhomes.locale.LocaleTemplateProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.module.ServiceModuleAssignment;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceDetail;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.namespace.NamespaceResource;
import com.everhomes.namespace.NamespaceResourceProvider;
import com.everhomes.namespace.NamespacesProvider;
import com.everhomes.namespace.NamespacesService;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.organization.ImportFileService;
import com.everhomes.organization.ImportFileTask;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationDetail;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationMemberLog;
import com.everhomes.organization.OrganizationOwner;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.OrganizationWorkPlaces;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.point.UserLevel;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.address.ApartmentDTO;
import com.everhomes.rest.address.CommunityAdminStatus;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.address.DeleteApartmentCommand;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.asset.AssetTargetType;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.community.ApartmentCountInBuildingDTO;
import com.everhomes.rest.community.ApartmentExportDataDTO;
import com.everhomes.rest.community.ApartmentInfoDTO;
import com.everhomes.rest.community.BuildingAdminStatus;
import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.rest.community.BuildingExportDataDTO;
import com.everhomes.rest.community.BuildingExportDetailDTO;
import com.everhomes.rest.community.BuildingInfoDTO;
import com.everhomes.rest.community.BuildingOrderDTO;
import com.everhomes.rest.community.BuildingServiceErrorCode;
import com.everhomes.rest.community.BuildingStatisticsDTO;
import com.everhomes.rest.community.BuildingStatisticsForAppDTO;
import com.everhomes.rest.community.BuildingStatus;
import com.everhomes.rest.community.CaculateBuildingAreaCommand;
import com.everhomes.rest.community.CaculateCommunityAreaCommand;
import com.everhomes.rest.community.ChangeBuildingOrderCommand;
import com.everhomes.rest.community.ChangeOrganizationCommunitiesCommand;
import com.everhomes.rest.community.CommunityAuthPopupConfigDTO;
import com.everhomes.rest.community.CommunityDetailDTO;
import com.everhomes.rest.community.CommunityGeoPointDTO;
import com.everhomes.rest.community.CommunityInfoDTO;
import com.everhomes.rest.community.CommunityNotificationTemplateCode;
import com.everhomes.rest.community.CommunityServiceErrorCode;
import com.everhomes.rest.community.CommunityStatisticsDTO;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.community.CreateChildProjectCommand;
import com.everhomes.rest.community.CreateResourceCategoryAssignmentCommand;
import com.everhomes.rest.community.CreateResourceCategoryCommand;
import com.everhomes.rest.community.DeleteChildProjectCommand;
import com.everhomes.rest.community.FindNearbyMixCommunityCommand;
import com.everhomes.rest.community.FloorRangeDTO;
import com.everhomes.rest.community.GetBuildingCommand;
import com.everhomes.rest.community.GetBuildingStatisticsCommand;
import com.everhomes.rest.community.GetCommunitiesByIdsCommand;
import com.everhomes.rest.community.GetCommunitiesByNameAndCityIdCommand;
import com.everhomes.rest.community.GetCommunityAuthPopupConfigCommand;
import com.everhomes.rest.community.GetCommunityByIdCommand;
import com.everhomes.rest.community.GetCommunityByUuidCommand;
import com.everhomes.rest.community.GetCommunityDetailCommand;
import com.everhomes.rest.community.GetCommunityStatisticsCommand;
import com.everhomes.rest.community.GetFloorRangeCommand;
import com.everhomes.rest.community.GetNearbyCommunitiesByIdCommand;
import com.everhomes.rest.community.GetTreeProjectCategoriesCommand;
import com.everhomes.rest.community.ImportBuildingDataCommand;
import com.everhomes.rest.community.ImportBuildingDataDTO;
import com.everhomes.rest.community.ImportCommunityDataDTO;
import com.everhomes.rest.community.ListAllCommunitiesResponse;
import com.everhomes.rest.community.ListApartmentsInCommunityCommand;
import com.everhomes.rest.community.ListApartmentsInCommunityResponse;
import com.everhomes.rest.community.ListBuildingCommand;
import com.everhomes.rest.community.ListBuildingCommandResponse;
import com.everhomes.rest.community.ListBuildingsByKeywordsCommand;
import com.everhomes.rest.community.ListBuildingsByKeywordsResponse;
import com.everhomes.rest.community.ListBuildingsForAppCommand;
import com.everhomes.rest.community.ListBuildingsForAppResponse;
import com.everhomes.rest.community.ListChildProjectCommand;
import com.everhomes.rest.community.ListCommunitesByStatusCommand;
import com.everhomes.rest.community.ListCommunitesByStatusCommandResponse;
import com.everhomes.rest.community.ListCommunitiesByCategoryCommand;
import com.everhomes.rest.community.ListCommunitiesByKeywordResponse;
import com.everhomes.rest.community.ListCommunitiesByOrgIdAndAppIdCommand;
import com.everhomes.rest.community.ListCommunitiesByOrgIdAndAppIdResponse;
import com.everhomes.rest.community.ListCommunitiesByOrgIdCommand;
import com.everhomes.rest.community.ListCommunitiesByOrgIdResponse;
import com.everhomes.rest.community.ListCommunitiesCommand;
import com.everhomes.rest.community.ListCommunitiesResponse;
import com.everhomes.rest.community.ListResourceCategoryCommand;
import com.everhomes.rest.community.ResourceCategoryAssignmentDTO;
import com.everhomes.rest.community.ResourceCategoryDTO;
import com.everhomes.rest.community.ResourceCategoryErrorCode;
import com.everhomes.rest.community.ResourceCategoryStatus;
import com.everhomes.rest.community.ResourceCategoryType;
import com.everhomes.rest.community.UpdateBuildingOrderCommand;
import com.everhomes.rest.community.UpdateChildProjectCommand;
import com.everhomes.rest.community.UpdateCommunityAuthPopupConfigCommand;
import com.everhomes.rest.community.UpdateCommunityNewCommand;
import com.everhomes.rest.community.UpdateCommunityRequestStatusCommand;
import com.everhomes.rest.community.admin.ApproveCommunityAdminCommand;
import com.everhomes.rest.community.admin.CheckAuditingType;
import com.everhomes.rest.community.admin.CheckUserAuditingAdminCommand;
import com.everhomes.rest.community.admin.CheckUserAuditingAdminResponse;
import com.everhomes.rest.community.admin.ComOrganizationMemberDTO;
import com.everhomes.rest.community.admin.CommunityAllUserDTO;
import com.everhomes.rest.community.admin.CommunityAuthUserAddressCommand;
import com.everhomes.rest.community.admin.CommunityAuthUserAddressResponse;
import com.everhomes.rest.community.admin.CommunityImportBaseConfigCommand;
import com.everhomes.rest.community.admin.CommunityImportOrganizationConfigCommand;
import com.everhomes.rest.community.admin.CommunityManagerDTO;
import com.everhomes.rest.community.admin.CommunityUserAddressDTO;
import com.everhomes.rest.community.admin.CommunityUserAddressResponse;
import com.everhomes.rest.community.admin.CommunityUserDto;
import com.everhomes.rest.community.admin.CommunityUserOrgDetailDTO;
import com.everhomes.rest.community.admin.CommunityUserResponse;
import com.everhomes.rest.community.admin.CountCommunityUserResponse;
import com.everhomes.rest.community.admin.CountCommunityUsersCommand;
import com.everhomes.rest.community.admin.CreateCommunitiesCommand;
import com.everhomes.rest.community.admin.CreateCommunitiesResponse;
import com.everhomes.rest.community.admin.CreateCommunityCommand;
import com.everhomes.rest.community.admin.CreateCommunityResponse;
import com.everhomes.rest.community.admin.DeleteBuildingAdminCommand;
import com.everhomes.rest.community.admin.DeleteResourceCategoryCommand;
import com.everhomes.rest.community.admin.ExportAllCommunityUsersCommand;
import com.everhomes.rest.community.admin.ExportBatchCommunityUsersCommand;
import com.everhomes.rest.community.admin.GetOrgIdByCommunityIdCommand;
import com.everhomes.rest.community.admin.ImportCommunityCommand;
import com.everhomes.rest.community.admin.ListAllCommunityUserResponse;
import com.everhomes.rest.community.admin.ListAllCommunityUsersCommand;
import com.everhomes.rest.community.admin.ListBuildingsByStatusCommandResponse;
import com.everhomes.rest.community.admin.ListCommunityAuthPersonnelsCommand;
import com.everhomes.rest.community.admin.ListCommunityAuthPersonnelsResponse;
import com.everhomes.rest.community.admin.ListCommunityByNamespaceIdCommand;
import com.everhomes.rest.community.admin.ListCommunityByNamespaceIdResponse;
import com.everhomes.rest.community.admin.ListCommunityManagersAdminCommand;
import com.everhomes.rest.community.admin.ListCommunityUsersCommand;
import com.everhomes.rest.community.admin.ListComunitiesByKeywordAdminCommand;
import com.everhomes.rest.community.admin.ListUserCommunitiesCommand;
import com.everhomes.rest.community.admin.OperateType;
import com.everhomes.rest.community.admin.OrgDTO;
import com.everhomes.rest.community.admin.OrganizationMemberLogDTO;
import com.everhomes.rest.community.admin.QryCommunityUserAddressByUserIdCommand;
import com.everhomes.rest.community.admin.QryCommunityUserAllByUserIdCommand;
import com.everhomes.rest.community.admin.RejectCommunityAdminCommand;
import com.everhomes.rest.community.admin.SmsTemplate;
import com.everhomes.rest.community.admin.UpdateBuildingAdminCommand;
import com.everhomes.rest.community.admin.UpdateCommunityAdminCommand;
import com.everhomes.rest.community.admin.UpdateCommunityPartialAdminCommand;
import com.everhomes.rest.community.admin.UpdateCommunityUserCommand;
import com.everhomes.rest.community.admin.UpdateResourceCategoryCommand;
import com.everhomes.rest.community.admin.UserCommunityDTO;
import com.everhomes.rest.community.admin.VerifyBuildingAdminCommand;
import com.everhomes.rest.community.admin.VerifyBuildingNameAdminCommand;
import com.everhomes.rest.community.admin.listBuildingsByStatusCommand;
import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.rest.group.GroupJoinPolicy;
import com.everhomes.rest.group.GroupMemberDTO;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.group.GroupPostFlag;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.messaging.MetaObjectType;
import com.everhomes.rest.messaging.QuestionMetaObject;
import com.everhomes.rest.namespace.NamespaceCommunityType;
import com.everhomes.rest.namespace.NamespaceResourceType;
import com.everhomes.rest.namespace.admin.NamespaceInfoDTO;
import com.everhomes.rest.openapi.ListAddressesForThirdPartyCommand;
import com.everhomes.rest.openapi.ListAddressesForThirdPartyResponse;
import com.everhomes.rest.openapi.ListBuildingsForThirdPartyCommand;
import com.everhomes.rest.openapi.ListBuildingsForThirdPartyResponse;
import com.everhomes.rest.openapi.ListCommunitiesForThirdPartyCommand;
import com.everhomes.rest.openapi.ListCommunitiesForThirdPartyResponse;
import com.everhomes.rest.organization.AuditAuth;
import com.everhomes.rest.organization.AuthFlag;
import com.everhomes.rest.organization.ExecutiveFlag;
import com.everhomes.rest.organization.ImportFileResultLog;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.organization.ImportFileTaskType;
import com.everhomes.rest.organization.OperationType;
import com.everhomes.rest.organization.OrganizationCommunityDTO;
import com.everhomes.rest.organization.OrganizationCommunityRequestType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationDetailDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberGroupType;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.organization.OrganizationServiceErrorCode;
import com.everhomes.rest.organization.OrganizationStatus;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.organization.PrivateFlag;
import com.everhomes.rest.organization.Status;
import com.everhomes.rest.organization.UserOrganizationStatus;
import com.everhomes.rest.organization.pm.AddressMappingStatus;
import com.everhomes.rest.organization.pm.PropertyErrorCode;
import com.everhomes.rest.region.RegionServiceErrorCode;
import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.NamespaceUserType;
import com.everhomes.rest.user.UserGender;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.UserSourceType;
import com.everhomes.rest.user.UserStatus;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.search.CommunitySearcher;
import com.everhomes.search.UserWithoutConfAccountSearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.servicehotline.ServiceConfiguration;
import com.everhomes.techpark.servicehotline.ServiceConfigurationsProvider;
import com.everhomes.user.EncryptionUtils;
import com.everhomes.user.User;
import com.everhomes.user.UserActivity;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserGroup;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.userOrganization.UserOrganizations;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.DownloadUtils;
import com.everhomes.util.PinYinHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.VersionRange;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import com.everhomes.version.VersionProvider;
import com.everhomes.version.VersionRealm;
import com.everhomes.version.VersionUpgradeRule;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.collect.Sets;
import org.jooq.Condition;
import org.jooq.JoinType;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.everhomes.util.RuntimeErrorException.errorWith;

@Component
public class CommunityServiceImpl implements CommunityService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommunityServiceImpl.class);
	private static final String COMMUNITY_NAME = "communityName";
	@Autowired
	private DbProvider dbProvider;
	@Autowired
	private CommunityProvider communityProvider;
	@Autowired
	private RegionProvider regionProvider;
	@Autowired
	private ConfigurationProvider configurationProvider;
	@Autowired
	private UserProvider userProvider;
	@Autowired
	private MessagingService messagingService;
	@Autowired
	private LocaleTemplateService localeTemplateService;
	@Autowired
	private CommunitySearcher communitySearcher;
	@Autowired
	private ContentServerService contentServerService;
	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private GroupProvider groupProvider;

	@Autowired
	private AddressProvider addressProvider;

	@Autowired
	private NamespaceResourceProvider namespaceResourceProvider;

	@Autowired
	private NamespacesProvider namespacesProvider;

	@Autowired
	private VersionProvider versionProvider;

	@Autowired
    private LocaleTemplateProvider localeTemplateProvider;

	@Autowired
	private ConfigurationsProvider configurationsProvider;

	@Autowired
	private CategoryProvider categoryProvider;

	@Autowired
	private ForumProvider forumProvider;

	@Autowired
	private AclProvider aclProvider;

	@Autowired
	private UserWithoutConfAccountSearcher userSearcher;

	@Autowired
	private ServiceModuleProvider serviceModuleProvider;

	@Autowired
	private  RolePrivilegeService rolePrivilegeService;

	@Autowired
	private  ImportFileService importFileService;

	@Autowired
	private  UserActivityProvider userActivityProvider;

    @Autowired
    private GroupMemberLogProvider groupMemberLogProvider;

    @Autowired
    private ServiceConfigurationsProvider serviceConfigurationsProvider;

	@Autowired
	private ContractProvider contractProvider;

	@Autowired
	private PropertyMgrService propertyMgrService;

	@Autowired
	private AssetService assetService;

	@Autowired
    private NamespaceProvider namespaceProvider;

	@Autowired
	private EnterpriseCustomerProvider customerProvider;

	@Autowired
	private PropertyMgrProvider propertyMgrProvider;

	@Autowired
	private FamilyProvider familyProvider;

    @Autowired
    private ServiceModuleAppAuthorizationService serviceModuleAppAuthorizationService;

    @Autowired
    private ServiceModuleAppAuthorizationProvider serviceModuleAppAuthorizationProvider;

    @Autowired
	private BuildingProvider buildingProvider;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
	private TaskService taskService;
    
    @Autowired
    private NamespacesService namespacesService;

	@Override
	public ListCommunitesByStatusCommandResponse listCommunitiesByStatus(ListCommunitesByStatusCommand cmd) {

		if(cmd.getPageAnchor() == null)
			cmd.setPageAnchor(0L);
		if(cmd.getStatus() == null)
			cmd.setStatus(CommunityAdminStatus.ACTIVE.getCode());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<Community> communities = this.communityProvider.listCommunitiesByStatus(locator, pageSize + 1,
				(loc, query) -> {
					Condition c = Tables.EH_COMMUNITIES.STATUS.eq(cmd.getStatus());
					query.addConditions(c);
					return query;
				});

		Long nextPageAnchor = null;
		if(communities != null && communities.size() > pageSize) {
			communities.remove(communities.size() - 1);
			nextPageAnchor = communities.get(communities.size() -1).getId();
		}
		ListCommunitesByStatusCommandResponse response = new ListCommunitesByStatusCommandResponse();
		response.setNextPageAnchor(nextPageAnchor);

		List<CommunityDTO> communityDTOs = communities.stream().map((c) ->{
			List<CommunityGeoPoint> geoPoints = this.communityProvider.listCommunityGeoPoints(c.getId());
			List<CommunityGeoPointDTO> getPointDTOs = null;
			if(geoPoints != null && !geoPoints.isEmpty()){
				getPointDTOs = geoPoints.stream().map(r -> {
					return ConvertHelper.convert(r, CommunityGeoPointDTO.class);
				}).collect(Collectors.toList());
			}
			CommunityDTO dto = ConvertHelper.convert(c, CommunityDTO.class);
			dto.setGeoPointList(getPointDTOs);
			return dto;
		}).collect(Collectors.toList());

		response.setRequests(communityDTOs);
		return response;
	}


	@Override
	public void updateCommunity(UpdateCommunityAdminCommand cmd) {
		if(cmd.getCommunityId() == null){
			throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
					"Invalid communityId parameter");
		}

//		if(cmd.getGeoPointList() == null || cmd.getGeoPointList().size() <= 0){
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid geoPointList parameter");
//		}
		//校验名字不能重复
		Community communityName = communityProvider.findCommunityByNamespaceIdAndName(cmd.getNamespaceId(), cmd.getName());
		if(communityName != null && !communityName.getId().equals(cmd.getId())){
			LOGGER.error("CommunityName is exist communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NAME_EXIST,
					"CommunityName is exist.");
		}

		Community community = this.communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Community is not found.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST,
					"Community is not found.");
		}
		User user = UserContext.current().getUser();
		long userId = user.getId();

		if(cmd.getAddress() != null && !cmd.getAddress().equals("")){
			community.setAddress(cmd.getAddress());
		}

		Region city = regionProvider.findRegionById(cmd.getCityId());
		if(city == null){
			throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_REGION_NOT_EXIST,
					"Invalid cityId parameter,city is not found.");
		}

		Region area = regionProvider.findRegionById(cmd.getAreaId());
		if(area == null){
			throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_REGION_NOT_EXIST,
					"Invalid areaId parameter,area is not found.");
		}
		if(StringUtils.isNotBlank(cmd.getCommunityNumber())) {
			checkCommunityNumberUnique(community.getId(), cmd.getCommunityNumber(), community.getNamespaceId());
		}
		community.setCommunityNumber(cmd.getCommunityNumber());
		community.setAliasName(cmd.getAliasName());
		community.setAreaId(cmd.getAreaId());
		community.setCityId(cmd.getCityId());
		community.setOperatorUid(userId);
		community.setStatus(CommunityAdminStatus.ACTIVE.getCode());
		community.setAreaName(area.getName());
		community.setCityName(city.getName());
		community.setAreaSize(cmd.getAreaSize());
		community.setName(cmd.getName());
		this.dbProvider.execute((TransactionStatus status) ->  {
			this.communityProvider.updateCommunity(community);
			communitySearcher.feedDoc(community);
			List<CommunityGeoPointDTO> geoList = cmd.getGeoPointList();

			if(geoList != null && geoList.size() > 0){

				List<CommunityGeoPoint> geoPointList = this.communityProvider.listCommunityGeoPoints(cmd.getCommunityId());

				for(int i=0;i<geoList.size();i++){
					CommunityGeoPointDTO geoDto= geoList.get(i);
					if(geoDto.getLatitude() != null && geoDto.getLongitude() != null){
						if(geoDto.getId() != null && geoPointList != null){
							for(int j=0;j<geoPointList.size();j++){
								CommunityGeoPoint geo = geoPointList.get(j);
								if(geo.getId().compareTo(geoDto.getId()) == 0 && geo.getCommunityId().compareTo(geoDto.getCommunityId()) == 0){
									geo.setLatitude(geoDto.getLatitude());
									geo.setLongitude(geoDto.getLongitude());
									geo.setGeohash(GeoHashUtils.encode(geoDto.getLatitude(), geoDto.getLongitude()));
									this.communityProvider.updateCommunityGeoPoint(geo);
									geoPointList.remove(j);
									break;
								}
							}
						}
						else{
							CommunityGeoPoint point = new CommunityGeoPoint();
							point.setCommunityId(cmd.getCommunityId());
							point.setDescription(geoDto.getDescription());
							point.setLatitude(geoDto.getLatitude());
							point.setLongitude(geoDto.getLongitude());
							String geohash = GeoHashUtils.encode(geoDto.getLatitude(), geoDto.getLongitude());
							point.setGeohash(geohash);
							this.communityProvider.createCommunityGeoPoint(point);
						}
					}
				}
				if(geoPointList != null && geoPointList.size() > 0){
					geoPointList.forEach(r -> {
						this.communityProvider.deleteCommunityGeoPoint(r);
					});
				}
			}

			return null;
		});
	}

	private void checkCommunityNumberUnique(Long id, String communityNumber, Integer namespaceId) {
		Community community = communityProvider.findCommunityByCommunityNumber(communityNumber, namespaceId);
		if(community != null && !community.getId().equals(id)) {
			LOGGER.error("Community number is already exsit.communityNumber=" + communityNumber);
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NUMBER_EXIST,
					"Community number is already exsit.");
		}
	}

	private void checkBuildingNumberUnique(Long id, String buildingNumber, Long communityId) {
		Building building = communityProvider.findBuildingByCommunityIdAndNumber(communityId, buildingNumber);
		if(building != null && !building.getId().equals(id)) {
			LOGGER.error("building number is already exsit.buildingNumber=" + buildingNumber);
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_BUILDING_NUMBER_EXIST,
					"building number is already exsit.");
		}
	}

	@Override
	public void approveCommuniy(ApproveCommunityAdminCommand cmd){
		if(cmd.getCommunityId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid communityId parameter");
		}

		Community community = this.communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Community is not found.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST,
					"Community is not found.");
		}
		User user = UserContext.current().getUser();
		long userId = user.getId();

		if(community.getCityId() == null || community.getCityId() == 0L){
		    LOGGER.error("Community missing infomation,cityId is null.communityId=" + cmd.getCommunityId());
            throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_CITY_NOT_EXIST,
                    "Community missing infomation,cityId is null.");
		}
		List<CommunityGeoPoint> geoPoints = this.communityProvider.listCommunityGeoPoints(cmd.getCommunityId());
		if(geoPoints == null || geoPoints.isEmpty()){
		    LOGGER.error("Community missing infomation,community geopoint is null.communityId=" + cmd.getCommunityId());
            throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_GEOPOIN_NOT_EXIST,
                    "Community missing infomation,community geopoint is null.");
		}

		community.setOperatorUid(userId);
		community.setStatus(CommunityAdminStatus.ACTIVE.getCode());
		this.communityProvider.updateCommunity(community);
		//create community index
		this.communitySearcher.feedDoc(community);

		sendNotificationForApproveCommunityByAdmin(userId,community.getCreatorUid(),community.getName());
	}

	private void sendNotificationForApproveCommunityByAdmin(long operatorId, long memberId, String communityName) {
		// send notification to the applicant
		try {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put(COMMUNITY_NAME, communityName);

			User user = userProvider.findUserById(memberId);
			String locale = user.getLocale();
			if(locale == null) locale = "zh_CN";

			// send notification member who applicant
			String scope = CommunityNotificationTemplateCode.SCOPE;
			int code = CommunityNotificationTemplateCode.COMMUNITY_ADD_APPROVE_FOR_APPLICANT;
			String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

			sendCommunityNotification(memberId, notifyTextForApplicant, null, null);

		} catch(Exception e) {
			LOGGER.error("Failed to send notification, operatorId=" + operatorId + ", memberId=" + memberId, e);
		}
	}
	private void sendCommunityNotification(long userId, String message,MetaObjectType metaObjectType, QuestionMetaObject metaObject) {
		if(message != null && message.length() != 0) {
			MessageDTO messageDto = new MessageDTO();
			messageDto.setAppId(AppConstants.APPID_MESSAGING);
			messageDto.setSenderUid(User.SYSTEM_UID);
			messageDto.setChannels(new MessageChannel("user", String.valueOf(userId)));
			messageDto.setMetaAppId(AppConstants.APPID_ADDRESS);
			messageDto.setBody(message);
			Map<String, String> metaMap = new HashMap<String, String>();
			messageDto.setMeta(metaMap);
			messageDto.getMeta().put("body-type", MessageBodyType.TEXT.getCode());
			if(metaObjectType != null && metaObject != null) {
				messageDto.getMeta().put("meta-object-type", metaObjectType.getCode());
				messageDto.getMeta().put("meta-object", StringHelper.toJsonString(metaObject));
			}
			messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, "user",
					String.valueOf(userId), messageDto, MessagingConstants.MSG_FLAG_STORED.getCode());
		}
	}

	@Override
	public void rejectCommunity(RejectCommunityAdminCommand cmd){
		if(cmd.getCommunityId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid communityId parameter");
		}

		Community community = this.communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Community is not found.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST,
					"Community is not found.");
		}
		User user = UserContext.current().getUser();
		long userId = user.getId();

		community.setOperatorUid(userId);
		community.setStatus(CommunityAdminStatus.INACTIVE.getCode());
		community.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		communityProvider.updateCommunity(community);
		sendNotificationForRejectCommunityByAdmin(userId,community.getCreatorUid(),community.getName());
	}

	private void sendNotificationForRejectCommunityByAdmin(long operatorId, long memberId, String communityName) {
		// send notification to the applicant
		try {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put(COMMUNITY_NAME, communityName);

			User user = userProvider.findUserById(memberId);
			String locale = user.getLocale();
			if(locale == null) locale = "zh_CN";

			// send notification member who applicant
			String scope = CommunityNotificationTemplateCode.SCOPE;
			int code = CommunityNotificationTemplateCode.COMMUNITY_ADD_REJECT_FOR_APPLICANT;
			String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

			sendCommunityNotification(memberId, notifyTextForApplicant, null, null);

		} catch(Exception e) {
			LOGGER.error("Failed to send notification, operatorId=" + operatorId + ", memberId=" + memberId, e);
		}
	}


	@Override
	public List<CommunityDTO> getCommunitiesByNameAndCityId(GetCommunitiesByNameAndCityIdCommand cmd){
		if(cmd.getCityId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid cityId parameter");
		}
		Region region = this.regionProvider.findRegionById(cmd.getCityId());
		if(region == null){
			throw RuntimeErrorException.errorWith(RegionServiceErrorCode.SCOPE, RegionServiceErrorCode.ERROR_REGION_NOT_EXIST,
					"City is not found");
		}

		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		List<Community> communities = this.communityProvider.findCommunitiesByNameAndCityId(cmd.getName(),cmd.getCityId(), namespaceId);
		List<CommunityDTO> result = communities.stream().map((r) ->{
			return ConvertHelper.convert(r, CommunityDTO.class);
		}).collect(Collectors.toList());
		return result;

	}

	@Override
	public List<CommunityDTO> getCommunitiesByIds(GetCommunitiesByIdsCommand cmd){
		if(cmd.getIds() == null || cmd.getIds().isEmpty()){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid ids parameter");
		}

		List<Community> communities = this.communityProvider.findCommunitiesByIds(cmd.getIds());
		List<CommunityDTO> result = communities.stream().map((r) ->{
			return ConvertHelper.convert(r, CommunityDTO.class);
		}).collect(Collectors.toList());
		return result;

	}

	@Override
	public void updateCommunityRequestStatus(UpdateCommunityRequestStatusCommand cmd){
		if(cmd.getId() == null || cmd.getRequestStatus() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid id or requestStatus parameter");
		}
		Community community = this.communityProvider.findCommunityById(cmd.getId());
		if(community == null){
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST,
					"Community is not found.");
		}

		community.setRequestStatus(cmd.getRequestStatus());
		this.communityProvider.updateCommunity(community);

	}

	@Override
	public List<CommunityDTO> getNearbyCommunityById(GetNearbyCommunitiesByIdCommand cmd){
		if(cmd.getId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid id parameter");
		}
		Community community = this.communityProvider.findCommunityById(cmd.getId());
		if(community == null){
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST,
					"Community is not found.");
		}
		List<CommunityDTO> result = this.communityProvider.findNearyByCommunityById(UserContext.getCurrentNamespaceId(UserContext.current().getNamespaceId()), cmd.getId()).stream().map((r) ->{
			return ConvertHelper.convert(r, CommunityDTO.class);
		}).collect(Collectors.toList());
		return result;
	}


	@Override
	public CommunityDTO getCommunityById(GetCommunityByIdCommand cmd) {
		if(cmd.getId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid id parameter");
		}

		Community community = this.communityProvider.findCommunityById(cmd.getId());
		if(community == null){
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST,
					"Community is not found.");
		}
		CommunityDTO communityDTO = ConvertHelper.convert(community, CommunityDTO.class);
		List<CommunityGeoPoint> geoPoints = this.communityProvider.listCommunityGeoPoints(cmd.getId());
		List<CommunityGeoPointDTO> getPointDTOs = null;
		if(geoPoints != null && !geoPoints.isEmpty()){
			getPointDTOs = geoPoints.stream().map(r -> {
				return ConvertHelper.convert(r, CommunityGeoPointDTO.class);
			}).collect(Collectors.toList());
			communityDTO.setGeoPointList(getPointDTOs);
		}

		List<OrganizationCommunityDTO> orgcoms = organizationProvider.findOrganizationCommunityByCommunityId(communityDTO.getId());
		if(orgcoms != null && orgcoms.size() > 0){
			Organization org = organizationProvider.findOrganizationById(orgcoms.get(0).getOrganizationId());
			if(org != null){
				communityDTO.setPmOrgId(org.getId());
				communityDTO.setPmOrgName(org.getName());
			}
		}

		return communityDTO;
	}

	@Override
	public CommunityDTO getCommunityForSdkById(GetCommunityByIdCommand cmd) {
		if(cmd.getId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid id parameter");
		}
        Community community = this.communityProvider.findCommunityById(cmd.getId());
        if (community == null) {
            return null;
        }
        CommunityDTO communityDTO = ConvertHelper.convert(community, CommunityDTO.class);
        return communityDTO;
	}


	@Override
	public CommunityDTO getCommunityByUuid(GetCommunityByUuidCommand cmd) {
		if(cmd.getUuid() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid id parameter");
		}

		Community community = this.communityProvider.findCommunityByUuid(cmd.getUuid());
		if(community == null){
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST,
					"Community is not found.");
		}
		CommunityDTO communityDTO = ConvertHelper.convert(community, CommunityDTO.class);
		List<CommunityGeoPoint> geoPoints = this.communityProvider.listCommunityGeoPoints(communityDTO.getId());
		List<CommunityGeoPointDTO> getPointDTOs = null;
		if(geoPoints != null && !geoPoints.isEmpty()){
			getPointDTOs = geoPoints.stream().map(r -> {
				return ConvertHelper.convert(r, CommunityGeoPointDTO.class);
			}).collect(Collectors.toList());
			communityDTO.setGeoPointList(getPointDTOs);
		}

		return communityDTO;
	}


	@Override
	public ListCommunitiesByKeywordResponse listCommunitiesByKeyword(
			ListComunitiesByKeywordAdminCommand cmd) {
		if(cmd.getPageAnchor()==null)
			cmd.setPageAnchor(0L);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		ListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<Community> list = this.communityProvider.listCommunitiesByKeyWord(locator, pageSize+1,cmd.getKeyword(), cmd.getNamespaceId(), cmd.getCommunityType());

		ListCommunitiesByKeywordResponse response = new ListCommunitiesByKeywordResponse();
		if(list != null && list.size() > pageSize){
			list.remove(list.size()-1);
			response.setNextPageAnchor(list.get(list.size()-1).getId());
		}
		if(list != null){
			List<CommunityDTO> resultList = list.stream().map((c) -> {
				return ConvertHelper.convert(c, CommunityDTO.class);
			}).collect(Collectors.toList());

			response.setRequests(resultList);
		}

		return response;
	}


	@Override
	public ListBuildingCommandResponse listBuildings(ListBuildingCommand cmd) {

		Long communityId = cmd.getCommunityId();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
		List<Building> buildings = communityProvider.ListBuildingsByCommunityId(locator, pageSize + 1,communityId, UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), null);
		//过滤富文本
		for (int i = 0; i < buildings.size(); i++) {
			Building building = buildings.get(i);
			String trafficDescription = parseHtml(building.getTrafficDescription());
			String description = parseHtml(building.getDescription());
			building.setDescription(description);
			building.setTrafficDescription(trafficDescription);
        }

		this.communityProvider.populateBuildingAttachments(buildings);

        Long nextPageAnchor = null;
        if(buildings.size() > pageSize) {
        	buildings.remove(buildings.size() - 1);
            nextPageAnchor = buildings.get(buildings.size() - 1).getDefaultOrder();
        }

        List<BuildingDTO> dtoList = buildings.stream().map((r) -> {

        	BuildingDTO dto = ConvertHelper.convert(r, BuildingDTO.class);

			populateBuilding(dto, r);

        	return dto;
        }).collect(Collectors.toList());

        if(CollectionUtils.isNotEmpty(dtoList)){

			//由于之前在返回给前端的楼栋信息中没有门牌的数量，所以在这里添加上
			//遍历楼栋的集合
			for(BuildingDTO buildingDTO : dtoList){
				//拿到每一个楼栋的名称，然后根据楼栋名称和项目编号查询eh_addresses表中的门牌总数
				//// TODO: 2018/5/11
				int apartmentCount = addressProvider.getApartmentCountByBuildNameAndCommunityId(buildingDTO.getName(),cmd.getCommunityId());
				buildingDTO.setApartmentCount(apartmentCount);
			}
		}

        return new ListBuildingCommandResponse(nextPageAnchor, dtoList);
	}

	//处理富文本方法
	public static String parseHtml(String html) {
		if (html == null || html == "") {
			return html = "";
		} else {
			html = html.replaceAll("<.*?>", " ").replaceAll(" ", "");
			html = html.replaceAll("<.*?", "");
			return html;
		}
	}

	private void processDetailUrl(BuildingDTO dto) {
		String homeUrl = configurationProvider.getValue(ConfigConstants.HOME_URL, "");
		String detailUrl = configurationProvider.getValue(ConfigConstants.APPLY_ENTRY_BUILDING_DETAIL_URL, "");

		detailUrl = String.format(detailUrl, dto.getId());

		dto.setDetailUrl(homeUrl + detailUrl);

	}

	@Override
	public void exportBuildingByCommunityId(ListBuildingCommand cmd, HttpServletResponse response) {
		cmd.setPageSize(10000);
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
        if (community == null) {
            LOGGER.error("Community is not exist.");
            throw errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST,
                    "Community is not exist.");
        }
		List<BuildingDTO> buildings = listBuildings(cmd).getBuildings();
		if (buildings != null && buildings.size() > 0) {
			String fileName = String.format("楼栋信息_%s", community.getName(), com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.NO_SLASH));
			ExcelUtils excelUtils = new ExcelUtils(response, fileName, "楼栋信息");

			List<BuildingExportDetailDTO> data = buildings.stream().map(this::convertToExportDetail).collect(Collectors.toList());
			excelUtils.setNeedTitleRemark(true).setTitleRemark("填写注意事项：（未按照如下要求填写，会导致数据不能正常导入）\n" +
                    "1、请不要修改此表格的格式，包括插入删除行和列、合并拆分单元格等。需要填写的单元格有字段规则校验，请按照要求输入。\n" +
                    "2、请在表格里面逐行录入数据，建议一次最多导入400条信息。\n" +
                    "3、请不要随意复制单元格，这样会破坏字段规则校验。\n" +
                    "4、带有星号（*）的红色字段为必填项。\n" +
                    "5、导入已存在的楼栋（楼栋名称相同认为是已存在的楼栋），将按照导入的楼栋信息更新系统已存在的楼栋信息。\n" +
                    "\n", (short) 13, (short) 2500).setNeedSequenceColumn(false).setIsCellStylePureString(true);
			String[] propertyNames = {"name", "buildingNumber", "aliasName", "address", "latitudeLongitude", "trafficDescription", "managerName", "contact", "areaSize", "description"};
			String[] titleNames = {"*楼栋名称", "楼栋编号", "简称", "*地址", "经纬度", "交通说明", "*联系人", "*联系电话","面积（平米）", "楼栋介绍"};

			int[] titleSizes = {20, 20, 20, 20, 20, 20, 20, 20, 20, 20};
			excelUtils.writeExcel(propertyNames, titleNames, titleSizes, data);
		} else {
			throw errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_NO_DATA,
					"no data");
		}
	}

	private BuildingExportDetailDTO convertToExportDetail(BuildingDTO dto) {
		BuildingExportDetailDTO exportDetailDTO = ConvertHelper.convert(dto, BuildingExportDetailDTO.class);
		try {
			exportDetailDTO.setName(dto.getBuildingName());
			Community community = communityProvider.findCommunityById(dto.getCommunityId());
			if(community != null) {
				exportDetailDTO.setCommuntiyName(community.getName());
			}

			if(dto.getLatitude() != null && dto.getLongitude() != null) {
				exportDetailDTO.setLatitudeLongitude(dto.getLongitude() + "," + dto.getLatitude());
			}
		} catch (Exception e) {
			LOGGER.error("dto : {}", dto);
			throw e;
		}

		return exportDetailDTO;
	}

	@Override
	public BuildingDTO getBuilding(GetBuildingCommand cmd) {

		Building building = communityProvider.findBuildingById(cmd.getBuildingId());
		if(building != null) {
            if(BuildingStatus.ACTIVE != BuildingStatus.fromCode(building.getStatus())) {

        		LOGGER.error("Building isalready deleted");
        		throw RuntimeErrorException.errorWith(BuildingServiceErrorCode.SCOPE,
        				BuildingServiceErrorCode.ERROR_BUILDING_DELETED, "Building already deleted");
            }
			this.communityProvider.populateBuildingAttachments(building);

			BuildingDTO dto = ConvertHelper.convert(building, BuildingDTO.class);

			populateBuilding(dto, building);

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
		}else {
            LOGGER.error("Building not found");
            throw RuntimeErrorException.errorWith(BuildingServiceErrorCode.SCOPE,
            		BuildingServiceErrorCode.ERROR_BUILDING_NOT_FOUND, "Building not found");
        }
	}

	/**
	 * 填充楼栋信息
	 */
	 private void populateBuilding(BuildingDTO dto, Building building) {
		 if(building == null) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("The building is null");
            }
		 } else {

			 dto.setBuildingName(dto.getName());
			 dto.setName(StringUtils.isBlank(dto.getAliasName()) ? dto.getName() : dto.getName());

		     String posterUri = building.getPosterUri();
             if(posterUri != null && posterUri.length() > 0) {
                 try{
                     String url = contentServerService.parserUri(posterUri, EntityType.USER.getCode(), UserContext.current().getUser().getId());
					 dto.setPosterUrl(url);
                 }catch(Exception e){
                     LOGGER.error("Failed to parse building poster uri, buildingId=" + building.getId() + ", posterUri=" + posterUri, e);
                 }
             }

//			 populateBuildingCreatorInfo(building);
			 populateBuildingManagerInfo(dto);
//			 populateBuildingOperatorInfo(building);

			 populateBuildingAttachments(dto, building.getAttachments());

			 processDetailUrl(dto);
		 }
	 }


	 private void populateBuildingCreatorInfo(BuildingDTO building) {

		 String creatorNickName = building.getCreatorNickName();
         String creatorAvatar = building.getCreatorAvatar();

         User creator = userProvider.findUserById(building.getCreatorUid());
         if(creator != null) {
             if(creatorNickName == null || creatorNickName.trim().length() == 0) {
            	 building.setCreatorNickName(creator.getNickName());
             }
             if(creatorAvatar == null || creatorAvatar.trim().length() == 0) {
            	 building.setCreatorAvatar(creator.getAvatar());
             }
         }
         creatorAvatar = building.getCreatorAvatar();
         if(creatorAvatar != null && creatorAvatar.length() > 0) {
             String avatarUrl = contentServerService.parserUri(creatorAvatar, EntityType.USER.getCode(), UserContext.current().getUser().getId());
             building.setCreatorAvatarUrl(avatarUrl);
         }

	 }

	 private void populateBuildingManagerInfo(BuildingDTO building) {

         if(null == building.getManagerUid()){
        	 return;
         }

		 OrganizationMember member = organizationProvider.findOrganizationMemberById(building.getManagerUid());
		 if(null != member){
			 building.setManagerNickName(member.getContactName());
			 building.setManagerName(member.getContactName());
			 //modify by wh 2016-11-8 加了管理员电话新字段
			 //dto.setContact(member.getContactToken());
			 building.setManagerContact(member.getContactToken());
		 }

	 }

	 private void populateBuildingOperatorInfo(BuildingDTO building) {

		 String operatorNickName = building.getOperateNickName();
         String operatorAvatar = building.getOperateAvatar();
         if(building.getOperatorUid() != null) {

	         User operator = userProvider.findUserById(building.getOperatorUid());
	         if(operator != null) {
	             if(operatorNickName == null || operatorNickName.trim().length() == 0) {
	            	 building.setOperateNickName(operator.getNickName());
	             }
	             if(operatorAvatar == null || operatorAvatar.trim().length() == 0) {
	            	 building.setOperateAvatar(operator.getAvatar());
	             }
	         }
	         operatorAvatar = building.getOperateAvatar();
	         if(operatorAvatar != null && operatorAvatar.length() > 0) {
	             String avatarUrl = contentServerService.parserUri(operatorAvatar, EntityType.USER.getCode(), UserContext.current().getUser().getId());
	             building.setOperateAvatarUrl(avatarUrl);
	         }
         }
	 }

	 private void populateBuildingAttachments(BuildingDTO building, List<BuildingAttachment> attachmentList) {

		 if(attachmentList == null || attachmentList.size() == 0) {
	            if(LOGGER.isInfoEnabled()) {
	                LOGGER.info("The building attachment list is empty, buildingId=" + building.getId());
	            }
		 } else {
	            for(BuildingAttachment attachment : attachmentList) {
					populateBuildingAttachment(building, attachment);
	            }
		 }
	 }

	 private void populateBuildingAttachment(BuildingDTO building, BuildingAttachment attachment) {

		 if(attachment == null) {
			 if(LOGGER.isInfoEnabled()) {
				 LOGGER.info("The building attachment is null, buildingId=" + building.getId());
			 }
		 } else {
			 String contentUri = attachment.getContentUri();
			 if(contentUri != null && contentUri.length() > 0) {
				 try{
					 String url = contentServerService.parserUri(contentUri, EntityType.USER.getCode(), UserContext.current().getUser().getId());
					 attachment.setContentUrl(url);
				 }catch(Exception e){
					 LOGGER.error("Failed to parse attachment uri, buildingId=" + building.getId() + ", attachmentId=" + attachment.getId(), e);
				 }
			 } else {
				 if(LOGGER.isWarnEnabled()) {
					 LOGGER.warn("The content uri is empty, attachmentId=" + attachment.getId());
				 }
			 }
		 }
	 }


	@Override
	public BuildingDTO updateBuilding(UpdateBuildingAdminCommand cmd) {

		//首先需要根据namespaceId和communityId来进行查询数据库eh_building中是否存在楼栋名称相同的楼栋
		List<com.everhomes.building.Building> buildingList = buildingProvider.getBuildingByCommunityIdAndNamespaceId(cmd.getCommunityId(),cmd.getNamespaceId(),cmd.getName());
		//非空校验 exclude itself
		if(CollectionUtils.isNotEmpty(buildingList) && buildingList.size()>1){
			//说明库里面之前就有和该楼栋名称相同的楼栋，那么就不能用该名称作为新建的楼栋，直接给前端报错
			LOGGER.error("the buildingName has been used");
			throw RuntimeErrorException.errorWith(BuildingServiceErrorCode.SCOPE, BuildingServiceErrorCode.ERROR_BUILDINGNAME_HASBEENUSED,
					"the buildingName has been used");
		}
		Building building = ConvertHelper.convert(cmd, Building.class);

		building.setStatus(CommunityAdminStatus.ACTIVE.getCode());
		building.setNamespaceId(null == cmd.getNamespaceId() ? Namespace.DEFAULT_NAMESPACE : cmd.getNamespaceId());
		if(StringUtils.isNotBlank(cmd.getGeoString())){
			String[] geoString = cmd.getGeoString().split(",");
			double longitude = Double.valueOf(geoString[0]);
			double latitude = Double.valueOf(geoString[1]);
			building.setLatitude(latitude);
			building.setLongitude(longitude);
			String geohash = GeoHashUtils.encode(latitude, longitude);
			building.setGeohash(geohash);
		}
		if(cmd.getEntryDate() != null) {
			building.setEntryDate(new Timestamp(cmd.getEntryDate()));
		}

		if(StringUtils.isNotBlank(cmd.getBuildingNumber())){
			checkBuildingNumberUnique(building.getId(), cmd.getBuildingNumber(), building.getCommunityId());
		}
		building.setBuildingNumber(cmd.getBuildingNumber());

		Long userId = UserContext.currentUserId();

		dbProvider.execute((TransactionStatus status) -> {
			if (cmd.getId() == null) {
				assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_CREATE_BUILDING, cmd.getOrganizationId(), cmd.getCommunityId());

				//在该方法开头已经检验过了，因此这里检查园区下是否有同名的楼栋的代码可以注释掉
				//检查园区下是否有同名的楼栋
				//checkBuildingNameUnique(cmd.getName(), cmd.getCommunityId());
				LOGGER.info("add building, cmd={}", cmd);
				this.communityProvider.createBuilding(userId, building);
			} else {
				assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_UPDATE_BUILDING, cmd.getOrganizationId(), cmd.getCommunityId());

				LOGGER.info("update building, cmd={}", cmd);
				Building b = this.communityProvider.findBuildingById(cmd.getId());
				building.setCreatorUid(b.getCreatorUid());
				building.setCreateTime(b.getCreateTime());
				building.setNamespaceId(b.getNamespaceId());
				this.communityProvider.updateBuilding(building);
			}
			processBuildingAttachments(userId, cmd.getAttachments(), building);

			return null;
		});

		BuildingDTO dto = ConvertHelper.convert(building, BuildingDTO.class);

		populateBuilding(dto, building);

		return dto;
	}

	private void checkBuildingNameUnique(String buildingName, Long communityId) {
		Building building = communityProvider.findBuildingByCommunityIdAndName(communityId, buildingName);
		if(building != null) {
			LOGGER.error("building name already exsits.buildingName=" + buildingName);
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_BUILDING_NAME_EXIST,
					"building name already exsits.");
		}
	}


	@Override
	public void deleteBuilding(DeleteBuildingAdminCommand cmd) {

		assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_DELETE_BUILDING, cmd.getOrganizationId(), cmd.getCommunityId());
		
		Building building = this.communityProvider.findBuildingById(cmd.getBuildingId());
//		1.若楼栋或楼栋下的门牌关联了合同或其他业务（如车辆、服务等），则
//		不允许删除；
//		2.若楼栋或楼栋下的门牌没有做任何关联，则删除楼栋时，连同楼栋下的
//		子节点一同删除；
		dbProvider.execute((TransactionStatus status) -> {
			if(building != null) {
				List<Contract> contracts = contractProvider.listContractByBuildingName(building.getName(), building.getCommunityId());
				if(contracts != null && contracts.size() > 0) {
					contracts.forEach(contract -> {
						if(contract.getStatus() == ContractStatus.ACTIVE.getCode() || contract.getStatus() == ContractStatus.WAITING_FOR_LAUNCH.getCode()
								|| contract.getStatus() == ContractStatus.WAITING_FOR_APPROVAL.getCode() || contract.getStatus() == ContractStatus.APPROVE_QUALITIED.getCode()
								|| contract.getStatus() == ContractStatus.EXPIRING.getCode() || contract.getStatus() == ContractStatus.DRAFT.getCode()) {
							LOGGER.error("the building has attach to contract. address id: {}", cmd.getBuildingId());
							throw RuntimeErrorException.errorWith(BuildingServiceErrorCode.SCOPE, BuildingServiceErrorCode.ERROR_BUILDING_HAS_CONTRACT,
									"the building has attach to contract");
						}
					});
				}
				List<ApartmentDTO> apartments = addressProvider.listApartmentsByBuildingName(building.getCommunityId(), building.getName() , 0 , Integer.MAX_VALUE-1);
				if(apartments != null && apartments.size() > 0) {
					apartments.forEach(apartmentDTO -> {
						DeleteApartmentCommand command = new DeleteApartmentCommand();
						command.setId(apartmentDTO.getAddressId());
						propertyMgrService.deleteApartment(command);
					});
				}
				//删除楼宇下的房源与企业客户的关联
				//customerProvider.deleteCustomerEntryInfoByBuildingId(building.getId());
				//删除楼栋时，用置状态的方式代替直接删除楼栋的方式(影响较大) by tangcen 2018年8月5日15:14:43
				//this.communityProvider.deleteBuilding(building);
				building.setStatus(BuildingAdminStatus.INACTIVE.getCode());
				this.communityProvider.updateBuilding(building);
			}
			return null;
		});

	}


	@Override
	public Boolean verifyBuildingName(VerifyBuildingNameAdminCommand cmd) {
		Boolean isValid = this.communityProvider.verifyBuildingName(cmd.getCommunityId(), cmd.getBuildingName());
		return isValid;
	}

    private void processBuildingAttachments(Long userId, List<AttachmentDescriptor> attachmentList, Building building) {

        this.communityProvider.deleteBuildingAttachmentsByBuildingId(building.getId());

        if(attachmentList != null) {
			List<BuildingAttachment> results = new ArrayList<BuildingAttachment>();

            BuildingAttachment attachment = null;
            for(AttachmentDescriptor descriptor : attachmentList) {
                attachment = new BuildingAttachment();
                attachment.setCreatorUid(userId);
                attachment.setBuildingId(building.getId());
                attachment.setContentType(descriptor.getContentType());
                attachment.setContentUri(descriptor.getContentUri());
                attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

                // Make sure we can save as many attachments as possible even if any of them failed
                try {
                	this.communityProvider.createBuildingAttachment(attachment);
                    results.add(attachment);
                } catch(Exception e) {
                    LOGGER.error("Failed to save the attachment, userId={}, attachment={}", userId, attachment, e);
                }
            }

            building.setAttachments(results);
        }
    }


	@Override
	public List<CommunityManagerDTO> getCommunityManagers(
			ListCommunityManagersAdminCommand cmd) {

		List<OrganizationMember> member = organizationProvider.listOrganizationMembersByOrgId(cmd.getOrganizationId());

		List<CommunityManagerDTO> managers = member.stream().map(r -> {
			CommunityManagerDTO dto = new CommunityManagerDTO();
			dto.setManagerId(r.getTargetId());
			dto.setManagerName(r.getContactName());
			dto.setManagerPhone(r.getContactToken());
			return dto;
		}).collect(Collectors.toList());

		return managers;
	}


	@Override
	public List<UserCommunityDTO> getUserCommunities(
			ListUserCommunitiesCommand cmd) {

		List<OrganizationCommunity> communities = new ArrayList<OrganizationCommunity>();
		List<OrganizationMember> members = organizationProvider.listOrganizationMembers(cmd.getUserId());

		for(OrganizationMember member : members) {
			List<OrganizationCommunity> oc = organizationProvider.listOrganizationCommunities(member.getOrganizationId());
			communities.addAll(oc);
		}

		List<UserCommunityDTO> usercommunities = communities.stream().map(r -> {
			UserCommunityDTO dto = new UserCommunityDTO();
			dto.setCommunityId(r.getCommunityId());
			dto.setOrganizationId(r.getOrganizationId());

			Community community = communityProvider.findCommunityById(r.getCommunityId());
			dto.setCommunityName(community.getName());
			return dto;
		}).collect(Collectors.toList());

		return usercommunities;
	}


	@Override
	public void approveBuilding(VerifyBuildingAdminCommand cmd) {
		if(cmd.getBuildingId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid buildingId parameter");
		}

		Building building = this.communityProvider.findBuildingById(cmd.getBuildingId());
		if(building == null){
			LOGGER.error("Building is not found.buildingId=" + cmd.getBuildingId());
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_BUILDING_NOT_EXIST,
					"Building is not found.");
		}
		User user = UserContext.current().getUser();
		long userId = user.getId();

		if(building.getCommunityId() == null || building.getCommunityId().longValue() == 0){
		    LOGGER.error("Building missing infomation,communityId is null.buildingId=" + cmd.getBuildingId());
            throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_BUILDING_COMMUNITY_NOT_EXIST,
                    "Building missing infomation,communityId is null.");
		}


		building.setOperatorUid(userId);
		building.setStatus(CommunityAdminStatus.ACTIVE.getCode());
		this.communityProvider.updateBuilding(building);
	}


	@Override
	public void rejectBuilding(VerifyBuildingAdminCommand cmd) {

		if(cmd.getBuildingId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid buildingId parameter");
		}

		Building building = this.communityProvider.findBuildingById(cmd.getBuildingId());
		if(building == null){
			LOGGER.error("Building is not found.buildingId=" + cmd.getBuildingId());
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_BUILDING_NOT_EXIST,
					"Building is not found.");
		}
		User user = UserContext.current().getUser();
		long userId = user.getId();

		building.setOperatorUid(userId);
		building.setStatus(CommunityAdminStatus.ACTIVE.getCode());
		building.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		this.communityProvider.updateBuilding(building);
	}


	@Override
	public ListBuildingsByStatusCommandResponse listBuildingsByStatus(
			listBuildingsByStatusCommand cmd) {

		if(cmd.getPageAnchor() == null)
			cmd.setPageAnchor(0L);
		if(cmd.getStatus() == null)
			cmd.setStatus(CommunityAdminStatus.ACTIVE.getCode());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<Building> buildings = this.communityProvider.listBuildingsByStatus(locator, pageSize + 1,
				(loc, query) -> {
					Condition c = Tables.EH_BUILDINGS.STATUS.eq(cmd.getStatus());
					query.addConditions(c);
					return query;
				});

		Long nextPageAnchor = null;
		if(buildings != null && buildings.size() > pageSize) {
			buildings.remove(buildings.size() - 1);
			nextPageAnchor = buildings.get(buildings.size() -1).getId();
		}
		ListBuildingsByStatusCommandResponse response = new ListBuildingsByStatusCommandResponse();
		response.setNextPageAnchor(nextPageAnchor);

		List<BuildingDTO> buildingDTOs = buildings.stream().map((c) ->{
			BuildingDTO dto = ConvertHelper.convert(c, BuildingDTO.class);
			return dto;
		}).collect(Collectors.toList());

		response.setBuildings(buildingDTOs);
		return response;
	}


/*	原楼栋导入方法
 * @Override
	public ImportFileTaskDTO importBuildingData(Long communityId, MultipartFile file) {
		Long userId = UserContext.current().getUser().getId();
		ImportFileTask task = new ImportFileTask();
		try {
			//解析excel
			List resultList = PropMrgOwnerHandler.processorExcel(file.getInputStream());

			if(null == resultList || resultList.isEmpty()){
				LOGGER.error("File content is empty。userId="+userId);
				throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_FILE_IS_EMPTY,
						"File content is empty");
			}
			task.setOwnerType(EntityType.COMMUNITY.getCode());
			task.setOwnerId(communityId);
			task.setType(ImportFileTaskType.BUILDING.getCode());
			task.setCreatorUid(userId);
			task = importFileService.executeTask(() -> {
					ImportFileResponse response = new ImportFileResponse();
					List<ImportBuildingDataDTO> datas = handleImportBuildingData(resultList);
					if(datas.size() > 0){
						//设置导出报错的结果excel的标题
						response.setTitle(datas.get(0));
						datas.remove(0);
					}
					List<ImportFileResultLog<ImportBuildingDataDTO>> results = importBuildingData(datas, userId, communityId);
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
	*/

	@Override
	public ImportFileTaskDTO importBuildingData(ImportBuildingDataCommand cmd, MultipartFile file) {
		assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_IMPORT_BUILDING, cmd.getOrganizationId(), cmd.getCommunityId());
		
		Long userId = UserContext.current().getUser().getId();
		ImportFileTask task = new ImportFileTask();
		try {
			//解析excel
			List resultList = PropMrgOwnerHandler.processorExcel(file.getInputStream());

			if(null == resultList || resultList.isEmpty()){
				LOGGER.error("File content is empty。userId="+userId);
				throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_FILE_IS_EMPTY,
						"File content is empty");
			}
			task.setOwnerType(EntityType.COMMUNITY.getCode());
			task.setOwnerId(cmd.getCommunityId());
			task.setType(ImportFileTaskType.BUILDING.getCode());
			task.setCreatorUid(userId);
			task = importFileService.executeTask(() -> {
					ImportFileResponse response = new ImportFileResponse();
					List<ImportBuildingDataDTO> datas = handleImportBuildingData(resultList);
					if(datas.size() > 0){
						//设置导出报错的结果excel的标题
						response.setTitle(datas.get(0));
						datas.remove(0);
					}
					List<ImportFileResultLog<ImportBuildingDataDTO>> results = importBuildingData(datas, userId, cmd.getCommunityId());
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

	private List<ImportFileResultLog<ImportBuildingDataDTO>> importBuildingData(List<ImportBuildingDataDTO> datas,Long userId, Long communityId) {
		OrganizationDTO org = this.organizationService.getUserCurrentOrganization();
		Community community = communityProvider.findCommunityById(communityId);
		List<OrganizationMember> orgMem = this.organizationProvider.listOrganizationMembersByOrgId(org.getId());
		Map<String, OrganizationMember> ct = new HashMap<String, OrganizationMember>();
		if(orgMem != null) {
			orgMem.stream().map(r -> {
				ct.put(r.getContactToken(), r);
				return null;
			});
		}
		List<ImportFileResultLog<ImportBuildingDataDTO>> list = new ArrayList<>();
		for (ImportBuildingDataDTO data : datas) {
			ImportFileResultLog<ImportBuildingDataDTO> log = checkData(data,communityId);
			if (log != null) {
				list.add(log);
				continue;
			}

			Building building = communityProvider.findBuildingByCommunityIdAndName(communityId, data.getName());



			if (building == null) {
				building = new Building();
				building.setName(data.getName());
				building.setBuildingNumber(data.getBuildingNumber());
				building.setAliasName(data.getAliasName());
				building.setAddress(data.getAddress());
				building.setManagerName(data.getContactor());
				building.setContact(data.getPhone());
				if (StringUtils.isNotBlank(data.getFloorNumber())) {
					building.setFloorNumber(Integer.valueOf(data.getFloorNumber()));
				}
				String contactToken = data.getPhone();
				if(ct.get(contactToken) != null) {
					OrganizationMember om = ct.get(contactToken);
					building.setManagerUid(om.getTargetId());
				}else {
					///////////////////////////////////
				}
				building.setCommunityId(communityId);
				building.setDescription(data.getDescription());
				building.setTrafficDescription(data.getTrafficDescription());
//				building.setNamespaceBuildingType(data.getNamespaceBuildingType());
//				building.setNamespaceBuildingToken(data.getNamespaceBuildingToken());
				if (StringUtils.isNotEmpty(data.getLongitudeLatitude())) {
					String[] temp = data.getLongitudeLatitude().replace("，", ",").replace("、", ",").split(",");
					building.setLongitude(Double.parseDouble(temp[0]));
					building.setLatitude(Double.parseDouble(temp[1]));
				}

				building.setNamespaceId(community.getNamespaceId());
				building.setStatus(BuildingAdminStatus.ACTIVE.getCode());

				communityProvider.createBuilding(userId, building);
			}else {
				building.setName(data.getName());
				building.setAddress(data.getAddress());
				building.setManagerName(data.getContactor());
				building.setContact(data.getPhone());
				if (StringUtils.isNotBlank(data.getFloorNumber())) {
					building.setFloorNumber(Integer.valueOf(data.getFloorNumber()));
				}
				String contactToken = data.getPhone();
				if(ct.get(contactToken) != null) {
					OrganizationMember om = ct.get(contactToken);
					building.setManagerUid(om.getTargetId());
				}else {
					///////////////////////////////////
				}
				if (StringUtils.isNotBlank(data.getDescription())) {
					building.setDescription(data.getDescription());
				}
				if (StringUtils.isNotBlank(data.getTrafficDescription())) {
					building.setTrafficDescription(data.getTrafficDescription());
				}
				if (StringUtils.isNotEmpty(data.getLongitudeLatitude())) {
					String[] temp = data.getLongitudeLatitude().replace("，", ",").replace("、", ",").split(",");
					building.setLongitude(Double.parseDouble(temp[0]));
					building.setLatitude(Double.parseDouble(temp[1]));
				} else {
					building.setLongitude(null);
					building.setLatitude(null);
				}
				if (StringUtils.isNotBlank(data.getBuildingNumber())) {
					building.setBuildingNumber(data.getBuildingNumber());
				}
				if (StringUtils.isNotBlank(data.getAliasName())) {
					building.setAliasName(data.getAliasName());
				}


//				building.setNamespaceBuildingType(data.getNamespaceBuildingType());
//				building.setNamespaceBuildingToken(data.getNamespaceBuildingToken());
				building.setNamespaceId(community.getNamespaceId());
				building.setStatus(CommunityAdminStatus.ACTIVE.getCode());
				building.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

				communityProvider.updateBuilding(building);
			}
		}
		return list;
	}


	private ImportFileResultLog<ImportBuildingDataDTO> checkData(ImportBuildingDataDTO data,Long communityId) {
		ImportFileResultLog<ImportBuildingDataDTO> log = new ImportFileResultLog<>(CommunityServiceErrorCode.SCOPE);
		//必填项校检
		if (StringUtils.isEmpty(data.getName())) {
			log.setCode(CommunityServiceErrorCode.ERROR_BUILDING_NAME_EMPTY);
			log.setData(data);
			log.setErrorLog("building name cannot be empty");
			return log;
		}
		LOGGER.info(String.valueOf(data.getName().length()));
		//校验楼栋名称的长度不能大于20个汉字
		if(data.getName().length() > 20){
			log.setCode(CommunityServiceErrorCode.ERROR_BUILDING_NAME_OVER_FLOW);
			log.setData(data);
			log.setErrorLog("building name cannot over than 20");
			return log;
		}

		//merge conflic
//
//		//进行非空校验
//		if(building != null){
//			if(building.getName().equals(data.getName())){
//				log.setCode(CommunityServiceErrorCode.ERROR_BUILDING_NAME_REPEATED);
//				log.setData(data);
//				log.setErrorLog("building name is repeat");
//				return log;
//			}
//		}



/*		if (StringUtils.isEmpty(data.getAddress())) {
			log.setCode(CommunityServiceErrorCode.ERROR_ADDRESS_EMPTY);
			log.setData(data);
			log.setErrorLog("address cannot be empty");
			return log;
		}*/


		if (StringUtils.isEmpty(data.getContactor())) {
			log.setCode(CommunityServiceErrorCode.ERROR_CONTACTOR_EMPTY);
			log.setData(data);
			log.setErrorLog("contactor cannot be empty");
			return log;
		}
		if (StringUtils.isEmpty(data.getPhone())) {
			log.setCode(CommunityServiceErrorCode.ERROR_PHONE_EMPTY);
			log.setData(data);
			log.setErrorLog("phone cannot be empty");
			return log;
		}
		if (StringUtils.isNotEmpty(data.getLongitudeLatitude()) && !data.getLongitudeLatitude().replace("，", ",").replace("、", ",").contains(",")) {
			log.setCode(CommunityServiceErrorCode.ERROR_LATITUDE_LONGITUDE);
			log.setData(data);
			log.setErrorLog("latitude longitude error");
			return log;
		}
		//正则校验数字
		if (StringUtils.isNotEmpty(data.getFloorNumber())) {
			String reg = "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";
			if(!Pattern.compile(reg).matcher(data.getFloorNumber()).find()){
				log.setCode(CommunityServiceErrorCode.ERROR_FLOORNUMBER_FORMAT);
				log.setData(data);
				log.setErrorLog("FloorNumber format is error");
				return log;
			}
		}
		if (StringUtils.isNotEmpty(data.getBuildingNumber())) {
			Building building = communityProvider.findBuildingByCommunityIdAndNumber(communityId, data.getBuildingNumber());
			if(building != null && !building.getName().equals(data.getName())) {
				log.setCode(CommunityServiceErrorCode.ERROR_BUILDING_NUMBER_EXIST);
				log.setData(data);
				log.setErrorLog("building number exists");
				return log;
			}
		}
		return null;
	}

	private List<ImportBuildingDataDTO> handleImportBuildingData(List resultList) {
		List<ImportBuildingDataDTO> list = new ArrayList<>();
		for(int i = 1; i < resultList.size(); i++) {
			RowResult r = (RowResult) resultList.get(i);
			if (StringUtils.isNotBlank(r.getA()) || StringUtils.isNotBlank(r.getB()) || StringUtils.isNotBlank(r.getC()) || StringUtils.isNotBlank(r.getD()) ||
					StringUtils.isNotBlank(r.getE()) || StringUtils.isNotBlank(r.getF()) || StringUtils.isNotBlank(r.getG()) || StringUtils.isNotBlank(r.getH()) ||
					StringUtils.isNotBlank(r.getI()) || StringUtils.isNotBlank(r.getJ())) {
				ImportBuildingDataDTO data = new ImportBuildingDataDTO();
				data.setName(trim(r.getA()));
				data.setBuildingNumber(trim(r.getB()));
				data.setAliasName(trim(r.getC()));
				data.setFloorNumber(trim(r.getD()));
				data.setAddress(trim(r.getE()));
				data.setLongitudeLatitude(trim(r.getF()));
				data.setContactor(trim(r.getG()));
				data.setPhone(trim(r.getH()));
				data.setDescription(trim(r.getI()));
				data.setTrafficDescription(trim(r.getJ()));
				//加上来源第三方和在第三方的唯一标识 没有则不填 by xiongying20170814
				//data.setNamespaceBuildingType(trim(r.getK()));
				//data.setNamespaceBuildingToken(trim(r.getL()));
				list.add(data);
			}
		}
		return list;
	}

	private String trim(String string) {
		if (string != null) {
			return string.trim();
		}
		return "";
	}

	private Long getCurrentCommunityId(Long userId) {
		OrganizationDTO org = this.organizationService.getUserCurrentOrganization();
		if (org != null) {
			return org.getCommunityId();
		}
		return null;
	}


	@Override
	public ImportDataResponse importBuildingData(MultipartFile mfile, Long userId) {
		ImportDataResponse importDataResponse = new ImportDataResponse();
		try {
			//解析excel
			List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());

			if(null == resultList || resultList.isEmpty()){
				LOGGER.error("File content is empty。userId="+userId);
				throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL,
						"File content is empty");
			}
			LOGGER.debug("Start import data...,total:" + resultList.size());
			//导入数据，返回导入错误的日志数据集
			List<String> errorDataLogs = importBuilding(convertToStrList(resultList), userId);
			LOGGER.debug("End import data...,fail:" + errorDataLogs.size());
			if(null == errorDataLogs || errorDataLogs.isEmpty()){
				LOGGER.debug("Data import all success...");
			}else{
				//记录导入错误日志
				for (String log : errorDataLogs) {
					LOGGER.error(log);
				}
			}

			importDataResponse.setTotalCount((long)resultList.size()-1);
			importDataResponse.setFailCount((long)errorDataLogs.size());
			importDataResponse.setLogs(errorDataLogs);
		} catch (IOException e) {
			LOGGER.error("File can not be resolved...");
			e.printStackTrace();
		}
		return importDataResponse;
	}

	private List<String> convertToStrList(List list) {
		List<String> result = new ArrayList<String>();
		boolean firstRow = true;
		for (Object o : list) {
			if(firstRow){
				firstRow = false;
				continue;
			}
			RowResult r = (RowResult)o;
			StringBuffer sb = new StringBuffer();
			sb.append(r.getA()).append("||");
			sb.append(r.getB()).append("||");
			sb.append(r.getC()).append("||");
			sb.append(r.getD()).append("||");
			sb.append(r.getE()).append("||");
			sb.append(r.getF()).append("||");
			sb.append(r.getG()).append("||");
			sb.append(r.getH());
			result.add(sb.toString());
		}
		return result;
	}

	private List<String> importBuilding(List<String> list, Long userId){
		List<String> errorDataLogs = new ArrayList<String>();
		//userID查orgid
		OrganizationDTO org = this.organizationService.getUserCurrentOrganization();
		List<OrganizationMember> orgMem = this.organizationProvider.listOrganizationMembersByOrgId(org.getId());
		Map<String, OrganizationMember> ct = new HashMap<String, OrganizationMember>();
		if(orgMem != null) {
			orgMem.stream().map(r -> {
				ct.put(r.getContactToken(), r);
				return null;
			});
		}
		for (String str : list) {
			String[] s = str.split("\\|\\|");
			dbProvider.execute((TransactionStatus status) -> {
				Building building = new Building();
				building.setName(s[0]);
				building.setAliasName(s[1]);
				building.setAddress(s[2]);
				building.setContact(s[3]);
				building.setAreaSize(Double.valueOf(s[4]));
				String contactToken = s[6];

				if(ct.get(contactToken) != null) {
					OrganizationMember om = ct.get(contactToken);
					building.setManagerUid(om.getTargetId());
				}else {
					///////////////////////////////////
				}
				building.setCommunityId(org.getCommunityId());
				building.setDescription(s[7]);
				building.setStatus(CommunityAdminStatus.ACTIVE.getCode());

				LOGGER.info("add building");
				this.communityProvider.createBuilding(userId, building);
				return null;
			});
		}
		return errorDataLogs;

	}

	@Override
	public CommunityAuthUserAddressResponse listCommunityAuthUserAddress(CommunityAuthUserAddressCommand cmd){
	    checkUserPrivilege(cmd.getCurrentOrgId(), PrivilegeConstants.AUTHENTIFICATION_LIST_VIEW, cmd.getCommunityId(), cmd.getAppId());
		// Long communityId = cmd.getCommunityId();
//        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<NamespaceResource> resourceList = namespaceResourceProvider.listResourceByNamespace(cmd.getNamespaceId(), NamespaceResourceType.COMMUNITY);
        if (resourceList == null) {
            return new CommunityAuthUserAddressResponse();
        }
        //不通过域空间查询，通过项目查询 add by yanlong.liang 20180723
//        List<Long> communityIds = resourceList.stream().map(NamespaceResource::getResourceId).collect(Collectors.toList());
        List<Long> communityIds = new ArrayList<>();
        communityIds.add(cmd.getCommunityId());
        List<Group> groups = groupProvider.listGroupByCommunityIds(communityIds, (loc, query) -> {
            Condition c = Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode());
            query.addConditions(c);
            return query;
        });

		List<Long> groupIds = new ArrayList<>();
		for (Group group : groups) {
			groupIds.add(group.getId());
		}

		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        List<GroupMemberDTO> memberDTOList;

        if (cmd.getMemberStatus() != null && cmd.getMemberStatus().equals(GroupMemberStatus.REJECT.getCode())) {
            memberDTOList = listCommunityRejectUserAddress(cmd.getUserInfoKeyword(), cmd.getIdentifierToken(), cmd.getCommunityKeyword(), communityIds, locator, pageSize);
        } else if (cmd.getMemberStatus() != null && cmd.getMemberStatus().equals(GroupMemberStatus.ACTIVE.getCode())) {
            memberDTOList = listCommunityActiveUserAddress(cmd, communityIds, locator, pageSize);
        } else {
            memberDTOList = listCommunityWaitingApproveUserAddress(cmd, groupIds, locator, pageSize);
        }
		Collections.sort(memberDTOList, new Comparator<GroupMemberDTO>() {
			@Override
			public int compare(GroupMemberDTO o1, GroupMemberDTO o2) {
				if (o1.getApproveTime() == null || o2.getApproveTime() == null) {
					return -1;
				}
				return o2.getApproveTime().compareTo(o1.getApproveTime());
			}
		});
		
		//小区待认证数据的排序，创建时间降序
		if(cmd.getMemberStatus().equals(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode())){
			Collections.sort(memberDTOList,new Comparator<GroupMemberDTO>(){
				@Override
				public int compare(GroupMemberDTO o1, GroupMemberDTO o2) {
					if(o1.getCreateTime() == null || o2.getCreateTime() == null)
						return -1;
					return o2.getCreateTime().compareTo(o1.getCreateTime());
				}
				
			});
		}
		
		CommunityAuthUserAddressResponse res = new CommunityAuthUserAddressResponse();
		res.setDtos(memberDTOList);
		res.setNextPageAnchor(locator.getAnchor());
		return res;
	}

    private List<GroupMemberDTO> listCommunityActiveUserAddress(CommunityAuthUserAddressCommand cmd, List<Long> communityIds, CrossShardListingLocator locator, int pageSize) {
        List<GroupMemberLog> memberLogs = groupMemberLogProvider.queryGroupMemberLog(cmd.getUserInfoKeyword(),cmd.getIdentifierToken(), cmd.getCommunityKeyword(), communityIds,
                GroupMemberStatus.ACTIVE.getCode(), locator, pageSize);
        if (memberLogs != null) {
            return memberLogs.stream().map(r -> {
                GroupMember member = ConvertHelper.convert(r, GroupMember.class);
                GroupMemberDTO dto = toGroupMemberDTO(member);
                Address address = addressProvider.findAddressById(r.getAddressId());
                if (null != address) {
                    dto.setAddressId(address.getId());
                    dto.setApartmentName(address.getApartmentName());
                    dto.setBuildingName(address.getBuildingName());
                }
                Community community = communityProvider.findCommunityById(r.getCommunityId());
                if (community != null) {
                    dto.setCityName(community.getCityName());
                    dto.setAreaName(community.getAreaName());
                    dto.setCommunityName(community.getName());
                }
                dto.setOperateType(OperateType.MANUAL.getCode());
                return dto;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private List<GroupMemberDTO> listCommunityWaitingApproveUserAddress(CommunityAuthUserAddressCommand cmd, List<Long> groupIds, CrossShardListingLocator locator, int pageSize) {
        List<GroupMemberDTO> memberDTOList;
        List<GroupMember> groupMembers = groupProvider.listGroupMemberByGroupIds(groupIds, locator, pageSize + 1, (loc, query) -> {
            Condition c = Tables.EH_GROUP_MEMBERS.MEMBER_TYPE.eq(EntityType.USER.getCode());
            c = c.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(cmd.getMemberStatus()));

            if (StringUtils.isNotBlank(cmd.getUserInfoKeyword()) || StringUtils.isNotBlank(cmd.getIdentifierToken())) {
            	if (cmd.getUserInfoKeyword() == null) {
            		cmd.setUserInfoKeyword("");
				}
                String keyword = "%" + cmd.getUserInfoKeyword() + "%";
//                query.addJoin(Tables.EH_USERS, JoinType.JOIN, Tables.EH_GROUP_MEMBERS.MEMBER_ID.eq(Tables.EH_USERS.ID));
//                query.addJoin(Tables.EH_USER_IDENTIFIERS, JoinType.JOIN, Tables.EH_USER_IDENTIFIERS.OWNER_UID.eq(Tables.EH_USERS.ID));
                Condition condition = Tables.EH_USERS.NICK_NAME.like(keyword);
                if (StringUtils.isNotBlank(cmd.getIdentifierToken())) {
                	
                	//modify by momoubin,2018/11/30：小区用户认证查询条件修改or为and，"昵称"和"手机号"为与的条件
                   condition =  condition.and(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.like("%"+cmd.getIdentifierToken()+"%"));
                }
                query.addConditions(condition);
            }
            if (StringUtils.isNotBlank(cmd.getCommunityKeyword())) {
                String keyword = "%" + cmd.getCommunityKeyword() + "%";
                query.addJoin(Tables.EH_GROUPS, JoinType.JOIN, Tables.EH_GROUP_MEMBERS.GROUP_ID.eq(Tables.EH_GROUPS.ID));
                query.addJoin(Tables.EH_COMMUNITIES, JoinType.JOIN, Tables.EH_GROUPS.INTEGRAL_TAG2.eq(Tables.EH_COMMUNITIES.ID));
                query.addConditions(Tables.EH_COMMUNITIES.NAME.like(keyword));
            }

            query.addConditions(c);
            if (null != locator.getAnchor()) {
                query.addConditions(Tables.EH_GROUP_MEMBERS.MEMBER_ID.lt(locator.getAnchor()));
            }
            query.addOrderBy(Tables.EH_GROUP_MEMBERS.ID.desc());
            return query;
        });
        memberDTOList = groupMembers.stream().map(this::toGroupMemberDTO).collect(Collectors.toList());
        for (GroupMemberDTO groupMemberDTO : memberDTOList) {
            groupMemberDTO.setOperateType(OperateType.MANUAL.getCode());
        }
		if (memberDTOList != null && memberDTOList.size() > pageSize) {
			locator.setAnchor(memberDTOList.get(memberDTOList.size() - 1).getId());
			memberDTOList = memberDTOList.subList(0, pageSize);
		} else {
			locator.setAnchor(null);
		}
        return memberDTOList;
    }

    private GroupMemberDTO toGroupMemberDTO(GroupMember member) {
        GroupMemberDTO dto = ConvertHelper.convert(member, GroupMemberDTO.class);
        Group group = groupProvider.findGroupById(dto.getGroupId());
        if(null != group) {
            Address address = addressProvider.findAddressById(group.getFamilyAddressId());
            if (null != address) {
                dto.setAddressId(address.getId());
                dto.setApartmentName(address.getApartmentName());
                dto.setBuildingName(address.getBuildingName());
            }
            Long communityId = group.getFamilyCommunityId();
            Community community = communityProvider.findCommunityById(communityId);
            if (community != null) {
                dto.setCityName(community.getCityName());
                dto.setAreaName(community.getAreaName());
                dto.setCommunityName(community.getName());
            }
        }

        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(dto.getMemberId(), IdentifierType.MOBILE.getCode());
        if(null != userIdentifier) {
            dto.setCellPhone(userIdentifier.getIdentifierToken());
            User user = userProvider.findUserById(userIdentifier.getOwnerUid());
            if(null != user) {
                dto.setInviterNickName(user.getNickName());
            }
        }
        if (member.getOperatorUid() != null) {
            userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(member.getOperatorUid(), IdentifierType.MOBILE.getCode());
            if(null != userIdentifier) {
                dto.setOperatorPhone(userIdentifier.getIdentifierToken());
                User user = userProvider.findUserById(userIdentifier.getOwnerUid());
                if(null != user) {
                    dto.setOperatorName(user.getNickName());
                }
            }
        }
        return dto;
    }

    private List<GroupMemberDTO> listCommunityRejectUserAddress(String userInfoKeyword, String identifierToken, String communityKeyword, List<Long> communityIds, CrossShardListingLocator locator, int pageSize) {
        List<GroupMemberLog> memberLogs = groupMemberLogProvider.queryGroupMemberLog(userInfoKeyword,identifierToken, communityKeyword,
                communityIds, GroupMemberStatus.REJECT.getCode(), locator, pageSize);
        if (memberLogs != null) {
            return memberLogs.stream().map(r -> {
                GroupMember member = ConvertHelper.convert(r, GroupMember.class);
                GroupMemberDTO dto = toGroupMemberDTO(member);
                Address address = addressProvider.findAddressById(r.getAddressId());
                if (null != address) {
                    dto.setAddressId(address.getId());
                    dto.setApartmentName(address.getApartmentName());
                    dto.setBuildingName(address.getBuildingName());
                }
                Community community = communityProvider.findCommunityById(r.getCommunityId());
                if (community != null) {
                    dto.setCityName(community.getCityName());
                    dto.setAreaName(community.getAreaName());
                    dto.setCommunityName(community.getName());
                }
                dto.setOperateType(OperateType.MANUAL.getCode());
                return dto;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();

		/*List<UserGroupHistory> histories = this.userGroupHistoryProvider.queryUserGroupHistoryByGroupIds(userInfoKeyword, communityKeyword, communityIds, locator, pageSize);
		return histories.stream().map(r -> {
            GroupMember member = ConvertHelper.convert(r, GroupMember.class);
            member.setMemberId(r.getOwnerUid());
            member.setApproveTime(r.getCreateTime());
            Address address = addressProvider.findAddressById(r.getAddressId());
            return member;
        }).collect(Collectors.toList());*/
	}

	@Override
	public CommunityUserAddressResponse listUserBycommunityIdV2(ListCommunityUsersCommand cmd){
		if(AuthFlag.UNAUTHORIZED == AuthFlag.fromCode(cmd.getIsAuth())){
			return  listUnAuthUsersForResidential(cmd);
		}else{
			return listUserBycommunityId(cmd);
		}
	}

	@Override
	public CommunityUserAddressResponse listUserBycommunityId(ListCommunityUsersCommand cmd){
		Long communityId = cmd.getCommunityId();

		CommunityUserAddressResponse res = new CommunityUserAddressResponse();
		List<CommunityUserAddressDTO> dtos = new ArrayList<>();

		Integer namespaceId = UserContext.getCurrentNamespaceId();

		List<Group> groups = groupProvider.listGroupByCommunityId(communityId, (loc, query) -> {
            Condition c = Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode());
            query.addConditions(c);
            return query;
        });

		List<Long> groupIds = new ArrayList<Long>();
		for (Group group : groups) {
			groupIds.add(group.getId());
		}



		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		List<GroupMember> groupMembers = groupProvider.listGroupMemberByGroupIds(groupIds,locator,pageSize,(loc, query) -> {
			Condition c = Tables.EH_GROUP_MEMBERS.MEMBER_TYPE.eq(EntityType.USER.getCode());
			if(AuthFlag.fromCode(cmd.getIsAuth()) == AuthFlag.AUTHENTICATED){
				c = c.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode()));
			}else if(AuthFlag.fromCode(cmd.getIsAuth()) == AuthFlag.PENDING_AUTHENTICATION){
				Condition cond = Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.WAITING_FOR_ACCEPTANCE.getCode());
				cond = cond.or(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode()));
				c = c.and(cond);
			}else{
				c = c.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.ne(GroupMemberStatus.INACTIVE.getCode()));
			}
            query.addConditions(c);

			if(UserSourceType.WEIXIN == UserSourceType.fromCode(cmd.getUserSourceType())){
				query.addConditions(Tables.EH_USERS.NAMESPACE_USER_TYPE.eq(NamespaceUserType.WX.getCode()));
			}else if(UserSourceType.APP == UserSourceType.fromCode(cmd.getUserSourceType())){
				query.addConditions(Tables.EH_USERS.NAMESPACE_USER_TYPE.isNull());
			}else if(UserSourceType.ALIPAY == UserSourceType.fromCode(cmd.getUserSourceType())){
				query.addConditions(Tables.EH_USERS.NAMESPACE_USER_TYPE.eq(NamespaceUserType.ALIPAY.getCode()));
			}

			if(!StringUtils.isEmpty(cmd.getKeywords())){
				Condition cond = Tables.EH_USERS.NICK_NAME.like("%" + cmd.getKeywords() + "%");
				query.addConditions(cond);
			}
            if (!StringUtils.isBlank(cmd.getPhone())) {
                Condition cond = Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.like("%" + cmd.getPhone() + "%");
                query.addConditions(cond);
            }
			if (!StringUtils.isBlank(cmd.getAddress())) {
			    Condition cond = Tables.EH_ADDRESSES.ADDRESS.like("%" + cmd.getAddress() + "%");
				query.addConditions(cond);
            }
			if (cmd.getStartTime() != null && cmd.getEndTime() != null) {
                query.addConditions(Tables.EH_GROUP_MEMBERS.CREATE_TIME.ge(new Timestamp(cmd.getStartTime())));
                query.addConditions(Tables.EH_GROUP_MEMBERS.CREATE_TIME.le(new Timestamp(cmd.getEndTime())));

            }
            if(null != locator.getAnchor())
            	query.addConditions(Tables.EH_GROUP_MEMBERS.MEMBER_ID.lt(locator.getAnchor()));
            //query.addGroupBy(Tables.EH_GROUP_MEMBERS.MEMBER_ID);
			query.addOrderBy(Tables.EH_GROUP_MEMBERS.MEMBER_ID.desc());
			query.addOrderBy(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.desc());
			query.addGroupBy(Tables.EH_GROUP_MEMBERS.MEMBER_ID);
            return query;
        });

		// 有重复的用户，需要过滤 by lqs 20160831
		List<Long> userIdList = new ArrayList<Long>();
		for (GroupMember member : groupMembers) {
			User user = userProvider.findUserById(member.getMemberId());

			if(null != user && !userIdList.contains(user.getId())){
				UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(member.getMemberId(), IdentifierType.MOBILE.getCode());

				CommunityUserAddressDTO dto = new CommunityUserAddressDTO();
				dto.setUserId(user.getId());
				//dto.setUserName(user.getNickName());
				dto.setNickName(user.getNickName());
				dto.setGender(user.getGender());
				dto.setApplyTime(user.getCreateTime());
				if(null != userIdentifier)
					dto.setPhone(userIdentifier.getIdentifierToken());
				dto.setIsAuth(AuthFlag.UNAUTHORIZED.getCode());

				if(NamespaceUserType.fromCode(user.getNamespaceUserType()) == NamespaceUserType.WX){
					dto.setUserSourceType(UserSourceType.WEIXIN.getCode());
				} else if (NamespaceUserType.fromCode(user.getNamespaceUserType()) == NamespaceUserType.ALIPAY){
					dto.setUserSourceType(UserSourceType.ALIPAY.getCode());
				}

				List<UserGroup> userGroups = userProvider.listUserGroups(user.getId(), GroupDiscriminator.FAMILY.getCode());
				//添加地址信息
				addGroupAddressDto(dto, userGroups);
				
				//用户认证状态的修改：如果搜索条件为“认证中”、“已认证”，根据情况修改dto的isAuth值,add by momoubin,18/12/11
				if(cmd.getIsAuth() == AuthFlag.PENDING_AUTHENTICATION.getCode()){ //认证中
					dto.setIsAuth(AuthFlag.PENDING_AUTHENTICATION.getCode());
				}else if(cmd.getIsAuth() == AuthFlag.AUTHENTICATED.getCode()){ //已认证
					dto.setIsAuth(AuthFlag.AUTHENTICATED.getCode());
				}
				
				//最新活跃时间 add by sfyan 20170620
				List<UserActivity> userActivities = userActivityProvider.listUserActivetys(user.getId(), 1);
				if(userActivities.size() > 0){
					dto.setRecentlyActiveTime(userActivities.get(0).getCreateTime().getTime());
				}

				userIdList.add(user.getId());
				dtos.add(dto);
			}
		}

		res.setDtos(dtos);
		res.setNextPageAnchor(locator.getAnchor());
		return res;
	}


	private void addGroupAddressDto(CommunityUserAddressDTO dto, List<UserGroup> userGroups){
		List<AddressDTO> addressDtos = new ArrayList<>();
		if(userGroups == null){
			dto.setAddressDtos(addressDtos);
			return;
		}

		for (UserGroup userGroup : userGroups) {
			Address groupAddress = addressProvider.findGroupAddress(userGroup.getGroupId());
			AddressDTO addressDTO = ConvertHelper.convert(groupAddress, AddressDTO.class);
			if(addressDTO != null){
				if(GroupMemberStatus.fromCode(userGroup.getMemberStatus()) == GroupMemberStatus.ACTIVE){
					addressDTO.setUserAuth(AuthFlag.AUTHENTICATED.getCode().byteValue());

					//有一个地址认证了就是认证了
					dto.setIsAuth(AuthFlag.AUTHENTICATED.getCode());
				}else if(GroupMemberStatus.fromCode(userGroup.getMemberStatus()) == GroupMemberStatus.WAITING_FOR_ACCEPTANCE || GroupMemberStatus.fromCode(userGroup.getMemberStatus()) == GroupMemberStatus.WAITING_FOR_APPROVAL){
					addressDTO.setUserAuth(AuthFlag.PENDING_AUTHENTICATION.getCode().byteValue());

					//有一个地址是认证中，则状态是认证中或者已认证
					if(!AuthFlag.AUTHENTICATED.getCode().equals(dto.getIsAuth())){
						dto.setIsAuth(AuthFlag.PENDING_AUTHENTICATION.getCode());
					}

				}else {
					addressDTO.setUserAuth(AuthFlag.UNAUTHORIZED.getCode().byteValue());
				}
			}

			addressDtos.add(addressDTO);
		}
		dto.setAddressDtos(addressDtos);
	}

	@Override
	public CommunityUserAddressResponse listOwnerBycommunityId(ListCommunityUsersCommand cmd){
		CommunityUserAddressResponse res = new CommunityUserAddressResponse();

		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<OrganizationOwner> owners = organizationProvider.listOrganizationOwnerByCommunityId(cmd.getCommunityId(),locator, cmd.getPageSize(),(loc, query) -> {
			if(org.springframework.util.StringUtils.isEmpty(cmd.getKeywords())){
				Condition cond = Tables.EH_ORGANIZATION_OWNERS.CONTACT_NAME.like(cmd.getKeywords() + "%");
				cond = cond.or(Tables.EH_ORGANIZATION_OWNERS.CONTACT_TOKEN.eq(cmd.getKeywords()));
				query.addConditions(cond);
			}
            return query;
        });

		List<CommunityUserAddressDTO> dtos = new ArrayList<CommunityUserAddressDTO>();
		for (OrganizationOwner organizationOwner : owners) {
			CommunityUserAddressDTO dto = new CommunityUserAddressDTO();
			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(organizationOwner.getNamespaceId(), organizationOwner.getContactToken());
			dto.setIsAuth(2);
			if(null != userIdentifier){
				dto.setUserId(userIdentifier.getOwnerUid());
				dto.setIsAuth(1);
			}

			dto.setUserName(organizationOwner.getContactName());
			dto.setNickName(organizationOwner.getContactName());
			dto.setPhone(organizationOwner.getContactToken());
			dtos.add(dto);
		}
		res.setDtos(dtos);
		res.setNextPageAnchor(locator.getAnchor());
		return res;
	}

	@Override
	public CommunityUserAddressDTO qryCommunityUserAddressByUserId(QryCommunityUserAddressByUserIdCommand cmd){
		CommunityUserAddressDTO dto = new CommunityUserAddressDTO();
		Integer namespaceId = UserContext.getCurrentNamespaceId();

		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getContactToken());
		if(null == userIdentifier){
			List<OrganizationOwner> owners = organizationProvider.findOrganizationOwnerByTokenOrNamespaceId(cmd.getContactToken(), namespaceId);
			List<AddressDTO> addressDtos = new ArrayList<AddressDTO>();
			for (OrganizationOwner organizationOwner : owners) {
				Address address = addressProvider.findAddressById(organizationOwner.getAddressId());
				if(null != address)
					addressDtos.add(ConvertHelper.convert(address, AddressDTO.class));
			}
			dto.setAddressDtos(addressDtos);
			return dto;
		}
		User user = userProvider.findUserById(userIdentifier.getOwnerUid());
		List<UserGroup> usreGroups = userProvider.listUserGroups(user.getId(), GroupDiscriminator.FAMILY.getCode());
		//List<AddressDTO> addressDtos = new ArrayList<AddressDTO>();

		dto.setIsAuth(AuthFlag.UNAUTHORIZED.getCode());
		//添加地址信息
		addGroupAddressDto(dto, usreGroups);
        //小区认证记录
        List<GroupMemberLog> memberLogList = this.groupMemberLogProvider.listGroupMemberLogByUserId(user.getId(), null);
        List<GroupMemberDTO> memberLogs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(memberLogList)) {
            memberLogList.stream().forEach(r -> {
                GroupMember member = ConvertHelper.convert(r, GroupMember.class);
                GroupMemberDTO groupMemberDTO = toGroupMemberDTO(member);
                Address address = addressProvider.findAddressById(r.getAddressId());
                if (null != address) {
                    groupMemberDTO.setAddressId(address.getId());
                    groupMemberDTO.setApartmentName(address.getApartmentName());
                    groupMemberDTO.setBuildingName(address.getBuildingName());
                }
                Community community = communityProvider.findCommunityById(r.getCommunityId());
                if (community != null) {
                    groupMemberDTO.setCityName(community.getCityName());
                    groupMemberDTO.setAreaName(community.getAreaName());
                    groupMemberDTO.setCommunityName(community.getName());
                }
                groupMemberDTO.setOperateType(OperateType.MANUAL.getCode());
                memberLogs.add(groupMemberDTO);
            });
            //按照时间倒叙排列
            Collections.sort(memberLogs, new Comparator<GroupMemberDTO>() {
                @Override
                public int compare(GroupMemberDTO o1, GroupMemberDTO o2) {
                    if (o1.getApproveTime() == null || o2.getApproveTime() == null) {
                        return -1;
                    }
                    return o2.getApproveTime().compareTo(o1.getApproveTime());
                }
            });
        }
        dto.setGroupmemberLogDTOs(memberLogs);
//		if(null != usreGroups){
//			for (UserGroup userGroup : usreGroups) {
//				Group group = groupProvider.findGroupById(userGroup.getGroupId());
//				if(null != group && group.getFamilyCommunityId().equals(cmd.getCommunityId())){
//					Address address = addressProvider.findAddressById(group.getFamilyAddressId());
//					if(null != address){
//						address.setMemberStatus(userGroup.getMemberStatus());
//						addressDtos.add(ConvertHelper.convert(address, AddressDTO.class));
//					}
//				}
//			}
//		}
		if(null != user){
			dto.setUserId(user.getId());
			//dto.setUserName(user.getNickName());
			dto.setNickName(user.getNickName());
			dto.setGender(user.getGender());
			dto.setPhone(null != userIdentifier ? userIdentifier.getIdentifierToken() : null);
			dto.setApplyTime(user.getCreateTime());
			dto.setIdentityNumber(user.getIdentityNumberTag());
            if (NamespaceUserType.WX.getCode().equals(user.getNamespaceUserType())) {
                dto.setUserSourceType(UserSourceType.WEIXIN.getCode());
            }else {
                dto.setUserSourceType(UserSourceType.APP.getCode());
            }
			//dto.setAddressDtos(addressDtos);
            String showVipFlag = this.configurationProvider.getValue(user.getNamespaceId(), ConfigConstants.SHOW_USER_VIP_LEVEL, "");
            if ("true".equals(showVipFlag)) {
                dto.setShowVipLevelFlag(com.everhomes.rest.common.TrueOrFalseFlag.TRUE.getCode());
            }
            dto.setVipLevel(user.getVipLevel());
            //最新活跃时间 add by sfyan 20170620
			List<UserActivity> userActivities = userActivityProvider.listUserActivetys(user.getId(), 1);
			if(userActivities.size() > 0){
				dto.setRecentlyActiveTime(userActivities.get(0).getCreateTime().getTime());
			}
			UserIdentifier emailIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.EMAIL.getCode());
			if (emailIdentifier != null) {
			    dto.setEmail(emailIdentifier.getIdentifierToken());
            }
		}
		return dto;
	}

	@Override
	public CommunityUserAddressDTO qryCommunityUserEnterpriseByUserId(QryCommunityUserAddressByUserIdCommand cmd){
		CommunityUserAddressDTO dto = new CommunityUserAddressDTO();

		User user = userProvider.findUserById(cmd.getUserId());
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(cmd.getUserId(), IdentifierType.MOBILE.getCode());
		UserIdentifier emailIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(cmd.getUserId(), IdentifierType.EMAIL.getCode());
		if (emailIdentifier != null) {
		    dto.setEmail(emailIdentifier.getIdentifierToken());
        }
		List<OrganizationMember> members = organizationProvider.listOrganizationMembers(user.getId());

		dto.setIsAuth(AuthFlag.UNAUTHORIZED.getCode());

		List<OrganizationDetailDTO> orgDtos = new ArrayList<OrganizationDetailDTO>();
		if(null != members){
			orgDtos.addAll(populateOrganizationDetails(members));

			for (OrganizationMember member : members) {
				if (OrganizationMemberStatus.ACTIVE == OrganizationMemberStatus.fromCode(member.getStatus()) && OrganizationGroupType.ENTERPRISE == OrganizationGroupType.fromCode(member.getGroupType())) {
					dto.setIsAuth(AuthFlag.AUTHENTICATED.getCode());
					break;
                }else if (OrganizationMemberStatus.WAITING_FOR_APPROVAL == OrganizationMemberStatus.fromCode(member.getStatus()) && OrganizationGroupType.ENTERPRISE == OrganizationGroupType.fromCode(member.getGroupType())){
                    dto.setIsAuth(AuthFlag.PENDING_AUTHENTICATION.getCode());
                    break;
                }
			}
		}


		if(null != user){
			dto.setUserId(user.getId());
			//dto.setUserName(user.getNickName());
			dto.setNickName(user.getNickName());
			dto.setGender(user.getGender());
			dto.setPhone(null != userIdentifier ? userIdentifier.getIdentifierToken() : null);
			dto.setApplyTime(user.getCreateTime());
			dto.setOrgDtos(orgDtos);
			dto.setCreateTime(user.getCreateTime() != null ? user.getCreateTime().getTime() : null);
			dto.setExecutiveFlag(user.getExecutiveTag());
			dto.setIdentityNumber(user.getIdentityNumberTag());
			dto.setPosition(user.getPositionTag());
            String showVipFlag = this.configurationProvider.getValue(user.getNamespaceId(), ConfigConstants.SHOW_USER_VIP_LEVEL, "");
            if ("true".equals(showVipFlag)) {
                dto.setShowVipLevelFlag(com.everhomes.rest.common.TrueOrFalseFlag.TRUE.getCode());
            }
            dto.setVipLevel(user.getVipLevel());
            if (NamespaceUserType.WX.getCode().equals(user.getNamespaceUserType())) {
                dto.setUserSourceType(UserSourceType.WEIXIN.getCode());
            }else {
                dto.setUserSourceType(UserSourceType.APP.getCode());
            }
        }
		List<OrganizationMemberLog> memberLogs = this.organizationProvider.listOrganizationMemberLogs(user.getId());
		List<OrganizationMemberLogDTO> memberLog = new ArrayList<>();
		if(null != memberLogs){
			for(OrganizationMemberLog log : memberLogs){
				OrganizationMemberLogDTO logDTO = ConvertHelper.convert(log, OrganizationMemberLogDTO.class);
				logDTO.setOperateTime(log.getOperateTime().getTime());
				Organization org = this.organizationProvider.findOrganizationById(log.getOrganizationId());
				if(null != org)
					logDTO.setOrganizationName(org.getName());
				User operator = this.userProvider.findUserById(log.getOperatorUid());
				if(null != operator)
					logDTO.setOperatorNickName(operator.getNickName());
                memberLog.add(logDTO);
			}
			if (!CollectionUtils.isEmpty(memberLog)) {
                //按照时间倒叙排列
                Collections.sort(memberLog, new Comparator<OrganizationMemberLogDTO>() {
                    @Override
                    public int compare(OrganizationMemberLogDTO o1, OrganizationMemberLogDTO o2) {
                        if (o1.getOperateTime() == null || o2.getOperateTime() == null) {
                            return -1;
                        }
                        return o2.getOperateTime().compareTo(o1.getOperateTime());
                    }
                });
            }
		}
        dto.setMemberLogDTOs(memberLog);
		//最新活跃时间 add by sfyan 20170620
		List<UserActivity> userActivities = userActivityProvider.listUserActivetys(cmd.getUserId(), 1);
		if(userActivities.size() > 0){
			dto.setRecentlyActiveTime(userActivities.get(0).getCreateTime().getTime());
		}
		return dto;
	}

	@Override
	public CommunityUserAddressDTO qryCommunityUserAllByUserId(QryCommunityUserAllByUserIdCommand cmd) {
        CommunityUserAddressDTO communityUserAddressDTO = new CommunityUserAddressDTO();
        List<Community> communities = this.communityProvider.listNamespaceCommunities(cmd.getNamespaceId());
        if (!CollectionUtils.isEmpty(communities)) {
            List<CommunityUserAddressDTO> list = new ArrayList<>();
            QryCommunityUserAddressByUserIdCommand command = ConvertHelper.convert(cmd,QryCommunityUserAddressByUserIdCommand.class);
            for (Community community : communities) {
                if (community == null)
                    continue;

                if (CommunityType.RESIDENTIAL.equals(CommunityType.fromCode(community.getCommunityType()))){
                    //小区
                    command.setCommunityId(community.getId());
                    CommunityUserAddressDTO residential = this.qryCommunityUserAddressByUserId(command);
                    list.add(residential);
                } else if (CommunityType.COMMERCIAL.equals(CommunityType.fromCode(community.getCommunityType()))) {
                    //园区
                    command.setCommunityId(community.getId());
                    CommunityUserAddressDTO commercial = this.qryCommunityUserEnterpriseByUserId(command);
                    list.add(commercial);
                }
            }
            User user = this.userProvider.findUserById(cmd.getUserId());
            if (user != null) {
                communityUserAddressDTO = ConvertHelper.convert(user, CommunityUserAddressDTO.class);
                communityUserAddressDTO.setIdentityNumber(user.getIdentityNumberTag());
                if (NamespaceUserType.WX.getCode().equals(user.getNamespaceUserType())) {
                    communityUserAddressDTO.setUserSourceType(UserSourceType.WEIXIN.getCode());
                }else {
                    communityUserAddressDTO.setUserSourceType(UserSourceType.APP.getCode());
                }
                //最新活跃时间 add by sfyan 20170620
                List<UserActivity> userActivities = userActivityProvider.listUserActivetys(cmd.getUserId(), 1);
                if(userActivities.size() > 0){
                    communityUserAddressDTO.setRecentlyActiveTime(userActivities.get(0).getCreateTime().getTime());
                }
            }

            //是否展示会员等级
            String showVipFlag = this.configurationProvider.getValue(user.getNamespaceId(), ConfigConstants.SHOW_USER_VIP_LEVEL, "");
            if ("true".equals(showVipFlag)) {
                communityUserAddressDTO.setShowVipLevelFlag(com.everhomes.rest.common.TrueOrFalseFlag.TRUE.getCode());
            }
            if (!CollectionUtils.isEmpty(list)) {
                communityUserAddressDTO = list.get(0);
                List<OrganizationDetailDTO> orgDtos = new ArrayList<>();
                List<AddressDTO> addressDtos = new ArrayList<>();
                List<OrganizationMemberLogDTO> memberLogDTOs = new ArrayList<>();

                boolean auth = false;
                boolean pending = false;
                for (CommunityUserAddressDTO dto : list) {
                    if (!CollectionUtils.isEmpty(dto.getOrgDtos()) && CollectionUtils.isEmpty(orgDtos)) {
                        orgDtos.addAll(dto.getOrgDtos());
                    }
                    if (!CollectionUtils.isEmpty(dto.getAddressDtos()) && CollectionUtils.isEmpty(addressDtos)) {
                        addressDtos.addAll(dto.getAddressDtos());
                    }
                    if (!CollectionUtils.isEmpty(dto.getMemberLogDTOs()) && CollectionUtils.isEmpty(memberLogDTOs)) {
                        memberLogDTOs.addAll(dto.getMemberLogDTOs());
                    }
                    if (AuthFlag.AUTHENTICATED.getCode().equals(dto.getIsAuth())) {
                        auth = true;
                    }else if (AuthFlag.PENDING_AUTHENTICATION.getCode().equals(dto.getIsAuth())) {
                        pending = true;
                    }
                }
                //按照时间倒叙排列
                Collections.sort(memberLogDTOs, new Comparator<OrganizationMemberLogDTO>() {
                    @Override
                    public int compare(OrganizationMemberLogDTO o1, OrganizationMemberLogDTO o2) {
                        if (o1.getOperateTime() == null || o2.getOperateTime() == null) {
                            return -1;
                        }
                        return o2.getOperateTime().compareTo(o1.getOperateTime());
                    }
                });
                communityUserAddressDTO.setOrgDtos(orgDtos);
                communityUserAddressDTO.setAddressDtos(addressDtos);
                communityUserAddressDTO.setMemberLogDTOs(memberLogDTOs);
                if (auth) {
                    communityUserAddressDTO.setIsAuth(AuthFlag.AUTHENTICATED.getCode());
                }else if (pending) {
                    communityUserAddressDTO.setIsAuth(AuthFlag.PENDING_AUTHENTICATION.getCode());
                }else {
                    communityUserAddressDTO.setIsAuth(AuthFlag.UNAUTHORIZED.getCode());
                }
            }

            //小区认证记录
            List<GroupMemberLog> memberLogList = this.groupMemberLogProvider.listGroupMemberLogByUserId(user.getId(), null);
            List<GroupMemberDTO> memberLogs = new ArrayList<>();
            if (!CollectionUtils.isEmpty(memberLogList)) {
                memberLogList.stream().forEach(r -> {
                    GroupMember member = ConvertHelper.convert(r, GroupMember.class);
                    GroupMemberDTO groupMemberDTO = toGroupMemberDTO(member);
                    Address address = addressProvider.findAddressById(r.getAddressId());
                    if (null != address) {
                        groupMemberDTO.setAddressId(address.getId());
                        groupMemberDTO.setApartmentName(address.getApartmentName());
                        groupMemberDTO.setBuildingName(address.getBuildingName());
                    }
                    Community community = communityProvider.findCommunityById(r.getCommunityId());
                    if (community != null) {
                        groupMemberDTO.setCityName(community.getCityName());
                        groupMemberDTO.setAreaName(community.getAreaName());
                        groupMemberDTO.setCommunityName(community.getName());
                    }
                    groupMemberDTO.setOperateType(OperateType.MANUAL.getCode());
                    memberLogs.add(groupMemberDTO);
                });

                //按照时间倒叙排列
                Collections.sort(memberLogs, new Comparator<GroupMemberDTO>() {
                    @Override
                    public int compare(GroupMemberDTO o1, GroupMemberDTO o2) {
                        if (o1.getApproveTime() == null || o2.getApproveTime() == null) {
                            return -1;
                        }
                        return o2.getApproveTime().compareTo(o1.getApproveTime());
                    }
                });
            }
            communityUserAddressDTO.setGroupmemberLogDTOs(memberLogs);
        }
		return communityUserAddressDTO;
	}

	private Set<OrganizationDetailDTO> populateOrganizationDetails(List<OrganizationMember> members) {
		Set<OrganizationDetailDTO> set = new HashSet<>();

		for (OrganizationMember member : members) {
			OrganizationDetail detail = organizationProvider.findOrganizationDetailByOrganizationId(member.getOrganizationId());
			Organization organization = organizationProvider.findOrganizationById(member.getOrganizationId());

			// 通过SQL插入到eh_organization_details里面的数据有可能会漏填displayName，此时界面会显示为undefined，
			// 故需要对该情况补回该名字 by lqs 20170421
//			if(null == detail){
//				detail = new OrganizationDetail();
//				if(null != organization){
//					detail.setDisplayName(organization.getName());
//					detail.setOrganizationId(organization.getId());
//				}
//			}
			String displayName = organization.getName();
			Long organizationId = organization.getId();
			if(detail != null){
				if(!StringUtils.isBlank(detail.getDisplayName())){
					displayName = detail.getDisplayName();
				}
				if(detail.getOrganizationId() != null) {
					organizationId = detail.getOrganizationId();
				}
			} else {
				detail = new OrganizationDetail();
			}
			detail.setDisplayName(displayName);
			detail.setOrganizationId(organizationId);

			OrganizationDetailDTO detailDto = ConvertHelper.convert(detail, OrganizationDetailDTO.class);

			detailDto.setOrganizationMemberName(member.getContactName());

			List<OrganizationAddress> orgAddresses = organizationProvider.findOrganizationAddressByOrganizationId(detailDto.getOrganizationId());

			List<AddressDTO> addressDtos = new ArrayList<AddressDTO>();
			if(null != orgAddresses){
				for (OrganizationAddress organizationAddress : orgAddresses) {
					Address address = addressProvider.findAddressById(organizationAddress.getAddressId());
					if(null != address)
						addressDtos.add(ConvertHelper.convert(address, AddressDTO.class));
				}
			}
			detailDto.setAddresses(addressDtos);

			if (null != organization && organization.getGroupType().equals(OrganizationGroupType.ENTERPRISE.getCode())
					&& OrganizationStatus.fromCode(organization.getStatus()) == OrganizationStatus.ACTIVE) {

				//增加返回用户在企业中是否是高管、职位的信息  add by yanjun 20171017
				UserOrganizations userOrg = organizationProvider.findActiveAndWaitUserOrganizationByUserIdAndOrgId(member.getTargetId(), detailDto.getOrganizationId());
				if(userOrg != null){
					CommunityUserOrgDetailDTO communityUserOrgDetailDTO = new CommunityUserOrgDetailDTO();
					communityUserOrgDetailDTO.setDetailId(userOrg.getId());
					communityUserOrgDetailDTO.setExecutiveFlag(userOrg.getExecutiveTag());
					communityUserOrgDetailDTO.setPositionTag(userOrg.getPositionTag());
                    communityUserOrgDetailDTO.setIsAuth(AuthFlag.PENDING_AUTHENTICATION.getCode());
					if (OrganizationMemberStatus.ACTIVE == OrganizationMemberStatus.fromCode(member.getStatus())) {
					    communityUserOrgDetailDTO.setIsAuth(AuthFlag.AUTHENTICATED.getCode());
                    }
					detailDto.setCommunityUserOrgDetailDTO(communityUserOrgDetailDTO);
				}

				set.add(detailDto);
			}
		}

		return set;
	}

	private CommunityUserResponse listUserByOrganizationIdOrCommunityId(ListCommunityUsersCommand cmd){
		if(null == cmd.getOrganizationId() && null == cmd.getCommunityId()){
			LOGGER.error("organizationId and communityId All are empty");
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, ErrorCodes.ERROR_INVALID_PARAMETER,
					"organizationId and communityId All are empty");
		}
		CommunityUserResponse res = new CommunityUserResponse();

		List<Long> organizationIds = this.getAllOrganizationIds(cmd.getCommunityId(), cmd.getOrganizationId());

		Condition cond = Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode());

		if(1 == cmd.getIsAuth()){
			cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
		}else if(2 == cmd.getIsAuth()){
			Condition condition = Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.WAITING_FOR_ACCEPTANCE.getCode());
			condition = condition.or(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.WAITING_FOR_APPROVAL.getCode()));
			cond = cond.and(condition);
		}else{
			cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.INACTIVE.getCode()));
		}

		if(StringUtils.isNotBlank(cmd.getKeywords())){
			Condition condition = Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq(cmd.getKeywords());
			condition = condition.or(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_NAME.eq(cmd.getKeywords()));
			cond = cond.and(condition);
		}

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<OrganizationMember> members = organizationProvider.listOrganizationMemberByOrganizationIds(locator, pageSize, cond, organizationIds);

		List<CommunityUserDto> dtos = new ArrayList<CommunityUserDto>();

		for (OrganizationMember organizationMember : members) {
			CommunityUserDto dto = new CommunityUserDto();
			dto.setUserId(organizationMember.getTargetId());
			dto.setUserName(organizationMember.getContactName());
			dto.setPhone(organizationMember.getContactToken());
			dto.setApplyTime(organizationMember.getCreateTime());
			if(OrganizationMemberStatus.fromCode(organizationMember.getStatus()) == OrganizationMemberStatus.ACTIVE){
				dto.setIsAuth(1);
			}else{
				dto.setIsAuth(2);
			}
			dtos.add(dto);
		}

		res.setNextPageAnchor(locator.getAnchor());
		res.setUserCommunities(dtos);
		return res;
	}

	private List<Long> getAllOrganizationIds(Long communityId, Long orgId){
		List<Long> communityIds = new ArrayList<Long>();
		List<Long> organizationIds = new ArrayList<Long>();
		if(null == communityId && null != orgId){
			List<CommunityDTO> communityDTOs = organizationService.listAllChildrenOrganizationCoummunities(orgId);
			for (CommunityDTO communityDTO : communityDTOs) {
				communityIds.add(communityDTO.getId());
			}
			organizationIds.add(orgId);
		}else{
			communityIds.add(communityId);
		}

		//查询园区下面的所有公司
		for (Long commId : communityIds) {
			List<OrganizationCommunityRequest> organizationCommunityRequests = organizationProvider.queryOrganizationCommunityRequestByCommunityId(new CrossShardListingLocator(), commId, 1000000, null);
			for (OrganizationCommunityRequest organizationCommunityRequest : organizationCommunityRequests) {
				organizationIds.add(organizationCommunityRequest.getMemberId());
			}
		}

		//查询所有公司下面所有的子公司
		List<String> groupTypes = new ArrayList<String>();
		groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
		groupTypes.add(OrganizationGroupType.GROUP.getCode());

		List<Long> childOrganizationIds = new ArrayList<Long>();
		for (Long organizationId : organizationIds) {
			List<Organization> organizations = organizationProvider.listOrganizationByGroupTypes("/" + organizationId + "/%", groupTypes);
			for (Organization organization : organizations) {
				childOrganizationIds.add(organization.getId());
			}
		}
		organizationIds.addAll(childOrganizationIds);

		return organizationIds;
	}

	@Override
	public CommunityUserResponse listUserCommunitiesV2(
			ListCommunityUsersCommand cmd) {
		if(AuthFlag.UNAUTHORIZED == AuthFlag.fromCode(cmd.getIsAuth())){
			return  listUnAuthUsersForCommercial(cmd);
		}else{
			return listUserCommunities(cmd);
		}
	}

    @Override
    public ListAllCommunityUserResponse listAllUserCommunities(ListAllCommunityUsersCommand cmd) {
        ListAllCommunityUserResponse response = new ListAllCommunityUserResponse();
        Integer namespaceId = cmd.getNamespaceId();
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        Long time = System.currentTimeMillis();
        List<User> userList = this.userProvider.listUsers(locator, pageSize, new ListingQueryBuilderCallback(){
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId));
                if(cmd.getStartTime() != null){
                    query.addConditions(Tables.EH_USERS.CREATE_TIME.ge(new Timestamp(cmd.getStartTime())));
                }
                if(cmd.getEndTime() != null){
                    query.addConditions(Tables.EH_USERS.CREATE_TIME.le(new Timestamp(cmd.getEndTime())));
                }
                if (!StringUtils.isBlank(cmd.getPhone())) {
                    query.addConditions(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.like("%" + cmd.getPhone() + "%"));
                }
                if(!StringUtils.isEmpty(cmd.getKeywords())){
                    Condition cond = Tables.EH_ORGANIZATION_MEMBERS.CONTACT_NAME.like("%" + cmd.getKeywords() + "%");
                    cond = cond.or(Tables.EH_USERS.NICK_NAME.like("%" + cmd.getKeywords() + "%"));
                    query.addConditions(cond);
                }

                if(UserSourceType.WEIXIN == UserSourceType.fromCode(cmd.getUserSourceType())){
                    query.addConditions(Tables.EH_USERS.NAMESPACE_USER_TYPE.eq(NamespaceUserType.WX.getCode()));
                }else if(UserSourceType.APP == UserSourceType.fromCode(cmd.getUserSourceType())){
                    query.addConditions(Tables.EH_USERS.NAMESPACE_USER_TYPE.isNull());
                }else if(UserSourceType.ALIPAY == UserSourceType.fromCode(cmd.getUserSourceType())) {
                	// 增加支付宝用户统计
					query.addConditions(Tables.EH_USERS.NAMESPACE_USER_TYPE.eq(NamespaceUserType.ALIPAY.getCode()));
				}

                query.addGroupBy(Tables.EH_USERS.ID);

                return query;
            }
        });
        List<CommunityAllUserDTO> communityAllUserDTOS = new ArrayList<>();
        if (userList != null) {
            for (User user : userList) {
                CommunityAllUserDTO communityAllUserDTO = new CommunityAllUserDTO();
                communityAllUserDTO.setUserId(user.getId());
                communityAllUserDTO.setNickName(user.getNickName());
                List<Long> userIds = new ArrayList<>();
                userIds.add(user.getId());
                List<OrganizationMember> members = this.organizationProvider.listAllOrganizationMembersByUID(userIds);
                Set<String> nameSet = new HashSet<>();
                if (!CollectionUtils.isEmpty(members)) {
                    for (OrganizationMember organizationMember : members) {
                        nameSet.add(organizationMember.getContactName());
                    }
                }
                String userName = "-";
                if (nameSet.size() > 0 && nameSet.size() <= 2) {
                    userName = nameSet.toString().substring(1, nameSet.toString().length() -1 ).replaceAll(",",";");
                }else if (nameSet.size() > 2) {
                    List<String> nameList = Lists.newArrayList(nameSet);
                    userName = nameList.get(0) + ";" + nameList.get(1) + "...";
                }
                communityAllUserDTO.setUserName(userName);
                communityAllUserDTO.setPhone(user.getIdentifierToken());
                communityAllUserDTO.setGender(user.getGender());
                if (NamespaceUserType.WX.getCode().equals(user.getNamespaceUserType())) {
                    communityAllUserDTO.setUserSourceType(UserSourceType.WEIXIN.getCode());
                }else if (NamespaceUserType.ALIPAY.getCode().equals(user.getNamespaceUserType())){
                    communityAllUserDTO.setUserSourceType(UserSourceType.ALIPAY.getCode());
                }else {
					communityAllUserDTO.setUserSourceType(UserSourceType.APP.getCode());
				}
                communityAllUserDTO.setApplyTime(user.getCreateTime());
                communityAllUserDTOS.add(communityAllUserDTO);
            }

        }
        response.setNextPageAnchor(locator.getAnchor());
        response.setUserCommunities(communityAllUserDTOS);
        return response;
    }

    @Override
	public CommunityUserResponse listUserCommunities(
			ListCommunityUsersCommand cmd) {

		CommunityUserResponse res = new CommunityUserResponse();
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		Long time = System.currentTimeMillis();
		List<UserOrganizations> users = organizationProvider.listUserOrganizations(locator, pageSize, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId));
				query.addConditions(Tables.EH_USERS.STATUS.eq(UserStatus.ACTIVE.getCode()));
				if(null != cmd.getOrganizationId()){
					query.addConditions(Tables.EH_USER_ORGANIZATIONS.ORGANIZATION_ID.eq(cmd.getOrganizationId()));
				}


				if(UserSourceType.WEIXIN == UserSourceType.fromCode(cmd.getUserSourceType())){
					query.addConditions(Tables.EH_USERS.NAMESPACE_USER_TYPE.eq(NamespaceUserType.WX.getCode()));
				}else if(UserSourceType.APP == UserSourceType.fromCode(cmd.getUserSourceType())){
					query.addConditions(Tables.EH_USERS.NAMESPACE_USER_TYPE.isNull());
				}else if(UserSourceType.ALIPAY == UserSourceType.fromCode(cmd.getUserSourceType())){
					query.addConditions(Tables.EH_USERS.NAMESPACE_USER_TYPE.eq(NamespaceUserType.ALIPAY.getCode()));
				}

				if(null != cmd.getCommunityId()){
					query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.COMMUNITY_ID.eq(cmd.getCommunityId()));
				}

				if(!StringUtils.isEmpty(cmd.getKeywords())){
					Condition cond = Tables.EH_ORGANIZATION_MEMBERS.CONTACT_NAME.like("%" + cmd.getKeywords() + "%");
					cond = cond.or(Tables.EH_USERS.NICK_NAME.like("%" + cmd.getKeywords() + "%"));
					query.addConditions(cond);
				}

				if (!StringUtils.isBlank(cmd.getOrganizationNames())) {
				    String[] organizationName = cmd.getOrganizationNames().split(";");
				    Condition cond = Tables.EH_ORGANIZATIONS.NAME.like("%" + organizationName[0] + "%");
				    if (organizationName.length > 1) {
				        for (int i=1;i<organizationName.length;i++) {
				            cond = cond.or(Tables.EH_ORGANIZATIONS.NAME.like("%" + organizationName[i] + "%"));
                        }
                    }
				    query.addConditions(cond);
                }
                if (!StringUtils.isBlank(cmd.getPhone())) {
				    query.addConditions(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.like("%" + cmd.getPhone() + "%"));
                }
				if(null != UserGender.fromCode(cmd.getGender())){
					query.addConditions(Tables.EH_USERS.GENDER.eq(cmd.getGender()));
				}

				if(ExecutiveFlag.fromCode(cmd.getExecutiveFlag()) == ExecutiveFlag.YES){
					query.addConditions(Tables.EH_USER_ORGANIZATIONS.EXECUTIVE_TAG.eq(cmd.getExecutiveFlag()));
				}

				if(AuthFlag.AUTHENTICATED == AuthFlag.fromCode(cmd.getIsAuth())){
					query.addConditions(Tables.EH_USER_ORGANIZATIONS.STATUS.eq(UserOrganizationStatus.ACTIVE.getCode()));
				}else if(AuthFlag.PENDING_AUTHENTICATION == AuthFlag.fromCode(cmd.getIsAuth())){
					query.addConditions(Tables.EH_USER_ORGANIZATIONS.STATUS.eq(UserOrganizationStatus.WAITING_FOR_APPROVAL.getCode()));
				}else if(AuthFlag.UNAUTHORIZED == AuthFlag.fromCode(cmd.getIsAuth())){
					query.addConditions(Tables.EH_USER_ORGANIZATIONS.STATUS.isNull());
				}else if(AuthFlag.ALL == AuthFlag.fromCode(cmd.getIsAuth())) {
				    query.addConditions(Tables.EH_USER_ORGANIZATIONS.STATUS.isNotNull());
                }

				if(cmd.getStartTime() != null){
					query.addConditions(Tables.EH_USERS.CREATE_TIME.ge(new Timestamp(cmd.getStartTime())));
				}
				if(cmd.getEndTime() != null){
					query.addConditions(Tables.EH_USERS.CREATE_TIME.le(new Timestamp(cmd.getEndTime())));
				}


				query.addGroupBy(Tables.EH_USERS.ID);

				Condition cond = Tables.EH_USERS.ID.isNotNull();
				if(AuthFlag.UNAUTHORIZED == AuthFlag.fromCode(cmd.getIsAuth())){
					cond = cond.and(" `eh_users`.`id` not in (select user_id from eh_user_organizations where status = " + UserOrganizationStatus.ACTIVE.getCode() + " or status = " + UserOrganizationStatus.WAITING_FOR_APPROVAL.getCode() + ")");
				}else if(AuthFlag.PENDING_AUTHENTICATION == AuthFlag.fromCode(cmd.getIsAuth())){
					cond = cond.and(" `eh_users`.`id` not in (select user_id from eh_user_organizations where status = " + UserOrganizationStatus.ACTIVE.getCode() + ")");
				}

				if(ExecutiveFlag.fromCode(cmd.getExecutiveFlag()) == ExecutiveFlag.NO){
					cond = cond.and(" `eh_users`.`id` not in (select user_id from eh_user_organizations where executive_tag = " + ExecutiveFlag.YES.getCode()  + ")");
				}

				query.addHaving(cond);

				return query;
			}
		});

		LOGGER.debug("Get user organization list time:{}", System.currentTimeMillis() - time);
		List<CommunityUserDto> userCommunities = new ArrayList<>();

		for(UserOrganizations r: users){
			CommunityUserDto dto = ConvertHelper.convert(r, CommunityUserDto.class);
			//dto.setUserName(r.getNickName());
			dto.setNickName(r.getNickName());
			dto.setPhone(r.getPhoneNumber());
			dto.setApplyTime(r.getRegisterTime());
			dto.setIdentityNumber(r.getIdentityNumberTag());
			dto.setExecutiveFlag(r.getExecutiveTag());
			//最新活跃时间 add by sfyan 20170620
			List<UserActivity> userActivities = userActivityProvider.listUserActivetys(r.getUserId(), 1);
			if(userActivities.size() > 0){
				dto.setRecentlyActiveTime(userActivities.get(0).getCreateTime().getTime());
			}

			LOGGER.debug("user,userName:{}/userPhone:{}/userStatus:{}",r.getNickName(), r.getPhoneNumber(),r.getStatus());
			dto.setIsAuth(AuthFlag.UNAUTHORIZED.getCode());

			if (UserOrganizationStatus.WAITING_FOR_APPROVAL == UserOrganizationStatus.fromCode(r.getStatus()) || UserOrganizationStatus.ACTIVE == UserOrganizationStatus.fromCode(r.getStatus())) {
				List<OrganizationMember> ms = new ArrayList<>();
				List<OrganizationMember> members = organizationProvider.listOrganizationMembers(r.getUserId());
				dto.setIsAuth(AuthFlag.PENDING_AUTHENTICATION.getCode());
                Set<String> nameSet = new HashSet<>();

                for (OrganizationMember member : members) {
					if (OrganizationMemberStatus.ACTIVE == OrganizationMemberStatus.fromCode(member.getStatus()) && OrganizationGroupType.ENTERPRISE == OrganizationGroupType.fromCode(member.getGroupType())) {
						dto.setIsAuth(AuthFlag.AUTHENTICATED.getCode());
					}
                    if (!CollectionUtils.isEmpty(members)) {
                        nameSet.add(member.getContactName());
                    }
					ms.add(member);
				}
				String userName = "-";
                if (nameSet.size() > 0 && nameSet.size() <= 2) {
                    userName = nameSet.toString().substring(1, nameSet.toString().length() -1 ).replaceAll(",",";");
                }else if (nameSet.size() > 2) {
                    List<String> nameList = Lists.newArrayList(nameSet);
                    userName = nameList.get(0) + ";" + nameList.get(1) + "...";
                }
				dto.setUserName(userName);
				List<OrganizationDetailDTO> organizations = new ArrayList<>();
				organizations.addAll(populateOrganizationDetails(ms));
				dto.setOrganizations(organizations);
			} else {
				dto.setIsAuth(AuthFlag.UNAUTHORIZED.getCode());
			}

			User user = userProvider.findUserById(r.getUserId());
			if(user != null && NamespaceUserType.fromCode(user.getNamespaceUserType()) == NamespaceUserType.WX){
				dto.setUserSourceType(UserSourceType.WEIXIN.getCode());
			} else if (user != null && NamespaceUserType.fromCode(user.getNamespaceUserType()) == NamespaceUserType.ALIPAY) {
				dto.setUserSourceType(UserSourceType.ALIPAY.getCode());
			}

//			if(null != dto.getPhone()){
//				dto.setUserSourceType(UserSourceType.APP.getCode());
//			}
			userCommunities.add(dto);
		}
		LOGGER.debug("Get user detail list time:{}", System.currentTimeMillis() - time);
		res.setNextPageAnchor(locator.getAnchor());
		res.setUserCommunities(userCommunities);
		return res;
	}

	private CommunityUserResponse listUnAuthUsersForCommercial(ListCommunityUsersCommand cmd){
		CommunityUserResponse response = new CommunityUserResponse();
		//未认证的用户就不是高管，反正后面就是当这个参数没有。就在这限制一下吧，不然要是前端没限制这个条件会吓到测试
		if(ExecutiveFlag.fromCode(cmd.getExecutiveFlag()) == ExecutiveFlag.YES){
			return response;
		}
		List<CommunityUserDto> dtos = new ArrayList<>();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		List<User> users = userActivityProvider.listUnAuthUsersByProfileCommunityId(
		        cmd.getNamespaceId(),
                cmd.getCommunityId(), cmd.getPageAnchor(), pageSize + 1,
                CommunityType.COMMERCIAL.getCode(), cmd.getUserSourceType(),
                cmd.getKeywords(), cmd.getStartTime(), cmd.getEndTime());

		if(users != null){
			if(users.size() > pageSize){
				users.remove(pageSize);
				response.setNextPageAnchor(users.get(pageSize -1).getId());
			}
			for(User u: users){
				CommunityUserDto dto = new CommunityUserDto();
				//dto.setUserName(u.getNickName());
				dto.setNickName(u.getNickName());
				dto.setUserId(u.getId());
				// dto.setPhone(r.getPhoneNumber());
				dto.setApplyTime(u.getCreateTime());
				dto.setIdentityNumber(u.getIdentityNumberTag());
				dto.setGender(u.getGender());
				dto.setIsAuth(AuthFlag.UNAUTHORIZED.getCode());
				if(NamespaceUserType.fromCode(u.getNamespaceUserType()) == NamespaceUserType.WX){
					dto.setUserSourceType(UserSourceType.WEIXIN.getCode());
				} else if (NamespaceUserType.fromCode(u.getNamespaceUserType()) == NamespaceUserType.ALIPAY){
					dto.setUserSourceType(UserSourceType.ALIPAY.getCode());
				}

				//最新活跃时间 add by sfyan 20170620
				List<UserActivity> userActivities = userActivityProvider.listUserActivetys(u.getId(), 1);
				if(userActivities.size() > 0){
					dto.setRecentlyActiveTime(userActivities.get(0).getCreateTime().getTime());
				}

				UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(u.getId(), IdentifierType.MOBILE.getCode());
				if(userIdentifier != null){
					dto.setPhone(userIdentifier.getIdentifierToken());
				}

				dtos.add(dto);
			}

			response.setUserCommunities(dtos);
		}

		return response;
	}

	private CommunityUserAddressResponse listUnAuthUsersForResidential(ListCommunityUsersCommand cmd){
		CommunityUserAddressResponse response = new CommunityUserAddressResponse();

		//未认证的用户就不是高管，反正后面就是当这个参数没有。就在这限制一下吧，不然要是前端没限制这个条件会吓到测试
		if(ExecutiveFlag.fromCode(cmd.getExecutiveFlag()) == ExecutiveFlag.YES){
			return response;
		}
		List<CommunityUserAddressDTO> dtos = new ArrayList<>();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		List<User> users = userActivityProvider.listUnAuthUsersByProfileCommunityId(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getPageAnchor(), pageSize + 1, CommunityType.RESIDENTIAL.getCode(), cmd.getUserSourceType(), cmd.getKeywords(), cmd.getStartTime(), cmd.getEndTime());
		if(users != null){
			if(users.size() > pageSize){
				users.remove(pageSize);
				response.setNextPageAnchor(users.get(pageSize -1).getId());
			}
			for(User u: users){
				CommunityUserAddressDTO dto = new CommunityUserAddressDTO();
				//dto.setUserName(u.getNickName());
				dto.setNickName(u.getNickName());
				// dto.setPhone(r.getPhoneNumber());
				dto.setApplyTime(u.getCreateTime());
				dto.setIdentityNumber(u.getIdentityNumberTag());
				dto.setGender(u.getGender());
				dto.setIsAuth(AuthFlag.UNAUTHORIZED.getCode());
				if(NamespaceUserType.fromCode(u.getNamespaceUserType()) == NamespaceUserType.WX){
					dto.setUserSourceType(UserSourceType.WEIXIN.getCode());
				} else if (NamespaceUserType.fromCode(u.getNamespaceUserType()) == NamespaceUserType.ALIPAY){
					dto.setUserSourceType(UserSourceType.ALIPAY.getCode());
				}

				//最新活跃时间 add by sfyan 20170620
				List<UserActivity> userActivities = userActivityProvider.listUserActivetys(u.getId(), 1);
				if(userActivities.size() > 0){
					dto.setRecentlyActiveTime(userActivities.get(0).getCreateTime().getTime());
				}

				UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(u.getId(), IdentifierType.MOBILE.getCode());
				if(userIdentifier != null){
					dto.setPhone(userIdentifier.getIdentifierToken());
				}

				dtos.add(dto);
			}


			response.setDtos(dtos);
		}


		return response;
	}

	@Override
	public void exportCommunityUsers(ListCommunityUsersCommand cmd, HttpServletResponse response) {

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		cmd.setPageSize(pageSize);
		cmd.setPageAnchor(null);

		Community community = communityProvider.findCommunityById(cmd.getCommunityId());

		if(CommunityType.fromCode(community.getCommunityType()) == CommunityType.RESIDENTIAL){
			exportCommunityUsersForResidential(cmd, response);
		}else {
			exportCommunityUsersForCommercial(cmd, response);
		}

	}

    @Override
    public void exportAllCommunityUsers(ExportAllCommunityUsersCommand cmd) {

        Map<String, Object> params = new HashMap<>();
        try {
            params = objectToMap(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        String fileName = "用户列表.xlsx";
        Namespace namespace  = this.namespaceProvider.findNamespaceById(namespaceId);
        if (namespace != null) {
            fileName = namespace.getName()+"用户列表.xlsx";
        }
        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), CommunityAllUserApplyExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new Date());

    }

    @Override
    public void exportBatchCommunityUsers(ExportBatchCommunityUsersCommand cmd) {
        Map<String, Object> params = new HashMap<>();
        try {
            params = objectToMap(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        String fileName = "用户列表.xlsx";
        Namespace namespace  = this.namespaceProvider.findNamespaceById(namespaceId);
        if (namespace != null) {
            fileName = namespace.getName()+"用户列表.xlsx";
        }
        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), CommunityBatchUserApplyExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new Date());

    }

    public void exportCommunityUsersForCommercial(ListCommunityUsersCommand cmd, HttpServletResponse response) {

        Map<String, Object> params = new HashMap<>();
        try {
            params = objectToMap(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId();
		String fileName = "用户列表.xlsx";
		Namespace namespace  = this.namespaceProvider.findNamespaceById(namespaceId);
		if (namespace != null) {
			fileName = namespace.getName()+"用户列表.xlsx";
		}
		taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), CommunityUserApplyExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new Date());

	}
    private Map<String, Object> objectToMap(Object obj) throws Exception {
        if(obj == null){
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();

        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }

        return map;
    }

    public void exportCommunityUsersForResidential(ListCommunityUsersCommand cmd, HttpServletResponse response) {

        Map<String, Object> params = new HashMap<>();
        try {
            params = objectToMap(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        String fileName = "用户列表.xlsx";
        Namespace namespace  = this.namespaceProvider.findNamespaceById(namespaceId);
        if (namespace != null) {
            fileName = namespace.getName()+"用户列表.xlsx";
        }
        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), CommunityResidentialUserApplyExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new Date());
	}

	@Override
	public CountCommunityUserResponse countCommunityUsers(CountCommunityUsersCommand cmd) {

		int namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		if(namespaceId == Namespace.DEFAULT_NAMESPACE){
            LOGGER.error("Invalid parameter namespaceId={}", namespaceId);
            throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter namespaceId=" + namespaceId );
        }

        if(cmd.getCommunityId() == null){
            int allCount = userProvider.countUserByNamespaceId(namespaceId, null);
            int wxCount = userProvider.countUserByNamespaceIdAndNamespaceUserType(namespaceId, NamespaceUserType.WX.getCode());
            int alipayCount = userProvider.countUserByNamespaceIdAndNamespaceUserType(namespaceId, NamespaceUserType.ALIPAY.getCode());
            int maleCount = userProvider.countUserByNamespaceIdAndGender(namespaceId, UserGender.MALE.getCode());
            int femaleCount = userProvider.countUserByNamespaceIdAndGender(namespaceId, UserGender.FEMALE.getCode());

            int authUsers = 0;
            int authingUsers = 0;
//            int notAuthUsers = 0;
            List<Community> communities = this.communityProvider.listNamespaceCommunities(namespaceId);
            if (!CollectionUtils.isEmpty(communities)) {
                for (Community community : communities) {
                    if(CommunityType.fromCode(community.getCommunityType()) == CommunityType.RESIDENTIAL){
                        //小区用户统计
                        cmd.setCommunityId(community.getId());
                        CountCommunityUserResponse countCommunityUserResponse = this.countCommunityUsersForResidential(cmd);
                        authUsers += countCommunityUserResponse.getAuthUsers();
                        authingUsers += countCommunityUserResponse.getAuthingUsers();
                        //未认证用户 userprofile表中的用户-已认证或者认证中的用户
                        List<User> users = userActivityProvider.listUnAuthUsersByProfileCommunityId(cmd.getNamespaceId(), cmd.getCommunityId(), null, 1000000, CommunityType.RESIDENTIAL.getCode(), null, null);

                        //未认证
//                        int notAuthCount = 0;
//                        if(users != null){
//                            notAuthCount = users.size();
//                        }
//                        notAuthUsers += notAuthCount;
                    }else {
                        //园区用户统计
                        cmd.setCommunityId(community.getId());
                        CountCommunityUserResponse countCommunityUserResponse = this.countCommunityUsersForCommercial(cmd);
                        authUsers += countCommunityUserResponse.getAuthUsers();
                        authingUsers += countCommunityUserResponse.getAuthingUsers();

//                        List<User> users = userActivityProvider.listUnAuthUsersByProfileCommunityId(cmd.getNamespaceId(), cmd.getCommunityId(), null, 1000000, CommunityType.COMMERCIAL.getCode(), null, null);
//                        if(users == null){
//                            users = new ArrayList<>();
//                        }
//                        int notAuthCount = users.size();
//                        notAuthUsers += notAuthCount;
                    }
                }
            }
            CountCommunityUserResponse resp = new CountCommunityUserResponse();
            resp.setCommunityUsers(allCount);
            resp.setAuthUsers(authUsers);
            resp.setAuthingUsers(authingUsers);
            // 越来越多的用户来源域空间下全部用户统计时未认证人数统计不准确，认证和待认证是准确的，故改为总数减去已认证和待认证。
//            resp.setNotAuthUsers(notAuthUsers);
			resp.setNotAuthUsers(allCount - authUsers - authingUsers);
            resp.setWxUserCount(wxCount);
            resp.setAlipayUserCount(alipayCount);
            resp.setAppUserCount(allCount - wxCount - alipayCount);
            resp.setMaleCount(maleCount);
            resp.setFemaleCount(femaleCount);

            return resp;
        }

		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(CommunityType.fromCode(community.getCommunityType()) == CommunityType.RESIDENTIAL){
		    //小区用户统计
			return countCommunityUsersForResidential(cmd);
		}else {
            //园区用户统计
			return  countCommunityUsersForCommercial(cmd);
		}
	}

	private CountCommunityUserResponse countCommunityUsersForResidential(
			CountCommunityUsersCommand cmd) {
		List<Group> groups = groupProvider.listGroupByCommunityId(cmd.getCommunityId(), (loc, query) -> {
			Condition c = Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode());
			query.addConditions(c);
			return query;
		});

		List<Long> groupIds = new ArrayList<Long>();
		for (Group group : groups) {
			groupIds.add(group.getId());
		}

		CrossShardListingLocator locator = new CrossShardListingLocator();
		List<GroupMember> groupMembers = groupProvider.listGroupMemberByGroupIds(groupIds,locator,null,(loc, query) -> {
			Condition c = Tables.EH_GROUP_MEMBERS.MEMBER_TYPE.eq(EntityType.USER.getCode());
			c = c.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.notIn(GroupMemberStatus.INACTIVE.getCode(), GroupMemberStatus.REJECT.getCode()));
			query.addConditions(c);
			query.addGroupBy(Tables.EH_GROUP_MEMBERS.MEMBER_ID);
			return query;
		});

		//全部用户、已认证用户(只要有一个已认证) add by yanjun 20171018
		Set<Long> memberIds = new HashSet<>();
		Set<Long> authMemberIds = new HashSet<>();
		for(GroupMember m: groupMembers){
			memberIds.add(m.getMemberId());
			if(GroupMemberStatus.fromCode(m.getMemberStatus()) == GroupMemberStatus.ACTIVE){
				authMemberIds.add(m.getMemberId());
			}
		}

		//已认证
		int authCount = authMemberIds.size();
		//认证中 = 已认证、认证中 - 已认证
		int authingCount = memberIds.size() - authCount;

		//未认证用户 userprofile表中的用户-已认证或者认证中的用户
		List<User> users = userActivityProvider.listUnAuthUsersByProfileCommunityId(cmd.getNamespaceId(), cmd.getCommunityId(), null, 1000000, CommunityType.RESIDENTIAL.getCode(), null, null);

		//未认证
		int notAuthCount = 0;
		if(users != null){
			notAuthCount = users.size();
		}

		//全部 = 认证 + 认证中 + 未认证
		int allCount = authCount + authingCount + notAuthCount;

        //绑定微信的、男性、女性
        Set<Long> wxMemberIds = new HashSet<>();
        Set<Long> maleMemberIds = new HashSet<>();
        Set<Long> femaleMemberIds = new HashSet<>();

        List<User> allUsers = new ArrayList<>();

        //已认证或认证中
        List<Long> userIds = new ArrayList<>(memberIds);
        List<User> memberUsers = userProvider.listUserByIds(cmd.getNamespaceId(), userIds);
		allUsers.addAll(memberUsers);

        for(User u: allUsers){
            if(NamespaceUserType.fromCode(u.getNamespaceUserType()) == NamespaceUserType.WX){
                wxMemberIds.add(u.getId());
            }

            if(UserGender.fromCode(u.getGender()) == UserGender.MALE){
                maleMemberIds.add(u.getId());
            }

            if(UserGender.fromCode(u.getGender()) == UserGender.FEMALE){
                femaleMemberIds.add(u.getId());
            }
        }

		//绑定微信
		int wxCount = wxMemberIds.size();
        //男性用户
        int maleCount = maleMemberIds.size();
        //女性用户
        int femaleCount = femaleMemberIds.size();

		CountCommunityUserResponse resp = new CountCommunityUserResponse();
		resp.setCommunityUsers(allCount);
		resp.setAuthUsers(authCount);
		resp.setAuthingUsers(authingCount);
        resp.setNotAuthUsers(notAuthCount);
        resp.setWxUserCount(wxCount);
		resp.setAppUserCount(allCount - wxCount);
		resp.setMaleCount(maleCount);
		resp.setFemaleCount(femaleCount);

		return resp;
	}

	private CountCommunityUserResponse countCommunityUsersForCommercial(
			CountCommunityUsersCommand cmd) {

		//已认证、认证中
		int communityUserCount = organizationProvider.countUserOrganization(cmd.getNamespaceId(), cmd.getCommunityId());

		//已认证
		int authCount = organizationProvider.countUserOrganization(cmd.getNamespaceId(), cmd.getCommunityId(), UserOrganizationStatus.ACTIVE.getCode());

		//已认证、认证中的微信微信用户
		int wxAuthCount = organizationProvider.countUserOrganization(cmd.getNamespaceId(), cmd.getCommunityId(), null, NamespaceUserType.WX.getCode(), null);

        //未认证用户 userprofile表中的用户-已认证或者认证中的用户
        List<User> users = userActivityProvider.listUnAuthUsersByProfileCommunityId(cmd.getNamespaceId(), cmd.getCommunityId(), null, 1000000, CommunityType.COMMERCIAL.getCode(), null, null);
        if(users == null){
            users = new ArrayList<>();
        }
        int notAuthCount = users.size();

		//认证中 = 已认证、认证中 - 已认证
		int authingCount = communityUserCount - authCount;

		//总用户 =  已认证 + 认证中 + 未认证
		int allCount = authCount + authingCount + notAuthCount;


		//微信用户 = 已认证、认证中的微信微信用户
		int wxCount =  wxAuthCount;

        //男性用户
        //已认证、认证中
        int maleAuthCount = organizationProvider.countUserOrganization(cmd.getNamespaceId(), cmd.getCommunityId(), null, null, UserGender.MALE.getCode());
        int maleCount = maleAuthCount;
        //女性用户
        int femaleAuthCount = organizationProvider.countUserOrganization(cmd.getNamespaceId(), cmd.getCommunityId(), null, null, UserGender.FEMALE.getCode());
        int femaleCount = femaleAuthCount;

		CountCommunityUserResponse resp = new CountCommunityUserResponse();
		resp.setCommunityUsers(allCount);
		resp.setAuthUsers(authCount);
		resp.setAuthingUsers(authingCount);
        resp.setNotAuthUsers(notAuthCount);
        resp.setWxUserCount(wxCount);
		resp.setAppUserCount(allCount - wxCount);
		resp.setMaleCount(maleCount);
		resp.setFemaleCount(femaleCount);

		return resp;
	}

	@Override
	public CommunityUserAddressResponse listUserByNotJoinedCommunity(
			ListCommunityUsersCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		List<CommunityUserAddressDTO> dtos = new ArrayList<CommunityUserAddressDTO>();
		CommunityUserAddressResponse res = new CommunityUserAddressResponse();
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<User> users = userProvider.listUserByKeyword(cmd.getKeywords(), namespaceId, locator, Integer.MAX_VALUE);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		for (User user : users) {
			List<UserGroup> groups = userProvider.listUserGroups(user.getId(), GroupDiscriminator.FAMILY.getCode());
			if(null == groups || groups.size() == 0){
				CommunityUserAddressDTO dto = new CommunityUserAddressDTO();
				dto.setUserId(user.getId());
				dto.setUserName(user.getNickName());
				dto.setGender(user.getGender());
				dto.setIsAuth(2);
				dto.setPhone(user.getIdentifierToken());
				dto.setApplyTime(user.getCreateTime());
				dtos.add(dto);
				if(dtos.size() == pageSize){
					res.setDtos(dtos);
					res.setNextPageAnchor(user.getCreateTime().getTime());
					break;
				}
			}
		}

		return res;
	}

	@Override
	public List<CommunityDTO> listUnassignedCommunitiesByNamespaceId() {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		List<NamespaceResource> resources = namespaceResourceProvider.listResourceByNamespace(namespaceId, NamespaceResourceType.COMMUNITY);
		List<CommunityDTO> dtos = new ArrayList<CommunityDTO>();
		for (NamespaceResource namespaceResource : resources) {
			List<OrganizationCommunityDTO> organizationCommunities = organizationProvider.findOrganizationCommunityByCommunityId(namespaceResource.getResourceId());
			if(null == organizationCommunities || organizationCommunities.size() == 0){
				Community community = communityProvider.findCommunityById(namespaceResource.getResourceId());
				dtos.add(ConvertHelper.convert(community, CommunityDTO.class));
			}
		}
		return dtos;
	}


	@Override
	public CreateCommunityResponse createCommunity(final CreateCommunityCommand cmd) {
		final Long userId = UserContext.current().getUser().getId();
		final Integer namespaceId = cmd.getNamespaceId();
		checkCreateCommunityParameters(userId, cmd);

		CommunityDTO community = createCommunity(userId, namespaceId, cmd);

		CreateCommunityResponse response = new CreateCommunityResponse();
		response.setCommunityDTO(community);
		return response;
	}

	private CommunityDTO createCommunity(final Long userId, final Integer namespaceId, final CreateCommunityCommand cmd){
		List<CommunityDTO> communities = new ArrayList<>();
		List<Community> cs = new ArrayList<>();
		List<CommunityGeoPointDTO> points = new ArrayList<>();
		dbProvider.execute(s->{
			Long provinceId = createRegion(userId, namespaceId, getPath(cmd.getProvinceName()), 0L);  //创建省
			Long cityId = createRegion(userId, namespaceId, getPath(cmd.getProvinceName(), cmd.getCityName()), provinceId);  //创建市
			Long areaId = createRegion(userId, namespaceId, getPath(cmd.getProvinceName(), cmd.getCityName(), cmd.getAreaName()), cityId);  //创建区县

			cmd.setProvinceId(provinceId);
			cmd.setCityId(cityId);
			cmd.setAreaId(areaId);
			Community community = createCommunity(userId, cmd);
			cs.add(community);
//创建经纬度数据
			CommunityGeoPoint point = createCommunityGeoPoint(community.getId(), cmd.getLatitude(), cmd.getLongitude());
//添加园区与域空间的关联
			createNamespaceResource(namespaceId, community.getId());



			//标准版先写的使用pmOrgId创建OrganizationCommunity，后面定制版又使用了organizationId通过接口创建园区。
			//TODO 以后谁有心情把它统一一下吧，现在就这样
			//增加管理公司
			if(cmd.getPmOrgId() != null){
				organizationService.createOrganizationCommunity(cmd.getPmOrgId(), community.getId());
			}else if(cmd.getOrganizationId() != null){
				//添加企业可见园区,管理公司可以看到添加的园区
				OrganizationCommunity organizationCommunity = new OrganizationCommunity();
				organizationCommunity.setCommunityId(community.getId());
				organizationCommunity.setOrganizationId(cmd.getOrganizationId());
				organizationProvider.createOrganizationCommunity(organizationCommunity);
			}


			if(namespacesService.isStdNamespace(namespaceId) && cmd.getPmOrgId() != null){
				//新增所有已安装应用的授权
				serviceModuleAppAuthorizationService.updateAllAuthToNewOrganization(namespaceId, cmd.getPmOrgId(), community.getId());
			}

			points.add(ConvertHelper.convert(point, CommunityGeoPointDTO.class));
			CommunityDTO cd = ConvertHelper.convert(community, CommunityDTO.class);
			cd.setGeoPointList(points);
			communities.add(cd);
			return true;
		});
		communitySearcher.feedDoc(cs.get(0));
		return communities.get(0);
	}

	private Long createRegion(Long userId, Integer namespaceId, String path, Long parentId) {
		Region region = null;
		//查看此域空间是否存在该区域，若存在，则不添加
		region = regionProvider.findRegionByPath(namespaceId, path);
		if (region != null && region.getId() != null) {
			return region.getId();
		}
		//查看左邻域下是否存在该区域，若存在，则复制
		region = regionProvider.findRegionByPath(Namespace.DEFAULT_NAMESPACE, path);
		if (region != null && region.getId() != null) {
			region.setParentId(parentId);
			region.setId(null);
			region.setNamespaceId(namespaceId);
			regionProvider.createRegion(region);
			return region.getId();
		}
		//如果左邻域下也没有就抛出异常
		LOGGER.error(
				"Invalid region, operatorId=" + userId + ", path=" + path);
		throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_REGION_NOT_EXIST,
				"Invalid region");
	}

	private Community createCommunity(Long userId, CreateCommunityCommand cmd) {
		//创建社区
		Community community = ConvertHelper.convert(cmd, Community.class);
		community.setAptCount(0);


		//设置默认的forumId，要使用原有的项目里的forumId
		ListingLocator locator = new ListingLocator();
		List<Community> communities = communityProvider.listCommunities(UserContext.getCurrentNamespaceId(), locator, 1, null);
		if(communities != null && communities.size() > 0){
			community.setDefaultForumId(communities.get(0).getDefaultForumId());
			community.setFeedbackForumId(communities.get(0).getFeedbackForumId());
		}else {
			community.setDefaultForumId(1L);
			community.setFeedbackForumId(2L);
		}


		if(cmd.getStatus() != null){
			community.setStatus(cmd.getStatus());
		}else {
			community.setStatus(CommunityAdminStatus.ACTIVE.getCode());
		}

		communityProvider.createCommunity(userId, community);

		return community;
	}

	private CommunityGeoPoint createCommunityGeoPoint(Long communityId, Double latitude, Double longitude){
		//创建经纬度
		CommunityGeoPoint point = new CommunityGeoPoint();
		point.setCommunityId(communityId);
		point.setLatitude(latitude);
		point.setLongitude(longitude);
		point.setGeohash(GeoHashUtils.encode(latitude, longitude));
		communityProvider.createCommunityGeoPoint(point);
		return point;
	}

	private NamespaceResource createNamespaceResource(Integer namespaceId, Long resourceId) {
		NamespaceResource resource = new NamespaceResource();
		resource.setNamespaceId(namespaceId);
		resource.setResourceId(resourceId);
		resource.setResourceType(NamespaceResourceType.COMMUNITY.getCode());
		namespaceResourceProvider.createNamespaceResource(resource);
		return resource;
	}

	private void checkCreateCommunityParameters(final Long userId, final CreateCommunityCommand cmd) {
		if (cmd.getNamespaceId() == null || StringUtils.isBlank(cmd.getName()) || cmd.getCommunityType() == null
				|| StringUtils.isBlank(cmd.getProvinceName()) || StringUtils.isBlank(cmd.getCityName())
				|| StringUtils.isBlank(cmd.getAreaName()) || CommunityType.fromCode(cmd.getCommunityType()) == null) {
			LOGGER.error(
					"Invalid parameters, operatorId=" + userId + ", cmd=" + cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters");
		}
	}

	private String getPath(String... names) {
		StringBuilder sb = new StringBuilder();
		for (String name : names) {
			sb.append("/").append(name.trim());
		}
		return sb.toString();
	}

	@Override
	public void importCommunity(ImportCommunityCommand cmd, MultipartFile[] files) {
		Long userId = UserContext.current().getUser().getId();
		if (cmd.getNamespaceId()==null) {
			LOGGER.error(
					"Invalid parameters, operatorId=" + userId + ", cmd=" + cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters");
		}
		List<CreateCommunityCommand> list = getCommunitiesFromExcel(userId, cmd, files);
		list.forEach(c->createCommunity(userId, cmd.getNamespaceId(), c));
	}


	@SuppressWarnings("unchecked")
	private List<CreateCommunityCommand> getCommunitiesFromExcel(Long userId, ImportCommunityCommand cmd, MultipartFile[] files) {
		List<RowResult> resultList = null;
		try {
			resultList = PropMrgOwnerHandler.processorExcel(files[0].getInputStream());
		} catch (IOException e) {
			LOGGER.error("processStat Excel error, operatorId=" + userId + ", cmd=" + cmd);
			throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE,
					PropertyErrorCode.ERROR_PARSE_FILE, "processStat Excel error");
		}

		if (resultList != null && resultList.size() > 0) {
			final List<CreateCommunityCommand> list = new ArrayList<>();
			for (int i = 1, len = resultList.size(); i < len; i++) {
				RowResult result = resultList.get(i);

				CreateCommunityCommand ccmd = new CreateCommunityCommand();
				ccmd.setNamespaceId(cmd.getNamespaceId());
				ccmd.setName(RowResult.trimString(result.getA()));
				ccmd.setAliasName(RowResult.trimString(result.getB()));
				ccmd.setAddress(RowResult.trimString(result.getC()));
				ccmd.setDescription(RowResult.trimString(result.getD()));
				ccmd.setCommunityType(Byte.parseByte(RowResult.trimString(result.getE())));
				ccmd.setProvinceName(RowResult.trimString(result.getF()));
				ccmd.setCityName(RowResult.trimString(result.getG()));
				ccmd.setAreaName(RowResult.trimString(result.getH()));
				ccmd.setLatitude(Double.parseDouble(RowResult.trimString(result.getI())));
				ccmd.setLongitude(Double.parseDouble(RowResult.trimString(result.getJ())));

				checkCreateCommunityParameters(userId, ccmd);
				list.add(ccmd);
			}
			return list;
		}
		LOGGER.error("excel data format is not correct.rowCount=" + resultList.size());
		throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_EXCEL_DATA_FORMAT,
				"excel data format is not correct");
	}


	@Override
	public ListCommunityByNamespaceIdResponse listCommunityByNamespaceId(ListCommunityByNamespaceIdCommand cmd) {
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Long pageAnchor = cmd.getPageAnchor() == null? 0 : cmd.getPageAnchor();

		ListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(pageAnchor);

		List<CommunityDTO> communities = communityProvider.listCommunitiesByNamespaceId(null, cmd.getNamespaceId(), locator, pageSize+1);

		ListCommunityByNamespaceIdResponse response = new ListCommunityByNamespaceIdResponse();
		if (communities != null && communities.size() > 0) {
			if (communities.size() > pageSize) {
				communities.remove(communities.size()-1);
				pageAnchor = communities.get(communities.size()-1).getId();
			}else {
				pageAnchor = null;
			}
			populateCommunityGeoPoint(communities);
			response.setCommunities(communities);
			response.setNextPageAnchor(pageAnchor);
		}

		return response;
	}

	private void populateCommunityGeoPoint(List<CommunityDTO> communities) {
		communities.forEach(c->c.setGeoPointList(
				communityProvider.listCommunityGeoPoints(c.getId()).stream().map(r->ConvertHelper.convert(r, CommunityGeoPointDTO.class))
				.collect(Collectors.toList()))
				);
	}


	@Override
	public void communityImportBaseConfig(CommunityImportBaseConfigCommand cmd) {
		if (StringUtils.isBlank(cmd.getNamespaceName()) || StringUtils.isBlank(cmd.getCommunityType())
				|| NamespaceCommunityType.fromCode(cmd.getCommunityType())==null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: "+cmd);
		}
		dbProvider.execute(s->{
			Integer namespaceId = createNamespace(cmd.getNamespaceName());
			createNamespaceDetail(namespaceId, cmd.getCommunityType());
			createVersion(namespaceId, cmd.getAndroidRealm(), cmd.getIosRealm(), cmd.getVersionDescription(), cmd.getVersionRange(), cmd.getTargetVersion(), cmd.getForceUpgrade());
			createSmsTemplate(namespaceId, cmd.getSmsTemplates());
			createAppAgreementsUrl(namespaceId, cmd.getAppAgreementsUrl());
			createHomeUrl(namespaceId, cmd.getHomeUrl());
			createPostTypes(namespaceId, cmd.getPostTypes());
			return true;
		});
	}


	private Integer createNamespace(String namespaceName) {
		Namespace namespace = new Namespace();
		namespace.setName(namespaceName);
		namespacesProvider.createNamespace(namespace);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create namespace success: namespaceId="+namespace.getId()+", namespaceName="+namespaceName);
		}
		return namespace.getId();
	}

	private void createNamespaceDetail(Integer namespaceId, String communityType) {
		NamespaceDetail namespaceDetail = new NamespaceDetail();
		namespaceDetail.setNamespaceId(namespaceId);
		namespaceDetail.setResourceType(communityType);
		namespacesProvider.createNamespaceDetail(namespaceDetail);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create namespace detail success: namespaceId="+namespaceId+", communityType="+communityType);
		}
	}

	private void createVersion(Integer namespaceId, String androidRealm, String iosRealm, String versionDescription,
			String versionRange, String targetVersion, Byte forceUpgrade) {
		createVersion(namespaceId, androidRealm, versionDescription, versionRange, targetVersion, forceUpgrade);
		createVersion(namespaceId, iosRealm, versionDescription, versionRange, targetVersion, forceUpgrade);
	}

	private void createVersion(Integer namespaceId, String realm, String description, String versionRange, String targetVersion, Byte forceUpgrade){
		if(StringUtils.isBlank(realm)){
			return ;
		}
		VersionRealm versionRealm = new VersionRealm();
		versionRealm.setRealm(realm);
		versionRealm.setDescription(description);
		//persist中无namespaceId，大师说不用管，add by tt， 20160817
		versionProvider.createVersionRealm(versionRealm);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create version realm success: namespaceId="+namespaceId+", realm="+realm);
		}

		if(StringUtils.isBlank(versionRange) || StringUtils.isBlank(targetVersion)){
			return;
		}
		VersionUpgradeRule rule = new VersionUpgradeRule();
        rule.setRealmId(versionRealm.getId());
        rule.setForceUpgrade(forceUpgrade);
        rule.setTargetVersion(targetVersion);
        rule.setOrder(0);
        VersionRange range = new VersionRange(versionRange);
        rule.setMatchingLowerBound(range.getLowerBound());
        rule.setMatchingUpperBound(range.getUpperBound());
        versionProvider.createVersionUpgradeRule(rule);
        if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create version upgrade rule success: namespaceId="+namespaceId+", targetVersion="+targetVersion+", versionRange="+versionRange);
		}
	}

	private void createSmsTemplate(Integer namespaceId, List<SmsTemplate> smsTemplates) {
		if(smsTemplates == null || smsTemplates.isEmpty()){
			return;
		}
		smsTemplates.forEach(s->{
			if(StringUtils.isNotBlank(s.getTitle())){
				LocaleTemplate localeTemplate = new LocaleTemplate();
				localeTemplate.setCode(s.getCode());
				localeTemplate.setDescription(s.getTitle());
				localeTemplate.setLocale("zh_CN");
				localeTemplate.setNamespaceId(namespaceId);
				localeTemplate.setScope("sms.default.yzx");
				localeTemplate.setText(s.getTemplateId());
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("create sms template success: namespaceId="+namespaceId+", title="+s.getTitle()+", templateId="+s.getTemplateId()+", code="+s.getCode());
				}
				localeTemplateProvider.createLocaleTemplate(localeTemplate);
			}
		});
	}


	private void createAppAgreementsUrl(Integer namespaceId, String appAgreementsUrl) {
		if (StringUtils.isBlank(appAgreementsUrl)) {
			return ;
		}
		Configurations configurations = new Configurations();
		configurations.setName("app.agreements.url");
		configurations.setValue(appAgreementsUrl);
		configurations.setNamespaceId(namespaceId);
		configurationsProvider.createConfiguration(configurations);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create app.agreements.url success: namespaceId="+namespaceId+", app.agreements.url="+appAgreementsUrl);
		}
	}


	private void createHomeUrl(Integer namespaceId, String homeUrl) {
		if (StringUtils.isBlank(homeUrl)) {
			return ;
		}
		Configurations configurations = new Configurations();
		configurations.setName("home.url");
		configurations.setValue(homeUrl);
		configurations.setNamespaceId(namespaceId);
		configurationsProvider.createConfiguration(configurations);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create home.url success: namespaceId="+namespaceId+", home.url="+homeUrl);
		}
	}


	private void createPostTypes(Integer namespaceId, List<String> postTypes) {
		if (postTypes == null || postTypes.isEmpty()) {
			return;
		}
		postTypes.forEach(p->{
			if(StringUtils.isNotBlank(p)){
				Category category = new Category();
				category.setParentId(1L);
				category.setLinkId(0L);
				category.setName(p);
				category.setPath("帖子/"+p);
				category.setDefaultOrder(0);
				category.setStatus((byte)0);
				category.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				category.setNamespaceId(namespaceId);
				categoryProvider.createCategory(category);
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("create post type success: namespaceId="+namespaceId+", post type="+p);
				}
			}
		});
	}


	@Override
	public void communityImportOrganizationConfig(CommunityImportOrganizationConfigCommand cmd) {
		if (StringUtils.isBlank(cmd.getOrganizationName()) || StringUtils.isBlank(cmd.getAdminNickname())
				|| cmd.getNamespaceId() == null || cmd.getCommunityId() == null || StringUtils.isBlank(cmd.getAdminPhone())
				|| StringUtils.isBlank(cmd.getGroupName())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: "+cmd);
		}
		dbProvider.execute(s->{
			Organization organization = createOrganization(cmd.getNamespaceId(), cmd.getOrganizationName());
			createOrganizationCommunityRequest(organization.getId(), cmd.getCommunityId());
			Group group = createGroup(cmd.getNamespaceId(), organization, cmd.getGroupName(), cmd.getGroupDisplayName());
			createForum(cmd.getNamespaceId(), group);
			createOrganizationCommunities(cmd.getNamespaceId(), organization.getId());
			User user = createUser(cmd.getNamespaceId(), cmd.getAdminNickname(), cmd.getAdminPhone());
			createOrganizationMember(cmd.getNamespaceId(), organization.getId(), user.getId(), user.getNickName(), cmd.getAdminPhone());
			createAclRoleAssignment(organization.getId(), user.getId());
			return true;
		});
	}


	private Organization createOrganization(Integer namespaceId, String organizationName) {
		Organization organization = new Organization();
		organization.setParentId(0L);
		organization.setOrganizationType(OrganizationType.PM.getCode());
		organization.setName(organizationName);
		organization.setAddressId(0L);
		organization.setLevel(1);
		organization.setStatus(OrganizationStatus.ACTIVE.getCode());
		organization.setGroupType(OrganizationGroupType.ENTERPRISE.getCode());
		organization.setDirectlyEnterpriseId(0L);
		organization.setNamespaceId(namespaceId);
		organization.setShowFlag((byte) 1);
		organization.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		organizationProvider.createOrganization(organization);
		assetService.linkCustomerToBill(AssetTargetType.ORGANIZATION.getCode(), organization.getId(), organization.getName());

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create organization success: namespaceId="+namespaceId+", organizationId="+organization.getId()+", organizationName"+organizationName);
		}
		return organization;
	}


	private void createOrganizationCommunityRequest(Long organizationId, Long communityId) {
		OrganizationCommunityRequest request = new OrganizationCommunityRequest();
		request.setCommunityId(communityId);
		request.setMemberType(OrganizationCommunityRequestType.Organization.getCode());
		request.setMemberId(organizationId);
		request.setMemberStatus(OrganizationMemberStatus.ACTIVE.getCode());
		organizationProvider.createOrganizationCommunityRequest(request);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create organization community request success: organizationId="+organizationId+", communityId="+communityId);
		}
	}


	private Group createGroup(Integer namespaceId, Organization organization, String groupName,
			String groupDisplayName) {
		Group group = new Group();
		group.setNamespaceId(namespaceId);
		group.setName(groupName);
		group.setDisplayName(groupDisplayName);
		group.setCreatorUid(UserContext.current().getUser().getId());
		group.setPrivateFlag(PrivateFlag.PRIVATE.getCode());
		group.setJoinPolicy(GroupJoinPolicy.NEED_APPROVE.getCode());
		group.setDiscriminator(GroupDiscriminator.ENTERPRISE.getCode());
		group.setStatus(GroupAdminStatus.ACTIVE.getCode());
		group.setMemberCount(0L);
		group.setShareCount(0L);
		group.setPostFlag(GroupPostFlag.ALL.getCode());
		group.setVisibleRegionType(VisibleRegionType.REGION.getCode());
		group.setVisibleRegionId(organization.getId());
		groupProvider.createGroup(group);

		//更新组织中的groupId
		organization.setGroupId(group.getId());
		organizationProvider.updateOrganization(organization);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create group success: namespaceId="+namespaceId+", groupName="+groupName);
		}

		return group;
	}


	private void createForum(Integer namespaceId, Group group) {
		Forum forum = new Forum();
		forum.setNamespaceId(namespaceId);
		forum.setAppId(AppConstants.APPID_FORUM);
		forum.setOwnerType(EntityType.GROUP.getCode());
		forum.setOwnerId(group.getId());
		forum.setName(EntityType.GROUP.getCode() + "-" + group.getId());
		forum.setPostCount(0L);
		forum.setModifySeq(0L);
		forum.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		forumProvider.createForum(forum);

		//更新group中圈论坛id
		group.setIntegralTag4(forum.getId());
		groupProvider.updateGroup(group);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create forum success: namespaceId="+namespaceId+", groupId="+group.getId());
		}
	}


	private void createOrganizationCommunities(Integer namespaceId, Long organizationId) {
		List<Community> communities = communityProvider.listCommunitiesByNamespaceId(namespaceId);
		if(communities == null || communities.isEmpty()){
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("create organization communities error: namespaceId="+namespaceId+", organizationId="+organizationId+", no community!");
			}
			return ;
		}
		communities.forEach(c->{
			OrganizationCommunity organizationCommunity = new OrganizationCommunity();
			organizationCommunity.setCommunityId(c.getId());
			organizationCommunity.setOrganizationId(organizationId);
			organizationProvider.createOrganizationCommunity(organizationCommunity);
		});
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create organization communities success: namespaceId="+namespaceId+", organizationId="+organizationId+", communities count="+communities.size());
		}
	}


	private User createUser(Integer namespaceId, String adminNickname, String adminPhone) {
		User user = new User();
		user.setNickName(adminNickname);
		user.setStatus(UserStatus.ACTIVE.getCode());
		user.setPoints(0);
		user.setLevel(UserLevel.L1.getCode());
		user.setGender(UserGender.UNDISCLOSURED.getCode());
		user.setLocale("zh_CN");
		user.setNamespaceId(namespaceId);
		String salt = EncryptionUtils.createRandomSalt();
		user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", "123456",salt)));
		user.setSalt(salt);
		userProvider.createUser(user);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create user success: namespaceId="+namespaceId+", adminNickname="+adminNickname+", adminPhone="+adminPhone);
		}

		UserIdentifier userIdentifier = new UserIdentifier();
		userIdentifier.setOwnerUid(user.getId());
		userIdentifier.setIdentifierType(IdentifierType.MOBILE.getCode());
		userIdentifier.setIdentifierToken(adminPhone);
		userIdentifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
		userIdentifier.setNamespaceId(namespaceId);
		userProvider.createIdentifier(userIdentifier);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create user identifier success: namespaceId="+namespaceId+", userId="+user.getId());
		}

		return user;
	}


	private void createOrganizationMember(Integer namespaceId, Long organizationId, Long userId,
			String nickName, String phone) {
		OrganizationMember organizationMember = new OrganizationMember();
		organizationMember.setOrganizationId(organizationId);
		organizationMember.setTargetType(OrganizationMemberTargetType.USER.getCode());
		organizationMember.setTargetId(userId);
		organizationMember.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
		organizationMember.setContactName(nickName);
		organizationMember.setContactType(IdentifierType.MOBILE.getCode());
		organizationMember.setContactToken(phone);
		organizationMember.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
		organizationMember.setGender(UserGender.UNDISCLOSURED.getCode());
		organizationMember.setNamespaceId(namespaceId);
		organizationMember.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		organizationMember.setCreatorUid(UserContext.current().getUser().getId());
		organizationProvider.createOrganizationMember(organizationMember);
		userSearcher.feedDoc(organizationMember);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create organization member success: namespaceId="+namespaceId+", organizationId="+organizationId+", userId="+userId+", phone="+phone);
		}
	}


	private void createAclRoleAssignment(Long organizationId, Long userId) {
		RoleAssignment roleAssignment = new RoleAssignment();
		roleAssignment.setOwnerType(EntityType.ORGANIZATIONS.getCode());
		roleAssignment.setOwnerId(organizationId);
		roleAssignment.setTargetType(EntityType.USER.getCode());
		roleAssignment.setTargetId(userId);
		roleAssignment.setCreatorUid(UserContext.current().getUser().getId());
		roleAssignment.setRoleId(1001L);
		aclProvider.createRoleAssignment(roleAssignment);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("create acl role assignment success: organizationId="+organizationId+", userId="+userId);
		}
	}

	@Override
	public ListCommunityAuthPersonnelsResponse listCommunityAuthPersonnels(ListCommunityAuthPersonnelsCommand cmd) {

	    checkUserPrivilege(cmd.getCurrentOrgId(), PrivilegeConstants.AUTHENTIFICATION_LIST_VIEW,cmd.getCommunityId(), cmd.getAppId());
		// TODO Auto-generated method
        ListCommunityAuthPersonnelsResponse response = new ListCommunityAuthPersonnelsResponse();

//        Integer namespaceId = UserContext.getCurrentNamespaceId();
//        List<NamespaceResource> resourceList = namespaceResourceProvider.listResourceByNamespace(namespaceId, NamespaceResourceType.COMMUNITY);
//        if (resourceList == null) {
//            return response;
//        }
        //不通过域空间查询，通过项目查询 add by yanlong.liang 20180723
//        List<Long> communityIds = resourceList.stream().map(NamespaceResource::getResourceId).collect(Collectors.toList());
        List<Long> communityIds = new ArrayList<>();
        communityIds.add(cmd.getCommunityId());
        List<OrganizationCommunityRequest> orgs = this.organizationProvider.listOrganizationCommunityRequests(communityIds);
        if (null == orgs || orgs.size() == 0) {
			LOGGER.debug("orgs is null");
			return response;
        }

		List<Long> orgIds = new ArrayList<>();
		for(OrganizationCommunityRequest org : orgs) {
			orgIds.add(org.getMemberId());
		}
        List<Long> noAuthOrganizationIds = this.organizationProvider.listOrganizationIdFromUserAuthenticationOrganization(orgIds,
                UserContext.getCurrentNamespaceId(), com.everhomes.rest.common.TrueOrFalseFlag.FALSE.getCode());
        if (OrganizationMemberStatus.WAITING_FOR_APPROVAL.getCode() == cmd.getStatus() && !AuditAuth.ALL.getCode().equals(cmd.getAuditAuth())
                && !CollectionUtils.isEmpty(noAuthOrganizationIds)) {
            if (AuditAuth.BOTH.getCode().equals(cmd.getAuditAuth())) {
                for (Long orgId : noAuthOrganizationIds) {
                    if (orgIds.contains(orgId)) {
                        orgIds.remove(orgId);
                    }
                }
            }else if (AuditAuth.ONLY_COMPANY.getCode().equals(cmd.getAuditAuth())) {
                orgIds = noAuthOrganizationIds;
            }
        }
		LOGGER.debug("orgIds is：" + orgIds);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

        // 人员主动退出公司的记录也需要在项目管理的用户认证的已同意标签下显示 add by xq.tian 2017/07/12
        List<OrganizationMember> organizationMembers = null;
        if (OrganizationMemberStatus.fromCode(cmd.getStatus()) == OrganizationMemberStatus.ACTIVE) {
            List<OrganizationMemberLog> memberLogList = organizationProvider.listOrganizationMemberLogs(orgIds, cmd.getUserInfoKeyword(),cmd.getIdentifierToken(), cmd.getOrgNameKeyword(), locator, pageSize);
            if (memberLogList != null) {
                organizationMembers = memberLogList.stream()
                        .filter(r -> Objects.equals(r.getOperationType(), OperationType.JOIN.getCode()))
                        .map(r -> {
                            List<OrganizationMember> list = organizationProvider.findOrganizationMemberByOrgIdAndUIdWithoutAllStatus(r.getOrganizationId(), r.getUserId());
                            if (list != null && list.size() > 0) {
                                list = list.stream()
                                        .filter(member -> OrganizationGroupType.fromCode(member.getGroupType()) == OrganizationGroupType.ENTERPRISE)
                                        .filter(member -> OrganizationMemberTargetType.fromCode(member.getTargetType()) == OrganizationMemberTargetType.USER)
                                        // .limit(1)
                                        .map(member -> {
                                            member.setOperatorUid(r.getOperatorUid());
                                            member.setApproveTime(r.getOperateTime() != null ? r.getOperateTime().getTime() : null);
                                            member.setContactName(r.getContactName());
                                            member.setContactToken(r.getContactToken());
                                            member.setContactDescription(r.getContactDescription());
                                          //由于一个人可能重复认证（认证了某公司通过后，退出了再重新认证，这时，会有两条认证信息，最极限的情况是除了主键与创建时间，其他信息全相同）
                                            //所以这种情况没法准备对应信息，但为了ID唯一，不能取没法把握的信息的ID（搞不好他就弄同一个ID了） update by huanglm 20180706
                                            member.setId(r.getId());
                                            return member;
                                        }).collect(Collectors.toList());
                                if (list.size() > 0) {
                                    return list.get(0);
                                }
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                LOGGER.debug("orgmemberlogId is：{}" , orgIds);
            }
        } else {
            organizationMembers = this.organizationProvider.listOrganizationPersonnels(
                    cmd.getUserInfoKeyword(), cmd.getIdentifierToken(), cmd.getOrgNameKeyword(), orgIds, cmd.getStatus(), null, locator, pageSize);
			LOGGER.debug("wait approve organizationMembers cmd:" + cmd);
			LOGGER.debug("wait approve organizationMembers size " + organizationMembers.size());
        }

		if(organizationMembers == null || organizationMembers.size() == 0) {
			return response;
		}

		response.setNextPageAnchor(locator.getAnchor());

        List<ComOrganizationMemberDTO> dtoList = organizationMembers.stream()
            .map((c) -> {
                ComOrganizationMemberDTO dto = ConvertHelper.convert(c, ComOrganizationMemberDTO.class);
				if (c.getOperatorUid() != null && c.getOperatorUid() > 0) {
					User operator = userProvider.findUserById(c.getOperatorUid());
					UserIdentifier operatorIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(c.getOperatorUid(), IdentifierType.MOBILE.getCode());
					dto.setOperatorName(operator != null ? operator.getNickName() : "");
					dto.setOperatorPhone(operatorIdentifier != null ? operatorIdentifier.getIdentifierToken() : "");
                    if (OperateType.IMPORT.getCode().equals(c.getSourceType())) {
                        dto.setOperateType(OperateType.IMPORT.getCode());
                    }else {
                        dto.setOperateType(OperateType.MANUAL.getCode());
                    }
				} else if (OrganizationMemberStatus.fromCode(cmd.getStatus()) == OrganizationMemberStatus.ACTIVE) {
					dto.setOperatorName("--");
                    dto.setOperateType(OperateType.EMAIL.getCode());
				}
                if (dto.getOrganizationName() == null || dto.getOrganizationName().isEmpty()) {
                    Organization organization = organizationProvider.findOrganizationById(dto.getOrganizationId());
                    if (organization != null) {
                        dto.setOrganizationName(organization.getName());
                    }
                }
                dto.setAuthFlag(com.everhomes.rest.common.TrueOrFalseFlag.TRUE.getCode());
                if (!CollectionUtils.isEmpty(noAuthOrganizationIds)) {
					List<OrganizationMember> manage = this.organizationProvider.listOrganizationMembersByOrgIdAndMemberGroup(dto.getOrganizationId(), OrganizationMemberGroupType.MANAGER.getCode());
					List<Long> manageUserIds = new ArrayList<>();
					if (!CollectionUtils.isEmpty(manage)) {
						for (OrganizationMember member : manage) {
							manageUserIds.add(member.getTargetId());
						}
					}
				    if (noAuthOrganizationIds.contains(dto.getOrganizationId()) && !manageUserIds.contains(UserContext.currentUserId())) {
				        dto.setAuthFlag(com.everhomes.rest.common.TrueOrFalseFlag.FALSE.getCode());
                    }
                }
                if (dto.getNickName() == null || dto.getNickName().isEmpty()) {
                    User user = userProvider.findUserById(dto.getTargetId());
                    if (user != null && user.getId() != 0) {
                        dto.setNickName(user.getNickName());
                    }
                }
                return dto;
            }).collect(Collectors.toList());

        response.setMembers(dtoList);
        //已认证或已拒绝的排序
		Collections.sort(response.getMembers(), new Comparator<ComOrganizationMemberDTO>() {
			@Override
			public int compare(ComOrganizationMemberDTO o1, ComOrganizationMemberDTO o2) {
				if (o1.getApproveTime() == null || o2.getApproveTime() == null) {
					return -1;
				}
				return o2.getApproveTime().compareTo(o1.getApproveTime());
			}
		});
		//未认证的排序
		if(OrganizationMemberStatus.fromCode(cmd.getStatus()) == OrganizationMemberStatus.WAITING_FOR_APPROVAL){
			Collections.sort(response.getMembers(), new Comparator<ComOrganizationMemberDTO>() {
				@Override
				public int compare(ComOrganizationMemberDTO o1, ComOrganizationMemberDTO o2) {
					if (o1.getCreateTime() == null || o2.getCreateTime() == null) {
						return -1;
					}
					return o2.getCreateTime().compareTo(o1.getCreateTime());
				}
			});
		}
		return response;
	}

	private boolean checkUserPrivilege(Long orgId, Long privilegeId, Long communityId, Long appId){
		return userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), orgId, privilegeId, appId,null,communityId);
	}
	@Override
	public void updateCommunityUser(UpdateCommunityUserCommand cmd) {
		if(null == cmd.getUserId() )
			throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
					"Invalid userid parameter");
		User user = userProvider.findUserById(cmd.getUserId());

		if(null == user)
			throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_USER_NOT_EXIST,
					"Invalid userid parameter: no this user");
		user.setIdentityNumberTag(cmd.getIdentityNumber());

		//更新用户信息和公司信息   edit by yanjun 20171016
		dbProvider.execute((status) -> {
			userProvider.updateUser(user);
			if(cmd.getOrganizations() != null){
				for (CommunityUserOrgDetailDTO dto: cmd.getOrganizations()){
					UserOrganizations userOrganization = organizationProvider.findUserOrganizationById(dto.getDetailId());

					if(userOrganization != null){
						userOrganization.setExecutiveTag(dto.getExecutiveFlag());
						userOrganization.setPositionTag(dto.getPositionTag());
						organizationProvider.updateUserOrganization(userOrganization);
					}
				}
			}
			return null;
		});

	}

	public void createResourceCategory(CreateResourceCategoryCommand cmd) {

		Long ownerId = cmd.getOwnerId();
		String ownerType = cmd.getOwnerType();
		String name = cmd.getName();
		checkResourceCategoryName(name);
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());

		if(null == cmd.getType()){
			cmd.setType(ResourceCategoryType.CATEGORY.getCode());
		}

		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		Long parentId = cmd.getParentId();
		ResourceCategory category = null;
		ResourceCategory parentCategory = null;
		if(null == parentId || parentId == 0){

			category = communityProvider.findResourceCategoryByParentIdAndName(ownerId, ownerType, 0L, name, cmd.getType());
			checkResourceCategoryExsit(category);
			category = new ResourceCategory();
			category.setParentId(0L);
		}else{
			parentCategory = communityProvider.findResourceCategoryById(parentId);
			checkResourceCategoryIsNull(parentCategory);
			category = communityProvider.findResourceCategoryByParentIdAndName(ownerId, ownerType, parentId, name,cmd.getType());
			checkResourceCategoryExsit(category);
			category = new ResourceCategory();
			category.setPath(parentCategory.getPath());
			category.setParentId(parentId);
		}

		category.setCreateTime(new Timestamp(System.currentTimeMillis()));
		category.setCreatorUid(UserContext.current().getUser().getId());
		category.setName(name);
		category.setNamespaceId(namespaceId);

		category.setStatus(ResourceCategoryStatus.ACTIVE.getCode());
		category.setOwnerType(ownerType);
		category.setOwnerId(ownerId);
		category.setType(ResourceCategoryType.CATEGORY.getCode());
		communityProvider.createResourceCategory(category);

	}


	@Override
	public void updateResourceCategory(UpdateResourceCategoryCommand cmd) {
		checkResourceCategoryId(cmd.getId());
		checkResourceCategoryName(cmd.getName());

		ResourceCategory category = communityProvider.findResourceCategoryById(cmd.getId());
		checkResourceCategoryIsNull(category);

		category.setName(cmd.getName());
//		String path = category.getPath();
//		path = path.substring(0, path.lastIndexOf("/"));
//
//		category.setPath(path + "/" + cmd.getName());
		category.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		communityProvider.updateResourceCategory(category);
	}


	@Override
	public void deleteResourceCategory(DeleteResourceCategoryCommand cmd) {
		checkResourceCategoryId(cmd.getId());

		ResourceCategory category = communityProvider.findResourceCategoryById(cmd.getId());
		checkResourceCategoryIsNull(category);
//删除分类时，删除园区与该分类的关联关系
		List<ResourceCategoryAssignment> assignments = communityProvider.listResourceCategoryAssignment(category.getId(), category.getNamespaceId());
		if (assignments!=null && assignments.size()>0) {
			for (ResourceCategoryAssignment assignment : assignments) {
				communityProvider.deleteResourceCategoryAssignmentById(assignment.getId());
			}
		}

		category.setStatus(ResourceCategoryStatus.INACTIVE.getCode());
		communityProvider.updateResourceCategory(category);
	}


	@Override
	public void createResourceCategoryAssignment(CreateResourceCategoryAssignmentCommand cmd) {
		if(null == cmd.getResourceId()) {
        	LOGGER.error("ResourceId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"ResourceId cannot be null.");
        }

    	if(StringUtils.isBlank(cmd.getResourceType())) {
        	LOGGER.error("ResourceType cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"ResourceType cannot be null.");
        }

		Integer namespaceId = cmd.getNamespaceId();
		ResourceCategoryAssignment rca = communityProvider.findResourceCategoryAssignment(cmd.getResourceId(), cmd.getResourceType(),
				namespaceId);
		if(null != rca) {
			if(null != cmd.getResourceCategoryId()) {
				ResourceCategory category = communityProvider.findResourceCategoryById(cmd.getResourceCategoryId());
				checkResourceCategoryIsNull(category);
				rca.setResourceCategryId(category.getId());
				communityProvider.updateResourceCategoryAssignment(rca);
			}else{
				communityProvider.deleteResourceCategoryAssignmentById(rca.getId());
			}
		}else{
			//分类无可以改成无
//			if(null == cmd.getResourceCategoryId()) {
//	        	LOGGER.error("CategoryId cannot be null.");
//	    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//	    				"CategoryId cannot be null.");
//	        }
			ResourceCategory category = communityProvider.findResourceCategoryById(cmd.getResourceCategoryId());
			checkResourceCategoryIsNull(category);
			rca = new ResourceCategoryAssignment();
			rca.setCreateTime(new Timestamp(System.currentTimeMillis()));
			rca.setCreatorUid(UserContext.current().getUser().getId());
			rca.setNamespaceId(namespaceId);
			rca.setResourceCategryId(category.getId());
			rca.setResourceId(cmd.getResourceId());
			rca.setResourceType(cmd.getResourceType());
			communityProvider.createResourceCategoryAssignment(rca);
		}

	}


	@Override
	public void deleteResourceCategoryAssignment(CreateResourceCategoryAssignmentCommand cmd) {
		if(null == cmd.getResourceId()) {
        	LOGGER.error("ResourceId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"ResourceId cannot be null.");
        }

    	if(StringUtils.isBlank(cmd.getResourceType())) {
        	LOGGER.error("ResourceType cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"ResourceType cannot be null.");
        }

		Integer namespaceId = UserContext.current().getUser().getNamespaceId();
		ResourceCategoryAssignment rca = communityProvider.findResourceCategoryAssignment(cmd.getResourceId(), cmd.getResourceType(),
				namespaceId);
		if(null != rca)
			communityProvider.deleteResourceCategoryAssignmentById(rca.getId());

	}

	private void checkResourceCategoryId(Long id) {
		if(null == id) {
        	LOGGER.error("Id cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Id cannot be null.");
        }
	}

	private void checkResourceCategoryName(String name) {
		if(StringUtils.isBlank(name)) {
        	LOGGER.error("Name cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Name cannot be null.");
        }
	}

	private void checkResourceCategoryIsNull(ResourceCategory category) {
		if(null == category) {
			LOGGER.error("ResourceCategory not found.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"ResourceCategory not found.");
		}
	}

	private void checkResourceCategoryExsit(ResourceCategory category) {
		if(null != category) {
			LOGGER.error("ResourceCategory have been in existing");
			throw RuntimeErrorException.errorWith(ResourceCategoryErrorCode.SCOPE, ResourceCategoryErrorCode.ERROR_RESOURCE_CATEGORY_EXIST,
					"ResourceCategory have been in existing");
		}
	}

	private void checkOwnerIdAndOwnerType(String ownerType, Long ownerId){
		if(null == ownerId) {
        	LOGGER.error("OwnerId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OwnerId cannot be null.");
        }

    	if(StringUtils.isBlank(ownerType)) {
        	LOGGER.error("OwnerType cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OwnerType cannot be null.");
        }
	}


	@Override
	public ListCommunitiesByKeywordResponse listCommunitiesByCategory(ListCommunitiesByCategoryCommand cmd) {
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		int namespaceId =UserContext.getCurrentNamespaceId(cmd.getNamespaceId());

		List<Community> list = communityProvider.listCommunitiesByCategory(cmd.getNamespaceId(), cmd.getCityId(), cmd.getAreaId(),
				cmd.getCategoryId(), cmd.getKeywords(), cmd.getPageAnchor(), pageSize);

		ListCommunitiesByKeywordResponse response = new ListCommunitiesByKeywordResponse();

		if(list.size() > 0){
			List<CommunityDTO> resultList = list.stream().map((c) -> {
				CommunityDTO dto = ConvertHelper.convert(c, CommunityDTO.class);
				ResourceCategoryAssignment ra = communityProvider.findResourceCategoryAssignment(c.getId(), EntityType.COMMUNITY.getCode(), namespaceId);
				if(null != ra) {
					ResourceCategory category = communityProvider.findResourceCategoryById(ra.getResourceCategryId());
					dto.setCategoryId(category.getId());
					dto.setCategoryName(category.getName());
				}

				return dto;
			}).collect(Collectors.toList());
    		response.setRequests(resultList);
    		if(list.size() != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getId());
        	}
    	}

		return response;
	}


	@Override
	public List<ResourceCategoryDTO> listResourceCategories(ListResourceCategoryCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());

		List<ResourceCategoryDTO> temp = communityProvider.listResourceCategory(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getParentId(), null, ResourceCategoryType.CATEGORY.getCode())
			.stream().map(r -> {
				ResourceCategoryDTO dto = ConvertHelper.convert(r, ResourceCategoryDTO.class);

				return dto;
			}).collect(Collectors.toList());

		return temp;
	}

	@Override
	public List<ResourceCategoryDTO> listTreeResourceCategoryAssignments(ListResourceCategoryCommand cmd) {
		if(null == cmd.getParentId())
			cmd.setParentId(0L);
		List<ResourceCategoryDTO> list = listTreeResourceCategories(cmd);
		Integer namespaceId = UserContext.current().getUser().getNamespaceId();

		setResourceDTOs(list, namespaceId);

		return list;
	}

	private void setResourceDTOs(List<ResourceCategoryDTO> list, Integer namespaceId){
		if(null != list) {
			for(ResourceCategoryDTO r: list) {
				List<ResourceCategoryAssignment> resourceCategoryAssignments = communityProvider.listResourceCategoryAssignment(r.getId(), namespaceId);
				List<ResourceCategoryAssignmentDTO> resourceDTOs = resourceCategoryAssignments.stream().map(ra -> {
					ResourceCategoryAssignmentDTO dto = ConvertHelper.convert(ra, ResourceCategoryAssignmentDTO.class);
					Community community = communityProvider.findCommunityById(ra.getResourceId());
					dto.setResourceName(community.getName());
					return dto;
				}).collect(Collectors.toList());
				setResourceDTOs(r.getCategoryDTOs(), namespaceId);
				r.setResourceDTOs(resourceDTOs);
			}
		}

	}

	private ResourceCategoryDTO getChildCategories(List<ResourceCategoryDTO> list, ResourceCategoryDTO dto){

		List<ResourceCategoryDTO> childrens = new ArrayList<ResourceCategoryDTO>();

		for (ResourceCategoryDTO resourceCategoryDTO : list) {
			if(dto.getId().equals(resourceCategoryDTO.getParentId())){
				childrens.add(getChildCategories(list, resourceCategoryDTO));
			}
		}
		dto.setCategoryDTOs(childrens);

		return dto;
	}


	@Override
	public List<ResourceCategoryDTO> listTreeResourceCategories(ListResourceCategoryCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());

		List<ResourceCategoryDTO> result = new ArrayList<ResourceCategoryDTO>();
		String path = null;
		Long parentId = null == cmd.getParentId() ? 0L : cmd.getParentId();
		if(null != cmd.getParentId()) {
			ResourceCategory resourceCategory = communityProvider.findResourceCategoryById(cmd.getParentId());
			checkResourceCategoryIsNull(resourceCategory);
			path = resourceCategory.getPath();
		}

		List<ResourceCategoryDTO> temp = communityProvider.listResourceCategory(cmd.getOwnerId(), cmd.getOwnerType(), null, path, ResourceCategoryType.CATEGORY.getCode())
			.stream().map(r -> {
				ResourceCategoryDTO dto = ConvertHelper.convert(r, ResourceCategoryDTO.class);

				return dto;
			}).collect(Collectors.toList());

		for(ResourceCategoryDTO s: temp) {
			getChildCategories(temp, s);
			if(s.getParentId() == parentId) {
				result.add(s);
			}
		}

		return result;
	}

	@Override
	public List<ProjectDTO> listChildProjects(ListChildProjectCommand cmd){
		List<ProjectDTO> dtos = new ArrayList<>();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		List<ResourceCategory> categorys = communityProvider.listResourceCategory(cmd.getProjectId(), cmd.getProjectType(),0L, null, ResourceCategoryType.OBJECT.getCode());
		for (ResourceCategory category: categorys) {
			ProjectDTO dto = new ProjectDTO();
			dto.setProjectName(category.getName());
			dto.setProjectId(category.getId());
			dto.setProjectType(EntityType.CHILD_PROJECT.getCode());
			dto.setParentId(cmd.getProjectId());
			List<ResourceCategoryAssignment> buildingCategorys = communityProvider.listResourceCategoryAssignment(category.getId(), namespaceId);
			List<ProjectDTO> buildingProjects = new ArrayList<>();
			for (ResourceCategoryAssignment buildingCategory: buildingCategorys) {
				if(EntityType.fromCode(buildingCategory.getResourceType()) == EntityType.BUILDING){
					Building building = communityProvider.findBuildingById(buildingCategory.getResourceId());
					if(null != building){
						ProjectDTO buildingProject = new ProjectDTO();
						buildingProject.setParentId(category.getId());
						buildingProject.setProjectId(building.getId());
						buildingProject.setProjectName(building.getName());
						buildingProject.setProjectType(EntityType.BUILDING.getCode());
						Community community = communityProvider.findCommunityById(building.getCommunityId());
						if (community != null) {
							buildingProject.setCommunityType(community.getCommunityType());
						}
						buildingProjects.add(buildingProject);
					}
				}
			}
			dto.setProjects(buildingProjects);
			dtos.add(dto);

		}

		return dtos;
	}

	@Override
	public void createChildProject(CreateChildProjectCommand cmd){
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		User user = UserContext.current().getUser();
		ResourceCategory project = new ResourceCategory();
		project.setCreatorUid(user.getId());
		project.setNamespaceId(namespaceId);
		project.setStatus(ResourceCategoryStatus.ACTIVE.getCode());
		project.setName(cmd.getName());
		project.setOwnerId(cmd.getProjectId());
		project.setOwnerType(cmd.getProjectType());
		project.setParentId(0L);
		project.setType(ResourceCategoryType.OBJECT.getCode());

		this.dbProvider.execute((TransactionStatus status) ->  {
			communityProvider.createResourceCategory(project);
			if(null != cmd.getBuildingIds() && cmd.getBuildingIds().size() > 0){
				this.addResourceCategoryAssignment(cmd.getBuildingIds(), project.getId());
			}
			return null;
		});
	}

	@Override
	public void updateChildProject(UpdateChildProjectCommand cmd){
		ResourceCategory project = communityProvider.findResourceCategoryById(cmd.getId());
		if(null != project){
			this.dbProvider.execute((TransactionStatus status) ->  {
				project.setName(cmd.getName());
				project.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				communityProvider.updateResourceCategory(project);

				if(null != cmd.getBuildingIds() && cmd.getBuildingIds().size() > 0){
					this.addResourceCategoryAssignment(cmd.getBuildingIds(), project.getId());
				}
				return null;
			});
		}
	}

	private void addResourceCategoryAssignment(List<Long> buildingIds, Long categoryId){
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		List<ResourceCategoryAssignment> bulidingCategorys = communityProvider.listResourceCategoryAssignment(categoryId,namespaceId);

		for (ResourceCategoryAssignment bulidingCategory: bulidingCategorys) {
			communityProvider.deleteResourceCategoryAssignmentById(bulidingCategory.getId());
		}

		List<ServiceModuleAssignment> smas = serviceModuleProvider.listServiceModuleAssignmentsByTargetIdAndOwnerId(EntityType.RESOURCE_CATEGORY.getCode(), categoryId, null, null, null);

		for (Long buildingId: buildingIds) {
			ResourceCategoryAssignment  projectAssignment = communityProvider.findResourceCategoryAssignment(buildingId, EntityType.BUILDING.getCode(), namespaceId);
			if(null != projectAssignment){
				communityProvider.deleteResourceCategoryAssignmentById(projectAssignment.getId());
			}
			projectAssignment = new ResourceCategoryAssignment();
			projectAssignment.setNamespaceId(namespaceId);
			projectAssignment.setCreatorUid(UserContext.current().getUser().getId());
			projectAssignment.setResourceCategryId(categoryId);
			projectAssignment.setResourceId(buildingId);
			projectAssignment.setResourceType(EntityType.BUILDING.getCode());
			communityProvider.createResourceCategoryAssignment(projectAssignment);

//			for (ServiceModuleAssignment sma: smas) {
//				rolePrivilegeService.assignmentPrivileges(projectAssignment.getResourceType(),projectAssignment.getResourceId(), sma.getTargetType(),sma.getTargetId(),"M" + sma.getModuleId() + "." + sma.getOwnerType() + sma.getOwnerId(), sma.getModuleId(),ServiceModulePrivilegeType.SUPER);
//			}

		}

	}

	@Override
	public void deleteChildProject(DeleteChildProjectCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		this.dbProvider.execute((TransactionStatus status) -> {
			communityProvider.deleteResourceCategoryById(cmd.getId());
			List<ResourceCategoryAssignment> bulidingCategorys = communityProvider.listResourceCategoryAssignment(cmd.getId(), namespaceId);
			for (ResourceCategoryAssignment bulidingCategory : bulidingCategorys) {
				communityProvider.deleteResourceCategoryAssignmentById(bulidingCategory.getId());
			}

			List<ServiceModuleAssignment> moduleAssignments = serviceModuleProvider.listResourceAssignmentGroupByTargets(EntityType.RESOURCE_CATEGORY.getCode(), cmd.getId(), null);
			for (ServiceModuleAssignment moduleAssignment: moduleAssignments) {
				AclRoleDescriptor aclRoleDescriptor = new AclRoleDescriptor(moduleAssignment.getTargetType(), moduleAssignment.getTargetId());
				List<Acl> acls = aclProvider.getResourceAclByRole(EntityType.RESOURCE_CATEGORY.getCode(), cmd.getId(), aclRoleDescriptor);
				for (Acl acl: acls) {
					aclProvider.deleteAcl(acl.getId());
				}
			}
			return null;
		});
	}

	@Override
	public List<ProjectDTO> getTreeProjectCategories(GetTreeProjectCategoriesCommand cmd){
		Integer namespaceId = UserContext.getCurrentNamespaceId();
	    if(cmd.getOwnerId() != null) {
	        Organization org = organizationProvider.findOrganizationById(cmd.getOwnerId());
	        if(org != null && org.getNamespaceId() != null) {
	            namespaceId = org.getNamespaceId();
	        }
	    }
		List<Community> communities = communityProvider.listCommunitiesByNamespaceId(namespaceId);
		List<ProjectDTO> projects = new ArrayList<>();
		for (Community community: communities) {
			ProjectDTO project = new ProjectDTO();
			project.setProjectId(community.getId());
			project.setProjectType(EntityType.COMMUNITY.getCode());
			project.setProjectName(community.getName());
			projects.add(project);
		}
		return rolePrivilegeService.getTreeProjectCategories(namespaceId, projects);
	}

	@Override
	public void updateBuildingOrder(UpdateBuildingOrderCommand cmd) {
		if (null == cmd.getId()) {
			throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE,
					PropertyErrorCode.ERROR_NULL_PARAMETER,
					"Invalid id parameter in the command");
		}
		if (null == cmd.getExchangeId()) {
			throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE,
					PropertyErrorCode.ERROR_NULL_PARAMETER,
					"Invalid exchangeId parameter in the command");
		}
		Building building = communityProvider.findBuildingById(cmd.getId());
		Building exchangeBuilding = communityProvider.findBuildingById(cmd.getExchangeId());

		if (null == building) {
			LOGGER.error("Building not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE,
					PropertyErrorCode.ERROR_BUILDING_NOT_EXIST,
					"Building not found");
		}
		if (null == exchangeBuilding) {
			LOGGER.error("Building not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE,
					PropertyErrorCode.ERROR_BUILDING_NOT_EXIST,
					"Building not found");
		}

		Long order = building.getDefaultOrder();
		Long exchangeOrder = exchangeBuilding.getDefaultOrder();

		dbProvider.execute((TransactionStatus status) -> {
			building.setDefaultOrder(exchangeOrder);
			exchangeBuilding.setDefaultOrder(order);
			communityProvider.updateBuilding(building);
			communityProvider.updateBuilding(exchangeBuilding);
			return null;
		});
	}

    @Override
    public CommunityAuthPopupConfigDTO getCommunityAuthPopupConfig(GetCommunityAuthPopupConfigCommand cmd) {
        Integer namespaceId = cmd.getNamespaceId();
        if (namespaceId == null) {
            namespaceId = UserContext.getCurrentNamespaceId();
        }
        ServiceConfiguration conf = serviceConfigurationsProvider.getServiceConfiguration(
                namespaceId, EntityType.NAMESPACE.getCode(), namespaceId.longValue(), ServiceConfiguration.COMMUNITY_AUTH_POPUP);

        if (conf == null) {
            conf = new ServiceConfiguration();
            conf.setNamespaceId(namespaceId);
            conf.setValue(String.valueOf(TrueOrFalseFlag.TRUE.getCode()));
            conf.setOwnerType(EntityType.NAMESPACE.getCode());
            conf.setOwnerId(namespaceId.longValue());
            conf.setName(ServiceConfiguration.COMMUNITY_AUTH_POPUP);
            serviceConfigurationsProvider.createServiceConfiguration(conf);
        }
        return new CommunityAuthPopupConfigDTO(Byte.valueOf(conf.getValue()));
    }

    @Override
    public CommunityAuthPopupConfigDTO updateCommunityAuthPopupConfig(UpdateCommunityAuthPopupConfigCommand cmd) {
        Integer namespaceId = cmd.getNamespaceId();
        if (namespaceId == null) {
            namespaceId = UserContext.getCurrentNamespaceId();
        }
        ServiceConfiguration conf = serviceConfigurationsProvider.getServiceConfiguration(
                namespaceId, EntityType.NAMESPACE.getCode(), namespaceId.longValue(), ServiceConfiguration.COMMUNITY_AUTH_POPUP);

        TrueOrFalseFlag status = TrueOrFalseFlag.fromCode(cmd.getStatus());
        String value = String.valueOf(status != null ? status.getCode() : TrueOrFalseFlag.TRUE.getCode());
        if (conf == null) {
            conf = new ServiceConfiguration();
            conf.setNamespaceId(namespaceId);
            conf.setValue(value);
            conf.setOwnerType(EntityType.NAMESPACE.getCode());
            conf.setOwnerId(namespaceId.longValue());
            conf.setName(ServiceConfiguration.COMMUNITY_AUTH_POPUP);
            serviceConfigurationsProvider.createServiceConfiguration(conf);
        } else {
            conf.setValue(value);
            serviceConfigurationsProvider.updateServiceConfiguration(conf);
        }
        return new CommunityAuthPopupConfigDTO(Byte.valueOf(conf.getValue()));
    }

	@Override
	public ListCommunitiesByOrgIdResponse listCommunitiesByOrgId(ListCommunitiesByOrgIdCommand cmd) {
		if(cmd.getPageAnchor()==null)
			cmd.setPageAnchor(0L);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		ListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<Community> list = this.communityProvider.listCommunitiesByOrgId(locator, pageSize+1,cmd.getOrgId(), cmd.getKeyword());

		ListCommunitiesByOrgIdResponse response = new ListCommunitiesByOrgIdResponse();
		if(list != null && list.size() > pageSize){
			list.remove(list.size()-1);
			response.setNextPageAnchor(list.get(list.size()-1).getId());
		}
		if(list != null){
			List<CommunityDTO> resultList = list.stream().map((c) -> {
				return ConvertHelper.convert(c, CommunityDTO.class);
			}).collect(Collectors.toList());

			response.setList(resultList);
		}

		return response;

	}

	//导入项目信息
	@Override
	public ImportFileTaskDTO importCommunityDataAdmin(ImportCommunityCommand cmd, MultipartFile file) {
		Long userId = UserContext.current().getUser().getId();
		ImportFileTask task = new ImportFileTask();
		try {
			//解析excel
			List resultList = PropMrgOwnerHandler.processorExcel(file.getInputStream());

			if(null == resultList || resultList.isEmpty()){
				LOGGER.error("File content is empty。userId="+userId);
				throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_FILE_IS_EMPTY,
						"File content is empty");
			}
			task.setOwnerType(EntityType.COMMUNITY.getCode());
			task.setOwnerId((cmd.getNamespaceId()).longValue());
			task.setType(ImportFileTaskType.COMMUNITY.getCode());
			task.setCreatorUid(userId);
			task = importFileService.executeTask(() -> {
					ImportFileResponse response = new ImportFileResponse();
					List<ImportCommunityDataDTO> datas = handleImportCommunityData(resultList);
					if(datas.size() > 0){
						//设置导出报错的结果excel的标题
						response.setTitle(datas.get(0));
						datas.remove(0);
					}
					List<ImportFileResultLog<ImportCommunityDataDTO>> results = importCommunityDataAdmin(datas, userId, cmd);
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
    public CheckUserAuditingAdminResponse checkUserAuditing(CheckUserAuditingAdminCommand cmd) {
        CheckUserAuditingAdminResponse response = new CheckUserAuditingAdminResponse();
        boolean isAuditing = checkUserPrivilege(cmd.getCurrentOrgId(), PrivilegeConstants.AUTHENTIFICATION_AUDITING, cmd.getCommunityId(), cmd.getAppId());
        if (isAuditing) {
            response.setIsAuditing(CheckAuditingType.YES.getCode());
        }else {
            response.setIsAuditing(CheckAuditingType.NO.getCode());
        }
        return response;
    }

    private List<ImportFileResultLog<ImportCommunityDataDTO>> importCommunityDataAdmin(List<ImportCommunityDataDTO> datas,
			Long userId, ImportCommunityCommand cmd) {
		OrganizationDTO org = this.organizationService.getUserCurrentOrganization();
		List<OrganizationMember> orgMem = this.organizationProvider.listOrganizationMembersByOrgId(org.getId());

		Map<String, OrganizationMember> ct = new HashMap<String, OrganizationMember>();
		if(orgMem != null) {
			orgMem.stream().map(r -> {
				ct.put(r.getContactToken(), r);
				return null;
			});
		}
		List<ImportFileResultLog<ImportCommunityDataDTO>> list = new ArrayList<>();
		for (ImportCommunityDataDTO data : datas) {
			ImportFileResultLog<ImportCommunityDataDTO> log = checkCommunityData(data, cmd.getNamespaceId(), userId);
			if (log != null) {
				list.add(log);
				continue;
			}

			Community community = communityProvider.findCommunityByNamespaceIdAndName(cmd.getNamespaceId(), data.getName());
			if (community == null) {
				community = new Community();
				community.setName(data.getName());
				community.setCommunityNumber(data.getCommunityNumber());
				community.setAliasName(data.getAliasName());
				community.setCityName(data.getCityName());
				community.setAreaName(data.getAreaName());
				community.setAddress(data.getAddress());
				if (StringUtils.isNotBlank(data.getAreaSize())) {
					community.setAreaSize(Double.valueOf(data.getAreaSize()));
				}
				if (StringUtils.isNotBlank(data.getRentArea())) {
					community.setRentArea(Double.valueOf(data.getRentArea()));
				}
				if (StringUtils.isNotBlank(data.getCommunityType())) {
					if (data.getCommunityType().equals("商用")) {
						community.setCommunityType((byte)1);
					}
					if (data.getCommunityType().equals("住宅")) {
						community.setCommunityType((byte)0);
					}
				}
				//设置默认的forumId，要使用原有的项目里的forumId
				ListingLocator locator = new ListingLocator();
				List<Community> communities = communityProvider.listCommunities(cmd.getNamespaceId(), locator, 1, null);
				if(communities != null && communities.size() > 0){
					community.setDefaultForumId(communities.get(0).getDefaultForumId());
					community.setFeedbackForumId(communities.get(0).getFeedbackForumId());
				}else {
					community.setDefaultForumId(1L);
					community.setFeedbackForumId(2L);
				}
				community.setCityId(data.getCityId());
				community.setAreaId(data.getAreaId());
				//community.setCommunityType((byte)1);
				community.setStatus(CommunityAdminStatus.ACTIVE.getCode());
				community.setAptCount(0);
				community.setNamespaceId(cmd.getNamespaceId());

				//插入园区
				//communityProvider.createCommunity(userId, community);
				community = createCommunityData(userId, community);
				//添加企业可见园区,管理公司可以看到添加的园区
				OrganizationCommunity organizationCommunity = new OrganizationCommunity();
				organizationCommunity.setCommunityId(community.getId());
				organizationCommunity.setOrganizationId(cmd.getOrganizationId());
				organizationProvider.createOrganizationCommunity(organizationCommunity);

				//园区和域空间关联
				createNamespaceResource(cmd.getNamespaceId(), community.getId());
				communitySearcher.feedDoc(community);

				//创建经纬度数据
				CommunityGeoPoint geoPoint = new CommunityGeoPoint();
				geoPoint.setCommunityId(community.getId());
				geoPoint.setLongitude(Double.valueOf(data.getLongitude()));
				geoPoint.setLatitude(Double.valueOf(data.getLatitude()));
				String geohash = GeoHashUtils.encode(Double.valueOf(data.getLatitude()), Double.valueOf(data.getLongitude()));
				geoPoint.setGeohash(geohash);
				communityProvider.createCommunityGeoPoint(geoPoint);
			}else {
				community.setName(data.getName());
				if (StringUtils.isNotBlank(data.getCommunityNumber())) {
					community.setCommunityNumber(data.getCommunityNumber());
				}
				if (StringUtils.isNotBlank(data.getAliasName())) {
					community.setAliasName(data.getAliasName());
				}
				if (StringUtils.isNotBlank(data.getCityName())) {
					community.setCityName(data.getCityName());
				}
				if (StringUtils.isNotBlank(data.getAreaName())) {
					community.setAreaName(data.getAreaName());
				}
				if (StringUtils.isNotBlank(data.getAddress())) {
					community.setAddress(data.getAddress());
				}
				if (StringUtils.isNotBlank(data.getAreaSize())) {
					community.setAreaSize(Double.valueOf(data.getAreaSize()));
				}
				if (StringUtils.isNotBlank(data.getRentArea())) {
					community.setRentArea(Double.valueOf(data.getRentArea()));
				}
				if (StringUtils.isNotBlank(data.getCommunityType())) {
					if (data.getCommunityType().equals("商用")) {
						community.setCommunityType((byte)1);
					}
					if (data.getCommunityType().equals("住宅")) {
						community.setCommunityType((byte)0);
					}
				}
				community.setCityId(data.getCityId());
				community.setAreaId(data.getAreaId());

				communityProvider.updateCommunity(community);
				communitySearcher.feedDoc(community);


				CommunityGeoPoint geoPoint = communityProvider.findCommunityGeoPointByCommunityId(community.getId());
				if (geoPoint != null) {
					geoPoint.setLongitude(Double.valueOf(data.getLongitude()));
					geoPoint.setLatitude(Double.valueOf(data.getLatitude()));
					String geohash = GeoHashUtils.encode(Double.valueOf(data.getLatitude()), Double.valueOf(data.getLongitude()));
					geoPoint.setGeohash(geohash);
					communityProvider.updateCommunityGeoPoint(geoPoint);
				}else {
					CommunityGeoPoint geo = new CommunityGeoPoint();
					geo.setCommunityId(community.getId());
					geo.setLongitude(Double.valueOf(data.getLongitude()));
					geo.setLatitude(Double.valueOf(data.getLatitude()));
					String geohash = GeoHashUtils.encode(Double.valueOf(data.getLatitude()), Double.valueOf(data.getLongitude()));
					geo.setGeohash(geohash);
					communityProvider.createCommunityGeoPoint(geo);
				}

			}
		}
		return list;
	}

	private Community createCommunityData(Long userId, Community cmd) {
		//创建社区
		Community community = ConvertHelper.convert(cmd, Community.class);
		communityProvider.createCommunity(userId, community);
		return community;
	}

	private ImportFileResultLog<ImportCommunityDataDTO> checkCommunityData(ImportCommunityDataDTO data, Integer namespaceId, Long userId) {
		ImportFileResultLog<ImportCommunityDataDTO> log = new ImportFileResultLog<>(CommunityServiceErrorCode.SCOPE);

		if (StringUtils.isEmpty(data.getName())) {
			log.setCode(CommunityServiceErrorCode.ERROR_COMMUNITY_NAME_EMPTY);
			log.setData(data);
			log.setErrorLog("Community name cannot be empty");
			return log;
		}
		//检查项目编号,一个域空间下只存在一个项目编号
		/*if (StringUtils.isNotEmpty(data.getCommunityNumber())) {
			Community community = communityProvider.findCommunityByNumber(data.getCommunityNumber(), namespaceId);
			if(community != null) {
				log.setCode(CommunityServiceErrorCode.ERROR_COMMUNITY_NUMBER_EXIST);
				log.setData(data);
				log.setErrorLog("CommunityNumber exist ");
				return log;
			}
		}*/
		//正则校验数字
		if (StringUtils.isNotEmpty(data.getAreaSize())) {
			String reg = "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";
			if(!Pattern.compile(reg).matcher(data.getAreaSize()).find()){
				log.setCode(CommunityServiceErrorCode.ERROR_AREASIZE_FORMAT);
				log.setData(data);
				log.setErrorLog("AreaSize format is error");
				return log;
			}
		}
		if (StringUtils.isNotEmpty(data.getRentArea())) {
			String reg = "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";
			if(!Pattern.compile(reg).matcher(data.getRentArea()).find()){
				log.setCode(CommunityServiceErrorCode.ERROR_AREASIZE_FORMAT);
				log.setData(data);
				log.setErrorLog("AreaSize format is error");
				return log;
			}
		}

		if (StringUtils.isEmpty(data.getLongitude())) {
			log.setCode(CommunityServiceErrorCode.ERROR_COMMUNITY_LONGITUDE_EMPTY);
			log.setData(data);
			log.setErrorLog("Community longitude cannot be empty");
			return log;
		}else {
			String reg = "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";
			if(!Pattern.compile(reg).matcher(data.getLongitude()).find()){
				log.setCode(CommunityServiceErrorCode.ERROR_LONGITUDE_FORMAT);
				log.setData(data);
				log.setErrorLog("longitude format is error");
				return log;
			}else {
				if (Double.valueOf(data.getLongitude()).doubleValue() > 136 || Double.valueOf(data.getLongitude()).doubleValue() < 70) {
					log.setCode(CommunityServiceErrorCode.ERROR_LONGITUDE_RANGE);
					log.setData(data);
					log.setErrorLog("longitude range is error");
					return log;
				}
			}
		}

		if (StringUtils.isEmpty(data.getLatitude())) {
			log.setCode(CommunityServiceErrorCode.ERROR_COMMUNITY_LATITUDE_EMPTY);
			log.setData(data);
			log.setErrorLog("Community latitude cannot be empty");
			return log;
		}else {
			String reg = "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";
			if(!Pattern.compile(reg).matcher(data.getLatitude()).find()){
				log.setCode(CommunityServiceErrorCode.ERROR_LATITUDE_FORMAT);
				log.setData(data);
				log.setErrorLog("latitude format is error");
				return log;
			}else {
				if (Double.valueOf(data.getLatitude()).doubleValue() > 54 || Double.valueOf(data.getLatitude()).doubleValue() < 3) {
					log.setCode(CommunityServiceErrorCode.ERROR_LATITUDE_RANGE);
					log.setData(data);
					log.setErrorLog("latitude range is error");
					return log;
				}
			}
		}

		if (StringUtils.isEmpty(data.getCommunityType())) {
			log.setCode(CommunityServiceErrorCode.ERROR_COMMUNITY_TYPE);
			log.setData(data);
			log.setErrorLog("Community type cannot be empty");
			return log;
		}else {
			if (!data.getCommunityType().equals("商用") && !data.getCommunityType().equals("住宅")  ) {
				log.setCode(CommunityServiceErrorCode.ERROR_COMMUNITY_TYPE_CONTENT);
				log.setData(data);
				log.setErrorLog("Community type content cannot be empty");
				return log;
			}
		}

		if (StringUtils.isEmpty(data.getAddress()) && (data.getAddress()).length()>50) {
			log.setCode(CommunityServiceErrorCode.ERROR_ADDRESS_LENGTH);
			log.setData(data);
			log.setErrorLog("address length error");
			return log;
		}
		if (StringUtils.isEmpty(data.getProvinceName()) || StringUtils.isEmpty(data.getCityName()) || StringUtils.isEmpty(data.getAreaName())) {
			log.setCode(CommunityServiceErrorCode.ERROR_PROVINCECITYAREA_EMPTY);
			log.setData(data);
			log.setErrorLog("provinceCityArea name cannot be empty");
			return log;
		} else {
			Long provinceId = null;
			Long cityId = null;
			Long areaId = null;
			try {
				provinceId = createRegion(userId, namespaceId, getPath(data.getProvinceName()), 0L); // 创建省
			} catch (Exception e) {
				log.setCode(CommunityServiceErrorCode.ERROR_PROVINCE_NAME);
				log.setData(data);
				log.setErrorLog("province name error");
				return log;
			}
			try {
				cityId = createRegion(userId, namespaceId, getPath(data.getProvinceName(), data.getCityName()), provinceId); // 创建市
			} catch (Exception e) {
				log.setCode(CommunityServiceErrorCode.ERROR_CITY_NAME);
				log.setData(data);
				log.setErrorLog("city name errory");
				return log;
			}
			try {
				areaId = createRegion(userId, namespaceId, getPath(data.getProvinceName(), data.getCityName(), data.getAreaName()), cityId); // 创建区县
			} catch (Exception e) {
				log.setCode(CommunityServiceErrorCode.ERROR_AREA_NAME);
				log.setData(data);
				log.setErrorLog("area name errory");
				return log;
			}
			data.setCityId(cityId);
			data.setAreaId(areaId);
		}

		return null;
	}

	private List<ImportCommunityDataDTO> handleImportCommunityData(List resultList) {
		List<ImportCommunityDataDTO> list = new ArrayList<>();
		for(int i = 1; i < resultList.size(); i++) {
			RowResult r = (RowResult) resultList.get(i);
			if (StringUtils.isNotBlank(r.getA()) || StringUtils.isNotBlank(r.getB()) || StringUtils.isNotBlank(r.getC()) || StringUtils.isNotBlank(r.getD()) ||
					StringUtils.isNotBlank(r.getE()) || StringUtils.isNotBlank(r.getF()) || StringUtils.isNotBlank(r.getG()) || StringUtils.isNotBlank(r.getH()) ||
					StringUtils.isNotBlank(r.getI()) || StringUtils.isNotBlank(r.getJ()) || StringUtils.isNotBlank(r.getK()) || StringUtils.isNotBlank(r.getL())) {
				ImportCommunityDataDTO data = new ImportCommunityDataDTO();
				data.setName(trim(r.getA()));
				data.setCommunityNumber(trim(r.getB()));
				data.setAliasName(trim(r.getC()));
				data.setProvinceName(trim(r.getD()));
				data.setCityName(trim(r.getE()));
				data.setAreaName(trim(r.getF()));
				data.setAddress(trim(r.getG()));
				data.setAreaSize(trim(r.getH()));
				data.setRentArea(trim(r.getI()));
				data.setLongitude(trim(r.getJ()));
				data.setLatitude(trim(r.getK()));
				data.setCommunityType(trim(r.getL()));
				list.add(data);
			}
		}
		return list;
	}



	@Override
	public ListAllCommunitiesResponse listAllCommunities() {

		List<Community> communities = communityProvider.listCommunitiesByNamespaceId(UserContext.getCurrentNamespaceId());

		Set<Long> familyCommunityIdSet = new HashSet<>();
		Set<Long> orgCommunityIdSet = new HashSet<>();

		User user = UserContext.current().getUser();

		//登录的话找加入的家庭和公司
		if(user != null && user.getId() != null){
			orgCommunityIdSet = getWorkPlaceCommunityIdsByUserId(user.getId());
			familyCommunityIdSet = getFamilyCommunityIdsByUserId(user.getId());
		}


		List<CommunityInfoDTO> dtos = new ArrayList<>();
		for (Community com: communities){
			CommunityInfoDTO dto = ConvertHelper.convert(com, CommunityInfoDTO.class);
			
			//add by momoubin,2018/12/13,项目图片uri转url
			if(StringUtils.isNotBlank(com.getBackgroundImgUri())){
				String backgroundImgUri = com.getBackgroundImgUri();
				String backgroundImgUrl = contentServerService.parserUri(backgroundImgUri, EntityType.USER.getCode(), user.getId());
				dto.setBackgroundImgUrl(backgroundImgUrl);				
			}
			
			if(familyCommunityIdSet.contains(com.getId())){
				dto.setApartmentFlag(TrueOrFalseFlag.TRUE.getCode());
			}

			if(orgCommunityIdSet.contains(com.getId())){
				dto.setSiteFlag(TrueOrFalseFlag.TRUE.getCode());
			}

			String pinyin = PinYinHelper.getPinYin(dto.getName());
			dto.setFullPinyin(pinyin.replaceAll(" ", ""));
			dto.setCapitalPinyin(PinYinHelper.getCapitalInitial(pinyin));

			dtos.add(dto);
		}

		ListAllCommunitiesResponse response = new ListAllCommunitiesResponse();
		response.setDtos(dtos);

		return response;
	}

	private Set<Long> getFamilyCommunityIdsByUserId(Long userId){


		Set<Long> familyCommunityIdSet = new HashSet<>();

		List<FamilyDTO> familyDtos = familyProvider.getUserFamiliesByUserId(userId);

		if(familyDtos != null && familyDtos.size() > 0){
			for(FamilyDTO dto: familyDtos){
				if(GroupMemberStatus.ACTIVE == GroupMemberStatus.fromCode(dto.getMembershipStatus())){
					familyCommunityIdSet.add(dto.getCommunityId());
				}
			}
		}

		return familyCommunityIdSet;
	}


	private Set<Long> getWorkPlaceCommunityIdsByUserId(Long userId){


		Set<Long> orgCommunityIdSet = new HashSet<>();

		List<OrganizationMember> members = organizationProvider.listOrganizationMembersByOrganizationIdAndMemberGroup(null, OrganizationMemberTargetType.USER.getCode(), userId);
		if(members != null){
			for (OrganizationMember member: members) {
				if(OrganizationGroupType.ENTERPRISE == OrganizationGroupType.fromCode(member.getGroupType())){


					//定制版要直接使用OrganizationCommunityRequest表的数据
					if(namespacesService.isStdNamespace(UserContext.getCurrentNamespaceId())){
						List<OrganizationWorkPlaces> workPlaces = organizationProvider.findOrganizationWorkPlacesByOrgId(member.getOrganizationId());
						if(workPlaces != null && workPlaces.size() > 0){
							for (OrganizationWorkPlaces workPlace: workPlaces){
								orgCommunityIdSet.add(workPlace.getCommunityId());
							}
						}
					}else {

						OrganizationCommunityRequest organizationCommunityRequest = organizationProvider.getOrganizationCommunityRequestByOrganizationId(member.getOrganizationId());
						if(organizationCommunityRequest != null){
							orgCommunityIdSet.add(organizationCommunityRequest.getCommunityId());
						}

					}

				}
			}
		}

		return orgCommunityIdSet;

	}


	@Override
	public CommunityInfoDTO findNearbyMixCommunity(FindNearbyMixCommunityCommand cmd) {

		List<CommunityGeoPoint> pointList;


//TODO 5的范围太小，但是改成3的话，会把大量的数据查询出来。因为0域空间有大量的Community和CommunityGeoPoint数据。
		//TODO findCommunityGeoPointByGeoHash接口应该带上namespaceId，内部和eh_communities连表查询。并且规定不能查询0域空间。
		for(int i = 12; i > 5; i--){
			pointList = this.communityProvider.findCommunityGeoPointByGeoHash(cmd.getLatitude(), cmd.getLongitude(), i);
			if(pointList != null && pointList.size() > 0){
				for(CommunityGeoPoint point: pointList){


					Community community = communityProvider.findCommunityById(point.getCommunityId());
					if(community == null || !community.getNamespaceId().equals(UserContext.getCurrentNamespaceId())){
						continue;
					}

					CommunityInfoDTO dto = ConvertHelper.convert(community, CommunityInfoDTO.class);

					Set<Long> familyCommunityIdSet;
					Set<Long> orgCommunityIdSet;

					User user = UserContext.current().getUser();

					//登录的话找加入的家庭和公司
					if(user != null && user.getId() != null){
						orgCommunityIdSet = getWorkPlaceCommunityIdsByUserId(user.getId());
						if(orgCommunityIdSet.contains(community.getId())){
							dto.setSiteFlag(TrueOrFalseFlag.TRUE.getCode());
						}
						familyCommunityIdSet = getFamilyCommunityIdsByUserId(user.getId());
						if(familyCommunityIdSet.contains(community.getId())){
							dto.setApartmentFlag(TrueOrFalseFlag.TRUE.getCode());
						}
					}

					String pinyin = PinYinHelper.getPinYin(dto.getName());
					dto.setFullPinyin(pinyin.replaceAll(" ", ""));
					dto.setCapitalPinyin(PinYinHelper.getCapitalInitial(pinyin));

					return dto;
				}

			}
		}


		return null;
	}

	@Override
	public CommunityInfoDTO findDefaultCommunity() {
		Community defaultCommunity = communityProvider.findDefaultCommunity(UserContext.getCurrentNamespaceId());
		if(defaultCommunity == null){
			defaultCommunity = communityProvider.findAnyCommunity(UserContext.getCurrentNamespaceId());
		}


		if(defaultCommunity != null){
			CommunityInfoDTO dto = ConvertHelper.convert(defaultCommunity, CommunityInfoDTO.class);
			String pinyin = PinYinHelper.getPinYin(dto.getName());
			dto.setFullPinyin(pinyin.replaceAll(" ", ""));
			dto.setCapitalPinyin(PinYinHelper.getCapitalInitial(pinyin));

			return dto;
		}

		return null;
	}

	@Override
	public ListCommunitiesResponse listCommunities(ListCommunitiesCommand cmd) {
		if(cmd.getPageAnchor()==null)
			cmd.setPageAnchor(0L);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		ListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<Community> list = this.communityProvider.listCommunities(cmd.getNamespaceId(),cmd.getCommunityType(),cmd.getOrgId(),cmd.getKeyword(),cmd.getStatus(), cmd.getOrganizationOwnerFlag(), locator, pageSize+1);

		ListCommunitiesResponse response = new ListCommunitiesResponse();
		if(list != null && list.size() > pageSize){
			list.remove(list.size()-1);
			response.setNextPageAnchor(list.get(list.size()-1).getId());
		}
		if(list != null){
			List<CommunityDTO> dtos = new ArrayList<>();
			for(Community community: list){
				CommunityDTO dto = ConvertHelper.convert(community, CommunityDTO.class);
				List<OrganizationCommunityDTO> orgcoms = organizationProvider.findOrganizationCommunityByCommunityId(dto.getId());
				if(orgcoms != null && orgcoms.size() > 0){
					Organization org = organizationProvider.findOrganizationById(orgcoms.get(0).getOrganizationId());
					if(org != null){
						dto.setPmOrgId(org.getId());
						dto.setPmOrgName(org.getName());
					}
				}
				dtos.add(dto);
			}


			response.setList(dtos);
		}

		return response;
	}

	@Override
	public CreateCommunitiesResponse createCommunities(CreateCommunitiesCommand cmd) {
		List<CommunityDTO> dtos = new ArrayList<>();

		for(CreateCommunityCommand cmdOne: cmd.getCommunities()){
			CreateCommunityResponse community = createCommunity(cmdOne);
			dtos.add(community.getCommunityDTO());
		}

		CreateCommunitiesResponse response = new CreateCommunitiesResponse();
		response.setDtos(dtos);
		return response;
	}

	@Override
	public void updateCommunityPartial(UpdateCommunityPartialAdminCommand cmd) {
		if(cmd.getCommunityId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid communityId parameter");
		}

		Community community = this.communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null){
			LOGGER.error("Community is not found.communityId=" + cmd.getCommunityId());
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST,
					"Community is not found.");
		}
		User user = UserContext.current().getUser();
		long userId = user.getId();

		if(cmd.getName() != null){
			community.setName(cmd.getName());
		}

		if(cmd.getStatus() != null){
			community.setStatus(cmd.getStatus());
		}

		if(cmd.getProvinceName() != null && cmd.getCityName() != null && cmd.getAreaName() != null){
			Long provinceId = createRegion(userId, community.getNamespaceId(), getPath(cmd.getProvinceName()), 0L);  //创建省
			Long cityId = createRegion(userId, community.getNamespaceId(), getPath(cmd.getProvinceName(), cmd.getCityName()), provinceId);  //创建市
			Long areaId = createRegion(userId, community.getNamespaceId(), getPath(cmd.getProvinceName(), cmd.getCityName(), cmd.getAreaName()), cityId);  //创建区县
			community.setProvinceId(provinceId);
			community.setCityId(cityId);
			community.setAreaId(areaId);
			community.setProvinceName(cmd.getProvinceName());
			community.setCityName(cmd.getCityName());
			community.setAreaName(cmd.getAreaName());
		}


		if(cmd.getAddress() != null){
			community.setAddress(cmd.getAddress());
		}


		if(cmd.getLatitude() != null && cmd.getLongitude() != null){
			CommunityGeoPoint geoPoint = communityProvider.findCommunityGeoPointByCommunityId(community.getId());
			if(geoPoint != null){
				geoPoint.setLatitude(cmd.getLatitude());
				geoPoint.setLongitude(cmd.getLongitude());
				geoPoint.setGeohash(GeoHashUtils.encode(cmd.getLatitude(), cmd.getLongitude()));
				communityProvider.updateCommunityGeoPoint(geoPoint);
			}
		}


		community.setOperatorUid(userId);
		communityProvider.updateCommunity(community);

	}

	@Override
	public void changeOrganizationCommunities(ChangeOrganizationCommunitiesCommand cmd) {

		dbProvider.execute(status -> {

			if(cmd.getCommunityIds() != null && !"".equals(cmd.getCommunityIds())){
				for (Long communityId: cmd.getCommunityIds()){
					changeOrganizationCommunity(communityId, cmd.getFromOrgId(), cmd.getToOrgId());
				}
			}

			return null;
		});


	}

	@Override
	public void changeOrganizationCommunity(Long communityId, Long fromOrgId, Long toOrgId) {

		if(communityId == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid communityId parameter");
		}

		Community community = this.communityProvider.findCommunityById(communityId);
		if(community == null){
			LOGGER.error("Community is not found.communityId=" + communityId);
			throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST,
					"Community is not found.");
		}

		//查询原属记录
		OrganizationCommunity orgcom = organizationProvider.findOrganizationProperty(communityId);


		dbProvider.execute(status -> {

			if(orgcom != null){
				orgcom.setOrganizationId(toOrgId);
				organizationProvider.updateOrganizationCommunity(orgcom);
			}else {

				OrganizationCommunity organizationCommunity = new OrganizationCommunity();
				organizationCommunity.setOrganizationId(toOrgId);
				organizationCommunity.setCommunityId(communityId);
				organizationProvider.createOrganizationCommunity(organizationCommunity);
			}


			//更新授权，删除原有授权，新增新的授权
			serviceModuleAppAuthorizationService.updateAllAuthToNewOrganization(community.getNamespaceId(), toOrgId, community.getId());


			return null;
		});



	}


	@Override
	public ListBuildingsByKeywordsResponse listBuildingsByKeywords(ListBuildingsByKeywordsCommand cmd) {
		int pageSize =  cmd.getPageSize() != null ? cmd.getPageSize() : 1000;
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<Building> buildings = communityProvider.listBuildingsByKeywords(cmd.getNamespaceId(),cmd.getCommunityId(),
        																	cmd.getBuildingId(),cmd.getKeyWords(),locator,pageSize);
        Long nextPageAnchor = null;
        if(buildings.size() > pageSize) {
        	buildings.remove(buildings.size() - 1);
            nextPageAnchor = buildings.get(buildings.size() - 1).getDefaultOrder();
        }

        List<BuildingInfoDTO> dtoList = new ArrayList<>();
        buildings.stream().forEach((r) -> {
        	BuildingInfoDTO dto = ConvertHelper.convert(r, BuildingInfoDTO.class);
        	dto.setBuildingId(r.getId());
        	dto.setBuildingName(r.getName());
        	//在租合同数
        	Integer relatedContractNumber = contractProvider.countRelatedContractNumberInBuilding(r.getName(),r.getCommunityId());
        	dto.setRelatedContractNumber(relatedContractNumber);

        	Double totalRent = contractProvider.getTotalRentInBuilding(r.getName());
    		if (r.getRentArea() != null && r.getRentArea() > 0) {
    			Double areaAveragePrice = totalRent/r.getRentArea();
        		dto.setAreaAveragePrice(doubleRoundHalfUp(areaAveragePrice,2));
			}
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
    		dtoList.add(dto);
        });

        ListBuildingsByKeywordsResponse response = new ListBuildingsByKeywordsResponse();
        response.setNextPageAnchor(nextPageAnchor);
        response.setBuildings(dtoList);

        return response;
	}


	@Override
	public CommunityStatisticsDTO getCommunityStatistics(GetCommunityStatisticsCommand cmd) {
		CommunityStatisticsDTO result =  new CommunityStatisticsDTO();
		if (cmd.getCommunityId() == null) {
			initializeCommunityStatisticsDTO(result);
			List<Community> communities = communityProvider.findCommunitiesByNamespaceId(cmd.getNameSpaceId());
			for (Community community : communities) {
				CommunityStatisticsDTO dto = generateCommunityStatistics(community.getId());
				result.setApartmentNumber(result.getApartmentNumber() + dto.getApartmentNumber());
				result.setAreaSize(result.getAreaSize() + dto.getAreaSize());
				result.setRentArea(result.getRentArea() + dto.getRentArea());
				result.setFreeArea(result.getFreeArea() + dto.getFreeArea());
				result.setChargeArea(result.getChargeArea() + dto.getChargeArea());
				result.setTotalRent(result.getTotalRent() + dto.getTotalRent());
				result.setRelatedContractNumber(result.getRelatedContractNumber() + dto.getRelatedContractNumber());
			}
			result.setTotalRent(doubleRoundHalfUp(result.getTotalRent(),2));
			if (result.getRentArea() != null && result.getRentArea() > 0) {
				Double areaAveragePrice = result.getTotalRent()/result.getRentArea();
				result.setAreaAveragePrice(doubleRoundHalfUp(areaAveragePrice,2));
			}
			result.setAreaSize(doubleRoundHalfUp(result.getAreaSize(),2));
			result.setRentArea(doubleRoundHalfUp(result.getRentArea(),2));
			result.setFreeArea(doubleRoundHalfUp(result.getFreeArea(),2));
			result.setChargeArea(doubleRoundHalfUp(result.getChargeArea(),2));
			result.setTotalRent(doubleRoundHalfUp(result.getTotalRent(),2));
		}else {
			result = generateCommunityStatistics(cmd.getCommunityId());
		}
		return result;
	}

	private void initializeCommunityStatisticsDTO(CommunityStatisticsDTO dto){
		dto.setApartmentNumber(0);
		dto.setAreaSize(0.0);
		dto.setRentArea(0.0);
		dto.setFreeArea(0.0);
		dto.setChargeArea(0.0);
		dto.setTotalRent(0.0);
		dto.setRelatedContractNumber(0);
	}

	private CommunityStatisticsDTO generateCommunityStatistics(Long communityId){
		Community community = communityProvider.findCommunityById(communityId);

		CommunityStatisticsDTO dto = ConvertHelper.convert(community, CommunityStatisticsDTO.class);
		dto.setCommunityId(community.getId());
		dto.setCommunityName(community.getName());

		Integer buildingNumber = communityProvider.countActiveBuildingsByCommunityId(community.getId());
		dto.setBuildingNumber(buildingNumber);
		Integer apartmentNumber = communityProvider.countActiveApartmentsByCommunityId(community.getId());
		dto.setApartmentNumber(apartmentNumber);
		//在租合同数
		Integer relatedContractNumber = communityProvider.countRelatedContractNumberInCommunity(community.getId());
		dto.setRelatedContractNumber(relatedContractNumber);
		//在租实时均价
		Double totalRent = contractProvider.getTotalRentInCommunity(community.getId());
		dto.setTotalRent(doubleRoundHalfUp(totalRent,2));

		if (community.getRentArea() != null && community.getRentArea() > 0) {
			Double areaAveragePrice = totalRent/community.getRentArea();
			dto.setAreaAveragePrice(doubleRoundHalfUp(areaAveragePrice,2));
		}
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
	}

	@Override
	public CommunityDetailDTO getCommunityDetail(GetCommunityDetailCommand cmd) {
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());

		CommunityDetailDTO dto = ConvertHelper.convert(community, CommunityDetailDTO.class);
		dto.setCommunityId(community.getId());
		dto.setCommunityName(community.getName());
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

		ResourceCategoryAssignment assignment = communityProvider.findResourceCategoryAssignment(cmd.getCommunityId(),cmd.getProjectType(),cmd.getNamespaceId());
		if (assignment != null) {
			ResourceCategory category = communityProvider.findResourceCategoryById(assignment.getResourceCategryId());
			checkResourceCategoryIsNull(category);
			dto.setCategoryId(category.getId());
			dto.setCategoryName(category.getName());
		}
		return dto;
	}


	@Override
	public void updateCommunityAndCategory(UpdateCommunityNewCommand cmd) {
		assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_UPDATE_COMMUNITY, cmd.getOrganizationId(), cmd.getCommunityId());

		Community community = communityProvider.findCommunityById(cmd.getCommunityId());

		community.setName(cmd.getCommunityName());
		community.setAliasName(cmd.getAliasName());
		community.setAddress(cmd.getAddress());
		community.setCommunityNumber(cmd.getCommunityNumber());

//		面积数据由房源的面积数据累加得来，不接受直接修改
//		community.setAreaSize(cmd.getAreaSize());
//		community.setRentArea(cmd.getRentArea());
//		community.setFreeArea(cmd.getFreeArea());
//		community.setChargeArea(cmd.getChargeArea());

		if (cmd.getCityId() != null) {
			Region region = regionProvider.findRegionById(cmd.getCityId());
			checkRegionIsNull(region);
			community.setCityId(cmd.getCityId());
			community.setCityName(region.getName());
		}

		if (cmd.getAreaId() != null) {
			Region region = regionProvider.findRegionById(cmd.getAreaId());
			checkRegionIsNull(region);
			community.setAreaId(cmd.getAreaId());
			community.setAreaName(region.getName());
		}

		communityProvider.updateCommunity(community);
		//更新园区项目分类
		Integer namespaceId = cmd.getNamespaceId();
		ResourceCategoryAssignment rca = communityProvider.findResourceCategoryAssignment(cmd.getCommunityId(), cmd.getResourceType(), namespaceId);
		if(null != rca) {
			if(null != cmd.getCategoryId()) {
				ResourceCategory category = communityProvider.findResourceCategoryById(cmd.getCategoryId());
				checkResourceCategoryIsNull(category);
				rca.setResourceCategryId(category.getId());
				communityProvider.updateResourceCategoryAssignment(rca);
			}else{
				communityProvider.deleteResourceCategoryAssignmentById(rca.getId());
			}
		}else{
			if(null != cmd.getCategoryId()){
				ResourceCategory category = communityProvider.findResourceCategoryById(cmd.getCategoryId());
				checkResourceCategoryIsNull(category);
				rca = new ResourceCategoryAssignment();
				rca.setCreateTime(new Timestamp(System.currentTimeMillis()));
				rca.setCreatorUid(UserContext.current().getUser().getId());
				rca.setNamespaceId(namespaceId);
				rca.setResourceCategryId(category.getId());
				rca.setResourceId(cmd.getCommunityId());
				rca.setResourceType(cmd.getResourceType());
				communityProvider.createResourceCategoryAssignment(rca);
			}
		}
	}

	private void checkRegionIsNull(Region region) {
		if(null == region) {
			LOGGER.error("Region not found.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Region not found.");
		}
	}

	@Override
	public BuildingStatisticsDTO getBuildingStatistics(GetBuildingStatisticsCommand cmd) {
		Building building = communityProvider.findBuildingById(cmd.getBuildingId());
		BuildingStatisticsDTO dto = ConvertHelper.convert(building, BuildingStatisticsDTO.class);
		dto.setBuildingId(building.getId());
		dto.setBuildingName(building.getName());

		Integer apartmentNumber = addressProvider.countApartmentNumberByBuildingName(building.getCommunityId(), building.getName());
		dto.setApartmentNumber(apartmentNumber);

		//在租合同数
		Integer relatedContractNumber = contractProvider.countRelatedContractNumberInBuilding(building.getName(),building.getCommunityId());
    	dto.setRelatedContractNumber(relatedContractNumber);

    	//在租实时均价
    	Double totalRent = contractProvider.getTotalRentInBuilding(building.getName());
    	dto.setTotalRent(doubleRoundHalfUp(totalRent,2));
    	if (building.getRentArea() != null && building.getRentArea() > 0) {
			Double areaAveragePrice = totalRent/building.getRentArea();
			dto.setAreaAveragePrice(doubleRoundHalfUp(areaAveragePrice,2));
    	}

		Integer relatedEnterpriseCustomerNumber = addressProvider.countRelatedEnterpriseCustomerNumber(building.getCommunityId(),building.getId());
		dto.setRelatedEnterpriseCustomerNumber(relatedEnterpriseCustomerNumber);

		Integer relatedOrganizationOwnerNumber = addressProvider.countRelatedOrganizationOwnerNumber(building.getCommunityId(), building.getId());
		dto.setRelatedOrganizationOwnerNumber(relatedOrganizationOwnerNumber);

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
		if(dto.getTotalRent()!=null){
			dto.setTotalRent(doubleRoundHalfUp(dto.getTotalRent(),2));
		}
		
		if (dto.getAreaSize()!=null && dto.getAreaSize()!=0) {
			if (dto.getFreeArea()!=null) {
				BigDecimal freeRate = new BigDecimal(dto.getFreeArea()).divide(new BigDecimal(dto.getAreaSize()),2,RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
				dto.setFreeRate(freeRate);
			}else {
				dto.setFreeRate(BigDecimal.ZERO);
			}
		}
		
		return dto;
	}


	@Override
	public void exportBuildingByKeywords(ListBuildingsByKeywordsCommand cmd, HttpServletResponse response) {
		assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_EXPORT_BUILDING, cmd.getOrganizationId(), cmd.getCommunityId());
		
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
        if (community == null) {
            LOGGER.error("Community is not exist.");
            throw errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST,
                    "Community is not exist.");
        }

        int pageSize =  cmd.getPageSize() != null ? cmd.getPageSize() : 10000;
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<Building> buildings = communityProvider.listBuildingsByKeywords(cmd.getNamespaceId(),cmd.getCommunityId(),
        																	cmd.getBuildingId(),cmd.getKeyWords(),locator,pageSize);
        if (buildings != null && buildings.size() > 0) {
			String fileName = String.format("楼栋信息导出_%s", community.getName());
			ExcelUtils excelUtils = new ExcelUtils(response, fileName, "楼栋信息");
			excelUtils = excelUtils.setNeedSequenceColumn(false);
			
			List<BuildingExportDataDTO> data = buildings.stream().map(r->{
					BuildingExportDataDTO dto = ConvertHelper.convert(r, BuildingExportDataDTO.class);
					dto.setLatitudeLongitude(r.getLatitude() + "," + r.getLongitude());
					dto.setDescription(parseHtml(dto.getDescription()));
					dto.setTrafficDescription(parseHtml(dto.getTrafficDescription()));
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
				}
			).collect(Collectors.toList());
			//设置excel提示
			excelUtils.setNeedTitleRemark(true).setTitleRemark("导出注意事项：\n" +
                    "1、该导出结果可直接应用于系统的楼宇导入操作，但进行楼宇导入操作时，建筑面积、在租面积、可招租面积、收费面积这四个字段的值将不会记录到系统当中。\n" +
                    "2、带有星号（*）的字段为导入时的必填项。\n" +
                    "3、请不要随意复制单元格，这样会破坏字段规则校验。\n" +
                    "4、导入已存在的楼栋（楼栋名称相同认为是已存在的楼栋），将按照导入的楼栋信息更新系统已存在的楼栋信息。\n" +
                    "\n", (short) 13, (short) 2500)
			.setNeedSequenceColumn(false)
			.setIsCellStylePureString(true)
			.setTitleRemarkColorIndex(HSSFColor.YELLOW.index);
			String[] propertyNames = {"name", "buildingNumber", "aliasName", "floorNumber", "address","latitudeLongitude", "managerName", "contact", "description", "trafficDescription", "areaSize", "rentArea", "freeArea", "chargeArea"};
			String[] titleNames = {"*楼宇名称", "楼宇编号", "简称", "楼层数", "*地址", "经纬度", "*联系人", "*联系电话", "楼宇介绍", "交通说明", "建筑面积(㎡)", "在租面积(㎡)", "可招租面积(㎡)", "收费面积(㎡)"};
			int[] titleSizes = {20, 20, 20, 20, 40, 25, 20, 20, 20, 20, 20, 20, 20, 20};
			excelUtils.writeExcel(propertyNames, titleNames, titleSizes, data);
		} else {
			throw errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_NO_DATA,
					"no data");
		}
	}

	@Override
	public void caculateCommunityArea(CaculateCommunityAreaCommand cmd) {
		List<Community> communities = communityProvider.findCommunitiesByNamespaceId(cmd.getNamespaceId());
		for (Community community : communities) {
			initCommunityData(community);
			List<Address> addresses = addressProvider.findActiveAddressByCommunityId(community.getId());
			for (Address address : addresses) {
				community.setAptCount(community.getAptCount() + 1);
				community.setAreaSize(community.getAreaSize() + (address.getAreaSize() != null ? address.getAreaSize() : 0.0));
				community.setRentArea(community.getRentArea() + (address.getRentArea() != null ? address.getRentArea() : 0.0));
				community.setChargeArea(community.getChargeArea() + (address.getChargeArea() != null ? address.getChargeArea() : 0.0));
				community.setFreeArea(community.getFreeArea() + (address.getFreeArea() != null ? address.getFreeArea() : 0.0));
				community.setSharedArea(community.getSharedArea() + (address.getSharedArea() != null ? address.getSharedArea() : 0.0));
				address.setCommunityName(community.getName());
				addressProvider.updateAddress(address);
			}
			communityProvider.updateCommunity(community);
		}
	}

	private void initCommunityData(Community community){
		community.setAptCount(0);
		community.setAreaSize(0.0);
		community.setRentArea(0.0);
		community.setChargeArea(0.0);
		community.setFreeArea(0.0);
		community.setSharedArea(0.0);
	}

	@Override
	public void caculateBuildingArea(CaculateBuildingAreaCommand cmd) {
		List<Community> communities = communityProvider.findCommunitiesByNamespaceId(cmd.getNamespaceId());
		for (Community community : communities) {
			List<Building> buildings = communityProvider.findBuildingsByCommunityId(community.getId());
			for (Building building : buildings) {
				initBuildingData(building);
				List<Address> addresses = addressProvider.findActiveAddressByBuildingNameAndCommunityId(building.getName(),community.getId());
				for (Address address : addresses) {
					building.setAreaSize(building.getAreaSize() + (address.getAreaSize() != null ? address.getAreaSize() : 0.0));
					building.setRentArea(building.getRentArea() + (address.getRentArea() != null ? address.getRentArea() : 0.0));
					building.setChargeArea(building.getChargeArea() + (address.getChargeArea() != null ? address.getChargeArea() : 0.0));
					building.setFreeArea(building.getFreeArea() + (address.getFreeArea() != null ? address.getFreeArea() : 0.0));
					building.setSharedArea(building.getSharedArea() + (address.getSharedArea() != null ? address.getSharedArea() : 0.0));
					address.setBuildingId(building.getId());
					addressProvider.updateAddress(address);
				}
				communityProvider.updateBuilding(building);
			}
		}
	}

	private void initBuildingData(Building building) {
		building.setAreaSize(0.0);
		building.setRentArea(0.0);
		building.setChargeArea(0.0);
		building.setFreeArea(0.0);
		building.setSharedArea(0.0);
	}

	@Override
	public ListApartmentsInCommunityResponse listApartmentsInCommunity(ListApartmentsInCommunityCommand cmd) {
    	assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_GET_APARTMENT_DETAIL, cmd.getOrganizationId(), cmd.getCommunityId());
		
		ListApartmentsInCommunityResponse result = new ListApartmentsInCommunityResponse();
		List<ApartmentInfoDTO> apartments = new ArrayList<>();
		initListApartmentsInCommunityResponse(result);

		//管理公司全部项目范围
		if (cmd.getAllScope() == 1) {
			List<Long> communityIds = organizationService.getOrganizationProjectIdsByAppId(cmd.getOrganizationId(), cmd.getAppId());
			cmd.setCommunityIds(communityIds);
		}
		
		List<Address> addresses = addressProvider.listApartmentsInCommunity(cmd);

		List<Long> addressIdList = addresses.stream().map(a->a.getId()).collect(Collectors.toList());

		Map<Long, CommunityAddressMapping> communityAddressMappingMap = propertyMgrProvider.mapAddressMappingByAddressIds(addressIdList);

		List<Long> filterAddressIdList = new ArrayList<>();
		for (Address address : addresses) {
			filterAddressIdList.add(address.getId());
			//获取房源状态
			byte livingStatus = AddressMappingStatus.LIVING.getCode();
			CommunityAddressMapping mapping = communityAddressMappingMap.get(address.getId());
			if(mapping != null){
				livingStatus = mapping.getLivingStatus().byteValue();
			}
			//按房源状态筛选
			if (cmd.getLivingStatus()!=null && livingStatus!=cmd.getLivingStatus().byteValue()) {
				filterAddressIdList.remove(address.getId());
				continue;
			}

			ApartmentInfoDTO dto = convertToApartmentInfoDTO(address,livingStatus);
			apartments.add(dto);
			caculateTotalApartmentStatistic(result,dto);
		}
		//在租合同数
		int totalRelatedContractNumber = 0;
		totalRelatedContractNumber = contractProvider.getRelatedContractCountByAddressIds(filterAddressIdList);
		result.setTotalRelatedContractNumber(totalRelatedContractNumber);
		//在租合同总额
		double totalRent = 0;
		totalRent = contractProvider.getTotalRentByAddressIds(filterAddressIdList);
		result.setTotalRent(totalRent);
		result.setApartments(apartments);
		result.setTotalAreaSize(doubleRoundHalfUp(result.getTotalAreaSize(),2));
		result.setTotalRentArea(doubleRoundHalfUp(result.getTotalRentArea(),2));
		result.setTotalFreeArea(doubleRoundHalfUp(result.getTotalFreeArea(),2));
		result.setTotalChargeArea(doubleRoundHalfUp(result.getTotalChargeArea(),2));
		result.setTotalRent(doubleRoundHalfUp(result.getTotalRent(),2));
		return result;
	}

	private void caculateTotalApartmentStatistic(ListApartmentsInCommunityResponse result, ApartmentInfoDTO dto) {
		result.setTotalApartmentNumber(result.getTotalApartmentNumber() + 1);
//		result.setTotalRelatedContractNumber(result.getTotalRelatedContractNumber() + dto.getRelatedContractNumber());
		result.setTotalAreaSize(result.getTotalAreaSize().doubleValue() + (dto.getAreaSize()!=null ?dto.getAreaSize().doubleValue():0.0));
		result.setTotalRentArea(result.getTotalRentArea().doubleValue() + (dto.getRentArea()!=null ?dto.getRentArea().doubleValue():0.0));
		result.setTotalFreeArea(result.getTotalFreeArea().doubleValue() + (dto.getFreeArea()!=null ?dto.getFreeArea().doubleValue():0.0));
		result.setTotalChargeArea(result.getTotalChargeArea().doubleValue() + (dto.getChargeArea()!=null ?dto.getChargeArea().doubleValue():0.0));
//		result.setTotalRent(result.getTotalRent().doubleValue() + (dto.getTotalRent()!=null ? dto.getTotalRent().doubleValue():0.0));
	}

	private void initListApartmentsInCommunityResponse(ListApartmentsInCommunityResponse response){
		response.setTotalRelatedContractNumber(0);
		response.setTotalApartmentNumber(0);
		response.setTotalAreaSize(0.0);
		response.setTotalRentArea(0.0);
		response.setTotalFreeArea(0.0);
		response.setTotalChargeArea(0.0);
		response.setTotalRent(0.0);
	}

	private ApartmentInfoDTO convertToApartmentInfoDTO(Address address,byte livingStatus){
		ApartmentInfoDTO dto = new ApartmentInfoDTO();
		dto.setCommunityId(address.getCommunityId());
		dto.setCommunityName(address.getCommunityName());
		dto.setAddressId(address.getId());
		dto.setBuildingName(address.getBuildingName());
		dto.setApartmentFloor(address.getApartmentFloor());
		dto.setApartmentName(address.getApartmentName());
		dto.setLivingStatus(livingStatus);
//		dto.setAreaAveragePrice(areaAveragePrice);
//		dto.setTotalRent(totalRent);
//		dto.setRelatedContractNumber(relatedContractNumber);
		dto.setAreaSize(address.getAreaSize());
		dto.setRentArea(address.getRentArea());
		dto.setFreeArea(address.getFreeArea());
		dto.setChargeArea(address.getChargeArea());

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
//		if(dto.getTotalRent()!=null){
//			dto.setTotalRent(doubleRoundHalfUp(dto.getTotalRent(),2));
//		}
		return dto;
	}

	//四舍五入截断double类型数据
	private double doubleRoundHalfUp(double input,int scale){
		BigDecimal digit = new BigDecimal(input);
		return digit.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	@Override
	public FloorRangeDTO getFloorRange(GetFloorRangeCommand cmd) {
		FloorRangeDTO dto = new FloorRangeDTO();
		List<Building> buildings = new ArrayList<>();

		if (cmd.getCommunityId()!=null) {
			buildings = communityProvider.findBuildingsByCommunityId(cmd.getCommunityId());
		}else if (cmd.getNamespaceId()!=null) {
			buildings = communityProvider.findBuildingsByNamespaceId(cmd.getNamespaceId());
		}

		int maxFloor = 0;
		for (Building building : buildings) {
			if (building.getFloorNumber() == null) {
				continue;
			}
			if (building.getFloorNumber().intValue() > maxFloor) {
				maxFloor = building.getFloorNumber().intValue();
			}
		}
		dto.setMaxFloor(maxFloor);

		return dto;
	}

	@Override
	public void changeBuildingOrder(ChangeBuildingOrderCommand cmd) {
		assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_CHANGE_BUILDING_ORDER, cmd.getOrganizationId(), cmd.getCommunityId());
		
		if (cmd.getBuildingOrders() != null && cmd.getBuildingOrders().size() > 0) {
			List<BuildingOrderDTO> buildingOrders = cmd.getBuildingOrders();

			List<Long> buildingIds = new ArrayList<>();
			Map<Long, Long> newBuildingIdOrderMap = new HashMap<>();
			buildingOrders.stream().forEach(r->{
				buildingIds.add(r.getBuildingId());
				newBuildingIdOrderMap.put(r.getBuildingId(), r.getDefaultOrder());
			});

			Map<Long, Building> buildingIdAndBuildingMap = communityProvider.mapBuildingIdAndBuilding(buildingIds);
			for (Long buildingId : buildingIds) {
				Long newOrder = newBuildingIdOrderMap.get(buildingId);
				Building building = buildingIdAndBuildingMap.get(buildingId);
				Long oldOrder = building.getDefaultOrder();
				if (!newOrder.equals(oldOrder)) {
					building.setDefaultOrder(newOrder);
					communityProvider.updateBuilding(building);
				}
			}
		}
	}

	@Override
	public void exportApartmentsInCommunity(ListApartmentsInCommunityCommand cmd,HttpServletResponse response) {
		assetManagementPrivilegeCheck(cmd.getNamespaceId(), PrivilegeConstants.PM_PROPERTY_MANAGEMENT_EXPORT_APARTMENTS_IN_COMMUNITY, cmd.getOrganizationId(), cmd.getCommunityId());
		
		ListApartmentsInCommunityResponse result = new ListApartmentsInCommunityResponse();
		List<ApartmentExportDataDTO> data = new ArrayList<>();
		initListApartmentsInCommunityResponse(result);

		List<Address> addresses = addressProvider.listApartmentsInCommunity(cmd);
		List<Long> addressIdList = addresses.stream().map(a->a.getId()).collect(Collectors.toList());
		Map<Long, CommunityAddressMapping> communityAddressMappingMap = propertyMgrProvider.mapAddressMappingByAddressIds(addressIdList);

		for (Address address : addresses) {
			//获取房源状态
			byte livingStatus = AddressMappingStatus.LIVING.getCode();
			CommunityAddressMapping mapping = communityAddressMappingMap.get(address.getId());
			if(mapping != null){
				livingStatus = mapping.getLivingStatus().byteValue();
			}
			//按房源状态筛选
			if (cmd.getLivingStatus()!=null && livingStatus!=cmd.getLivingStatus().byteValue()) {
				continue;
			}
			//获取在租实时均价
			//double areaAveragePrice = 0;
			double totalRent = 0;
			List<Contract> contracts = contractProvider.findContractByAddressId(address.getId());
			if (contracts != null && contracts.size() > 0){
				for (Contract contract : contracts) {
					totalRent += (contract.getRent()!=null ? contract.getRent().doubleValue() : 0);
				}
				totalRent = doubleRoundHalfUp(totalRent,2);
			}
//			if (address.getRentArea() != null && address.getRentArea() > 0) {
//				areaAveragePrice = doubleRoundHalfUp(totalRent/address.getRentArea(),2);
//	    	}
//			//按在租实时均价筛选
//			if (cmd.getAreaAveragePriceFrom() != null && areaAveragePrice < cmd.getAreaAveragePriceFrom().doubleValue()) {
//				continue;
//			}
//			if (cmd.getAreaAveragePriceTo() != null && areaAveragePrice > cmd.getAreaAveragePriceTo().doubleValue()) {
//				continue;
//			}
			ApartmentExportDataDTO dto = convertToApartmentExportDataDTO(address,livingStatus,totalRent);
			data.add(dto);
		}
		if (data != null && data.size() > 0) {
			String fileName = String.format("房源信息导出");
			ExcelUtils excelUtils = new ExcelUtils(response, fileName, "房源信息");
			String[] propertyNames = {"communityName","buildingName","apartmentFloor","apartmentName","livingStatus","areaSize","rentArea","freeArea","chargeArea","orientation","namespaceAddressType","namespaceAddressToken"};
			String[] titleNames = {"项目名称", "楼宇名称", "楼层名称", "房源", "状态", "建筑面积(㎡)","在租面积(㎡)", "可招租面积(㎡)", "收费面积(㎡)","朝向","第三方来源","第三方标识"};
			int[] titleSizes = {20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20};
			excelUtils.writeExcel(propertyNames, titleNames, titleSizes, data);
		} else {
			throw errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_NO_DATA,
					"no data");
		}
	}

	private ApartmentExportDataDTO convertToApartmentExportDataDTO(Address address, byte livingStatus, double totalRent) {
		ApartmentExportDataDTO dto = new ApartmentExportDataDTO();
		Community community = communityProvider.findCommunityById(address.getCommunityId());
		dto.setCommunityName(community.getName());
		dto.setBuildingName(address.getBuildingName());
		dto.setApartmentFloor(address.getApartmentFloor());
		dto.setApartmentName(address.getApartmentName());
		dto.setLivingStatus(AddressMappingStatus.fromCode(livingStatus).getDesc());
		dto.setAreaSize(address.getAreaSize());
		dto.setRentArea(address.getRentArea());
		dto.setFreeArea(address.getFreeArea());
		dto.setChargeArea(address.getChargeArea());
//		dto.setAreaAveragePrice(areaAveragePrice);
		dto.setOrientation(address.getOrientation());
		dto.setNamespaceAddressType(address.getNamespaceAddressType());
		dto.setNamespaceAddressToken(address.getNamespaceAddressToken());
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
	}

	@Override
	public void caculateAllCommunityArea() {
		List<NamespaceInfoDTO> namespaces = namespacesProvider.listNamespace();
		if (namespaces!=null && namespaces.size()>0) {
			for (NamespaceInfoDTO namespaceInfo : namespaces) {
				//排除0域空间的数据
				if (namespaceInfo.getId().intValue() == 0) {
					continue;
				}
				LOGGER.info("caculating community area progress starts, namespace_id = {},namespace_name={}", namespaceInfo.getId(),namespaceInfo.getName());
				long startTime = System.currentTimeMillis();

				List<Community> communities = communityProvider.findCommunitiesByNamespaceId(namespaceInfo.getId());
				if (communities!=null && communities.size()>0) {
					for (Community community : communities) {
						initCommunityData(community);
						List<Address> addresses = addressProvider.findActiveAddressByCommunityId(community.getId());
						for (Address address : addresses) {
							community.setAptCount(community.getAptCount() + 1);
							community.setAreaSize(community.getAreaSize() + (address.getAreaSize() != null ? address.getAreaSize() : 0.0));
							community.setRentArea(community.getRentArea() + (address.getRentArea() != null ? address.getRentArea() : 0.0));
							community.setChargeArea(community.getChargeArea() + (address.getChargeArea() != null ? address.getChargeArea() : 0.0));
							community.setFreeArea(community.getFreeArea() + (address.getFreeArea() != null ? address.getFreeArea() : 0.0));
							community.setSharedArea(community.getSharedArea() + (address.getSharedArea() != null ? address.getSharedArea() : 0.0));
							address.setCommunityName(community.getName());
							addressProvider.updateAddress(address);
						}
						communityProvider.updateCommunity(community);
					}
				}
				long endTime = System.currentTimeMillis();
				long timeCost = endTime - startTime;
				LOGGER.info("caculating community area progress ends, namespace_id = {},namespace_name={},time_cost:{}ms",
							namespaceInfo.getId(),namespaceInfo.getName(),timeCost);
			}
		}
	}

	@Override
	public void caculateAllBuildingArea() {
		List<NamespaceInfoDTO> namespaces = namespacesProvider.listNamespace();
		if (namespaces!=null && namespaces.size()>0) {
			for (NamespaceInfoDTO namespaceInfo : namespaces) {
				//排除0域空间的数据
				if (namespaceInfo.getId().intValue() == 0) {
					continue;
				}
				LOGGER.info("caculating building area progress starts, namespace_id = {},namespace_name={}", namespaceInfo.getId(),namespaceInfo.getName());
				long startTime = System.currentTimeMillis();

				List<Community> communities = communityProvider.findCommunitiesByNamespaceId(namespaceInfo.getId());
				if (communities!=null && communities.size()>0) {
					for (Community community : communities) {
						List<Building> buildings = communityProvider.findBuildingsByCommunityId(community.getId());
						if (buildings!=null && buildings.size()>0) {
							for (Building building : buildings) {
								initBuildingData(building);
								List<Address> addresses = addressProvider.findActiveAddressByBuildingNameAndCommunityId(building.getName(),community.getId());
								for (Address address : addresses) {
									building.setAreaSize(building.getAreaSize() + (address.getAreaSize() != null ? address.getAreaSize() : 0.0));
									building.setRentArea(building.getRentArea() + (address.getRentArea() != null ? address.getRentArea() : 0.0));
									building.setChargeArea(building.getChargeArea() + (address.getChargeArea() != null ? address.getChargeArea() : 0.0));
									building.setFreeArea(building.getFreeArea() + (address.getFreeArea() != null ? address.getFreeArea() : 0.0));
									building.setSharedArea(building.getSharedArea() + (address.getSharedArea() != null ? address.getSharedArea() : 0.0));
									address.setBuildingId(building.getId());
									addressProvider.updateAddress(address);
								}
								communityProvider.updateBuilding(building);
							}
						}
					}
				}
				long endTime = System.currentTimeMillis();
				long timeCost = endTime - startTime;
				LOGGER.info("caculating building area progress ends, namespace_id = {},namespace_name={},time_cost:{}ms",
							namespaceInfo.getId(),namespaceInfo.getName(),timeCost);
			}
		}
	}



	@Override
	public ListCommunitiesByOrgIdAndAppIdResponse listCommunitiesByOrgIdAndAppId(ListCommunitiesByOrgIdAndAppIdCommand cmd) {

		Integer namespaceId = UserContext.getCurrentNamespaceId();

		final List<Long> projectIds = new ArrayList<>();

		if(namespacesService.isStdNamespace(namespaceId)){
			//标准版
			List<ServiceModuleAppAuthorization> authorizations = serviceModuleAppAuthorizationService.listCommunityRelationOfOrgIdAndAppId(UserContext.getCurrentNamespaceId(), cmd.getOrgId(), cmd.getAppId());

			if(authorizations.size() > 0){
				authorizations.forEach(r -> projectIds.add(r.getProjectId()));
			}

		}else {
			//定制版

			List<OrganizationCommunity> organizationCommunities = organizationProvider.listOrganizationCommunities(cmd.getOrgId());

			if(organizationCommunities.size() > 0){
				organizationCommunities.forEach(r -> projectIds.add(r.getCommunityId()));
			}
		}


		List<Community> communities = communityProvider.listCommunities(namespaceId, new ListingLocator(), 1000000, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_COMMUNITIES.ID.in(projectIds));
				return query;
			}
		});

		List<ProjectDTO> dtos = new ArrayList<>();

		if(communities != null && communities.size() > 0){

			for (Community community: communities) {
				ProjectDTO dto = new ProjectDTO();
				dto.setProjectName(community.getName());
				dto.setProjectId(community.getId());
				dto.setCommunityType(community.getCommunityType());
				dto.setProjectType(com.everhomes.entity.EntityType.COMMUNITY.getCode());
				dtos.add(dto);
			}
		}

		ListCommunitiesByOrgIdAndAppIdResponse response = new ListCommunitiesByOrgIdAndAppIdResponse();
		response.setDtos(dtos);

		return response;
	}

	@Override
	public OrgDTO getOrgIdByCommunityId(GetOrgIdByCommunityIdCommand cmd) {
		//获取园区所属的管理公司id
		Long currentOrganizationId = communityProvider.getOrganizationIdByCommunityId(cmd.getCommunityId());
		OrgDTO dto = new OrgDTO();
		dto.setId(currentOrganizationId);
		return dto;
	}
	

	private void assetManagementPrivilegeCheck(Integer namespaceId, Long privilegeId, Long orgId, Long communityId) {
		userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), orgId, privilegeId, ServiceModuleConstants.ASSET_MANAGEMENT, null, null, null, communityId);
	}
	
	@Override
	public BuildingStatisticsForAppDTO getBuildingStatisticsForApp(GetBuildingStatisticsCommand cmd) {
		if(cmd.getBuildingId() == null){
			throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
					"Invalid buildingId parameter");
		}
		
		Building building = communityProvider.findBuildingById(cmd.getBuildingId());
		if (building == null || building.getStatus()==BuildingAdminStatus.INACTIVE.getCode()) {
			throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_BUILDING_NOT_EXIST,
					"building not exist.");
		}

		BuildingStatisticsForAppDTO dto = ConvertHelper.convert(building, BuildingStatisticsForAppDTO.class);
		dto.setBuildingId(building.getId());
		dto.setBuildingName(building.getName());

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
		
		if (dto.getAreaSize()!=null && dto.getAreaSize()!=0) {
			if (dto.getFreeArea()!=null) {
				BigDecimal freeRate = new BigDecimal(dto.getFreeArea()).divide(new BigDecimal(dto.getAreaSize()),4,RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
				dto.setFreeRate(freeRate);
			}else {
				dto.setFreeRate(BigDecimal.ZERO);
			}
		}
		
		ApartmentCountInBuildingDTO apartmentCountResult = countApartmentInBuilding(cmd.getBuildingId());
		dto.setTotalApartmentCount(apartmentCountResult.getTotalApartmentCount());
		dto.setDefaultApartmentCount(apartmentCountResult.getDefaultApartmentCount());
		dto.setFreeApartmentCount(apartmentCountResult.getFreeApartmentCount());
		dto.setLivingApartmentCount(apartmentCountResult.getLivingApartmentCount());
		dto.setOccupiedApartmentCount(apartmentCountResult.getOccupiedApartmentCount());
		dto.setRentApartmentCount(apartmentCountResult.getRentApartmentCount());
		dto.setSaledApartmentCount(apartmentCountResult.getSaledApartmentCount());
		dto.setUnsaleApartmentCount(apartmentCountResult.getUnsaleApartmentCount());
		dto.setSignedUpCount(apartmentCountResult.getSignedUpCount());
		dto.setWaitingRoomCount(apartmentCountResult.getWaitingRoomCount());
		
		return dto;
	}
	
	@Override
	public ApartmentCountInBuildingDTO countApartmentInBuilding(Long buildingId){
		if(buildingId == null){
			throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
					"Invalid buildingId parameter");
		}
		Building building = communityProvider.findBuildingById(buildingId);
		if (building == null || building.getStatus()==BuildingAdminStatus.INACTIVE.getCode()) {
			throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_BUILDING_NOT_EXIST,
					"building not exist.");
		}
		
		ApartmentCountInBuildingDTO result = new ApartmentCountInBuildingDTO();
		
		List<Address> addresses = addressProvider.findActiveApartmentsByBuildingId(buildingId);
		List<Long> addressIdList = addresses.stream().map(a->a.getId()).collect(Collectors.toList());
		Map<Long, CommunityAddressMapping> communityAddressMappingMap = propertyMgrProvider.mapAddressMappingByAddressIds(addressIdList);
		
		Map<Byte, Integer> resultMap = new HashMap<>();
		Integer totalApartmentCount = 0;
		resultMap.put(AddressMappingStatus.DEFAULT.getCode(), 0);
		resultMap.put(AddressMappingStatus.LIVING.getCode(), 0);
		resultMap.put(AddressMappingStatus.RENT.getCode(), 0);
		resultMap.put(AddressMappingStatus.FREE.getCode(), 0);
		resultMap.put(AddressMappingStatus.SALED.getCode(), 0);
		resultMap.put(AddressMappingStatus.UNSALE.getCode(), 0);
		resultMap.put(AddressMappingStatus.OCCUPIED.getCode(), 0);
		resultMap.put(AddressMappingStatus.SIGNEDUP.getCode(), 0);
		resultMap.put(AddressMappingStatus.WAITINGROOM.getCode(), 0);
		
		for(Address address : addresses){
			totalApartmentCount++;
			CommunityAddressMapping communityAddressMapping = communityAddressMappingMap.get(address.getId());
			if (communityAddressMapping != null) {
				resultMap.put(communityAddressMapping.getLivingStatus(), resultMap.get(communityAddressMapping.getLivingStatus()) + 1);
			}else {
				resultMap.put(AddressMappingStatus.LIVING.getCode(), resultMap.get(AddressMappingStatus.LIVING.getCode()) + 1);
			}
		}
		
		result.setTotalApartmentCount(totalApartmentCount);
		result.setFreeApartmentCount(resultMap.get(AddressMappingStatus.FREE.getCode()));
		result.setLivingApartmentCount(resultMap.get(AddressMappingStatus.LIVING.getCode()));
		result.setOccupiedApartmentCount(resultMap.get(AddressMappingStatus.OCCUPIED.getCode()));
		result.setRentApartmentCount(resultMap.get(AddressMappingStatus.RENT.getCode()));
		result.setSaledApartmentCount(resultMap.get(AddressMappingStatus.SALED.getCode()));
		result.setUnsaleApartmentCount(resultMap.get(AddressMappingStatus.UNSALE.getCode()));
		result.setDefaultApartmentCount(resultMap.get(AddressMappingStatus.DEFAULT.getCode()));
		result.setSignedUpCount(resultMap.get(AddressMappingStatus.SIGNEDUP.getCode()));
		result.setWaitingRoomCount(resultMap.get(AddressMappingStatus.WAITINGROOM.getCode()));
		
		return result;
	}


	@Override
	public ListBuildingsForAppResponse listBuildingsForApp(ListBuildingsForAppCommand cmd) {
		if(cmd.getCommunityId() == null){
			throw RuntimeErrorException.errorWith(PropertyErrorCode.SCOPE, PropertyErrorCode.ERROR_NULL_PARAMETER,
					"Invalid communityId parameter");
		}
		
		ListBuildingsForAppResponse response = new ListBuildingsForAppResponse();
		List<BuildingStatisticsForAppDTO> results = new ArrayList<>();
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
		List<Building> buildings = communityProvider.ListBuildingsByCommunityId(locator, pageSize+1 ,cmd.getCommunityId(), null, null);
		
		Long nextPageAnchor = null;
        if(buildings.size() > pageSize) {
        	buildings.remove(buildings.size() - 1);
            nextPageAnchor = buildings.get(buildings.size() - 1).getDefaultOrder();
        }
		response.setNextPageAnchor(nextPageAnchor);
		
		for (Building building : buildings) {
			GetBuildingStatisticsCommand cmd2 = new GetBuildingStatisticsCommand();
			cmd2.setBuildingId(building.getId());
			BuildingStatisticsForAppDTO dto = getBuildingStatisticsForApp(cmd2);
			
			dto.setPosterUri(building.getPosterUri());
			Long userId = UserContext.currentUserId();
			if (dto.getPosterUri() != null) {
				dto.setPosterUrl(contentServerService.parserUri(dto.getPosterUri(), EntityType.USER.getCode(), userId));
			}
			
			results.add(dto);
		}
		response.setResults(results);
		
		return response;
	} 


	@Override
	public ListCommunitiesForThirdPartyResponse listCommunitiesForThirdParty(ListCommunitiesForThirdPartyCommand cmd) {
		ListCommunitiesForThirdPartyResponse response = new ListCommunitiesForThirdPartyResponse();
		
		Long pageAnchor = cmd.getPageAnchor()!=null ? cmd.getPageAnchor() : 0L;
		Integer pageSize = cmd.getPageSize();
		if (pageSize == null) {
			pageSize = 10;
		}else if (pageSize > 1000) {
			pageSize = 1000;
		}
		
		Timestamp timestamp = null;
		if (cmd.getUpdateTime() != null) {
			timestamp = new Timestamp(cmd.getUpdateTime());
		}
		
		Integer currentNamespaceId = UserContext.getCurrentNamespaceId();
		
		List<com.everhomes.rest.openapi.CommunityDTO> results = communityProvider.listCommunitiesForThirdParty(currentNamespaceId,cmd.getCommunityId(),pageAnchor,pageSize+1,timestamp);
		
		if (results!=null && results.size() > pageSize) {
			results.remove(results.size()-1);
			Long nextPageAnchor = pageAnchor + pageSize.longValue();
			response.setNextPageAnchor(nextPageAnchor);
		}
		
		response.setResults(results);
		
		return response;
	}


	@Override

	public ImportFileTaskDTO importBuildingData(Long communityId, MultipartFile file) {
		// TODO Auto-generated method stub
		return null;
	}	
	
	public ListBuildingsForThirdPartyResponse listBuildingsForThirdParty(ListBuildingsForThirdPartyCommand cmd) {
		ListBuildingsForThirdPartyResponse response = new ListBuildingsForThirdPartyResponse();
		
		Long pageAnchor = cmd.getPageAnchor()!=null ? cmd.getPageAnchor() : 0L;
		Integer pageSize = cmd.getPageSize();
		if (pageSize == null) {
			pageSize = 10;
		}else if (pageSize > 1000) {
			pageSize = 1000;
		}
		
		Timestamp timestamp = null;
		if (cmd.getUpdateTime() != null) {
			timestamp = new Timestamp(cmd.getUpdateTime());
		}
		
		Integer currentNamespaceId = UserContext.getCurrentNamespaceId();
		
		List<com.everhomes.rest.openapi.BuildingDTO> results = communityProvider.listBuildingsForThirdParty(currentNamespaceId,cmd.getCommunityId(),pageAnchor,pageSize+1,timestamp);
		
		if (results!=null && results.size() > pageSize) {
			results.remove(results.size()-1);
			Long nextPageAnchor = pageAnchor + pageSize.longValue();
			response.setNextPageAnchor(nextPageAnchor);
		}
		
		for (com.everhomes.rest.openapi.BuildingDTO buildingDTO : results) {
			if (buildingDTO.getCommunityId() != null) {
				Community community = communityProvider.findCommunityById(buildingDTO.getCommunityId());
				if (community != null) {
					buildingDTO.setCommunityName(community.getName());
				}
			}
		}
		
		response.setResults(results);
		
		return response;
	}

	@Override
	public ListAddressesForThirdPartyResponse listAddressesForThirdParty(ListAddressesForThirdPartyCommand cmd) {
		ListAddressesForThirdPartyResponse response = new ListAddressesForThirdPartyResponse();
		
		Long pageAnchor = cmd.getPageAnchor()!=null ? cmd.getPageAnchor() : 0L;
		Integer pageSize = cmd.getPageSize();
		if (pageSize == null) {
			pageSize = 10;
		}else if (pageSize > 1000) {
			pageSize = 1000;
		}
		
		Timestamp timestamp = null;
		if (cmd.getUpdateTime() != null) {
			timestamp = new Timestamp(cmd.getUpdateTime());
		}
		
		Integer currentNamespaceId = UserContext.getCurrentNamespaceId();
		
		List<com.everhomes.rest.openapi.ApartmentDTO> results = communityProvider.listAddressesForThirdParty(currentNamespaceId,cmd.getCommunityId(),cmd.getBuildingId(),pageAnchor,pageSize+1,timestamp);
		
		if (results!=null && results.size() > pageSize) {
			results.remove(results.size()-1);
			Long nextPageAnchor = pageAnchor + pageSize.longValue();
			response.setNextPageAnchor(nextPageAnchor);
		}
		
		response.setResults(results);
		
		return response;
	}

}

