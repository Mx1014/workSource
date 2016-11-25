package com.everhomes.statistics.terminal;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhTerminalAppVersionStatisticsDao;
import com.everhomes.server.schema.tables.daos.EhTerminalDayStatisticsDao;
import com.everhomes.server.schema.tables.daos.EhTerminalHourStatisticsDao;
import com.everhomes.server.schema.tables.pojos.EhTerminalAppVersionStatistics;
import com.everhomes.server.schema.tables.pojos.EhTerminalDayStatistics;
import com.everhomes.server.schema.tables.pojos.EhTerminalHourStatistics;
import com.everhomes.server.schema.tables.records.EhTerminalAppVersionStatisticsRecord;
import com.everhomes.server.schema.tables.records.EhTerminalDayStatisticsRecord;
import com.everhomes.server.schema.tables.records.EhTerminalHourStatisticsRecord;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;

/**
 * Created by sfyan on 2016/11/24.
 */
public class StatTerminalProviderImpl implements StatTerminalProvider{

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private DbProvider dbProvider;

    @Override
    public void createTerminalDayStatistics(TerminalDayStatistics terminalDayStatistics) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhTerminalDayStatistics.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        terminalDayStatistics.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        terminalDayStatistics.setId(id);
        EhTerminalDayStatisticsDao dao = new EhTerminalDayStatisticsDao(context.configuration());
        dao.insert(terminalDayStatistics);
    }

    @Override
    public void createTerminalHourStatistics(TerminalHourStatistics terminalHourStatistics) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhTerminalHourStatistics.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        terminalHourStatistics.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        terminalHourStatistics.setId(id);
        EhTerminalHourStatisticsDao dao = new EhTerminalHourStatisticsDao(context.configuration());
        dao.insert(terminalHourStatistics);
    }

    @Override
    public void createTerminalAppVersionStatistics(TerminalAppVersionStatistics terminalAppVersionStatistics) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhTerminalAppVersionStatistics.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        terminalAppVersionStatistics.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        terminalAppVersionStatistics.setId(id);
        EhTerminalAppVersionStatisticsDao dao = new EhTerminalAppVersionStatisticsDao(context.configuration());
        dao.insert(terminalAppVersionStatistics);
    }

    @Override
    public void deleteTerminalDayStatistics(String date) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhTerminalDayStatisticsRecord> delete = context.deleteQuery(Tables.EH_TERMINAL_DAY_STATISTICS);
        delete.addConditions(Tables.EH_TERMINAL_DAY_STATISTICS.DATE.eq(date));
        delete.execute();
    }

    @Override
    public void deleteTerminalHourStatistics(String date) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhTerminalHourStatisticsRecord> delete = context.deleteQuery(Tables.EH_TERMINAL_HOUR_STATISTICS);
        delete.addConditions(Tables.EH_TERMINAL_HOUR_STATISTICS.DATE.eq(date));
        delete.execute();
    }

    @Override
    public void deleteTerminalAppVersionStatistics(String date) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhTerminalAppVersionStatisticsRecord> delete = context.deleteQuery(Tables.EH_TERMINAL_APP_VERSION_STATISTICS);
        delete.addConditions(Tables.EH_TERMINAL_APP_VERSION_STATISTICS.DATE.eq(date));
        delete.execute();
    }


    
}
