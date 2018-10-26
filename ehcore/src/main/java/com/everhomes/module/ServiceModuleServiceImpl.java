package com.everhomes.module;

import com.everhomes.acl.*;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.ResourceCategory;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.menu.Target;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespacesService;
import com.everhomes.organization.*;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.namespace.ListCommunityByNamespaceCommandResponse;
import com.everhomes.rest.organization.ListCommunitiesByOrganizationIdCommand;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppProvider;
import com.everhomes.rest.acl.*;
import com.everhomes.rest.address.CommunityAdminStatus;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.common.AllFlagType;
import com.everhomes.rest.common.EntityType;
import com.everhomes.rest.community.CommunityFetchType;
import com.everhomes.rest.module.*;
import com.everhomes.rest.oauth2.ControlTargetOption;
import com.everhomes.rest.oauth2.ModuleManagementType;
import com.everhomes.rest.openapi.techpark.AllFlag;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.portal.MultipleFlag;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.portal.ServiceModuleAppStatus;
import com.everhomes.rest.portal.TreeServiceModuleAppsResponse;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.excel.ExcelUtils;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang.StringUtils;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.everhomes.rest.oauth2.ControlTargetOption.ALL_COMMUNITY;
import static com.everhomes.rest.oauth2.ModuleManagementType.COMMUNITY_CONTROL;

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

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private ServiceModuleAppProvider serviceModuleAppProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private WebMenuPrivilegeProvider webMenuPrivilegeProvider;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private AclProvider aclProvider;

    @Autowired
    private ServiceModuleAppService serviceModuleAppService;

    @Autowired
    private ServiceModuleAppAuthorizationService serviceModuleAppAuthorizationService;


    @Autowired
    private ServiceModuleEntryProvider serviceModuleEntryProvider;

    @Autowired
    private ContentServerService contentServerService;


    @Autowired
    private AppCategoryProvider appCategoryProvider;
    
    @Autowired
    private PortalService portalService;

    @Autowired
    private NamespacesService namespacesService;


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
    public ListServiceModulesResponse listAllServiceModules(ListServiceModulesCommand cmd){
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<ServiceModule> list = serviceModuleProvider.listServiceModule(locator, pageSize, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                String path = null;
                if(null != cmd.getParentId()){
                    ServiceModule serviceModule = checkServiceModule(cmd.getParentId());
                    path = serviceModule.getPath() + "/%";
                }
                query.addConditions(Tables.EH_SERVICE_MODULES.STATUS.eq(ServiceModuleStatus.ACTIVE.getCode()));
                if(!StringUtils.isEmpty(path)){
                    query.addConditions(Tables.EH_SERVICE_MODULES.PATH.like(path));

                }
                return null;
            }
        });
        ListServiceModulesResponse res = new ListServiceModulesResponse();
        res.setNextPageAnchor(locator.getAnchor());
        res.setDtos(list.stream().map(r -> {
            return processServiceModuleDTO(r);
        }).collect(Collectors.toList()));

        return res;
    }

    private ServiceModuleDTO processServiceModuleDTO(ServiceModule module){
        ServiceModuleDTO dto = ConvertHelper.convert(module, ServiceModuleDTO.class);
        if(module.getCreateTime() != null)
            dto.setCreateTime(module.getCreateTime().getTime());
        if(module.getUpdateTime() != null)
            dto.setUpdateTime(module.getUpdateTime().getTime());
        User operator = userProvider.findUserById(module.getOperatorUid());
        if(null != operator) dto.setOperatorUName(operator.getNickName());

        if(module.getIconUri() != null){
            String url = contentServerService.parserUri(module.getIconUri(), module.getClass().getSimpleName(), module.getId());
            dto.setIconUrl(url);
        }
        return dto;
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
            if(null != module){//查找三级菜单
                serviceModules = serviceModuleProvider.listServiceModule(module.getPath() + "/%");
                serviceModules.sort(Comparator.comparingInt(ServiceModule::getDefaultOrder));
                if(serviceModules == null || serviceModules.size() == 0){//如果三级菜单不存在，则直接使用二级菜单
                    serviceModules = Collections.singletonList(module);
                    startLevel --;
                }
            }
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

        // 根据应用id进行过滤不需要的权限项
        BanPrivilegeHandler handler = getBanPrivilegeHandler();
        if (null != handler && results.size() > 0) {
            List<Long> banPrivilegeIds = handler.listBanPrivilegesByModuleIdAndAppId(cmd.getNamespaceId(), cmd.getModuleId(), cmd.getAppId());
            if(banPrivilegeIds != null && banPrivilegeIds.size() > 0){
                banPrivilegeIds = banPrivilegeIds.stream().filter(r-> r != 0L).collect(Collectors.toList());
                for (ServiceModuleDTO nextDto : results) {
                    // 进入禁止权限显示的方法
                    if (nextDto.getServiceModules() != null && nextDto.getServiceModules().size() > 0 && banPrivilegeIds != null && banPrivilegeIds.size() > 0) {
                        List<ServiceModuleDTO> temp = new ArrayList<>(nextDto.getServiceModules());
                        for (ServiceModuleDTO dto : nextDto.getServiceModules()) {
                            if (ServiceModuleTreeVType.PRIVILEGE.equals(ServiceModuleTreeVType.fromCode(dto.getvType())) && banPrivilegeIds.contains(dto.getId())) {
                                LOGGER.debug("privilegeId ban, privilegeId = {}, namespaceId= {}, moduleId= {}, appId = {}", dto.getId(), UserContext.getCurrentNamespaceId(), cmd.getModuleId(), cmd.getAppId());
//                                results.remove(nextDto);
                                temp.remove(dto);
                            }
                        }
                        nextDto.setServiceModules(temp);
                    }
                }
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
//                        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "JsonParse Organization is not matched.");
                        continue;
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
    public TreeServiceModuleAppsResponse treeServiceModuleApps(TreeServiceModuleCommand cmd) {
        TreeServiceModuleAppsResponse response = new TreeServiceModuleAppsResponse();

        checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());

        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        //过滤出与scopes匹配的serviceModule
//        List<ServiceModuleDTO> tempList = filterByScopes(namespaceId, cmd.getOwnerType(), cmd.getOwnerId());
        //todo
        List<Long> moduleIds = serviceModuleAppService.listReleaseServiceModuleIdsWithParentByNamespace(UserContext.getCurrentNamespaceId());
        if(moduleIds.size() == 0){
            return response;
        }
        List<ServiceModuleDTO> tempList = this.serviceModuleProvider.listServiceModuleDtos(moduleIds);

        List<ServiceModuleDTO> communityControlList = new ArrayList<>();
        List<ServiceModuleDTO> orgControlList = new ArrayList<>();
        List<ServiceModuleDTO> unlimitControlList = new ArrayList<>();
        //按控制范围进行区分
        //把三级模块加入list
        tempList.stream().filter(r->!"".equals(r.getModuleControlType()) && r.getModuleControlType() != null && r.getLevel() == 3).map(r->{
            switch (ModuleManagementType.fromCode(r.getModuleControlType())){
                case COMMUNITY_CONTROL:
                    communityControlList.add(r);
                    break;
                case ORG_CONTROL:
                    orgControlList.add(r);
                    break;
                case UNLIMIT_CONTROL:
                    unlimitControlList.add(r);
                    break;
            }
            return null;
        }).collect(Collectors.toList());

        //把二级分类加入所有list
        tempList.stream().filter(r -> r.getLevel() == 2 || r.getLevel() == 1).map(r -> {
            communityControlList.add(ConvertHelper.convert(r,ServiceModuleDTO.class));
            orgControlList.add(ConvertHelper.convert(r,ServiceModuleDTO.class));
            unlimitControlList.add(ConvertHelper.convert(r,ServiceModuleDTO.class));
            return null;
        }).collect(Collectors.toList());


        List<ServiceModuleDTO> c = this.getServiceModuleAppsAsLevelTree(communityControlList, 0L);
        List<ServiceModuleDTO> o = this.getServiceModuleAppsAsLevelTree(orgControlList, 0L);
        List<ServiceModuleDTO> u = this.getServiceModuleAppsAsLevelTree(unlimitControlList, 0L);


        //只要二级的 start
        List<ServiceModuleDTO> subC = new ArrayList<>();
        for (ServiceModuleDTO s: c){
            if(s.getServiceModules() != null && s.getServiceModules().size() > 0){
                subC.addAll(s.getServiceModules());
            }
        }

        List<ServiceModuleDTO> subo = new ArrayList<>();
        for (ServiceModuleDTO s: o){
            if(s.getServiceModules() != null && s.getServiceModules().size() > 0){
                subo.addAll(s.getServiceModules());
            }
        }

        List<ServiceModuleDTO> subu = new ArrayList<>();
        for (ServiceModuleDTO s: u){
            if(s.getServiceModules() != null && s.getServiceModules().size() > 0){
                subu.addAll(s.getServiceModules());
            }
        }
        //只要二级的 end

        subC=subC.stream().filter(r->r.getServiceModuleApps() != null && r.getServiceModuleApps().size() > 0).collect(Collectors.toList());
        subo=subo.stream().filter(r->r.getServiceModuleApps() != null && r.getServiceModuleApps().size() > 0).collect(Collectors.toList());
        subu=subu.stream().filter(r->r.getServiceModuleApps() != null && r.getServiceModuleApps().size() > 0).collect(Collectors.toList());

        response.setCommunityControlList(subC);
        response.setOrgControlList(subo);
        response.setUnlimitControlList(subu);

        return response;
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


        //定制版的App广场item没有appId和moduleId，直接查询公司管理的项目
        if(cmd.getModuleId() == null && cmd.getAppId() == null){

            ListCommunitiesByOrganizationIdCommand listCmd = new ListCommunitiesByOrganizationIdCommand();
            listCmd.setOrganizationId(cmd.getOrganizationId());
            ListCommunityByNamespaceCommandResponse listRes = organizationService.listCommunityByOrganizationId(listCmd);

            List<ProjectDTO> dtos = new ArrayList<>();
            if(listRes != null && listRes.getCommunities() != null && listRes.getCommunities().size() > 0){
                for (CommunityDTO community: listRes.getCommunities()){
                    ProjectDTO dto = toProjectDto(community);
                    dtos.add(dto);
                }
            }
            return dtos;
        }

        if(cmd.getModuleId() == null && cmd.getAppId() != null){
            ServiceModuleApp app = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(cmd.getAppId());
            if(app != null){
                cmd.setModuleId(app.getModuleId());
            }
        }

        List<ProjectDTO> dtos = getUserProjectsByModuleId(userId, cmd.getOrganizationId(), cmd.getModuleId(), cmd.getAppId());
        if(cmd.getCommunityFetchType() != null){
            return rolePrivilegeService.getTreeProjectCategories(namespaceId, dtos, CommunityFetchType.fromCode(cmd.getCommunityFetchType()));
        }
        return rolePrivilegeService.getTreeProjectCategories(namespaceId, dtos);

    }


    private ProjectDTO toProjectDto(CommunityDTO communityDto){
        ProjectDTO dto = new ProjectDTO();
        dto.setProjectId(communityDto.getId());
        dto.setProjectName(communityDto.getName());
        dto.setProjectType(com.everhomes.entity.EntityType.COMMUNITY.getCode());
        dto.setCommunityType(communityDto.getCommunityType());
        return dto;
    }


    @Override
    public List<ProjectDTO> listUserRelatedProjectByModuleId(ListUserRelatedProjectByModuleCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = cmd.getUserId();
        if(null == cmd.getUserId()){
            userId = user.getId();
        }
        Long orgId = cmd.getOrganizationId();
        if(null == cmd.getOrganizationId()){
            OrganizationMemberDetails orgmb = organizationProvider.findOrganizationMemberDetailsByTargetId(userId);
            if(null != orgmb)
                orgId = orgmb.getOrganizationId();

        }

        return getUserProjectsByModuleId(userId, orgId, cmd.getModuleId(), cmd.getAppId());
    }

    @Override
    public List<CommunityDTO> listUserRelatedCommunityByModuleId(ListUserRelatedProjectByModuleCommand cmd) {
        User user = UserContext.current().getUser();
        List<CommunityDTO> dtos = new ArrayList<>();
        List<ProjectDTO> projects = getUserProjectsByModuleId(user.getId(), cmd.getOrganizationId(), cmd.getModuleId(), null);
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
        if(cmd.getAppId() != null){
            if(userPrivilegeMgr.checkModuleAppAdmin(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), userId, cmd.getAppId())){
                return 1;
            }
        }
        return 0;
    }

    @Override
    public ServiceModuleDTO createServiceModule(CreateServiceModuleCommand cmd) {
        User user = UserContext.current().getUser();
        ServiceModule parentModule = checkServiceModule(cmd.getParentId());
        ServiceModule module = ConvertHelper.convert(cmd, ServiceModule.class);
        module.setLevel(2);
        module.setPath(parentModule.getPath());
        module.setLevel(parentModule.getLevel() + 1);
        module.setStatus(ServiceModuleStatus.ACTIVE.getCode());
        module.setCreatorUid(user.getId());
        module.setOperatorUid(user.getId());
        serviceModuleProvider.createServiceModule(module);
        return processServiceModuleDTO(module);
    }

    @Override
    public ServiceModuleDTO updateServiceModule(UpdateServiceModuleCommand cmd) {
        User user = UserContext.current().getUser();
        ServiceModule module = checkServiceModule(cmd.getId());
        module.setDescription(cmd.getDescription());
        module.setName(cmd.getName());
        module.setInstanceConfig(cmd.getInstanceConfig());
        module.setOperatorUid(user.getId());
        module.setIconUri(cmd.getIconUri());
        serviceModuleProvider.updateServiceModule(module);
        return processServiceModuleDTO(module);
    }

    @Override
    public void deleteServiceModule(DeleteServiceModuleCommand cmd) {
        ServiceModule module = checkServiceModule(cmd.getId());
        serviceModuleProvider.deleteServiceModuleById(module.getId());
    }

    private ServiceModule checkServiceModule(Long id){
        ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(id);
        if(null == serviceModule){
            LOGGER.error("Unable to find the serviceModule.moduleId = {}", id);
            throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
                    "Unable to find the serviceModule.");
        }
        return serviceModule;
    }

    // Added by janson, TODO for administrator in zuolin admin
    private boolean checkZuolinRoot(long userId) {
        if(aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(),
                UserContext.current().getUser().getId(), Privilege.Write, null)) {
            return true;
        }

        return false;
    }

    private boolean checkModuleManage(Long userId, Long organizationId, Long moduleId) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");

        //TODO add by yanjun, check by lv
        if(aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(),
                UserContext.current().getUser().getId(), Privilege.Write, null)) {
            return true;
        }

        // by lei.lv 不再支持模块管理员
       // if (resolver.checkSuperAdmin(userId, organizationId) || resolver.checkModuleAdmin(EntityType.ORGANIZATIONS.getCode(), organizationId, userId, moduleId)) {
        if (resolver.checkSuperAdmin(userId, organizationId)) {
            return true;
        }
        return false;
    }

    /**
     * 获取模块下授权的用户项目
     * @param userId
     * @param organizationId
     * @param moduleId
     * @param appId
     * @return
     */
    private List<ProjectDTO> getUserProjectsByModuleId(Long userId, Long organizationId, Long moduleId, Long appId){
        boolean allProjectFlag = false;
        List<ProjectDTO> dtos = new ArrayList<>();

        if(checkZuolinRoot(userId)) {
            //如果是左邻运营后台的项目导航，则左邻后台管理员可以拿到此域空间下所有的项目，与管理公司或者普通公司无关。
            ListingLocator locator = new ListingLocator();
            List<Community> communities = this.communityProvider.listCommunities(UserContext.getCurrentNamespaceId() , null, null, null, CommunityAdminStatus.ACTIVE.getCode(), null, locator, 100);
            for (Community community: communities) {
                ProjectDTO dto = new ProjectDTO();
                dto.setProjectId(community.getId());
                dto.setProjectName(community.getName());
                dto.setProjectType(com.everhomes.entity.EntityType.COMMUNITY.getCode());
                dto.setCommunityType(community.getCommunityType());
                dtos.add(dto);
            }
            return dtos;
        }

        //物业超级管理员拿所有项目
        //这一步是校验是不是超级管理员，如果是超级管理员那么就将allProjectFlag状态置为true
        if(checkModuleManage(userId, organizationId, moduleId)){
            //说明是超级管理员
            allProjectFlag = true;
        }else{
            //说明不是超级管理员
            List<Target> targets = new ArrayList<>();
            targets.add(new Target(com.everhomes.entity.EntityType.USER.getCode(), userId));
            //获取人员的所有相关机构
            List<Long> orgIds = organizationService.getIncludeOrganizationIdsByUserId(userId, organizationId);
            for (Long orgId: orgIds) {
                targets.add(new Target(com.everhomes.entity.EntityType.ORGANIZATIONS.getCode(), orgId));
            }

            //获取人员和人员所有机构所赋予模块的所属项目范围(应用管理员) -- add by lei.lv
            // todo 加上应用
            List<Authorization> authorizations_apps =  authorizationProvider.listAuthorizations(EntityType.ORGANIZATIONS.getCode(), organizationId, EntityType.USER.getCode(), userId, com.everhomes.entity.EntityType.SERVICE_MODULE_APP.getCode(), null, IdentityType.MANAGE.getCode(), appId, null, null, false);
            if(authorizations_apps != null && authorizations_apps.size() > 0){
                authorizations_apps = authorizations_apps.stream().filter(r->ModuleManagementType.fromCode(r.getModuleControlType()) == COMMUNITY_CONTROL).limit(1).collect(Collectors.toList());
                if(authorizations_apps !=null && authorizations_apps.size() > 0) {
                    Authorization authorization = authorizations_apps.get(0);//每一个userId+organizationId在同一个type下只有一个control_id
                    if (ControlTargetOption.fromCode(authorization.getControlOption()) == ALL_COMMUNITY){
                        allProjectFlag = true;
                    }else{
                        List<ControlTarget> configs = authorizationProvider.listAuthorizationControlConfigs(userId,authorization.getControlId());
                        List<Project> projectList = configs.stream().map(r->new Project(EntityType.COMMUNITY.getCode(), r.getId())).collect(Collectors.toList());
                        for (Project project : projectList) {
                            processProject(project);
                            dtos.add(ConvertHelper.convert(project, ProjectDTO.class));
                        }
                    }

                }
            }


            //获取人员和人员所有机构所赋予模块的所属项目范围(权限细化)
            List<Project> project_relation = authorizationProvider.getAuthorizationProjectsByAppIdAndTargets(IdentityType.ORDINARY.getCode(), com.everhomes.entity.EntityType.SERVICE_MODULE_APP.getCode(), moduleId, appId, targets);
            for (Project project: project_relation) {
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

        List<Long> projectIds  = new ArrayList<>();

        //标准版和定制版之分
        if(namespacesService.isStdNamespace(UserContext.getCurrentNamespaceId())){
            //获取公司管理的全部项目
            List<ServiceModuleAppAuthorization> serviceModuleAppAuthorizations = serviceModuleAppAuthorizationService.listCommunityRelationOfOrgIdAndAppId(UserContext.getCurrentNamespaceId(), organizationId, appId);
            if(serviceModuleAppAuthorizations != null){
                projectIds = serviceModuleAppAuthorizations.stream().map(r -> r.getProjectId()).collect(Collectors.toList());
            }

        }else {
            List<Community> communities = communityProvider.listCommunitiesByNamespaceId(UserContext.getCurrentNamespaceId());

            if(communities != null){
                projectIds = communities.stream().map(r -> r.getId()).collect(Collectors.toList());
            }
        }

        // 获取到了授权项目
        if(projectIds != null && projectIds.size() >0){
            List<ProjectDTO> projectDtos = new ArrayList<>();
            for (Long projectId : projectIds) {
                Community community = communityProvider.findCommunityById(projectId);
                if (null == community) {
                    continue;
                }

                if (LOGGER.isDebugEnabled())
                    LOGGER.info("community:" + community);

                if (community.getNamespaceId().equals(UserContext.getCurrentNamespaceId())) {
                    ProjectDTO dto = new ProjectDTO();
                    dto.setProjectId(community.getId());
                    dto.setProjectName(community.getName());
                    dto.setProjectType(com.everhomes.entity.EntityType.COMMUNITY.getCode());
                    dto.setCommunityType(community.getCommunityType());
                    projectDtos.add(dto);
                }
            }

            List<Long> orgAllCommunityIds = projectDtos.stream().map(r->r.getProjectId()).collect(Collectors.toList());


            if(allProjectFlag){
                return projectDtos;
            }

            //其他权限 去重
            List<Long> ids = new ArrayList<>();
            List<ProjectDTO> processDtos = new ArrayList<>();
            dtos.stream().map(r->{
                if (!ids.contains(r.getProjectId())){
                    ids.add(r.getProjectId());
                    processDtos.add(r);
                }
                return null;
            }).collect(Collectors.toList());

            return processDtos.stream().filter(r->orgAllCommunityIds.contains(r.getProjectId())).collect(Collectors.toList());
        }

        return new ArrayList<>();
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
            project.setCommunityType(community.getCommunityType());
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
        List<ServiceModule> list = serviceModuleProvider.listServiceModule(null, ServiceModuleType.PARK.getCode());

        if(namespaceId != Namespace.DEFAULT_NAMESPACE){
            List<ServiceModuleScope> scopes = serviceModuleProvider.listServiceModuleScopes(namespaceId, ownerType, ownerId, ServiceModuleScopeApplyPolicy.REVERT.getCode());
            if (null == scopes || scopes.size() == 0) {
                scopes = serviceModuleProvider.listServiceModuleScopes(namespaceId, null, null, ServiceModuleScopeApplyPolicy.REVERT.getCode());
            }
            if (scopes.size() != 0) {
            	//临时解决办法，未来应该使用应用来逻辑闭环 by janson
            	ServiceModuleScope s1 = new ServiceModuleScope();
            	s1.setId(200l);
            	s1.setModuleId(200l);
            	s1.setNamespaceId(namespaceId);
            	s1.setApplyPolicy(ServiceModuleScopeApplyPolicy.REVERT.getCode());
            	scopes.add(s1);
                list = filterList(list, scopes);
            }
        }

        List<ServiceModuleDTO> temp = list.stream().map(r -> {
            ServiceModuleDTO dto = ConvertHelper.convert(r, ServiceModuleDTO.class);
            return dto;
        }).collect(Collectors.toList());
        return temp;
    }

    @Override
    public ReflectionServiceModuleApp getOrCreateReflectionServiceModuleApp(Integer namespaceId, String actionData, String instanceConfig, String itemLabel, ServiceModule serviceModule) {
        ReflectionServiceModuleApp reflectionApp = null;
        String customTag = "";
        Long webMenuId = 0L;
        switch (MultipleFlag.fromCode(serviceModule.getMultipleFlag())){
            case NO:
                reflectionApp = this.serviceModuleProvider.findReflectionServiceModuleAppByParam(namespaceId, serviceModule.getId(), null);
                // 取非多入口的模块的菜单id
                List<WebMenu>  webMenus = this.webMenuPrivilegeProvider.listWebMenuByMenuIds(Collections.singletonList(serviceModule.getId()));
                if(webMenus != null && webMenus.size() > 0){
                    webMenuId = webMenus.get(0).getId();
                }
                break;
            case YES:
                String handlerPrefix = PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX;
                PortalPublishHandler handler = PlatformContext.getComponent(handlerPrefix + serviceModule.getId());
                if(null != handler){
                    customTag = handler.getCustomTag(namespaceId, serviceModule.getId(), instanceConfig);
                    LOGGER.debug("get customTag from handler = {}, customTag =s {}",handler,customTag);
                    // 取多入口的模块的菜单id
                    webMenuId = handler.getWebMenuId(namespaceId, serviceModule.getId(), instanceConfig);
                }
                reflectionApp = this.serviceModuleProvider.findReflectionServiceModuleAppByParam(namespaceId, serviceModule.getId(), customTag);
                break;
        }
        if (reflectionApp != null){//更新为有效
            reflectionApp.setName(itemLabel);
            reflectionApp.setStatus(ServiceModuleAppStatus.ACTIVE.getCode());
            reflectionApp.setInstanceConfig(instanceConfig);
            reflectionApp.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            reflectionApp.setActionData(actionData);
            reflectionApp.setCustomTag(customTag);
            reflectionApp.setMenuId(webMenuId);
            this.serviceModuleProvider.updateReflectionServiceModuleApp(reflectionApp);
        }else{//创建
            ReflectionServiceModuleApp reflectionApp_new = new ReflectionServiceModuleApp();
            Long activeAppId = this.sequenceProvider.getNextSequence("activeAppId");
            reflectionApp_new.setActiveAppId(activeAppId);
            reflectionApp_new.setNamespaceId(namespaceId);
            reflectionApp_new.setActionData(actionData);
            reflectionApp_new.setModuleControlType(serviceModule.getModuleControlType());
            reflectionApp_new.setActionType(serviceModule.getActionType());
            reflectionApp_new.setMultipleFlag(serviceModule.getMultipleFlag());
            reflectionApp_new.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            reflectionApp_new.setInstanceConfig(instanceConfig);
            reflectionApp_new.setStatus(ServiceModuleAppStatus.ACTIVE.getCode());
            reflectionApp_new.setCustomTag(customTag);
            reflectionApp_new.setName(itemLabel);
            reflectionApp_new.setModuleId(serviceModule.getId());
            reflectionApp_new.setMenuId(webMenuId);
            this.serviceModuleProvider.createReflectionServiceModuleApp(reflectionApp_new);
        }

        return null;
    }

    /**
     * 获取serviceModule的树形目录（level  = 3）
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
            if (current.getParentId().equals(parentId)
                    && (ServiceModuleCategory.fromCode(current.getCategory()) == ServiceModuleCategory.CLASSIFY || ServiceModuleCategory.fromCode(current.getCategory()) == ServiceModuleCategory.MODULE )) {
                System.out.println(current.getId());
                List<ServiceModuleDTO> c_node = getServiceModuleAsLevelTree(tempList, current.getId());
                current.setServiceModules(c_node);
                results.add(current);
            }
        }
        return results;
    }


    /**
     * 获取serviceModuleApps的树形目录（level  = 2）
     *
     * @param tempList
     * @param parentId
     * @return
     */
    private List<ServiceModuleDTO> getServiceModuleAppsAsLevelTree(List<ServiceModuleDTO> tempList, Long parentId) {
        List<ServiceModuleDTO> results = new ArrayList<>();
        Iterator<ServiceModuleDTO> iter = tempList.iterator();
        while (iter.hasNext()) {
            ServiceModuleDTO current = iter.next();
            if (current.getParentId().equals(parentId)
                    &&  (ServiceModuleCategory.fromCode(current.getCategory()) == ServiceModuleCategory.CLASSIFY || ServiceModuleCategory.fromCode(current.getCategory()) == ServiceModuleCategory.MODULE )) {
                List<ServiceModuleDTO> c_node = getServiceModuleAppsAsLevelTree(tempList, current.getId());
                current.setServiceModules(c_node);
                if (ServiceModuleCategory.fromCode(current.getCategory()) == ServiceModuleCategory.CLASSIFY ){
                    List<Long> moduleIds = c_node.stream().map(ServiceModuleDTO::getId).collect(Collectors.toList());
                    if(moduleIds.size() != 0 && current.getLevel() == 2){
                        //给一级模块设置APPS
                        // 这里因为运营后台的不完善，暂时使用reflectionServiceModuleApps代替真正的serviceModuleApps。原来的代码为serviceModuleAppProvider.listServiceModuleAppsByModuleIds(UserContext.getCurrentNamespaceId(), moduleIds);
//                        List<ServiceModuleAppDTO> apps = serviceModuleProvider.listReflectionServiceModuleAppsByModuleIds(UserContext.getCurrentNamespaceId(), moduleIds);
                        List<ServiceModuleApp> apps = serviceModuleAppService.listReleaseServiceModuleAppByModuleIds(UserContext.getCurrentNamespaceId(), moduleIds);
                        if(apps != null){
                            current.setServiceModuleApps(apps.stream().map(r-> ConvertHelper.convert(r,ServiceModuleAppDTO.class)).collect(Collectors.toList()));
                            LOGGER.debug(current.getName()+ "分类下一共有"+moduleIds.toString() +"的模块和"+ apps.size() + "个应用");
                        }
                    }
                }
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

    private List<ServiceModuleDTO> processServiceModuleDtoTreeToAppTree(List<ServiceModuleDTO> dtos) {
        List dtos_ = dtos.stream().filter(r -> (r.getServiceModules() != null && r.getServiceModules().size() != 0)).map(r -> {
            List<ServiceModuleAppDTO> apps = new ArrayList<>();
            for (ServiceModuleDTO dto : r.getServiceModules()) {
                if (dto.getServiceModuleApps() != null) {
                    apps.addAll(dto.getServiceModuleApps());
                }
            }
            r.setServiceModuleApps(apps);
            return r;
        }).collect(Collectors.toList());
        return dtos_;
    }

    @Override
    public List<Long> listServiceModulefunctions(ListServiceModulefunctionsCommand cmd) {
        List<Long> functionIds = new ArrayList<>();
//        List<Long> privilegeIds = rolePrivilegeService.listUserPrivilegeByModuleId(cmd.getNamespaceId(), EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), UserContext.currentUserId(), cmd.getModuleId());
//        privilegeIds.add(0L);
//        List<ServiceModuleFunction> moduleFunctions = serviceModuleProvider.listFunctions(cmd.getModuleId(), privilegeIds);
//        if(moduleFunctions != null && moduleFunctions.size() > 0) {
//            moduleFunctions.forEach(moduleFunction -> {
//                functionIds.add(moduleFunction.getId());
//            });
//        }
//        List<ServiceModuleExcludeFunction> excludeFunctions = serviceModuleProvider.listExcludeFunctions(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getModuleId());
//        if (excludeFunctions != null && excludeFunctions.size() > 0) {
//            excludeFunctions.forEach(excludeFunction -> {
//                functionIds.remove(excludeFunction.getFunctionId());
//            });
//        }

        //不根据权限
        List<ServiceModuleFunction> moduleFunctions = serviceModuleProvider.listFunctions(cmd.getModuleId(), null);
        if(moduleFunctions != null && moduleFunctions.size() > 0) {
            moduleFunctions.forEach(moduleFunction -> {
                functionIds.add(moduleFunction.getId());
            });
        }
        List<ServiceModuleExcludeFunction> excludeFunctions = serviceModuleProvider.listExcludeFunctions(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getModuleId());
        if (excludeFunctions != null && excludeFunctions.size() > 0) {
            excludeFunctions.forEach(excludeFunction -> {
                functionIds.remove(excludeFunction.getFunctionId());
            });
        }
        //白名单的定义：无论是配置在白名单还是黑名单中的按钮，都要先配置在eh_service_module_functions中，在此基础上，如果配置在白名单中的按钮所在的域空间与当前域空间一致，则显示该按钮。没有配置白名单的域空间则不显示该按钮
        //实现方法是先取出所有的白名单中有的ID，作为准备从所有按钮（functionIds）中去掉的list（withoutWhiteList）。然后取出当前域空间和园区生效的白名单的ID（includeFunctions），并将它从withoutWhiteList中去除，将剩下的withoutWhiteList从functionIds中刨去，则剩下的就是根据白名单保留后的结果
        //group by出所有的在白名单中的按钮ID，作为准备去除的部分
        List<Long> withoutWhiteList = serviceModuleProvider.listExcludeCauseWhiteList();
        //获得当前生效的白名单按钮ID
        List<ServiceModuleIncludeFunction> includeFunctions = serviceModuleProvider.listIncludeFunctions(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getModuleId());
        if(withoutWhiteList != null && withoutWhiteList.size() > 0){
            if (includeFunctions != null && includeFunctions.size() > 0) {
                //将生效的白名单从将要去除的id列表中去除
                includeFunctions.forEach(r -> withoutWhiteList.remove(r.getFunctionId()));

                //将剩下的去除列表中的id从全部生效的按钮id中去除
                /*
                includeFunctions.forEach(includeFunction -> {
                    functionIds.remove(includeFunction.getFunctionId());
                });*/
            }
            withoutWhiteList.forEach(functionIds::remove);

        }


        return functionIds;
    }

    @Override
    public Byte checkUserRelatedProjectAllFlag(ListUserRelatedProjectByModuleCommand cmd) {
        Long userId = cmd.getUserId() != null ?  cmd.getUserId(): UserContext.current().getUser().getId();
        if(checkModuleManage(userId, cmd.getOrganizationId(), cmd.getModuleId())){
            return AllFlag.ALL.getCode();
        }else{
            List<Target> targets = new ArrayList<>();
            targets.add(new Target(com.everhomes.entity.EntityType.USER.getCode(), userId));
            //获取人员的所有相关机构
            List<Long> orgIds = organizationService.getIncludeOrganizationIdsByUserId(userId, cmd.getOrganizationId());
            for (Long orgId: orgIds) {
                targets.add(new Target(com.everhomes.entity.EntityType.ORGANIZATIONS.getCode(), orgId));
            }

            // by lei.lv 取消掉模块管理员的逻辑
//            //获取人员和人员所有机构所赋予模块的所属项目范围(模块管理)
//            List<Project> projects = authorizationProvider.getAuthorizationProjectsByAuthIdAndTargets(EntityType.SERVICE_MODULE.getCode(), cmd.getModuleId(), targets);
//            for (Project project: projects) {
//                //在模块下拥有全部项目权限
//                if(EntityType.ALL == EntityType.fromCode(project.getProjectType())){
//                    return AllFlag.ALL.getCode();
//                }
//            }
            //获取人员和人员所有机构所赋予模块的所属项目范围(应用管理员) -- add by lei.lv
            List<Authorization> authorizations_apps =  authorizationProvider.listAuthorizations(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), EntityType.USER.getCode(), userId, com.everhomes.entity.EntityType.SERVICE_MODULE_APP.getCode(), null, IdentityType.MANAGE.getCode(), true, null, null);
            if(authorizations_apps != null && authorizations_apps.size() > 0){
                authorizations_apps = authorizations_apps.stream().filter(r->ModuleManagementType.fromCode(r.getModuleControlType()) == COMMUNITY_CONTROL).limit(1).collect(Collectors.toList());
                if(authorizations_apps !=null && authorizations_apps.size() > 0) {
                    Authorization authorization = authorizations_apps.get(0);//每一个userId+organizationId在同一个type下只有一个control_id
                    if (ControlTargetOption.fromCode(authorization.getControlOption()) == ALL_COMMUNITY){
                        return AllFlag.ALL.getCode();
                    }
                }
            }
            //获取人员和人员所有机构所赋予模块的所属项目范围(权限细化)
            List<Project> project_relation = authorizationProvider.getAuthorizationProjectsByAppIdAndTargets(IdentityType.ORDINARY.getCode(), com.everhomes.entity.EntityType.SERVICE_MODULE_APP.getCode(), cmd.getModuleId(), cmd.getAppId(), targets);
            for (Project project: project_relation) {
                //在模块下拥有全部项目权限
                if(EntityType.ALL == EntityType.fromCode(project.getProjectType())){
                    return AllFlag.ALL.getCode();
                }
            }
        }

        return AllFlag.NOT_ALL.getCode();
    }

    @Override
    public ServiceModuleAppDTO findServiceModuleAppById(Long id){
        ServiceModuleApp serviceModuleApp = serviceModuleAppProvider.findServiceModuleAppById(id);
        return ConvertHelper.convert(serviceModuleApp, ServiceModuleAppDTO.class);

    }

    public BanPrivilegeHandler getBanPrivilegeHandler() {
        BanPrivilegeHandler handler = null;

        String handlerPrefix = BanPrivilegeHandler.BAN_PRIVILEGE_OBJECT_PREFIX;
        handler = PlatformContext.getComponent(handlerPrefix);

        return handler;
    }

    @Override
    public ListServiceModuleEntriesResponse listServiceModuleEntries(ListServiceModuleEntriesCommand cmd) {
        ListServiceModuleEntriesResponse response = new ListServiceModuleEntriesResponse();
        List<ServiceModuleEntry> entries = serviceModuleEntryProvider.listServiceModuleEntries(cmd.getModuleId(), cmd.getAppCategoryId(), null, null, null);

        List<ServiceModuleEntryDTO> dtos = new ArrayList<>();
        if(entries != null){
            for (ServiceModuleEntry entry: entries){
                ServiceModuleEntryDTO dto = ConvertHelper.convert(entry, ServiceModuleEntryDTO.class);
                if(dto.getIconUri() != null){
                    String url = contentServerService.parserUri(entry.getIconUri(), entry.getClass().getSimpleName(), entry.getId());
                    dto.setIconUrl(url);
                }

                if(dto.getAppCategoryId() != null){
                    AppCategory appCategory = appCategoryProvider.findById(dto.getAppCategoryId());
                    if(appCategory != null){
                        dto.setAppCategoryName(appCategory.getName());
                    }
                }
                dtos.add(dto);
            }
        }

        response.setDtos(dtos);
        return response;
    }

    @Override
    public void updateServiceModuleEntries(UpdateServiceModuleEntriesCommand cmd) {

        if(cmd.getModuleId() == 0){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "moduleId = " + cmd.getModuleId());
        }

        if(cmd.getDtos() == null || cmd.getDtos().size() == 0){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "entry size = 0 ");
        }

        dbProvider.execute((status) ->{

            List<ServiceModuleEntry> entries = serviceModuleEntryProvider.listServiceModuleEntries(cmd.getModuleId(), null, null, null, null);
            if(entries != null){
                for (ServiceModuleEntry entry: entries){
                    serviceModuleEntryProvider.delete(entry.getId());
                }
            }

            for (ServiceModuleEntryDTO dto: cmd.getDtos()){
                ServiceModuleEntry entry = ConvertHelper.convert(dto, ServiceModuleEntry.class);
                entry.setModuleId(cmd.getModuleId());
                serviceModuleEntryProvider.create(entry);
            }

            return null;
        });


    }


    @Override
    public List<ServiceModuleDTO> listServiceModulesByAppType(ListServiceModulesByAppTypeCommand cmd) {

        List<ServiceModule> serviceModules = serviceModuleProvider.listServiceModules(cmd.getAppType(), cmd.getKeyWord());


        if(serviceModules == null){
            return null;
        }

        List<ServiceModuleDTO> dtos = serviceModules.stream().map(r -> processServiceModuleDTO(r)).collect(Collectors.toList());
        return dtos;
    }


    @Override
    public void updateServiceModuleEntry(UpdateServiceModuleEntryCommand cmd) {

        if(cmd.getId() == null){
            LOGGER.error("invalid parameter, cmd = {}.", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameter, cmd = " + cmd.toString());
        }

        ServiceModuleEntry serviceModuleEntry = serviceModuleEntryProvider.findById(cmd.getId());
        if(serviceModuleEntry == null){
            LOGGER.error("serviceModuleEntry not find, cmd = {}.", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "serviceModuleEntry not find cmd = " + cmd.toString());
        }

        AppCategory appCategory = appCategoryProvider.findById(cmd.getAppCategoryId());
        if(appCategory == null || !appCategory.getLocationType().equals(cmd.getLocationType())){
            LOGGER.error("AppCategory not find, cmd = {}.", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "appCategory not find cmd = " + cmd.toString());
        }


        //可以编辑位置信息，这些字段必传
        if(TerminalType.fromCode(cmd.getTerminalType()) == null || ServiceModuleLocationType.fromCode(cmd.getLocationType()) == null
                || ServiceModuleSceneType.fromCode(cmd.getSceneType()) == null){
            LOGGER.error("invalid parameter cmd = {}", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameter cmd = " + cmd);
        }

        //同样的位置不能有两个入口
        List<ServiceModuleEntry> entries = serviceModuleEntryProvider.listServiceModuleEntries(serviceModuleEntry.getModuleId(), null, cmd.getTerminalType(), cmd.getLocationType(), cmd.getSceneType());

        if(entries != null && entries.size() > 0){
            for (ServiceModuleEntry entry: entries){
                if(!entry.getId().equals(cmd.getId())){
                    LOGGER.error("entry already exists");
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "entry already exists");
                }
            }
        }

        serviceModuleEntry.setEntryName(cmd.getEntryName());
        serviceModuleEntry.setIconUri(cmd.getIconUri());
        serviceModuleEntry.setTerminalType(cmd.getTerminalType());
        serviceModuleEntry.setLocationType(cmd.getLocationType());
        serviceModuleEntry.setSceneType(cmd.getSceneType());
        serviceModuleEntry.setAppCategoryId(cmd.getAppCategoryId());

        serviceModuleEntryProvider.udpate(serviceModuleEntry);

    }

    @Override
    public ListAppCategoryResponse listAppCategory(ListAppCategoryCommand cmd) {


        if(cmd.getLocationType() == null){
            LOGGER.error("invalid parameter, cmd = {}.", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameter, cmd = " + cmd.toString());
        }

        if(cmd.getParentId() == null){
            cmd.setParentId(0L);
        }

        ListAppCategoryResponse response = new ListAppCategoryResponse();
        List<AppCategoryDTO> treeDto = getTreeDto(cmd.getLocationType(), cmd.getParentId());
        response.setDtos(treeDto);

        return response;
    }


    private List<AppCategoryDTO> getTreeDto(Byte locationType, Long parentId){
        List<AppCategory> appCategories = appCategoryProvider.listAppCategories(locationType, parentId);
        if(appCategories == null || appCategories.size() == 0){
            return null;
        }

        List<AppCategoryDTO> dtos = new ArrayList<>();
        for(AppCategory appCategory: appCategories){
            AppCategoryDTO dto = ConvertHelper.convert(appCategory, AppCategoryDTO.class);
            List<AppCategoryDTO> subTree = getTreeDto(dto.getLocationType(), dto.getId());
            dto.setDtos(subTree);
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public void updateAppCategory(UpdateAppCategoryCommand cmd) {

        if(cmd.getId() == null){
            LOGGER.error("invalid parameter, cmd = {}.", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameter, cmd = " + cmd.toString());
        }

        AppCategory appCategory = appCategoryProvider.findById(cmd.getId());
        if(appCategory == null){
            LOGGER.error("serviceModuleEntry not find, cmd = {}.", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "serviceModuleEntry not find cmd = " + cmd.toString());
        }

        appCategory.setName(cmd.getName());
        appCategoryProvider.udpate(appCategory);
    }


    @Override
    public AppCategoryDTO createAppCategory(CreateAppCategoryCommand cmd) {

        if(cmd.getParentId() == null){
            cmd.setParentId(0L);
        }else if(cmd.getParentId() != 0L){
            AppCategory appCategory = appCategoryProvider.findById(cmd.getParentId());
            if(appCategory == null || TrueOrFalseFlag.fromCode(appCategory.getLeafFlag()) == TrueOrFalseFlag.TRUE ){

            }
        }




        AppCategory appCategory = ConvertHelper.convert(cmd, AppCategory.class);

        Long maxDefaultOrder = appCategoryProvider.findMaxDefaultOrder(cmd.getLocationType(), cmd.getParentId());
        if(maxDefaultOrder == null){
            maxDefaultOrder = 0L;
        }
        maxDefaultOrder = maxDefaultOrder + 1;
        appCategory.setDefaultOrder(maxDefaultOrder);

        appCategoryProvider.create(appCategory);
        AppCategoryDTO dto = toAppCategoryDTO(appCategory);

        return dto;
    }

    @Override
    public void deleteAppCategory(DeleteAppCategoryCommand cmd) {

        AppCategory appCategory = appCategoryProvider.findById(cmd.getId());
        if(appCategory == null){
            LOGGER.error("appCategory not found, id={}", cmd.getId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "appCategory not found, id=" + cmd.getId());
        }

        List<AppCategory> appCategories = appCategoryProvider.listAppCategories(appCategory.getLocationType(), appCategory.getId());
        if(appCategories != null && appCategories.size() > 0){
            LOGGER.error("it has sub AppCategory, delete denied, id={}", cmd.getId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "it has sub AppCategory, delete denied, id=" + cmd.getId());
        }

        appCategoryProvider.delete(cmd.getId());
    }

    @Override
    public void reorderAppCategory(ReorderAppCategoryCommand cmd) {

        if(cmd.getIds() == null || cmd.getIds().size() == 0 || cmd.getParentId() == null  || cmd.getLocationType() == null){
            LOGGER.error("invalid parameter cmd = {}", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameter cmd = " + cmd);

        }

        dbProvider.execute((status) -> {
            Long order = 1L;
            for (Long id: cmd.getIds()){
                AppCategory appCategory = appCategoryProvider.findById(id);

                if(appCategory == null || !cmd.getLocationType().equals(appCategory.getLocationType()) || !cmd.getParentId().equals(appCategory.getParentId())){
                    LOGGER.error("appCategory parameter and cmd parameter exception, id={}", id);
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "appCategory parameter and cmd parameter exception, id=" + id);
                }

                appCategory.setDefaultOrder(order);
                appCategoryProvider.udpate(appCategory);
                order = order + 1;
            }
            return null;
        });

    }

    @Override
    public void changeServiceModuleEntryCategory(ChangeServiceModuleEntryCategoryCommand cmd) {


        if(cmd.getAppCategoryId() == null || cmd.getEntryIds() == null || cmd.getEntryIds().size() == 0){
            LOGGER.error("error invalid parameter cmd = {}", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "error invalid parameter cmd =  " + cmd);
        }

        AppCategory appCategory = appCategoryProvider.findById(cmd.getAppCategoryId());
        if(appCategory == null){
            LOGGER.error("appCategory not found id = {}", cmd.getAppCategoryId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "appCategory not found id = " + cmd.getAppCategoryId());
        }

        dbProvider.execute((status) ->{

            for (Long id: cmd.getEntryIds()){
                ServiceModuleEntry entry = serviceModuleEntryProvider.findById(id);
                if(entry == null){
                    LOGGER.error("ServiceModuleEntry not found id = {}", id);
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "ServiceModuleEntry not found id = " + id);
                }

                entry.setAppCategoryId(cmd.getAppCategoryId());

                serviceModuleEntryProvider.udpate(entry);
            }

            return null;
        });

    }

    @Override
    public void exportServiceModuleEntries(HttpServletResponse response) {
        List<ServiceModuleEntry> serviceModuleEntries = serviceModuleEntryProvider.listServiceModuleEntries(null, null, null, null, null);

        List<ServiceModuleEntryExportDTO> dtos = new ArrayList<>();
        Integer order = 1;
        for (ServiceModuleEntry entry: serviceModuleEntries){
            ServiceModuleEntryExportDTO exportDto = getExportDto(entry);
            exportDto.setOrder(order);
            order = order + 1;
            dtos.add(exportDto);
        }

        String fileName = String.format("应用入口信息_%s", DateUtil.dateToStr(new Date(), DateUtil.NO_SLASH));
        ExcelUtils excelUtils = new ExcelUtils(response, fileName, "报名信息");
        List<String> propertyNames = new ArrayList<String>(Arrays.asList("order", "moduleName", "appTypeName", "terminalTypeName", "locationTypeName", "sceneTypeName", "entryName", "appCategoryName", "defaultOrder"));
        List<String> titleNames = new ArrayList<String>(Arrays.asList("序号", "功能模块名称", "功能分类", "应用入口终端", "应用入口位置", "应用入口属性", "应用入口名称", "应用入口分类", "分类排序"));

        List<Integer> titleSizes = new ArrayList<Integer>(Arrays.asList(10, 20, 20, 20, 20, 20, 20, 20, 10));

        excelUtils.setNeedSequenceColumn(false);
        excelUtils.writeExcel(propertyNames, titleNames, titleSizes, dtos);
    }

    private ServiceModuleEntryExportDTO getExportDto(ServiceModuleEntry entry){

        ServiceModuleEntryExportDTO dto = ConvertHelper.convert(entry, ServiceModuleEntryExportDTO.class);
        if(entry.getModuleId() != null && entry.getModuleId() != 0){
            ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(entry.getModuleId());
            if(serviceModule != null){
                dto.setModuleName(serviceModule.getName());
                if(ServiceModuleAppType.fromCode(serviceModule.getAppType()) == ServiceModuleAppType.OA){
                    dto.setAppTypeName("企业应用");
                }else if(ServiceModuleAppType.fromCode(serviceModule.getAppType()) == ServiceModuleAppType.COMMUNITY){
                    dto.setAppTypeName("园区应用");
                }else if(ServiceModuleAppType.fromCode(serviceModule.getAppType()) == ServiceModuleAppType.SERVICE){
                    dto.setAppTypeName("服务应用");
                }
            }
        }

        if(TerminalType.fromCode(dto.getTerminalType()) == TerminalType.MOBILE){
            dto.setTerminalTypeName("移动端");
        }else if(TerminalType.fromCode(dto.getTerminalType()) == TerminalType.PC){
            dto.setTerminalTypeName("PC端");
        }

        if(ServiceModuleLocationType.fromCode(dto.getTerminalType()) == ServiceModuleLocationType.MOBILE_COMMUNITY){
            dto.setLocationTypeName("移动端广场");
        }else if(ServiceModuleLocationType.fromCode(dto.getTerminalType()) == ServiceModuleLocationType.MOBILE_WORKPLATFORM){
            dto.setLocationTypeName("移动端工作台");
        }else if(ServiceModuleLocationType.fromCode(dto.getTerminalType()) == ServiceModuleLocationType.PC_INDIVIDUAL){
            dto.setLocationTypeName("PC门户");
        }else if(ServiceModuleLocationType.fromCode(dto.getTerminalType()) == ServiceModuleLocationType.PC_MANAGEMENT){
            dto.setLocationTypeName("PC管理后台");
        }else if(ServiceModuleLocationType.fromCode(dto.getTerminalType()) == ServiceModuleLocationType.PC_WORKPLATFORM){
            dto.setLocationTypeName("PC工作台");
        }


        if(ServiceModuleSceneType.fromCode(dto.getSceneType()) == ServiceModuleSceneType.MANAGEMENT){
            dto.setSceneTypeName("管理端");
        }else if(ServiceModuleSceneType.fromCode(dto.getSceneType()) == ServiceModuleSceneType.CLIENT){
            dto.setSceneTypeName("用户端");
        }


        if(dto.getAppCategoryId() != null){
            AppCategory appCategory = appCategoryProvider.findById(dto.getAppCategoryId());
            if(appCategory != null){
                dto.setAppCategoryName(appCategory.getName());
            }
        }
        return dto;
    }

    @Override
    public void reorderServiceModuleEntries(ReorderServiceModuleEntriesCommand cmd) {


        if(cmd.getAppCategoryId() == null || cmd.getEntryIds() == null || cmd.getEntryIds().size() == 0){
            LOGGER.error("invalid parameter cmd = {}", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameter cmd = " + cmd);
        }

        List<ServiceModuleEntry> serviceModuleEntries = serviceModuleEntryProvider.listServiceModuleEntries(null, cmd.getAppCategoryId(), null, null, null);

        if(serviceModuleEntries != null){

            Integer order = 0;
            for (Long entryId: cmd.getEntryIds()){
                order = order + 1;

                for (ServiceModuleEntry entry: serviceModuleEntries){
                    if(entryId.equals(entry.getId())){
                        entry.setDefaultOrder(order);
                        serviceModuleEntryProvider.udpate(entry);
                        break;
                    }

                }
            }
        }
    }

    private AppCategoryDTO toAppCategoryDTO(AppCategory appCategory){
        AppCategoryDTO dto = ConvertHelper.convert(appCategory, AppCategoryDTO.class);
        return dto;
    }
    

    @Override
	public List<ServiceModuleAppDTO> getModuleApps(Integer namespaceId, Long moduleId) {
		ListServiceModuleAppsCommand cmd = new ListServiceModuleAppsCommand();
		cmd.setNamespaceId(namespaceId);
		cmd.setModuleId(moduleId);
		ListServiceModuleAppsResponse resp = portalService.listServiceModuleApps(cmd);
		if (null == resp || CollectionUtils.isEmpty(resp.getServiceModuleApps())) {
			return null;
		}

		return resp.getServiceModuleApps();
	}

    @Override
    public void deleteServiceModuleEntry(DeleteServiceModuleEntryCommand cmd) {

        ServiceModuleEntry serviceModuleEntry = serviceModuleEntryProvider.findById(cmd.getId());
        if(serviceModuleEntry == null){
            LOGGER.error("serviceModuleEntry not find, cmd = {}.", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "serviceModuleEntry not find cmd = " + cmd.toString());
        }

        serviceModuleEntryProvider.delete(serviceModuleEntry.getId());
    }

    @Override
    public ServiceModuleEntryDTO createServiceModuleEntry(CreateServiceModuleEntryCommand cmd) {

        ServiceModule module = serviceModuleProvider.findServiceModuleById(cmd.getModuleId());

        if(module == null || TerminalType.fromCode(cmd.getTerminalType()) == null || ServiceModuleLocationType.fromCode(cmd.getLocationType()) == null
                || ServiceModuleSceneType.fromCode(cmd.getSceneType()) == null){
            LOGGER.error("invalid parameter cmd = {}", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameter cmd = " + cmd);
        }

        List<ServiceModuleEntry> serviceModuleEntries = serviceModuleEntryProvider.listServiceModuleEntries(cmd.getModuleId(), null, cmd.getTerminalType(), cmd.getLocationType(), cmd.getSceneType());

        if(serviceModuleEntries != null && serviceModuleEntries.size() > 0){
            LOGGER.error("entry already exists");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "entry already exists");
        }

        ServiceModuleEntry serviceModuleEntry = ConvertHelper.convert(cmd, ServiceModuleEntry.class);

        serviceModuleEntry.setDefaultOrder(1000);

        serviceModuleEntryProvider.create(serviceModuleEntry);

        ServiceModuleEntryDTO dto = ConvertHelper.convert(serviceModuleEntry, ServiceModuleEntryDTO.class);
        if(dto.getIconUri() != null){
            String url = contentServerService.parserUri(serviceModuleEntry.getIconUri(), serviceModuleEntry.getClass().getSimpleName(), serviceModuleEntry.getId());
            dto.setIconUrl(url);
        }

        if(dto.getAppCategoryId() != null){
            AppCategory appCategory = appCategoryProvider.findById(dto.getId());
            if(appCategory != null){
                dto.setAppCategoryName(appCategory.getName());
            }
        }

        return dto;

    }

    @Override
    public ListLeafAppCategoryResponse listLeafAppCategory(ListLeafAppCategoryCommand cmd) {

        List<AppCategoryDTO> parentDtos = new ArrayList<>();

        for(ServiceModuleLocationType locationType: ServiceModuleLocationType.values()){

            //接口传来location则使用传来的
            if(cmd.getLocationType() != null && locationType.getCode() != cmd.getLocationType()){
                continue;
            }

            AppCategoryDTO parentDto = new AppCategoryDTO();
            parentDto.setLocationType(locationType.getCode());
            parentDto.setParentId(0L);

            List<AppCategory> appCategories = appCategoryProvider.listLeafAppCategories(locationType.getCode());
            if(appCategories != null && appCategories.size() >= 0){
                List<AppCategoryDTO> dtos = new ArrayList<>();
                for(AppCategory appCategory: appCategories){
                    AppCategoryDTO dto = ConvertHelper.convert(appCategory, AppCategoryDTO.class);
                    dtos.add(dto);
                }

                parentDto.setDtos(dtos);
                parentDtos.add(parentDto);
            }
        }

        ListLeafAppCategoryResponse response = new ListLeafAppCategoryResponse();
        response.setDtos(parentDtos);


        return response;
    }
}
