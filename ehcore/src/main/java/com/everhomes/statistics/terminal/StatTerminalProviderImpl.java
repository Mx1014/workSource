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
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sfyan on 2016/11/24.
 */
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

    @Override
    public TerminalDayStatistics getTerminalDayStatisticsByDay(String date, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhTerminalDayStatisticsRecord> query = context.selectQuery(Tables.EH_TERMINAL_DAY_STATISTICS);
        query.addConditions(Tables.EH_TERMINAL_DAY_STATISTICS.DATE.eq(date));
        query.addConditions(Tables.EH_TERMINAL_DAY_STATISTICS.NAMESPACE_ID.eq(namespaceId));
        return query.fetchOne().map(r ->{
            return ConvertHelper.convert(r, TerminalDayStatistics.class);
        });
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
        return query.fetchOne().map(r ->{
            resules.add(ConvertHelper.convert(r, TerminalDayStatistics.class));
            return null;
        });
    }

    @Override
    public List<TerminalHourStatistics> listTerminalHourStatisticsByDay(String date, Integer namespaceId) {
        List<TerminalHourStatistics> resules = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhTerminalHourStatisticsRecord> query = context.selectQuery(Tables.EH_TERMINAL_HOUR_STATISTICS);
        query.addConditions(Tables.EH_TERMINAL_HOUR_STATISTICS.DATE.eq(date));
        query.addConditions(Tables.EH_TERMINAL_HOUR_STATISTICS.NAMESPACE_ID.eq(namespaceId));
        query.addOrderBy(Tables.EH_TERMINAL_HOUR_STATISTICS.HOUR);
        return query.fetch().map(r ->{
            resules.add(ConvertHelper.convert(r, TerminalHourStatistics.class));
            return null;
        });
    }

    @Override
    public TerminalDayStatistics statisticalUserActivity(String date, String hour, Integer namespaceId){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        TerminalDayStatistics statistics = new TerminalDayStatistics();

        Integer startingPosition = 10;
        if(!StringUtils.isEmpty(hour)){
            startingPosition = 13;
            date = date + " " + hour;
        }

        Condition condition = Tables.EH_USER_ACTIVITIES.CREATE_TIME.substring(0,startingPosition).eq(date);
        if(null != namespaceId){
            condition = condition.and(Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(namespaceId));
        }
        SelectConditionStep<Record2<Integer,Integer>> selectConditionStep1 = context.select(
                Tables.EH_USER_ACTIVITIES.ID.count(),
                Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.countDistinct() )
                .from(Tables.EH_USER_ACTIVITIES)
                .where(condition)
                .and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.isNotNull())
                .and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.ne(""));
        LOGGER.debug("statistical start number and active user number sql:{}", selectConditionStep1.getSQL());
        selectConditionStep1.fetchOne().map(r ->{
            statistics.setStartNumber(Long.valueOf(r.getValue(0).toString()));
            statistics.setActiveUserNumber(Long.valueOf(r.getValue(1).toString()));
            return null;
        });

        Condition condition1= Tables.EH_USER_ACTIVITIES.CREATE_TIME.substring(0,startingPosition).lt(date);
        if(null != namespaceId){
            condition1 = condition1.and(Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(namespaceId));
        }
        SelectConditionStep<Record1<Integer>> selectConditionStep2 = context.select(
                Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.countDistinct())
                .from(Tables.EH_USER_ACTIVITIES)
                .where(condition)
                .and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.notIn(
                        context.selectDistinct(
                                Tables.EH_USER_ACTIVITIES.IMEI_NUMBER)
                                .from(Tables.EH_USER_ACTIVITIES)
                                .where(condition1)

                ))
                .and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.isNotNull())
                .and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.ne(""));
        LOGGER.debug("statistical new user number sql:{}", selectConditionStep2.getSQL());
        selectConditionStep2.fetchOne().map(r ->{
                    statistics.setNewUserNumber(Long.valueOf(r.getValue(0).toString()));
                return null;
        });

        Condition condition2 = Tables.EH_USER_ACTIVITIES.CREATE_TIME.substring(0,startingPosition).le(date);
        if(null != namespaceId){
            condition2 = condition2.and(Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(namespaceId));
        }
        SelectConditionStep<Record1<Integer>> selectConditionStep3 = context.select(
                Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.countDistinct())
                .from(Tables.EH_USER_ACTIVITIES)
                .where(condition2)
                .and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.isNotNull())
                .and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.ne(""));
        LOGGER.debug("statistical cumulative user number sql:{}", selectConditionStep3.getSQL());
        selectConditionStep3.fetchOne().map(r ->{
            statistics.setCumulativeUserNumber(Long.valueOf(r.getValue(0).toString()));
            return null;
        });

        return statistics;
    }

    @Override
    public TerminalAppVersionStatistics statisticalAppVersionUserActivity(List<String> notVersions,String version, String date, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        TerminalAppVersionStatistics statistics = new TerminalAppVersionStatistics();

        Condition condition = Tables.EH_USER_ACTIVITIES.CREATE_TIME.substring(0,10).eq(date);
        if(null != namespaceId){
            condition = condition.and(Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(namespaceId));
        }
        Condition versionCond = Tables.EH_USER_ACTIVITIES.APP_VERSION_NAME.eq(version);
        if(version.split(".").length > 2){
            versionCond = versionCond.or(Tables.EH_USER_ACTIVITIES.APP_VERSION_NAME.like(version + ".%"));
        }
        Condition notInCond = null;
        if(null != notVersions && notVersions.size() > 0){
            notInCond = Tables.EH_USER_ACTIVITIES.APP_VERSION_NAME.notIn(notVersions);
            for (String v :notVersions) {
                notInCond = notInCond.or(Tables.EH_USER_ACTIVITIES.APP_VERSION_NAME.like(v + ".%"));
            }
        }

        if(null != notInCond){
            versionCond = versionCond.and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.notIn(
                    context.selectDistinct(
                            Tables.EH_USER_ACTIVITIES.IMEI_NUMBER)
                            .from(Tables.EH_USER_ACTIVITIES)
                            .where(notInCond)));
        }

        SelectConditionStep<Record2<Integer,Integer>> step1 = context.select(
                Tables.EH_USER_ACTIVITIES.ID.count(),
                Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.countDistinct() )
                .from(Tables.EH_USER_ACTIVITIES)
                .where(condition)
                .and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.isNotNull())
                .and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.ne(""))
                .and(versionCond);
        LOGGER.debug("statistical start number and active user number sql:{}", step1.getSQL());
        step1.fetchOne().map(r ->{
            statistics.setStartNumber(Long.valueOf(r.getValue(0).toString()));
            statistics.setActiveUserNumber(Long.valueOf(r.getValue(1).toString()));
            return null;
        });

        Condition ltCond= Tables.EH_USER_ACTIVITIES.CREATE_TIME.substring(0,10).lt(date);
        if(null != namespaceId){
            ltCond = ltCond.and(Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(namespaceId));
        }
        Condition newUserCond = Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.notIn(
                context.selectDistinct(
                        Tables.EH_USER_ACTIVITIES.IMEI_NUMBER)
                        .from(Tables.EH_USER_ACTIVITIES)
                        .where(ltCond)
                        .and(versionCond));
        SelectConditionStep<Record1<Integer>> step2 = context.select(
                Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.countDistinct() )
                .from(Tables.EH_USER_ACTIVITIES)
                .where(condition)
                .and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.isNotNull())
                .and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.ne(""))
                .and(versionCond)
                .and(newUserCond);
        LOGGER.debug("statistical start number and active user number sql:{}", step1.getSQL());
        step1.fetchOne().map(r ->{
            statistics.setNewUserNumber(Long.valueOf(r.getValue(0).toString()));
            return null;
        });

        Condition leCond = Tables.EH_USER_ACTIVITIES.CREATE_TIME.substring(0,10).le(date);
        if(null != namespaceId){
            leCond = leCond.and(Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(namespaceId));
        }
        SelectConditionStep<Record1<Integer>> step3 = context.select(
                Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.countDistinct() )
                .from(Tables.EH_USER_ACTIVITIES)
                .where(leCond)
                .and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.isNotNull())
                .and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.ne(""))
                .and(versionCond);
        LOGGER.debug("statistical new user number sql:{}", step3.getSQL());
        step3.fetchOne().map(r ->{
            statistics.setCumulativeUserNumber(Long.valueOf(r.getValue(0).toString()));
            return null;
        });
        return statistics;
    }

    @Override
    public Long getTerminalActiveUserNumberByDate(String startDate, String endDate, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Condition condition = Tables.EH_USER_ACTIVITIES.CREATE_TIME.substring(0,10).ge(startDate);
        condition = condition.and(Tables.EH_USER_ACTIVITIES.CREATE_TIME.substring(0,10).le(endDate));
        if(null != namespaceId){
            condition = condition.and(Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(namespaceId));
        }
        SelectConditionStep<Record1<Integer>> step = context.select(
                Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.countDistinct() )
                .from(Tables.EH_USER_ACTIVITIES)
                .where(condition)
                .and(Tables.EH_USER_ACTIVITIES.IMEI_NUMBER.isNotNull());
        LOGGER.debug("statistical cumulative user number sql:{}", step.getSQL());
        return step.fetchOne().map(r ->{
            return Long.valueOf(r.getValue(0).toString());
        });
    }
}
