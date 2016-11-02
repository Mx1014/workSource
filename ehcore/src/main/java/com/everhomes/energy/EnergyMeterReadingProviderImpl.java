package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterReadingLogsDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterReadingLogs;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

/**
 * Created by xq.tian on 2016/11/1.
 */
@Repository
public class EnergyMeterReadingProviderImpl implements EnergyMeterReadingLogProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public long createEnergyMeterReadingLog(EnergyMeterReadingLog log) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnergyMeterReadingLogs.class));
        log.setId(id);
        log.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        log.setCreatorUid(UserContext.current().getUser().getId());
        rwDao().insert(log);
        return id;
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    // 可读写的dao
    private EhEnergyMeterReadingLogsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhEnergyMeterReadingLogsDao(context.configuration());
    }
}
