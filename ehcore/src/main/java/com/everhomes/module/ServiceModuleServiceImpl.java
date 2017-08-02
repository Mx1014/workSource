package com.everhomes.module;

import com.everhomes.acl.AuthorizationProvider;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.ResourceCategory;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.menu.Target;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.rest.acl.*;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.common.AllFlagType;
import com.everhomes.rest.common.EntityType;
import com.everhomes.rest.module.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceModuleServiceImpl implements ServiceModuleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModuleServiceImpl.class);

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ServiceModuleProvider serviceModuleProvider;

    @Autowired
    private AuthorizationProvider authorizationProvider;

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private DbProvider dbProvider;


    @Override
    public List<ServiceModuleDTO> listServiceModules(ListServiceModulesCommand cmd) {
        // checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        List<ServiceModuleScope> scopes = serviceModuleProvider.listServiceModuleScopes(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), ServiceModuleScopeApplyPolicy.REVERT.getCode());

        if (null == scopes || scopes.size() == 0) {
            scopes = serviceModuleProvider.listServiceModuleScopes(namespaceId, null, null, ServiceModuleScopeApplyPolicy.REVERT.getCode());
        }

        List<ServiceModule> list = serviceModuleProvider.listServiceModule(cmd.getLevel(), ServiceModuleType.PARK.getCode());
        if (scopes.size() != 0)
            list = filterList(list, scopes);

        List<ServiceModuleDTO> temp = list.stream().map(r -> {
            ServiceModuleDTO dto = ConvertHelper.convert(r, ServiceModuleDTO.class);
            return dto;
        }).collect(Collectors.toList());
        // List<ServiceModuleDTO> result = new ArrayList<ServiceModuleDTO>();
        //
        // for(ServiceModuleDTO s: temp) {
        // getChildServiceModules(temp, s);
        // if(s.getParentId() == 0) {
        // result.add(s);
        // }
        // }

        return temp;
    }

    @Override
    public List<ServiceModuleDTO> listTreeServiceModules(ListServiceModulesCommand cmd) {
        checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());

        Integer namespaceId = UserContext.current().getUser().getNamespaceId();
        //过滤出与scopes匹配的serviceModule
        List<ServiceModuleDTO> tempList = filterByScopes(namespaceId, cmd.getOwnerType(), cmd.getOwnerId());

        List<ServiceModuleDTO> result = new ArrayList<>();

        for (ServiceModuleDTO s : tempList) {
            getChildServiceModules(tempList, s);
            if (s.getParentId() == 0) {
                result.add(s);
            }
        }
        return result;
    }

    @Override
    public List<ServiceModuleDTO> listServiceModulePrivileges(ListServiceModulePrivilegesCommand cmd) {
        Integer startLevel = 2; // 从二级开始查询
        List<Byte> types = cmd.getTypes();

        // 默认查询左邻运营后台需要的模块，即园区模块和左邻运营方需要的系统管理模块
        if (null == types || types.size() == 0) {
            types = new ArrayList<>();
            types.add(ServiceModuleType.PARK.getCode());
            types.add(ServiceModuleType.MANAGER.getCode());
        }
        List<ServiceModule> serviceModules = new ArrayList<>();
        if(null != cmd.getModuleId()){
            ServiceModule module = serviceModuleProvider.findServiceModuleById(cmd.getModuleId());
            startLevel = module.getLevel() + 1;
            if(null != module)
                serviceModules = serviceModuleProvider.listServiceModule(module.getPath() + "/%");
        }else{
            serviceModules =  serviceModuleProvider.listServiceModule(startLevel, types);
        }


        List<ServiceModuleDTO> dtos = serviceModules.stream().map(r -> {
            ServiceModuleDTO dto = ConvertHelper.convert(r, ServiceModuleDTO.class);
            return dto;
        }).collect(Collectors.toList());

        List<ServiceModuleDTO> results = new ArrayList<ServiceModuleDTO>();

        for (ServiceModuleDTO s : dtos) {

            // 获取子节点
            getChildServiceModules(dtos, s);

            // 以startLevel级别作为每个模块的根节点
            if (s.getLevel() == startLevel) {
                results.add(s);
            }
        }

        return results;
    }


    private List<ServiceModule> filterList(List<ServiceModule> modules, List<ServiceModuleScope> scopes) {
        List<ServiceModule> result = new ArrayList<ServiceModule>();
        outer:
        for (ServiceModule m : modules) {
            for (ServiceModuleScope s : scopes) {
                if (s.getModuleId().equals(m.getId())) {
                    result.add(m);
                    continue outer;
                }
            }
        }
        return result;
    }

    private ServiceModuleDTO getChildServiceModules(List<ServiceModuleDTO> list, ServiceModuleDTO dto) {

        List<ServiceModuleDTO> childrens = new ArrayList<ServiceModuleDTO>();

        if (dto.getLevel() == 1) {
            dto.setvType(ServiceModuleTreeVType.MODULE_CATEGORY.getCode());
        } else if (dto.getLevel() == 2) {
            dto.setvType(ServiceModuleTreeVType.SERVICE_MODULE.getCode());
        } else if (dto.getLevel() == 3) {
            dto.setvType(ServiceModuleTreeVType.PRIVILEGE_CATEGORY.getCode());
        }

        for (ServiceModuleDTO serviceModuleDTO : list) {
            serviceModuleDTO.setvType(ServiceModuleTreeVType.SERVICE_MODULE.getCode());
            if (dto.getId().equals(serviceModuleDTO.getParentId())) {
                childrens.add(getChildServiceModules(list, serviceModuleDTO));
            }
        }
        if (childrens.size() > 0)
            dto.setServiceModules(childrens);
        else {
            List<ServiceModulePrivilege> modulePrivileges = serviceModuleProvider.listServiceModulePrivileges(dto.getId(), ServiceModulePrivilegeType.ORDINARY);
            List<ServiceModuleDTO> ps = new ArrayList<ServiceModuleDTO>();
            for (ServiceModulePrivilege modulePrivilege : modulePrivileges) {
                ServiceModuleDTO p = new ServiceModuleDTO();
                p.setId(modulePrivilege.getPrivilegeId());
                p.setName(modulePrivilege.getRemark());
                p.setvType(ServiceModuleTreeVType.PRIVILEGE.getCode());
                ps.add(p);
            }
            dto.setServiceModules(ps);
        }

        return dto;
    }

    private void checkOwnerIdAndOwnerType(String ownerType, Long ownerId) {
        if (null == ownerId) {
            LOGGER.error("OwnerId cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "OwnerId cannot be null.");
        }

        if (StringUtils.isBlank(ownerType)) {
            LOGGER.error("OwnerType cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "OwnerType cannot be null.");
        }
    }

    @Override
    public void assignmentServiceModule(AssignmentServiceModuleCommand cmd) {
        //参数检查
        if (cmd.getTargets() == null) {
            LOGGER.error("Targets of AssignmentServiceModuleCommand is not completed. cmd = {]", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Targets of AssignmentServiceModuleCommand is not completed.");
        }
        List<AssignmentTarget> targets = cmd.getTargets();
        final List<Project> projects = new ArrayList<>();
        final List<Long> moduleIds = new ArrayList<>();

        if (cmd.getAllModuleFlag() == AllFlagType.NO.getCode()){
            moduleIds.addAll(cmd.getModuleIds());
        }

        if(cmd.getAllProjectFlag() == AllFlagType.NO.getCode()){
            projects.addAll(cmd.getProjects());
        }

        //设置标记
        boolean isCreate = (cmd.getId() == null);
        boolean isUpdate = (cmd.getId() != null && cmd.getId() != 0);

        this.dbProvider.execute((status) -> {

            //判断业务关系ID是否存在，分别进行保存和更新操作
            // 1.先保存relation表的一条记录
            // 1.1组装relation对象
            ServiceModuleAssignmentRelation relation = new ServiceModuleAssignmentRelation();
            if (isCreate) {
                relation.setOwnerId(cmd.getOwnerId());
                relation.setOwnerType(cmd.getOwnerType());
                relation.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                relation.setCreatorUid(UserContext.current().getUser().getId());
            } else if (isUpdate) {//更新
                relation = this.serviceModuleProvider.findServiceModuleAssignmentRelationById(cmd.getId());
            }

            relation.setAllModuleFlag(cmd.getAllModuleFlag());
            relation.setAllProjectFlag(cmd.getAllProjectFlag());
            relation.setTargetJson(StringHelper.toJsonString(targets));
            relation.setProjectJson(StringHelper.toJsonString(projects));
            if (cmd.getAllModuleFlag() == AllFlagType.NO.getCode()) {
                relation.setModuleJson(StringHelper.toJsonString(moduleIds));
            } else {
                relation.setModuleJson("");
            }
            relation.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            relation.setOperatorUid(UserContext.current().getUser().getId());

            // 1.2保存更新，并指定relation_id
            Long relation_id = 0L;
            if (isCreate) {
                relation_id = this.serviceModuleProvider.createModuleAssignmentRetion(relation);
            } else if (isUpdate) {
                this.serviceModuleProvider.updateServiceModuleAssignmentRelation(relation);
                relation_id = relation.getId();
            }

            // 2.再保存assigment表的多条记录
            if (isUpdate) {
                //删除
                List<ServiceModuleAssignment> assignmentList = this.serviceModuleProvider.findServiceModuleAssignmentListByRelationId(relation_id);
                this.serviceModuleProvider.deleteServiceModuleAssignments(assignmentList);
            }

            //保存assigment多条记录
            List<ServiceModuleAssignment> assignmentList = new ArrayList<>();
            /**1.根据targetIds进行循环处理**/
            for (AssignmentTarget target : targets) {
                /**2.1如果全范围标识YES,根据范围进行一次处理**/
                if (AllFlagType.fromCode(cmd.getAllProjectFlag()) == AllFlagType.YES) {
                    Project project_none = new Project();
                    project_none.setProjectId(0L);
                    project_none.setProjectType(EntityType.ALL.getCode());
                    /**3.根据moduleId进行循环处理**/
                    if (AllFlagType.fromCode(cmd.getAllModuleFlag()) == AllFlagType.YES) {
                        ServiceModuleAssignment assignment = buildAssignment(target, project_none, 0L, AllFlagType.YES.getCode(), AllFlagType.YES.getCode(), relation_id);
                        assignmentList.add(assignment);
                    } else if (AllFlagType.fromCode(cmd.getAllModuleFlag()) == AllFlagType.NO) {
                        for (Long moduleId : moduleIds) {
                            ServiceModuleAssignment assignment = buildAssignment(target, project_none, moduleId, AllFlagType.YES.getCode(), AllFlagType.NO.getCode(), relation_id);
                            assignmentList.add(assignment);
                        }
                    }
                    /**2.2如果全范围标识NO,根据范围进行循环处理**/
                } else if (AllFlagType.fromCode(cmd.getAllProjectFlag()) == AllFlagType.NO) {
                    for (Project project : projects) {
                        /**3.根据moduleId进行循环处理**/
                        if (AllFlagType.fromCode(cmd.getAllModuleFlag()) == AllFlagType.YES) {
                            ServiceModuleAssignment assignment = buildAssignment(target, project, 0L, AllFlagType.NO.getCode(), AllFlagType.YES.getCode(), relation_id);
                            assignmentList.add(assignment);
                        } else if (AllFlagType.fromCode(cmd.getAllModuleFlag()) == AllFlagType.NO) {
                            for (Long moduleId : moduleIds) {
                                ServiceModuleAssignment assignment = buildAssignment(target, project, moduleId, AllFlagType.NO.getCode(), AllFlagType.NO.getCode(), relation_id);
                                assignmentList.add(assignment);
                            }
                        }
                    }
                }
            }
            this.serviceModuleProvider.batchCreateServiceModuleAssignment(assignmentList);
            return null;
        });

    }

    @Override
    public void deleteServiceModuleAssignmentRelation(DeleteServiceModuleAssignmentRelationCommand cmd) {
        this.dbProvider.execute((status) -> {
            // 1.1 查詢relation表的記錄
            ServiceModuleAssignmentRelation relation = this.serviceModuleProvider.findServiceModuleAssignmentRelationById(cmd.getId());
            if (relation == null) {
                LOGGER.error("ServiceModuleAssignmentRelation is not matched. cmd = {}", cmd);
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "ServiceModuleAssignmentRelation is not matched.");
            }
            // 1.2刪除relation表的记录
            this.serviceModuleProvider.deleteServiceModuleAssignmentRelationById(cmd.getId());
            // 2.1查询assignment表的多条记录
            List<ServiceModuleAssignment> assignments = this.serviceModuleProvider.findServiceModuleAssignmentListByRelationId(cmd.getId());
            if (assignments == null) {
                LOGGER.error("ServiceModuleAssignment is not matched. cmd = {}", cmd);
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "ServiceModuleAssignment is not matched.");
            }
            // 2.2删除assignment表的多条记录
            this.serviceModuleProvider.deleteServiceModuleAssignments(assignments);
            return null;
        });
    }

    @Override
    public List<ServiceModuleAssignmentRelationDTO> listServiceModuleAssignmentRelations
            (ListServiceModuleAssignmentRelationsCommand cmd) {

        Type targetType = new TypeToken<List<AssignmentTarget>>() {
        }.getType();
        Type projectType = new TypeToken<List<Project>>() {
        }.getType();
        Type modulesType = new TypeToken<List<Long>>() {
        }.getType();

        if (cmd.getOwnerId() == null || cmd.getOwnerType() == null) {
            LOGGER.error("ListServiceModuleAssignmentRelationsCommand is not completed. cmd = {}", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "ListServiceModuleAssignmentRelationsCommand is not matched.");
        }

        // 根据owenerType和owenerId查询出多条relatios表记录
        List<ServiceModuleAssignmentRelation> relations = this.serviceModuleProvider.listServiceModuleAssignmentRelations(cmd.getOwnerType(), cmd.getOwnerId());
//        if (relations == null || relations.size() == 0) {
//            LOGGER.error("ServiceModuleAssignmentRelations is not found. cmd = {}", cmd);
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "ServiceModuleAssignmentRelations is not found.");
//        }
        List<ServiceModuleAssignmentRelationDTO> results = relations.stream().map(r -> {
            ServiceModuleAssignmentRelationDTO dto = new ServiceModuleAssignmentRelationDTO();
            dto.setId(r.getId());
            dto.setOwnerType(r.getOwnerType());
            dto.setOwnerId(r.getOwnerId());
            dto.setAllModuleFlag(r.getAllModuleFlag());

            //处理targets
            List<AssignmentTarget> targets = GsonUtil.fromJson(r.getTargetJson(), targetType);
            for (AssignmentTarget target : targets) {
                // 机构
                if (EntityType.fromCode(target.getTargetType()) == EntityType.ORGANIZATIONS) {
                    Organization organization = this.organizationProvider.findOrganizationById(target.getTargetId());
                    if (organization == null) {
                        LOGGER.error("JsonParse Organization is not matched. cmd = {}", cmd);
                        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "JsonParse Organization is not matched.");
                    }
                    target.setTargetName(organization.getName());
                }
            }
            dto.setTargets(targets);

            //处理projects
            if (r.getAllProjectFlag() == AllFlagType.NO.getCode()) {
                List<Project> projects = GsonUtil.fromJson(r.getProjectJson(), projectType);
                for (Project project : projects) {
                    processProject(project);
                }
                dto.setAllProjectFlag(AllFlagType.NO.getCode());
                dto.setProjects(projects);
            } else if (r.getAllProjectFlag() == AllFlagType.YES.getCode()) {
                /**如果范围全选标识为NO，则默认不设置project**/
                dto.setAllProjectFlag(AllFlagType.YES.getCode());
            }

            //处理modules
            List<ServiceModuleOut> modulesOut = new ArrayList<>();
            //如果模块全选标识为NO
            if (r.getAllModuleFlag() == AllFlagType.NO.getCode()) {
                List<Long> moduleIds = GsonUtil.fromJson(r.getModuleJson(), modulesType);
                for (Long moduleId : moduleIds) {
                    ServiceModule module = this.serviceModuleProvider.findServiceModuleById(moduleId);
                    modulesOut.add(ConvertHelper.convert(module, ServiceModuleOut.class));
                }
                dto.setModules(modulesOut);
            } else if (r.getAllModuleFlag() == AllFlagType.YES.getCode()) {
                /**如果模块全选标识为NO，则默认不设置modules**/
                dto.setAllModuleFlag(AllFlagType.YES.getCode());
            }

            return ConvertHelper.convert(dto, ServiceModuleAssignmentRelationDTO.class);
        }).collect(Collectors.toList());

        return results;
    }

    @Override
    public List<ServiceModuleDTO> treeServiceModules(TreeServiceModuleCommand cmd) {
        checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());

        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        //过滤出与scopes匹配的serviceModule
        List<ServiceModuleDTO> tempList = filterByScopes(namespaceId, cmd.getOwnerType(), cmd.getOwnerId());

        return this.getServiceModuleAsLevelTree(tempList, 0L);
    }

    @Override
    public ServiceModuleDTO getServiceModule(GetServiceModuleCommand cmd) {
        checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
        ServiceModule serviceModule = this.serviceModuleProvider.findServiceModuleById(cmd.getModuleId());
        if (null == serviceModule) {
            LOGGER.error("Unable to find the serviceModule. serviceModuleId = {}", cmd.getModuleId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Unable to find the serviceModule.");
        }
        return ConvertHelper.convert(serviceModule, ServiceModuleDTO.class);
    }

    private void checkTarget(String targetType, Long targetId) {
        if (null == com.everhomes.entity.EntityType.fromCode(targetType)) {
            LOGGER.error("params targetType is null");
            throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
                    "params targetType is null.");
        }

        if (null == targetId) {
            LOGGER.error("params targetId is null");
            throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
                    "params targetId is null.");
        }

        if (com.everhomes.entity.EntityType.USER == com.everhomes.entity.EntityType.fromCode(targetType)) {
            checkUser(targetId);
        } else if (com.everhomes.entity.EntityType.ORGANIZATIONS == com.everhomes.entity.EntityType.fromCode(targetType)) {
            checkOrganization(targetId);
        }
    }

    private User checkUser(Long userId) {
        User user = userProvider.findUserById(userId);
        if (null == user) {
            LOGGER.error("Unable to find the user. user = {}", userId);
            throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
                    "user non-existent.");
        }
        return user;
    }


    @Override
    public List<ProjectDTO> listUserRelatedCategoryProjectByModuleId(ListUserRelatedProjectByModuleCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = cmd.getUserId();
        if(null == cmd.getUserId()){
            userId = user.getId();
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<ProjectDTO> dtos = getUserProjectsByModuleId(userId, cmd.getOrganizationId(), cmd.getModuleId());
        return rolePrivilegeService.getTreeProjectCategories(namespaceId, dtos);
    }

    @Override
    public List<ProjectDTO> listUserRelatedProjectByModuleId(ListUserRelatedProjectByModuleCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = cmd.getUserId();
        if(null == cmd.getUserId()){
            userId = user.getId();
        }
        return getUserProjectsByModuleId(userId, cmd.getOrganizationId(), cmd.getModuleId());
    }

    @Override
    public List<CommunityDTO> listUserRelatedCommunityByModuleId(ListUserRelatedProjectByModuleCommand cmd) {
        User user = UserContext.current().getUser();
        List<CommunityDTO> dtos = new ArrayList<>();
        List<ProjectDTO> projects = getUserProjectsByModuleId(user.getId(), cmd.getOrganizationId(), cmd.getModuleId());
        for (ProjectDTO project: projects) {
            if(EntityType.fromCode(project.getProjectType()) == EntityType.COMMUNITY){
                Community community = communityProvider.findCommunityById(project.getProjectId());
                if(null != community){
                    dtos.add(ConvertHelper.convert(community, CommunityDTO.class));
                }
            }
        }
        return dtos;
    }

    @Override
    public Byte checkModuleManage(CheckModuleManageCommand cmd){
        Long userId = cmd.getUserId();
        User user = UserContext.current().getUser();
        if(null == userId){
            userId = user.getId();
        }
        if(checkModuleManage(userId, cmd.getOrganizationId(), cmd.getModuleId())){
            return 1;
        }
        return 0;
    }

    private boolean checkModuleManage(Long userId, Long organizationId, Long moduleId) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        if (resolver.checkSuperAdmin(userId, organizationId) || resolver.checkModuleAdmin(EntityType.ORGANIZATIONS.getCode(), organizationId, userId, moduleId)) {
            return true;
        }
        return false;
    }

    /**
     * 获取模块下授权的用户项目
     * @param userId
     * @param organizationId
     * @param moduleId
     * @return
     */
    private List<ProjectDTO> getUserProjectsByModuleId(Long userId, Long organizationId, Long moduleId){
        boolean allProjectFlag = false;
        List<ProjectDTO> dtos = new ArrayList<>();
        //物业超级管理员拿所有项目
        if(checkModuleManage(userId, organizationId, moduleId)){
            allProjectFlag = true;
        }else{
            List<Target> targets = new ArrayList<>();
            targets.add(new Target(com.everhomes.entity.EntityType.USER.getCode(), userId));
            //获取人员的所有相关机构
            List<Long> orgIds = organizationService.getIncludeOrganizationIdsByUserId(userId, organizationId);
            for (Long orgId: orgIds) {
                targets.add(new Target(com.everhomes.entity.EntityType.ORGANIZATIONS.getCode(), orgId));
            }

            //获取人员和人员所有机构所赋予模块的所属项目范围
            List<Project> projects = authorizationProvider.getAuthorizationProjectsByAuthIdAndTargets(EntityType.SERVICE_MODULE.getCode(), moduleId, targets);
            for (Project project: projects) {
                //在模块下拥有全部项目权限
                if(EntityType.ALL == EntityType.fromCode(project.getProjectType())){
                    allProjectFlag = true;
                    break;
                }else {
                    processProject(project);
                    dtos.add(ConvertHelper.convert(project, ProjectDTO.class));
                }
            }
        }

        if(allProjectFlag){
            List<CommunityDTO> communitydtos = organizationService.listAllChildrenOrganizationCoummunities(organizationId);
            for (CommunityDTO community: communitydtos) {
                ProjectDTO dto = new ProjectDTO();
                dto.setProjectId(community.getId());
                dto.setProjectName(community.getName());
                dto.setProjectType(com.everhomes.entity.EntityType.COMMUNITY.getCode());
                dtos.add(dto);
            }
        }
        return dtos;
    }


    private void processProject(Project project){
        // 判断园区
        if (EntityType.fromCode(project.getProjectType()) == EntityType.COMMUNITY) {
            Community community = this.communityProvider.findCommunityById(project.getProjectId());
            if (community == null) {
                LOGGER.error("JsonParse Community is not matched. projectId = {}", project.getProjectId());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "JsonParse Community is not matched.");
            }
            project.setProjectType(EntityType.COMMUNITY.getCode());
            project.setProjectName(community.getName());
        } else if (EntityType.fromCode(project.getProjectType()) == EntityType.CHILD_PROJECT) {// 判断子项目
            ResourceCategory resourceCategory = this.communityProvider.findResourceCategoryById(project.getProjectId());
            if (resourceCategory == null) {
                LOGGER.error("JsonParse ResourceCategory is not matched. projectId = {}", project.getProjectId());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "JsonParse ResourceCategory is not matched.");
            }
            project.setProjectType(EntityType.CHILD_PROJECT.getCode());
            project.setProjectName(resourceCategory.getName());
        }
    }

    private Organization checkOrganization(Long organizationId) {
        Organization org = organizationProvider.findOrganizationById(organizationId);
        if (org == null) {
            LOGGER.error("Unable to find the organization.organizationId = {}", organizationId);
            throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
                    "Unable to find the organization.");
        }
        return org;
    }

    private ServiceModuleAssignment buildAssignment(AssignmentTarget target, Project project, Long moduleId, byte AllOwnerFlag, byte AllModuleFlag, Long relation_id) {
        ServiceModuleAssignment assignment = new ServiceModuleAssignment();
        assignment.setNamespaceId(UserContext.getCurrentNamespaceId());
        assignment.setOrganizationId(0L);
        assignment.setTargetId(target.getTargetId());
        assignment.setTargetType(target.getTargetType());
        assignment.setOwnerType(project.getProjectType());
        assignment.setOwnerId(project.getProjectId());
        assignment.setModuleId(moduleId);
        assignment.setCreateUid(UserContext.current().getUser().getId());
        assignment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        assignment.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        assignment.setAssignmentType((byte) 0);
        assignment.setAllModuleFlag(AllModuleFlag);
        assignment.setIncludeChildFlag(target.getIncludeChildFlag());
        assignment.setRelationId(relation_id);
        return assignment;
    }

    @Override
    public List<ServiceModuleDTO> filterByScopes(int namespaceId, String ownerType, Long ownerId) {
        List<ServiceModuleScope> scopes = serviceModuleProvider.listServiceModuleScopes(namespaceId, ownerType, ownerId, ServiceModuleScopeApplyPolicy.REVERT.getCode());

        if (null == scopes || scopes.size() == 0) {
            scopes = serviceModuleProvider.listServiceModuleScopes(namespaceId, null, null, ServiceModuleScopeApplyPolicy.REVERT.getCode());
        }

        List<ServiceModule> list = serviceModuleProvider.listServiceModule(null, ServiceModuleType.PARK.getCode());
        if (scopes.size() != 0)
            list = filterList(list, scopes);

        List<ServiceModuleDTO> temp = list.stream().map(r -> {
            ServiceModuleDTO dto = ConvertHelper.convert(r, ServiceModuleDTO.class);
            return dto;
        }).collect(Collectors.toList());
        return temp;
    }

    /**
     * 获取serviceModule的树形目录（level  = 2）
     *
     * @param tempList
     * @param parentId
     * @return
     */
    private List<ServiceModuleDTO> getServiceModuleAsLevelTree(List<ServiceModuleDTO> tempList, Long parentId) {
        List<ServiceModuleDTO> results = new ArrayList<>();
        Iterator<ServiceModuleDTO> iter = tempList.iterator();
        while (iter.hasNext()) {
            ServiceModuleDTO current = iter.next();
            if (current.getParentId().equals(parentId) && current.getLevel() < 3) {
                List<ServiceModuleDTO> c_node = getServiceModuleAsLevelTree(tempList, current.getId());
                current.setServiceModules(c_node);
                results.add(current);
            }
        }
        return results;
    }

    /**
     * 检查assignmentService更新命令参数
     *
     * @param cmd
     */
    private void checkAssignmentCommand(AssignmentServiceModuleCommand cmd) {
        if (cmd.getTargets() == null || cmd.getProjects() == null || cmd.getModuleIds() == null) {
            LOGGER.error("AssignmentServiceModuleCommand is not completed. cmd = {]", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "AssignmentServiceModuleCommand is not completed.");
        }

        List<AssignmentTarget> targets = cmd.getTargets();
        List<Project> projects = cmd.getProjects();
        List<Long> moduleIds = cmd.getModuleIds();

        for (AssignmentTarget target : targets) {
            checkTarget(target.getTargetType(), target.getTargetId());
        }
        for (Project project : projects) {
            if (project.getProjectId() == null || project.getProjectType() == null) {
                LOGGER.error("project is illegal. cmd = {}", cmd);
                break;
            }
        }
        for (Long moduleId : moduleIds) {
            if (moduleId == null) {
                LOGGER.error("moduleId is illegal. cmd = {}", cmd);
                break;
            }
        }
    }

}
