package com.everhomes.decoration;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.decoration.DecorationAttachmentDTO;
import com.everhomes.rest.decoration.DecorationIllustrationDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhDecorationAtttachment;
import com.everhomes.server.schema.tables.pojos.EhDecorationWorkers;
import com.everhomes.server.schema.tables.daos.EhDecorationSettingDao;
import com.everhomes.server.schema.tables.daos.EhDecorationWorkersDao;
import com.everhomes.server.schema.tables.pojos.EhDecorationSetting;
import com.everhomes.server.schema.tables.records.EhDecorationAtttachmentRecord;
import com.everhomes.server.schema.tables.records.EhDecorationSettingRecord;
import com.everhomes.server.schema.tables.records.EhDecorationWorkersRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class DecorationProviderImpl implements  DecorationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(DecorationProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createDecorationSetting(DecorationSetting setting) {
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
        return id;
    }

    @Override
    public void updateDecorationSetting(DecorationSetting setting) {
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

    @Override
    public DecorationSetting getDecorationSettingById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_DECORATION_SETTING);
        Condition condition = Tables.EH_DECORATION_SETTING.ID.eq(id);
        step.where(condition);
        DecorationSetting setting = step.fetchOne().map(r->ConvertHelper.convert(r,DecorationSetting.class));
        return setting;
    }

    @Override
    public void createDecorationAttachment(DecorationAttachment attachment) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhDecorationAtttachment.class));
        attachment.setId(id);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDecorationAtttachmentRecord record = ConvertHelper.convert(attachment,EhDecorationAtttachmentRecord.class);
        InsertQuery<EhDecorationAtttachmentRecord> query = context
                .insertQuery(Tables.EH_DECORATION_ATTTACHMENT);
        query.setRecord(record);
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhDecorationAtttachment.class, null);
    }

    @Override
    public List<DecorationAttachmentDTO> listDecorationAttachmentBySettingId(Long settingId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_DECORATION_ATTTACHMENT);
        Condition condition = Tables.EH_DECORATION_ATTTACHMENT.SETTING_ID.eq(settingId);
        step.where(condition);
        List<DecorationAttachmentDTO> list = step.fetch().map(r-> ConvertHelper.convert(r,DecorationAttachmentDTO.class)
        );
        return list;
    }

    @Override
    public void deleteDecorationBySettingId(Long settingId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        DeleteWhereStep<EhDecorationAtttachmentRecord> step = context
                .delete(Tables.EH_DECORATION_ATTTACHMENT);
        Condition condition = Tables.EH_DECORATION_ATTTACHMENT.SETTING_ID.eq(settingId);
        step.where(condition).execute();
    }

    @Override
    public DecorationRequest getRequestById(Long requestId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_DECORATION_REQUESTS);
        Condition condition = Tables.EH_DECORATION_REQUESTS.ID.eq(requestId);
        step.where(condition);
        return step.fetchOne().map(r->ConvertHelper.convert(r,DecorationRequest.class));
    }

    @Override
    public DecorationWorker getDecorationWorkerById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_DECORATION_WORKERS);
        Condition condition = Tables.EH_DECORATION_WORKERS.ID.eq(id);
        step.where(condition);
        return step.fetchOne().map(r->ConvertHelper.convert(r,DecorationWorker.class));
    }

    @Override
    public List<DecorationWorker> listWorkersByRequestId(Long requestId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_DECORATION_WORKERS);
        Condition condition = Tables.EH_DECORATION_WORKERS.REQUEST_ID.eq(requestId);
        step.where(condition);
        return step.fetch().map(r->ConvertHelper.convert(r,DecorationWorker.class));
    }

    @Override
    public Long createDecorationWorker(DecorationWorker worker) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhDecorationWorkers.class));
        worker.setId(id);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDecorationWorkersRecord record = ConvertHelper.convert(worker,EhDecorationWorkersRecord.class);
        InsertQuery<EhDecorationWorkersRecord> query = context
                .insertQuery(Tables.EH_DECORATION_WORKERS);
        query.setRecord(record);
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhDecorationWorkers.class, null);
        return id;
    }

    @Override
    public void updateDecorationWorker(DecorationWorker worker) {
        assert (worker.getId() == null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDecorationWorkersDao dao = new EhDecorationWorkersDao(context.configuration());
        dao.update(worker);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDecorationWorkers.class,
                worker.getId());
    }

    @Override
    public void deleteDecorationWorkerById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        DeleteWhereStep<EhDecorationWorkersRecord> step = context
                .delete(Tables.EH_DECORATION_WORKERS);
        Condition condition = Tables.EH_DECORATION_WORKERS.ID.eq(id);
        step.where(condition).execute();
    }
}
