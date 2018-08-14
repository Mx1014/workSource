package com.everhomes.general_form;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhGeneralFormKvConfigsDao;
import com.everhomes.server.schema.tables.pojos.EhGeneralFormKvConfigs;
import com.everhomes.server.schema.tables.records.EhGeneralFormKvConfigsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GeneralFormKvConfigProviderImpl implements GeneralFormKvConfigProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createGeneralFormKvConfig(GeneralFormKvConfig config) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhGeneralFormKvConfigs.class));
        config.setId(id);
        config.setCreateTime(DateUtils.currentTimestamp());
        config.setCreatorUid(UserContext.currentUserId());
        rwDao().insert(config);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhGeneralFormKvConfigs.class, id);
    }

    @Override
    public void updateGeneralFormKvConfig(GeneralFormKvConfig config) {
        config.setUpdateTime(DateUtils.currentTimestamp());
        config.setUpdaterUid(UserContext.currentUserId());
        rwDao().update(config);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhGeneralFormKvConfigs.class, config.getId());
    }

    @Override
    public GeneralFormKvConfig findById(Long id) {
        return ConvertHelper.convert(dao().findById(id), GeneralFormKvConfig.class);
    }

    @Override
    public GeneralFormKvConfig findByKey(Integer namespaceId, String moduleType, Long moduleId,
                                         String projectType, Long projectId, String ownerType, Long ownerId, String key) {
        com.everhomes.server.schema.tables.EhGeneralFormKvConfigs t = Tables.EH_GENERAL_FORM_KV_CONFIGS;
        SelectQuery<EhGeneralFormKvConfigsRecord> query = context().selectFrom(t).getQuery();
        
        ifNotNull(namespaceId, () -> query.addConditions(t.NAMESPACE_ID.eq(namespaceId)));
        ifNotNull(projectType, () -> query.addConditions(t.PROJECT_TYPE.eq(projectType)));
        ifNotNull(projectId, () -> query.addConditions(t.PROJECT_ID.eq(projectId)));
        ifNotNull(moduleType, () -> query.addConditions(t.MODULE_TYPE.eq(moduleType)));
        ifNotNull(moduleId, () -> query.addConditions(t.MODULE_ID.eq(moduleId)));
        ifNotNull(ownerType, () -> query.addConditions(t.OWNER_TYPE.eq(ownerType)));
        ifNotNull(ownerId, () -> query.addConditions(t.OWNER_ID.eq(ownerId)));
        
        query.addConditions(t.KEY.eq(key));
        
        return query.fetchAnyInto(GeneralFormKvConfig.class);
    }

    private EhGeneralFormKvConfigsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhGeneralFormKvConfigsDao(context.configuration());
    }

    private EhGeneralFormKvConfigsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhGeneralFormKvConfigsDao(context.configuration());
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
