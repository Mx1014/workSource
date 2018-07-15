package com.everhomes.statistics.terminal;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.statistics.terminal.ListTerminalStatisticsByDateCommand;
import com.everhomes.rest.statistics.terminal.ListTerminalStatisticsByDayCommand;
import com.everhomes.rest.statistics.terminal.TerminalStatisticsChartCommand;
import com.everhomes.rest.statistics.terminal.TerminalStatisticsTaskDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAppVersionDao;
import com.everhomes.server.schema.tables.daos.EhUserActivitiesDao;
import com.everhomes.server.schema.tables.pojos.EhAppVersion;
import com.everhomes.server.schema.tables.pojos.EhUserActivities;
import com.everhomes.user.UserActivity;
import com.everhomes.util.Version;
import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xq.tian on 2018/3/21.
 */
public class StatTerminalServiceImplTest extends CoreServerTestCase {

    @Autowired
    private StatTerminalService statTerminalService;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    private static final Integer NS = 11;

    @Before
    public void initData() {
        truncateTable();
        initAppVersion();
        initUserActivity();
    }

    private void initUserActivity() {
        EhAppVersionDao versionDao = new EhAppVersionDao(dbProvider.getDslContext(AccessSpec.readWrite()).configuration());
        EhUserActivitiesDao dao = new EhUserActivitiesDao(dbProvider.getDslContext(AccessSpec.readWrite()).configuration());

        List<EhAppVersion> versionList = versionDao.fetchByNamespaceId(NS);

        final int ACTIVITY_PER_VERSION = 100;
        long id = sequenceProvider.getNextSequenceBlock(
                NameMapper.getSequenceDomainFromTablePojo(EhUserActivities.class), versionList.size() * ACTIVITY_PER_VERSION * 10);

        for (int j = 0; j < 10; j++) {
            LocalDateTime time = LocalDateTime.now().minusDays(j+1);
            for (EhAppVersion version : versionList) {
                for (int i = 0; i < ACTIVITY_PER_VERSION; i++) {
                    UserActivity userActivity = new UserActivity();
                    userActivity.setId(id++);
                    userActivity.setAppVersionName(version.getName());
                    userActivity.setCreateTime(Timestamp.valueOf(time.withHour((int) (id%24))));
                    userActivity.setImeiNumber(id+"");
                    userActivity.setNamespaceId(NS);
                    userActivity.setUid(id);// different per activity
                    dao.insert(userActivity);
                }
            }
        }
    }

    private void initAppVersion() {
        EhAppVersionDao dao = new EhAppVersionDao(dbProvider.getDslContext(AccessSpec.readWrite()).configuration());
        long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhAppVersion.class), 10);
        for (int i = 0; i < 5; i++) {
            AppVersion version = new AppVersion();
            version.setId(id++);
            version.setName(String.format("5.6.%d", i));
            version.setNamespaceId(NS);
            version.setType("android");
            version.setRealm("r");
            version.setDefaultOrder((int) Version.fromVersionString(version.getName()).getEncodedValue());
            dao.insert(version);
        }
        for (int i = 0; i < 5; i++) {
            AppVersion version = new AppVersion();
            version.setId(id++);
            version.setName(String.format("5.6.%d", i));
            version.setNamespaceId(NS);
            version.setType("ios");
            version.setRealm("r");
            version.setDefaultOrder((int) Version.fromVersionString(version.getName()).getEncodedValue());
            dao.insert(version);
        }
    }

    private void truncateTable() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        context.truncate(Tables.EH_USER_ACTIVITIES).execute();
        context.truncate(Tables.EH_APP_VERSION).execute();
        context.truncate(Tables.EH_TERMINAL_STATISTICS_TASKS).execute();
        context.truncate(Tables.EH_TERMINAL_HOUR_STATISTICS).execute();
        context.truncate(Tables.EH_TERMINAL_DAY_STATISTICS).execute();
        context.truncate(Tables.EH_TERMINAL_APP_VERSION_ACTIVES).execute();
        context.truncate(Tables.EH_TERMINAL_APP_VERSION_STATISTICS).execute();
        context.truncate(Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES).execute();
    }

    @Test
    public void exportTerminalHourLineChart() {
        TerminalStatisticsChartCommand cmd = new TerminalStatisticsChartCommand();
        cmd.setDates(Arrays.asList("20180101", "20180207"));
        cmd.setNamespaceId(1000000);
        statTerminalService.exportTerminalHourLineChart(cmd);
    }

    @Test
    public void exportTerminalDayLineChart() {
        ListTerminalStatisticsByDateCommand cmd = new ListTerminalStatisticsByDateCommand();
        cmd.setStartDate("20180101");
        cmd.setEndDate("20180301");
        cmd.setNamespaceId(1000000);
        statTerminalService.exportTerminalDayLineChart(cmd);
    }

    @Test
    public void exportTerminalAppVersionPieChart() {
        ListTerminalStatisticsByDayCommand cmd = new ListTerminalStatisticsByDayCommand();
        cmd.setDate("20180301");
        cmd.setNamespaceId(1000000);
        statTerminalService.exportTerminalAppVersionPieChart(cmd);
    }

    @Test
    public void executeStatTask() {
        List<TerminalStatisticsTaskDTO> task =
                statTerminalService.executeStatTask(NS, LocalDate.now().minusDays(10), LocalDate.now());
        assertNotNull("task should be not null", task);
        assertEquals("task.size() should be 1", 10, task.size());
    }
}