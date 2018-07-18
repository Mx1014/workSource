package com.everhomes.statistics.terminal;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.user.UserStatus;
import com.everhomes.rest.version.VersionRealmType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.util.DateHelper;
import com.everhomes.util.DateUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by sfyan on 2016/11/24.
 */
@Component
public class StatTerminalProviderImpl implements StatTerminalProvider {

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
    public void createTerminalAppVersionStatistics(List<TerminalAppVersionStatistics> terminalAppVersionStatistics) {
        long id = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(
                EhTerminalAppVersionStatistics.class), terminalAppVersionStatistics.size());
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        for (TerminalAppVersionStatistics stat : terminalAppVersionStatistics) {
            stat.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            stat.setId(id++);
        }
        EhTerminalAppVersionStatisticsDao dao = new EhTerminalAppVersionStatisticsDao(context.configuration());
        dao.insert(terminalAppVersionStatistics.toArray(new EhTerminalAppVersionStatistics[0]));
    }

    @Override
    public void createTerminalStatisticsTask(TerminalStatisticsTask task) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhTerminalStatisticsTasks.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        task.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        task.setId(id);
        EhTerminalStatisticsTasksDao dao = new EhTerminalStatisticsTasksDao(context.configuration());
        dao.insert(task);
    }

    @Override
    public void updateTerminalStatisticsTask(TerminalStatisticsTask task) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        task.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhTerminalStatisticsTasksDao dao = new EhTerminalStatisticsTasksDao(context.configuration());
        dao.update(task);
    }

    @Override
    public void deleteTerminalDayStatistics(Integer namespaceId, String date) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhTerminalDayStatisticsRecord> delete = context.deleteQuery(Tables.EH_TERMINAL_DAY_STATISTICS);
        delete.addConditions(Tables.EH_TERMINAL_DAY_STATISTICS.NAMESPACE_ID.eq(namespaceId));
        delete.addConditions(Tables.EH_TERMINAL_DAY_STATISTICS.DATE.eq(date));
        delete.execute();
    }

    @Override
    public void deleteTerminalHourStatistics(Integer namespaceId, String date) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhTerminalHourStatisticsRecord> delete = context.deleteQuery(Tables.EH_TERMINAL_HOUR_STATISTICS);
        delete.addConditions(Tables.EH_TERMINAL_HOUR_STATISTICS.NAMESPACE_ID.eq(namespaceId));
        delete.addConditions(Tables.EH_TERMINAL_HOUR_STATISTICS.DATE.eq(date));
        delete.execute();
    }

    @Override
    public void deleteTerminalAppVersionStatistics(Integer namespaceId, String date) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhTerminalAppVersionStatisticsRecord> delete = context.deleteQuery(Tables.EH_TERMINAL_APP_VERSION_STATISTICS);
        delete.addConditions(Tables.EH_TERMINAL_APP_VERSION_STATISTICS.NAMESPACE_ID.eq(namespaceId));
        delete.addConditions(Tables.EH_TERMINAL_APP_VERSION_STATISTICS.DATE.eq(date));
        delete.execute();
    }

    @Override
    public void createTerminalAppVersionCumulatives(TerminalAppVersionCumulatives terminalAppVersionCumulatives) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhTerminalAppVersionCumulatives.class));
        terminalAppVersionCumulatives.setCreateTime(DateUtils.currentTimestamp());
        terminalAppVersionCumulatives.setId(id);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhTerminalAppVersionCumulativesDao dao = new EhTerminalAppVersionCumulativesDao(context.configuration());
        dao.insert(terminalAppVersionCumulatives);
    }

    @Override
    public void deleteTerminalAppVersionCumulativeById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhTerminalAppVersionCumulativesDao dao = new EhTerminalAppVersionCumulativesDao(context.configuration());
        dao.deleteById(id);
    }

    @Override
    public void createTerminalAppVersionActives(TerminalAppVersionActives terminalAppVersionActives) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhTerminalAppVersionActives.class));
        terminalAppVersionActives.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        terminalAppVersionActives.setId(id);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhTerminalAppVersionActivesDao dao = new EhTerminalAppVersionActivesDao(context.configuration());
        dao.insert(terminalAppVersionActives);
    }

    @Override
    public void deleteTerminalAppVersionActivesById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhTerminalAppVersionActivesDao dao = new EhTerminalAppVersionActivesDao(context.configuration());
        dao.deleteById(id);
    }

    @Override
    public List<TerminalAppVersionActives> getTerminalAppVersionActive(String date, String version, String imei, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhTerminalAppVersionActivesRecord> query = context.selectQuery(Tables.EH_TERMINAL_APP_VERSION_ACTIVES);
        if (date != null) {
            query.addConditions(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.DATE.eq(date));
        }
        if (null != version) {
            query.addConditions(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.APP_VERSION.eq(version));
        }
        query.addConditions(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.NAMESPACE_ID.eq(namespaceId));
        if (null != imei) {
            query.addConditions(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.IMEI_NUMBER.eq(imei));
        }
        return query.fetchInto(TerminalAppVersionActives.class);
    }

    @Override
    public TerminalDayStatistics getTerminalDayStatisticsByDay(String date, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhTerminalDayStatisticsRecord> query = context.selectQuery(Tables.EH_TERMINAL_DAY_STATISTICS);
        query.addConditions(Tables.EH_TERMINAL_DAY_STATISTICS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_TERMINAL_DAY_STATISTICS.DATE.eq(date));
        return query.fetchAnyInto(TerminalDayStatistics.class);
    }

    @Override
    public TerminalStatisticsTask getTerminalStatisticsTaskByTaskNo(Integer namespaceId, String taskNo) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhTerminalStatisticsTasksRecord> query = context.selectQuery(Tables.EH_TERMINAL_STATISTICS_TASKS);
        query.addConditions(Tables.EH_TERMINAL_STATISTICS_TASKS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_TERMINAL_STATISTICS_TASKS.TASK_NO.eq(taskNo));
        return query.fetchAnyInto(TerminalStatisticsTask.class);
    }

    @Override
    public List<TerminalDayStatistics> listTerminalDayStatisticsByDate(String startDate, String endDate, Integer namespaceId) {
        com.everhomes.server.schema.tables.EhTerminalDayStatistics t = Tables.EH_TERMINAL_DAY_STATISTICS;
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhTerminalDayStatisticsRecord> query = context.selectQuery(t);
        query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(t.DATE.ge(startDate));
        query.addConditions(t.DATE.le(endDate));
        query.addOrderBy(t.DATE);
        return query.fetchInto(TerminalDayStatistics.class);
    }

    @Override
    public List<TerminalHourStatistics> listTerminalHourStatisticsByDay(String date, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhTerminalHourStatistics t = Tables.EH_TERMINAL_HOUR_STATISTICS;
        SelectQuery<EhTerminalHourStatisticsRecord> query = context.selectQuery(t);
        query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(t.DATE.eq(date));
        query.addOrderBy(t.HOUR);
        return query.fetchInto(TerminalHourStatistics.class);
    }

    @Override
    public List<TerminalAppVersionStatistics> listTerminalAppVersionStatisticsByDay(String date, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhTerminalAppVersionStatistics t = Tables.EH_TERMINAL_APP_VERSION_STATISTICS;
        SelectQuery<EhTerminalAppVersionStatisticsRecord> query = context.selectQuery(t);
        query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(t.DATE.eq(date));
        return query.fetchInto(TerminalAppVersionStatistics.class);
    }

    @Override
    public Map<String, Integer> statisticalByInterval(Integer namespaceId, LocalDateTime start, LocalDateTime end) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhUserActivities t = Tables.EH_USER_ACTIVITIES;

        SelectQuery<Record2<Integer, Integer>> query = context
                .select(t.ID.count(), t.UID.countDistinct()).from(t).getQuery();

        query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
        if (start != null) {
            query.addConditions(t.CREATE_TIME.ge(Timestamp.valueOf(start)));
        }
        if (end != null) {
            query.addConditions(t.CREATE_TIME.lt(Timestamp.valueOf(end)));
        }

        List<Map<String, Integer>> fetch = query.fetch(record -> {
            Map<String, Integer> map = new HashMap<>(2);
            map.put("startupNumber", record.value1());
            map.put("activeUserNumber", record.value2());
            return map;
        });
        if (fetch.size() > 0) {
            return fetch.iterator().next();
        }
        return new HashMap<>(0);
    }

    @Override
    public List<Long> listUserIdByInterval(Integer namespaceId, LocalDateTime start, LocalDateTime end) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhUserActivities t = Tables.EH_USER_ACTIVITIES;

        SelectQuery<Record1<Long>> query = context.selectDistinct(t.UID).from(t).getQuery();

        query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
        if (start != null) {
            query.addConditions(t.CREATE_TIME.ge(Timestamp.valueOf(start)));
        }
        if (end != null) {
            query.addConditions(t.CREATE_TIME.lt(Timestamp.valueOf(end)));
        }
        return query.fetchInto(Long.class);
    }

    @Override
    public Integer countVersionCumulativeUserNumber(String version, Integer namespaceId, String date) {
        com.everhomes.server.schema.tables.EhTerminalAppVersionActives t = Tables.EH_TERMINAL_APP_VERSION_ACTIVES;
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<Record1<Integer>> query = context.select(DSL.countDistinct(t.IMEI_NUMBER)).from(t).getQuery();
        query.addConditions(t.NAMESPACE_ID.eq(namespaceId));

        if (!StringUtils.isEmpty(version)) {
            query.addConditions(t.APP_VERSION.eq(version));
        }
        if (date != null) {
            query.addConditions(t.DATE.le(date));
        }
        return query.fetchAnyInto(Integer.class);
    }

    @Override
    public Integer countVersionActiveUserNumberByDay(String date, String version, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhTerminalAppVersionActives t = Tables.EH_TERMINAL_APP_VERSION_ACTIVES;

        SelectQuery<Record1<Integer>> query = context.select(DSL.countDistinct(t.IMEI_NUMBER)).from(t).getQuery();
        query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(t.DATE.eq(date));

        if (!StringUtils.isEmpty(version)) {
            query.addConditions(t.APP_VERSION.eq(version));
        }
        return query.fetchAnyInto(Integer.class);
    }

    @Override
    public List<AppVersion> listAppVersions(Integer namespaceId) {
        com.everhomes.server.schema.tables.EhAppVersion t = Tables.EH_APP_VERSION;
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhAppVersionRecord> query = context.selectQuery(t);
        if (null != namespaceId) {
            query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
        } else {
            query.addGroupBy(t.NAMESPACE_ID);
        }
        query.addGroupBy(t.NAME);
        query.addOrderBy(t.DEFAULT_ORDER.desc());
        return query.fetchInto(AppVersion.class);
    }

    @Override
    public void createAppVersion(AppVersion appVersion) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAppVersion.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        appVersion.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        appVersion.setId(id);
        EhAppVersionDao dao = new EhAppVersionDao(context.configuration());
        dao.insert(appVersion);
    }


    @Override
    public AppVersion findAppVersion(Integer namespaceId, String name, String type) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhAppVersion t = Tables.EH_APP_VERSION;

        SelectQuery<EhAppVersionRecord> query = context.selectQuery(t);
        query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(t.NAME.eq(name));
        query.addConditions(t.TYPE.eq(type));

        return query.fetchAnyInto(AppVersion.class);
    }

    @Override
    public void deleteTerminalStatTask(Integer namespaceId, String startDate, String endDate) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhTerminalStatisticsTasksRecord> query = context.deleteQuery(Tables.EH_TERMINAL_STATISTICS_TASKS);
        if (namespaceId != null) {
            query.addConditions(Tables.EH_TERMINAL_STATISTICS_TASKS.NAMESPACE_ID.eq(namespaceId));
        }
        query.addConditions(Tables.EH_TERMINAL_STATISTICS_TASKS.TASK_NO.between(startDate, endDate));
        query.execute();
    }

    // DELETE FROM eh_terminal_app_version_cumulatives
    // WHERE namespace_id = 999983 AND imei_number NOT IN (SELECT concat('', id, '') FROM eh_users WHERE namespace_id = 999983);
    @Override
    public void cleanTerminalAppVersionCumulativeByCondition(Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        com.everhomes.server.schema.tables.EhUsers u = Tables.EH_USERS;
        com.everhomes.server.schema.tables.EhTerminalAppVersionActives t = Tables.EH_TERMINAL_APP_VERSION_ACTIVES;
        com.everhomes.server.schema.tables.EhUserActivities at = Tables.EH_USER_ACTIVITIES;

        List<Long> ids = context.select(u.ID).from(u)
                .where(u.NAMESPACE_ID.eq(namespaceId))
                .and(u.STATUS.eq(UserStatus.ACTIVE.getCode()))
                .and(u.NAMESPACE_USER_TOKEN.eq(""))
                .and(u.NAMESPACE_USER_TYPE.isNull())
                .fetchInto(Long.class);

        if (ids.size() == 0) {
            ids.add(0L);
        }

        List<String> idStrList = ids.stream().map(Object::toString).collect(Collectors.toList());
        List<Long> id1s = context.select(t.ID).from(t)
                .where(t.NAMESPACE_ID.eq(namespaceId))
                .and(t.IMEI_NUMBER.notIn(idStrList))
                .fetchInto(Long.class);

        List<Long> atids = context.select(at.ID).from(at)
                .where(at.NAMESPACE_ID.eq(namespaceId))
                .and(at.UID.notIn(ids))
                .fetchInto(Long.class);

        for (Long id1 : id1s) {
            context.delete(t).where(t.ID.eq(id1)).execute();
        }

        for (Long id1 : atids) {
            context.delete(at).where(at.ID.eq(id1)).execute();
        }
    }

    @Override
    public AppVersion findLastAppVersion(Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhAppVersionRecord> query = context.selectQuery(Tables.EH_APP_VERSION);
        if (null != namespaceId) {
            query.addConditions(Tables.EH_APP_VERSION.NAMESPACE_ID.eq(namespaceId));
        }
        query.addOrderBy(Tables.EH_APP_VERSION.ID.desc());
        query.addLimit(1);
        return query.fetchAnyInto(AppVersion.class);
    }

    @Override
    public void cleanUserActivitiesWithNullAppVersion(Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhUserActivities t = Tables.EH_USER_ACTIVITIES;

        List<Long> ids = context.select(t.ID).from(t).where(t.APP_VERSION_NAME.isNull()).fetchInto(Long.class);
        for (Long id : ids) {
            context.delete(t).where(t.ID.eq(id)).execute();
        }
    }

    @Override
    public void createTerminalHourStatistics(List<TerminalHourStatistics> hourStats) {
        Timestamp createTime = new Timestamp(DateHelper.currentGMTTime().getTime());
        long id = this.sequenceProvider.getNextSequenceBlock(
                NameMapper.getSequenceDomainFromTablePojo(EhTerminalHourStatistics.class), hourStats.size());
        for (TerminalHourStatistics stat : hourStats) {
            stat.setId(id++);
            stat.setCreateTime(createTime);
        }
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhTerminalHourStatisticsDao dao = new EhTerminalHourStatisticsDao(context.configuration());
        dao.insert(hourStats.toArray(new EhTerminalHourStatistics[0]));
    }

    @Override
    public void deleteTerminalAppVersionCumulative(String imeiNumber, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhTerminalAppVersionActives t = Tables.EH_TERMINAL_APP_VERSION_ACTIVES;
        context.delete(t)
                .where(t.NAMESPACE_ID.eq(namespaceId))
                .and(t.IMEI_NUMBER.eq(imeiNumber))
                .execute();
    }

    @Override
    public void deleteTerminalStatTask(Integer namespaceId, String taskNo) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhTerminalStatisticsTasks t = Tables.EH_TERMINAL_STATISTICS_TASKS;
        context.delete(t)
                .where(t.NAMESPACE_ID.eq(namespaceId))
                .and(t.TASK_NO.eq(taskNo))
                .execute();
    }

    @Override
    public void cleanInvalidAppVersion(Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhAppVersion t = Tables.EH_APP_VERSION;

        List<AppVersion> list = context.selectFrom(t)
                .where(t.NAMESPACE_ID.eq(namespaceId)).fetchInto(AppVersion.class);

        for (AppVersion version : list) {
            if (VersionRealmType.fromCode(version.getRealm()) == null) {
                context.delete(t).where(t.ID.eq(version.getId())).execute();
            }
        }
    }

    @Override
    public List<String> listUserActivityAppVersions(Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhUserActivities t = Tables.EH_USER_ACTIVITIES;

        return context.select(t.APP_VERSION_NAME)
                .from(t)
                .where(t.NAMESPACE_ID.eq(namespaceId))
                .groupBy(t.APP_VERSION_NAME)
                .fetchInto(String.class);
    }

    @Override
    public void correctUserActivity(Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhUserActivities t = Tables.EH_USER_ACTIVITIES;

        context.delete(t)
                .where(t.NAMESPACE_ID.eq(namespaceId))
                .and(t.UID.eq(0L))
                .execute();
    }
}