package com.everhomes.decoration;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.decoration.DecorationIllustrationDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhDecorationSettingDao;
import com.everhomes.server.schema.tables.pojos.EhDecorationSetting;
import com.everhomes.server.schema.tables.records.EhDecorationSettingRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class DecorationProviderImpl implements  DecorationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(DecorationProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createDecorationSetting(EhDecorationSetting setting) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhDecorationSetting.class));
        setting.setId(id);
        setting.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDecorationSettingRecord record = ConvertHelper.convert(setting,EhDecorationSettingRecord.class);
        InsertQuery<EhDecorationSettingRecord> query = context
                .insertQuery(Tables.EH_DECORATION_SETTING);
        query.setRecord(record);
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhDecorationSetting.class, null);
        return null;
    }

    @Override
    public void updateDecorationSetting(EhDecorationSetting setting) {
        assert (setting.getId() == null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDecorationSettingDao dao = new EhDecorationSettingDao(context.configuration());
        dao.update(setting);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDecorationSetting.class,
                setting.getId());
    }

    @Override
    public DecorationSetting getDecorationSetting(Integer namespaceId, Long communityId, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_DECORATION_SETTING);
        Condition condition = Tables.EH_DECORATION_SETTING.COMMUNITY_ID.eq(communityId);
        if (null != namespaceId)
            condition = condition.and(Tables.EH_DECORATION_SETTING.NAMESPACE_ID.eq(namespaceId));
        if (null != ownerType)
            condition = condition.and(Tables.EH_DECORATION_SETTING.OWNER_TYPE.eq(ownerType));
        if (null != ownerId)
            condition = condition.and(Tables.EH_DECORATION_SETTING.OWNER_ID.eq(ownerId));
        step.where(condition);
        DecorationSetting setting = step.fetchOne().map(r->ConvertHelper.convert(r,DecorationSetting.class));
        return setting;
    }
}
