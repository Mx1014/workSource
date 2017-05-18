package com.everhomes.module;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;






import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;






import com.everhomes.acl.AclProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.ResourceCategory;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.rest.acl.ListServiceModulePrivilegesCommand;
import com.everhomes.rest.acl.ListServiceModulesCommand;
import com.everhomes.rest.acl.ServiceModuleAssignmentRelationDTO;
import com.everhomes.rest.acl.ServiceModuleDTO;
import com.everhomes.rest.acl.ServiceModuleTreeVType;
import com.everhomes.rest.common.EntityType;
import com.everhomes.rest.module.AssignmentServiceModuleCommand;
import com.everhomes.rest.module.AssignmentTarget;
import com.everhomes.rest.module.DeleteServiceModuleAssignmentRelationCommand;
import com.everhomes.rest.module.ListServiceModuleAssignmentRelationsCommand;
import com.everhomes.rest.module.Project;
import com.everhomes.rest.module.ServiceModuleOut;
import com.everhomes.rest.module.ServiceModuleScopeApplyPolicy;
import com.everhomes.rest.module.ServiceModuleType;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.google.gson.reflect.TypeToken;

@Service
public class ServiceModuleServiceImpl implements ServiceModuleService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModuleServiceImpl.class);

	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private ServiceModuleProvider serviceModuleProvider;

	@Autowired
	private AclProvider aclProvider;

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
		List<ServiceModuleScope> scopes = serviceModuleProvider.listServiceModuleScopes(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), ServiceModuleScopeApplyPolicy.REVERT.getCode());

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

		List<ServiceModuleDTO> result = new ArrayList<ServiceModuleDTO>();

		for (ServiceModuleDTO s : temp) {
			getChildServiceModules(temp, s);
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

		List<ServiceModule> serviceModules = serviceModuleProvider.listServiceModule(startLevel, types);

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
		outer: for (ServiceModule m : modules) {
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
		if (cmd.getTargets() == null || cmd.getProjects() == null || cmd.getModuleIds() == null) {
			LOGGER.error("AssignmentServiceModuleCommand is not completed. cmd = {]", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "AssignmentServiceModuleCommand is not completed.");
		}

		List<AssignmentTarget> targets = cmd.getTargets();
		List<Project> projects = cmd.getProjects();
		List<Long> moduleIds = cmd.getModuleIds();
		// 1.先保存relation表的一条记录
		ServiceModuleAssignmentRelation relation = new ServiceModuleAssignmentRelation();
		relation.setOwnerId(cmd.getOwnerId());
		relation.setOwnerType(cmd.getOwnerType());
		relation.setAllModuleFlag(cmd.getAllModuleFlag());
		relation.setTargetJson(StringHelper.toJsonString(targets));
		relation.setOwnerJson(StringHelper.toJsonString(projects));
		relation.setModuleJson(StringHelper.toJsonString(moduleIds));
		relation.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		relation.setUpdateUid(UserContext.current().getUser().getId());
		relation.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		relation.setCreateUid(UserContext.current().getUser().getId());

		// 2.再保存assigment表的多条记录
		Long relation_id = this.serviceModuleProvider.createModuleAssignmentRetion(relation);
		List<ServiceModuleAssignment> assignmentList = new ArrayList<ServiceModuleAssignment>();
		for (AssignmentTarget target : targets) {
			if(target.getTargetId() == null || target.getTargetType() == null){
				LOGGER.error("target is illegal. cmd = {}",cmd);
				break;
			}
			for (Long moduleId : moduleIds) {
				if(moduleId == null){
					LOGGER.error("moduleId is illegal. cmd = {}",cmd);
					break;
				}
				for (Project project : projects) {
					if(project.getProjectId() == null || project.getProjectType() == null){
						LOGGER.error("project is illegal. cmd = {}",cmd);
						break;
					}
					ServiceModuleAssignment assignment = new ServiceModuleAssignment();
					assignment.setNamespaceId(UserContext.getCurrentNamespaceId());
					// !后补
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
					assignment.setAllModuleFlag(cmd.getAllModuleFlag());
					assignment.setIncludeChildFlag(target.getIncludeChildFlag());
					assignment.setRelationId(relation_id);
					assignmentList.add(assignment);
				}
			}
		}

		this.serviceModuleProvider.batchCreateServiceModuleAssignment(assignmentList);
	}

	@Override
	public void deleteServiceModuleAssignmentRelation(DeleteServiceModuleAssignmentRelationCommand cmd) {
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

	}

	@Override
	public List<ServiceModuleAssignmentRelationDTO> listServiceModuleAssignmentRelations(ListServiceModuleAssignmentRelationsCommand cmd) {

		Type targetType = new TypeToken<List<AssignmentTarget>>() {}.getType();
		Type projectType = new TypeToken<List<Project>>() {}.getType();
		Type modulesType = new TypeToken<List<Long>>() {}.getType();

		if (cmd.getOwnerId() == null || cmd.getOwnerType() == null) {
			LOGGER.error("ListServiceModuleAssignmentRelationsCommand is not completed. cmd = {}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "ListServiceModuleAssignmentRelationsCommand is not matched.");
		}

		// 根据owenerType和owenerId查询出多条relatios表记录
		List<ServiceModuleAssignmentRelation> relations = this.serviceModuleProvider.listServiceModuleAssignmentRelations(cmd.getOwnerType(), cmd.getOwnerId());
		if (relations == null || relations.size() == 0) {
			LOGGER.error("ServiceModuleAssignmentRelations is not found. cmd = {}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "ServiceModuleAssignmentRelations is not found.");
		}
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
					Organization organization= this.organizationProvider.findOrganizationById(target.getTargetId());
					if(organization == null){
						LOGGER.error("JsonParse Organization is not matched. cmd = {}", cmd);
						throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "JsonParse Organization is not matched.");
					}
					target.setTargetName(organization.getName());
				}
			}
			dto.setTargets(targets);

			//处理owners
			List<Project> owners = GsonUtil.fromJson(r.getOwnerJson(), projectType);
			for (Project owner : owners) {
				// 小区
				if (EntityType.fromCode(owner.getProjectType()) == EntityType.COMMUNITY) {
					Community community = this.communityProvider.findCommunityById(owner.getProjectId());
					if(community == null){
						LOGGER.error("JsonParse Community is not matched. cmd = {}", cmd);
						throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "JsonParse Community is not matched.");
					}
					owner.setProjectName(community.getName());
				} else if (EntityType.fromCode(owner.getProjectType()) == EntityType.RESOURCE_CATEGORY) {// 子项目
					ResourceCategory resourceCategory = this.communityProvider.findResourceCategoryById(owner.getProjectId());
					if(resourceCategory == null){
						LOGGER.error("JsonParse ResourceCategory is not matched. cmd = {}", cmd);
						throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "JsonParse ResourceCategory is not matched.");
					}
					owner.setProjectName(resourceCategory.getName());
				}
			}
			dto.setProjects(owners);

			//处理modules
			List<Long> moduleIds = GsonUtil.fromJson(r.getModuleJson(), modulesType);
			List<ServiceModuleOut> modulesOut = new ArrayList<ServiceModuleOut>();
			for(Long moduleId:moduleIds){
				ServiceModule module = this.serviceModuleProvider.findServiceModuleById(moduleId);
				modulesOut.add(ConvertHelper.convert(module, ServiceModuleOut.class));
			}
			dto.setModules(modulesOut);
			
			return ConvertHelper.convert(dto, ServiceModuleAssignmentRelationDTO.class);
		}).collect(Collectors.toList());

		return results;
	}
}
