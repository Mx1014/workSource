// @formatter:off
package com.everhomes.module;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.acl.ServiceModuleCategory;
import com.everhomes.rest.acl.ServiceModuleDTO;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.rest.module.ServiceModuleStatus;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.portal.ServiceModuleAppStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhReflectionServiceModuleAppsDao;
import com.everhomes.server.schema.tables.daos.EhServiceModuleAssignmentRelationsDao;
import com.everhomes.server.schema.tables.daos.EhServiceModuleAssignmentsDao;
import com.everhomes.server.schema.tables.daos.EhServiceModuleExcludeFunctionsDao;
import com.everhomes.server.schema.tables.daos.EhServiceModulesDao;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

import org.apache.tools.ant.taskdefs.condition.And;
import org.jooq.*;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.everhomes.server.schema.tables.EhReflectionServiceModuleApps.EH_REFLECTION_SERVICE_MODULE_APPS;

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
        if (null != privilegeType) {
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
    public ServiceModulePrivilege getServiceModulePrivilegesByModuleIdAndPrivilegeId(Long moduleId, Long privilegeId) {
        List<ServiceModulePrivilege> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhServiceModulePrivilegesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_PRIVILEGES);
        Condition cond = Tables.EH_SERVICE_MODULE_PRIVILEGES.PRIVILEGE_ID.eq(privilegeId);
        cond.and(Tables.EH_SERVICE_MODULE_PRIVILEGES.MODULE_ID.eq(moduleId));
        query.addConditions(cond);
        query.addOrderBy(Tables.EH_SERVICE_MODULE_PRIVILEGES.DEFAULT_ORDER);
        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, ServiceModulePrivilege.class));
            return null;
        });

        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public void updateServiceModuleAssignmentRelation(ServiceModuleAssignmentRelation relation) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhServiceModuleAssignmentRelationsDao dao = new EhServiceModuleAssignmentRelationsDao(context.configuration());
        dao.update(relation);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceModuleAssignmentRelations.class, relation.getId());
    }


    @Override
    public List<ServiceModulePrivilege> listServiceModulePrivilegesByPrivilegeId(Long privilegeId, ServiceModulePrivilegeType privilegeType) {
        List<ServiceModulePrivilege> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhServiceModulePrivilegesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_PRIVILEGES);
        Condition cond = Tables.EH_SERVICE_MODULE_PRIVILEGES.PRIVILEGE_ID.eq(privilegeId);
        if (null != privilegeType) {
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
        if (null != privilegeType) {
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
    public List<ServiceModuleAssignment> listServiceModuleAssignments(Condition condition) {
        List<ServiceModuleAssignment> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhServiceModuleAssignmentsRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_ASSIGNMENTS);
        // 不再以organization作为条件进行判断
        // Condition cond = Tables.EH_SERVICE_MODULE_ASSIGNMENTS.ORGANIZATION_ID.eq(organizationId);
//        if (null != condition) {
//            cond = cond.and(condition);
//        }
        if (null != condition) {
            query.addConditions(condition);
        }
        query.addOrderBy(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_TYPE);
        query.addOrderBy(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_ID);
        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, ServiceModuleAssignment.class));
            return null;
        });
        return results;
    }

    @Override
    public List<ServiceModuleAssignment> listServiceModuleAssignmentByModuleId(String ownerType, Long ownerId, Long moduleId) {
        Condition cond = Tables.EH_SERVICE_MODULE_ASSIGNMENTS.MODULE_ID.eq(moduleId);
        cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_ID.eq(ownerId));
        cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_TYPE.eq(ownerType));
        return listServiceModuleAssignments(cond);
    }

    @Override
    public List<ServiceModuleAssignment> listResourceAssignments(String targetType, Long targetId, Long organizationId, List<Long> moduleIds) {
        List<ServiceModuleAssignment> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhServiceModuleAssignmentsRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_ASSIGNMENTS);
        Condition cond = Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_TYPE.eq(targetType);
        cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_ID.eq(targetId));
        if (null != organizationId) {
            cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.ORGANIZATION_ID.eq(organizationId));
        }
        if (null != moduleIds && moduleIds.size() > 0) {
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

        if (null != organizationId) {
            cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.ORGANIZATION_ID.eq(organizationId));
        }
        if (null != moduleIds && moduleIds.size() > 0) {
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
        if (null != organizationId) {
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
        if (null != organizationId) {
            cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.ORGANIZATION_ID.eq(organizationId));
        }

        if (!org.springframework.util.StringUtils.isEmpty(targetType)) {
            cond = cond.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_TYPE.eq(targetType));
        }
        if (null != targetId) {
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
    public List<ServiceModule> listServiceModule() {
        return listServiceModule("");
    }

    @Override
    public List<ServiceModule> listServiceModule(Integer level, Byte type) {
        List<ServiceModule> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhServiceModules.class));
        SelectQuery<EhServiceModulesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULES);

        Condition cond = Tables.EH_SERVICE_MODULES.STATUS.eq(ServiceModuleStatus.ACTIVE.getCode());
        if (null != level)
            cond = cond.and(Tables.EH_SERVICE_MODULES.LEVEL.eq(level));
        if (null != type)
            cond = cond.and(Tables.EH_SERVICE_MODULES.TYPE.eq(type));

        query.addConditions(cond);
        query.addOrderBy(Tables.EH_SERVICE_MODULES.DEFAULT_ORDER.asc());
        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, ServiceModule.class));
            return null;
        });
        return results;
    }

    @Override
    public List<ServiceModule> listServiceModules(Byte appType, String keyword) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhServiceModules.class));
        SelectQuery<EhServiceModulesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULES);
        query.addConditions(Tables.EH_SERVICE_MODULES.CATEGORY.eq(ServiceModuleCategory.MODULE.getCode()));

        if(appType != null){
            query.addConditions(Tables.EH_SERVICE_MODULES.APP_TYPE.eq(appType));
        }

        if(!org.springframework.util.StringUtils.isEmpty(keyword)){
            Condition condition = Tables.EH_SERVICE_MODULES.NAME.like("%" + keyword.trim() + "%");
            condition = condition.or(Tables.EH_SERVICE_MODULES.ID.like("%" + keyword.trim() + "%"));

            query.addConditions(condition);
        }
        query.addOrderBy(Tables.EH_SERVICE_MODULES.ID.asc());
        List<ServiceModule> results = query.fetch().map((r) -> ConvertHelper.convert(r, ServiceModule.class));
        return results;
    }

    @Override
    public List<ServiceModule> listServiceModulesByMenuAuthFlag(Byte menuAuthFlag) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhServiceModules.class));
        SelectQuery<EhServiceModulesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULES);

        Condition cond = Tables.EH_SERVICE_MODULES.STATUS.eq(ServiceModuleStatus.ACTIVE.getCode());
        cond = cond.and(Tables.EH_SERVICE_MODULES.MENU_AUTH_FLAG.eq(menuAuthFlag));
        query.addConditions(cond);
        List<ServiceModule> results  = query.fetch().map((r) -> ConvertHelper.convert(r, ServiceModule.class));
        return results;
    }

    @Override
    public List<ServiceModule> listServiceModule(String path) {
        List<ServiceModule> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhServiceModules.class));
        SelectQuery<EhServiceModulesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULES);

        Condition cond = Tables.EH_SERVICE_MODULES.STATUS.eq(ServiceModuleStatus.ACTIVE.getCode());
        if (!org.springframework.util.StringUtils.isEmpty(path)) {
            cond = cond.and(Tables.EH_SERVICE_MODULES.PATH.like(path));
        }

        query.addConditions(cond);
        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, ServiceModule.class));
            return null;
        });
        return results;
    }

    @Override
    public List<ServiceModule> listServiceModule(Integer startLevel, List<Byte> types) {
        List<ServiceModule> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhServiceModules.class));
        SelectQuery<EhServiceModulesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULES);

        Condition cond = Tables.EH_SERVICE_MODULES.STATUS.eq(ServiceModuleStatus.ACTIVE.getCode());
        if (null != startLevel)
            cond = cond.and(Tables.EH_SERVICE_MODULES.LEVEL.ge(startLevel));
        if (null != types && types.size() > 0)
            cond = cond.and(Tables.EH_SERVICE_MODULES.TYPE.in(types));

        query.addConditions(cond);
        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, ServiceModule.class));
            return null;
        });
        return results;
    }

    @Override
    public List<ServiceModule> listServiceModule(CrossShardListingLocator locator, Integer pageSize, ListingQueryBuilderCallback queryBuilderCallback) {
        List<ServiceModule> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhServiceModules.class));
        SelectQuery<EhServiceModulesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULES);
        pageSize = pageSize + 1;
        if (null != queryBuilderCallback)
            queryBuilderCallback.buildCondition(locator, query);
        if (null != locator && null != locator.getAnchor())
            query.addConditions(Tables.EH_SERVICE_MODULES.ID.lt(locator.getAnchor()));
        query.addOrderBy(Tables.EH_SERVICE_MODULES.ID.desc());
        query.addLimit(pageSize);
        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, ServiceModule.class));
            return null;
        });
        if (null != locator)
            locator.setAnchor(null);

        if (results.size() >= pageSize) {
            results.remove(results.size() - 1);
            locator.setAnchor(results.get(results.size() - 1).getId());
        }
        return results;
    }

    @Override
    public List<ServiceModule> listServiceModule(List<Long> ids) {
        List<ServiceModule> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhServiceModules.class));
        SelectQuery<EhServiceModulesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULES);
        Condition cond = Tables.EH_SERVICE_MODULES.STATUS.eq(ServiceModuleStatus.ACTIVE.getCode());
        cond = cond.and(Tables.EH_SERVICE_MODULES.ID.in(ids));
        query.addConditions(cond);
        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, ServiceModule.class));
            return null;
        });
        return results;
    }

    @Override
    public List<ServiceModuleDTO> listServiceModuleDtos(List<Long> ids) {
        List<ServiceModuleDTO> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhServiceModules.class));
        SelectQuery<EhServiceModulesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULES);
        Condition cond = Tables.EH_SERVICE_MODULES.STATUS.eq(ServiceModuleStatus.ACTIVE.getCode());
        cond = cond.and(Tables.EH_SERVICE_MODULES.ID.in(ids));
        query.addConditions(cond);
        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, ServiceModuleDTO.class));
            return null;
        });
        return results;
    }

    @Override
    public List<ServiceModule> listServiceModule(Byte actionType) {
        List<ServiceModule> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhServiceModules.class));
        SelectQuery<EhServiceModulesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULES);
        Condition cond = Tables.EH_SERVICE_MODULES.STATUS.eq(ServiceModuleStatus.ACTIVE.getCode());
        cond = cond.and(Tables.EH_SERVICE_MODULES.ACTION_TYPE.eq(actionType));
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
        if (!StringUtils.isEmpty(ownerType))
            cond = cond.and(Tables.EH_SERVICE_MODULE_SCOPES.OWNER_TYPE.eq(ownerType));
        if (null != ownerId)
            cond = cond.and(Tables.EH_SERVICE_MODULE_SCOPES.OWNER_ID.eq(ownerId));
        if (null != applyPolicy)
            cond = cond.and(Tables.EH_SERVICE_MODULE_SCOPES.APPLY_POLICY.eq(applyPolicy));

        query.addConditions(cond);
        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, ServiceModuleScope.class));
            return null;
        });
        return results;
    }

    @Override
    public Long createModuleAssignmentRetion(ServiceModuleAssignmentRelation relation) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceModuleAssignmentRelations.class));
        relation.setId(id);
        EhServiceModuleAssignmentRelationsDao dao = new EhServiceModuleAssignmentRelationsDao(context.configuration());
        dao.insert(relation);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceModuleAssignmentRelations.class, id);
        return id;

    }

    @Override
    public void batchCreateServiceModuleAssignment(List<ServiceModuleAssignment> moduleAssignmentList) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        long id = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhServiceModuleAssignments.class), Long.valueOf(moduleAssignmentList.size()));
        List<EhServiceModuleAssignments> records = new ArrayList<>();
        for (int i = 0; i < moduleAssignmentList.size(); i++) {
            moduleAssignmentList.get(i).setId(id);
            EhServiceModuleAssignments record = ConvertHelper.convert(moduleAssignmentList.get(i), EhServiceModuleAssignments.class);
            records.add(record);
            id++;
        }
        EhServiceModuleAssignmentsDao dao = new EhServiceModuleAssignmentsDao(context.configuration());
        dao.insert(records);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceModuleAssignments.class, null);
    }

    @Override
    public ServiceModuleAssignmentRelation findServiceModuleAssignmentRelationById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhServiceModuleAssignmentRelationsDao dao = new EhServiceModuleAssignmentRelationsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ServiceModuleAssignmentRelation.class);
    }

    @Override
    public List<ServiceModuleAssignment> findServiceModuleAssignmentListByRelationId(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Condition conditon = Tables.EH_SERVICE_MODULE_ASSIGNMENTS.RELATION_ID.eq(id);
        List<ServiceModuleAssignment> list = context.select().from(Tables.EH_SERVICE_MODULE_ASSIGNMENTS).where(conditon).fetch().map(r -> {
            return ConvertHelper.convert(r, ServiceModuleAssignment.class);
        });
        return list;
    }

    @Override
    public void deleteServiceModuleAssignmentRelationById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhServiceModuleAssignmentRelationsDao dao = new EhServiceModuleAssignmentRelationsDao(context.configuration());
        dao.deleteById(id);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceModuleAssignmentRelations.class, id);
    }

    @Override
    public void deleteServiceModuleAssignments(List<ServiceModuleAssignment> assignments) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        List<Long> ids = assignments.stream().map(r -> {
            return r.getId();
        }).collect(Collectors.toList());
        EhServiceModuleAssignmentsDao dao = new EhServiceModuleAssignmentsDao(context.configuration());
        dao.deleteById(ids);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceModuleAssignmentsDao.class, null);

    }

    @Override
    public List<ServiceModuleAssignmentRelation> listServiceModuleAssignmentRelations(String ownerType, Long ownerId) {
        List<ServiceModuleAssignmentRelation> results = new ArrayList<ServiceModuleAssignmentRelation>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        Condition condition = Tables.EH_SERVICE_MODULE_ASSIGNMENT_RELATIONS.OWNER_ID.eq(ownerId).and(Tables.EH_SERVICE_MODULE_ASSIGNMENT_RELATIONS.OWNER_TYPE.endsWith(ownerType));
        SelectQuery<EhServiceModuleAssignmentRelationsRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_ASSIGNMENT_RELATIONS);
        query.addConditions(condition);
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, ServiceModuleAssignmentRelation.class));
            return null;
        });
        return results;
    }

    @Override
    public void createServiceModule(ServiceModule serviceModule) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceModules.class));
        serviceModule.setId(id);
        if (null == serviceModule.getCreateTime())
            serviceModule.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        if (null == serviceModule.getUpdateTime())
            serviceModule.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        if (null == serviceModule.getPath()) {
            serviceModule.setPath("");
        }
        serviceModule.setPath(serviceModule.getPath() + "/" + id);
        EhServiceModulesDao dao = new EhServiceModulesDao(context.configuration());
        dao.insert(serviceModule);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceModules.class, id);
    }

    @Override
    public void updateServiceModule(ServiceModule serviceModule) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        if (null == serviceModule.getUpdateTime())
            serviceModule.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhServiceModulesDao dao = new EhServiceModulesDao(context.configuration());
        dao.update(serviceModule);
    }

    @Override
    public void deleteServiceModuleById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModules.class));
        EhServiceModulesDao dao = new EhServiceModulesDao(context.configuration());
        dao.deleteById(id);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceModules.class, id);
    }

    @Override
    public void createReflectionServiceModuleApp(ReflectionServiceModuleApp reflectionServiceModuleApp) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhReflectionServiceModuleApps.class));
        reflectionServiceModuleApp.setId(id);
        EhReflectionServiceModuleAppsDao dao = new EhReflectionServiceModuleAppsDao(context.configuration());
        dao.insert(reflectionServiceModuleApp);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhReflectionServiceModuleAppsDao.class, id);
    }

    @Override
    public void updateReflectionServiceModuleApp(ReflectionServiceModuleApp reflectionServiceModuleApp) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        UpdateQuery<EhReflectionServiceModuleAppsRecord> item = context.updateQuery(Tables.EH_REFLECTION_SERVICE_MODULE_APPS);
        item.addValue(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.NAME, reflectionServiceModuleApp.getName());
        item.addValue(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.STATUS, reflectionServiceModuleApp.getStatus());
        item.addValue(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.INSTANCE_CONFIG, reflectionServiceModuleApp.getInstanceConfig());
        item.addValue(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.UPDATE_TIME, reflectionServiceModuleApp.getUpdateTime());
        item.addValue(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.ACTION_DATA, reflectionServiceModuleApp.getActionData());
        item.addValue(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.CUSTOM_TAG, reflectionServiceModuleApp.getCustomTag());
        item.addValue(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.CUSTOM_PATH, reflectionServiceModuleApp.getCustomPath());
        item.addValue(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.MENU_ID, reflectionServiceModuleApp.getMenuId());
        item.addConditions(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.ID.eq(reflectionServiceModuleApp.getId()));
        item.setReturning(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.ID);
        item.execute();
    }

    @Override
    public ReflectionServiceModuleApp findReflectionServiceModuleAppById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhReflectionServiceModuleAppsDao dao = new EhReflectionServiceModuleAppsDao(context.configuration());
        EhReflectionServiceModuleApps app = dao.findById(id);
        if (app != null) {
            return ConvertHelper.convert(app, ReflectionServiceModuleApp.class);
        }
        return null;
    }

    @Override
    public ReflectionServiceModuleApp findReflectionServiceModuleAppByParam(Integer namespaceId, Long moduleId, String custom_tag) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhReflectionServiceModuleAppsRecord> query = context.selectQuery(Tables.EH_REFLECTION_SERVICE_MODULE_APPS);
        Condition condition = Tables.EH_REFLECTION_SERVICE_MODULE_APPS.NAMESPACE_ID.eq(namespaceId);
        condition = condition.and(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.MODULE_ID.eq(moduleId));
        if (custom_tag != null)
            condition = condition.and(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.CUSTOM_TAG.eq(custom_tag));
        query.addConditions(condition);
        EhReflectionServiceModuleAppsRecord record = query.fetchAny();
        if (record != null) {
            return ConvertHelper.convert(record, ReflectionServiceModuleApp.class);
        }
        return null;
    }

    @Override
    public Long getMaxActiveAppId() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.ACTIVE_APP_ID.max()).from(Tables.EH_REFLECTION_SERVICE_MODULE_APPS).fetchOne().value1();
    }

    @Override
    public void lapseReflectionServiceModuleAppByNamespaceId(Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        context.update(Tables.EH_REFLECTION_SERVICE_MODULE_APPS).set(EH_REFLECTION_SERVICE_MODULE_APPS.STATUS, ServiceModuleAppStatus.INACTIVE.getCode())
                .where(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.NAMESPACE_ID.eq(namespaceId));
    }

    @Override
    public List<ServiceModuleAppDTO> listReflectionServiceModuleAppsByModuleIds(Integer namespaceId, List<Long> moduleIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Condition cond = Tables.EH_REFLECTION_SERVICE_MODULE_APPS.NAMESPACE_ID.eq(namespaceId);
        if (null != moduleIds && moduleIds.size() > 0)
            cond = cond.and(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.MODULE_ID.in(moduleIds));
        List<ReflectionServiceModuleApp> apps = context.select().from(Tables.EH_REFLECTION_SERVICE_MODULE_APPS)
                .where(cond)
                .and(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.STATUS.eq(ServiceModuleAppStatus.ACTIVE.getCode()))
                .orderBy(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.MODULE_ID.asc())
                .fetch().map(r -> ConvertHelper.convert(r, ReflectionServiceModuleApp.class));
        if (apps != null && apps.size() > 0) {
            return apps.stream().map(r -> ReflectionServiceModuleApp.getServiceModuleAppDTO(r)).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<ServiceModuleAppDTO> listReflectionServiceModuleApp(Integer namespaceId, Long moduleId, Byte actionType, String customTag, String customPath, String controlOption) {
        LOGGER.debug("listReflectionServiceModuleApp, namespaceId = {}, moduleId = {}, actionType = {}, customTag = {}, customPath= {}, controlOption = {}", namespaceId, moduleId, actionType, customTag, customPath, controlOption);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Condition cond = Tables.EH_REFLECTION_SERVICE_MODULE_APPS.NAMESPACE_ID.eq(namespaceId);
        if (null != moduleId)
            cond = cond.and(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.MODULE_ID.eq(moduleId));
        if (null != actionType)
            cond = cond.and(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.ACTION_TYPE.eq(actionType));
        if (null != customTag)
            cond = cond.and(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.CUSTOM_TAG.eq(customTag));
        if (null != customPath)
            cond = cond.and(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.CUSTOM_PATH.eq(customPath));
        if (!StringUtils.isEmpty(controlOption))
            cond = cond.and(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.MODULE_CONTROL_TYPE.eq(controlOption));
        List<ReflectionServiceModuleApp> apps = context.select().from(Tables.EH_REFLECTION_SERVICE_MODULE_APPS)
                .where(cond)
                .and(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.STATUS.eq(ServiceModuleAppStatus.ACTIVE.getCode()))
                .orderBy(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.ID.asc())
                .fetch().map(r -> ConvertHelper.convert(r, ReflectionServiceModuleApp.class));
        if (apps != null && apps.size() > 0) {
            return apps.stream().map(r -> ReflectionServiceModuleApp.getServiceModuleAppDTO(r)).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public ServiceModuleApp findReflectionServiceModuleAppByActiveAppId(Long id) {
        assert (id != null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Record r = context.select().from(Tables.EH_REFLECTION_SERVICE_MODULE_APPS).where(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.ACTIVE_APP_ID.eq(id))
                .fetchAny();
        if(r != null){
            return ReflectionServiceModuleApp.getServiceModuleApp(ConvertHelper.convert(r, ReflectionServiceModuleApp.class));
        }
        return null;
    }

    @Override
    public ServiceModuleApp findReflectionServiceModuleAppByMenuId(Long id) {
        assert (id != null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Record r = context.select().from(Tables.EH_REFLECTION_SERVICE_MODULE_APPS).where(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.MENU_ID.eq(id))
                .fetchAny();
        return ReflectionServiceModuleApp.getServiceModuleApp(ConvertHelper.convert(r, ReflectionServiceModuleApp.class));
    }

    @Override
    public List<ServiceModuleAppDTO> listReflectionServiceModuleAppByActiveAppIds(Integer namespaceId, List<Long> appIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Condition cond = Tables.EH_REFLECTION_SERVICE_MODULE_APPS.NAMESPACE_ID.eq(namespaceId);
        if (null != appIds && appIds.size() > 0)
            cond = cond.and(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.ACTIVE_APP_ID.in(appIds));
        List<ReflectionServiceModuleApp> apps = context.select().from(Tables.EH_REFLECTION_SERVICE_MODULE_APPS)
                .where(cond)
                .and(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.STATUS.eq(ServiceModuleAppStatus.ACTIVE.getCode()))
                .orderBy(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.MODULE_ID.asc())
                .fetch().map(r -> ConvertHelper.convert(r, ReflectionServiceModuleApp.class));
        if (apps != null && apps.size() > 0) {
            return apps.stream().map(r -> ReflectionServiceModuleApp.getServiceModuleAppDTO(r)).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Map<Long, ServiceModuleApp> listReflectionAcitveAppIdByNamespaceId(Integer namespaceId) {
        Map<Long, ServiceModuleApp> appMap = new HashMap<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Condition cond = Tables.EH_REFLECTION_SERVICE_MODULE_APPS.NAMESPACE_ID.eq(namespaceId);
        context.select().from(Tables.EH_REFLECTION_SERVICE_MODULE_APPS)
                .where(cond)
                .and(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.STATUS.eq(ServiceModuleAppStatus.ACTIVE.getCode()))
                .orderBy(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.ID.asc())
                .fetch().map(r -> {
            appMap.put(r.getValue(Tables.EH_REFLECTION_SERVICE_MODULE_APPS.MENU_ID), ReflectionServiceModuleApp.getServiceModuleApp(ConvertHelper.convert(r, ReflectionServiceModuleApp.class)));
            return null;
        });
        return appMap;
    }

    @Override
    public List<ServiceModuleFunction> listFunctionsByIds(List<Long> ids) {
        List<ServiceModuleFunction> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhServiceModuleFunctions.class));
        SelectQuery<EhServiceModuleFunctionsRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_FUNCTIONS);
        query.addConditions(Tables.EH_SERVICE_MODULE_FUNCTIONS.ID.in(ids));

        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, ServiceModuleFunction.class));
            return null;
        });
        return results;
    }

    @Override
    public List<ServiceModuleFunction> listFunctions(Long moduleId, List<Long> privilegeIds) {
        List<ServiceModuleFunction> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhServiceModuleFunctions.class));
        SelectQuery<EhServiceModuleFunctionsRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_FUNCTIONS);

        if (moduleId != null)
            query.addConditions(Tables.EH_SERVICE_MODULE_FUNCTIONS.MODULE_ID.eq(moduleId));

        if(privilegeIds != null && privilegeIds.size() > 0)
            query.addConditions(Tables.EH_SERVICE_MODULE_FUNCTIONS.PRIVILEGE_ID.in(privilegeIds));

        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, ServiceModuleFunction.class));
            return null;
        });
        return results;
    }

    @Override
    public List<ServiceModuleExcludeFunction> listExcludeFunctions(Integer namespaceId, Long comunityId, Long moduleId) {
        List<ServiceModuleExcludeFunction> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhServiceModuleExcludeFunctions.class));
        SelectQuery<EhServiceModuleExcludeFunctionsRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS);

        Condition cond = Tables.EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS.NAMESPACE_ID.eq(namespaceId);
        if (comunityId != null)
            cond = cond.and(Tables.EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS.COMMUNITY_ID.eq(comunityId)
                    .or(Tables.EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS.COMMUNITY_ID.eq(0L))
                    .or(Tables.EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS.COMMUNITY_ID.isNull()));
        if (moduleId != null)
            cond = cond.and(Tables.EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS.MODULE_ID.eq(moduleId));

        query.addConditions(cond);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("listExcludeFunctions, sql=" + query.getSQL());
            LOGGER.debug("listExcludeFunctions, bindValues=" + query.getBindValues());
        }
        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, ServiceModuleExcludeFunction.class));
            return null;
        });
        return results;
    }

    @Override
    public List<Long> listExcludeCauseWhiteList(){
        List<Long> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(ServiceModuleIncludeFunction.class));
        SelectQuery<EhServiceModuleIncludeFunctionsRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_INCLUDE_FUNCTIONS);

        context.select(Tables.EH_SERVICE_MODULE_INCLUDE_FUNCTIONS.FUNCTION_ID)
                .from(Tables.EH_SERVICE_MODULE_INCLUDE_FUNCTIONS)
                .groupBy(Tables.EH_SERVICE_MODULE_INCLUDE_FUNCTIONS.FUNCTION_ID)
                .fetch().map(r -> {
            results.add(r.getValue(Tables.EH_SERVICE_MODULE_INCLUDE_FUNCTIONS.FUNCTION_ID));
            return null;
        });
        return results;

    }


    @Override
    public List<ServiceModuleIncludeFunction> listIncludeFunctions(Integer namespaceId, Long communityId, Long moduleId) {
        List<ServiceModuleIncludeFunction> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(ServiceModuleIncludeFunction.class));
        SelectQuery<EhServiceModuleIncludeFunctionsRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_INCLUDE_FUNCTIONS);

        Condition cond = Tables.EH_SERVICE_MODULE_INCLUDE_FUNCTIONS.NAMESPACE_ID.eq(namespaceId);
        Condition cond2 = null;
        if (communityId != null) {
            cond2 = Tables.EH_SERVICE_MODULE_INCLUDE_FUNCTIONS.COMMUNITY_ID.eq(communityId);
            cond2 = cond2.or(Tables.EH_SERVICE_MODULE_INCLUDE_FUNCTIONS.COMMUNITY_ID.eq(0L));
            cond2 = cond2.or(Tables.EH_SERVICE_MODULE_INCLUDE_FUNCTIONS.COMMUNITY_ID.isNull());
            cond = cond.and(cond2);
        }
        if (moduleId != null)
            cond = cond.and(Tables.EH_SERVICE_MODULE_INCLUDE_FUNCTIONS.MODULE_ID.eq(moduleId));

        query.addConditions(cond);

        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, ServiceModuleIncludeFunction.class));
            return null;
        });
        return results;
    }


	@Override
	public void deleteServiceModuleFromBlack(Long Id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleExcludeFunctions.class));
        EhServiceModuleExcludeFunctionsDao dao = new EhServiceModuleExcludeFunctionsDao(context.configuration());
        //dao.deleteById(Id);
        dao.deleteById(Id);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, ServiceModuleExcludeFunction.class, Id);
		
	}

	@Override
	public Long getServiceModuleFromBlackId(Integer namespaceId, Long functionId) {
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(Tables.EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS.ID).from(Tables.EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS)
        		.where(Tables.EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS.NAMESPACE_ID.eq(namespaceId).and(Tables.EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS.FUNCTION_ID.eq(functionId))).fetchOne().value1();
		
	}

	@Override
	public void createServiceModuleExcludeFunction(ServiceModuleExcludeFunction serviceModuleExcludeFunction) {
		
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhServiceModuleExcludeFunctions.class));

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		
		
		EhServiceModuleExcludeFunctionsDao dao = new EhServiceModuleExcludeFunctionsDao(context.configuration());

		serviceModuleExcludeFunction.setId(id);
		/*parkingLot.setCreateTime(new Timestamp(System.currentTimeMillis()));
		parkingLot.setCreatorUid(UserContext.currentUserId());*/

		dao.insert(serviceModuleExcludeFunction);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceModuleExcludeFunctions.class, id);
		
	}
}
