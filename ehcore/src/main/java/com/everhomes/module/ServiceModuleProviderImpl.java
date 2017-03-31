// @formatter:off
package com.everhomes.module;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleAssignment;
import com.everhomes.module.ServiceModulePrivilege;
import com.everhomes.module.ServiceModulePrivilegeType;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.module.ServiceModuleScope;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.module.ServiceModuleStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhServiceModules;
import com.everhomes.server.schema.tables.daos.EhServiceModuleAssignmentsDao;
import com.everhomes.server.schema.tables.daos.EhServiceModulesDao;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleAssignments;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleScopes;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceModuleProviderImpl implements ServiceModuleProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModuleProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public List<ServiceModulePrivilege> listServiceModulePrivileges(Long moduleId, ServiceModulePrivilegeType privilegeType) {
		List<ServiceModulePrivilege> results = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhServiceModulePrivilegesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_PRIVILEGES);
		Condition cond = Tables.EH_SERVICE_MODULE_PRIVILEGES.MODULE_ID.eq(moduleId);
		if(null != privilegeType){
			cond = cond.and(Tables.EH_SERVICE_MODULE_PRIVILEGES.PRIVILEGE_TYPE.eq(privilegeType.getCode()));
		}
		query.addConditions(cond);
		query.addOrderBy(Tables.EH_SERVICE_MODULE_PRIVILEGES.DEFAULT_ORDER);

		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, ServiceModulePrivilege.class));
			return null;
		});
		return results;
	}

	@Override
	public List<ServiceModulePrivilege> listServiceModulePrivilegesByPrivilegeId(Long privilegeId, ServiceModulePrivilegeType privilegeType) {
		List<ServiceModulePrivilege> results = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhServiceModulePrivilegesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_PRIVILEGES);
		Condition cond = Tables.EH_SERVICE_MODULE_PRIVILEGES.PRIVILEGE_ID.eq(privilegeId);
		if(null != privilegeType){
			cond = cond.and(Tables.EH_SERVICE_MODULE_PRIVILEGES.PRIVILEGE_TYPE.eq(privilegeType.getCode()));
		}
		query.addConditions(cond);
		query.addOrderBy(Tables.EH_SERVICE_MODULE_PRIVILEGES.DEFAULT_ORDER);

		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, ServiceModulePrivilege.class));
			return null;
		});
		return results;
	}

	@Override
	public List<ServiceModulePrivilege> listServiceModulePrivileges(List<Long> moduleIds, ServiceModulePrivilegeType privilegeType) {
		List<ServiceModulePrivilege> results = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhServiceModulePrivilegesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_PRIVILEGES);
		Condition cond = Tables.EH_SERVICE_MODULE_PRIVILEGES.MODULE_ID.in(moduleIds);
		if(null != privilegeType){
			cond = cond.and(Tables.EH_SERVICE_MODULE_PRIVILEGES.PRIVILEGE_TYPE.eq(privilegeType.getCode()));
		}
		query.addConditions(cond);
		query.addOrderBy(Tables.EH_SERVICE_MODULE_PRIVILEGES.DEFAULT_ORDER);

		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, ServiceModulePrivilege.class));
			return null;
		});
		return results;
	}

	@Override
	public Long createServiceModuleAssignment(ServiceModuleAssignment serviceModuleAssignment) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceModuleAssignments.class));
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAssignments.class));
		serviceModuleAssignment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		serviceModuleAssignment.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		serviceModuleAssignment.setId(id);
		EhServiceModuleAssignmentsDao dao = new EhServiceModuleAssignmentsDao(context.configuration());
		dao.insert(serviceModuleAssignment);
		return id;
	}

	@Override
	public void deleteServiceModuleAssignmentById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhServiceModuleAssignmentsDao dao = new EhServiceModuleAssignmentsDao(context.configuration());
		dao.deleteById(id);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceModuleAssignments.class, id);
	}

	@Override
	public ServiceModule findServiceModuleById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhServiceModulesDao dao = new EhServiceModulesDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), ServiceModule.class);
	}

	@Override
	public List<ServiceModuleAssignment> listServiceModuleAssignments(Condition condition, Long organizationId) {
		List<ServiceModuleAssignment> results = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhServiceModuleAssignmentsRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_ASSIGNMENTS);
		Condition cond = Tables.EH_SERVICE_MODULE_ASSIGNMENTS.ORGANIZATION_ID.eq(organizationId);
		if(null != condition){
			cond = cond.and(condition);
		}
		query.addConditions(cond);
		query.addOrderBy(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_TYPE);
		query.addOrderBy(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_ID);
		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, ServiceModuleAssignment.class));
			return null;
		});
		return results;
	}

	@Override
	public List<ServiceModuleAssignment> listServiceModuleAssignmentByModuleId(String ownerType, Long ownerId, Long organizationId, Long moduleId){
		Condition cond = Tables.EH_SERVICE_MODULE_ASSIGNMENTS.MODULE_ID.eq(moduleId);
		cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_ID.eq(ownerId));
		cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_TYPE.eq(ownerType));
		return listServiceModuleAssignments(cond, organizationId);
	}

	@Override
	public List<ServiceModuleAssignment> listResourceAssignments(String targetType, Long targetId, Long organizationId, List<Long> moduleIds) {
		List<ServiceModuleAssignment> results = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhServiceModuleAssignmentsRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_ASSIGNMENTS);
		Condition cond = Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_TYPE.eq(targetType);
		cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_ID.eq(targetId));
		if(null != organizationId){
			cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.ORGANIZATION_ID.eq(organizationId));
		}
		if(null != moduleIds && moduleIds.size() >0){
			cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.MODULE_ID.in(moduleIds));
		}
		query.addConditions(cond);
		query.addGroupBy(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_TYPE);
		query.addGroupBy(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_ID);
		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, ServiceModuleAssignment.class));
			return null;
		});
		return results;
	}

	@Override
	public List<ServiceModuleAssignment> listResourceAssignments(String targetType, List<Long> targetIds, Long organizationId, List<Long> moduleIds) {
		List<ServiceModuleAssignment> results = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhServiceModuleAssignmentsRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_ASSIGNMENTS);
		Condition cond = Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_TYPE.eq(targetType);
		cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_ID.in(targetIds));

		if(null != organizationId){
			cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.ORGANIZATION_ID.eq(organizationId));
		}
		if(null != moduleIds && moduleIds.size() >0){
			cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.MODULE_ID.in(moduleIds));
		}
		query.addConditions(cond);
		query.addGroupBy(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_TYPE);
		query.addGroupBy(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_ID);
		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, ServiceModuleAssignment.class));
			return null;
		});
		return results;
	}

	@Override
	public List<ServiceModuleAssignment> listResourceAssignmentGroupByTargets(String ownerType, Long ownerId, Long organizationId) {
		List<ServiceModuleAssignment> results = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhServiceModuleAssignmentsRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_ASSIGNMENTS);
		Condition cond = Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_TYPE.eq(ownerType);
		cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_ID.eq(ownerId));
		if(null != organizationId){
			cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.ORGANIZATION_ID.eq(organizationId));
		}
		query.addConditions(cond);
		query.addGroupBy(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_ID);
		query.addGroupBy(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_TYPE);
		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, ServiceModuleAssignment.class));
			return null;
		});
		return results;
	}

	@Override
	public List<ServiceModuleAssignment> listServiceModuleAssignmentsByTargetId(String targetType, Long targetId, Long organizationId) {
		List<ServiceModuleAssignment> results = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhServiceModuleAssignmentsRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_ASSIGNMENTS);
		Condition cond = Tables.EH_SERVICE_MODULE_ASSIGNMENTS.ORGANIZATION_ID.eq(organizationId);
		cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_TYPE.eq(targetType));
		cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_ID.eq(targetId));
		query.addConditions(cond);
		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, ServiceModuleAssignment.class));
			return null;
		});
		return results;
	}

	@Override
	public List<ServiceModuleAssignment> listServiceModuleAssignmentsByTargetIdAndOwnerId(String ownerType, Long ownerId, String targetType, Long targetId, Long organizationId) {
		List<ServiceModuleAssignment> results = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhServiceModuleAssignmentsRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_ASSIGNMENTS);
		Condition cond = Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_TYPE.eq(ownerType);
		cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_ID.eq(ownerId));
		if(null != organizationId){
			cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.ORGANIZATION_ID.eq(organizationId));
		}

		if(!org.springframework.util.StringUtils.isEmpty(targetType)){
			cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_TYPE.eq(targetType));
		}
		if(null != targetId){
			cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_ID.eq(targetId));
		}
		query.addConditions(cond);
		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, ServiceModuleAssignment.class));
			return null;
		});

		return results;
	}

	@Override
	public List<ServiceModule> listServiceModule(Integer level, Byte type) {
		List<ServiceModule> results = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhServiceModules.class));
		SelectQuery<EhServiceModulesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULES);
		
		Condition cond = Tables.EH_SERVICE_MODULES.STATUS.eq(ServiceModuleStatus.ACTIVE.getCode());
		if(null != level)
			cond = cond.and(Tables.EH_SERVICE_MODULES.LEVEL.eq(level));
		if(null != type)
			cond = cond.and(Tables.EH_SERVICE_MODULES.TYPE.eq(type));
		
		query.addConditions(cond);
		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, ServiceModule.class));
			return null;
		});
		return results;
	}
	
	@Override
	public List<ServiceModuleScope> listServiceModuleScopes(Integer namespaceId, String ownerType, Long ownerId, Byte applyPolicy) {
		List<ServiceModuleScope> results = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhServiceModuleScopes.class));
		SelectQuery<EhServiceModuleScopesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_SCOPES);
		
		Condition cond = Tables.EH_SERVICE_MODULE_SCOPES.NAMESPACE_ID.eq(namespaceId);
		if(!StringUtils.isEmpty(ownerType))
			cond = cond.and(Tables.EH_SERVICE_MODULE_SCOPES.OWNER_TYPE.eq(ownerType));
		if(null != ownerId)
			cond = cond.and(Tables.EH_SERVICE_MODULE_SCOPES.OWNER_ID.eq(ownerId));
		if(null != applyPolicy)
			cond = cond.and(Tables.EH_SERVICE_MODULE_SCOPES.APPLY_POLICY.eq(applyPolicy));

		query.addConditions(cond);
		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, ServiceModuleScope.class));
			return null;
		});
		return results;
	}
}
