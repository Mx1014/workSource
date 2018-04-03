package com.everhomes.statistics.terminal;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.sms.DateUtil;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sfyan on 2016/11/24.
 */
@Component
public class StatTerminalProviderImpl implements StatTerminalProvider{

    private static final Logger LOGGER = LoggerFactory.getLogger(StatTerminalProviderImpl.class);

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
        terminalAppVersionCumulatives.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
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
    public TerminalAppVersionCumulatives getTerminalAppVersionCumulative(String version, String imei, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhTerminalAppVersionCumulativesRecord> query = context.selectQuery(Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES);
        if(null != version){
            query.addConditions(Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES.APP_VERSION.eq(version));
        }
        query.addConditions(Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES.NAMESPACE_ID.eq(namespaceId));
        if(null != imei){
            query.addConditions(Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES.IMEI_NUMBER.eq(imei));
        }
        return query.fetchAnyInto(TerminalAppVersionCumulatives.class);
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
    public TerminalAppVersionActives getTerminalAppVersionActive(String date, String version, String imei, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhTerminalAppVersionActivesRecord> query = context.selectQuery(Tables.EH_TERMINAL_APP_VERSION_ACTIVES);
        query.addConditions(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.DATE.eq(date));
        if(null != version){
            query.addConditions(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.APP_VERSION.eq(version));
        }
        query.addConditions(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.NAMESPACE_ID.eq(namespaceId));
        if(null != imei){
            query.addConditions(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.IMEI_NUMBER.eq(imei));
        }
        return query.fetchAnyInto(TerminalAppVersionActives.class);
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
        List<TerminalDayStatistics> resules = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhTerminalDayStatisticsRecord> query = context.selectQuery(Tables.EH_TERMINAL_DAY_STATISTICS);
        query.addConditions(Tables.EH_TERMINAL_DAY_STATISTICS.DATE.ge(startDate));
        query.addConditions(Tables.EH_TERMINAL_DAY_STATISTICS.DATE.le(endDate));
        query.addConditions(Tables.EH_TERMINAL_DAY_STATISTICS.NAMESPACE_ID.eq(namespaceId));
        query.addOrderBy(Tables.EH_TERMINAL_DAY_STATISTICS.DATE);
        query.fetch().map(r ->{
            resules.add(ConvertHelper.convert(r, TerminalDayStatistics.class));
            return null;
        });

        return resules;
    }

    @Override
    public List<TerminalHourStatistics> listTerminalHourStatisticsByDay(String date, Integer namespaceId) {
        List<TerminalHourStatistics> resules = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhTerminalHourStatisticsRecord> query = context.selectQuery(Tables.EH_TERMINAL_HOUR_STATISTICS);
        query.addConditions(Tables.EH_TERMINAL_HOUR_STATISTICS.DATE.eq(date));
        query.addConditions(Tables.EH_TERMINAL_HOUR_STATISTICS.NAMESPACE_ID.eq(namespaceId));
        query.addOrderBy(Tables.EH_TERMINAL_HOUR_STATISTICS.HOUR);
        query.fetch().map(r ->{
            resules.add(ConvertHelper.convert(r, TerminalHourStatistics.class));
            return null;
        });

        return resules;
    }

    @Override
    public List<TerminalAppVersionStatistics> listTerminalAppVersionStatisticsByDay(String date, Integer namespaceId) {
        List<TerminalAppVersionStatistics> resules = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhTerminalAppVersionStatisticsRecord> query = context.selectQuery(Tables.EH_TERMINAL_APP_VERSION_STATISTICS);
        query.addConditions(Tables.EH_TERMINAL_APP_VERSION_STATISTICS.DATE.eq(date));
        query.addConditions(Tables.EH_TERMINAL_APP_VERSION_STATISTICS.NAMESPACE_ID.eq(namespaceId));
        query.fetch().map(r ->{
            resules.add(ConvertHelper.convert(r, TerminalAppVersionStatistics.class));
            return null;
        });

        return resules;
    }

    @Override
    public TerminalDayStatistics statisticalUserActivity(String date, String hour, Integer namespaceId){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        TerminalDayStatistics statistics = new TerminalDayStatistics();

        Condition condition = Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(namespaceId);

        condition = condition.and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.isNotNull())
                .and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.ne(""));

        // Integer startingPosition = 10;
        String newDate = date + " 00";
        long mill = 24 * 60 * 60 * 1000 - 1;

        if(!StringUtils.isEmpty(hour)){
            // startingPosition = 13;
            newDate = date + " " + hour;
            mill = 60 * 60 * 1000 - 1;
        }

        Date date1 = DateUtil.strToDate(newDate, "yyyy-MM-dd HH");
        long time1 = date1.getTime();
        Timestamp minTime = new Timestamp(time1);
        Timestamp maxTime = new Timestamp(time1 + mill);

        condition = condition.and(Tables.EH_USER_ACTIVITIES.CREATE_TIME.between(minTime, maxTime));
        // Condition condition = Tables.EH_USER_ACTIVITIES.CREATE_TIME.substring(1,startingPosition).eq(date);
        /*if(null != namespaceId){
            condition = condition.and(Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(namespaceId));
        }*/
        SelectConditionStep<Record2<Integer,Integer>> selectConditionStep1 = context.select(
                Tables.EH_USER_ACTIVITIES.ID.count(),
                Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.countDistinct())
                .from(Tables.EH_USER_ACTIVITIES)
                .where(condition);
        Record2<Integer, Integer> fetchAny = selectConditionStep1.fetchAny();
        if(null == fetchAny){
            statistics.setStartNumber(0L);
            statistics.setActiveUserNumber(0L);
        }else{
            fetchAny.map(r ->{
                statistics.setStartNumber(Long.valueOf(r.getValue(0).toString()));
                statistics.setActiveUserNumber(Long.valueOf(r.getValue(1).toString()));
                return null;
            });
        }


        Condition condition1 = Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(namespaceId);
        condition1 = condition1.and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.isNotNull());
        condition1 = condition1.and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.ne(""));

        condition1 = condition1.and(Tables.EH_USER_ACTIVITIES.CREATE_TIME.lt(minTime));
        // Condition condition1= Tables.EH_USER_ACTIVITIES.CREATE_TIME.substring(1,startingPosition).lt(date);
        /*if(null != namespaceId){
            condition1 = condition1.and(Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(namespaceId));
        }*/

        SelectConditionStep<Record1<Integer>> selectConditionStep2 = context.select(
                Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.countDistinct())
                .from(Tables.EH_USER_ACTIVITIES)
                .where(condition)
                .and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.notIn(
                        context.selectDistinct(
                                Tables.EH_USER_ACTIVITIES.IMEI_NUMBER)
                                .from(Tables.EH_USER_ACTIVITIES)
                                .where(condition1)

                ));
        Record1<Integer> fetchAny1 = selectConditionStep2.fetchAny();
        if(null == fetchAny1){
            statistics.setNewUserNumber(0L);
        }else {
            fetchAny1.map(r -> {
                statistics.setNewUserNumber(Long.valueOf(r.getValue(0).toString()));
                return null;
            });
        }

        long previousTimestamp = time1 - mill;
        String previousTime = DateUtil.dateToStr(new Date(previousTimestamp), "yyyyMMdd");

        Table tb;
        Field fd;
        Condition condition2;
        if (hour != null) {
            int hourInt = Integer.parseInt(hour);
            String hourStr;
            if (hourInt <= 1) {
                long yesterdayTime = previousTimestamp - mill - mill;
                previousTime = DateUtil.dateToStr(new Date(yesterdayTime), "yyyyMMdd");
                hourStr = "24";
            } else {
                int h = hourInt - 1;
                if (h < 10) {
                    hourStr = "0" + h;
                } else {
                    hourStr = String.valueOf(h);
                }
            }

            tb = Tables.EH_TERMINAL_HOUR_STATISTICS;
            fd = Tables.EH_TERMINAL_HOUR_STATISTICS.CUMULATIVE_USER_NUMBER;
            condition2 = Tables.EH_TERMINAL_HOUR_STATISTICS.NAMESPACE_ID.eq(namespaceId);
            condition2 = condition2.and(Tables.EH_TERMINAL_HOUR_STATISTICS.DATE.eq(previousTime));

            condition2 = condition2.and(Tables.EH_TERMINAL_HOUR_STATISTICS.HOUR.eq(hourStr));
        } else {
            tb = Tables.EH_TERMINAL_DAY_STATISTICS;
            fd = Tables.EH_TERMINAL_DAY_STATISTICS.CUMULATIVE_USER_NUMBER;
            condition2 = Tables.EH_TERMINAL_DAY_STATISTICS.NAMESPACE_ID.eq(namespaceId);
            condition2 = condition2.and(Tables.EH_TERMINAL_DAY_STATISTICS.DATE.eq(previousTime));
        }

        Record record = context.select(fd).from(tb).where(condition2).fetchAny();
        if (record != null) {
            statistics.setCumulativeUserNumber(Long.valueOf(record.getValue(0).toString()) + statistics.getNewUserNumber());
        } else {
            statistics.setCumulativeUserNumber(0L);
        }

        /*Condition condition2 = Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(namespaceId);
        condition2 = condition2.and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.isNotNull());
        condition2 = condition2.and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.ne(""));

        condition2 = condition2.and(Tables.EH_USER_ACTIVITIES.CREATE_TIME.le(minTime));
        // Condition condition2 = Tables.EH_USER_ACTIVITIES.CREATE_TIME.substring(1,startingPosition).le(date);
       *//* if(null != namespaceId){
            condition2 = condition2.and(Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(namespaceId));
        }*//*
        SelectConditionStep<Record1<Integer>> selectConditionStep3 = context.select(
                Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.countDistinct())
                .from(Tables.EH_USER_ACTIVITIES)
                .where(condition2);
        Record1<Integer> fetchAny2 = selectConditionStep3.fetchAny();
        if(null == fetchAny2){
            statistics.setCumulativeUserNumber(0L);
        }else {
            fetchAny2.map(r -> {
                statistics.setCumulativeUserNumber(Long.valueOf(r.getValue(0).toString()));
                return null;
            });
        }*/

        return statistics;
    }



    @Override
    public Long getTerminalActiveUserNumberByDate(String startDate, String endDate, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        Condition condition = Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(namespaceId);

        condition = condition.and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.isNotNull());
        condition = condition.and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.ne(""));

        Date date1 = DateUtil.strToDate(startDate, "yyyy-MM-dd");
        long time1 = date1.getTime();
        Timestamp minTime = new Timestamp(time1);

        Date date2 = DateUtil.strToDate(endDate, "yyyy-MM-dd");
        long time2 = date2.getTime();
        Timestamp maxTime = new Timestamp(time2);

        condition = condition.and(Tables.EH_USER_ACTIVITIES.CREATE_TIME.between(minTime, maxTime));

        // Condition condition = Tables.EH_USER_ACTIVITIES.CREATE_TIME.substring(1,10).ge(startDate);
        // condition = condition.and(Tables.EH_USER_ACTIVITIES.CREATE_TIME.substring(1,10).le(endDate));
        /*if(null != namespaceId){
            condition = condition.and(Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(namespaceId));
        }*/
        SelectConditionStep<Record1<Integer>> step = context.select(
                Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.countDistinct())
                .from(Tables.EH_USER_ACTIVITIES)
                .where(condition);
        Record1<Integer> fetchAny = step.fetchAny();
        if(null == fetchAny){
            return 0L;
        }
        return fetchAny.map(r ->{
            return Long.valueOf(r.getValue(0).toString());
        });
    }


    @Override
    public Long getTerminalCumulativeUserNumber(String version, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Condition condition = Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES.NAMESPACE_ID.eq(namespaceId);
        if(!StringUtils.isEmpty(version)){
            condition = condition.and(Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES.APP_VERSION.eq(version));
        }
        SelectConditionStep<Record1<Integer>> step = context.select(
                Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES.IMEI_NUMBER.countDistinct())
                .from(Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES)
                .where(condition);
        Record1<Integer> fetchAny = step.fetchAny();
        if(null == fetchAny){
            return 0L;
        }
        return fetchAny.map(r ->{
            return Long.valueOf(r.getValue(0).toString());
        });
    }

    @Override
    public Long getTerminalAppVersionActiveUserNumberByDay(String date, String version, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Condition condition = Tables.EH_TERMINAL_APP_VERSION_ACTIVES.NAMESPACE_ID.eq(namespaceId);
        condition = condition.and(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.DATE.eq(date));
        if(!StringUtils.isEmpty(version)){
            condition = condition.and(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.APP_VERSION.eq(version));
        }
        SelectConditionStep<Record1<Integer>> step = context.select(
                Tables.EH_TERMINAL_APP_VERSION_ACTIVES.IMEI_NUMBER.countDistinct())
                .from(Tables.EH_TERMINAL_APP_VERSION_ACTIVES)
                .where(condition);
        Record1<Integer> fetchAny = step.fetchAny();
        if(null == fetchAny){
            return 0L;
        }
        return fetchAny.map(r ->{
            return Long.valueOf(r.getValue(0).toString());
        });
    }

    @Override
    public Long getTerminalAppVersionNewUserNumberByDay(String date, String version, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Condition condition = Tables.EH_TERMINAL_APP_VERSION_ACTIVES.NAMESPACE_ID.eq(namespaceId);
        condition = condition.and(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.DATE.eq(date));
        if(!StringUtils.isEmpty(version)){
            condition = condition.and(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.APP_VERSION.eq(version));
        }

        Condition condition1= Tables.EH_TERMINAL_APP_VERSION_ACTIVES.DATE.lt(date);
        if(null != namespaceId){
            condition1 = condition1.and(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.NAMESPACE_ID.eq(namespaceId));
        }
        condition = condition.and(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.IMEI_NUMBER.notIn(context.selectDistinct(
                Tables.EH_TERMINAL_APP_VERSION_ACTIVES.IMEI_NUMBER)
                .from(Tables.EH_TERMINAL_APP_VERSION_ACTIVES)
                .where(condition1)));
        SelectConditionStep<Record1<Integer>> step = context.select(
                Tables.EH_TERMINAL_APP_VERSION_ACTIVES.IMEI_NUMBER.countDistinct())
                .from(Tables.EH_TERMINAL_APP_VERSION_ACTIVES)
                .where(condition);
        Record1<Integer> fetchAny = step.fetchAny();
        if(null == fetchAny){
            return 0L;
        }
        return fetchAny.map(r ->{
            return Long.valueOf(r.getValue(0).toString());
        });
    }

    @Override
    public Long getTerminalStartNumberByDay(String date, String version, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        Condition condition = Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(namespaceId);

        condition = condition.and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.isNotNull())
                .and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.ne(""));

        if(!StringUtils.isEmpty(version)){
            Condition versionCond = Tables.EH_USER_ACTIVITIES.APP_VERSION_NAME.eq(version);
            if(version.split("\\.").length > 2){
                versionCond = versionCond.or(Tables.EH_USER_ACTIVITIES.APP_VERSION_NAME.like(version + ".%"));
            }
            condition = condition.and(versionCond);
        }

        long aDayMill = 24 * 60 * 60 * 1000 - 1;
        Date date1 = DateUtil.strToDate(date, "yyyy-MM-dd");

        long time = date1.getTime();
        Timestamp minTime = new Timestamp(time);
        Timestamp maxTime = new Timestamp(time + aDayMill);

        condition = condition.and(Tables.EH_USER_ACTIVITIES.CREATE_TIME.between(minTime, maxTime));

        // Condition condition = Tables.EH_USER_ACTIVITIES.CREATE_TIME.substring(1,10).eq(date);
        /*if(null != namespaceId){
            condition = condition.and(Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(namespaceId));
        }*/

        SelectConditionStep<Record1<Integer>> step = context.select(
                Tables.EH_USER_ACTIVITIES.ID.count())
                .from(Tables.EH_USER_ACTIVITIES)
                .where(condition);
        Record1<Integer> fetchAny = step.fetchAny();
        if(null == fetchAny){
            return 0L;
        }
        return fetchAny.map(r ->{
            return Long.valueOf(r.getValue(0).toString());
        });
    }

    @Override
    public List<AppVersion> listAppVersions(Integer namespaceId) {
        List<AppVersion> resules = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhAppVersionRecord> query = context.selectQuery(Tables.EH_APP_VERSION);
        if(null != namespaceId){
            query.addConditions(Tables.EH_APP_VERSION.NAMESPACE_ID.eq(namespaceId));
        }else{
            query.addGroupBy(Tables.EH_APP_VERSION.NAMESPACE_ID);
        }
        query.addGroupBy(Tables.EH_APP_VERSION.NAME);
        query.addOrderBy(Tables.EH_APP_VERSION.DEFAULT_ORDER.desc());

        query.fetch().map(r ->{
            resules.add(ConvertHelper.convert(r, AppVersion.class));
            return null;
        });

        return resules;
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
        List<AppVersion> resules = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhAppVersionRecord> query = context.selectQuery(Tables.EH_APP_VERSION);
        query.addConditions(Tables.EH_APP_VERSION.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_APP_VERSION.NAME.eq(name));
        query.addConditions(Tables.EH_APP_VERSION.TYPE.eq(type));
        query.fetch().map(r ->{
            resules.add(ConvertHelper.convert(r, AppVersion.class));
            return null;
        });

        if(resules.size() > 0){
            return resules.get(0);
        }
        return null;
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
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectConditionStep<Record1<String>> userIdList = context
                .select(DSL.cast(Tables.EH_USERS.ID, SQLDataType.VARCHAR))
                .from(Tables.EH_USERS)
                .where(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId));

        context.delete(Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES)
                .where(Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES.IMEI_NUMBER.notIn(userIdList))
                .execute();

        context.delete(Tables.EH_TERMINAL_APP_VERSION_ACTIVES)
                .where(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.IMEI_NUMBER.notIn(userIdList))
                .execute();
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
        context.delete(Tables.EH_USER_ACTIVITIES)
                .where(Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_USER_ACTIVITIES.APP_VERSION_NAME.isNull())
                .execute();
    }
}
