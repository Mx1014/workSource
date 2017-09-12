// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhStatEventDeviceLogsDao;
import com.everhomes.server.schema.tables.pojos.EhStatEventDeviceLogs;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StatEventDeviceLogProviderImpl implements StatEventDeviceLogProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createStatEventDeviceLog(StatEventDeviceLog statEventDeviceLog) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatEventDeviceLogs.class));
        statEventDeviceLog.setId(id);
        statEventDeviceLog.setCreateTime(DateUtils.currentTimestamp());
        // statEventDeviceLog.setCreatorUid(UserContext.currentUserId());
        rwDao().insert(statEventDeviceLog);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhStatEventDeviceLogs.class, id);
    }

    @Override
    public void updateStatEventDeviceLog(StatEventDeviceLog statEventDeviceLog) {
        // statEventDeviceLog.setUpdateTime(DateUtils.currentTimestamp());
        // statEventDeviceLog.setUpdateUid(UserContext.currentUserId());
        rwDao().update(statEventDeviceLog);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhStatEventDeviceLogs.class, statEventDeviceLog.getId());
    }

    @Override
    public StatEventDeviceLog findStatEventDeviceLogById(Long id) {
        return ConvertHelper.convert(dao().findById(id), StatEventDeviceLog.class);
    }

    // @Override
    // public List<StatEventDeviceLog> listStatEventDeviceLog() {
    // 	return getReadOnlyContext().select().from(Tables.EH_STAT_EVENT_DEVICE_LOGS)
    //			.orderBy(Tables.EH_STAT_EVENT_DEVICE_LOGS.ID.asc())
    //			.fetch().map(r -> ConvertHelper.convert(r, StatEventDeviceLog.class));
    // }

    private EhStatEventDeviceLogsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhStatEventDeviceLogsDao(context.configuration());
    }

    private EhStatEventDeviceLogsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhStatEventDeviceLogsDao(context.configuration());
    }
}
