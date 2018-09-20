package com.everhomes.flow;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhFlowKvConfigsDao;
import com.everhomes.server.schema.tables.pojos.EhFlowKvConfigs;
import com.everhomes.server.schema.tables.records.EhFlowKvConfigsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FlowKvConfigProviderImpl implements FlowKvConfigProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createFlowKvConfig(FlowKvConfig flowKvConfig) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowKvConfigs.class));
        flowKvConfig.setId(id);
        flowKvConfig.setCreateTime(DateUtils.currentTimestamp());
        flowKvConfig.setCreatorUid(UserContext.currentUserId());
        rwDao().insert(flowKvConfig);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhFlowKvConfigs.class, id);
    }

    @Override
    public void updateFlowKvConfig(FlowKvConfig flowKvConfig) {
        flowKvConfig.setUpdateTime(DateUtils.currentTimestamp());
        flowKvConfig.setUpdaterUid(UserContext.currentUserId());
        rwDao().update(flowKvConfig);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFlowKvConfigs.class, flowKvConfig.getId());
    }

    @Override
    public FlowKvConfig findById(Long id) {
        return ConvertHelper.convert(dao().findById(id), FlowKvConfig.class);
    }

    @Override
    public FlowKvConfig findByKey(Integer namespaceId, String moduleType, Long moduleId,
                                  String projectType, Long projectId, String ownerType, Long ownerId, String key) {
        com.everhomes.server.schema.tables.EhFlowKvConfigs t = Tables.EH_FLOW_KV_CONFIGS;
        SelectQuery<EhFlowKvConfigsRecord> query = context().selectFrom(t).getQuery();

        ifNotNull(namespaceId, () -> query.addConditions(t.NAMESPACE_ID.eq(namespaceId)));
        ifNotNull(moduleType, () -> query.addConditions(t.MODULE_TYPE.eq(moduleType)));
        ifNotNull(moduleId, () -> query.addConditions(t.MODULE_ID.eq(moduleId)));
        ifNotNull(projectType, () -> query.addConditions(t.PROJECT_TYPE.eq(projectType)));
        ifNotNull(projectId, () -> query.addConditions(t.PROJECT_ID.eq(projectId)));
        ifNotNull(ownerType, () -> query.addConditions(t.OWNER_TYPE.eq(ownerType)));
        ifNotNull(ownerId, () -> query.addConditions(t.OWNER_ID.eq(ownerId)));

        query.addConditions(t.KEY.eq(key));

        return query.fetchAnyInto(FlowKvConfig.class);
    }

    private EhFlowKvConfigsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhFlowKvConfigsDao(context.configuration());
    }

    private EhFlowKvConfigsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhFlowKvConfigsDao(context.configuration());
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    private void ifNotNull(Object condition, Callback callback) {
        if (condition != null && condition.toString().trim().length() > 0) {
            callback.condition();
        }
    }

    private interface Callback {
        void condition();
    }
}
