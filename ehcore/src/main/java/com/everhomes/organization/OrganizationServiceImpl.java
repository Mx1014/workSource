// @formatter:off
package com.everhomes.organization;

import com.everhomes.acl.*;
import com.everhomes.aclink.DoorAccessService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Building;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.FamilyProvider;
import com.everhomes.family.FamilyService;
import com.everhomes.forum.Forum;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.ForumService;
import com.everhomes.forum.Post;
import com.everhomes.group.Group;
import com.everhomes.group.GroupAdminStatus;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.mail.MailHandler;
import com.everhomes.menu.Target;
import com.everhomes.messaging.MessagingService;
import com.everhomes.module.ServiceModuleAssignment;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractBuildingMappingProvider;
import com.everhomes.organization.pm.CommunityPmContact;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rentalv2.RentalNotificationTemplateCode;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.acl.RoleConstants;
import com.everhomes.rest.acl.admin.AclRoleAssignmentsDTO;
import com.everhomes.rest.acl.admin.DeleteOrganizationAdminCommand;
import com.everhomes.rest.acl.admin.RoleDTO;
import com.everhomes.rest.address.AddressAdminStatus;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.business.listUsersOfEnterpriseCommand;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.common.IncludeChildFlagType;
import com.everhomes.rest.common.QuestionMetaActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.contract.ContractDTO;
import com.everhomes.rest.enterprise.*;
import com.everhomes.rest.family.LeaveFamilyCommand;
import com.everhomes.rest.family.ParamType;
import com.everhomes.rest.forum.*;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.rest.group.GroupJoinPolicy;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.group.GroupPrivacy;
import com.everhomes.rest.launchpad.ItemKind;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.module.Project;
import com.everhomes.rest.namespace.ListCommunityByNamespaceCommandResponse;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.organization.CreateOrganizationOwnerCommand;
import com.everhomes.rest.organization.DeleteOrganizationOwnerCommand;
import com.everhomes.rest.organization.pm.*;
import com.everhomes.rest.region.RegionScope;
import com.everhomes.rest.search.GroupQueryResult;
import com.everhomes.rest.search.OrganizationQueryResult;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.techpark.company.ContactType;
import com.everhomes.rest.ui.privilege.EntrancePrivilege;
import com.everhomes.rest.ui.privilege.GetEntranceByPrivilegeCommand;
import com.everhomes.rest.ui.privilege.GetEntranceByPrivilegeResponse;
import com.everhomes.rest.ui.user.ContactSignUpStatus;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.user.*;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.search.PostAdminQueryFilter;
import com.everhomes.search.PostSearcher;
import com.everhomes.search.UserWithoutConfAccountSearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.*;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.userOrganization.UserOrganizationProvider;
import com.everhomes.userOrganization.UserOrganizations;
import com.everhomes.util.*;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.*;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private ContractBuildingMappingProvider contractBuildingMappingProvider;

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
        if (null == OrganizationGroupType.fromCode(cmd.getGroupType())) {
            LOGGER.error("organization group type error. cmd = {}", cmd);
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ASSIGNMENT_EXISTS,
                    "organization group type error");
        }
        Organization organization = ConvertHelper.convert(cmd, Organization.class);
        Organization parOrg = this.checkOrganization(cmd.getParentId());

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
     * @param addMemberIds
     * @param delMemberIds
     * @param organization
     */
    private void batchUpdateOrganizationMember(List<Long> addMemberIds, List<Long> delMemberIds, Organization organization) {
        if (null != delMemberIds) {
            for (Long memberId : delMemberIds) {
                organizationProvider.deleteOrganizationMemberById(memberId);
            }
        } else {
            LOGGER.debug("delete members is null");
        }

        if (null != addMemberIds) {
            for (Long memberId : addMemberIds) {
                OrganizationMember member = organizationProvider.findOrganizationMemberById(memberId);
                if (null != member) {
                    OrganizationMember organizationMember = organizationProvider.findOrganizationMemberByOrgIdAndToken(member.getContactToken(), organization.getId());
                    if (null == organizationMember) {
                        member.setOrganizationId(organization.getId());
                        member.setGroupType(organization.getGroupType());
                        member.setGroupPath(organization.getPath());
                        organizationProvider.createOrganizationMember(member);
                    } else {
                        LOGGER.debug("organization member already existing. organizationId = {}, contactToken = {}", organizationMember.getOrganizationId(), organizationMember.getContactToken());
                    }
                }
            }
        } else {
            LOGGER.debug("add members is null");
        }

    }

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


    @Override
    public void updateChildrenOrganization(UpdateOrganizationsCommand cmd) {

        User user = UserContext.current().getUser();

        //先判断，后台管理员才能创建。状态直接设为正常

        if (null == cmd.getNaviFlag()) {
            cmd.setNaviFlag((byte) 1);
        }

        Organization parOrg = this.checkOrganization(cmd.getId());

        parOrg.setShowFlag(cmd.getNaviFlag());
        parOrg.setName(cmd.getName());

        Organization org = dbProvider.execute((TransactionStatus status) -> {
            if (OrganizationGroupType.fromCode(parOrg.getGroupType()) == OrganizationGroupType.ENTERPRISE) {
                OrganizationDetail enterprise = organizationProvider.findOrganizationDetailByOrganizationId(parOrg.getId());
                if (null != enterprise) {
                    enterprise.setAddress(cmd.getAddress());
                    organizationProvider.updateOrganizationDetail(enterprise);
                }

                // 创建经理群组
                Organization managerGroup = this.createManagerGroup(parOrg, parOrg.getId());

                // 更新经理群组人员
                this.batchUpdateOrganizationMember(cmd.getAddManagerMemberIds(), cmd.getDelManagerMemberIds(), managerGroup);

            } else if (OrganizationGroupType.fromCode(parOrg.getGroupType()) == OrganizationGroupType.JOB_POSITION) {

                //更新通用岗位
                this.updateOrganizationJobPositionMap(parOrg, cmd.getJobPositionIds());

                //更新组人员
                this.batchUpdateOrganizationMember(cmd.getAddMemberIds(), cmd.getDelMemberIds(), parOrg);
            } else if (OrganizationGroupType.fromCode(parOrg.getGroupType()) == OrganizationGroupType.JOB_LEVEL) {
                // 增加职级大小
                parOrg.setSize(cmd.getSize());
                //更新组人员
                this.batchUpdateOrganizationMember(cmd.getAddMemberIds(), cmd.getDelMemberIds(), parOrg);
            } else if (OrganizationGroupType.fromCode(parOrg.getGroupType()) == OrganizationGroupType.DEPARTMENT || OrganizationGroupType.fromCode(parOrg.getGroupType()) == OrganizationGroupType.GROUP) {
                // 创建经理群组
                Organization managerGroup = this.createManagerGroup(parOrg, parOrg.getDirectlyEnterpriseId());
                // 更新经理群组人员
                this.batchUpdateOrganizationMember(cmd.getAddManagerMemberIds(), cmd.getDelManagerMemberIds(), managerGroup);
            }

            parOrg.setOperatorUid(user.getId());
            organizationProvider.updateOrganization(parOrg);

            if (null != cmd.getCommunityId()) {
                if (OrganizationCommunityScopeType.CURRENT == OrganizationCommunityScopeType.fromCode(cmd.getScopeType())) {
                    //修改当前节点
                    updateCurrentOrganziationCommunityReqeust(user.getId(), parOrg.getId(), cmd.getCommunityId());
                } else if (OrganizationCommunityScopeType.CURRENT_CHILD == OrganizationCommunityScopeType.fromCode(cmd.getScopeType())) {
                    //修改当前节点
                    updateCurrentOrganziationCommunityReqeust(user.getId(), parOrg.getId(), cmd.getCommunityId());
                    //修改所有子节点
                    updateChildOrganizationCommunityRequest(user.getId(), parOrg.getPath(), cmd.getCommunityId());
                } else if (OrganizationCommunityScopeType.CURRENT_LEVEL_CHILD == OrganizationCommunityScopeType.fromCode(cmd.getScopeType())) {
                    //修改所有子节点
                    updateChildOrganizationCommunityRequest(user.getId(), parOrg.getPath(), cmd.getCommunityId());

                    //如果修改的是根节点，则不需要修改同级只需要修改当前节点
                    if (0L != parOrg.getParentId()) {
                        //修改同级节点包括当前节点
                        updateLevenOrganizationCommunityRequest(user.getId(), parOrg.getParentId(), cmd.getCommunityId());
                    } else {
                        //修改当前节点
                        updateCurrentOrganziationCommunityReqeust(user.getId(), parOrg.getId(), cmd.getCommunityId());
                    }
                }
            } else {
                LOGGER.warn("communityId is null");
            }

            return parOrg;
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
            dtos.add(toOrganizationDetailDTO(id, false));
        }
        resp.setDtos(dtos);
        return resp;
    }

    private OrganizationDetailDTO toOrganizationDetailDTO(Long id, Boolean flag){
        Long userId = UserContext.current().getUser().getId();

        Organization organization = organizationProvider.findOrganizationById(id);
        OrganizationDetail org = organizationProvider.findOrganizationDetailByOrganizationId(id);
        if(null == organization){
            LOGGER.debug("organization is null, id = " + id);
            return null;
        }else if(OrganizationGroupType.fromCode(organization.getGroupType()) != OrganizationGroupType.ENTERPRISE){
            LOGGER.debug("organization not is enterprise, id = " + id);
            return null;
        }else if(organization.getParentId() != 0L){
            LOGGER.debug("organization is children organization, id = " + id);
            return null;
        }

        OrganizationDTO organizationDTO = processOrganizationCommunity(ConvertHelper.convert(organization, OrganizationDTO.class));

        if(null == org){
            org = new OrganizationDetail();
            org.setOrganizationId(organization.getId());
        }

        OrganizationDetailDTO dto = ConvertHelper.convert(org, OrganizationDetailDTO.class);
        //modify by dengs,20170512,将经纬度转换成 OrganizationDetailDTO 里面的类型，不改动dto，暂时不影响客户端。后面考虑将dto的经纬度改成Double
        if(null != org.getLatitude())
            dto.setLatitude(org.getLatitude().toString());
        if(null != org.getLongitude())
            dto.setLongitude(org.getLongitude().toString());
        //end
        dto.setEmailDomain(org.getEmailDomain());
        dto.setName(organization.getName());
        dto.setCommunityId(organizationDTO.getCommunityId());
        dto.setCommunityName(organizationDTO.getCommunityName());
        dto.setAvatarUri(org.getAvatar());
        if(null != org.getCheckinDate())
            dto.setCheckinDate(org.getCheckinDate().getTime());
        if(!StringUtils.isEmpty(org.getAvatar()))
            dto.setAvatarUrl(contentServerService.parserUri(dto.getAvatarUri(), EntityType.ORGANIZATIONS.getCode(), dto.getOrganizationId()));

        if(!StringUtils.isEmpty(dto.getPostUri()))
            dto.setPostUrl(contentServerService.parserUri(dto.getPostUri(), EntityType.ORGANIZATIONS.getCode(), dto.getOrganizationId()));

        List<OrganizationAddress> organizationAddresses = organizationProvider.findOrganizationAddressByOrganizationId(dto.getOrganizationId());
        List<AddressDTO> addresses = organizationAddresses.stream().map(r->{
            OrganizationAddressDTO address = ConvertHelper.convert(r,OrganizationAddressDTO.class);
            Address addr = addressProvider.findAddressById(address.getAddressId());
            return ConvertHelper.convert(addr, AddressDTO.class);
        }).collect(Collectors.toList());

        dto.setAddresses(addresses);
        List<OrganizationAttachment> attachments = organizationProvider.listOrganizationAttachments(dto.getOrganizationId());

        if(null != attachments && 0 != attachments.size()){
            for (OrganizationAttachment attachment : attachments) {
                attachment.setContentUrl(contentServerService.parserUri(attachment.getContentUri(), EntityType.ORGANIZATIONS.getCode(), dto.getOrganizationId()));
            }

            dto.setAttachments(attachments.stream().map(r->{ return ConvertHelper.convert(r,AttachmentDescriptor.class); }).collect(Collectors.toList()));
        }

        List<Long> roles = new ArrayList<Long>();
        roles.add(RoleConstants.ENTERPRISE_SUPER_ADMIN);

        if(flag){
            List<OrganizationMember> members = this.getOrganizationAdminMemberRole(dto.getOrganizationId(), roles);
            if(members.size() > 0){
                dto.setMember(ConvertHelper.convert(members.get(0), OrganizationMemberDTO.class));
            }
        }

        dto.setAccountName(org.getContactor());
        dto.setAccountPhone(org.getContact());

        dto.setServiceUserId(org.getServiceUserId());

        OrganizationMember m = organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, id);
        if(null != m ){
            dto.setMember(ConvertHelper.convert(m, OrganizationMemberDTO.class));
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
		for(Long id : rlt.getIds()) {
        	OrganizationDetailDTO dto = this.toOrganizationDetailDTO(id, cmd.getQryAdminRoleFlag());
        	if(null != dto)
        		dtos.add(dto);
        }
        addExtraInfo(dtos);
        resp.setDtos(dtos);
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
		cmd.setPageSize(10000);
		List<OrganizationDetailDTO> organizationDetailDTOs = listEnterprises(cmd).getDtos();
		 if (organizationDetailDTOs != null && organizationDetailDTOs.size() > 0) {
	            String fileName = String.format("企业信息_%s", DateUtil.dateToStr(new Date(), DateUtil.NO_SLASH));
	            ExcelUtils excelUtils = new ExcelUtils(response, fileName, "企业信息");
//	            
	            List<OrganizationExportDetailDTO> data = organizationDetailDTOs.stream().map(this::convertToExportDetail).collect(Collectors.toList());
	            String[] propertyNames = {"displayName", "emailDomain", "apartments", "signupCount", "memberCount", "serviceUserName", "admins", "address", "contact", "checkinDateString", "description"};
	            String[] titleNames = {"企业名称", "邮箱域名", "楼栋门牌", "注册人数", "企业人数", "客服经理", "企业管理员", "地址", "咨询电话", "入驻时间", "企业介绍"};
	            int[] titleSizes = {20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20};
	            excelUtils.writeExcel(propertyNames, titleNames, titleSizes, data);
	        } else {
	            throw errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_NO_DATA,
	                    "no data");
	        }
	}

	private OrganizationExportDetailDTO convertToExportDetail(OrganizationDetailDTO organizationDetailDTO) {
		OrganizationExportDetailDTO exportDetailDTO = ConvertHelper.convert(organizationDetailDTO, OrganizationExportDetailDTO.class);
		try {
			if(exportDetailDTO.getAddresses() != null) {
				String apartments = String.join("\n", exportDetailDTO.getAddresses().stream().filter(a->a != null).map(AddressDTO::getAddress).collect(Collectors.toList()));
				exportDetailDTO.setApartments(apartments);
			}
			if (exportDetailDTO.getAdminMembers() != null) {
				String admins = String.join("\n", exportDetailDTO.getAdminMembers().stream().filter(a->a != null).map(this::toAdminString).collect(Collectors.toList()));
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
		return organizationContactDTO.getContactName()+"("+organizationContactDTO.getContactToken()+")";
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
        return rolePrivilegeService.listOrganizationAdministrators(command);
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
            OrganizationMember organizationMember = organizationProvider.findOrganizationMemberByOrgIdAndUId(serviceUserId, organizationCommunityDTO.getOrganizationId());
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

    @Override
    public OrganizationDTO createEnterprise(CreateEnterpriseCommand cmd) {

        User user = UserContext.current().getUser();

        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());

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
            organization.setEmailDomain(cmd.getEmailDomain());
            organizationProvider.createOrganization(organization);

            OrganizationDetail enterprise = new OrganizationDetail();
            enterprise.setOrganizationId(organization.getId());
            enterprise.setAddress(cmd.getAddress());
            enterprise.setDescription(cmd.getDescription());
            enterprise.setAvatar(cmd.getAvatar());
            enterprise.setCreateTime(organization.getCreateTime());
            if (!StringUtils.isEmpty(cmd.getCheckinDate())) {
                java.sql.Date checkinDate = DateUtil.parseDate(cmd.getCheckinDate());
                if (null != checkinDate) {
                    enterprise.setCheckinDate(new Timestamp(checkinDate.getTime()));
                }
            }
            enterprise.setContact(cmd.getContactsPhone());
            enterprise.setDisplayName(cmd.getDisplayName());
            enterprise.setPostUri(cmd.getPostUri());
            enterprise.setMemberCount(cmd.getMemberCount());
            enterprise.setEmailDomain(cmd.getEmailDomain());
            enterprise.setServiceUserId(cmd.getServiceUserId());
			if(cmd.getLatitude()!=null)
				enterprise.setLatitude(Double.valueOf(cmd.getLatitude()));
			if(cmd.getLongitude()!=null)
				enterprise.setLongitude(Double.valueOf(cmd.getLongitude()));
            organizationProvider.createOrganizationDetail(enterprise);

            // 把代码移到一个独立的方法，以便其它地方也可以调用 by lqs 20161101
            createActiveOrganizationCommunityRequest(user.getId(), enterprise.getOrganizationId(), cmd.getCommunityId());
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
            organization.setDescription(enterprise.getDescription());

            organizationSearcher.feedDoc(organization);
            return null;
        });
        List<AttachmentDescriptor> attachments = cmd.getAttachments();

        if (null != attachments && 0 != attachments.size()) {
            this.addAttachments(organization.getId(), attachments, user.getId());
        }

        List<OrganizationAddressDTO> addressDTOs = cmd.getAddressDTOs();
        if (null != addressDTOs && 0 != addressDTOs.size()) {
            this.addAddresses(organization.getId(), addressDTOs, user.getId());
        }

        return ConvertHelper.convert(organization, OrganizationDTO.class);
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
					OrganizationAddress address = ConvertHelper.convert(organizationAddressDTO, OrganizationAddress.class);
					address.setOrganizationId(id);
					address.setCreatorUid(userId);
					address.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					address.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					address.setStatus(OrganizationAddressStatus.ACTIVE.getCode());
				    this.organizationProvider.createOrganizationAddress(address);
				}
			}
            return null;
        });

    }

    @Override
    public void updateEnterprise(UpdateEnterpriseCommand cmd) {
        updateEnterprise(cmd, true);
    }
	
	public void updateEnterprise(UpdateEnterpriseCommand cmd, boolean updateAttachmentAndAddress) {
        //先判断，后台管理员才能创建。状态直接设为正常
        Organization organization = checkOrganization(cmd.getId());
        User user = UserContext.current().getUser();

        dbProvider.execute((TransactionStatus status) -> {
            organization.setId(cmd.getId());
            organization.setName(cmd.getName());
            organization.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            organization.setEmailDomain(cmd.getEmailDomain());
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
					if(null != checkinDate){
						organizationDetail.setCheckinDate(new Timestamp(checkinDate.getTime()));
					}
				}else {
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
					if(null != checkinDate){
						organizationDetail.setCheckinDate(new Timestamp(checkinDate.getTime()));
					}
				}else {
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
            organizationSearcher.feedDoc(organization);
            return null;
        });

		if (updateAttachmentAndAddress) {
		List<AttachmentDescriptor> attachments = cmd.getAttachments();

//			if(null != attachments && 0 != attachments.size()){
            this.addAttachments(organization.getId(), attachments, user.getId());
//			}

		List<OrganizationAddressDTO> addressDTOs = cmd.getAddressDTOs();
//			if(null != addressDTOs && 0 != addressDTOs.size()){
            this.addAddresses(organization.getId(), addressDTOs, user.getId());
//			}
        }
    }

    @Override
    public void deleteEnterpriseById(DeleteOrganizationIdCommand cmd) {
        Organization organization = checkOrganization(cmd.getId());
		User user = UserContext.current().getUser();

		dbProvider.execute((TransactionStatus status) -> {
			organization.setStatus(OrganizationStatus.DELETED.getCode());
			Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
			organization.setUpdateTime(now);
			organizationProvider.updateOrganization(organization);

			OrganizationCommunityRequest r = organizationProvider.getOrganizationCommunityRequestByOrganizationId(organization.getId());

			if(null != r){
				r.setMemberStatus(OrganizationCommunityRequestStatus.INACTIVE.getCode());
				r.setUpdateTime(now);
				organizationProvider.updateOrganizationCommunityRequest(r);
			}

            List<OrganizationMember> members = organizationProvider.listOrganizationMembers(organization.getId(), null);
            //把user_organization表中的相应记录更新为失效
            inactiveUserOrganizationWithMembers(members);

			List<OrganizationAddress> organizationAddresses = organizationProvider.findOrganizationAddressByOrganizationId(organization.getId());

			if(null != organizationAddresses && 0 != organizationAddresses.size()){
				for (OrganizationAddress organizationAddress : organizationAddresses) {
					organizationAddress.setStatus(OrganizationAddressStatus.INACTIVE.getCode());
					organizationAddress.setUpdateTime(now);
					organizationProvider.updateOrganizationAddress(organizationAddress);
				}
			}

			/**modify by lei.lv**/
			//删除organizaiton时，需要把organizaiton下面的所有机构状态置为无效，而且把人员和机构的关系置为无效状态
			List<Organization> underOrganiztions = organizationProvider.findOrganizationByPath(organization.getPath());
			underOrganiztions.stream().map((o) ->{
				//更新机构
				o.setStatus(OrganizationStatus.INACTIVE.getCode());
				o.setUpdateTime(now);
				organizationProvider.updateOrganization(o);
				//更新人员
				List<OrganizationMember> underOrganiztionsMembers = organizationProvider.listOrganizationMembersByOrgIdWithAllStatus(o.getId());
				for (OrganizationMember m : underOrganiztionsMembers){
					m.setStatus(OrganizationMemberStatus.INACTIVE.getCode());
					m.setUpdateTime(now);
					m.setOperatorUid(user.getId());
					organizationProvider.updateOrganizationMember(m);
					//解除门禁权限
					doorAccessService.deleteAuthWhenLeaveFromOrg(UserContext.getCurrentNamespaceId(), m.getOrganizationId(), m.getTargetId());
				}
				return null;
			}).collect(Collectors.toList());

            organizationSearcher.deleteById(cmd.getId());
            return null;
        });
    }

	@Override
	public void applyOrganizationMember(ApplyOrganizationMemberCommand cmd) {
		this.checkCommunityIdIsNull(cmd.getCommunityId());
		this.checkCommunity(cmd.getCommunityId());
		User user  = UserContext.current().getUser();
		this.checkCommunityIdIsEqual(user.getCommunityId().longValue(),cmd.getCommunityId().longValue());
		Organization organization = this.checkOrganizationByCommIdAndOrgType(cmd.getCommunityId(), cmd.getOrganizationType());

		this.checkUserInOrg(user.getId(), organization.getId());
		OrganizationMember member = this.createOrganizationMember(user,organization.getId(),cmd.getContactDescription());
		member.setGroupType(organization.getGroupType());
		member.setGroupPath(organization.getPath());
		organizationProvider.createOrganizationMember(member);
	}

	private OrganizationMember checkUserNotInOrg(Long userId, Long orgId) {
		OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(userId,orgId);
		if(member == null){
			LOGGER.error("User is not in the organization.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"User is not in the organization.");
		}
		return member;
	}

	private void checkUserInOrg(Long userId, Long orgId) {
		OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(userId,orgId);
		if(member != null){
			LOGGER.error("User is in the organization.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"User is in the organization.");
		}
	}

	private Organization checkOrganizationByCommIdAndOrgType(Long communityId,String orgType) {
		Organization org = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(communityId, orgType);
		if(org == null) {
			LOGGER.error("organization can not find by communityId and orgType.communityId="+communityId+",orgType="+orgType);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"organization can not find by communityId and orgType.");
		}
		return org;
	}

	private void checkCommunityIdIsNull(Long communityId) {
		if(communityId == null){
			LOGGER.error("communityId paramter is empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"communityId paramter is empty");
		}

	}

	private void checkCommunityIdIsEqual(long longValue, long longValue2) {
		if(longValue != longValue2){
			LOGGER.error("communityId not equal.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"communityId not equal.");
		}
	}

	private OrganizationMember createOrganizationMember(User user, Long organizationId, String contactDescription) {
		OrganizationMember member = new OrganizationMember();

		member.setContactDescription(contactDescription);
		member.setContactName(user.getNickName());
		member.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
		member.setOrganizationId(organizationId);
		member.setStatus(OrganizationMemberStatus.WAITING_FOR_APPROVAL.getCode());
		member.setTargetId(user.getId());
		member.setTargetType(OrganizationMemberTargetType.USER.getCode());

		UserIdentifier identifier = this.getUserMobileIdentifier(user.getId());
		if(identifier != null){
			member.setContactToken(identifier.getIdentifierToken());
			member.setContactType(identifier.getIdentifierType());
		}

		return member;
	}

	private UserIdentifier getUserMobileIdentifier(Long userId) {
		List<UserIdentifier> userIndIdentifiers  = userProvider.listUserIdentifiersOfUser(userId);
		if(userIndIdentifiers != null && userIndIdentifiers.size() > 0){
			for (UserIdentifier userIdentifier : userIndIdentifiers) {
				if(userIdentifier.getIdentifierType().byteValue() == IdentifierType.MOBILE.getCode()){
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
        if (cmd.getForumId() == null ||
                cmd.getVisibleRegionId() == null ||
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
        Organization organization = getOrganization(cmd);
        if (organization == null) {
            LOGGER.error("Unable to find the organization.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_CLASS_NOT_FOUND,
                    "Unable to find the organization.");
        }
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
            OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(user.getId(), organization.getId());
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
	    switch(mixType) {
	    case CHILDREN_ALL:
	        Organization organization = organizationProvider.findOrganizationById(organizationId);
	        if(organization == null) {
	            LOGGER.error("Organization not found, cmd=" + cmd);
	            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
	                    "Organization not found");
	        } else {
	            if(organization.getGroupId() != null) {
	                groupDto = groupProvider.findGroupById(organization.getGroupId());
	            }
                if(groupDto != null) {
                    forumIdList.add(groupDto.getOwningForumId());
                }
	        }

	        groupDto = null; // 如果缺少这一句，则即使每个子公司都没有group，仍然会加到论坛列表中 by lqs 20160429
	        List<String> groupTypes = new ArrayList<String>();
            groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
            List<Organization> subOrgList = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "/%", groupTypes);
            for(Organization subOrg : subOrgList) {
                if(subOrg.getGroupId() != null) {
                    groupDto = groupProvider.findGroupById(subOrg.getGroupId());
                }
                // 添加forumId不为空，空的话后面会报空指针异常。在forum-2.0中，新建帖子和查询帖子的分类都已把子公司去除了。
				// 此处查询"全部"暂时保留   add by yanjun 20170616
                if(groupDto != null && !StringUtils.isEmpty(groupDto.getOwningForumId())) {
                    forumIdList.add(groupDto.getOwningForumId());
                }
            }
    	    ListTopicByForumCommand forumCmd = new ListTopicByForumCommand();
    	    forumCmd.setForumIdList(forumIdList);
    	    forumCmd.setPageAnchor(cmd.getPageAnchor());
    	    forumCmd.setPageSize(cmd.getPageSize());
    	    forumCmd.setExcludeCategories(cmd.getExcludeCategories());
    	    forumCmd.setCategoryId(cmd.getCategoryId());
    	    response = forumService.listTopicsByForums(forumCmd);
	        break;
	    case COMMUNITY_ALL:
	    	QueryOrganizationTopicCommand command = ConvertHelper.convert(cmd, QueryOrganizationTopicCommand.class);
	    	command.setOrganizationId(organizationId);
	    	command.setPrivateFlag(PostPrivacy.PRIVATE.getCode());

	    	// 因为此处EmbeddedAppId为空，在listOrgTopics方法中不会走到活动的查询中，因此此处加上论坛的CategoryId不会和活动的CategoryId混淆。
			// add by yanjn  20170612
	    	command.setCategoryId(cmd.getCategoryId());

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

	@Caching(evict={@CacheEvict(value="ForumPostById", key="#topicId")})
	private void sendComment(long topicId, long forumId, long orgId, OrganizationMember member, long category,int namespaceId) {
		User user = UserContext.current().getUser();
		Post comment = new Post();
		comment.setParentPostId(topicId);
		comment.setForumId(forumId);
		comment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		comment.setCreatorUid(user.getId());
		comment.setContentType(PostContentType.TEXT.getCode());
		String template = this.getOrganizationAssignTopicForCommentTemplate(orgId,member,namespaceId);
		if(StringUtils.isEmpty(template)){
			template = "该请求已安排人员处理";
		}
		if(LOGGER.isDebugEnabled()){
			try {
				LOGGER.error("sendComment_template="+(new String(template.getBytes(),"UTF-8")));
			} catch (UnsupportedEncodingException e) {
				LOGGER.error(e.getMessage());
			}
		}
		if (!StringUtils.isEmpty(template)) {
			comment.setContent(template);
			forumProvider.createPost(comment);
		}
	}

	private String getOrganizationAssignTopicForCommentTemplate(long orgId,OrganizationMember member,int namespaceId) {

		Map<String,Object> map = new HashMap<String, Object>();
		if(member != null){
			map.put("memberName", member.getContactName());
			map.put("memberContactToken", member.getContactToken());
		}
		User user = null;
		if(0 == member.getTargetId()){
			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, member.getContactToken());
			if(userIdentifier != null)
				user = userProvider.findUserById(userIdentifier.getOwnerUid());
		}else{
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
    public void deleteOrganization(DeleteOrganizationIdCommand cmd) {

        User user = UserContext.current().getUser();

        this.checkOrganizationIdIsNull(cmd.getId());
        Organization organization = this.checkOrganization(cmd.getId());

        //modify all subset States by sfyan 20160430
        List<String> groupTypeList = new ArrayList<String>();
        groupTypeList.add(OrganizationGroupType.GROUP.getCode());
        groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
        groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
        groupTypeList.add(OrganizationGroupType.JOB_POSITION.getCode());
        groupTypeList.add(OrganizationGroupType.JOB_LEVEL.getCode());

        List<Organization> organizations = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "/%", groupTypeList);
        organizations.add(organization);

        dbProvider.execute((TransactionStatus status) -> {
            for (Organization org : organizations) {
                org.setStatus(OrganizationStatus.DELETED.getCode());
                org.setOperatorUid(user.getId());
                organizationProvider.updateOrganization(org);

                List<OrganizationCommunity> orgCommunities = organizationProvider.listOrganizationCommunities(org.getId());

                for (OrganizationCommunity orgCommunity : orgCommunities) {
                    organizationProvider.deleteOrganizationCommunityById(orgCommunity.getId());
                }

                //把机构入驻的园区关系修改成无效
                deleteCurrentOrganizationCommunityReqeust(user.getId(), org.getId());
			}
            //把机构下的所有人员修改成无效
            List<OrganizationMember> members = organizationProvider.listOrganizationMemberByPath(organization.getPath(), null, "");
            for (OrganizationMember member: members) {
                member.setOperatorUid(user.getId());
                member.setStatus(OrganizationMemberStatus.INACTIVE.getCode());
                organizationProvider.updateOrganizationMember(member);
                //解除门禁权限
                doorAccessService.deleteAuthWhenLeaveFromOrg(UserContext.getCurrentNamespaceId(), member.getOrganizationId(), member.getTargetId());
            }
            //把user_organization表中的相应记录更新为失效
            inactiveUserOrganizationWithMembers(members);
            return null;
        });

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
        List<OrganizationMember> orgMembers = this.organizationProvider.listOrganizationMembers(userId);

        OrganizationGroupType tempGroupType = null;
        List<OrganizationDTO> dtos = new ArrayList<OrganizationDTO>();
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

            OrganizationDTO dto = toOrganizationDTO(userId, org);
            dtos.add(dto);
        }
        Long endTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("TrackUserRelatedCost:listUserRelateOrganizations:elapse:{}", endTime - startTime);
        }
        return dtos;
    }

    private OrganizationDTO toOrganizationDTO(Long userId, Organization organization) {
        OrganizationDTO organizationDto = ConvertHelper.convert(organization, OrganizationDTO.class);
        organizationDto.setOrganizationType(organization.getOrganizationType());

        OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, organization.getId());
        if (member != null && member.getStatus() != null)
            organizationDto.setMemberStatus(member.getStatus());
        else
            organizationDto.setMemberStatus(OrganizationMemberStatus.INACTIVE.getCode());

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
                organizationDto.setCommunityId(communityId);
                organizationDto.setCommunityName(community.getName());
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

        OrganizationMember orgMember = organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, organization.getId());
        if (orgMember != null) {
            organizationDto.setMemberStatus(orgMember.getStatus());
        }

        return organizationDto;
    }

    @Override
    public List<OrganizationSimpleDTO> listUserRelateOrgs(ListUserRelatedOrganizationsCommand cmd) {
        return listUserRelateOrgs(cmd, UserContext.current().getUser());
    }


    @Override
    public List<OrganizationSimpleDTO> listUserRelateOrganizations(ListUserRelatedOrganizationsCommand cmd){
        Long userId = UserContext.current().getUser().getId();
        return listUserRelateOrganizations(userId);
    }

    public List<OrganizationSimpleDTO> listUserRelateOrganizations(Long userId){
        List<OrganizationSimpleDTO> orgs = new ArrayList<>();

        List<RoleAssignment> roleAssignments = aclProvider.getRoleAssignmentByTarget(EntityType.USER.getCode(), userId);
        Set<Long> organizationIds = new HashSet<>();
        for (RoleAssignment roleAssignment: roleAssignments) {
            if(EntityType.ORGANIZATIONS == EntityType.fromCode(roleAssignment.getOwnerType()) && (roleAssignment.getRoleId() == RoleConstants.PM_SUPER_ADMIN || roleAssignment.getRoleId() == RoleConstants.ENTERPRISE_SUPER_ADMIN)){
                organizationIds.add(roleAssignment.getOwnerId());
            }
        }

        Set<Long> orgIds = new HashSet<>();
        List<Target> targets = new ArrayList<>();
        targets.add(new Target(EntityType.USER.getCode(), userId));

        List<Project> projects = authorizationProvider.getManageAuthorizationProjectsByAuthAndTargets(EntityType.SERVICE_MODULE.getCode(), null, targets);
        for (Project project: projects) {
            if(EntityType.fromCode(project.getProjectType()) == EntityType.ORGANIZATIONS){
                organizationIds.add(project.getProjectId());
            }
        }

        List<OrganizationMember> orgMembers = this.organizationProvider.listOrganizationMembers(userId);
        for (OrganizationMember member: orgMembers) {
            if(OrganizationMemberStatus.ACTIVE == OrganizationMemberStatus.fromCode(member.getStatus())){
                Organization org = this.organizationProvider.findOrganizationById(member.getOrganizationId());
                if(null != org && OrganizationStatus.ACTIVE == OrganizationStatus.fromCode(org.getStatus())){
                    addPathOrganizationId(org.getPath(), orgIds);
                }
            }

        }

        //把用户所有关联的部门放到targets里面查询
        for (Long orgId: orgIds) {
            targets.add(new Target(EntityType.ORGANIZATIONS.getCode(), orgId));
        }

        //获取人员和人员所有机构所赋予模块的所属项目范围
        List<String> scopes = authorizationProvider.getAuthorizationScopesByAuthAndTargets(EntityType.SERVICE_MODULE.getCode(), null, targets);
        for (String scope: scopes) {
            if(null != scope){
                String[] scopeStrs = scope.split("\\.");
                if(scopeStrs.length == 2){
                    if(EntityType.AUTHORIZATION_RELATION == EntityType.fromCode(scopeStrs[0])){
                        AuthorizationRelation authorizationRelation = authorizationProvider.findAuthorizationRelationById(Long.valueOf(scopeStrs[1]));
                        if(EntityType.fromCode(authorizationRelation.getOwnerType()) == EntityType.ORGANIZATIONS){
                            organizationIds.add(authorizationRelation.getOwnerId());
                        }
                    }
                }
            }
        }

        for (Long organizationId: organizationIds) {
            Organization org = organizationProvider.findOrganizationById(organizationId);
            if(null != org && OrganizationStatus.ACTIVE == OrganizationStatus.fromCode(org.getStatus()) && 0L == org.getParentId()){
                OrganizationSimpleDTO tempSimpleOrgDTO = ConvertHelper.convert(org, OrganizationSimpleDTO.class);
                //物业或业委增加小区Id和小区name信息
                if (org.getOrganizationType().equals(OrganizationType.GARC.getCode()) || org.getOrganizationType().equals(OrganizationType.PM.getCode())) {
                    this.addCommunityInfoToUserRelaltedOrgsByOrgId(tempSimpleOrgDTO);
                }
                orgs.add(tempSimpleOrgDTO);
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
                        if (org.getOrganizationType().equals(OrganizationType.GARC.getCode()) || org.getOrganizationType().equals(OrganizationType.PM.getCode())) {
                            this.addCommunityInfoToUserRelaltedOrgsByOrgId(tempSimpleOrgDTO);
                        }
                        orgs.add(tempSimpleOrgDTO);
                    }
                } else {

                    if (OrganizationGroupType.ENTERPRISE == OrganizationGroupType.fromCode(org.getGroupType())) {
                        OrganizationSimpleDTO tempSimpleOrgDTO = ConvertHelper.convert(org, OrganizationSimpleDTO.class);
                        //物业或业委增加小区Id和小区name信息
                        if (org.getOrganizationType().equals(OrganizationType.GARC.getCode()) || org.getOrganizationType().equals(OrganizationType.PM.getCode())) {
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
        OrganizationCommunity orgComm = this.organizationProvider.findOrganizationCommunityByOrgId(org.getId());
        if (orgComm != null) {
            Long communityId = orgComm.getCommunityId();
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
                    OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(user.getId(), org.getId());
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
        OrganizationMember operOrgMember = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, orgId);
        if (operOrgMember == null) {
            LOGGER.error("Operator not found.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Operator not found.");
        }
        return operOrgMember;
    }

    private OrganizationMember checkDesOrgMember(Long userId, Long orgId) {
        OrganizationMember desOrgMember = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, orgId);
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

        OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getCreatorUid(), organization.getId());
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
                OrganizationMember orgMember = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(user.getId(), org.getId());
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
        List<Building> buildings = this.communityProvider.ListBuildingsByCommunityId(locator, AppConstants.PAGINATION_MAX_SIZE + 1, community.getCommunityId(), namespaceId);
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
//	        LOGGER.info("User id is null, ignore to process partner organization user, userId=" + userId + ", partnerId=" + partnerId);
//	        return;
//	    }
//	    User user = userProvider.findUserById(userId);
//	    if(user == null) {
//            LOGGER.error("User not found, userId=" + userId + ", partnerId=" + partnerId);
//            return;
//        }
//
//	    if(partnerId == null || partnerId <= 0) {
//            LOGGER.info("Partner id is null, ignore to process partner organization user, userId=" + userId + ", partnerId=" + partnerId);
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
//            OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(user.getId(), organization.getId());
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


    @Override
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
    }

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

//		OrganizationMember organizationMember = organizationProvider.findOrganizationMemberByOrgIdAndUId(user.getId(), organizationId);
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
        OrganizationMember organizationmember = organizationProvider.findOrganizationMemberByOrgIdAndUId(cmd.getTargetId(), organization.getId());
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

            return organizationDTO;
        }

        //未查询到匹配的记录
        organizationmember = this.dbProvider.execute((TransactionStatus status) -> {
            UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(cmd.getTargetId(),
                    IdentifierType.MOBILE.getCode());

            OrganizationMember member = new OrganizationMember();
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

            member.setCreatorUid(user.getId());
            member.setNickName(user.getNickName());
            member.setAvatar(user.getAvatar());
            member.setApplyDescription(cmd.getContactDescription());

            /**创建企业级的member/detail/user_organiztion记录**/
            OrganizationMember tempMember = createOrganiztionMemberWithDetailAndUserOrganization(member, cmd.getOrganizationId());
            member.setId(tempMember.getId());

            return member;
        });

        if (organizationmember == null) {
            LOGGER.error("Failed to apply for organization member, userId="
                    + cmd.getTargetId() + ", cmd=" + cmd);
            return null;
        } else {

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

            return organizationDTO;
        }
    }


    /**
     * 批准用户加入企业
     */
    @Override
    public void approveForEnterpriseContact(ApproveContactCommand cmd) {

        User operator = UserContext.current().getUser();
        Long operatorUid = operator.getId();
        // 如果有人先把申请拒绝了，那就找不到此人了，此时也让它成功以便客户端不报错 by lqs 20160415
        // OrganizationMember member = checkEnterpriseContactParameter(cmd.getEnterpriseId(), cmd.getUserId(), operatorUid, "approveForEnterpriseContact");
        OrganizationMember member = null;
        if (cmd.getEnterpriseId() != null && cmd.getUserId() != null) {
            member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(cmd.getUserId(), cmd.getEnterpriseId());
        } else {
            LOGGER.error("Invalid enterprise id or target user id, operatorUid=" + operatorUid + ", cmd=" + cmd);
        }

        if (member != null) {
            if (OrganizationMemberStatus.fromCode(member.getStatus()) != OrganizationMemberStatus.WAITING_FOR_APPROVAL) {
                //不抛异常会导致客户端小黑条消不掉 by sfyan 20170120
                LOGGER.debug("organization member status error, status={}, cmd={}", member.getStatus(), cmd);
//				throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_MEMBER_STSUTS_MODIFIED,
//						"organization member status error.");
            } else {
                member.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
                updateEnterpriseContactStatus(operator.getId(), member);
                DaoHelper.publishDaoAction(DaoAction.CREATE, OrganizationMember.class, member.getId());
                sendMessageForContactApproved(member);
                //记录添加log
                OrganizationMemberLog orgLog = ConvertHelper.convert(cmd, OrganizationMemberLog.class);
                orgLog.setOrganizationId(member.getOrganizationId());
                orgLog.setContactName(member.getContactName());
                orgLog.setContactToken(member.getContactToken());
                orgLog.setUserId(member.getTargetId());
                orgLog.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                orgLog.setOperationType(OperationType.JOIN.getCode());
                orgLog.setRequestType(RequestType.USER.getCode());
                orgLog.setOperatorUid(UserContext.current().getUser().getId());
                this.organizationProvider.createOrganizationMemberLog(orgLog);

                this.doorAccessService.joinCompanyAutoAuth(UserContext.getCurrentNamespaceId(), cmd.getEnterpriseId(), cmd.getUserId());
            }

        } else {
            LOGGER.warn("Enterprise contact not found, maybe it has been rejected, operatorUid=" + operatorUid + ", cmd=" + cmd);
        }
    }

    /**
     * 拒绝申请
     */
    @Override
    public void rejectForEnterpriseContact(RejectContactCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorUid = operator.getId();
        OrganizationMember member = checkEnterpriseContactParameter(cmd.getEnterpriseId(), cmd.getUserId(), operatorUid, "rejectForEnterpriseContact");
        if (OrganizationMemberStatus.fromCode(member.getStatus()) != OrganizationMemberStatus.WAITING_FOR_APPROVAL) {
            //不抛异常会导致客户端小黑条消不掉 by sfyan 20170120
            LOGGER.debug("organization member status error, status={}, cmd={}", member.getStatus(), cmd);
//			throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_MEMBER_STSUTS_MODIFIED,
//					"organization member status error.");
        } else {
            deleteEnterpriseContactStatus(operatorUid, member);
            sendMessageForContactReject(member);
        }

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
	public List<OrganizationMember> listOrganizationMemberByOrganizationPathAndUserId(String path, Long userId){
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode());
		if(null == userIdentifier)
			return null;
		return listOrganizationMemberByOrganizationPathAndContactToken(path, userIdentifier.getIdentifierToken());
	}

    @Override
    public String checkIfLastOnNode(DeleteOrganizationPersonnelByContactTokenCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        //先判断是否是子公司
        Organization org = this.organizationProvider.findOrganizationById(cmd.getOrganizationId());
        if(org != null){
            if(org.getParentId() == 0L){//为总公司
                if(organizationProvider.checkIfLastOnNode(namespaceId, cmd.getOrganizationId(), cmd.getContactToken(), org.getPath())){//如果是总公司下最后一个onNode节点
                    return DeleteOrganizationContactScopeType.ALL_NOTE.getCode();
                }else {//不是最后一个节点
                    return "0";
                }
            }else{//为分公司
                if(organizationProvider.checkIfLastOnNode(namespaceId, cmd.getOrganizationId(), cmd.getContactToken(), org.getPath())) {//如果是分公司下最后一个onNode节点
                    if(organizationProvider.checkIfLastOnNode(namespaceId, cmd.getOrganizationId(), cmd.getContactToken(), "/" + org.getParentId())) {//继续查询是否是总公司下最后一个onNode节点
                        return DeleteOrganizationContactScopeType.ALL_NOTE.getCode();
                    }else{//是分公司最后一个节点而不是总公司最后一个节
                        return DeleteOrganizationContactScopeType.CHILD_ENTERPRISE.getCode();
                    }
                }else{//不是总公司最后一个节点
                    return "0";
                }
            }
        }
        return "0";
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
            //所在的机构直属于当前公司或者就是当前公司的成员 需要删除
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

        //发消息等等操作
        leaveOrganizationAfterOperation(user.getId(), members);

    }

    /**
     * 退出公司后需要记录日志、解除门禁、设置默认小区、更新搜索引擎数据、以及发消息
     *
     * @param members
     */
    private void leaveOrganizationAfterOperation(Long operatorUid, List<OrganizationMember> members) {
        for (OrganizationMember m : members) {
            if (OrganizationGroupType.fromCode(m.getGroupType()) == OrganizationGroupType.ENTERPRISE) {

                //记录删除log
                OrganizationMemberLog orgLog = new OrganizationMemberLog();
                orgLog.setOrganizationId(m.getOrganizationId());
                orgLog.setContactName(m.getContactName());
                orgLog.setContactToken(m.getContactToken());
                orgLog.setUserId(m.getTargetId());
                orgLog.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                orgLog.setOperationType(OperationType.QUIT.getCode());
                orgLog.setRequestType(RequestType.ADMIN.getCode());
                orgLog.setOperatorUid(UserContext.current().getUser().getId());
                this.organizationProvider.createOrganizationMemberLog(orgLog);

                Integer namespaceId = UserContext.getCurrentNamespaceId();
                if (OrganizationMemberTargetType.fromCode(m.getTargetType()) == OrganizationMemberTargetType.USER) {
                    //Remove door auth, by Janon 2016-12-15
                    doorAccessService.deleteAuthWhenLeaveFromOrg(UserContext.getCurrentNamespaceId(), m.getOrganizationId(), m.getTargetId());

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
     * 退出企业
     */
    @Override
    public void leaveForEnterpriseContact(LeaveEnterpriseCommand cmd) {
        User user = UserContext.current().getUser();
        long userId = user.getId();
        String tag = "leaveEnterpriseContact";

        Long enterpriseId = cmd.getEnterpriseId();

        //退出公司 add by sfyan20170428
        leaveOrganizaitonByUserId(userId, enterpriseId, userId);
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

    @Override
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

        List<OrganizationMember> organizationMembers = organizationProvider.listOrganizationMembers(locator, pageSize, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {

                query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
                List<String> groupTypes = new ArrayList<>();
                if (null != cmd.getFilterScopeTypes() && cmd.getFilterScopeTypes().size() > 0) {
                    Condition cond = null;
                    if (cmd.getFilterScopeTypes().contains(FilterOrganizationContactScopeType.CURRENT.getCode())) {
                        /**当前节点是企业**/
                        if (org.getGroupType().equals(OrganizationGroupType.ENTERPRISE.getCode())) {
                            //寻找隶属企业的直属隐藏部门
                            Organization underDirectOrg = organizationProvider.findUnderOrganizationByParentOrgId(org.getId());
                            if(underDirectOrg == null){//没有添加过直属人员
                                cond = (Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.lt(0L));//确保查询不到
                            }else{
                                cond = Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(underDirectOrg.getId());
                                cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.eq(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode()));
                            }

                        }else{/**当前节点不是企业**/
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
					}else if(groupTypes.size() > 0){
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
    }

    @Override
    public List<OrganizationMemberDTO> convertOrganizationMemberDTO(List<OrganizationMember> organizationMembers, Organization org) {
        return this.convertDTO(organizationMembers, org);
    }

    @Override
    public ListOrganizationMemberCommandResponse listOrganizationPersonnelsByRoleIds(ListOrganizationPersonnelByRoleIdsCommand cmd) {
        ListOrganizationContactCommand command = new ListOrganizationContactCommand();
        command.setOrganizationId(cmd.getOrganizationId());
        command.setPageSize(100000);
        command.setKeywords(cmd.getKeywords());
        ListOrganizationMemberCommandResponse response = this.listOrganizationPersonnels(command, false);

        List<OrganizationMemberDTO> roleMembers = new ArrayList<OrganizationMemberDTO>();

        List<OrganizationMemberDTO> members = response.getMembers();

        if (null != members && 0 != members.size()) {
            for (OrganizationMemberDTO organizationMemberDTO : members) {
                List<RoleDTO> RoleDTOs = organizationMemberDTO.getRoles();
                if (null == RoleDTOs) {
                    continue;
                }
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

        List<OrganizationMember> organizationMembers = this.organizationProvider.listOrganizationPersonnels(cmd.getKeywords(), orgCommoand, cmd.getIsSignedup(), null, locator, pageSize);

        if (0 == organizationMembers.size()) {
            return response;
        }

        response.setNextPageAnchor(locator.getAnchor());

        response.setMembers(organizationMembers.stream().map((c) -> {
            return ConvertHelper.convert(c, OrganizationMemberDTO.class);
        }).collect(Collectors.toList()));

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

        OrganizationMember member = organizationProvider.findOrganizationPersonnelByPhone(cmd.getEnterpriseId(), cmd.getPhone());

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

        Integer namespaceId = UserContext.getCurrentNamespaceId();

        OrganizationMember organizationMember = ConvertHelper.convert(cmd, OrganizationMember.class);
        organizationMember.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
        organizationMember.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
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

        if (null == cmd.getAccountPhone()) {
            LOGGER.error("contactToken can not be empty.");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER, "contactToken can not be empty.");
        }

        if (null == cmd.getAccountName()) {
            LOGGER.error("contactName can not be empty.");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER, "contactName can not be empty.");
        }
        if (exNamespaceId == null) {
            exNamespaceId = UserContext.getCurrentNamespaceId(null);
        }
        Integer namespaceId = exNamespaceId;

		Organization org = checkOrganization(cmd.getOrganizationId());

        OrganizationMember member = organizationProvider.findOrganizationPersonnelByPhone(cmd.getOrganizationId(), cmd.getAccountPhone());

        return this.dbProvider.execute((TransactionStatus status) -> {
            OrganizationMember m = member;

            if (null == m) {
                CreateOrganizationMemberCommand memberCmd = new CreateOrganizationMemberCommand();
                memberCmd.setContactName(cmd.getAccountName());
                memberCmd.setContactToken(cmd.getAccountPhone());
                memberCmd.setOrganizationId(cmd.getOrganizationId());
                memberCmd.setGender(UserGender.UNDISCLOSURED.getCode());
                m = this.createOrganizationPersonnel(memberCmd);
            } else {
				List<OrganizationMember> members = listOrganizationMemberByOrganizationPathAndContactToken(org.getPath(), cmd.getAccountPhone());
				for (OrganizationMember organizationMember: members) {
					organizationMember.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
					organizationMember.setContactName(cmd.getAccountName());
					organizationProvider.updateOrganizationMember(organizationMember);
                }
            }

            //创建用户
            UserIdentifier userIdentifier = null;
            userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, m.getContactToken());

            if (null == userIdentifier) {
                User newuser = new User();
                newuser.setStatus(UserStatus.ACTIVE.getCode());
                newuser.setNamespaceId(namespaceId);
                newuser.setNickName(cmd.getAccountName());
                newuser.setGender(UserGender.UNDISCLOSURED.getCode());
                String salt = EncryptionUtils.createRandomSalt();
                newuser.setSalt(salt);
                try {
                    newuser.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", salt)));
                } catch (Exception e) {
                    LOGGER.error("encode password failed");
                    throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PASSWORD, "Unable to create password hash");

                }

                userProvider.createUser(newuser);

                userIdentifier = new UserIdentifier();
                userIdentifier.setOwnerUid(newuser.getId());
                userIdentifier.setIdentifierType(IdentifierType.MOBILE.getCode());
                userIdentifier.setIdentifierToken(cmd.getAccountPhone());
                userIdentifier.setNamespaceId(namespaceId);

                userIdentifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
                userProvider.createIdentifier(userIdentifier);
            }

            //循环更新
            List<OrganizationMember> members = this.organizationProvider.listOrganizationMembersByPhoneAndNamespaceId(m.getContactToken(), namespaceId);
            for(OrganizationMember _m :members){
                if (_m.getTargetType().equals(OrganizationMemberTargetType.UNTRACK.getCode())) {
                    _m.setContactName(cmd.getAccountName());
                    _m.setTargetType(OrganizationMemberTargetType.USER.getCode());
                    _m.setTargetId(userIdentifier.getOwnerUid());
                    _m.setNamespaceId(namespaceId);
                    organizationProvider.updateOrganizationMember(_m);

                    //创建管理员的同时会同时创建一个用户，因此需要在user_organization中添加一条记录 modify at 17/6/20
                    //仅当target为user且grouptype为企业时添加
                    if(_m.getTargetType().equals(OrganizationMemberTargetType.USER.getCode()) && _m.getGroupType().equals(OrganizationType.ENTERPRISE.getCode())){
                        createOrUpdateUserOrganization(_m);
                    }
                }
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
            return m;
        });
    }


    @Override
    public OrganizationMemberDTO processUserForMemberWithoutMessage (UserIdentifier identifier){
        return processUserForMember(identifier, false);
    }

    @Override
    public OrganizationMemberDTO processUserForMember (UserIdentifier identifier){
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

                    this.updateMemberUser(member);
                    DaoHelper.publishDaoAction(DaoAction.CREATE, OrganizationMember.class, member.getId());

                    // 机构是公司的情况下 才发送短信
                    if (OrganizationGroupType.fromCode(org.getGroupType()) == OrganizationGroupType.ENTERPRISE) {
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
            LOGGER.error("Failed to process the enterprise contact for the user, userId=" + identifier.getOwnerUid(), e);
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
		Long communityId = cmd.getCommunityId();
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
			task.setType(ImportFileTaskType.ENGERPRISE.getCode());
			task.setCreatorUid(userId);
			task = importFileService.executeTask(() -> {
					ImportFileResponse response = new ImportFileResponse();
					List<ImportEnterpriseDataDTO> datas = handleImportEnterpriseData(resultList);
					if(datas.size() > 0){
						//设置导出报错的结果excel的标题
						response.setTitle(datas.get(0));
						datas.remove(0);
					}
					List<ImportFileResultLog<ImportEnterpriseDataDTO>> results = importEnterprise(datas, userId, cmd);
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
	public ImportFileResponse<ImportEnterpriseDataDTO> importEnterpriseData(MultipartFile mfile,
												   Long userId, ImportEnterpriseDataCommand cmd) {
		ImportFileResponse<ImportEnterpriseDataDTO> importDataResponse = new ImportFileResponse<>();
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
			List<ImportFileResultLog<ImportEnterpriseDataDTO>> errorDataLogs = importEnterprise(handleImportEnterpriseData(resultList), userId, cmd);
			LOGGER.debug("End import data...,fail:" + errorDataLogs.size());
			if(null == errorDataLogs || errorDataLogs.isEmpty()){
				LOGGER.debug("Data import all success...");
			}

			importDataResponse.setTotalCount((long)resultList.size()-1);
			importDataResponse.setFailCount((long)errorDataLogs.size());
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

			if(null == resultList || resultList.isEmpty()){
				LOGGER.error("File content is empty。userId="+userId);
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
					if(datas.size() > 0){
						//设置导出报错的结果excel的标题
						response.setTitle(datas.get(0));
						datas.remove(0);
					}
					List<ImportFileResultLog<ImportOrganizationContactDataDTO>> results = importOrganizationPersonnel(datas, userId, cmd);
					response.setTotalCount((long)datas.size());
					response.setFailCount((long)results.size());
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

	private List<ImportEnterpriseDataDTO> handleImportEnterpriseData(List list){
        List<ImportEnterpriseDataDTO> datas = new ArrayList<>();
		for(int i = 1; i < list.size(); i++) {
			RowResult r = (RowResult)list.get(i);
			if (org.apache.commons.lang.StringUtils.isNotBlank(r.getA()) || org.apache.commons.lang.StringUtils.isNotBlank(r.getB()) || 
					org.apache.commons.lang.StringUtils.isNotBlank(r.getC()) || org.apache.commons.lang.StringUtils.isNotBlank(r.getD()) || 
					org.apache.commons.lang.StringUtils.isNotBlank(r.getE()) || org.apache.commons.lang.StringUtils.isNotBlank(r.getF()) || 
					org.apache.commons.lang.StringUtils.isNotBlank(r.getG()) || org.apache.commons.lang.StringUtils.isNotBlank(r.getH()) || 
					org.apache.commons.lang.StringUtils.isNotBlank(r.getI()) || org.apache.commons.lang.StringUtils.isNotBlank(r.getJ()) || 
					org.apache.commons.lang.StringUtils.isNotBlank(r.getK())) {
			ImportEnterpriseDataDTO data = new ImportEnterpriseDataDTO();
			if(null != r.getA())
				data.setName(r.getA().trim());
			if(null != r.getB())
				data.setDisplayName(r.getB().trim());
			if(null != r.getC())
				data.setAdminName(r.getC().trim());
			if(null != r.getD())
				data.setAdminToken(r.getD().trim());
			if(null != r.getE())
				data.setEmail(r.getE().trim());
			if(null != r.getF())
				data.setBuildingName(r.getF().trim());
			if(null != r.getG())
				data.setAddress(r.getG().trim());
			if(null != r.getH())
				data.setContact(r.getH().trim());
			if(null != r.getI())
				data.setNumber(r.getI().trim());
			if(null != r.getJ())
				data.setCheckinDate(r.getJ().trim());
			if(null != r.getK())
				data.setDescription(r.getK().trim());
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

        List<ImportFileResultLog<ImportEnterpriseDataDTO>> errorDataLogs = new ArrayList<>();

        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());

        Community community = communityProvider.findCommunityById(cmd.getCommunityId());

		// 业务太复杂，导入企业时如果本身系统里面已存在，要覆盖掉，如果是本次导入了同一企业多行，要合并门牌及管理员
		Map<Long, List<Long>> orgAddressIds = new HashMap<>();
		Map<Long, List<String>> orgAdminAccounts = new HashMap<>();
		
        for (ImportEnterpriseDataDTO data : list) {
            CreateEnterpriseCommand enterpriseCommand = new CreateEnterpriseCommand();
            ImportFileResultLog<ImportEnterpriseDataDTO> log = new ImportFileResultLog<>(OrganizationServiceErrorCode.SCOPE);
            if (StringUtils.isEmpty(data.getName())) {
                LOGGER.error("enterprise name is null, data = {}", data);
                log.setData(data);
                log.setErrorLog("enterprise name is null");
				log.setCode(OrganizationServiceErrorCode.ERROR_ENTERPRISE_NAME_EMPTY);
                errorDataLogs.add(log);
                continue;
            }

            if (StringUtils.isEmpty(data.getBuildingName())) {
                LOGGER.error("building name is null, data = {}", data);
                log.setData(data);
                log.setErrorLog("building name is null");
				log.setCode(OrganizationServiceErrorCode.ERROR_BUILDING_NAME_EMPTY);
                errorDataLogs.add(log);
                continue;
            }

            if (StringUtils.isEmpty(data.getAddress())) {
                LOGGER.error("address name is null, data = {}", data);
                log.setData(data);
                log.setErrorLog("address name is null");
				log.setCode(OrganizationServiceErrorCode.ERROR_APARTMENT_NAME_EMPTY);
                errorDataLogs.add(log);
                continue;
            }
            enterpriseCommand.setName(data.getName());
            enterpriseCommand.setDisplayName(data.getDisplayName());
            enterpriseCommand.setContactsPhone(data.getContact());
            enterpriseCommand.setDescription(data.getDescription());
            enterpriseCommand.setContactor(data.getAdminName());
            enterpriseCommand.setNamespaceId(namespaceId);
            enterpriseCommand.setCommunityId(cmd.getCommunityId());
            enterpriseCommand.setEmailDomain(data.getEmail());
            enterpriseCommand.setCheckinDate(data.getCheckinDate());
            if (!StringUtils.isEmpty(data.getNumber())) {
                enterpriseCommand.setMemberCount(Long.parseLong(data.getNumber().toString()));
            }

            Building building = communityProvider.findBuildingByCommunityIdAndName(community.getId(), data.getBuildingName());

            if (null == building) {
                LOGGER.error("building Non-existent, buildingName = {}", data.getBuildingName());
                log.setData(data);
                log.setErrorLog("building Non-existent");
				log.setCode(OrganizationServiceErrorCode.ERROR_BUILDING_NOT_EXIST);
                errorDataLogs.add(log);
                continue;
            }

            Address address = addressProvider.findAddressByBuildingApartmentName(namespaceId, community.getId(), data.getBuildingName(), data.getAddress());

            if (null == address) {
                LOGGER.error("address Non-existent, address = {}", data.getAddress());
                log.setData(data);
                log.setErrorLog("address Non-existent");
				log.setCode(OrganizationServiceErrorCode.ERROR_APARTMENT_NOT_EXIST);
                errorDataLogs.add(log);
                continue;
            }

            OrganizationAddress orgAddress = organizationProvider.findOrganizationAddressByAddressId(address.getId());
			Organization org = organizationProvider.findOrganizationByName(data.getName(), OrganizationGroupType.ENTERPRISE.getCode(), 0L, namespaceId);

			if(null != orgAddress && (org == null || org.getId().longValue() != orgAddress.getOrganizationId().longValue())){
                LOGGER.error("address has been checked in, address = {}", data.getAddress());
                log.setData(data);
                log.setErrorLog("address has been checked in");
				log.setCode(OrganizationServiceErrorCode.ERROR_APARTMENT_CHECKED_IN);
                errorDataLogs.add(log);
                continue;
            }


            if (null == org) {
                OrganizationDTO dto = this.createEnterprise(enterpriseCommand);
                org = ConvertHelper.convert(dto, Organization.class);
			}else {
				UpdateEnterpriseCommand updateEnterpriseCommand = ConvertHelper.convert(enterpriseCommand, UpdateEnterpriseCommand.class);
				updateEnterpriseCommand.setId(org.getId());
				updateEnterprise(updateEnterpriseCommand, false);
            }

            //添加门牌入住
			if (orgAddressIds.get(org.getId()) == null) {
				organizationProvider.deleteOrganizationAddressByOrganizationId(org.getId());
				orgAddressIds.put(org.getId(), new ArrayList<>());
			}
			if (!orgAddressIds.get(org.getId()).contains(address.getId())) {
			orgAddress = new OrganizationAddress();
			orgAddress.setBuildingName(building.getName());
			orgAddress.setBuildingId(building.getId());
			orgAddress.setAddressId(address.getId());
			orgAddress.setStatus(OrganizationAddressStatus.ACTIVE.getCode());
			orgAddress.setOrganizationId(org.getId());
			orgAddress.setCreatorUid(user.getId());
			orgAddress.setOperatorUid(user.getId());
			organizationProvider.createOrganizationAddress(orgAddress);
				orgAddressIds.get(org.getId()).add(address.getId());
			}

            //添加管理员
			if (orgAdminAccounts.get(org.getId()) == null) {
				deleteOrganizationAllAdmins(org.getId());
				orgAdminAccounts.put(org.getId(), new ArrayList<>());
			}
			if (!orgAdminAccounts.get(org.getId()).contains(data.getAdminToken())) {
			CreateOrganizationAccountCommand accountCommand = new CreateOrganizationAccountCommand();
			accountCommand.setOrganizationId(org.getId());
			accountCommand.setAccountPhone(data.getAdminToken());
			accountCommand.setAccountName(data.getAdminName());
			if(!StringUtils.isEmpty(accountCommand.getAccountPhone())){
				this.createOrganizationAccount(accountCommand, RoleConstants.ENTERPRISE_SUPER_ADMIN);
				}
				orgAdminAccounts.get(org.getId()).add(data.getAdminToken());
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
		DeleteOrganizationAdminCommand deleteCmd = ConvertHelper.convert(listCmd, DeleteOrganizationAdminCommand.class);
		for (OrganizationContactDTO organizationContactDTO : list) {
			deleteCmd.setUserId(organizationContactDTO.getTargetId());
			rolePrivilegeService.deleteOrganizationAdministrators(deleteCmd);
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

    public static void main(String[] args) {
        String s = "/1000750/1003880/1003883/1003903";
        String[] pathArr = s.split("/");
        System.out.println();
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

            if(null != member.getDetailId()){
                OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(member.getDetailId());
                detail.setTargetId(member.getTargetId());
                detail.setTargetType(member.getTargetType());
                organizationProvider.updateOrganizationMemberDetails(detail,detail.getId());

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
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_GROUP.getCode()).enter(() -> {
            //modify by wh  2016-10-12 拒绝后置为拒绝状态而非删除
            member.setStatus(OrganizationMemberStatus.REJECT.getCode());
            this.organizationProvider.updateOrganizationMember(member);
            //this.organizationProvider.deleteOrganizationMemberById(member.getId());
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
            } else if (OrganizationGroupType.fromCode(org.getGroupType()) == OrganizationGroupType.GROUP) {
                List<OrganizationDTO> groups = new ArrayList<>();
                groups.add(orgDTO);
                groups.addAll(this.getOrganizationMemberGroups(OrganizationGroupType.GROUP, dto.getContactToken(), directlyEnterprise.getPath()));
                dto.setGroups(groups);
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
    public List<Long> getIncludeOrganizationIdsByUserId(Long userId, Long organizationId){
        List<Long> orgnaizationIds = new ArrayList<>();

        List<String> groupTypes = new ArrayList<>();
        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
        groupTypes.add(OrganizationGroupType.GROUP.getCode());
        groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
        List<OrganizationDTO> orgs = getOrganizationMemberGroups(groupTypes, userId, organizationId);
        for (OrganizationDTO dto: orgs) {
            addPathOrganizationId(dto.getPath(), orgnaizationIds);
        }
        return orgnaizationIds;
    }

    private void addPathOrganizationId(String path, List<Long> orgnaizationIds){
        String[] idStrs = path.split("/");
        for (String idStr: idStrs) {
            if(!StringUtils.isEmpty(idStr)){
                Long id = Long.valueOf(idStr);
                orgnaizationIds.add(id);
            }
        }
    }

    private void addPathOrganizationId(String path, Set<Long> orgnaizationIds){
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

    @Override
    public List<Object> getOrganizationMemberIdAndVisibleFlag(String contactToken, Long organizationId){
        OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndToken(contactToken,organizationId);
        List<Object> result = new ArrayList<>();
        result.add(member.getId());
        Byte visibleFlag;
        if (null == VisibleFlag.fromCode(member.getVisibleFlag())) {
            visibleFlag = VisibleFlag.SHOW.getCode();
        } else {
            visibleFlag = member.getVisibleFlag();
        }
        result.add(visibleFlag);
        return result;
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
    public OrganizationMenuResponse listAllChildrenOrganizationMenus(Long id,
                                                                     List<String> groupTypes, Byte naviFlag) {
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
    public ListOrganizationsCommandResponse listAllChildrenOrganizations(Long id,
                                                                         List<String> groupTypes) {
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
        return this.getOrganizationManagers(cmd.getOrganizationId());
    }

    @Override
    public List<OrganizationManagerDTO> listOrganizationAllManagers(ListOrganizationManagersCommand cmd) {
        checkOrganization(cmd.getOrganizationId());
        List<String> types = new ArrayList<>();
        types.add(OrganizationGroupType.GROUP.getCode());
        types.add(OrganizationGroupType.DEPARTMENT.getCode());
        List<Organization> organizations = organizationProvider.listOrganizationByGroupTypes(cmd.getOrganizationId(), types);
        List<Long> organizationIds = new ArrayList<>();
        for (Organization organization : organizations) {
            organizationIds.add(organization.getId());
        }
        return this.getOrganizationManagers(organizationIds);
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
        List<OrganizationManagerDTO> dtos = new ArrayList<>();
        //机构经理
        List<String> types = new ArrayList<>();
        types.add(OrganizationGroupType.MANAGER.getCode());
        List<Long> managerGroupIds = new ArrayList<>();
        for (Long organizationId : organizationIds) {
            List<Organization> managerGroups = organizationProvider.listOrganizationByGroupTypes(organizationId, types);
            if (0 < managerGroups.size()) {
                managerGroupIds.add(managerGroups.get(0).getId());
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

    private String getNotifyText(Organization org, OrganizationMember member, User user, int code) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("enterpriseName", org.getName());
        map.put("userName", null == member.getContactName() ? member.getContactToken().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2") : member.getContactName());
        if (member.getApplyDescription() != null && member.getApplyDescription().length() > 0) {
            map.put("description", String.format("(%s)", member.getApplyDescription()));
        } else {
            map.put("description", "");
        }

        String scope = EnterpriseNotifyTemplateCode.SCOPE;

        user = UserContext.current().getUser();

        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, user.getLocale(), map, "");

        return notifyTextForApplicant;
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
        String notifyTextForOperator = this.getNotifyText(org, member, user, EnterpriseNotifyTemplateCode.ENTERPRISE_CONTACT_REQUEST_TO_JOIN_FOR_OPERATOR);

        includeList = getOrganizationAdminIncludeList(member.getOrganizationId(), user.getId(), user.getId());
        if (includeList.size() > 0) {

            QuestionMetaObject metaObject = createGroupQuestionMetaObject(org, member, null);
            metaObject.setRequestInfo(notifyTextForOperator);

            QuestionMetaActionData actionData = new QuestionMetaActionData();
            actionData.setMetaObject(metaObject);

            String routerUri = RouterBuilder.build(Router.ENTERPRISE_MEMBER_APPLY, actionData);
            sendRouterEnterpriseNotificationUseSystemUser(includeList, null, notifyTextForOperator, routerUri);

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
        String notifyTextForApplicant = this.getNotifyText(org, member, user, EnterpriseNotifyTemplateCode.ENTERPRISE_USER_SUCCESS_MYSELF);

        QuestionMetaObject metaObject = createGroupQuestionMetaObject(org, member, null);

        // send notification to who is requesting to join the enterprise
        List<Long> includeList = new ArrayList<>();

        includeList.add(member.getTargetId());
        sendEnterpriseNotificationUseSystemUser(includeList, null, notifyTextForApplicant);

        //同意加入公司通知客户端  by sfyan 20160526
		sendEnterpriseNotification(includeList, null, notifyTextForApplicant, MetaObjectType.ENTERPRISE_AGREE_TO_JOIN, metaObject);

        // send notification to all the other members in the group
        notifyTextForApplicant = this.getNotifyText(org, member, user, EnterpriseNotifyTemplateCode.ENTERPRISE_USER_SUCCESS_OTHER);
        // 消息只发给公司的管理人员  by sfyan 20170213
        includeList = this.includeOrgList(org, member.getTargetId());
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

    private void sendMessageForContactReject(OrganizationMember member) {
        // send notification to who is requesting to join the enterprise
        Organization org = this.organizationProvider.findOrganizationById(member.getOrganizationId());
        User user = userProvider.findUserById(member.getTargetId());

        // send notification to who is requesting to join the enterprise
        String notifyTextForApplicant = this.getNotifyText(org, member, user, EnterpriseNotifyTemplateCode.ENTERPRISE_USER_REJECT_JOIN);

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
        String notifyTextForApplicant = this.getNotifyText(org, member, user, EnterpriseNotifyTemplateCode.ENTERPRISE_CONTACT_LEAVE_FOR_APPLICANT);
        List<Long> includeList = new ArrayList<Long>();
        //给申请人发的信息应为私信by xiongying 20160524
        sendMessageToUser(member.getTargetId(), notifyTextForApplicant, null);
//       includeList.add(member.getTargetId());
//       sendEnterpriseNotification(org.getId(), includeList, null, notifyTextForApplicant, null, null);

        // send notification to all the other members in the enterprise
        notifyTextForApplicant = this.getNotifyText(org, member, user, EnterpriseNotifyTemplateCode.ENTERPRISE_CONTACT_LEAVE_FOR_OTHER);

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
            List<Long> includeList, List<Long> excludeList, String message, String routerUri) {
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
            RouterMetaObject mo = new RouterMetaObject();
            mo.setUrl(routerUri);
            messageDto.getMeta().put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
            messageDto.getMeta().put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(mo));

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

        OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(targetId, enterpriseId);
        if (member == null) {
            LOGGER.error("Enterprise contact not found, operatorUid=" + operatorUid
                    + ", enterpriseId=" + enterpriseId + ", targetId=" + targetId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ENTERPRISE_CONTACT_NOT_FOUND,
                    "Unable to find the enterprise contact");
        }

        return member;
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
        }

        if (target != null) {
            metaObject.setTargetType(EntityType.USER.getCode());
            metaObject.setTargetId(target.getTargetId());
            metaObject.setRequestId(target.getId());
        }

        return metaObject;
    }

    private Organization checkOrganization(Long orgId) {
        Organization org = organizationProvider.findOrganizationById(orgId);
        if (org == null) {
            LOGGER.error("Unable to find the organization.organizationId=" + orgId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Unable to find the organization.");
        }
        return org;
    }

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
            OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getTargetId(), cmd.getOrganizationId());
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
        OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getTargetId(), cmd.getOrganizationId());
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

            OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getTargetId(), cmd.getOrganizationId());
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

    private List<OrganizationMember> convertPinyin(List<OrganizationMember> organizationMembers) {

        organizationMembers = organizationMembers.stream().map((c) -> {
            String pinyin = PinYinHelper.getPinYin(c.getContactName());
            c.setFullInitial(PinYinHelper.getFullCapitalInitial(pinyin));
            c.setFullPinyin(pinyin.replaceAll(" ", ""));
            c.setInitial(PinYinHelper.getCapitalInitial(c.getFullPinyin()));
            return c;
        }).collect(Collectors.toList());

        Collections.sort(organizationMembers);

        return organizationMembers;
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
        Long userId = UserContext.current().getUser().getId();
        SceneTokenDTO sceneToken = userService.checkSceneToken(userId, cmd.getSceneToken());
        if (UserCurrentEntityType.fromCode(sceneToken.getEntityType()) != UserCurrentEntityType.ORGANIZATION) {
            return checkOfficalPrivilege(-1L);
        }

        return checkOfficalPrivilege(sceneToken.getEntityId());
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
        ListOrganizationMemberCommandResponse response = this.listOrganizationPersonnels(command, false);
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
        orgMember.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
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

        User user = new User();
        String password = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";
        user.setStatus(UserStatus.ACTIVE.getCode());
        user.setNamespaceId(namespaceId);
        user.setNickName(nickName);
        user.setGender(UserGender.UNDISCLOSURED.getCode());
        String salt = EncryptionUtils.createRandomSalt();
        user.setSalt(salt);
        try {
            user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", password, salt)));
        } catch (Exception e) {
            LOGGER.error("encode password failed");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PASSWORD, "Unable to create password hash");

        }
        userProvider.createUser(user);

        UserIdentifier newIdentifier = new UserIdentifier();
        newIdentifier.setOwnerUid(user.getId());
        newIdentifier.setIdentifierType(IdentifierType.MOBILE.getCode());
        newIdentifier.setIdentifierToken(identifierToken);
        newIdentifier.setNamespaceId(namespaceId);

        newIdentifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
        userProvider.createIdentifier(newIdentifier);

        //刷新地址信息
        propertyMgrService.processUserForOwner(newIdentifier);
    }

    @Override
    public void deleteOrganizationPersonnelByContactToken(
            DeleteOrganizationPersonnelByContactTokenCommand cmd) {

        Organization organization = this.checkOrganization(cmd.getOrganizationId());


        if (StringUtils.isEmpty(cmd.getContactToken())) {
            LOGGER.error("contactToken is null");
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_CONTACTTOKEN_ISNULL, "contactToken is null");
        }

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

        Integer namespaceId = UserContext.getCurrentNamespaceId();

        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getContactToken());

        OrganizationMember organizationMember = ConvertHelper.convert(cmd, OrganizationMember.class);
        organizationMember.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
        organizationMember.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
        organizationMember.setContactType(IdentifierType.MOBILE.getCode());
        organizationMember.setCreatorUid(user.getId());
        organizationMember.setNamespaceId(namespaceId);
        organizationMember.setGroupPath(org.getPath());
        organizationMember.setGroupType(org.getGroupType());
        organizationMember.setOperatorUid(user.getId());
        organizationMember.setGroupId(0l);
        /**Modify by lei.lv**/
        java.util.Date nDate = DateHelper.currentGMTTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = sdf.format(nDate);
        java.sql.Date now = java.sql.Date.valueOf(sDate);

        java.sql.Date checkInTime = cmd.getCheckInTime() != null ? java.sql.Date.valueOf(cmd.getCheckInTime()):now;

        organizationMember.setCheckInTime(checkInTime);
        if (organizationMember.getEmployeeStatus() != null) {
            if (organizationMember.getEmployeeStatus().equals(EmployeeStatus.PROBATION.getCode())) {
                organizationMember.setEmploymentTime(java.sql.Date.valueOf(cmd.getEmploymentTime()));
            } else {
                organizationMember.setEmploymentTime(checkInTime);
            }
        } else {
            organizationMember.setEmploymentTime(checkInTime);
        }

        //手机号已注册，就把user id 跟通讯录关联起来
        if (null != userIdentifier) {
            organizationMember.setTargetType(OrganizationMemberTargetType.USER.getCode());
            organizationMember.setTargetId(userIdentifier.getOwnerUid());
        } else {
            organizationMember.setTargetType(OrganizationMemberTargetType.UNTRACK.getCode());
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
        List<Long> memberDetailIds = new ArrayList<>();
        List<Long> current_detailId = new ArrayList<>();
        // 需要添加直属的企业ID集合
        List<Long> direct_under_enterpriseIds = new ArrayList<>();

        Map<Long, Boolean> joinEnterpriseMap = new HashMap<>();

        List<OrganizationMember> leaveMembers = new ArrayList<>();

        dbProvider.execute((TransactionStatus status) -> {

            List<Long> departmentIds = cmd.getDepartmentIds();

            List<Long> groupIds = cmd.getGroupIds();

            List<Long> jobPositionIds = cmd.getJobPositionIds();

            List<Long> jobLevelIds = cmd.getJobLevelIds();

//			if(null == childOrganizationIds || 0 == childOrganizationIds.size()){
//				if(null == desOrgMember){
//					LOGGER.error("phone number already exists. organizationId = {}", finalOrganizationId);
//					throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
//							"phone number already exists.");
//				}
//				organizationMember.setOrganizationId(finalOrganizationId);
//				organizationProvider.createOrganizationMember(organizationMember);
//				return null;
//			}

            /**modify by lei.lv*/
            //总公司和分公司的ID集合
            enterpriseIds.add(org.getId());

            if (null != departmentIds) {
                for (Long departmentId : departmentIds) {
                    Organization o = checkOrganization(departmentId);
                    if (OrganizationGroupType.ENTERPRISE == OrganizationGroupType.fromCode(o.getGroupType())) {
                        if (!enterpriseIds.contains(o.getId())) {
                            //直属场景
                            direct_under_enterpriseIds.add(o.getId());
                            enterpriseIds.add(o.getId());
                        }
                    } else {
                        if (!enterpriseIds.contains(o.getDirectlyEnterpriseId())) {
                            //挂靠场景
                            enterpriseIds.add(o.getDirectlyEnterpriseId());
                        }
                    }
                }
            }else{//如果没有选择部门，则默认直属当前的organizationId
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

            // 先把把成员从公司所有部门都删除掉
//			for (Organization organization : childOrganizations) {
//				OrganizationMember groupMember = organizationProvider.findOrganizationMemberByOrgIdAndToken(cmd.getContactToken(), organization.getId());
//				if(null != groupMember){
//					organizationProvider.deleteOrganizationMemberById(groupMember.getId());
//				}
//			}

            // 先把把成员从公司所有机构都删除掉
//            List<OrganizationMember> members = organizationProvider.listOrganizationMemberByPath(org.getPath(), groupTypes, cmd.getContactToken());
//            for (OrganizationMember member : members) {
//                if (!enterpriseIds.contains(member.getOrganizationId())) {
//                    //记录 退出的公司
//                    if (OrganizationGroupType.ENTERPRISE == OrganizationGroupType.fromCode(member.getGroupType())) {
//                        leaveMembers.add(member);
//                    }
//                    organizationProvider.deleteOrganizationMemberById(member.getId());
//                }
//            }
            /**删除公司级别以下及退出公司的记录**/
            for (Long enterpriseId : enterpriseIds){
                Organization enterprise = checkOrganization(enterpriseId);
                List<OrganizationMember> members = organizationProvider.listOrganizationMemberByPath(enterprise.getPath(), groupTypes, cmd.getContactToken());
                for (OrganizationMember member : members) {
                    if(!member.getGroupType().equals(OrganizationGroupType.ENTERPRISE.getCode())){//删除所有非公司的记录
                        organizationProvider.deleteOrganizationMemberById(member.getId());
                    }else if(member.getGroupType().equals(OrganizationGroupType.ENTERPRISE.getCode()) && !enterpriseIds.contains(member.getOrganizationId())){//删除其他公司（既是本次退出公司）的记录
                        organizationProvider.deleteOrganizationMemberById(member.getId());
                        leaveMembers.add(member);
                    }
                }
            }

			//加入到公司
            for (Long enterpriseId : enterpriseIds) {
                OrganizationMember desOrgMember = this.organizationProvider.findOrganizationMemberByOrgIdAndToken(cmd.getContactToken(), enterpriseId);
                Organization enterprise = checkOrganization(enterpriseId);
                organizationMember.setOrganizationId(enterpriseId);
                organizationMember.setGroupType(enterprise.getGroupType());
                organizationMember.setGroupPath(enterprise.getPath());

                //获取detailId
                Long new_detail_id = getEnableDetailOfOrganizationMember(organizationMember, enterpriseId);

                if (null == desOrgMember) {
                    // 记录一下，成员是新加入公司的
                    joinEnterpriseMap.put(enterpriseId, true);
                    /**Modify BY lei.lv cause MemberDetail**/
                    //绑定member表的detail_id
                    organizationMember.setDetailId(new_detail_id);
                    organizationProvider.createOrganizationMember(organizationMember);
                    memberDetailIds.add(new_detail_id);
                    //保存当前企业关联的detailId,用于多个返回值时进行比对
                    if(enterpriseId.equals(org.getId())){
                        current_detailId.add(new_detail_id);
                    }
                    //新增userOrganization表记录
                    //仅当target为user且grouptype为企业时添加
                    if(organizationMember.getTargetType().equals(OrganizationMemberTargetType.USER.getCode()) && organizationMember.getGroupType().equals(OrganizationType.ENTERPRISE.getCode())){
                        createOrUpdateUserOrganization(organizationMember);
                    }
                } else {
                    /**Modify BY lei.lv cause MemberDetail**/
                    //绑定member表的detail_id
                    desOrgMember.setDetailId(new_detail_id);
                    desOrgMember.setOrganizationId(organizationMember.getOrganizationId());
                    desOrgMember.setGroupType(organizationMember.getGroupType());
                    desOrgMember.setGroupPath(organizationMember.getGroupPath());
                    organizationProvider.updateOrganizationMember(desOrgMember);
                    memberDetailIds.add(new_detail_id);
                    //保存当前企业关联的detailId,用于多个返回值时进行比对
                    if (enterpriseId.equals(org.getId())) {
                        current_detailId.add(new_detail_id);
                    }
                    //更新userOrganization表记录
                    //仅当target为user且grouptype为企业时添加
                    if (desOrgMember.getTargetType().equals(OrganizationMemberTargetType.USER.getCode()) && desOrgMember.getGroupType().equals(OrganizationType.ENTERPRISE.getCode())) {
                        createOrUpdateUserOrganization(organizationMember);
                    }
                }
            }


            //添加除公司之外的机构成员
            departments.addAll(repeatCreateOrganizationmembers(departmentIds,cmd.getContactToken(),enterpriseIds,organizationMember));
            groups.addAll(repeatCreateOrganizationmembers(groupIds,cmd.getContactToken(),enterpriseIds,organizationMember));
            jobPositions.addAll(repeatCreateOrganizationmembers(jobPositionIds,cmd.getContactToken(),enterpriseIds,organizationMember));
            jobLevels.addAll(repeatCreateOrganizationmembers(jobLevelIds,cmd.getContactToken(),enterpriseIds,organizationMember));

            dto.setGroups(groups);

            dto.setDepartments(departments);

            dto.setJobPositions(jobPositions);

            dto.setJobLevels(jobLevels);

            dto.setMemberDetailIds(memberDetailIds);

            dto.setDetailId(current_detailId.get(0));

            return null;
        });


        // 如果有加入的公司 需要发送加入公司的消息等系列操作
        for (Long enterpriseId : enterpriseIds) {

            organizationMember.setOrganizationId(enterpriseId);
            userSearcher.feedDoc(organizationMember);
            //是往公司添加新成员就需要发消息
            if (null != joinEnterpriseMap.get(enterpriseId) && joinEnterpriseMap.get(enterpriseId)) {
                if (OrganizationMemberTargetType.fromCode(organizationMember.getTargetType()) == OrganizationMemberTargetType.USER) {
                    sendMessageForContactApproved(organizationMember);
                }

                //记录新增 log
                OrganizationMemberLog orgLog = ConvertHelper.convert(cmd, OrganizationMemberLog.class);
                orgLog.setOrganizationId(enterpriseId);
                orgLog.setContactName(cmd.getContactName());
                orgLog.setContactToken(cmd.getContactToken());
                orgLog.setUserId(organizationMember.getTargetId());
                orgLog.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                orgLog.setOperationType(OperationType.JOIN.getCode());
                orgLog.setRequestType(RequestType.ADMIN.getCode());
                orgLog.setOperatorUid(UserContext.current().getUser().getId());
                this.organizationProvider.createOrganizationMemberLog(orgLog);
				
				//自动加入公司
				this.doorAccessService.joinCompanyAutoAuth(UserContext.getCurrentNamespaceId(), enterpriseId, organizationMember.getTargetId());
            }
        }
        // 如果有退出的公司 需要发离开公司的消息等系列操作 add by sfyan  20170428
        if (leaveMembers.size() > 0) {
            leaveOrganizationAfterOperation(user.getId(), leaveMembers);
        }
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


    /**odify By lei.lv 2017-6-26**/
    @Override
    public List<OrganizationMemberDTO> listAllChildOrganizationPersonnel(Long organizationId, List<String> groupTypes, String userName) {
        Organization organization = checkOrganization(organizationId);
        List<OrganizationMember> members = this.organizationProvider.listOrganizationMemberByPath(userName, organization.getPath(), groupTypes, VisibleFlag.SHOW, new CrossShardListingLocator(), 1000000);
        List<OrganizationMemberDTO> dtos = members.stream().map(r -> {
            return ConvertHelper.convert(members,OrganizationMemberDTO.class);
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
            contactTokens = members.stream().map(r -> {
                return r.getContactToken();
            }).collect(Collectors.toList());
        }

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
        List<Organization> organizations = organizationProvider.listOrganizations(OrganizationType.PM.getCode(), 0L,
                null, null);
        return organizations.stream().map(r -> ConvertHelper.convert(r, OrganizationDTO.class)).collect(Collectors.toList());
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
        trees.add(allTreeDTO);
        for (OrganizationTreeDTO orgTreeDTO : dtos) {
            if (orgTreeDTO.getParentId().equals(dto.getOrganizationId())) {
                OrganizationTreeDTO organizationTreeDTO = processOrganizationTree(dtos, orgTreeDTO);
                trees.add(organizationTreeDTO);
            }
        }

        dto.setTrees(trees);
        return dto;
    }

    @Override
    public ListOrganizationContactCommandResponse listOrganizationContacts(ListOrganizationContactCommand cmd) {
        ListOrganizationContactCommandResponse response = new ListOrganizationContactCommandResponse();
        Organization org = this.checkOrganization(cmd.getOrganizationId());
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        Organization orgCommoand = new Organization();
        orgCommoand.setId(org.getId());
        orgCommoand.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
        VisibleFlag visibleFlag = VisibleFlag.SHOW;
        if (VisibleFlag.ALL == VisibleFlag.fromCode(cmd.getVisibleFlag())) {
            visibleFlag = null;
        } else if (null != VisibleFlag.fromCode(cmd.getVisibleFlag())) {
            visibleFlag = VisibleFlag.fromCode(cmd.getVisibleFlag());
        }

        List<OrganizationMember> organizationMembers = null;
        if (OrganizationGroupType.fromCode(org.getGroupType()) == OrganizationGroupType.ENTERPRISE || (null != cmd.getFilterScopeTypes() && cmd.getFilterScopeTypes().contains(FilterOrganizationContactScopeType.CURRENT.getCode()))) {
            organizationMembers = this.organizationProvider.listOrganizationPersonnels(cmd.getKeywords(), orgCommoand, cmd.getIsSignedup(), visibleFlag, locator, pageSize);
        } else {
            List<String> groupTypes = new ArrayList<>();
            groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
            groupTypes.add(OrganizationGroupType.GROUP.getCode());
            organizationMembers = this.organizationProvider.listOrganizationMemberByPath(cmd.getKeywords(), org.getPath(), groupTypes, visibleFlag, locator, pageSize);
        }

        //转拼音
        organizationMembers = convertPinyin(organizationMembers);

        if (0 == organizationMembers.size()) {
            return response;
        }
        List<OrganizationContactDTO> members = organizationMembers.stream().map(r -> {
            OrganizationContactDTO dto = ConvertHelper.convert(r, OrganizationContactDTO.class);

            //获取用户头像 昵称
            if (OrganizationMemberTargetType.USER == OrganizationMemberTargetType.fromCode(r.getTargetType())) {
                User user = userProvider.findUserById(r.getTargetId());
                if (null != user) {
                    String avatarUri = user.getAvatar();
                    if (StringUtils.isEmpty(avatarUri))
                        avatarUri = userService.getUserAvatarUriByGender(user.getId(), user.getNamespaceId(), user.getGender());

                    dto.setAvatar(contentServerService.parserUri(avatarUri, EntityType.USER.getCode(), user.getId()));
                    dto.setNickName(dto.getNickName());
                }
            } else {
                String avatarUri = userService.getUserAvatarUriByGender(0L, UserContext.getCurrentNamespaceId(), dto.getGender());
                dto.setAvatar(contentServerService.parserUri(avatarUri, EntityType.USER.getCode(), 0L));
            }

            // 是否展示手机号
            if (r.getIntegralTag4() != null && r.getIntegralTag4() == 1) {
                dto.setContactToken(null);
            }

            //其他字符置换成#号
            if (!StringUtils.isEmpty(r.getInitial())) {
                dto.setInitial(r.getInitial().replace("~", "#"));
            }

            return dto;
        }).collect(Collectors.toList());

        response.setNextPageAnchor(locator.getAnchor());
        response.setMembers(members);
        return response;
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
        contractDTO.setOrganizationName(contract.getOrganizationName());
        contractDTO.setAdminMembers(getAdmins(contract.getOrganizationId()));
        contractDTO.setSignupCount(getSignupCount(contract.getOrganizationId()));

        OrganizationDetail organizationDetail = organizationProvider.findOrganizationDetailByOrganizationId(contract.getOrganizationId());
        if (organizationDetail != null) {
            contractDTO.setContract(organizationDetail.getContact());
            contractDTO.setContactor(organizationDetail.getContactor());
            contractDTO.setServiceUserId(organizationDetail.getServiceUserId());

            OrganizationServiceUser user = getServiceUser(contract.getOrganizationId(), organizationDetail.getServiceUserId());
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
    public void updateOrganizationJobPosition(UpdateOrganizationJobPositionCommand cmd) {
        checkId(cmd.getId());

        OrganizationJobPosition organizationJobPosition = checkOrganizationJobPositionIsNull(cmd.getId());
        organizationJobPosition.setName(cmd.getName());
        organizationProvider.updateOrganizationJobPosition(organizationJobPosition);

    }

    @Override
    public void deleteOrganizationJobPosition(DeleteOrganizationIdCommand cmd) {

        checkId(cmd.getId());

        OrganizationJobPosition organizationJobPosition = checkOrganizationJobPositionIsNull(cmd.getId());
        organizationJobPosition.setStatus(OrganizationJobPositionStatus.INACTIVE.getCode());
        organizationProvider.updateOrganizationJobPosition(organizationJobPosition);
    }

    public List<OrganizationDTO> listOrganizationsByEmail(ListOrganizationsByEmailCommand cmd) {
        Long userId = UserContext.current().getUser().getId();
//		SceneTokenDTO sceneToken = userService.checkSceneToken(userId, cmd.getSceneToken());
        //通过namespace和email domain 找企业
        String emailDomain = cmd.getEmail().substring(cmd.getEmail().indexOf("@") + 1);
		List<Organization> organizations = this.organizationProvider.listOrganizationByEmailDomainAndNamespace(UserContext.getCurrentNamespaceId(), emailDomain,cmd.getCommunityId());
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
			if(null != detail){
				dto.setDisplayName(detail.getDisplayName());
			}
            OrganizationMember m = organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, r.getId());
            if(null != m ){
                dto.setMemberStatus(m.getStatus());
            }
			return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void applyForEnterpriseContactByEmail(ApplyForEnterpriseContactByEmailCommand cmd) {
        Long userId = UserContext.current().getUser().getId();
//		SceneTokenDTO sceneToken = userService.checkSceneToken(userId, cmd.getSceneToken());
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
		map.put("nickName", null == nickName ? useridentifier.getIdentifierToken().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2") : nickName);
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
            approveForEnterpriseContact(cmd2);
            return configProvider.getValue("auth.success", "");
        } catch (Exception e) {
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
    public void deleteChildrenOrganizationJobLevel(DeleteOrganizationIdCommand cmd) {
        deleteOrganization(cmd);

    }

    @Override
    public ListChildrenOrganizationJobLevelResponse listChildrenOrganizationJobLevels(ListAllChildrenOrganizationsCommand cmd) {

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
                List<OrganizationMember> organizationMembers = organizationProvider.listOrganizationPersonnels(cmd.getKeywords(),
                        orgCommoand, null, null, locator, 10000);
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
        checkName(cmd.getName());
        cmd.setGroupType(OrganizationGroupType.JOB_POSITION.getCode());
        createChildrenOrganization(cmd);
    }

    @Override
    public void updateChildrenOrganizationJobPosition(UpdateOrganizationsCommand cmd) {
        updateChildrenOrganization(cmd);
    }

    @Override
    public void deleteChildrenOrganizationJobPosition(DeleteOrganizationIdCommand cmd) {
        deleteOrganization(cmd);
    }

    @Override
    public ListChildrenOrganizationJobPositionResponse listChildrenOrganizationJobPositions(ListAllChildrenOrganizationsCommand cmd) {
        checkId(cmd.getId());
        Organization organization = organizationProvider.findOrganizationById(cmd.getId());
        if (null == organization) {
            LOGGER.error("Organization not found, cmd={}", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Organization not found.");
        }
        Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

        ListChildrenOrganizationJobPositionResponse response = new ListChildrenOrganizationJobPositionResponse();

        List<String> groupTypes = new ArrayList<String>();
        groupTypes.add(OrganizationGroupType.JOB_POSITION.getCode());
        List<Organization> list = organizationProvider.listOrganizationByGroupTypes(cmd.getId(), groupTypes, cmd.getKeywords(), cmd.getPageAnchor(), pageSize);

        int size = list.size();
        if (size > 0) {
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
                List<OrganizationMember> organizationMembers = organizationProvider.listOrganizationPersonnels(null,
                        orgCommoand, null, null, locator, 10000);
                dto.setMembers(organizationMembers.stream().map(m -> ConvertHelper.convert(m, OrganizationMemberDTO.class))
                        .collect(Collectors.toList()));

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
    public List<OrganizationDTO> listOrganizationsByModuleId(ListOrganizationByModuleIdCommand cmd) {
        List<ServiceModuleAssignment> assignments = serviceModuleProvider.listServiceModuleAssignmentByModuleId(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getModuleId());
        assignments.addAll(serviceModuleProvider.listServiceModuleAssignmentByModuleId(com.everhomes.rest.common.EntityType.ALL.getCode(), 0L, cmd.getModuleId()));//负责全部业务范围的对象，也要查询出来
        assignments.addAll(serviceModuleProvider.listServiceModuleAssignmentByModuleId(cmd.getOwnerType(), cmd.getOwnerId(), 0L)); //负责全部业务模块的对象，也要查询出来

        //将targetType = EntityType.ORGANIZATIONS的assigments过滤出targetId的set集合
        Set<Long> targetIdSet = new HashSet<>();
        for (ServiceModuleAssignment assignment : assignments) {
            if (EntityType.fromCode(assignment.getTargetType()) == EntityType.ORGANIZATIONS) {
                //包含子部门，就拿path下面所有的机构
                if(IncludeChildFlagType.YES == IncludeChildFlagType.fromCode(assignment.getIncludeChildFlag())){
                    Organization organization = organizationProvider.findOrganizationById(assignment.getTargetId());
                    if(null != organization){
                        addPathOrganizationId(organization.getPath(), targetIdSet);
                    }
                }else{
                    targetIdSet.add(assignment.getTargetId());
                }
            }
        }
        //循环查找targetIdSet
        List<OrganizationDTO> organizationDTOs = targetIdSet.stream().map(r -> {
            Organization organization = organizationProvider.findOrganizationById(r);
            if (null != organization && OrganizationStatus.fromCode(organization.getStatus()) == OrganizationStatus.ACTIVE) {
                if (null == cmd.getGroupTypes() || cmd.getGroupTypes().size() == 0) {//未指定机构类型
                    return ConvertHelper.convert(organization, OrganizationDTO.class);
                } else {
                    if (cmd.getGroupTypes().contains(organization.getGroupType())) {//符合指定机构类型
                        return ConvertHelper.convert(organization, OrganizationDTO.class);
                    }
                }
            }
            return null;
        }).collect(Collectors.toList());

        return organizationDTOs;
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
            dtos.add(ConvertHelper.convert(member, OrganizationContactDTO.class));
        }
        return dtos;
    }

    @Override
    public List<OrganizationMember> listOrganizationContactByJobPositionId(List<Long> organizationIds, Long jobPositionId){
        return listOrganizationContactByJobPositionId(null, organizationIds, jobPositionId);
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

    private List<OrganizationMember> listOrganizationContactByJobPositionId(Long enterpriseId, Long jobPositionId) {
        return listOrganizationContactByJobPositionId(enterpriseId, null, jobPositionId);
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
            for (Long organizationid : organizationIds) {
                jobPositions.addAll(organizationProvider.listOrganizationByGroupTypes(organizationid, groupTypes));
            }

            for (Organization jobPosition : jobPositions) {
                if (null != organizationProvider.getOrganizationJobPositionMapByOrgIdAndJobPostionId(jobPosition.getId(), jobPositionId)) {
                    jobPositionIds.add(jobPosition.getId());
                }
            }
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
            List<OrganizationMember> depart_members = new ArrayList<>();
            for(Long orgId :organizationIds){
                members.stream().map(r ->{
                    Organization org = this.checkOrganization(orgId);
                    if(org != null){
                        if(this.organizationProvider.checkOneOfOrganizationWithContextToken(org.getPath(), r.getContactToken())){
                            depart_members.add(r);
                        }
                    }
                   return null;
                }).collect(Collectors.toList());
            }
            return depart_members;
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
    public ImportFileResponse<ImportOrganizationContactDataDTO> getImportFileResult(GetImportFileResultCommand cmd) {
        return importFileService.getImportFileResult(cmd.getTaskId());
    }

    @Override
    public void exportImportFileFailResultXls(GetImportFileResultCommand cmd, HttpServletResponse httpResponse) {
//		Map<String, String> titleMap = new HashMap<>();
//		titleMap.put("contactName", "姓名");
//		titleMap.put("contactToken", "手机号");
//		titleMap.put("gender", "性别");
//		titleMap.put("orgnaizationPath", "部门");
//		titleMap.put("jobPosition", "岗位");
//		titleMap.put("jobLevel", "职级");
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
        if(org == null){
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
				query.addConditions(Tables.EH_ORGANIZATION_MEMBERS. ORGANIZATION_ID.eq(org.getId()));
                //控制只查找手机用户contactType=0
                //query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TYPE.eq(value));
                return query;
            }
        };

        List<OrganizationMember> organizationMembers = organizationProvider.listUsersOfEnterprise(locator, pageSize, callback);
		List<OrganizationContactDTO> dtos = organizationMembers.stream().map(r->{

            User userInfo = this.userProvider.findUserById(r.getTargetId());
			if(userInfo == null){
				LOGGER.error("Nick name is not found ="+cmd.getOrganizationId());
                r.setNickName("");
			}else{
                r.setNickName(userInfo.getNickName());
            }
            return ConvertHelper.convert(r, OrganizationContactDTO.class);

        }).collect(Collectors.toList());

		Integer totalRecords =  organizationProvider.countUsersOfEnterprise(locator, callback);

        ListOrganizationContactCommandResponse response = new ListOrganizationContactCommandResponse();
        response.setNextPageAnchor(locator.getAnchor());
        response.setMembers(dtos);
        response.setTotalCount(totalRecords);
        response.setNamespaceId(org.getNamespaceId());

        return response;

    }

    //  New 20th.May
    @Override
    public ListPersonnelsV2CommandResponse listOrganizationPersonnelsV2(ListPersonnelsV2Command cmd) {
        ListPersonnelsV2CommandResponse response = new ListPersonnelsV2CommandResponse();
        ListOrganizationMemberCommandResponse res = this.listOrganizationPersonnels(ConvertHelper.convert(cmd, ListOrganizationContactCommand.class), false);

        if (res.getMembers() == null || res.getMembers().isEmpty()) {
            return response;
        } else {
            //  查找合同到期时间
            List<Long> detailIds = new ArrayList<>();
            res.getMembers().forEach(r -> {
                detailIds.add(r.getDetailId());
            });
            List<Object[]> endTimeList = this.organizationProvider.findContractEndTimeById(detailIds);

            response.setMembers(res.getMembers().stream().filter(r ->{
                return !StringUtils.isEmpty(r.getDetailId());
            }).map(r -> {
                    OrganizationMemberV2DTO dto = ConvertHelper.convert(r, OrganizationMemberV2DTO.class);
                    //  设置合同到期时间
                    if (endTimeList != null) {
                        endTimeList.forEach(rr -> {

                            if (rr[0].equals(dto.getDetailId())) {
                                dto.setEndTime((java.sql.Date) rr[1]);
                            }
                        });
                    }
                    return dto;
            }).collect(Collectors.toList()));

            response.setNextPageOffset(res.getNextPageOffset());
            response.setNextPageAnchor(res.getNextPageAnchor());
            return response;
        }
    }

    @Override
    public PersonnelsDetailsV2Response getOrganizationPersonnelDetailsV2(GetPersonnelDetailsV2Command cmd) {
        PersonnelsDetailsV2Response response = new PersonnelsDetailsV2Response();
        OrganizationMemberDetails memberDetails = this.organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getDetailId());


        OrganizationMemberBasicDTO basic = this.getOrganizationMemberBasicInfo(ConvertHelper.convert(cmd,GetOrganizationMemberInfoCommand.class));
        OrganizationMemberBackGroundDTO backGround = ConvertHelper.convert(memberDetails,OrganizationMemberBackGroundDTO.class);
        OrganizationMemberSocialSecurityDTO socialSecurity = ConvertHelper.convert(memberDetails,OrganizationMemberSocialSecurityDTO.class);

        List<OrganizationMemberContractsDTO> contracts = this.listOrganizationMemberContracts(ConvertHelper.convert(cmd, ListOrganizationMemberContractsCommand.class));

        if (basic != null) {
            response.setBasic(basic);
        }
        if(backGround != null){
            backGround.setEducations(this.listOrganizationMemberEducations(ConvertHelper.convert(cmd, ListOrganizationMemberEducationsCommand.class)));
            backGround.setWorkExperiences(this.listOrganizationMemberWorkExperiences(ConvertHelper.convert(cmd, ListOrganizationMemberWorkExperiencesCommand.class)));
            response.setBackGround(backGround);
        }
        if(socialSecurity != null){
            socialSecurity.setInsurances(this.listOrganizationMemberInsurances(ConvertHelper.convert(cmd, ListOrganizationMemberInsurancesCommand.class)));
            response.setSocialSecurity(socialSecurity);
        }
        if(contracts != null ){
            response.setContracts(contracts);
        }
        return response;
    }

    //  Updated By R form function addOrganizationPersonnel
    @Override
    public OrganizationMemberDTO addOrganizationPersonnelV2(AddOrganizationPersonnelV2Command cmd) {
        OrganizationMemberDTO memberDTO = this.addOrganizationPersonnel(ConvertHelper.convert(cmd, AddOrganizationPersonnelCommand.class));

        if(StringUtils.isEmpty(cmd.getDetailId())){
            this.addProfileJobChangeLogs(memberDTO.getDetailId(),PersonChangeType.ENTRY.getCode(),
                    "eh_organization_member_details","",DateUtil.parseTimestamp(cmd.getCheckInTime()));
        }else{
            if(!StringUtils.isEmpty(cmd.getUpdateLogs())){
                if(!StringUtils.isEmpty(cmd.getUpdateLogs().getDepartment()))
                    this.addProfileJobChangeLogs(memberDTO.getDetailId(),PersonChangeType.DEPCHANGE.getCode(),
                            "eh_organization_member_details",cmd.getUpdateLogs().getDepartment(),null);
                if(!StringUtils.isEmpty(cmd.getUpdateLogs().getJobPosition()))
                    this.addProfileJobChangeLogs(memberDTO.getDetailId(),PersonChangeType.POICHANGE.getCode(),
                            "eh_organization_member_details",cmd.getUpdateLogs().getJobPosition(),null);
                if(!StringUtils.isEmpty(cmd.getUpdateLogs().getJobLevelIds()) && memberDTO.getJobLevels().size() > 0)
                    this.addProfileJobChangeLogs(memberDTO.getDetailId(),PersonChangeType.LEVCHANGE.getCode(),
                            "eh_organization_member_details",memberDTO.getJobLevels().get(0).getName(),null);
            }
        }

        //  计算档案完整度
        GetProfileIntegrityCommand integrity = new GetProfileIntegrityCommand();
        integrity.setDetailId(memberDTO.getDetailId());
        this.getProfileIntegrity(integrity);
        return memberDTO;

    }

    @Override
    public OrganizationMemberBasicDTO getOrganizationMemberBasicInfo(GetOrganizationMemberInfoCommand cmd) {
        LOGGER.info("Invoke GetOrganizationMemberInfoCommand.cmd.getDetailId={}", cmd.getDetailId());
        if (cmd.getDetailId() == null) {
            return null;
        }
        OrganizationMemberDetails memberDetails = this.organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getDetailId());
        if (memberDetails != null) {
            OrganizationMemberBasicDTO memberDTO = ConvertHelper.convert(memberDetails, OrganizationMemberBasicDTO.class);

            //  计算在职天数
            Long workingDays = ((new Timestamp(DateHelper.currentGMTTime().getTime()).getTime() - memberDetails.getCheckInTime().getTime()) / (24 * 60 * 60 * 1000));
            memberDTO.setWorkingDays(workingDays);


            Long orgId;
            Organization org = this.checkOrganization(memberDTO.getOrganizationId());
            if (org.getGroupType().equals(OrganizationGroupType.DEPARTMENT.getCode())) {
                orgId = org.getDirectlyEnterpriseId();
            } else {
                orgId = org.getId();
            }
            Long directlyOrgId = orgId;

            List<String> groupTypes = new ArrayList<>();
            groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
            groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
            groupTypes.add(OrganizationGroupType.GROUP.getCode());

            Organization directlyEnterprise = checkOrganization(directlyOrgId);

            List<OrganizationDTO> departments = new ArrayList<>();
            memberDTO.setDepartments(this.getOrganizationMemberGroups(groupTypes, memberDTO.getContactToken(), directlyEnterprise.getPath()));
            //岗位
            memberDTO.setJobPositions(this.getOrganizationMemberGroups(OrganizationGroupType.JOB_POSITION, memberDTO.getContactToken(), directlyEnterprise.getPath()));

            //职级
            memberDTO.setJobLevels(this.getOrganizationMemberGroups(OrganizationGroupType.JOB_LEVEL, memberDTO.getContactToken(), directlyEnterprise.getPath()));

            if (OrganizationMemberTargetType.USER.getCode().equals(memberDTO.getTargetType())) {
                User user = userProvider.findUserById(memberDTO.getTargetId());
                if (null != user) {
                    memberDTO.setAvatar(contentServerService.parserUri(user.getAvatar(), EntityType.USER.getCode(), user.getId()));
                    memberDTO.setNickName(memberDTO.getNickName());
                }

            }
            List<Object> result = this.getOrganizationMemberIdAndVisibleFlag(memberDTO.getContactToken(),memberDTO.getOrganizationId());
            memberDTO.setMembersId((Long)result.get(0));
            memberDTO.setVisibleFlag((Byte)result.get(1));
            return memberDTO;
        } else {
            return null;
        }
    }

    @Override
    public void updateOrganizationMemberBackGround(UpdateOrganizationMemberBackGroundCommand cmd) {
        OrganizationMemberDetails organizationMemberDetails = this.organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getDetailId());

        organizationMemberDetails.setEnName(cmd.getEnName() != null ? cmd.getEnName() : "");
        organizationMemberDetails.setBirthday(!StringUtils.isEmpty(cmd.getBirthday()) ? java.sql.Date.valueOf(cmd.getBirthday()) : null);
        if (!StringUtils.isEmpty(cmd.getMaritalFlag())) {
            organizationMemberDetails.setMaritalFlag(cmd.getMaritalFlag());
        }
        organizationMemberDetails.setPoliticalStatus(cmd.getPoliticalStatus() != null ? cmd.getPoliticalStatus() : "");
        organizationMemberDetails.setNativePlace(cmd.getNativePlace() != null ? cmd.getNativePlace() : "");
        organizationMemberDetails.setRegResidence(cmd.getRegResidence() != null ? cmd.getRegResidence() : "");
        organizationMemberDetails.setIdNumber(cmd.getIdNumber() != null ? cmd.getIdNumber() : "");
        organizationMemberDetails.setEmail(cmd.getEmail() != null ? cmd.getEmail() : "");
        organizationMemberDetails.setWechat(cmd.getWechat() != null ? cmd.getWechat() : "");
        organizationMemberDetails.setQq(cmd.getQq() != null ? cmd.getQq() : "");
        organizationMemberDetails.setEmergencyName(cmd.getEmergencyName() != null ? cmd.getEmergencyName() : "");
        organizationMemberDetails.setEmergencyContact(cmd.getEmergencyContact() != null ? cmd.getEmergencyContact() : "");
        organizationMemberDetails.setAddress(cmd.getAddress() != null ? cmd.getAddress() : "");
        organizationMemberDetails.setSalaryCardNumber(cmd.getSalaryCardNumber() != null ? cmd.getSalaryCardNumber() : "");
        organizationMemberDetails.setSocialSecurityNumber(cmd.getSocialSecurityNumber() != null ? cmd.getSocialSecurityNumber() : "");
        organizationMemberDetails.setProvidentFundNumber(cmd.getProvidentFundNumber() != null ? cmd.getProvidentFundNumber() : "");


        this.organizationProvider.updateOrganizationMemberDetails(organizationMemberDetails, cmd.getDetailId());

    }

    @Override
    public OrganizationMemberEducationsDTO addOrganizationMemberEducations(AddOrganizationMemberEducationsCommand cmd) {
/*        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
	    User user =UserContext.current().getUser();
        if (cmd.getDetailId() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter, detailId should not be null or empty");
        OrganizationMemberEducations education = new OrganizationMemberEducations();
        education.setDetailId(cmd.getDetailId());
        education.setNamespaceId(user.getNamespaceId());
        education.setSchoolName(cmd.getSchoolName());
//        maplog.put("学校名字：")
        education.setDegree(cmd.getDegree());
        education.setMajor(cmd.getMajor());
        education.setEnrollmentTime(java.sql.Date.valueOf(cmd.getEnrollmentTime()));
        education.setGraduationTime(java.sql.Date.valueOf(cmd.getGraduationTime()));
        //	获取当前时间
        Date date = new Date();
        Timestamp now = new Timestamp(date.getTime());
        education.setCreateTime(now);
        //	获取操作人员
        if (user.getId() != null)
            education.setCreatorUid(user.getId());
        else
            education.setCreatorUid(Long.valueOf(0));
        //  暂且设置新增数据为有效数据
        education.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
        this.organizationProvider.createOranizationMemberEducationInfo(education);

//        this.addProfileLogs(education.getDetailId(),);
//        LOGGER.debug("No organization community filter for the user, userId={}, sceneToken={}", user.getId(), sceneToken);
        return ConvertHelper.convert(education, OrganizationMemberEducationsDTO.class);
    }

    @Override
    public void deleteOrganizationMemberEducations(DeleteOrganizationMemberEducationsCommand cmd) {
        if (cmd.getId() == null)
            return;
        LOGGER.info("Invoke deleteOrganizationMemberEducations.education.id={}" + cmd.getId());
        OrganizationMemberEducations education = this.organizationProvider.findOrganizationEducationInfoById(cmd.getId());
        if (education == null) {
            LOGGER.info("Cannot find the corresponding infomation of education");
            return;
        }
        education.setStatus(OrganizationMemberStatus.INACTIVE.getCode());
        this.organizationProvider.deleteOranizationMemberEducationInfo(education);
    }

    @Override
    public void updateOrganizationMemberEducations(UpdateOrganizationMemberEducationsCommand cmd) {
        if (cmd.getId() == null)
            return;
        LOGGER.info("Invoke updateOrganizationMemberEducations.education.id={}" + cmd.getId());
        OrganizationMemberEducations education = this.organizationProvider.findOrganizationEducationInfoById(cmd.getId());
        if (education == null) {
            LOGGER.info("Cannot find the corresponding infomation of education");
            return;
        }
        if(cmd.getSchoolName() != null)
        education.setSchoolName(cmd.getSchoolName());
        if(cmd.getDegree() != null)
        education.setDegree(cmd.getDegree());
        if(cmd.getMajor() != null)
        education.setMajor(cmd.getMajor());
        if(cmd.getEnrollmentTime() != null)
        education.setEnrollmentTime(java.sql.Date.valueOf(cmd.getEnrollmentTime()));
        if(cmd.getGraduationTime() != null)
        education.setGraduationTime(java.sql.Date.valueOf(cmd.getGraduationTime()));

        this.organizationProvider.updateOranizationMemberEducationInfo(education);
    }

    @Override
    public List<OrganizationMemberEducationsDTO> listOrganizationMemberEducations(ListOrganizationMemberEducationsCommand cmd) {

        List<OrganizationMemberEducationsDTO> response = new ArrayList<>();
        List<OrganizationMemberEducations> educations = this.organizationProvider.listOrganizationMemberEducations(cmd.getDetailId());
        if (educations != null) {
            educations.forEach(r-> {
                OrganizationMemberEducationsDTO dto = ConvertHelper.convert(r, OrganizationMemberEducationsDTO.class);
                response.add(dto);
            });
        }
        return response;
    }

    @Override
    public OrganizationMemberWorkExperiencesDTO addOrganizationMemberWorkExperiences(AddOrganizationMemberWorkExperiencesCommand cmd) {
        User user =UserContext.current().getUser();
        if (cmd.getDetailId() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter, detailId should not be null or empty");
        OrganizationMemberWorkExperiences experience = new OrganizationMemberWorkExperiences();
        experience.setDetailId(cmd.getDetailId());
        experience.setNamespaceId(user.getNamespaceId());
        experience.setEnterpriseName(cmd.getEnterpriseName());
        experience.setPosition(cmd.getPosition());
        experience.setJobType(cmd.getJobType());
        experience.setEntryTime(java.sql.Date.valueOf(cmd.getEntryTime()));
        experience.setDepartureTime(java.sql.Date.valueOf(cmd.getDepartureTime()));
        //	获取当前时间
        Date date = new Date();
        Timestamp now = new Timestamp(date.getTime());
        experience.setCreateTime(now);
        //	获取操作人员
        if (user.getId() != null)
            experience.setCreatorUid(user.getId());
        else
            experience.setCreatorUid(Long.valueOf(0));
        //  暂且设置新增数据为有效数据
        experience.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
        this.organizationProvider.createOranizationMemberWorkExperience(experience);

        return ConvertHelper.convert(experience, OrganizationMemberWorkExperiencesDTO.class);
    }

    @Override
    public List<OrganizationMemberWorkExperiencesDTO> listOrganizationMemberWorkExperiences(ListOrganizationMemberWorkExperiencesCommand cmd) {

        List<OrganizationMemberWorkExperiencesDTO> response = new ArrayList<>();
        List<OrganizationMemberWorkExperiences> workExperiences = this.organizationProvider.listOrganizationMemberWorkExperiences(cmd.getDetailId());
        if (workExperiences != null) {
            workExperiences.forEach(r -> {
                OrganizationMemberWorkExperiencesDTO dto = ConvertHelper.convert(r, OrganizationMemberWorkExperiencesDTO.class);
                response.add(dto);
            });
        }
        return response;
    }

    @Override
    public void deleteOrganizationMemberWorkExperiences(DeleteOrganizationMemberWorkExperiencesCommand cmd) {
        if (cmd.getId() == null)
            return;
        LOGGER.info("Invoke deleteOrganizationMemberWorkExperiences.workExperience.id={}" + cmd.getId());
        OrganizationMemberWorkExperiences experience = this.organizationProvider.findOrganizationWorkExperienceById(cmd.getId());
        if (experience == null) {
            LOGGER.info("Cannot find the corresponding infomation of work experience");
            return;
        }
        experience.setStatus(OrganizationMemberStatus.INACTIVE.getCode());
        this.organizationProvider.deleteOranizationMemberWorkExperience(experience);
    }

    @Override
    public void updateOrganizationMemberWorkExperiences(UpdateOrganizationMemberWorkExperiencesCommand cmd) {
        if (cmd.getId() == null)
            return;
        LOGGER.info("Invoke updateOrganizationMemberWorkExperiences.workExperience.id={}" + cmd.getId());
        OrganizationMemberWorkExperiences experience = this.organizationProvider.findOrganizationWorkExperienceById(cmd.getId());
        if (experience == null) {
            LOGGER.info("Cannot find the corresponding infomation of work experience");
            return;
        }
        if (cmd.getEnterpriseName() != null)
            experience.setEnterpriseName(cmd.getEnterpriseName());
        if (cmd.getPosition() != null)
            experience.setPosition(cmd.getPosition());
        if (cmd.getJobType() != null)
            experience.setJobType(cmd.getJobType());
        if (cmd.getEntryTime() != null)
            experience.setEntryTime(java.sql.Date.valueOf(cmd.getEntryTime()));
        if (cmd.getDepartureTime() != null)
            experience.setDepartureTime(java.sql.Date.valueOf(cmd.getDepartureTime()));

        this.organizationProvider.updateOranizationMemberWorkExperience(experience);
    }

    @Override
    public OrganizationMemberInsurancesDTO addOrganizationMemberInsurances(AddOrganizationMemberInsurancesCommand cmd) {
        User user = UserContext.current().getUser();
        if (cmd.getDetailId() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter, detailId should not be null or empty");
        OrganizationMemberInsurances insurance = new OrganizationMemberInsurances();
        insurance.setDetailId(cmd.getDetailId());
        insurance.setNamespaceId(user.getNamespaceId());
        insurance.setName(cmd.getName());
        insurance.setEnterprise(cmd.getEnterprise());
        insurance.setNumber(cmd.getNumber());
        insurance.setStartTime(java.sql.Date.valueOf(cmd.getStartTime()));
        insurance.setEndTime(java.sql.Date.valueOf(cmd.getEndTime()));
        //	获取当前时间
        Date date = new Date();
        Timestamp now = new Timestamp(date.getTime());
        insurance.setCreateTime(now);
        //	获取操作人员
        if (user.getId() != null)
            insurance.setCreatorUid(user.getId());
        else
            insurance.setCreatorUid(Long.valueOf(0));
        //  暂且设置新增数据为有效数据
        insurance.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
        this.organizationProvider.createOranizationMemberInsurance(insurance);

        return ConvertHelper.convert(insurance, OrganizationMemberInsurancesDTO.class);
    }


    @Override
    public void deleteOrganizationMemberInsurances(DeleteOrganizationMemberInsurancesCommand cmd) {
        if (cmd.getId() == null)
            return;
        LOGGER.info("Invoke deleteOrganizationMemberInsurances.insurance.id={}" + cmd.getId());
        OrganizationMemberInsurances insurance = this.organizationProvider.findOrganizationInsuranceById(cmd.getId());
        if (insurance == null) {
            LOGGER.info("Cannot find the corresponding infomation of insurance");
            return;
        }
        insurance.setStatus(OrganizationMemberStatus.INACTIVE.getCode());
        this.organizationProvider.deleteOranizationMemberInsurance(insurance);
    }

    @Override
    public void updateOrganizationMemberInsurances(UpdateOrganizationMemberInsurancesCommand cmd) {
        if (cmd.getId() == null)
            return;
        LOGGER.info("Invoke updateOrganizationMemberInsurance.insurance.id={}" + cmd.getId());
        OrganizationMemberInsurances insurance = this.organizationProvider.findOrganizationInsuranceById(cmd.getId());
//        OrganizationMemberWorkExperiences experience = this.organizationProvider.findOrganizationWorkExperienceById(cmd.getId());
        if (insurance == null) {
            LOGGER.info("Cannot find the corresponding infomation of insurance");
            return;
        }
        if(cmd.getName() != null)
        insurance.setName(cmd.getName());
        if(cmd.getEnterprise() != null)
        insurance.setEnterprise(cmd.getEnterprise());
        if(cmd.getNumber() != null)
        insurance.setNumber(cmd.getNumber());
        if(cmd.getStartTime() != null)
        insurance.setStartTime(java.sql.Date.valueOf(cmd.getStartTime()));
        if(cmd.getEndTime() != null)
        insurance.setEndTime(java.sql.Date.valueOf(cmd.getEndTime()));

        this.organizationProvider.updateOrganizationMemberInsurance(insurance);
    }

    @Override
    public List<OrganizationMemberInsurancesDTO> listOrganizationMemberInsurances(ListOrganizationMemberInsurancesCommand cmd) {
        List<OrganizationMemberInsurancesDTO> response = new ArrayList<>();
        List<OrganizationMemberInsurances> insurances = this.organizationProvider.listOrganizationMemberInsurances(cmd.getDetailId());
        if (insurances != null) {
            insurances.forEach(r -> {
                OrganizationMemberInsurancesDTO dto = ConvertHelper.convert(r, OrganizationMemberInsurancesDTO.class);
                response.add(dto);
            });
        }
        return response;
    }

    @Override
    public OrganizationMemberContractsDTO addOrganizationMemberContracts(AddOrganizationMemberContractsCommand cmd) {
        User user = UserContext.current().getUser();
        if (cmd.getDetailId() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter, detailId should not be null or empty");
        OrganizationMemberContracts contract = new OrganizationMemberContracts();
        contract.setDetailId(cmd.getDetailId());
        contract.setNamespaceId(user.getNamespaceId());
        contract.setContractNumber(cmd.getContractNumber());
        contract.setStartTime(java.sql.Date.valueOf(cmd.getStartTime()));
        contract.setEndTime(java.sql.Date.valueOf(cmd.getEndTime()));
        //	获取当前时间
        Date date = new Date();
        Timestamp now = new Timestamp(date.getTime());
        contract.setCreateTime(now);
        //	获取操作人员
        if (user.getId() != null)
            contract.setCreatorUid(user.getId());
        else
            contract.setCreatorUid(Long.valueOf(0));
        //  暂且设置新增数据为有效数据
        contract.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
        this.organizationProvider.createOranizationMemberContract(contract);

        return ConvertHelper.convert(contract, OrganizationMemberContractsDTO.class);
    }

    @Override
    public void deleteOrganizationMemberContracts(DeleteOrganizationMemberContractsCommand cmd) {
        if (cmd.getId() == null)
            return;
        LOGGER.info("Invoke deleteOrganizationMemberContracts.contract.id={}" + cmd.getId());
        OrganizationMemberContracts contract = this.organizationProvider.findOrganizationContractById(cmd.getId());
        if (contract == null) {
            LOGGER.info("Cannot find the corresponding infomation of contract");
            return;
        }
        contract.setStatus(OrganizationMemberStatus.INACTIVE.getCode());
        this.organizationProvider.deleteOranizationMemberContract(contract);
    }

    @Override
    public void updateOrganizationMemberContracts(UpdateOrganizationMemberContractsCommand cmd) {
        if(cmd.getId() == null)
            return;
        OrganizationMemberContracts contract = this.organizationProvider.findOrganizationContractById(cmd.getId());
        if (contract == null) {
            LOGGER.info("Cannot find the corresponding infomation of contract");
            return;
        }
        if(cmd.getContractNumber() != null)
        contract.setContractNumber(cmd.getContractNumber());
        if(cmd.getContractNumber() != null)
        contract.setStartTime(java.sql.Date.valueOf(cmd.getStartTime()));
        if(cmd.getContractNumber() != null)
        contract.setEndTime(java.sql.Date.valueOf(cmd.getEndTime()));

        this.organizationProvider.updateOrganizationMemberContract(contract);
    }

    @Override
    public List<OrganizationMemberContractsDTO> listOrganizationMemberContracts(ListOrganizationMemberContractsCommand cmd) {
        List<OrganizationMemberContractsDTO> response = new ArrayList<>();
        List<OrganizationMemberContracts> contracts = this.organizationProvider.listOrganizationMemberContracts(cmd.getDetailId());
        if (contracts != null) {
            contracts.forEach(r ->{
                OrganizationMemberContractsDTO dto = ConvertHelper.convert(r, OrganizationMemberContractsDTO.class);
                response.add(dto);
            });
        }
        return response;
    }



    @Override
    public void updateOrganizationEmployeeStatus(UpdateOrganizationEmployeeStatusCommand cmd) {
        if (cmd.getEmployeeStatus().equals(EmployeeStatus.PROBATION.getCode())) {
            this.organizationProvider.updateOrganizationEmploymentTime(cmd.getDetailId(), java.sql.Date.valueOf(cmd.getRemarks()));
        } else if (cmd.getEmployeeStatus().equals(EmployeeStatus.ONTHEJOB.getCode())) {
            this.organizationProvider.updateOrganizationEmployeeStatus(cmd.getDetailId(), cmd.getEmployeeStatus());
            this.addProfileJobChangeLogs(cmd.getDetailId(),PersonChangeType.POSITIVE.getCode(),"eh_organization_member_details",cmd.getRemarks(),null);

        } else if (cmd.getEmployeeStatus().equals(EmployeeStatus.LEAVETHEJOB.getCode())) {
            this.organizationProvider.updateOrganizationEmployeeStatus(cmd.getDetailId(), cmd.getEmployeeStatus());
            this.addProfileJobChangeLogs(cmd.getDetailId(),PersonChangeType.LEAVE.getCode(),"eh_organization_member_details",cmd.getRemarks(),null);
        }
    }

    @Override
    public List<MemberRecordChangesByJobDTO> listMemberRecordChangesByJob(ListMemberRecordChangesByJobCommand cmd) {
        List<MemberRecordChangesByJobDTO> response = new ArrayList<>();
        List<OrganizationMemberProfileLogs> logs = this.organizationProvider.listMemberRecordChanges(cmd.getDetailId());
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        if(logs != null){
            logs.forEach(r ->{
                MemberRecordChangesByJobDTO dto = new MemberRecordChangesByJobDTO();
                dto.setOperationTime(sdf.format(r.getOperationTime()));
                dto.setPersonChangeType(r.getPersonChangeType());
                dto.setReason(r.getPersonChangeReason());
                response.add(dto);
            });
        }
        return response;
    }

    @Override
    public ListMemberProfileRecordsCommandResponse listMemberRecordChangesByProfile(ListMemberProfileRecordsCommand cmd) {
/*        ListMemberProfileRecordsCommandResponse response = new ListMemberProfileRecordsCommandResponse();
        List<OrganizationMemberProfileLogs> records = this.organizationProvider.listMemberRecordChanges(cmd.getDetailId());
        if(records != null){
            response.setMemberProfileRecords(records.stream().map(r -> {
                MemberRecordChangesByProfileDTO dto = ConvertHelper.convert(r,MemberRecordChangesByProfileDTO.class);
                return dto;
            }).collect(Collectors.toList()));
        }
        return response;*/
        return null;
    }

    public OrganizationMemberProfileIntegrity getProfileIntegrity(GetProfileIntegrityCommand cmd) {
        OrganizationMemberProfileIntegrity result = new OrganizationMemberProfileIntegrity(0, 0, 0, 0);
        PersonnelsDetailsV2Response response = this.getOrganizationPersonnelDetailsV2(ConvertHelper.convert(cmd, GetPersonnelDetailsV2Command.class));

        //  基本信息完整度判断
        result.setBasicIntegrity(this.checkBasicIntegrity(response.getBasic()));

        //  背景信息完整度判断
        result.setBackEndIntegrity(this.checkBackEndIntegrity(response.getBackGround()));

        //  社保信息完整度判断
        result.setSocialSecurityIntegrity(this.checkSocialSecurityIntegrity(response.getSocialSecurity()));

        //  合同信息完整度判断
        if (StringUtils.isEmpty(response.getContracts()) || response.getContracts().isEmpty())
            result.setContractIntegrity(0);
        else
            result.setContractIntegrity(15);

        //  返回结果至数据库
        result.setProfileIntegrity(result.getBasicIntegrity() + result.getBackEndIntegrity()
                + result.getSocialSecurityIntegrity() + result.getContractIntegrity());
        this.organizationProvider.updateProfileIntegrity(cmd.getDetailId(), result.getProfileIntegrity());
        return result;
    }

    private Integer checkBasicIntegrity(OrganizationMemberBasicDTO basic) {
        if (StringUtils.isEmpty(basic.getContactName()))
            return 0;
        else if (StringUtils.isEmpty(basic.getGender()))
            return 0;
        else if (StringUtils.isEmpty(basic.getDepartments()) || basic.getDepartments().isEmpty())
            return 0;
        else if (StringUtils.isEmpty(basic.getJobPositions()) || basic.getJobPositions().isEmpty())
            return 0;
        else if (StringUtils.isEmpty(basic.getJobLevels()) || basic.getJobLevels().isEmpty())
            return 0;
        else if (StringUtils.isEmpty(basic.getEmployeeType()))
            return 0;
        else if (StringUtils.isEmpty(basic.getCheckInTime()))
            return 0;
        else if (StringUtils.isEmpty(basic.getContactToken()))
            return 0;
        else if (StringUtils.isEmpty(basic.getEmployeeNo()))
            return 0;
        else
            return 25;
    }

    private Integer checkBackEndIntegrity(OrganizationMemberBackGroundDTO backGround) {
        if (StringUtils.isEmpty(backGround.getEnName()))
            return 0;
        else if (StringUtils.isEmpty(backGround.getBirthday()))
            return 0;
        else if (StringUtils.isEmpty(backGround.getMaritalFlag()))
            return 0;
        else if (StringUtils.isEmpty(backGround.getPoliticalStatus()))
            return 0;
        else if (StringUtils.isEmpty(backGround.getNativePlace()))
            return 0;
        else if (StringUtils.isEmpty(backGround.getRegResidence()))
            return 0;
        else if (StringUtils.isEmpty(backGround.getIdNumber()))
            return 0;
        else if (StringUtils.isEmpty(backGround.getEmail()))
            return 0;
        else if (StringUtils.isEmpty(backGround.getWechat()))
            return 0;
        else if (StringUtils.isEmpty(backGround.getQq()))
            return 0;
        else if (StringUtils.isEmpty(backGround.getEmergencyName()))
            return 0;
        else if (StringUtils.isEmpty(backGround.getEmergencyContact()))
            return 0;
        else if (StringUtils.isEmpty(backGround.getAddress()))
            return 0;
        else if (StringUtils.isEmpty(backGround.getEducations()) || backGround.getEducations().isEmpty())
            return 0;
        else
            return 45;
    }

    private Integer checkSocialSecurityIntegrity(OrganizationMemberSocialSecurityDTO social) {
        if (StringUtils.isEmpty(social.getSalaryCardNumber()))
            return 0;
        else if (StringUtils.isEmpty(social.getSocialSecurityNumber()))
            return 0;
        else if (StringUtils.isEmpty(social.getProvidentFundNumber()))
            return 0;
        else if (StringUtils.isEmpty(social.getInsurances()) || social.getInsurances().isEmpty())
            return 0;
        else
            return 15;
    }

    private OrganizationMemberDetails getDetailFromOrganizationMember(OrganizationMember member) {
        return getDetailFromOrganizationMember(member, true, null);
    }

    private OrganizationMemberDetails getDetailFromOrganizationMember(OrganizationMember member, Boolean isCreate, OrganizationMemberDetails find_detail) {
        OrganizationMemberDetails detail = new OrganizationMemberDetails();
        java.util.Date nDate = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = sdf.format(nDate);
        java.sql.Date now = java.sql.Date.valueOf(sDate);

        if (isCreate && find_detail == null) {
            detail.setId(member.getDetailId() != null ? member.getDetailId() : 0L);
            detail.setNamespaceId(member.getNamespaceId() != null ? member.getNamespaceId() : 0);
            detail.setContactName(member.getContactName());
            detail.setContactToken(member.getContactToken());
            detail.setContactDescription(member.getContactDescription());
            detail.setEmployeeNo(member.getEmployeeNo());
            detail.setAvatar(member.getAvatar());
            detail.setGender(member.getGender());
            detail.setCheckInTime(member.getCheckInTime() != null ? member.getCheckInTime() : now);
            detail.setEmployeeStatus(member.getEmployeeStatus() != null ? member.getEmployeeStatus() : (byte) 0);
            detail.setEmploymentTime(member.getEmploymentTime() != null ? member.getEmploymentTime() : now);
            detail.setProfileIntegrity(member.getProfileIntegrity() != null ? member.getProfileIntegrity() : 0);
            detail.setEmployeeType(member.getEmployeeType());
            detail.setTargetType(member.getTargetType());
            detail.setTargetId(member.getTargetId());
        } else {
            detail = find_detail;
            detail.setContactName(member.getContactName());
            detail.setGender(member.getGender());
            detail.setEmployeeType(member.getEmployeeType());
            detail.setEmployeeNo(member.getEmployeeNo() != null ? member.getEmployeeNo() : "");
            detail.setCheckInTime(member.getCheckInTime());
        }
        return detail;
    }

    private void addProfileJobChangeLogs(Long detailId, String personChangeType, String tableName, String personChangeReason, Timestamp operationTime) {
        User user = UserContext.current().getUser();
        OrganizationMemberProfileLogs log = new OrganizationMemberProfileLogs();
        log.setDetailId(detailId);
        log.setNamespaceId(user.getNamespaceId());
        log.setOperatorUid(user.getId());
        log.setPersonChangeType(personChangeType);
        log.setPersonChangeReason(personChangeReason);
        log.setResourceType(tableName);

        //  若新增的时候根据入职日期来设置入职时间
        if (!StringUtils.isEmpty(operationTime))
            log.setOperationTime(operationTime);
        else
            log.setOperationTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        this.organizationProvider.createProfileLogs(log);
    }

    private void addProfileLogs(Long detailId, Integer namespaceId, String operationType, String tableName, String auditContent){
        OrganizationMemberProfileLogs log = new OrganizationMemberProfileLogs();
        log.setDetailId(detailId);
        log.setNamespaceId(namespaceId);
        log.setOperationType(operationType);
        log.setOperationTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        //  操作人未设置
        log.setResourceType(tableName);
        log.setAuditContent(auditContent);
        this.organizationProvider.createProfileLogs(log);
    }

    private List<OrganizationDTO> repeatCreateOrganizationmembers(List<Long> organizationIds, String contact_token, List<Long> enterpriseIds, OrganizationMember member){
        List<OrganizationDTO> results = new ArrayList<>();
        if (null != organizationIds) {
            removeRepeat(organizationIds);
            // 重新把成员添加到公司多个部门
            for (Long oId : organizationIds) {
                //排除掉上面已添加的公司机构成员
                if (!enterpriseIds.contains(oId)) {
                    Organization group = checkOrganization(oId);

                    member.setGroupPath(group.getPath());

                    member.setGroupType(group.getGroupType());

                    member.setOrganizationId(oId);

                    /**Modify BY lei.lv cause MemberDetail**/
                    if (OrganizationGroupType.ENTERPRISE != OrganizationGroupType.fromCode(group.getGroupType())) {
                        //找到部门对应的资料表记录
                        OrganizationMemberDetails old_detail = organizationProvider.findOrganizationMemberDetailsByOrganizationIdAndContactToken(group.getDirectlyEnterpriseId(), contact_token);
                        if (old_detail == null) {
                            LOGGER.error("Cannot find memberDetail of DirectlyEnterpriseId for this org。orgId={}", oId);
                            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ORG_TYPE,
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

    public ImportFileTaskDTO importOrganizationPersonnelFiles(MultipartFile mfile, Long userId, ImportOrganizationPersonnelDataCommand cmd) {
        ImportFileTask task = new ImportFileTask();
        try {
            //  解析excel
            List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());
            if (null == resultList || resultList.isEmpty()) {
                LOGGER.error("File content is empty。userId=" + userId);
                throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_FILE_IS_EMPTY,
                        "File content is empty");
            }
            task.setOwnerType(EntityType.ORGANIZATIONS.getCode());
            task.setOwnerId(cmd.getOrganizationId());
            task.setType(ImportFileTaskType.PERSONNEL_FILE.getCode());
            task.setCreatorUid(userId);
            task = importFileService.executeTask(new ExecuteImportTaskCallback() {
                @Override
                public ImportFileResponse importFile() {
                    ImportFileResponse response = new ImportFileResponse();
                    List<ImportOrganizationPersonnelFilesDTO> datas = handleImportOrganizationPersonnelFiles(resultList);
                    if (datas.size() > 0) {
                        //设置导出报错的结果excel的标题
                        response.setTitle(datas.get(0));
                        datas.remove(0);
                    }

                    List<ImportFileResultLog<ImportOrganizationPersonnelFilesDTO>> results = importOrganizationPersonnelFiles(datas, userId, cmd);
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

    private List<ImportOrganizationPersonnelFilesDTO> handleImportOrganizationPersonnelFiles(List list) {
        List<ImportOrganizationPersonnelFilesDTO> datas = new ArrayList<>();
        int row = 1;
        for (Object o : list) {
            if (row < 2) {
                row++;
                continue;
            }
            RowResult r = (RowResult) o;
            ImportOrganizationPersonnelFilesDTO data = new ImportOrganizationPersonnelFilesDTO();
            if (null != r.getCells().get("A"))
                data.setContactName(r.getCells().get("A").trim());
            if (null != r.getCells().get("B"))
                data.setGender(r.getCells().get("B").trim());
            if (null != r.getCells().get("C"))
                data.setContactToken(r.getCells().get("C").trim());
            if (null != r.getCells().get("D"))
                data.setOrgnaizationPath(r.getCells().get("D").trim());
            if (null != r.getCells().get("E"))
                data.setJobPosition(r.getCells().get("E").trim());
            if (null != r.getCells().get("F"))
                data.setCheckInTime(r.getCells().get("F").trim());
            if (null != r.getCells().get("G"))
                data.setEmployeeStatus(r.getCells().get("G").trim());
            if (null != r.getCells().get("H"))
                data.setEmploymentTime(r.getCells().get("H").trim());
            if (null != r.getCells().get("I"))
                data.setJobLevel(r.getCells().get("I").trim());
            if (null != r.getCells().get("J"))
                data.setEmployeeType(r.getCells().get("J").trim());
            if (null != r.getCells().get("K"))
                data.setEmployeeNo(r.getCells().get("K").trim());
            if (null != r.getCells().get("L"))
                data.setEnName(r.getCells().get("L").trim());
            if (null != r.getCells().get("M"))
                data.setBirthday(r.getCells().get("M").trim());
            if (null != r.getCells().get("N"))
                data.setMaritalFlag(r.getCells().get("N").trim());
            if (null != r.getCells().get("O"))
                data.setPoliticalStatus(r.getCells().get("O").trim());
            if (null != r.getCells().get("P"))
                data.setNativePlace(r.getCells().get("P").trim());
            if (null != r.getCells().get("Q"))
                data.setRegResidence(r.getCells().get("Q").trim());
            if (null != r.getCells().get("R"))
                data.setIdNumber(r.getCells().get("R").trim());
            if (null != r.getCells().get("S"))
                data.setEmail(r.getCells().get("S").trim());
            if (null != r.getCells().get("T"))
                data.setWechat(r.getCells().get("T").trim());
            if (null != r.getCells().get("U"))
                data.setQq(r.getCells().get("U").trim());
            if (null != r.getCells().get("V"))
                data.setEmergencyName(r.getCells().get("V").trim());
            if (null != r.getCells().get("W"))
                data.setEmergencyContact(r.getCells().get("W").trim());
            if (null != r.getCells().get("X"))
                data.setAddress(r.getCells().get("X").trim());
            if (null != r.getCells().get("Y"))
                data.setSchoolName(r.getCells().get("Y").trim());
            if (null != r.getCells().get("Z"))
                data.setDegree(r.getCells().get("Z").trim());
            if (null != r.getCells().get("AA"))
                data.setMajor(r.getCells().get("AA").trim());
            if (null != r.getCells().get("AB"))
                data.setEnrollmentTime(r.getCells().get("AB").trim());
            if (null != r.getCells().get("AC"))
                data.setGraduationTime(r.getCells().get("AC").trim());
            if (null != r.getCells().get("AD"))
                data.setEnterpriseName(r.getCells().get("AD").trim());
            if (null != r.getCells().get("AE"))
                data.setPosition(r.getCells().get("AE").trim());
            if (null != r.getCells().get("AF"))
                data.setJobType(r.getCells().get("AF").trim());
            if (null != r.getCells().get("AG"))
                data.setEntryTime(r.getCells().get("AG").trim());
            if (null != r.getCells().get("AH"))
                data.setDepartureTime(r.getCells().get("AH").trim());
            if (null != r.getCells().get("AI"))
                data.setSalaryCardNumber(r.getCells().get("AI").trim());
            if (null != r.getCells().get("AJ"))
                data.setSocialSecurityNumber(r.getCells().get("AJ").trim());
            if (null != r.getCells().get("AK"))
                data.setProvidentFundNumber(r.getCells().get("AK").trim());
            if (null != r.getCells().get("AL"))
                data.setInsuranceName(r.getCells().get("AL").trim());
            if (null != r.getCells().get("AM"))
                data.setInsuranceEnterprise(r.getCells().get("AM").trim());
            if (null != r.getCells().get("AN"))
                data.setInsuranceNumber(r.getCells().get("AN").trim());
            if (null != r.getCells().get("AO"))
                data.setInsuranceStartTime(r.getCells().get("AO").trim());
            if (null != r.getCells().get("AP"))
                data.setInsuranceEndTime(r.getCells().get("AP").trim());
            if (null != r.getCells().get("AQ"))
                data.setContractNumber(r.getCells().get("AQ").trim());
            if (null != r.getCells().get("AR"))
                data.setContractStartTime(r.getCells().get("AR").trim());
            if (null != r.getCells().get("AS"))
                data.setContractEndTime(r.getCells().get("AS").trim());

            datas.add(data);
        }
        return datas;
    }

    // update from importOrganizationPersonnel by R at 7th.June
    private List<ImportFileResultLog<ImportOrganizationPersonnelFilesDTO>> importOrganizationPersonnelFiles(List<ImportOrganizationPersonnelFilesDTO> list, Long userId, ImportOrganizationPersonnelDataCommand cmd) {
        List<ImportFileResultLog<ImportOrganizationPersonnelFilesDTO>> errorDataLogs = new ArrayList<>();
        ImportFileResultLog<ImportOrganizationPersonnelFilesDTO> log = new ImportFileResultLog<>(OrganizationServiceErrorCode.SCOPE);

        // 部门校验规则
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

        // 开始校验
        for (ImportOrganizationPersonnelFilesDTO data : list) {
            log = this.checkImportOrganizationMembers(data, deptMap, jobPositionMap, jobLevelMap, org, namespaceId);
            if (log != null) {
                errorDataLogs.add(log);
                continue;
            }
            log = this.checkImportOrganizationMemberDetails(data);
            if (log != null) {
                errorDataLogs.add(log);
                continue;
            }
            log = this.checkImportOrganizationMemberEducations(data);

            if (log != null) {
                errorDataLogs.add(log);
                continue;
            }
            log = this.checkImportOrganizationMemberWorkExperiences(data);
            if (log != null) {
                errorDataLogs.add(log);
                continue;
            }
            log = this.checkImportOrganizationMemberInsurances(data);
            if (log != null) {
                errorDataLogs.add(log);
                continue;
            }
            log = this.checkImportOrganizationMemberContracts(data);
            if (log != null) {
                errorDataLogs.add(log);
                continue;
            }
            log = this.checkImportDateFormat(data);
            if (log != null) {
                errorDataLogs.add(log);
                continue;
            }

            Long detailId = this.saveOrganizationMembers(data, cmd.getOrganizationId(), deptMap, jobPositionMap, jobLevelMap, org, namespaceId);

            if (detailId != null) {
                this.saveOrganizationMemberDetails(data, detailId);

                //  判断为空时则不需要添加
                if (this.checkEducationQualification(data).equals(1)) {
                    this.saveOrganizationMemberEducations(data, detailId);
                }
                if (this.checkWorkExperiencesQualification(data).equals(1)) {
                    this.saveOrganizationMemberWorkExperiences(data, detailId);
                }
                if (this.checkInsurancesQualification(data).equals(1)) {
                    this.saveOrganizationMemberInsurances(data, detailId);
                }
                if (this.checkContractsQualification(data).equals(1)) {
                    this.saveOrganizationMemberContracts(data, detailId);
                }

                //  计算档案完整度
                GetProfileIntegrityCommand integrity = new GetProfileIntegrityCommand();
                integrity.setDetailId(detailId);
                this.getProfileIntegrity(integrity);
            }
        }
        return errorDataLogs;
    }

    //  校验 member 基础信息
    private ImportFileResultLog<ImportOrganizationPersonnelFilesDTO> checkImportOrganizationMembers(
            ImportOrganizationPersonnelFilesDTO data, Map<String, Organization> deptMap,
            Map<String, Organization> jobPositionMap, Map<String, Organization> jobLevelMap,
            Organization org, int namespaceId) {

        ImportFileResultLog<ImportOrganizationPersonnelFilesDTO> log = new ImportFileResultLog<>(OrganizationServiceErrorCode.SCOPE);
        if (StringUtils.isEmpty(data.getContactName())) {
            LOGGER.warn("Organization member contactName is null. data = {}", data);
            log.setData(data);
            log.setErrorLog("Organization member contactName is null");
            log.setCode(OrganizationServiceErrorCode.ERROR_CONTACTNAME_ISNULL);
            return log;
        }

        if (StringUtils.isEmpty(data.getGender())) {
            LOGGER.warn("Organization member gender is null. data = {}", data);
            log.setData(data);
            log.setErrorLog("Organization member gender is null");
            log.setCode(OrganizationServiceErrorCode.ERROR_GENDER_ISNULL);
            return log;
        }

        if (StringUtils.isEmpty(data.getContactToken())) {
            LOGGER.warn("Organization member contactToken is null. data = {}", data);
            log.setData(data);
            log.setErrorLog("Organization member contactToken is null");
            log.setCode(OrganizationServiceErrorCode.ERROR_CONTACTTOKEN_ISNULL);
            return log;
        }else{
            if (!AccountValidatorUtil.isMobile(data.getContactToken())) {
                LOGGER.warn("Wrong contactToken format. data = {}", data);
                log.setData(data);
                log.setErrorLog("Wrong contactToken format");
                log.setCode(OrganizationServiceErrorCode.ERROR_CONTACTTOKEN_FORMAT);
                return log;
            }
        }

        if (!StringUtils.isEmpty(data.getOrgnaizationPath())) {
            String[] deptStrArr = data.getOrgnaizationPath().split(",");
            for (String deptName : deptStrArr) {
                Organization dept = deptMap.get(deptName.trim());
                if (null == dept) {
                    LOGGER.debug("Organization member department Non-existent. departmentName = {}", deptName);
                    log.setData(data);
                    log.setErrorLog("Organization member department Non-existent.");
                    log.setCode(OrganizationServiceErrorCode.ERROR_ORG_DEPARTMENT_NOT_EXIST);
                    return log;
                }
            }
        } else {
            LOGGER.warn("Organization member department is null. data = {}", data);
            log.setData(data);
            log.setErrorLog("Organization member department is null");
            log.setCode(OrganizationServiceErrorCode.ERROR_DEPARTMENT_ISNULL);
            return log;
        }

        if (!StringUtils.isEmpty(data.getJobPosition())) {
            String[] jobPositionStrArr = data.getJobPosition().split(",");
            for (String jobPositionName : jobPositionStrArr) {
                Organization jobPosition = jobPositionMap.get(jobPositionName.trim());
                if (null == jobPosition) {
                    LOGGER.debug("Organization member jobPosition Non-existent. jobPositionName = {}", jobPositionName);
                    log.setData(data);
                    log.setErrorLog("Organization member jobPosition Non-existent.");
                    log.setCode(OrganizationServiceErrorCode.ERROR_ORG_POSITION_NOT_EXIST);
                    return log;
                }
            }
        } else {
            LOGGER.warn("Organization member jobPosition is null. data = {}", data);
            log.setData(data);
            log.setErrorLog("Organization member jobPosition is null");
            log.setCode(OrganizationServiceErrorCode.ERROR_JOBPOSITION_ISNULL);
            return log;
        }

        if (StringUtils.isEmpty(data.getCheckInTime())) {
            LOGGER.warn("Organization member checkInTime is null. data = {}", data);
            log.setData(data);
            log.setErrorLog("Organization member checkInTime is null");
            log.setCode(OrganizationServiceErrorCode.ERROR_CHECKINTIME_ISNULL);
            return log;
        }

        if (!StringUtils.isEmpty(data.getEmployeeStatus())) {
            if (data.getEmployeeStatus().equals("否")) {
                if (StringUtils.isEmpty(data.getEmploymentTime())) {
                    LOGGER.warn("Organization member employeeTime is null. data = {}", data);
                    log.setData(data);
                    log.setErrorLog("Organization member employeeTime is null");
                    log.setCode(OrganizationServiceErrorCode.ERROR_EMPLOYEETIME_ISNULL);
                    return log;
                }
            }
        } else {
            LOGGER.warn("Organization member employeeStatus is null. data = {}", data);
            log.setData(data);
            log.setErrorLog("Organization member employeeStatus is null");
            log.setCode(OrganizationServiceErrorCode.ERROR_EMPLOYEESTATUS_ISNULL);
            return log;
        }

        if (!StringUtils.isEmpty(data.getJobLevel())) {
            String[] jobLevelStrArr = data.getJobLevel().split(",");
            for (String jobLevelName : jobLevelStrArr) {
                Organization jobLevel = jobLevelMap.get(jobLevelName);
                if (null == jobLevel) {
                    LOGGER.debug("Organization member jobLevel Non-existent. jobLevelName = {}", jobLevelName);
                    log.setData(data);
                    log.setErrorLog("Organization member jobLevel Non-existent.");
                    log.setCode(OrganizationServiceErrorCode.ERROR_ORG_LEVEL_NOT_EXIST);
                    return log;
                }
            }
        }

        VerifyPersonnelByPhoneCommand verifyCommand = new VerifyPersonnelByPhoneCommand();
        verifyCommand.setEnterpriseId(org.getId());
        verifyCommand.setNamespaceId(namespaceId);
        verifyCommand.setPhone(data.getContactToken());
        try {
            this.verifyPersonnelByPhone(verifyCommand);
        } catch (RuntimeErrorException e) {
            LOGGER.debug(e.getMessage());
            log.setData(data);
            log.setErrorLog(e.getMessage());
            log.setCode(e.getErrorCode());
            log.setScope(e.getErrorScope());
            return log;
        }

        return null;
    }

    private ImportFileResultLog<ImportOrganizationPersonnelFilesDTO> checkImportOrganizationMemberDetails(ImportOrganizationPersonnelFilesDTO data) {

        ImportFileResultLog<ImportOrganizationPersonnelFilesDTO> log = new ImportFileResultLog<>(OrganizationServiceErrorCode.SCOPE);
        if (!StringUtils.isEmpty(data.getEmergencyContact())) {
            if (!AccountValidatorUtil.isMobile(data.getEmergencyContact())) {
                LOGGER.warn("Wrong emergencyContact format. data = {}", data);
                log.setData(data);
                log.setErrorLog("Wrong emergencyContact format");
                log.setCode(OrganizationServiceErrorCode.ERROR_CONTACTTOKEN_FORMAT);
                return log;
            }
        }
        return null;

    }

    private ImportFileResultLog<ImportOrganizationPersonnelFilesDTO> checkImportOrganizationMemberEducations(ImportOrganizationPersonnelFilesDTO data) {

        ImportFileResultLog<ImportOrganizationPersonnelFilesDTO> log = new ImportFileResultLog<>(OrganizationServiceErrorCode.SCOPE);
        if (this.checkEducationQualification(data).equals(0)) {
            if (StringUtils.isEmpty(data.getSchoolName())) {
                LOGGER.warn("Organization member schoolName is null. data = {}", data);
                log.setData(data);
                log.setErrorLog("Organization member schoolName is null");
                log.setCode(OrganizationServiceErrorCode.ERROR_SCHOOLNAME_ISNULL);
                return log;
            } else if (StringUtils.isEmpty(data.getDegree())) {
                LOGGER.warn("Organization member degree is null. data = {}", data);
                log.setData(data);
                log.setErrorLog("Organization member degree is null");
                log.setCode(OrganizationServiceErrorCode.ERROR_DEGREE_ISNULL);
                return log;
            } else if (StringUtils.isEmpty(data.getMajor())) {
                LOGGER.warn("Organization member major is null. data = {}", data);
                log.setData(data);
                log.setErrorLog("Organization member major is null");
                log.setCode(OrganizationServiceErrorCode.ERROR_MAJOR_ISNULL);
                return log;
            } else if (StringUtils.isEmpty(data.getEnrollmentTime())) {
                LOGGER.warn("Organization member enrollmentTime is null. data = {}", data);
                log.setData(data);
                log.setErrorLog("Organization member enrollmentTime is null");
                log.setCode(OrganizationServiceErrorCode.ERROR_STARTTIME_ISNULL);
                return log;
            } else {
                LOGGER.warn("Organization member graduationTime is null. data = {}", data);
                log.setData(data);
                log.setErrorLog("Organization member graduationTime is null");
                log.setCode(OrganizationServiceErrorCode.ERROR_ENDTIME_ISNULL);
                return log;
            }
        } else {
            return null;
        }
    }

    private Integer checkEducationQualification(ImportOrganizationPersonnelFilesDTO data) {
        if (!StringUtils.isEmpty(data.getSchoolName()) && !StringUtils.isEmpty(data.getDegree())
                && !StringUtils.isEmpty(data.getMajor()) && !StringUtils.isEmpty(data.getEnrollmentTime())
                && !StringUtils.isEmpty(data.getGraduationTime())) {
            return 1;
        } else if (StringUtils.isEmpty(data.getSchoolName()) && StringUtils.isEmpty(data.getDegree())
                && StringUtils.isEmpty(data.getMajor()) && StringUtils.isEmpty(data.getEnrollmentTime())
                && StringUtils.isEmpty(data.getGraduationTime())) {
            return 2;
        } else
            return 0;
    }

    private ImportFileResultLog<ImportOrganizationPersonnelFilesDTO> checkImportOrganizationMemberWorkExperiences(ImportOrganizationPersonnelFilesDTO data) {

        ImportFileResultLog<ImportOrganizationPersonnelFilesDTO> log = new ImportFileResultLog<>(OrganizationServiceErrorCode.SCOPE);
        if (this.checkWorkExperiencesQualification(data).equals(0)) {
            if (StringUtils.isEmpty(data.getEnterpriseName())) {
                LOGGER.warn("Organization member enterpriseName is null. data = {}", data);
                log.setData(data);
                log.setErrorLog("Organization member enterpriseName is null");
                log.setCode(OrganizationServiceErrorCode.ERROR_ENTERPRISENAME_ISNULL);
                return log;
            } else if (StringUtils.isEmpty(data.getPosition())) {
                LOGGER.warn("Organization member position is null. data = {}", data);
                log.setData(data);
                log.setErrorLog("Organization member position is null");
                log.setCode(OrganizationServiceErrorCode.ERROR_POSITION_ISNULL);
                return log;
            } else if (StringUtils.isEmpty(data.getJobType())) {
                LOGGER.warn("Organization member jobType is null. data = {}", data);
                log.setData(data);
                log.setErrorLog("Organization member jobType is null");
                log.setCode(OrganizationServiceErrorCode.ERROR_JOBTYPE_ISNULL);
                return log;
            } else if (StringUtils.isEmpty(data.getEntryTime())) {
                LOGGER.warn("Organization member entryTime is null. data = {}", data);
                log.setData(data);
                log.setErrorLog("Organization member entryTime is null");
                log.setCode(OrganizationServiceErrorCode.ERROR_STARTTIME_ISNULL);
                return log;
            } else {
                LOGGER.warn("Organization member departureTime is null. data = {}", data);
                log.setData(data);
                log.setErrorLog("Organization member departureTime is null");
                log.setCode(OrganizationServiceErrorCode.ERROR_ENDTIME_ISNULL);
                return log;
            }
        } else {
            return null;
        }
    }

    private Integer checkWorkExperiencesQualification(ImportOrganizationPersonnelFilesDTO data) {
        if (!StringUtils.isEmpty(data.getEnterpriseName()) && !StringUtils.isEmpty(data.getPosition())
                && !StringUtils.isEmpty(data.getJobType()) && !StringUtils.isEmpty(data.getEntryTime())
                && !StringUtils.isEmpty(data.getDepartureTime())) {
            return 1;
        } else if (StringUtils.isEmpty(data.getEnterpriseName()) && StringUtils.isEmpty(data.getPosition())
                && StringUtils.isEmpty(data.getJobType()) && StringUtils.isEmpty(data.getEntryTime())
                && StringUtils.isEmpty(data.getDepartureTime())) {
            return 2;
        } else {
            return 0;
        }
    }

    private ImportFileResultLog<ImportOrganizationPersonnelFilesDTO> checkImportOrganizationMemberInsurances(ImportOrganizationPersonnelFilesDTO data) {

        ImportFileResultLog<ImportOrganizationPersonnelFilesDTO> log = new ImportFileResultLog<>(OrganizationServiceErrorCode.SCOPE);
        if (this.checkInsurancesQualification(data).equals(0)) {
            if (StringUtils.isEmpty(data.getInsuranceName())) {
                LOGGER.warn("Organization member insuranceName is null. data = {}", data);
                log.setData(data);
                log.setErrorLog("Organization member insuranceName is null");
                log.setCode(OrganizationServiceErrorCode.ERROR_INSURANCENAME_ISNULL);
                return log;
            } else if (StringUtils.isEmpty(data.getInsuranceEnterprise())) {
                LOGGER.warn("Organization member insuranceEnterprise is null. data = {}", data);
                log.setData(data);
                log.setErrorLog("Organization member insuranceEnterprise is null");
                log.setCode(OrganizationServiceErrorCode.ERROR_INSURANCEENTERPRISE_ISNULL);
                return log;
            } else if (StringUtils.isEmpty(data.getInsuranceNumber())) {
                LOGGER.warn("Organization member insuranceNumber is null. data = {}", data);
                log.setData(data);
                log.setErrorLog("Organization member insuranceNumber is null");
                log.setCode(OrganizationServiceErrorCode.ERROR_INSURANCENUMBER_ISNULL);
                return log;
            } else if (StringUtils.isEmpty(data.getInsuranceStartTime())) {
                LOGGER.warn("Organization member insuranceStartTime is null. data = {}", data);
                log.setData(data);
                log.setErrorLog("Organization member insuranceStartTime is null");
                log.setCode(OrganizationServiceErrorCode.ERROR_STARTTIME_ISNULL);
                return log;
            } else {
                LOGGER.warn("Organization member insuranceEndTime is null. data = {}", data);
                log.setData(data);
                log.setErrorLog("Organization member insuranceEndTime is null");
                log.setCode(OrganizationServiceErrorCode.ERROR_ENDTIME_ISNULL);
                return log;
            }
        } else {
            return null;
        }
    }

    private Integer checkInsurancesQualification(ImportOrganizationPersonnelFilesDTO data) {
        if (!StringUtils.isEmpty(data.getInsuranceName()) && !StringUtils.isEmpty(data.getInsuranceEnterprise())
                && !StringUtils.isEmpty(data.getInsuranceNumber()) && !StringUtils.isEmpty(data.getInsuranceStartTime())
                && !StringUtils.isEmpty(data.getInsuranceEndTime())) {
            return 1;
        } else if (StringUtils.isEmpty(data.getInsuranceName()) && StringUtils.isEmpty(data.getInsuranceEnterprise())
                && StringUtils.isEmpty(data.getInsuranceNumber()) && StringUtils.isEmpty(data.getInsuranceStartTime())
                && StringUtils.isEmpty(data.getInsuranceEndTime())) {
            return 2;
        } else {
            return 0;
        }
    }

    private ImportFileResultLog<ImportOrganizationPersonnelFilesDTO> checkImportOrganizationMemberContracts(ImportOrganizationPersonnelFilesDTO data) {

        ImportFileResultLog<ImportOrganizationPersonnelFilesDTO> log = new ImportFileResultLog<>(OrganizationServiceErrorCode.SCOPE);
        if (this.checkContractsQualification(data).equals(0)) {
            if (StringUtils.isEmpty(data.getContractNumber())) {
                LOGGER.warn("Organization member contractNumber is null. data = {}", data);
                log.setData(data);
                log.setErrorLog("Organization member contractNumber is null");
                log.setCode(OrganizationServiceErrorCode.ERROR_CONTRACTNUMBER_ISNULL);
                return log;
            } else if (StringUtils.isEmpty(data.getContractStartTime())) {
                LOGGER.warn("Organization member contractStartTime is null. data = {}", data);
                log.setData(data);
                log.setErrorLog("Organization member contractStartTime is null");
                log.setCode(OrganizationServiceErrorCode.ERROR_STARTTIME_ISNULL);
                return log;
            } else {
                LOGGER.warn("Organization member contractEndTime is null. data = {}", data);
                log.setData(data);
                log.setErrorLog("Organization member contractEndTime is null");
                log.setCode(OrganizationServiceErrorCode.ERROR_ENDTIME_ISNULL);
                return log;
            }
        } else {
            return null;
        }
    }

    private Integer checkContractsQualification(ImportOrganizationPersonnelFilesDTO data) {
        if (!StringUtils.isEmpty(data.getContractNumber()) && !StringUtils.isEmpty(data.getContractStartTime())
                && !StringUtils.isEmpty(data.getContractEndTime())) {
            return 1;
        } else if (StringUtils.isEmpty(data.getContractNumber()) && StringUtils.isEmpty(data.getContractStartTime())
                && StringUtils.isEmpty(data.getContractEndTime())) {
            return 2;
        } else {
            return 0;
        }
    }

    private ImportFileResultLog<ImportOrganizationPersonnelFilesDTO> checkImportDateFormat(ImportOrganizationPersonnelFilesDTO data) {

        ImportFileResultLog<ImportOrganizationPersonnelFilesDTO> log = new ImportFileResultLog<>(OrganizationServiceErrorCode.SCOPE);
        try {
            java.sql.Date.valueOf(data.getCheckInTime());
            if (!StringUtils.isEmpty(data.getEmploymentTime()))
                java.sql.Date.valueOf(data.getEmploymentTime());
            if (!StringUtils.isEmpty(data.getBirthday()))
                java.sql.Date.valueOf(data.getBirthday());
            if (!StringUtils.isEmpty(data.getEnrollmentTime()))
                java.sql.Date.valueOf(data.getEnrollmentTime());
            if (!StringUtils.isEmpty(data.getGraduationTime()))
                java.sql.Date.valueOf(data.getGraduationTime());
            if (!StringUtils.isEmpty(data.getEntryTime()))
                java.sql.Date.valueOf(data.getEntryTime());
            if (!StringUtils.isEmpty(data.getDepartureTime()))
                java.sql.Date.valueOf(data.getDepartureTime());
            if (!StringUtils.isEmpty(data.getInsuranceStartTime()))
                java.sql.Date.valueOf(data.getInsuranceStartTime());
            if (!StringUtils.isEmpty(data.getInsuranceEndTime()))
                java.sql.Date.valueOf(data.getInsuranceEndTime());
            if (!StringUtils.isEmpty(data.getContractStartTime()))
                java.sql.Date.valueOf(data.getContractStartTime());
            if (!StringUtils.isEmpty(data.getContractEndTime()))
                java.sql.Date.valueOf(data.getContractEndTime());
        } catch (Exception e) {
            LOGGER.warn("Organization member date format error. data = {}", data);
            log.setData(data);
            log.setErrorLog("Organization member date format error");
            log.setCode(OrganizationServiceErrorCode.ERROR_DATE_FORMAT_WRONG);
            return log;
        }
        return null;
    }

    private Long saveOrganizationMembers(
            ImportOrganizationPersonnelFilesDTO data, Long organizationId,
            Map<String, Organization> deptMap, Map<String, Organization> jobPositionMap,
            Map<String, Organization> jobLevelMap, Organization org,
            int namespaceId) {
        AddOrganizationPersonnelV2Command memberCommand = new AddOrganizationPersonnelV2Command();

        //  公司
        memberCommand.setOrganizationId(organizationId);

        //  联系号码
        memberCommand.setContactToken(data.getContactToken());

        //  姓名
        memberCommand.setContactName(data.getContactName());

        //  性别
        Byte gender;
        if (data.getGender().trim().equals("男")) {
            gender = 1;
        } else{
            gender = 2;
        }
        memberCommand.setGender(gender);

        //  获取部门
        String[] deptStrArr = data.getOrgnaizationPath().split(",");
        List<Long> departmentIds = new ArrayList<>();
        for (String deptName : deptStrArr) {
            Organization dept = deptMap.get(deptName.trim());
            departmentIds.add(dept.getId());
        }
        memberCommand.setDepartmentIds(departmentIds);

        //  获取岗位
        String[] jobPositionStrArr = data.getJobPosition().split(",");
        List<Long> jobPositionIds = new ArrayList<>();
        for (String jobPositionName : jobPositionStrArr) {
            Organization jobPosition = jobPositionMap.get(jobPositionName.trim());
            jobPositionIds.add(jobPosition.getId());
        }
        memberCommand.setJobPositionIds(jobPositionIds);

        //  入职日期
        memberCommand.setCheckInTime(data.getCheckInTime());

        //  试用期
        Byte employeeStatus;
        if(data.getEmployeeStatus().equals("是")){
            employeeStatus = 0;
        }else{
            employeeStatus = 1;
        }
        memberCommand.setEmployeeStatus(employeeStatus);

        //  转正日期
        if(!StringUtils.isEmpty(data.getEmploymentTime()))
            memberCommand.setEmploymentTime(data.getEmploymentTime());

        //  职级
        if (!StringUtils.isEmpty(data.getJobLevel())) {
            String[] jobLevelStrArr = data.getJobLevel().split(",");
            List<Long> jobLevelIds = new ArrayList<>();
            for (String jobLevelName : jobLevelStrArr) {
                Organization jobLevel = jobLevelMap.get(jobLevelName);
                jobLevelIds.add(jobLevel.getId());
            }
            memberCommand.setJobLevelIds(jobLevelIds);
        }

        //  员工类型
        Byte employeeType;
        if(!StringUtils.isEmpty(data.getEmployeeType())){
            if(data.getEmployeeType().equals("兼职")){
                employeeType = 1;
            }else if(data.getEmployeeType().equals("实习")){
                employeeType = 2;
            }else if(data.getEmployeeType().equals("劳动派遣")){
                employeeType = 3;
            }else{
                employeeType = 0;
            }
            memberCommand.setEmployeeType(employeeType);
        }
        if(!StringUtils.isEmpty(data.getEmployeeNo()))
            memberCommand.setEmployeeNo(data.getEmployeeNo());

        //  查找手机号
        VerifyPersonnelByPhoneCommand verifyCommand = new VerifyPersonnelByPhoneCommand();
        verifyCommand.setEnterpriseId(org.getId());
        verifyCommand.setNamespaceId(namespaceId);
        verifyCommand.setPhone(memberCommand.getContactToken());
        VerifyPersonnelByPhoneCommandResponse verifyRes = this.verifyPersonnelByPhone(verifyCommand);

        //  设置目标
        if (null != verifyRes && null != verifyRes.getDto()) {
            memberCommand.setTargetId(verifyRes.getDto().getTargetId());
            memberCommand.setTargetType(verifyRes.getDto().getTargetType());
        }

        //  新增人员并返回detailId
        OrganizationMemberDTO member = this.addOrganizationPersonnelV2(memberCommand);
        return member.getDetailId();
    }

    private void saveOrganizationMemberDetails(ImportOrganizationPersonnelFilesDTO data, Long detailId){

        UpdateOrganizationMemberBackGroundCommand member = new UpdateOrganizationMemberBackGroundCommand();
        //  员工标识号
        member.setDetailId(detailId);
        member.setEnName(data.getEnName());
        member.setBirthday(data.getBirthday());
        //  婚姻状态
        if(data.getMaritalFlag().equals("已婚")){
            member.setMaritalFlag(MaritalFlag.MARRIED.getCode());
        }else
            member.setMaritalFlag(MaritalFlag.UNMARRIED.getCode());
        member.setPoliticalStatus(data.getPoliticalStatus());
        member.setNativePlace(data.getNativePlace());
        member.setRegResidence(data.getRegResidence());
        member.setIdNumber(data.getIdNumber());
        member.setEmail(data.getEmail());
        member.setWechat(data.getWechat());
        member.setQq(data.getQq());
        member.setEmergencyName(data.getEmergencyName());
        member.setEmergencyContact(data.getEmergencyContact());
        member.setAddress(data.getAddress());
        //  工资卡号
        member.setSalaryCardNumber(data.getSalaryCardNumber());
        //  公积金卡号
        member.setSocialSecurityNumber(data.getSocialSecurityNumber());
        //  社保卡号
        member.setProvidentFundNumber(data.getProvidentFundNumber());

        this.updateOrganizationMemberBackGround(member);
    }
    private void saveOrganizationMemberEducations(ImportOrganizationPersonnelFilesDTO data, Long detailId){

        AddOrganizationMemberEducationsCommand education = new AddOrganizationMemberEducationsCommand();
        //  员工标识号
        education.setDetailId(detailId);
        education.setSchoolName(data.getSchoolName());
        education.setDegree(data.getDegree());
        education.setMajor(data.getMajor());
        education.setEnrollmentTime(data.getEnrollmentTime());
        education.setGraduationTime(data.getGraduationTime());

        this.addOrganizationMemberEducations(education);
    }
    private void saveOrganizationMemberWorkExperiences(ImportOrganizationPersonnelFilesDTO data, Long detailId){

        AddOrganizationMemberWorkExperiencesCommand workExperiences = new AddOrganizationMemberWorkExperiencesCommand();

        //  员工标识号
        workExperiences.setDetailId(detailId);
        workExperiences.setEnterpriseName(data.getEnterpriseName());
        workExperiences.setPosition(data.getPosition());
        //  工作类型
        Byte jobType;
        if(!StringUtils.isEmpty(data.getJobType())){
            if(data.getEmployeeType().equals("兼职")){
                jobType = 1;
            }else if(data.getEmployeeType().equals("实习")){
                jobType = 2;
            }else if(data.getEmployeeType().equals("劳动派遣")){
                jobType = 3;
            }else{
                jobType = 0;
            }
            workExperiences.setJobType(jobType);
        }
        workExperiences.setEntryTime(data.getEntryTime());
        workExperiences.setDepartureTime(data.getDepartureTime());

        this.addOrganizationMemberWorkExperiences(workExperiences);
    }
    private void saveOrganizationMemberInsurances(ImportOrganizationPersonnelFilesDTO data, Long detailId){

        AddOrganizationMemberInsurancesCommand insuraces = new AddOrganizationMemberInsurancesCommand();

        //  员工标识号
        insuraces.setDetailId(detailId);
        insuraces.setName(data.getInsuranceName());
        insuraces.setEnterprise(data.getInsuranceEnterprise());
        insuraces.setNumber(data.getContractNumber());
        insuraces.setStartTime(data.getInsuranceStartTime());
        insuraces.setEndTime(data.getInsuranceEndTime());

        this.addOrganizationMemberInsurances(insuraces);
    }
    private void saveOrganizationMemberContracts(ImportOrganizationPersonnelFilesDTO data, Long detailId){

        AddOrganizationMemberContractsCommand contracts = new AddOrganizationMemberContractsCommand();

        //  员工标识号
        contracts.setDetailId(detailId);
        contracts.setContractNumber(data.getContractNumber());
        contracts.setStartTime(data.getContractStartTime());
        contracts.setEndTime(data.getContractEndTime());

        this.addOrganizationMemberContracts(contracts);
    }

    public void exportOrganizationPersonnelFiles(
            ExcelOrganizationPersonnelCommand cmd,
            HttpServletResponse httpResponse){

        ListPersonnelsV2Command command =new ListPersonnelsV2Command();
        command.setKeywords(cmd.getKeywords());
        command.setOrganizationId(cmd.getOrganizationId());
        command.setPageSize(100000);
        ListPersonnelsV2CommandResponse response = this.listOrganizationPersonnelsV2(command);
        List<OrganizationMemberV2DTO> memberDTOs =response.getMembers();
        ByteArrayOutputStream out = null;
        XSSFWorkbook wb = this.createXSSFPersonnelFiles(memberDTOs);
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

    private XSSFWorkbook createXSSFPersonnelFiles(List<OrganizationMemberV2DTO> members) {
        XSSFWorkbook wb = new XSSFWorkbook();
        String sheetName = "通讯录";
        XSSFSheet sheet = wb.createSheet(sheetName);
        XSSFCellStyle style = wb.createCellStyle();// 样式对象
        XSSFDataFormat format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("yyyy-MM-dd"));
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 20);
        font.setFontName("Courier New");

        style.setFont(font);

        XSSFCellStyle titleStyle = wb.createCellStyle();// 样式对象
        titleStyle.setFont(font);
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        int rowNum = 0;

        XSSFRow rowStart = sheet.createRow(rowNum++);
        rowStart.setRowStyle(style);

        XSSFRow row1 = sheet.createRow(rowNum++);
//        row1.setRowStyle(style);
        //  创建标题
        this.createXSSFPersonnelFileTitle(row1);

        for (OrganizationMemberV2DTO member : members) {

            //	若无 detailId 说明为非法数据
            if(StringUtils.isEmpty(member.getDetailId()))
                continue;

            XSSFRow row = sheet.createRow(rowNum++);

            //  写入基本信息
            this.createXSSFPersonnelFileMember(row, member);
            //  开始写入 detail 信息
            this.createXSSFPersonnelFileMemberDetails(row, member.getDetailId());
            //  导出教育信息
            this.createXSSFPersonnelFileMemberEducation(row, member.getDetailId());
            //  导出工作经历
            this.createXSSFPersonnelFileMemberWorkExperience(row, member.getDetailId());
            // 导出保险信息
            this.createXSSFPersonnelFileMemberInsurance(row, member.getDetailId());
            //  导出合同信息
            this.createXSSFPersonnelFileMemberContract(row, member.getDetailId());
        }
        return wb;
    }

    private void createXSSFPersonnelFileTitle(XSSFRow row1) {
        row1.createCell(0).setCellValue("姓名");
        row1.createCell(1).setCellValue("性别");
        row1.createCell(2).setCellValue("手机号");
        row1.createCell(3).setCellValue("所属部门");
        row1.createCell(4).setCellValue("岗位");
        row1.createCell(5).setCellValue("入职日期");
        row1.createCell(6).setCellValue("试用期");
        row1.createCell(7).setCellValue("转正日期");
        row1.createCell(8).setCellValue("职级");
        row1.createCell(9).setCellValue("员工类型");
        row1.createCell(10).setCellValue("员工编号");
        row1.createCell(11).setCellValue("英文名");
        row1.createCell(12).setCellValue("出生日期");
        row1.createCell(13).setCellValue("婚姻状态");
        row1.createCell(14).setCellValue("政治面貌");
        row1.createCell(15).setCellValue("籍贯");
        row1.createCell(16).setCellValue("户口");
        row1.createCell(17).setCellValue("身份证");
        row1.createCell(18).setCellValue("邮箱");
        row1.createCell(19).setCellValue("微信");
        row1.createCell(20).setCellValue("QQ");
        row1.createCell(21).setCellValue("紧急联系人");
        row1.createCell(22).setCellValue("紧急联系电话");
        row1.createCell(23).setCellValue("住址");
        row1.createCell(24).setCellValue("学校名称");
        row1.createCell(25).setCellValue("学历");
        row1.createCell(26).setCellValue("专业");
        row1.createCell(27).setCellValue("入学时间");
        row1.createCell(28).setCellValue("毕业时间");
        row1.createCell(29).setCellValue("公司名称");
        row1.createCell(30).setCellValue("职位");
        row1.createCell(31).setCellValue("类型");
        row1.createCell(32).setCellValue("加入时间");
        row1.createCell(33).setCellValue("离开时间");
        row1.createCell(34).setCellValue("工资卡号");
        row1.createCell(35).setCellValue("社保电脑号");
        row1.createCell(36).setCellValue("公积金卡号");
        row1.createCell(37).setCellValue("保险名称");
        row1.createCell(38).setCellValue("保险公司");
        row1.createCell(39).setCellValue("保单编号");
        row1.createCell(40).setCellValue("生效时间");
        row1.createCell(41).setCellValue("到期时间");
        row1.createCell(42).setCellValue("劳动合同编号");
        row1.createCell(43).setCellValue("生效时间");
        row1.createCell(44).setCellValue("到期时间");
    }

    private void createXSSFPersonnelFileMember(XSSFRow row, OrganizationMemberV2DTO member) {
        //  写入初始信息
        row.createCell(0).setCellValue(member.getContactName());
        row.createCell(1).setCellValue(null == member.getGender() ? "" : member.getGender() == 1 ? "男" : "女");
        row.createCell(2).setCellValue(member.getContactToken());

        //  岗位
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
        row.createCell(3).setCellValue(departmentStr);

        //  部门
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
        row.createCell(4).setCellValue(jobPositionStr);

        row.createCell(5).setCellValue(String.valueOf(member.getCheckInTime()));
        row.createCell(6).setCellValue(member.getEmployeeStatus().equals(EmployeeStatus.PROBATION.getCode()) ? "是" : "否");
        row.createCell(7).setCellValue(String.valueOf(member.getEmploymentTime()));

        //  职级
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
        row.createCell(8).setCellValue(jobLevelStr);
        row.createCell(10).setCellValue(StringUtils.isEmpty(member.getEmployeeNo()) ? "" : member.getEmployeeNo());
    }

    private void createXSSFPersonnelFileMemberDetails(XSSFRow row, Long detialId) {
        OrganizationMemberDetails memberDetails = this.organizationProvider.findOrganizationMemberDetailsByDetailId(detialId);
        if (memberDetails != null) {
            if (!StringUtils.isEmpty(memberDetails.getEmployeeType())) {
                String employeeType = "";
                if (memberDetails.getEmployeeType().equals(EmployeeType.FULLTIME.getCode()))
                    employeeType = "全职";
                else if (memberDetails.getEmployeeType().equals(EmployeeType.PARTTIME.getCode()))
                    employeeType = "兼职";
                else if (memberDetails.getEmployeeType().equals(EmployeeType.INTERSHIP.getCode()))
                    employeeType = "实习";
                else if (memberDetails.getEmployeeType().equals(EmployeeType.LABORDISPATCH.getCode()))
                    employeeType = "劳动派遣";
                row.createCell(9).setCellValue(employeeType);
            } else
                row.createCell(9).setCellValue("");
            row.createCell(11).setCellValue(StringUtils.isEmpty(memberDetails.getEnName()) ? "" : memberDetails.getEnName());
            row.createCell(12).setCellValue(StringUtils.isEmpty(memberDetails.getBirthday()) ? "" : String.valueOf(memberDetails.getBirthday()));
            row.createCell(13).setCellValue(memberDetails.getMaritalFlag().equals(MaritalFlag.MARRIED.getCode()) ? "已婚" : "未婚");
            row.createCell(14).setCellValue(StringUtils.isEmpty(memberDetails.getPoliticalStatus()) ? "" : memberDetails.getPoliticalStatus());
            row.createCell(15).setCellValue(StringUtils.isEmpty(memberDetails.getNativePlace()) ? "" : memberDetails.getNativePlace());
            row.createCell(16).setCellValue(StringUtils.isEmpty(memberDetails.getRegResidence()) ? "" : memberDetails.getRegResidence());
            row.createCell(17).setCellValue(StringUtils.isEmpty(memberDetails.getIdNumber()) ? "" : memberDetails.getIdNumber());
            row.createCell(18).setCellValue(StringUtils.isEmpty(memberDetails.getEmail()) ? "" : memberDetails.getEmail());
            row.createCell(19).setCellValue(StringUtils.isEmpty(memberDetails.getWechat()) ? "" : memberDetails.getWechat());
            row.createCell(20).setCellValue(StringUtils.isEmpty(memberDetails.getQq()) ? "" : memberDetails.getQq());
            row.createCell(21).setCellValue(StringUtils.isEmpty(memberDetails.getEmergencyName()) ? "" : memberDetails.getEmergencyName());
            row.createCell(22).setCellValue(StringUtils.isEmpty(memberDetails.getEmergencyContact()) ? "" : memberDetails.getEmergencyContact());
            row.createCell(23).setCellValue(StringUtils.isEmpty(memberDetails.getAddress()) ? "" : memberDetails.getAddress());
            row.createCell(34).setCellValue(StringUtils.isEmpty(memberDetails.getSalaryCardNumber()) ? "" : memberDetails.getSalaryCardNumber());
            row.createCell(35).setCellValue(StringUtils.isEmpty(memberDetails.getSocialSecurityNumber()) ? "" : memberDetails.getSocialSecurityNumber());
            row.createCell(36).setCellValue(StringUtils.isEmpty(memberDetails.getProvidentFundNumber()) ? "" : memberDetails.getProvidentFundNumber());
        } else {
            row.createCell(9).setCellValue("");
            row.createCell(11).setCellValue("");
            row.createCell(12).setCellValue("");
            row.createCell(13).setCellValue("");
            row.createCell(14).setCellValue("");
            row.createCell(15).setCellValue("");
            row.createCell(16).setCellValue("");
            row.createCell(17).setCellValue("");
            row.createCell(18).setCellValue("");
            row.createCell(19).setCellValue("");
            row.createCell(20).setCellValue("");
            row.createCell(21).setCellValue("");
            row.createCell(22).setCellValue("");
            row.createCell(23).setCellValue("");
            row.createCell(34).setCellValue("");
            row.createCell(35).setCellValue("");
            row.createCell(36).setCellValue("");
        }
    }

    private void createXSSFPersonnelFileMemberEducation(XSSFRow row, Long detailId) {
        List<OrganizationMemberEducations> educations = this.organizationProvider.listOrganizationMemberEducations(detailId);
        if (educations != null && educations.size() > 0) {
            OrganizationMemberEducations education = educations.get(0);
            row.createCell(24).setCellValue(education.getSchoolName());
            row.createCell(25).setCellValue(education.getDegree());
            row.createCell(26).setCellValue(education.getMajor());
            row.createCell(27).setCellValue(String.valueOf(education.getEnrollmentTime()));
            row.createCell(28).setCellValue(String.valueOf(education.getGraduationTime()));
        } else {
            row.createCell(24).setCellValue("");
            row.createCell(25).setCellValue("");
            row.createCell(26).setCellValue("");
            row.createCell(27).setCellValue("");
            row.createCell(28).setCellValue("");
        }
    }

    private void createXSSFPersonnelFileMemberWorkExperience(XSSFRow row, Long detailId) {
        List<OrganizationMemberWorkExperiences> workExperiences = this.organizationProvider.listOrganizationMemberWorkExperiences(detailId);
        if (workExperiences != null && workExperiences.size() > 0) {
            OrganizationMemberWorkExperiences workExperience = workExperiences.get(0);
            row.createCell(29).setCellValue(workExperience.getEnterpriseName());
            row.createCell(30).setCellValue(workExperience.getPosition());
            String jobType = "";
            if (workExperience.getJobType().equals(EmployeeType.FULLTIME.getCode()))
                jobType = "全职";
            else if (workExperience.getJobType().equals(EmployeeType.PARTTIME.getCode()))
                jobType = "兼职";
            else if (workExperience.getJobType().equals(EmployeeType.INTERSHIP.getCode()))
                jobType = "实习";
            else if (workExperience.getJobType().equals(EmployeeType.LABORDISPATCH.getCode()))
                jobType = "劳动派遣";
            row.createCell(31).setCellValue(jobType);
            row.createCell(32).setCellValue(String.valueOf(workExperience.getEntryTime()));
            row.createCell(33).setCellValue(String.valueOf(workExperience.getDepartureTime()));
        } else {
            row.createCell(29).setCellValue("");
            row.createCell(30).setCellValue("");
            row.createCell(31).setCellValue("");
            row.createCell(32).setCellValue("");
            row.createCell(33).setCellValue("");
        }
    }

    private void createXSSFPersonnelFileMemberInsurance(XSSFRow row, Long detailId) {
        List<OrganizationMemberInsurances> insurances = this.organizationProvider.listOrganizationMemberInsurances(detailId);
        if (insurances != null && insurances.size() > 0) {
            OrganizationMemberInsurances insurance = insurances.get(0);
            row.createCell(37).setCellValue(insurance.getName());
            row.createCell(38).setCellValue(insurance.getEnterprise());
            row.createCell(39).setCellValue(insurance.getNumber());
            row.createCell(40).setCellValue(String.valueOf(insurance.getStartTime()));
            row.createCell(41).setCellValue(String.valueOf(insurance.getEndTime()));
        } else {
            row.createCell(37).setCellValue("");
            row.createCell(38).setCellValue("");
            row.createCell(39).setCellValue("");
            row.createCell(40).setCellValue("");
            row.createCell(41).setCellValue("");
        }
    }

    private void createXSSFPersonnelFileMemberContract(XSSFRow row, Long detailId) {
        List<OrganizationMemberContracts> contracts = this.organizationProvider.listOrganizationMemberContracts(detailId);
        if (contracts != null && contracts.size() > 0) {
            OrganizationMemberContracts contract = contracts.get(0);
            row.createCell(42).setCellValue(contract.getContractNumber());
            row.createCell(43).setCellValue(String.valueOf(contract.getStartTime()));
            row.createCell(44).setCellValue(String.valueOf(contract.getEndTime()));
        } else {
            row.createCell(42).setCellValue("");
            row.createCell(43).setCellValue("");
            row.createCell(44).setCellValue("");
        }
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

    /**
     * 检查是否有匹配的detail记录，如有则更新，若无则创建，并返回最终的detailId
     * @param organizationMember
     * @param organizationId
     * @return
     */
    private Long getEnableDetailOfOrganizationMember(OrganizationMember organizationMember, Long organizationId){

        //更新或创建detail记录
        OrganizationMemberDetails old_detail = organizationProvider.findOrganizationMemberDetailsByOrganizationIdAndContactToken(organizationId, organizationMember.getContactToken());
        Long new_detail_id = 0L;
        if (old_detail == null) { /**如果档案表中无记录**/
            OrganizationMemberDetails organizationMemberDetail = getDetailFromOrganizationMember(organizationMember, true, null);
            organizationMemberDetail.setOrganizationId(organizationId);
            new_detail_id = organizationProvider.createOrganizationMemberDetails(organizationMemberDetail);
        } else { /**如果档案表中有记录**/
            OrganizationMemberDetails organizationMemberDetail = getDetailFromOrganizationMember(organizationMember, false, old_detail);
            organizationMemberDetail.setOrganizationId(organizationId);
            organizationProvider.updateOrganizationMemberDetails(organizationMemberDetail, organizationMemberDetail.getId());
            new_detail_id = organizationMemberDetail.getId();
        }
        return new_detail_id;
    }

    /**
     * 创建member对应的organizationMember记录（如果已存在，则重新赋值后更新）
     * @param organizationMember
     * @return
     */
    private UserOrganizations createOrUpdateUserOrganization(OrganizationMember organizationMember){
        //根据namespaceId、organizationId、userId（userIdentifier.getOwnerUid()）来判断唯一记录
        UserOrganizations userOrganizations = userOrganizationProvider.findUserOrganizations(organizationMember.getNamespaceId(), organizationMember.getOrganizationId(), organizationMember.getTargetId());

        if(userOrganizations == null){
            userOrganizations = new UserOrganizations();
            userOrganizations.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            commonSetUserOrganization(userOrganizations,organizationMember);
            this.userOrganizationProvider.createUserOrganizations(userOrganizations);
        }else{
            userOrganizations.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            commonSetUserOrganization(userOrganizations,organizationMember);
            this.userOrganizationProvider.updateUserOrganizations(userOrganizations);
        }
        return userOrganizations;
    }

    /**
     * userOrganiztion批量赋值方法
     * @param userOrganizations
     * @param organizationMember
     */
    private void commonSetUserOrganization(UserOrganizations userOrganizations, OrganizationMember organizationMember){
        userOrganizations.setUserId(organizationMember.getTargetId());
        userOrganizations.setOrganizationId(organizationMember.getOrganizationId());
        userOrganizations.setGroupPath(organizationMember.getGroupPath());
        userOrganizations.setGroupType(organizationMember.getGroupType());
        userOrganizations.setStatus(UserOrganizationStatus.ACTIVE.getCode());
        userOrganizations.setNamespaceId(organizationMember.getNamespaceId());
        userOrganizations.setVisibleFlag(organizationMember.getVisibleFlag());
    }

    /**
     * 根据members退出删除user_organiztion表中的信息
     *
     * @param members
     */
    private void deleteUserOrganizationWithMembers(List<OrganizationMember> members) {
        dbProvider.execute((TransactionStatus status) -> {
            for (OrganizationMember member : members) {
                UserOrganizations userOrganization = userOrganizationProvider.findUserOrganizations(member.getNamespaceId(), member.getOrganizationId(), member.getTargetId());
                if (userOrganization != null) {
                    this.userOrganizationProvider.deleteUserOrganizations(userOrganization);
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
     * @param _organizationMember
     * @param organizationId
     */
    private OrganizationMember createOrganiztionMemberWithDetailAndUserOrganization(OrganizationMember _organizationMember, Long organizationId) {
        User user = UserContext.current().getUser();
        //深拷贝
        OrganizationMember organizationMember = ConvertHelper.convert(_organizationMember, OrganizationMember.class);
        /**创建/更新detail,并获取detailId**/
        Long new_detail_id = getEnableDetailOfOrganizationMember(organizationMember, organizationId);

        OrganizationMember desOrgMember = this.organizationProvider.findOrganizationMemberByOrgIdAndToken(organizationMember.getContactToken(), organizationId);

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

            /**创建onNode的记录**/
            Long hiddenDirectId = findDirectUnderOrganizationId(organizationId);
            Organization hiddenDirectOrganiztion = checkOrganization(hiddenDirectId);
            organizationMember.setGroupPath(hiddenDirectOrganiztion.getPath());
            organizationMember.setGroupType(hiddenDirectOrganiztion.getGroupType());
            organizationMember.setOrganizationId(hiddenDirectId);
            organizationMember.setDetailId(new_detail_id);
            organizationProvider.createOrganizationMember(organizationMember);
        }
        return organizationMember;// add by xq.tian 2017/07/05
    }
}


