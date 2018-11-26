package com.everhomes.flow;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowStatusType;
import com.everhomes.rest.flow.ListFlowCommand;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhFlowsDao;
import com.everhomes.server.schema.tables.pojos.EhFlows;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RecordHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class FlowProviderImpl implements FlowProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createFlow(Flow obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlows.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlows.class));
        obj.setId(id);
        prepareObj(obj);
        EhFlowsDao dao = new EhFlowsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateFlow(Flow obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlows.class));
        EhFlowsDao dao = new EhFlowsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteFlow(Flow obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlows.class));
        EhFlowsDao dao = new EhFlowsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public Flow getFlowById(Long id) {
        try {
            Flow[] result = new Flow[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlows.class));

            result[0] = context.select().from(Tables.EH_FLOWS)
                    .where(Tables.EH_FLOWS.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, Flow.class);
                    });

            return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<Flow> findFlowVersion(Long flowMainId , Integer namespaceId){
        ListingLocator locator = new ListingLocator();
        List<Flow> flows = this.queryFlows(locator, 0, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(
                    ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_FLOWS.NAMESPACE_ID.eq(namespaceId));
                query.addConditions(Tables.EH_FLOWS.FLOW_MAIN_ID.eq(flowMainId));
                return query;
            }

        });
        if (flows != null && flows.size() > 0) {
            return flows;
        }
        return null;
    }

    @Override
    public List<Flow> queryFlows(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlows.class));
        com.everhomes.server.schema.tables.EhFlows t = Tables.EH_FLOWS;

        SelectQuery<Record> query = context.select(t.fields()).from(t).getQuery();
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if (locator.getAnchor() != null) {
            query.addConditions(t.ID.lt(locator.getAnchor()));
        }

        if (count > 0) {
            query.addLimit(count + 1);
        }
        query.addOrderBy(t.ID.desc());

        LOGGER.debug("query flow sql: {}", query.getSQL(true));

        List<Flow> objs = query.fetch().map((r) -> {
            return RecordHelper.convert(r, Flow.class);
        });

        List<Flow> list = query.fetch().map((r) -> RecordHelper.convert(r, Flow.class));

        if (list.size() > count && count > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

    private void prepareObj(Flow obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
        obj.setStopTime(obj.getCreateTime());
        // obj.setRunTime(new Timestamp(DateHelper.parseDataString("1997-01-01", "yyyy-MM-dd").getTime()));
        obj.setRunTime(new Timestamp(DateHelper.parseDataString("1997-01-01", "yyyy-MM-dd").getTime()));
        obj.setUpdateTime(obj.getCreateTime());
    }

    @Override
    public Flow findFlowByName(Integer namespaceId, Long moduleId,
                               String moduleType, String projectType, Long projectId, Long ownerId, String ownerType, String name) {
        ListingLocator locator = new ListingLocator();
        List<Flow> flows = this.queryFlows(locator, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(
                    ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_FLOWS.NAMESPACE_ID.eq(namespaceId));
                query.addConditions(Tables.EH_FLOWS.MODULE_ID.eq(moduleId));
                if (moduleType != null) {
                    query.addConditions(Tables.EH_FLOWS.MODULE_TYPE.eq(moduleType));
                }
                if (projectType != null) {
                    query.addConditions(Tables.EH_FLOWS.PROJECT_TYPE.eq(projectType));
                }
                if (projectId != null) {
                    query.addConditions(Tables.EH_FLOWS.PROJECT_ID.eq(projectId));
                }
                query.addConditions(Tables.EH_FLOWS.OWNER_TYPE.eq(ownerType));
                query.addConditions(Tables.EH_FLOWS.OWNER_ID.eq(ownerId));
                query.addConditions(Tables.EH_FLOWS.FLOW_NAME.eq(name));
                query.addConditions(Tables.EH_FLOWS.STATUS.ne(FlowStatusType.INVALID.getCode()));
                query.addConditions(Tables.EH_FLOWS.FLOW_MAIN_ID.eq(0l)); // Got a main flow, not snapshot flow.
                return query;
            }

        });

        if (flows != null && flows.size() > 0) {
            return flows.get(0);
        }

        return null;
    }

    @Override
    public List<Flow> findFlowsByModule(ListingLocator locator, ListFlowCommand cmd) {
        if (locator.getAnchor() == null) {
            locator.setAnchor(cmd.getPageAnchor());
        }
        if (cmd.getModuleType() == null) {
            cmd.setModuleType(FlowModuleType.NO_MODULE.getCode());
        }
        if (cmd.getSnapshotFlag() != null) {
            return querySnapshotFlow(locator, cmd);
        } else {
            return queryConfigFlow(locator, cmd);
        }
    }

    private List<Flow> querySnapshotFlow(ListingLocator locator, ListFlowCommand cmd) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhFlows me = Tables.EH_FLOWS.as("me");
        com.everhomes.server.schema.tables.EhFlows other = Tables.EH_FLOWS.as("other");

        SelectQuery<Record> query = context.select(other.fields()).from(me)
                .join(other).on(me.FLOW_MAIN_ID.eq(other.ID)).getQuery();

        buildQueryCmdCond(other, cmd, query);

        query.addConditions(me.FLOW_VERSION.gt(0));

        if (locator.getAnchor() != null) {
            query.addConditions(other.ID.lt(locator.getAnchor()));
        }

        query.addGroupBy(other.ID);
        query.addLimit(cmd.getPageSize());
        query.addOrderBy(other.ID.desc());

        LOGGER.debug("query snapshot flow sql: {}", query.getSQL(true));

        List<Flow> objs = query.fetch().map((r) -> {
            Flow flow = RecordHelper.convert(r, Flow.class);
            flow.setFlowMainId(flow.getId());// TODO: just for api param
            return flow;
        });
        if (objs.size() >= cmd.getPageSize()) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }
        return objs;
    }

    private List<Flow> queryConfigFlow(ListingLocator locator, ListFlowCommand cmd) {
        return this.queryFlows(locator, cmd.getPageSize(), (locator1, query) -> {
            com.everhomes.server.schema.tables.EhFlows t = Tables.EH_FLOWS;

            buildQueryCmdCond(t, cmd, query);

            if (cmd.getFlowVersion() != null) {
                query.addConditions(t.FLOW_MAIN_ID.eq(cmd.getFlowMainId()));
                query.addConditions(t.FLOW_VERSION.eq(cmd.getFlowVersion()));
            } else {
                query.addConditions(t.FLOW_MAIN_ID.eq(0L)); // Got a main flow, not snapshot flow.
            }
            return query;
        });
    }

    private void buildQueryCmdCond(com.everhomes.server.schema.tables.EhFlows t, ListFlowCommand cmd, SelectQuery<? extends Record> query) {
        query.addConditions(t.NAMESPACE_ID.eq(cmd.getNamespaceId()));
        if (cmd.getModuleId() != null) {
            query.addConditions(t.MODULE_ID.eq(cmd.getModuleId()));
            query.addConditions(t.MODULE_TYPE.eq(cmd.getModuleType()));
        }
        if (cmd.getOwnerId() != null) {
            query.addConditions(t.OWNER_ID.eq(cmd.getOwnerId()));
        }
        if (cmd.getOwnerType() != null) {
            query.addConditions(t.OWNER_TYPE.eq(cmd.getOwnerType()));
        }
        if (cmd.getProjectId() != null) {
            query.addConditions(t.PROJECT_ID.eq(cmd.getProjectId()));
        }
        if (cmd.getProjectType() != null) {
            query.addConditions(t.PROJECT_TYPE.eq(cmd.getProjectType()));
        }
        //多管理公司
        if (cmd.getOrgId() != null) {
            query.addConditions(t.ORGANIZATION_ID.eq(cmd.getOrgId()));
        }

        query.addConditions(t.STATUS.ne(FlowStatusType.INVALID.getCode()));
    }

    @Override
    public void flowMarkUpdated(Flow flow) {
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        flow.setUpdateTime(now);
        if (!flow.getConfigStatus().equals(FlowStatusType.CONFIG.getCode())) {
            // update new version
            // flow.setFlowVersion(flow.getFlowVersion() + 1);
            flow.setConfigStatus(FlowStatusType.CONFIG.getCode());
        }
        updateFlow(flow);
    }

    @Override
    public Flow findSnapshotFlow(Long flowId, Integer flowVer) {
        ListingLocator locator = new ListingLocator();
        List<Flow> flows = this.queryFlows(locator, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(
                    ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_FLOWS.STATUS.ne(FlowStatusType.INVALID.getCode()));
                query.addConditions(Tables.EH_FLOWS.FLOW_MAIN_ID.eq(flowId));
                query.addConditions(Tables.EH_FLOWS.FLOW_VERSION.eq(flowVer));

                return query;
            }

        });

        if (flows == null || flows.size() == 0) {
            return null;
        }

        return flows.get(0);
    }

    @Override
    public Flow getEnabledConfigFlow(Integer namespaceId, String projectType, Long projectId, Long moduleId, String moduleType, Long ownerId, String ownerType) {
        List<Flow> flows = this.queryFlows(new ListingLocator(), 1, (locator, query) -> {

            query.addConditions(Tables.EH_FLOWS.NAMESPACE_ID.eq(namespaceId));
            if (projectType != null && projectId != null) {
                query.addConditions(Tables.EH_FLOWS.PROJECT_TYPE.eq(projectType));
                query.addConditions(Tables.EH_FLOWS.PROJECT_ID.eq(projectId));
            }

            query.addConditions(Tables.EH_FLOWS.MODULE_ID.eq(moduleId));
            if (moduleType != null) {
                query.addConditions(Tables.EH_FLOWS.MODULE_TYPE.eq(moduleType));
            }
            if (ownerId != null) {
                query.addConditions(Tables.EH_FLOWS.OWNER_ID.eq(ownerId));
            }
            if (StringUtils.isNotBlank(ownerType)) {
                query.addConditions(Tables.EH_FLOWS.OWNER_TYPE.eq(ownerType));
            }
            query.addConditions(Tables.EH_FLOWS.STATUS.eq(FlowStatusType.RUNNING.getCode()));
            query.addConditions(Tables.EH_FLOWS.FLOW_MAIN_ID.eq(0L));
            query.addOrderBy(Tables.EH_FLOWS.RUN_TIME.desc());

            return query;
        });

        if (flows != null && flows.size() > 0) {
            return flows.get(0);
        }
        return null;
    }

    @Override
    public List<Flow> listConfigFlowByCond(Integer namespaceId, String moduleType, Long moduleId, String projectType, Long projectId, String ownerType, Long ownerId) {
        return this.queryFlows(new ListingLocator(), -1, (locator, query) -> {
            com.everhomes.server.schema.tables.EhFlows t = Tables.EH_FLOWS;
            query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
            if (moduleType != null) {
                query.addConditions(t.MODULE_TYPE.eq(moduleType));
            }
            query.addConditions(t.MODULE_ID.eq(moduleId));
            query.addConditions(t.PROJECT_TYPE.eq(projectType));
            query.addConditions(t.PROJECT_ID.eq(projectId));
            query.addConditions(t.OWNER_TYPE.eq(ownerType));
            query.addConditions(t.OWNER_ID.eq(ownerId));
            query.addConditions(t.STATUS.ne(FlowStatusType.INVALID.getCode()));
            query.addConditions(t.FLOW_MAIN_ID.eq(0L));
            return query;
        });
    }

    @Override
    public Flow getSnapshotFlowById(Long flowId) {
        List<Flow> flows = this.queryFlows(new ListingLocator(), 1, (locator, query) -> {
            query.addConditions(Tables.EH_FLOWS.FLOW_MAIN_ID.eq(flowId));
            query.addOrderBy(Tables.EH_FLOWS.FLOW_VERSION.desc());
            return query;
        });

        if (flows != null && flows.size() > 0) {
            return flows.get(0);
        }
        return null;
    }
}
