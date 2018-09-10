// @formatter:off
package com.everhomes.flow;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.flow.FlowCommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhFlowServiceMappings;
import com.everhomes.server.schema.tables.daos.EhFlowServiceMappingsDao;
import com.everhomes.server.schema.tables.pojos.EhPointActions;
import com.everhomes.server.schema.tables.records.EhFlowServiceMappingsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FlowServiceMappingProviderImpl implements FlowServiceMappingProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createFlowServiceMapping(FlowServiceMapping serviceMapping) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowServiceMappings.class));
        serviceMapping.setId(id);
        serviceMapping.setCreateTime(DateUtils.currentTimestamp());
        serviceMapping.setCreatorUid(UserContext.currentUserId());
        rwDao().insert(serviceMapping);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPointActions.class, id);
    }

    @Override
    public void updateFlowServiceMapping(FlowServiceMapping serviceMapping) {
        rwDao().update(serviceMapping);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPointActions.class, serviceMapping.getId());
    }

    @Override
    public List<FlowServiceMapping> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        EhFlowServiceMappings t = Tables.EH_FLOW_SERVICE_MAPPINGS;
        SelectQuery<EhFlowServiceMappingsRecord> query = context().selectQuery(t);
        if (callback != null) {
            callback.buildCondition(locator, query);
        }
        if (locator.getAnchor() != null) {
            query.addConditions(t.ID.le(locator.getAnchor()));
        }

        if (count > 0) {
            query.addLimit(count + 1);
        }
        query.addOrderBy(t.ID.desc());

        List<FlowServiceMapping> list = query.fetchInto(FlowServiceMapping.class);
        if (list.size() > count && count > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

    @Override
    public FlowServiceMapping findById(Long id) {
        return ConvertHelper.convert(dao().findById(id), FlowServiceMapping.class);
    }

    @Override
    public FlowServiceMapping findConfigMapping(Integer namespaceId, String projectType, Long projectId,
                                                String moduleType, Long moduleId, String ownerType, Long ownerId) {
        EhFlowServiceMappings t = Tables.EH_FLOW_SERVICE_MAPPINGS;
        List<FlowServiceMapping> list = this.query(new ListingLocator(), 1, (locator, query) -> {
            buildCondition(namespaceId, projectType, projectId, moduleType, moduleId, ownerType, ownerId, t, query);
            return query;
        });
        if (list.size() > 0) {
            return list.iterator().next();
        }
        return null;
    }

    private void buildCondition(Integer namespaceId, String projectType, Long projectId, String moduleType, Long moduleId,
                                String ownerType, Long ownerId, EhFlowServiceMappings t, SelectQuery<? extends Record> query) {
        query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(t.STATUS.ne(FlowCommonStatus.INVALID.getCode()));
        if (projectType != null && projectId != null) {
            query.addConditions(t.PROJECT_TYPE.eq(projectType));
            query.addConditions(t.PROJECT_ID.eq(projectId));
        }
        if (moduleType != null && moduleId != null) {
            query.addConditions(t.MODULE_TYPE.eq(moduleType));
            query.addConditions(t.MODULE_ID.eq(moduleId));
        }
        if (ownerType != null && ownerId != null) {
            query.addConditions(t.OWNER_TYPE.eq(ownerType));
            query.addConditions(t.OWNER_ID.eq(ownerId));
        }
    }

    @Override
    public List<FlowServiceMapping> listFlowServiceMapping(Integer namespaceId, String projectType, Long projectId,
                                                           String moduleType, Long moduleId, String ownerType, Long ownerId) {
        EhFlowServiceMappings t = Tables.EH_FLOW_SERVICE_MAPPINGS;
        return this.query(new ListingLocator(), -1, (locator, query) -> {
            buildCondition(namespaceId, projectType, projectId, moduleType, moduleId, ownerType, ownerId, t, query);
            return query;
        });
    }

    @Override
    public void deleteFlowServiceMappingByFlowMainId(Long flowMainId) {
        EhFlowServiceMappings t = Tables.EH_FLOW_SERVICE_MAPPINGS;
        rwContext().update(t).set(t.STATUS, FlowCommonStatus.INVALID.getCode()).execute();
    }

    @Override
    public List<FlowServiceMapping> listFlowServiceMappingByFlowMainId(Long flowMainId, Integer flowVersion) {
        EhFlowServiceMappings t = Tables.EH_FLOW_SERVICE_MAPPINGS;
        return this.query(new ListingLocator(), -1, (locator, query) -> {
            query.addConditions(t.FLOW_MAIN_ID.eq(flowMainId));
            query.addConditions(t.FLOW_VERSION.eq(flowVersion));
            return query;
        });
    }

    private EhFlowServiceMappingsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhFlowServiceMappingsDao(context.configuration());
    }

    private EhFlowServiceMappingsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhFlowServiceMappingsDao(context.configuration());
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    private DSLContext rwContext() {
        return dbProvider.getDslContext(AccessSpec.readWrite());
    }
}
