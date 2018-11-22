// @formatter:off
package com.everhomes.organization;

import com.everhomes.acl.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.aclink.DoorAccessService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.archives.ArchivesProvider;
import com.everhomes.archives.ArchivesService;
import com.everhomes.archives.ArchivesUtil;
import com.everhomes.asset.AssetService;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.bus.LocalEventBus;
import com.everhomes.bus.LocalEventContext;
import com.everhomes.bus.SystemEvent;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.common.IdentifierTypeEnum;
import com.everhomes.community.Building;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.CommunityService;
import com.everhomes.community.ResourceCategory;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.controller.XssCleaner;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.customer.CustomerEntryInfo;
import com.everhomes.customer.CustomerService;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.discover.RestReturn;
import com.everhomes.enterprise.EnterpriseCommunityMap;
import com.everhomes.enterprise.EnterpriseProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.FamilyProvider;
import com.everhomes.family.FamilyService;
import com.everhomes.forum.Forum;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.ForumService;
import com.everhomes.forum.Post;
import com.everhomes.group.*;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.mail.MailHandler;
import com.everhomes.menu.Target;
import com.everhomes.messaging.MessagingService;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleAssignment;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.namespace.NamespacesService;
import com.everhomes.openapi.AppNamespaceMapping;
import com.everhomes.openapi.AppNamespaceMappingProvider;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractBuildingMappingProvider;
import com.everhomes.organization.pm.CommunityPmContact;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.portal.PortalService;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rentalv2.RentalNotificationTemplateCode;
import com.everhomes.rest.acl.DeleteServiceModuleAdministratorsCommand;
import com.everhomes.rest.acl.ListAppAuthorizationsByOrganizatioinIdCommand;
import com.everhomes.rest.acl.ListAppAuthorizationsByOwnerIdResponse;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.acl.PrivilegeServiceErrorCode;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.acl.RoleConstants;
import com.everhomes.rest.acl.admin.AclRoleAssignmentsDTO;
import com.everhomes.rest.acl.admin.CreateOrganizationAdminCommand;
import com.everhomes.rest.acl.admin.DeleteOrganizationAdminCommand;
import com.everhomes.rest.acl.admin.RoleDTO;
import com.everhomes.rest.aclink.DeleteAuthByOwnerCommand;
import com.everhomes.rest.address.AddressAdminStatus;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.address.CreateOfficeSiteCommand;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.archives.AddArchivesContactCommand;
import com.everhomes.rest.archives.ArchivesContactDTO;
import com.everhomes.rest.archives.ListArchivesContactsCommand;
import com.everhomes.rest.archives.ListArchivesContactsResponse;
import com.everhomes.rest.archives.TransferArchivesEmployeesCommand;
import com.everhomes.rest.asset.AssetTargetType;
import com.everhomes.rest.business.listUsersOfEnterpriseCommand;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.comment.AddCommentCommand;
import com.everhomes.rest.common.*;
import com.everhomes.rest.community.CommunityFetchType;
import com.everhomes.rest.community.CommunityServiceErrorCode;
import com.everhomes.rest.community.admin.OperateType;
import com.everhomes.rest.community.ListCommunitiesByOrgIdAndAppIdCommand;
import com.everhomes.rest.community.ListCommunitiesByOrgIdAndAppIdResponse;
import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.contract.ContractDTO;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.customer.DeleteEnterpriseCustomerCommand;
import com.everhomes.rest.customer.NamespaceCustomerType;
import com.everhomes.rest.enterprise.*;
import com.everhomes.rest.equipment.AdminFlag;
import com.everhomes.rest.family.LeaveFamilyCommand;
import com.everhomes.rest.family.ParamType;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.forum.*;
import com.everhomes.rest.group.*;
import com.everhomes.rest.investment.CustomerLevelType;
import com.everhomes.rest.investment.InvitedCustomerType;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.launchpad.ItemKind;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.module.ListUserRelatedProjectByModuleCommand;
import com.everhomes.rest.module.Project;
import com.everhomes.rest.namespace.ListCommunityByNamespaceCommandResponse;
import com.everhomes.rest.order.OwnerType;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.organization.pm.AddPmBuildingCommand;
import com.everhomes.rest.organization.pm.ChildrenOrganizationJobLevelDTO;
import com.everhomes.rest.organization.pm.DeletePmCommunityCommand;
import com.everhomes.rest.organization.pm.ListPmBuildingCommand;
import com.everhomes.rest.organization.pm.ListPmManagementsCommand;
import com.everhomes.rest.organization.pm.PmBuildingDTO;
import com.everhomes.rest.organization.pm.PmManagementsDTO;
import com.everhomes.rest.organization.pm.PmManagementsResponse;
import com.everhomes.rest.organization.pm.PmMemberGroup;
import com.everhomes.rest.organization.pm.PmMemberStatus;
import com.everhomes.rest.organization.pm.PropertyServiceErrorCode;
import com.everhomes.rest.organization.pm.UnassignedBuildingDTO;
import com.everhomes.rest.organization.pm.UpdateOrganizationMemberByIdsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.portal.ServiceAllianceInstanceConfig;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.region.RegionScope;
import com.everhomes.rest.search.GroupQueryResult;
import com.everhomes.rest.search.OrganizationQueryResult;
import com.everhomes.rest.servicemoduleapp.ServiceModuleAppAuthorizationDTO;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.techpark.company.ContactType;
import com.everhomes.rest.techpark.expansion.EnterpriseDetailDTO;
import com.everhomes.rest.techpark.expansion.ListEnterpriseDetailResponse;
import com.everhomes.rest.ui.privilege.EntrancePrivilege;
import com.everhomes.rest.ui.privilege.GetEntranceByPrivilegeCommand;
import com.everhomes.rest.ui.privilege.GetEntranceByPrivilegeResponse;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.rest.user.UserGender;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.UserStatus;
import com.everhomes.rest.user.UserTokenCommand;
import com.everhomes.rest.user.UserTokenCommandResponse;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.search.PostAdminQueryFilter;
import com.everhomes.search.PostSearcher;
import com.everhomes.search.UserWithoutConfAccountSearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhCustomerEntryInfos;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMembers;
import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.server.schema.tables.records.EhOrganizationsRecord;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.sms.SmsProvider;
import com.everhomes.uniongroup.UniongroupService;
import com.everhomes.user.EncryptionUtils;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserGroup;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProfile;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.user.sdk.SdkUserService;
import com.everhomes.userOrganization.UserOrganizationProvider;
import com.everhomes.userOrganization.UserOrganizations;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.PinYinHelper;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import com.everhomes.util.WebTokenGenerator;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import com.mysql.fabric.xmlrpc.base.Array;


import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.common.collect.Lists;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.util.derby.sys.Sys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import javassist.bytecode.stackmap.BasicBlock.Catch;

import javax.servlet.http.HttpServletResponse;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;


import static com.everhomes.util.RuntimeErrorException.errorWith;

@Component
public class OrganizationServiceImpl implements OrganizationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationServiceImpl.class);
    private static final String ASSIGN_TASK_AUTO_COMMENT = "assign.task.auto.comment";
    private static final String ASSIGN_TASK_AUTO_SMS = "assign.task.auto.sms";


    ExecutorService pool = Executors.newFixedThreadPool(3);
    @Autowired
    private DbProvider dbProvider;


    @Autowired
    private AppNamespaceMappingProvider appNamespaceMappingProvider;
    
    @Autowired
    private ContractBuildingMappingProvider contractBuildingMappingProvider;

    @Autowired
    private UniongroupService uniongroupService;

    @Autowired
    private NamespaceProvider namespaceProvider;

    @Autowired
    LocaleStringService localeStringService;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private PropertyMgrProvider propertyMgrProvider;

    @Autowired
    private PropertyMgrService propertyMgrService;

    @Autowired
    private ForumService forumService;

    @Autowired
    private ForumProvider forumProvider;

    @Autowired
    private SmsProvider smsProvider;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private FamilyProvider familyProvider;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private UserActivityProvider userActivityProvider;

    @Autowired
    private AddressProvider addressProvider;

    @Autowired
    private RegionProvider regionProvider;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private CategoryProvider categoryProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private PostSearcher postSearcher;

    @Autowired
    private AclProvider aclProvider;

    @Autowired
    private OrganizationSearcher organizationSearcher;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private GroupProvider groupProvider;

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @Autowired
    private FamilyService familyService;

    @Autowired
    private UserWithoutConfAccountSearcher userSearcher;

    @Autowired
    private ServiceModuleProvider serviceModuleProvider;

    @Autowired
    private DoorAccessService doorAccessService;

    @Autowired
    private ImportFileService importFileService;

    @Autowired
    private AuthorizationProvider authorizationProvider;

    @Autowired
    private UserOrganizationProvider userOrganizationProvider;

    @Autowired
    private ArchivesService archivesService;

    @Autowired
    private ArchivesProvider archivesProvider;
    @Autowired
    private EnterpriseCustomerProvider enterpriseCustomerProvider;

    @Autowired
    private EnterpriseCustomerSearcher enterpriseCustomerSearcher;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PortalService portalService;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private EnterpriseCustomerProvider customerProvider;

    @Autowired
    private EnterpriseProvider enterpriseProvider;

    @Autowired
    private ServiceModuleAppService serviceModuleAppService;

    @Autowired
    private ServiceModuleAppAuthorizationService serviceModuleAppAuthorizationService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private ServiceModuleService serviceModuleService;
    @Autowired
    private CommunityService communityService;

    @Autowired
    private NamespacesService namespacesService;

    @Autowired
    private SdkUserService sdkUserService;

    private int getPageCount(int totalCount, int pageSize) {
        int pageCount = totalCount / pageSize;

        if (totalCount % pageSize != 0) {
            pageCount++;
        }
        return pageCount;
    }

    @Override
    public OrganizationDTO createChildrenOrganization(CreateOrganizationCommand cmd) {
        User user = UserContext.current().getUser();

        //TODO 校验权限有问题，里面使用了组织架构的内容。敢哥说先注释掉。
        //权限校验
        //checkOrganizationpPivilege(cmd.getParentId(),PrivilegeConstants.CREATE_DEPARTMENT);

        if (null == OrganizationGroupType.fromCode(cmd.getGroupType())) {
            LOGGER.error("organization group type error. cmd = {}", cmd);
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ASSIGNMENT_EXISTS,
                    "organization group type error");
        }
        Organization organization = ConvertHelper.convert(cmd, Organization.class);
        Organization parOrg = this.checkOrganization(cmd.getParentId());

        //同级重名校验
        Organization down_organization = organizationProvider.findOrganizationByParentAndName(parOrg.getId(),cmd.getName());
        if(down_organization != null){
            OrganizationDTO orgDto_error = new OrganizationDTO();
            orgDto_error.setErrorCode(OrganizationServiceErrorCode.ERROR_DEPARTMENT_EXISTS);
            LOGGER.error("name repeat, cmd = {}", cmd);
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_DEPARTMENT_EXISTS,
                    "name repeat");
        }

        //default show navi, add by sfyan 20160505
        if (null == cmd.getNaviFlag()) {
            cmd.setNaviFlag(OrganizationNaviFlag.SHOW_NAVI.getCode());
        }
        organization.setShowFlag(cmd.getNaviFlag());
        organization.setPath(parOrg.getPath());
        organization.setLevel(parOrg.getLevel() + 1);
        organization.setOrganizationType(parOrg.getOrganizationType());
        organization.setStatus(OrganizationStatus.ACTIVE.getCode());
        organization.setNamespaceId(parOrg.getNamespaceId());
        organization.setCreatorUid(user.getId());
        organization.setOrder(0);

        Organization org = dbProvider.execute((TransactionStatus status) -> {

            Long directlyEnterpriseId = parOrg.getDirectlyEnterpriseId();
            if (OrganizationGroupType.fromCode(parOrg.getGroupType()) == OrganizationGroupType.ENTERPRISE) {
                directlyEnterpriseId = parOrg.getId();
            }

            if (OrganizationGroupType.fromCode(organization.getGroupType()) == OrganizationGroupType.ENTERPRISE) {
                this.createChildrenEnterprise(organization, cmd.getAddress(), cmd.getAddManagerMemberIds(), cmd.getDelManagerMemberIds());
            } else if (OrganizationGroupType.fromCode(organization.getGroupType()) == OrganizationGroupType.JOB_POSITION) {
                organization.setDirectlyEnterpriseId(directlyEnterpriseId);
                organizationProvider.createOrganization(organization);
                //更新通用岗位
                this.updateOrganizationJobPositionMap(organization, cmd.getJobPositionIds());

                //更新组人员
                this.batchUpdateOrganizationMember(cmd.getAddMemberIds(), cmd.getDelMemberIds(), organization);
            } else if (OrganizationGroupType.fromCode(organization.getGroupType()) == OrganizationGroupType.JOB_LEVEL) {
                organization.setDirectlyEnterpriseId(directlyEnterpriseId);
                // 增加职级大小
                organization.setSize(cmd.getSize());
                organizationProvider.createOrganization(organization);
                //更新组人员
                this.batchUpdateOrganizationMember(cmd.getAddMemberIds(), cmd.getDelMemberIds(), organization);
            } else if (OrganizationGroupType.fromCode(organization.getGroupType()) == OrganizationGroupType.DEPARTMENT || OrganizationGroupType.fromCode(organization.getGroupType()) == OrganizationGroupType.GROUP) {
                organization.setDirectlyEnterpriseId(directlyEnterpriseId);

                organizationProvider.createOrganization(organization);
                // 创建经理群组
                Organization managerGroup = this.createManagerGroup(organization, organization.getDirectlyEnterpriseId());

                // 更新经理群组人员
                this.batchUpdateOrganizationMember(cmd.getAddManagerMemberIds(), cmd.getDelManagerMemberIds(), managerGroup);
            }

            if (null != cmd.getCommunityId()) {
                updateCurrentOrganziationCommunityReqeust(user.getId(), organization.getId(), cmd.getCommunityId());
            }
            return organization;
        });

        return ConvertHelper.convert(org, OrganizationDTO.class);
    }

    /**
     * 更新机构岗位和通用岗位的关系
     *
     * @param organization
     * @param jobPositionIds
     */
    private void updateOrganizationJobPositionMap(Organization organization, List<Long> jobPositionIds) {
        if (null == jobPositionIds) {
            LOGGER.debug("organization job position is null");
            return;
        }

        // 删除原有的
        List<OrganizationJobPositionMap> jobPositionMaps = organizationProvider.listOrganizationJobPositionMaps(organization.getId());
        for (OrganizationJobPositionMap jobPositionMap : jobPositionMaps) {
            organizationProvider.deleteOrganizationJobPositionMapById(jobPositionMap.getId());
        }

        // 添加通用岗位
        for (Long jobPositionId : jobPositionIds) {
            OrganizationJobPositionMap jobPositionMap = new OrganizationJobPositionMap();
            jobPositionMap.setOrganizationId(organization.getId());
            jobPositionMap.setNamespaceId(organization.getNamespaceId());
            jobPositionMap.setJobPositionId(jobPositionId);
            jobPositionMap.setCreatorUid(UserContext.current().getUser().getId());
            organizationProvider.createOrganizationJobPositionMap(jobPositionMap);
        }
    }

    /**
     * 创建子公司
     *
     * @param organization
     * @param address
     * @param addManagerMemberIds
     * @param delManagerMemberIds
     */
    private void createChildrenEnterprise(Organization organization, String address, List<Long> addManagerMemberIds, List<Long> delManagerMemberIds) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        organization.setDirectlyEnterpriseId(0L);
        //添加子公司的时候默认给公司添加圈 sfyan 20160706
        Group group = new Group();
        group.setName(organization.getName());
        group.setDisplayName(organization.getName());
        group.setEnterpriseAddress(address);

        group.setDescription(organization.getName());
        group.setStatus(OrganizationStatus.ACTIVE.getCode());

        group.setCreatorUid(UserContext.current().getUser().getId());

        group.setNamespaceId(namespaceId);

        group.setDiscriminator(GroupDiscriminator.ENTERPRISE.getCode());
        groupProvider.createGroup(group);

        //创建公司
        organization.setGroupId(group.getId());
        organizationProvider.createOrganization(organization);

        // 创建公司详情
        OrganizationDetail enterprise = new OrganizationDetail();
        enterprise.setOrganizationId(organization.getId());
        enterprise.setAddress(address);
        enterprise.setCreateTime(organization.getCreateTime());
        organizationProvider.createOrganizationDetail(enterprise);

        // 创建经理群组
        Organization managerGroup = this.createManagerGroup(organization, organization.getId());

        // 更新经理群组人员
        this.batchUpdateOrganizationMember(addManagerMemberIds, delManagerMemberIds, managerGroup);

        // 获取父亲公司所入驻的园区，由于创建子公司时在界面上没有指定其所在园区，会导致服务市场上拿不到数据，
        // 故需要补充其所在园区 by lqs 20161101
//		Long communityId = getOrganizationActiveCommunityId(organization.getParentId());
//		if(communityId != null) {
//			createActiveOrganizationCommunityRequest(UserContext.current().getUser().getId(), organization.getId(), communityId);
//		} else {
//			LOGGER.error("Community not found, organizationId={}, parentOrgId={}", organization.getId(), organization.getParentId());
//		}
    }

    /**
     * 更新组人员
     *
     * @param addDetailIds
     * @param delDetailIds
     * @param organization
     */
    // modify by lei.lv 为了适配新后台，需要改成用detailId进行创建的形式

    private void batchUpdateOrganizationMember(List<Long> addDetailIds, List<Long> delDetailIds, Organization organization) {
        if (null != delDetailIds) {
            organizationProvider.deleteOrganizationMembersByGroupTypeWithDetailIds(UserContext.getCurrentNamespaceId(), delDetailIds, organization.getGroupType());
//            for (Long memberId : delMemberIds) {
//                organizationProvider.deleteOrganizationMemberById(memberId);
//            }
        } else {
            LOGGER.debug("delDetailIds is null");
        }

//        if (null != addMemberIds) {
//            for (Long memberId : addMemberIds) {
//                OrganizationMember member = organizationProvider.findOrganizationMemberById(memberId);
//                if (null != member) {
//                    OrganizationMember organizationMember = organizationProvider.findOrganizationMemberByOrgIdAndToken(member.getContactToken(), organization.getId());
//                    if (null == organizationMember) {
//                        member.setOrganizationId(organization.getId());
//                        member.setGroupType(organization.getGroupType());
//                        member.setGroupPath(organization.getPath());
//                        organizationProvider.createOrganizationMember(member);
//                    } else {
//                        LOGGER.debug("organization member already existing. organizationId = {}, contactToken = {}", organizationMember.getOrganizationId(), organizationMember.getContactToken());
//                    }
//                }
//            }
        Long enterPriseId = getTopEnterpriserIdOfOrganization(organization.getId());
        if (null != addDetailIds) {
            for (Long detailId : addDetailIds) {
                OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
                if(detail != null){
                    OrganizationMember organizationMember = organizationProvider.findOrganizationMemberByOrgIdAndToken(detail.getContactToken(), organization.getId());
                    if (null == organizationMember) {
                        OrganizationMember enterPriseMember = organizationProvider.findOrganizationMemberByOrgIdAndToken(detail.getContactToken(), enterPriseId);
                        enterPriseMember.setOrganizationId(organization.getId());
                        enterPriseMember.setGroupType(organization.getGroupType());
                        enterPriseMember.setGroupPath(organization.getPath());
                        enterPriseMember.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
                        organizationProvider.createOrganizationMember(enterPriseMember);
                    }
                }
            }
        } else {
            LOGGER.debug("add members is null");
        }

    }
//SDFSDF
    /**
     * 创建经理组
     *
     * @param organization
     * @param enterpriseId
     * @return
     */
    private Organization createManagerGroup(Organization organization, Long enterpriseId) {
        Organization managerGroup = null;
        List<String> groupTypes = new ArrayList<>();
        groupTypes.add(OrganizationGroupType.MANAGER.getCode());
        List<Organization> managerGroups = organizationProvider.listOrganizationByGroupTypes(organization.getId(), groupTypes);
        if (0 < managerGroups.size()) {
            managerGroup = managerGroups.get(0);
        } else {
            managerGroup = new Organization();
            managerGroup.setNamespaceId(organization.getNamespaceId());
            managerGroup.setName(organization.getName() + "经理组");
            managerGroup.setGroupType(OrganizationGroupType.MANAGER.getCode());
            managerGroup.setDirectlyEnterpriseId(enterpriseId);
            managerGroup.setParentId(organization.getId());
            managerGroup.setShowFlag(organization.getShowFlag());
            managerGroup.setPath(organization.getPath());
            managerGroup.setLevel(organization.getLevel() + 1);
            managerGroup.setOrganizationType(organization.getOrganizationType());
            managerGroup.setStatus(OrganizationStatus.ACTIVE.getCode());
            organizationProvider.createOrganization(managerGroup);
        }
        return managerGroup;
    }

    @Override
    public void setAclRoleAssignmentRole(
            SetAclRoleAssignmentCommand cmd, EntityType entityType) {
        RoleAssignment roleAssignment = new RoleAssignment();
        List<RoleAssignment> roleAssignments = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), entityType.getCode(), cmd.getTargetId());

        //Set roles for no, add by sfyan 20160504
        if (null == cmd.getRoleId()) {
            if (null != roleAssignments && 0 < roleAssignments.size()) {
                for (RoleAssignment assignment : roleAssignments) {
                    aclProvider.deleteRoleAssignment(assignment.getId());
                }
            }
            return;
        }


        if (null != roleAssignments && 0 < roleAssignments.size()) {
            for (RoleAssignment assignment : roleAssignments) {
                if (assignment.getRoleId().equals(cmd.getRoleId())) {
                    return;
                }
            }
        }

        dbProvider.execute((TransactionStatus status) -> {
            roleAssignment.setRoleId(cmd.getRoleId());
            roleAssignment.setOwnerType(EntityType.ORGANIZATIONS.getCode());
            roleAssignment.setOwnerId(cmd.getOrganizationId());
            roleAssignment.setTargetType(entityType.getCode());
            roleAssignment.setTargetId(cmd.getTargetId());
            roleAssignment.setCreatorUid(UserContext.current().getUser().getId());
            aclProvider.createRoleAssignment(roleAssignment);
            return null;
        });
    }

//SDFD


    @Override
    public void updateChildrenOrganization(UpdateOrganizationsCommand cmd) {
        if(cmd.getJobPositionIds() == null || cmd.getJobPositionIds().size() == 0){
            checkOrganizationPrivilege(cmd.getId(),PrivilegeConstants.MODIFY_DEPARTMENT);//修改部门校验
        }else{
            checkOrganizationPrivilege(cmd.getParentId(),PrivilegeConstants.MODIFY_DEPARTMENT_JOB_POSITION);//修改部门岗位校验
        }

        User user = UserContext.current().getUser();

        //先判断，后台管理员才能创建。状态直接设为正常

        if (null == cmd.getNaviFlag()) {
            cmd.setNaviFlag((byte) 1);
        }

        Organization modifyOrg = this.checkOrganization(cmd.getId());

        modifyOrg.setShowFlag(cmd.getNaviFlag());

        //name check
        if(!StringUtils.isEmpty(cmd.getName())){
            //同级重名校验
            Organization down_organization = organizationProvider.findOrganizationByParentAndName(modifyOrg.getParentId(), cmd.getName());
            if(down_organization != null && !down_organization.getId().equals(cmd.getId())){
                OrganizationDTO orgDto_error = new OrganizationDTO();
                orgDto_error.setErrorCode(OrganizationServiceErrorCode.ERROR_DEPARTMENT_EXISTS);
                LOGGER.error("name repeat, cmd = {}", cmd);
                throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_DEPARTMENT_EXISTS,
                        "name repeat");
            }
            modifyOrg.setName(cmd.getName());
        }

        dbProvider.execute((TransactionStatus status) -> {
            if (OrganizationGroupType.fromCode(modifyOrg.getGroupType()) == OrganizationGroupType.ENTERPRISE) {
                OrganizationDetail enterprise = organizationProvider.findOrganizationDetailByOrganizationId(modifyOrg.getId());
                if (null != enterprise) {
                    enterprise.setAddress(cmd.getAddress());
                    organizationProvider.updateOrganizationDetail(enterprise);
                }
                //如果是enterprise，要判断是否有直属部门，要同时更新直属部门的名字
                Organization underOrg = this.organizationProvider.findUnderOrganizationByParentOrgId(modifyOrg.getId());
                if (underOrg != null) {
                    underOrg.setName(cmd.getName());
                    this.organizationProvider.updateOrganization(underOrg);
                }

                // 创建经理群组
                Organization managerGroup = this.createManagerGroup(modifyOrg, modifyOrg.getId());

                // 更新经理群组人员
                this.batchUpdateOrganizationMember(cmd.getAddManagerMemberIds(), cmd.getDelManagerMemberIds(), managerGroup);

            } else if (OrganizationGroupType.fromCode(modifyOrg.getGroupType()) == OrganizationGroupType.JOB_POSITION) {

                //更新通用岗位
                this.updateOrganizationJobPositionMap(modifyOrg, cmd.getJobPositionIds());

                //更新组人员
                this.batchUpdateOrganizationMember(cmd.getAddMemberIds(), cmd.getDelMemberIds(), modifyOrg);
            } else if (OrganizationGroupType.fromCode(modifyOrg.getGroupType()) == OrganizationGroupType.JOB_LEVEL) {
                // 增加职级大小
                modifyOrg.setSize(cmd.getSize());
                //更新组人员
                this.batchUpdateOrganizationMember(cmd.getAddMemberIds(), cmd.getDelMemberIds(), modifyOrg);
            } else if (OrganizationGroupType.fromCode(modifyOrg.getGroupType()) == OrganizationGroupType.DEPARTMENT || OrganizationGroupType.fromCode(modifyOrg.getGroupType()) == OrganizationGroupType.GROUP) {
                // 创建经理群组
                Organization managerGroup = this.createManagerGroup(modifyOrg, modifyOrg.getDirectlyEnterpriseId());
                // 更新经理群组人员
                this.batchUpdateOrganizationMember(cmd.getAddManagerMemberIds(), cmd.getDelManagerMemberIds(), managerGroup);
            }

            modifyOrg.setOperatorUid(user.getId());
            organizationProvider.updateOrganization(modifyOrg);

            if (null != cmd.getCommunityId()) {
                if (OrganizationCommunityScopeType.CURRENT == OrganizationCommunityScopeType.fromCode(cmd.getScopeType())) {
                    //修改当前节点
                    updateCurrentOrganziationCommunityReqeust(user.getId(), modifyOrg.getId(), cmd.getCommunityId());
                } else if (OrganizationCommunityScopeType.CURRENT_CHILD == OrganizationCommunityScopeType.fromCode(cmd.getScopeType())) {
                    //修改当前节点
                    updateCurrentOrganziationCommunityReqeust(user.getId(), modifyOrg.getId(), cmd.getCommunityId());
                    //修改所有子节点
                    updateChildOrganizationCommunityRequest(user.getId(), modifyOrg.getPath(), cmd.getCommunityId());
                } else if (OrganizationCommunityScopeType.CURRENT_LEVEL_CHILD == OrganizationCommunityScopeType.fromCode(cmd.getScopeType())) {
                    //修改所有子节点
                    updateChildOrganizationCommunityRequest(user.getId(), modifyOrg.getPath(), cmd.getCommunityId());

                    //如果修改的是根节点，则不需要修改同级只需要修改当前节点
                    if (0L != modifyOrg.getParentId()) {
                        //修改同级节点包括当前节点
                        updateLevenOrganizationCommunityRequest(user.getId(), modifyOrg.getParentId(), cmd.getCommunityId());
                    } else {
                        //修改当前节点
                        updateCurrentOrganziationCommunityReqeust(user.getId(), modifyOrg.getId(), cmd.getCommunityId());
                    }
                }
            } else {
                LOGGER.warn("communityId is null");
            }

            return modifyOrg;
        });
    }


    private void deleteCurrentOrganizationCommunityReqeust(Long operatorUid, Long orgId) {
        updateCurrentOrganziationCommunityReqeust(operatorUid, orgId, null);
    }

    private void updateCurrentOrganziationCommunityReqeust(Long operatorUid, Long orgId, Long communityId) {
        OrganizationCommunityRequest request = organizationProvider.getOrganizationCommunityRequestByOrganizationId(orgId);
        if (null != request) {
            request.setOperatorUid(operatorUid);
            request.setMemberStatus(OrganizationCommunityRequestStatus.INACTIVE.getCode());
            organizationProvider.updateOrganizationCommunityRequest(request);
        }

        if (null != communityId) {
            createActiveOrganizationCommunityRequest(operatorUid, orgId, communityId);
        }
    }

    private void updateChildOrganizationCommunityRequest(Long operatorUid, String path, Long communityId) {
        List<String> groupTypes = new ArrayList<>();
        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
        groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
        groupTypes.add(OrganizationGroupType.GROUP.getCode());
        List<Organization> orgs = organizationProvider.listOrganizationByGroupTypes(path + "/%", groupTypes);
        for (Organization org : orgs) {
            updateCurrentOrganziationCommunityReqeust(operatorUid, org.getId(), communityId);
        }
    }

    private void updateLevenOrganizationCommunityRequest(Long operatorUid, Long parentId, Long communityId) {
        List<String> groupTypes = new ArrayList<>();
        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
        groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
        groupTypes.add(OrganizationGroupType.GROUP.getCode());
        List<Organization> orgs = organizationProvider.listOrganizationByGroupTypes(parentId, groupTypes);
        for (Organization org : orgs) {
            updateCurrentOrganziationCommunityReqeust(operatorUid, org.getId(), communityId);
        }
    }

    @Override
    public ListEnterprisesCommandResponse searchEnterprise(SearchOrganizationCommand cmd) {
        ListEnterprisesCommandResponse resp = new ListEnterprisesCommandResponse();
        GroupQueryResult rlt = this.organizationSearcher.query(cmd);
        resp.setNextPageAnchor(rlt.getPageAnchor());
        List<OrganizationDetailDTO> dtos = new ArrayList<OrganizationDetailDTO>();
        for (Long id : rlt.getIds()) {
            OrganizationDetailDTO detail = toOrganizationDetailDTO(id, false);
            if(detail != null) {
                dtos.add(detail);
            }

        }
        dtos = dtos.stream().filter(r->r != null).collect(Collectors.toList());
        LOGGER.debug("searchEnterprise result = {}", dtos.toString());
        resp.setDtos(dtos);
        return resp;
    }

    private OrganizationDetailDTO toOrganizationDetailDTO(Long id, Boolean flag) {
        Long userId = UserContext.current().getUser().getId();

        Organization organization = organizationProvider.findOrganizationById(id);
        OrganizationDetail org = organizationProvider.findOrganizationDetailByOrganizationId(id);
        if (null == organization) {
            LOGGER.debug("organization is null, id = " + id);
            return null;
        } else if (OrganizationGroupType.fromCode(organization.getGroupType()) != OrganizationGroupType.ENTERPRISE) {
            LOGGER.debug("organization not is enterprise, id = " + id);
            return null;
        } else if (organization.getParentId() != 0L) {
            LOGGER.debug("organization is children organization, id = " + id);
            return null;
        }

        OrganizationDTO organizationDTO = processOrganizationCommunity(ConvertHelper.convert(organization, OrganizationDTO.class));

        if (null == org) {
            org = new OrganizationDetail();
            org.setOrganizationId(organization.getId());
        }

        OrganizationDetailDTO dto = ConvertHelper.convert(org, OrganizationDetailDTO.class);
        //modify by dengs,20170512,将经纬度转换成 OrganizationDetailDTO 里面的类型，不改动dto，暂时不影响客户端。后面考虑将dto的经纬度改成Double
        if (null != org.getLatitude())
            dto.setLatitude(org.getLatitude().toString());
        if (null != org.getLongitude())
            dto.setLongitude(org.getLongitude().toString());
        //end
        dto.setUnifiedSocialCreditCode(organization.getUnifiedSocialCreditCode());
        dto.setWebsite(organization.getWebsite());
        dto.setEmailDomain(organization.getEmailDomain());
        if(dto.getEmailDomain() == null) {
            //FIXME organizationDetail update both domain!
            dto.setEmailDomain(org.getEmailDomain());
        }
        dto.setWebsite(organization.getWebsite());
        dto.setName(organization.getName());
        dto.setCommunityId(organizationDTO.getCommunityId());
        dto.setCommunityName(organizationDTO.getCommunityName());
        dto.setAdminMembers(getAdmins(org.getOrganizationId()));
        if(!StringUtils.isEmpty(organization.getWebsite())){
            dto.setWebsite(organization.getWebsite());
        }
        if(!StringUtils.isEmpty(organization.getUnifiedSocialCreditCode())){
            dto.setUnifiedSocialCreditCode(organization.getUnifiedSocialCreditCode());
        }

        dto.setAvatarUri(org.getAvatar());
        if (!StringUtils.isEmpty(org.getDisplayName())) {
            dto.setDisplayName(org.getDisplayName());
        }
        if (null != org.getCheckinDate())
            dto.setCheckinDate(org.getCheckinDate().getTime());
        if (!StringUtils.isEmpty(org.getAvatar()))
            dto.setAvatarUrl(contentServerService.parserUri(dto.getAvatarUri(), EntityType.ORGANIZATIONS.getCode(), dto.getOrganizationId()));

        if (!StringUtils.isEmpty(dto.getPostUri()))
            dto.setPostUrl(contentServerService.parserUri(dto.getPostUri(), EntityType.ORGANIZATIONS.getCode(), dto.getOrganizationId()));

        List<OrganizationAddress> organizationAddresses = organizationProvider.findOrganizationAddressByOrganizationId(dto.getOrganizationId());
        List<AddressDTO> addresses = organizationAddresses.stream().map(r -> {
            OrganizationAddressDTO address = ConvertHelper.convert(r, OrganizationAddressDTO.class);
            Address addr = addressProvider.findAddressById(address.getAddressId());

            AddressDTO addressDTO = ConvertHelper.convert(addr, AddressDTO.class);
            if (null != addr) {
                Building building = communityProvider.findBuildingByCommunityIdAndName(addr.getCommunityId(), addr.getBuildingName());
                if (null != building) {
                    addressDTO.setBuildingId(building.getId());
                }
            }
            return addressDTO;
        }).collect(Collectors.toList());

        dto.setAddresses(addresses);
        List<OrganizationAttachment> attachments = organizationProvider.listOrganizationAttachments(dto.getOrganizationId());

        if (null != attachments && 0 != attachments.size()) {
            for (OrganizationAttachment attachment : attachments) {
                attachment.setContentUrl(contentServerService.parserUri(attachment.getContentUri(), EntityType.ORGANIZATIONS.getCode(), dto.getOrganizationId()));
            }

            dto.setAttachments(attachments.stream().map(r -> {
                return ConvertHelper.convert(r, AttachmentDescriptor.class);
            }).collect(Collectors.toList()));
        }

        List<Long> roles = new ArrayList<Long>();
        roles.add(RoleConstants.ENTERPRISE_SUPER_ADMIN);

        if (flag) {
            List<OrganizationMember> members = this.getOrganizationAdminMemberRole(dto.getOrganizationId(), roles);
            if (members.size() > 0) {
                dto.setMember(ConvertHelper.convert(members.get(0), OrganizationMemberDTO.class));
            }
        }

        dto.setAccountName(org.getContactor());
        dto.setAccountPhone(org.getContact());

        dto.setServiceUserId(org.getServiceUserId());

        OrganizationMember m = organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, id);
        if (null != m) {
            dto.setMember(ConvertHelper.convert(m, OrganizationMemberDTO.class));
        }

        //21002 企业管理1.4（来源于第三方数据，企业名称栏为灰色不可修改） add by xiongying20171219
        EnterpriseCustomer customer = enterpriseCustomerProvider.findByOrganizationId(dto.getOrganizationId());
        if(customer != null && !StringUtils.isEmpty(customer.getNamespaceCustomerType())) {
            dto.setThirdPartFlag(true);
        }

        return dto;
    }

    @Override
    public ListEnterprisesCommandResponse listNewEnterprises(ListEnterprisesCommand cmd) {
        ListEnterprisesCommandResponse resp = new ListEnterprisesCommandResponse();
        List<OrganizationDetailDTO> dtos = new ArrayList<OrganizationDetailDTO>();
        SearchOrganizationCommand command = ConvertHelper.convert(cmd, SearchOrganizationCommand.class);
        command.setKeyword(cmd.getKeywords());  //两个字段不一样，操蛋
        GroupQueryResult rlt = organizationSearcher.query(command);
        for (Long id : rlt.getIds()) {
            OrganizationDetailDTO dto = this.toOrganizationDetailDTO(id, cmd.getQryAdminRoleFlag());
            if (null != dto)
                dtos.add(dto);
        }
        addExtraInfo(dtos);
        resp.setDtos(dtos);
        resp.setNextPageAnchor(rlt.getPageAnchor());
        return resp;
    }

    @Override
    public ListEnterpriseDetailResponse listEnterprisesAbstract(ListEnterprisesCommand cmd) {
        ListEnterpriseDetailResponse resp = new ListEnterpriseDetailResponse();
        List<EnterpriseDetailDTO> dtos = new ArrayList<>();
        SearchOrganizationCommand command = ConvertHelper.convert(cmd, SearchOrganizationCommand.class);
        command.setKeyword(cmd.getKeywords());  //两个字段不一样，操蛋
        GroupQueryResult rlt = organizationSearcher.query(command);
        for (Long id : rlt.getIds()) {
            EnterpriseDetailDTO dto = new EnterpriseDetailDTO();
            Organization organization = organizationProvider.findOrganizationById(id);
            OrganizationDetail org = organizationProvider.findOrganizationDetailByOrganizationId(id);
            if (null == organization) {
                LOGGER.debug("organization is null, id = " + id);
//                return null;
                continue;
            } else if (OrganizationGroupType.fromCode(organization.getGroupType()) != OrganizationGroupType.ENTERPRISE) {
                LOGGER.debug("organization not is enterprise, id = " + id);
//                return null;
                continue;
            } else if (organization.getParentId() != 0L) {
                LOGGER.debug("organization is children organization, id = " + id);
//                return null;
                continue;
            }
            dto.setId(organization.getId());
            dto.setEnterpriseName(organization.getName());
            if(org != null) {
                if(dto.getEnterpriseName() == null || com.mysql.jdbc.StringUtils.isNullOrEmpty(dto.getEnterpriseName()))
                    dto.setEnterpriseName(org.getDisplayName());

                dto.setAvatarUri(org.getAvatar());
                dto.setPosturi(org.getPostUri());
                if (!StringUtils.isEmpty(org.getAvatar())) {
                    dto.setAvatarUrl(contentServerService.parserUri(dto.getAvatarUri(), EntityType.ORGANIZATIONS.getCode(), organization.getId()));
                }
                if (!StringUtils.isEmpty(org.getPostUri())) {
                    dto.setPosturl(contentServerService.parserUri(dto.getPosturi(), EntityType.ORGANIZATIONS.getCode(), organization.getId()));
                }

            }

            String pinyin = PinYinHelper.getPinYin(dto.getEnterpriseName());
            dto.setFullInitial(PinYinHelper.getFullCapitalInitial(pinyin));
            dto.setFullPinyin(pinyin.replaceAll(" ", ""));
            dto.setInitial(PinYinHelper.getCapitalInitial(dto.getFullPinyin()));

            List<OrganizationAddress> organizationAddresses = organizationProvider.findOrganizationAddressByOrganizationId(organization.getId());

            if(organizationAddresses != null && organizationAddresses.size() > 0) {
                Address addr = addressProvider.findAddressById(organizationAddresses.get(0).getAddressId());
                if(addr != null)
                    dto.setAddress(addr.getAddress());

            }
            dtos.add(dto);
        }

        resp.setDetails(dtos);
        resp.setNextPageAnchor(rlt.getPageAnchor());
        return resp;
    }

    @Override
    public ListEnterprisesCommandResponse listEnterprises(
            ListEnterprisesCommand cmd) {
        // 更改成全部走搜索引擎
        return listNewEnterprises(cmd);
//		ListEnterprisesCommandResponse resp = new ListEnterprisesCommandResponse();
//
//		List<OrganizationDetailDTO> dtos = new ArrayList<OrganizationDetailDTO>();
//
//		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
//
//		Long communityId = cmd.getCommunityId();
//
//		String keywords = cmd.getKeywords();
//
//		TrueOrFalseFlag setAdminFlag = TrueOrFalseFlag.fromCode(cmd.getSetAdminFlag());
//
//		if(!StringUtils.isEmpty(keywords)){
//			SearchOrganizationCommand command = ConvertHelper.convert(cmd, SearchOrganizationCommand.class);
//			command.setKeyword(keywords);
//			GroupQueryResult rlt = this.organizationSearcher.query(command);
//	        resp.setNextPageAnchor(rlt.getPageAnchor());
//	        for(Long id : rlt.getIds()) {
//	        	OrganizationDetailDTO dto = this.toOrganizationDetailDTO(id, cmd.getQryAdminRoleFlag());
//	        	if(null != dto)
//	        		dtos.add(dto);
//	        }
//	        addExtraInfo(dtos);
//	        resp.setDtos(dtos);
//			return resp;
//		}
//
//		if(!StringUtils.isEmpty(cmd.getBuildingName())){
//			Building building = communityProvider.findBuildingByCommunityIdAndName(communityId, cmd.getBuildingName());
//			if(null != building){
//				cmd.setBuildingId(building.getId());
//			}
//		}
//
//		Long buildingId = cmd.getBuildingId();
//		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
//		CrossShardListingLocator locator = new CrossShardListingLocator();
//		locator.setAnchor(cmd.getPageAnchor());
//		if(null != buildingId){
//			if (setAdminFlag == null) {
//				List<OrganizationAddress> addresses = organizationProvider.listOrganizationAddressByBuildingId(buildingId, pageSize, locator);
//				for (OrganizationAddress address : addresses) {
//					OrganizationDetailDTO dto = this.toOrganizationDetailDTO(address.getOrganizationId(), cmd.getQryAdminRoleFlag());
//					if(null != dto)
//						dtos.add(dto);
//				}
//			}else {
//				List<Long> organizationIds = organizationProvider.listOrganizationIdByBuildingId(buildingId, setAdminFlag.getCode(), pageSize, locator);
//				for (Long organizationId : organizationIds) {
//					OrganizationDetailDTO dto = this.toOrganizationDetailDTO(organizationId, cmd.getQryAdminRoleFlag());
//					if(null != dto)
//						dtos.add(dto);
//				}
//			}
//		}else if(null != communityId){
//			if (setAdminFlag == null) {
//				List<OrganizationCommunityRequest> requests = organizationProvider.queryOrganizationCommunityRequestByCommunityId(locator, communityId, pageSize, null);
//				for (OrganizationCommunityRequest req : requests) {
//					OrganizationDetailDTO dto = this.toOrganizationDetailDTO(req.getMemberId(), cmd.getQryAdminRoleFlag());
//					if(null != dto)
//						dtos.add(dto);
//				}
//			}else {
//				List<Long> organizationIds = organizationProvider.listOrganizationIdByCommunityId(communityId, setAdminFlag.getCode(), pageSize, locator);
//				for (Long organizationId : organizationIds) {
//					OrganizationDetailDTO dto = this.toOrganizationDetailDTO(organizationId, cmd.getQryAdminRoleFlag());
//					if(null != dto)
//						dtos.add(dto);
//				}
//			}
//		}else{
//			List<Organization> organizations = organizationProvider.listEnterpriseByNamespaceIds(namespaceId, OrganizationType.ENTERPRISE.getCode(), setAdminFlag==null?null:setAdminFlag.getCode(), locator, pageSize);
//			for (Organization organization : organizations) {
//				OrganizationDetailDTO dto = this.toOrganizationDetailDTO(organization.getId(), cmd.getQryAdminRoleFlag());
//				if(null != dto)
//					dtos.add(dto);
//			}
//		}
//		addExtraInfo(dtos);
//		resp.setDtos(dtos);
//		resp.setNextPageAnchor(locator.getAnchor());
//		return resp;
    }

    @Override
    public void exportEnterprises(ListEnterprisesCommand cmd, HttpServletResponse response) {
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), cmd.getManageOrganizationId(), PrivilegeConstants.ORGANIZATION_EXPORT, ServiceModuleConstants.ORGANIZATION_MODULE, ActionType.OFFICIAL_URL.getCode(), null, cmd.getManageOrganizationId(), cmd.getCommunityId());
        cmd.setPageSize(10000);
        List<OrganizationDetailDTO> organizationDetailDTOs = listEnterprises(cmd).getDtos();
        if (organizationDetailDTOs != null && organizationDetailDTOs.size() > 0) {
            String fileName = String.format("企业信息_%s", DateUtil.dateToStr(new Date(), DateUtil.NO_SLASH));
            ExcelUtils excelUtils = new ExcelUtils(response, fileName, "企业信息");
//
            List<OrganizationExportDetailDTO> data = organizationDetailDTOs.stream().map(this::convertToExportDetail).collect(Collectors.toList());
            String[] propertyNames = {"name", "emailDomain", "apartments", "signupCount", "memberCount", "serviceUserName", "admins", "address", "contact", "checkinDateString", "description", "unifiedSocialCreditCode"};
            String[] titleNames = {"企业名称", "邮箱域名", "楼栋门牌", "注册人数", "企业人数", "客服经理", "企业管理员", "地址", "咨询电话", "入驻时间", "企业介绍", "统一社会信用代码"};
            int[] titleSizes = {20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20};
            excelUtils.writeExcel(propertyNames, titleNames, titleSizes, data);
        } else {
            throw errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_NO_DATA,
                    "no data");
        }
    }

    private OrganizationExportDetailDTO convertToExportDetail(OrganizationDetailDTO organizationDetailDTO) {
        OrganizationExportDetailDTO exportDetailDTO = ConvertHelper.convert(organizationDetailDTO, OrganizationExportDetailDTO.class);
        try {
            if (exportDetailDTO.getAddresses() != null) {
                String apartments = String.join("\n", exportDetailDTO.getAddresses().stream().filter(a -> a != null).map(AddressDTO::getAddress).collect(Collectors.toList()));
                exportDetailDTO.setApartments(apartments);
            }
            if (exportDetailDTO.getAdminMembers() != null) {
                String admins = String.join("\n", exportDetailDTO.getAdminMembers().stream().filter(a -> a != null).map(this::toAdminString).collect(Collectors.toList()));
                exportDetailDTO.setAdmins(admins);
            }
            if (exportDetailDTO.getCheckinDate() != null) {
                exportDetailDTO.setCheckinDateString(DateUtil.dateToStr(new Date(exportDetailDTO.getCheckinDate()), DateUtil.YMR_SLASH));
            }
        } catch (Exception e) {
            LOGGER.error("dto : {}", organizationDetailDTO);
            throw e;
        }

        return exportDetailDTO;
    }

    private String toAdminString(OrganizationContactDTO organizationContactDTO) {
        return organizationContactDTO.getContactName() + "(" + organizationContactDTO.getContactToken() + ")";
    }

    private void addExtraInfo(List<OrganizationDetailDTO> organizationDetailList) {
        for (OrganizationDetailDTO organizationDetailDTO : organizationDetailList) {
            addExtraInfo(organizationDetailDTO);
        }
    }

    // 添加管理员列表，添加客服人员，添加注册人数, add by tt, 20161129
    private void addExtraInfo(OrganizationDetailDTO organizationDetailDTO) {
        addAdmins(organizationDetailDTO);
        addServiceUser(organizationDetailDTO);
        addSignupCount(organizationDetailDTO);
    }

    private void addAdmins(OrganizationDetailDTO organizationDetailDTO) {
        organizationDetailDTO.setAdminMembers(getAdmins(organizationDetailDTO.getOrganizationId()));
    }

    @Override
    public List<OrganizationContactDTO> getAdmins(Long organizationId) {
//		ListOrganizationAdministratorCommand cmd = new ListOrganizationAdministratorCommand();
//		cmd.setOrganizationId(organizationId);
//		ListOrganizationMemberCommandResponse  response = rolePrivilegeService.listOrganizationAdministrators(cmd);
//		return response.getMembers();
        ListServiceModuleAdministratorsCommand command = new ListServiceModuleAdministratorsCommand();
        command.setOrganizationId(organizationId);
        List<OrganizationContactDTO> justOneAdmin = new ArrayList<>();
        List<OrganizationContactDTO> getAdmin = rolePrivilegeService.listOrganizationAdministrators(command);
        //由于之前的企业可以设置多个管理员，故有可能返回多个管理员结果，此时只显示最新新增的一个
        if(getAdmin == null || getAdmin.size() == 0){
            return null;
        }else{
            justOneAdmin.add(getAdmin.get(0));
            return justOneAdmin;
        }
    }

    private void addServiceUser(OrganizationDetailDTO organizationDetailDTO) {
        OrganizationServiceUser user = getServiceUser(organizationDetailDTO.getOrganizationId(), organizationDetailDTO.getServiceUserId());
        if (user != null) {
            organizationDetailDTO.setServiceUserName(user.getServiceUserName());
            organizationDetailDTO.setServiceUserPhone(user.getServiceUserPhone());
        }
    }

    @Override
    public OrganizationServiceUser getServiceUser(Long organizationId) {
        OrganizationDetail organizationDetail = organizationProvider.findOrganizationDetailByOrganizationId(organizationId);
        if (organizationDetail == null) {
            return null;
        }
        return getServiceUser(organizationId, organizationDetail.getServiceUserId());
    }

    @Override
    public OrganizationServiceUser getServiceUser(Long organizationId, Long serviceUserId) {
        if (serviceUserId == null) {
            return null;
        }

        //1. 找到企业入驻的园区
        OrganizationCommunityRequest organizationCommunityRequest = organizationProvider.getOrganizationCommunityRequestByOrganizationId(organizationId);
        if (organizationCommunityRequest == null) {
            return null;
        }
        //2. 找到园区对应的管理公司
        List<OrganizationCommunityDTO> organizationCommunityList = organizationProvider.findOrganizationCommunityByCommunityId(organizationCommunityRequest.getCommunityId());
        if (organizationCommunityList == null || organizationCommunityList.size() == 0) {
            return null;
        }
        //3. 找到管理公司的这个人
        for (OrganizationCommunityDTO organizationCommunityDTO : organizationCommunityList) {
            OrganizationMember organizationMember = organizationProvider.findOrganizationMemberByUIdAndOrgId(serviceUserId, organizationCommunityDTO.getOrganizationId());
            if (organizationMember != null) {
                OrganizationServiceUser user = new OrganizationServiceUser();
                user.setServiceUserId(serviceUserId);
                user.setServiceUserName(organizationMember.getContactName());
                user.setServiceUserPhone(organizationMember.getContactToken());
                return user;
            }
        }
        return null;
    }

    private void addSignupCount(OrganizationDetailDTO organizationDetailDTO) {
        organizationDetailDTO.setSignupCount(getSignupCount(organizationDetailDTO.getOrganizationId()));
    }

    private Integer getSignupCount(Long organizationId) {
        return organizationProvider.getSignupCount(organizationId);
    }

    @Override
    public List<String> getBusinessContactPhone(Long organizationId) {
        List<String> phoneList = new ArrayList<>();
        OrganizationDetail organizationDetail = organizationProvider.findOrganizationDetailByOrganizationId(organizationId);
        if (organizationDetail != null) {
            String contact = organizationDetail.getContact();
            if (org.apache.commons.lang.StringUtils.isNotBlank(contact)) {
                String[] contactArray = contact.trim().split(",");
                for (String phone : contactArray) {
                    if (org.apache.commons.lang.StringUtils.isNotBlank(phone) && (phone = phone.trim()).startsWith("1") && phone.length() == 11) {
                        phoneList.add(phone);
                    }
                }
            }
        }
        return phoneList;
    }

    @Override
    public List<String> getAdminPhone(Long organizationId) {
        List<String> phoneList = new ArrayList<>();
        List<OrganizationContactDTO> organizationMemberList = getAdmins(organizationId);
        if (organizationMemberList != null && !organizationMemberList.isEmpty()) {
            for (OrganizationContactDTO organizationMemberDTO : organizationMemberList) {
                String phone = organizationMemberDTO.getContactToken();
                if (org.apache.commons.lang.StringUtils.isNotBlank(phone) && (phone = phone.trim()).startsWith("1") && phone.length() == 11) {
                    phoneList.add(phone);
                }
            }
        }
        return phoneList;
    }

    @Override
    public Set<String> getOrganizationContactPhone(Long organizationId) {
        Set<String> phoneSet = new HashSet<>();
        phoneSet.addAll(getBusinessContactPhone(organizationId));
        phoneSet.addAll(getAdminPhone(organizationId));

        return phoneSet;
    }

    private void checkUnifiedSocialCreditCode(String unifiedSocialCreditCode, Integer namespaceId, Long organizationId) {
        List<Organization> organizations =  organizationProvider.findNamespaceUnifiedSocialCreditCode(unifiedSocialCreditCode, namespaceId);
        if(organizations != null && organizations.size() > 0) {
            if(organizationId != null) {
                for(Organization org : organizations) {
                    if(organizationId.equals(org.getId())) {
                        return;
                    }
                }
            }
            LOGGER.error("unifiedSocialCreditCode:{} already exist in namespace:{}", unifiedSocialCreditCode, namespaceId);
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_UNIFIEDSOCIALCREDITCODE_EXIST,
                    "unifiedSocialCreditCode already exist.");
        }

    }

    private void checkOrgNameUnique(Long id, Integer namespaceId, String orgName) {
        if(org.apache.commons.lang.StringUtils.isNotBlank(orgName)) {
            Organization org = organizationProvider.findOrganizationByName(orgName, namespaceId);
            if(org != null) {
                if(id != null) {
                    if(id.equals(org.getId())) {
                        return;
                    }
                }
                LOGGER.error("organizationName {} in namespace {} already exist!", orgName, namespaceId);
                throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ORG_EXIST,
                        "organizationName is already exist");
            }
        }

    }

    private boolean checkOrgNameUniqueNew(Long id, Integer namespaceId, String orgName) {
        if(org.apache.commons.lang.StringUtils.isNotBlank(orgName)) {
            Organization org = organizationProvider.findOrganizationByName(orgName, namespaceId);
            if(org != null) {
                if(id != null) {
                    if(id.equals(org.getId())) {
                        return false;
                    }
                }
                LOGGER.error("organizationName {} in namespace {} already exist!", orgName, namespaceId);
                return false;
//                throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ORG_EXIST,
//                        "organizationName is already exist");
            }
        }
        return true;

    }

    /**
     * 创建企业（标准版）
     * @param cmd
     * @return
     */
    @Override
    public OrganizationDTO createStandardEnterprise(CreateEnterpriseStandardCommand cmd) {
        //1.从上下文中拿到用户信息
        User user = UserContext.current().getUser();
        //2.从上下文中拿到域空间ID
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        if(cmd.getCheckPrivilege() != null && cmd.getCheckPrivilege()) {
            userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), cmd.getManageOrganizationId(),
                    PrivilegeConstants.ORGANIZATION_CREATE, ServiceModuleConstants.ORGANIZATION_MODULE,
                    ActionType.OFFICIAL_URL.getCode(), null, cmd.getManageOrganizationId(), cmd.getCommunityId());
        }


        if(org.apache.commons.lang.StringUtils.isNotBlank(cmd.getUnifiedSocialCreditCode())) {
            checkUnifiedSocialCreditCode(cmd.getUnifiedSocialCreditCode(), namespaceId, null);
        }
        checkOrgNameUnique(null, cmd.getNamespaceId(), cmd.getName());
        //创建公司Organization类的对象
        Organization organization = new Organization();
        dbProvider.execute((TransactionStatus status) -> {
            //创建群组Group类的对象
            Group group = new Group();
            //将数据封装在对象中
            group.setName(cmd.getName());
            group.setDisplayName(cmd.getDisplayName());
            group.setEnterpriseAddress(cmd.getAddress());

            group.setDescription(cmd.getContactsPhone());
            group.setStatus(OrganizationStatus.ACTIVE.getCode());

            group.setCreatorUid(user.getId());

            group.setNamespaceId(namespaceId);

            group.setDiscriminator(GroupDiscriminator.ENTERPRISE.getCode());

            group.setPrivateFlag(GroupPrivacy.PRIVATE.getCode());

            groupProvider.createGroup(group);

            organization.setParentId(0L);
            organization.setLevel(1);
            organization.setPath("");
            organization.setName(cmd.getName());
            organization.setGroupType(OrganizationGroupType.ENTERPRISE.getCode());
            organization.setStatus(OrganizationStatus.ACTIVE.getCode());
            organization.setOrganizationType(OrganizationType.ENTERPRISE.getCode());
            organization.setNamespaceId(namespaceId);
            organization.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            organization.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            organization.setGroupId(group.getId());
            organization.setWebsite(cmd.getWebsite());
            organization.setEmailDomain(cmd.getEmailDomain());
            organization.setUnifiedSocialCreditCode(cmd.getUnifiedSocialCreditCode());
            //表明该公司是否是管理公司 1-是 0-否
            if(cmd.getPmFlag() != null){
                organization.setPmFlag(cmd.getPmFlag().byteValue());
                if(cmd.getPmFlag().byteValue() == TrueOrFalseFlag.TRUE.getCode()){
                    organization.setOrganizationType(OrganizationType.PM.getCode());
                }
            }
            //表明该公司是否是服务商，1-服务商 0-否
            if(cmd.getServiceSupportFlag() != null){
                organization.setServiceSupportFlag(cmd.getServiceSupportFlag().byteValue());
            }
            if(cmd.getWorkbenchFlag() != null){
                organization.setWorkPlatformFlag(cmd.getWorkbenchFlag().byteValue());
            }
            organizationProvider.createOrganization(organization);

            //根据是否是管理公司来进行添加eh_organization_communities表数据，只有是管理公司才能拥有管理的项目
            if(cmd.getPmFlag() != null && cmd.getPmFlag() == Integer.valueOf(TrueOrFalseFlag.TRUE.getCode())){
                //说明是管理员，那么我们就可以将管理的项目添加到eh_organization_communities表中
                if(cmd.getProjectIds()!= null){
                    for(Long lon : cmd.getProjectIds()){
                        OrganizationCommunity organizationCommunity = new OrganizationCommunity();
                        organizationCommunity.setCommunityId(lon);
                        organizationCommunity.setOrganizationId(organization.getId());
                        organizationProvider.insertOrganizationCommunity(organizationCommunity);
                    }
                }

            }

            //向办公地点表中添加数据
            if(cmd.getOfficeSites() != null){
                //说明传过来的所在项目和名称以及其中的楼栋和门牌不为空，那么我们将其进行遍历
                for(CreateOfficeSiteCommand createOfficeSiteCommand :cmd.getOfficeSites()){
                    //这样的话拿到的是每一个办公所在地，以及其中的楼栋和门牌
                    //首先我们将办公地址名称和办公地点id持久化到表eh_organization_workPlaces中
                    //创建OrganizationWorkPlaces类的对象
                    OrganizationWorkPlaces organizationWorkPlaces = new OrganizationWorkPlaces();
                    //将数据封装在对象中
                    organizationWorkPlaces.setCommunityId(createOfficeSiteCommand.getCommunityId());
                    organizationWorkPlaces.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    organizationWorkPlaces.setWorkplaceName(createOfficeSiteCommand.getSiteName());
                    organizationWorkPlaces.setProvinceId(createOfficeSiteCommand.getProvinceId());
                    organizationWorkPlaces.setCityId(createOfficeSiteCommand.getCityId());
                    organizationWorkPlaces.setAreaId(createOfficeSiteCommand.getAreaId());
                    organizationWorkPlaces.setWholeAddressName(createOfficeSiteCommand.getWholeAddressName());
                    //将上面的organization对象中的id也封装在对象中
                    organizationWorkPlaces.setOrganizationId(organization.getId());
                    //调用organizationProvider中的insertIntoOrganizationWorkPlaces方法,将对象持久化到数据库
                    organizationProvider.insertIntoOrganizationWorkPlaces(organizationWorkPlaces);
                    //现在的情况是这样的，我们还需要进行维护一张表eh_enterprise_community_map的信息，这张表其实在标准版中是不适用的
                    //后面的话会逐步的进行淘汰，但是现在当给eh_organization_workPlaces表中添加一调数据的同时，还需要向eh_enterprise_community_map
                    //中添加同样的数据
                    //// TODO: 2018/5/4
                    //创建EnterpriseCommunityMap类的对象
                    EnterpriseCommunityMap enterpriseCommunityMap = new EnterpriseCommunityMap();
                    //将数据封装在对象EnterpriseCommunityMap中
                    enterpriseCommunityMap.setCommunityId(createOfficeSiteCommand.getCommunityId());
                    enterpriseCommunityMap.setMemberId(organization.getId());
                    enterpriseCommunityMap.setMemberType(EnterpriseCommunityMapType.Enterprise.getCode());
                    enterpriseCommunityMap.setMemberStatus(EnterpriseCommunityMapStatus.ACTIVE.getCode());
                    //调用enterpriseProvider中的insertIntoEnterpriseCommunityMap(EnterpriseCommunityMap enterpriseCommunityMap)方法，将数据封装在
                    //表eh_enterprise_community_map中
                    enterpriseProvider.insertIntoEnterpriseCommunityMap(enterpriseCommunityMap);


                    //在这里我们还需要维护eh_organization_community_requests这张表
                    //创建OrganizationCommunityRequest类的对象
                    OrganizationCommunityRequest organizationCommunityRequest = new OrganizationCommunityRequest();
                    //将数据封装在对象OrganizationCommunityRequest对象中
                    organizationCommunityRequest.setCommunityId(createOfficeSiteCommand.getCommunityId());
                    organizationCommunityRequest.setMemberId(organization.getId());
                    organizationCommunityRequest.setMemberType(EnterpriseCommunityMapType.Organization.getCode());
                    organizationCommunityRequest.setMemberStatus(EnterpriseCommunityMapStatus.ACTIVE.getCode());
                    //// TODO: 2018/5/22
                    enterpriseProvider.insertIntoOrganizationCommunityRequest(organizationCommunityRequest);


                    //接下来我们需要将对应的所在项目的楼栋和门牌也持久化到项目和楼栋门牌的关系表eh_communityAndBuilding_relationes中
                    //首先进行遍历楼栋集合
                    if(createOfficeSiteCommand.getSiteDtos() != null){
                        //说明楼栋和门牌不为空，注意他是一个集合
                        //遍历
                        for(OrganizationSiteApartmentDTO organizationSiteApartmentDTO : createOfficeSiteCommand.getSiteDtos()){
                            //这样的话我们拿到的是每一个楼栋以及对应的门牌
                            //创建CommunityAndBuildingRelationes对象，并且将数据封装在对象中，然后持久化到数据库
                            CommunityAndBuildingRelationes communityAndBuildingRelationes = new CommunityAndBuildingRelationes();
                            communityAndBuildingRelationes.setCommunityId(createOfficeSiteCommand.getCommunityId());
                            communityAndBuildingRelationes.setAddressId(organizationSiteApartmentDTO.getApartmentId());
                            communityAndBuildingRelationes.setBuildingId(organizationSiteApartmentDTO.getBuildingId());
                            //调用organizationProvider中的insertIntoCommunityAndBuildingRelationes方法，将对象持久化到数据库
                            organizationProvider.insertIntoCommunityAndBuildingRelationes(communityAndBuildingRelationes);
                        }
                    }

                }
            }

            OrganizationDetail organizationDetail = new OrganizationDetail();
            organizationDetail.setOrganizationId(organization.getId());
            organizationDetail.setAddress(cmd.getAddress());
            organizationDetail.setDescription(cmd.getDescription());
            organizationDetail.setAvatar(cmd.getAvatar());
            organizationDetail.setMemberRange(cmd.getMemberRange());
            organizationDetail.setCreateTime(organization.getCreateTime());
            if (!StringUtils.isEmpty(cmd.getCheckinDate())) {
                java.sql.Date checkinDate = DateUtil.parseDate(cmd.getCheckinDate());
                if (null != checkinDate) {
                    organizationDetail.setCheckinDate(new Timestamp(checkinDate.getTime()));
                }
            }
            organizationDetail.setContact(cmd.getContactsPhone());
            organizationDetail.setDisplayName(cmd.getDisplayName());
            organizationDetail.setPostUri(cmd.getPostUri());
            organizationDetail.setMemberCount(cmd.getMemberCount());
            organizationDetail.setEmailDomain(cmd.getEmailDomain());
            organizationDetail.setServiceUserId(cmd.getServiceUserId());

            if (cmd.getLatitude() != null)
                organizationDetail.setLatitude(Double.valueOf(cmd.getLatitude()));
            if (cmd.getLongitude() != null)
                organizationDetail.setLongitude(Double.valueOf(cmd.getLongitude()));
            organizationProvider.createOrganizationDetail(organizationDetail);

            //根据传进来的手机号进行校验，判断该手机号是否已经进行注册
            //非空校验

            if(cmd.getEntries() != null && cmd.getContactor() != null && organization.getId() != null){
                //接下来创建超级管理员
                //创建CreateOrganizationAdminCommand类的对象
                CreateOrganizationAdminCommand cmdnew = new CreateOrganizationAdminCommand();
                //将数据封装进去
                cmdnew.setContactToken(cmd.getEntries());
                cmdnew.setContactName(cmd.getContactor());
                cmdnew.setOrganizationId(organization.getId());
                OrganizationContactDTO organizationContactDTO = rolePrivilegeService.createOrganizationSuperAdmin(cmdnew);
                //查看eh_organization_members表中信息
                OrganizationMember organizationMember = organizationProvider.findOrganizationMemberSigned(cmd.getEntries(),
                        cmd.getNamespaceId(),OrganizationMemberGroupType.MANAGER.getCode());
                //将该organizationMember的id值更新到eh_organizations表中的admin_target_id字段中
                if(organizationMember != null){
                    //创建Organization类的对象
                    Organization organization1 = new Organization();
                    //封装信息
                    organization1.setAdminTargetId(organizationMember.getTargetId());
                    organization1.setId(organization.getId());
                    //更新eh_organizations表信息
                    organizationProvider.updateOrganizationByOrgId(organization1);
                }
            }


            /*if(cmd.getEntries() != null && !"".equals(cmd.getEntries())){
                //说明手机号已经传进来了，那么我们根据该手机号去查eh_user_identifiers表中看是否已经注册
                UserIdentifier userIdentifier = userProvider.getUserByToken(cmd.getEntries(),cmd.getNamespaceId());
                if(userIdentifier != null){
                    //说明已经进行注册，eh_user_identifiers表中存在记录，但是eh_organization_members表中不一定存在记录
                    //那么还需要查询该表
                    //// TODO: 2018/4/28
                    OrganizationMember organizationMember = organizationProvider.findOrganizationMemberSigned(cmd.getEntries(),
                            cmd.getNamespaceId());
                    if(organizationMember != null){
                        //说明已经加入了公司，那么我们就将新建的公司的admin_target_id值更改为organizationMember中的id值,并且将其对应的organizationMembers表中的member_group字段变更为manager表示的是管理员
                        organizationMember.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
                        organizationProvider.updateOrganizationMember(organizationMember);
                        //// TODO: 2018/4/28
                        //创建Organization类的对象
                        Organization organization1 = new Organization();
                        //封装信息
                        organization1.setAdminTargetId(organizationMember.getId());
                        organization1.setId(organization.getId());
                        //更新eh_organizations表信息
                        organizationProvider.updateOrganizationByOrgId(organization1);
                    }else{
                        //// TODO: 2018/4/28 说明没有加入公司,但是已经注册了，那么我们就将其加入刚新建的公司中
                        //创建OrganizationMember类的对象
                        OrganizationMember organizationMember1 = new OrganizationMember();
                        //封装信息
                        organizationMember1.setOrganizationId(organization.getId());
                        organizationMember1.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
                        if(cmd.getContactor() != null && !"".equals(cmd.getContactor())){
                            //说明管理员的名字不为空，那么封装名字
                            organizationMember1.setContactName(cmd.getContactor());
                        }
                        organizationMember1.setContactToken(cmd.getEntries());
                        organizationMember1.setTargetType(OrganizationMemberTargetType.USER.getCode());
                        organizationMember1.setTargetId(userIdentifier.getOwnerUid());
                        organizationMember1.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
                        organizationMember1.setGroupType("ENTERPRISE");
                        organizationMember1.setCreatorUid(user.getId());
                        //持久化到数据库中
                        //// TODO: 2018/4/28
                        organizationProvider.insertIntoOrganizationMember(organizationMember1);
                        //更新eh_organizations表中的admin_target_id字段
                        //创建一个Organization类的对象
                        Organization organization2 = new Organization();
                        //封装信息
                        organization2.setAdminTargetId(organizationMember1.getId());
                        organization2.setId(organization.getId());
                        //更新eh_organizations表信息
                        organizationProvider.updateOrganizationByOrgId(organization2);
                    }

                }else{
                    //// TODO: 2018/4/28 说明未进行注册,未进行注册也可以加入企业，所以我们还是需要查eh_organizarion_members表
                    //// TODO: 2018/4/28
                    OrganizationMember organizationMember = organizationProvider.findOrganizationMemberNoSigned(cmd.getEntries(),cmd.getNamespaceId());
                    //判断
                    if(organizationMember != null){
                        //说明已经加入了公司，但是没有注册信息,那么我们需要将该organizationMembers表中的member_group字段设置为manager,表示的是管理员
                        organizationMember.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
                        organizationProvider.updateOrganizationMember(organizationMember);
                        //更新eh_organizations表中的admin_target_id字段信息
                        //创建Organization类的对象
                        Organization organization3 = new Organization();
                        //封装数据
                        organization3.setAdminTargetId(organizationMember.getId());
                        organization3.setId(organization.getId());
                        //更新eh_organizations表信息
                        organizationProvider.updateOrganizationByOrgId(organization3);
                    }else{
                        //说明没有注册信息，也没有加入公司，那么我们就帮他加入公司，只要后面注册之后，他就是超级管理员了

                        //创建OrganizationMember类的对象
                        OrganizationMember organizationMember2 = new OrganizationMember();
                        //封装信息
                        organizationMember2.setOrganizationId(organization.getId());
                        organizationMember2.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
                        if(cmd.getContactor() != null && !"".equals(cmd.getContactor())){
                            //说明管理员的名字不为空，那么封装名字
                            organizationMember2.setContactName(cmd.getContactor());
                        }
                        organizationMember2.setContactToken(cmd.getEntries());
                        organizationMember2.setTargetType(OrganizationMemberTargetType.UNTRACK.getCode());
                        organizationMember2.setTargetId(0L);
                        organizationMember2.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
                        organizationMember2.setGroupType("ENTERPRISE");
                        organizationMember2.setCreatorUid(user.getId());
                        //持久化到数据库中
                        //// TODO: 2018/4/28
                        organizationProvider.insertIntoOrganizationMember(organizationMember2);
                        //更新eh_organizations表中的admin_target_id字段
                        //创建一个Organization类的对象
                        Organization organization4 = new Organization();
                        //封装信息
                        organization4.setAdminTargetId(organizationMember2.getId());
                        organization4.setId(organization.getId());
                        //更新eh_organizations表信息
                        organizationProvider.updateOrganizationByOrgId(organization4);

                    }
                }

            }*/


            // 把代码移到一个独立的方法，以便其它地方也可以调用 by lqs 20161101
            //在这里谁有给organization_Community_requests表中添加了新的数据，这个现在是不需要的
            //所以我将其进行注释 modify by yuanlei
//            createActiveOrganizationCommunityRequest(user.getId(), organizationDetail.getOrganizationId(), cmd.getCommunityId());
//			OrganizationCommunityRequest organizationCommunityRequest = new OrganizationCommunityRequest();
//			organizationCommunityRequest.setCommunityId(cmd.getCommunityId());
//			organizationCommunityRequest.setMemberType(OrganizationCommunityRequestType.Organization.getCode());
//			organizationCommunityRequest.setMemberId(enterprise.getOrganizationId());
//
//			organizationCommunityRequest.setMemberStatus(OrganizationCommunityRequestStatus.ACTIVE.getCode());
//			organizationCommunityRequest.setCreatorUid(user.getId());
//			organizationCommunityRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//			organizationCommunityRequest.setApproveTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//
//	        this.organizationProvider.createOrganizationCommunityRequest(organizationCommunityRequest);

            // 把企业所在的小区信息放到eh_organization_community_requests表，从eh_organizations表删除掉，以免重复 by lqs 20160512
            //organization.setCommunityId(cmd.getCommunityId());
            organization.setDescription(organizationDetail.getDescription());

            List<AttachmentDescriptor> attachments = cmd.getAttachments();

            if (null != attachments && 0 != attachments.size()) {
                this.addAttachments(organization.getId(), attachments, user.getId());
            }

            List<OrganizationAddressDTO> addressDTOs = cmd.getAddressDTOs();
            if (null != addressDTOs && 0 != addressDTOs.size()) {
                this.addAddresses(organization.getId(), addressDTOs, user.getId());
            }

            //安装默认应用，包括系统 add by yanjun 201805281536
            serviceModuleAppService.installDefaultAppByOrganizationId(organization.getId());

//            createEnterpriseCustomer(organization, cmd.getAvatar(), organizationDetail, cmd.getCommunityId(), addressDTOs);
            return null;
        });

        organizationSearcher.feedDoc(organization);


        return ConvertHelper.convert(organization, OrganizationDTO.class);
    }

    /**
     * 编辑单个公司的属性 TODO MUST BE MODIFY !!!!!!!!!
     * @param cmd
     */
    public void updateEnterpriseDetail(UpdateEnterpriseDetailCommand cmd){
        //所有的操作都保持在一个事务当中
        dbProvider.execute((TransactionStatus status) -> {
            //1.创建一个Organization类的对象
            Organization organization = new Organization();
            //对企业名称进行查重校验
//            checkOrgNameUnique(null, cmd.getNamespaceId(), cmd.getName());
            Organization org = organizationProvider.findOrganizationByName(cmd.getName(), cmd.getNamespaceId());
            if(org != null){
                //说明eh_organizations表中存在改公司，那么我们在判断一下该公司的名称是不是和传进来的公司的名称一致，如果是一致的话，那么就不能对其进行修改，否则
                //就可以进行修改
                organization.setName(org.getName());
            }else{
                //说明传进来的公司的名称和之前的名称不一致，那么我们就将传进来的名称改为现在公司的名称
                //将企业名称封装在Organization对象中
                organization.setName(cmd.getName());
            }

            //将企业简称、人员规模、是否属于管理公司、是否属于服务商封装在OrganizationDetail对象中
            if(cmd.getPmFlag() != null){
                //封装是否是管理公司标志
                organization.setPmFlag(cmd.getPmFlag().byteValue());
                if(cmd.getPmFlag() == Integer.valueOf(TrueOrFalseFlag.TRUE.getCode())){
                    //更新对应的eh_organizations表中的organization_type字段为PM表示的是管理公司
                    organization.setOrganizationType(OrganizationType.PM.getCode());
                } else {
                    organization.setOrganizationType(OrganizationType.ENTERPRISE.getCode());
                }
            }else{
                organization.setPmFlag(OrganizationStatus.UNTREATED.getCode());
                organization.setOrganizationType(OrganizationType.ENTERPRISE.getCode());
            }
            if(cmd.getServiceSupportFlag() != null){
                //封装是否是服务商标志
                organization.setServiceSupportFlag(cmd.getServiceSupportFlag().byteValue());
            }else{
                organization.setServiceSupportFlag(OrganizationStatus.UNTREATED.getCode());
            }

            organization.setId(cmd.getOrganizationId());
            //判断传过来的organizationId是否为空，不为空的话，就根据organizationId来进行更新eh_organizations表中
            //的企业名称
            if(cmd.getOrganizationId() != null){
                //说明传过来的organizationId有值，那么就进行更细企业名称
                organizationProvider.updateOrganizationProperty(organization);
                //开始更新eh_organization_details表中的企业简称、人员规模、是否属于管理公司、是否属于服务商
                //创建一个OrganizationDetail类的对象
                OrganizationDetail organizationDetail = new OrganizationDetail();
                //// TODO: 2018/5/8
                //将企业简称、人员规模、是否属于管理公司、是否属于服务商封装在OrganizationDetail对象中
/*                if(cmd.getPmFlag() != null){
                    //封装是否是管理公司标志
                    organizationDetail.setPmFlag(cmd.getPmFlag().byteValue());
                }else{
                    organizationDetail.setPmFlag(OrganizationStatus.UNTREATED.getCode());
                }
                if(cmd.getServiceSupportFlag() != null){
                    //封装是否是服务商标志
                    organizationDetail.setServiceSupportFlag(cmd.getServiceSupportFlag().byteValue());
                }else{
                    organizationDetail.setServiceSupportFlag(OrganizationStatus.UNTREATED.getCode());
                }*/
                //封装企业logo
                organizationDetail.setAvatar(cmd.getAvatar());
                //封装企业简称
                organizationDetail.setDisplayName(cmd.getDisplayName());
                //封装人员规模
                organizationDetail.setMemberRange(cmd.getMemberRange());
                organizationDetail.setOrganizationId(cmd.getOrganizationId());
                //更新到eh_organization_detail表
                organizationProvider.updateOrganizationDetail(organizationDetail);
            }
            return null;
        });
    }

    /**
     * 更新办公地点以及其中的楼栋和门牌
     * @param cmd
     */
    public void insertWorkPlacesAndBuildings(UpdateWorkPlaceCommand cmd){
        //必须在同一个事物中完成
        dbProvider.execute((TransactionStatus status) -> {
            //向办公地点表中添加数据
            if(cmd.getOfficeSites() != null){
                //说明传过来的所在项目和名称以及其中的楼栋和门牌不为空，那么我们将其进行遍历
                for(CreateOfficeSiteCommand createOfficeSiteCommand :cmd.getOfficeSites()){
                    //这样的话拿到的是每一个办公所在地，以及其中的楼栋和门牌
                    //首先我们将办公地址名称和办公地点id持久化到表eh_organization_workPlaces中
                    //创建OrganizationWorkPlaces类的对象
                    OrganizationWorkPlaces organizationWorkPlaces = new OrganizationWorkPlaces();
                    //将数据封装在对象中
                    organizationWorkPlaces.setCommunityId(createOfficeSiteCommand.getCommunityId());
                    organizationWorkPlaces.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    organizationWorkPlaces.setWorkplaceName(createOfficeSiteCommand.getSiteName());
                    //将上面的organization对象中的id也封装在对象中
                    organizationWorkPlaces.setOrganizationId(cmd.getOrganizationId());
                    organizationWorkPlaces.setWholeAddressName(createOfficeSiteCommand.getWholeAddressName());
                    //调用organizationProvider中的insertIntoOrganizationWorkPlaces方法,将对象持久化到数据库
                    organizationProvider.insertIntoOrganizationWorkPlaces(organizationWorkPlaces);
                    //现在的情况是这样的，我们还需要进行维护一张表eh_enterprise_community_map的信息，这张表其实在标准版中是不适用的
                    //后面的话会逐步的进行淘汰，但是现在当给eh_organization_workPlaces表中添加一调数据的同时，还需要向eh_enterprise_community_map
                    //中添加同样的数据
                    //// TODO: 2018/5/4
                    //创建EnterpriseCommunityMap类的对象
                    EnterpriseCommunityMap enterpriseCommunityMap = new EnterpriseCommunityMap();
                    //将数据封装在对象EnterpriseCommunityMap中
                    enterpriseCommunityMap.setCommunityId(createOfficeSiteCommand.getCommunityId());
                    enterpriseCommunityMap.setMemberId(cmd.getOrganizationId());
                    enterpriseCommunityMap.setMemberType(EnterpriseCommunityMapType.Enterprise.getCode());
                    enterpriseCommunityMap.setMemberStatus(EnterpriseCommunityMapStatus.ACTIVE.getCode());
                    //调用enterpriseProvider中的insertIntoEnterpriseCommunityMap(EnterpriseCommunityMap enterpriseCommunityMap)方法，将数据封装在
                    //表eh_enterprise_community_map中
                    enterpriseProvider.insertIntoEnterpriseCommunityMap(enterpriseCommunityMap);

                    //在这里我们还需要维护eh_organization_community_requests这张表
                    //创建OrganizationCommunityRequest类的对象
                    OrganizationCommunityRequest organizationCommunityRequest = new OrganizationCommunityRequest();
                    //将数据封装在对象OrganizationCommunityRequest对象中
                    organizationCommunityRequest.setCommunityId(createOfficeSiteCommand.getCommunityId());
                    organizationCommunityRequest.setMemberId(cmd.getOrganizationId());
                    organizationCommunityRequest.setMemberType(EnterpriseCommunityMapType.Organization.getCode());
                    organizationCommunityRequest.setMemberStatus(EnterpriseCommunityMapStatus.ACTIVE.getCode());
                    //// TODO: 2018/5/22
                    enterpriseProvider.insertIntoOrganizationCommunityRequest(organizationCommunityRequest);

                    //接下来我们需要将对应的所在项目的楼栋和门牌也持久化到项目和楼栋门牌的关系表eh_communityAndBuilding_relationes中
                    //首先进行遍历楼栋集合
                    if(createOfficeSiteCommand.getSiteDtos() != null){
                        //说明楼栋和门牌不为空，注意他是一个集合
                        //遍历
                        for(OrganizationSiteApartmentDTO organizationSiteApartmentDTO : createOfficeSiteCommand.getSiteDtos()){
                            //这样的话我们拿到的是每一个楼栋以及对应的门牌
                            //创建CommunityAndBuildingRelationes对象，并且将数据封装在对象中，然后持久化到数据库
                            CommunityAndBuildingRelationes communityAndBuildingRelationes = new CommunityAndBuildingRelationes();
                            communityAndBuildingRelationes.setWorkplaceId(organizationWorkPlaces.getId());
                            communityAndBuildingRelationes.setCommunityId(createOfficeSiteCommand.getCommunityId());
                            communityAndBuildingRelationes.setAddressId(organizationSiteApartmentDTO.getApartmentId());
                            communityAndBuildingRelationes.setBuildingId(organizationSiteApartmentDTO.getBuildingId());
                            //调用organizationProvider中的insertIntoCommunityAndBuildingRelationes方法，将对象持久化到数据库
                            organizationProvider.insertIntoCommunityAndBuildingRelationes(communityAndBuildingRelationes);
                        }
                    }
                }
            }
            return null;
        });
    }



    /**
     * 创建企业
     * @param cmd
     * @return
     */
    @Override
    public OrganizationDTO createEnterprise(CreateEnterpriseCommand cmd) {
        if(cmd.getCheckPrivilege() != null && cmd.getCheckPrivilege()) {
            userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), cmd.getManageOrganizationId(),
                    PrivilegeConstants.ORGANIZATION_CREATE, ServiceModuleConstants.ORGANIZATION_MODULE,
                    ActionType.OFFICIAL_URL.getCode(), null, cmd.getManageOrganizationId(), cmd.getCommunityId());
        }
        User user = UserContext.current().getUser();

        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());

        if(org.apache.commons.lang.StringUtils.isNotBlank(cmd.getUnifiedSocialCreditCode())) {
            checkUnifiedSocialCreditCode(cmd.getUnifiedSocialCreditCode(), namespaceId, null);
        }
        checkOrgNameUnique(null, cmd.getNamespaceId(), cmd.getName());

        Organization organization = new Organization();
        dbProvider.execute((TransactionStatus status) -> {

            Group group = new Group();
            group.setName(cmd.getName());
            group.setDisplayName(cmd.getDisplayName());
            group.setEnterpriseAddress(cmd.getAddress());

            group.setDescription(cmd.getContactsPhone());
            group.setStatus(OrganizationStatus.ACTIVE.getCode());

            group.setCreatorUid(user.getId());

            group.setNamespaceId(namespaceId);

            group.setDiscriminator(GroupDiscriminator.ENTERPRISE.getCode());

            group.setPrivateFlag(GroupPrivacy.PRIVATE.getCode());

            groupProvider.createGroup(group);

            organization.setParentId(0L);
            organization.setLevel(1);
            organization.setPath("");
            organization.setName(cmd.getName());
            organization.setGroupType(OrganizationGroupType.ENTERPRISE.getCode());
            organization.setStatus(OrganizationStatus.ACTIVE.getCode());
            organization.setOrganizationType(OrganizationType.ENTERPRISE.getCode());
            organization.setNamespaceId(namespaceId);
            organization.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            organization.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            organization.setGroupId(group.getId());
            organization.setWebsite(cmd.getWebsite());
            organization.setEmailDomain(cmd.getEmailDomain());
            organization.setUnifiedSocialCreditCode(cmd.getUnifiedSocialCreditCode());
            organizationProvider.createOrganization(organization);
            assetService.linkCustomerToBill(AssetTargetType.ORGANIZATION.getCode(), organization.getId(), organization.getName());

            OrganizationDetail organizationDetail = new OrganizationDetail();
            organizationDetail.setOrganizationId(organization.getId());
            organizationDetail.setAddress(cmd.getAddress());
            organizationDetail.setDescription(cmd.getDescription());
            organizationDetail.setAvatar(cmd.getAvatar());
            organizationDetail.setCreateTime(organization.getCreateTime());
            if (!StringUtils.isEmpty(cmd.getCheckinDate())) {
                java.sql.Date checkinDate = DateUtil.parseDate(cmd.getCheckinDate());
                if (null != checkinDate) {
                    organizationDetail.setCheckinDate(new Timestamp(checkinDate.getTime()));
                }
            }
            organizationDetail.setContact(cmd.getContactsPhone());
            organizationDetail.setDisplayName(cmd.getDisplayName());
            organizationDetail.setPostUri(cmd.getPostUri());
            organizationDetail.setMemberCount(cmd.getMemberCount());
            organizationDetail.setEmailDomain(cmd.getEmailDomain());
            organizationDetail.setServiceUserId(cmd.getServiceUserId());
            if (cmd.getLatitude() != null)
                organizationDetail.setLatitude(Double.valueOf(cmd.getLatitude()));
            if (cmd.getLongitude() != null)
                organizationDetail.setLongitude(Double.valueOf(cmd.getLongitude()));
            organizationProvider.createOrganizationDetail(organizationDetail);

            // 把代码移到一个独立的方法，以便其它地方也可以调用 by lqs 20161101
            createActiveOrganizationCommunityRequest(user.getId(), organizationDetail.getOrganizationId(), cmd.getCommunityId());
//			OrganizationCommunityRequest organizationCommunityRequest = new OrganizationCommunityRequest();
//			organizationCommunityRequest.setCommunityId(cmd.getCommunityId());
//			organizationCommunityRequest.setMemberType(OrganizationCommunityRequestType.Organization.getCode());
//			organizationCommunityRequest.setMemberId(enterprise.getOrganizationId());
//
//			organizationCommunityRequest.setMemberStatus(OrganizationCommunityRequestStatus.ACTIVE.getCode());
//			organizationCommunityRequest.setCreatorUid(user.getId());
//			organizationCommunityRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//			organizationCommunityRequest.setApproveTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//
//	        this.organizationProvider.createOrganizationCommunityRequest(organizationCommunityRequest);

            // 把企业所在的小区信息放到eh_organization_community_requests表，从eh_organizations表删除掉，以免重复 by lqs 20160512
            //organization.setCommunityId(cmd.getCommunityId());
            organization.setDescription(organizationDetail.getDescription());

            List<AttachmentDescriptor> attachments = cmd.getAttachments();

            if (null != attachments && 0 != attachments.size()) {
                this.addAttachments(organization.getId(), attachments, user.getId());
            }

            List<OrganizationAddressDTO> addressDTOs = cmd.getAddressDTOs();
            if (null != addressDTOs && 0 != addressDTOs.size()) {
                this.addAddresses(organization.getId(), addressDTOs, user.getId());
            }

            createEnterpriseCustomer(organization, cmd.getAvatar(), organizationDetail, cmd.getCommunityId(), addressDTOs);
            return null;
        });

        organizationSearcher.feedDoc(organization);


        return ConvertHelper.convert(organization, OrganizationDTO.class);
    }

    private void createEnterpriseCustomer(Organization organization, String logo, OrganizationDetail enterprise,
                                          Long communityId, List<OrganizationAddressDTO> addressDTOs) {
//        if(organization.getNamespaceId() == 999971 || organization.getNamespaceId() == 999983) {
//            LOGGER.error("Insufficient privilege, createEnterpriseCustomer");
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
//                    "Insufficient privilege");
//        }
        List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomerByNamespaceIdAndName(organization.getNamespaceId(), communityId, organization.getName());
        if(customers != null && customers.size() > 0) {
            EnterpriseCustomer customer = customers.get(0);
            customer.setOrganizationId(organization.getId());
            customer.setCorpWebsite(organization.getWebsite());
            customer.setCorpLogoUri(logo);
            customer.setCustomerSource(InvitedCustomerType.ENTEPRIRSE_CUSTOMER.getCode());
            customer.setLevelItemId((long)CustomerLevelType.REGISTERED_CUSTOMER.getCode());
            customer.setContactAddress(enterprise.getAddress());
            customer.setLatitude(enterprise.getLatitude());
            customer.setLongitude(enterprise.getLongitude());
            customer.setCorpEntryDate(enterprise.getCheckinDate());
            customer.setCorpOpAddress(enterprise.getAddress());
            customer.setCorpDescription(enterprise.getDescription());
            customer.setCorpEmployeeAmount(enterprise.getMemberCount() == null ? null : enterprise.getMemberCount().intValue());
            customer.setPostUri(enterprise.getPostUri());
            customer.setNickName(enterprise.getDisplayName());
            customer.setHotline(enterprise.getContact());
            customer.setCorpEmail(enterprise.getEmailDomain());
            customer.setUnifiedSocialCreditCode(organization.getUnifiedSocialCreditCode());
//            if(customer.getTrackingUid() == null) {
//                customer.setTrackingUid(-1L);
//            }
            enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
            enterpriseCustomerSearcher.feedDoc(customer);

            //企业管理楼栋与客户tab页的入驻信息双向同步 产品功能22898
            this.updateCustomerEntryInfo(customer, addressDTOs);

        } else {
            EnterpriseCustomer customer = new EnterpriseCustomer();
            customer.setCommunityId(communityId);
            customer.setNamespaceId(organization.getNamespaceId());
            customer.setOrganizationId(organization.getId());
            customer.setName(organization.getName());
            customer.setCorpWebsite(organization.getWebsite());
            customer.setCorpLogoUri(logo);
            customer.setContactAddress(enterprise.getAddress());
            customer.setLatitude(enterprise.getLatitude());
            customer.setLongitude(enterprise.getLongitude());
            customer.setCorpEntryDate(enterprise.getCheckinDate());
            customer.setCorpDescription(enterprise.getDescription());
            customer.setCorpEmployeeAmount(enterprise.getMemberCount() == null ? null : enterprise.getMemberCount().intValue());
            customer.setPostUri(enterprise.getPostUri());
            customer.setNickName(enterprise.getDisplayName());
            customer.setHotline(enterprise.getContact());
            customer.setCorpEmail(enterprise.getEmailDomain());
            customer.setUnifiedSocialCreditCode(organization.getUnifiedSocialCreditCode());
            customer.setCustomerSource(InvitedCustomerType.ENTEPRIRSE_CUSTOMER.getCode());
            customer.setLevelItemId((long)CustomerLevelType.REGISTERED_CUSTOMER.getCode());
//            customer.setTrackingUid(-1L);
            enterpriseCustomerProvider.createEnterpriseCustomer(customer);
            enterpriseCustomerSearcher.feedDoc(customer);

            //企业管理楼栋与客户tab页的入驻信息双向同步 产品功能22898
            this.updateCustomerEntryInfo(customer, addressDTOs);
        }

    }

    private void createActiveOrganizationCommunityRequest(Long creatorId, Long organizationId, Long communityId) {
        OrganizationCommunityRequest organizationCommunityRequest = new OrganizationCommunityRequest();
        organizationCommunityRequest.setCommunityId(communityId);
        organizationCommunityRequest.setMemberType(OrganizationCommunityRequestType.Organization.getCode());
        organizationCommunityRequest.setMemberId(organizationId);

        organizationCommunityRequest.setMemberStatus(OrganizationCommunityRequestStatus.ACTIVE.getCode());
        organizationCommunityRequest.setCreatorUid(creatorId);
        organizationCommunityRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        organizationCommunityRequest.setApproveTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        this.organizationProvider.createOrganizationCommunityRequest(organizationCommunityRequest);
    }

    /**
     * 添加banner图片
     *
     * @param id
     * @param attachments
     */
    private void addAttachments(Long id, List<AttachmentDescriptor> attachments, Long userId) {
        dbProvider.execute((TransactionStatus status) -> {

            this.organizationProvider.deleteOrganizationAttachmentsByOrganizationId(id);
            if (attachments != null && attachments.size() > 0) {
                for (AttachmentDescriptor attachmentDescriptor : attachments) {
                    OrganizationAttachment attachment = ConvertHelper.convert(attachmentDescriptor, OrganizationAttachment.class);
                    attachment.setCreatorUid(userId);
                    attachment.setOrganizationId(id);
                    attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    this.organizationProvider.createOrganizationAttachment(attachment);
                }
            }
            return null;
        });

    }

    private void updateCustomerEntryInfo(EnterpriseCustomer customer, List<OrganizationAddressDTO> addressDTOs) {
        LOGGER.debug("updateCustomerEntryInfo customer id: {}, address: {}", customer.getId(), addressDTOs);
        if (addressDTOs != null && addressDTOs.size() > 0) {
            List<CustomerEntryInfo> entryInfos = enterpriseCustomerProvider.listCustomerEntryInfos(customer.getId());
            List<Long> addressIds = new ArrayList<>();
            if(entryInfos != null && entryInfos.size() > 0) {
                addressIds = entryInfos.stream().map(entryInfo -> {
                    return entryInfo.getAddressId();
                }).collect(Collectors.toList());
            }

            for (OrganizationAddressDTO organizationAddressDTO : addressDTOs) {
                if(addressIds.contains(organizationAddressDTO.getAddressId())) {
                    continue;
                }

                CustomerEntryInfo info = new CustomerEntryInfo();
                info.setNamespaceId(customer.getNamespaceId());
                info.setCustomerType(CustomerType.ENTERPRISE.getCode());
                info.setCustomerId(customer.getId());
                info.setCustomerName(customer.getName());
                info.setAddressId(organizationAddressDTO.getAddressId());
                info.setBuildingId(organizationAddressDTO.getBuildingId());
                enterpriseCustomerProvider.createCustomerEntryInfo(info);
            }

        }
    }

    /**
     * 添加地址
     *
     * @param id
     * @param addressDTOs
     * @param userId
     */
    private void addAddresses(Long id, List<OrganizationAddressDTO> addressDTOs, Long userId) {

        dbProvider.execute((TransactionStatus status) -> {

            this.organizationProvider.deleteOrganizationAddressByOrganizationId(id);
            if (addressDTOs != null && addressDTOs.size() > 0) {
                for (OrganizationAddressDTO organizationAddressDTO : addressDTOs) {
                    Address addr = this.addressProvider.findAddressById(organizationAddressDTO.getAddressId());
                    OrganizationAddress address = ConvertHelper.convert(organizationAddressDTO, OrganizationAddress.class);
                    address.setOrganizationId(id);
                    address.setCreatorUid(userId);
                    address.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    address.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

                    if(addr != null) {
                        //地址冗余字段 added by jannson
                        address.setBuildingName(addr.getBuildingName());
                        Building building = this.communityProvider.findBuildingByCommunityIdAndName(addr.getCommunityId(), addr.getBuildingName());
                        if(building != null) {
                            address.setBuildingId(building.getId());
                            }

                    }

                    address.setStatus(OrganizationAddressStatus.ACTIVE.getCode());



                    this.organizationProvider.createOrganizationAddress(address);
                }
            }

            return null;
        });

    }

    @Override
    public void updateEnterprise(UpdateEnterpriseCommand cmd) {
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), cmd.getManageOrganizationId(), PrivilegeConstants.ORGANIZATION_UPDATE, ServiceModuleConstants.ORGANIZATION_MODULE, ActionType.OFFICIAL_URL.getCode(), null, cmd.getManageOrganizationId(), cmd.getCommunityId());
        updateEnterprise(cmd, true);
    }

    @Override
    public void updateEnterprise(UpdateEnterpriseCommand cmd, boolean updateAttachmentAndAddress) {
        //先判断，后台管理员才能创建。状态直接设为正常
        Organization organization = checkOrganization(cmd.getId());
        User user = UserContext.current().getUser();
        if(org.apache.commons.lang.StringUtils.isNotBlank(cmd.getUnifiedSocialCreditCode())) {
            checkUnifiedSocialCreditCode(cmd.getUnifiedSocialCreditCode(), cmd.getNamespaceId(), cmd.getId());
        }
        checkOrgNameUnique(cmd.getId(), cmd.getNamespaceId(), cmd.getName());
        EnterpriseCustomer customer = enterpriseCustomerProvider.findByOrganizationId(organization.getId());
        dbProvider.execute((TransactionStatus status) -> {
            //查到有关联的客户则同步修改过去
            if(customer != null) {
                //产品功能 #20796 同步过来的客户名称不可改
                if(NamespaceCustomerType.EBEI.equals(NamespaceCustomerType.fromCode(customer.getNamespaceCustomerType()))
                        || NamespaceCustomerType.SHENZHOU.equals(NamespaceCustomerType.fromCode(customer.getNamespaceCustomerType())) ) {
                    if(!customer.getName().equals(cmd.getName())) {
                        LOGGER.error("Insufficient privilege");
                        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                                "Insufficient privilege");
                    }
                }
                customer.setName(cmd.getName());
                customer.setNickName(cmd.getDisplayName());
                customer.setContactAddress(cmd.getAddress());
                customer.setLatitude(cmd.getLatitude());
                customer.setLongitude(cmd.getLongitude());
                customer.setCorpWebsite(organization.getWebsite());
                customer.setCorpLogoUri(cmd.getAvatar());
                customer.setPostUri(cmd.getPostUri());
                customer.setHotline(cmd.getContactsPhone());
                customer.setCorpDescription(cmd.getDescription());
                Date date = DateUtil.parseDate(cmd.getCheckinDate());
                if (date != null) {
                    customer.setCorpEntryDate(new Timestamp(date.getTime()));
                }
                customer.setUnifiedSocialCreditCode(cmd.getUnifiedSocialCreditCode());
                customer.setCorpEmail(cmd.getEmailDomain());
                customer.setCorpEmployeeAmount(cmd.getMemberCount() == null ? null : cmd.getMemberCount().intValue());

                enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
                enterpriseCustomerSearcher.feedDoc(customer);
            }

            organization.setId(cmd.getId());
            organization.setName(cmd.getName());
            organization.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            organization.setEmailDomain(cmd.getEmailDomain());
            organization.setUnifiedSocialCreditCode(cmd.getUnifiedSocialCreditCode());
            organization.setWebsite(cmd.getWebsite());
            organizationProvider.updateOrganization(organization);

            OrganizationDetail organizationDetail = organizationProvider.findOrganizationDetailByOrganizationId(organization.getId());
            if (null == organizationDetail) {
                organizationDetail = new OrganizationDetail();
                organizationDetail.setOrganizationId(organization.getId());
                organizationDetail.setCreateTime(organization.getCreateTime());
                organizationDetail.setAddress(cmd.getAddress());
                organizationDetail.setDescription(cmd.getDescription());
                organizationDetail.setEmailDomain(cmd.getEmailDomain());
                organizationDetail.setAvatar(cmd.getAvatar());
                if (!StringUtils.isEmpty(cmd.getCheckinDate())) {
                    java.sql.Date checkinDate = DateUtil.parseDate(cmd.getCheckinDate());
                    if (null != checkinDate) {
                        organizationDetail.setCheckinDate(new Timestamp(checkinDate.getTime()));
                    }
                } else {
                    organizationDetail.setCheckinDate(null);
                }
                organizationDetail.setContact(cmd.getContactsPhone());
                organizationDetail.setDisplayName(cmd.getDisplayName());
                organizationDetail.setPostUri(cmd.getPostUri());
                organizationDetail.setServiceUserId(cmd.getServiceUserId());
                organizationDetail.setLatitude(cmd.getLatitude());
                organizationDetail.setLongitude(cmd.getLongitude());
                organizationDetail.setMemberCount(cmd.getMemberCount());
                organizationProvider.createOrganizationDetail(organizationDetail);
            } else {
                organizationDetail.setEmailDomain(cmd.getEmailDomain());
                organizationDetail.setAddress(cmd.getAddress());
                organizationDetail.setDescription(cmd.getDescription());
                organizationDetail.setAvatar(cmd.getAvatar());
                if (!StringUtils.isEmpty(cmd.getCheckinDate())) {
                    java.sql.Date checkinDate = DateUtil.parseDate(cmd.getCheckinDate());
                    if (null != checkinDate) {
                        organizationDetail.setCheckinDate(new Timestamp(checkinDate.getTime()));
                    }
                } else {
                    organizationDetail.setCheckinDate(null);
                }
                organizationDetail.setContact(cmd.getContactsPhone());
                organizationDetail.setDisplayName(cmd.getDisplayName());
                organizationDetail.setPostUri(cmd.getPostUri());
                organizationDetail.setServiceUserId(cmd.getServiceUserId());
                organizationDetail.setLatitude(cmd.getLatitude());
                organizationDetail.setLongitude(cmd.getLongitude());
                organizationDetail.setMemberCount(cmd.getMemberCount());
                organizationProvider.updateOrganizationDetail(organizationDetail);
            }
            // 把小区ID移到eh_organization_community_requests后，在企业更新时需要补充小区修改，但修改小区逻辑未想好故暂时不支持 by lqs 20160512
//			if(cmd.getCommunityId() != null) {
//			    OrganizationCommunityRequest orgCmntyRequest = organizationProvider.getOrganizationCommunityRequestByOrganizationId(organization.getId());
//			    if(orgCmntyRequest != null) {
//			        orgCmntyRequest.setCommunityId(cmd.getCommunityId());
//			        organizationProvider.updateOrganizationCommunityRequest(orgCmntyRequest);
//			    }
//			}
            // 把企业所在的小区信息放到eh_organization_community_requests表，从eh_organizations表删除掉，以免重复 by lqs 20160512
            // organization.setCommunityId(cmd.getCommunityId());
            organization.setDescription(organizationDetail.getDescription());
            List<OrganizationAddressDTO> addressDTOs = cmd.getAddressDTOs();
            // fix 26997 不管有没有传门牌 都要处理
            this.addAddresses(organization.getId(), addressDTOs, user.getId());
            organizationSearcher.feedDoc(organization);

            //没有有关联的客户则新增一条
            if(customer == null) {
                EnterpriseCustomer enterpriseCustomer = new EnterpriseCustomer();
                enterpriseCustomer.setCommunityId(cmd.getCommunityId());
                enterpriseCustomer.setNamespaceId(organization.getNamespaceId());
                enterpriseCustomer.setOrganizationId(organization.getId());
                enterpriseCustomer.setName(organization.getName());
                enterpriseCustomer.setCorpWebsite(organization.getWebsite());
                enterpriseCustomer.setCorpLogoUri(cmd.getAvatar());
                enterpriseCustomer.setContactAddress(organizationDetail.getAddress());
                enterpriseCustomer.setLatitude(organizationDetail.getLatitude());
                enterpriseCustomer.setLongitude(organizationDetail.getLongitude());
                enterpriseCustomer.setCorpEmail(organizationDetail.getEmailDomain());
                enterpriseCustomerProvider.createEnterpriseCustomer(enterpriseCustomer);
                enterpriseCustomerSearcher.feedDoc(enterpriseCustomer);

                //企业管理楼栋与客户tab页的入驻信息双向同步 产品功能22898
                this.updateCustomerEntryInfo(enterpriseCustomer, addressDTOs);
            }

            return null;
        });

        if (updateAttachmentAndAddress) {
            List<AttachmentDescriptor> attachments = cmd.getAttachments();

//			if(null != attachments && 0 != attachments.size()){
            this.addAttachments(organization.getId(), attachments, user.getId());
        }
    }

    @Override
    public void deleteEnterpriseById(DeleteOrganizationIdCommand cmd, Boolean checkAuth) {
        if(checkAuth) {
            userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), cmd.getManageOrganizationId(), PrivilegeConstants.ORGANIZATION_DELETE, ServiceModuleConstants.ORGANIZATION_MODULE, ActionType.OFFICIAL_URL.getCode(), null, cmd.getManageOrganizationId(), cmd.getCommunityId());
        }

        Organization organization = checkOrganization(cmd.getId());
        //产品功能 #20796
//        if(organization.getNamespaceId() == 999971 || organization.getNamespaceId() == 999983) {
//            LOGGER.error("Insufficient privilege, createEnterprise");
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
//                    "Insufficient privilege");
//        }
        User user = UserContext.current().getUser();

        Long startTime = DateHelper.currentGMTTime().getTime();
		dbProvider.execute((TransactionStatus status) -> {
            //已删除则直接跳出
            if(OrganizationStatus.DELETED.equals(OrganizationStatus.fromCode(organization.getStatus()))) {
                return null;
            }

            //如在客户管理中有则同步删除
            EnterpriseCustomer customer = enterpriseCustomerProvider.findByOrganizationId(organization.getId());
            if(customer != null) {
                DeleteEnterpriseCustomerCommand command = new DeleteEnterpriseCustomerCommand();
                command.setId(customer.getId());
                command.setCommunityId(customer.getCommunityId());
                command.setNamespaceId(customer.getNamespaceId());
                customerService.deleteEnterpriseCustomer(command, false);
            }

            Long deleteEnterpriseCustomerTime = DateHelper.currentGMTTime().getTime();
            LOGGER.debug("deleteEnterpriseById deleteEnterpriseCustomer els: {}", deleteEnterpriseCustomerTime - startTime);

            organization.setStatus(OrganizationStatus.DELETED.getCode());
            Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
            organization.setUpdateTime(now);
            organizationProvider.updateOrganization(organization);

            OrganizationCommunityRequest r = organizationProvider.getOrganizationCommunityRequestByOrganizationId(organization.getId());

            if (null != r) {
                r.setMemberStatus(OrganizationCommunityRequestStatus.INACTIVE.getCode());
                r.setUpdateTime(now);
                organizationProvider.updateOrganizationCommunityRequest(r);
            }

            Long updateOrganizationTime = DateHelper.currentGMTTime().getTime();
            LOGGER.debug("deleteEnterpriseById updateOrganization els: {}", updateOrganizationTime - deleteEnterpriseCustomerTime);

            List<OrganizationMember> members = organizationProvider.listOrganizationMembers(organization.getId(), null);
            //把user_organization表中的相应记录更新为失效
            inactiveUserOrganizationWithMembers(members);

            Long membersTime = DateHelper.currentGMTTime().getTime();
            LOGGER.debug("deleteEnterpriseById inactiveUserOrganizationWithMembers els: {}", membersTime - updateOrganizationTime);

            List<OrganizationAddress> organizationAddresses = organizationProvider.findOrganizationAddressByOrganizationId(organization.getId());

            if (null != organizationAddresses && 0 != organizationAddresses.size()) {
                for (OrganizationAddress organizationAddress : organizationAddresses) {
                    organizationAddress.setStatus(OrganizationAddressStatus.INACTIVE.getCode());
                    organizationAddress.setUpdateTime(now);
                    organizationProvider.updateOrganizationAddress(organizationAddress);
                }
            }
            Long organizationAddressesTime = DateHelper.currentGMTTime().getTime();
            LOGGER.debug("deleteEnterpriseById updateOrganizationAddress els: {}", organizationAddressesTime - membersTime);

            /**modify by lei.lv**/
            //删除organizaiton时，需要把organizaiton下面的所有机构状态置为无效，而且把人员和机构的关系置为无效状态
            List<Organization> underOrganiztions = organizationProvider.findOrganizationByPath(organization.getPath());
            underOrganiztions.stream().map((o) -> {
                //更新机构
                o.setStatus(OrganizationStatus.INACTIVE.getCode());
                o.setUpdateTime(now);
                organizationProvider.updateOrganization(o);
                //更新人员
                List<OrganizationMember> underOrganiztionsMembers = organizationProvider.listOrganizationMembersByOrgIdWithAllStatus(o.getId());
                for (OrganizationMember m : underOrganiztionsMembers) {
                    m.setStatus(OrganizationMemberStatus.INACTIVE.getCode());
                    m.setUpdateTime(now);
                    m.setOperatorUid(user.getId());
                    organizationProvider.updateOrganizationMember(m);
                    //解除门禁权限
                    doorAccessService.deleteAuthWhenLeaveFromOrg(UserContext.getCurrentNamespaceId(), m.getOrganizationId(), m.getTargetId());
                }
                return null;
            }).collect(Collectors.toList());

            Long underOrganiztionsTime = DateHelper.currentGMTTime().getTime();
            LOGGER.debug("deleteEnterpriseById underOrganiztions els: {}", underOrganiztionsTime - organizationAddressesTime);
            organizationSearcher.deleteById(cmd.getId());
            return null;
        });
        Long endTime = DateHelper.currentGMTTime().getTime();
        LOGGER.debug("deleteEnterpriseById total els: {}", endTime - startTime);
    }

    @Override
    public void applyOrganizationMember(ApplyOrganizationMemberCommand cmd) {
        this.checkCommunityIdIsNull(cmd.getCommunityId());
        this.checkCommunity(cmd.getCommunityId());
        User user = UserContext.current().getUser();
        this.checkCommunityIdIsEqual(user.getCommunityId().longValue(), cmd.getCommunityId().longValue());
        Organization organization = this.checkOrganizationByCommIdAndOrgType(cmd.getCommunityId(), cmd.getOrganizationType());

        this.checkUserInOrg(user.getId(), organization.getId());
        OrganizationMember member = this.createOrganizationMember(user, organization.getId(), cmd.getContactDescription());
        member.setGroupType(organization.getGroupType());
        member.setGroupPath(organization.getPath());
        organizationProvider.createOrganizationMember(member);
    }

    private OrganizationMember checkUserNotInOrg(Long userId, Long orgId) {
        OrganizationMember member = this.organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, orgId);
        if (member == null) {
            LOGGER.error("User is not in the organization.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "User is not in the organization.");
        }
        return member;
    }

    private void checkUserInOrg(Long userId, Long orgId) {
        OrganizationMember member = this.organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, orgId);
        if (member != null) {
            LOGGER.error("User is in the organization.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "User is in the organization.");
        }
    }

    private Organization checkOrganizationByCommIdAndOrgType(Long communityId, String orgType) {
        Organization org = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(communityId, orgType);
        if (org == null) {
            LOGGER.error("organization can not find by communityId and orgType.communityId=" + communityId + ",orgType=" + orgType);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "organization can not find by communityId and orgType.");
        }
        return org;
    }

    private void checkCommunityIdIsNull(Long communityId) {
        if (communityId == null) {
            LOGGER.error("communityId paramter is empty");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "communityId paramter is empty");
        }

    }

    private void checkCommunityIdIsEqual(long longValue, long longValue2) {
        if (longValue != longValue2) {
            LOGGER.error("communityId not equal.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "communityId not equal.");
        }
    }

    private OrganizationMember createOrganizationMember(User user, Long organizationId, String contactDescription) {
        OrganizationMember member = new OrganizationMember();

        member.setContactDescription(contactDescription);
        member.setContactName(user.getNickName());
        member.setOrganizationId(organizationId);
        member.setStatus(OrganizationMemberStatus.WAITING_FOR_APPROVAL.getCode());
        member.setTargetId(user.getId());
        member.setTargetType(OrganizationMemberTargetType.USER.getCode());

        UserIdentifier identifier = this.getUserMobileIdentifier(user.getId());
        if (identifier != null) {
            member.setContactToken(identifier.getIdentifierToken());
            member.setContactType(identifier.getIdentifierType());
        }

        return member;
    }

    private UserIdentifier getUserMobileIdentifier(Long userId) {
        List<UserIdentifier> userIndIdentifiers = userProvider.listUserIdentifiersOfUser(userId);
        if (userIndIdentifiers != null && userIndIdentifiers.size() > 0) {
            for (UserIdentifier userIdentifier : userIndIdentifiers) {
                if (userIdentifier.getIdentifierType().byteValue() == IdentifierType.MOBILE.getCode()) {
                    return userIdentifier;
                }
            }
        }
        return null;
    }

    @Deprecated
    @Override
    public void createOrganizationMember(CreateOrganizationMemberCommand cmd) {
        //先判断，后台管理员才能创建。状态直接设为正常
        this.convertCreateOrganizationMemberCommand(cmd);
        OrganizationMemberTargetType targetType = OrganizationMemberTargetType.fromCode(cmd.getTargetType());
        Organization org = this.checkOrganization(cmd.getGroupId());
        if (targetType == OrganizationMemberTargetType.USER) {//添加已注册用户为管理员。
            Long addUserId = cmd.getTargetId();
            if (addUserId != null && addUserId.longValue() != 0) {
                this.checkUserInOrg(addUserId, cmd.getOrganizationId());
                User addUser = userProvider.findUserById(addUserId);

                OrganizationMemberGroupType memberGroup = OrganizationMemberGroupType.fromCode(cmd.getMemberGroup());
                cmd.setMemberGroup(memberGroup.getCode());

                OrganizationMember departmentMember = ConvertHelper.convert(cmd, OrganizationMember.class);
                departmentMember.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
                departmentMember.setContactName(addUser.getNickName());
                departmentMember.setGroupPath(org.getPath());

                UserIdentifier identifier = this.getUserMobileIdentifier(addUserId);
                if (identifier != null) {
                    departmentMember.setContactToken(identifier.getIdentifierToken());
                }
                departmentMember.setContactType(IdentifierType.MOBILE.getCode());
                departmentMember.setTargetId(identifier.getOwnerUid());
                departmentMember.setGroupType(org.getGroupType());
                organizationProvider.createOrganizationMember(departmentMember);

                //记录添加log
                OrganizationMemberLog orgLog = ConvertHelper.convert(cmd, OrganizationMemberLog.class);
                orgLog.setOrganizationId(departmentMember.getOrganizationId());
                orgLog.setContactName(departmentMember.getContactName());
                orgLog.setContactToken(departmentMember.getContactToken());
                orgLog.setUserId(departmentMember.getTargetId());
                orgLog.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                orgLog.setOperationType(OperationType.JOIN.getCode());
                orgLog.setRequestType(RequestType.ADMIN.getCode());
                orgLog.setOperatorUid(UserContext.current().getUser().getId());
                orgLog.setContactDescription(departmentMember.getContactDescription());
                this.organizationProvider.createOrganizationMemberLog(orgLog);
            }
        } else {//添加未注册用户为管理员。
            OrganizationMember departmentMember = ConvertHelper.convert(cmd, OrganizationMember.class);
            departmentMember.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
            departmentMember.setContactType(IdentifierType.MOBILE.getCode());
            departmentMember.setTargetId(0l);
            departmentMember.setGroupPath(org.getPath());
            departmentMember.setGroupType(org.getGroupType());

            organizationProvider.createOrganizationMember(departmentMember);

            //记录添加log
            OrganizationMemberLog orgLog = ConvertHelper.convert(cmd, OrganizationMemberLog.class);
            orgLog.setUserId(departmentMember.getTargetId());
            orgLog.setRequestType(RequestType.ADMIN.getCode());
            orgLog.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            orgLog.setOperationType(OperationType.JOIN.getCode());
            orgLog.setOperatorUid(UserContext.current().getUser().getId());
            orgLog.setContactDescription(departmentMember.getContactDescription());
            this.organizationProvider.createOrganizationMemberLog(orgLog);
        }

    }


    @Override
    public void createRoleOrganizationMember(CreateOrganizationMemberCommand cmd) {


    }

    private void convertCreateOrganizationMemberCommand(CreateOrganizationMemberCommand cmd) {
        OrganizationMemberGroupType memberGroup = OrganizationMemberGroupType.fromCode(cmd.getMemberGroup());
        if (memberGroup == null)
            cmd.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
        if (cmd.getTargetType() != null)
            cmd.setTargetType(cmd.getTargetType().toUpperCase());
    }

    @Override
    public void createOrganizationCommunity(CreateOrganizationCommunityCommand cmd) {
        Organization organization = this.checkOrganization(cmd.getOrganizationId());
        if (organization.getOrganizationType().equals(OrganizationType.PM.getCode()) || organization.getOrganizationType().equals(OrganizationType.GARC.getCode())) {
            LOGGER.error("pm or garc could not create community mapping.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "pm or garc could not create community mapping.");
        }
        List<Long> communityIds = cmd.getCommunityIds();
        if (communityIds != null && communityIds.size() > 0) {
            for (Long id : communityIds) {
                this.checkCommunity(id);
                OrganizationCommunity departmentCommunity = new OrganizationCommunity();
                departmentCommunity.setCommunityId(id);
                departmentCommunity.setOrganizationId(cmd.getOrganizationId());
                organizationProvider.createOrganizationCommunity(departmentCommunity);
            }
        }
    }

    /**
     * 标准版里pm or garc可以创建管理关系
     * @param orgId
     * @param communityId
     */
    @Override
    public void createOrganizationCommunity(Long orgId, Long communityId) {
        this.checkOrganization(orgId);
        this.checkCommunity(communityId);
        OrganizationCommunity departmentCommunity = new OrganizationCommunity();
        departmentCommunity.setCommunityId(communityId);
        departmentCommunity.setOrganizationId(orgId);
        organizationProvider.createOrganizationCommunity(departmentCommunity);

    }

    @Override
    public void createOrganizationCommunityByAdmin(CreateOrganizationCommunityCommand cmd) {
        Organization organization = this.checkOrganization(cmd.getOrganizationId());
        if (organization.getOrganizationType().equals(OrganizationType.PM.getCode()) || organization.getOrganizationType().equals(OrganizationType.GARC.getCode())) {
            OrganizationCommunity orgComm = this.organizationProvider.findOrganizationCommunityByOrgId(cmd.getOrganizationId());
            if (orgComm != null) {
                LOGGER.error("pm or garc community mapping was created.");
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        "pm or garc community mapping was created.");
            }
        }
        List<Long> communityIds = cmd.getCommunityIds();
        if (communityIds != null && communityIds.size() > 0) {
            for (Long id : communityIds) {
                this.checkCommunity(id);
                OrganizationCommunity departmentCommunity = new OrganizationCommunity();
                departmentCommunity.setCommunityId(id);
                departmentCommunity.setOrganizationId(cmd.getOrganizationId());
                organizationProvider.createOrganizationCommunity(departmentCommunity);
            }
        }

    }

    @Override
    public void deleteOrganizationCommunity(DeleteOrganizationCommunityCommand cmd) {
        this.checkOrganizationIdIsNull(cmd.getOrganizationId());
        this.checkCommunityIdsIsNull(cmd.getCommunityIds());
        this.dbProvider.execute(s -> {
            for (Long id : cmd.getCommunityIds()) {
                OrganizationCommunity orgCommunity = this.checkOrgCommuByOrgIdAndCommId(cmd.getOrganizationId(), id);
                organizationProvider.deleteOrganizationCommunity(orgCommunity);
            }
            return s;
        });

    }

    private OrganizationCommunity checkOrgCommuByOrgIdAndCommId(Long organizationId, Long communityId) {
        OrganizationCommunity orgCommunity = organizationProvider.findOrganizationCommunityByOrgIdAndCmmtyId(organizationId, communityId);
        if (orgCommunity == null) {
            LOGGER.error("organization community not found.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "organization community not found.");
        }
        return orgCommunity;
    }

    private void checkCommunityIdsIsNull(List<Long> communityIds) {
        if (communityIds == null || communityIds.isEmpty()) {
            LOGGER.error("communityIds is empty.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "communityIds is empty.");
        }

    }

    @Override
    public void createPropertyOrganization(CreatePropertyOrganizationCommand cmd) {
        this.checkCommunityIdIsNull(cmd.getCommunityId());
        Community community = this.checkCommunity(cmd.getCommunityId());

        this.dbProvider.execute(s -> {
            Organization organization = new Organization();
            organization.setLevel(0);
            organization.setAddressId(cmd.getAddressId());
            organization.setName(community.getName() + "物业");
            organization.setOrganizationType(OrganizationType.PM.getCode());
            organization.setParentId(0l);
            organization.setStatus(OrganizationStatus.ACTIVE.getCode());
            organizationProvider.createOrganization(organization);
            OrganizationCommunity departmentCommunity = new OrganizationCommunity();
            departmentCommunity.setCommunityId(community.getId());
            departmentCommunity.setOrganizationId(organization.getId());
            organizationProvider.createOrganizationCommunity(departmentCommunity);

            return s;
        });
    }

    @Override
    public ListOrganizationsCommandResponse listOrganizations(ListOrganizationsCommand cmd) {
        ListOrganizationsCommandResponse response = new ListOrganizationsCommandResponse();
        cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
        int totalCount = organizationProvider.countOrganizations(cmd.getOrganizationType(), cmd.getName());
        if (totalCount == 0) return response;

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        int pageCount = getPageCount(totalCount, pageSize);

        List<Organization> result = organizationProvider.listOrganizations(cmd.getOrganizationType(), cmd.getName(), cmd.getPageOffset(), pageSize);
        response.setDtos(result.stream()
                .map(r -> {
                    return ConvertHelper.convert(r, OrganizationDTO.class);
                }).collect(Collectors.toList()));

        response.setNextPageOffset(cmd.getPageOffset() == pageCount ? null : cmd.getPageOffset() + 1);
        return response;
    }

    @Override
    public ListOrganizationMemberCommandResponse getUserOwningOrganizations() {
        User user = UserContext.current().getUser();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        ListOrganizationMemberCommandResponse response = new ListOrganizationMemberCommandResponse();
        List<OrganizationMember> result = organizationProvider.listOrganizationMembers(user.getId());
        if (result != null && result.size() > 0) {
            List<OrganizationMemberDTO> members = new ArrayList<OrganizationMemberDTO>();
            for (OrganizationMember organizationMember : result) {
                Organization organization = organizationProvider.findOrganizationById(organizationMember.getOrganizationId());

                //Filter out the inactive organization add by sfyan 20130430
                OrganizationStatus orgStatus = OrganizationStatus.fromCode(organization.getStatus());
                if (orgStatus != OrganizationStatus.ACTIVE) {
                    LOGGER.error("The member is ignored for organization not active, userId=" + user.getId()
                            + ", organizationId=" + organizationMember.getOrganizationId() + ", orgMemberId=" + organizationMember.getId()
                            + ", namespaceId=" + namespaceId + ", orgStatus" + orgStatus);
                    continue;
                }
                if (OrganizationType.PM != OrganizationType.fromCode(organization.getOrganizationType())) {
                    OrganizationMemberDTO dto = ConvertHelper.convert(organizationMember, OrganizationMemberDTO.class);
                    dto.setOrganizationName(organization.getName());
                    members.add(dto);
                }
            }
            if (members != null && members.size() > 0) {
                response.setMembers(members);
                UserProfile userProfile = userActivityProvider.findUserProfileBySpecialKey(user.getId(), "currentOrganizationName");
                if (userProfile == null) {
                    Long organizationId = members.get(0).getOrganizationId();
                    Organization organization = organizationProvider.findOrganizationById(organizationId);
                    userProfile = new UserProfile();
                    userProfile.setItemKind(ItemKind.ENTITY.getCode());
                    userProfile.setOwnerId(user.getId());
                    userProfile.setItemName("currentOrganizationName");
                    userProfile.setItemValue(String.valueOf(organization.getId()));
                    userActivityProvider.addUserProfile(userProfile);
                }
            }
        }
        return response;
    }

    @Override
    public UserTokenCommandResponse findUserByIndentifier(UserTokenCommand cmd) {
        User operator = UserContext.current().getUser();
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());

        UserTokenCommandResponse commandResponse = new UserTokenCommandResponse();
        User user = userService.findUserByIndentifier(namespaceId, cmd.getUserIdentifier());
        if (user != null) {
            List<String> phones = new ArrayList<String>();
            phones.add(cmd.getUserIdentifier());
            UserInfo userInfo = ConvertHelper.convert(user, UserInfo.class);
            userInfo.setPhones(phones);
            commandResponse.setUser(userInfo);
        }
        return commandResponse;
    }

    @Override
    public ListOrganizationMemberCommandResponse listOrgMembers(ListOrganizationMemberCommand cmd) {
        ListOrganizationMemberCommandResponse response = new ListOrganizationMemberCommandResponse();

        cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset().longValue(), pageSize);

        List<OrganizationMember> result = organizationProvider.listOrganizationMembers(cmd.getOrganizationId(), null, offset, pageSize + 1);
        if (result != null && !result.isEmpty()) {
            if (result.size() == pageSize + 1) {
                result.remove(result.size() - 1);
                response.setNextPageOffset(cmd.getPageOffset() + 1);
            }

            response.setMembers(result.stream()
                    .map(r -> {
                        OrganizationMemberDTO dto = ConvertHelper.convert(r, OrganizationMemberDTO.class);
                        Organization organization = organizationProvider.findOrganizationById(dto.getOrganizationId());
                        dto.setOrganizationName(organization.getName());
                        return dto;
                    }).collect(Collectors.toList()));
        }
        return response;
    }


    @Override
    public PostDTO createTopic(NewTopicCommand cmd) {
        if(cmd.getForumId() == null){
            Forum forum = forumService.findFourmByNamespaceId(cmd.getNamespaceId());
            if(forum != null){
                cmd.setForumId(forum.getId());
            }

        }

        if (cmd.getForumId() == null ||
                cmd.getVisibleRegionType() == null ||
                cmd.getContentCategory() == null ||
                cmd.getCreatorTag() == null || cmd.getCreatorTag().equals("") ||
                cmd.getTargetTag() == null || cmd.getTargetTag().equals("") ||
                cmd.getSubject() == null || cmd.getSubject().equals("")) {
            LOGGER.error("ForumId or visibleRegionId or visibleRegionType or creatorTag or targetTag or subject is null or empty.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "ForumId or visibleRegionId or visibleRegionType or creatorTag or targetTag or subject is null or empty.");
        }
        this.convertNewTopicCommand(cmd);

        //不检查org，新的发布方式可以发布到几个园区、公司和全部，不方便检查 edit by yanjun 20170823
//        Organization organization = getOrganization(cmd);
//        if (organization == null) {
//            LOGGER.error("Unable to find the organization.");
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_CLASS_NOT_FOUND,
//                    "Unable to find the organization.");
//        }
        //Temporarily do not check, delete by sfyan, 20160511
//		this.checkUserHaveRightToNewTopic(cmd,organization);
        /*if(cmd.getEmbeddedAppId() == null)
            cmd.setEmbeddedAppId(AppConstants.APPID_ORGTASK);*/
        PostDTO post = forumService.createTopic(cmd);
        return post;
    }

    private void convertNewTopicCommand(NewTopicCommand cmd) {
        //convert attachments contentType to upper case.
        List<AttachmentDescriptor> attachments = cmd.getAttachments();
        if (attachments != null && !attachments.isEmpty()) {
            for (AttachmentDescriptor r : attachments) {
                if (r.getContentType() != null && !r.getContentType().equals(""))
                    r.setContentType(r.getContentType().toUpperCase());
            }
        }

    }

    private void checkUserHaveRightToNewTopic(NewTopicCommand cmd, Organization organization) {
        User user = UserContext.current().getUser();
        PostEntityTag creatorTag = PostEntityTag.fromCode(cmd.getCreatorTag());
        if (creatorTag == null) {
            LOGGER.error("creatorTag format is wrong.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "creatorTag format is wrong.");
        }
        if (!creatorTag.getCode().equals(PostEntityTag.USER.getCode())) {
            OrganizationMember member = this.organizationProvider.findOrganizationMemberByUIdAndOrgId(user.getId(), organization.getId());
            if (member == null) {
                LOGGER.error("could not found member in the organization.");
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        "could not found member in the organization.");
            }
        }
    }


    private Organization getOrganization(NewTopicCommand cmd) {
        PostEntityTag creatorTag = PostEntityTag.fromCode(cmd.getCreatorTag());
        PostEntityTag targetTag = PostEntityTag.fromCode(cmd.getTargetTag());

        Organization organization = null;
        if (creatorTag == null) {
            LOGGER.error("creatorTag could not be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "creatorTag could not be null.");
        }
        if (targetTag == null) {
            LOGGER.error("targetTag could not be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "targetTag could not be null.");
        }
        //PostEntityTag.USER
        switch (targetTag) {
            case USER:
//			if(creatorTag.getCode().equals(PostEntityTag.PM.getCode()) || creatorTag.getCode().equals(PostEntityTag.GARC.getCode())){
//				organization = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(cmd.getVisibleRegionId(), cmd.getCreatorTag());break;
//			}
//			else if(!creatorTag.getCode().equals(PostEntityTag.USER.getCode())){
//				organization = this.organizationProvider.findOrganizationById(cmd.getVisibleRegionId());break;
//			}
                //update by tt, 20160804
                if (!PostEntityTag.USER.getCode().equals(creatorTag.getCode()) && cmd.getVisibleRegionType() != null
                        && VisibleRegionType.REGION.getCode() == cmd.getVisibleRegionType().byteValue()) {
                    organization = this.organizationProvider.findOrganizationById(cmd.getVisibleRegionId());
                } else {
                    organization = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(cmd.getVisibleRegionId(), cmd.getCreatorTag());
                }
                break;
            case PM:
            case GARC:
                organization = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(cmd.getVisibleRegionId(), cmd.getTargetTag());
                break;
            case GANC:
            case GAPS:
            case GACW:
                organization = this.organizationProvider.findOrganizationById(cmd.getVisibleRegionId());
                break;
            default:
                LOGGER.error("creatorTag or targetTag format is wrong.");
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        "creatorTag or targetTag format is wrong.");
        }

        return organization;
    }

    @Override
    public ListPostCommandResponse queryTopicsByCategory(QueryOrganizationTopicCommand cmd) {

        return this.forumService.queryOrganizationTopics(cmd);
    }

    @Override
    public ListPostCommandResponse listOrgTopics(QueryOrganizationTopicCommand cmd) {

        return this.forumService.listOrgTopics(cmd);
    }

    @Override
    public ListPostCommandResponse listTopics(ListTopicCommand cmd) {
        Long communityId = cmd.getCommunityId();
        this.checkCommunityIdIsNull(communityId);
        this.checkCommunity(communityId);
        return forumService.listTopics(cmd);
    }

    @Override
    public ListPostCommandResponse listOrgMixTopics(ListOrgMixTopicCommand cmd) {
        ListPostCommandResponse response = new ListPostCommandResponse();
        OrganizationTopicMixType mixType = OrganizationTopicMixType.fromCode(cmd.getMixType());
        if (mixType == null) {
            LOGGER.error("Invalid mix type, cmd=" + cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid mix type");
        }

        List<Long> forumIdList = new ArrayList<Long>();

        Group groupDto = null;
        Long organizationId = cmd.getOrganizationId();
        switch (mixType) {
            case CHILDREN_ALL:
                Organization organization = organizationProvider.findOrganizationById(organizationId);
                if (organization == null) {
                    LOGGER.error("Organization not found, cmd=" + cmd);
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                            "Organization not found");
                } else {
                    if (organization.getGroupId() != null) {
                        groupDto = groupProvider.findGroupById(organization.getGroupId());
                    }
                    if (groupDto != null) {
                        forumIdList.add(groupDto.getOwningForumId());
                    }
                }

                groupDto = null; // 如果缺少这一句，则即使每个子公司都没有group，仍然会加到论坛列表中 by lqs 20160429
                List<String> groupTypes = new ArrayList<String>();
                groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
                List<Organization> subOrgList = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "/%", groupTypes);
                for (Organization subOrg : subOrgList) {
                    if (subOrg.getGroupId() != null) {
                        groupDto = groupProvider.findGroupById(subOrg.getGroupId());
                    }
                    // 添加forumId不为空，空的话后面会报空指针异常。在forum-2.0中，新建帖子和查询帖子的分类都已把子公司去除了。
                    // 此处查询"全部"暂时保留   add by yanjun 20170616
                    if (groupDto != null && !StringUtils.isEmpty(groupDto.getOwningForumId())) {
                        forumIdList.add(groupDto.getOwningForumId());
                    }
                }
                ListTopicByForumCommand forumCmd = new ListTopicByForumCommand();
                forumCmd.setForumIdList(forumIdList);
                forumCmd.setPageAnchor(cmd.getPageAnchor());
                forumCmd.setPageSize(cmd.getPageSize());
                forumCmd.setExcludeCategories(cmd.getExcludeCategories());
                forumCmd.setCategoryId(cmd.getCategoryId());
                forumCmd.setTag(cmd.getTag());
            	forumCmd.setForumEntryId(cmd.getForumEntryId());
                response = forumService.listTopicsByForums(forumCmd);
                break;
            case COMMUNITY_ALL:
                QueryOrganizationTopicCommand command = ConvertHelper.convert(cmd, QueryOrganizationTopicCommand.class);
                command.setOrganizationId(organizationId);
                command.setPrivateFlag(PostPrivacy.PRIVATE.getCode());

                // 因为此处EmbeddedAppId为空，在listOrgTopics方法中不会走到活动的查询中，因此此处加上论坛的CategoryId不会和活动的CategoryId混淆。
                // add by yanjn  20170612
                command.setCategoryId(cmd.getCategoryId());

                command.setTag(cmd.getTag());

                response = forumService.listOrgTopics(command);
                break;
        }

        return response;
    }

    @Override
    public PostDTO getTopic(GetTopicCommand cmd) {

        //权限控制
        return forumService.getTopic(cmd);
    }

    @Override
    public PostDTO createComment(NewCommentCommand cmd) {

        //权限控制
        return forumService.createComment(cmd);
    }

    @Override
    public ListPostCommandResponse listTopicComments(ListTopicCommentCommand cmd) {

        //权限控制
        return forumService.listTopicComments(cmd);
    }

    @Override
    public void likeTopic(LikeTopicCommand cmd) {

        //权限控制
        forumService.likeTopic(cmd);
    }

    @Override
    public void cancelLikeTopic(CancelLikeTopicCommand cmd) {

        //权限控制
        forumService.cancelLikeTopic(cmd);
    }


//    @Caching(evict = {@CacheEvict(value = "ForumPostById", key = "#topicId")})
    private void sendComment(long topicId, long forumId, long orgId, OrganizationMember member, long category, int namespaceId) {
        User user = UserContext.current().getUser();
        Post comment = new Post();
        comment.setParentPostId(topicId);
        comment.setForumId(forumId);
        comment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        comment.setCreatorUid(user.getId());
        comment.setContentType(PostContentType.TEXT.getCode());
        String template = this.getOrganizationAssignTopicForCommentTemplate(orgId, member, namespaceId);
        if (StringUtils.isEmpty(template)) {
            template = "该请求已安排人员处理";
        }
        if (LOGGER.isDebugEnabled()) {
            try {
                LOGGER.error("sendComment_template=" + (new String(template.getBytes(), "UTF-8")));
            } catch (UnsupportedEncodingException e) {
                LOGGER.error(e.getMessage());
            }
        }
        if (!StringUtils.isEmpty(template)) {
            comment.setContent(template);
            forumProvider.createPost(comment);
        }
    }

    private String getOrganizationAssignTopicForCommentTemplate(long orgId, OrganizationMember member, int namespaceId) {

        Map<String, Object> map = new HashMap<String, Object>();
        if (member != null) {
            map.put("memberName", member.getContactName());
            map.put("memberContactToken", member.getContactToken());
        }
        User user = null;
        if (0 == member.getTargetId()) {
            UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, member.getContactToken());
            if (userIdentifier != null)
                user = userProvider.findUserById(userIdentifier.getOwnerUid());
        } else {
            user = userProvider.findUserById(member.getTargetId());
        }

        // 当被分派任务的人不在左邻系统中时，使用当前用户来获取locale，而不是抛异常 by lqs 20151222
//		if(null == user){
//			LOGGER.error("Users do not register.");
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Users do not register.");
//		}

        String locale = "zh_CN";
        if (user == null) {
            user = UserContext.current().getUser();
        }
        if (user != null) {
            locale = user.getLocale();
        }

        String notifyText = localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE, OrganizationNotificationTemplateCode.ORGANIZATION_ASSIGN_TOPIC_FOR_COMMENT, locale, map, "");

        return notifyText;
    }

    @Override
    public ListOrganizationCommunityCommandResponse listOrganizationCommunities(ListOrganizationCommunityCommand cmd) {
        ListOrganizationCommunityCommandResponse response = new ListOrganizationCommunityCommandResponse();

        cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
        int totalCount = organizationProvider.countOrganizationCommunitys(cmd.getOrganizationId());
        if (totalCount == 0) return response;

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        int pageCount = getPageCount(totalCount, pageSize);

        List<OrganizationCommunity> result = organizationProvider.listOrganizationCommunities(cmd.getOrganizationId(), cmd.getPageOffset(), pageSize);
        List<OrganizationCommunityDTO> orgComms = new ArrayList<OrganizationCommunityDTO>();
        if (result != null && result.size() > 0) {
            for (OrganizationCommunity organizationCommunity : result) {
                OrganizationCommunityDTO dto = ConvertHelper.convert(organizationCommunity, OrganizationCommunityDTO.class);
                Community community = communityProvider.findCommunityById(organizationCommunity.getCommunityId());
                dto.setCommunityName(community.getName());
                dto.setCityName(community.getCityName());
                dto.setAreaName(community.getAreaName());
                dto.setStatus(community.getStatus());
                dto.setCommunityType(community.getCommunityType());
                orgComms.add(dto);
            }
        }
        response.setCommunities(orgComms);
        response.setNextPageOffset(cmd.getPageOffset() == pageCount ? null : cmd.getPageOffset() + 1);
        return response;
    }

    @Override
    public ListOrganizationCommunityV2CommandResponse listOrganizationCommunitiesV2(ListOrganizationCommunityCommand cmd) {
        ListOrganizationCommunityV2CommandResponse response = new ListOrganizationCommunityV2CommandResponse();

        cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
        int totalCount = organizationProvider.countOrganizationCommunitys(cmd.getOrganizationId());
        if (totalCount == 0) return response;

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        int pageCount = getPageCount(totalCount, pageSize);

        List<OrganizationCommunity> result = organizationProvider.listOrganizationCommunities(cmd.getOrganizationId(), cmd.getPageOffset(), pageSize);
        List<CommunityDTO> communityList = new ArrayList<CommunityDTO>();
        if (result != null && result.size() > 0) {
            CommunityDTO dto = null;
            for (OrganizationCommunity organizationCommunity : result) {
                Community community = communityProvider.findCommunityById(organizationCommunity.getCommunityId());
                dto = ConvertHelper.convert(community, CommunityDTO.class);
                communityList.add(dto);
            }
        }
        response.setCommunities(communityList);
        response.setNextPageOffset(cmd.getPageOffset() == pageCount ? null : cmd.getPageOffset() + 1);
        return response;
    }

    @Override
    public List<Long> getOrganizationCommunityIdById(Long organizationId) {
        List<Long> communityIdList = new ArrayList<Long>();
        if (organizationId == null) {
            LOGGER.error("Invalid organization id, organizationId=" + organizationId);
            return communityIdList;
        }

        List<OrganizationCommunity> communityList = this.organizationProvider.listOrganizationCommunities(organizationId);
        if (communityList != null && communityList.size() > 0) {
            for (OrganizationCommunity community : communityList) {
                communityIdList.add(community.getCommunityId());
            }
        }

        return communityIdList;
    }

    @Override
    public void createOrgContact(CreateOrganizationContactCommand cmd) {
        if (cmd.getContactType() == null) {
            cmd.setContactType(IdentifierType.MOBILE.getCode());
        } else {
            IdentifierType type = IdentifierType.fromCode(cmd.getContactType());
            cmd.setContactType(type.getCode());
        }
        CommunityPmContact contact = ConvertHelper.convert(cmd, CommunityPmContact.class);
        propertyMgrProvider.createPropContact(contact);
    }

    @Override
    public void updateOrgContact(UpdateOrganizationContactCommand cmd) {
        if (cmd.getContactType() == null) {
            cmd.setContactType(IdentifierType.MOBILE.getCode());
        } else {
            IdentifierType type = IdentifierType.fromCode(cmd.getContactType());
            cmd.setContactType(type.getCode());
        }
        CommunityPmContact contact = ConvertHelper.convert(cmd, CommunityPmContact.class);
        propertyMgrProvider.updatePropContact(contact);
    }

    @Override
    public void deleteOrgContact(DeleteOrganizationIdCommand cmd) {
        CommunityPmContact contact = propertyMgrProvider.findPropContactById(cmd.getId());
        if (contact == null) {
            LOGGER.error("organization contact not found.");
        } else {
            propertyMgrProvider.deletePropContact(cmd.getId());
        }
    }

    @Override
    public ListOrganizationContactCommandResponse listOrgContact(ListOrganizationContactCommand cmd) {
        //权限控制
        ListOrganizationContactCommandResponse response = new ListOrganizationContactCommandResponse();

        List<CommunityPmContact> result = propertyMgrProvider.listCommunityPmContacts(cmd.getOrganizationId());
        response.setMembers(result.stream()
                .map(r -> {
                    return ConvertHelper.convert(r, OrganizationContactDTO.class);
                })
                .collect(Collectors.toList()));
        return response;
    }

    @Override
    public Boolean deleteOrganization(DeleteOrganizationIdCommand cmd) {

        User user = UserContext.current().getUser();

        //权限校验
        if (cmd.getCheckAuth() == null) {
            checkOrganizationPrivilege(cmd.getId(), PrivilegeConstants.DELETE_DEPARTMENT);
        }
        this.checkOrganizationIdIsNull(cmd.getId());
        Organization organization = this.checkOrganization(cmd.getId());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());

        //：todo 判断该机构（子公司/部门/职级）是否有活动状态的人员
        if(organization.getGroupType().equals(OrganizationGroupType.ENTERPRISE.getCode()) || organization.getGroupType().equals(OrganizationGroupType.DEPARTMENT.getCode()) || organization.getGroupType().equals(OrganizationGroupType.JOB_LEVEL.getCode())){
            //查询需要失效的所有人
            List<String> groupTypes = new ArrayList<>();
            groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
            groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
            groupTypes.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
            groupTypes.add(OrganizationGroupType.JOB_POSITION.getCode());
            groupTypes.add(OrganizationGroupType.JOB_LEVEL.getCode());
            List<OrganizationMember> if_empty_members = organizationProvider.listOrganizationMemberByPath(organization.getPath(), groupTypes, "");
            if(if_empty_members != null && if_empty_members.size() > 0){
                if_empty_members = if_empty_members.stream().filter(r-> r.getStatus().equals(OrganizationMemberStatus.ACTIVE.getCode())).collect(Collectors.toList());
            }
            //2.如果仍有活动的人员,直接返回false
            if(if_empty_members.size() != 0){
                return false;
            }
        }
        //:todo 2部门岗位不需要作判断


        //3.如果没有活动的
        List<Organization> organizations = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "%", null);

        List<Long> organizationIds = organizations.stream().map(r -> r.getId()).collect(Collectors.toList());

        dbProvider.execute((TransactionStatus status) -> {

            //更新organization
            organizationProvider.updateOrganization(organizationIds, OrganizationStatus.DELETED.getCode(), user.getId(), now);

            //删除organizaitonCommunity
            organizationProvider.deleteOrganizationCommunityByOrgIds(organizationIds);

            //把机构入驻的园区关系修改成无效
            organizationProvider.updateOrganizationCommunityRequestByOrgIds(organizationIds, OrganizationCommunityRequestStatus.INACTIVE.getCode(), user.getId(), now);

            //把机构下的所有人员修改成无效
            organizationProvider.updateOrganizationMemberByOrgPaths(organization.getPath() + "%", OrganizationMemberStatus.INACTIVE.getCode(), user.getId(), now);


            //查询需要失效的所有人
            List<OrganizationMember> members = organizationProvider.listOrganizationMemberByPath(organization.getPath(), null, "");

            //把user_organization表中的相应记录更新为失效
            inactiveUserOrganizationWithMembers(members);

            if(members.size() > 0){
                ExecutorUtil.submit(new Runnable() {
                    @Override
                    public void run() {
                        Timestamp authStart = new Timestamp(DateHelper.currentGMTTime().getTime());
                        LOGGER.debug("authStart: " + sdf.format(authStart));
                        try {
                            members.forEach(member -> {
                                //解除门禁权限
                                doorAccessService.deleteAuthWhenLeaveFromOrg(UserContext.getCurrentNamespaceId(), member.getOrganizationId(), member.getTargetId());
                            });
                        } catch (Exception e) {
                            LOGGER.error("deleteAuth task failure.", e);
                        } finally {
                            Timestamp authEnd = new Timestamp(DateHelper.currentGMTTime().getTime());
                            LOGGER.debug("authEnd : " + sdf.format(authEnd));
                        }
                    }
                });
            }

            return null;
        });
        return true;
    }

    @Override
    public void deleteOrganizationMember(DeleteOrganizationIdCommand cmd) {
        User user = UserContext.current().getUser();
        //权限控制
        OrganizationMember member = organizationProvider.findOrganizationMemberById(cmd.getId());
        if (member == null || StringUtils.isEmpty(member.getContactToken())) {
            LOGGER.error("organization member is not found.contactId=" + cmd.getId() + ",userId=" + user.getId());
        } else {
            String path = member.getGroupPath();
            //查询出人员在这个组织架构的所有关系
            if (DeleteOrganizationContactScopeType.ALL_NOTE == DeleteOrganizationContactScopeType.fromCode(cmd.getScopeType())) {
                path = member.getGroupPath();
                if (path.indexOf("/", 1) > 0) {
                    path = path.substring(0, path.indexOf("/", 1));
                }
                //查询当前节点的机构所属公司的所有机构跟人员的关系
            } else if (DeleteOrganizationContactScopeType.CHILD_ENTERPRISE == DeleteOrganizationContactScopeType.fromCode(cmd.getScopeType())) {
                Organization org = checkOrganization(member.getOrganizationId());
                if (OrganizationGroupType.ENTERPRISE != OrganizationGroupType.fromCode(org.getGroupType())) {
                    org = checkOrganization(org.getDirectlyEnterpriseId());
                }
                path = org.getPath();
                //人员跟当前节点机构的关系
            }

            leaveOrganizationMemberByOrganizationPathAndContactToken(path, member.getContactToken());

        }

    }

    /**
     * @param m        要删除的机构成员以及全部的权限
     * @param isDelete 是否物理删除，从数据库中直接把成员从机构删除掉，还是只是置状态成无效
     */
    private void deleteOrganizationMember(OrganizationMember m, Boolean isDelete) {

        if (isDelete) {
            organizationProvider.deleteOrganizationMemberById(m.getId());
        } else {
            m.setStatus(OrganizationMemberStatus.INACTIVE.getCode());
            organizationProvider.updateOrganizationMember(m);
        }

        //同事把用户权限去掉
        if (OrganizationMemberTargetType.fromCode(m.getTargetType()) == OrganizationMemberTargetType.USER) {
            //删除用户角色
            List<RoleAssignment> userRoles = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), m.getOrganizationId(), EntityType.USER.getCode(), m.getTargetId());
            for (RoleAssignment roleAssignment : userRoles) {
                aclProvider.deleteRoleAssignment(roleAssignment.getId());
            }

            //删除用户权限
            AclRoleDescriptor descriptor = new AclRoleDescriptor(EntityType.USER.getCode(), m.getTargetId());
            List<Acl> acls = aclProvider.getResourceAclByRole(EntityType.ORGANIZATIONS.getCode(), m.getOrganizationId(), descriptor);
            for (Acl acl : acls) {
                aclProvider.deleteAcl(acl.getId());
            }

        }
    }


    @Override
    public void sendOrgMessage(SendOrganizationMessageCommand cmd) {
        this.checkOrganizationIdIsNull(cmd.getOrganizationId());
        this.checkCommunityIdsIsNull(cmd.getCommunityIds());
        this.checkOrganization(cmd.getOrganizationId());

        List<Long> familyIds = new ArrayList<Long>();
        List<Long> communityIds = cmd.getCommunityIds();

        if (communityIds == null || communityIds.size() == 0) {
            List<CommunityDTO> dtos = this.listAllChildrenOrganizationCoummunities(cmd.getOrganizationId());
            if (dtos != null && dtos.size() > 0) {
                for (CommunityDTO dto : dtos) {
                    communityIds.add(dto.getId());
                }
            }
        }
        if (communityIds != null && communityIds.size() > 0) {
            for (Long communityId : communityIds) {
                OrganizationCommunity organizationCommunity = organizationProvider.findOrganizationCommunityByOrgIdAndCmmtyId(cmd.getOrganizationId(), communityId);
                if (organizationCommunity != null) {
                    List<Group> familyList = familyProvider.listCommunityFamily(communityId);
                    addFamilyIds(familyIds, familyList);
                } else {
                    LOGGER.error("the community is not belong to the organization.communityId=" + communityId + ".organizationId=" + cmd.getOrganizationId());
                }
            }
        }

        if (familyIds != null && familyIds.size() > 0) {
            for (Long familyId : familyIds) {
                sendNoticeToFamilyById(familyId, cmd.getMessage());
            }
        }
    }

    private void addFamilyIds(List<Long> familyIds, List<Group> familyList) {
        if (familyList != null && familyList.size() > 0) {
            for (Group group : familyList) {
                familyIds.add(group.getId());
            }
        }
    }

    public void sendNoticeToFamilyById(Long familyId, String message) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_FAMILY);
        messageDto.setSenderUid(UserContext.current().getUser().getId());
        messageDto.setChannels(new MessageChannel(MessageChannelType.GROUP.getCode(), String.valueOf(familyId)));
        messageDto.setMetaAppId(AppConstants.APPID_FAMILY);
        messageDto.setBody(message);

        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_FAMILY, MessageChannelType.GROUP.getCode(),
                String.valueOf(familyId), messageDto, MessagingConstants.MSG_FLAG_STORED.getCode());
    }

    @Override
    public void setCurrentOrganization(SetCurrentOrganizationCommand cmd) {
        User user = UserContext.current().getUser();
        String organizationId = String.valueOf(cmd.getOrganizationId());
        userActivityProvider.updateUserProfile(user.getId(), "currentOrganizationName", organizationId);

        String key = UserCurrentEntityType.ORGANIZATION.getUserProfileKey();
        long timestemp = DateHelper.currentGMTTime().getTime();
        userActivityProvider.updateUserCurrentEntityProfile(user.getId(), key, cmd.getOrganizationId(), timestemp, user.getNamespaceId());
    }

    @Override
    public OrganizationDTO getUserCurrentOrganization() {
        User user = UserContext.current().getUser();

        OrganizationDTO dto = new OrganizationDTO();
        //UserProfile userProfile = this.getUserProfileByUidAndItemName(user.getId(),"currentOrganizationName");

        String key = UserCurrentEntityType.ORGANIZATION.getUserProfileKey();
        UserProfile userProfile = userActivityProvider.findUserProfileBySpecialKey(user.getId(), key);

        if (userProfile != null) {
            Long organizationId = Long.parseLong((userProfile.getItemValue()));
            Organization organization = organizationProvider.findOrganizationById(organizationId);
            if (organization != null) {
                dto = ConvertHelper.convert(organization, OrganizationDTO.class);
                if (dto.getOrganizationType().equals(OrganizationType.PM.getCode()) || dto.getOrganizationType().equals(OrganizationType.GARC.getCode())) {
                    Long orgId = dto.getId();
                    if (0 != dto.getParentId()) {
                        orgId = Long.valueOf(dto.getPath().split("/")[1]);
                    }
                    OrganizationCommunity orgComm = this.organizationProvider.findOrganizationCommunityByOrgId(orgId);
                    if (orgComm != null) {
                        Community community = this.communityProvider.findCommunityById(orgComm.getCommunityId());
                        if (community != null) {
                            dto.setCommunityId(orgComm.getCommunityId());
                            dto.setCommunityName(community.getName());
                        }
                    }
                }
            }
        }
        return dto;
    }

    private UserProfile getUserProfileByUidAndItemName(Long userId, String itemName) {
        List<UserProfile> userProfiles = userActivityProvider.findProfileByUid(userId);
        UserProfile userProfile = null;
        if (userProfiles != null && userProfiles.size() > 0) {
            for (UserProfile profile : userProfiles) {
                if (itemName.equals(profile.getItemName())) {
                    userProfile = profile;
                }
            }
        }
        return userProfile;
    }

    public Map<String, Long> getOrganizationRegionMap(Long communityId) {
        Long userId = -1L;
        User user = UserContext.current().getUser();
        if (user != null) {
            userId = user.getId();
        }

        Map<String, Long> map = new HashMap<String, Long>();

        List<Organization> list = this.organizationProvider.findOrganizationByCommunityId(communityId);

        if (LOGGER.isDebugEnabled())
            LOGGER.info("getOrganizationRegionMap-orgs=" + StringHelper.toJsonString(list));

        for (Organization organization : list) {
            OrganizationType type = OrganizationType.fromCode(organization.getOrganizationType());
            if (type != null) {
                // 对于物业和业委，其region为小区ID；对于居委和公安，其region为机构ID
                switch (type) {
                    case PM:
                    case GARC:
                        map.put(type.getCode(), communityId);
                        break;
                    case GANC:
                    case GAPS:
                    case GACW:
                        map.put(type.getCode(), organization.getId());
                        break;
                    default:
                        LOGGER.error("Organization type not supported, userId=" + userId + ", communityId=" + communityId
                                + ", organizationId=" + organization.getId() + ", organizationType=" + organization.getOrganizationType());
                }
            } else {
                LOGGER.error("Organization type is null, userId=" + userId + ", communityId=" + communityId
                        + ", organizationId=" + organization.getId() + ", organizationType=" + organization.getOrganizationType());
            }
        }

        // 为了兼容没有机构的情况，需要为其提供默认值
        OrganizationType[] values = OrganizationType.values();
        for (OrganizationType value : values) {
            Long organizatioinId = map.get(value.getCode());
            if (organizatioinId == null) {
                if (value == OrganizationType.PM || value == OrganizationType.GARC) {
                    map.put(value.getCode(), communityId);
                } else {
                    map.put(value.getCode(), 0L);
                }
            }
        }

        return map;
    }

    /**
     * 根据小区ID获取机构及该机构在树型结构的组织架构上的所有上层机构。
     * 修改1：找出小区的所有上级机构，update by tt, 20160810
     *
     * @param communityId 小区ID
     */
    public List<Organization> getOrganizationTreeUpToRoot(Long communityId) {
        long startTime = System.currentTimeMillis();
        List<Long> organizationIds = getOrganizationIdsTreeUpToRoot(communityId);
        List<Organization> orgResultist = organizationProvider.listOrganizationsByIds(organizationIds);
        long endTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.info("List all the organization from bottom to the top in the tree, communityId=" + communityId
                    + ", size=" + orgResultist.size() + ", elapse=" + (endTime - startTime));
        }

        return orgResultist;
    }

    @Override
    public List<Long> getOrganizationIdsTreeUpToRoot(Long communityId) {
//	    List<Organization> orgResultist = new ArrayList<Organization>();

        List<Organization> orgDbist = this.organizationProvider.findOrganizationByCommunityId(communityId);
//	    String rootPath = null;
        Set<Long> organizationIds = new HashSet<>();
        if (orgDbist != null && !orgDbist.isEmpty()) {
            for (Organization org : orgDbist) {
//	        orgResultist.add(org);
//	        rootPath = getOrganizationRootPath(org.getPath());
//	        if(rootPath != null && rootPath.length() > 0) {
//	            List<Organization> tempDbist = this.organizationProvider.findOrganizationByPath(rootPath);
//	            if(tempDbist != null && tempDbist.size() > 0) {
//	                orgResultist.addAll(tempDbist);
//	            }
//	        }

                if (org != null && org.getPath() != null) {
                    organizationIds.addAll(Arrays.asList(org.getPath().trim().split("/"))
                            .stream().map(o -> StringUtils.isEmpty(o.trim()) ? null : Long.valueOf(o))
                            .filter(f -> f != null).collect(Collectors.toSet()));
                }
            }
        }
        return new ArrayList<>(organizationIds);
    }

    /**
     * 根据路径<code>path</code>取得其对应的根路径
     *
     * @param path 路径
     * @return 根据
     */
    private String getOrganizationRootPath(String path) {
        // 路径的格式为：/第一层机构ID/第二层机构ID/.../第N层机构ID
        // 从最后一层机构ID往前一层一层剥除直到最后一层
        if (path != null && path.length() > 0) {
            int index = path.lastIndexOf('/');
            while (index > 0) {
                path = path.substring(0, index);
                index = path.lastIndexOf('/');
            }
        }

        return path;
    }

    @Override
    public List<OrganizationDetailDTO> listUserRelateEnterprises(ListUserRelatedEnterprisesCommand cmd) {
        User user = UserContext.current().getUser();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<OrganizationMember> orgMembers = this.organizationProvider.listOrganizationMembers(user.getId());

        Community community = null;
        if (null != cmd.getCommunityId()) {
            community = communityProvider.findCommunityById(cmd.getCommunityId());
        }
        List<OrganizationDetailDTO> dtos = new ArrayList<OrganizationDetailDTO>();
        for (OrganizationMember member : orgMembers) {
            Organization org = this.organizationProvider.findOrganizationById(member.getOrganizationId());

            //Filter out the inactive organization add by sfyan 20130430
            OrganizationStatus orgStatus = OrganizationStatus.fromCode(org.getStatus());
            if (orgStatus != OrganizationStatus.ACTIVE) {
                LOGGER.error("The member is ignored for organization not active, userId=" + user.getId()
                        + ", organizationId=" + member.getOrganizationId() + ", orgMemberId=" + member.getId()
                        + ", namespaceId=" + namespaceId + ", orgStatus" + orgStatus);
                continue;
            }
            if (OrganizationGroupType.ENTERPRISE.getCode().equals(org.getGroupType())) {
                OrganizationDetailDTO dto = this.toOrganizationDetailDTO(org.getId(), false);
                dto.setMember(ConvertHelper.convert(member, OrganizationMemberDTO.class));
                dto.setCommunityId(cmd.getCommunityId());
                dto.setCommunity(ConvertHelper.convert(community, CommunityDTO.class));
                dtos.add(dto);
            }
        }
        return dtos;
    }

    @Override
    public List<OrganizationDTO> listUserRelateOrganizations(Integer namespaceId, Long userId, OrganizationGroupType groupType) {
        Long startTime = System.currentTimeMillis();

        List<Organization> organizations = listUserOrganizations(namespaceId, userId, groupType);

        List<OrganizationDTO> dtos = new ArrayList<>();

        for (Organization org: organizations) {
            OrganizationDTO dto = toOrganizationDTO(userId, org);

            dtos.add(dto);
        }

        //：todo 去重
        dtos = new ArrayList<>(new HashSet<>(dtos));

        Long endTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("TrackUserRelatedCost:listUserRelateOrganizations:elapse:{}", endTime - startTime);
        }
        return dtos;
    }

    @Override
    public List<Organization> listUserOrganizations(Integer namespaceId, Long userId, OrganizationGroupType groupType) {
        OrganizationGroupType tempGroupType = null;

        List<OrganizationMember> orgMembers = this.organizationProvider.listOrganizationMembers(userId);

        List<Organization> dtos = new ArrayList<>();
        for (OrganizationMember member : orgMembers) {
            // 如果机构不存在，则丢弃该成员对应的机构
            Organization org = this.organizationProvider.findOrganizationById(member.getOrganizationId());
            if (org == null) {
                LOGGER.error("The member is ignored for organization not found, userId=" + userId
                        + ", organizationId=" + member.getOrganizationId() + ", orgMemberId=" + member.getId()
                        + ", namespaceId=" + namespaceId + ", groupType=" + groupType);
                continue;
            }

            // 如果指定的机构的类型，则把不符合指定机构类型的机构都丢弃
            tempGroupType = OrganizationGroupType.fromCode(org.getGroupType());
            if (groupType != null && groupType != tempGroupType) {
                LOGGER.error("The member is ignored for organization group type not matched, userId=" + userId
                        + ", organizationId=" + member.getOrganizationId() + ", orgMemberId=" + member.getId()
                        + ", namespaceId=" + namespaceId + ", groupType=" + groupType + ", currGroupType" + tempGroupType);
                continue;
            }

            //Filter out the inactive organization add by sfyan 20130430
            OrganizationStatus orgStatus = OrganizationStatus.fromCode(org.getStatus());
            if (orgStatus != OrganizationStatus.ACTIVE) {
                LOGGER.error("The member is ignored for organization not active, userId=" + userId
                        + ", organizationId=" + member.getOrganizationId() + ", orgMemberId=" + member.getId()
                        + ", namespaceId=" + namespaceId + ", groupType=" + groupType + ", orgStatus" + orgStatus);
                continue;
            }

            //Filter out the child organization add by lei.lv 20171124
            Long parentId = org.getParentId();
            if (parentId != 0L) {
                LOGGER.error("The member's organization is child-enterprise, userId=" + userId
                        + ", organizationId=" + member.getOrganizationId() + ", orgMemberId=" + member.getId()
                        + ", namespaceId=" + namespaceId + ", groupType=" + groupType + ", orgStatus" + orgStatus);
                continue;
            }
            dtos.add(org);

        }

        return dtos;
    }

    private OrganizationDTO toOrganizationDTO(Long userId, Organization organization) {
        OrganizationDTO organizationDto = ConvertHelper.convert(organization, OrganizationDTO.class);
        organizationDto.setOrganizationType(organization.getOrganizationType());

        OrganizationDetail organizationDetail = organizationProvider.findOrganizationDetailByOrganizationId(organization.getId());
        if (organizationDetail == null) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Organization detail record is null, userId={}, organizationId={}", userId, organization.getId());
            }
        } else {
            organizationDto.setAddress(organizationDetail.getAddress());
            organizationDto.setContact(organizationDetail.getContact());
            organizationDto.setDisplayName(organizationDetail.getDisplayName());
            organizationDto.setAvatarUri(organizationDetail.getAvatar());
            String avatarUri = organizationDetail.getAvatar();
            if (avatarUri != null && avatarUri.trim().length() == 0) {
                organizationDto.setAvatarUrl(contentServerService.parserUri(organizationDetail.getPostUri(),
                        EntityType.ORGANIZATIONS.getCode(), organization.getId()));
            }
        }

        //增加门牌地址
        List<OrganizationAddress> organizationAddresses = organizationProvider.findOrganizationAddressByOrganizationId(organization.getId());
        if (null != organizationAddresses) {
            List<String> doorplateAddresses = new ArrayList<String>();
            for (OrganizationAddress organizationAddress : organizationAddresses) {
                Address address = addressProvider.findAddressById(organizationAddress.getAddressId());
                if (null != address) {
                    doorplateAddresses.add(address.getAddress());
                }

            }

            if (0 < doorplateAddresses.size()) {
                organizationDto.setAddress(doorplateAddresses.get(0));
            }
        }


        // 企业入驻的园区
        Long communityId = getOrganizationActiveCommunityId(organization.getId());
        // 园区对应的类型、论坛等信息
        if (communityId != null) {
            Community community = communityProvider.findCommunityById(communityId);
            if (community != null) {
                organizationDto.setCityId(community.getCityId());
                organizationDto.setCityName(community.getCityName());
                organizationDto.setAreaId(community.getAreaId());
                organizationDto.setAreaName(community.getAreaName());
                organizationDto.setCommunityId(communityId);
                organizationDto.setCommunityName(community.getName());
                organizationDto.setCommunityAliasName(community.getAliasName());
                organizationDto.setCommunityType(community.getCommunityType());
                organizationDto.setDefaultForumId(community.getDefaultForumId());
                organizationDto.setFeedbackForumId(community.getFeedbackForumId());
            } else {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn("Organization community not found, userId={}, organizationId={}, communityId={}",
                            userId, organization.getId(), communityId);
                }
            }
        } else {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Organization community id is null, userId={}, organizationId={}",
                        userId, organization.getId());
            }
        }

        OrganizationMember orgMember = organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, organization.getId());
        if (orgMember != null) {
            organizationDto.setMemberStatus(orgMember.getStatus());
            if(OrganizationMemberGroupType.fromCode(orgMember.getMemberGroup()) == OrganizationMemberGroupType.MANAGER){
                organizationDto.setManagerFlag(AdminFlag.YES.getCode());
            }
        }

        return organizationDto;
    }

    @Override
    public List<OrganizationSimpleDTO> listUserRelateOrgs(ListUserRelatedOrganizationsCommand cmd) {
        return listUserRelateOrgs(cmd, UserContext.current().getUser());
    }


    @Override
    public List<OrganizationSimpleDTO> listUserRelateOrganizations(ListUserRelatedOrganizationsCommand cmd) {
        Long userId = UserContext.current().getUser().getId();
        return listUserRelateOrganizations(userId);
    }

    public List<OrganizationSimpleDTO> listUserRelateOrganizations(Long userId) {
        List<OrganizationSimpleDTO> orgs = new ArrayList<>();
        Set<Long> organizationIds = new HashSet<>();


        List<OrganizationMember> members = organizationProvider.listOrganizationMembersByOrganizationIdAndMemberGroup(OrganizationMemberGroupType.MANAGER.getCode(), OrganizationMemberTargetType.USER.getCode(), userId);
        for (OrganizationMember member: members) {
            if(OrganizationGroupType.ENTERPRISE == OrganizationGroupType.fromCode(member.getGroupType())){
                organizationIds.add(member.getOrganizationId());
            }
        }

        /* 人员在新的管理员数据里没有 则去查老的管理员是角色的时候数据（之前添加的管理员可以不在公司，所以在organizationMember里面查询不到）,如果打算去掉老的，需要迁移数据把创建管理员没有添加到公司的数据给补上 */
        if(organizationIds.size() == 0){
            List<RoleAssignment> roleAssignments = aclProvider.getRoleAssignmentByTarget(EntityType.USER.getCode(), userId);
        for (RoleAssignment roleAssignment: roleAssignments) {
            if(EntityType.ORGANIZATIONS == EntityType.fromCode(roleAssignment.getOwnerType()) && (roleAssignment.getRoleId() == RoleConstants.PM_SUPER_ADMIN || roleAssignment.getRoleId() == RoleConstants.ENTERPRISE_SUPER_ADMIN)){
                    organizationIds.add(roleAssignment.getOwnerId());
                }
            }
        }

        // 如果是模块管理员，则列出模块管理员所在的公司
        Set<Long> orgIds = new HashSet<>();
        List<Target> targets = new ArrayList<>();
        targets.add(new Target(EntityType.USER.getCode(), userId));

        List<Project> projects = authorizationProvider.getManageAuthorizationProjectsByAuthAndTargets(EntityType.SERVICE_MODULE.getCode(), null, targets);
        for (Project project : projects) {
            if (EntityType.fromCode(project.getProjectType()) == EntityType.ORGANIZATIONS) {
                organizationIds.add(project.getProjectId());
            }
        }

        //把用户 所有关联的部门放到targets里面查询
        List<OrganizationMember> orgMembers = this.organizationProvider.listOrganizationMembers(userId);
        for (OrganizationMember member : orgMembers) {
            if (OrganizationMemberStatus.ACTIVE == OrganizationMemberStatus.fromCode(member.getStatus())) {
                Organization org = this.organizationProvider.findOrganizationById(member.getOrganizationId());
                if (null != org && OrganizationStatus.ACTIVE == OrganizationStatus.fromCode(org.getStatus())) {
                    addPathOrganizationId(org.getPath(), orgIds);

                    //Added by janson, normal user in PM company, may remove at later
                    if (org.getOrganizationType().equals(OrganizationType.GARC.getCode()) || org.getOrganizationType().equals(OrganizationType.PM.getCode())) {
                    	organizationIds.add(org.getId());
                    }

                }
            }
        }

        for (Long orgId : orgIds) {
            targets.add(new Target(EntityType.ORGANIZATIONS.getCode(), orgId));

            //普通公司也可以登录后台 modify by Janson
            organizationIds.add(orgId);
        }

        //检查应用管理员+权限细化 add by lei.lv
        List<Project> projects_app = authorizationProvider.getAuthorizationProjectsByAuthIdAndTargets(EntityType.SERVICE_MODULE_APP.getCode(), null, targets);
        for (Project project : projects_app) {
            // 应用管理员
            if (EntityType.fromCode(project.getProjectType()) == EntityType.ORGANIZATIONS) {
                organizationIds.add(project.getProjectId());
            }
            // 权限细化 (敢哥说先这么改)
            if(EntityType.fromCode(project.getProjectType()) == EntityType.COMMUNITY || EntityType.fromCode(project.getProjectType()) == EntityType.ALL){
                List<Organization> organizations = organizationProvider.listOrganizations(OrganizationType.PM.getCode(), UserContext.getCurrentNamespaceId(), 0L, null, null);
                if(organizations != null && organizations.size() > 0){
                    organizationIds.add(organizations.get(0).getId());
                }
            }
        }


        //获取人员和人员所有机构所赋予模块的所属项目范围（旧的权限细化）
        List<String> scopes = authorizationProvider.getAuthorizationScopesByAuthAndTargets(EntityType.SERVICE_MODULE.getCode(), null, targets);
        for (String scope : scopes) {
            if (null != scope) {
                String[] scopeStrs = scope.split("\\.");
                if (scopeStrs.length == 2) {
                    if (EntityType.AUTHORIZATION_RELATION == EntityType.fromCode(scopeStrs[0])) {
                        AuthorizationRelation authorizationRelation = authorizationProvider.findAuthorizationRelationById(Long.valueOf(scopeStrs[1]));
                        if (authorizationRelation != null && EntityType.fromCode(authorizationRelation.getOwnerType()) == EntityType.ORGANIZATIONS) {
                            organizationIds.add(authorizationRelation.getOwnerId());
                        }
                    }
                }
            }
        }


        for (Long organizationId : organizationIds) {
            Organization org = organizationProvider.findOrganizationById(organizationId);
            if (null != org && OrganizationStatus.ACTIVE == OrganizationStatus.fromCode(org.getStatus()) && 0L == org.getParentId()) {
                OrganizationSimpleDTO tempSimpleOrgDTO = ConvertHelper.convert(org, OrganizationSimpleDTO.class);
                OrganizationDetail organizationDetail = organizationProvider.findOrganizationDetailByOrganizationId(org.getId());
                tempSimpleOrgDTO.setDisplayName(tempSimpleOrgDTO.getName());
                if(null != organizationDetail) {
                	// #39544 企业客户未认证，则企业没有 organization detail
                	tempSimpleOrgDTO.setDisplayName(organizationDetail.getDisplayName());
                }
                
                //物业或业委增加小区Id和小区name信息
                if(org.getOrganizationType() != null){
                if (org.getOrganizationType().equals(OrganizationType.GARC.getCode())
                		||org.getOrganizationType().equals(OrganizationType.ENTERPRISE.getCode())
                		|| org.getOrganizationType().equals(OrganizationType.PM.getCode())) {
                        this.addCommunityInfoToUserRelaltedOrgsByOrgId(tempSimpleOrgDTO);
                    }
                    orgs.add(tempSimpleOrgDTO);
                }
            }
        }
        return orgs;
    }

    //Added by Janson 20161217
    @Override
    public List<OrganizationSimpleDTO> listUserRelateOrgs(ListUserRelatedOrganizationsCommand cmd, User user) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        List<OrganizationSimpleDTO> orgs = new ArrayList<OrganizationSimpleDTO>();
        List<OrganizationMember> orgMembers = this.organizationProvider.listOrganizationMembers(user.getId());

        for (OrganizationMember member : orgMembers) {
            if (member.getStatus().equals(OrganizationMemberStatus.ACTIVE.getCode())) {
                Organization org = this.organizationProvider.findOrganizationById(member.getOrganizationId());

                //Filter out the inactive organization add by sfyan 20130430
                OrganizationStatus orgStatus = OrganizationStatus.fromCode(org.getStatus());
                if (orgStatus != OrganizationStatus.ACTIVE) {
                    LOGGER.error("The member is ignored for organization not active, userId=" + user.getId()
                            + ", organizationId=" + member.getOrganizationId() + ", orgMemberId=" + member.getId()
                            + ", namespaceId=" + namespaceId + ", orgStatus" + orgStatus);
                    continue;
                }
                if (null != cmd && cmd.getOrganiztionType() != null && !cmd.getOrganiztionType().equals("")) {
                    if (org.getOrganizationType().equals(cmd.getOrganiztionType()) && !org.getGroupType().equals(OrganizationGroupType.DEPARTMENT.getCode())) {
                        OrganizationSimpleDTO tempSimpleOrgDTO = ConvertHelper.convert(org, OrganizationSimpleDTO.class);
                        //物业或业委增加小区Id和小区name信息
                        if (org.getOrganizationType().equals(OrganizationType.GARC.getCode())
                        		|| org.getOrganizationType().equals(OrganizationType.PM.getCode())) {
                            this.addCommunityInfoToUserRelaltedOrgsByOrgId(tempSimpleOrgDTO);
                        }
                        orgs.add(tempSimpleOrgDTO);
                    }
                } else {

                    if (OrganizationGroupType.ENTERPRISE == OrganizationGroupType.fromCode(org.getGroupType())) {
                        OrganizationSimpleDTO tempSimpleOrgDTO = ConvertHelper.convert(org, OrganizationSimpleDTO.class);
                        //物业或业委增加小区Id和小区name信息
                        if (org.getOrganizationType() != null && (org.getOrganizationType().equals(OrganizationType.GARC.getCode()) || org.getOrganizationType().equals(OrganizationType.PM.getCode()))) {
                            this.addCommunityInfoToUserRelaltedOrgsByOrgId(tempSimpleOrgDTO);
                        }
                        tempSimpleOrgDTO.setContactName(member.getContactName());
                        tempSimpleOrgDTO.setContactToken(member.getContactToken());
                        orgs.add(tempSimpleOrgDTO);
                    }

                }
            }
        }
        return orgs;
    }

    private void addCommunityInfoToUserRelaltedOrgsByOrgId(OrganizationSimpleDTO org) {
//        OrganizationCommunity orgComm = this.organizationProvider.findOrganizationCommunityByOrgId(org.getId());
        Long communityId = this.getOrganizationActiveCommunityId(org.getId());
        if (communityId != null) {
            Community community = this.communityProvider.findCommunityById(communityId);
            if (community != null) {
                org.setCommunityId(communityId);
                org.setCommunityName(community.getName());
            }
        }
    }

    @Override
    public OrganizationDTO getOrganizationByComunityidAndOrgType(GetOrgDetailCommand cmd) {
        List<OrganizationCommunityDTO> orgCommunitys = this.organizationProvider.findOrganizationCommunityByCommunityId(cmd.getCommunityId());
        if (LOGGER.isDebugEnabled())
            LOGGER.info("getOrganizationByComunityidAndOrgType-orgCommunitys=" + StringHelper.toJsonString(orgCommunitys));

        if (orgCommunitys != null && !orgCommunitys.isEmpty()) {
            for (int i = 0; i < orgCommunitys.size(); i++) {
                OrganizationDTO org = this.organizationProvider.findOrganizationByIdAndOrgType(orgCommunitys.get(i).getOrganizationId(), cmd.getOrganizationType());
                if (org != null) {
                    User user = UserContext.current().getUser();
                    OrganizationMember member = this.organizationProvider.findOrganizationMemberByUIdAndOrgId(user.getId(), org.getId());
                    if (member != null && member.getStatus() != null)
                        org.setMemberStatus(member.getStatus());
                    else
                        org.setMemberStatus(OrganizationMemberStatus.INACTIVE.getCode());

                    return org;
                }
            }
        }
        return null;
    }

    @Override
    public int rejectOrganization(RejectOrganizationCommand cmd) {
        this.checkCommunityIdIsNull(cmd.getCommunityId());
        this.checkCommunity(cmd.getCommunityId());
        Organization organization = this.checkOrganizationByCommIdAndOrgType(cmd.getCommunityId(), cmd.getOrganizationType());

        User user = UserContext.current().getUser();
        OrganizationMember member = this.checkUserNotInOrg(user.getId(), organization.getId());
        member.setStatus(OrganizationMemberStatus.INACTIVE.getCode());
        this.organizationProvider.updateOrganizationMember(member);
        return 1;
    }

    @Override
    public int userExitOrganization(UserExitOrganizationCommand cmd) {
        this.checkCommunityIdIsNull(cmd.getCommunityId());
        this.checkCommunity(cmd.getCommunityId());
        Organization org = this.checkOrganizationByCommIdAndOrgType(cmd.getCommunityId(), cmd.getOrganizationType());

        Long organizationId = org.getId();
        User user = UserContext.current().getUser();
        OrganizationMember member = this.checkUserNotInOrg(user.getId(), organizationId);
        this.organizationProvider.deleteOrganizationMember(member);
        return 1;
    }

    @Override
    public void approveOrganizationMember(OrganizationMemberCommand cmd) {
        if (cmd.getOrganizationId() == null || cmd.getMemberId() == null) {
            LOGGER.error("propterty organizationId or memberId paramter can not be null or empty");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "propterty organizationId or memberId paramter can not be null or empty");
        }
        Organization organization = this.checkOrganization(cmd.getOrganizationId());
        OrganizationMember communityPmMember = this.checkDesOrgMember(cmd.getMemberId(), cmd.getOrganizationId());
        User user = UserContext.current().getUser();
        OrganizationMember operOrgMember = this.checkOperOrgMember(user.getId(), cmd.getOrganizationId());

        dbProvider.execute((status) -> {
            communityPmMember.setStatus(PmMemberStatus.ACTIVE.getCode());
            organizationProvider.updateOrganizationMember(communityPmMember);

            //给用户发审核结果通知
            String templateToUser = this.getOrganizationMemberApproveForApplicant(operOrgMember.getContactName(), organization.getName(), communityPmMember.getTargetId());
            if (templateToUser == null)
                templateToUser = "管理员已同意您加入组织";
            sendOrganizationNotificationToUser(communityPmMember.getTargetId(), templateToUser);
            //给其他管理员发通知
            List<OrganizationMember> orgMembers = this.organizationProvider.listOrganizationMembersByOrgId(organization.getId());
            if (orgMembers != null && !orgMembers.isEmpty()) {
                for (OrganizationMember member : orgMembers) {
                    if (member.getTargetId().compareTo(communityPmMember.getTargetId()) != 0 && member.getTargetId().compareTo(operOrgMember.getTargetId()) != 0 && member.getMemberGroup().equals(PmMemberGroup.MANAGER.getCode())) {
                        String templateToManager = this.getOrganizationMemberApproveForManager(operOrgMember.getContactName(), communityPmMember.getContactName(), organization.getName(), member.getTargetId());
                        if (templateToManager == null)
                            templateToManager = "管理员同意用户加入组织";
                        sendOrganizationNotificationToUser(member.getTargetId(), templateToManager);
                    }
                }
            }


            //记录新增 log
            OrganizationMemberLog orgLog = ConvertHelper.convert(cmd, OrganizationMemberLog.class);
            orgLog.setOrganizationId(communityPmMember.getOrganizationId());
            orgLog.setContactName(communityPmMember.getContactName());
            orgLog.setContactToken(communityPmMember.getContactToken());
            orgLog.setUserId(communityPmMember.getTargetId());
            orgLog.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            orgLog.setOperationType(OperationType.JOIN.getCode());
            orgLog.setRequestType(RequestType.USER.getCode());
            orgLog.setOperatorUid(UserContext.current().getUser().getId());
            orgLog.setContactDescription(communityPmMember.getContactDescription());
            this.organizationProvider.createOrganizationMemberLog(orgLog);

            return status;
        });
    }

    private String getOrganizationMemberApproveForManager(String operName, String userName, String orgName, Long managerId) {
        User user = this.userProvider.findUserById(managerId);
        String locale = user.getLocale();
        if (locale == null)
            locale = "zh_CN";

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("memberName", operName);
        map.put("userName", userName);
        map.put("orgName", orgName);

        String template = this.localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE,
                OrganizationNotificationTemplateCode.ORGANIZATION_MEMBER_APPROVE_FOR_MANAGER, locale, map, "");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.error("approveForManager_template=" + template);
        }
        return template;
    }

    private String getOrganizationMemberApproveForApplicant(String operName, String orgName, long userId) {
        User user = this.userProvider.findUserById(userId);
        String locale = user.getLocale();
        if (locale == null)
            locale = "zh_CN";

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("memberName", operName);
        map.put("orgName", orgName);

        String template = this.localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE,
                OrganizationNotificationTemplateCode.ORGANIZATION_MEMBER_APPROVE_FOR_APPLICANT, locale, map, "");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.error("approveForApplicant_template=" + template);
        }
        return template;
    }

    private void sendOrganizationNotificationToUser(Long userId, String message) {
        if (message != null && message.length() != 0) {
            String channelType = MessageChannelType.USER.getCode();
            String channelToken = String.valueOf(userId);
            MessageDTO messageDto = new MessageDTO();
            messageDto.setAppId(AppConstants.APPID_MESSAGING);
            messageDto.setSenderUid(User.SYSTEM_UID);
            messageDto.setChannels(new MessageChannel(channelType, channelToken));
            messageDto.setBodyType(MessageBodyType.NOTIFY.getCode());
            messageDto.setBody(message);
            messageDto.setMetaAppId(AppConstants.APPID_DEFAULT);
            messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, channelType,
                    channelToken, messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
        }
    }

    @Override
    public void rejectOrganizationMember(OrganizationMemberCommand cmd) {
        if (cmd.getOrganizationId() == null || cmd.getMemberId() == null) {
            LOGGER.error("propterty organizationId or memberId paramter can not be null or empty");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "propterty organizationId or memberId paramter can not be null or empty");
        }
        Organization organization = this.checkOrganization(cmd.getOrganizationId());
        OrganizationMember communityPmMember = this.checkDesOrgMember(cmd.getMemberId(), cmd.getOrganizationId());
        User user = UserContext.current().getUser();
        OrganizationMember operOrgMember = this.checkOperOrgMember(user.getId(), cmd.getOrganizationId());

        dbProvider.execute((status) -> {
            organizationProvider.deleteOrganizationMember(communityPmMember);

            //给用户发审核结果通知
            String templateToUser = this.getOrganizationMemberRejectForApplicant(operOrgMember.getContactName(), organization.getName(), communityPmMember.getTargetId());
            if (templateToUser == null)
                templateToUser = "管理员拒绝您加入组织";
            sendOrganizationNotificationToUser(communityPmMember.getTargetId(), templateToUser);
            //给其他管理员发通知
            List<OrganizationMember> orgMembers = this.organizationProvider.listOrganizationMembersByOrgId(organization.getId());
            if (orgMembers != null && !orgMembers.isEmpty()) {
                for (OrganizationMember member : orgMembers) {
                    if (member.getTargetId().compareTo(communityPmMember.getTargetId()) != 0 && member.getTargetId().compareTo(operOrgMember.getTargetId()) != 0 && member.getMemberGroup().equals(PmMemberGroup.MANAGER.getCode())) {
                        String templateToManager = this.getOrganizationMemberRejectForManager(operOrgMember.getContactName(), communityPmMember.getContactName(), organization.getName(), member.getTargetId());
                        if (templateToManager == null)
                            templateToManager = "管理员拒绝用户加入组织";
                        sendOrganizationNotificationToUser(member.getTargetId(), templateToManager);
                    }
                }
            }
            return status;
        });
    }

    private String getOrganizationMemberRejectForManager(String operName, String userName, String orgName, Long managerId) {
        User user = this.userProvider.findUserById(managerId);
        String locale = user.getLocale();
        if (locale == null)
            locale = "zh_CN";

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("memberName", operName);
        map.put("userName", userName);
        map.put("orgName", orgName);

        String template = this.localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE,
                OrganizationNotificationTemplateCode.ORGANIZATION_MEMBER_REJECT_FOR_MANAGER, locale, map, "");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.error("rejectForManager_template=" + template);
        }
        return template;
    }

    private String getOrganizationMemberRejectForApplicant(String operName, String orgName, Long userId) {
        User user = this.userProvider.findUserById(userId);

        if (null == user) {
            LOGGER.error("Users do not register.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Users do not register.");
        }

        String locale = user.getLocale();
        if (locale == null)
            locale = "zh_CN";

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("memberName", operName);
        map.put("orgName", orgName);

        String template = this.localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE,
                OrganizationNotificationTemplateCode.ORGANIZATION_MEMBER_REJECT_FOR_APPLICANT, locale, map, "");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.error("rejectForApllicant_template=" + template);
        }
        return template;
    }

    @Override
    public ListTopicsByTypeCommandResponse listTopicsByType(ListTopicsByTypeCommand cmd) {
        User user = UserContext.current().getUser();
        Long commuId = cmd.getCommunityId();

        if (null == cmd.getCommunityId()) {
            commuId = user.getCommunityId();
        }

        Community community = communityProvider.findCommunityById(commuId);
        Organization organization = this.checkOrganization(cmd.getOrganizationId());

        if (cmd.getPageOffset() == null)
            cmd.setPageOffset(1L);

        ListTopicsByTypeCommandResponse response = new ListTopicsByTypeCommandResponse();
        List<OrganizationTaskDTO2> list = new ArrayList<OrganizationTaskDTO2>();

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset(), pageSize);

        List<OrganizationTask> orgTaskList = this.organizationProvider.listOrganizationTasksByOrgIdAndType(organization.getId(), cmd.getTaskType(), cmd.getTaskStatus(), pageSize + 1, offset);
        if (orgTaskList != null && !orgTaskList.isEmpty()) {
            if (orgTaskList.size() == pageSize + 1) {
                response.setNextPageOffset(cmd.getPageOffset() + 1);
                orgTaskList.remove(orgTaskList.size() - 1);
            }
            for (OrganizationTask task : orgTaskList) {
                try {
                    PostDTO dto = this.forumService.getTopicById(task.getApplyEntityId(), commuId, false);
                    if (dto.getForumId().equals(community.getDefaultForumId())) {
                        OrganizationTaskDTO2 taskDto = ConvertHelper.convert(dto, OrganizationTaskDTO2.class);
                        this.convertTaskToDto(task, taskDto);
                        list.add(taskDto);
                    }

                } catch (Exception e) {
                    LOGGER.error("could not found topic by task's applyEntityId.taskId=" + task.getId() + ",applyEntityId=" + task.getApplyEntityId());
                }
            }
        }

        response.setRequests(list);
        return response;
    }

    private void convertTaskToDto(OrganizationTask task,
                                  OrganizationTaskDTO2 taskDto) {
        taskDto.setTaskId(task.getId());
        taskDto.setOrganizationId(task.getOrganizationId());
        taskDto.setOrganizationType(task.getOrganizationType());
        taskDto.setApplyEntityType(task.getApplyEntityType());
        taskDto.setApplyEntityId(task.getApplyEntityId());
        taskDto.setTargetType(task.getTargetType());
        taskDto.setTargetId(task.getTargetId());
        taskDto.setTaskType(task.getTaskType());
        taskDto.setDescription(task.getDescription());
        taskDto.setTaskStatus(task.getTaskStatus());
        taskDto.setOperatorUid(task.getOperatorUid());
        taskDto.setOperateTime(task.getOperateTime());
        taskDto.setTaskCreatorUid(task.getCreatorUid());
        taskDto.setTaskCreateTime(task.getCreateTime());
        taskDto.setUnprocessedTime(task.getUnprocessedTime());
        taskDto.setProcessingTime(task.getProcessingTime());
        taskDto.setProcessedTime(task.getProcessedTime());

        List<OrganizationMember> member = this.organizationProvider.listOrganizationMembers(task.getOperatorUid());

        if (member != null && member.size() > 0) {
            String orgGroup = member.get(0).getMemberGroup();
            if (OrganizationGroup.MANAGER.getCode().equals(orgGroup) ||
                    orgGroup == OrganizationGroup.MANAGER.getCode()) {
                taskDto.setAssignStatus(0);
            } else {
                taskDto.setAssignStatus(1);
            }
        }
    }

    private void checkOrganizationIdIsNull(Long organizationId) {
        if (organizationId == null) {
            LOGGER.error("propterty organizationId paramter can not be null or empty");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "propterty organizationId paramter can not be null or empty");
        }
    }

    @Override
    public void assignOrgTopic(AssginOrgTopicCommand cmd) {
        if (cmd.getOrganizationId() == null || cmd.getTopicId() == null || cmd.getUserId() == null) {
            LOGGER.error("propterty organizationId or topicId or userId paramter can not be null or empty.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "propterty organizationId or topicId or userId paramter can not be null or empty.");
        }

        Organization organization = this.checkOrganization(cmd.getOrganizationId());
        Post topic = this.checkTopic(cmd.getTopicId());
        OrganizationMember desOrgMember = null;
        if (0 == cmd.getUserId()) {
            desOrgMember = this.checkDesOrgMember(cmd.getContactPhone(), organization.getId());
        } else {
            desOrgMember = this.checkDesOrgMember(cmd.getUserId(), organization.getId());
        }

        OrganizationMember member = desOrgMember;
        User user = UserContext.current().getUser();
        int namespaceId = null == cmd.getNamespaceId() ? user.getNamespaceId() : cmd.getNamespaceId();
        if (null == cmd.getParentId()) cmd.setParentId(organization.getId());
        OrganizationMember operOrgMember = this.checkOperOrgMember(user.getId(), cmd.getParentId());
        OrganizationTask task = this.checkOrgTask(cmd.getParentId(), cmd.getTopicId());

        dbProvider.execute((status) -> {
//			///////////////////////////////////////////////////
//			List<OrganizationMember> member = this.organizationProvider.listOrganizationMembers(cmd.getUserId());
//			if(member != null){
//				String orgGroup = member.getMemberGroup();
//				if(OrganizationGroup.CUSTOMER_SERVICE.getCode().equals(orgGroup) ||
//						orgGroup == OrganizationGroup.CUSTOMER_SERVICE.getCode()){
//					task.setTaskStatus(OrganizationTaskStatus.PROCESSING.getCode());
//				}
//
//				else {
//					task.setTaskStatus(OrganizationTaskStatus.UNPROCESSED.getCode());
//				}
//			}
            task.setTaskStatus(OrganizationTaskStatus.PROCESSING.getCode());
            task.setOperateTime(new Timestamp(System.currentTimeMillis()));
            task.setOperatorUid(user.getId());
            task.setProcessingTime(new Timestamp(System.currentTimeMillis()));
            this.organizationProvider.updateOrganizationTask(task);
            //发送评论
            sendComment(cmd.getTopicId(), topic.getForumId(), organization.getId(), member, topic.getCategoryId(), namespaceId);
            //给分配的处理人员发任务分配短信
            sendSmToOrgMemberForAssignOrgTopic(organization, operOrgMember, member, task);
            return status;
        });
    }

    private OrganizationTask checkOrgTask(Long orgId, Long topicId) {
        OrganizationTask task = this.organizationProvider.findOrgTaskByOrgIdAndEntityId(orgId, topicId);
        if (task == null) {
            LOGGER.error("Unable to find the topic task.");
            throw RuntimeErrorException.errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_INVALID_TASK,
                    "Unable to find the topic task.");
        }
        return task;
    }

    private OrganizationMember checkOperOrgMember(Long userId, Long orgId) {
        OrganizationMember operOrgMember = this.organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, orgId);
        if (operOrgMember == null) {
            LOGGER.error("Operator not found.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Operator not found.");
        }
        return operOrgMember;
    }

    private OrganizationMember checkDesOrgMember(Long userId, Long orgId) {
        OrganizationMember desOrgMember = this.organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, orgId);
        if (desOrgMember == null) {
            LOGGER.error("User is not in the organization.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "User is not in the organization.");
        }
        return desOrgMember;
    }

    private OrganizationMember checkDesOrgMember(String contactPhone, Long orgId) {
        OrganizationMember desOrgMember = this.organizationProvider.findOrganizationMemberByOrgIdAndToken(contactPhone, orgId);
        if (desOrgMember == null) {
            LOGGER.error("User is not in the organization.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "User is not in the organization.");
        }
        return desOrgMember;
    }

    private Post checkTopic(Long topicId) {
        Post topic = this.forumProvider.findPostById(topicId);
        if (topic == null) {
            LOGGER.error("Unable to find the topic.");
            throw RuntimeErrorException.errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_INVALID_TOPIC,
                    "Unable to find the topic.");
        }
        return topic;
    }

    private void sendSmToOrgMemberForAssignOrgTopic(Organization organization, OrganizationMember operOrgMember, OrganizationMember desOrgMember, OrganizationTask task) {

        OrganizationMember member = this.organizationProvider.findOrganizationMemberByUIdAndOrgId(task.getCreatorUid(), organization.getId());
        List<Tuple<String, Object>> variables = null;
        //组织代发求助帖
        if (member != null) {
            variables = smsProvider.toTupleList(SmsTemplateCode.KEY_PHONE, operOrgMember.getContactToken());

        } else {//用户自己发求助帖
            User user = this.userProvider.findUserById(task.getCreatorUid());
            if (user != null) {
                UserIdentifier identify = this.getUserMobileIdentifier(user.getId());
                if (identify != null) {
                    variables = smsProvider.toTupleList(SmsTemplateCode.KEY_PHONE, operOrgMember.getContactToken());
                } else {
                    variables = smsProvider.toTupleList(SmsTemplateCode.KEY_PHONE, "");
                }
            }

        }
        smsProvider.addToTupleList(variables, SmsTemplateCode.KEY_TOPICTYPE, OrganizationTaskType.fromCode(task.getTaskType()).getName());
        String templateScope = SmsTemplateCode.SCOPE;
        int templateId = SmsTemplateCode.ORGANIZATION_ASSIGNED_CODE;
        String templateLocale = UserContext.current().getUser().getLocale();
        smsProvider.sendSms(UserContext.current().getUser().getNamespaceId(), desOrgMember.getContactToken(), templateScope, templateId, templateLocale, variables);
    }

    @Override
    public void setOrgTopicStatus(SetOrgTopicStatusCommand cmd) {
        if (cmd.getOrganizationId() == null || cmd.getStatus() == null || cmd.getTopicId() == null) {
            LOGGER.error("propterty organizationId or status or topicId paramter can not be null or empty");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "propterty organizationId or status or topicId paramter can not be null or empty");
        }
        Organization organization = this.checkOrganization(cmd.getOrganizationId());
        User user = UserContext.current().getUser();
        this.checkOperOrgMember(user.getId(), organization.getId());
        Post topic = this.checkTopic(cmd.getTopicId());
        OrganizationTask task = this.checkOrgTask(organization.getId(), topic.getId());
        OrganizationTaskStatus taskSatus = this.checkTaskStatus(cmd.getStatus());

        dbProvider.execute((status) -> {
            task.setTaskStatus(taskSatus.getCode());
            task.setOperatorUid(user.getId());
            task.setOperateTime(new Timestamp(System.currentTimeMillis()));
            this.setOrgTaskTimeByStatus(task, taskSatus.getCode());
            this.organizationProvider.updateOrganizationTask(task);
			/*if(cmd.getStatus() == OrganizationTaskStatus.PROCESSING.getCode()){
				//发送评论
				sendComment(topic.getId(),topic.getForumId(),organization.getId(),user.getId(),topic.getCategoryId());
			}*/
            return status;
        });
    }

    private void setOrgTaskTimeByStatus(OrganizationTask task, byte code) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        if (OrganizationTaskStatus.UNPROCESSED.getCode() == code)
            task.setUnprocessedTime(currentTimestamp);
        if (OrganizationTaskStatus.PROCESSING.getCode() == code)
            task.setProcessingTime(currentTimestamp);
        if (OrganizationTaskStatus.PROCESSED.getCode() == code)
            task.setProcessedTime(currentTimestamp);
    }

    private OrganizationTaskStatus checkTaskStatus(Byte status) {
        OrganizationTaskStatus taskStatus = OrganizationTaskStatus.fromCode(status);
        if (taskStatus == null) {
            LOGGER.error("task status is wrong.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "task status is wrong.");
        }
        return taskStatus;
    }

    @Override
    public void userJoinOrganization(UserJoinOrganizationCommand cmd) {
        User user = UserContext.current().getUser();
        if (cmd.getName() == null || cmd.getName().isEmpty() || cmd.getOrgType() == null || cmd.getOrgType().isEmpty()) {
            LOGGER.error("propterty name or orgType paramter can not be null or empty");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "propterty name or orgType paramter can not be null or empty");
        }

        this.dbProvider.execute(status -> {
            Organization org = null;
            Community community = null;
            Address address = null;
            Region city = null;
            Region area = null;

            org = this.organizationProvider.findOrganizationByName(cmd.getName());
            if (org == null) {//组织不存在，先创建组织用户再加入组织
                //地址不为空，先查找地址，不存在则创建
                if (cmd.getAddress() != null && !cmd.getAddress().isEmpty()) {
                    address = addressProvider.findAddressByAddress(cmd.getAddress());
                    if (address == null) {
                        city = this.checkCity(cmd.getCityId());
                        area = this.checkArea(cmd.getAreaId());
                        //创建地址
                        address = new Address();
                        address.setAddress(cmd.getAddress());
                        address.setCityId(city.getId());
                        address.setCityName(city.getName());
                        address.setAreaId(area.getId());
                        address.setAreaName(area.getName());
                        address.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        address.setCreatorUid(user.getId());
                        address.setStatus(AddressAdminStatus.CONFIRMING.getCode());
                        this.addressProvider.createAddress(address);
                    }
                }
                //创建组织
                org = new Organization();
                if (address != null)
                    org.setAddressId(address.getId());
                else
                    org.setAddressId(0L);
                org.setName(cmd.getName());
                org.setOrganizationType(cmd.getOrgType());
                if (cmd.getParentId() != null) {
                    Organization parOrg = this.checkOrganization(cmd.getParentId());
                    org.setLevel(parOrg.getLevel() + 1);
                    org.setParentId(parOrg.getId());
                    org.setPath(parOrg.getPath() + "/" + cmd.getName());
                } else {
                    org.setLevel(0);
                    org.setParentId(0L);
                    org.setPath("/" + cmd.getName());
                }
                org.setStatus(OrganizationStatus.INACTIVE.getCode());
                org.setDescription(cmd.getDescription());
                this.organizationProvider.createOrganization(org);
                //创建组织小区关联
                if (cmd.getOrgType().equals(OrganizationType.PM.getCode()) || cmd.getOrgType().equals(OrganizationType.GARC.getCode())) {
                    community = this.checkCommunity(cmd.getCommunityId());
                    OrganizationCommunity orgComm = new OrganizationCommunity();
                    orgComm.setCommunityId(community.getId());
                    orgComm.setOrganizationId(org.getId());
                    this.organizationProvider.createOrganizationCommunity(orgComm);
                }
            }
            //创建组织成员
            if (cmd.isUserJoin()) {
                OrganizationMember orgMember = this.organizationProvider.findOrganizationMemberByUIdAndOrgId(user.getId(), org.getId());
                if (orgMember != null) {
                    LOGGER.error("user have be organization member.");
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                            "user have be organization member.");
                }
                orgMember = new OrganizationMember();
                orgMember.setContactDescription(cmd.getDescription());
                orgMember.setContactName(user.getNickName());
                UserIdentifier identifier = this.getUserMobileIdentifier(user.getId());
                if (identifier != null) {
                    orgMember.setContactToken(identifier.getIdentifierToken());
                    orgMember.setContactType(identifier.getIdentifierType());
                }
                if (cmd.getMemberType() == null || cmd.getMemberType().trim().equals(""))
                    orgMember.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
                else {
                    OrganizationMemberGroupType type = OrganizationMemberGroupType.fromCode(cmd.getMemberType());
                    if (type != null) orgMember.setMemberGroup(type.getCode());
                    else orgMember.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
                }
                orgMember.setOrganizationId(org.getId());
                orgMember.setStatus(OrganizationMemberStatus.WAITING_FOR_APPROVAL.getCode());
                orgMember.setTargetId(user.getId());
                orgMember.setTargetType(OrganizationMemberTargetType.USER.getCode());
                orgMember.setGroupPath(org.getPath());
                orgMember.setGroupType(org.getGroupType());
                this.organizationProvider.createOrganizationMember(orgMember);
            }

            return status;
        });
    }

    private Region checkArea(Long areaId) {
        if (areaId == null) {
            LOGGER.error("areaId could not be null");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "areaId could not be null");
        }
        Region area = regionProvider.findRegionById(areaId);
        if (area == null) {
            LOGGER.error("area not found.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "area not found.");
        }
        return area;
    }

    private Region checkCity(Long cityId) {
        if (cityId == null) {
            LOGGER.error("cityId could not be null");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "cityId could not be null");
        }
        Region city = regionProvider.findRegionById(cityId);
        if (city == null) {
            LOGGER.error("city not found.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "city not found.");
        }
        return city;
    }

    private void checkAddressIsNull(String addressName) {
        if (addressName == null) {
            LOGGER.error("address could not be null");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "address could not be null");
        }
    }

    private Community checkCommunity(Long communityId) {
        Community community = this.communityProvider.findCommunityById(communityId);
        if (community == null) {
            LOGGER.error("community not found");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "community not found");
        }
        return community;
    }

    @Override
    public void deleteOrgMember(OrganizationMemberCommand cmd) {
        if (cmd.getOrganizationId() == null || cmd.getMemberId() == null) {
            LOGGER.error("propterty organizationId or memberId paramter can not be null or empty");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "propterty organizationId or memberId paramter can not be null or empty");
        }
        Organization organization = this.checkOrganization(cmd.getOrganizationId());
        OrganizationMember communityPmMember = this.checkDesOrgMember(cmd.getMemberId(), cmd.getOrganizationId());
        User user = UserContext.current().getUser();

        if (null == cmd.getParentId()) cmd.setParentId(cmd.getOrganizationId());
        OrganizationMember operOrgMember = this.checkOperOrgMember(user.getId(), cmd.getParentId());

        dbProvider.execute((status) -> {
            organizationProvider.deleteOrganizationMember(communityPmMember);
            //给用户发审核结果通知
            if (0 != communityPmMember.getTargetId()) {
                String templateToUser = this.getOrganizationMemberRejectForApplicant(operOrgMember.getContactName(), organization.getName(), communityPmMember.getTargetId());
                if (templateToUser == null)
                    templateToUser = "管理员拒绝您加入组织";
                sendOrganizationNotificationToUser(communityPmMember.getTargetId(), templateToUser);
            }

            //给其他管理员发通知
            List<OrganizationMember> orgMembers = this.organizationProvider.listOrganizationMembersByOrgId(organization.getId());
            if (orgMembers != null && !orgMembers.isEmpty()) {
                for (OrganizationMember member : orgMembers) {
                    if (member.getTargetId().compareTo(communityPmMember.getTargetId()) != 0 && member.getTargetId().compareTo(operOrgMember.getTargetId()) != 0 && member.getMemberGroup().equals(PmMemberGroup.MANAGER.getCode())) {
                        String templateToManager = this.getOrganizationMemberDeleteForManager(operOrgMember.getContactName(), communityPmMember.getContactName(), organization.getName(), member.getTargetId());
                        if (templateToManager == null)
                            templateToManager = "管理员拒绝用户加入组织";
                        sendOrganizationNotificationToUser(member.getTargetId(), templateToManager);
                    }
                }
            }
            return status;
        });

    }

    @Override
    public void updateTopicPrivacy(UpdateTopicPrivacyCommand cmd) {
        Long forumId = cmd.getForumId();
        Long topicId = cmd.getTopicId();
        PostPrivacy privacy = PostPrivacy.fromCode(cmd.getPrivacy());
        this.forumService.updatePostPrivacy(forumId, topicId, privacy);
    }

    private String getOrganizationMemberDeleteForManager(String operName, String userName, String orgName, Long managerId) {
        User user = this.userProvider.findUserById(managerId);
        if (null == user) {
            return null;
        }
        String locale = user.getLocale();
        if (locale == null)
            locale = "zh_CN";

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("memberName", operName);
        map.put("userName", userName);
        map.put("orgName", orgName);

        String template = this.localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE,
                OrganizationNotificationTemplateCode.ORGANIZATION_MEMBER_DELETE_FOR_MANAGER, locale, map, "");
        return template;
    }

    @Override
    public void createOrganizationByAdmin(CreateOrganizationByAdminCommand cmd) {
        this.checkOrgNameIsNull(cmd.getName());
        OrganizationType organizationType = this.checkOrgType(cmd.getOrganizationType());
        //先判断，后台管理员才能创建。状态直接设为正常
        cmd.setOrganizationType(organizationType.getCode());
        Organization org = ConvertHelper.convert(cmd, Organization.class);
        if (cmd.getParentId() == null) {
            org.setParentId(0L);
            org.setPath("/" + org.getName());
            org.setLevel(1);
        } else {
            Organization parOrg = this.checkOrganization(cmd.getParentId());
            org.setPath(parOrg.getPath() + "/" + org.getName());
            org.setLevel(parOrg.getLevel() + 1);
        }
        if (cmd.getAddressId() == null) {
            org.setAddressId(0L);
        }
        org.setStatus(OrganizationStatus.ACTIVE.getCode());
        this.dbProvider.execute(s -> {
            organizationProvider.createOrganization(org);
            if (cmd.getOrganizationType().equals(OrganizationType.PM.getCode()) || cmd.getOrganizationType().equals(OrganizationType.GARC.getCode())) {
                this.checkCommunityIdIsNull(cmd.getCommunityId());
                this.checkCommunity(cmd.getCommunityId());
                OrganizationCommunity orgComm = new OrganizationCommunity();
                orgComm.setCommunityId(cmd.getCommunityId());
                orgComm.setOrganizationId(org.getId());
                organizationProvider.createOrganizationCommunity(orgComm);
            }
            return s;
        });
    }

    private OrganizationType checkOrgType(String orgType) {
        OrganizationType type = OrganizationType.fromCode(orgType);
        if (type == null) {
            LOGGER.error("orgType is wrong.orgType=" + orgType);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "orgType is wrong.");
        }
        return type;

    }

    private void checkOrgNameIsNull(String orgName) {
        if (orgName == null || orgName.equals("")) {
            LOGGER.error("orgName is empty.orgName=" + orgName);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "orgName is empty.");
        }
    }

    @Override
    public void addOrgAddress(AddOrgAddressCommand cmd) {
        User user = UserContext.current().getUser();
        this.checkOrganizationIdIsNull(cmd.getOrgId());
        this.checkAddressIsNull(cmd.getAddress());
        Organization org = this.checkOrganization(cmd.getOrgId());
        this.dbProvider.execute(s -> {
            Address address = addressProvider.findAddressByAddress(cmd.getAddress());
            if (address == null) {
                this.checkArea(cmd.getAreaId());
                this.checkCity(cmd.getCityId());
                Region city = this.checkCity(cmd.getCityId());
                Region area = this.checkArea(cmd.getAreaId());
                //创建地址
                address = new Address();
                address.setAddress(cmd.getAddress());
                address.setCityId(city.getId());
                address.setCityName(city.getName());
                address.setAreaId(area.getId());
                address.setAreaName(area.getName());
                address.setCreateTime(new Timestamp(System.currentTimeMillis()));
                address.setCreatorUid(user.getId());
                address.setStatus(AddressAdminStatus.ACTIVE.getCode());
                this.addressProvider.createAddress(address);
            }
            org.setAddressId(address.getId());
            this.organizationProvider.updateOrganization(org);
            return s;
        });
    }

    @Override
    public void importOrganization(MultipartFile[] files) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        this.checkFilesIsNull(files, user.getId());
        String rootPath = System.getProperty("user.dir");
        String filePath = rootPath + File.separator + UUID.randomUUID().toString() + ".xlsx";
        LOGGER.error("importOrganization-filePath=" + filePath);
        //将原文件暂存在服务器中
        try {
            this.storeFile(files[0], filePath);
        } catch (Exception e) {
            LOGGER.error("importOrganization-store file fail.message=" + e.getMessage());
        }
        ImportOrganizationRunnable r = new ImportOrganizationRunnable(this, filePath, userId);
        pool.execute(r);
    }

    @Override
    public void executeImportOrganization(String filePath, Long userId) {
        long parseBeginTime = System.currentTimeMillis();
        LOGGER.info("executeImportOrganization-parseBeginTime=" + parseBeginTime);
        ArrayList resultList = new ArrayList();
        try {
            File file = new File(filePath);
            if (!file.exists())
                LOGGER.error("executeImportOrganization-file is not exist.filePath=" + filePath);
            InputStream in = new FileInputStream(file);
            resultList = PropMrgOwnerHandler.processorExcel(in);
        } catch (IOException e) {
            LOGGER.error("executeImportOrganization-parse file fail.message=" + e.getMessage());
        } /*finally {
			File file = new File(filePath);
			if(file.exists())
				file.delete();
		}*/

        List<OrganizationDTO2> list = this.convertToOrganizations(resultList, userId);
        if (list == null || list.isEmpty()) {
            LOGGER.error("organizations list is empty.userId=" + userId);
            return;
        }

        for (OrganizationDTO2 r : list) {
            this.checkCityNameIsNull(r.getCityName());
            this.checkAreaNameIsNull(r.getAreaName());
            Region city = this.checkCityName(r.getCityName());
            r.setCityId(city.getId());
            Region area = this.checkAreaName(r.getAreaName(), city.getId());
            r.setAreaId(area.getId());
            this.setTokenList(r, r.getTokens());
            this.setCommunityIdsByCommunityName(r, r.getCommunityNames(), r.getCityId(), r.getAreaId());
        }
        long parseEndTime = System.currentTimeMillis();
        LOGGER.info("executeImportOrganization-parseElapse=" + (parseEndTime - parseBeginTime));

        List<OrganizationVo> orgVos = new ArrayList<OrganizationVo>();
        int orgCount = 0;
        int orgCommCount = 0;
        int orgContactCount = 0;
        int addressCount = 0;

        long listBeginTime = System.currentTimeMillis();
        LOGGER.info("executeImportOrganization-listBeginTime=" + listBeginTime);
        for (OrganizationDTO2 r : list) {
            OrganizationVo orgVo = new OrganizationVo();
            //地址
            Address address = addressProvider.findAddressByRegionAndAddress(r.getCityId(), r.getAreaId(), r.getAddressName());
            if (LOGGER.isDebugEnabled())
                LOGGER.info("address not found.cityId=" + r.getCityId() + ",areaId=" + r.getAreaId());

            if (address == null) {
                //创建地址
                Region city = this.regionProvider.findRegionById(r.getCityId());
                Region area = this.regionProvider.findRegionById(r.getAreaId());
                address = new Address();
                address.setAddress(r.getAddressName());
                address.setCityId(city.getId());
                address.setCityName(city.getName());
                address.setAreaId(area.getId());
                address.setAreaName(area.getName());
                address.setLongitude(r.getLongitude());
                address.setLatitude(r.getLatitude());
                address.setCreateTime(new Timestamp(System.currentTimeMillis()));
                address.setCreatorUid(userId);
                address.setStatus(AddressAdminStatus.ACTIVE.getCode());
                orgVo.setAddress(address);
                addressCount++;
                //this.addressProvider.createAddress(address);
            } else {
                r.setAddressId(address.getId());
            }
            //机构
            Organization org = this.convertOrgDTO2ToOrg(r);
            orgVo.setOrg(org);
            orgCount++;
            //this.organizationProvider.createOrganization(org);
            //机构小区
            for (Long comId : r.getCommunityIds()) {
                OrganizationCommunity orgComm = new OrganizationCommunity();
                orgComm.setCommunityId(comId);
                if (orgVo.getOrgComms() == null || orgVo.getOrgComms().isEmpty())
                    orgVo.setOrgComms(new ArrayList<OrganizationCommunity>());
                orgVo.getOrgComms().add(orgComm);
                orgCommCount++;
                //orgComm.setCommunityId(org.getId());
                //this.organizationProvider.createOrganizationCommunity(orgComm);
            }
            //机构电话
            for (String token : r.getTokenList()) {
                CommunityPmContact orgContact = new CommunityPmContact();
                orgContact.setContactName(null);
                orgContact.setContactToken(token);
                orgContact.setContactType(IdentifierType.MOBILE.getCode());
                orgContact.setCreateTime(new Timestamp(System.currentTimeMillis()));
                orgContact.setCreatorUid(userId);
                if (orgVo.getOrgContacts() == null)
                    orgVo.setOrgContacts(new ArrayList<CommunityPmContact>());
                orgVo.getOrgContacts().add(orgContact);
                orgContactCount++;
                //				orgContact.setCommunityId(org.getId());
                //				this.propertyMgrProvider.createPropContact(orgContact);
            }
            orgVos.add(orgVo);
        }
        long listEndTime = System.currentTimeMillis();
        LOGGER.info("executeImportOrganization-listElapse=" + (listEndTime - listBeginTime));
        LOGGER.info("executeImportOrganization:orgCount=" + orgCount + ",addressCount=" + addressCount + ",orgCommCount=" + orgCommCount + ",orgContactCount=" + orgContactCount);

        long beginTime = System.currentTimeMillis();
        LOGGER.info("executeImportOrganization-executeBeginTime=" + beginTime);
        this.dbProvider.execute(s -> {
            for (OrganizationVo r : orgVos) {
                if (r.getAddress() != null) {
                    this.addressProvider.createAddress(r.getAddress());
                    r.getOrg().setAddressId(r.getAddress().getId());
                }
                this.organizationProvider.createOrganization(r.getOrg());
                if (r.getOrgComms() != null && !r.getOrgComms().isEmpty()) {
                    for (OrganizationCommunity om : r.getOrgComms()) {
                        om.setOrganizationId(r.getOrg().getId());
                        this.organizationProvider.createOrganizationCommunity(om);
                    }
                }
                if (r.getOrgContacts() != null && !r.getOrgContacts().isEmpty()) {
                    for (CommunityPmContact oc : r.getOrgContacts()) {
                        oc.setOrganizationId(r.getOrg().getId());
                        this.propertyMgrProvider.createPropContact(oc);
                    }
                }
            }
            return s;
        });

        long endTime = System.currentTimeMillis();
        LOGGER.info("executeImportOrganization-executeElapse=" + (endTime - beginTime));
    }

    private void checkAreaNameIsNull(String areaName) {
        if (areaName == null || areaName.trim().equals("")) {
            LOGGER.error("areaName is empty.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "areaName is empty.");
        }
    }

    private void setAreaId(OrganizationDTO2 r, String areaName, Long cityId) {
        Region area = this.checkAreaName(areaName, cityId);
        r.setAreaId(area.getId());
    }

    private Region checkAreaName(String areaName, Long cityId) {
        List<Region> areas = this.regionProvider.listRegionByName(cityId, RegionScope.AREA, null, null, areaName);
        if (areas == null || areas.isEmpty()) {
            LOGGER.error("area is not found by areaName.areaName=" + areaName + ",cityId=" + cityId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "area is not found by areaName.");
        } else if (areas.size() != 1) {
            LOGGER.error("area is not unique found by areaName.areaName=" + areaName + ",cityId=" + cityId);
			/*throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"city is not unique found by cityName.");*/
        }
        return areas.get(0);
    }

    private Organization convertOrgDTO2ToOrg(OrganizationDTO2 r) {
        Organization org = new Organization();
        org.setAddressId(r.getAddressId());
        org.setLevel(1);
        org.setName(r.getOrgName());
        org.setOrganizationType(r.getOrgType());
        org.setParentId(0L);
        org.setPath("/" + r.getOrgName());
        org.setStatus(OrganizationStatus.ACTIVE.getCode());
        return org;
    }

    private void setCommunityIdsByCommunityName(OrganizationDTO2 r, String communityNames, Long cityId, Long areaId) {
        String[] commNames = communityNames.split(",");
        for (String communityName : commNames) {
            Community comm = this.findCommunityByCommName(communityName, cityId, areaId);
            if (r.getCommunityIds() == null)
                r.setCommunityIds(new ArrayList<Long>());
            r.getCommunityIds().add(comm.getId());
        }
    }

    private Community findCommunityByCommName(String communityName, Long cityId, Long areaId) {
        List<Community> comms = this.communityProvider.findCommunitiesByNameCityIdAreaId(communityName, cityId, null);
        if (comms == null || comms.isEmpty()) {
            LOGGER.error("community is not found by communityName.communityName=" + communityName + ",cityId=" + cityId + ",areaId=" + null);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "community is not found by communityName.");
        } else if (comms.size() != 1) {
            LOGGER.error("community is not unique found by communityName.communityName=" + communityName + ",cityId=" + cityId + ",areaId=" + null);
			/*throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"community is not unique found by communityName.");*/
        }
        return comms.get(0);
    }

    private void setAddressId(OrganizationDTO2 r, String addressName) {
        Address address = this.checkAddressByAddressName(addressName);
        r.setAddressId(address.getId());
    }

    private Address checkAddressByAddressName(String addressName) {
        Address address = addressProvider.findAddressByAddress(addressName);
        if (address == null) {
            LOGGER.error("address is not found by addressName.addressName=" + addressName);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "address is not found by addressName.");
        }
        return address;
    }

    private void setTokenList(OrganizationDTO2 r, String tokens) {
        String[] strList = tokens.split(",");
        for (String token : strList) {
            if (r.getTokenList() == null)
                r.setTokenList(new ArrayList<String>());
            r.getTokenList().add(token);
        }
    }

    private void setCityId(OrganizationDTO2 r, String cityName) {
        Region city = this.checkCityName(cityName);
        r.setCityId(city.getId());
    }

    private Region checkCityName(String cityName) {
        List<Region> citys = this.regionProvider.listRegionByName(null, RegionScope.CITY, null, null, cityName);
        if (citys == null || citys.isEmpty()) {
            LOGGER.error("city is not found by cityName.cityName=" + cityName);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "city is not found by cityName.");
        } else if (citys.size() != 1) {
            LOGGER.error("city is not unique found by cityName.cityName=" + cityName);
			/*throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"city is not unique found by cityName.");*/
        }
        return citys.get(0);
    }

    private void checkCityNameIsNull(String cityName) {
        if (cityName == null || cityName.trim().equals("")) {
            LOGGER.error("cityName is empty.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "cityName is empty.");
        }
    }

    private List<OrganizationDTO2> convertToOrganizations(ArrayList list, Long userId) {
        if (list == null || list.isEmpty()) {
            LOGGER.error("resultList is empty.userId=" + userId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "resultList is empty.");
        }
        List<OrganizationDTO2> result = new ArrayList<OrganizationDTO2>();
        for (int rowIndex = 1; rowIndex < list.size(); rowIndex++) {
            RowResult r = (RowResult) list.get(rowIndex);
            if (r.getA() == null || r.getA().trim().equals("")) {
                LOGGER.error("have row is empty.rowIndex=" + (rowIndex + 1));
                break;
            }
            OrganizationDTO2 dto = new OrganizationDTO2();
            dto.setCityName(this.getCityName(r.getA()));
            dto.setAreaName(this.setAreaName(r.getB()));
            dto.setOrgName(this.getOrgName(r.getC()));
            dto.setOrgType(this.getOrgType(r.getD()));
            dto.setTokens(this.getTokens(r.getE()));
            dto.setAddressName(this.getAddressName(r.getF()));
            dto.setLongitude(this.getLongitude(r.getG()));
            dto.setLatitude(this.getLatitude(r.getH()));
            dto.setCommunityNames(this.getCommunityNames(r.getI()));
            result.add(dto);
        }
        return result;
    }

    private String setAreaName(String areaName) {
        return areaName == null ? null : areaName.trim();
    }

    private String getCommunityNames(String comName) {
        return comName == null ? null : comName.trim();
    }

    private Double getLatitude(String latitude) {
        return Double.valueOf(latitude == null ? null : latitude.trim());
    }

    private Double getLongitude(String longitude) {
        return Double.valueOf(longitude == null ? null : longitude.trim());
    }

    private String getAddressName(String addressName) {
        return addressName == null ? null : addressName.trim();
    }

    private String getTokens(String tokens) {
        return tokens == null ? null : tokens.trim();
    }

    private String getOrgType(String orgType) {
        return orgType == null ? null : orgType.trim();
    }

    private String getOrgName(String orgName) {
        return orgName == null ? null : orgName.trim();
    }

    private String getCityName(String cityName) {

        return cityName == null ? null : cityName.trim();
    }

    private void checkFilesIsNull(MultipartFile[] files, Long userId) {
        if (files == null) {
            LOGGER.error("files is null。userId=" + userId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "files is null");
        }
    }

    private void storeFile(MultipartFile file, String filePath1) throws Exception {
        OutputStream out = new FileOutputStream(new File(filePath1));
        InputStream in = file.getInputStream();
        byte[] buffer = new byte[1024];
        while (in.read(buffer) != -1) {
            out.write(buffer);
        }
        out.close();
        in.close();
    }

    @Override
    public void importOrgPost(MultipartFile[] files) {
        User manaUser = UserContext.current().getUser();
        Long userId = manaUser.getId();
        this.checkFilesIsNull(files, manaUser.getId());

        String rootPath = System.getProperty("user.dir");
        String filePath = rootPath + File.separator + UUID.randomUUID().toString() + ".xlsx";
        LOGGER.error("importOrgPost-filePath=" + filePath);
        //将原文件暂存在服务器中
        try {
            this.storeFile(files[0], filePath);
        } catch (Exception e) {
            LOGGER.error("importOrgPost-store file fail.message=" + e.getMessage());
        }
        ImportOrgPostRunnable r = new ImportOrgPostRunnable(this, filePath, userId);
        pool.execute(r);
    }

    @Override
    public void executeImportOrgPost(String filePath, Long userId) {
        User operator = UserContext.current().getUser();

        String password = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";
        long parseBeginTime = System.currentTimeMillis();
        LOGGER.info(parseBeginTime + "executeImportOrgPost-parseBeginTime=" + parseBeginTime);

        ArrayList resultList = new ArrayList();
        try {
            File file = new File(filePath);
            if (!file.exists())
                LOGGER.error("executeImportOrgPost-file is not exist.filePath=" + filePath);
            InputStream in = new FileInputStream(file);
            resultList = PropMrgOwnerHandler.processorExcel(in);
        } catch (IOException e) {
            LOGGER.error("executeImportOrgPost-parse file fail.message=" + e.getMessage());
        } finally {
            File file = new File(filePath);
            if (file.exists())
                file.delete();
        }

        List<OrganizationPostDTO> list = this.convertToOrgPostDto(resultList, userId);

        if (list == null || list.isEmpty()) {
            LOGGER.error("orgPost list is empty.userId=" + userId);
            return;
        }
        long parseEndTime = System.currentTimeMillis();
        LOGGER.info(parseBeginTime + "executeImportOrgPost-parseElapse=" + (parseEndTime - parseBeginTime));

        List<OrganizationPostVo> orgPostVos = new ArrayList<OrganizationPostVo>();
        int postCount = 0;
        int taskCount = 0;
        int userCount = 0;
        int userIdenCount = 0;

        long listBeginTime = System.currentTimeMillis();
        LOGGER.info(parseBeginTime + "executeImportOrgPost-listBeginTime=" + listBeginTime);
        for (OrganizationPostDTO r : list) {
            OrganizationPostVo orgPostVo = new OrganizationPostVo();

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
            //createTime
            Date createDate = null;
            if (r.getCreateTime() != null) {
                try {
                    createDate = format.parse(r.getCreateTime());
                } catch (ParseException e) {
                    LOGGER.error("post create date not format to MM/dd/yy.createDateStr=" + r.getCreateTime());
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                            "post create date not format to MM/dd/yy.");
                }
            } else {
                createDate = new Date();
            }

            createDate = OrgDateUtils.getRadomTime(createDate);
            Timestamp currentTime = new Timestamp(createDate.getTime());

            //city
            this.checkCityNameIsNull(r.getCityName());
            Region city = this.checkCityName(r.getCityName());
            r.setCityId(city.getId());
            //org
            List<Organization> orgs = this.checkOrganizationByName(r.getOrgName(), r.getOrgType());
            Organization org = this.checkOrgAddressCity(orgs, r.getCityId());
            //token
            User tokenUser = null;
            if (r.getToken() != null && !r.getToken().trim().equals("")) {
                tokenUser = this.userService.findUserByIndentifier(operator.getNamespaceId(), r.getToken());
                if (tokenUser == null) {
                    User user = new User();
                    user.setAccountName(r.getToken());
                    user.setNickName(r.getUserName());
                    user.setStatus(UserStatus.ACTIVE.getCode());
                    user.setPoints(0);
                    user.setLevel((byte) 1);
                    user.setGender((byte) 1);
                    //password
                    String salt = EncryptionUtils.createRandomSalt();
                    user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", password, salt)));
                    user.setSalt(salt);
                    userCount++;
                    orgPostVo.setUser(user);
                    //this.userProvider.createUser(user);
                    UserIdentifier userIden = new UserIdentifier();
                    userIden.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
                    userIden.setIdentifierToken(r.getToken());
                    userIden.setIdentifierType(IdentifierType.MOBILE.getCode());
					/*userIden.setOwnerUid(user.getId());*/
                    userIdenCount++;
                    orgPostVo.setUserIden(userIden);
                    //this.userProvider.createIdentifier(userIden);
                } else {
                    orgPostVo.setUser(null);
                    orgPostVo.setUserIden(null);
                }
            }

            //post
            Post post = new Post();
            post.setAppId(AppConstants.APPID_FORUM);
            post.setForumId(ForumConstants.SYSTEM_FORUM);
            post.setParentPostId(0L);
            post.setSubject(r.getSubject());
            post.setContent(r.getContent());
            post.setContentType(PostContentType.TEXT.getCode());
            post.setPrivateFlag(PostPrivacy.PUBLIC.getCode());
            post.setCreateTime(currentTime);
            post.setUpdateTime(currentTime);
            if (r.getToken() == null || r.getToken().trim().equals(""))//token is null then use the loginUser id
                post.setCreatorUid(userId);
            else if (tokenUser != null && tokenUser.getId() != null)//token not null and use is found by token
                post.setCreatorUid(tokenUser.getId());

            if (org.getOrganizationType().equals(OrganizationType.PM.getCode()) || org.getOrganizationType().equals(OrganizationType.GARC.getCode())) {
                OrganizationCommunity orgComm = this.checkOrgCommByOrgId(org.getId());
                post.setVisibleRegionType(VisibleRegionType.COMMUNITY.getCode());
                post.setVisibleRegionId(orgComm.getCommunityId());
            } else {
                post.setVisibleRegionType(VisibleRegionType.REGION.getCode());
                post.setVisibleRegionId(org.getId());
            }

            Category category = this.checkCategory(r.getPostType());
            post.setCategoryId(category.getId());
            post.setCategoryPath(category.getPath());

            OrganizationTask task = null;
            if (r.getPostType().longValue() == CategoryConstants.CATEGORY_ID_NOTICE) {//公告
                post.setCreatorTag(org.getOrganizationType());
                post.setTargetTag(PostEntityTag.USER.getCode());
                orgPostVo.setTask(null);
            } else {
                post.setCreatorTag(PostEntityTag.USER.getCode());
                post.setTargetTag(org.getOrganizationType());
                task = new OrganizationTask();
                task.setOrganizationId(org.getId());
                task.setOrganizationType(org.getOrganizationType());
                task.setApplyEntityType(OrganizationTaskApplyEnityType.TOPIC.getCode());
                task.setApplyEntityId(0L); // 还没有帖子ID
                task.setTargetType(org.getOrganizationType());
                task.setTargetId(org.getId());
                if (r.getToken() == null || r.getToken().trim().equals(""))//token is null then use the loginUser id
                    task.setCreatorUid(userId);
                if (tokenUser != null && tokenUser.getId() != null)//token not null and use is found by token
                    task.setCreatorUid(tokenUser.getId());

                task.setCreateTime(currentTime);
                task.setUnprocessedTime(currentTime);

                OrganizationTaskType taskType = this.getOrganizationTaskType(r.getPostType());
                if (taskType != null) {
                    task.setTaskType(taskType.getCode());
                }

                task.setTaskStatus(OrganizationTaskStatus.UNPROCESSED.getCode());
                task.setOperatorUid(0L);
                taskCount++;
                orgPostVo.setTask(task);
                //this.organizationProvider.createOrganizationTask(task);
				/*post.setEmbeddedAppId(27L);
				post.setEmbeddedId(task.getId());*/
            }

            postCount++;
            orgPostVo.setPost(post);
            orgPostVos.add(orgPostVo);
            //this.forumProvider.createPost(post);
			/*if(task != null){
				task.setApplyEntityId(post.getId());
				this.organizationProvider.updateOrganizationTask(task);
			}*/
        }
        long listEndTime = System.currentTimeMillis();
        LOGGER.info(parseBeginTime + "executeImportOrgPost-listElapse=" + (listEndTime - listBeginTime));
        LOGGER.info(parseBeginTime + "executeImportOrgPost-count:userCount=" + userCount + ";userIdenCount=" + userIdenCount + ";taskCount=" + taskCount + ";postCount=" + postCount);

        if (orgPostVos == null || orgPostVos.isEmpty())
            return;

        long beginTime = System.currentTimeMillis();
        LOGGER.info(parseBeginTime + "executeImportOrgPost-executeBeginTime=" + beginTime);
        this.dbProvider.execute(s -> {
            for (OrganizationPostVo r : orgPostVos) {
                if (r.getUser() != null && r.getUserIden() != null) {
                    this.userProvider.createUser(r.getUser());
                    r.getUserIden().setOwnerUid(r.getUser().getId());//use the create user id
                    this.userProvider.createIdentifier(r.getUserIden());
                }
                if (r.getTask() != null && r.getPost() != null) {
                    if (r.getUser() != null) {//use the create user id
                        r.getTask().setCreatorUid(r.getUser().getId());
                        r.getPost().setCreatorUid(r.getUser().getId());
                    }
                    this.organizationProvider.createOrganizationTask(r.getTask());
                    r.getPost().setEmbeddedAppId(27L);
                    r.getPost().setEmbeddedId(r.getTask().getId());
                    this.forumProvider.createPost(r.getPost());
                    r.getTask().setApplyEntityId(r.getPost().getId());
                    this.organizationProvider.updateOrganizationTask(r.getTask());
                } else {
                    if (r.getUser() != null)//use the create user id
                        r.getPost().setCreatorUid(r.getUser().getId());
                    this.forumProvider.createPost(r.getPost());
                }
            }
            return s;
        });
        long endTime = System.currentTimeMillis();
        LOGGER.info(parseBeginTime + "executeImportOrgPost-executeElapse=" + (endTime - beginTime));
    }

    private Category checkCategory(Long postType) {
        Category category = this.categoryProvider.findCategoryById(postType);
        if (category == null) {
            LOGGER.error("category not found.categoryId=" + postType);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "category not found.");
        }
        return category;
    }

    private OrganizationCommunity checkOrgCommByOrgId(Long orgId) {
        List<OrganizationCommunity> orgComms = this.organizationProvider.listOrganizationCommunities(orgId);
        if (orgComms == null || orgComms.isEmpty()) {
            LOGGER.error("organization community not found.orgId=" + orgId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "organization community not found.");
        }
        if (orgComms.size() != 1)
            LOGGER.error("organization community not unique found.orgId=" + orgId);

        if (LOGGER.isDebugEnabled())
            LOGGER.error("checkOrgCommByOrgId-orgComms=" + StringHelper.toJsonString(orgComms));

        return orgComms.get(0);
    }

    private OrganizationTaskType getOrganizationTaskType(Long contentCategoryId) {
        if (contentCategoryId != null) {
            if (contentCategoryId == CategoryConstants.CATEGORY_ID_NOTICE) {
                return OrganizationTaskType.NOTICE;
            }
            if (contentCategoryId == CategoryConstants.CATEGORY_ID_REPAIRS) {
                return OrganizationTaskType.REPAIRS;
            }
            if (contentCategoryId == CategoryConstants.CATEGORY_ID_CONSULT_APPEAL) {
                return OrganizationTaskType.CONSULT_APPEAL;
            }
            if (contentCategoryId == CategoryConstants.CATEGORY_ID_COMPLAINT_ADVICE) {
                return OrganizationTaskType.COMPLAINT_ADVICE;
            }

            if (contentCategoryId == CategoryConstants.CATEGORY_ID_CLEANING) {
                return OrganizationTaskType.CLEANING;
            }
            if (contentCategoryId == CategoryConstants.CATEGORY_ID_HOUSE_KEEPING) {
                return OrganizationTaskType.HOUSE_KEEPING;
            }
            if (contentCategoryId == CategoryConstants.CATEGORY_ID_MAINTENANCE) {
                return OrganizationTaskType.MAINTENANCE;
            }
            if (contentCategoryId == CategoryConstants.CATEGORY_ID_EMERGENCY_HELP) {
                return OrganizationTaskType.EMERGENCY_HELP;
            }
        }
        LOGGER.error("Content category is not matched in organization type.contentCategoryId=" + contentCategoryId);
        return null;
    }

    private Long getTaskContentCategory(OrganizationTaskType taskType) {
        if (taskType != null) {
            if (taskType == OrganizationTaskType.NOTICE) {
                return CategoryConstants.CATEGORY_ID_NOTICE;
            }
            if (taskType == OrganizationTaskType.REPAIRS) {
                return CategoryConstants.CATEGORY_ID_REPAIRS;
            }
            if (taskType == OrganizationTaskType.CONSULT_APPEAL) {
                return CategoryConstants.CATEGORY_ID_CONSULT_APPEAL;
            }
            if (taskType == OrganizationTaskType.COMPLAINT_ADVICE) {
                return CategoryConstants.CATEGORY_ID_COMPLAINT_ADVICE;
            }
            if (taskType == OrganizationTaskType.CLEANING) {
                return CategoryConstants.CATEGORY_ID_CLEANING;
            }
            if (taskType == OrganizationTaskType.HOUSE_KEEPING) {
                return CategoryConstants.CATEGORY_ID_HOUSE_KEEPING;
            }
            if (taskType == OrganizationTaskType.MAINTENANCE) {
                return CategoryConstants.CATEGORY_ID_MAINTENANCE;
            }
            if (taskType == OrganizationTaskType.EMERGENCY_HELP) {
                return CategoryConstants.CATEGORY_ID_EMERGENCY_HELP;
            }
        }
        LOGGER.error("Content category is not matched in organization type.OrganizationTaskType=" + taskType);
        return null;
    }

    private Organization checkOrgAddressCity(List<Organization> orgs, Long cityId) {
        if (orgs == null || orgs.isEmpty())
            return null;
        for (Organization r : orgs) {
            Address address = this.addressProvider.findAddressById(r.getAddressId());
            if (address == null || address.getCityId().longValue() != cityId.longValue())
                continue;
            return r;
        }
        LOGGER.error("address cityId not equal to city id.orgs=" + StringHelper.toJsonString(orgs) + ",cityId=" + cityId.longValue());
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                "address cityId not equal to city id.");
    }

    private List<Organization> checkOrganizationByName(String orgName, String orgType) {
        List<Organization> orgs = this.organizationProvider.listOrganizationByName(orgName, orgType);

        if (orgs == null || orgs.isEmpty()) {
            LOGGER.error("organization not found by orgName.orgName=" + orgName);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "organization not found by orgName.");
        }

		/*if(LOGGER.isDebugEnabled())
			LOGGER.error("checkOrganizationByName-orgs="+StringHelper.toJsonString(orgs));*/

        return orgs;
    }

    private List<OrganizationPostDTO> convertToOrgPostDto(ArrayList list, Long userId) {
        if (list == null || list.isEmpty()) {
            LOGGER.error("resultList is empty.userId=" + userId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "resultList is empty.");
        }
        List<OrganizationPostDTO> result = new ArrayList<OrganizationPostDTO>();
        for (int rowIndex = 1; rowIndex < list.size(); rowIndex++) {
            RowResult r = (RowResult) list.get(rowIndex);
            if (r.getA() == null || r.getA().trim().equals("")) {
                LOGGER.error("have row is empty.rowIndex=" + (rowIndex + 1));
                break;
            }
            OrganizationPostDTO dto = new OrganizationPostDTO();
            dto.setCityName(r.getA());
            dto.setOrgName(r.getB());
            dto.setOrgType(r.getC());
            dto.setPostType(Long.valueOf(r.getD()));
            dto.setSubject(r.getE());
            dto.setContent(r.getF());
            dto.setUserName(r.getG());
            dto.setToken(r.getH());
            dto.setCreateTime(r.getI());
            result.add(dto);
        }
        return result;
    }

    @Override
    public void addPmBuilding(AddPmBuildingCommand cmd) {

        dbProvider.execute((TransactionStatus status) -> {

            //二期实现机构管理楼栋
//			this.organizationProvider.deletePmBuildingByOrganizationId(cmd.getCommunityId());
//			if(cmd.getIsAll() == 0) {
//				List<Long> buildingIds = this.communityProvider.listBuildingIdByCommunityId(cmd.getCommunityId());
//				cmd.setBuildingIds(buildingIds);
//			}
//
//			for(Long buildingId: cmd.getBuildingIds()) {
//				OrganizationAssignedScopes pmBuilding = new OrganizationAssignedScopes();
//				pmBuilding.setCommunityId(cmd.getCommunityId());
//				pmBuilding.setScopeCode(OrganizationScopeCode.BUILDING.getCode());
//				pmBuilding.setScopeId(buildingId);
//				this.organizationProvider.addPmBuilding(pmBuilding);
//			}

            OrganizationCommunity organizationCommunity = organizationProvider.findOrganizationCommunityByOrgIdAndCmmtyId(cmd.getOrganizationId(), cmd.getCommunityId());

            if (null == organizationCommunity) {
                organizationCommunity = new OrganizationCommunity();
                organizationCommunity.setCommunityId(cmd.getCommunityId());
                organizationCommunity.setOrganizationId(cmd.getOrganizationId());
                organizationProvider.createOrganizationCommunity(organizationCommunity);
            } else {
                LOGGER.error("Community already exists, organizationId=" + cmd.getOrganizationId() + ", communityId=" + cmd.getCommunityId());
                throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_COMMUNITY_EXISTS,
                        "Community already exists.");
            }


            return null;
        });

    }

    @Override
    public void deletePmCommunity(DeletePmCommunityCommand cmd) {
        dbProvider.execute((TransactionStatus status) -> {

            OrganizationCommunity organizationCommunity = organizationProvider.findOrganizationCommunityByOrgIdAndCmmtyId(cmd.getOrganizationId(), cmd.getCommunityId());
            if (null != organizationCommunity) {
                organizationProvider.deleteOrganizationCommunityById(organizationCommunity.getId());
            }

            //二期实现删除机构所有管辖楼栋

            return null;
        });

    }

    @Override
    public List<PmBuildingDTO> listPmBuildings(
            ListPmBuildingCommand cmd) {

        List<OrganizationAssignedScopes> pm = this.organizationProvider.findPmBuildingId(cmd.getOrgId());
        List<PmBuildingDTO> pmBuildings = new ArrayList<PmBuildingDTO>();
        if (pm != null) {

            pmBuildings = pm.stream().map(r -> {
                PmBuildingDTO dto = new PmBuildingDTO();
                dto.setBuildingId(r.getScopeId());
                Building building = communityProvider.findBuildingById(r.getScopeId());
                if (building != null)
                    dto.setBuildingName(building.getName());
                return dto;
            }).collect(Collectors.toList());

        }
        return pmBuildings;
    }

    @Override
    public List<UnassignedBuildingDTO> listUnassignedBuilding(ListPmBuildingCommand cmd) {

        List<OrganizationAssignedScopes> scopes = this.organizationProvider.findPmBuildingId(cmd.getOrgId());
        List<Long> pmBuildingIds = new ArrayList<Long>();
        if (scopes != null) {
            pmBuildingIds = this.organizationProvider.findPmBuildingId(cmd.getOrgId()).stream().map(r -> {
                Long id = r.getScopeId();
                return id;
            }).collect(Collectors.toList());
        }

        OrganizationCommunity community = this.organizationProvider.findOrganizationCommunityByOrgId(cmd.getOrgId());

        if (community == null) {
            LOGGER.error("community not found.orgId=" + cmd.getOrgId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "community not found.");
        }

        Integer namespaceId = null == cmd.getNamespaceId() ? Namespace.DEFAULT_NAMESPACE : cmd.getNamespaceId();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<Building> buildings = this.communityProvider.ListBuildingsByCommunityId(locator, AppConstants.PAGINATION_MAX_SIZE + 1, community.getCommunityId(), namespaceId, null);
        List<Building> unassigned = new ArrayList<Building>();
        for (Building building : buildings) {
            if (!pmBuildingIds.contains(building.getId()))
                unassigned.add(building);
        }

        List<UnassignedBuildingDTO> unassignedBuildings = unassigned.stream().map(r -> {
            UnassignedBuildingDTO dto = new UnassignedBuildingDTO();
            dto.setBuildingId(r.getId());
            dto.setBuildingName(r.getName());
            return dto;
        }).collect(Collectors.toList());

        return unassignedBuildings;
    }


    public List<PmManagementCommunityDTO> listPmManagementComunites(ListPmManagementComunitesCommand cmd) {
        List<OrganizationCommunity> orgCommunities = organizationProvider.listOrganizationCommunities(cmd.getOrganizationId());

        List<PmManagementCommunityDTO> dtos = new ArrayList<PmManagementCommunityDTO>();

        for (OrganizationCommunity organizationCommunity : orgCommunities) {
            Community community = communityProvider.findCommunityById(organizationCommunity.getCommunityId());
            if (null != community) {
                PmManagementCommunityDTO dto = ConvertHelper.convert(community, PmManagementCommunityDTO.class);
                //一期默认全部，二期做具体业务判断是否全部还是部分
                dto.setIsAll(PmManagementIsAll.ALL.getCode());

                dtos.add(dto);

            }
        }

        return dtos;
    }


    @Override
    public PmManagementsResponse listPmManagements(ListPmManagementsCommand cmd) {

        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor() == null ? 0L : cmd.getPageAnchor());
        if (cmd.getPageSize() == null) {
            int value = configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
            cmd.setPageSize(value);
        }


        List<Organization> org = this.organizationProvider.listPmManagements(locator, cmd.getPageSize() + 1, cmd.getOrganizationId(), cmd.getCommunityId());

        Long nextPageAnchor = null;
        if (org != null && org.size() > cmd.getPageSize()) {
            org.remove(org.size() - 1);
            nextPageAnchor = org.get(org.size() - 1).getId();
        }
        PmManagementsResponse response = new PmManagementsResponse();
        response.setNextPageAnchor(nextPageAnchor);
        List<PmManagementsDTO> pmManagements = new ArrayList<PmManagementsDTO>();
        if (org != null) {
            pmManagements = org.stream().map(pm -> {
                PmManagementsDTO management = new PmManagementsDTO();
                management.setIsAll(1);
                List<OrganizationAssignedScopes> scopes = this.organizationProvider.findPmBuildingId(pm.getId());
                int size = this.communityProvider.countBuildingsBycommunityId(cmd.getCommunityId());
                if (scopes != null && scopes.size() == size)
                    management.setIsAll(0);
                if (scopes != null) {
                    List<PmBuildingDTO> buildings = scopes.stream().map(r -> {
                        PmBuildingDTO dto = new PmBuildingDTO();
                        dto.setBuildingId(r.getScopeId());
                        Building building = communityProvider.findBuildingById(r.getScopeId());
                        if (building != null)
                            dto.setBuildingName(building.getName());
                        return dto;
                    }).collect(Collectors.toList());

                    management.setBuildings(buildings);
                }
                management.setPmName(pm.getName());
                management.setPmId(pm.getId());
                Address addr = this.addressProvider.findAddressById(pm.getAddressId());
                if (addr != null)
                    management.setPlate(addr.getAddress());
                return management;
            }).collect(Collectors.toList());
        }

        response.setPmManagement(pmManagements);
        return response;
    }

    @Override
    public ListTopicsByTypeCommandResponse listUserTask(ListUserTaskCommand cmd) {

        User user = UserContext.current().getUser();
        Long commuId = cmd.getCommunityId();

        if (null == cmd.getCommunityId()) {
            commuId = user.getCommunityId();
        }

        Community community = communityProvider.findCommunityById(commuId);

        if (cmd.getPageOffset() == null)
            cmd.setPageOffset(1L);

        ListTopicsByTypeCommandResponse response = new ListTopicsByTypeCommandResponse();
        List<OrganizationTaskDTO2> list = new ArrayList<OrganizationTaskDTO2>();

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset(), pageSize);

        List<OrganizationTask> orgTaskList = this.organizationProvider.listOrganizationTasksByOperatorUid(user.getId(), cmd.getTaskType(), pageSize + 1, offset);
        if (orgTaskList != null && !orgTaskList.isEmpty()) {
            if (orgTaskList.size() == pageSize + 1) {
                response.setNextPageOffset(cmd.getPageOffset() + 1);
                orgTaskList.remove(orgTaskList.size() - 1);
            }
            for (OrganizationTask task : orgTaskList) {
                try {
                    if (task.getOrganizationId().equals(cmd.getOrganizationId())) {
                        PostDTO dto = this.forumService.getTopicById(task.getApplyEntityId(), commuId, false);
                        if (dto.getForumId().equals(community.getDefaultForumId())) {
                            OrganizationTaskDTO2 taskDto = ConvertHelper.convert(dto, OrganizationTaskDTO2.class);
                            this.convertTaskToDto(task, taskDto);
                            list.add(taskDto);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("could not found topic by task's applyEntityId.taskId=" + task.getId() + ",applyEntityId=" + task.getApplyEntityId());
                }
            }
        }

        response.setRequests(list);
        return response;
    }

    // 每个不同版登录进来算不同的用户，故不需要再有partner user
//	@Override
//	public void processPartnerOrganizationUser(Long userId, Long partnerId) {
//	    long startTime = System.currentTimeMillis();
//	    if(userId == null || userId <= 0) {
//	        LOGGER.info("User id is null, ignore to processStat partner organization user, userId=" + userId + ", partnerId=" + partnerId);
//	        return;
//	    }
//	    User user = userProvider.findUserById(userId);
//	    if(user == null) {
//            LOGGER.error("User not found, userId=" + userId + ", partnerId=" + partnerId);
//            return;
//        }
//
//	    if(partnerId == null || partnerId <= 0) {
//            LOGGER.info("Partner id is null, ignore to processStat partner organization user, userId=" + userId + ", partnerId=" + partnerId);
//            return;
//	    }
//        Organization organization = organizationProvider.findOrganizationById(partnerId);
//        if(organization == null) {
//            LOGGER.error("Organization not found, userId=" + userId + ", partnerId=" + partnerId);
//            return;
//        }
//
//        OrganizationType type = OrganizationType.fromCode(organization.getOrganizationType());
//        if(type == OrganizationType.PARTNER) {
//            setDefaultPartnerCommunity(user, organization);
//        } else {
//            LOGGER.error("Organization is not partner type, userId=" + userId + ", partnerId=" + partnerId + ", organizationType=" + type);
//        }
//
//        long endTime = System.currentTimeMillis();
//        if(LOGGER.isInfoEnabled()) {
//            LOGGER.info("Process partner organization user, userId=" + userId + ", partnerId=" + partnerId + ", elapse=" + (endTime - startTime));
//        }
//	}

//	private void setDefaultPartnerCommunity(User user, Organization organization) {
//	    try {
//	        List<UserCurrentEntity> entityList = userService.listUserCurrentEntity(user.getId());
//	        if(!containPartnerCommunity(entityList)) {
//	            int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, 1);
//	            List<OrganizationCommunity> result = organizationProvider.listOrganizationCommunities(organization.getId(), 1, pageSize);
//	            if(result != null && result.size() > 0) {
//	                Long communityId = result.get(0).getCommunityId();
//	                userService.updateUserCurrentCommunityToProfile(user.getId(), communityId);
//	                if(LOGGER.isInfoEnabled()) {
//	                    LOGGER.info("Set default partner community, userId=" + user.getId() + ", communityId=" + communityId
//	                        + ", partnerId=" + organization.getId());
//	                }
//	            } else {
//	                if(LOGGER.isInfoEnabled()) {
//	                    LOGGER.info("Community not found, ignore to set default partner community, userId=" + user.getId()
//	                        + ", partnerId=" + organization.getId());
//	                }
//	            }
//	        }
//	    } catch(Exception e) {
//	        LOGGER.error("Failed to set default partner community, userId=" + user.getId() + ", partnerId=" + organization.getId());
//	    }
//	}

//	private boolean containPartnerCommunity(List<UserCurrentEntity> entityList) {
//	    if(entityList == null || entityList.size() == 0) {
//	        return false;
//	    }
//
//	    boolean isFound = false;
//	    for(UserCurrentEntity entity : entityList) {
//	        UserCurrentEntityType type = UserCurrentEntityType.fromCode(entity.getEntityType());
//	        if(type == UserCurrentEntityType.COMMUNITY_COMMERCIAL) {
//	            isFound = true;
//	            break;
//	        }
//	    }
//
//	    return isFound;
//	}

//	private void joinPartnerOrganization(User user, Organization organization) {
//        try {
//            OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(user.getId(), organization.getId());
//            if(member != null) {
//                LOGGER.error("Organization member already existed, userId=" + user.getId() + ", partnerId=" + organization.getId());
//                return;
//            }
//
//            member = new OrganizationMember();
//
//            member.setContactName(user.getNickName());
//            member.setCommunityId(organization.getId());
//            member.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
//            member.setTargetId(user.getId());
//            member.setTargetType(OrganizationMemberTargetType.USER.getCode());
//
//            UserIdentifier identifier = this.getUserMobileIdentifier(user.getId());
//            if(identifier != null){
//                member.setContactToken(identifier.getIdentifierToken());
//                member.setContactType(identifier.getIdentifierType());
//            }
//
//            this.organizationProvider.createOrganizationMember(member);
//        } catch(Exception e) {
//            LOGGER.error("Failed to join partner organization, userId=" + user.getId() + ", partnerId=" + organization.getId(), e);
//        }
//	}

    @Override
    public SearchTopicsByTypeResponse searchTopicsByType(
            SearchTopicsByTypeCommand cmd) {
        PostAdminQueryFilter filter = new PostAdminQueryFilter();
        String keyword = cmd.getKeyword();
        if (!StringUtils.isEmpty(keyword)) {
            filter.addQueryTerm(PostAdminQueryFilter.TERM_CONTENT);
            filter.addQueryTerm(PostAdminQueryFilter.TERM_SUBJECT);
            filter.setQueryString(keyword);
        }

        List<Long> parentPostId = new ArrayList<Long>();
        parentPostId.add(0L);
        filter.includeFilter(PostAdminQueryFilter.TERM_PARENTPOSTID, parentPostId);

        int pageNum = 0;
        if (cmd.getPageAnchor() != null) {
            pageNum = cmd.getPageAnchor().intValue();
        } else {
            pageNum = 0;
        }
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        filter.setPageInfo(pageNum, pageSize);
        ListPostCommandResponse queryResponse = postSearcher.query(filter);

        SearchTopicsByTypeResponse response = new SearchTopicsByTypeResponse();
        response.setKeywords(queryResponse.getKeywords());
        response.setNextPageAnchor(queryResponse.getNextPageAnchor());

        List<OrganizationTask> orgTaskList = new ArrayList<OrganizationTask>();
        if ("mytask".equals(cmd.getFlag())) {
            User user = UserContext.current().getUser();
            orgTaskList = this.organizationProvider.listOrganizationTasksByOperatorUid(user.getId(), cmd.getTaskType(), AppConstants.PAGINATION_MAX_SIZE, 0);
        } else {

            orgTaskList = this.organizationProvider.listOrganizationTasksByOrgIdAndType(cmd.getOrganizationId(), cmd.getTaskType(), cmd.getTaskStatus(), AppConstants.PAGINATION_MAX_SIZE, 0);

        }


        if (orgTaskList != null && orgTaskList.size() > 0) {
            Map<Long, OrganizationTask> taskMap = new HashMap<Long, OrganizationTask>();
            for (OrganizationTask orgTask : orgTaskList) {
                taskMap.put(orgTask.getApplyEntityId(), orgTask);
            }

            List<OrganizationTaskDTO2> taskList = new ArrayList<OrganizationTaskDTO2>();
            List<PostDTO> postList = queryResponse.getPosts();
            for (PostDTO post : postList) {
                try {
                    if (taskMap.get(post.getId()) != null) {
                        OrganizationTask task = taskMap.get(post.getId());
                        PostDTO temp = this.forumService.getTopicById(post.getId(), cmd.getCommunityId(), false);
                        OrganizationTaskDTO2 taskDto = ConvertHelper.convert(temp, OrganizationTaskDTO2.class);
                        this.convertTaskToDto(task, taskDto);

                        taskList.add(taskDto);
                    }

                } catch (Exception e) {
                    LOGGER.error(e.toString());
                }
            }

            response.setRequests(taskList);
        }

        return response;
    }

    @Override
    public void createDepartment(CreateDepartmentCommand cmd) {
        this.checkOrgNameIsNull(cmd.getDepartmentName());
        DepartmentType departmentType = this.checkDepartmentType(cmd.getDepartmentType());

        cmd.setDepartmentType(departmentType.getCode());
        Organization org = new Organization();
        org.setName(cmd.getDepartmentName());
        org.setOrganizationType(OrganizationType.PM.getCode());
        org.setDepartmentType(cmd.getDepartmentType());
        org.setParentId(cmd.getParentId());

        Organization parOrg = this.checkOrganization(cmd.getParentId());
        org.setPath(parOrg.getPath());
        org.setLevel(parOrg.getLevel() + 1);

        org.setAddressId(0L);
        org.setStatus(OrganizationStatus.ACTIVE.getCode());
        this.dbProvider.execute(s -> {
            organizationProvider.createOrganization(org);

            User user = UserContext.current().getUser();
            RoleAssignment roleAssignment = new RoleAssignment();
            roleAssignment.setCreatorUid(user.getId());
            roleAssignment.setOwnerType("system");
            roleAssignment.setRoleId(cmd.getRoleId());
            roleAssignment.setTargetType(EntityType.ORGANIZATIONS.getCode());
            roleAssignment.setTargetId(org.getId());
            roleAssignment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            aclProvider.createRoleAssignment(roleAssignment);
            return s;
        });
    }

    private DepartmentType checkDepartmentType(String depType) {
        DepartmentType type = DepartmentType.fromCode(depType);
        if (type == null) {
            LOGGER.error("depType is wrong.depType=" + depType);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "depType is wrong.");
        }
        return type;

    }


/*    @Override
    public ListDepartmentsCommandResponse listDepartments(
            ListDepartmentsCommand cmd) {
        ListDepartmentsCommandResponse response = new ListDepartmentsCommandResponse();
        cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
        Organization org = organizationProvider.findOrganizationById(cmd.getParentId());
        if (org != null) {
            int totalCount = organizationProvider.countDepartments(org.getPath() + "/%");
            if (totalCount == 0) return response;

            int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
            int pageCount = getPageCount(totalCount, pageSize);

            List<Organization> result = organizationProvider.listDepartments(org.getPath() + "/%", cmd.getPageOffset(), pageSize);
            List<Organization> depts = organizationProvider.listDepartments(org.getPath() + "/%", 1, 1000);

            if (result != null && result.size() > 0) {
                List<RoleAssignment> roleAssignments = this.aclProvider.getAllRoleAssignments();
                Map<Long, RoleAssignment> roleAssignmentMap = new HashMap<Long, RoleAssignment>();
                for (RoleAssignment roleass : roleAssignments) {
                    if (EntityType.ORGANIZATIONS.getCode().equals(roleass.getTargetType()))
                        roleAssignmentMap.put(roleass.getTargetId(), roleass);
                }
                Map<Long, Organization> deptMaps = this.convertDeptListToMap(depts);
                response.setDepartments(result.stream().map(r -> {
                    DepartmentDTO department = new DepartmentDTO();
                    department.setId(r.getId());
                    department.setDepartmentName(r.getName());
                    department.setDepartmentType(r.getDepartmentType());
                    department.setSuperiorDepartment(null == deptMaps.get(r.getParentId()) ? "" : deptMaps.get(r.getParentId()).getName());
                    if (roleAssignmentMap.get(department.getId()) != null) {
                        Long roleId = roleAssignmentMap.get(department.getId()).getRoleId();
                        Role role = this.aclProvider.getRoleById(roleId);
                        if (role != null)
                            department.setRole(role.getName());
                    }

                    return department;
                }).collect(Collectors.toList()));
            }
            response.setNextPageOffset(cmd.getPageOffset() == pageCount ? null : cmd.getPageOffset() + 1);
        }

        return response;
    }*/

    private Map<Long, Organization> convertDeptListToMap(List<Organization> depts) {
        Map<Long, Organization> map = new HashMap<Long, Organization>();
        if (null == depts) {
            return map;
        }
        for (Organization dept : depts) {
            map.put(dept.getId(), dept);
        }
        return map;
    }


    @Override
    public boolean updateOrganizationMemberByIds(
            UpdateOrganizationMemberByIdsCommand cmd) {
//		organizationProvider.updateOrganizationMemberByIds(cmd.getIds(), cmd.getOrgId());
        return true;
    }

    @Override
    public void checkOrganizationPrivilege(long uid, long organizationId, long privilege) {
        ResourceUserRoleResolver resolver = PlatformContext.getComponent(EntityType.ORGANIZATIONS.getCode());
        List<Long> roles = resolver.determineRoleInResource(uid, organizationId, EntityType.ORGANIZATIONS.getCode(), organizationId);
        if (!this.aclProvider.checkAccess(EntityType.GROUP.getCode(), organizationId, EntityType.USER.getCode(), uid, privilege,
                roles))
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                    "Insufficient privilege");
    }

    @Override
    public List<Long> getUserResourcePrivilege(long uid, long organizationId) {
        User user = UserContext.current().getUser();
        List<Long> userRoleIds = aclProvider.getRolesFromResourceAssignments("system", null, EntityType.USER.getCode(), user.getId(), null);

        if (null != userRoleIds && 0 != userRoleIds.size()) return userRoleIds;

        userRoleIds = new ArrayList<Long>();

//		OrganizationMember organizationMember = organizationProvider.findOrganizationMemberByUIdAndOrgId(user.getId(), organizationId);
//
//		if(null == organizationMember){
//			userRoleIds.add(RoleConstants.ORGANIZATION_TASK_MGT);
//			return userRoleIds;
//		}
//
//		if(null != organizationMember.getGroupId()){
//			userRoleIds =  aclProvider.getRolesFromResourceAssignments("system", null, EntityType.ORGANIZATIONS.getCode(), organizationMember.getGroupId(), null);
//			return userRoleIds;
//		}
//
//		if(OrganizationMemberGroupType.MANAGER.getCode().equals(organizationMember.getMemberGroup())){
//			userRoleIds.add(RoleConstants.ORGANIZATION_ADMIN);
//		}else{
//			userRoleIds.add(RoleConstants.ORGANIZATION_TASK_MGT);
//		}

        return userRoleIds;
    }

    /**
     * 申请加入企业
     *
     * @param cmd
     * @return
     */
    @Override
    public OrganizationDTO applyForEnterpriseContact(CreateOrganizationMemberCommand cmd) {
        User user = UserContext.current().getUser();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if (StringUtils.isEmpty(cmd.getTargetId())) {
            cmd.setTargetId(user.getId());
        }

        Organization organization = this.checkOrganization(cmd.getOrganizationId());

        Long communityId = this.getOrganizationActiveCommunityId(organization.getId());

        Community community = new Community();

        if (null != communityId) {
            community = communityProvider.findCommunityById(communityId);
        }

        // Check exists
        OrganizationMember organizationmember = organizationProvider.findOrganizationMemberByUIdAndOrgId(cmd.getTargetId(), organization.getId());
        if (null != organizationmember) {

            OrganizationDTO organizationDTO = ConvertHelper.convert(organization, OrganizationDTO.class);

            OrganizationDetailDTO organizationDetailDTO = toOrganizationDetailDTO(organization.getId(), false);

            organizationDTO.setDisplayName(organizationDetailDTO.getDisplayName());
            organizationDTO.setAvatarUrl(organizationDetailDTO.getAvatarUrl());
            organizationDTO.setContact(organizationDetailDTO.getContact());
            organizationDTO.setDescription(organizationDetailDTO.getDescription());
            organizationDTO.setAddress(organizationDetailDTO.getAddress());
            organizationDTO.setMemberStatus(organizationmember.getStatus());
            if (null != community) {
                organizationDTO.setCommunityId(community.getId());
                organizationDTO.setCommunityName(community.getName());
                organizationDTO.setDefaultForumId(community.getDefaultForumId());
                organizationDTO.setFeedbackForumId(community.getFeedbackForumId());
                organizationDTO.setCommunityType(community.getCommunityType());
            }
            organizationDTO.setManagerList(getAdmins(cmd.getOrganizationId()));
            return organizationDTO;
        }

        //未查询到匹配的记录
        organizationmember = this.dbProvider.execute((TransactionStatus status) -> {
            UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(cmd.getTargetId(),
                    IdentifierType.MOBILE.getCode());

            OrganizationMember member = new OrganizationMember();
            member.setNamespaceId(namespaceId);
            member.setContactToken(identifier.getIdentifierToken());
            member.setContactType(identifier.getIdentifierType());
            member.setContactName(StringUtils.isEmpty(cmd.getContactName()) ? user.getNickName() : cmd.getContactName());
            member.setOrganizationId(cmd.getOrganizationId());
            member.setTargetType(OrganizationMemberTargetType.USER.getCode());
            member.setTargetId(cmd.getTargetId());
            member.setStatus(OrganizationMemberStatus.WAITING_FOR_APPROVAL.getCode());
            member.setTargetId(identifier.getOwnerUid());
            member.setGroupType(organization.getGroupType());
            member.setGroupPath(organization.getPath());
            member.setGender(cmd.getGender());
            member.setEmployeeNo(cmd.getEmployeeNo());
            member.setContactDescription(cmd.getContactDescription());
            // organizationProvider.createOrganizationMember(member);
            member.setStringTag3(cmd.getEmail());
            member.setCreatorUid(user.getId());
            member.setNickName(user.getNickName());
            member.setAvatar(user.getAvatar());

            /**创建企业级的member/detail/user_organiztion记录**/
            OrganizationMember tempMember = createOrganiztionMemberWithoutDetailAndUserOrganization(member, cmd.getOrganizationId());
            member.setId(tempMember.getId());
            //增加customer admin record 状态
            try {
                customerProvider.updateEnterpriseCustomerAdminRecord(member.getContactToken(), member.getNamespaceId());
                customerProvider.updateCustomerTalentRegisterStatus(member.getContactToken());
            } catch (Exception e) {
                LOGGER.error("update enterprise customer admins status error:{}", e);
            }
            return member;
        });

        if (organizationmember == null) {
            LOGGER.error("Failed to apply for organization member, userId="
                    + cmd.getTargetId() + ", cmd=" + cmd);
            return null;
        } else {

            addWaittingOrganizationMemberLog(organizationmember);
            sendMessageForContactApply(organizationmember);

            OrganizationDTO organizationDTO = ConvertHelper.convert(organization, OrganizationDTO.class);

            OrganizationDetailDTO organizationDetailDTO = toOrganizationDetailDTO(organization.getId(), false);

            organizationDTO.setDisplayName(organizationDetailDTO.getDisplayName());
            organizationDTO.setAvatarUrl(organizationDetailDTO.getAvatarUrl());
            organizationDTO.setContact(organizationDetailDTO.getContact());
            organizationDTO.setDescription(organizationDetailDTO.getDescription());
            organizationDTO.setAddress(organizationDetailDTO.getAddress());
            organizationDTO.setMemberStatus(organizationmember.getStatus());

            if (null != community) {
                organizationDTO.setCommunityId(community.getId());
                organizationDTO.setCommunityName(community.getName());
                organizationDTO.setDefaultForumId(community.getDefaultForumId());
                organizationDTO.setFeedbackForumId(community.getFeedbackForumId());
                organizationDTO.setCommunityType(community.getCommunityType());
            }
            organizationDTO.setManagerList(getAdmins(cmd.getOrganizationId()));
            return organizationDTO;
        }
    }

    private void addWaittingOrganizationMemberLog(OrganizationMember organizationMember){

        OrganizationMemberLog orgLog = new OrganizationMemberLog();
        orgLog.setOrganizationId(organizationMember.getOrganizationId());
        orgLog.setContactName(organizationMember.getContactName());
        orgLog.setContactToken(organizationMember.getContactToken());
        orgLog.setUserId(organizationMember.getTargetId());
        orgLog.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        orgLog.setOperationType(OperationType.WAITING_FOR_APPROVAL.getCode());
        orgLog.setRequestType(RequestType.USER.getCode());
        orgLog.setOperatorUid(UserContext.current().getUser().getId());
        orgLog.setContactDescription(organizationMember.getContactDescription());
        this.organizationProvider.createOrganizationMemberLog(orgLog);
    }

    private boolean checkUserEmailDomain(ApplyForEnterpriseContactNewCommand cmd) {
        if (cmd.getOrganizationId() == null) {
            LOGGER.error("OrganizationId is null");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "OrganizationId is null");
        }
        Organization organization = this.organizationProvider.findOrganizationById(cmd.getOrganizationId());
        if (organization == null) {
            LOGGER.error("organization is null");
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ORG_NOT_EXIST, "organization not exists");
        }
        boolean flag = false;
        if (!StringUtils.isEmpty(cmd.getEmail())) {
            String[] email = cmd.getEmail().split("@");
            if (email != null && email.length > 1) {
                String emailDomain = email[1];
                if (!StringUtils.isEmpty(emailDomain) && emailDomain.equals(organization.getEmailDomain())) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    @Override
    public OrganizationDTO applyForEnterpriseContactNew(ApplyForEnterpriseContactNewCommand cmd) {
        boolean flag = checkUserEmailDomain(cmd);
        OrganizationDTO dto = new OrganizationDTO();
        if (flag) {
            ApplyForEnterpriseContactByEmailCommand command = ConvertHelper.convert(cmd, ApplyForEnterpriseContactByEmailCommand.class);
            this.applyForEnterpriseContactByEmail(command);
            return dto;
        }else {
            CreateOrganizationMemberCommand command = ConvertHelper.convert(cmd, CreateOrganizationMemberCommand.class);
            dto = this.applyForEnterpriseContact(command);
            return dto;
        }
    }


    /**
     * 批准用户加入企业
     */
    @Override
    public void approveForEnterpriseContact(ApproveContactCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorUid = operator.getId();
        User user = this.userProvider.findUserById(cmd.getUserId());
        // 如果有人先把申请拒绝了，那就找不到此人了，此时也让它成功以便客户端不报错 by lqs 20160415
        // OrganizationMember member = checkEnterpriseContactParameter(cmd.getEnterpriseId(), cmd.getUserId(), operatorUid, "approveForEnterpriseContact");
        List<OrganizationMember> members = new ArrayList<>();
        if (cmd.getEnterpriseId() != null && cmd.getUserId() != null) {
            members = this.organizationProvider.findOrganizationMembersByOrgIdAndUId(cmd.getUserId(), cmd.getEnterpriseId());
        } else {
            LOGGER.error("Invalid enterprise id or target user id, operatorUid=" + operatorUid + ", cmd=" + cmd);
        }

        if (members.size() > 0) {
            for (OrganizationMember member : members) {
                if (member != null) {
                    if (OrganizationMemberStatus.fromCode(member.getStatus()) != OrganizationMemberStatus.WAITING_FOR_APPROVAL) {
                        //不抛异常会导致客户端小黑条消不掉 by sfyan 20170120
                        LOGGER.debug("organization member status error, status={}, cmd={}", member.getStatus(), cmd);
//				throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_MEMBER_STSUTS_MODIFIED,
//						"organization member status error.");
//  HEAD
                    } else {
                        //将直接更新状态修改为
//                        member.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
//                        member.setOperatorUid(operatorUid);
//                        member.setApproveTime(System.currentTimeMillis());
//                        updateEnterpriseContactStatus(operator.getId(), member);
                        AddArchivesContactCommand addArchivesContactCommand = ConvertHelper.convert(member, AddArchivesContactCommand.class);
                        addArchivesContactCommand.setOrganizationId(cmd.getEnterpriseId());
                        List<Long> departmentIds = new ArrayList<>();
                        departmentIds.add(cmd.getEnterpriseId());
                        addArchivesContactCommand.setDepartmentIds(departmentIds);
                        addArchivesContactCommand.setNamespaceId(user.getNamespaceId());
                        this.archivesService.addArchivesContact(addArchivesContactCommand);
                        DaoHelper.publishDaoAction(DaoAction.CREATE, OrganizationMember.class, member.getId());
                        sendMessageForContactApproved(member);

                        //通过认证的同步到企业客户的人才团队中 21710
                        customerService.createCustomerTalentFromOrgMember(member.getOrganizationId(), member);
                        //人事信息中添加其他信息
                        OrganizationMemberDetails detail = organizationProvider
                        		.findOrganizationMemberDetailsByOrganizationIdAndContactToken(member.getOrganizationId(), member.getContactToken());
                        if( detail != null ){
                            UserIdentifier identifier = this.userProvider.findUserIdentifiersOfUser(user.getId(),user.getNamespaceId());
                            if (identifier != null) {
                                detail.setRegionCode(identifier.getRegionCode().toString());
                            }
                            detail.setWorkEmail(member.getStringTag3());
                        	Date date = new Date();
                        	detail.setCheckInTime(new java.sql.Date(date.getTime()));
                        	organizationProvider.updateOrganizationMemberDetails(detail, detail.getId());
                        }
                        
                    }
                } else {
                    LOGGER.warn("Enterprise contact not found, maybe it has been rejected, operatorUid=" + operatorUid + ", cmd=" + cmd);
//
//                } else {
//                    member.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
//                    member.setOperatorUid(operatorUid);
//                    member.setApproveTime(System.currentTimeMillis());
//                    updateEnterpriseContactStatus(operator.getId(), member);
//                    DaoHelper.publishDaoAction(DaoAction.CREATE, OrganizationMember.class, member.getId());
//                    sendMessageForContactApproved(member);
//                    //记录添加log
//                    OrganizationMemberLog orgLog = ConvertHelper.convert(cmd, OrganizationMemberLog.class);
//                    orgLog.setOrganizationId(member.getOrganizationId());
//                    orgLog.setContactName(member.getContactName());
//                    orgLog.setContactToken(member.getContactToken());
//                    orgLog.setUserId(member.getTargetId());
//                    orgLog.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//                    orgLog.setOperationType(OperationType.JOIN.getCode());
//                    orgLog.setRequestType(RequestType.USER.getCode());
//                    orgLog.setOperatorUid(UserContext.current().getUser().getId());
//                        orgLog.setContactDescription(member.getContactDescription());
//                    this.organizationProvider.createOrganizationMemberLog(orgLog);
//  master
                }
            }
        }
        this.doorAccessService.joinCompanyAutoAuth(UserContext.getCurrentNamespaceId(), cmd.getEnterpriseId(), cmd.getUserId());

    }

    /**
     * 拒绝申请
     */
    @Override
    public void rejectForEnterpriseContact(RejectContactCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorUid = operator.getId();
        OrganizationMember member = checkEnterpriseContactParameterContainReject(cmd.getEnterpriseId(), cmd.getUserId(), operatorUid, "rejectForEnterpriseContact");
        if (OrganizationMemberStatus.fromCode(member.getStatus()) != OrganizationMemberStatus.WAITING_FOR_APPROVAL) {
            //不抛异常会导致客户端小黑条消不掉 by sfyan 20170120
            LOGGER.debug("organization member status error, status={}, cmd={}", member.getStatus(), cmd);
			throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_MEMBER_STSUTS_MODIFIED,
					"organization member status error.");
        } else {
            member.setOperatorUid(operatorUid);
            member.setApproveTime(System.currentTimeMillis());
            //拒绝申请时，增加认证记录 add by 梁燕龙 20180920
            addRejectOrganizationMemberLog(member);
            deleteEnterpriseContactStatus(operatorUid, member);
            sendMessageForContactReject(member , cmd.getRejectText());
        }

    }

    private void addRejectOrganizationMemberLog(OrganizationMember organizationMember) {
        OrganizationMemberLog orgLog = new OrganizationMemberLog();
        orgLog.setOrganizationId(organizationMember.getOrganizationId());
        orgLog.setContactName(organizationMember.getContactName());
        orgLog.setContactToken(organizationMember.getContactToken());
        orgLog.setUserId(organizationMember.getTargetId());
        orgLog.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        orgLog.setOperationType(OperationType.REJECT.getCode());
        orgLog.setRequestType(RequestType.USER.getCode());
        orgLog.setOperatorUid(UserContext.current().getUser().getId());
        orgLog.setContactDescription(organizationMember.getContactDescription());
        this.organizationProvider.createOrganizationMemberLog(orgLog);
    }


    /**
     * 批准用户加入企业
     */
    @Override
    public void batchApproveForEnterpriseContact(BatchApproveContactCommand cmd) {

        User operator = UserContext.current().getUser();
        Long operatorUid = operator.getId();

        if (cmd.getApproveInfo() != null && cmd.getApproveInfo().size() > 0) {
            for (ApproveContactCommand approvalCmd : cmd.getApproveInfo()) {
                this.approveForEnterpriseContact(approvalCmd);

            }
        } else {
            LOGGER.error("Invalid enterprise id or target user id, operatorUid=" + operatorUid + ", cmd=" + cmd);
        }

    }

    /**
     * 拒绝申请
     */
    @Override
    public void batchRejectForEnterpriseContact(BatchRejectContactCommand cmd) {

        User operator = UserContext.current().getUser();
        Long operatorUid = operator.getId();

        if (cmd.getRejectInfo() != null && cmd.getRejectInfo().size() > 0) {
            for (RejectContactCommand rejectCmd : cmd.getRejectInfo()) {
                rejectCmd.setRejectText(cmd.getRejectText());
                this.rejectForEnterpriseContact(rejectCmd);

            }
        } else {
            LOGGER.error("Invalid enterprise id or target user id, operatorUid=" + operatorUid + ", cmd=" + cmd);
        }

    }

    private List<OrganizationMember> listOrganizationMemberByOrganizationPathAndContactToken(String path, String contactToken) {
        return organizationProvider.listOrganizationMemberByPath(path, null, contactToken);
    }

    @Override
    public List<OrganizationMember> listOrganizationMemberByOrganizationPathAndUserId(String path, Long userId) {
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode());
        if (null == userIdentifier)
            return null;
        return listOrganizationMemberByOrganizationPathAndContactToken(path, userIdentifier.getIdentifierToken());
    }

    @Override
    public String checkIfLastOnNode(DeleteOrganizationPersonnelByContactTokenCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        //先判断是否是子公司
        Organization org = this.organizationProvider.findOrganizationById(cmd.getOrganizationId());
        if (org != null) {
            if (org.getParentId() == 0L) {//为总公司
                if (organizationProvider.checkIfLastOnNode(namespaceId, cmd.getOrganizationId(), cmd.getContactToken(), org.getPath())) {//如果是总公司下最后一个onNode节点
                    return DeleteOrganizationContactScopeType.ALL_NOTE.getCode();
                } else {//不是最后一个节点
                    return "0";
                }
            } else {//为分公司
                if (organizationProvider.checkIfLastOnNode(namespaceId, cmd.getOrganizationId(), cmd.getContactToken(), org.getPath())) {//如果是分公司下最后一个onNode节点
                    if (organizationProvider.checkIfLastOnNode(namespaceId, cmd.getOrganizationId(), cmd.getContactToken(), "/" + org.getParentId())) {//继续查询是否是总公司下最后一个onNode节点
                        return DeleteOrganizationContactScopeType.ALL_NOTE.getCode();
                    } else {//是分公司最后一个节点而不是总公司最后一个节
                        return DeleteOrganizationContactScopeType.CHILD_ENTERPRISE.getCode();
                    }
                } else {//不是总公司最后一个节点
                    return "0";
                }
            }
        }
        return "0";
    }

// HEAD
//    public void updatePressTest() {
//        this.organizationProvider.updatePressTest();
//    }
//
//    @Override
//    public void deletePressTest() {
//        this.organizationProvider.deletePressTest();
//=======
    @Override
    public ListOrganizationMemberCommandResponse syncOrganizationMemberStatus() {
        // 1.查同一个域空间下，同一个公司的member的记录，按手机号码分组
        List<EhOrganizationMembers> ehMembers = this.organizationProvider.listOrganizationMembersGroupByToken();
        ListOrganizationMemberCommandResponse res = new ListOrganizationMemberCommandResponse();
        List<OrganizationMemberDTO> members = new ArrayList<>();
        for (EhOrganizationMembers m : ehMembers) {
            List<EhOrganizationMembers> m_token = this.organizationProvider.listOrganizationMemberByToken(m.getContactToken());
            Map map = new HashMap();
            for (EhOrganizationMembers r : m_token) {
                Long enterpriseId = getTopEnterpriserIdOfOrganization(r.getOrganizationId());
                OrganizationMember des = this.organizationProvider.findOrganizationMemberByOrgIdAndToken(r.getContactToken(), enterpriseId);
                if (des != null) {
                    continue;
                }
                if (enterpriseId != null && map.keySet().contains(enterpriseId)) {
                    if (r.getStatus().equals(OrganizationMemberStatus.ACTIVE.getCode()) && !r.getGroupType().equals(OrganizationGroupType.ENTERPRISE.getCode())) {//如果状态不同
                        LOGGER.debug("token :" + r.getContactToken() + "  id :" + r.getId());
                        members.add(ConvertHelper.convert(r, OrganizationMemberDTO.class));
                        continue;
                    }
                } else if (!map.keySet().contains(enterpriseId) && r.getGroupPath().split("/").length == 2) {
                    if (!r.getStatus().equals(OrganizationMemberStatus.ACTIVE.getCode()) && r.getGroupType().equals(OrganizationGroupType.ENTERPRISE.getCode())) {
                        map.put(enterpriseId, r.getStatus()); //标识着一个企业内一个人的唯一状态
                    }
                }
            }
            ;
        }
        res.setMembers(members);
        return res;
    }

    @Override
    public void transferOrganizationPersonels(TransferArchivesEmployeesCommand cmd) {
        if (cmd.getDetailIds() == null || cmd.getDetailIds().size() == 0) {
            LOGGER.error("DetailId is null");
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_PARAMETER_NOT_EXIST, "DetailIds is null");
        }
        Organization org = checkOrganization(cmd.getOrganizationId());

        Integer namespaceId = cmd.getNamespaceId();

        List<Long> departmentIds = cmd.getDepartmentIds();

        List<Long> enterpriseIds = new ArrayList<>();

        List<Long> detailIds = cmd.getDetailIds();

        if(departmentIds != null && departmentIds.size() > 0){

            // 需要添加直属的企业ID集合
            List<Long> direct_under_enterpriseIds = new ArrayList<>();

            // 离开公司的人员集合
            List<OrganizationMember> leaveMembers = new ArrayList<>();

            //总公司和分公司的ID集合
            organizationPersonelsDatasProcess(enterpriseIds, departmentIds, direct_under_enterpriseIds, org);

            detailIds.forEach(detailId -> {
                OrganizationMember enterprise_member = getEnableEnterprisePersonel(org, detailId);
                if (enterprise_member != null) {
                    String token = enterprise_member.getContactToken();
                    //删除记录
                    List<String> groupTypes_full = new ArrayList<>();
                    groupTypes_full.add(OrganizationGroupType.DEPARTMENT.getCode());
                    groupTypes_full.add(OrganizationGroupType.GROUP.getCode());
                    groupTypes_full.add(OrganizationGroupType.ENTERPRISE.getCode());
                    groupTypes_full.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
                    deleteOrganizaitonMemberUnderEnterprise(enterpriseIds, groupTypes_full, leaveMembers, token);
                    //重复添加纪录
                    repeatCreateOrganizationmembers(departmentIds, token, enterpriseIds, enterprise_member);
                    //删除置顶信息
                    archivesProvider.deleteArchivesStickyContactsByDetailId(namespaceId, detailId);
                }
            });

        }

        //1. 删除通用岗位+detailIds所确认的部门岗位条目
//            List<OrganizationJobPositionMap> jobPositionMaps = organizationProvider.listOrganizationJobPositionMapsByJobPositionId(cmd.getCommonJobPositionId());
//            if(jobPositionMaps!=null && jobPositionMaps.size() > 0){
//                List<Long> organizationJobPositionIds = jobPositionMaps.stream().map(r->{
//                    return r.getOrganizationId();
//                }).collect(Collectors.toList());
//                organizationProvider.deleteOrganizationPersonelByJobPositionIdsAndDetailIds(organizationJobPositionIds, cmd.getDetailIds());
//            }
        if (cmd.getJobPositionIds() != null) {
            //1.统一删除所有的部门岗位条目
            //modify by yuanlei
            //下面的这行代码是删除之前原有的部门岗位，这样的话，每次员工在新添加部门岗位时，就会将原来的部门岗位进行删除，不满足一个员工可以存在于多个部门岗位中
            //所以我们再添加新的部门岗位时，不能删除之前原有的部门岗位，所以将下面的这行代码进行注释掉
//            this.organizationProvider.deleteOrganizationMembersByGroupTypeWithDetailIds(namespaceId, cmd.getDetailIds(), OrganizationGroupType.JOB_POSITION.getCode());
            //2. 统一新增岗位
            detailIds.forEach(detailId -> {
                OrganizationMember enterprise_member = getEnableEnterprisePersonel(org, detailId);
                String token = enterprise_member.getContactToken();
                repeatCreateOrganizationmembers(cmd.getJobPositionIds(), token, enterpriseIds, enterprise_member);
            });
        }

        if (cmd.getJobLevelIds() != null) {
            //1.统一删除原有职级
            this.organizationProvider.deleteOrganizationMembersByGroupTypeWithDetailIds(namespaceId, cmd.getDetailIds(), OrganizationGroupType.JOB_LEVEL.getCode());
            //2.统一新增职级
            detailIds.forEach(detailId -> {
                OrganizationMember enterprise_member = getEnableEnterprisePersonel(org, detailId);
                String token = enterprise_member.getContactToken();
                repeatCreateOrganizationmembers(cmd.getJobLevelIds(), token, enterpriseIds, enterprise_member);
            });
        }
    }

    @Override
    public ListOrganizationMemberCommandResponse listOrganizationPersonnelsWithDownStream(ListOrganizationContactCommand cmd) {
        Long topOrgId = getTopOrganizationId(cmd.getOrganizationId());
        Organization topOrg = this.checkOrganization(topOrgId);
        ListOrganizationMemberCommandResponse response = new ListOrganizationMemberCommandResponse();
        Organization org = this.checkOrganization(cmd.getOrganizationId());
        if (null == org)
            return response;
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        //  兼容 admin/org/listOrganizationPersonnels 接口, 给一个固定的类型
        if(cmd.getFilterScopeTypes() == null)
            cmd.setFilterScopeTypes(Collections.singletonList(FilterOrganizationContactScopeType.CURRENT.getCode()));

        /*
        VisibleFlag visibleFlag;
        if (VisibleFlag.fromCode(cmd.getVisibleFlag()) != VisibleFlag.SHOW)
            visibleFlag = VisibleFlag.ALL;

        if (VisibleFlag.ALL == VisibleFlag.fromCode(cmd.getVisibleFlag())) {
            visibleFlag = null;
        } else if (null != VisibleFlag.fromCode(cmd.getVisibleFlag())) {
            visibleFlag = VisibleFlag.fromCode(cmd.getVisibleFlag());
        }
        */

        List<OrganizationMember> organizationMembers = organizationProvider.listOrganizationPersonnelsWithDownStream(cmd.getKeywords(), cmd.getIsSignedup(), locator, pageSize, cmd, cmd.getFilterScopeTypes().get(0), cmd.getTargetTypes());
        if (0 == organizationMembers.size()) {
            return response;
        }


        // Map<String, OrganizationMember> contact_member = organizationMembers.stream().collect(Collectors.toMap(OrganizationMember::getContactToken, Function.identity()));
        // 使用上面的转化方法能够发现环境中的垃圾数据，在下一次下定决心清理垃圾数据时再使用
        Map<String, OrganizationMember> contact_member = new HashMap<>();
        organizationMembers.stream().map(r -> {
            contact_member.put(r.getContactToken(), r);
            return null;
        }).collect(Collectors.toList());
        // 开始聚合
        List<String> groupTypes = new ArrayList<>();
        groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
        groupTypes.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
        groupTypes.add(OrganizationGroupType.GROUP.getCode());
        groupTypes.add(OrganizationGroupType.JOB_POSITION.getCode());
        groupTypes.add(OrganizationGroupType.JOB_LEVEL.getCode());
        List<String> tokens = organizationMembers.stream().map(EhOrganizationMembers::getContactToken).collect(Collectors.toList());
        List<OrganizationMember> origins = organizationProvider.listOrganizationMemberByPath(topOrg.getPath(), groupTypes, tokens);

        Map<String, OrganizationMemberDTO> target_map = new HashMap<>();

        origins.forEach(r -> {
            Organization org_now = organizationProvider.findOrganizationById(r.getOrganizationId());
            if (org_now == null) {
                return;
            }
            //准备放入的dto
            OrganizationMemberDTO dto = ConvertHelper.convert(contact_member.get(r.getContactToken()), OrganizationMemberDTO.class);
            //拿出的dto
            OrganizationMemberDTO orgDto_target = target_map.get(r.getContactToken());
            //本次获得的orgDto
            OrganizationDTO orgDTO_now = ConvertHelper.convert(org_now, OrganizationDTO.class);

            switch (OrganizationGroupType.fromCode(r.getGroupType())) {
                case ENTERPRISE:
                    if (orgDto_target == null) {
                        target_map.put(dto.getContactToken(), dto);
                    }
                    break;
                case DIRECT_UNDER_ENTERPRISE:
                case DEPARTMENT:
                case GROUP:
                    List<OrganizationDTO> departments = new ArrayList<>();
                    //拼装
                    if (orgDto_target == null) {  //首次拼装
                        departments.add(orgDTO_now);
                        dto.setDepartments(departments);
                        target_map.put(dto.getContactToken(), dto);
                    } else {
                        if (orgDto_target.getDepartments() == null) {
                            departments.add(orgDTO_now);
                            orgDto_target.setDepartments(departments);
                        } else {
                            orgDto_target.getDepartments().add(orgDTO_now);
                        }
                    }
                    break;
                case JOB_POSITION:
                    List<OrganizationDTO> jobPositions = new ArrayList<>();
                    //拼装
                    if (orgDto_target == null) {  //首次拼装
                        jobPositions.add(orgDTO_now);
                        dto.setJobPositions(jobPositions);
                        target_map.put(dto.getContactToken(), dto);
                    } else {
                        if (orgDto_target.getJobPositions() == null) {
                            jobPositions.add(orgDTO_now);
                            orgDto_target.setJobPositions(jobPositions);
                        } else {
                            orgDto_target.getJobPositions().add(orgDTO_now);
                        }
                    }
                    break;
                case JOB_LEVEL:
                    List<OrganizationDTO> jobLevels = new ArrayList<>();
                    if (orgDto_target == null) {  //首次拼装
                        jobLevels.add(orgDTO_now);
                        dto.setJobLevels(jobLevels);
                        target_map.put(dto.getContactToken(), dto);
                    }else{
                        if(orgDto_target.getJobLevels() == null){
                            jobLevels.add(orgDTO_now);
                            orgDto_target.setJobLevels(jobLevels);
                        }else{
                            orgDto_target.getJobLevels().add(orgDTO_now);
                        }
                    }
                    break;
                default:
                    break;
            }
        });

        //   the contactToken should be hidden while the visible flag is show.
        List<OrganizationMemberDTO> members;
        if(VisibleFlag.fromCode(cmd.getVisibleFlag()) == VisibleFlag.SHOW)
            members = target_map.values().stream().peek(r -> {
                if(VisibleFlag.fromCode(r.getVisibleFlag()) == VisibleFlag.HIDE)
                    r.setContactToken(null);
            }).collect(Collectors.toList());
        else
            members = new ArrayList<>(target_map.values());
        //  set the order
        members.sort((o1, o2) -> o2.getDetailId().compareTo(o1.getDetailId()));

        response.setNextPageAnchor(locator.getAnchor());
        response.setMembers(members);
        return response;
    }

    @Override
    public Long updateOrganizationMemberInfoByDetailId(Long detailId, String contactToken, String contactName, Byte gender) {
            this.organizationProvider.updateOrganizationMemberByDetailId(detailId, contactToken, contactName, gender);
            return detailId;
    }

    @Override
    public void sortOrganizationsAtSameLevel(SortOrganizationsAtSameLevelCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Organization org = this.organizationProvider.findOrganizationById(cmd.getChildIds().get(0));
        if(org != null){
            // 权限校验
            checkOrganizationPrivilege(org.getParentId(),PrivilegeConstants.CHANGE_DEPARTMENT_ORDER);
        }

        this.coordinationProvider.getNamedLock(CoordinationLocks.ORGANIZATION_ORDER_LOCK.getCode()).enter(() -> {
            if(cmd.getChildIds() != null && cmd.getChildIds().size() > 0){
                List<Long> childIds = cmd.getChildIds();
                for (Long orgId : childIds) {
                    this.organizationProvider.updateOrganizationDefaultOrder(namespaceId, orgId, childIds.indexOf(orgId));
                    LOGGER.debug("sortOrganizationsAtSameLevel" + childIds.indexOf(orgId)+ "namespaceId:" + namespaceId);
                }
            }
            return null;
        });
    }

    @Override
    public FindOrgPersonelCommandResponse findOrgPersonel(FindOrgPersonelCommand cmd) {
        FindOrgPersonelCommandResponse res = new FindOrgPersonelCommandResponse();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        //:todo 人员
        ListOrganizationContactCommand  cmd_1 = new ListOrganizationContactCommand();
        cmd_1.setOrganizationId(cmd.getOrganizationId());
        cmd_1.setKeywords(cmd.getKeywords());
        cmd_1.setFilterScopeTypes(Collections.singletonList("current"));
        cmd_1.setVisibleFlag(VisibleFlag.ALL.getCode());
        ListOrganizationMemberCommandResponse res_1 = listOrganizationPersonnelsWithDownStream(cmd_1);
        res.setMembers(res_1.getMembers());

        //:todo 部门/部门岗位 +2018年10月22日增加公司搜索
        ListOrganizationsByNameCommand cmd_2 = new ListOrganizationsByNameCommand();
        cmd_2.setName(cmd.getKeywords());
        cmd_2.setNamespaceId(namespaceId);
        res.setDepartments(this.organizationProvider.listOrganizationByName(cmd.getKeywords(),  Arrays.asList(OrganizationGroupType.DEPARTMENT.getCode(), OrganizationGroupType.ENTERPRISE.getCode()), null, namespaceId, cmd.getOrganizationId()));

        //:todo 部门岗位
        ListOrganizationsByNameCommand cmd_3 = new ListOrganizationsByNameCommand();
        cmd_3.setName(cmd.getKeywords());
        cmd_3.setNamespaceId(namespaceId);
        res.setDepartJobPoisitions(this.organizationProvider.listOrganizationByName(cmd.getKeywords(), Collections.singletonList(OrganizationGroupType.JOB_POSITION.getCode()), null, namespaceId, cmd.getOrganizationId()));

        //:todo 岗位
        ListOrganizationsByNameCommand cmd_4 = new ListOrganizationsByNameCommand();
        cmd_4.setName(cmd.getKeywords());
        cmd_4.setNamespaceId(namespaceId);
        String owner_type = "EhOrganizations";
        List<OrganizationJobPosition> jps = this.organizationProvider.listOrganizationJobPositions(owner_type,cmd.getOrganizationId(),cmd.getKeywords(),null,null);
        if(jps != null && jps.size() > 0){
            List<OrganizationJobPositionDTO> jobPositionDTOs = jps.stream().map(r->{
               return ConvertHelper.convert(r,OrganizationJobPositionDTO.class);
            }).collect(Collectors.toList());
            res.setJobPositions(jobPositionDTOs);
        }

        return res;
    }

	@Override
    public List<Long> listDetailIdsByEnterpriseId(Long enterpriseId) {
            Organization org = this.organizationProvider.findOrganizationById(enterpriseId);
            if(org != null){
                return this.organizationProvider.queryOrganizationPersonnelDetailIds(new ListingLocator(), enterpriseId, null);
            }
        return null;
    }

    @Override
    public Long getOrganizationNameByNameAndType(String name, String groupType) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(groupType)) {
            LOGGER.error("groupTypes or name is null");
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
                    "groupTypes or name is null");
        }

        Integer namespaceId = UserContext.getCurrentNamespaceId();

        LOGGER.debug("getOrganizationNameByNameAndType namespaceId = " + namespaceId);

        String[] list = name.split("/");
        if(list.length > 0) {
            Organization org = listUnderOrganizations(0, null, namespaceId, list);
            if(org != null){
                System.out.println(org.getName());
                return org.getId();
            }
        }
        return null;
    }

    @Override
    public Boolean deleteChildrenOrganizationAsList(DeleteChildrenOrganizationAsListCommand cmd) {
        if(cmd.getIds() != null && cmd.getTag() != null && (cmd.getTag().equals(OrganizationGroupType.JOB_LEVEL.getCode()) || cmd.getTag().equals(OrganizationGroupType.JOB_POSITION.getCode()))){
        }else {
            LOGGER.error("deleteChildrenOrganizationAsList is not allowed.");
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
                    "deleteChildrenOrganizationAsList is not allowed. ");
        }


        if(cmd.getTag().equals(OrganizationGroupType.JOB_POSITION.getCode())){
            cmd.getIds().forEach(r->{
                checkOrganizationPrivilege(r, PrivilegeConstants.DELETE_JOB_POSITION);
            });
        }

        //：todo 判断该机构（子公司/部门/职级）是否有活动状态的人员
        if(cmd.getTag().equals(OrganizationGroupType.JOB_LEVEL.getCode())){
            //查询需要失效的所有人
            for(Long id : cmd.getIds()) {
                Organization organization = this.checkOrganization(id);
                List<String> groupTypes = new ArrayList<>();
                groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
                groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
                groupTypes.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
                groupTypes.add(OrganizationGroupType.JOB_POSITION.getCode());
                groupTypes.add(OrganizationGroupType.JOB_LEVEL.getCode());
                List<OrganizationMember> if_empty_members = organizationProvider.listOrganizationMemberByPath(organization.getPath(), groupTypes, "");
                //2.如果仍有活动的人员,直接返回false
                if (if_empty_members.size() != 0) {
                    return false;
                }
            }
        }
        //:todo 2部门岗位不需要作判断

        User user = UserContext.current().getUser();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());


        //:todo 如果没有活动的 循环撤销
        for(Long id : cmd.getIds()) {
            Organization organization = this.checkOrganization(id);
            List<Organization> organizations = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "%", null);

            List<Long> organizationIds = organizations.stream().map(r -> r.getId()).collect(Collectors.toList());

            dbProvider.execute((TransactionStatus status) -> {

                //更新organization
                organizationProvider.updateOrganization(organizationIds, OrganizationStatus.DELETED.getCode(), user.getId(), now);

                //删除organizaitonCommunity
                organizationProvider.deleteOrganizationCommunityByOrgIds(organizationIds);

                //把机构入驻的园区关系修改成无效
                organizationProvider.updateOrganizationCommunityRequestByOrgIds(organizationIds, OrganizationCommunityRequestStatus.INACTIVE.getCode(), user.getId(), now);

                //把机构下的所有人员修改成无效
                organizationProvider.updateOrganizationMemberByOrgPaths(organization.getPath() + "%", OrganizationMemberStatus.INACTIVE.getCode(), user.getId(), now);


                //查询需要失效的所有人
                List<OrganizationMember> members = organizationProvider.listOrganizationMemberByPath(organization.getPath(), null, "");

                //把user_organization表中的相应记录更新为失效
                inactiveUserOrganizationWithMembers(members);

                if(members.size() > 0){
                    ExecutorUtil.submit(new Runnable() {
                        @Override
                        public void run() {
                            Timestamp authStart = new Timestamp(DateHelper.currentGMTTime().getTime());
                            LOGGER.debug("authStart: " + sdf.format(authStart));
                            try {
                                members.forEach(member -> {
                                    //解除门禁权限
                                    doorAccessService.deleteAuthWhenLeaveFromOrg(UserContext.getCurrentNamespaceId(), member.getOrganizationId(), member.getTargetId());
                                });
                            } catch (Exception e) {
                                LOGGER.error("deleteAuth task failure.", e);
                            } finally {
                                Timestamp authEnd = new Timestamp(DateHelper.currentGMTTime().getTime());
                                LOGGER.debug("authEnd : " + sdf.format(authEnd));
                            }
                        }
                    });
                }

                return null;
            });
        }

        return true;
    }

    @Override
    public void deleteOrganizationJobPositionsByPositionIdAndDetails(DeleteOrganizationJobPositionsByPositionIdAndDetailsCommand cmd) {
        //权限校验
        cmd.getDetailIds().forEach(detailId ->{
            Long departmentId = getDepartmentByDetailId(detailId);
            checkOrganizationPrivilege(departmentId, PrivilegeConstants.DELETE_PERSON);
        });
//        1. 删除通用岗位+detailIds所确认的部门岗位条目
        List<OrganizationJobPositionMap> jobPositionMaps = organizationProvider.listOrganizationJobPositionMapsByJobPositionId(cmd.getJobPositionId());
        if (jobPositionMaps != null && jobPositionMaps.size() > 0) {
            List<Long> organizationJobPositionIds = jobPositionMaps.stream().map(r -> {
                return r.getOrganizationId();
            }).collect(Collectors.toList());
            organizationProvider.deleteOrganizationPersonelByJobPositionIdsAndDetailIds(organizationJobPositionIds, cmd.getDetailIds());
        }
    }

    /**
     * 根据contactToken退出删除organization path路径下的所有机构
     *
     * @param path
     * @param contactToken
     */
    private void leaveOrganizationMemberByOrganizationPathAndContactToken(String path, String contactToken) {
        List<OrganizationMember> members = listOrganizationMemberByOrganizationPathAndContactToken(path, contactToken);
        leaveOrganizationMembers(members);
        deleteUserOrganizationWithMembers(members);
    }

    /**
     * 根据userId退出删除organization path路径下的所有机构
     *
     * @param path
     * @param userId
     */
    private void leaveOrganizationMemberByOrganizationPathAndUserId(String path, Long userId) {
        List<OrganizationMember> members = listOrganizationMemberByOrganizationPathAndUserId(path, userId);
        leaveOrganizationMembers(members);
        deleteUserOrganizationWithMembers(members);
    }

    /**
     * 根据手机号退出公司，organizationId必须是公司
     *
     * @param operatorUid
     * @param organizationId
     * @param contactToken
     */
    private void leaveOrganizaitonByContactToken(Long operatorUid, Long organizationId, String contactToken) {

        Organization organization = checkEnterpriseParameter(organizationId, operatorUid, "leaveOrganizaitonByContactToken");

        List<OrganizationMember> members = new ArrayList<>();
        List<OrganizationMember> organizationMembers = listOrganizationMemberByOrganizationPathAndContactToken(organization.getPath(), contactToken);

        //如果成员在一套组织架构体系中同事存在于多个公司的情况需要进行筛选删除
        //只要退出和删除当前公司和当前公司的所有机构成员信息，不要删除或退出掉子公司和子公司的所有机构的成员信息 add by sfyan 20170427
        for (OrganizationMember organizationMember : organizationMembers) {
            Organization org = organizationProvider.findOrganizationById(organizationMember.getOrganizationId());
            //所在的机构直属于当前公司或者就是当前公司的成员 需要删除(筛选出属于本公司的记录)
            if (organization.getId().equals(org.getDirectlyEnterpriseId()) || organization.getId().equals(org.getId())) {
                members.add(organizationMember);
            }
        }

        // 退出公司 add by sfyan 20170427
        leaveOrganizationMembers(members);
        deleteUserOrganizationWithMembers(members);

    }

    /**
     * 根据用户id退出公司，organizationId必须是公司
     *
     * @param operatorUid
     * @param organizationId
     * @param userId
     */
    private void leaveOrganizaitonByUserId(Long operatorUid, Long organizationId, Long userId) {
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode());
        leaveOrganizaitonByContactToken(operatorUid, organizationId, userIdentifier.getIdentifierToken());
    }

    private void leaveOrganizationMember(OrganizationMember member) {
        List<OrganizationMember> members = new ArrayList<>();
        members.add(member);
        leaveOrganizationMembers(members);
        deleteUserOrganizationWithMembers(members);
    }

    /**
     * 根据members退出删除机构成员信息
     *
     * @param members
     */
    private void leaveOrganizationMembers(List<OrganizationMember> members) {
        User user = UserContext.current().getUser();

        dbProvider.execute((TransactionStatus status) -> {
            for (OrganizationMember m : members) {
                //跟公司的关系只做状态删除，其他都直接删除
                if (OrganizationGroupType.fromCode(m.getGroupType()) == OrganizationGroupType.ENTERPRISE) {
                    deleteOrganizationMember(m, false);
                } else {
                    deleteOrganizationMember(m, true);
                }
            }
            return null;
        });

        // 删除考勤规则的操作
        if(members != null && members.size() > 0)
            this.uniongroupService.syncUniongroupAfterLeaveTheJob(members.get(0).getDetailId());

        Integer namespaceId = UserContext.getCurrentNamespaceId();

        //执行太慢，开一个线程来做
        ExecutorUtil.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    // 发消息等等操作
                    //设置上下文对象 Added by Jannson
                    UserContext.setCurrentNamespaceId(namespaceId);
                    UserContext.setCurrentUser(user);

                    leaveOrganizationAfterOperation(user.getId(), members);

                    //设置完成之后要清空
                    UserContext.setCurrentNamespaceId(null);
                    UserContext.setCurrentUser(null);
                } catch (Exception e) {
                    LOGGER.error("leaveOrganizationAfterOperation error", e);
                }
            }
        });
    }

    /**
     * 退出公司后需要记录日志、解除门禁、设置默认小区、更新搜索引擎数据、以及发消息
     *
     * @param members
     */
    private void leaveOrganizationAfterOperation(Long operatorUid, List<OrganizationMember> members) {
        for (OrganizationMember m : members) {
            if (OrganizationGroupType.fromCode(m.getGroupType()) == OrganizationGroupType.ENTERPRISE) {

//                //记录删除log
//                OrganizationMemberLog orgLog = new OrganizationMemberLog();
//                orgLog.setOrganizationId(m.getOrganizationId());
//                orgLog.setContactName(m.getContactName());
//                orgLog.setContactToken(m.getContactToken());
//                orgLog.setUserId(m.getTargetId());
//                orgLog.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//                orgLog.setOperationType(OperationType.QUIT.getCode());
//                orgLog.setRequestType(RequestType.ADMIN.getCode());
//                orgLog.setOperatorUid(UserContext.current().getUser().getId());
//                orgLog.setContactDescription(m.getContactDescription());
//                this.organizationProvider.createOrganizationMemberLog(orgLog);

                if(OrganizationMemberGroupType.fromCode(m.getMemberGroup()) == OrganizationMemberGroupType.MANAGER){
                    List<OrganizationMember> managers = organizationProvider.listOrganizationMembersByOrganizationIdAndMemberGroup(m.getOrganizationId(), OrganizationMemberGroupType.MANAGER.getCode(), null);

                    //删除人员是管理员的时候 需要检查公司是否还有管理员，没有的话需要变更公司的flag
                    if(managers.size() == 0){
                        Organization o = organizationProvider.findOrganizationById(m.getOrganizationId());
                        if(null != o) {
                            o.setSetAdminFlag(TrueOrFalseFlag.FALSE.getCode());
                            organizationProvider.updateOrganization(o);
                            organizationSearcher.feedDoc(o);
                        }
                    }
                }

                // 删除模块管理员
                if(OrganizationMemberTargetType.fromCode(m.getTargetType()) == OrganizationMemberTargetType.USER){
                    DeleteServiceModuleAdministratorsCommand dscommand = new DeleteServiceModuleAdministratorsCommand();
                    dscommand.setTargetId(m.getTargetId());
                    dscommand.setTargetType(OwnerType.USER.getCode());
                    dscommand.setOwnerType(OwnerType.ORGANIZATION.getCode());
                    dscommand.setOwnerId(m.getOrganizationId());
                    dscommand.setOrganizationId(m.getOrganizationId());
                    this.rolePrivilegeService.deleteServiceModuleAdministrators(dscommand);
                }

                Integer namespaceId = UserContext.getCurrentNamespaceId();
                if (OrganizationMemberTargetType.fromCode(m.getTargetType()) == OrganizationMemberTargetType.USER) {
                    //Remove door auth, by Janon 2016-12-15
                    doorAccessService.deleteAuthWhenLeaveFromOrg(UserContext.getCurrentNamespaceId(), m.getOrganizationId(), m.getTargetId());
                    LOGGER.debug("deleteUserDoorAccess, m.namespaceId  = {}, UserContext.getCurrentNamespaceId  = {}, UserContext.current.getNamespaceId()  = {}, m.namespaceId  = {}, orgMemberId = {}, useId =  {}",
                            m.getNamespaceId(),UserContext.getCurrentNamespaceId(), UserContext.current().getNamespaceId(), m.getOrganizationId(),  m.getTargetId());

                    // 需要给用户默认一下小区（以机构所在园区为准），否则会在用户退出时没有小区而客户端拿不到场景而卡死
                    // http://devops.lab.everhomes.com/issues/2812  by lqs 20161017
                    setUserDefaultCommunityByOrganization(namespaceId, m.getTargetId(), m.getOrganizationId());
                }

                // 退出公司 发消息
                sendMessageForContactLeave(m);

                userSearcher.feedDoc(m);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Enterprise contact is deleted(active), operatorUid=" + operatorUid + ", contactId=" + m.getTargetId()
                            + ", enterpriseId=" + m.getOrganizationId() + ", status=" + m.getStatus() + ", removeFromDb=" + m.getStatus());
                }
            }
        }
    }

/**
 *
 * @param member
 * @param notSendMsgFlag 不发消息标志,默认是发消息的,除非该值为 true ,即为false 或空表示发消息
 */
    private void joinOrganizationAfterOperation(OrganizationMember member ,boolean notSendMsgFlag) {
        userSearcher.feedDoc(member);

        if (OrganizationMemberTargetType.fromCode(member.getTargetType()) == OrganizationMemberTargetType.USER
        		&& !notSendMsgFlag) {
        	LOGGER.info("sendMessageForContactApproved  and notSendMsgFlag:{}",notSendMsgFlag);
            sendMessageForContactApproved(member);
        }

        //记录新增 log
        OrganizationMemberLog orgLog = ConvertHelper.convert(member, OrganizationMemberLog.class);
        orgLog.setOrganizationId(member.getOrganizationId());
        orgLog.setContactName(member.getContactName());
        orgLog.setContactToken(member.getContactToken());
        orgLog.setUserId(member.getTargetId());
        orgLog.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        orgLog.setOperationType(OperationType.JOIN.getCode());
        orgLog.setRequestType(RequestType.ADMIN.getCode());
        orgLog.setOperatorUid(UserContext.current().getUser().getId());
        orgLog.setContactDescription(member.getContactDescription());
        this.organizationProvider.createOrganizationMemberLog(orgLog);

        if(OrganizationMemberGroupType.fromCode(member.getMemberGroup()) == OrganizationMemberGroupType.MANAGER){
            List<OrganizationMember> managers = organizationProvider.listOrganizationMembersByOrganizationIdAndMemberGroup(member.getOrganizationId(), OrganizationMemberGroupType.MANAGER.getCode(), null);

            //添加人员是管理员的时候 需要检查公司是否还有管理员，有的话需要变更公司的flag
            if(managers.size() > 0){
                Organization o = organizationProvider.findOrganizationById(member.getOrganizationId());
                if(null != o){
                    o.setSetAdminFlag(TrueOrFalseFlag.TRUE.getCode());
                    organizationProvider.updateOrganization(o);
                    organizationSearcher.feedDoc(o);
                }
            }
        }

        //自动加入公司
        try {
            this.doorAccessService.joinCompanyAutoAuth(UserContext.getCurrentNamespaceId(), member.getOrganizationId(), member.getTargetId());
        }catch(Exception e){
            e.getMessage();
        }
    }

    /**
     * 退出企业
     */
    @Override
    public void leaveForEnterpriseContact(LeaveEnterpriseCommand cmd) {

        User user = UserContext.current().getUser();
        long userId = user.getId();
        String tag = "leaveEnterpriseContact";
        // 判断是否为管理员，如果是管理员，不允许退出公司 add by yanlong.liang 20180723
        if (rolePrivilegeService.checkIsSystemOrAppAdmin(cmd.getEnterpriseId(),userId)) {
            LOGGER.error("user is administrator", userId);
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_USER_IS_ADMINISTRATOR,
                    "user is administrator, can't leave");
        }

        Long enterpriseId = cmd.getEnterpriseId();

        //退出公司 add by sfyan20170428
        leaveOrganizaitonByUserId(userId, enterpriseId, userId);
        // 退出公司后，取消公司门禁 add by yanlong.liang 20180723
        this.doorAccessService.deleteAuthWhenLeaveFromOrg(user.getNamespaceId(),enterpriseId,userId);

        //是否删除大堂门禁
        boolean deleteLobbyFlag = true;
        List<OrganizationMember> list = this.organizationProvider.listOrganizationMembersByUId(userId);
        Long communityId = getOrganizationActiveCommunityId(enterpriseId);
        if (!CollectionUtils.isEmpty(list)) {
            List<OrganizationCommunityDTO> dtoList = this.organizationProvider.findOrganizationCommunityByCommunityId(communityId);
            if (!CollectionUtils.isEmpty(dtoList)) {
                for (OrganizationMember member : list) {
                    for (OrganizationCommunityDTO dto : dtoList) {
                        if (member.getOrganizationId().equals(dto.getOrganizationId())) {
                            deleteLobbyFlag = false;
                            break;
                        }
                    }
                }
            }
        }
        if (deleteLobbyFlag) {
            DeleteAuthByOwnerCommand deleteAuthByOwnerCommand = new DeleteAuthByOwnerCommand();
            deleteAuthByOwnerCommand.setNamespaceId(user.getNamespaceId());
            deleteAuthByOwnerCommand.setOwnerId(communityId);
            deleteAuthByOwnerCommand.setOwnerType((byte)0);
            deleteAuthByOwnerCommand.setUserId(userId);
            this.doorAccessService.deleteAuthByOwner(deleteAuthByOwnerCommand);
        }
        
     // 离开企业事件
        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(user.getId());
            context.setNamespaceId(user.getNamespaceId());
            event.setContext(context);

            event.setEntityType(EntityType.USER.getCode());
            event.setEntityId(user.getId());
            event.setEventName(SystemEvent.ACCOUNT_LEAVE_ENTERPRISE.dft());
        });
//        OrganizationMember member = checkEnterpriseContactParameter(cmd.getEnterpriseId(), userId, userId, tag);
//        member.setStatus(OrganizationMemberStatus.INACTIVE.getCode());
//        updateEnterpriseContactStatus(userId, member);
//
//
//    	//记录退出log
//    	OrganizationMemberLog orgLog = ConvertHelper.convert(cmd, OrganizationMemberLog.class);
//    	orgLog.setOrganizationId(member.getOrganizationId());
//    	orgLog.setContactName(member.getContactName());
//    	orgLog.setContactToken(member.getContactToken());
//    	orgLog.setUserId(member.getTargetId());
//    	orgLog.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//    	orgLog.setOperationType(OperationType.QUIT.getCode());
//    	orgLog.setRequestType(RequestType.USER.getCode());
//    	orgLog.setOperatorUid(UserContext.current().getUser().getId());
//    	this.organizationProvider.createOrganizationMemberLog(orgLog);
//		//退出机构，要把在机构相关的角色权限删除掉 by sfyan 20161018
//		if(OrganizationMemberTargetType.fromCode(member.getTargetType()) == OrganizationMemberTargetType.USER){
//			List<RoleAssignment> userRoles = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), member.getOrganizationId(), EntityType.USER.getCode(), member.getTargetId());
//			for (RoleAssignment roleAssignment : userRoles) {
//				aclProvider.deleteRoleAssignment(roleAssignment.getId());
//			}
//		}
//        sendMessageForContactLeave(member);
//
//        //Remove door auth, by Janon 2016-12-15
//        doorAccessService.deleteAuthWhenLeaveFromOrg(UserContext.getCurrentNamespaceId(), enterpriseId, userId);
//
//        // 需要给用户默认一下小区（以机构所在园区为准），否则会在用户退出时没有小区而客户端拿不到场景而卡死
//        // http://devops.lab.everhomes.com/issues/2812  by lqs 20161017
//        Integer namespaceId = UserContext.getCurrentNamespaceId();
//        setUserDefaultCommunityByOrganization(namespaceId, userId, enterpriseId);
    }

    /*@Override
    public ListOrganizationMemberCommandResponse listOrganizationPersonnels(ListOrganizationContactCommand cmd, boolean pinyinFlag) {
        Long startTime1 = System.currentTimeMillis();
        ListOrganizationMemberCommandResponse response = new ListOrganizationMemberCommandResponse();
        Organization org = this.checkOrganization(cmd.getOrganizationId());
        if (null == org)
            return response;

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        String keywords = cmd.getKeywords();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        Long startTime2 = System.currentTimeMillis();

        //组装参数
        Organization orgCommoand = new Organization();
        orgCommoand.setId(org.getId());
        orgCommoand.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
        VisibleFlag visibleFlag = VisibleFlag.SHOW;
        if (VisibleFlag.ALL == VisibleFlag.fromCode(cmd.getVisibleFlag())) {
            visibleFlag = null;
        } else if (null != VisibleFlag.fromCode(cmd.getVisibleFlag())) {
            visibleFlag = VisibleFlag.fromCode(cmd.getVisibleFlag());
        }

        // 换个provider
//        List<OrganizationMember> organizationMembers = organizationProvider.listOrganizationPersonnels(keywords, orgCommoand, cmd.getIsSignedup(), visibleFlag, locator, pageSize, cmd);
        List<OrganizationMember> organizationMembers = organizationProvider.listOrganizationMembers(locator, pageSize, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {

                query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
                List<String> groupTypes = new ArrayList<>();
                if (null != cmd.getFilterScopeTypes() && cmd.getFilterScopeTypes().size() > 0) {
                    Condition cond = null;
                    if (cmd.getFilterScopeTypes().contains(FilterOrganizationContactScopeType.CURRENT.getCode())) {
                        *//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**当前节点是企业**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//*
                        if (org.getGroupType().equals(OrganizationGroupType.ENTERPRISE.getCode())) {
                            //寻找隶属企业的直属隐藏部门
                            Organization underDirectOrg = organizationProvider.findUnderOrganizationByParentOrgId(org.getId());
                            if (underDirectOrg == null) {//没有添加过直属人员
                                cond = (Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.lt(0L));//确保查询不到
                            } else {
                                cond = Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(underDirectOrg.getId());
                                cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.eq(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode()));
                            }

                        } else {*//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**当前节点不是企业**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//*
                            cond = Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(org.getId());
                        }
                    }
                    if (cmd.getFilterScopeTypes().contains(FilterOrganizationContactScopeType.CHILD_DEPARTMENT.getCode())) {
                        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
                    }

                    if (cmd.getFilterScopeTypes().contains(FilterOrganizationContactScopeType.CHILD_ENTERPRISE.getCode())) {
                        groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
                    }

                    if (cmd.getFilterScopeTypes().contains(FilterOrganizationContactScopeType.CHILD_GROUP.getCode())) {
                        groupTypes.add(OrganizationGroupType.GROUP.getCode());
                    }

                    //多条件查询中选择了本结点且选择了其他group条件
                    if (groupTypes.size() > 0 && null != cond) {
                        cond = Tables.EH_ORGANIZATION_MEMBERS.GROUP_PATH.like(org.getPath() + "%");
                        cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.in(groupTypes));
                    } else if (groupTypes.size() > 0) {
                        cond = Tables.EH_ORGANIZATION_MEMBERS.GROUP_PATH.like(org.getPath() + "/%");
                        cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.in(groupTypes));
                    }
                    if (null != cond)
                        query.addConditions(cond);
                } else {
                    groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
                    groupTypes.add(OrganizationGroupType.GROUP.getCode());
                    groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
                    query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.in(groupTypes));
                    query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.GROUP_PATH.like(org.getPath() + "%"));
                }

                if (null != cmd.getTargetTypes() && cmd.getTargetTypes().size() > 0) {
                    query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.in(cmd.getTargetTypes()));
                }

                if (null != cmd.getIsSignedup() && cmd.getIsSignedup() == ContactSignUpStatus.SIGNEDUP.getCode()) {
                    query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.ne(0L));
                    query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode()));
                }

                if (!StringUtils.isEmpty(keywords)) {
                    query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq(keywords).or(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_NAME.like("%" + keywords + "%")));
                }

                query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc());
                query.addGroupBy(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN);
                return query;
            }
        });

        Long endTime2 = System.currentTimeMillis();
        if (pinyinFlag) {
            organizationMembers = convertPinyin(organizationMembers);
        }

        if (0 == organizationMembers.size()) {
            return response;
        }

        response.setNextPageAnchor(locator.getAnchor());

        Long startTime3 = System.currentTimeMillis();
        response.setMembers(this.convertDTO(organizationMembers, org));
        Long endTime = System.currentTimeMillis();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Track: listOrganizationPersonnels: get organization member elapse:{}, convert elapse:{}, total elapse:{}", endTime2 - startTime2, endTime - startTime3, endTime - startTime1);
        }
        return response;
    }*/

/*    @Override
    public List<OrganizationMemberDTO> convertOrganizationMemberDTO(List<OrganizationMember> organizationMembers, Organization org) {
        return this.convertDTO(organizationMembers, org);
    }*/

    @Override
    public ListOrganizationMemberCommandResponse listOrganizationPersonnelsByRoleIds(ListOrganizationPersonnelByRoleIdsCommand cmd) {
        ListOrganizationContactCommand command = new ListOrganizationContactCommand();
        command.setOrganizationId(cmd.getOrganizationId());
        command.setPageSize(100000);
        command.setKeywords(cmd.getKeywords());
        ListOrganizationMemberCommandResponse response = this.listOrganizationPersonnelsWithDownStream(command);

        List<OrganizationMemberDTO> roleMembers = new ArrayList<OrganizationMemberDTO>();

        List<OrganizationMemberDTO> members = response.getMembers();

        if (null != members && 0 != members.size()) {
            for (OrganizationMemberDTO organizationMemberDTO : members) {
                List<RoleDTO> RoleDTOs = organizationMemberDTO.getRoles();
                if (null == RoleDTOs) {
                    continue;
                }
                User user = this.userProvider.findUserById(organizationMemberDTO.getTargetId());
                if (user != null)
                    organizationMemberDTO.setNickName(user.getNickName());
                List<RoleDTO> dtos = new ArrayList<RoleDTO>();
                for (RoleDTO dto : RoleDTOs) {
                    if (null != dto && cmd.getRoleIds().contains(dto.getId())) {
                        dtos.add(dto);
                        organizationMemberDTO.setRoles(dtos);
                        roleMembers.add(organizationMemberDTO);
                        break;
                    }
                }
            }
        }

        response.setMembers(roleMembers);

        return response;

    }

    @Override
    public ListOrganizationMemberCommandResponse listOrgAuthPersonnels(ListOrganizationContactCommand cmd) {

        if (null == cmd.getNamespaceId()) {
            cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        }

        ListOrganizationMemberCommandResponse response = new ListOrganizationMemberCommandResponse();
        Organization org = this.checkOrganization(cmd.getOrganizationId());
        if (null == org)
            return response;

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        Organization orgCommoand = new Organization();
        orgCommoand.setId(org.getId());
        if (cmd.getStatus() == null)
            orgCommoand.setStatus(OrganizationMemberStatus.WAITING_FOR_APPROVAL.getCode());
        else
            orgCommoand.setStatus(cmd.getStatus());
        orgCommoand.setGroupType(org.getGroupType());

        // 人员退出公司页需要在内部管理的员工认证的已同意标签下显示 add by xq.tian 2017/07/12
        List<OrganizationMember> organizationMembers = null;
        if (OrganizationMemberStatus.fromCode(cmd.getStatus()) == OrganizationMemberStatus.ACTIVE) {
            List<Long> orgIds = Collections.singletonList(cmd.getOrganizationId());
            List<OrganizationMemberLog> memberLogList = organizationProvider.listOrganizationMemberLogs(orgIds, cmd.getKeywords(), cmd.getIdentifierToken(),null, locator, pageSize);
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
            }
        } else {
            organizationMembers = this.organizationProvider.listOrganizationPersonnels(cmd.getNamespaceId(), cmd.getIdentifierToken(), cmd.getKeywords(),
                    orgCommoand, cmd.getIsSignedup(), null, locator, pageSize);
        }

        if (organizationMembers == null || 0 == organizationMembers.size()) {
            return response;
        }

        response.setNextPageAnchor(locator.getAnchor());

        response.setMembers(organizationMembers.stream().map((c) -> {
            OrganizationMemberDTO dto = ConvertHelper.convert(c, OrganizationMemberDTO.class);
            if (c.getOperatorUid() != null && c.getOperatorUid() > 0) {
                User operator = userProvider.findUserById(c.getOperatorUid());
                UserIdentifier operatorIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(c.getOperatorUid(), IdentifierType.MOBILE.getCode());
                dto.setOperatorName(operator != null ? operator.getNickName() : "");
                dto.setOperatorPhone(operatorIdentifier != null ? operatorIdentifier.getIdentifierToken() : "");
                //兼容旧数据
                if (OperateType.IMPORT.getCode().equals(c.getSourceType())) {
                    dto.setOperateType(OperateType.IMPORT.getCode());
                }else {
                    dto.setOperateType(OperateType.MANUAL.getCode());
                }
            } else if (OrganizationMemberStatus.fromCode(cmd.getStatus()) == OrganizationMemberStatus.ACTIVE) {
                dto.setOperatorName("--");
                dto.setOperateType(OperateType.EMAIL.getCode());
            }
            if (OrganizationMemberTargetType.fromCode(c.getTargetType()) == OrganizationMemberTargetType.USER) {
                if (c.getTargetId() != null && c.getTargetId() != 0) {
                    User user = userProvider.findUserById(c.getTargetId());
                    if (user != null) {
                        dto.setNickName(user.getNickName());
                    }
                }
            }
            if (dto.getOrganizationName() == null || dto.getOrganizationName().isEmpty()) {
                Organization organization = organizationProvider.findOrganizationById(dto.getOrganizationId());
                if (organization != null) {
                    dto.setOrganizationName(organization.getName());
                }
            }
            return dto;
        }).collect(Collectors.toList()));

        Collections.sort(response.getMembers(), new Comparator<OrganizationMemberDTO>() {
            @Override
            public int compare(OrganizationMemberDTO o1, OrganizationMemberDTO o2) {
                if (o1.getApproveTime() == null || o2.getApproveTime() == null) {
                    return -1;
                }
                return o2.getApproveTime().compareTo(o1.getApproveTime());
            }
        });
        return response;
    }

    @Override
    public ListOrganizationMemberCommandResponse listParentOrganizationPersonnels(
            ListOrganizationMemberCommand cmd) {
        ListOrganizationMemberCommandResponse response = new ListOrganizationMemberCommandResponse();

        Organization org = this.checkOrganization(cmd.getOrganizationId());
        List<String> groupTypes = cmd.getGroupTypes();
        if (null == groupTypes || 0 == groupTypes.size()) {
            LOGGER.error("groupTypes is null, groupTypes = {}", groupTypes);
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
                    "groupTypes is null.");
        }

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

        if (OrganizationGroupType.fromCode(org.getGroupType()) == OrganizationGroupType.ENTERPRISE) {
            groupTypes.remove(OrganizationGroupType.DEPARTMENT.getCode());
        }

        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<OrganizationMember> organizationMembers = this.organizationProvider.listParentOrganizationMembers(org.getPath(), groupTypes, locator, pageSize);
        response.setNextPageAnchor(locator.getAnchor());

        response.setMembers(this.convertDTO(organizationMembers, org));

        return response;
    }

    @Override
    public VerifyPersonnelByPhoneCommandResponse verifyPersonnelByPhone(VerifyPersonnelByPhoneCommand cmd) {

        VerifyPersonnelByPhoneCommandResponse res = new VerifyPersonnelByPhoneCommandResponse();

        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());

        OrganizationMember member = organizationProvider.findOrganizationPersonnelByPhone(cmd.getEnterpriseId(), cmd.getPhone(),cmd.getNamespaceId());

        if (member != null) {
            if (member.getStatus().equals(OrganizationMemberStatus.ACTIVE.getCode()))
                throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_PHONE_ALREADY_EXIST,
                        "phone number already exists.");

            if (member.getStatus().equals(OrganizationMemberStatus.WAITING_FOR_APPROVAL.getCode()))
                throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_PHONE_ALREADY_APPLY,
                        "Mobile phone number has been applied to join the company, please approve.");
        }

        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getPhone());

        if (null != userIdentifier) {
            User user = userProvider.findUserById(userIdentifier.getOwnerUid());
            OrganizationMemberDTO dto = new OrganizationMemberDTO();
            dto.setTargetId(user.getId());
            dto.setContactToken(userIdentifier.getIdentifierToken());
            dto.setContactName(user.getNickName());
            dto.setTargetType(OrganizationMemberTargetType.USER.getCode());
            res.setDto(dto);
        }
        return res;
    }

    @Override
    public boolean verifyPersonnelByWorkEmail(Long orgId, Long detailId, String workEmail) {
        OrganizationMemberDetails employee = organizationProvider.findOrganizationPersonnelByWorkEmail(orgId, workEmail);
        if (employee == null)
            return true;
        return employee.getId().equals(detailId);
    }

    @Override
    public boolean verifyPersonnelByWorkEmail(Long orgId, String contactToken, String workEmail) {
        OrganizationMemberDetails employee = organizationProvider.findOrganizationPersonnelByWorkEmail(orgId, workEmail);
        if (employee == null)
            return true;
        return employee.getContactToken().equals(contactToken);
    }

    @Override
    public boolean verifyPersonnelByAccount(Long detailId, String account) {
        OrganizationMemberDetails employee = organizationProvider.findOrganizationPersonnelByAccount(account);
        if (employee == null)
            return true;
        return employee.getId().equals(detailId);
    }

    @Override
    public boolean verifyPersonnelByAccount(String contactToken, String account) {
        OrganizationMemberDetails employee = organizationProvider.findOrganizationPersonnelByAccount(account);
        if (employee == null)
            return true;
        return employee.getContactToken().equals(contactToken);
    }

    @Override
    public void updateOrganizationPersonnel(UpdateOrganizationMemberCommand cmd) {
        OrganizationMember member = organizationProvider.findOrganizationMemberById(cmd.getId());
        member.setContactName(cmd.getContactName());
        if (null != cmd.getGroupId()) {
            Organization group = checkOrganization(cmd.getGroupId());
            member.setGroupPath(group.getPath());
            member.setGroupId(cmd.getGroupId());
        }

        if (null != cmd.getGender()) member.setGender(cmd.getGender());
        if (null != cmd.getEmployeeNo()) member.setEmployeeNo(cmd.getEmployeeNo());

        organizationProvider.updateOrganizationMember(member);
    }

    @Override
    public OrganizationMember createOrganizationPersonnel(
            CreateOrganizationMemberCommand cmd) {
        User user = UserContext.current().getUser();


        Organization org = checkOrganization(cmd.getOrganizationId());

        //检查org是否为总公司，如果不是，则不允许创建管理员
        if (org != null && org.getParentId() != 0l) {
            LOGGER.error("org is not allowed. organizationId = {}", cmd.getOrganizationId());
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
                    "org is not allowed. ");
        }

        Integer namespaceId = UserContext.getCurrentNamespaceId();

        OrganizationMember organizationMember = ConvertHelper.convert(cmd, OrganizationMember.class);
        organizationMember.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
        organizationMember.setContactType(IdentifierType.MOBILE.getCode());
        organizationMember.setCreatorUid(user.getId());
        organizationMember.setNamespaceId(namespaceId);
        organizationMember.setGroupId(0l);
        organizationMember.setGroupType(org.getGroupType());
        organizationMember.setGroupPath(org.getPath());
        organizationMember.setGender(cmd.getGender());
        if (StringUtils.isEmpty(organizationMember.getTargetId())) {
            organizationMember.setTargetType(OrganizationMemberTargetType.UNTRACK.getCode());
            organizationMember.setTargetId(0l);
        }

//        dbProvider.execute((TransactionStatus status) -> {
//
//            Long organizationId = cmd.getOrganizationId();
//            Long groupId = cmd.getGroupId();
//
//            if (OrganizationGroupType.fromCode(org.getGroupType()) == OrganizationGroupType.ENTERPRISE) {
//                OrganizationMember desOrgMember = this.organizationProvider.findOrganizationMemberByOrgIdAndToken(cmd.getContactToken(), organizationId);
//                if (null == groupId || 0 == groupId) {
//                    if (null != desOrgMember) {
//                        LOGGER.error("phone number already exists. organizationId = {}", organizationId);
//                        throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
//                                "phone number already exists.");
//                    }
//                    organizationMember.setOrganizationId(organizationId);
//                    organizationProvider.createOrganizationMember(organizationMember);
//                    return null;
//                }
//
//                if (null == desOrgMember) {
//                    organizationMember.setOrganizationId(organizationId);
//                    organizationProvider.createOrganizationMember(organizationMember);
//                }
//            } else if (OrganizationGroupType.fromCode(org.getGroupType()) == OrganizationGroupType.DEPARTMENT) {
//                groupId = cmd.getOrganizationId();
//                organizationId = org.getDirectlyEnterpriseId();
//                OrganizationMember desOrgMember = this.organizationProvider.findOrganizationMemberByOrgIdAndToken(cmd.getContactToken(), organizationId);
//                if (null == desOrgMember) {
//                    organizationMember.setOrganizationId(organizationId);
//                    organizationProvider.createOrganizationMember(organizationMember);
//                }
//            } else {
//                groupId = cmd.getOrganizationId();
//            }
//
//            Organization group = checkOrganization(groupId);
//            organizationMember.setGroupPath(group.getPath());
//            OrganizationMember groupMember = this.organizationProvider.findOrganizationMemberByOrgIdAndToken(cmd.getContactToken(), groupId);
//            if (null != groupMember) {
//                LOGGER.error("phone number already exists. organizationId = {}", groupId);
//                throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
//                        "phone number already exists.");
//            }
//            organizationMember.setOrganizationId(groupId);
//            organizationProvider.createOrganizationMember(organizationMember);
//
//            return null;
//        });

        dbProvider.execute((TransactionStatus status) -> {

            Long organizationId = cmd.getOrganizationId();

            if (OrganizationGroupType.fromCode(org.getGroupType()) == OrganizationGroupType.ENTERPRISE) {
                /**创建企业级的member/detail/user_organiztion记录**/
                createOrganiztionMemberWithDetailAndUserOrganization(organizationMember, organizationId);
            } else {//如果不是企业，则报错
                LOGGER.error("organization is not a enterprise. organizationId = {}", organizationId);
                throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
                        "organization is not a enterprise.");
            }
            return null;
        });


        //记录新增 log
        OrganizationMemberLog orgLog = ConvertHelper.convert(cmd, OrganizationMemberLog.class);
        orgLog.setOrganizationId(organizationMember.getOrganizationId());
        orgLog.setContactName(organizationMember.getContactName());
        orgLog.setContactToken(organizationMember.getContactToken());
        orgLog.setUserId(organizationMember.getTargetId());
        orgLog.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        orgLog.setOperationType(OperationType.JOIN.getCode());
        orgLog.setRequestType(RequestType.USER.getCode());
        orgLog.setOperatorUid(UserContext.current().getUser().getId());
        orgLog.setContactDescription(organizationMember.getContactDescription());
        this.organizationProvider.createOrganizationMemberLog(orgLog);

        if (OrganizationMemberTargetType.fromCode(organizationMember.getTargetType()) == OrganizationMemberTargetType.USER) {
            userSearcher.feedDoc(organizationMember);
        }
        sendMessageForContactApproved(organizationMember);
        return organizationMember;
    }


    @Override
    public void updatePersonnelsToDepartment(UpdatePersonnelsToDepartment cmd) {
        Organization org = checkOrganization(cmd.getGroupId());
        if (!org.getGroupType().equals(OrganizationGroupType.DEPARTMENT.getCode())) {
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
                    "groupId Invalid parameter.");
        }
        List<Long> ids = cmd.getIds();
        if (null == ids || 0 == ids.size()) {
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
                    "ids Invalid parameter.");
        }
        organizationProvider.updateOrganizationMemberByIds(ids, org);
    }

    @Override
    public void addPersonnelsToGroup(AddPersonnelsToGroup cmd) {
        User user = UserContext.current().getUser();

        List<Long> ids = cmd.getIds();

        Organization org = this.checkOrganization(cmd.getGroupId());

        Timestamp time = new Timestamp(DateHelper.currentGMTTime().getTime());
        for (Long id : ids) {
            OrganizationMember member = organizationProvider.findOrganizationMemberById(id);

            if (null == member) {
                continue;
            }

            OrganizationMember m = organizationProvider.findOrganizationMemberByOrgIdAndToken(member.getContactToken(), cmd.getGroupId());
            if (null == m) {
                OrganizationMember orgMember = new OrganizationMember();
                member.setId(null);
                orgMember.setOrganizationId(cmd.getGroupId());
                orgMember.setTargetType(member.getTargetType());
                orgMember.setContactName(member.getContactName());
                orgMember.setContactToken(member.getContactToken());
                orgMember.setContactType(ContactType.MOBILE.getCode());
                orgMember.setTargetId(member.getTargetId());
                orgMember.setCreatorUid(user.getId());
                orgMember.setCreateTime(time);
                orgMember.setUpdateTime(time);
                orgMember.setGroupId(0l);
                orgMember.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
                orgMember.setGroupType(org.getGroupType());
                orgMember.setGroupPath(org.getPath());
                organizationProvider.createOrganizationMember(orgMember);
            }
        }
    }

    @Override
    public ListOrganizationMemberCommandResponse listPersonnelNotJoinGroups(
            ListPersonnelNotJoinGroupCommand cmd) {

        ListOrganizationMemberCommandResponse res = new ListOrganizationMemberCommandResponse();

        List<OrganizationMember> members = organizationProvider.listOrganizationMembersByOrgId(cmd.getOrganizationId());

        List<String> phones = new ArrayList<String>();

        if (null != members && 0 != members.size()) {
            for (OrganizationMember member : members) {
                phones.add(member.getContactToken());
            }
        }

        members = organizationProvider.listOrganizationMembersByPhones(phones, cmd.getDepartmentId());

        res.setMembers(members.stream().map(r -> {
            return ConvertHelper.convert(r, OrganizationMemberDTO.class);
        }).collect(Collectors.toList()));

        return res;
    }

    /**
     * 查出此公司所管理的项目
     */
    @Override
    public List<CommunityDTO> listAllChildrenOrganizationCoummunities(Long organizationId) {

        Integer namespaceId = UserContext.getCurrentNamespaceId();

        Organization organization = this.checkOrganization(organizationId);

        List<String> groupTypes = new ArrayList<String>();
        groupTypes.add(OrganizationGroupType.GROUP.getCode());
        groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());

        List<Organization> orgs = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "/%", groupTypes);
        orgs.add(organization);
        if (LOGGER.isDebugEnabled())
            LOGGER.info("orgs:" + orgs);
        List<CommunityDTO> dtos = new ArrayList<CommunityDTO>();

        for (Organization org : orgs) {
            //查出此公司的所有部门所管理的项目总和
            List<OrganizationCommunity> organizationCommunitys = organizationProvider.listOrganizationCommunities(org.getId());
            for (OrganizationCommunity organizationCommunity : organizationCommunitys) {
                Community community = communityProvider.findCommunityById(organizationCommunity.getCommunityId());
                if (null == community) {
                    continue;
                }

                if (LOGGER.isDebugEnabled())
                    LOGGER.info("community:" + community);

                if (community.getNamespaceId().equals(namespaceId)) {
                    dtos.add(ConvertHelper.convert(community, CommunityDTO.class));
                }
            }
        }

        return dtos;
    }

    @Override
    public ListCommunityByNamespaceCommandResponse listCommunityByOrganizationId(
            ListCommunitiesByOrganizationIdCommand cmd) {
        ListCommunityByNamespaceCommandResponse res = new ListCommunityByNamespaceCommandResponse();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        Organization organization = this.checkOrganization(cmd.getOrganizationId());
        List<CommunityDTO> dtos = new ArrayList<CommunityDTO>();
        List<OrganizationCommunity> organizationCommunitys = organizationProvider.listOrganizationCommunities(organization.getId());
        for (OrganizationCommunity organizationCommunity : organizationCommunitys) {
            Community community = communityProvider.findCommunityById(organizationCommunity.getCommunityId());
            if (null == community) {
                continue;
            }
            if (community.getNamespaceId().equals(namespaceId)) {
                dtos.add(ConvertHelper.convert(community, CommunityDTO.class));
            }
        }
        res.setCommunities(dtos);

        return res;
    }

    @Override
    public OrganizationMember createOrganizationAccount(CreateOrganizationAccountCommand cmd, Long roleId) {
        return createOrganizationAccount(cmd, roleId, null);
    }

    @Override
    public OrganizationMember createOrganizationAccount(CreateOrganizationAccountCommand cmd, Long roleId, Integer exNamespaceId) {

        User user = UserContext.current().getUser();
        if (null == cmd.getAccountPhone()) {
            LOGGER.error("contactToken can not be empty.");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER, "contactToken can not be empty.");
        }

        if (null == cmd.getAccountName()) {
            LOGGER.error("contactName can not be empty.");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER, "contactName can not be empty.");
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId(exNamespaceId);

        Organization org = checkOrganization(cmd.getOrganizationId());

        OrganizationMember member = organizationProvider.findOrganizationPersonnelByPhone(cmd.getOrganizationId(), cmd.getAccountPhone(),namespaceId);
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getAccountPhone());
        if (null == member) {
            member = new OrganizationMember();
            member.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
            member.setContactType(IdentifierType.MOBILE.getCode());
            member.setCreatorUid(user.getId());
            member.setNamespaceId(namespaceId);
            member.setGroupId(0l);
            member.setGroupType(org.getGroupType());
            member.setGroupPath(org.getPath());
            member.setGender(UserGender.UNDISCLOSURED.getCode());
            member.setContactName(cmd.getAccountName());
            member.setContactToken(cmd.getAccountPhone());
            if (null != userIdentifier) {
                member.setTargetType(OrganizationMemberTargetType.USER.getCode());
                member.setTargetId(userIdentifier.getOwnerUid());
            } else {
                member.setTargetType(OrganizationMemberTargetType.UNTRACK.getCode());
                member.setTargetId(0L);
            }

//  HEAD
//        return this.dbProvider.execute((TransactionStatus status) -> {
////            OrganizationMember m = member;
//
////            if (null == m) {
////                CreateOrganizationMemberCommand memberCmd = new CreateOrganizationMemberCommand();
////                memberCmd.setContactName(cmd.getAccountName());
////                memberCmd.setContactToken(cmd.getAccountPhone());
////                memberCmd.setOrganizationId(cmd.getOrganizationId());
////                memberCmd.setGender(UserGender.UNDISCLOSURED.getCode());
////                m = this.createOrganizationPersonnel(memberCmd);
////            } else {
////				List<OrganizationMember> members = listOrganizationMemberByOrganizationPathAndContactToken(org.getPath(), cmd.getAccountPhone());
////				for (OrganizationMember organizationMember: members) {
////					organizationMember.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
////					organizationMember.setContactName(cmd.getAccountName());
////					organizationProvider.updateOrganizationMember(organizationMember);
////                }
////            }
//
//
//            //创建用户
//            UserIdentifier userIdentifier = null;
//            userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getAccountPhone());
//
//            if (null == userIdentifier) {
//                User newuser = new User();
//                newuser.setStatus(UserStatus.ACTIVE.getCode());
//                newuser.setNamespaceId(namespaceId);
//                newuser.setNickName(cmd.getAccountName());
//                newuser.setGender(UserGender.UNDISCLOSURED.getCode());
//                String salt = EncryptionUtils.createRandomSalt();
//                newuser.setSalt(salt);
//                try {
//                    newuser.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", salt)));
//                } catch (Exception e) {
//                    LOGGER.error("encode password failed");
//                    throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PASSWORD, "Unable to create password hash");
// 
            createOrganiztionMemberWithDetailAndUserOrganization(member, org.getId());
        } else if (OrganizationMemberStatus.ACTIVE != OrganizationMemberStatus.fromCode(member.getStatus())) {
            //把正在申请加入公司状态的 记录改成正常
            member.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
            organizationProvider.updateOrganizationMember(member);
        }

        //刷新企业通讯录
        if (null != userIdentifier)
            processUserForMemberWithoutMessage(userIdentifier);

        if (null != cmd.getAssignmentId())
            aclProvider.deleteRoleAssignment(cmd.getAssignmentId());

        if (null != roleId) {
            SetAclRoleAssignmentCommand roleCmd = new SetAclRoleAssignmentCommand();
            roleCmd.setRoleId(roleId);
            roleCmd.setTargetId(userIdentifier.getOwnerUid());
            roleCmd.setOrganizationId(cmd.getOrganizationId());
            this.setAclRoleAssignmentRole(roleCmd, EntityType.USER);
        }

        OrganizationDetail detail = organizationProvider.findOrganizationDetailByOrganizationId(cmd.getOrganizationId());
        if (null == detail) {
            LOGGER.error("organization detail is null, organizationId = {}", cmd.getOrganizationId());
        } else {
            // 如果是金蝶过来的数据，则不更新此两列
            if (detail.getNamespaceOrganizationType() == null || !detail.getNamespaceOrganizationType().equals(NamespaceOrganizationType.JINDIE.getCode())) {
                detail.setContactor(cmd.getAccountName());
                detail.setContact(cmd.getAccountPhone());
                organizationProvider.updateOrganizationDetail(detail);
            }
        }


        return member;
    }


    /**
     * 根据组织id、用户名、手机号
     * 创建机构账号，包括注册、把用户添加到公司
     * @param organizationId
     * @param contactName
     * @param contactToken
     * @return
     */
    @Override
    public OrganizationMember createOrganiztionMemberWithDetailAndUserOrganizationAdmin(Long organizationId, String contactName
    								,
                                                                                        String contactToken ,boolean notSendMsgFlag) {

        if (null == contactToken) {
            LOGGER.error("contactToken can not be empty.");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER, "contactToken can not be empty.");
        }

        if (null == contactName) {
            LOGGER.error("contactName can not be empty.");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER, "contactName can not be empty.");
        }

        //获取登录App的用户信息
        User user = UserContext.current().getUser();
        //根据组织id来查询eh_organizations表信息
        Organization org = checkOrganization(organizationId);
        //获取当前App所在的域空间
        Integer namespaceId = UserContext.getCurrentNamespaceId(org.getNamespaceId());
        //根据组织id和手机号来查询eh_organization_members表中有效的用户信息
        OrganizationMember member = organizationProvider.findOrganizationPersonnelByPhone(organizationId, contactToken,org.getNamespaceId());
        //根据域空间id和注册的手机号来查询对应的注册信息
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, contactToken);
        //声明一个boolean类型的变量
        boolean sendMsgFlag = false;

        if (null == member) {
            //说明eh_organization_members表中没有对应的有效的用户信息，那么久新建一个对象，并且将信息封装在对象中
            member = new OrganizationMember();
            member.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
            member.setContactType(IdentifierType.MOBILE.getCode());
            member.setCreatorUid(user.getId());
            member.setNamespaceId(namespaceId);
            member.setGroupId(0l);
            member.setGroupType(org.getGroupType());
            member.setGroupPath(org.getPath());
            member.setGender(UserGender.UNDISCLOSURED.getCode());
            member.setContactName(contactName);
            member.setContactToken(contactToken);
            member.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
            member.setOrganizationId(organizationId);
            if (null != userIdentifier) {
                //说明该用户之前已经注册过，eh_user_identifiers表中是存在注册信息的，但是eh_organization_members表中是不存在人员信息的，
                //那么我们就需要将members表中的target_type字段更改为USER,表示已经注册
                member.setTargetType(OrganizationMemberTargetType.USER.getCode());
                member.setTargetId(userIdentifier.getOwnerUid());
                sendMsgFlag = true;
            } else {
                //说明用户之前是没有进行注册的，也没有加入公司的OA体系，那么我们就设置target_type为UNTRACK表示未注册
                member.setTargetType(OrganizationMemberTargetType.UNTRACK.getCode());
                member.setTargetId(0L);
            }
            createOrganiztionMemberWithDetailAndUserOrganization(member, org.getId());
        } else {
            //说明organizationMember信息不为空
            if(OrganizationMemberGroupType.fromCode(member.getMemberGroup()) == OrganizationMemberGroupType.MANAGER){
                LOGGER.error("This user has been added to the administrator list.");
                throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_ADMINISTRATORS_LIST_EXISTS,
                        "This user has been added to the administrator list.");
            }
            member.setContactName(contactName);
            member.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
            if (null != userIdentifier) {
            	  member.setTargetType(OrganizationMemberTargetType.USER.getCode());
            	  member.setTargetId(userIdentifier.getOwnerUid());
                  sendMsgFlag = true;
            } else {
            	  member.setTargetType(OrganizationMemberTargetType.UNTRACK.getCode());
            	  member.setTargetId(0L);
            }
            if (OrganizationMemberStatus.ACTIVE != OrganizationMemberStatus.fromCode(member.getStatus())) {
                //把正在申请加入公司状态的 记录改成正常
                member.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
                if (OrganizationMemberTargetType.USER == OrganizationMemberTargetType.fromCode(member.getTargetType())) {
                    UserOrganizations userOrganizations = userOrganizationProvider.findUserOrganizations(namespaceId, organizationId, member.getTargetId());
                    if (null != userOrganizations) {
                        userOrganizations.setStatus(UserOrganizationStatus.ACTIVE.getCode());
                        userOrganizationProvider.updateUserOrganizations(userOrganizations);
                    }
                    sendMsgFlag = true;
                }
            }
            organizationProvider.updateOrganizationMember(member);
            List<OrganizationMember> ms = organizationProvider.listOrganizationMemberByPath(org.getPath() + "/%", null, member.getContactToken());
            for (OrganizationMember m: ms) {
                m.setContactName(contactName);
                organizationProvider.updateOrganizationMember(m);
            }

            if(null != member.getDetailId()){
                OrganizationMemberDetails detail =  organizationProvider.findOrganizationMemberDetailsByDetailId(member.getDetailId());
                detail.setContactName(contactName);
                organizationProvider.updateOrganizationMemberDetails(detail, member.getDetailId());
                // 删除离职Log表中的记录
                this.archivesService.deleteArchivesDismissEmployees(detail.getId(),detail.getOrganizationId());
            } else {
            	// 如果档案记录不存在，则再重新创建一个。 added by janson
            	Long new_detail_id = getEnableDetailOfOrganizationMember(member, organizationId);
            	member.setDetailId(new_detail_id);
            	organizationProvider.updateOrganizationMember(member);
            }

        }

        // 删除离职Log表中的记录
        OrganizationMemberDetails old_detail = organizationProvider.findOrganizationMemberDetailsByOrganizationIdAndContactToken(org.getId(), contactToken);
        if(old_detail != null){
            this.archivesService.deleteArchivesDismissEmployees(old_detail.getId(), old_detail.getOrganizationId());
        }


        //是注册用户或者从加入公司待审核的注册用户 则需要发送消息等等操作
        if (sendMsgFlag) {
            joinOrganizationAfterOperation(member,notSendMsgFlag);
        }
        
		//add by moubinmo；特殊情况：创建的管理员是没有注册激活的用户，添加需要添加一条 groupType 为  DIRECT_UNDER_ENTERPRISE 的部门信息到 eh_organization_members 表，否则从管理后台查出的人事记录没有部门记录。
		createOrganiztionMemberOfDirectUnderEnterprise(member, organizationId);

        return member;
    }

    @Override
    public ListOrganizationMemberCommandResponse listOrganizationPersonnelsByOrgIds(ListOrganizationPersonnelsByOrgIdsCommand cmd) {
        ListOrganizationMemberCommandResponse response = new ListOrganizationMemberCommandResponse();

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        String keywords = cmd.getKeywords();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<OrganizationMember> organizationMembers = organizationProvider.listOrganizationMembers(locator, pageSize, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {

                query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
                List<String> groupTypes = new ArrayList<>();
                Condition cond = null;
                groupTypes.add(OrganizationGroupType.JOB_POSITION.getCode());
                query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.in(groupTypes));

                if(cmd.getOrganizationIds() != null && cmd.getOrganizationIds().size() > 0){
                    query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.in(cmd.getOrganizationIds()));
                }

                if (!StringUtils.isEmpty(keywords)) {
                    query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq(keywords).or(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_NAME.like("%" + keywords + "%")));
                }
                query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc());
                query.addGroupBy(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN);
                return query;
            }
        });

        if (0 == organizationMembers.size()) {
            return response;
        }

        response.setNextPageAnchor(locator.getAnchor());

        response.setMembers(organizationMembers.stream().map(r->{
            return ConvertHelper.convert(r, OrganizationMemberDTO.class);
        }).collect(Collectors.toList()));

        return response;
    }

    @Override
    public Integer cleanWrongStatusOrganizationMembers(Integer namespaceId) {
        Tuple tuple = this.coordinationProvider.getNamedLock(CoordinationLocks.CLEANWRONGSTATUS_ORGANIZATIONMEMBERS.getCode() + namespaceId).enter(() -> {
            Integer count = 0;
            List<EhOrganizationsRecord> orgs = this.organizationProvider.listLapseOrganizations(namespaceId);
            Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
            if (orgs != null && orgs.size() > 0) {
                for (EhOrganizationsRecord r : orgs) {
                    int i = this.organizationProvider.updateOrganizationMembersToInactiveByPath(r.getPath(), now);
                    LOGGER.debug("cleanWrongStatusOrganizationMembers queue organizaitonId:" + r.getId() + " count: " + i);
                    count += i;
                };
            }
            return count;
        });
        return (Integer) tuple.first();
    }

    @Override
    public Long modifyPhoneNumberByDetailId(Long detailId, String contactToken) {
        OrganizationMemberDetails detail = this.organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
        if(detail == null || !detail.getTargetType().equals(OrganizationMemberTargetType.UNTRACK.getCode())){
            return 0L;
        }
        List<String> groupTypes = new ArrayList<>();
        groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
        groupTypes.add(OrganizationGroupType.GROUP.getCode());
        groupTypes.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
        groupTypes.add(OrganizationGroupType.JOB_POSITION.getCode());
        groupTypes.add(OrganizationGroupType.JOB_LEVEL.getCode());
        groupTypes.add(OrganizationGroupType.MANAGER.getCode());
        List<OrganizationMember> members = organizationProvider.listOrganizationMembersByDetailId(detailId, groupTypes);
        if (members != null) {
            members.stream().filter(r ->
                    r.getContactType() != null && r.getContactType() == IdentifierType.MOBILE.getCode()
            ).forEach(r -> {
                r.setContactToken(contactToken);
                organizationProvider.updateOrganizationMember(r);
            });

            List<Long> detailIds = members.stream().map(OrganizationMember::getDetailId).collect(Collectors.toList());
            List<OrganizationMemberDetails> details = organizationProvider.findDetailInfoListByIdIn(detailIds);
            if (details != null) {
                details.forEach(r -> {
                    r.setContactToken(contactToken);
                    organizationProvider.updateOrganizationMemberDetails(r, r.getId());
                });
            }
        }
        return detailId;
    }

    public List<OrganizationManagerDTO> getManagerByTargetIdAndOrgId(Long orgId, Long targetId, Integer level) {
        OrganizationMember member  = this.organizationProvider.findDepartmentMemberByTargetIdAndOrgId(targetId, orgId);
        List<OrganizationManagerDTO> managers =  new ArrayList<>();
        if(member != null){
            // todo 本部门
            Long org_id = member.getOrganizationId();
            int i = 0;
            while (i < level){
                Long temp_org_id = checkOrganization(org_id).getParentId();
                if (temp_org_id != 0) {
                    org_id = temp_org_id;
                    i++;
                } else {
                    return new ArrayList<>();
                }
            }
            managers = getOrganizationManagers(org_id);
        }

        return managers;
    }

    private UserIdentifier createUserAndIdentifier(Integer namespaceId, String nickName, String identifierToken) {
        User user = new User();
        user.setStatus(UserStatus.ACTIVE.getCode());
        user.setNamespaceId(namespaceId);
        user.setNickName(nickName);
        user.setGender(UserGender.UNDISCLOSURED.getCode());
        String salt = EncryptionUtils.createRandomSalt();
        user.setSalt(salt);
        try {
            user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", salt)));
        } catch (Exception e) {
            LOGGER.error("encode password failed");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PASSWORD, "Unable to create password hash");
        }
        userProvider.createUser(user);
        UserIdentifier userIdentifier = new UserIdentifier();
        userIdentifier.setOwnerUid(user.getId());
        userIdentifier.setIdentifierType(IdentifierType.MOBILE.getCode());
        userIdentifier.setIdentifierToken(identifierToken);
        userIdentifier.setNamespaceId(namespaceId);
        userIdentifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
        userProvider.createIdentifier(userIdentifier);
        return userIdentifier;
    }

    @Override
    public OrganizationMemberDTO processUserForMemberWithoutMessage(UserIdentifier identifier) {
        return processUserForMember(identifier, false);
    }

    @Override
    public OrganizationMemberDTO  processUserForMember(UserIdentifier identifier) {
        return processUserForMember(identifier, true);
    }

    @Override
    public OrganizationMemberDTO processUserForMember(Integer namespaceId, String identifierToken, Long ownerId) {
        UserIdentifier identifier = userProvider.findClaimingIdentifierByToken(namespaceId, identifierToken);
        LOGGER.info("processUserForMember namespaceId = {},identifierToken = {}, identifier={}", namespaceId, identifierToken, identifier);
        if (identifier == null) {
            identifier = ConvertHelper.convert(this.sdkUserService.getUserIdentifierByIdentifierToken(namespaceId,identifierToken), UserIdentifier.class);
            LOGGER.info("get userIdentifier from unite user namespaceId = {},identifierToken = {}, identifier={}", namespaceId, identifierToken, identifier);
            if (identifier != null) {
                this.userProvider.createIdentifierFromUnite(identifier);
            } else {
                LOGGER.warn("Sdk user service getUserIdentifier return null, namespaceId={}, identifierToken= {}", namespaceId,identifierToken);
                return null;
            }
        }
        this.propertyMgrService.processUserForOwner(identifier);
        return processUserForMember(identifier, true);
    }


    private OrganizationMemberDTO processUserForMember(UserIdentifier identifier, boolean needSendMessage) {
        try {
            User user = userProvider.findUserById(identifier.getOwnerUid());
            List<OrganizationMember> members = this.organizationProvider.listOrganizationMembersByPhone(identifier.getIdentifierToken());
            OrganizationMember organizationMember = null;
            Organization firstOrg = null; // 第一个机构
            for (OrganizationMember member : members) {
                Organization org = organizationProvider.findOrganizationById(member.getOrganizationId());
                if (org.getNamespaceId() == null || !org.getNamespaceId().equals(identifier.getNamespaceId())) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Ignore the enterprise who is dismatched to namespace, enterpriseId=" + org.getId()
                                + ", enterpriseNamespaceId=" + org.getNamespaceId() + ", userId=" + identifier.getOwnerUid()
                                + ", userNamespaceId=" + identifier.getNamespaceId());
                    }
                    continue;
                }

                if (firstOrg == null) {
                    firstOrg = org;
                }

                if (OrganizationMemberStatus.fromCode(member.getStatus()) == OrganizationMemberStatus.ACTIVE) {
                    member.setTargetId(user.getId());
                    member.setTargetType(OrganizationMemberTargetType.USER.getCode());
                    this.updateMemberUser(member);
                    DaoHelper.publishDaoAction(DaoAction.CREATE, OrganizationMember.class, member.getId());
                    //修改企业客户管理中的状态 by jiarui
                    try {
                        enterpriseCustomerProvider.updateEnterpriseCustomerAdminRecord(member.getContactToken(),member.getNamespaceId());
                        customerProvider.updateCustomerTalentRegisterStatus(member.getContactToken());
                    }catch (Exception e){
                        LOGGER.error("update enterprise customer admin record erro:{}",e);
                    }

                    // 机构是公司的情况下 才发送短信
                    if (OrganizationGroupType.fromCode(org.getGroupType()) == OrganizationGroupType.ENTERPRISE) {

                        //标识了管理员的人员 需要分配具体管理员的权限 add by sfyan 20170818
                        if (OrganizationMemberGroupType.MANAGER == OrganizationMemberGroupType.fromCode(member.getMemberGroup())) {
                            Long adminPrivilegeId = null;
                            Long adminRoleId = null;
                            if (OrganizationType.PM == OrganizationType.fromCode(org.getOrganizationType())) {
                                adminPrivilegeId = PrivilegeConstants.ORGANIZATION_SUPER_ADMIN;
                                adminRoleId = RoleConstants.PM_SUPER_ADMIN;
                            } else if (OrganizationType.ENTERPRISE == OrganizationType.fromCode(org.getOrganizationType())) {
                                adminPrivilegeId = PrivilegeConstants.ORGANIZATION_ADMIN;
                                adminRoleId = RoleConstants.ENTERPRISE_SUPER_ADMIN;
                            }

                            if (null != adminPrivilegeId) {
                                //分配具体公司管理员权限
                                rolePrivilegeService.assignmentPrivileges(EntityType.ORGANIZATIONS.getCode(), org.getId(), EntityType.USER.getCode(), member.getTargetId(), "admin", adminPrivilegeId);

                                //分配管理员角色
                                rolePrivilegeService.assignmentAclRole(EntityType.ORGANIZATIONS.getCode(), org.getId(), EntityType.USER.getCode(), member.getTargetId(), identifier.getNamespaceId(), user.getId(), adminRoleId);
                            }

                        }


                        //创建管理员的同时会同时创建一个用户，因此需要在user_organization中添加一条记录 modify at 17/08/16
                        //仅当target为user且grouptype为企业时添加
                        createOrUpdateUserOrganization(member);
                        if (needSendMessage) {
                            sendMessageForContactApproved(member);
                        }
                        userSearcher.feedDoc(member);
                        //支持多部门 记录可能存在多条，故取公司这条
                        organizationMember = member;
                        if (LOGGER.isInfoEnabled()) {
                            LOGGER.info("User join the enterprise automatically, userId=" + identifier.getOwnerUid()
                                    + ", contactId=" + member.getId() + ", enterpriseId=" + member.getOrganizationId());
                        }
                        //自动加入公司的门禁 add by lei.lv
                        this.doorAccessService.joinCompanyAutoAuth(UserContext.getCurrentNamespaceId(), member.getOrganizationId(), member.getTargetId());

                        //通过认证的同步到企业客户的人才团队中 21710
                        customerService.createCustomerTalentFromOrgMember(member.getOrganizationId(), member);
                    } else {
                        if (LOGGER.isInfoEnabled()) {
                            LOGGER.debug("organization group type not enterprise, organizationId={}, groupType={}, memberId={}", member.getOrganizationId(), member.getStatus(), member.getId());
                        }
                    }

                } else {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Enterprise contact is already authenticated, userId=" + identifier.getOwnerUid()
                                + ", contactId=" + member.getId() + ", enterpriseId=" + member.getOrganizationId());
                    }
                }

            }

            // 需要给用户默认一下小区（以机构所在园区为准），否则会在用户退出时没有小区而客户端拿不到场景而卡死
            // http://devops.lab.everhomes.com/issues/2812  by lqs 20161017
            if (firstOrg != null) {
                Integer namespaceId = UserContext.getCurrentNamespaceId();
                setUserDefaultCommunityByOrganization(namespaceId, identifier.getOwnerUid(), firstOrg.getId());
            }

            return ConvertHelper.convert(organizationMember, OrganizationMemberDTO.class);
        } catch (Exception e) {
            LOGGER.error("Failed to processStat the enterprise contact for the user, userId=" + identifier.getOwnerUid(), e);
        }
        return null;
    }


    @Override
    public List<AclRoleAssignmentsDTO> listAclRoleByUserId(ListAclRoleByUserIdCommand cmd) {
        if (null == cmd.getUserId()) {
            cmd.setUserId(UserContext.current().getUser().getId());
        }

        List<AclRoleAssignmentsDTO> dtos = new ArrayList<AclRoleAssignmentsDTO>();

        List<Long> resources = aclProvider.getRolesFromResourceAssignments(EntityType.ORGANIZATIONS.getCode(), null, EntityType.USER.getCode(), cmd.getUserId(), null);

        if (null != resources && resources.size() > 0) {
            for (Long roleId : resources) {
                Role role = aclProvider.getRoleById(roleId);
                AclRoleAssignmentsDTO dto = new AclRoleAssignmentsDTO();
                dto.setRoleId(role.getId());
                dto.setRoleName(role.getName());
                dtos.add(dto);
            }
        }

        return dtos;
    }


    @Override
    public ImportFileTaskDTO importEnterpriseData(ImportEnterpriseDataCommand cmd, MultipartFile file, Long userId) {
//        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), cmd.getManageOrganizationId(), PrivilegeConstants.ORGANIZATION_IMPORT, ServiceModuleConstants.ORGANIZATION_MODULE, ActionType.OFFICIAL_URL.getCode(), null, cmd.getManageOrganizationId(), cmd.getCommunityId());

        //创建楼栋和门牌的集合
        List<OrganizationSiteApartmentDTO> siteDtos = Lists.newArrayList();
        //创建OrganizationApartDTO类的对象
//        OrganizationSiteApartmentDTO organizationSiteApartmentDTO = new OrganizationSiteApartmentDTO();

        //创建CommunityDTO类的对象
//        CommunityDTO communityDTO = new CommunityDTO();

        Long communityId = cmd.getCommunityId();
        ImportFileTask task = new ImportFileTask();
        try {
            //解析excel
            List resultList = PropMrgOwnerHandler.processorExcel(file.getInputStream());

            if (null == resultList || resultList.isEmpty()) {
                LOGGER.error("File content is empty。userId=" + userId);
                throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_FILE_IS_EMPTY,
                        "File content is empty");
            }
            task.setOwnerType(EntityType.COMMUNITY.getCode());
            task.setOwnerId(communityId);
            task.setType(ImportFileTaskType.ENGERPRISE.getCode());
            task.setCreatorUid(userId);
            task = importFileService.executeTask(() -> {
                ImportFileResponse response = new ImportFileResponse();
                List<ImportEnterpriseDataDTO> datas = handleImportEnterpriseData(resultList);
                if (datas.size() > 0) {
                    //设置导出报错的结果excel的标题
                    response.setTitle(datas.get(0));
                    datas.remove(0);
                    datas.remove(0);
                }
                //开始拆包
                List<ImportEnterpriseDataDTO> dataNew = Lists.newArrayList();
                for (int i = 0; i < datas.size(); i++) {
                    ImportEnterpriseDataDTO importEnterpriseDataDTO = datas.get(i);
                    ImportEnterpriseDataDTO data = new ImportEnterpriseDataDTO();

                    if(importEnterpriseDataDTO.getName() != null){
                        //说明公司的名称不为空，那么取出公司的名称
                        data.setName(importEnterpriseDataDTO.getName());
                    }
                    if(importEnterpriseDataDTO.getDisplayName() != null){
                        //说明公司的简称不为空，那么取出公司的简称
                        data.setDisplayName(importEnterpriseDataDTO.getDisplayName());
                    }
                    if(importEnterpriseDataDTO.getMemberRange() != null){
                        //说明人员规模不为空，那么取出人员规模
                        data.setMemberRange(importEnterpriseDataDTO.getMemberRange());
                    }
                    if(importEnterpriseDataDTO.getAdminToken() != null){
                        //说明管理员手机号不为空，那么取出管理员手机号
                        data.setAdminToken(importEnterpriseDataDTO.getAdminToken());
                    }
                    if(importEnterpriseDataDTO.getAdminName() != null){
                        //说明管理员姓名不为空，那么取出管理员姓名
                        data.setAdminName(importEnterpriseDataDTO.getAdminName());
                    }
                    if(importEnterpriseDataDTO.getWorkPlaceName() != null){
                        //说明办公地点名称不为空，那么取出办公地点名称
                        data.setWorkPlaceName(importEnterpriseDataDTO.getWorkPlaceName());
                    }
                    if(importEnterpriseDataDTO.getCommunityName() != null){
                        //说明办公地点所属项目不为空，那么将办公地点所属项目取出来
                        data.setCommunityName(importEnterpriseDataDTO.getCommunityName());
                    }
                    if(importEnterpriseDataDTO.getPmFlag() != null){
                        //说明管理公司标志不为空，那么取出管理公司标志
                        data.setPmFlag(importEnterpriseDataDTO.getPmFlag());
                    }
                    if(importEnterpriseDataDTO.getServiceSupportFlag() != null){
                        //说明服务商标志不为空，那么将服务商标志取出来
                        data.setServiceSupportFlag(importEnterpriseDataDTO.getServiceSupportFlag());
                    }
                    if(importEnterpriseDataDTO.getWorkPlatFormFlag() != null){
                        //说明是否开启移动工作台标志不为空，那么将其取出来
                        data.setWorkPlatFormFlag(importEnterpriseDataDTO.getWorkPlatFormFlag());
                    }




                    if(importEnterpriseDataDTO.getBuildingNameAndApartmentName() != null){
                        for(String str : importEnterpriseDataDTO.getBuildingNameAndApartmentName().split(",")){
                            //创建OrganizationApartDTO类的对象
                            OrganizationSiteApartmentDTO organizationSiteApartmentDTO = new OrganizationSiteApartmentDTO();
                            if(str.contains("-")){
                                if(str.split("-")[0] != null){
                                    organizationSiteApartmentDTO.setBuildingName(str.split("-")[0]);
                                }
                                if(str.split("-")[1] != null){
                                    organizationSiteApartmentDTO.setApartmentName(str.split("-")[1]);
                                }
                            }else{
                                organizationSiteApartmentDTO.setBuildingName(str.split("-")[0]);
                            }
                            siteDtos.add(organizationSiteApartmentDTO);
                        }
                        data.setSiteDtos(siteDtos);
                    }
                    if(importEnterpriseDataDTO.getCommunityNames() != null){
                        //创建管理的项目的集合
                        List<CommunityDTO> communityDTOList = Lists.newArrayList();
                        for(String str : importEnterpriseDataDTO.getCommunityNames().split(",")){
                            //创建CommunityDTO类的对象
                            CommunityDTO communityDTO = new CommunityDTO();
                            communityDTO.setName(str);
                            communityDTOList.add(communityDTO);
                        }
                        data.setCommunityDTOList(communityDTOList);
                    }
//                    datas.add(data);
                    dataNew.add(data);
                }

                List<ImportFileResultLog<ImportEnterpriseDataDTO>> results = importEnterprise(dataNew, userId, cmd);

                response.setFailCount((long) results.size());
                response.setTotalCount((long) datas.size());
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
    public ImportFileResponse<ImportEnterpriseDataDTO> importEnterpriseData(MultipartFile mfile,
                                                                            Long userId, ImportEnterpriseDataCommand cmd) {
        ImportFileResponse<ImportEnterpriseDataDTO> importDataResponse = new ImportFileResponse<>();
        try {
            //解析excel
            List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());

            if (null == resultList || resultList.isEmpty()) {
                LOGGER.error("File content is empty。userId=" + userId);
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL,
                        "File content is empty");
            }
            LOGGER.debug("Start import data...,total:" + resultList.size());
            //导入数据，返回导入错误的日志数据集
            List<ImportFileResultLog<ImportEnterpriseDataDTO>> errorDataLogs = importEnterprise(handleImportEnterpriseData(resultList), userId, cmd);
            LOGGER.debug("End import data...,fail:" + errorDataLogs.size());
            if (null == errorDataLogs || errorDataLogs.isEmpty()) {
                LOGGER.debug("Data import all success...");
            }

            importDataResponse.setTotalCount((long) resultList.size() - 1);
            importDataResponse.setFailCount((long) errorDataLogs.size());
            importDataResponse.setLogs(errorDataLogs);
        } catch (IOException e) {
            LOGGER.error("File can not be resolved. e = {}", e);
        }
        return importDataResponse;
    }

    @Override
    public ImportFileTaskDTO importOrganizationPersonnelData(MultipartFile mfile,
                                                             Long userId, ImportOrganizationPersonnelDataCommand cmd) {
        ImportFileTask task = new ImportFileTask();
        try {
            //解析excel
            List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());

            if (null == resultList || resultList.isEmpty()) {
                LOGGER.error("File content is empty。userId=" + userId);
                throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_FILE_IS_EMPTY,
                        "File content is empty");
            }
            task.setOwnerType(EntityType.ORGANIZATIONS.getCode());
            task.setOwnerId(cmd.getOrganizationId());
            task.setType(ImportFileTaskType.ORGANIZATION_CONTACT.getCode());
            task.setCreatorUid(userId);
            task = importFileService.executeTask(new ExecuteImportTaskCallback() {
                @Override
                public ImportFileResponse importFile() {
                    ImportFileResponse response = new ImportFileResponse();
                    List<ImportOrganizationContactDataDTO> datas = handleImportOrganizationContactData(resultList);
                    if (datas.size() > 0) {
                        //设置导出报错的结果excel的标题
                        response.setTitle(datas.get(0));
                        datas.remove(0);
                    }
                    List<ImportFileResultLog<ImportOrganizationContactDataDTO>> results = importOrganizationPersonnel(datas, userId, cmd);
                    response.setTotalCount((long) datas.size());
                    response.setFailCount((long) results.size());
                    response.setLogs(results);
                    return response;
                }
            }, task);

        } catch (IOException e) {
            LOGGER.error("File can not be resolved...");
            e.printStackTrace();
        }
        return ConvertHelper.convert(task, ImportFileTaskDTO.class);
    }

    private List<ImportEnterpriseDataDTO> handleImportEnterpriseData(List list) {
        List<ImportEnterpriseDataDTO> datas = new ArrayList<>();
/*        //创建楼栋和门牌的集合
        List<OrganizationSiteApartmentDTO> siteDtos = Lists.newArrayList();
        //创建OrganizationApartDTO类的对象
        OrganizationSiteApartmentDTO organizationSiteApartmentDTO = new OrganizationSiteApartmentDTO();
        //创建管理的项目的集合
        List<CommunityDTO> communityDTOList = Lists.newArrayList();
        //创建CommunityDTO类的对象
        CommunityDTO communityDTO = new CommunityDTO();*/
        for (int i = 0; i < list.size(); i++) {
            RowResult r = (RowResult) list.get(i);
            if (org.apache.commons.lang.StringUtils.isNotBlank(r.getA()) || org.apache.commons.lang.StringUtils.isNotBlank(r.getB()) ||
                    org.apache.commons.lang.StringUtils.isNotBlank(r.getC()) || org.apache.commons.lang.StringUtils.isNotBlank(r.getD()) ||
                    org.apache.commons.lang.StringUtils.isNotBlank(r.getE()) || org.apache.commons.lang.StringUtils.isNotBlank(r.getF()) ||
                    org.apache.commons.lang.StringUtils.isNotBlank(r.getG()) || org.apache.commons.lang.StringUtils.isNotBlank(r.getH()) ||
                    org.apache.commons.lang.StringUtils.isNotBlank(r.getI()) || org.apache.commons.lang.StringUtils.isNotBlank(r.getJ()) ||
                    org.apache.commons.lang.StringUtils.isNotBlank(r.getK()) || org.apache.commons.lang.StringUtils.isNotBlank(r.getL())) {
                ImportEnterpriseDataDTO data = new ImportEnterpriseDataDTO();
                if (null != r.getA())
                    //设置公司名称
                    data.setName(r.getA().trim());
                if (null != r.getB())
                    //设置公司简称
                    data.setDisplayName(r.getB().trim());
                if (null != r.getC())
                    //设置人员规模
                    data.setMemberRange(r.getC().trim());
                if (null != r.getD())
                    //设置管理员手机号
                    data.setAdminToken(r.getD().trim());
                if (null != r.getE())
                    //设置管理员姓名
                    data.setAdminName(r.getE().trim());
                if (null != r.getF())
                    //设置办公地点名称
                    data.setWorkPlaceName(r.getF().trim());
                if (null != r.getG())
                    //设置办公地点所属项目名称
                    data.setCommunityName(r.getG().trim());
                if (null != r.getH()){
                    /*for(String str : r.getH().split(",")){
                        if(str.split("-")[0] != null){
                            organizationSiteApartmentDTO.setBuildingName(str.split("-")[0]);
                        }
                        if(str.split("-")[1] != null){
                            organizationSiteApartmentDTO.setApartmentName(str.split("-")[1]);
                        }
                        siteDtos.add(organizationSiteApartmentDTO);
                    }
                    data.setSiteDtos(siteDtos);*/
                    data.setBuildingNameAndApartmentName(r.getH().trim());
                }
                if (null != r.getJ())
                    data.setPmFlag(r.getJ().trim());
                if (null != r.getK()){
                    /*for(String str : r.getJ().split(",")){
                        communityDTO.setName(str);
                        communityDTOList.add(communityDTO);
                    }
                    data.setCommunityDTOList(communityDTOList);*/
                    data.setCommunityNames(r.getK().trim());
                }
                if (null != r.getL())
                    data.setServiceSupportFlag(r.getL().trim());
                if (null != r.getM())
                    data.setWorkPlatFormFlag(r.getM().trim());
                    datas.add(data);
            }
        }
        return datas;
    }

    private List<ImportOrganizationContactDataDTO> handleImportOrganizationContactData(List list) {
        List<ImportOrganizationContactDataDTO> datas = new ArrayList<>();
        int row = 1;
        for (Object o : list) {
            if (row < 2) {
                row++;
                continue;
            }
            RowResult r = (RowResult) o;
            ImportOrganizationContactDataDTO data = new ImportOrganizationContactDataDTO();
            if (null != r.getA())
                data.setContactName(r.getA().trim());
            if (null != r.getB())
                data.setContactToken(r.getB().trim());
            if (null != r.getC())
                data.setGender(r.getC().trim());
            if (null != r.getD())
                data.setOrgnaizationPath(r.getD().trim());
            if (null != r.getE())
                data.setJobPosition(r.getE().trim());
            if (null != r.getF())
                data.setJobLevel(r.getF().trim());
            datas.add(data);
        }
        return datas;
    }

    private List<String> convertToStrList(List list) {
        List<String> result = new ArrayList<String>();
        int row = 1;
        for (Object o : list) {
            if (row < 3) {
                row++;
                continue;
            }
            RowResult r = (RowResult) o;
            StringBuffer sb = new StringBuffer();
            sb.append(r.getA().trim()).append("||");
            sb.append(r.getB().trim()).append("||");
            sb.append(r.getC().trim()).append("||");
            sb.append(r.getD().trim()).append("||");
            sb.append(r.getE().trim()).append("||");
            sb.append(r.getF().trim()).append("||");
            sb.append(r.getG().trim()).append("||");
            sb.append(r.getH().trim()).append("||");
            sb.append(r.getI().trim()).append("||");
            sb.append(r.getJ().trim()).append("||");
            sb.append(r.getK().trim());
            result.add(sb.toString().replace("null", ""));
        }
        return result;
    }

    private List<ImportFileResultLog<ImportEnterpriseDataDTO>> importEnterprise(List<ImportEnterpriseDataDTO> list, Long userId, ImportEnterpriseDataCommand cmd) {
        User user = UserContext.current().getUser();
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());

        List<ImportFileResultLog<ImportEnterpriseDataDTO>> errorDataLogs = new ArrayList<>();


            //首先需要进行非空校验
            if(!CollectionUtils.isEmpty(list)){
                //说明集合不为空，那么我们将该集合进行遍历
                for(ImportEnterpriseDataDTO importEnterpriseDataDTO : list){
                    Community community = communityProvider.findCommunityByNameAndNamespaceId(importEnterpriseDataDTO.getCommunityName(),namespaceId);

                    ImportFileResultLog<ImportEnterpriseDataDTO> log = new ImportFileResultLog<>(OrganizationServiceErrorCode.SCOPE);
                    if(community != null ){
                        //创建公司Organization类的对象
                        Organization organization = new Organization();
                        if (StringUtils.isEmpty(importEnterpriseDataDTO.getName())) {
                            LOGGER.error("enterprise name is null, data = {}", importEnterpriseDataDTO);
                            log.setData(importEnterpriseDataDTO);
                            log.setErrorLog("enterprise name is null");
                            log.setCode(OrganizationServiceErrorCode.ERROR_ENTERPRISE_NAME_EMPTY);
                            errorDataLogs.add(log);
                            continue;
//                            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ENTERPRISE_NAME_EMPTY,
//                                    "enterprise name is null");
                        }

                        LOGGER.info(String.valueOf(importEnterpriseDataDTO.getName().getBytes().length));
                        if(importEnterpriseDataDTO.getName().getBytes().length > 150){
                            //说明公司的名称的长度大于50个字，那么就不允许
                            LOGGER.error("enterprise name is over than 100 bytes, data = {}", importEnterpriseDataDTO);
                            log.setData(importEnterpriseDataDTO);
                            log.setErrorLog("enterprise name is over than 100 bytes");
                            log.setCode(OrganizationServiceErrorCode.ERROR_ORGANIZATION_NAME_OVERFLOW);
                            errorDataLogs.add(log);
                            continue;
                        }

                        if (CollectionUtils.isEmpty(importEnterpriseDataDTO.getSiteDtos())) {
                            LOGGER.error("buildingName and apartmentName are null, data = {}", importEnterpriseDataDTO);
                            log.setData(importEnterpriseDataDTO);
                            log.setErrorLog("buildingName and apartmentName are null");
                            log.setCode(OrganizationServiceErrorCode.ERROR_BUILDING_NAME_EMPTY);
                            errorDataLogs.add(log);
                            continue;
//                            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_BUILDING_NAME_EMPTY,
//                                    "buildingName and apartmentName are null");
                        }

                        if(StringUtils.isEmpty(importEnterpriseDataDTO.getAdminToken())){
                            LOGGER.error("adminToken is null, data = {}", importEnterpriseDataDTO);
                            LOGGER.error("adminToken is null, data = {}", importEnterpriseDataDTO);
                            log.setData(importEnterpriseDataDTO);
                            log.setErrorLog("adminToken is null");
                            log.setCode(OrganizationServiceErrorCode.ERROR_CONTACTTOKEN_ISNULL);
                            errorDataLogs.add(log);
                            continue;
//                            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_CONTACTTOKEN_ISNULL,
//                                    "adminToken is null");
                        }

                        if(StringUtils.isEmpty(importEnterpriseDataDTO.getAdminName())){
                            LOGGER.error("adminName is null, data = {}", importEnterpriseDataDTO);
                            LOGGER.error("adminName is null, data = {}", importEnterpriseDataDTO);
                            log.setData(importEnterpriseDataDTO);
                            log.setErrorLog("adminName is null");
                            log.setCode(OrganizationServiceErrorCode.ERROR_ADMINNAME_ISNULL);
                            errorDataLogs.add(log);
                            continue;
//                            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ADMINNAME_ISNULL,
//                                    "adminName is null");
                        }

                        if(StringUtils.isEmpty(importEnterpriseDataDTO.getWorkPlaceName())){
                            LOGGER.error("workPlaceName is null, data = {}", importEnterpriseDataDTO);
                            LOGGER.error("workPlaceName is null, data = {}", importEnterpriseDataDTO);
                            log.setData(importEnterpriseDataDTO);
                            log.setErrorLog("workPlaceName is null");
                            log.setCode(OrganizationServiceErrorCode.ERROR_WORKPLACENAME_ISNULL);
                            errorDataLogs.add(log);
                            continue;
//                            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_WORKPLACENAME_ISNULL,
//                                    "workPlaceName is null");
                        }

                        if(StringUtils.isEmpty(importEnterpriseDataDTO.getCommunityName())){
                            LOGGER.error("communityName is null, data = {}", importEnterpriseDataDTO);
                            LOGGER.error("communityName is null, data = {}", importEnterpriseDataDTO);
                            log.setData(importEnterpriseDataDTO);
                            log.setErrorLog("communityName is null");
                            log.setCode(OrganizationServiceErrorCode.ERROR_COMMUNITYNAME_ISNULL);
                            errorDataLogs.add(log);
                            continue;
//                            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_COMMUNITYNAME_ISNULL,
//                                    "communityName is null");
                        }

                        if(StringUtils.isEmpty(importEnterpriseDataDTO.getPmFlag())){
                            LOGGER.error("PmFlag is null, data = {}", importEnterpriseDataDTO);
                            LOGGER.error("PmFlag is null, data = {}", importEnterpriseDataDTO);
                            log.setData(importEnterpriseDataDTO);
                            log.setErrorLog("PmFlag is null");
                            log.setCode(OrganizationServiceErrorCode.ERROR_PMFLAG_ISNULL);
                            errorDataLogs.add(log);
                            continue;
//                            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_PMFLAG_ISNULL,
//                                    "PmFlag is null");
                        }

                        if(StringUtils.isEmpty(importEnterpriseDataDTO.getServiceSupportFlag())){
                            LOGGER.error("serviceSupportFlag is null, data = {}", importEnterpriseDataDTO);
                            LOGGER.error("serviceSupportFlag is null, data = {}", importEnterpriseDataDTO);
                            log.setData(importEnterpriseDataDTO);
                            log.setErrorLog("serviceSupportFlag is null");
                            log.setCode(OrganizationServiceErrorCode.ERROR_SERVICESUPPORT_ISNULL);
                            errorDataLogs.add(log);
                            continue;
//                            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_SERVICESUPPORT_ISNULL,
//                                    "serviceSupportFlag is null");
                        }

                        if(StringUtils.isEmpty(importEnterpriseDataDTO.getWorkPlatFormFlag())){
                            LOGGER.error("workPlatformFlag is null, data = {}", importEnterpriseDataDTO);
                            LOGGER.error("workPlatformFlag is null, data = {}", importEnterpriseDataDTO);
                            log.setData(importEnterpriseDataDTO);
                            log.setErrorLog("workPlatformFlag is null");
                            log.setCode(OrganizationServiceErrorCode.ERROR_WORKPLATFORM_ISNULL);
                            errorDataLogs.add(log);
                            continue;
//                            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_WORKPLATFORM_ISNULL,
//                                    "workPlatformFlag is null");
                        }

                        dbProvider.execute((TransactionStatus status) -> {
                            //创建群组Group类的对象
                            Group group = new Group();
                            //将数据封装在对象中
                            group.setName(importEnterpriseDataDTO.getName());
                            group.setDisplayName(importEnterpriseDataDTO.getDisplayName());

                            group.setStatus(OrganizationStatus.ACTIVE.getCode());

                            group.setCreatorUid(user.getId());

                            group.setNamespaceId(namespaceId);

                            group.setDiscriminator(GroupDiscriminator.ENTERPRISE.getCode());

                            group.setPrivateFlag(GroupPrivacy.PRIVATE.getCode());

                            groupProvider.createGroup(group);


                            //检查公司名称是否已经存在
                            boolean flag = checkOrgNameUniqueNew(null, cmd.getNamespaceId(), importEnterpriseDataDTO.getName());
                            if(flag == true){
                                //设置eh_organizations表中的parent_id字段
                                organization.setParentId(0L);
                                //设置level
                                organization.setLevel(1);
                                //设置path
                                organization.setPath("");
                                //设置企业名称
                                organization.setName(importEnterpriseDataDTO.getName());
                                //设置group_type字段
                                organization.setGroupType(OrganizationGroupType.ENTERPRISE.getCode());
                                //设置status为2
                                organization.setStatus(OrganizationStatus.ACTIVE.getCode());
                                //设置organizationType
                                organization.setOrganizationType(OrganizationType.ENTERPRISE.getCode());
                                //设置域空间Id
                                organization.setNamespaceId(namespaceId);
                                //设置创建时间
                                organization.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                                //设置更新时间
                                organization.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

                                organization.setGroupId(group.getId());
                                //表明该公司是否是管理公司 1-是 0-否
                                if(importEnterpriseDataDTO.getPmFlag() != null && importEnterpriseDataDTO.getPmFlag().equals("是")){
                                    organization.setPmFlag(Byte.valueOf("1"));
                                }else{
                                    organization.setPmFlag(Byte.valueOf("0"));
                                }
                                //表明该公司是否是服务商，1-服务商 0-否
                                if(importEnterpriseDataDTO.getServiceSupportFlag() != null && importEnterpriseDataDTO.getServiceSupportFlag().equals("是")){
                                    organization.setServiceSupportFlag(Byte.valueOf("1"));
                                }else{
                                    organization.setServiceSupportFlag(Byte.valueOf("0"));
                                }
                                if(importEnterpriseDataDTO.getWorkPlatFormFlag() != null && importEnterpriseDataDTO.getWorkPlatFormFlag().equals("启用")){
                                    organization.setWorkPlatformFlag(Byte.valueOf("1"));
                                }else{
                                    organization.setWorkPlatformFlag(Byte.valueOf("0"));
                                }
                                organizationProvider.createOrganization(organization);




                                //根据是否是管理公司来进行添加eh_organization_communities表数据，只有是管理公司才能拥有管理的项目
                                if(importEnterpriseDataDTO.getPmFlag() != null && importEnterpriseDataDTO.getPmFlag().equals("是")){
                                    //说明是管理员，那么我们就可以将管理的项目添加到eh_organization_communities表中
                                    if(!CollectionUtils.isEmpty(importEnterpriseDataDTO.getCommunityDTOList())){
                                        //说明管理的项目不为空，那么我们根据管理的项目名称查出来管理项目的id的集合
                                        //// TODO: 2018/5/14
                                        List<CommunityDTO> communityDTOList = importEnterpriseDataDTO.getCommunityDTOList();
                                        //进行非空校验
                                        if(!CollectionUtils.isEmpty(communityDTOList)){
                                            //集合communityDTOList不为空
                                            //创建一个集合List<String>用于承载项目名称
                                            List<String> communityNameList = Lists.newArrayList();
                                            for(CommunityDTO communityDTO : communityDTOList){
                                                communityNameList.add(communityDTO.getName());
                                            }
                                            //// TODO: 2018/5/14
                                            List<Long> communityIdList = organizationProvider.findCommunityIdListByNames(communityNameList);
                                            //进行非空校验
                                            if(!CollectionUtils.isEmpty(communityIdList)){
                                                //说明集合communityIdList不为空
                                                for(Long lon : communityIdList){
                                                    OrganizationCommunity organizationCommunity = new OrganizationCommunity();
                                                    organizationCommunity.setCommunityId(lon);
                                                    organizationCommunity.setOrganizationId(organization.getId());
                                                    organizationProvider.insertOrganizationCommunity(organizationCommunity);
                                                }
                                            }
                                        }


                                    }
                                }

                                //首先需要创建List<CreateOfficeSiteCommand> officeSites集合
                                List<CreateOfficeSiteCommand> officeSites = Lists.newArrayList();
                                //创建CreateOfficeSiteCommand类的对象
                                CreateOfficeSiteCommand createOfficeSiteCommand = new CreateOfficeSiteCommand();
                                //创建List<OrganizationSiteApartmentDTO> siteDtos集合
                                List<OrganizationSiteApartmentDTO> siteDtos = Lists.newArrayList();
                                List<OrganizationSiteApartmentDTO> siteDtosNew = Lists.newArrayList();
                                //创建OrganizationSiteApartmentDTO类的对象
                                OrganizationSiteApartmentDTO organizationSiteApartmentDTO = new OrganizationSiteApartmentDTO();
                                //将数据封装在对象CreateOfficeSiteCommand中
                                createOfficeSiteCommand.setCommunityId(community.getId());
                                createOfficeSiteCommand.setSiteName(importEnterpriseDataDTO.getWorkPlaceName());
                                //从ImportEnterpriseDataDTO对象中拿到楼栋和门牌对应的名称的集合
                                siteDtos = importEnterpriseDataDTO.getSiteDtos();
                                //进行非空校验
                                if(!CollectionUtils.isEmpty(siteDtos)){
                                    //说明拿到的楼栋和门牌的集合不为空，那么根据每一个楼栋和门牌去查询表eh_buildings表中的信息
                                    for(OrganizationSiteApartmentDTO organizationSiteApartmentDTO1 : siteDtos){
                                        //// TODO: 2018/5/14
                                        Building building = organizationProvider.findBuildingByCommunityIdAndBuildingNameWithNamespaceId(community.getId(),namespaceId,organizationSiteApartmentDTO1.getBuildingName());
                                        //进行非空校验
                                        if(building == null || "".equals(building)){
                                            //说明根据楼栋号查询不到楼栋信息，那么就不存在改楼栋，所以我们需要给前端提示信息
                                            LOGGER.error("building Non-existent, buildingName = {}",organizationSiteApartmentDTO1.getBuildingName());
                                            log.setErrorLog("building Non-existent");
                                            log.setCode(OrganizationServiceErrorCode.ERROR_BUILDING_NOT_EXIST);
                                            errorDataLogs.add(log);
                                            continue;
//                                        throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_BUILDING_NOT_EXIST,
//                                           "building Non-existent");
                                        }
                                        //接下来根据communityId和namespaceId和buildingName和apartmentName来查询eh_addresses表中的信息
                                        //// TODO: 2018/5/14
                                        Address address = addressProvider.findAddressByBuildingApartmentName(namespaceId,community.getId(),building.getName(),organizationSiteApartmentDTO1.getApartmentName());
                                        //进行非空校验
                                        if(address == null || "".equals(address)){
                                            //说明根据该楼栋名称和门牌名称以及域空间ID和项目编号查询不到该门牌信息，所以我们需要给出前端提示信息
                                            LOGGER.error("address Non-existent, address = {}", organizationSiteApartmentDTO1.getApartmentName());
                                            log.setErrorLog("address Non-existent");
                                            log.setCode(OrganizationServiceErrorCode.ERROR_APARTMENT_NOT_EXIST);
                                            errorDataLogs.add(log);
                                            continue;
//                                        throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_APARTMENT_NOT_EXIST,
//                                           "address Non-existent");
                                        }
                                        //能进行到这里说明楼栋和门牌都查询到了，那么我们将该信息封装在对象OrganizationSiteApartmentDTO中
                                        organizationSiteApartmentDTO.setBuildingId(building.getId());
                                        organizationSiteApartmentDTO.setApartmentId(address.getId());
                                        //将organizationSiteApartmentDTO对象添加到集合中
                                        siteDtosNew.add(organizationSiteApartmentDTO);
                                    }
                                    createOfficeSiteCommand.setSiteDtos(siteDtosNew);
                                    //添加到集合List<CreateOfficeSiteCommand>中
                                    officeSites.add(createOfficeSiteCommand);
                                    //向办公地点表中添加数据
                                    if(!CollectionUtils.isEmpty(officeSites)){
                                        //说明传过来的所在项目和名称以及其中的楼栋和门牌不为空，那么我们将其进行遍历
                                        for(CreateOfficeSiteCommand createOfficeSite : officeSites){
                                            //这样的话拿到的是每一个办公所在地，以及其中的楼栋和门牌
                                            //首先我们将办公地址名称和办公地点id持久化到表eh_organization_workPlaces中
                                            //创建OrganizationWorkPlaces类的对象
                                            OrganizationWorkPlaces organizationWorkPlaces = new OrganizationWorkPlaces();
                                            //将数据封装在对象中
                                            organizationWorkPlaces.setCommunityId(createOfficeSite.getCommunityId());
                                            organizationWorkPlaces.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                                            organizationWorkPlaces.setWorkplaceName(createOfficeSite.getSiteName());
                                            //将上面的organization对象中的id也封装在对象中
                                            organizationWorkPlaces.setOrganizationId(organization.getId());
                                            //调用organizationProvider中的insertIntoOrganizationWorkPlaces方法,将对象持久化到数据库
                                            organizationProvider.insertIntoOrganizationWorkPlaces(organizationWorkPlaces);
                                            //现在的情况是这样的，我们还需要进行维护一张表eh_enterprise_community_map的信息，这张表其实在标准版中是不适用的
                                            //后面的话会逐步的进行淘汰，但是现在当给eh_organization_workPlaces表中添加一调数据的同时，还需要向eh_enterprise_community_map
                                            //中添加同样的数据
                                            //// TODO: 2018/5/4
                                            //创建EnterpriseCommunityMap类的对象
                                            EnterpriseCommunityMap enterpriseCommunityMap = new EnterpriseCommunityMap();
                                            //将数据封装在对象EnterpriseCommunityMap中
                                            enterpriseCommunityMap.setCommunityId(createOfficeSite.getCommunityId());
                                            enterpriseCommunityMap.setMemberId(organization.getId());
                                            enterpriseCommunityMap.setMemberType(EnterpriseCommunityMapType.Enterprise.getCode());
                                            enterpriseCommunityMap.setMemberStatus(EnterpriseCommunityMapStatus.ACTIVE.getCode());
                                            //调用enterpriseProvider中的insertIntoEnterpriseCommunityMap(EnterpriseCommunityMap enterpriseCommunityMap)方法，将数据封装在
                                            //表eh_enterprise_community_map中
                                            enterpriseProvider.insertIntoEnterpriseCommunityMap(enterpriseCommunityMap);


                                            //在这里我们还需要维护eh_organization_community_requests这张表
                                            //创建OrganizationCommunityRequest类的对象
                                            OrganizationCommunityRequest organizationCommunityRequest = new OrganizationCommunityRequest();
                                            //将数据封装在对象OrganizationCommunityRequest对象中
                                            organizationCommunityRequest.setCommunityId(createOfficeSiteCommand.getCommunityId());
                                            organizationCommunityRequest.setMemberId(organization.getId());
                                            organizationCommunityRequest.setMemberType(EnterpriseCommunityMapType.Organization.getCode());
                                            organizationCommunityRequest.setMemberStatus(EnterpriseCommunityMapStatus.ACTIVE.getCode());
                                            //// TODO: 2018/5/22
                                            enterpriseProvider.insertIntoOrganizationCommunityRequest(organizationCommunityRequest);


                                            //接下来我们需要将对应的所在项目的楼栋和门牌也持久化到项目和楼栋门牌的关系表eh_communityAndBuilding_relationes中
                                            //首先进行遍历楼栋集合
                                            if(createOfficeSite.getSiteDtos() != null){
                                                //说明楼栋和门牌不为空，注意他是一个集合
                                                //遍历
                                                for(OrganizationSiteApartmentDTO organizationSiteApartment : createOfficeSite.getSiteDtos()){
                                                    //这样的话我们拿到的是每一个楼栋以及对应的门牌
                                                    //创建CommunityAndBuildingRelationes对象，并且将数据封装在对象中，然后持久化到数据库
                                                    CommunityAndBuildingRelationes communityAndBuildingRelationes = new CommunityAndBuildingRelationes();
                                                    communityAndBuildingRelationes.setCommunityId(createOfficeSite.getCommunityId());
                                                    communityAndBuildingRelationes.setAddressId(organizationSiteApartment.getApartmentId());
                                                    communityAndBuildingRelationes.setBuildingId(organizationSiteApartment.getBuildingId());
                                                    communityAndBuildingRelationes.setWorkplaceId(organizationWorkPlaces.getId());
                                                    //调用organizationProvider中的insertIntoCommunityAndBuildingRelationes方法，将对象持久化到数据库
                                                    organizationProvider.insertIntoCommunityAndBuildingRelationes(communityAndBuildingRelationes);
                                                }
                                            }

                                        }
                                    }
                                }

                                //向eh_organization_details表中添加数据
                                OrganizationDetail organizationDetail = new OrganizationDetail();
                                organizationDetail.setOrganizationId(organization.getId());
                                organizationDetail.setAddress(null);
                                organizationDetail.setDescription(null);
                                organizationDetail.setAvatar(null);
                                organizationDetail.setMemberRange(importEnterpriseDataDTO.getMemberRange());
                                organizationDetail.setCreateTime(organization.getCreateTime());
                                organizationDetail.setCheckinDate(null);
                                organizationDetail.setContact(importEnterpriseDataDTO.getAdminToken());
                                organizationDetail.setDisplayName(importEnterpriseDataDTO.getDisplayName());
                                organizationDetail.setPostUri(null);
                                organizationDetail.setMemberCount(0L);
                                organizationDetail.setServiceUserId(null);
                                organizationDetail.setLatitude(null);
                                organizationDetail.setContactor(importEnterpriseDataDTO.getAdminName());
                                organizationDetail.setContact(importEnterpriseDataDTO.getContact());
                                organizationProvider.createOrganizationDetail(organizationDetail);




                                //根据传进来的手机号进行校验，判断该手机号是否已经进行注册
                                //非空校验

                                if(importEnterpriseDataDTO.getAdminToken() != null && importEnterpriseDataDTO.getAdminName() != null && organization.getId() != null){
                                    //接下来创建超级管理员
                                    //创建CreateOrganizationAdminCommand类的对象
                                    CreateOrganizationAdminCommand cmdnew = new CreateOrganizationAdminCommand();
                                    //将数据封装进去
                                    cmdnew.setContactToken(importEnterpriseDataDTO.getAdminToken());
                                    cmdnew.setContactName(importEnterpriseDataDTO.getAdminName());
                                    cmdnew.setOrganizationId(organization.getId());
                                    OrganizationContactDTO organizationContactDTO = rolePrivilegeService.createOrganizationSuperAdmin(cmdnew);
                                    //查看eh_organization_members表中信息
                                    OrganizationMember organizationMember = organizationProvider.findOrganizationMemberSigned(importEnterpriseDataDTO.getAdminToken(),
                                            cmd.getNamespaceId(),OrganizationMemberGroupType.MANAGER.getCode());
                                    //将该organizationMember的id值更新到eh_organizations表中的admin_target_id字段中
                                    if(organizationMember != null){
                                        //创建Organization类的对象
                                        Organization organization1 = new Organization();
                                        //封装信息
                                        organization1.setAdminTargetId(organizationMember.getId());
                                        organization1.setId(organization.getId());
                                        //更新eh_organizations表信息
                                        organizationProvider.updateOrganizationByOrgId(organization1);
                                    }
                                }





                                /*//根据传进来的手机号进行校验，判断该手机号是否已经进行注册
                                //非空校验
                                if(importEnterpriseDataDTO.getAdminToken() != null && !"".equals(importEnterpriseDataDTO.getAdminToken())){
                                    //说明手机号已经传进来了，那么我们根据该手机号去查eh_user_identifiers表中看是否已经注册
                                    UserIdentifier userIdentifier = userProvider.getUserByToken(importEnterpriseDataDTO.getAdminToken(),namespaceId);
                                    if(userIdentifier != null){
                                        //说明已经进行注册，eh_user_identifiers表中存在记录，但是eh_organization_members表中不一定存在记录
                                        //那么还需要查询该表
                                        //// TODO: 2018/4/28
                                        OrganizationMember organizationMember = organizationProvider.findOrganizationMemberSigned(importEnterpriseDataDTO.getAdminToken(),
                                                namespaceId);
                                        if(organizationMember != null){
                                            //说明已经加入了公司，那么我们就将新建的公司的admin_target_id值更改为organizationMember中的id值
                                            //// TODO: 2018/4/28
                                            //创建Organization类的对象
                                            Organization organization1 = new Organization();
                                            //封装信息
                                            organization1.setAdminTargetId(organizationMember.getId());
                                            organization1.setId(organization.getId());
                                            //更新eh_organizations表信息
                                            organizationProvider.updateOrganizationByOrgId(organization1);
                                        }else{
                                            //// TODO: 2018/4/28 说明没有加入公司,但是已经注册了，那么我们就将其加入刚新建的公司中
                                            //创建OrganizationMember类的对象
                                            OrganizationMember organizationMember1 = new OrganizationMember();
                                            //封装信息
                                            organizationMember1.setOrganizationId(organization.getId());
                                            organizationMember1.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
                                            if(importEnterpriseDataDTO.getAdminName() != null && !"".equals(importEnterpriseDataDTO.getAdminName())){
                                                //说明管理员的名字不为空，那么封装名字
                                                organizationMember1.setContactName(importEnterpriseDataDTO.getAdminName());
                                            }
                                            organizationMember1.setContactToken(importEnterpriseDataDTO.getAdminToken());
                                            organizationMember1.setTargetType(OrganizationMemberTargetType.USER.getCode());
                                            organizationMember1.setTargetId(userIdentifier.getOwnerUid());
                                            organizationMember1.setGroupType(OrganizationMemberGroupType.MANAGER.getCode());
                                            organizationMember1.setCreatorUid(user.getId());
                                            //持久化到数据库中
                                            //// TODO: 2018/4/28
                                            organizationProvider.insertIntoOrganizationMember(organizationMember1);
                                            //更新eh_organizations表中的admin_target_id字段
                                            //创建一个Organization类的对象
                                            Organization organization2 = new Organization();
                                            //封装信息
                                            organization2.setAdminTargetId(organizationMember1.getId());
                                            organization2.setId(organization.getId());
                                            //更新eh_organizations表信息
                                            organizationProvider.updateOrganizationByOrgId(organization2);
                                        }

                                    }else{
                                        //// TODO: 2018/4/28 说明未进行注册,未进行注册也可以加入企业，所以我们还是需要查eh_organizarion_members表
                                        //// TODO: 2018/4/28
                                        OrganizationMember organizationMember = organizationProvider.findOrganizationMemberNoSigned(importEnterpriseDataDTO.getAdminToken(),namespaceId);
                                        //判断
                                        if(organizationMember != null){
                                            //说明已经加入了公司，但是没有注册信息
                                            //更新eh_organizations表中的admin_target_id字段信息
                                            //创建Organization类的对象
                                            Organization organization3 = new Organization();
                                            //封装数据
                                            organization3.setAdminTargetId(organizationMember.getId());
                                            organization3.setId(organization.getId());
                                            //更新eh_organizations表信息
                                            organizationProvider.updateOrganizationByOrgId(organization3);
                                        }else{
                                            //说明没有注册信息，也没有加入公司，那么我们就帮他加入公司，只要后面注册之后，他就是超级管理员了

                                            //创建OrganizationMember类的对象
                                            OrganizationMember organizationMember2 = new OrganizationMember();
                                            //封装信息
                                            organizationMember2.setOrganizationId(organization.getId());
                                            organizationMember2.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
                                            if(importEnterpriseDataDTO.getAdminName() != null && !"".equals(importEnterpriseDataDTO.getAdminName())){
                                                //说明管理员的名字不为空，那么封装名字
                                                organizationMember2.setContactName(importEnterpriseDataDTO.getAdminName());
                                            }
                                            organizationMember2.setContactToken(importEnterpriseDataDTO.getAdminToken());
                                            organizationMember2.setTargetType(OrganizationMemberTargetType.UNTRACK.getCode());
                                            organizationMember2.setTargetId(0L);
                                            organizationMember2.setGroupType(OrganizationMemberGroupType.MANAGER.getCode());
                                            organizationMember2.setCreatorUid(user.getId());
                                            //持久化到数据库中
                                            //// TODO: 2018/4/28
                                            organizationProvider.insertIntoOrganizationMember(organizationMember2);
                                            //更新eh_organizations表中的admin_target_id字段
                                            //创建一个Organization类的对象
                                            Organization organization4 = new Organization();
                                            //封装信息
                                            organization4.setAdminTargetId(organizationMember2.getId());
                                            organization4.setId(organization.getId());
                                            //更新eh_organizations表信息
                                            organizationProvider.updateOrganizationByOrgId(organization4);

                                        }
                                    }

                                }*/




                            }else{
                                LOGGER.error("enterpriseName has already exist, data = {}", importEnterpriseDataDTO);
                                log.setData(importEnterpriseDataDTO);
                                log.setErrorLog("enterpriseName has already exist");
                                log.setCode(OrganizationServiceErrorCode.ERROR_ORG_EXIST);
                                errorDataLogs.add(log);
                            }


                            return null;
                        });

                    }else{
                        //说明在该域空间下面不存在该项目，所以我们需要向前端给出提示错误
                        LOGGER.error("community is not exists, data = {}", importEnterpriseDataDTO);
                        log.setData(importEnterpriseDataDTO);
                        log.setErrorLog("community is not exists");
                        log.setCode(CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST);
                        errorDataLogs.add(log);
                        continue;
//                        throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST,
//                                "community is not exists");
                    }
                }
            }


        return errorDataLogs;

    }

    private void deleteOrganizationAllAdmins(Long organizationId) {
        ListServiceModuleAdministratorsCommand listCmd = new ListServiceModuleAdministratorsCommand();
        listCmd.setOwnerType(EhOrganizations.class.getSimpleName());
        listCmd.setOwnerId(0L);
        listCmd.setOrganizationId(organizationId);
        List<OrganizationContactDTO> list = rolePrivilegeService.listOrganizationAdministrators(listCmd);
        if (list != null && list.size() > 0) {
            DeleteOrganizationAdminCommand deleteCmd = ConvertHelper.convert(listCmd, DeleteOrganizationAdminCommand.class);
            for (OrganizationContactDTO organizationContactDTO : list) {
                deleteCmd.setUserId(organizationContactDTO.getTargetId());
                rolePrivilegeService.deleteOrganizationAdministrators(deleteCmd);
            }
        }
    }

    private List<ImportFileResultLog<ImportOrganizationContactDataDTO>> importOrganizationPersonnel(List<ImportOrganizationContactDataDTO> list, Long userId, ImportOrganizationPersonnelDataCommand cmd) {
        Organization org = checkOrganization(cmd.getOrganizationId());
        int namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        List<String> groupTypes = new ArrayList<>();
        groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
        groupTypes.add(OrganizationGroupType.GROUP.getCode());
        List<Organization> depts = organizationProvider.listOrganizationByGroupTypes(org.getPath() + "%", groupTypes);
        List<Organization> jobPositions = organizationProvider.listOrganizationByGroupTypes(org.getPath() + "%",
                Collections.singletonList(OrganizationGroupType.JOB_POSITION.getCode()));
        List<Organization> jobLevels = organizationProvider.listOrganizationByGroupTypes(org.getPath() + "%",
                Collections.singletonList(OrganizationGroupType.JOB_LEVEL.getCode()));

        Map<String, Organization> jobPositionMap = this.convertOrgListToStrMap(jobPositions);
        Map<String, Organization> deptMap = this.convertDeptListToStrMap(depts);
        Map<String, Organization> jobLevelMap = this.convertOrgListToStrMap(jobLevels);

        List<ImportFileResultLog<ImportOrganizationContactDataDTO>> errorDataLogs = new ArrayList<>();

        outer:
        for (ImportOrganizationContactDataDTO data : list) {
            ImportFileResultLog<ImportOrganizationContactDataDTO> log = new ImportFileResultLog<>(OrganizationServiceErrorCode.SCOPE);
            if (StringUtils.isEmpty(data.getContactName())) {
                LOGGER.warn("Organization member contactName is null. data = {}", data);
                log.setData(data);
                log.setErrorLog("Organization member contactName is null");
                log.setCode(OrganizationServiceErrorCode.ERROR_CONTACTNAME_ISNULL);
                errorDataLogs.add(log);
                continue outer;
            }

            if (StringUtils.isEmpty(data.getContactToken())) {
                LOGGER.warn("Organization member contactToken is null. data = {}", data);
                log.setData(data);
                log.setErrorLog("Organization member contactToken is null");
                log.setCode(OrganizationServiceErrorCode.ERROR_CONTACTTOKEN_ISNULL);
                errorDataLogs.add(log);
                continue outer;
            }

            AddOrganizationPersonnelCommand memberCommand = new AddOrganizationPersonnelCommand();

            memberCommand.setOrganizationId(cmd.getOrganizationId());
            memberCommand.setContactToken(data.getContactToken());
            memberCommand.setContactName(data.getContactName());
            Byte gender = 0;
            if (!StringUtils.isEmpty(data.getGender())) {
                if (data.getGender().trim().equals("男")) {
                    gender = 1;
                } else if (data.getGender().trim().equals("女")) {
                    gender = 2;
                }
            }
            memberCommand.setGender(gender);

            if (!StringUtils.isEmpty(data.getOrgnaizationPath())) {
                String[] deptStrArr = data.getOrgnaizationPath().split(",");
                List<Long> departmentIds = new ArrayList<>();
                for (String deptName : deptStrArr) {
                    Organization dept = deptMap.get(deptName.trim());
                    if (null == dept) {
                        LOGGER.debug("Organization member department Non-existent. departmentName = {}", deptName);
                        log.setData(data);
                        log.setErrorLog("Organization member department Non-existent.");
                        log.setCode(OrganizationServiceErrorCode.ERROR_ORG_NOT_EXIST);
                        errorDataLogs.add(log);
                        continue outer;
                    }
                    departmentIds.add(dept.getId());
                }
                memberCommand.setDepartmentIds(departmentIds);
            }

            if (!StringUtils.isEmpty(data.getJobPosition())) {
                String[] jobPositionStrArr = data.getJobPosition().split(",");
                List<Long> jobPositionIds = new ArrayList<>();
                for (String jobPositionName : jobPositionStrArr) {
                    Organization jobPosition = jobPositionMap.get(jobPositionName.trim());
                    if (null == jobPosition) {
                        LOGGER.debug("Organization member jobPosition Non-existent. jobPositionName = {}", jobPositionName);
                        log.setData(data);
                        log.setErrorLog("Organization member jobPosition Non-existent.");
                        log.setCode(OrganizationServiceErrorCode.ERROR_ORG_NOT_EXIST);
                        errorDataLogs.add(log);
                        continue outer;
                    }
                    jobPositionIds.add(jobPosition.getId());
                }
                memberCommand.setJobPositionIds(jobPositionIds);
            }

            if (!StringUtils.isEmpty(data.getJobLevel())) {
                String[] jobLevelStrArr = data.getJobLevel().split(",");
                List<Long> jobLevelIds = new ArrayList<>();
                for (String jobLevelName : jobLevelStrArr) {
                    Organization jobLevel = jobLevelMap.get(jobLevelName);
                    if (null == jobLevel) {
                        LOGGER.debug("Organization member jobLevel Non-existent. jobLevelName = {}", jobLevelName);
                        log.setData(data);
                        log.setErrorLog("Organization member jobLevel Non-existent.");
                        log.setCode(OrganizationServiceErrorCode.ERROR_ORG_NOT_EXIST);
                        errorDataLogs.add(log);
                        continue outer;
                    }
                    jobLevelIds.add(jobLevel.getId());
                }
                memberCommand.setJobLevelIds(jobLevelIds);
            }

            VerifyPersonnelByPhoneCommand verifyCommand = new VerifyPersonnelByPhoneCommand();
            verifyCommand.setEnterpriseId(org.getId());
            verifyCommand.setNamespaceId(namespaceId);
            verifyCommand.setPhone(memberCommand.getContactToken());

            VerifyPersonnelByPhoneCommandResponse verifyRes = null;
            try {
                verifyRes = this.verifyPersonnelByPhone(verifyCommand);
            } catch (RuntimeErrorException e) {
                LOGGER.debug(e.getMessage());
                log.setData(data);
                log.setErrorLog(e.getMessage());
                log.setCode(e.getErrorCode());
                log.setScope(e.getErrorScope());
                errorDataLogs.add(log);
                continue outer;
            }

            if (null != verifyRes && null != verifyRes.getDto()) {
                memberCommand.setTargetId(verifyRes.getDto().getTargetId());
                memberCommand.setTargetType(verifyRes.getDto().getTargetType());
            }

            this.addOrganizationPersonnel(memberCommand);
        }
        return errorDataLogs;
    }

    private Map<String, Organization> convertDeptListToStrMap(List<Organization> depts) {
        Map<String, Organization> map = new HashMap<String, Organization>();
        Map<Long, String> temp = new HashMap<Long, String>();

        if (null == depts) {
            return map;
        }
        for (Organization dept : depts) {
            temp.put(dept.getId(), dept.getName());
        }
        for (Organization dept : depts) {
            String path = dept.getPath();
            String[] pathArr = path.split("/");
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (String idStr : pathArr) {
                if (i > 1 && !"".equals(idStr)) {
                    Long id = Long.valueOf(idStr);
                    String orgName = temp.get(Long.valueOf(id));
                    if (StringUtils.isEmpty(orgName)) {
                        sb = new StringBuilder();
                        break;
                    }
                    if (sb.toString().isEmpty()) {
                        sb.append(orgName);
                    } else {
                        sb.append("-").append(orgName);
                    }
                }
                i++;
            }

            if (!sb.toString().isEmpty()) {
                map.put(sb.toString(), dept);
            }

        }
        return map;
    }

    private Map<String, Organization> convertOrgListToStrMap(List<Organization> depts) {
        Map<String, Organization> map = new HashMap<String, Organization>();

        if (null == depts) {
            return map;
        }
        for (Organization dept : depts) {
            map.put(dept.getName(), dept);
        }

        return map;
    }

    /**
     * 修改用户 通讯录的对应信息
     *
     * @param member
     */
    private void updateMemberUser(OrganizationMember member) {
        this.dbProvider.execute((TransactionStatus status) -> {
            member.setTargetType(OrganizationMemberTargetType.USER.getCode());
            organizationProvider.updateOrganizationMember(member);

            if (null != member.getDetailId()) {
                OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(member.getDetailId());
                detail.setTargetId(member.getTargetId());
                detail.setTargetType(member.getTargetType());
                organizationProvider.updateOrganizationMemberDetails(detail, detail.getId());

                User user = userProvider.findUserById(member.getTargetId());
                user.setNickName(detail.getContactName());
                if (StringUtils.isEmpty(user.getAvatar())) {
                    user.setAvatar(detail.getAvatar());
                }
                if (StringUtils.isEmpty(user.getAvatar())) {
                    user.setAvatar(configurationProvider.getValue(UserContext.getCurrentNamespaceId(), "user.avatar.undisclosured.url", ""));
                }

                userProvider.updateUser(user);
            }
            return null;
        });
    }


    /**
     * 修改通讯录人员的状态
     *
     * @param operatorUid
     * @param member
     */
    private void updateEnterpriseContactStatus(Long operatorUid, OrganizationMember member) {
        Organization organization = this.checkOrganization(member.getOrganizationId());

        List<String> groupTypes = new ArrayList<String>();

        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());

        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_GROUP.getCode()).enter(() -> {
            List<Organization> departments = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "/%", groupTypes);

            //退出企业  部门下的记录 都删除掉
            for (Organization department : departments) {
                OrganizationMember organizationMember = organizationProvider.findOrganizationMemberByOrgIdAndToken(member.getContactToken(), department.getId());
                if (null != organizationMember) {
                    organizationProvider.deleteOrganizationMemberById(organizationMember.getId());
                }
            }

            // 公司下的记录改状态
            if (null != member) {
                organizationProvider.updateOrganizationMember(member);
            }

            // 同步userOrganization
            if (member.getGroupType().equals(OrganizationGroupType.ENTERPRISE.getCode())) {
                this.createOrUpdateUserOrganization(member);
            }
            return null;

        });
        userSearcher.feedDoc(member);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Enterprise contact is deleted(active), operatorUid=" + operatorUid + ", contactId=" + member.getTargetId()
                    + ", enterpriseId=" + member.getOrganizationId() + ", status=" + member.getStatus() + ", removeFromDb=" + member.getStatus());
        }
    }

    /**
     * 从企业通讯录里面删除人员
     *
     * @param operatorUid
     * @param member
     */
    private void deleteEnterpriseContactStatus(Long operatorUid, OrganizationMember member) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_GROUP.getCode()).enter(() -> {
            //modify by wh  2016-10-12 拒绝后置为拒绝状态而非删除
            member.setStatus(OrganizationMemberStatus.REJECT.getCode());
            this.organizationProvider.updateOrganizationMember(member);
            //this.organizationProvider.deleteOrganizationMemberById(member.getId());
            //更新user_organization表的记录
            UserOrganizations userOrganization = this.userOrganizationProvider.findUserOrganizations(namespaceId, member.getOrganizationId(), member.getTargetId());
            if(userOrganization != null) {
                this.userOrganizationProvider.rejectUserOrganizations(userOrganization);
            }

            return null;
        });

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Enterprise contact is deleted(active), operatorUid=" + operatorUid + ", contactId=" + member.getTargetId()
                    + ", enterpriseId=" + member.getOrganizationId() + ", status=" + member.getStatus() + ", removeFromDb=" + member.getStatus());
        }
    }

    @Override
    public List<OrganizationMemberDTO> listOrganizationMemberDTOs(Long orgId, List<Long> memberUids) {

        List<OrganizationMember> organizationMembers = this.organizationProvider.listOrganizationMembers(orgId, memberUids);
        Organization org = this.checkOrganization(orgId);
        return this.convertDTO(organizationMembers, org);

    }

    /**
     * 补充返回用户信息，部门 角色
     *
     * @param organizationMembers
     * @param org
     * @return
     */
    public List<OrganizationMemberDTO> convertDTO(List<OrganizationMember> organizationMembers, Organization org) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        Long orgId = null;

        if (org.getGroupType().equals(OrganizationGroupType.DEPARTMENT.getCode())) {
            orgId = org.getDirectlyEnterpriseId();
        } else {
            orgId = org.getId();
        }
        Long startTime = System.currentTimeMillis();

        //用户角色目前不用了，暂时注释掉 add by sfyan 20170427
//		List<Role> roles= aclProvider.getRolesByOwner(Namespace.DEFAULT_NAMESPACE, AppConstants.APPID_PARK_ADMIN, EntityType.ORGANIZATIONS.getCode(), null);
//
//		List<Role> orgRoles = aclProvider.getRolesByOwner(namespaceId, AppConstants.APPID_PARK_ADMIN, EntityType.ORGANIZATIONS.getCode(), null);
//		if(null != roles){
//			roles.addAll(orgRoles);
//		}
//		roles.addAll(aclProvider.getRolesByOwner(namespaceId, AppConstants.APPID_PARK_ADMIN, EntityType.ORGANIZATIONS.getCode(), orgId));
//	    Map<Long, Role> roleMap =  this.convertOrganizationRoleMap(roles);

        Long endTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Track: listOrganizationPersonnels:convertDTO: get role elapse:{}", endTime - startTime);
        }


        Long directlyOrgId = orgId;

        OrganizationDTO orgDTO = ConvertHelper.convert(org, OrganizationDTO.class);

        List<String> groupTypes = new ArrayList<>();
        //只过滤onNode的记录，排除掉belongTo在公司下的记录
        //OrganizationGroupType.ENTERPRISE的记录都是belongTo的记录
        groupTypes.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
        groupTypes.add(OrganizationGroupType.GROUP.getCode());

        Organization directlyEnterprise = checkOrganization(directlyOrgId);

        return organizationMembers.stream().map((c) -> {
            Long organizationId = directlyOrgId;
            if (!StringUtils.isEmpty(c.getInitial())) {
                c.setInitial(c.getInitial().replace("~", "#"));
            }

            OrganizationMemberDTO dto = ConvertHelper.convert(c, OrganizationMemberDTO.class);

            Long startTime1_1 = System.currentTimeMillis();
            if (OrganizationGroupType.fromCode(org.getGroupType()) == OrganizationGroupType.DEPARTMENT || OrganizationGroupType.fromCode(org.getGroupType()) == OrganizationGroupType.ENTERPRISE || OrganizationGroupType.fromCode(org.getGroupType()) == OrganizationGroupType.GROUP) {

//				dto.setGroups(this.getOrganizationMemberGroups(OrganizationGroupType.GROUP, dto.getContactToken(), directlyEnterprise.getPath())); //已经跟部门子公司放入一起

                List<OrganizationDTO> departments = new ArrayList<>();
//				if(OrganizationGroupType.fromCode(org.getGroupType()) == OrganizationGroupType.DEPARTMENT){
//					departments.add(orgDTO);
//				}
                departments.addAll(this.getOrganizationMemberGroups(groupTypes, dto.getContactToken(), directlyEnterprise.getPath()));
                departments = departments.stream().map(r -> {
                    String[] pathStrs = r.getPath().split("/");
                    String pathName = "";
                    for (String idStr : pathStrs) {
                        if (!"".equals(idStr)) {
                            Long id = Long.valueOf(idStr);
                            Organization o = organizationProvider.findOrganizationById(id);
                            if (id.equals(organizationId)) {
                                pathName = "start";
                            } else if ("start".equals(pathName)) {
                                pathName = null != o ? o.getName() : "未知";
                            } else if (!"".equals(pathName)) {
                                pathName += null != o ? "-" + o.getName() : "-未知";
                            }
                        }
                    }
                    if ("start".equals(pathName) && directlyEnterprise.getParentId() != 0L) {
                        r.setPathName(directlyEnterprise.getName());
                    } else {
                        r.setPathName(pathName);
                    }

                    return r;
                }).collect(Collectors.toList());
                dto.setDepartments(departments);
            }
            Long endTime1_1 = System.currentTimeMillis();

            //岗位
            dto.setJobPositions(this.getOrganizationMemberGroups(OrganizationGroupType.JOB_POSITION, dto.getContactToken(), directlyEnterprise.getPath()));

            //职级
            dto.setJobLevels(this.getOrganizationMemberGroups(OrganizationGroupType.JOB_LEVEL, dto.getContactToken(), directlyEnterprise.getPath()));


            if (OrganizationMemberTargetType.USER.getCode().equals(dto.getTargetType())) {
                User user = userProvider.findUserById(dto.getTargetId());
                if (null != user) {
                    dto.setAvatar(contentServerService.parserUri(user.getAvatar(), EntityType.USER.getCode(), user.getId()));
                    dto.setNickName(dto.getNickName());
                }
            }

            if (c.getIntegralTag4() != null && c.getIntegralTag4() == 1) {
                dto.setContactToken(null);
            }

            if (null == VisibleFlag.fromCode(c.getVisibleFlag())) {
                dto.setVisibleFlag(VisibleFlag.SHOW.getCode());
            }

            Long startTime2_2 = System.currentTimeMillis();
            /**
             * 补充用户角色
             */
			/*if(c.getTargetType().equals(OrganizationMemberTargetType.USER.getCode())){
				List<RoleAssignment> resources = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), organizationId, EntityType.USER.getCode(), c.getTargetId());
				if(null != resources && 0 != resources.size()){
					List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
					for (RoleAssignment resource : resources) {
						Role role = roleMap.get(resource.getRoleId());
						if(null != role)
							roleDTOs.add(ConvertHelper.convert(role, RoleDTO.class));
					}
					dto.setRoles(roleDTOs);
				}
			}*/ // 暂时不需要

            Long endTime2_2 = System.currentTimeMillis();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Track: listOrganizationPersonnels:convertDTO:map:{}: get organizatin elapse:{}, get organization elapse:{}, total elapse:{}", c.getContactToken(), endTime1_1 - startTime1_1, endTime2_2 - startTime2_2);
            }
            return dto;
        }).collect(Collectors.toList());

    }

    @Override
    public List<Long> getIncludeOrganizationIdsByUserId(Long userId, Long organizationId) {
        List<Long> orgnaizationIds = new ArrayList<>();

        List<String> groupTypes = new ArrayList<>();
        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
        groupTypes.add(OrganizationGroupType.GROUP.getCode());
        groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
        groupTypes.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
        List<OrganizationDTO> orgs = getOrganizationMemberGroups(groupTypes, userId, organizationId);
        for (OrganizationDTO dto : orgs) {
            addPathOrganizationId(dto.getPath(), orgnaizationIds);
        }

        //Added by janson，这个接口原本的意思是拿用户所有的公司，但是在 getOrganizationMemberGroups 是拿用户自己关联的子公司，所以这里把本公司加上
        orgnaizationIds.add(organizationId);

        return orgnaizationIds;
    }


    private List<Long> getChildOrganizationIds(Long organizationId, List<String> groupTypes) {
        List<Long> orgnaizationIds = new ArrayList<>();
        Organization org = checkOrganization(organizationId);
        List<Organization> organizations = organizationProvider.listOrganizationByGroupTypes(org.getPath() + "/%", groupTypes);
        for (Organization organization : organizations) {
            orgnaizationIds.add(organization.getId());
        }
        return orgnaizationIds;
    }

    private void addPathOrganizationId(String path, List<Long> orgnaizationIds) {
        String[] idStrs = path.split("/");
        for (String idStr : idStrs) {
            if (!StringUtils.isEmpty(idStr)) {
                Long id = Long.valueOf(idStr);
                orgnaizationIds.add(id);
            }
        }
    }

    private void addPathOrganizationId(String path, Set<Long> orgnaizationIds) {
        List<Long> orgIds = new ArrayList<>();
        orgIds.addAll(orgnaizationIds);
        this.addPathOrganizationId(path, orgIds);
        orgnaizationIds.addAll(orgIds);
    }

    @Override
    public List<OrganizationDTO> getOrganizationMemberGroups(List<String> groupTypes, Long userId, Long organizationId) {
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode());
        Organization organization = organizationProvider.findOrganizationById(organizationId);
        if (null != userIdentifier && null != organization) {
            return getOrganizationMemberGroups(groupTypes, userIdentifier.getIdentifierToken(), organization.getPath());
        }
        return new ArrayList<>();
    }

    @Override
    public List<OrganizationDTO> getOrganizationMemberGroups(List<String> groupTypes, String token, String orgPath) {
        List<OrganizationDTO> groups = new ArrayList<OrganizationDTO>();
//		List<Organization> depts = organizationProvider.listOrganizationByGroupTypes(orgPath+"/%", groupTypes);
//		List<Long> deptIds = new ArrayList<Long>();
//		for (Organization organization : depts) {
//			deptIds.add(organization.getId());
//		}
        List<OrganizationMember> members = organizationProvider.listOrganizationMemberByPath(orgPath, groupTypes, token);
        for (OrganizationMember member : members) {
            Organization group = organizationProvider.findOrganizationById(member.getOrganizationId());
            //comment by janson, 这段代码看不懂，为何只留下非顶级公司的组？
            if (null != group && OrganizationStatus.fromCode(group.getStatus()) == OrganizationStatus.ACTIVE && group.getParentId() != 0L) {
                groups.add(ConvertHelper.convert(group, OrganizationDTO.class));
            }
        }

        return groups;
    }

    @Override
    public List<OrganizationDTO> getOrganizationMemberGroups(OrganizationGroupType organizationGroupType, String token, String orgPath) {
        List<String> groupTypes = new ArrayList<>();
        groupTypes.add(organizationGroupType.getCode());
        return getOrganizationMemberGroups(groupTypes, token, orgPath);
    }

    @Override
    public List<OrganizationDTO> getOrganizationMemberGroups(OrganizationGroupType organizationGroupType, Long userId, Long organizationId) {
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode());
        Organization organization = organizationProvider.findOrganizationById(organizationId);
        if (null != userIdentifier && null != organization) {
            return this.getOrganizationMemberGroups(organizationGroupType, userIdentifier.getIdentifierToken(), organization.getPath());
        }
        return new ArrayList<>();
    }

//  HEAD
//    public List<Object> getOrganizationMemberIdAndVisibleFlag(String contactToken, Long organizationId) {
//        OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndToken(contactToken, organizationId);
//        if(member == null)
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//                    "Invalid parameter, organizationId cannot be found");
//        List<Object> result = new ArrayList<>();
//        result.add(member.getId());
//=======
    public Byte getOrganizationMemberVisibleFlag(String contactToken, Long organizationId) {
        OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndToken(contactToken, organizationId);
        if (member == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter, organizationId cannot be found");
/*        List<Object> result = new ArrayList<>();
        result.add(member.getId());*/
//  master
        Byte visibleFlag;
        if (null == VisibleFlag.fromCode(member.getVisibleFlag())) {
            visibleFlag = VisibleFlag.SHOW.getCode();
        } else {
            visibleFlag = member.getVisibleFlag();
        }
//        result.add(visibleFlag);
        return visibleFlag;
    }

    @Override
    public CommunityOrganizationTreeResponse listCommunityOrganizationTree(ListCommunityOrganizationTreeCommand cmd) {
        CommunityOrganizationTreeResponse response = new CommunityOrganizationTreeResponse();

        List<CommunityDTO> communityDTOs = this.listAllChildrenOrganizationCoummunities(cmd.getOrganizationId());

        List<CommunityOrganizationTreeDTO> treeDTOs = new ArrayList<>();
        if (communityDTOs != null && communityDTOs.size() > 0) {
            for (CommunityDTO communityDTO : communityDTOs) {
                CommunityOrganizationTreeDTO treeDTO = new CommunityOrganizationTreeDTO();
                treeDTO.setId(communityDTO.getId());
                treeDTO.setName(communityDTO.getName());
                ListEnterprisesCommand listEnterprisesCmd = new ListEnterprisesCommand();
                listEnterprisesCmd.setQryAdminRoleFlag(false);
                listEnterprisesCmd.setCommunityId(communityDTO.getId());
                ListEnterprisesCommandResponse enterprisesResponse = this.listEnterprises(listEnterprisesCmd);
                if (enterprisesResponse != null && enterprisesResponse.getDtos() != null) {
                    treeDTO.setOrganizations(enterprisesResponse.getDtos());
                }
                treeDTOs.add(treeDTO);
            }
        }
        response.setCommunities(treeDTOs);
        return response;
    }
    
    @Override
    public OrganizationMenuResponse openListAllChildrenOrganizations(OpenListAllChildrenOrganizationsCommand cmd){

        Organization org = checkOrganization(cmd.getId());

		AppNamespaceMapping appNamespaceMapping = appNamespaceMappingProvider.findAppNamespaceMappingByAppKey(cmd.getAppKey());
		if (appNamespaceMapping == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
					"not exist app namespace mapping");
		}
		
		if(!org.getNamespaceId().equals(appNamespaceMapping.getNamespaceId())){
			return null;
		}
    	return listAllChildrenOrganizationMenus(cmd.getId(), cmd.getGroupTypes(), cmd.getNaviFlag());
    }

    @Override
    public UserAuthenticationOrganizationDTO createUserAuthenticationOrganization(CreateUserAuthenticationOrganizationCommand cmd) {
        UserAuthenticationOrganization existsAuth = this.organizationProvider.getUserAuthenticationOrganization(cmd.getOrganizationId(), cmd.getNamespaceId());
        if (existsAuth != null) {
            existsAuth.setStatus(Status.INACTIVE.getCode());
            this.organizationProvider.updateUserAuthenticationOrganization(existsAuth);
        }

        UserAuthenticationOrganization newUserAuth = ConvertHelper.convert(cmd, UserAuthenticationOrganization.class);
        newUserAuth.setStatus(Status.ACTIVE.getCode());
        newUserAuth.setCreateTime(new Timestamp(new Date().getTime()));
        newUserAuth.setCreatorUid(UserContext.currentUserId());
        this.organizationProvider.createUserAuthenticationOrganization(newUserAuth);
        return ConvertHelper.convert(newUserAuth, UserAuthenticationOrganizationDTO.class);
    }

    @Override
    public UserAuthenticationOrganizationDTO getUserAuthenticationOrganization(GetUserAuthenticationOrganizationCommand cmd) {
        UserAuthenticationOrganization existsAuth = this.organizationProvider.getUserAuthenticationOrganization(cmd.getOrganizationId(), cmd.getNamespaceId());
        if (existsAuth != null) {
            return ConvertHelper.convert(existsAuth, UserAuthenticationOrganizationDTO.class);
        }else {
            UserAuthenticationOrganizationDTO dto = ConvertHelper.convert(cmd, UserAuthenticationOrganizationDTO.class);
            dto.setAuthFlag(com.everhomes.rest.common.TrueOrFalseFlag.TRUE.getCode());
            return dto;
        }
    }


    @Override
    public OrganizationMenuResponse listAllChildrenOrganizationMenus(Long id, List<String> groupTypes, Byte naviFlag) {
        Long startTime = System.currentTimeMillis();
        if (null == naviFlag) {
            naviFlag = OrganizationNaviFlag.SHOW_NAVI.getCode();
        }

        OrganizationMenuResponse res = new OrganizationMenuResponse();

        Organization org = this.checkOrganization(id);

        if (null == org) {
            return res;
        }

        List<Organization> orgs = organizationProvider.listOrganizationByGroupTypes(org.getPath() + "/%", groupTypes);

        OrganizationDTO dto = ConvertHelper.convert(org, OrganizationDTO.class);

        if (0 == orgs.size()) {
            res.setOrganizationMenu(dto);
            return res;
        }



        List<OrganizationDTO> rganizationDTOs = new ArrayList<OrganizationDTO>();
        for (Organization organization : orgs) {
            OrganizationDTO orgDto = ConvertHelper.convert(organization, OrganizationDTO.class);
            //机构经理
            orgDto.setManagers(getOrganizationManagers(orgDto.getId()));
            //把机构的入住园区加入
            orgDto = processOrganizationCommunity(orgDto);
            if (OrganizationNaviFlag.fromCode(naviFlag) == OrganizationNaviFlag.HIDE_NAVI) {
                if (OrganizationNaviFlag.fromCode(organization.getShowFlag()) == OrganizationNaviFlag.SHOW_NAVI) {
                    rganizationDTOs.add(orgDto);
                }
            } else {
                rganizationDTOs.add(orgDto);
            }
        }

        Long startTime1 = System.currentTimeMillis();
        //:todo 递归
        dto = this.getOrganizationMenu(rganizationDTOs, dto);
        dto = processOrganizationCommunity(dto);
        res.setOrganizationMenu(dto);
        Long endTime = System.currentTimeMillis();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Track: listAllChildrenOrganizationMenus: get tree elapse:{},  total elapse:{}", endTime - startTime1, endTime - startTime);
        }

        return res;
    }

    private OrganizationDTO processOrganizationCommunity(OrganizationDTO organization) {
        OrganizationCommunityRequest resquest = organizationProvider.getOrganizationCommunityRequestByOrganizationId(organization.getId());
        if (null != resquest) {
            Community community = communityProvider.findCommunityById(resquest.getCommunityId());
            organization.setCommunityId(resquest.getCommunityId());
            if (null != community) {
                organization.setCommunityName(community.getName());
            }
        }
        return organization;
    }

    @Override
    public ListOrganizationsCommandResponse listChildrenOrganizations(Long id, List<String> groupTypes) {
        return this.listChildrenOrganizations(id, groupTypes, null);
    }

    @Override
    public ListOrganizationsCommandResponse listChildrenOrganizations(Long id,
                                                                      List<String> groupTypes, String keywords) {
        ListOrganizationsCommandResponse res = new ListOrganizationsCommandResponse();
        Organization org = this.checkOrganization(id);
        List<Organization> orgs = organizationProvider.listOrganizationByGroupTypes(org.getId(), groupTypes, keywords);
        if (0 == orgs.size()) {
            return res;
        }
        res.setDtos(this.convertOrg(orgs, org));
        return res;
    }

    @Override
    public ListOrganizationsCommandResponse listAllChildrenOrganizations(Long id, List<String> groupTypes) {
        ListOrganizationsCommandResponse res = new ListOrganizationsCommandResponse();
        Organization org = this.checkOrganization(id);
        List<Organization> orgs = organizationProvider.listOrganizationByGroupTypes(org.getPath() + "/%", groupTypes);
        if (0 == orgs.size()) {
            return res;
        }
        res.setDtos(this.convertOrg(orgs, org));
        return res;
    }


    @Override
    public List<Organization> getSyncDatas(CrossShardListingLocator locator) {
//		int pageSize = 200;

//		List<OrganizationCommunityRequest> requests = organizationProvider.queryOrganizationCommunityRequests(locator, pageSize, new ListingQueryBuilderCallback() {
//			@Override
//			public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
//					SelectQuery<? extends Record> query) {
//				query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_STATUS.ne(OrganizationCommunityRequestStatus.INACTIVE.getCode()));
//		        query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_TYPE.eq(OrganizationCommunityRequestType.Organization.getCode()));
//				return query;
//			}
//		});
//
//		return requests.stream().map((r)->{
//			Organization organization = organizationProvider.findOrganizationById(r.getMemberId());
//			if(null != organization){
//				OrganizationDetail detail = organizationProvider.findOrganizationDetailByOrganizationId(organization.getId());
//
//				organization.setCommunityId(r.getCommunityId());
//				if(null != detail)
//					organization.setDescription(detail.getDescription());
//			}
//			return organization;
//		}).collect(Collectors.toList());
        List<String> groupTypes = new ArrayList<String>();
        groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
        List<Organization> organizations = organizationProvider.listOrganizationByGroupTypes(0L, groupTypes);

        List<Organization> orgs = organizations.stream().map((r) -> {
            OrganizationDetail detail = organizationProvider.findOrganizationDetailByOrganizationId(r.getId());
            if (null != detail)
                r.setDescription(detail.getDescription());

            // 把企业所在的小区信息放到eh_organization_community_requests表，从eh_organizations表删除掉，以免重复 by lqs 20160512
//			OrganizationCommunityRequest request = organizationProvider.getOrganizationCommunityRequestByOrganizationId(r.getId());
//			if(request != null)
//				r.setCommunityId(request.getCommunityId());
            return r;
        }).collect(Collectors.toList());

        return orgs;

    }

    private List<OrganizationDTO> convertOrg(List<Organization> orgs, Organization org) {
        Long orgId = null;
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if (org.getGroupType().equals(OrganizationGroupType.ENTERPRISE.getCode())) {
            orgId = org.getId();
        } else {
            orgId = org.getDirectlyEnterpriseId();
            org = this.checkOrganization(orgId);
        }

        List<String> groupTypes = new ArrayList<String>();
        groupTypes.add(OrganizationGroupType.GROUP.getCode());
        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
        groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());

        List<Organization> depts = organizationProvider.listOrganizationByGroupTypes(org.getPath() + "/%", groupTypes);

        depts.add(org);

        Map<Long, Organization> deptMaps = this.convertDeptListToMap(depts);

        List<Role> roles = aclProvider.getRolesByOwner(Namespace.DEFAULT_NAMESPACE, AppConstants.APPID_PARK_ADMIN, EntityType.ORGANIZATIONS.getCode(), null);

        roles.addAll(aclProvider.getRolesByOwner(namespaceId, AppConstants.APPID_PARK_ADMIN, EntityType.ORGANIZATIONS.getCode(), orgId));

//	    Map<Long, Role> roleMap =  this.convertOrganizationRoleMap(roles);

        Long ownerId = orgId;
        List<OrganizationDTO> rganizationDTOs = orgs.stream().map(r -> {
            OrganizationDTO dto = ConvertHelper.convert(r, OrganizationDTO.class);
            //把机构的入住园区加入
            dto = processOrganizationCommunity(dto);
            if (OrganizationGroupType.fromCode(dto.getGroupType()) == OrganizationGroupType.ENTERPRISE) {
                OrganizationDetail enterprise = organizationProvider.findOrganizationDetailByOrganizationId(dto.getId());
                if (null != enterprise) {
                    dto.setAddress(enterprise.getAddress());
                }
                // return dto;
            }
            Organization depart = deptMaps.get(dto.getParentId());
//			List<RoleAssignment> resources = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), ownerId, EntityType.ORGANIZATIONS.getCode(), dto.getId());
            if (null != depart) dto.setParentName(depart.getName());
            //暂时屏蔽机构角色
//			if(null != resources && resources.size() > 0){
//				List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
//				for (RoleAssignment resource : resources) {
//					Role role = roleMap.get(resource.getRoleId());
//					if(null != role)
//						roleDTOs.add(ConvertHelper.convert(role, RoleDTO.class));
//				}
//				dto.setRoles(roleDTOs);
//			}

            //机构经理
            dto.setManagers(getOrganizationManagers(r.getId()));
            return dto;
        }).collect(Collectors.toList());

        return rganizationDTOs;
    }

    @Override
    public List<OrganizationManagerDTO> listOrganizationManagers(ListOrganizationManagersCommand cmd) {
        checkOrganization(cmd.getOrganizationId());
        return this.getOrganizationManagers(Collections.singletonList(cmd.getOrganizationId()));
    }

    @Override
    public List<OrganizationManagerDTO> listOrganizationAllManagers(ListOrganizationManagersCommand cmd) {
        Organization org = checkOrganization(cmd.getOrganizationId());
        List<String> types = new ArrayList<>();
//        types.add(OrganizationGroupType.GROUP.getCode());
//        types.add(OrganizationGroupType.DEPARTMENT.getCode());
//        List<Organization> organizations = organizationProvider.listOrganizationByGroupTypes(cmd.getOrganizationId(), types);
//        List<Long> organizationIds = new ArrayList<>();
//        for (Organization organization : organizations) {
//            organizationIds.add(organization.getId());
//        }
//        return this.getOrganizationManagers(organizationIds);
        types.add(OrganizationGroupType.MANAGER.getCode());
        List<OrganizationMember> organizationMembers = organizationProvider.listOrganizationMemberByPath(null, org.getPath(), types, null, new CrossShardListingLocator(), 1000000);
        List<OrganizationManagerDTO> organizationManagerDTOs = organizationMembers.stream().map(r -> {
            return ConvertHelper.convert(r, OrganizationManagerDTO.class);
        }).collect(Collectors.toList());
        return organizationManagerDTOs;
    }

    @Override
    public List<OrganizationManagerDTO> listModuleOrganizationManagers(ListOrganizationByModuleIdCommand cmd) {
        List<OrganizationDTO> organizations = listOrganizationsByModuleId(cmd);
        List<Long> organizationIds = new ArrayList<>();
        for (OrganizationDTO organization : organizations) {
            organizationIds.add(organization.getId());
        }
        return this.getOrganizationManagers(organizationIds);
    }

    /**
     * 获取机构经理
     *
     * @param organizationId
     * @return
     */
    private List<OrganizationManagerDTO> getOrganizationManagers(Long organizationId) {
        List<Long> organizationIds = new ArrayList<>();
        organizationIds.add(organizationId);
        return this.getOrganizationManagers(organizationIds);
    }

    /**
     * 获取机构集合经理
     *
     * @param organizationIds
     * @return
     */
    @Override
    public List<OrganizationManagerDTO> getOrganizationManagers(List<Long> organizationIds) {
//        List<OrganizationManagerDTO> dtos = new ArrayList<>();
//        //机构经理
//        List<String> types = new ArrayList<>();
//        types.add(OrganizationGroupType.MANAGER.getCode());
//        List<Long> managerGroupIds = new ArrayList<>();
//        for (Long organizationId : organizationIds) {
//            List<Organization> managerGroups = organizationProvider.listOrganizationByGroupTypes(organizationId, types);
//            if (0 < managerGroups.size()) {
//                managerGroupIds.add(managerGroups.get(0).getId());
//            }
//        }
//
//        if (0 < managerGroupIds.size()) {
//            List<OrganizationMember> members = organizationProvider.getOrganizationMemberByOrgIds(managerGroupIds, new ListingQueryBuilderCallback() {
//                @Override
//                public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
//                    query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
//                    query.addGroupBy(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN);
//                    return query;
//                }
//            });
//            for (OrganizationMember member : members) {
//                OrganizationManagerDTO managerDTO = ConvertHelper.convert(member, OrganizationManagerDTO.class);
//                managerDTO.setMemberId(member.getId());
//                dtos.add(managerDTO);
//            }
//        }
//        return dtos;

        List<OrganizationManagerDTO> dtos = new ArrayList<>();
        //机构经理
        List<String> types = new ArrayList<>();
        types.add(OrganizationGroupType.MANAGER.getCode());
        List<Long> managerGroupIds = new ArrayList<>();
        for (Long organizationId : organizationIds) {
            List<Organization> managerGroups = organizationProvider.listOrganizationByGroupTypes(organizationId, types);
            if (0 < managerGroups.size()) {
                managerGroups.forEach(r -> {
                    managerGroupIds.add(r.getId());
                });
            }
        }

        if (0 < managerGroupIds.size()) {
            List<OrganizationMember> members = organizationProvider.getOrganizationMemberByOrgIds(managerGroupIds, new ListingQueryBuilderCallback() {
                @Override
                public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                    query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
                    query.addGroupBy(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN);
                    return query;
                }
            });
            for (OrganizationMember member : members) {
                OrganizationManagerDTO managerDTO = ConvertHelper.convert(member, OrganizationManagerDTO.class);
                managerDTO.setMemberId(member.getId());
                managerDTO.setDetailId(member.getDetailId());
                OrganizationMemberDetails memberDetails = organizationProvider.findOrganizationMemberDetailsByDetailId(member.getDetailId());
                if (memberDetails != null) {
                    managerDTO.setContactName(memberDetails.getContactName());
                }
                dtos.add(managerDTO);
            }
        }
        return dtos;
    }

    /**
     * 转换map
     *
     * @param roles
     * @return
     */
    private Map<Long, Role> convertOrganizationRoleMap(List<Role> roles) {
        Map<Long, Role> map = new HashMap<Long, Role>();
        if (null == roles) {
            return map;
        }
        for (Role role : roles) {
            map.put(role.getId(), role);
        }
        return map;
    }

    private String getNotifyText(Organization org, OrganizationMember member, User user, int code ,String textInfo) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("enterpriseName", org.getName());
        map.put("userName", null == member.getContactName() ? member.getContactToken().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2") : member.getContactName());
        map.put("userToken", member.getContactToken());

        map.put("textInfo", textInfo==null?"":textInfo);
        if (member.getContactDescription() != null && member.getContactDescription().length() > 0) {
            map.put("description", String.format("(%s)", member.getContactDescription()));
        } else {
            map.put("description", "");
        }

        String scope = EnterpriseNotifyTemplateCode.SCOPE;

        user = UserContext.current().getUser();

        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, user.getLocale(), map, "");

        return notifyTextForApplicant;
    }

    private List<Long> listOrganzationAdminIds(Long organizationId) {
        ListServiceModuleAdministratorsCommand cmd = new ListServiceModuleAdministratorsCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setActivationFlag(ActivationFlag.YES.getCode());
        List<OrganizationContactDTO> orgs = rolePrivilegeService.listOrganizationSuperAdministrators(cmd);
        if(orgs != null && orgs.size() > 0) {
            return orgs.stream().map((r)-> {
                return r.getTargetId();
            }).collect(Collectors.toList());
        }

        return new ArrayList<Long>();
    }

    private void sendMessageForContactApply(OrganizationMember member) {

        Organization org = this.organizationProvider.findOrganizationById(member.getOrganizationId());
        User user = userProvider.findUserById(member.getTargetId());

        // send notification to who is requesting to join the enterprise
        // String notifyTextForApplicant = this.getNotifyText(org, member, user, EnterpriseNotifyTemplateCode.ENTERPRISE_CONTACT_REQUEST_TO_JOIN_FOR_APPLICANT);

        List<Long> includeList = new ArrayList<Long>();

        //给申请人发的信息应为私信by xiongying 20160524
        // sendMessageToUser(member.getTargetId(), notifyTextForApplicant, null);// 不给申请人发送消息
//         includeList.add(member.getTargetId());
//
//         sendEnterpriseNotification(org.getId(), includeList, null, notifyTextForApplicant, null, null);

        // send notification to all the other members in the group
        String notifyTextForOperator = this.getNotifyText(org, member, user, EnterpriseNotifyTemplateCode.ENTERPRISE_CONTACT_REQUEST_TO_JOIN_FOR_OPERATOR ,null);

        //Updated by Jannson 
        //includeList = getOrganizationAdminIncludeList(member.getOrganizationId(), user.getId(), user.getId());
        includeList = listOrganzationAdminIds(member.getOrganizationId());
        if (includeList.size() > 0) {

            QuestionMetaObject metaObject = createGroupQuestionMetaObject(org, member, null);
            metaObject.setRequestInfo(notifyTextForOperator);

            sendRouterEnterpriseNotificationUseSystemUser(includeList, null, notifyTextForOperator, metaObject, member.getOrganizationId());

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Send waiting approval message to admin member in organization, userId=" + user.getId()
                        + ", organizationId=" + org.getId() + ", adminList=" + includeList);
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("No admin contact found in organization, userId=" + user.getId() + ", organizationId=" + org.getId());
            }
        }
    }

    private void sendMessageForContactApproved(OrganizationMember member) {
        Organization org = this.organizationProvider.findOrganizationById(member.getOrganizationId());
        User user = userProvider.findUserById(member.getTargetId());

        Long groupId = org.getGroupId();

        if (null != groupId) {
            Group group = groupProvider.findGroupById(groupId);
            if (null == group) {
                LOGGER.error("group non-existent。organizationId = {}, groupId = {}", org.getId(), groupId);
//				throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_PARAMETER_NOT_EXIST,
//						"group non-existent。");
                return;
            }
        } else {
            LOGGER.error("organization groupId is null。organizationId=" + org.getId());
//			throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
//					"organization groupId is null");
            return;
        }

        // send notification to who is requesting to join the enterprise
        String notifyTextForApplicant = this.getNotifyText(org, member, user, EnterpriseNotifyTemplateCode.ENTERPRISE_USER_SUCCESS_MYSELF,null);

        QuestionMetaObject metaObject = createGroupQuestionMetaObject(org, member, null);

        // send notification to who is requesting to join the enterprise
        List<Long> includeList = new ArrayList<>();

        includeList.add(member.getTargetId());
        sendEnterpriseNotificationUseSystemUser(includeList, null, notifyTextForApplicant);

        //同意加入公司通知客户端  by sfyan 20160526
        sendEnterpriseNotification(includeList, null, notifyTextForApplicant, MetaObjectType.ENTERPRISE_AGREE_TO_JOIN, metaObject);

        // send notification to all the other members in the group
        notifyTextForApplicant = this.getNotifyText(org, member, user, EnterpriseNotifyTemplateCode.ENTERPRISE_USER_SUCCESS_OTHER,null);
        // 消息只发给公司的管理人员  by sfyan 20170213
//        includeList = this.includeOrgList(org, member.getTargetId());
        includeList = listOrganzationAdminIds(member.getOrganizationId());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Send has approval message to admin member in organization, organizationId=" + org.getId() + ", adminList=" + includeList);
        }
        sendEnterpriseNotificationUseSystemUser(includeList, null, notifyTextForApplicant);
    }

    private void sendMessageToUser(Long uid, String content, Map<String, String> meta) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uid.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_GROUP);
        if (null != meta && meta.size() > 0) {
            messageDto.getMeta().putAll(meta);
        }
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                uid.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }

    private void sendMessageForContactReject(OrganizationMember member , String rejectText) {
        // send notification to who is requesting to join the enterprise
        Organization org = this.organizationProvider.findOrganizationById(member.getOrganizationId());
        User user = userProvider.findUserById(member.getTargetId());

        // send notification to who is requesting to join the enterprise
        String notifyTextForApplicant = this.getNotifyText(org, member, user, EnterpriseNotifyTemplateCode.ENTERPRISE_USER_REJECT_JOIN,rejectText);

        // send notification to who is requesting to join the enterprise

        List<Long> includeList = new ArrayList<Long>();
        //给申请人发的信息应为私信by xiongying 20160524
        sendMessageToUser(member.getTargetId(), notifyTextForApplicant, null);
//       includeList.add(member.getTargetId());
//       sendEnterpriseNotification(org.getId(), includeList, null, notifyTextForApplicant, null, null);

        // send notification to all the other members in the group
        // code = EnterpriseNotifyTemplateCode.ENTERPRISE_USER_SUCCESS_OTHER;
        // notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        // sendEnterpriseNotification(enterprise.getId(), null, includeList, notifyTextForApplicant, null, null);
    }

    private void sendMessageForContactLeave(OrganizationMember member) {
        Organization org = this.organizationProvider.findOrganizationById(member.getOrganizationId());
        User user = userProvider.findUserById(member.getTargetId());

        // send notification to who is requesting to join the enterprise
        String notifyTextForApplicant = this.getNotifyText(org, member, user, EnterpriseNotifyTemplateCode.ENTERPRISE_CONTACT_LEAVE_FOR_APPLICANT,null);
        List<Long> includeList = new ArrayList<Long>();
        //给申请人发的信息应为私信by xiongying 20160524
        sendMessageToUser(member.getTargetId(), notifyTextForApplicant, null);
//       includeList.add(member.getTargetId());
//       sendEnterpriseNotification(org.getId(), includeList, null, notifyTextForApplicant, null, null);

        //发消息给客户端 lei.lv
        includeList.add(member.getTargetId());
        QuestionMetaObject metaObject = createGroupQuestionMetaObject(org, member, null);
        sendEnterpriseNotification(includeList, null, notifyTextForApplicant, MetaObjectType.ENTERPRISE_AGREE_TO_JOIN, metaObject);
        includeList.clear();

        // send notification to all the other members in the enterprise
        notifyTextForApplicant = this.getNotifyText(org, member, user, EnterpriseNotifyTemplateCode.ENTERPRISE_CONTACT_LEAVE_FOR_OTHER,null);

        //消息只发给公司的管理人员  by sfyan 20170213
        includeList = this.includeOrgList(org, member.getTargetId());
        sendEnterpriseNotificationUseSystemUser(includeList, null, notifyTextForApplicant);


    }

    /**
     * 获取初自己以外所有管理人员
     *
     * @param org
     * @param excludeUserId
     * @return
     */
    private List<Long> includeOrgList(Organization org, Long excludeUserId) {

        List<OrganizationContactDTO> contacts = new ArrayList<>();
        ListServiceModuleAdministratorsCommand cmd = new ListServiceModuleAdministratorsCommand();
        cmd.setOrganizationId(org.getId());
        //获取企业管理员 by sfyan 20170213
        List<OrganizationContactDTO> orgAdmin = rolePrivilegeService.listOrganizationAdministrators(cmd);
        if (null != orgAdmin) {
            contacts.addAll(orgAdmin);
        }

        //是管理公司的情况下，需要再获取超级管理员 by sfyan 20170213
        if (OrganizationType.fromCode(org.getOrganizationType()) == OrganizationType.PM) {
            List<OrganizationContactDTO> superAdmin = rolePrivilegeService.listOrganizationSuperAdministrators(cmd);
            if (null != superAdmin) {
                contacts.addAll(superAdmin);
            }
        }

//	   List<OrganizationMember> members = organizationProvider.listOrganizationMembersByOrgId(org.getId());
        List<Long> includeList = new ArrayList<Long>();
        for (OrganizationContactDTO contact : contacts) {
            if (OrganizationMemberTargetType.USER.getCode().equals(contact.getTargetType()) && excludeUserId != contact.getTargetId()) {
                includeList.add(contact.getTargetId());
            }

        }
        return includeList.stream().distinct().collect(Collectors.toList());
    }

    private void sendRouterEnterpriseNotificationUseSystemUser(
            List<Long> includeList, List<Long> excludeList, String message, QuestionMetaObject metaObject, Long organizationId) {
        if (message != null && message.length() != 0) {
            MessageDTO messageDto = new MessageDTO();
            messageDto.setAppId(AppConstants.APPID_MESSAGING);
            messageDto.setSenderUid(User.SYSTEM_UID);
            messageDto.setBodyType(MessageBodyType.TEXT.getCode());
            messageDto.setBody(message);
            messageDto.setMetaAppId(AppConstants.APPID_ENTERPRISE);
            if (includeList != null && includeList.size() > 0) {
                messageDto.getMeta().put(MessageMetaConstant.INCLUDE,
                        StringHelper.toJsonString(includeList));
            }
            if (excludeList != null && excludeList.size() > 0) {
                messageDto.getMeta().put(MessageMetaConstant.EXCLUDE,
                        StringHelper.toJsonString(excludeList));
            }

            if (includeList != null && includeList.size() > 0) {
                if (excludeList != null && excludeList.size() > 0) {
                    includeList = includeList.stream().filter(r -> !excludeList.contains(r)).collect(Collectors.toList());
                }
                includeList.stream().distinct().forEach(targetId -> {
                    List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(targetId);
                    if (!CollectionUtils.isEmpty(members)) {
                        members.stream().forEach(member -> {
                            if (organizationId.equals(member.getOrganizationId()) && OrganizationMemberGroupType.MANAGER.getCode().equals(member.getMemberGroup())) {
                                metaObject.setEnterpriseManageFlag(ManageType.ENTERPRISE.getCode());
                            }
                        });
                    }
                    QuestionMetaActionData actionData = new QuestionMetaActionData();
                    actionData.setMetaObject(metaObject);

                    String routerUri = RouterBuilder.build(Router.ENTERPRISE_MEMBER_APPLY, actionData);
                    RouterMetaObject mo = new RouterMetaObject();
                    mo.setUrl(routerUri);
                    messageDto.getMeta().put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
                    messageDto.getMeta().put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(mo));
                    messageDto.setChannels(Collections.singletonList(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(targetId))));
                    messagingService.routeMessage(User.SYSTEM_USER_LOGIN,
                            AppConstants.APPID_MESSAGING, ChannelType.USER.getCode(), String.valueOf(targetId),
                            messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
                });
            }
        }
    }

    private void sendEnterpriseNotification(List<Long> includeList, List<Long> excludeList, String message,
                                            MetaObjectType metaObjectType, QuestionMetaObject metaObject) {
        if (message != null && message.length() != 0) {
            MessageDTO messageDto = new MessageDTO();
            messageDto.setAppId(AppConstants.APPID_MESSAGING);
            messageDto.setSenderUid(User.SYSTEM_UID);
            messageDto.setBodyType(MessageBodyType.NOTIFY.getCode());
            messageDto.setBody(message);
            messageDto.setMetaAppId(AppConstants.APPID_ENTERPRISE);
            if (includeList != null && includeList.size() > 0) {
                messageDto.getMeta().put(MessageMetaConstant.INCLUDE,
                        StringHelper.toJsonString(includeList));
            }
            if (excludeList != null && excludeList.size() > 0) {
                messageDto.getMeta().put(MessageMetaConstant.EXCLUDE,
                        StringHelper.toJsonString(excludeList));
            }
            if (metaObjectType != null && metaObject != null) {
                messageDto.getMeta().put(MessageMetaConstant.META_OBJECT_TYPE,
                        metaObjectType.getCode());
                messageDto.getMeta().put(MessageMetaConstant.META_OBJECT,
                        StringHelper.toJsonString(metaObject));
            }

            if (includeList != null) {
                for (Long targetId : includeList) {
                    messageDto.setChannels(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(targetId)));
                    messagingService.routeMessage(User.SYSTEM_USER_LOGIN,
                            AppConstants.APPID_MESSAGING, ChannelType.USER.getCode(), String.valueOf(targetId),
                            messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
                }
            }
        }
    }

    private void sendEnterpriseNotificationUseSystemUser(List<Long> includeList, List<Long> excludeList, String message) {
        if (message != null && message.length() != 0) {
            MessageDTO messageDto = new MessageDTO();
            messageDto.setAppId(AppConstants.APPID_MESSAGING);
            messageDto.setSenderUid(User.SYSTEM_UID);
            messageDto.setBodyType(MessageBodyType.TEXT.getCode());
            messageDto.setBody(message);
            messageDto.setMetaAppId(AppConstants.APPID_ENTERPRISE);
            if (includeList != null && includeList.size() > 0) {
                messageDto.getMeta().put(MessageMetaConstant.INCLUDE,
                        StringHelper.toJsonString(includeList));
            }
            if (excludeList != null && excludeList.size() > 0) {
                messageDto.getMeta().put(MessageMetaConstant.EXCLUDE,
                        StringHelper.toJsonString(excludeList));
            }
            if (includeList != null && includeList.size() > 0) {
                if (excludeList != null && excludeList.size() > 0) {
                    includeList = includeList.stream().filter(r -> !excludeList.contains(r)).collect(Collectors.toList());
                }
                includeList.stream().distinct().forEach(targetId -> {
                    messageDto.setChannels(Collections.singletonList(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(targetId))));
                    messagingService.routeMessage(User.SYSTEM_USER_LOGIN,
                            AppConstants.APPID_MESSAGING, ChannelType.USER.getCode(), String.valueOf(targetId),
                            messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
                });
            }
        }
    }

    /**
     * 处理层级菜单
     *
     * @param orgs
     * @return
     */
    private OrganizationDTO getOrganizationMenu(List<OrganizationDTO> orgs, OrganizationDTO dto) {

        List<OrganizationDTO> orgChildrens = new ArrayList<OrganizationDTO>();

        for (OrganizationDTO organization : orgs) {
            if (dto.getId().equals(organization.getParentId())) {
                OrganizationDTO organizationDTO = getOrganizationMenu(orgs, organization);
                orgChildrens.add(organizationDTO);
            }
        }
        orgChildrens.sort(Comparator.comparingInt(OrganizationDTO::getOrder));
        dto.setChildrens(orgChildrens);

        return dto;
    }

    private Organization checkEnterpriseParameter(Long enterpriseId, Long operatorUid, String tag) {
        if (enterpriseId == null) {
            LOGGER.error("organization id is null, operatorUid=" + operatorUid + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Enterprise id can not be null");
        }

        Organization organization = this.organizationProvider.findOrganizationById(enterpriseId);
        if (organization == null) {
            LOGGER.error("organization not found, operatorUid=" + operatorUid + ", enterpriseId=" + enterpriseId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(EnterpriseServiceErrorCode.SCOPE, EnterpriseServiceErrorCode.ERROR_ENTERPRISE_NOT_FOUND,
                    "Unable to find the organization");
        }

        if (OrganizationStatus.ACTIVE != OrganizationStatus.fromCode(organization.getStatus())) {
            LOGGER.error("organization status not active, operatorUid=" + operatorUid + ", enterpriseId=" + enterpriseId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(EnterpriseServiceErrorCode.SCOPE, EnterpriseServiceErrorCode.ERROR_ENTERPRISE_NOT_FOUND,
                    "Unable to find the organization");
        }

        if (OrganizationGroupType.ENTERPRISE != OrganizationGroupType.fromCode(organization.getGroupType())) {
            LOGGER.error("organization groupType not enterprise, operatorUid=" + operatorUid + ", enterpriseId=" + enterpriseId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(EnterpriseServiceErrorCode.SCOPE, EnterpriseServiceErrorCode.ERROR_ENTERPRISE_NOT_FOUND,
                    "Unable to find the organization");
        }

        return organization;
    }

    private OrganizationMember checkEnterpriseContactParameter(Long enterpriseId, Long targetId, Long operatorUid, String tag) {
        if (targetId == null) {
            LOGGER.error("Enterprise contact target user id is null, operatorUid=" + operatorUid
                    + ", enterpriseId=" + enterpriseId + ", targetId=" + targetId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Enterprise contact target user id can not be null");
        }

        OrganizationMember member = this.organizationProvider.findOrganizationMemberByUIdAndOrgId(targetId, enterpriseId);
        if (member == null) {
            LOGGER.error("Enterprise contact not found, operatorUid=" + operatorUid
                    + ", enterpriseId=" + enterpriseId + ", targetId=" + targetId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ENTERPRISE_CONTACT_NOT_FOUND,
                    "Unable to find the enterprise contact");
        }

        return member;
    }

    /**
     * add by yuanlei
     * 查询OrganizationMember对象的方法
     * @param enterpriseId
     * @param targetId
     * @param operatorUid
     * @param tag
     * @return
     */
    private OrganizationMember checkEnterpriseContactParameterContainReject(Long enterpriseId, Long targetId, Long operatorUid, String tag){
        //1.首先需要对参数进行非空校验
        if (targetId == null) {
            //说明参数为空
            LOGGER.info("Enterprise contact target user id is null, operatorUid=" + operatorUid
                    + ", enterpriseId=" + enterpriseId + ", targetId=" + targetId + ", tag=" + tag);
            //返回给前端信息
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Enterprise contact target user id can not be null");
        }
        //说明参数不为空,那么我们根据targetId和enterpriseId来查询数据库得到OrganizationMember对象
        OrganizationMember organizationMember = this.organizationProvider.findOrganizationMemberByUIdAndOrgId(targetId, enterpriseId);
        //对OrganizationMember对象进行非空校验
        if (organizationMember == null) {
            LOGGER.error("Enterprise contact not found, operatorUid=" + operatorUid
                    + ", enterpriseId=" + enterpriseId + ", targetId=" + targetId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ENTERPRISE_CONTACT_NOT_FOUND,
                    "Unable to find the enterprise contact");
        }

        return organizationMember;

    }


    private QuestionMetaObject createGroupQuestionMetaObject(Organization org, OrganizationMember requestor, OrganizationMember target) {
        QuestionMetaObject metaObject = new QuestionMetaObject();

        if (org != null) {
            metaObject.setResourceType(EntityType.ORGANIZATIONS.getCode());
            metaObject.setResourceId(org.getId());
        }

        if (requestor != null) {
            metaObject.setRequestorUid(requestor.getTargetId());
            metaObject.setRequestTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            metaObject.setRequestorNickName(requestor.getNickName());
            //add by yuanlei
            metaObject.setContactName(requestor.getContactName());
            metaObject.setContactDescription(requestor.getContactDescription());
            String avatar = requestor.getAvatar();
            metaObject.setRequestorAvatar(avatar);
            if (avatar != null && avatar.length() > 0) {
                try {
                    String url = contentServerService.parserUri(avatar, EntityType.USER.getCode(), UserContext.current().getUser().getId());
                    metaObject.setRequestorAvatarUrl(url);
                } catch (Exception e) {
                    LOGGER.error("Failed to parse avatar uri of organization member, organizationId=" + requestor.getOrganizationId()
                            + ", memberId=" + requestor.getId() + ", userId=" + requestor.getTargetId(), e);
                }
            }
            metaObject.setRequestId(requestor.getId());
            //根据owner_uid、和identifier_type字段来查询表eh_user_identifiers表
            UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(
                    requestor.getTargetId(), IdentifierTypeEnum.MOBILE.getCode());
            if((userIdentifier != null) && !"".equals(userIdentifier)){
                metaObject.setPhoneNo(userIdentifier.getIdentifierToken());
                //申请加入企业消息推送增加区号. add by yanlong.liang 20180725
                metaObject.setRegionCode(userIdentifier.getRegionCode());
            }
        }

        if (target != null) {
            metaObject.setTargetType(EntityType.USER.getCode());
            metaObject.setTargetId(target.getTargetId());
            metaObject.setRequestId(target.getId());
        }

        return metaObject;
    }


    /**
     * 根据组织id来查询
     * @param orgId
     * @return
     */
    private Organization checkOrganization(Long orgId) {
        Organization org = organizationProvider.findOrganizationById(orgId);
        if (org == null) {
            LOGGER.error("Unable to find the organization.organizationId=" + orgId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Unable to find the organization.");
        }
        return org;
    }

    /* Add by Jannson 这个接口已经无法使用。管理员添加的时候没有添加到 eh_acl_role_assignments 中，导致查不到 */
    private List<OrganizationMember> getOrganizationAdminMemberRole(Long organizationId, List<Long> roles) {
        List<OrganizationMember> members = organizationProvider.listOrganizationMembersByOrgId(organizationId);

        List<OrganizationMember> roleMembers = new ArrayList<OrganizationMember>();
        if (0 == members.size()) {
            return roleMembers;
        }

        for (OrganizationMember member : members) {
            if (member.getTargetType().equals(OrganizationMemberTargetType.USER.getCode())) {
                List<Long> resources = aclProvider.getRolesFromResourceAssignments(EntityType.ORGANIZATIONS.getCode(), organizationId, EntityType.USER.getCode(), member.getTargetId(), null);

                if (null != resources && resources.size() > 0) {
                    for (Long roleId : resources) {
                        if (roles.contains(roleId)) {
                            roleMembers.add(member);
                            continue;
                        }
                    }
                }

                if (null != member.getGroupId() && 0 != member.getGroupId()) {
                    resources = aclProvider.getRolesFromResourceAssignments("system", null, EntityType.ORGANIZATIONS.getCode(), member.getGroupId(), null);
                    if (null != resources && resources.size() > 0) {
                        for (Long roleId : resources) {
                            if (roles.contains(roleId)) {
                                roleMembers.add(member);
                            }
                        }
                    }
                }

            }
        }

        return roleMembers;
    }

    /* Added by Jannson 此函数失效 */
    private List<Long> getOrganizationAdminIncludeList(Long organizationId, Long operatorId, Long targetId) {

        List<Long> memberIds = new ArrayList<Long>();

        List<Long> roles = new ArrayList<Long>();
        roles.add(RoleConstants.PM_ORDINARY_ADMIN);
        roles.add(RoleConstants.PM_SUPER_ADMIN);
        roles.add(RoleConstants.ENTERPRISE_ORDINARY_ADMIN);
        roles.add(RoleConstants.ENTERPRISE_SUPER_ADMIN);

        List<OrganizationMember> members = this.getOrganizationAdminMemberRole(organizationId, roles);

        if (members.size() == 0) {
            return memberIds;
        }

        memberIds = members.stream().map((r) -> {
            return r.getTargetId();
        }).collect(Collectors.toList());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Get organization member admin include list, organizationId=" + organizationId + ", operatorId=" + operatorId
                    + ", targetId=" + targetId + ", includeList=" + memberIds);
        }

        return memberIds;
    }


    @Override
    public PostDTO acceptTask(ProcessOrganizationTaskCommand cmd) {
        // TODO Auto-generated method stub

        User user = UserContext.current().getUser();
        Long taskId = cmd.getTaskId();
        OrganizationTask task = organizationProvider.findOrganizationTaskById(taskId);

	    	/* 根据用户不同 查询不同的任务类型贴*/
        List<Long> privileges = rolePrivilegeService.getUserPrivileges(null, cmd.getOrganizationId(), user.getId());

        this.checkPrivileged(privileges, user, task);

        if (OrganizationTaskStatus.fromCode(task.getTaskStatus()) == OrganizationTaskStatus.UNPROCESSED
                && (task.getTargetId().equals(user.getId())
                || ((StringUtils.isEmpty(task.getTargetId()) || task.getTargetId() == 0)))) {
            task.setTargetId(user.getId());
            task.setTargetType(OrganizationTaskTargetType.USER.getCode());
            task.setTaskStatus(OrganizationTaskStatus.PROCESSING.getCode());
            task.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            task.setOperatorUid(user.getId());
            task.setProcessingTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

            organizationProvider.updateOrganizationTask(task);

            NewCommentCommand command = new NewCommentCommand();
//	    		command.setForumId(1l);
            //论坛id为帖子所在论坛 by xiongying 20160509
            Post topic = this.checkTopic(task.getApplyEntityId());
            if (topic != null) {
                command.setForumId(topic.getForumId());
            }
            command.setTopicId(task.getApplyEntityId());
            command.setContentType(PostContentType.TEXT.getCode());
            Map<String, Object> map = new HashMap<String, Object>();
            UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
            map.put("targetUName", user.getNickName());
            map.put("targetUToken", userIdentifier.getIdentifierToken());
            task.setTargetToken(userIdentifier.getIdentifierToken());
            task.setTargetName(user.getNickName());
            String content = localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE, OrganizationNotificationTemplateCode.ORGANIZATION_TASK_ACCEPT_COMMENT, user.getLocale(), map, "");
            command.setContent(content);
            this.createComment(command);
        } else {
            LOGGER.error("Tasks have been processed, status=" + task.getTaskStatus() + ", targetId=" + task.getTargetId());
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ORG_TASK_ALREADY_PROCESSED,
                    "Tasks have been processed.");
        }

        Long communityId = null;
        if (VisibleRegionType.fromCode(task.getVisibleRegionType()) == VisibleRegionType.COMMUNITY) {
            communityId = task.getVisibleRegionId();
        }

        PostDTO dto = this.forumService.getTopicById(task.getApplyEntityId(), communityId, false);
        if (null != dto) {
            dto.setEmbeddedJson(StringHelper.toJsonString(task));
        }
        return dto;
    }

    @Override
    public PostDTO grabTask(ProcessOrganizationTaskCommand cmd) {
        // TODO Auto-generated method stub
        User user = UserContext.current().getUser();
        Long taskId = cmd.getTaskId();
        OrganizationTask task = organizationProvider.findOrganizationTaskById(taskId);

	    	/* 根据用户不同 查询不同的任务类型贴*/
        List<Long> privileges = rolePrivilegeService.getUserPrivileges(null, cmd.getOrganizationId(), user.getId());

        this.checkPrivileged(privileges, user, task);

        if (OrganizationTaskStatus.fromCode(task.getTaskStatus()) == OrganizationTaskStatus.UNPROCESSED
                && (StringUtils.isEmpty(task.getTargetId()) || task.getTargetId() == 0)) {
            task.setTaskStatus(OrganizationTaskStatus.PROCESSING.getCode());
            task.setTargetId(user.getId());
            task.setTargetType(OrganizationTaskTargetType.USER.getCode());
            task.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            task.setOperatorUid(user.getId());
            task.setProcessingTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

            organizationProvider.updateOrganizationTask(task);

            NewCommentCommand command = new NewCommentCommand();
//	    		command.setForumId(1l);
            //论坛id为帖子所在论坛 by xiongying 20160509
            Post topic = this.checkTopic(task.getApplyEntityId());
            if (topic != null) {
                command.setForumId(topic.getForumId());
            }
            command.setTopicId(task.getApplyEntityId());
            command.setContentType(PostContentType.TEXT.getCode());
            Map<String, Object> map = new HashMap<String, Object>();
            OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(task.getTargetId(), cmd.getOrganizationId());
            if (null != member) {
                map.put("targetUName", member.getContactName());
                map.put("targetUToken", member.getContactToken());
                String content = localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE, OrganizationNotificationTemplateCode.ORGANIZATION_TASK_ACCEPT_COMMENT, user.getLocale(), map, "");
                task.setTargetToken(member.getContactToken());
                task.setTargetName(member.getContactName());
                command.setContent(content);
                this.createComment(command);
            }

        } else {
            LOGGER.error("Tasks have been processed, status=" + task.getTaskStatus() + ", targetId=" + task.getTargetId());
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ORG_TASK_ALREADY_PROCESSED,
                    "Tasks have been processed.");
        }

        Long communityId = null;
        if (VisibleRegionType.fromCode(task.getVisibleRegionType()) == VisibleRegionType.COMMUNITY) {
            communityId = task.getVisibleRegionId();
        }

        PostDTO dto = this.forumService.getTopicById(task.getApplyEntityId(), communityId, false);
        if (null != dto) {
            dto.setEmbeddedJson(StringHelper.toJsonString(task));
        }
        return dto;
    }

    @Override
    public PostDTO processingTask(ProcessOrganizationTaskCommand cmd) {

        User user = UserContext.current().getUser();
        Long taskId = cmd.getTaskId();
        OrganizationTask task = organizationProvider.findOrganizationTaskById(taskId);

        if (null == task) {
            LOGGER.error("Task does not exist, taskId=" + taskId);
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ORG_TASK_NOT_EXIST,
                    "Task does not exist.");
        }

        Post post = forumProvider.findPostById(task.getApplyEntityId());

        Map<String, Object> map = new HashMap<String, Object>();

	    	/* 根据用户不同 查询不同的任务类型贴*/
        List<Long> privileges = rolePrivilegeService.getUserPrivileges(null, cmd.getOrganizationId(), user.getId());
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
        //当可以查询全部的任务类型时
        if (privileges.contains(PrivilegeConstants.TaskAllListPosts)) {
            if (null != cmd.getUserId()) {
                if (cmd.getUserId().equals(user.getId()) && OrganizationTaskStatus.fromCode(cmd.getTaskStatus()) == OrganizationTaskStatus.UNPROCESSED) {
                    cmd.setTaskStatus(OrganizationTaskStatus.PROCESSING.getCode());
                }
            } else if (!StringUtils.isEmpty(task.getTargetId()) && task.getTargetId() != 0) {
                cmd.setUserId(task.getTargetId());
            } else {
                if (OrganizationTaskStatus.fromCode(cmd.getTaskStatus()) == OrganizationTaskStatus.UNPROCESSED || OrganizationTaskStatus.fromCode(cmd.getTaskStatus()) == OrganizationTaskStatus.PROCESSING) {
                    //异常
                    LOGGER.error("This task is not assigned to any personnel, targetId=" + task.getTargetId());
                    throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ORG_TASK_NOT_ASSIGNED_PERSONNEL,
                            "This task is not assigned to any personnel.");
                }
                cmd.setUserId(0l);
            }


            task.setTargetId(cmd.getUserId());
            task.setTargetType(OrganizationTaskTargetType.USER.getCode());
            task.setTaskStatus(cmd.getTaskStatus());
            //当智能处理部分类型时
        } else if (privileges.contains(PrivilegeConstants.TaskGuaranteeListPosts)) {
	    		/*根据权限仅限操作保修贴*/
            if (OrganizationTaskType.fromCode(task.getTaskType()) != OrganizationTaskType.REPAIRS) {
                returnNoPrivileged(privileges, user);
            }

            if (user.getId().equals(task.getTargetId())) {
                if (OrganizationTaskStatus.fromCode(cmd.getTaskStatus()) == OrganizationTaskStatus.PROCESSED) {
                } else if (OrganizationTaskStatus.fromCode(cmd.getTaskStatus()) == OrganizationTaskStatus.UNPROCESSED && null != cmd.getUserId()) {
                    if (cmd.getUserId().equals(task.getTargetId())) {
                        cmd.setTaskStatus(OrganizationTaskStatus.PROCESSING.getCode());
                    } else {
                        task.setTargetId(cmd.getUserId());
                        task.setTargetType(OrganizationTaskTargetType.USER.getCode());
                    }
                } else {
                    //异常
                    LOGGER.error("Cannot perform this operation on a task, status=" + cmd.getTaskStatus());
                    throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ORG_TASK_CANNOT_OPERATE,
                            "Cannot perform this operation on a task.");
                }

                task.setTaskStatus(cmd.getTaskStatus());
            } else {
                //异常
                LOGGER.error("Tasks have been processed, status=" + task.getTaskStatus() + ", targetId=" + task.getTargetId());
                throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ORG_TASK_ALREADY_PROCESSED,
                        "Tasks have been processed.");
            }

        } else if (privileges.contains(PrivilegeConstants.TaskSeekHelpListPosts)) {

	    		/*根据权限仅限操作紧急求助帖*/
            if (OrganizationTaskType.fromCode(task.getTaskType()) != OrganizationTaskType.EMERGENCY_HELP) {
                returnNoPrivileged(privileges, user);
            }

            if (user.getId().equals(task.getTargetId()) || task.getTargetId() == 0 || StringUtils.isEmpty(task.getTargetId())) {
                if (OrganizationTaskStatus.fromCode(cmd.getTaskStatus()) == OrganizationTaskStatus.OTHER) {
                    //异常
                    LOGGER.error("Cannot perform this operation on a task, status=" + cmd.getTaskStatus());
                    throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ORG_TASK_CANNOT_OPERATE,
                            "Cannot perform this operation on a task.");
                }

                if (OrganizationTaskStatus.fromCode(cmd.getTaskStatus()) != OrganizationTaskStatus.PROCESSED && null != cmd.getUserId()) {
                    task.setTargetId(cmd.getUserId());
                    task.setTargetType(OrganizationTaskTargetType.USER.getCode());
                }

                task.setTaskStatus(cmd.getTaskStatus());

            } else {
                //异常
                LOGGER.error("Tasks have been processed, status=" + task.getTaskStatus() + ", targetId=" + task.getTargetId());
                throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ORG_TASK_ALREADY_PROCESSED,
                        "Tasks have been processed.");
            }
        } else {
            returnNoPrivileged(privileges, user);
        }
        OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(task.getTargetId(), cmd.getOrganizationId());
        if (null != member) {
            map.put("operatorUName", user.getNickName());
            map.put("operatorUToken", userIdentifier.getIdentifierToken());

            map.put("targetUName", member.getContactName());
            map.put("targetUToken", member.getContactToken());
            User create = userProvider.findUserById(task.getCreatorUid());
            UserIdentifier createIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(task.getCreatorUid(), IdentifierType.MOBILE.getCode());
            if (null != createIdentifier) {
                map.put("createUName", null != create ? create.getNickName() : "[无]");
                map.put("createUToken", createIdentifier.getIdentifierToken());
            }

            task.setTargetName(member.getContactName());
            task.setTargetToken(member.getContactToken());
        }

        task.setTaskCategory(cmd.getTaskCategory());
        task.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        task.setOperatorUid(user.getId());
        if (OrganizationTaskStatus.fromCode(cmd.getTaskStatus()) == OrganizationTaskStatus.UNPROCESSED) {
            task.setUnprocessedTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        } else if (OrganizationTaskStatus.fromCode(cmd.getTaskStatus()) == OrganizationTaskStatus.PROCESSING) {
            task.setProcessingTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        } else if (OrganizationTaskStatus.fromCode(cmd.getTaskStatus()) == OrganizationTaskStatus.PROCESSED) {
            task.setProcessedTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        }
        organizationProvider.updateOrganizationTask(task);

        if (null != post) {
            post.setPrivateFlag(cmd.getPrivateFlag());
            post.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            post.setEmbeddedJson(StringHelper.toJsonString(task));
            forumProvider.updatePost(post);
        }
        this.sendTaskMsg(map, task, user);

        Long communityId = null;
        if (VisibleRegionType.fromCode(task.getVisibleRegionType()) == VisibleRegionType.COMMUNITY) {
            communityId = task.getVisibleRegionId();
        }

        PostDTO dto = this.forumService.getTopicById(task.getApplyEntityId(), communityId, false);
        if (null != dto) {
            dto.setEmbeddedJson(StringHelper.toJsonString(task));
        }
        return dto;
    }


    @Override
    public PostDTO refuseTask(ProcessOrganizationTaskCommand cmd) {
        // TODO Auto-generated method stub

        User user = UserContext.current().getUser();
        Long taskId = cmd.getTaskId();
        OrganizationTask task = organizationProvider.findOrganizationTaskById(taskId);

	    	/* 根据用户不同 查询不同的任务类型贴*/
        List<Long> privileges = rolePrivilegeService.getUserPrivileges(null, cmd.getOrganizationId(), user.getId());

        this.checkPrivileged(privileges, user, task);

        if (OrganizationTaskStatus.fromCode(task.getTaskStatus()) == OrganizationTaskStatus.UNPROCESSED
                && (task.getTargetId().equals(user.getId())
                || ((StringUtils.isEmpty(task.getTargetId()) || task.getTargetId() == 0)))) {
            task.setTaskStatus(OrganizationTaskStatus.UNPROCESSED.getCode());
            task.setTargetId(0l);
            task.setTargetType(null);
            task.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            Long operatorUid = task.getOperatorUid();
            task.setOperatorUid(user.getId());
            task.setUnprocessedTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

            organizationProvider.updateOrganizationTask(task);
            UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("targetUName", user.getNickName());
            map.put("targetUToken", userIdentifier.getIdentifierToken());
            sendOrganizationNotificationToUser(operatorUid, localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE, OrganizationNotificationTemplateCode.ORGANIZATION_TASK_REFUSE, user.getLocale(), map, ""));
        } else {
            LOGGER.error("Tasks have been processed, status=" + task.getTaskStatus() + ", targetId=" + task.getTargetId());
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ORG_TASK_ALREADY_PROCESSED,
                    "Tasks have been processed.");
        }

        Long communityId = null;
        if (VisibleRegionType.fromCode(task.getVisibleRegionType()) == VisibleRegionType.COMMUNITY) {
            communityId = task.getVisibleRegionId();
        }

        PostDTO dto = this.forumService.getTopicById(task.getApplyEntityId(), communityId, false);
        if (null != dto) {
            dto.setEmbeddedJson(StringHelper.toJsonString(task));
        }
        return dto;
    }


    @Override
    public ListPostCommandResponse listTaskTopicsByType(ListTopicsByTypeCommand cmd) {
        User user = UserContext.current().getUser();
        Long commuId = cmd.getCommunityId();

        if (null == cmd.getCommunityId()) {
            commuId = user.getCommunityId();
        }

        Community community = communityProvider.findCommunityById(commuId);
        Organization organization = this.checkOrganization(cmd.getOrganizationId());

        List<String> groupTypes = new ArrayList<String>();
        groupTypes.add(OrganizationGroupType.GROUP.getCode());
        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
        groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());

        List<Organization> organizations = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "/%", groupTypes);

        List<Long> organizationIds = new ArrayList<Long>();
        organizationIds.add(organization.getId());
        for (Organization org : organizations) {
            organizationIds.add(org.getId());
        }

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageOffset());
        List<OrganizationTask> orgTasks = organizationProvider.listOrganizationTasksByTypeOrStatus(locator, organizationIds, cmd.getTargetId(), cmd.getTaskType(), cmd.getTaskStatus(), VisibleRegionType.COMMUNITY.getCode(), commuId, pageSize);
        List<PostDTO> dtos = new ArrayList<PostDTO>();

        for (OrganizationTask task : orgTasks) {
            PostDTO dto = this.forumService.getTopicById(task.getApplyEntityId(), commuId, false);
            if (null == dto) {
                continue;
            }

            OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(task.getTargetId(), cmd.getOrganizationId());
            if (null != member) {
                task.setTargetName(member.getContactName());
                task.setTargetToken(member.getContactToken());
            }

            if (dto.getForumId().equals(community.getDefaultForumId())) {
                task.setOption(cmd.getOption());
                task.setEntrancePrivilege(cmd.getEntrancePrivilege());
                dto.setEmbeddedJson(StringHelper.toJsonString(task));
                dtos.add(dto);
            }
        }

        ListPostCommandResponse res = new ListPostCommandResponse();
        res.setNextPageAnchor(locator.getAnchor());
        res.setPosts(dtos);

        return res;
    }

    @Override
    public ListPostCommandResponse listAllTaskTopics(ListTopicsByTypeCommand cmd) {
        User user = UserContext.current().getUser();

//	    	List<Long> privileges = rolePrivilegeService.getUserPrivileges(null, cmd.getOrganizationId(), user.getId());

        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");

        if (resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.TaskAllListPosts)) {

        } else if (resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.TaskGuaranteeListPosts)) {
            if (StringUtils.isEmpty(cmd.getTaskType())) {
                cmd.setTargetId(user.getId());
            } else if (!StringUtils.isEmpty(cmd.getTaskType()) && OrganizationTaskType.fromCode(cmd.getTaskType()) != OrganizationTaskType.REPAIRS) {
                returnNoPrivileged(null, user);
            }
            cmd.setTaskType(OrganizationTaskType.REPAIRS.getCode());
        } else if (resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.TaskSeekHelpListPosts)) {
            if (StringUtils.isEmpty(cmd.getTaskType())) {
                cmd.setTargetId(user.getId());
            } else if (!StringUtils.isEmpty(cmd.getTaskType()) && OrganizationTaskType.fromCode(cmd.getTaskType()) != OrganizationTaskType.EMERGENCY_HELP) {
                returnNoPrivileged(null, user);
            }
            cmd.setTaskType(OrganizationTaskType.EMERGENCY_HELP.getCode());
        } else {
            returnNoPrivileged(null, user);
        }

        return this.listTaskTopicsByType(cmd);
    }

    @Override
    public ListPostCommandResponse listMyTaskTopics(ListTopicsByTypeCommand cmd) {
        User user = UserContext.current().getUser();

//	    	List<Long> privileges = rolePrivilegeService.getUserPrivileges(null, cmd.getOrganizationId(), user.getId());
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
			/* 根据用户不同 查询不同的任务类型贴*/

        cmd.setTargetId(user.getId());
        if (resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.TaskAllListPosts)) {
        } else if (resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.TaskGuaranteeListPosts)) {
            if (!StringUtils.isEmpty(cmd.getTaskType()) && OrganizationTaskType.fromCode(cmd.getTaskType()) != OrganizationTaskType.REPAIRS) {
                returnNoPrivileged(null, user);
            }
            cmd.setTaskType(OrganizationTaskType.REPAIRS.getCode());
        } else if (resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.TaskSeekHelpListPosts)) {
            if (!StringUtils.isEmpty(cmd.getTaskType()) && OrganizationTaskType.fromCode(cmd.getTaskType()) != OrganizationTaskType.EMERGENCY_HELP) {
                returnNoPrivileged(null, user);
            }
            cmd.setTaskType(OrganizationTaskType.EMERGENCY_HELP.getCode());
        } else {
            returnNoPrivileged(null, user);
        }

        return this.listTaskTopicsByType(cmd);
    }

    @Override
    public ListPostCommandResponse listGrabTaskTopics(ListTopicsByTypeCommand cmd) {
        User user = UserContext.current().getUser();

        List<Long> privileges = rolePrivilegeService.getUserPrivileges(null, cmd.getOrganizationId(), user.getId());

			/* 根据用户不同 查询不同的任务类型贴*/
        cmd.setTargetId(0l);
        cmd.setTaskStatus(OrganizationTaskStatus.UNPROCESSED.getCode());
        if (privileges.contains(PrivilegeConstants.TaskAllListPosts)) {
            cmd.setTaskType("");
        } else if (privileges.contains(PrivilegeConstants.TaskGuaranteeListPosts)) {
            cmd.setTaskType(OrganizationTaskType.REPAIRS.getCode());
        } else if (privileges.contains(PrivilegeConstants.TaskSeekHelpListPosts)) {
            cmd.setTaskType(OrganizationTaskType.EMERGENCY_HELP.getCode());
        } else {
            returnNoPrivileged(privileges, user);
        }

        return this.listTaskTopicsByType(cmd);
    }

    @Override
    public GetEntranceByPrivilegeResponse getEntranceByPrivilege(GetEntranceByPrivilegeCommand cmd, Long organizationId) {
        User user = UserContext.current().getUser();
        GetEntranceByPrivilegeResponse res = new GetEntranceByPrivilegeResponse();

        List<Long> privileges = rolePrivilegeService.getUserPrivileges(cmd.getModule(), organizationId, user.getId());

			/* 根据用户不同 查询不同的任务类型贴*/
        if (privileges.contains(PrivilegeConstants.TaskAllListPosts)) {
            res.setEntrancePrivilege(EntrancePrivilege.TASK_ALL_LIST.getCode());
        } else if (privileges.contains(PrivilegeConstants.TaskGuaranteeListPosts)) {
            res.setEntrancePrivilege(EntrancePrivilege.TASK_GUARANTEE_LIST.getCode());
        } else if (privileges.contains(PrivilegeConstants.TaskSeekHelpListPosts)) {
            res.setEntrancePrivilege(EntrancePrivilege.TASK_SEEK_HELP_LIST.getCode());
        } else {
            returnNoPrivileged(privileges, user);
        }

        return res;
    }


    private void sendTaskMsg(Map<String, Object> map, OrganizationTask task, User user) {

        Integer namespaceId = UserContext.getCurrentNamespaceId();

        NewCommentCommand command = new NewCommentCommand();
        command.setTopicId(task.getApplyEntityId());
        command.setContentType(PostContentType.TEXT.getCode());
        command.setForumId(1l);
        String contentMsg = "";
        String contentComment = "";
        if (OrganizationTaskStatus.fromCode(task.getTaskStatus()) == OrganizationTaskStatus.PROCESSED) {
            contentMsg = localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE, OrganizationNotificationTemplateCode.ORGANIZATION_TASK_FINISH, user.getLocale(), map, "");
            contentComment = localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE, OrganizationNotificationTemplateCode.ORGANIZATION_TASK_FINISH_COMMENT, user.getLocale(), map, "");
            sendOrganizationNotificationToUser(task.getCreatorUid(), contentMsg);
        } else if (OrganizationTaskStatus.fromCode(task.getTaskStatus()) == OrganizationTaskStatus.UNPROCESSED || OrganizationTaskStatus.fromCode(task.getTaskStatus()) == OrganizationTaskStatus.PROCESSING) {
            contentMsg = localeTemplateService.getLocaleTemplateString(SmsTemplateCode.SCOPE, SmsTemplateCode.PM_TASK_PROCESS_MSG_CODE, user.getLocale(), map, "");
            contentComment = localeTemplateService.getLocaleTemplateString(OrganizationNotificationTemplateCode.SCOPE, OrganizationNotificationTemplateCode.ORGANIZATION_TASK_PROCESSING_COMMENT, user.getLocale(), map, "");
            UserIdentifier target = userProvider.findClaimedIdentifierByOwnerAndType(task.getTargetId(), IdentifierType.MOBILE.getCode());
            if (null != target) {
                List<Tuple<String, Object>> variables = smsProvider.toTupleList("operatorUName", map.get("operatorUName"));
                smsProvider.addToTupleList(variables, "createUName", map.get("createUName"));
                smsProvider.addToTupleList(variables, "createUToken", map.get("createUToken"));
                //发送短信
                smsProvider.sendSms(namespaceId, target.getIdentifierToken(), SmsTemplateCode.SCOPE, SmsTemplateCode.PM_TASK_PROCESS_MSG_CODE, user.getLocale(), variables);
            }
        } else {
            //关闭 不要发任何消息
            return;
        }
        command.setContent(contentComment);
        this.createComment(command);
    }

    /**
     * 抛出无权限
     */
    private void returnNoPrivileged(List<Long> privileges, User user) {
        LOGGER.error("non-privileged, privileges=" + privileges + ", userId=" + user.getId());
        throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_NO_PRIVILEGED,
                "non-privileged.");
    }

    /**
     * 校验权限
     *
     * @param privileges
     */
    private void checkPrivileged(List<Long> privileges, User user, OrganizationTask task) {
        if (privileges.contains(PrivilegeConstants.TaskAllListPosts)) {

        } else if (privileges.contains(PrivilegeConstants.TaskGuaranteeListPosts)) {
				/*根据权限仅限操作保修贴*/
            if (OrganizationTaskType.fromCode(task.getTaskType()) != OrganizationTaskType.REPAIRS) {
                returnNoPrivileged(privileges, user);
            }
        } else if (privileges.contains(PrivilegeConstants.TaskSeekHelpListPosts)) {
				/*根据权限仅限操作紧急求助帖*/
            if (OrganizationTaskType.fromCode(task.getTaskType()) != OrganizationTaskType.EMERGENCY_HELP) {
                returnNoPrivileged(privileges, user);
            }
        } else {
            returnNoPrivileged(privileges, user);
        }
    }

    @Override
    public void createOrganizationOwner(CreateOrganizationOwnerCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getContactToken());

        Address address = null;
        if (null != cmd.getAddressId()) {
            address = addressProvider.findAddressById(cmd.getAddressId());
        }

        if (null == address && null != cmd.getCommunityId() && StringUtils.isEmpty(cmd.getBuildingName()) && StringUtils.isEmpty(cmd.getBuildingName())) {
            address = this.addressProvider.findApartmentAddress(UserContext.getCurrentNamespaceId(), cmd.getCommunityId(),
                    cmd.getBuildingName(), cmd.getApartmentName());
        }

        if (null == address) {
            LOGGER.error("invalid parameter, addressId=" + cmd.getAddressId() + ", communityId=" + cmd.getCommunityId() + ", buidlingName=" + cmd.getBuildingName() + ", apartmentName=" + cmd.getApartmentName());
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
                    "invalid parameter.");
        }


        if (null == userIdentifier) {
            OrganizationOwner owner = organizationProvider.getOrganizationOwnerByTokenOraddressId(cmd.getContactToken(), address.getId());
            if (null == owner) {
                owner = ConvertHelper.convert(cmd, OrganizationOwner.class);
                owner.setAddressId(address.getId());
                if (null == owner.getContactType()) {
                    owner.setContactType(ContactType.MOBILE.getCode());
                }
                owner.setNamespaceId(namespaceId);
                organizationProvider.createOrganizationOwner(owner);
            } else {
                owner.setContactName(cmd.getContactName());
                owner.setContactDescription(cmd.getContactDescription());

                organizationProvider.updateOrganizationOwner(owner);
            }
        } else {
            User user = userProvider.findUserById(userIdentifier.getOwnerUid());
            address.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
            familyService.getOrCreatefamily(address, user);
        }
    }

    @Override
    public void deleteOrganizationOwner(DeleteOrganizationOwnerCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getContactToken());
        if (null == userIdentifier) {
            OrganizationOwner owner = organizationProvider.getOrganizationOwnerByTokenOraddressId(cmd.getContactToken(), cmd.getAddressId());
            if (null != owner) {
                organizationProvider.deleteOrganizationOwnerById(owner.getId());
            }
        } else {
            User user = userProvider.findUserById(userIdentifier.getOwnerUid());
            List<UserGroup> userGroups = userProvider.listUserGroups(user.getId(), GroupDiscriminator.FAMILY.getCode());

            if (null != userGroups) {
                for (UserGroup userGroup : userGroups) {
                    Group group = groupProvider.findGroupById(userGroup.getGroupId());
                    if (null != group && group.getFamilyAddressId().equals(cmd.getAddressId())) {
                        LeaveFamilyCommand command = new LeaveFamilyCommand();
                        command.setId(group.getId());
                        command.setType(ParamType.FAMILY.getCode());
                        familyService.leave(command, user);
                    }
                }
            }

        }
    }

    @Override
    public ImportDataResponse importOwnerData(MultipartFile mfile, ImportOwnerDataCommand cmd) {
        ImportDataResponse importDataResponse = new ImportDataResponse();
        User user = UserContext.current().getUser();
        try {
            //解析excel
            List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());

            if (null == resultList || resultList.isEmpty()) {
                LOGGER.error("File content is empty。userId=" + user.getId());
                throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL,
                        "File content is empty");
            }
            LOGGER.debug("Start import data...,total:" + resultList.size());

            //导入数据，返回导入错误的日志数据集
            List<String> errorDataLogs = new ArrayList<String>();
            List<String> list = convertToStrList(resultList);
            for (String str : list) {
                String[] s = str.split("\\|\\|");

                CreateOrganizationOwnerCommand command = new CreateOrganizationOwnerCommand();
                command.setContactName(s[0]);
                command.setContactType(ContactType.MOBILE.getCode());
                command.setContactToken(s[1]);
                command.setBuildingName(s[2]);
                command.setApartmentName(s[3]);
                command.setContactDescription(s[4]);
                this.createOrganizationOwner(command);

            }
            LOGGER.debug("End import data...,fail:" + errorDataLogs.size());
            if (null == errorDataLogs || errorDataLogs.isEmpty()) {
                LOGGER.debug("Data import all success...");
            } else {
                //记录导入错误日志
                for (String log : errorDataLogs) {
                    LOGGER.error(log);
                }
            }

            importDataResponse.setTotalCount((long) resultList.size() - 1);
            importDataResponse.setFailCount((long) errorDataLogs.size());
        } catch (IOException e) {
            LOGGER.error("File can not be resolved...");
            e.printStackTrace();
        }
        return importDataResponse;
    }

    @Override
    public List<GroupMember> listMessageGroupMembers(Long groupId) {
        List<GroupMember> members = new ArrayList<GroupMember>();
        Organization organization = organizationProvider.findOrganizationByGroupId(groupId);
        if (null == organization) {
            return members;
        }

        Integer namespaceId = UserContext.getCurrentNamespaceId(organization.getNamespaceId());
        if (!namespaceId.equals(organization.getNamespaceId())) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Namespace error for group forward");
        }

        List<OrganizationMember> organizationMember = organizationProvider.listOrganizationMembersByOrgId(organization.getId());
        for (OrganizationMember member : organizationMember) {
            if (OrganizationMemberStatus.fromCode(member.getStatus()) == OrganizationMemberStatus.ACTIVE && OrganizationMemberTargetType.fromCode(member.getTargetType()) == OrganizationMemberTargetType.USER) {
                GroupMember groupMember = new GroupMember();
                groupMember.setMemberId(member.getTargetId());
                groupMember.setMemberType(EntityType.USER.getCode());
                members.add(groupMember);
            }
        }

        return members;
    }

    @Override
    public Long getOrganizationActiveCommunityId(Long organizationId) {
        Long communityId = null;
        OrganizationCommunityRequest orgCmntyRequest = organizationProvider.getOrganizationCommunityRequestByOrganizationId(organizationId);
        if (orgCmntyRequest != null) {
            OrganizationCommunityRequestStatus status = OrganizationCommunityRequestStatus.fromCode(orgCmntyRequest.getMemberStatus());
            if (status == OrganizationCommunityRequestStatus.ACTIVE) {
                communityId = orgCmntyRequest.getCommunityId();
            }
        }

        return communityId;
    }

    @Override
    public OrganizationDTO getOrganizationById(Long organizationId) {
        if (organizationId == null) {
            return null;
        }

        User user = UserContext.current().getUser();
        Organization organization = organizationProvider.findOrganizationById(organizationId);

        return toOrganizationDTO(user.getId(), organization);
    }

    @Override
    public OrganizationDetailDTO getOrganizationDetailById(GetOrganizationDetailByIdCommand cmd) {
        OrganizationDetailDTO organizationDetailDTO = toOrganizationDetailDTO(cmd.getId(), false);
        addServiceUser(organizationDetailDTO);
        return organizationDetailDTO;
    }

    @Override
    public OrganizationDetailDTO getOrganizationDetailWithDefaultAttachmentById(GetOrganizationDetailByIdCommand cmd) {
        OrganizationDetailDTO dto = toOrganizationDetailDTO(cmd.getId(), false);
        if(dto.getAttachments() == null || dto.getAttachments().size() == 0) {
            List<AttachmentDescriptor> attachmentDescriptors = new ArrayList<>();
            AttachmentDescriptor ad = new AttachmentDescriptor();
            ad.setContentType(PostContentType.IMAGE.getCode());
            String uri = configurationProvider.getValue("enterprise.default.attachment", "");
            ad.setContentUri(uri);
            if(uri != null) {
                ad.setContentUrl(contentServerService.parserUri(uri, EntityType.ORGANIZATIONS.getCode(), dto.getOrganizationId()));
            }
            attachmentDescriptors.add(ad);
            dto.setAttachments(attachmentDescriptors);
        }
        return dto;
    }

    /**
     * 根据organizationId来查询eh_organizations表中的总公司的organizationId
     * @param organizationId
     * @return
     */
    @Override
    public Long getTopOrganizationId(Long organizationId) {
        Organization organization = organizationProvider.findOrganizationById(organizationId);
        if (organization != null) {
            String path = organization.getPath();
            String[] ogs = path.split("/");
            return Long.valueOf(ogs[1]);
        }
        return null;
    }

    @Override
    public SearchOrganizationCommandResponse searchOrganization(
            SearchOrganizationCommand cmd) {
        SearchOrganizationCommandResponse resp = new SearchOrganizationCommandResponse();
        OrganizationQueryResult olt = this.organizationSearcher.queryOrganization(cmd);
        resp.setDtos(olt.getDtos());
        resp.setNextPageAnchor(olt.getPageAnchor());
        return resp;
    }

    @Override
    public ListOrganizationsByNameResponse listOrganizationByName(ListOrganizationsByNameCommand cmd) {
        ListOrganizationsByNameResponse resp = new ListOrganizationsByNameResponse();
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        List<Organization> orgs = organizationProvider.listOrganizationByName(locator, pageSize, namespaceId, cmd.getName());
        List<OrganizationDTO> dtos = new ArrayList<OrganizationDTO>();
        if (orgs != null) {
            for (Organization org : orgs) {
                OrganizationDTO dto = ConvertHelper.convert(org, OrganizationDTO.class);
                if (dto != null) {
                    dtos.add(dto);
                }
            }
        }

        resp.setDtos(dtos);
        resp.setNextPageAnchor(locator.getAnchor());

        return resp;
    }

    @Override
    public List<OrganizationDTO> listAllChildrenOrganizationMenusWithoutMenuStyle(Long id,
                                                                                  List<String> groupTypes, Byte naviFlag) {

        if (null == naviFlag) {
            naviFlag = OrganizationNaviFlag.SHOW_NAVI.getCode();
        }


        Organization org = this.checkOrganization(id);

        if (null == org) {
            return null;
        }

        List<Organization> orgs = organizationProvider.listOrganizationByGroupTypes(org.getPath() + "/%", groupTypes);

        if (0 == orgs.size()) {
            return null;
        }

        List<OrganizationDTO> rganizationDTOs = new ArrayList<OrganizationDTO>();
        for (Organization organization : orgs) {
            OrganizationDTO orgDto = ConvertHelper.convert(organization, OrganizationDTO.class);
            if (OrganizationNaviFlag.fromCode(naviFlag) == OrganizationNaviFlag.HIDE_NAVI) {
                if (OrganizationNaviFlag.fromCode(organization.getShowFlag()) == OrganizationNaviFlag.SHOW_NAVI) {
                    rganizationDTOs.add(orgDto);
                }
            } else {
                rganizationDTOs.add(orgDto);
            }
        }


        return rganizationDTOs;
    }


    @Override
    public CheckOfficalPrivilegeResponse checkOfficalPrivilegeByScene(CheckOfficalPrivilegeBySceneCommand cmd) {
        //Long userId = UserContext.current().getUser().getId();
        //SceneTokenDTO sceneToken = userService.checkSceneToken(userId, cmd.getSceneToken());
        AppContext appContext = UserContext.current().getAppContext();
        if (appContext == null || appContext.getOrganizationId() == null) {
            return checkOfficalPrivilege(-1L);
        }

        return checkOfficalPrivilege(appContext.getOrganizationId());
    }

    @Override
    public CheckOfficalPrivilegeResponse checkOfficalPrivilege(CheckOfficalPrivilegeCommand cmd) {
        return checkOfficalPrivilege(cmd.getOrganizationId());
    }

    @Override
    public void exportRoleAssignmentPersonnelXls(
            ExcelOrganizationPersonnelCommand cmd,
            HttpServletResponse httpResponse) {

        ListOrganizationContactCommand command = new ListOrganizationContactCommand();
        command.setKeywords(cmd.getKeywords());
        command.setOrganizationId(cmd.getOrganizationId());
        command.setPageSize(100000);
        ListOrganizationMemberCommandResponse response = this.listOrganizationPersonnelsWithDownStream(command);
        List<OrganizationMemberDTO> memberDTOs = response.getMembers();
        ByteArrayOutputStream out = null;
        XSSFWorkbook wb = this.createXSSFWorkbook(memberDTOs);
        try {
            out = new ByteArrayOutputStream();
            wb.write(out);
            DownloadUtil.download(out, httpResponse);
        } catch (Exception e) {
            LOGGER.error("export error, e = {}", e);
        } finally {
            try {
                wb.close();
                out.close();
            } catch (IOException e) {
                LOGGER.error("close error", e);
            }
        }
    }

    /**
     * 创建excel
     *
     * @param members
     * @return
     */
    @Override
    public XSSFWorkbook createXSSFWorkbook(List<OrganizationMemberDTO> members) {
        XSSFWorkbook wb = new XSSFWorkbook();
        String sheetName = "通讯录";
        XSSFSheet sheet = wb.createSheet(sheetName);
        XSSFCellStyle style = wb.createCellStyle();// 样式对象
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 20);
        font.setFontName("Courier New");

        style.setFont(font);

        XSSFCellStyle titleStyle = wb.createCellStyle();// 样式对象
        titleStyle.setFont(font);
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        int rowNum = 0;

        XSSFRow row1 = sheet.createRow(rowNum++);
        row1.setRowStyle(style);
        row1.createCell(0).setCellValue("工号");
        row1.createCell(1).setCellValue("姓名");
        row1.createCell(2).setCellValue("性别");
        row1.createCell(3).setCellValue("手机号");
        row1.createCell(4).setCellValue("部门");
        row1.createCell(5).setCellValue("岗位");
        row1.createCell(6).setCellValue("职级");

        for (OrganizationMemberDTO member : members) {
            XSSFRow row = sheet.createRow(rowNum++);
            row.setRowStyle(style);
            row.createCell(0).setCellValue(StringUtils.isEmpty(member.getEmployeeNo()) ? "" : String.valueOf(member.getEmployeeNo()));
            row.createCell(1).setCellValue(member.getContactName());
            row.createCell(2).setCellValue(null == member.getGender() ? "" : member.getGender() == 1 ? "男" : "女");
            row.createCell(3).setCellValue(member.getContactToken());
            List<OrganizationDTO> departments = member.getDepartments();
            String departmentStr = "";
            if (null != departments) {
                for (OrganizationDTO department : departments) {
                    departmentStr += "," + department.getPathName();
                }
            }

            if (!StringUtils.isEmpty(departmentStr)) {
                departmentStr = departmentStr.substring(1);
            }
            row.createCell(4).setCellValue(departmentStr);

            List<OrganizationDTO> jobPositions = member.getJobPositions();
            String jobPositionStr = "";
            if (null != jobPositions) {
                for (OrganizationDTO jobPosition : jobPositions) {
                    jobPositionStr += "," + jobPosition.getName();
                }
            }

            if (!StringUtils.isEmpty(jobPositionStr)) {
                jobPositionStr = jobPositionStr.substring(1);
            }
            row.createCell(5).setCellValue(jobPositionStr);

            List<OrganizationDTO> jobLevels = member.getJobLevels();
            String jobLevelStr = "";
            if (null != jobLevels) {
                for (OrganizationDTO jobLevel : jobLevels) {
                    jobLevelStr += "," + jobLevel.getName();
                }
            }

            if (!StringUtils.isEmpty(jobLevelStr)) {
                jobLevelStr = jobLevelStr.substring(1);
            }
            row.createCell(6).setCellValue(jobLevelStr);
        }

        return wb;
    }

    private CheckOfficalPrivilegeResponse checkOfficalPrivilege(Long organizationId) {
        CheckOfficalPrivilegeResponse response = new CheckOfficalPrivilegeResponse();
        response.setOfficialFlag((byte) 0);
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        if (organizationId != null && organizationId.longValue() >= 0) {
            Organization organization = organizationProvider.findOrganizationById(organizationId);
            if (organization != null && OrganizationType.isGovAgencyOrganization(organization.getOrganizationType())) {
                try {
                    if (resolver.checkUserPrivilege(UserContext.current().getUser().getId(), EntityType.ORGANIZATIONS.getCode(), organizationId, organizationId, PrivilegeConstants.OfficialActivity)) {
                        response.setOfficialFlag((byte) 1);
                    }
                } catch (Exception e) {
                }
            }
        }

        return response;
    }

    @Override
    public void addNewOrganizationInZuolin(AddNewOrganizationInZuolinCommand cmd) {

        if (cmd.getMobile().length() != 11 || !org.apache.commons.lang.StringUtils.isNumeric(cmd.getMobile())) {
            LOGGER.error("mobile is wrong!");
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_MOBILE_NUM,
                    "mobile is wrong!");
        }
        //没传namespaceId和communityId时加到左邻域空间的左邻园区内
        if (null == cmd.getNamespaceId()) {
            cmd.setNamespaceId(0);
        }

        if (null == cmd.getCommunityId()) {
            cmd.setCommunityId(240111044331051380L);
        }

        //没传organizationType则默认为普通公司
        if (StringUtils.isEmpty(cmd.getOrganizationType())) {
            cmd.setOrganizationType(OrganizationType.ENTERPRISE.getCode());
        }

        if (!OrganizationType.ENTERPRISE.equals(OrganizationType.fromCode(cmd.getOrganizationType()))
                && !OrganizationType.PM.equals(OrganizationType.fromCode(cmd.getOrganizationType()))) {
            LOGGER.error("organization type is wrong!");
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ORG_TYPE,
                    "organization type is wrong!");
        }

        Organization org = organizationProvider.findOrganizationByNameAndNamespaceId(cmd.getOrgName(), cmd.getNamespaceId());
        if (null != org) {
            LOGGER.error("organization already exist in the namespace!");
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ORG_EXIST,
                    "organization already exist in the namespace!");
        }

        UserIdentifier identifier = userProvider.findClaimedIdentifierByToken(cmd.getNamespaceId(), cmd.getMobile());
        if (null != identifier) {
            LOGGER.warn("User identifier token has already been claimed.");
        }

        this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_NEW_ORG.getCode()).tryEnter(() -> {
            this.dbProvider.execute((TransactionStatus status) -> {

                if (null == identifier) {
                    createUser(cmd.getNamespaceId(), cmd.getContactor(), cmd.getMobile());
                }

                //create group
                Group group = createGroup(cmd.getOrgName(), cmd.getNamespaceId());

                // create the group owned forum and save it
                Forum forum = createGroupForum(group);
                group.setOwningForumId(forum.getId());
                this.groupProvider.updateGroup(group);

                //create organization
                Organization organization = createOrganization(cmd.getOrgName(), cmd.getNamespaceId(),
                        cmd.getOrganizationType(), group.getId(), cmd.getCommunityId());

                //create administrator: add user group; add in organization member; add acl
                OrganizationMember orgMember = addIntoOrgAndAssignRole(cmd.getNamespaceId(), cmd.getContactor(), cmd.getMobile(),
                        cmd.getOrganizationType(), organization.getId());

                organizationSearcher.feedDoc(organization);
                userSearcher.feedDoc(orgMember);
                return null;
            });
        });
    }

    private OrganizationMember addIntoOrgAndAssignRole(Integer namespaceId, String contactor, String identifierToken,
                                                       String organizationType, Long orgId) {

        Organization org = this.checkOrganization(orgId);

        UserIdentifier useridentifier = userProvider.findClaimedIdentifierByToken(namespaceId, identifierToken);

        UserGroup userGroup = new UserGroup();
        userGroup.setOwnerUid(useridentifier.getOwnerUid());
        userGroup.setGroupDiscriminator(GroupDiscriminator.ENTERPRISE.getCode());
        userGroup.setGroupId(orgId);
        userGroup.setMemberRole(Role.ResourceUser);
        userGroup.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
        this.userProvider.createUserGroup(userGroup);

        OrganizationMember orgMember = new OrganizationMember();
        orgMember.setOrganizationId(orgId);
        orgMember.setTargetType(OrganizationMemberTargetType.USER.getCode());
        orgMember.setContactName(contactor);
        orgMember.setContactToken(identifierToken);
        orgMember.setContactType(ContactType.MOBILE.getCode());
        orgMember.setTargetId(useridentifier.getOwnerUid());
        orgMember.setCreatorUid(UserContext.current().getUser().getId());
        orgMember.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
        orgMember.setGroupPath(org.getPath());
        orgMember.setGroupType(org.getGroupType());
        organizationProvider.createOrganizationMember(orgMember);

        RoleAssignment roleAssignment = new RoleAssignment();
        roleAssignment.setCreatorUid(UserContext.current().getUser().getId());
        roleAssignment.setOwnerId(orgId);
        roleAssignment.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        if (OrganizationType.PM.equals(OrganizationType.fromCode(organizationType))) {
            roleAssignment.setRoleId(RoleConstants.PM_SUPER_ADMIN);
        }
        if (OrganizationType.ENTERPRISE.equals(OrganizationType.fromCode(organizationType))) {
            roleAssignment.setRoleId(RoleConstants.ENTERPRISE_SUPER_ADMIN);
        }

        roleAssignment.setTargetType(EntityType.USER.getCode());
        roleAssignment.setTargetId(useridentifier.getOwnerUid());
        roleAssignment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        aclProvider.createRoleAssignment(roleAssignment);

        return orgMember;
    }

    private Organization createOrganization(String orgName, Integer namespaceId, String organizationType, Long groupId, Long communityId) {
        Organization organization = new Organization();
        organization.setParentId(0L);
        organization.setLevel(1);
        organization.setPath("");
        organization.setName(orgName);
        organization.setGroupType(OrganizationGroupType.ENTERPRISE.getCode());
        organization.setStatus(OrganizationStatus.ACTIVE.getCode());
        organization.setOrganizationType(organizationType);
        organization.setNamespaceId(namespaceId);
        organization.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        organization.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        organization.setGroupId(groupId);
        organizationProvider.createOrganization(organization);

        OrganizationCommunityRequest organizationCommunityRequest = new OrganizationCommunityRequest();
        organizationCommunityRequest.setCommunityId(communityId);
        organizationCommunityRequest.setMemberType(OrganizationCommunityRequestType.Organization.getCode());
        organizationCommunityRequest.setMemberId(organization.getId());

        organizationCommunityRequest.setMemberStatus(OrganizationCommunityRequestStatus.ACTIVE.getCode());
        organizationCommunityRequest.setCreatorUid(UserContext.current().getUser().getId());
        organizationCommunityRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        organizationCommunityRequest.setApproveTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        this.organizationProvider.createOrganizationCommunityRequest(organizationCommunityRequest);

        return organization;
    }

    private Group createGroup(String orgName, Integer namespaceId) {
        Group group = new Group();
        group.setName(orgName);
        group.setDisplayName(orgName);
        group.setDiscriminator(GroupDiscriminator.ENTERPRISE.getCode());
        group.setPrivateFlag(GroupPrivacy.PRIVATE.getCode());
        group.setJoinPolicy(GroupJoinPolicy.NEED_APPROVE.getCode());
        group.setStatus(GroupAdminStatus.ACTIVE.getCode());
        group.setNamespaceId(namespaceId);
        group.setCreatorUid(UserContext.current().getUser().getId());
        this.groupProvider.createGroup(group);

        return group;
    }

    private Forum createGroupForum(Group group) {
        Forum forum = new Forum();
        forum.setOwnerType(EntityType.GROUP.getCode());
        forum.setOwnerId(group.getId());
        forum.setAppId(AppConstants.APPID_FORUM);
        forum.setNamespaceId(group.getNamespaceId());
        forum.setName(group.getName());
        forum.setModifySeq(0L);
        Timestamp currTime = new Timestamp(DateHelper.currentGMTTime().getTime());
        forum.setUpdateTime(currTime);
        forum.setCreateTime(currTime);

        this.forumProvider.createForum(forum);
        return forum;
    }

    private void createUser(Integer namespaceId, String nickName, String identifierToken) {

        UserIdentifier userIdentifier = createUserAndIdentifier(namespaceId, nickName, identifierToken);

        //刷新地址信息
        propertyMgrService.processUserForOwner(userIdentifier);
    }

    @Override
    public void deleteOrganizationPersonnelByContactToken(
            DeleteOrganizationPersonnelByContactTokenCommand cmd) {

        if (StringUtils.isEmpty(cmd.getContactToken())) {
            LOGGER.error("contactToken is null");
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_CONTACTTOKEN_ISNULL, "contactToken is null");
        }

        Organization organization = this.checkOrganization(cmd.getOrganizationId());

        String path = organization.getPath();
        //查询出人员在这个组织架构的所有关系（全部节点选项）
        if (DeleteOrganizationContactScopeType.ALL_NOTE == DeleteOrganizationContactScopeType.fromCode(cmd.getScopeType())) {
            if (path.indexOf("/", 1) > 0) {//如果有子节点的情况，选取第一个节点
                path = path.substring(0, path.indexOf("/", 1));
            }
            //查询当前节点的机构所属公司的所有机构跟人员的关系(当前公司选项)
        } else if (DeleteOrganizationContactScopeType.CHILD_ENTERPRISE == DeleteOrganizationContactScopeType.fromCode(cmd.getScopeType())) {
            if (OrganizationGroupType.ENTERPRISE != OrganizationGroupType.fromCode(organization.getGroupType())) {
                organization = checkOrganization(organization.getDirectlyEnterpriseId());
                path = organization.getPath();
            }
        }

        leaveOrganizationMemberByOrganizationPathAndContactToken(path, cmd.getContactToken());
    }

    @Override
    public OrganizationMemberDTO addOrganizationPersonnel(AddOrganizationPersonnelCommand cmd) {
        User user = UserContext.current().getUser();

        if (StringUtils.isEmpty(cmd.getContactToken())) {
            LOGGER.error("contactToken is null");
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_CONTACTTOKEN_ISNULL, "contactToken is null");
        }

        Organization org = checkOrganization(cmd.getOrganizationId());

        if (OrganizationGroupType.ENTERPRISE != OrganizationGroupType.fromCode(org.getGroupType())) {
            LOGGER.error("organization type not enterprise. organizationId = {}", cmd.getOrganizationId());
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER, "organization type not enterprise");
        }


        Integer namespaceId = cmd.getNamespaceId()==null?UserContext.getCurrentNamespaceId():cmd.getNamespaceId();

        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getContactToken());

        OrganizationMember organizationMember = ConvertHelper.convert(cmd, OrganizationMember.class);
        organizationMember.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
        organizationMember.setContactType(IdentifierType.MOBILE.getCode());
        organizationMember.setCreatorUid(user.getId());
        organizationMember.setNamespaceId(namespaceId);
        organizationMember.setGroupPath(org.getPath());
        organizationMember.setGroupType(org.getGroupType());
        organizationMember.setOperatorUid(user.getId());
        Byte visibleFlag = cmd.getVisibleFlag() != null ? cmd.getVisibleFlag() : Byte.valueOf("0");
        organizationMember.setVisibleFlag(visibleFlag);
        organizationMember.setGroupId(0l);
        organizationMember.setSourceType(cmd.getOperateType());
        /**Modify by lei.lv**//*
        organizationMember.setEmployeeType(cmd.getEmployeeType() !=null ? cmd.getEmployeeType() : EmployeeType.FULLTIME.getCode());
        if (cmd.getCheckInTime() != null) {
            organizationMember.setCheckInTime(java.sql.Date.valueOf(cmd.getCheckInTime()));
            organizationMember.setEmploymentTime(java.sql.Date.valueOf(cmd.getCheckInTime()));
        }
*/
        //手机号已注册，就把user id 跟通讯录关联起来
        if (null != userIdentifier) {
            organizationMember.setTargetType(OrganizationMemberTargetType.USER.getCode());
            organizationMember.setTargetId(userIdentifier.getOwnerUid());
        } else {
            organizationMember.setTargetType(OrganizationMemberTargetType.UNTRACK .getCode());
            organizationMember.setTargetId(0L);
        }


        List<String> groupTypes = new ArrayList<String>();
        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
        groupTypes.add(OrganizationGroupType.GROUP.getCode());
        groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
        groupTypes.add(OrganizationGroupType.JOB_LEVEL.getCode());
        groupTypes.add(OrganizationGroupType.JOB_POSITION.getCode());
        groupTypes.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());

//		List<Organization> childOrganizations = organizationProvider.listOrganizationByGroupTypes(org.getPath() + "/%", groupTypes);

        OrganizationMemberDTO dto = ConvertHelper.convert(organizationMember, OrganizationMemberDTO.class);

        List<OrganizationDTO> groups = new ArrayList<OrganizationDTO>();
        List<OrganizationDTO> departments = new ArrayList<OrganizationDTO>();
        List<OrganizationDTO> jobPositions = new ArrayList<OrganizationDTO>();
        List<OrganizationDTO> jobLevels = new ArrayList<OrganizationDTO>();
        List<Long> enterpriseIds = new ArrayList<>();
//        List<Long> current_detailId = new ArrayList<>();
        // 需要添加直属的企业ID集合
        List<Long> direct_under_enterpriseIds = new ArrayList<>();

        Map<Long, Boolean> joinEnterpriseMap = new HashMap<>();

        List<OrganizationMember> leaveMembers = new ArrayList<>();

        dbProvider.execute((TransactionStatus status) -> {
            this.coordinationProvider.getNamedLock(CoordinationLocks.ADD_ORGANIZATION_PERSONEL.getCode()).enter(() -> {

                List<Long> departmentIds = cmd.getDepartmentIds();

                List<Long> groupIds = cmd.getGroupIds();

                List<Long> jobPositionIds = cmd.getJobPositionIds();

                List<Long> jobLevelIds = cmd.getJobLevelIds();

                //加上部门唯一性校验
                if (cmd.getDepartmentIds() != null && cmd.getDepartmentIds().size() > 1) {
                    LOGGER.error("there are more than one department in this cmd, cmd = {}", cmd);
                    throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER, "there are more than one department in this cmd");
                }

                //部门的数据处理
                organizationPersonelsDatasProcess(enterpriseIds, departmentIds, direct_under_enterpriseIds, org);

                /**删除公司级别以下及退出公司的记录**/
                deleteOrganizaitonMemberUnderEnterprise(enterpriseIds, groupTypes, leaveMembers, cmd.getContactToken());

                Long new_detail_id = 0L;

                //加入到公司
                for (Long enterpriseId : enterpriseIds) {
                    OrganizationMember desOrgMember = this.organizationProvider.findOrganizationMemberByOrgIdAndToken(cmd.getContactToken(), enterpriseId);
                    Organization enterprise = checkOrganization(enterpriseId);
                    organizationMember.setOrganizationId(enterpriseId);
                    organizationMember.setGroupType(enterprise.getGroupType());
                    organizationMember.setGroupPath(enterprise.getPath());

                    //获取detailId
                    new_detail_id = getEnableDetailOfOrganizationMember(organizationMember, enterpriseId);

                    if (null == desOrgMember) {
                        // 记录一下，成员是新加入公司的
                        joinEnterpriseMap.put(enterpriseId, true);
                        /**Modify BY lei.lv cause MemberDetail**/
                        //绑定member表的detail_id
                        organizationMember.setDetailId(new_detail_id);
                        organizationProvider.createOrganizationMember(organizationMember);
                        //新增userOrganization表记录
                        //仅当target为user且grouptype为企业时添加
                        if (organizationMember.getTargetType().equals(OrganizationMemberTargetType.USER.getCode()) && organizationMember.getGroupType().equals(OrganizationType.ENTERPRISE.getCode())) {
                            createOrUpdateUserOrganization(organizationMember);
                        }
                    } else {
                        /**Modify BY lei.lv cause MemberDetail**/
                        //绑定member表的detail_id
                        desOrgMember.setDetailId(new_detail_id);
                        desOrgMember.setOrganizationId(organizationMember.getOrganizationId());
                        desOrgMember.setGroupType(organizationMember.getGroupType());
                        desOrgMember.setGroupPath(organizationMember.getGroupPath());
                        desOrgMember.setContactName(organizationMember.getContactName());
                        desOrgMember.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
                        desOrgMember.setVisibleFlag(organizationMember.getVisibleFlag());
                        desOrgMember.setSourceType(organizationMember.getSourceType());
                        organizationProvider.updateOrganizationMember(desOrgMember);
                        //更新userOrganization表记录
                        //仅当target为user且grouptype为企业时添加
                        if (desOrgMember.getTargetType().equals(OrganizationMemberTargetType.USER.getCode()) && desOrgMember.getGroupType().equals(OrganizationType.ENTERPRISE.getCode())) {
                            createOrUpdateUserOrganization(desOrgMember);
                        }
                    }
                }


                //添加除公司之外的机构成员
                departments.addAll(repeatCreateOrganizationmembers(departmentIds, cmd.getContactToken(), enterpriseIds, organizationMember));

                //：todo 自动加入薪酬组
                this.uniongroupService.reallocatedUnion(org.getId(), departmentIds, organizationMember);

                groups.addAll(repeatCreateOrganizationmembers(groupIds, cmd.getContactToken(), enterpriseIds, organizationMember));
                jobPositions.addAll(repeatCreateOrganizationmembers(jobPositionIds, cmd.getContactToken(), enterpriseIds, organizationMember));
                jobLevels.addAll(repeatCreateOrganizationmembers(jobLevelIds, cmd.getContactToken(), enterpriseIds, organizationMember));

                dto.setGroups(groups);

                dto.setDepartments(departments);

                dto.setJobPositions(jobPositions);

                dto.setJobLevels(jobLevels);

                dto.setMemberDetailIds(Collections.singletonList(new_detail_id));

                dto.setDetailId(new_detail_id);

                return null;
            });

            return null;
        });


        // 如果有加入的公司 已注册用户需要发送加入公司的消息等系列操作 add sfyan 20170818
        if (OrganizationMemberTargetType.fromCode(organizationMember.getTargetType()) == OrganizationMemberTargetType.USER) {
            for (Long enterpriseId : enterpriseIds) {
                //是往公司添加新成员就需要发消息
                if (null != joinEnterpriseMap.get(enterpriseId) && joinEnterpriseMap.get(enterpriseId)) {
                    organizationMember.setOrganizationId(enterpriseId);
                    joinOrganizationAfterOperation(organizationMember,false);
                }
                else{
                	//始终都要发消息
                    organizationMember.setOrganizationId(enterpriseId);
                	//2018年10月17日 修改为新注册发消息,其它不发消息
//                    sendMessageForContactApproved(organizationMember);
                }
            }
            // 如果有退出的公司 需要发离开公司的消息等系列操作 add by sfyan  20170428
            if (leaveMembers.size() > 0) {
                leaveOrganizationAfterOperation(user.getId(), leaveMembers);
            }
        }
        
        // 用户通过认证事件
        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(organizationMember.getTargetId());
            context.setNamespaceId(namespaceId);
            event.setContext(context);

            event.setEntityType(EntityType.USER.getCode());
            event.setEntityId(organizationMember.getTargetId());
            event.setEventName(SystemEvent.ACCOUNT_AUTH_SUCCESS.dft());
            Map<String, Object> params = new HashMap<>();
            params.put("orgId", organizationMember.getGroupPath().split("/")[1]);
            event.setParams(params);
            LOGGER.info("publish event :[{}]",event);
        });
        
        return dto;
    }

    /**
     * 去重
     *
     * @param ids
     */
    private void removeRepeat(List<Long> ids) {
        List<Long> results = new ArrayList<>();
        for (Long id : ids) {
            if (!results.contains(id)) {
                results.add(id);
            }
        }
        ids.removeAll(ids);
        ids.addAll(results);
    }

    @Override
    public OrganizationDTO getMemberTopDepartment(List<String> groupTypes, String token, Long organizationId) {

        Organization organization = checkOrganization(organizationId);

        List<OrganizationDTO> dtos = getOrganizationMemberGroups(groupTypes, token, organization.getPath());

        if (null == dtos || 0 == dtos.size()) {
            return ConvertHelper.convert(organization, OrganizationDTO.class);
        }
        //检查是否有公司直属的记录
        for (OrganizationDTO dto : dtos) {
            if (dto.getGroupType().equals(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode())) {
                //找到直属部门的上级公司
                Organization enterprise = checkOrganization(dto.getParentId());
                if (enterprise != null) {
                    OrganizationDTO enterpriseDTO = ConvertHelper.convert(checkOrganization(dto.getParentId()), OrganizationDTO.class);
                    return enterpriseDTO;
                }
            }
        }

        //如果没有直属记录，则返回首位层级最高的部门
        OrganizationDTO topDepartment = null;
        Integer topDepartmentNum = 10000;
        for (OrganizationDTO dto : dtos) {
            if (dto.getPath().split("/").length < topDepartmentNum) {
                topDepartmentNum = dto.getPath().split("/").length;
                topDepartment = dto;
            }
        }

        return topDepartment;
    }


    /**
     * modify By lei.lv 2017-6-26
     **/
    @Override
    public List<OrganizationMemberDTO> listAllChildOrganizationPersonnel(Long organizationId, List<String> groupTypes, String userName) {
        Organization organization = checkOrganization(organizationId);
        List<OrganizationMember> members = this.organizationProvider.listOrganizationMemberByPath(userName, organization.getPath(), groupTypes, VisibleFlag.SHOW, new CrossShardListingLocator(), 1000000);
        List<OrganizationMemberDTO> dtos = members.stream().map(r -> {
            return ConvertHelper.convert(r, OrganizationMemberDTO.class);
        }).collect(Collectors.toList());
        return dtos;
    }

    @Override
    public void updateOrganizationContactVisibleFlag(UpdateOrganizationContactVisibleFlagCommand cmd) {

        Organization organization = this.checkOrganization(cmd.getOrganizationId());

        if (OrganizationGroupType.fromCode(organization.getGroupType()) != OrganizationGroupType.ENTERPRISE) {
            organization = this.checkOrganization(organization.getDirectlyEnterpriseId());
        }

        VisibleFlag visibleFlag = VisibleFlag.fromCode(cmd.getVisibleFlag());

        if (null == visibleFlag) {
            visibleFlag = VisibleFlag.SHOW;
        }

        List<String> groupTypeList = new ArrayList<String>();
        groupTypeList.add(OrganizationGroupType.GROUP.getCode());
        groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
        groupTypeList.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
        groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());

        List<Organization> organizations = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "/%", groupTypeList);

        List<Long> organizationIds = new ArrayList<Long>();
        organizationIds.add(organization.getId());
        for (Organization org : organizations) {
            organizationIds.add(org.getId());
        }

        List<OrganizationMember> members = organizationProvider.listOrganizationMemberByTokens(cmd.getContactToken(), organizationIds);

        for (OrganizationMember member : members) {
            member.setVisibleFlag(visibleFlag.getCode());
            organizationProvider.updateOrganizationMember(member);
        }

    }

    @Override
    public void batchUpdateOrganizationContactVisibleFlag(BatchUpdateOrganizationContactVisibleFlagCommand cmd) {
        List<String> contactTokens = cmd.getContactTokens();

        //参数不传代表默认操作全部的通讯录隐藏显示
        if (null == contactTokens) {
            List<OrganizationMember> members = this.organizationProvider.listOrganizationMembersByOrgId(cmd.getOrganizationId());
            if(null !=members) {
                contactTokens = members.stream().map(r -> {
                    return r.getContactToken();
                }).collect(Collectors.toList());
            }
        }
        if(null != contactTokens)
            for (String contactToken : contactTokens) {
                UpdateOrganizationContactVisibleFlagCommand command = new UpdateOrganizationContactVisibleFlagCommand();
                command.setVisibleFlag(cmd.getVisibleFlag());
                command.setOrganizationId(cmd.getOrganizationId());
                command.setContactToken(contactToken);
                this.updateOrganizationContactVisibleFlag(command);
            }
    }

    @Override
    public OrganizationTreeDTO listAllTreeOrganizations(ListAllTreeOrganizationsCommand cmd) {

        Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
        if (org == null) {
            return new OrganizationTreeDTO();
        }
        List<Organization> organizations = new ArrayList<Organization>();
        List<String> groupTypeList = new ArrayList<String>();
        groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
        List<Organization> enterprises = organizationProvider.listOrganizationByGroupTypes(org.getPath() + "/%", groupTypeList);
        organizations.addAll(enterprises);
        groupTypeList = new ArrayList<String>();
        groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
        List<Organization> departments = organizationProvider.listOrganizationByGroupTypes(org.getPath() + "/%", groupTypeList);
        organizations.addAll(departments);

        OrganizationTreeDTO dto = ConvertHelper.convert(org, OrganizationTreeDTO.class);
        dto.setOrganizationId(org.getId());
        dto.setOrganizationName(org.getName());
        List<OrganizationTreeDTO> organizationTreeDTOs = new ArrayList<OrganizationTreeDTO>();
        for (Organization organization : organizations) {
            OrganizationTreeDTO orgTreeDTO = ConvertHelper.convert(organization, OrganizationTreeDTO.class);
            orgTreeDTO.setOrganizationId(organization.getId());
            orgTreeDTO.setOrganizationName(organization.getName());
//			if(organization.getDirectlyEnterpriseId().equals(org.getId())){
            organizationTreeDTOs.add(orgTreeDTO);
//			}
        }

        dto = this.processOrganizationTree(organizationTreeDTOs, dto);
        return dto;
    }

    @Override
    public List<OrganizationDTO> listAllPmOrganizations() {
        List<Organization> organizations = organizationProvider.listOrganizations(OrganizationType.PM.getCode(), null,
                0L, null, null);
        return organizations.stream().map(r -> ConvertHelper.convert(r, OrganizationDTO.class)).collect(Collectors.toList());
    }


    @Override
    public OrganizationDTO listPmOrganizationsByNamespaceId(Integer namespaceId) {
        List<Organization> organizations = organizationProvider.listOrganizations(OrganizationType.PM.getCode(), namespaceId,
                0L, null, null);
        return ConvertHelper.convert(organizations.get(0), OrganizationDTO.class);
    }

    /**
     * 机构树状处理
     *
     * @param dtos
     * @param dto
     * @return
     */
    private OrganizationTreeDTO processOrganizationTree(List<OrganizationTreeDTO> dtos, OrganizationTreeDTO dto) {

        List<OrganizationTreeDTO> trees = new ArrayList<OrganizationTreeDTO>();
        OrganizationTreeDTO allTreeDTO = ConvertHelper.convert(dto, OrganizationTreeDTO.class);
        allTreeDTO.setOrganizationName("全部");
        allTreeDTO.setOrder(Integer.valueOf(-1));
        trees.add(allTreeDTO);
        for (OrganizationTreeDTO orgTreeDTO : dtos) {
            if (orgTreeDTO.getParentId().equals(dto.getOrganizationId())) {
                OrganizationTreeDTO organizationTreeDTO = processOrganizationTree(dtos, orgTreeDTO);
                organizationTreeDTO.getTrees();
                trees.add(organizationTreeDTO);
            }
        }
        //同级排序
        trees.sort(Comparator.comparingInt(OrganizationTreeDTO::getOrder));
        dto.setTrees(trees);
        return dto;
    }

    /* 仅仅由企业通讯录调用,其它业务不再提供维护 */
    @Override
    public ListOrganizationContactCommandResponse listOrganizationContacts(ListOrganizationContactCommand cmd) {
        ListOrganizationContactCommandResponse response = new ListOrganizationContactCommandResponse();
        Organization org = this.checkOrganization(cmd.getOrganizationId());
        if(null == org)
            return response;
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        if (null == cmd.getNamespaceId()) {
            cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        }
        ListArchivesContactsCommand cmd1 = new ListArchivesContactsCommand();
        cmd1.setFilterScopeTypes(Collections.singletonList(FilterOrganizationContactScopeType.CURRENT.getCode()));
        cmd1.setKeywords(cmd.getKeywords());
        cmd1.setNamespaceId(cmd.getNamespaceId());
        cmd1.setOrganizationId(cmd.getOrganizationId());
        cmd1.setPageSize(pageSize);
		ListArchivesContactsResponse resp1 = archivesService.listArchivesContacts(cmd1);
		if(resp1.getContacts() == null)
            return response;
        //  默认（包含本节点及下级部门）
		List<ArchivesContactDTO> organizationMembers = resp1.getContacts();

        //  校验权限（管理员）
        Long enterpriseId = getTopOrganizationId(cmd.getOrganizationId());
        boolean temp;
        try{
            temp = userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), enterpriseId, PrivilegeConstants.CREATE_OR_MODIFY_PERSON, FlowConstants.ORGANIZATION_MODULE, null, null, cmd.getOrganizationId(), null);
        }catch (Exception e){
            temp = false;
        }
        boolean adminFlag = temp;

        List<OrganizationContactDTO> members = organizationMembers.stream().map(r -> {
            OrganizationContactDTO dto = ConvertHelper.convert(r, OrganizationContactDTO.class);
            try{
	            dto.setId(r.getDetailId());
				dto.setContactName(r.getContactName());
				dto.setContactToken(r.getContactToken());
				dto.setTargetId(r.getTargetId());
				//增加岗位显示与 detailId added by ryan 20120713
				dto.setDetailId(r.getDetailId());
				
	            //  2.detailId, 隐藏性信息
	            dto.setVisibleFlag(r.getVisibleFlag());
	
	            
	            //  1.添加职位
	            dto.setJobPosition(archivesService.convertToOrgNames(archivesService.getEmployeeJobPosition(r.getDetailId())));
	            //  3.头像
	            if (OrganizationMemberTargetType.USER == OrganizationMemberTargetType.fromCode(r.getTargetType())) {
	                User user = userProvider.findUserById(r.getTargetId());
	                if (null != user) {
	                    String avatarUri = user.getAvatar();
	                    if (StringUtils.isEmpty(avatarUri))
	                        avatarUri = userService.getUserAvatarUriByGender(user.getId(), user.getNamespaceId(), user.getGender());
	
	                    dto.setAvatar(contentServerService.parserUri(avatarUri, EntityType.USER.getCode(), user.getId()));
	                }
	            } else {
	                String avatarUri = userService.getUserAvatarUriByGender(0L, UserContext.getCurrentNamespaceId(), dto.getGender());
	                dto.setAvatar(contentServerService.parserUri(avatarUri, EntityType.USER.getCode(), 0L));
	            }
	            //  4.手机号屏蔽
	            if (!adminFlag)
	                if (VisibleFlag.fromCode(dto.getVisibleFlag()) == VisibleFlag.HIDE) {
	                    dto.setContactToken(null);
	                }
            }catch(Exception e){
            	LOGGER.error("listOrganizationContacts convert ArchivesContactDTO to OrganizationContactDTO has error! ArchivesContactDTO = " + r.toString(),e);
            }
            return dto;
        }).collect(Collectors.toList());

        //  拼音及排序
        try{
        	members = convertPinyin(members);
        }catch(Exception e){
        	LOGGER.error("convertPinyin has error! ",e);
        }
        response.setNextPageAnchor(resp1.getNextPageAnchor());
        response.setMembers(members);
        return response;
    }

    private List<OrganizationContactDTO> convertPinyin(List<OrganizationContactDTO> members) {

        members = members.stream().peek((c) -> {
            String pinyin = PinYinHelper.getPinYin(c.getContactName());
            c.setFullInitial(PinYinHelper.getFullCapitalInitial(pinyin));
            c.setFullPinyin(pinyin.replaceAll(" ", ""));
            c.setInitial(PinYinHelper.getCapitalInitial(c.getFullPinyin()));
            //  其他字符置换成#号
            if (!StringUtils.isEmpty(c.getInitial())) {
                c.setInitial(c.getInitial().replace("~", "#"));
            }
        }).collect(Collectors.toList());

        Collections.sort(members);

        return members;
    }


    @Override
    public OrganizationDTO getContactTopDepartment(GetContactTopDepartmentCommand cmd) {
        Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
        if (org == null) {
            return new OrganizationDTO();
        }
        Long userId = UserContext.current().getUser().getId();
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode());
        List<String> groupTypes = new ArrayList<String>();
        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
        return this.getMemberTopDepartment(groupTypes, userIdentifier.getIdentifierToken(), cmd.getOrganizationId());
    }

    private void setUserDefaultCommunityByOrganization(Integer namespaceId, Long userId, Long oranizationId) {
        Long communityId = getOrganizationActiveCommunityId(oranizationId);
        if (communityId != null) {
            userService.updateUserCurrentCommunityToProfile(userId, communityId, namespaceId);
        } else {
            LOGGER.error("Community not found in organization, userId={}, namespaceId={}, organizationId={}",
                    userId, namespaceId, oranizationId);
        }
    }

    @Override
    public ListOrganizationJobPositionResponse listOrganizationJobPositions(ListOrganizationJobPositionCommand cmd) {
        ListOrganizationJobPositionResponse response = new ListOrganizationJobPositionResponse();

        Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

        checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());

        List<OrganizationJobPosition> list = organizationProvider.listOrganizationJobPositions(cmd.getOwnerType(), cmd.getOwnerId(),
                cmd.getKeywords(), cmd.getPageAnchor(), cmd.getPageSize());

        int size = list.size();
        if (size > 0) {
            response.setRequests(list.stream().map(r -> {
                OrganizationJobPositionDTO dto = ConvertHelper.convert(r, OrganizationJobPositionDTO.class);
                return dto;
            }).collect(Collectors.toList()));

            if (size != pageSize) {
                response.setNextPageAnchor(null);
            } else {
                response.setNextPageAnchor(list.get(list.size() - 1).getId());
            }
        }

        return response;
    }

    @Override
    public ContractDTO processContract(Contract contract) {
        return processContract(contract, null);
    }

    @Override
    public ContractDTO processContract(Contract contract, Integer namespaceId) {
        namespaceId = namespaceId == null ? UserContext.getCurrentNamespaceId() : namespaceId;

        ContractDTO contractDTO = ConvertHelper.convert(contract, ContractDTO.class);
        contractDTO.setContractNumber(contract.getContractNumber());
        contractDTO.setContractEndDate(contract.getContractEndDate());
        contractDTO.setOrganizationName(contract.getCustomerName());
        //应该从合同中拿到客户id，根据客户id查客户，从客户表中拿到organizationid by xiongying 201708
        contractDTO.setAdminMembers(getAdmins(contract.getCustomerId()));
        contractDTO.setSignupCount(getSignupCount(contract.getCustomerId()));

        OrganizationDetail organizationDetail = organizationProvider.findOrganizationDetailByOrganizationId(contract.getCustomerId());
        if (organizationDetail != null) {
            contractDTO.setContract(organizationDetail.getContact());
            contractDTO.setContactor(organizationDetail.getContactor());
            contractDTO.setServiceUserId(organizationDetail.getServiceUserId());

            OrganizationServiceUser user = getServiceUser(contract.getCustomerId(), organizationDetail.getServiceUserId());
            if (user != null) {
                contractDTO.setServiceUserId(organizationDetail.getServiceUserId());
                contractDTO.setServiceUserName(user.getServiceUserName());
                contractDTO.setServiceUserPhone(user.getServiceUserPhone());
            }
        }

        List<BuildingApartmentDTO> buildings = contractBuildingMappingProvider.listBuildingsByContractNumber(namespaceId, contractDTO.getContractNumber());
        contractDTO.setBuildings(buildings);
        return contractDTO;
    }

    @Override
    public void createOrganizationJobPosition(CreateOrganizationJobPositionCommand cmd) {

        //权限
        checkOrganizationPrivilege(cmd.getOwnerId(),PrivilegeConstants.CREATE_JOB_POSITION);

        checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
        checkName(cmd.getName());

        OrganizationJobPosition organizationJobPosition = organizationProvider.findOrganizationJobPositionByName(
                cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName());

        if (null != organizationJobPosition) {
            LOGGER.error("OrganizationJobPosition in existing.");
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ORG_JOB_POSITION_EXISTS,
                    "OrganizationJobPosition in existing.");
        }

        User user = UserContext.current().getUser();

        organizationJobPosition = new OrganizationJobPosition();
        organizationJobPosition.setName(cmd.getName());
        organizationJobPosition.setCreatorUid(user.getId());
        organizationJobPosition.setNamespaceId(user.getNamespaceId());
        organizationJobPosition.setOwnerId(cmd.getOwnerId());
        organizationJobPosition.setOwnerType(cmd.getOwnerType());
        organizationJobPosition.setStatus(OrganizationJobPositionStatus.ACTIVE.getCode());
        organizationProvider.createOrganizationJobPosition(organizationJobPosition);

    }

    @Override
    public Long updateOrganizationJobPosition(UpdateOrganizationJobPositionCommand cmd) {
        // 权限
        checkId(cmd.getId());

        OrganizationJobPosition organizationJobPosition = checkOrganizationJobPositionIsNull(cmd.getId());

        checkOrganizationPrivilege(organizationJobPosition.getOwnerId(),PrivilegeConstants.MODIFY_JOB_POSITION);


        OrganizationJobPosition organizationJobPosition_sameName = organizationProvider.findOrganizationJobPositionByName(
                organizationJobPosition.getOwnerType(), organizationJobPosition.getOwnerId(), cmd.getName());

        if(organizationJobPosition_sameName != null){
            return Long.valueOf(OrganizationServiceErrorCode.ERROR_JOB_POSITION_EXISTS);
        }

        organizationJobPosition.setName(cmd.getName());
        organizationProvider.updateOrganizationJobPosition(organizationJobPosition);
        return organizationJobPosition.getId();
    }

    @Override
    public Boolean  deleteOrganizationJobPosition(DeleteOrganizationIdCommand cmd) {

        checkId(cmd.getId());

        OrganizationJobPosition organizationJobPosition = checkOrganizationJobPositionIsNull(cmd.getId());

        // 权限
        checkOrganizationPrivilege(organizationJobPosition.getOwnerId(),PrivilegeConstants.DELETE_JOB_POSITION);


        if(cmd.getEnterpriseId() != null){
            //:todo 删除时置空判断
            List<OrganizationMember> emptyMember = this.listOrganizationContactByJobPositionId(cmd.getEnterpriseId(), cmd.getId());
            if(emptyMember != null && emptyMember.size() > 0){
                return false;
            }
        }

        organizationJobPosition.setStatus(OrganizationJobPositionStatus.INACTIVE.getCode());
        organizationProvider.updateOrganizationJobPosition(organizationJobPosition);

        return true;
    }

    public List<OrganizationDTO> listOrganizationsByEmail(ListOrganizationsByEmailCommand cmd) {
        Long userId = UserContext.current().getUser().getId();
//		SceneTokenDTO sceneToken = userService.checkSceneToken(userId, cmd.getSceneToken());
        //通过namespace和email domain 找企业
        String emailDomain = cmd.getEmail().substring(cmd.getEmail().indexOf("@") + 1);
        List<Organization> organizations = this.organizationProvider.listOrganizationByEmailDomainAndNamespace(UserContext.getCurrentNamespaceId(), emailDomain, cmd.getCommunityId());
        //TODO: 判断邮箱是否被使用
        OrganizationMember member = organizationProvider.getOrganizationMemberByContactToken(UserContext.getCurrentNamespaceId(), cmd.getEmail());
        if (null != member) {
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_EMAIL_REPEAT,
                    "email already exists");
        }
        if (null == organizations || organizations.size() == 0) {
            return null;
        }
        //如果只有一个公司,直接认证
//		if(organizations.size() == 1){
//			ApplyForEnterpriseContactByEmailCommand cmd2 = ConvertHelper.convert(cmd, ApplyForEnterpriseContactByEmailCommand.class);
//			cmd2.setOrganizationId(organizations.get(0).getId());
//			applyForEnterpriseContactByEmail(cmd2);
//		}
        return organizations.stream().map(r -> {
            OrganizationDTO dto = processOrganizationCommunity(ConvertHelper.convert(r, OrganizationDTO.class));
            OrganizationDetail detail = organizationProvider.findOrganizationDetailByOrganizationId(dto.getId());
            if (null != detail) {
                dto.setDisplayName(detail.getDisplayName());
            }
            OrganizationMember m = organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, r.getId());
            if (null != m) {
                dto.setMemberStatus(m.getStatus());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void applyForEnterpriseContactByEmail(ApplyForEnterpriseContactByEmailCommand cmd) {
        Long userId = UserContext.current().getUser().getId();
//		SceneTokenDTO sceneToken = userService.checkSceneToken(userId, cmd.getSceneToken());
        //用户认证邮箱认证时，校验邮箱是否已经被认证.
        OrganizationMemberDetails details = this.organizationProvider.findOrganizationMemberDetailsByEmail(cmd.getEmail(), cmd.getOrganizationId());
        if (details != null) {
            LOGGER.error("email is exists, email ={}", cmd.getEmail());
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_EMAIL_IS_EXISTS,
                    "email is exists, email ={}",cmd.getEmail());
        }
        VerifyEnterpriseContactDTO dto = ConvertHelper.convert(cmd, VerifyEnterpriseContactDTO.class);
        dto.setUserId(userId);
        dto.setEnterpriseId(cmd.getOrganizationId());

        // 添加联系人
        CreateOrganizationMemberCommand cmd2 = new CreateOrganizationMemberCommand();
        cmd2.setContactType(ContactType.MOBILE.getCode());
        UserIdentifier useridentifier = this.getUserMobileIdentifier(userId);
        cmd2.setContactToken(useridentifier.getIdentifierToken());
        cmd2.setOrganizationId(cmd.getOrganizationId());
        cmd2.setTargetType(OrganizationMemberTargetType.USER.getCode());
        cmd2.setTargetId(userId);
        cmd2.setContactDescription(cmd.getContactDescription());
        cmd2.setContactName(cmd.getContactName());
        cmd2.setEmail(cmd.getEmail());
        applyForEnterpriseContact(cmd2);
        //目前写死30分钟
        dto.setEndTime(DateHelper.currentGMTTime().getTime() + 30 * 60 * 1000L);
        String verifyToken = WebTokenGenerator.getInstance().toWebToken(dto);
        String host = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), "home.url", "");
        String verifyUrl = host + "/evh/org/verifyEnterpriseContact?verifyToken=" + verifyToken;
        //TODO: send email
        Map<String, Object> map = new HashMap<String, Object>();
        String nickName = UserContext.current().getUser().getNickName();
        String account = configProvider.getValue(UserContext.getCurrentNamespaceId(), "mail.smtp.account", "zuolin@zuolin.com");
        String locale = "zh_CN";
        map.put("nickName", null == nickName ? useridentifier.getIdentifierToken().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2") : nickName);
        Namespace namespace = namespaceProvider.findNamespaceById(UserContext.getCurrentNamespaceId());
        String appName = "左邻";
        if (null != namespace && namespace.getName() != null)
            appName = namespace.getName();
        map.put("appName", appName);
        map.put("verifyUrl", verifyUrl);
        String mailSubject = this.localeStringService.getLocalizedString(VerifyMailTemplateCode.SCOPE,
                VerifyMailTemplateCode.SUBJECT_CODE, RentalNotificationTemplateCode.locale, "加入企业验证邮件");
        map.put("title", mailSubject);
        String mailText = localeTemplateService.getLocaleTemplateString(VerifyMailTemplateCode.SCOPE, VerifyMailTemplateCode.TEXT_CODE, locale, map, "");

//        LOGGER.debug("\n mailText = " + mailText);
//		Email email = new EmailBuilder()
//	    .from(appName,account)
//	    .to(UserContext.current().getUser().getNickName(), cmd.getEmail())
//	    .subject(mailSubject)
//	    .text(mailText)
//	    .build();
        try {
            String address = configProvider.getValue(UserContext.getCurrentNamespaceId(), "mail.smtp.address", "smtp.mxhichina.com");
            String passwod = configProvider.getValue(UserContext.getCurrentNamespaceId(), "mail.smtp.passwod", "abc123!@#");
            int port = configProvider.getIntValue(UserContext.getCurrentNamespaceId(), "mail.smtp.port", 25);
//            LOGGER.debug("\n mail text : " + mailText);
//			new Mailer(address, port , account , passwod).sendMail(email);
            //另一种发送方式
            String handlerName = MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP;
            MailHandler handler = PlatformContext.getComponent(handlerName);

            handler.sendMail(UserContext.getCurrentNamespaceId(), account, cmd.getEmail(), mailSubject, mailText);

        } catch (Exception e) {
            LOGGER.debug("had a error in send message !!!!!++++++++++++++++++++++");
            LOGGER.error(e.getMessage());

            e.printStackTrace();
            // LOGGER.error(e.getStackTrace());
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_SEND_EMAIL,
                    "send email error");
        }

    }

    @Override
    public String verifyEnterpriseContact(VerifyEnterpriseContactCommand cmd) {
        try {
            VerifyEnterpriseContactDTO dto = WebTokenGenerator.getInstance().fromWebToken(cmd.getVerifyToken(), VerifyEnterpriseContactDTO.class);
            if (dto == null || dto.getEndTime() == null || dto.getEnterpriseId() == null || dto.getUserId() == null) {
                return configProvider.getValue("auth.fail", "");
            }
            if (DateHelper.currentGMTTime().getTime() > dto.getEndTime())
                return configProvider.getValue("auth.overtime", "");

            ApproveContactCommand cmd2 = ConvertHelper.convert(dto, ApproveContactCommand.class);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("email verify approveForEnterpriseContact cmd2 = {}", cmd2);
            }
            cmd2.setOperateType(OperateType.EMAIL.getCode());
            approveForEnterpriseContact(cmd2);

            String success = configProvider.getValue("auth.success", "");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("email verify auth success redirectUrl = {}", success);
            }

            return success;
        } catch (Exception e) {
            LOGGER.error("email verify enterprise contact error, token = {}", cmd.getVerifyToken());
            return configProvider.getValue("auth.fail", "");
        }
    }

    private void checkName(String name) {
        if (StringUtils.isEmpty(name)) {
            LOGGER.error("Name cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Name cannot be null.");
        }
    }

    private void checkId(Long id) {
        if (null == id) {
            LOGGER.error("Id cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Id cannot be null.");
        }
    }

    private void checkOwnerIdAndOwnerType(String ownerType, Long ownerId) {
        if (null == ownerId) {
            LOGGER.error("OwnerId cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "OwnerId cannot be null.");
        }

        if (StringUtils.isEmpty(ownerType)) {
            LOGGER.error("OwnerType cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "OwnerType cannot be null.");
        }
    }

    private OrganizationJobPosition checkOrganizationJobPositionIsNull(Long id) {

        OrganizationJobPosition organizationJobPosition = organizationProvider.findOrganizationJobPositionById(id);

        if (null == organizationJobPosition) {
            LOGGER.error("OrganizationJobPosition not found, id={}", id);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "OrganizationJobPosition not found.");
        }

        return organizationJobPosition;
    }

    @Override
    public void createChildrenOrganizationJobLevel(CreateOrganizationCommand cmd) {
        checkName(cmd.getName());
        cmd.setGroupType(OrganizationGroupType.JOB_LEVEL.getCode());
        createChildrenOrganization(cmd);
    }

    @Override
    public void updateChildrenOrganizationJobLevel(UpdateOrganizationsCommand cmd) {
        updateChildrenOrganization(cmd);

    }

    @Override
    public Boolean deleteChildrenOrganizationJobLevel(DeleteOrganizationIdCommand cmd) {
       return deleteOrganization(cmd);

    }

    @Override
    public ListChildrenOrganizationJobLevelResponse listChildrenOrganizationJobLevels(ListAllChildrenOrganizationsCommand cmd) {

        if (null == cmd.getNamespaceId()) {
            cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        }
        checkId(cmd.getId());
        Organization organization = organizationProvider.findOrganizationById(cmd.getId());
        if (null == organization) {
            LOGGER.error("Organization not found, cmd={}", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Organization not found.");
        }
        Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

        ListChildrenOrganizationJobLevelResponse response = new ListChildrenOrganizationJobLevelResponse();

        List<String> groupTypes = new ArrayList<String>();
        groupTypes.add(OrganizationGroupType.JOB_LEVEL.getCode());
        List<Organization> list = organizationProvider.listOrganizationByGroupTypes(cmd.getId(), groupTypes, null, cmd.getPageAnchor(), pageSize);

        int size = list.size();
        if (size > 0) {
            response.setRequests(list.stream().map(r -> {
                ChildrenOrganizationJobLevelDTO dto = ConvertHelper.convert(r, ChildrenOrganizationJobLevelDTO.class);
                Organization orgCommoand = new Organization();
                orgCommoand.setId(r.getId());
                orgCommoand.setStatus(OrganizationMemberStatus.ACTIVE.getCode());

                CrossShardListingLocator locator = new CrossShardListingLocator();
                locator.setAnchor(cmd.getPageAnchor());
                List<OrganizationMember> organizationMembers = organizationProvider.listOrganizationPersonnels(cmd.getNamespaceId(),
                        cmd.getKeywords(), orgCommoand, null, null, locator, 10000);
                dto.setMembers(organizationMembers.stream().map(m -> ConvertHelper.convert(m, OrganizationMemberDTO.class))
                        .collect(Collectors.toList()));
                dto.setParentName(organization.getName());
                return dto;
            }).collect(Collectors.toList()));

            if (size != pageSize) {
                response.setNextPageAnchor(null);
            } else {
                response.setNextPageAnchor(list.get(list.size() - 1).getId());
            }
        }
        return response;
    }

    @Override
    public void createChildrenOrganizationJobPosition(CreateOrganizationCommand cmd) {
//		checkOwnerIdAndOwnerType(cmd.getownerType, ownerId);
        //权限校验
        checkOrganizationPrivilege(cmd.getParentId(),PrivilegeConstants.MODIFY_DEPARTMENT_JOB_POSITION);
        checkName(cmd.getName());
        cmd.setGroupType(OrganizationGroupType.JOB_POSITION.getCode());
        createChildrenOrganization(cmd);
    }

    @Override
    public void updateChildrenOrganizationJobPosition(UpdateOrganizationsCommand cmd) {
        updateChildrenOrganization(cmd);
    }

    @Override
    public Boolean deleteChildrenOrganizationJobPosition(DeleteOrganizationIdCommand cmd) {
        return deleteOrganization(cmd);
    }

    @Override
    public ListChildrenOrganizationJobPositionResponse listChildrenOrganizationJobPositions(ListAllChildrenOrganizationsCommand cmd) {

        if (null == cmd.getNamespaceId()) {
            cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        }
        // add 2017.11.22
//        if(cmd.getId() == null){
//            groupTypes.add(OrganizationGroupType.JOB_POSITION.getCode());
//            organizationProvider.listOrganizationMemberByPath()
//            List<Organization> list = organizationProvider.listOrganizationByGroupTypes(cmd.getId(), groupTypes, cmd.getKeywords(), cmd.getPageAnchor(), pageSize);
//        }

        checkId(cmd.getId());
        Organization organization = organizationProvider.findOrganizationById(cmd.getId());
        if (null == organization) {
            LOGGER.error("Organization not found, cmd={}", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Organization not found.");
        }

        Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize()) + 1;

        ListChildrenOrganizationJobPositionResponse response = new ListChildrenOrganizationJobPositionResponse();

        List<String> groupTypes = new ArrayList<String>();
        groupTypes.add(OrganizationGroupType.JOB_POSITION.getCode());
        List<Organization> list = new ArrayList<>();
        if(organization.getGroupType().equals(OrganizationGroupType.ENTERPRISE.getCode()) && organization.getParentId() == 0L) {//传入的id为总公司时
            list = organizationProvider.listOrganizationByGroupTypesAndPath(organization.getPath() + "%",groupTypes, cmd.getKeywords(), cmd.getPageAnchor(), pageSize);
        }else{
            list = organizationProvider.listOrganizationByGroupTypes(cmd.getId(), groupTypes, cmd.getKeywords(), cmd.getPageAnchor(), pageSize);
        }

        int size = list.size();
        if (size > 0) {

            if (size != pageSize) {
                response.setNextPageAnchor(null);
            } else {
                list.remove(list.size() - 1);
                response.setNextPageAnchor(list.get(list.size() - 1).getId());
            }
            
            response.setRequests(list.stream().map(r -> {
                ChildrenOrganizationJobPositionDTO dto = ConvertHelper.convert(r, ChildrenOrganizationJobPositionDTO.class);
                dto.setParentName(organization.getName());
                List<OrganizationJobPositionMap> organizationJobPositionMaps = organizationProvider.listOrganizationJobPositionMaps(r.getId());
                List<OrganizationJobPositionDTO> organizationJobPositionDTOs = new ArrayList<OrganizationJobPositionDTO>();
                organizationJobPositionMaps.stream().map(o -> {
                    OrganizationJobPosition organizationJobPosition = organizationProvider.findOrganizationJobPositionById(o.getJobPositionId());
                    organizationJobPositionDTOs.add(ConvertHelper.convert(organizationJobPosition, OrganizationJobPositionDTO.class));
                    return null;
                }).collect(Collectors.toList());
                dto.setJobPositions(organizationJobPositionDTOs);

                Organization orgCommoand = new Organization();
                orgCommoand.setId(r.getId());
                orgCommoand.setStatus(OrganizationMemberStatus.ACTIVE.getCode());

                CrossShardListingLocator locator = new CrossShardListingLocator();
                locator.setAnchor(cmd.getPageAnchor());
                if(cmd.getSimpleFlag() == null || cmd.getSimpleFlag() == OrganizationChildrenJobPositionSimpleFlag.NO.getCode()) {
                    List<OrganizationMember> organizationMembers = organizationProvider.listOrganizationPersonnels(null,
                            orgCommoand, null, null, locator, 10000);
                    dto.setMembers(organizationMembers.stream().map(m -> ConvertHelper.convert(m, OrganizationMemberDTO.class))
                            .collect(Collectors.toList()));
                }
                return dto;
            }).collect(Collectors.toList()));

        }
        return response;
    }

    @Override
    public List<OrganizationDTO> listOrganizationsByModuleId(ListOrganizationByModuleIdCommand cmd) {
        List<ServiceModuleAssignment> assignments = serviceModuleProvider.listServiceModuleAssignmentByModuleId(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getModuleId());
        assignments.addAll(serviceModuleProvider.listServiceModuleAssignmentByModuleId(com.everhomes.rest.common.EntityType.ALL.getCode(), 0L, cmd.getModuleId()));//负责全部业务范围的对象，也要查询出来
        assignments.addAll(serviceModuleProvider.listServiceModuleAssignmentByModuleId(cmd.getOwnerType(), cmd.getOwnerId(), 0L)); //负责全部业务模块的对象，也要查询出来

        ServiceModule module = serviceModuleProvider.findServiceModuleById(cmd.getModuleId());
        // 说明这是一个子模块, 再看他有没有父模块的责任部门授权
        if (module != null && module.getPath().split("/").length > 2) {
            String[] split = module.getPath().split("/");
            for (int i = 2; i < split.length; i++) {
                // 父模块授权信息
                assignments.addAll(serviceModuleProvider.listServiceModuleAssignmentByModuleId(cmd.getOwnerType(), cmd.getOwnerId(), Long.valueOf(split[i])));
            }
        }

        //如果本身是子项目,则查询其父项目对应的assignments
        ResourceCategory rc = communityProvider.findResourceCategoryById(cmd.getOwnerId());
        if(rc != null){//是子项目
            Long parentOwnerId = rc.getOwnerId();
            assignments.addAll(serviceModuleProvider.listServiceModuleAssignmentByModuleId(cmd.getOwnerType(), parentOwnerId, cmd.getModuleId()));
            assignments.addAll(serviceModuleProvider.listServiceModuleAssignmentByModuleId(cmd.getOwnerType(), parentOwnerId, 0L)); //负责全部业务模块的对象，也要查询出来
        }

        List<String> groupTypes = new ArrayList<>();

        if (null == cmd.getGroupTypes() || cmd.getGroupTypes().size() == 0) {//未指定机构类型
            groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
            groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
            groupTypes.add(OrganizationGroupType.GROUP.getCode());
        } else {
            groupTypes = cmd.getGroupTypes();
        }
        //将targetType = EntityType.ORGANIZATIONS的assigments过滤出targetId的set集合
        Set<Long> targetIdSet = new HashSet<>();
        for (ServiceModuleAssignment assignment : assignments) {
            if (EntityType.fromCode(assignment.getTargetType()) == EntityType.ORGANIZATIONS) {
                targetIdSet.add(assignment.getTargetId());
                //包含子部门，下面所有的机构
                if (IncludeChildFlagType.YES == IncludeChildFlagType.fromCode(assignment.getIncludeChildFlag())) {
                    List<Long> orgIds = getChildOrganizationIds(assignment.getTargetId(), groupTypes);
                    if (null != orgIds && orgIds.size() > 0)
                        targetIdSet.addAll(orgIds);
                }
            }
        }
        //循环查找targetIdSet
        List<OrganizationDTO> dtos = new ArrayList<>();
        targetIdSet.stream().map(r -> {
            Organization organization = organizationProvider.findOrganizationById(r);
            if (null != organization && OrganizationStatus.fromCode(organization.getStatus()) == OrganizationStatus.ACTIVE) {
                dtos.add(ConvertHelper.convert(organization, OrganizationDTO.class));
            }
            return null;
        }).collect(Collectors.toList());

        return dtos;
    }

    @Override
    public List<OrganizationContactDTO> listOrganizationsContactByModuleId(ListOrganizationByModuleIdCommand cmd) {
        List<OrganizationDTO> organizations = listOrganizationsByModuleId(ConvertHelper.convert(cmd, ListOrganizationByModuleIdCommand.class));
        List<Long> organizationIds = new ArrayList<>();
        for (OrganizationDTO organization : organizations) {
            organizationIds.add(organization.getId());
        }
        List<OrganizationContactDTO> dtos = new ArrayList<>();

        if (organizationIds.size() > 0) {
            List<OrganizationMember> members = organizationProvider.getOrganizationMemberByOrgIds(organizationIds, new ListingQueryBuilderCallback() {
                @Override
                public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                    query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
                    query.addGroupBy(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN);
                    return query;
                }
            });

            if (null == members || members.size() == 0) {
                return new ArrayList<>();
            }

            for (OrganizationMember member : members) {
                dtos.add(ConvertHelper.convert(member, OrganizationContactDTO.class));
            }
        }
        return dtos;
    }

    @Override
    public List<OrganizationContactDTO> listOrganizationContactByJobPositionId(ListOrganizationContactByJobPositionIdCommand cmd) {
        Organization organization = checkOrganization(cmd.getOrganizationId());
        List<OrganizationMember> members = null;
        if (OrganizationGroupType.fromCode(organization.getGroupType()) == OrganizationGroupType.ENTERPRISE) {
            members = listOrganizationContactByJobPositionId(organization.getId(), cmd.getJobPositionId());
        } else {
            List<Long> organizationIds = new ArrayList<>();
            organizationIds.add(organization.getId());
            members = listOrganizationContactByJobPositionId(organizationIds, cmd.getJobPositionId());
        }

        List<OrganizationContactDTO> dtos = new ArrayList<>();
        if (null == members || members.size() == 0) {
            return new ArrayList<>();
        }

        for (OrganizationMember member : members) {
            //:todo 寻找部门名
            StringBuffer departmentName = new StringBuffer();
            OrganizationContactDTO dto = ConvertHelper.convert(member, OrganizationContactDTO.class);
            List<String> groupTypes = new ArrayList<>();
            groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
            groupTypes.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
            List<OrganizationMember> departs = this.organizationProvider.listOrganizationMembersByDetailIdAndPath(dto.getDetailId(), organization.getPath(), groupTypes);
            if(departs != null && departs.size() > 0){
                for (OrganizationMember depart:departs){
                    Organization org = this.organizationProvider.findOrganizationById(depart.getOrganizationId());
                    if(org != null && org.getStatus().equals(OrganizationStatus.ACTIVE.getCode())){
                        departmentName.append(org.getName()+" ");
                    }
                }
            }
            dto.setDepartmentName(departmentName.toString());

            //todo 查找工号
            OrganizationMemberDetails detail = this.organizationProvider.findOrganizationMemberDetailsByDetailId(member.getDetailId());
            if(detail != null)
                dto.setEmployeeNo(detail.getEmployeeNo());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<OrganizationContactDTO> listModuleOrganizationContactByJobPositionId(ListModuleOrganizationContactByJobPositionIdCommand cmd) {
        List<OrganizationDTO> organizations = listOrganizationsByModuleId(ConvertHelper.convert(cmd, ListOrganizationByModuleIdCommand.class));
        List<Long> organizationIds = new ArrayList<>();
        for (OrganizationDTO organization : organizations) {
            organizationIds.add(organization.getId());
        }
        List<OrganizationMember> members = listOrganizationContactByJobPositionId(organizationIds, cmd.getJobPositionId());
        List<OrganizationContactDTO> dtos = new ArrayList<>();
        for (OrganizationMember member : members) {
            dtos.add(ConvertHelper.convert(member, OrganizationContactDTO.class));
        }
        return dtos;
    }

    public List<OrganizationMember> listOrganizationContactByJobPositionId(Long enterpriseId, Long jobPositionId) {
        return listOrganizationContactByJobPositionId(enterpriseId, null, jobPositionId);
    }

    @Override
    public List<OrganizationMember> listOrganizationContactByJobPositionId(List<Long> organizationIds, Long jobPositionId) {
        return listOrganizationContactByJobPositionId(null, organizationIds, jobPositionId);
    }

    private List<OrganizationMember> listOrganizationContactByJobPositionId(Long enterpriseId, List<Long> organizationIds, Long jobPositionId) {
        List<Long> jobPositionIds = new ArrayList<>();
        if (null != enterpriseId) {
            List<OrganizationJobPositionMap> maps = organizationProvider.listOrganizationJobPositionMapsByJobPositionId(jobPositionId);
            for (OrganizationJobPositionMap map : maps) {
                jobPositionIds.add(map.getOrganizationId());
            }
        } else {
            List<String> groupTypes = new ArrayList<>();
            groupTypes.add(OrganizationGroupType.JOB_POSITION.getCode());
            List<Organization> jobPositions = new ArrayList<>();
            //查询部门下的岗位
            for (Long organizationid : organizationIds) {
                jobPositions.addAll(organizationProvider.listOrganizationByGroupTypes(organizationid, groupTypes));
            }


            //与通用岗位匹配
            for (Organization jobPosition : jobPositions) {
                if (null != organizationProvider.getOrganizationJobPositionMapByOrgIdAndJobPostionId(jobPosition.getId(), jobPositionId)) {
                    jobPositionIds.add(jobPosition.getId());
                }
            }
            jobPositionIds.add(jobPositionId);//这个接口拿部门岗位人员有问题 增加这行  by jiarui
        }

        //organizationIds改为jobPositionIds by xiongying20170124
        if (jobPositionIds.size() > 0) {
            List<OrganizationMember> members = organizationProvider.getOrganizationMemberByOrgIds(jobPositionIds, new ListingQueryBuilderCallback() {
                @Override
                public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                    query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
                    query.addGroupBy(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN);
                    return query;
                }
            });

            return members;
        }

        return new ArrayList<>();
    }

    @Override
    public List<OrgAddressDTO> listUserRelatedOrganizationAddresses(ListUserRelatedOrganizationAddressesCommand cmd) {
        if (null == cmd.getUserId()) {
            cmd.setUserId(UserContext.current().getUser().getId());
        }
        List<OrgAddressDTO> dtos = new ArrayList<>();
        List<OrganizationMember> members = organizationProvider.listOrganizationMembers(cmd.getUserId());
        for (OrganizationMember member : members) {
            if (OrganizationMemberStatus.fromCode(member.getStatus()) == OrganizationMemberStatus.ACTIVE) {
                Organization organization = organizationProvider.findOrganizationById(member.getOrganizationId());
                if (null != organization) {
                    List<OrganizationAddress> addresses = organizationProvider.findOrganizationAddressByOrganizationId(organization.getId());
                    if (null != addresses && addresses.size() > 0) {
                        for (OrganizationAddress organizationAddress : addresses) {
                            Address address = addressProvider.findAddressById(organizationAddress.getAddressId());
                            if (null != address) {
                                OrgAddressDTO dto = ConvertHelper.convert(address, OrgAddressDTO.class);
                                dto.setAddressId(address.getId());
                                dto.setOrganizationId(organization.getId());
                                dto.setDisplayName(organization.getName());
                                Community community = communityProvider.findCommunityById(address.getCommunityId());
                                if (null != community) {
                                    dto.setCommunityName(community.getName());
                                }
                                dtos.add(dto);
                            }
                        }
                    }

                }
            }
        }
        return dtos;
    }

    @Override
    public List<OrgAddressDTO> listOrganizationAddresses(ListOrganizationAddressesCommand cmd) {

        List<OrgAddressDTO> dtos = new ArrayList<>();

        List<OrganizationAddress> addresses = organizationProvider.findOrganizationAddressByOrganizationId(cmd.getOrganizationId());

        for (OrganizationAddress organizationAddress : addresses) {
            Address address = addressProvider.findAddressById(organizationAddress.getAddressId());
            if (null != address) {
                OrgAddressDTO dto = ConvertHelper.convert(address, OrgAddressDTO.class);
                dto.setAddressId(address.getId());
                dtos.add(dto);
            }
        }

        return dtos;
    }

    @Override
    public ImportFileResponse<ImportOrganizationContactDataDTO> getImportFileResult(GetImportFileResultCommand cmd) {
        return importFileService.getImportFileResult(cmd.getTaskId());
    }

    @Override
    public void exportImportFileFailResultXls(GetImportFileResultCommand cmd, HttpServletResponse httpResponse) {
        importFileService.exportImportFileFailResultXls(httpResponse, cmd.getTaskId());
    }

    @Override
    public ListOrganizationContactCommandResponse listUsersOfEnterprise(listUsersOfEnterpriseCommand cmd) {
        if (cmd.getOrganizationId() == null) {
            LOGGER.error("No OrganizationId enter =" + cmd.getOrganizationId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "No OrganizationId enter!");
        }

        User user = UserContext.current().getUser();
        long userId = user.getId();
        String tag = "listUsersOfEnterprise";
        Organization org = checkEnterpriseParameter(cmd.getOrganizationId(), userId, tag);
        if (org == null) {
            LOGGER.error("No Organization is found! , {}", cmd.getOrganizationId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "No Organization is found!");
        }

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        ListingQueryBuilderCallback callback = new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                //控制用户状态为可用
                query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
                //控制用户type为user
                query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.equal(OrganizationMemberTargetType.USER.getCode()));
                //查找组织ID等于输入参数的记录
                query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(org.getId()));
                //控制只查找手机用户contactType=0
                //query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TYPE.eq(value));
                return query;
            }
        };

        List<OrganizationMember> organizationMembers = organizationProvider.listUsersOfEnterprise(locator, pageSize, callback);
        List<OrganizationContactDTO> dtos = organizationMembers.stream().map(r -> {

            User userInfo = this.userProvider.findUserById(r.getTargetId());
            if (userInfo == null) {
                LOGGER.error("Nick name is not found =" + cmd.getOrganizationId());
                r.setNickName("");
            } else {
                r.setNickName(userInfo.getNickName());
            }
            return ConvertHelper.convert(r, OrganizationContactDTO.class);

        }).collect(Collectors.toList());

        Integer totalRecords = organizationProvider.countUsersOfEnterprise(locator, callback);

        ListOrganizationContactCommandResponse response = new ListOrganizationContactCommandResponse();
        response.setNextPageAnchor(locator.getAnchor());
        response.setMembers(dtos);
        response.setTotalCount(totalRecords);
        response.setNamespaceId(org.getNamespaceId());

        return response;

    }

    private OrganizationMemberDetails getDetailFromOrganizationMember(OrganizationMember member) {
        return getDetailFromOrganizationMember(member, true, null);
    }

    /**
     * 根据organizationMember对象来创建OrganizationMemberDetails对象
     * @param member
     * @param isCreate
     * @param find_detail
     * @return
     */
    private OrganizationMemberDetails getDetailFromOrganizationMember(OrganizationMember member, Boolean isCreate, OrganizationMemberDetails find_detail) {
        //创建OrganizationMemberDetails对象
        OrganizationMemberDetails detail = new OrganizationMemberDetails();

        if (isCreate && find_detail == null) {
            detail.setId(member.getDetailId() != null ? member.getDetailId() : 0L);
            detail.setNamespaceId(member.getNamespaceId() != null ? member.getNamespaceId() : 0);
            detail.setRegionCode(member.getRegionCode());
            detail.setContactName(member.getContactName());
            detail.setGender(member.getGender());
            detail.setContactToken(member.getContactToken());
            detail.setContactDescription(member.getContactDescription());
            //  there is already a unique avatar for employee's archive. by ryan
            //  detail.setAvatar(member.getAvatar());
            detail.setTargetType(member.getTargetType());
            detail.setTargetId(member.getTargetId());
        } else {
            detail = find_detail;
            detail.setContactName(member.getContactName());
            detail.setGender(member.getGender());
            //  the user's status may be changed like registered so updating it. by ryan
            detail.setTargetType(member.getTargetType());
            detail.setTargetId(member.getTargetId());
        }
        return detail;
    }

    private List<OrganizationDTO> repeatCreateOrganizationmembers(List<Long> organizationIds, String contact_token, List<Long> enterpriseIds, OrganizationMember member) {
        List<OrganizationDTO> results = new ArrayList<>();
        if (null != organizationIds) {
            removeRepeat(organizationIds);
            // 重新把成员添加到公司多个机构
            for (Long oId : organizationIds) {
                if (null == oId) {
                    continue;
                }
                //排除掉上面已添加的公司机构成员
                if (!enterpriseIds.contains(oId)) {
                    Organization group = checkOrganization(oId);

                    member.setGroupPath(group.getPath());

                    member.setGroupType(group.getGroupType());

                    member.setOrganizationId(oId);

                    /**Modify BY lei.lv cause MemberDetail**/
                    if (OrganizationGroupType.ENTERPRISE != OrganizationGroupType.fromCode(group.getGroupType())) {

                        //找到部门对应的资料表记录
                        OrganizationMemberDetails old_detail = organizationProvider.findOrganizationMemberDetailsByOrganizationIdAndContactToken(oId, contact_token);
                        if (old_detail == null) {
                            LOGGER.error("Cannot find memberDetail of DirectlyEnterpriseId for this org。orgId={}", oId);
                            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
                                    "Cannot find memberDetail of DirectlyEnterpriseId for this org");
                        }
                        member.setDetailId(old_detail.getId());
                        organizationProvider.createOrganizationMember(member);
                        results.add(ConvertHelper.convert(group, OrganizationDTO.class));
                    }
                }
            }
        }
        return results;
    }

    private Long findDirectUnderOrganizationId(Long enterPriseId) {
        Organization parOrg = checkOrganization(enterPriseId);
        /**先查organization表里有没有该企业直属部门（隐藏）**/
        Organization under_org = organizationProvider.findUnderOrganizationByParentOrgId(parOrg.getId());
        //如果没有查询到该企业有直属的隐藏部门
        if (under_org == null) {
            User current_user = UserContext.current().getUser();
            Organization org_under_direct = new Organization();
            org_under_direct.setName(parOrg.getName());
            org_under_direct.setPath(parOrg.getPath());
            org_under_direct.setLevel(parOrg.getLevel() + 1);
            org_under_direct.setOrganizationType(parOrg.getOrganizationType());
            org_under_direct.setStatus(OrganizationStatus.ACTIVE.getCode());
            org_under_direct.setNamespaceId(parOrg.getNamespaceId());
            org_under_direct.setCreatorUid(current_user.getId());
            org_under_direct.setGroupType(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
            org_under_direct.setDirectlyEnterpriseId(parOrg.getId());
            org_under_direct.setParentId(parOrg.getId());
            organizationProvider.createOrganization(org_under_direct);
            return org_under_direct.getId();
        } else {//如果查询到该企业有直属的隐藏部门
            return under_org.getId();
        }
    }

    /* 检查是否有匹配的detail记录，如有则更新，若无则创建，并返回最终的detailId */
    private Long getEnableDetailOfOrganizationMember(OrganizationMember organizationMember, Long organizationId) {

        //更新或创建detail记录，根据组织id和手机号来查询eh_organization_memner_details表中的信息
        OrganizationMemberDetails old_detail = organizationProvider.findOrganizationMemberDetailsByOrganizationIdAndContactToken(organizationId, organizationMember.getContactToken());
        Long new_detail_id;
        if (old_detail == null) {
            /* 如果档案表中无记录 */
            //根据OrganizationMember对象来创建organization_member_details表信息
            OrganizationMemberDetails organizationMemberDetail = getDetailFromOrganizationMember(organizationMember, true, null);
            //organizationMemberDetails表中的organizationId指的是总公司的organizationId
            organizationMemberDetail.setOrganizationId(getTopOrganizationId(organizationId));
            organizationMemberDetail.setCheckInTime(ArchivesUtil.currentDate());
            //根据OrganizationMemberDetails来获取detailId的方法
            new_detail_id = organizationProvider.createOrganizationMemberDetails(organizationMemberDetail);
        } else { /* 如果档案表中有记录 */
            OrganizationMemberDetails organizationMemberDetail = getDetailFromOrganizationMember(organizationMember, false, old_detail);
            organizationMemberDetail.setOrganizationId(old_detail.getOrganizationId());
            organizationProvider.updateOrganizationMemberDetails(organizationMemberDetail, organizationMemberDetail.getId());
            new_detail_id = organizationMemberDetail.getId();
        }
        return new_detail_id;
    }

    /**
     * 创建member对应的organizationMember记录（如果已存在，则重新赋值后更新）
     *
     * @param organizationMember
     * @return
     */
    private UserOrganizations createOrUpdateUserOrganization(OrganizationMember organizationMember) {
        //根据namespaceId、organizationId、userId（userIdentifier.getOwnerUid()）来判断唯一记录
        UserOrganizations userOrganizations = userOrganizationProvider.findUserOrganizations(organizationMember.getNamespaceId(), organizationMember.getOrganizationId(), organizationMember.getTargetId());

        if (userOrganizations == null) {
            userOrganizations = new UserOrganizations();
            userOrganizations.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            commonSetUserOrganization(userOrganizations, organizationMember);
            this.userOrganizationProvider.createUserOrganizations(userOrganizations);
        } else {
            userOrganizations.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            commonSetUserOrganization(userOrganizations, organizationMember);
            this.userOrganizationProvider.updateUserOrganizations(userOrganizations);
        }
        return userOrganizations;
    }

    /**
     * userOrganiztion批量赋值方法
     *
     * @param userOrganizations
     * @param organizationMember
     */
    private void commonSetUserOrganization(UserOrganizations userOrganizations, OrganizationMember organizationMember) {
        if (organizationMember.getGroupType().equals(OrganizationGroupType.ENTERPRISE.getCode())) {
            userOrganizations.setUserId(organizationMember.getTargetId());
            userOrganizations.setOrganizationId(organizationMember.getOrganizationId());
            userOrganizations.setGroupPath(organizationMember.getGroupPath());
            userOrganizations.setGroupType(organizationMember.getGroupType());
            userOrganizations.setStatus(organizationMember.getStatus());
            userOrganizations.setNamespaceId(organizationMember.getNamespaceId());
            userOrganizations.setVisibleFlag(organizationMember.getVisibleFlag());
        } else {
            LOGGER.error("User_organization is not right.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "User_organization is not right.");
        }
    }

    /**
     * 根据members退出删除user_organiztion表中的信息
     *
     * @param members
     */
    private void deleteUserOrganizationWithMembers(List<OrganizationMember> members) {
        dbProvider.execute((TransactionStatus status) -> {
            for (OrganizationMember member : members) {
                if(OrganizationGroupType.fromCode(member.getGroupType()) == OrganizationGroupType.ENTERPRISE){
                    UserOrganizations userOrganization = userOrganizationProvider.findUserOrganizations(member.getNamespaceId(), member.getOrganizationId(), member.getTargetId());
                    if (userOrganization != null) {
                        this.userOrganizationProvider.deleteUserOrganizations(userOrganization);
                    }
                }
            }
            return null;
        });
    }

    /**
     * 根据members把user_organiztion表中的记录更新为失效
     *
     * @param members
     */
    private void inactiveUserOrganizationWithMembers(List<OrganizationMember> members) {
        dbProvider.execute((TransactionStatus status) -> {
            for (OrganizationMember member : members) {
                UserOrganizations userOrganizations = userOrganizationProvider.findUserOrganizations(member.getNamespaceId(), member.getOrganizationId(), member.getTargetId());
                if (userOrganizations != null) {
                    this.userOrganizationProvider.inactiveUserOrganizations(userOrganizations);
                }
            }
            return null;
        });
    }

    /**
     * 创建企业级的member/detail/user_organiztion记录
     *
     * @param _organizationMember
     * @param organizationId
     */
    private OrganizationMember createOrganiztionMemberWithDetailAndUserOrganization(OrganizationMember _organizationMember,
                                                                                    Long organizationId) {
        User user = UserContext.current().getUser();
        //深拷贝
        OrganizationMember organizationMember = ConvertHelper.convert(_organizationMember, OrganizationMember.class);
        /**创建/更新detail,并获取detailId**/
        // 申请加入企业的时候，不需要在人事档案中新增数据，在审核通过后，再在人事档案中新增数据。 add by yanlong.liang 20180725
        Long new_detail_id = getEnableDetailOfOrganizationMember(organizationMember, organizationId);
        //根据手机号和组织id来查询对应的OrganizationMember信息
        OrganizationMember desOrgMember = this.organizationProvider.findOrganizationMemberByOrgIdAndToken(organizationMember.getContactToken(),
                organizationId);

        //如果企业中没有有该记录
        if (null == desOrgMember) {
            /**创建belongTo的记录**/
            organizationMember.setOrganizationId(organizationId);
            organizationMember.setOperatorUid(user.getId());
            //绑定member表的detail_id
            organizationMember.setDetailId(new_detail_id);
            organizationProvider.createOrganizationMember(organizationMember);

            /**创建user_organization的记录（仅当target为user且grouptype为企业时添加）**/
            if (organizationMember.getTargetType().equals(OrganizationMemberTargetType.USER.getCode()) && organizationMember.getGroupType().equals(OrganizationType.ENTERPRISE.getCode())) {
                createOrUpdateUserOrganization(organizationMember);
            }

//            /**创建onNode的记录**/
//            Long hiddenDirectId = findDirectUnderOrganizationId(organizationId);
//            Organization hiddenDirectOrganiztion = checkOrganization(hiddenDirectId);
//            organizationMember.setGroupPath(hiddenDirectOrganiztion.getPath());
//            organizationMember.setGroupType(hiddenDirectOrganiztion.getGroupType());
//            organizationMember.setOrganizationId(hiddenDirectId);
//            organizationMember.setDetailId(new_detail_id);
//            organizationProvider.createOrganizationMember(organizationMember);
        }
        return organizationMember;// add by xq.tian 2017/07/05
    }
    
	/**
     * 创建企业级的member记录，因为未激活用户没有直属部门的记录，需要在添加时加入该记录
     * @param _organizationMember
     * @param organizationId
     * @author mmb
     */
    private void createOrganiztionMemberOfDirectUnderEnterprise(OrganizationMember _organizationMember,
    														Long organizationId){
    	//获取登录App的用户信息
        User user = UserContext.current().getUser();
        //深拷贝
        OrganizationMember member = ConvertHelper.convert(_organizationMember, OrganizationMember.class);
        //获取当前App所在的域空间
        Integer namespaceId = UserContext.getCurrentNamespaceId(member.getNamespaceId());
        
        //从organizations表查部门数据
        List<String> types = new ArrayList<>();
        types.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
        Organization organization = organizationProvider.listOrganizationsByPathAndToken(member.getOrganizationId(), types,namespaceId);
        
        if(organization == null){
        	return;
        }
        
        //查找记录（groupType=DIRECT_UNDER_ENTERPRISE/targetId/organizationId），null则插入
        List<String> groupTypeList = new ArrayList<>();
        groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
        groupTypeList.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
        OrganizationMember selectMember = organizationProvider.listOrganizationMembersByGroupTypeAndContactToken(groupTypeList,member.getContactToken(),organization.getPath());
        
        if(selectMember == null){
        	// organizationId要从organizations表找到主属部门记录，然后添加；
        	member.setGroupPath(organization.getPath());
            member.setMemberGroup(null);
            member.setOrganizationId(organization.getId());
        	member.setGroupType("DIRECT_UNDER_ENTERPRISE");  //必须是DIRECT_UNDER_ENTERPRISE
        	organizationProvider.createOrganizationMember(member);
        	LOGGER.debug("插入新的【OrganizationMember】数据： "+member.toString());
        }
    }
    
    private OrganizationMember createOrganiztionMemberWithoutDetailAndUserOrganization(OrganizationMember _organizationMember, Long organizationId) {
        User user = UserContext.current().getUser();
        OrganizationMember organizationMember = ConvertHelper.convert(_organizationMember, OrganizationMember.class);

        OrganizationMember desOrgMember = this.organizationProvider.findOrganizationMemberByOrgIdAndToken(organizationMember.getContactToken(), organizationId);

        //如果企业中没有有该记录
        if (null == desOrgMember) {
            /**创建belongTo的记录**/
            organizationMember.setOrganizationId(organizationId);
            organizationMember.setOperatorUid(user.getId());
            organizationProvider.createOrganizationMember(organizationMember);

            /**创建user_organization的记录（仅当target为user且grouptype为企业时添加）**/
            if (organizationMember.getTargetType().equals(OrganizationMemberTargetType.USER.getCode()) && organizationMember.getGroupType().equals(OrganizationType.ENTERPRISE.getCode())) {
                createOrUpdateUserOrganization(organizationMember);
            }
        }
        return organizationMember;// add by xq.tian 2017/07/05
    }
    /**
     * 获取一个组织的总公司ID
     **/
    private Long getTopEnterpriserIdOfOrganization(Long organizationId) {
        Organization org = checkOrganization(organizationId);
        //判断是总公司
        if (org != null && org.getParentId() == 0L && org.getGroupType() == OrganizationGroupType.ENTERPRISE.getCode()) {
            return organizationId;
        }
        //不是总公司
        if (org != null && !StringUtils.isEmpty(org.getPath())) {
            return Long.valueOf(org.getPath().split("/")[1]);
        } else {
            return null;
        }

    }

    /*
     * 删除公司级别以下及退出公司的记录
     **/
    private void deleteOrganizaitonMemberUnderEnterprise(List<Long> enterpriseIds, List<String> groupTypes, List<OrganizationMember> leaveMembers, String token) {
        for (Long enterpriseId : enterpriseIds) {
            Organization enterprise = checkOrganization(enterpriseId);
            List<OrganizationMember> members = organizationProvider.listOrganizationMemberByPath(enterprise.getPath(), groupTypes, token);
            for (OrganizationMember member : members) {
                if (!member.getGroupType().equals(OrganizationGroupType.ENTERPRISE.getCode())) {//删除所有非公司的记录
                    organizationProvider.deleteOrganizationMemberById(member.getId());
                } else if (member.getGroupType().equals(OrganizationGroupType.ENTERPRISE.getCode()) && !enterpriseIds.contains(member.getOrganizationId())) {//删除其他公司（既是本次退出公司）的记录
                    organizationProvider.deleteOrganizationMemberById(member.getId());
                    leaveMembers.add(member);
                }
            }
        }
    }

    @Override
    public Organization createUniongroupOrganization(Long organizationId, String name,String groupType) {
        checkNameRepeat(organizationId, name, groupType,null);
        User user = UserContext.current().getUser();
        Organization parOrg = this.checkOrganization(organizationId);
        Organization organization = ConvertHelper.convert(parOrg, Organization.class);

        organization.setName(name);
        organization.setDirectlyEnterpriseId(organizationId);
        organization.setParentId(organizationId);
		organization.setGroupType(groupType);
        organization.setLevel(parOrg.getLevel() + 1);
        organization.setStatus(OrganizationStatus.ACTIVE.getCode());
        organization.setCreatorUid(user.getId());
        organization.setOperatorUid(user.getId());
        organizationProvider.createOrganization(organization);

        return organization;
    }
    @Override
    public void checkNameRepeat(Long organizationId, String name, String groupType,Long groupId) {
        Organization org = organizationProvider.findOrganizationByName(groupType, name, organizationId,groupId);
        if (null != org) {
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE,
                    OrganizationServiceErrorCode.ERROR_ORG_NAME_REPEAT,  "名称重复错误");

        }

    }

    /**
     * 根据域空间id、企业类型、关键字、来查询企业信息
     * @param cmd
     * @return
     */
    @Override
    public ListPMOrganizationsResponse listEnterpriseByNamespaceIds(ListEnterpriseByNamespaceIdCommand cmd) {
        //创建ListPMOrganizationsResponse对象
        ListPMOrganizationsResponse res = new ListPMOrganizationsResponse();
        //创建分页的对象
        CrossShardListingLocator locator = new CrossShardListingLocator();
        //设置第几页
        locator.setAnchor(cmd.getPageAnchor());
        //定义页面大小
        Integer pageSize = cmd.getPageSize();
        //根据域空间id、企业类型、关键字、来查询企业信息
        List<Organization> organizations = this.organizationProvider.listEnterpriseByNamespaceIds(cmd.getNamespaceId(), cmd.getType(),
                null, cmd.getKeywords(), locator, pageSize);
        if(organizations != null || !"".equals(organizations)){
            //采用forEach循环遍历集合
            for(Organization organization : organizations){
                //将数据封装在对象Organization中
                OrganizationDetail organizationDetail = organizationProvider.getOrganizationDetailByOrgId(organization.getId());
                if(organizationDetail != null){
                    organization.setAvatarUri(organizationDetail.getAvatar());
                    organization.setMemberRange(organizationDetail.getMemberRange());
                }
                organization.setProjectsCount(organizationProvider.getCommunityByOrganizationId(organization.getId()));
            }
        }

        if(organizations != null){
            List<OrganizationDTO> dtoList = organizations.stream().map(r->ConvertHelper.convert(r, OrganizationDTO.class)).collect(Collectors.toList());
            //// TODO: 2018/5/8
            res.setNextPageAnchor(locator.getAnchor());
            res.setDtos(dtoList);
        }
        return res;
    }

    @Override
    public void leaveTheJob(LeaveTheJobCommand cmd) {
        OrganizationMemberDetails detail = this.organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getDetailId());
        //组织架构删除
        DeleteOrganizationPersonnelByContactTokenCommand deleteOrganizationPersonnelByContactTokenCommand = new DeleteOrganizationPersonnelByContactTokenCommand();
        deleteOrganizationPersonnelByContactTokenCommand.setOrganizationId(cmd.getOrganizationId());
        deleteOrganizationPersonnelByContactTokenCommand.setContactToken(detail.getContactToken());
        deleteOrganizationPersonnelByContactTokenCommand.setScopeType(DeleteOrganizationContactScopeType.ALL_NOTE.getCode());
        deleteOrganizationPersonnelByContactToken(deleteOrganizationPersonnelByContactTokenCommand);
        //离职时薪酬组相关的改动
        try{
            this.uniongroupService.syncUniongroupAfterLeaveTheJob(cmd.getDetailId());
        }finally {
            return;
        }
    }

    //人事调动的处理方法
    private void organizationPersonelsDatasProcess(List<Long> enterpriseIds, List<Long> departmentIds, List<Long> direct_under_enterpriseIds, Organization org){
        /**modify by lei.lv*/
        //总公司和分公司的ID集合
        enterpriseIds.add(org.getId());

        if (null != departmentIds) {
            for (Long departmentId : departmentIds) {
                Organization o = checkOrganization(departmentId);
                if (OrganizationGroupType.ENTERPRISE == OrganizationGroupType.fromCode(o.getGroupType())) {
                    if (o.getId().longValue() == org.getId().longValue()) {
                        //如果是总公司
                        direct_under_enterpriseIds.add(o.getId());
                    }
                    if (!enterpriseIds.contains(o.getId())) {
                        //直属场景
                        direct_under_enterpriseIds.add(o.getId());
                        //企业级记录
                        enterpriseIds.add(o.getId());
                    }
                } else {
                    if (!enterpriseIds.contains(o.getDirectlyEnterpriseId())) {
                        //添加企业级记录
                        enterpriseIds.add(o.getDirectlyEnterpriseId());
                    }
                }
            }
        } else {//如果没有选择部门，则默认直属当前的organizationId
            direct_under_enterpriseIds.add(org.getId());
        }


        //创建直属企业的直属部门
        if (null != direct_under_enterpriseIds) {
            //遍历需要添加直属的企业
            for (Long enterPriseId : direct_under_enterpriseIds) {
                //寻找企业下的直属隐藏部门的organizationId
                Long hiddenDirectId = findDirectUnderOrganizationId(enterPriseId);
                //把需要添加直属的公司隐藏部门点加到departmentIds
                if (!departmentIds.contains(hiddenDirectId)) {
                    departmentIds.add(hiddenDirectId);
                }
            }
        }
    }


    //获取可用的企业级记录
    private OrganizationMember getEnableEnterprisePersonel(Organization org, Long detailId){
        //根据detailId获得contactToken
        OrganizationMemberDetails detail = this.organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
        String token = detail.getContactToken();
        if (StringUtils.isEmpty(token)) {
            LOGGER.error("contactToken is null");
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_CONTACTTOKEN_ISNULL, "contactToken is null");
        }
        //寻找有效的Enterprise记录
        List<String> groupTypes = new ArrayList<String>();
        groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
        List<OrganizationMember> members = organizationProvider.listOrganizationMemberByPath(org.getPath(), groupTypes, token);
        if (members == null || members.size() == 0) {
            LOGGER.error("Enterprise_member is null");
            return null;
//            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ORG_TYPE, "Enterprise_member is null");
        }
        members = members.stream().filter(r -> r.getOrganizationId().equals(org.getId())).collect(Collectors.toList());
        OrganizationMember enterprise_member = members.get(0);
        enterprise_member.setContactToken(token);
        return enterprise_member;
    }

    //递归
    private Organization listUnderOrganizations(int i, List<Organization> orgs, Integer namespaceId, String[] list) {
        LOGGER.debug("listUnderOrganizations start, i ={}, orgs = {}, namespaceId = {}, list={}" , i, orgs, namespaceId, list);
        if (orgs == null) {
            //:todo 第一次进入
            List<Organization> orgs_0 = this.organizationProvider.listOrganizationByActualName(list[0], null, null, namespaceId);
            LOGGER.debug("listUnderOrganizations oneStep" + orgs_0.toString());
            if (orgs_0 != null) {
                return listUnderOrganizations(i + 1, orgs_0, namespaceId, list);
            }
        }
        if (orgs.size() == 1 && i == list.length) {
            //:todo 获得1个结果 结束递归
            LOGGER.debug("listUnderOrganizations threeStep" + orgs.get(0).toString());
            return orgs.get(0);
        }
        if (orgs.size() > 1 && i == list.length) {
            //todo 获得多个结果 到达极限 报错
            LOGGER.error("cannot find the exact :organization. path = {}", list.toString());
//            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER, "cannot find the exact organization. path = {}", list.toString());
            return null;
        } else {
            //todo 递归
            List<Organization> result = orgs.stream().map(r -> {
                List<Organization> orgs_1 = this.organizationProvider.listOrganizationByActualName(list[i], null, r.getId(), namespaceId);
                LOGGER.debug("listUnderOrganizations twoStep" + orgs_1.toString());
                if (orgs_1 != null && orgs_1.size() > 0) {
                    return listUnderOrganizations(i + 1, orgs_1, namespaceId, list);
                }
                return null;
            }).filter(r -> {
                return r != null;
            }).collect(Collectors.toList());

            if(result != null && result.size()>0){
                return result.get(0);
            }

            return null;
        }
    }


    @Override
    public Byte getOrganizationDetailFlag(GetOrganizationDetailFlagCommand cmd) {
        CommunityOrganizationDetailDisplay display = organizationProvider.findOrganizationDetailFlag(cmd.getNamespaceId(), cmd.getCommunityId());
        return display == null ? 0 : display.getDetailFlag();
    }

    @Override
    public Byte setOrganizationDetailFlag(SetOrganizationDetailFlagCommand cmd) {
        CommunityOrganizationDetailDisplay display = organizationProvider.findOrganizationDetailFlag(cmd.getNamespaceId(), cmd.getCommunityId());
        if(display == null) {
            display = ConvertHelper.convert(cmd, CommunityOrganizationDetailDisplay.class);
            organizationProvider.createCommunityOrganizationDetailDisplay(display);
        } else {
            display.setDetailFlag(cmd.getDetailFlag());
            organizationProvider.updateCommunityOrganizationDetailDisplay(display);
        }
        return display.getDetailFlag();
    }

    @Override
    public void checkOrganizationPrivilege(Long orgId, Long privilegeId){
        Long organizationId = getTopEnterpriserIdOfOrganization(orgId);
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), organizationId, privilegeId, FlowConstants.ORGANIZATION_MODULE, null, null, orgId, null);
    }

    @Override
    @Deprecated
    public Long getDepartmentByDetailId(Long detailId){
        List<String> groupTypes = new ArrayList<>();
        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
        groupTypes.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
        List<OrganizationMember> members = organizationProvider.listOrganizationMembersByDetailId(detailId, groupTypes);
        if(members != null && members.size() > 0){
            return members.get(0).getOrganizationId();
        }else{
            groupTypes.clear();
            groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
            List<OrganizationMember> member_enterprise = organizationProvider.listOrganizationMembersByDetailId(detailId, groupTypes);
            if(member_enterprise != null && member_enterprise.size() > 0){
               return member_enterprise.get(0).getOrganizationId();
            }
        }
        return null;
    }

    @Override
    public ListPMOrganizationsResponse listPMOrganizations(ListPMOrganizationsCommand cmd){
        List<Organization> organizations = organizationProvider.listPMOrganizations(cmd.getNamespaceId());
        ListPMOrganizationsResponse response = new ListPMOrganizationsResponse();
        if(organizations != null){
            List<OrganizationDTO> collect = organizations.stream().map(r -> ConvertHelper.convert(r, OrganizationDTO.class)).collect(Collectors.toList());
            response.setDtos(collect);
        }
        return response;
    }

    @Override
    public List<Long> listDetailIdWithEnterpriseExclude(String keywords, Integer namespaceId, Long enterpriseId, Timestamp checkinTimeStart,
                                                        Timestamp checkinTimeEnd, Timestamp dissmissTimeStart, Timestamp dissmissTimeEnd,
                                                        CrossShardListingLocator locator, Integer pageSize,List<Long> notinDetails,List<Long> inDetails) {
        List groupTypes = new ArrayList();
        groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
        Organization org = checkOrganization(enterpriseId);
        List<Organization> underEnterprises = this.organizationProvider.listOrganizationByGroupTypesAndPath(org.getPath() + "/%", groupTypes, null, null, null);
        List<String> smallPath = new ArrayList();
        underEnterprises.forEach(r -> smallPath.add(r.getPath()));
        groupTypes.clear();
        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
        groupTypes.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
        return this.organizationProvider.listMemberDetailIdWithExclude(keywords, namespaceId, org.getPath(), smallPath, checkinTimeStart,
                checkinTimeEnd, dissmissTimeStart, dissmissTimeEnd, locator, pageSize,notinDetails,inDetails,groupTypes);
    }



    @Override
    public Long getDepartmentByDetailIdAndOrgId(Long detailId, Long orgId) {
        Organization org = checkOrganization(orgId);
        List<String> groupTypes = new ArrayList<>();
        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
        groupTypes.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
        List<OrganizationMember> members = organizationProvider.listOrganizationMembersByDetailIdAndPath(detailId, org.getPath(), groupTypes);
        if(members != null && members.size() > 0){
            return members.get(0).getOrganizationId();
        }else{
            groupTypes.clear();
            groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
            List<OrganizationMember> member_enterprise = organizationProvider.listOrganizationMembersByDetailIdAndPath(detailId, org.getPath(), groupTypes);
            if(member_enterprise != null && member_enterprise.size() > 0){
                return member_enterprise.get(0).getOrganizationId();
            }
        }
        return null;
    }


    /**
     * 根据organizationId来查询公司详细信息
     * 表eh_organizations和表eh_organization_details进行联查
     * @param cmd
     * @return
     */
    @Override
    public OrganizationAndDetailDTO getOrganizationDetailByOrgId(FindEnterpriseDetailCommand cmd){
        //创建OrganizationAndDetailDTO对象
        OrganizationAndDetailDTO organizationAndDetailDTO = new OrganizationAndDetailDTO();
        if(cmd.getOrganizationId() != null && cmd.getNamespaceId() != null){
            //根据organizationId和namespaceId进行查询节点信息以及明细
            organizationAndDetailDTO = organizationProvider.getOrganizationAndDetailByorgIdAndNameId(cmd.getOrganizationId(),cmd.getNamespaceId());
            //根据组织编号organizationId来查询eh_organization_communities表中该公司管理的项目，注意一个公司可以管理多个项目
            //所以可以是一个集合
            if(organizationAndDetailDTO.getPmFlag() == TrueOrFalseFlag.TRUE.getCode()){
                //说明该公司是管理公司，那么我们需要将该管理公司所管理的项目编号全部返回给前端
                //我们首先查出该公司管理的项目集合
                List<OrganizationCommunity> organizationCommunityList = organizationProvider.listOrganizationCommunities(cmd.getOrganizationId());
                //创建一个List<Long>集合，来存放项目编号的集合
                List<Long> list = Lists.newArrayList();
                //采用forEach循环遍历管理项目
                for(OrganizationCommunity organizationCommunity : organizationCommunityList){
                    //将communityId封装在集合中
                    list.add(organizationCommunity.getCommunityId());
                }
                //将项目编号的集合封装在OrganizationAndDetailDTO对象中返回给前端
                organizationAndDetailDTO.setCommunityIds(list);
            }
        }

        if(organizationAndDetailDTO.getAdminTargetId() != null){
            //说明超级管理员不为空，那么我们就将超级管理员查询出来，并且封装在对象OrganizationAndDetailDTO中
            //创建User对象
//            OrganizationMember organizationMember = organizationProvider.findOrganizationMemberById(organizationAndDetailDTO.getAdminTargetId());
            //在这里就不能再使用以前判断超级管理员的逻辑了，应该改为现有的逻辑
            //// TODO: 2018/6/5
            OrganizationMember organizationMember = organizationProvider.findOrganizationMemberByOrgIdAndSoOn(organizationAndDetailDTO.getOrganizationId(),organizationAndDetailDTO.getAdminTargetId());
            if(organizationMember != null){
                organizationAndDetailDTO.setContactor(organizationMember.getContactName());
                organizationAndDetailDTO.setEntries(organizationMember.getContactToken());
            }
        }
        //接下来我们需要做的是：将该公司所在的办公地点、所属项目名称、以及该项目下面的所有的楼栋和门牌查询出来
        if(organizationAndDetailDTO.getOrganizationId() != null){
            //说明上面查询出来的organizationId是存在的，那么我们根据这个organizationId可以在办公地点表eh_organization_workPlaces中查询出
            //办公地点id（community_id）可能是一个集合（一个公司存在多个办公地点）
            List<OrganizationWorkPlaces> organizationWorkPlacesList = organizationProvider.findOrganizationWorkPlacesByOrgId(organizationAndDetailDTO.getOrganizationId());
            //非空判断
            if(!CollectionUtils.isEmpty(organizationWorkPlacesList)){
                //说明办公地点存在，那么我们将其进行遍历
                for(OrganizationWorkPlaces organizationWorkPlaces : organizationWorkPlacesList){
                    //创建一个对象，用于承载办公点名称和项目名称以及里面的楼栋和门牌集合
                    OfficeSiteDTO officeSiteDTO = new OfficeSiteDTO();
                    //将办公地点名称封装在对象OfficeSiteDTO中
                    officeSiteDTO.setSiteName(organizationWorkPlaces.getWorkplaceName());
                    officeSiteDTO.setCommunityId(organizationWorkPlaces.getCommunityId());
                    officeSiteDTO.setWholeAddressName(organizationWorkPlaces.getWholeAddressName());
                    officeSiteDTO.setId(organizationWorkPlaces.getId());
                    //根据查询到的community_id来查询表eh_communities然后得到所属项目的名称，并且将其封装在对象OfficeSiteDTO中
                    String communityName = organizationProvider.getCommunityNameByCommunityId(organizationWorkPlaces.getCommunityId());
                    officeSiteDTO.setCommunityName(communityName);
                    //然后根据community_id来查询项目和楼栋门牌的关系表eh_communityAndBuilding_relationes中对应的building_id和
                    //address_id,(注意一个community_id可能对应多个building_id和address_id)
                    List<CommunityAndBuildingRelationes> communityAndBuildingRelationesList = organizationProvider.getCommunityAndBuildingRelationesByWorkPlaceId(organizationWorkPlaces.getId());
                    //非空校验
                    if(!CollectionUtils.isEmpty(communityAndBuildingRelationesList)){
                        //说明集合中存在值，进行遍历
                        for(CommunityAndBuildingRelationes communityAndBuildingRelationes : communityAndBuildingRelationesList){
                            //拿到对应的building_id和address_id，然后根据这个分别查询楼栋表eh_buildings和门牌表eh_address表中的信息
                            //这个是正常的逻辑，但是现在呢？由于eh_buildings表和eh_addresses表中的数据是有些出入，所以呢，现在我们暂时不去
                            //查询eh_buildings表中的信息，eh_addresses表中也是存在对应的楼栋号信息的，所以我们现在就根据addressId只从eh_addresses表中
                            //拿数据,buildingId就暂时不用
                            Address address = addressProvider.findAddressById(communityAndBuildingRelationes.getAddressId());
                            //创建OrganizationApartDTO对象，用于承载楼栋名称和门牌名称
                            OrganizationApartDTO organizationApartDTO = new OrganizationApartDTO();
                            //非空校验
                            if(address != null){
                                //说明查询出来的address是存在信息的，那么将楼栋名称和门牌名称封装进去
                                organizationApartDTO.setBuildingName(address.getBuildingName());
                                organizationApartDTO.setApartmentName(address.getApartmentName());
                            }
                            //接下来我们需要将OrganizationApartDTO对象添加到OfficeSiteDTO对象中的List<OrganizationApartDTO>集合中去
                            if(CollectionUtils.isEmpty(officeSiteDTO.getSiteDtos())){
                                //如果集合为空，那么久new一个，然后再添加
                                List<OrganizationApartDTO> organizationApartDTOList = Lists.newArrayList();
                                organizationApartDTOList.add(organizationApartDTO);
                                officeSiteDTO.setSiteDtos(organizationApartDTOList);
                            }else{
                                officeSiteDTO.getSiteDtos().add(organizationApartDTO);
                            }
                        }

                    }
                    if(CollectionUtils.isEmpty(organizationAndDetailDTO.getOfficeSites())){
                        List<OfficeSiteDTO> officeSiteDTOList = Lists.newArrayList();
                        officeSiteDTOList.add(officeSiteDTO);
                        organizationAndDetailDTO.setOfficeSites(officeSiteDTOList);
                    }else{
                        //将officeSiteDTO对象添加到OrganizationAndDetailDTO对象中的List<OfficeSiteDTO>集合中
                        organizationAndDetailDTO.getOfficeSites().add(officeSiteDTO);

                    }
                }
            }
        }
        return organizationAndDetailDTO;
    }

    /**
     * 根据用户id来进行更高超级管理员手机号
     * @param cmd
     */
    @Override
    public void updateSuperAdmin(UpdateSuperAdminCommand cmd){
        //所以的操作必须在同一个事物中进行
        dbProvider.execute((TransactionStatus status) -> {
            //根据手机号、域空间id、organizationId来查询eh_organization_members表中的信息
            //判断
            /*if(cmd.getEntries() != null && cmd.getNamespaceId() != null && cmd.getOrganizationId() != null){

                //判断该用户是否已经注册，在这里我们的更换的超级管理员只能是已经注册的
                if(cmd.getIsSigned() == TrueOrFalseFlag.TRUE.getCode()){
                    //说明该更换的超级管理员已经进行注册，那么我们可以继续向下走，否则不能进行
                    //判断该用户是否已经加入了企业，也就是说在eh_organization_members表中是否存在记录
                    if(cmd.getIsJoined() == TrueOrFalseFlag.TRUE.getCode()){
                        //说明该更换的超级管理员已经加入了企业，在eh_organization_members表中存在记录，那么就好办，我们只需要根据该更换的超级
                        //管理员的手机号、域空间ID、组织Id来查询eh_organization_members表中的信息，并且将该信息的id变更为eh_organizations表
                        //中的admin_target_id字段就ok
                        //查询表eh_organization_members
                        this.updateOrganizationSuperAdmin(cmd.getEntries(),cmd.getNamespaceId(),cmd.getOrganizationId());
                    }else if(cmd.getIsJoined() == TrueOrFalseFlag.FALSE.getCode()){
                        //说明该更换的超级管理员没有加入企业，那也就是说在eh_organization_members表中是不存在记录的，那么我们就给其在eh_organization_members
                        //表中创建一条记录，帮助其成为该公司的员工
                        //创建OrganizationMember类的对象
                        OrganizationMember organizationMember = new OrganizationMember();
                        //将数据封装在该对象中
                        organizationMember.setOrganizationId(cmd.getOrganizationId());
                        organizationMember.setTargetType(OrganizationMemberTargetType.USER.getCode());
                        organizationMember.setContactName(cmd.getContactor());
                        organizationMember.setContactToken(cmd.getEntries());
                        organizationMember.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
                        organizationMember.setGroupType(OrganizationTypeEnum.ENTERPRISE.getCode());
                        organizationMember.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
                        organizationMember.setNamespaceId(cmd.getNamespaceId());
                        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByTokenAndNamespaceId(cmd.getEntries(),cmd.getNamespaceId());
                        organizationMember.setTargetId(userIdentifier.getOwnerUid());
                        //// TODO: 2018/5/9
                        organizationProvider.insertIntoOrganizationMember(organizationMember);
                        //更改eh_organizations表中的admin_target_id字段
                        Organization organization = new Organization();
                        //将数据封装在Organization对象中
                        organization.setId(cmd.getOrganizationId());
                        organization.setAdminTargetId(organizationMember.getId());
                        //更新eh_organizations表中的信息
                        organizationProvider.updateOrganizationSuperAdmin(organization);

                    }

                }

            }*/

            if(cmd.getEntries() != null && cmd.getContactor() != null && cmd.getOrganizationId() != null){
                //接下来创建超级管理员
                //创建CreateOrganizationAdminCommand类的对象
                CreateOrganizationAdminCommand cmdnew = new CreateOrganizationAdminCommand();
                //将数据封装进去
                cmdnew.setContactToken(cmd.getEntries());
                cmdnew.setContactName(cmd.getContactor());
                cmdnew.setOrganizationId(cmd.getOrganizationId());
                OrganizationContactDTO organizationContactDTO = rolePrivilegeService.createOrganizationSuperAdmin(cmdnew);


                //查看eh_organization_members表中信息
                OrganizationMember organizationMember = organizationProvider.findOrganizationMemberSigned(cmd.getEntries(),
                        cmd.getNamespaceId(),OrganizationMemberGroupType.MANAGER.getCode());
                //将该organizationMember的id值更新到eh_organizations表中的admin_target_id字段中
                if(organizationMember != null){
                    //创建Organization类的对象
                    Organization organization1 = new Organization();
                    //封装信息
                    organization1.setAdminTargetId(organizationMember.getTargetId());
                    organization1.setId(cmd.getOrganizationId());
                    //更新eh_organizations表信息
                    organizationProvider.updateOrganizationByOrgId(organization1);
                }
            }

            return null;
        });

    }



    /**
     * 添加入驻企业（标准版）
     * @param cmd
     */
    @Override
    public void createSettledEnterprise(CreateSettledEnterpriseCommand cmd){
        //所有的操作都保持在一个事务中
        dbProvider.execute((TransactionStatus status) -> {
            //向办公地点表中添加数据
            if(cmd.getOfficeSites() != null){
                //说明传过来的所在项目和名称以及其中的楼栋和门牌不为空，那么我们将其进行遍历
                for(CreateOfficeSiteCommand createOfficeSiteCommand :cmd.getOfficeSites()){
                    //这样的话拿到的是每一个办公所在地，以及其中的楼栋和门牌
                    //首先我们将办公地址名称和办公地点id持久化到表eh_organization_workPlaces中
                    //创建OrganizationWorkPlaces类的对象
                    OrganizationWorkPlaces organizationWorkPlaces = new OrganizationWorkPlaces();
                    //将数据封装在对象中
                    organizationWorkPlaces.setCommunityId(createOfficeSiteCommand.getCommunityId());
                    organizationWorkPlaces.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    organizationWorkPlaces.setWorkplaceName(createOfficeSiteCommand.getSiteName());
                    //将上面的organization对象中的id也封装在对象中
                    organizationWorkPlaces.setOrganizationId(cmd.getOrganizationId());
                    //调用organizationProvider中的insertIntoOrganizationWorkPlaces方法,将对象持久化到数据库
                    organizationProvider.insertIntoOrganizationWorkPlaces(organizationWorkPlaces);
                    //接下来我们需要将对应的所在项目的楼栋和门牌也持久化到项目和楼栋门牌的关系表eh_communityAndBuilding_relationes中


                    //在这里我们还需要维护eh_organization_community_requests这张表
                    //创建OrganizationCommunityRequest类的对象
                    OrganizationCommunityRequest organizationCommunityRequest = new OrganizationCommunityRequest();
                    //将数据封装在对象OrganizationCommunityRequest对象中
                    organizationCommunityRequest.setCommunityId(createOfficeSiteCommand.getCommunityId());
                    organizationCommunityRequest.setMemberId(cmd.getOrganizationId());
                    organizationCommunityRequest.setMemberType(EnterpriseCommunityMapType.Organization.getCode());
                    organizationCommunityRequest.setMemberStatus(EnterpriseCommunityMapStatus.ACTIVE.getCode());
                    //// TODO: 2018/5/22
                    enterpriseProvider.insertIntoOrganizationCommunityRequest(organizationCommunityRequest);


                    //首先进行遍历楼栋集合
                    if(createOfficeSiteCommand.getSiteDtos() != null){
                        //说明楼栋和门牌不为空，注意他是一个集合
                        //遍历
                        for(OrganizationSiteApartmentDTO organizationSiteApartmentDTO : createOfficeSiteCommand.getSiteDtos()){
                            //这样的话我们拿到的是每一个楼栋以及对应的门牌
                            //创建CommunityAndBuildingRelationes对象，并且将数据封装在对象中，然后持久化到数据库
                            CommunityAndBuildingRelationes communityAndBuildingRelationes = new CommunityAndBuildingRelationes();
                            communityAndBuildingRelationes.setCommunityId(createOfficeSiteCommand.getCommunityId());
                            communityAndBuildingRelationes.setAddressId(organizationSiteApartmentDTO.getApartmentId());
                            communityAndBuildingRelationes.setBuildingId(organizationSiteApartmentDTO.getBuildingId());
                            //调用organizationProvider中的insertIntoCommunityAndBuildingRelationes方法，将对象持久化到数据库
                            organizationProvider.insertIntoCommunityAndBuildingRelationes(communityAndBuildingRelationes);
                            //// TODO: 2018/5/28
                            //同时向eh_organization_addresses表中添加一条记录，表示的是该楼栋中的该门牌已经被入驻，
                            //创建OrganizationAddress类的对象
                            OrganizationAddress organizationAddress = new OrganizationAddress();
                            //将数据封装在对象OrganizationAddress对象中
                            organizationAddress.setAddressId(organizationSiteApartmentDTO.getApartmentId());
                            organizationAddress.setBuildingId(organizationSiteApartmentDTO.getBuildingId());
                            organizationAddress.setOrganizationId(cmd.getOrganizationId());
                            organizationAddress.setBuildingName(organizationSiteApartmentDTO.getBuildingName());
                            //持久化到数据库
                            //// TODO: 2018/5/28
                            organizationProvider.insertIntoOrganizationAddress(organizationAddress);

                        }
                    }

                }
            }
            return null;
        });
    }

    /**
     * 根据组织ID来删除办公地点
     * @param cmd
     */
    @Override
    public void deleteWorkPlacesByOrgId(DeleteWorkPlacesCommand cmd){
        //1.首先需要对参数进行非空校验
        if(cmd.getOrganizationId() != null){
            //说明前端传过来的参数不为空，那么我们需要根据该organization_id来删除eh_communityAndBuilding_relationes
            //表中的关系，就表示的是将该项目下的该公司的办公地点删除了

        	OrganizationWorkPlaces wp = organizationProvider.findWorkPlacesByOrgId(cmd.getOrganizationId(), cmd.getSiteName(), cmd.getCommunityId());
        	if(wp == null) {
        		return;
        	}

        	EnterpriseCommunityMap enterpriseCommunityMap = new EnterpriseCommunityMap();
        	enterpriseCommunityMap.setCommunityId(wp.getCommunityId());
        	enterpriseCommunityMap.setMemberId(cmd.getOrganizationId());
        	enterpriseProvider.deleteEnterpriseFromEnterpriseCommunityMapByOrgIdAndCommunityId(enterpriseCommunityMap);

        	organizationProvider.deleteOrganizationCommunityRequestByCommunityIdAndOrgId(wp.getCommunityId(), wp.getOrganizationId());
        	organizationProvider.deleteWorkPlacesByOrgId(cmd.getOrganizationId(), cmd.getSiteName(), cmd.getCommunityId());

        }else{
            LOGGER.info("organizationId can not be null");
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_CONTACTTOKEN_ISNULL, "organizationId can not be null");
        }
    }

    /**
     * 根据手机号、域空间Id、组织ID来查询eh_organization_members表中是否存在记录，如果存在记录的话，将该记录的id值设置为eh_organizations表中的admin_target_id值
     * @param entries
     * @param namespaceId
     * @param organizationId
     */
    private void updateOrganizationSuperAdmin(String entries,Integer namespaceId,Long organizationId) {
        //查询表eh_organization_members
        Long id = organizationProvider.findOrganizationMembersByTokenAndSoON(entries, namespaceId, organizationId);
        //非空校验
        dbProvider.execute((TransactionStatus status) -> {
            if (id != null && !"".equals(id)) {
                //说明有值,然后我们将该organizationMembers表中的member_group字段设置为manager表示的是管理员
                //创建一个OrganizationMember对象
                OrganizationMember organizationMember = new OrganizationMember();
                //封装数据进去
                organizationMember.setId(id);
                organizationMember.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
                //更新OrganizationMember表数据
                organizationProvider.updateOrganizationMember(organizationMember);
                //然后我们将这个id更新到eh_organizations表中的对应的admin_target_id字段中
                //创建一个Organization类的对象
                Organization organization = new Organization();
                //将数据封装在Organization对象中
                organization.setId(organizationId);
                organization.setAdminTargetId(id);
                //更新eh_organizations表中的信息
                organizationProvider.updateOrganizationSuperAdmin(organization);
            }
            return null;
        });
    }

    /**
     * 根据公司ID和域空间ID来删除公司以及相应的信息
     * @param cmd
     */
    @Override
    public void destoryOrganizationByOrgId(DestoryOrganizationCommand cmd){
        //1.首先需要进行非空校验
        if(cmd.getNamespaceId() != null && cmd.getOrganizationId() != null){
            //说明传过来的参数不为空，那么我们就跟军该参数进行下面的一系列的操作
            //下面所有的操作都保证在同一个事务中进行
            dbProvider.execute((TransactionStatus status) -> {
                //删除公司之前还需要检查该公司名下是否存在管理的项目，如果有则给出提示“无法注销企业。当前企业仍存在需要管理的项目。请转移项目管理权至其它公司后再试。”
                //根据organizationId来查询eh_organization_communities表中管理的项目是否存在
                List<OrganizationCommunity> organizationCommunityList = organizationProvider.listOrganizationCommunities(cmd.getOrganizationId());
                //非空判断
                if(!CollectionUtils.isEmpty(organizationCommunityList)){
                    //说明该公司下面存在管理公司，那么我们现在就报错给前端，提示"无法注销企业。当前企业仍存在需要管理的项目。请转移项目管理权至其它公司后再试。"
                    LOGGER.error("there are communiyies under this organization");
                    throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
                            "there are communiyies under this organization");
                }
                //删除该公司下面的所有的应用
                //// TODO: 2018/5/15 这个严军还没有实现暂时不调用
//                serviceModuleAppService.uninstallAppByOrganizationId(cmd.getOrganizationId());
                //删除所有被其它公司授权管理应用的记录
                //// TODO: 2018/5/15 这个严军暂时还没有实现，暂时不调用
//                serviceModuleAppAuthorizationService.deleteServiceModuleAppAuthorizationByOrganizationId(cmd.getOrganizationId());
                //删除该公司下面的人事档案信息即根据域空间Id和组织Id来删除表eh_organization_member_details中信息
                organizationProvider.deleteOrganizationMemberDetailByNamespaceIdAndOrgId(cmd.getOrganizationId(),cmd.getNamespaceId());
                //根据域空间Id和组织ID来删除eh_organization_members表中的信息
                organizationProvider.deleteOrganizationMemberByNamespaceIdAndOrgId(cmd.getOrganizationId(),cmd.getNamespaceId());
                //根据组织ID来删除表eh_organization_details中的信息
                organizationProvider.deleteOrganizationDetailByOrganizationId(cmd.getOrganizationId());
                //根据组织id来删除eh_organizations表中的信息
                organizationProvider.deleteOrganizationsById(cmd.getOrganizationId());
                //根据组织ID来删除eh_organization_workplaces表（公司所在项目关系表）中的信息
                organizationProvider.deleteOrganizationWorkPlacesByOrgId(cmd.getOrganizationId());
                //根据组织Id来删除表eh_organization_community_requests表中的信息
                organizationProvider.deleteOrganizationCommunityRequestByOrgId(cmd.getOrganizationId());
                return null;
            });
        }
    }

    /**
     * 根据公司Id、域空间Id、移动工作台状态来开启或者禁用移动工作台
     * @param cmd
     */
    @Override
    public void changeWorkBenchFlag(ChangeWorkBenchFlagCommand cmd){
        //1.首先需要对参数进行非空校验
        if(cmd.getOrganizationId() != null && cmd.getNamespaceId() != null && cmd.getWorkBenchFlag() != null){
            //说明参数不为空，那么我们就根据该参数来进行修改eh_organizations表中的work_platform_flag字段的值，1-表示的是开启工作台，0-表示的是禁用工作台
            //调用dao层进行修改数据
            organizationProvider.updateWorkBenchFlagByOrgIdAndNamespaceIdWithWorkBenchFlag(cmd.getOrganizationId(),cmd.getNamespaceId(),cmd.getWorkBenchFlag());

            //发送消息
            //根据公司id来查询eh_organization_members表中的target_id集合
            List<Long> targetIdList = organizationProvider.findTargetIdListByOrgId(cmd.getOrganizationId());
            Organization organization = organizationProvider.findOrganizationById(cmd.getOrganizationId());
            //进行非空校验
            if(!CollectionUtils.isEmpty(targetIdList)){
                //说明查询出来的targetIdList不为空，然后我们根据前端传过来的工作台标志，判断是开启工作台还是关闭工作台，然后进行不同的操作
                //采用forEach循环进行遍历集合targetIdList
                for(Long lon : targetIdList){
                    if(cmd.getWorkBenchFlag() == TrueOrFalseFlag.TRUE.getCode()){
                        //说明是开启工作台
                        String openOrCloseType = MetaObjectType.WORK_BENCH_FLAG_OPEN.getCode();

                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("organizationName", organization.getName());

                        String scope = WorkBenchTemplateCode.SCOPE;
                        Integer namespaceId = 2;
                        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(namespaceId,scope, WorkBenchTemplateCode.WORK_BENCH_PLATFORM_OPEN, "zh_CN", map, "");

                        groupService.workBenchSendMessageToUser(lon , notifyTextForApplicant , openOrCloseType);
                    }else if(cmd.getWorkBenchFlag() == TrueOrFalseFlag.FALSE.getCode()){
                        //说明是进行的是关闭工作台
                        String openOrCloseType = MetaObjectType.WORK_BENCH_FLAG_CLOSE.getCode();
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("organizationName", organization.getName());

                        String scope = WorkBenchTemplateCode.SCOPE;
                        Integer namespaceId = 2;
                        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(namespaceId,scope, WorkBenchTemplateCode.WORK_BENCH_PLATFORM_CLOSE, "zh_CN", map, "");
                        groupService.workBenchSendMessageToUser(lon , notifyTextForApplicant , openOrCloseType);
                    }
                }
            }
        }
    }


    /**
     * 根据公司id、办公地点名称、项目id、办公地点名称全称来进行修改办公地点名称
     * @param cmd
     */
    @Override
    public void updateWholeAddressName(WholeAddressComamnd cmd){

        //首先需要进行非空校验
        if(cmd.getCommunityId() != null && cmd.getOrganizationId() != null && cmd.getSiteName() != null && cmd.getWholeAddressNameNew() != null &&
                cmd.getWholeAddressNameOld() != null){
            //说明上面的参数不为空，那么我们就根据上面的参数来进行更新表eh_organization_workplaces中的信息
            //// TODO: 2018/5/25
            organizationProvider.updateWholeAddressName(cmd.getOrganizationId(),cmd.getSiteName(),cmd.getCommunityId(),cmd.getWholeAddressNameNew(),cmd.getWholeAddressNameOld());
        }
    }

    @Override
    public void updateCustomerEntryInfo(EnterpriseCustomer customer, OrganizationAddress address) {
        LOGGER.debug("updateCustomerEntryInfo customer id: {}, address: {}", customer.getId(), address);
        if (address != null) {
            List<CustomerEntryInfo> entryInfos = enterpriseCustomerProvider.listCustomerEntryInfos(customer.getId());
            List<Long> addressIds = new ArrayList<>();
            if (entryInfos != null && entryInfos.size() > 0) {
                addressIds = entryInfos.stream().map(EhCustomerEntryInfos::getAddressId).collect(Collectors.toList());
            }

            if (!addressIds.contains(address.getAddressId())) {
                CustomerEntryInfo info = new CustomerEntryInfo();
                info.setNamespaceId(customer.getNamespaceId());
                info.setCustomerType(CustomerType.ENTERPRISE.getCode());
                info.setCustomerId(customer.getId());
                info.setCustomerName(customer.getName());
                info.setAddressId(address.getAddressId());
                info.setBuildingId(address.getBuildingId());
                enterpriseCustomerProvider.createCustomerEntryInfo(info);
            }
        }
    }


    @Override
    public OrganizationDTO getAuthOrgByProjectIdAndAppId(GetAuthOrgByProjectIdAndAppIdCommand cmd) {

        OrganizationDTO dto = null;

        if(namespacesService.isStdNamespace(UserContext.getCurrentNamespaceId())){
            //标准版
            ServiceModuleAppAuthorization serviceModuleAppAuthorization = serviceModuleAppAuthorizationService.findServiceModuleAppAuthorization(cmd.getProjectId(), cmd.getAppId());
            if(serviceModuleAppAuthorization != null){
                Organization organization = organizationProvider.findOrganizationById(serviceModuleAppAuthorization.getOrganizationId());
                dto = ConvertHelper.convert(organization, OrganizationDTO.class);
            }

        }else {
            //定制版

            List<OrganizationCommunityDTO> organizationCommunities = organizationProvider.findOrganizationCommunityByCommunityId(cmd.getProjectId());

            if(organizationCommunities != null && organizationCommunities.size() > 0){
                Organization organization = organizationProvider.findOrganizationById(organizationCommunities.get(0).getOrganizationId());

                dto = ConvertHelper.convert(organization, OrganizationDTO.class);
            }
        }

        return dto;
    }



    @Override
    public ListUserOrganizationsResponse listUserOrganizations(ListUserOrganizationsCommand cmd) {

        Long userId = cmd.getUserId();
        if(userId == null){
            userId = UserContext.currentUserId();
        }

        if(userId == null){
            return null;
        }

        List<OrganizationMember> orgMembers = this.organizationProvider.listOrganizationMembers(userId);
        if(orgMembers == null || orgMembers.size() == 0){
            return null;
        }


        //查找项目的管理公司  add by yanjun 20180801
        GetAuthOrgByProjectIdAndAppIdCommand authCmd = new GetAuthOrgByProjectIdAndAppIdCommand();
        authCmd.setProjectId(cmd.getProjectId());
        authCmd.setAppId(cmd.getAppId());
        OrganizationDTO authOrg = getAuthOrgByProjectIdAndAppId(authCmd);


        List<OrganizationDTO> dtos = new ArrayList<>();
        for (OrganizationMember member : orgMembers) {

            if(OrganizationGroupType.ENTERPRISE != OrganizationGroupType.fromCode(member.getGroupType())){
                continue;
            }

            // 如果机构不存在，则丢弃该成员对应的机构
            Organization org = this.organizationProvider.findOrganizationById(member.getOrganizationId());
            if (org == null) {
                LOGGER.error("The member is ignored for organization not found, userId=" + userId
                        + ", organizationId=" + member.getOrganizationId() + ", orgMemberId=" + member.getId());
                continue;
            }

            if (OrganizationGroupType.fromCode(org.getGroupType()) != OrganizationGroupType.ENTERPRISE) {
                LOGGER.error("The member is ignored for organization group type not matched, userId=" + userId
                        + ", organizationId=" + member.getOrganizationId() + ", orgMemberId=" + member.getId());
                continue;
            }

            OrganizationCommunityRequest request = organizationProvider.findOrganizationCommunityRequestByOrganizationId(cmd.getProjectId(), org.getId());
            if (request == null) {
                //不是该项目的，跳过
                continue;
            }


            //Filter out the inactive organization add by sfyan 20130430
            OrganizationStatus orgStatus = OrganizationStatus.fromCode(org.getStatus());
            if (orgStatus != OrganizationStatus.ACTIVE) {
                LOGGER.error("The member is ignored for organization not active, userId=" + userId
                        + ", organizationId=" + member.getOrganizationId() + ", orgMemberId=" + member.getId() + ", orgStatus" + orgStatus);
                continue;
            }

            //Filter out the child organization add by lei.lv 20171124
            Long parentId = org.getParentId();
            if (parentId != 0L) {
                LOGGER.error("The member's organization is child-enterprise, userId=" + userId
                        + ", organizationId=" + member.getOrganizationId() + ", orgMemberId=" + member.getId() + ", orgStatus" + orgStatus);
                continue;
            }

            OrganizationDTO dto = toOrganizationDTO(userId, org);


            if(authOrg != null && authOrg.getId().equals(dto.getId())){
                dto.setProjectManageFlag(com.everhomes.rest.common.TrueOrFalseFlag.TRUE.getCode());
            }else {
                dto.setProjectManageFlag(com.everhomes.rest.common.TrueOrFalseFlag.FALSE.getCode());
            }

            dtos.add(dto);
        }

        ListUserOrganizationsResponse response = new ListUserOrganizationsResponse();
        response.setDtos(dtos);
        return response;
    }
    
    @Override
	public List<Long> getProjectIdsByCommunityAndModuleApps(Integer namespaceId, Long communityId, Long moduleId, AppInstanceConfigConfigMatchCallBack matchCallback) {

		// 根据type获取相应的appId
    	namespaceId = null == namespaceId ? UserContext.getCurrentNamespaceId() : namespaceId;
		List<ServiceModuleAppDTO> dtos = serviceModuleService.getModuleApps(namespaceId, moduleId);
		if (CollectionUtils.isEmpty(dtos)) {
			return null;
		}

		ServiceModuleAppDTO targetAppDto = null;
		for (ServiceModuleAppDTO dto : dtos) {
			if (!StringUtils.isEmpty(dto.getInstanceConfig()) && matchCallback.match(dto.getInstanceConfig())) {
				targetAppDto = dto;
				break;
			}
		}

		if (null == targetAppDto) {
			return null;
		}

		// 获取到管理公司
		GetAuthOrgByProjectIdAndAppIdCommand cmd = new GetAuthOrgByProjectIdAndAppIdCommand();
		cmd.setAppId(targetAppDto.getOriginId());
		cmd.setProjectId(communityId);
		OrganizationDTO orgDto = getAuthOrgByProjectIdAndAppId(cmd);
		if (null == orgDto) {
			return null;
		}

		// 获取管理公司下的该应用下所有项目
		return getOrganizationProjectIdsByAppId(orgDto.getId(),targetAppDto.getOriginId());
	}

	@Override
	public List<Long> getOrganizationProjectIdsByAppId(Long organizationId, Long originAppId) {
		ListCommunitiesByOrgIdAndAppIdCommand listCmd = new ListCommunitiesByOrgIdAndAppIdCommand();
		listCmd.setOrgId(organizationId);
		listCmd.setAppId(originAppId);
		ListCommunitiesByOrgIdAndAppIdResponse resp = communityService.listCommunitiesByOrgIdAndAppId(listCmd);

		return resp.getDtos() == null ? null : resp.getDtos().stream().map(ProjectDTO::getProjectId).collect(Collectors.toList());

	}

    //	物业组所需获取企业员工的唯一标识符
    @Override
    public String getAccountByTargetIdAndOrgId(Long targetId, Long orgId){
        OrganizationMemberDetails employee = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(targetId, orgId);
        if(employee == null)
            return "";
        return employee.getAccount();
    }
}

