package com.everhomes.PmNotify;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.pmNotify.PmNotifyConfigurations;
import com.everhomes.pmNotify.PmNotifyLog;
import com.everhomes.pmNotify.PmNotifyProvider;
import com.everhomes.pmNotify.PmNotifyRecord;
import com.everhomes.rest.pmNotify.PmNotifyConfigurationStatus;
import com.everhomes.rest.pmNotify.PmNotifyRecordStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPmNotifyConfigurationsDao;
import com.everhomes.server.schema.tables.daos.EhPmNotifyLogsDao;
import com.everhomes.server.schema.tables.daos.EhPmNotifyRecordsDao;
import com.everhomes.server.schema.tables.pojos.EhPmNotifyConfigurations;
import com.everhomes.server.schema.tables.pojos.EhPmNotifyLogs;
import com.everhomes.server.schema.tables.pojos.EhPmNotifyRecords;
import com.everhomes.server.schema.tables.records.EhPmNotifyConfigurationsRecord;
import com.everhomes.server.schema.tables.records.EhPmNotifyRecordsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying.xiong on 2017/9/12.
 */
@Component
public class PmNotifyProviderImpl implements PmNotifyProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(PmNotifyProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createPmNotifyConfigurations(PmNotifyConfigurations configuration) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPmNotifyConfigurations.class));

        configuration.setId(id);
        configuration.setStatus(PmNotifyConfigurationStatus.VAILD.getCode());
        configuration.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPmNotifyConfigurationsDao dao = new EhPmNotifyConfigurationsDao(context.configuration());
        dao.insert(configuration);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPmNotifyConfigurations.class, null);
    }

    @Override
    public void updatePmNotifyConfigurations(PmNotifyConfigurations configuration) {
        assert(configuration.getId() != null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPmNotifyConfigurationsDao dao = new EhPmNotifyConfigurationsDao(context.configuration());
        dao.update(configuration);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPmNotifyConfigurations.class, configuration.getId());
    }

    @Override
    public List<PmNotifyConfigurations> listScopePmNotifyConfigurations(String ownerType, Byte scopeType, Long scopeId,Long targetId,String targetType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<PmNotifyConfigurations> result  = new ArrayList<PmNotifyConfigurations>();
        SelectQuery<EhPmNotifyConfigurationsRecord> query = context.selectQuery(Tables.EH_PM_NOTIFY_CONFIGURATIONS);
        query.addConditions(Tables.EH_PM_NOTIFY_CONFIGURATIONS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_PM_NOTIFY_CONFIGURATIONS.SCOPE_TYPE.eq(scopeType));
        query.addConditions(Tables.EH_PM_NOTIFY_CONFIGURATIONS.SCOPE_ID.eq(scopeId));
        query.addConditions(Tables.EH_PM_NOTIFY_CONFIGURATIONS.STATUS.eq(PmNotifyConfigurationStatus.VAILD.getCode()));
        if (targetId != null) {
            query.addConditions(Tables.EH_PM_NOTIFY_CONFIGURATIONS.TARGET_ID.eq(targetId));
        }
        if (StringUtils.isNotBlank(targetType)) {
            query.addConditions(Tables.EH_PM_NOTIFY_CONFIGURATIONS.TARGET_TYPE.eq(targetType));
        }
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, PmNotifyConfigurations.class));
            return null;
        });

        return result;
    }

    @Override
    public PmNotifyConfigurations findScopePmNotifyConfiguration(Long id, String ownerType, Byte scopeType, Long scopeId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<PmNotifyConfigurations> result  = new ArrayList<PmNotifyConfigurations>();
        SelectQuery<EhPmNotifyConfigurationsRecord> query = context.selectQuery(Tables.EH_PM_NOTIFY_CONFIGURATIONS);
        query.addConditions(Tables.EH_PM_NOTIFY_CONFIGURATIONS.ID.eq(id));
        query.addConditions(Tables.EH_PM_NOTIFY_CONFIGURATIONS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_PM_NOTIFY_CONFIGURATIONS.SCOPE_TYPE.eq(scopeType));
        query.addConditions(Tables.EH_PM_NOTIFY_CONFIGURATIONS.SCOPE_ID.eq(scopeId));
        query.addConditions(Tables.EH_PM_NOTIFY_CONFIGURATIONS.STATUS.eq(PmNotifyConfigurationStatus.VAILD.getCode()));
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, PmNotifyConfigurations.class));
            return null;
        });

        if(null != result && 0 != result.size()){
            return result.get(0);
        }
        return null;
    }

    @Override
    public void createPmNotifyRecord(PmNotifyRecord record) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPmNotifyRecords.class));

        record.setId(id);
        record.setStatus(PmNotifyRecordStatus.WAITING_FOR_SEND_OUT.getCode());
        record.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPmNotifyRecordsDao dao = new EhPmNotifyRecordsDao(context.configuration());
        dao.insert(record);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPmNotifyRecords.class, null);
    }

    @Override
    public void createPmNotifyLog(PmNotifyLog log) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPmNotifyLogs.class));

        log.setId(id);
        log.setStatus(PmNotifyConfigurationStatus.VAILD.getCode());
        log.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPmNotifyLogsDao dao = new EhPmNotifyLogsDao(context.configuration());
        dao.insert(log);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPmNotifyLogs.class, null);
    }

    @Override
    public boolean updateIfUnsend(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPmNotifyRecords.class));

        int effect = context.update(Tables.EH_PM_NOTIFY_RECORDS)
                .set(Tables.EH_PM_NOTIFY_RECORDS.STATUS, PmNotifyRecordStatus.ALREADY_SENDED.getCode())
                .where(Tables.EH_PM_NOTIFY_RECORDS.STATUS.eq(PmNotifyRecordStatus.WAITING_FOR_SEND_OUT.getCode()).and(Tables.EH_PM_NOTIFY_RECORDS.ID.eq(id)))
                .execute();

        if(effect > 0) {
            return true;
        }

        return false;
    }

    @Override
    public void invalidateNotifyRecord(List<Long> recordIds) {
        if (recordIds == null || recordIds.size() < 1) {
            return;
        }
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        context.update(Tables.EH_PM_NOTIFY_RECORDS)
                .set(Tables.EH_PM_NOTIFY_RECORDS.STATUS, PmNotifyRecordStatus.INVAILD.getCode())
                .where(Tables.EH_PM_NOTIFY_RECORDS.ID.in(recordIds))
                .execute();
    }

    @Override
    public List<PmNotifyRecord> listUnsendRecords() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<PmNotifyRecord> result  = new ArrayList<PmNotifyRecord>();
        SelectQuery<EhPmNotifyRecordsRecord> query = context.selectQuery(Tables.EH_PM_NOTIFY_RECORDS);
        query.addConditions(Tables.EH_PM_NOTIFY_RECORDS.STATUS.eq(PmNotifyRecordStatus.WAITING_FOR_SEND_OUT.getCode()));
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, PmNotifyRecord.class));
            return null;
        });

        return result;
    }

    @Override
    public PmNotifyRecord findRecordById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPmNotifyRecordsDao dao = new EhPmNotifyRecordsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), PmNotifyRecord.class);
    }
}
