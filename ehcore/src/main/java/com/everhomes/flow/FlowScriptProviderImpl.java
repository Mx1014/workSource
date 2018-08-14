package com.everhomes.flow;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.flow.FlowCommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhFlowScriptsDao;
import com.everhomes.server.schema.tables.pojos.EhFlowScripts;
import com.everhomes.server.schema.tables.records.EhFlowScriptsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FlowScriptProviderImpl implements FlowScriptProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowScriptProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long getNextId() {
        return this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowScripts.class));
    }

    @Override
    public void createFlowScriptWithId(FlowScript obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowScripts.class));

        obj.setCreateTime(DateUtils.currentTimestamp());
        obj.setCreatorUid(UserContext.currentUserId());
        prepareObj(obj);

        EhFlowScriptsDao dao = new EhFlowScriptsDao(context.configuration());
        dao.insert(obj);
    }

    @Override
    public void updateFlowScript(FlowScript obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowScripts.class));
        EhFlowScriptsDao dao = new EhFlowScriptsDao(context.configuration());
        obj.setUpdateTime(DateUtils.currentTimestamp());
        obj.setUpdateUid(UserContext.currentUserId());
        dao.update(obj);
    }

    @Override
    public FlowScript getFlowScriptById(Long id) {
        try {
            FlowScript[] result = new FlowScript[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowScripts.class));

            result[0] = context.select().from(Tables.EH_FLOW_SCRIPTS)
                    .where(Tables.EH_FLOW_SCRIPTS.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, FlowScript.class);
                    });

            return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<FlowScript> queryFlowScripts(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowScripts.class));

        SelectQuery<EhFlowScriptsRecord> query = context.selectQuery(Tables.EH_FLOW_SCRIPTS);
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if (locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FLOW_SCRIPTS.ID.gt(locator.getAnchor()));
        }

        query.addLimit(count + 1);

        List<FlowScript> objs = query.fetchInto(FlowScript.class);
        if (objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    @Override
    public FlowScript findNewestFlowScript(Long scriptMainId) {
        com.everhomes.server.schema.tables.EhFlowScripts t = Tables.EH_FLOW_SCRIPTS;
        List<FlowScript> flowScripts = queryFlowScripts(new ListingLocator(), 1, (locator, query) -> {
            query.addConditions(t.SCRIPT_MAIN_ID.eq(scriptMainId));
            query.addOrderBy(t.SCRIPT_VERSION.desc());
            return query;
        });
        if (flowScripts.size() > 0) {
            return flowScripts.iterator().next();
        }
        return null;
    }

    private void prepareObj(FlowScript obj) {

    }

    @Override
    public FlowScript findByMainIdAndVersion(Long scriptMainId, Integer scriptVersion) {
        com.everhomes.server.schema.tables.EhFlowScripts t = Tables.EH_FLOW_SCRIPTS;
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(t)
                .where(t.SCRIPT_MAIN_ID.eq(scriptMainId))
                .and(t.SCRIPT_VERSION.eq(scriptVersion))
                .fetchAnyInto(FlowScript.class);
    }

    @Override
    public FlowScript findById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhFlowScriptsDao dao = new EhFlowScriptsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), FlowScript.class);
    }

    @Override
    public List<FlowScript> listFlowScripts(Integer namespaceId, String ownerType, Long ownerId,
                                            String moduleType, Long moduleId, String scriptType,
                                            String keyword, int pageSize, ListingLocator locator) {

        com.everhomes.server.schema.tables.EhFlowScripts t = Tables.EH_FLOW_SCRIPTS;

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record1<Long>> subQuery = context.select(t.ID).from(t).getQuery();

        subQuery.addConditions(t.NAMESPACE_ID.eq(namespaceId));
        subQuery.addConditions(t.SCRIPT_VERSION.eq(0));

        if (ownerType != null && ownerId != null) {
            subQuery.addConditions(t.OWNER_TYPE.eq(ownerType));
            subQuery.addConditions(t.OWNER_ID.eq(ownerId));
        }
        if (moduleType != null && moduleId != null) {
            subQuery.addConditions(t.MODULE_TYPE.eq(moduleType));
            subQuery.addConditions(t.MODULE_ID.eq(moduleId));
        }
        if (keyword != null && keyword.trim().length() > 0) {
            subQuery.addConditions(t.NAME.like("%" + keyword.trim() + "%"));
        }
        if (scriptType != null) {
            subQuery.addConditions(t.SCRIPT_TYPE.eq(scriptType));
        }
        subQuery.addConditions(t.STATUS.eq(FlowCommonStatus.VALID.getCode()));

        Table<EhFlowScriptsRecord> table = context.selectFrom(t).orderBy(t.SCRIPT_VERSION.desc()).asTable();
        SelectQuery<EhFlowScriptsRecord> query = context.selectFrom(table).getQuery();

        query.addConditions(table.field(t.SCRIPT_MAIN_ID).in(subQuery));

        query.addGroupBy(table.field(t.SCRIPT_MAIN_ID));
        query.addOrderBy(table.field(t.ID));

        if (locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FLOW_SCRIPTS.ID.gt(locator.getAnchor()));
        }
        query.addLimit(pageSize + 1);

        LOGGER.debug("listFlowScripts sql: {}", query.getSQL(true));

        List<FlowScript> objs = query.fetchInto(FlowScript.class);
        if (objs.size() >= pageSize) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    @Override
    public List<FlowScript> listByScriptMainId(Long scriptMainId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhFlowScriptsDao dao = new EhFlowScriptsDao(context.configuration());
        List<EhFlowScripts> scriptsList = dao.fetchByScriptMainId(scriptMainId);
        return scriptsList.stream().map(
                r -> ConvertHelper.convert(r, FlowScript.class)).collect(Collectors.toList());
    }

    @Override
    public void updateFlowScripts(List<FlowScript> flowScripts) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhFlowScriptsDao dao = new EhFlowScriptsDao(context.configuration());
        for (FlowScript script : flowScripts) {
            script.setUpdateTime(DateUtils.currentTimestamp());
            script.setUpdateUid(UserContext.currentUserId());
        }
        dao.update(flowScripts.toArray(new EhFlowScripts[0]));
    }
}

