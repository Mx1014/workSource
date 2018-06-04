package com.everhomes.flow;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.flow.FlowScriptType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhFlowScriptConfigsDao;
import com.everhomes.server.schema.tables.pojos.EhFlowScriptConfigs;
import com.everhomes.server.schema.tables.records.EhFlowScriptConfigsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FlowScriptConfigProviderImpl implements FlowScriptConfigProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createFlowScriptConfig(FlowScriptConfig obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowScriptConfigs.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setCreateTime(DateUtils.currentTimestamp());
        obj.setCreatorUid(UserContext.currentUserId());
        obj.setId(id);
        EhFlowScriptConfigsDao dao = new EhFlowScriptConfigsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public List<FlowScriptConfig> queryFlowScriptConfig(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        com.everhomes.server.schema.tables.EhFlowScriptConfigs t = Tables.EH_FLOW_SCRIPT_CONFIGS;
        SelectQuery<EhFlowScriptConfigsRecord> query = context.selectQuery(t);
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if (locator.getAnchor() != null) {
            query.addConditions(t.ID.gt(locator.getAnchor()));
        }

        query.addLimit(count + 1);
        List<FlowScriptConfig> objs = query.fetchInto(FlowScriptConfig.class);

        if (objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
            objs.remove(objs.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return objs;
    }

    @Override
    public void deleteByOwner(String ownerType, Long ownerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhFlowScriptConfigs t = Tables.EH_FLOW_SCRIPT_CONFIGS;

        context.delete(t)
                .where(t.OWNER_TYPE.eq(ownerType))
                .and(t.OWNER_ID.eq(ownerId))
                .execute();
    }

    @Override
    public List<FlowScriptConfig> listByOwner(String ownerType, Long ownerId) {
        com.everhomes.server.schema.tables.EhFlowScriptConfigs t = Tables.EH_FLOW_SCRIPT_CONFIGS;
        return this.queryFlowScriptConfig(new ListingLocator(), 100, (locator, query) -> {
            query.addConditions(t.OWNER_TYPE.eq(ownerType));
            query.addConditions(t.OWNER_ID.eq(ownerId));
            return query;
        });
    }

    @Override
    public String getConfig(String ownerType, Long ownerId, String fieldName) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhFlowScriptConfigs t = Tables.EH_FLOW_SCRIPT_CONFIGS;
        return context.select(t.FIELD_VALUE).from(t)
                .where(t.OWNER_TYPE.eq(ownerType))
                .and(t.OWNER_ID.eq(ownerId))
                .and(t.FIELD_NAME.eq(fieldName))
                .fetchAnyInto(String.class);
    }

    @Override
    public void createFlowScriptConfigs(List<FlowScriptConfig> scriptConfigs) {
        for (FlowScriptConfig config : scriptConfigs) {
            long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowScriptConfigs.class));
            config.setCreateTime(DateUtils.currentTimestamp());
            config.setCreatorUid(UserContext.currentUserId());
            config.setId(id);
        }
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhFlowScriptConfigsDao dao = new EhFlowScriptConfigsDao(context.configuration());
        dao.insert(scriptConfigs.toArray(new EhFlowScriptConfigs[0]));
    }

    @Override
    public List<FlowScriptConfig> listByFlow(Long flowMainId, Integer flowVersion) {
        com.everhomes.server.schema.tables.EhFlowScriptConfigs t = Tables.EH_FLOW_SCRIPT_CONFIGS;
        return this.queryFlowScriptConfig(new ListingLocator(), 100, (locator, query) -> {
            query.addConditions(t.FLOW_MAIN_ID.eq(flowMainId));
            query.addConditions(t.FLOW_VERSION.eq(flowVersion));
            return query;
        });
    }

    @Override
    public List<FlowScriptConfig> listByModule(Long moduleId, FlowScriptType scriptType) {
        com.everhomes.server.schema.tables.EhFlowScriptConfigs t = Tables.EH_FLOW_SCRIPT_CONFIGS;
        return this.queryFlowScriptConfig(new ListingLocator(), 10000, (locator, query) -> {
            query.addConditions(t.MODULE_ID.eq(moduleId));
            query.addConditions(t.SCRIPT_TYPE.eq(scriptType.getCode()));
            return query;
        });
    }
}
