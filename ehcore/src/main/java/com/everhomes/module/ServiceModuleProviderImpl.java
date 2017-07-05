// @formatter:off
package com.everhomes.module;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.acl.AuthorizationRelation;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.module.ServiceModuleStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhServiceModuleAssignmentRelationsRecord;
import com.everhomes.server.schema.tables.records.EhServiceModuleAssignmentsRecord;
import com.everhomes.server.schema.tables.records.EhServiceModulePrivilegesRecord;
import com.everhomes.server.schema.tables.records.EhServiceModuleScopesRecord;
import com.everhomes.server.schema.tables.records.EhServiceModulesRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

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
        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, ServiceModule.class));
            return null;
        });
        return results;
    }

    @Override
    public List<ServiceModule> listServiceModule(String path) {
        List<ServiceModule> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhServiceModules.class));
        SelectQuery<EhServiceModulesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULES);

        Condition cond = Tables.EH_SERVICE_MODULES.STATUS.eq(ServiceModuleStatus.ACTIVE.getCode());
        if(!org.springframework.util.StringUtils.isEmpty(path)){
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
    public List<ServiceModule> listServiceModule(CrossShardListingLocator locator, Integer pageSize, ListingQueryBuilderCallback queryBuilderCallback){
        List<ServiceModule> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhServiceModules.class));
        SelectQuery<EhServiceModulesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULES);

        if(null != queryBuilderCallback)
            queryBuilderCallback.buildCondition(locator, query);
        if(null != locator && null != locator.getAnchor())
            query.addConditions(Tables.EH_SERVICE_MODULES.ID.lt(locator.getAnchor()));
        query.addOrderBy(Tables.EH_SERVICE_MODULES.ID.desc());
        query.addLimit(pageSize);
        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, ServiceModule.class));
            return null;
        });
        if(null!= locator)
            locator.setAnchor(null);

        if(results.size() >= pageSize){
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
        if(null == serviceModule.getCreateTime())
            serviceModule.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        if(null == serviceModule.getUpdateTime())
            serviceModule.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        if(null == serviceModule.getPath()){
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
        if(null == serviceModule.getUpdateTime())
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
}
