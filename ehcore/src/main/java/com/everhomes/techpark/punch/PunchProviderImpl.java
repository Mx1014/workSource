package com.everhomes.techpark.punch;

import com.everhomes.archives.ArchivesService;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.rest.approval.ApprovalStatus;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.ListTargetType;
import com.everhomes.rest.general_approval.GeneralApprovalAttribute;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.techpark.punch.ClockCode;
import com.everhomes.rest.techpark.punch.CreateType;
import com.everhomes.rest.techpark.punch.DateStatus;
import com.everhomes.rest.techpark.punch.ExtDTO;
import com.everhomes.rest.techpark.punch.PunchDayLogDTO;
import com.everhomes.rest.techpark.punch.PunchExceptionRequestStatisticsItemDTO;
import com.everhomes.rest.techpark.punch.PunchExceptionRequestStatisticsItemType;
import com.everhomes.rest.techpark.punch.PunchOwnerType;
import com.everhomes.rest.techpark.punch.PunchRquestType;
import com.everhomes.rest.techpark.punch.PunchRuleStatus;
import com.everhomes.rest.techpark.punch.PunchStatus;
import com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemType;
import com.everhomes.rest.techpark.punch.PunchType;
import com.everhomes.rest.techpark.punch.TimeCompareFlag;
import com.everhomes.rest.techpark.punch.UserPunchStatusCount;
import com.everhomes.rest.techpark.punch.ViewFlags;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhApprovalRequestsDao;
import com.everhomes.server.schema.tables.daos.EhPunchDayLogsDao;
import com.everhomes.server.schema.tables.daos.EhPunchExceptionApprovalsDao;
import com.everhomes.server.schema.tables.daos.EhPunchExceptionRequestsDao;
import com.everhomes.server.schema.tables.daos.EhPunchGeopointsDao;
import com.everhomes.server.schema.tables.daos.EhPunchGoOutLogsDao;
import com.everhomes.server.schema.tables.daos.EhPunchHolidaysDao;
import com.everhomes.server.schema.tables.daos.EhPunchLocationRulesDao;
import com.everhomes.server.schema.tables.daos.EhPunchLogsDao;
import com.everhomes.server.schema.tables.daos.EhPunchNotificationsDao;
import com.everhomes.server.schema.tables.daos.EhPunchOvertimeRulesDao;
import com.everhomes.server.schema.tables.daos.EhPunchRuleOwnerMapDao;
import com.everhomes.server.schema.tables.daos.EhPunchRulesDao;
import com.everhomes.server.schema.tables.daos.EhPunchSpecialDaysDao;
import com.everhomes.server.schema.tables.daos.EhPunchStatisticsDao;
import com.everhomes.server.schema.tables.daos.EhPunchTimeIntervalsDao;
import com.everhomes.server.schema.tables.daos.EhPunchTimeRulesDao;
import com.everhomes.server.schema.tables.daos.EhPunchWifiRulesDao;
import com.everhomes.server.schema.tables.daos.EhPunchWifisDao;
import com.everhomes.server.schema.tables.daos.EhPunchWorkdayRulesDao;
import com.everhomes.server.schema.tables.pojos.EhPunchDayLogs;
import com.everhomes.server.schema.tables.pojos.EhPunchExceptionApprovals;
import com.everhomes.server.schema.tables.pojos.EhPunchExceptionRequests;
import com.everhomes.server.schema.tables.pojos.EhPunchGeopoints;
import com.everhomes.server.schema.tables.pojos.EhPunchGoOutLogs;
import com.everhomes.server.schema.tables.pojos.EhPunchHolidays;
import com.everhomes.server.schema.tables.pojos.EhPunchLocationRules;
import com.everhomes.server.schema.tables.pojos.EhPunchLogs;
import com.everhomes.server.schema.tables.pojos.EhPunchNotifications;
import com.everhomes.server.schema.tables.pojos.EhPunchOvertimeRules;
import com.everhomes.server.schema.tables.pojos.EhPunchRuleOwnerMap;
import com.everhomes.server.schema.tables.pojos.EhPunchRules;
import com.everhomes.server.schema.tables.pojos.EhPunchSpecialDays;
import com.everhomes.server.schema.tables.pojos.EhPunchStatistics;
import com.everhomes.server.schema.tables.pojos.EhPunchTimeIntervals;
import com.everhomes.server.schema.tables.pojos.EhPunchTimeRules;
import com.everhomes.server.schema.tables.pojos.EhPunchWifiRules;
import com.everhomes.server.schema.tables.pojos.EhPunchWifis;
import com.everhomes.server.schema.tables.pojos.EhPunchWorkday;
import com.everhomes.server.schema.tables.pojos.EhPunchWorkdayRules;
import com.everhomes.server.schema.tables.records.EhPunchDayLogsRecord;
import com.everhomes.server.schema.tables.records.EhPunchExceptionApprovalsRecord;
import com.everhomes.server.schema.tables.records.EhPunchExceptionRequestsRecord;
import com.everhomes.server.schema.tables.records.EhPunchGeopointsRecord;
import com.everhomes.server.schema.tables.records.EhPunchHolidaysRecord;
import com.everhomes.server.schema.tables.records.EhPunchLocationRulesRecord;
import com.everhomes.server.schema.tables.records.EhPunchLogsRecord;
import com.everhomes.server.schema.tables.records.EhPunchNotificationsRecord;
import com.everhomes.server.schema.tables.records.EhPunchOvertimeRulesRecord;
import com.everhomes.server.schema.tables.records.EhPunchRuleOwnerMapRecord;
import com.everhomes.server.schema.tables.records.EhPunchRulesRecord;
import com.everhomes.server.schema.tables.records.EhPunchSchedulingsRecord;
import com.everhomes.server.schema.tables.records.EhPunchSpecialDaysRecord;
import com.everhomes.server.schema.tables.records.EhPunchStatisticsRecord;
import com.everhomes.server.schema.tables.records.EhPunchTimeIntervalsRecord;
import com.everhomes.server.schema.tables.records.EhPunchTimeRulesRecord;
import com.everhomes.server.schema.tables.records.EhPunchWifiRulesRecord;
import com.everhomes.server.schema.tables.records.EhPunchWifisRecord;
import com.everhomes.server.schema.tables.records.EhPunchWorkdayRecord;
import com.everhomes.server.schema.tables.records.EhPunchWorkdayRulesRecord;
import com.everhomes.techpark.punch.recordmapper.DailyPunchStatusStatisticsHistoryRecordMapper;
import com.everhomes.techpark.punch.recordmapper.DailyPunchStatusStatisticsTodayRecordMapper;
import com.everhomes.techpark.punch.recordmapper.DailyStatisticsByDepartmentBaseRecordMapper;
import com.everhomes.techpark.punch.recordmapper.DailyStatisticsByDepartmentHistoryRecordMapper;
import com.everhomes.techpark.punch.recordmapper.DailyStatisticsByDepartmentTodayRecordMapper;
import com.everhomes.techpark.punch.recordmapper.MonthlyPunchStatusStatisticsRecordMapper;
import com.everhomes.techpark.punch.recordmapper.MonthlyStatisticsByDepartmentRecordMapper;
import com.everhomes.techpark.punch.recordmapper.MonthlyStatisticsByMemberRecordMapper;
import com.everhomes.techpark.punch.recordmapper.PunchDayLogRecordMapper;
import com.everhomes.techpark.punch.recordmapper.PunchExceptionRequestStatisticsRecordMapper;
import com.everhomes.techpark.punch.recordmapper.PunchStatisticRecordMapper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.DeleteWhereStep;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record11;
import org.jooq.Record14;
import org.jooq.Record18;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Record8;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.SelectHavingStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
import org.jooq.UpdateConditionStep;
import org.jooq.UpdateQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PunchProviderImpl implements PunchProvider {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(PunchServiceImpl.class);
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private ArchivesService archivesService;
    @Override
    @Caching(evict = {@CacheEvict(value = "GetPunchRuleByPunchOrgId", key = "#obj.punchOrganizationId")})
    public Long createPunchRule(PunchRule obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchRules.class);
        long id = sequenceProvider.getNextSequence(key);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhPunchRulesDao dao = new EhPunchRulesDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "GetPunchRuleById", key = "#obj.id"),
            @CacheEvict(value = "GetPunchRuleByPunchOrgId", key = "#obj.punchOrganizationId"),
            @CacheEvict(value = "FindPunchOvertimeRulesByPunchRuleId", allEntries = true)})
    public void updatePunchRule(PunchRule obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchRulesDao dao = new EhPunchRulesDao(context.configuration());
        dao.update(obj);
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "GetPunchRuleById", key = "#obj.id"),
            @CacheEvict(value = "GetPunchRuleByPunchOrgId", key = "#obj.punchOrganizationId"),
            @CacheEvict(value = "FindPunchOvertimeRulesByPunchRuleId", allEntries = true)})
    public void deletePunchRule(PunchRule obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchRulesDao dao = new EhPunchRulesDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    @Cacheable(value = "GetPunchRuleById", key = "#id", unless = "#result == null")
    public PunchRule getPunchRuleById(Long id) {
        try {
            PunchRule[] result = new PunchRule[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

            result[0] = context.select().from(Tables.EH_PUNCH_RULES)
                    .where(Tables.EH_PUNCH_RULES.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, PunchRule.class);
                    });

            return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<PunchRule> queryPunchRules(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhPunchRulesRecord> query = context.selectQuery(Tables.EH_PUNCH_RULES);
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if (locator != null && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_PUNCH_RULES.ID.gt(locator.getAnchor()));
        }

        query.addOrderBy(Tables.EH_PUNCH_RULES.ID.asc());
        query.addLimit(count);
        List<PunchRule> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, PunchRule.class);
        });

        if (null == objs || objs.isEmpty())
            return null;
        return objs;
    }

    private void prepareObj(PunchRule obj) {
    }

    // @Cacheable(value="PunchLogs-List", key="{#queryDate,#userId,#companyId}",
    // unless="#result.size() == 0")
    @Override
    public List<PunchLog> listPunchLogsByDate(Long userId, Long companyId,
                                              String queryDate, byte clockCode) {
        Date date = java.sql.Date.valueOf(queryDate);
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        //DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_LOGS);
        Condition condition = Tables.EH_PUNCH_LOGS.PUNCH_DATE.equal(date);
        Condition condition2 = Tables.EH_PUNCH_LOGS.USER_ID.equal(userId);
        Condition condition3 = Tables.EH_PUNCH_LOGS.ENTERPRISE_ID.equal(companyId);
        Condition condition4 = Tables.EH_PUNCH_LOGS.PUNCH_STATUS.equal(clockCode);
        condition = condition.and(condition2);
        condition = condition.and(condition3);
        condition = condition.and(condition4);
        step.where(condition);
        List<PunchLog> result = step.orderBy(Tables.EH_PUNCH_LOGS.USER_ID.asc(),
                Tables.EH_PUNCH_LOGS.PUNCH_DATE.asc(), Tables.EH_PUNCH_LOGS.PUNCH_INTERVAL_NO.asc(),
                Tables.EH_PUNCH_LOGS.PUNCH_TYPE.asc(), Tables.EH_PUNCH_LOGS.PUNCH_TIME.asc())
                .fetch().map((r) -> {
                    return ConvertHelper.convert(r, PunchLog.class);
                });
        return result;
    }

    @Override
    public List<Date> listPunchLogsBwteenTwoDay(Long userId, Long companyId,
                                                String beginDate, String endDate) {
        Date beginSqlDate = java.sql.Date.valueOf(beginDate);
        Date endSqlDate = java.sql.Date.valueOf(endDate);
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record1<Date>> step = context.selectDistinct(
                Tables.EH_PUNCH_LOGS.PUNCH_DATE).from(Tables.EH_PUNCH_LOGS);
        Condition condition3 = Tables.EH_PUNCH_LOGS.PUNCH_DATE.between(
                beginSqlDate, endSqlDate);
        Condition condition2 = Tables.EH_PUNCH_LOGS.USER_ID.equal(userId);
        Condition condition = Tables.EH_PUNCH_LOGS.ENTERPRISE_ID.equal(companyId);
        condition = condition.and(condition2);
        condition = condition.and(condition3);
        step.where(condition);
        List<Date> result = step
                .orderBy(Tables.EH_PUNCH_LOGS.PUNCH_DATE.desc()).fetch()
                .map(r -> r.getValue(Tables.EH_PUNCH_LOGS.PUNCH_DATE));
        return result;
    }

    @Override
    public PunchTimeRule getPunchTimeRuleByCompanyId(String ownerType, Long companyId) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_TIME_RULES);
        Condition condition = Tables.EH_PUNCH_TIME_RULES.OWNER_ID.equal(companyId).and(Tables.EH_PUNCH_TIME_RULES.OWNER_TYPE.equal(ownerType));
        step.where(condition);
        List<PunchTimeRule> result = step.orderBy(Tables.EH_PUNCH_TIME_RULES.ID.desc())
                .fetch().map((r) -> {
                    return ConvertHelper.convert(r, PunchTimeRule.class);
                });
        if (null != result && result.size() > 0)
            return result.get(0);
        return null;
    }

    // @Caching(evict = {
    // @CacheEvict(value="PunchLogs-List",
    // key="{#punchLog.punchDate,#punchLog.userId,#punchLog.companyId}")
    // })
    @Override
    public void createPunchLog(PunchLog punchLog) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class,punchLog.getEnterpriseId() ));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
//long id = sequenceProvider.getNextSequence(NameMapper
//				.getSequenceDomainFromTablePojo(EhPunchLogs.class));
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchLogs.class);
        long id = sequenceProvider.getNextSequence(key);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("line 139 : Create punch log, key=" + key + ", newId=" + id + ", enterpriseId=" + punchLog.getEnterpriseId());
        }

        punchLog.setId(id);
        if(punchLog.getCreateType() == null){
        	punchLog.setCreateType(CreateType.NORMAL_PUNCH.getCode());
        }
        EhPunchLogsRecord record = ConvertHelper.convert(punchLog,
                EhPunchLogsRecord.class);
        InsertQuery<EhPunchLogsRecord> query = context
                .insertQuery(Tables.EH_PUNCH_LOGS);
        query.setRecord(record);
        query.execute();

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPunchLogs.class, null);

    }

    @Override
    public void deletePunchLog(PunchLog punchLog) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhPunchLogs.class));
        EhPunchLogsDao dao = new EhPunchLogsDao(context.configuration());
        dao.delete(punchLog);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchLogs.class, punchLog.getId());
    }

    @Override
    public List<PunchGeopoint> listPunchGeopointsByCompanyId(Long companyId) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_GEOPOINTS);
        Condition condition = Tables.EH_PUNCH_GEOPOINTS.ENTERPRISE_ID
                .equal(companyId);
        step.where(condition);
        List<PunchGeopoint> result = step
                .orderBy(Tables.EH_PUNCH_GEOPOINTS.ID.desc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, PunchGeopoint.class);
                });
        return result;
    }

    @Override
    public void createPunchTimeRule(PunchTimeRule punchTimeRule) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhPunchTimeRules.class));
        punchTimeRule.setId(id);
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class,punchRule.getEnterpriseId() ));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchTimeRulesRecord record = ConvertHelper.convert(punchTimeRule,
                EhPunchTimeRulesRecord.class);
        InsertQuery<EhPunchTimeRulesRecord> query = context.insertQuery(Tables.EH_PUNCH_TIME_RULES);
        query.setRecord(record);
        query.execute();

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPunchTimeRules.class, null);

    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindPunchTimeRuleById", key = "#punchTimeRule.id")})
    public void updatePunchTimeRule(PunchTimeRule punchTimeRule) {
        assert (punchTimeRule.getId() == null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchTimeRulesDao dao = new EhPunchTimeRulesDao(context.configuration());
        dao.update(punchTimeRule);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchTimeRules.class,
                punchTimeRule.getId());
    }

    @Override
    @Cacheable(value = "FindPunchTimeRuleById", key = "#id", unless = "#result == null")
    public PunchTimeRule getPunchTimeRuleById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchTimeRulesDao dao = new EhPunchTimeRulesDao(context.configuration());
        EhPunchTimeRules result = dao.findById(id);
        if (null == result)
            return null;
        return ConvertHelper.convert(result, PunchTimeRule.class);
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindPunchTimeRuleById", key = "#punchTimeRule.id")})
    public void deletePunchTimeRule(PunchTimeRule punchTimeRule) {
//
//		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
//		EhPunchTimeRulesDao dao = new EhPunchTimeRulesDao(context.configuration());
//		dao.deleteById(punchTimeRule.getId());
//
//		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchTimeRules.class,
//				punchTimeRule.getId());
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindPunchTimeRuleById", key = "#id")})
    public void deletePunchTimeRuleById(Long id) {

//		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
//		EhPunchTimeRulesDao dao = new EhPunchTimeRulesDao(context.configuration());
//		dao.deleteById(id);
//
//		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchTimeRules.class,id);
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindPunchTimeRuleById", key = "#id")})
    public void deletePunchTimeRuleByOwnerAndId(String ownerType, Long ownerId, Long id) {
//		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
//		DeleteWhereStep<EhPunchTimeRulesRecord> step = context
//				.delete(Tables.EH_PUNCH_TIME_RULES);
//		Condition condition = Tables.EH_PUNCH_TIME_RULES.ID.equal(id)
//				.and(Tables.EH_PUNCH_TIME_RULES.OWNER_ID.equal(ownerId))
//				.and(Tables.EH_PUNCH_TIME_RULES.OWNER_TYPE.equal(ownerType)) ; 
//		step.where(condition);
//		step.execute();
    }

    @Override
    @Cacheable(value = "FindPunchTimeRuleById", key = "#id", unless = "#result == null")
    public PunchTimeRule findPunchTimeRuleById(Long id) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhPunchTimeRulesDao dao = new EhPunchTimeRulesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), PunchTimeRule.class);
    }

    @Override
    public PunchTimeRule findPunchTimeRuleByCompanyId(Long companyId) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhPunchTimeRulesRecord> query = context
                .selectQuery(Tables.EH_PUNCH_TIME_RULES);
        query.addConditions(Tables.EH_PUNCH_TIME_RULES.OWNER_ID.eq(companyId));

        List<PunchTimeRule> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, PunchTimeRule.class));
            return null;
        });
        if (null != result && result.size() > 0)
            return result.get(0);
        return null;
    }

    @Override
    public List<PunchTimeRule> queryPunchTimeRules(String ownerType, Long ownerId, String targetType, Long targetId, String name) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhPunchTimeRulesRecord> query = context
                .selectQuery(Tables.EH_PUNCH_TIME_RULES);
        if (null != name)
            query.addConditions(Tables.EH_PUNCH_TIME_RULES.NAME.eq(name));
        query.addConditions(Tables.EH_PUNCH_TIME_RULES.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_PUNCH_TIME_RULES.OWNER_TYPE.eq(ownerType));
        if (null != targetType)
            query.addConditions(Tables.EH_PUNCH_TIME_RULES.TARGET_TYPE.eq(targetType));
        if (null != targetId)
            query.addConditions(Tables.EH_PUNCH_TIME_RULES.TARGET_ID.eq(targetId));

        List<PunchTimeRule> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, PunchTimeRule.class));
            return null;
        });
        if (null != result && result.size() > 0)
            return result;
        return null;
    }

    @Override
    public void createPunchGeopoint(PunchGeopoint punchGeopoint) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhPunchGeopoints.class));
        punchGeopoint.setId(id);
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class,punchGeopoint.getEnterpriseId() ));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchGeopointsRecord record = ConvertHelper.convert(punchGeopoint,
                EhPunchGeopointsRecord.class);
        InsertQuery<EhPunchGeopointsRecord> query = context
                .insertQuery(Tables.EH_PUNCH_GEOPOINTS);
        query.setRecord(record);
        query.execute();

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPunchGeopoints.class,
                null);

    }

    @Override
    public void updatePunchGeopoint(PunchGeopoint punchGeopoint) {
        assert (punchGeopoint.getId() == null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchGeopointsDao dao = new EhPunchGeopointsDao(
                context.configuration());
        dao.update(punchGeopoint);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchGeopoints.class,
                punchGeopoint.getId());
    }

    @Override
    public void deletePunchGeopoint(PunchGeopoint punchGeopoint) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchGeopointsDao dao = new EhPunchGeopointsDao(
                context.configuration());
        dao.deleteById(punchGeopoint.getId());

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchGeopoints.class,
                punchGeopoint.getId());
    }

    @Override
    public void deletePunchGeopointById(Long id) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchGeopointsDao dao = new EhPunchGeopointsDao(
                context.configuration());
        dao.deleteById(id);

        DaoHelper
                .publishDaoAction(DaoAction.MODIFY, EhPunchGeopoints.class, id);
    }

    @Override
    public PunchGeopoint findPunchGeopointById(Long id) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhPunchGeopointsDao dao = new EhPunchGeopointsDao(
                context.configuration());
        return ConvertHelper.convert(dao.findById(id), PunchGeopoint.class);
    }

    @Override
    public void createPunchWorkday(PunchWorkday punchWorkday) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhPunchWorkday.class));
        punchWorkday.setId(id);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchWorkdayRecord record = ConvertHelper.convert(punchWorkday,
                EhPunchWorkdayRecord.class);
        InsertQuery<EhPunchWorkdayRecord> query = context
                .insertQuery(Tables.EH_PUNCH_WORKDAY);
        query.setRecord(record);
        // query.setReturning(Tables.EH_PUNCH_TIME_RULES.ID);
        query.execute();
        // punchRule.setId(query.getReturnedRecord().getId());
        DaoHelper
                .publishDaoAction(DaoAction.CREATE, EhPunchWorkday.class, null);

    }

    @Override
    public List<PunchWorkday> listWorkdays(DateStatus dateStatus) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_WORKDAY);
        Condition condition = Tables.EH_PUNCH_WORKDAY.DATE_STATUS
                .equal(dateStatus.getCode());
        step.where(condition);
        List<PunchWorkday> result = step
                .orderBy(Tables.EH_PUNCH_WORKDAY.ID.desc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, PunchWorkday.class);
                });
        return result;
    }

    @Override
    public void createPunchExceptionRequest(
            PunchExceptionRequest punchExceptionRequest) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class,punchExceptionRequest.getEnterpriseId() ));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        long id = sequenceProvider
                .getNextSequence(NameMapper
                        .getSequenceDomainFromTablePojo(EhPunchExceptionRequests.class));
        punchExceptionRequest.setId(id);
        EhPunchExceptionRequestsRecord record = ConvertHelper.convert(
                punchExceptionRequest, EhPunchExceptionRequestsRecord.class);
        InsertQuery<EhPunchExceptionRequestsRecord> query = context
                .insertQuery(Tables.EH_PUNCH_EXCEPTION_REQUESTS);
        query.setRecord(record);
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.CREATE,
                EhPunchExceptionRequests.class, null);
    }

    @Override
    public List<PunchExceptionRequest> listExceptionRequestsByDate(Long userId,
                                                                   Long companyId, String logDay) {
        Date date = java.sql.Date.valueOf(logDay);
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_EXCEPTION_REQUESTS);
        Condition condition = Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE
                .equal(date);
        Condition condition2 = Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.equal(userId);
        Condition condition3 = Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.equal(companyId);
        condition = condition.and(condition2);
        condition = condition.and(condition3);
        step.where(condition);
        List<PunchExceptionRequest> result = step
                .orderBy(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ID.desc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, PunchExceptionRequest.class);
                });
        return result;
    }

    @Override
    public PunchExceptionApproval getPunchExceptionApprovalByDate(Long userId,
                                                                  Long companyId, String logDay) {
        Date date = java.sql.Date.valueOf(logDay);
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_EXCEPTION_APPROVALS);
        Condition condition = Tables.EH_PUNCH_EXCEPTION_APPROVALS.PUNCH_DATE
                .equal(date);
        Condition condition2 = Tables.EH_PUNCH_EXCEPTION_APPROVALS.USER_ID.equal(userId);
        Condition condition3 = Tables.EH_PUNCH_EXCEPTION_APPROVALS.ENTERPRISE_ID.equal(companyId);
        condition = condition.and(condition2);
        condition = condition.and(condition3);
        step.where(condition);
        List<PunchExceptionApproval> result = step
                .orderBy(Tables.EH_PUNCH_EXCEPTION_APPROVALS.ID.desc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, PunchExceptionApproval.class);
                });

        if (null != result && result.size() > 0)
            return result.get(0);
        return null;
    }

    @Override
    public Integer countExceptionRequests(Long userId, List<Long> userIds, Long companyId, String startDay, String endDay, Byte status,
                                          Byte processCode, Byte requestType) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectJoinStep<Record1<Integer>> step = context.selectCount().from(Tables.EH_PUNCH_EXCEPTION_REQUESTS);
//		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
//		step.join(Tables.).on(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));

        Condition condition = (Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.equal(companyId));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.eq(userId));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.REQUEST_TYPE.eq(requestType));
        if (userIds != null)
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.in(userIds));
        if (!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
            Date startDate = Date.valueOf(startDay);
            Date endDate = Date.valueOf(endDay);
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.between(startDate).and(endDate));
        }
        if (null != status && status != 0) {
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.eq(status));
        }
        if (null != processCode && processCode != 0) {
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.APPROVAL_STATUS.eq(processCode));
        }
        return step.where(condition).fetchOneInto(Integer.class);

    }

    @Override
    public List<PunchExceptionRequest> listExceptionRequests(Long userId, List<Long> userIds, Long companyId, String startDay, String endDay,
                                                             Byte status, Byte processCode, Integer pageOffset, Integer pageSize, Byte requestType) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select(Tables.EH_PUNCH_EXCEPTION_REQUESTS.fields()).from(Tables.EH_PUNCH_EXCEPTION_REQUESTS);
//		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
//		step.join(Tables.EH_GROUP_CONTACTS).on(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));

        Condition condition = (Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.equal(companyId));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.eq(userId));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.REQUEST_TYPE.eq(requestType));

        if (userIds != null)
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.in(userIds));
        if (!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
            Date startDate = Date.valueOf(startDay);
            Date endDate = Date.valueOf(endDay);
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.between(startDate).and(endDate));
        }
        if (null != status && status != 0) {
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.eq(status));
        }
        if (null != processCode && processCode != 0) {
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.APPROVAL_STATUS.eq(processCode));
        }

        Integer offset = pageOffset == null ? 1 : (pageOffset - 1) * pageSize;
        step.limit(offset, pageSize);
        List<EhPunchExceptionRequestsRecord> resultRecord = step.where(condition)
                .orderBy(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ID.desc()).fetch()
                .map(new EhPunchExceptionRequestMapper());

        List<PunchExceptionRequest> result = resultRecord.stream().map((r) -> {
            return ConvertHelper.convert(r, PunchExceptionRequest.class);
        }).collect(Collectors.toList());
        return result;

    }

    @Override
    public int countExceptionRequests(List<Long> userIds, Long companyId,
                                      String startDay, String endDay, Byte status,
                                      Byte processCode, Byte requestType) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectJoinStep<Record1<Integer>> step = context.selectCount().from(Tables.EH_PUNCH_EXCEPTION_REQUESTS);
//		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
//		step.join(Tables.EH_GROUP_CONTACTS).on(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));

        Condition condition = (Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.equal(companyId));
//		condition = condition.and(Tables.EH_GROUP_CONTACTS.OWNER_TYPE.eq(OwnerType.COMPANY.getCode()).and(Tables.EH_GROUP_CONTACTS.OWNER_ID.eq(companyId)));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.REQUEST_TYPE.eq(requestType));
        if (null != userIds) {
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.in(userIds));
        }

        if (!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
            Date startDate = Date.valueOf(startDay);
            Date endDate = Date.valueOf(endDay);
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.between(startDate).and(endDate));
        }
        if (null != status && status != 0) {
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.eq(status));
        }
        if (null != processCode && processCode != 0) {
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.APPROVAL_STATUS.eq(processCode));
        }
        return step.where(condition).fetchOneInto(Integer.class);

    }


    @Override
    public void updatePunchExceptionRequest(PunchExceptionRequest punchExceptionRequest) {
        assert (punchExceptionRequest.getId() == null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchExceptionRequestsDao dao = new EhPunchExceptionRequestsDao(
                context.configuration());
        dao.update(punchExceptionRequest);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchExceptionRequests.class,
                punchExceptionRequest.getId());
    }

    @Override
    public List<PunchExceptionRequest> listExceptionRequests(List<Long> userIds,
                                                             Long companyId, String startDay, String endDay, Byte status,
                                                             Byte processCode, Integer pageOffset, int pageSize, Byte requestType) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select(Tables.EH_PUNCH_EXCEPTION_REQUESTS.fields()).from(Tables.EH_PUNCH_EXCEPTION_REQUESTS);
//		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
//		step.join(Tables.EH_GROUP_CONTACTS).on(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));

        Condition condition = (Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.equal(companyId));
//		condition = condition.and(Tables.EH_GROUP_CONTACTS.OWNER_TYPE.eq(OwnerType.COMPANY.getCode()).and(Tables.EH_GROUP_CONTACTS.OWNER_ID.eq(companyId)));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.REQUEST_TYPE.eq(requestType));

        if (userIds != null)
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.in(userIds));
        if (!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
            Date startDate = Date.valueOf(startDay);
            Date endDate = Date.valueOf(endDay);
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.between(startDate).and(endDate));
        }
        if (null != status && status != 0) {
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.eq(status));
        }
        if (null != processCode && processCode != 0) {
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.APPROVAL_STATUS.eq(processCode));
        }
        Integer offset = pageOffset == null ? 1 : (pageOffset - 1) * pageSize;
        step.limit(offset, pageSize);
        List<EhPunchExceptionRequestsRecord> resultRecord = step.where(condition)
                .orderBy(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ID.desc()).fetch()
                .map(new EhPunchExceptionRequestMapper());

        List<PunchExceptionRequest> result = resultRecord.stream().map((r) -> {
            return ConvertHelper.convert(r, PunchExceptionRequest.class);
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public PunchExceptionApproval getExceptionApproval(Long userId, Long companyId,
                                                       Date punchDate) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_EXCEPTION_APPROVALS);
        Condition condition = (Tables.EH_PUNCH_EXCEPTION_APPROVALS.ENTERPRISE_ID.equal(companyId));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_APPROVALS.USER_ID.eq(userId));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_APPROVALS.PUNCH_DATE.eq(punchDate));
        step.where(condition);
        List<PunchExceptionApproval> result = step.orderBy(Tables.EH_PUNCH_EXCEPTION_APPROVALS.ID.desc())
                .fetch().map((r) -> {
                    return ConvertHelper.convert(r, PunchExceptionApproval.class);
                });
        if (null != result && result.size() > 0)
            return result.get(0);
        return null;
    }

    @Override
    public void createPunchExceptionApproval(
            PunchExceptionApproval punchExceptionApproval) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class,punchExceptionApproval.getEnterpriseId() ));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        long id = sequenceProvider
                .getNextSequence(NameMapper
                        .getSequenceDomainFromTablePojo(EhPunchExceptionApprovals.class));
        punchExceptionApproval.setId(id);
        EhPunchExceptionApprovalsRecord record = ConvertHelper.convert(
                punchExceptionApproval, EhPunchExceptionApprovalsRecord.class);
        InsertQuery<EhPunchExceptionApprovalsRecord> query = context
                .insertQuery(Tables.EH_PUNCH_EXCEPTION_APPROVALS);
        query.setRecord(record);
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.CREATE,
                EhPunchExceptionApprovals.class, null);

    }

    @Override
    public void updatePunchExceptionApproval(PunchExceptionApproval punchExceptionApproval) {
        assert (punchExceptionApproval.getId() == null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchExceptionApprovalsDao dao = new EhPunchExceptionApprovalsDao(
                context.configuration());
        dao.update(punchExceptionApproval);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchExceptionApprovals.class,
                punchExceptionApproval.getId());
    }

    @Override
    public void deletePunchExceptionApproval(Long id) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchExceptionApprovalsDao dao = new EhPunchExceptionApprovalsDao(context.configuration());
        dao.deleteById(id);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchExceptionApprovals.class, id);
    }

    @Override
    public PunchDayLog getDayPunchLogByDateAndDetailId(Long detailId, Long companyId, String punchDate) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_PUNCH_DAY_LOGS);
        Condition condition = (Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.equal(companyId));
        condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.eq(Date.valueOf(punchDate)));
        condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.DETAIL_ID.eq(detailId));

        step.where(condition);
        Record record = step.limit(1).fetchOne();
        if (record != null) {
            return ConvertHelper.convert(record, PunchDayLog.class);
        }
        return null;
    }

    @Override
    public int deletePunchDayLogByDateAndDetailId(Long detailId, Long companyId, String punchDate) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhPunchDayLogs.class));
        DeleteQuery<EhPunchDayLogsRecord> deleteQuery = context.deleteQuery(Tables.EH_PUNCH_DAY_LOGS);
        deleteQuery.addConditions(Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.equal(companyId));
        deleteQuery.addConditions(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.eq(Date.valueOf(punchDate)));
        deleteQuery.addConditions(Tables.EH_PUNCH_DAY_LOGS.DETAIL_ID.eq(detailId));
        return deleteQuery.execute();
    }

    @Override
    public PunchDayLog getDayPunchLogByDateAndUserId(Long userId, Long companyId, String punchDate) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_PUNCH_DAY_LOGS);
        Condition condition = (Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.equal(companyId));
        condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.eq(Date.valueOf(punchDate)));
        condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.USER_ID.eq(userId));
        step.where(condition);
        Record record = step.limit(1).fetchOne();
        if (record != null) {
            return ConvertHelper.convert(record, PunchDayLog.class);
        }
        return null;
    }

    @Override
    public void createPunchDayLog(PunchDayLog punchDayLog) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class,punchDayLog.getEnterpriseId() ));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        punchDayLog.setViewFlag(ViewFlags.NOTVIEW.getCode());
//long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPunchDayLogs.class));
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchDayLogs.class);
        long id = sequenceProvider.getNextSequence(key);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("line 645 : Create punch day log, key=" + key + ", newId=" + id + ", enterpriseId=" + punchDayLog.getEnterpriseId());
        }
        punchDayLog.setId(id);
        EhPunchDayLogsRecord record = ConvertHelper.convert(punchDayLog, EhPunchDayLogsRecord.class);
        InsertQuery<EhPunchDayLogsRecord> query = context.insertQuery(Tables.EH_PUNCH_DAY_LOGS);
        query.setRecord(record);
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPunchDayLogs.class, null);
    }

    @Override
    public void updatePunchDayLog(PunchDayLog punchDayLog) {
        assert (punchDayLog.getId() == null);

        punchDayLog.setViewFlag(ViewFlags.NOTVIEW.getCode());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchDayLogsDao dao = new EhPunchDayLogsDao(
                context.configuration());
        dao.update(punchDayLog);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchDayLogs.class,
                punchDayLog.getId());

    }


    @Override
    public int countPunchDayLogs(List<Long> userIds, Long companyId,
                                 String startDay, String endDay, Byte startTimeCompareFlag,
                                 String startTime, Byte endTimeCompareFlag, String endTime,
                                 Byte workTimeCompareFlag, String workTime, Byte status) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectJoinStep<Record1<Integer>> step = context.selectCount().from(Tables.EH_PUNCH_DAY_LOGS);
//		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
//		step.join(Tables.EH_GROUP_CONTACTS).on(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_DAY_LOGS.USER_ID));

        Condition condition = (Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.equal(companyId));
//		condition = condition.and(Tables.EH_GROUP_CONTACTS.OWNER_TYPE.eq(OwnerType.COMPANY.getCode()).and(Tables.EH_GROUP_CONTACTS.OWNER_ID.eq(companyId)));
        if (userIds != null)
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.USER_ID.in(userIds));
        if (!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
            Date startDate = Date.valueOf(startDay);
            Date endDate = Date.valueOf(endDay);
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.between(startDate).and(endDate));
        }
        if (null != startTimeCompareFlag && !StringUtils.isEmpty(startTime)) {
            Time arriveTime = Time.valueOf(startTime);
            if (startTimeCompareFlag.equals(0)) {
                condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME.lessOrEqual(arriveTime));
            } else {
                condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME.greaterOrEqual(arriveTime));
            }
        }
        if (null != endTimeCompareFlag && !StringUtils.isEmpty(endTime)) {
            Time leaveTime = Time.valueOf(endTime);
            if (endTimeCompareFlag.equals(0)) {
                condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME.lessOrEqual(leaveTime));
            } else {
                condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME.greaterOrEqual(leaveTime));
            }
        }
        if (null != workTimeCompareFlag && !StringUtils.isEmpty(workTime)) {
            Time sqlWorkTime = Time.valueOf(workTime);
            if (workTimeCompareFlag.equals(0)) {
                condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME.lessOrEqual(sqlWorkTime));
            } else {
                condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME.greaterOrEqual(sqlWorkTime));
            }
        }
        if (null != status && status != 0) {
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.STATUS.eq(status));
        }
        return step.where(condition).fetchOneInto(Integer.class);
    }

    @Override
    public List<PunchDayLog> listPunchDayLogs(List<Long> userIds, Long companyId,
                                              String startDay, String endDay, Byte status,
                                              Byte arriveTimeCompareFlag, String arriveTime,
                                              Byte leaveTimeCompareFlag, String leaveTime,
                                              Byte workTimeCompareFlag, String workTime, Integer pageOffset,
                                              Integer pageSize) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_PUNCH_DAY_LOGS);
//		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
//		step.join(Tables.EH_GROUP_CONTACTS).on(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_DAY_LOGS.USER_ID));

        Condition condition = (Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.equal(companyId));
//		condition = condition.and(Tables.EH_GROUP_CONTACTS.OWNER_TYPE.eq(OwnerType.COMPANY.getCode()).and(Tables.EH_GROUP_CONTACTS.OWNER_ID.eq(companyId)));
        if (userIds != null)
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.USER_ID.in(userIds));
        if (!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
            Date startDate = Date.valueOf(startDay);
            Date endDate = Date.valueOf(endDay);
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.between(startDate).and(endDate));
        }
        if (null != arriveTimeCompareFlag && !StringUtils.isEmpty(arriveTime)) {
            Time sqlArriveTime = Time.valueOf(arriveTime);
            if (arriveTimeCompareFlag.equals(TimeCompareFlag.LESSOREQUAL.getCode())) {
                condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME.lessOrEqual(sqlArriveTime));
            } else {
                condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME.greaterOrEqual(sqlArriveTime));
            }
        }
        if (null != leaveTimeCompareFlag && !StringUtils.isEmpty(leaveTime)) {
            Time sqlLeaveTime = Time.valueOf(leaveTime);
            if (leaveTimeCompareFlag.equals(TimeCompareFlag.LESSOREQUAL.getCode())) {
                condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.LEAVE_TIME.lessOrEqual(sqlLeaveTime));
            } else {
                condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.LEAVE_TIME.greaterOrEqual(sqlLeaveTime));
            }
        }
        if (null != workTimeCompareFlag && !StringUtils.isEmpty(workTime)) {
            Time sqlWorkTime = Time.valueOf(workTime);
            if (workTimeCompareFlag.equals(TimeCompareFlag.LESSOREQUAL.getCode())) {
                condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.WORK_TIME.lessOrEqual(sqlWorkTime));
            } else {
                condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.WORK_TIME.greaterOrEqual(sqlWorkTime));
            }
        }
        if (null != status && status != 0) {
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.STATUS.eq(status));
        }
        Integer offset = pageOffset == null ? 0 : (pageOffset - 1) * pageSize;
        step.limit(offset, pageSize);
//		List<EhPunchDayLogsRecord> resultRecord = step.where(condition)
//				.orderBy(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.desc(),Tables.EH_PUNCH_DAY_LOGS.USER_ID.desc()).fetch()
//				.map(new EhPunchDayLogMapper());
//		
//		List<PunchDayLog> result = resultRecord.stream().map((r) -> {
//            return ConvertHelper.convert(r, PunchDayLog.class);
//        }).collect(Collectors.toList());
        List<PunchDayLog> result = new ArrayList<PunchDayLog>();
        step.where(condition)
                .orderBy(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.desc(), Tables.EH_PUNCH_DAY_LOGS.USER_ID.desc()).fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, PunchDayLog.class));
            return null;
        });
        return result;
    }

    @Override
    public List<PunchDayLog> listPunchDayExceptionLogs(Long userId,
                                                       Long companyId, String startDay, String endDay) {
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select(Tables.EH_PUNCH_DAY_LOGS.fields()).from(Tables.EH_PUNCH_DAY_LOGS);
//		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
//		step.join(Tables.EH_GROUP_CONTACTS).on(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_DAY_LOGS.USER_ID));
//	 
        Condition condition = (Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.equal(companyId));
        condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.USER_ID.equal(userId));
//		condition = condition.and(Tables.EH_GROUP_CONTACTS.OWNER_TYPE.eq(OwnerType.COMPANY.getCode()).and(Tables.EH_GROUP_CONTACTS.OWNER_ID.eq(companyId)));

        if (!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
            Date startDate = Date.valueOf(startDay);
            Date endDate = Date.valueOf(endDay);
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.between(startDate).and(endDate));
        }
        //不等于正常状态
        List<Byte> ExceptionStatus = new ArrayList<Byte>();
        ExceptionStatus.add(PunchStatus.BELATE.getCode());
        ExceptionStatus.add(PunchStatus.LEAVEEARLY.getCode());
        ExceptionStatus.add(PunchStatus.BLANDLE.getCode());
        ExceptionStatus.add(PunchStatus.UNPUNCH.getCode());
        condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.STATUS.in(ExceptionStatus).or(Tables.EH_PUNCH_DAY_LOGS.MORNING_STATUS.in(ExceptionStatus))
                .or(Tables.EH_PUNCH_DAY_LOGS.AFTERNOON_STATUS.in(ExceptionStatus)));
        condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.VIEW_FLAG.eq(ViewFlags.NOTVIEW.getCode()));

        List<EhPunchDayLogsRecord> resultRecord = step.where(condition)
                .orderBy(Tables.EH_PUNCH_DAY_LOGS.ID.desc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, EhPunchDayLogsRecord.class);
                });

        List<PunchDayLog> result = resultRecord.stream().map((r) -> {
            return ConvertHelper.convert(r, PunchDayLog.class);
        }).collect(Collectors.toList());
        return result;
    }
    @Override
    public List<Long> listPunchLogUserId(Long enterpriseId, String startDay, String endDay){
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record1<Long>> step = context.selectDistinct(Tables.EH_PUNCH_LOGS.USER_ID).from(Tables.EH_PUNCH_LOGS); 
        Condition condition = Tables.EH_PUNCH_LOGS.ENTERPRISE_ID.eq(enterpriseId); 
        if (!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
            Date startDate = Date.valueOf(startDay);
            Date endDate = Date.valueOf(endDay);
            condition = condition.and(Tables.EH_PUNCH_LOGS.PUNCH_DATE.between(startDate).and(endDate));
        }
        // modify by wh 2017-4-25 order by punch date asc
        List<Long> resultRecord = step.where(condition)
                .orderBy(Tables.EH_PUNCH_LOGS.PUNCH_DATE.asc()).fetch()
                .map((r) -> {
                    return r.value1();
                });
        return resultRecord;
    }

    @Override
    public Integer countpunchStatistic(String punchMonth, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record1<Integer>> step = context.select(Tables.EH_PUNCH_STATISTICS.ID.count()).from(Tables.EH_PUNCH_STATISTICS)
                .where(Tables.EH_PUNCH_STATISTICS.OWNER_ID.eq(ownerId))
                .and(Tables.EH_PUNCH_STATISTICS.PUNCH_MONTH.eq(punchMonth));
        return step.fetchAny().value1();
    }

    @Override
	public List<Long> listPunchLogEnterprise(String startDay, String endDay){
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record1<Long>> step = context.selectDistinct(Tables.EH_PUNCH_LOGS.ENTERPRISE_ID).from(Tables.EH_PUNCH_LOGS); 
        Condition condition = Tables.EH_PUNCH_LOGS.USER_ID.isNotNull(); 
        if (!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
            Date startDate = Date.valueOf(startDay);
            Date endDate = Date.valueOf(endDay);
            condition = condition.and(Tables.EH_PUNCH_LOGS.PUNCH_DATE.between(startDate).and(endDate));
        }
        // modify by wh 2017-4-25 order by punch date asc
        List<Long> resultRecord = step.where(condition)
                .orderBy(Tables.EH_PUNCH_LOGS.PUNCH_DATE.asc()).fetch()
                .map((r) -> {
                    return r.value1();
                });
        return resultRecord;
	}


	@Override
	public List<PunchLog> listPunchLogs(List<Long> userIds, Long ownerId, String startDay,
			String endDay, Byte exceptionStatus, Integer pageOffset, Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_PUNCH_LOGS); 
        Condition condition = Tables.EH_PUNCH_LOGS.PUNCH_STATUS.equal(ClockCode.SUCESS.getCode());
    	condition = condition.and(Tables.EH_PUNCH_LOGS.PUNCH_TIME.isNotNull());
        if(null != ownerId){
        	condition = condition.and(Tables.EH_PUNCH_LOGS.ENTERPRISE_ID.equal(ownerId));
        }
        if(null != userIds){
        	condition = condition.and(Tables.EH_PUNCH_LOGS.USER_ID.in(userIds));
        } 
        if (!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
            Date startDate = Date.valueOf(startDay);
            Date endDate = Date.valueOf(endDay);
            condition = condition.and(Tables.EH_PUNCH_LOGS.PUNCH_DATE.between(startDate).and(endDate));
        }
        if(null!=exceptionStatus){
        	if(NormalFlag.fromCode(exceptionStatus) == NormalFlag.NEED){ 
            	condition = condition.and(Tables.EH_PUNCH_LOGS.STATUS.eq(PunchStatus.NORMAL.getCode()));
        	}else { 
            	condition = condition.and(Tables.EH_PUNCH_LOGS.STATUS.ne(PunchStatus.NORMAL.getCode()));
        	}
        }

        if (null != pageOffset && null != pageSize){
            step.limit(pageOffset, pageSize);
        }
        // modify by wh 2017-4-25 order by punch date asc
        List<EhPunchLogsRecord> resultRecord = step.where(condition).orderBy(Tables.EH_PUNCH_LOGS.PUNCH_DATE.desc(), Tables.EH_PUNCH_LOGS.PUNCH_ORGANIZATION_ID.asc(),
                Tables.EH_PUNCH_LOGS.DETAIL_ID.asc(), Tables.EH_PUNCH_LOGS.PUNCH_TIME.asc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, EhPunchLogsRecord.class);
                });

        List<PunchLog> result = resultRecord.stream().map((r) -> {
            return ConvertHelper.convert(r, PunchLog.class);
        }).collect(Collectors.toList());
        return result;
	}
    @Override
    public List<PunchLog> listPunchLogs(Long userId,
                                              Long companyId, String startDay, String endDay) { 
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_PUNCH_LOGS); 
        Condition condition = Tables.EH_PUNCH_LOGS.USER_ID.isNotNull();
        if(null != companyId){
        	condition = condition.and(Tables.EH_PUNCH_LOGS.ENTERPRISE_ID.equal(companyId));
        }
        if(null != userId){
        	condition = condition.and(Tables.EH_PUNCH_LOGS.USER_ID.equal(userId));
        } 
        if (!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
            Date startDate = Date.valueOf(startDay);
            Date endDate = Date.valueOf(endDay);
            condition = condition.and(Tables.EH_PUNCH_LOGS.PUNCH_DATE.between(startDate).and(endDate));
        }
        // modify by wh 2017-4-25 order by punch date asc
        List<EhPunchLogsRecord> resultRecord = step.where(condition)
                .orderBy(Tables.EH_PUNCH_LOGS.PUNCH_DATE.asc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, EhPunchLogsRecord.class);
                });

        List<PunchLog> result = resultRecord.stream().map((r) -> {
            return ConvertHelper.convert(r, PunchLog.class);
        }).collect(Collectors.toList());
        return result;
    }
    @Override
    public List<PunchDayLog> listPunchDayLogs(Long userId,
                                              Long companyId, String startDay, String endDay) { 
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_PUNCH_DAY_LOGS); 
        Condition condition = Tables.EH_PUNCH_DAY_LOGS.USER_ID.isNotNull();
        if(null != companyId){
        	condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.equal(companyId));
        }
        if(null != userId){
        	condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.USER_ID.equal(userId));
        } 
        if (!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
            Date startDate = Date.valueOf(startDay);
            Date endDate = Date.valueOf(endDay);
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.between(startDate).and(endDate));
        }
        // modify by wh 2017-4-25 order by punch date asc
        List<EhPunchDayLogsRecord> resultRecord = step.where(condition)
                .orderBy(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.asc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, EhPunchDayLogsRecord.class);
                });

        List<PunchDayLog> result = resultRecord.stream().map((r) -> {
            return ConvertHelper.convert(r, PunchDayLog.class);
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<PunchDayLog> listPunchDayLogsIncludeEndDay(Long detailId, Long companyId, String startDay, String endDay) {
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_PUNCH_DAY_LOGS);
        Condition condition = (Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.equal(companyId));
        condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.DETAIL_ID.equal(detailId));
        if (!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
            Date startDate = Date.valueOf(startDay);
            Date endDate = Date.valueOf(endDay);
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.greaterOrEqual(startDate));
            //modify by 2017-09-30 由于月刷新的endate是前一天,所以要包含enddate
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.lessOrEqual(endDate));
        }
        // modify by wh 2017-4-25 order by punch date asc
        List<EhPunchDayLogsRecord> resultRecord = step.where(condition)
                .orderBy(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.asc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, EhPunchDayLogsRecord.class);
                });

        List<PunchDayLog> result = resultRecord.stream().map((r) -> {
            return ConvertHelper.convert(r, PunchDayLog.class);
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<PunchExceptionRequest> listExceptionNotViewRequests(
            Long userId, Long companyId, String startDay, String endDay) {

        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select(Tables.EH_PUNCH_EXCEPTION_REQUESTS.fields()).from(Tables.EH_PUNCH_EXCEPTION_REQUESTS);
//		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
//		step.join(Tables.EH_GROUP_CONTACTS).on(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
        Condition condition = (Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.equal(companyId));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.eq(userId));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.REQUEST_TYPE.eq(PunchRquestType.APPROVAL.getCode()));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.VIEW_FLAG.eq(ViewFlags.NOTVIEW.getCode()));
        if (!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
            Date startDate = Date.valueOf(startDay);
            Date endDate = Date.valueOf(endDay);
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.between(startDate).and(endDate));
        }
        List<EhPunchExceptionRequestsRecord> resultRecord = step.where(condition)
                .orderBy(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ID.desc()).fetch()
                .map(new EhPunchExceptionRequestMapper());
        List<PunchExceptionRequest> result = resultRecord.stream().map((r) -> {
            return ConvertHelper.convert(r, PunchExceptionRequest.class);
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public void viewDateFlags(Long userId, Long companyId, String format) {
        Date logDate = Date.valueOf(format);
        //update 申请表
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(com.everhomes.server.schema.tables.EhPunchExceptionRequests.class,companyId));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.update(Tables.EH_PUNCH_EXCEPTION_REQUESTS).set(Tables.EH_PUNCH_EXCEPTION_REQUESTS.VIEW_FLAG, ViewFlags.ISVIEW.getCode())
                .where(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.eq(userId))
                .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.eq(companyId))
                .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.eq(logDate)).execute();

        //update 日志表
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(com.everhomes.server.schema.tables.EhPunchDayLogs.class,companyId));
        context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.update(Tables.EH_PUNCH_DAY_LOGS).set(Tables.EH_PUNCH_DAY_LOGS.VIEW_FLAG, ViewFlags.ISVIEW.getCode())
                .where(Tables.EH_PUNCH_DAY_LOGS.USER_ID.eq(userId))
                .and(Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.eq(companyId))
                .and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.eq(logDate)).execute();

        //update 审批表
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(com.everhomes.server.schema.tables.EhPunchExceptionApprovals.class,companyId));
        context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.update(Tables.EH_PUNCH_EXCEPTION_APPROVALS).set(Tables.EH_PUNCH_EXCEPTION_APPROVALS.VIEW_FLAG, ViewFlags.ISVIEW.getCode())
                .where(Tables.EH_PUNCH_EXCEPTION_APPROVALS.USER_ID.eq(userId))
                .and(Tables.EH_PUNCH_EXCEPTION_APPROVALS.ENTERPRISE_ID.eq(companyId))
                .and(Tables.EH_PUNCH_EXCEPTION_APPROVALS.PUNCH_DATE.eq(logDate)).execute();
    }

    @Override
    public List<UserPunchStatusCount> listUserStatusPunch(Long companyId, String startDay,
                                                          String endDay) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        Condition condition = Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.eq(companyId);
        if (!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
            Date startDate = Date.valueOf(startDay);
            Date endDate = Date.valueOf(endDay);
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.between(startDate).and(endDate));
        }


        SelectHavingStep<Record3<Long, Byte, Integer>> step = context.select(Tables.EH_PUNCH_DAY_LOGS.USER_ID, Tables.EH_PUNCH_DAY_LOGS.STATUS,
                Tables.EH_PUNCH_DAY_LOGS.ID.count())
                .from(Tables.EH_PUNCH_DAY_LOGS)
                .where(condition)
                .groupBy(Tables.EH_PUNCH_DAY_LOGS.STATUS, Tables.EH_PUNCH_DAY_LOGS.USER_ID);

        List<UserPunchStatusCount> result = new ArrayList<UserPunchStatusCount>();
        step.orderBy(Tables.EH_PUNCH_DAY_LOGS.ID.desc())
                .fetch().map((r) -> {
            UserPunchStatusCount userPunchStatusCount = new UserPunchStatusCount();
            userPunchStatusCount.setUserId(r.value1());
            userPunchStatusCount.setStatus(r.value2());
            userPunchStatusCount.setCount(r.value3());
            result.add(userPunchStatusCount);
            return null;
        });
        return result;

    }

    @Override
    public List<UserPunchStatusCount> listUserApprovalStatusPunch(Long companyId, String startDay,
                                                                  String endDay) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        Condition condition = Tables.EH_PUNCH_EXCEPTION_APPROVALS.ENTERPRISE_ID.eq(companyId);
        if (!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
            Date startDate = Date.valueOf(startDay);
            Date endDate = Date.valueOf(endDay);
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_APPROVALS.PUNCH_DATE.between(startDate).and(endDate));
        }


        SelectHavingStep<Record3<Long, Byte, Integer>> step = context.select(Tables.EH_PUNCH_EXCEPTION_APPROVALS.USER_ID, Tables.EH_PUNCH_EXCEPTION_APPROVALS.APPROVAL_STATUS,
                Tables.EH_PUNCH_EXCEPTION_APPROVALS.ID.count())
                .from(Tables.EH_PUNCH_EXCEPTION_APPROVALS)
                .where(condition)
                .groupBy(Tables.EH_PUNCH_EXCEPTION_APPROVALS.APPROVAL_STATUS, Tables.EH_PUNCH_EXCEPTION_APPROVALS.USER_ID);

        List<UserPunchStatusCount> result = new ArrayList<UserPunchStatusCount>();
        step.orderBy(Tables.EH_PUNCH_EXCEPTION_APPROVALS.ID.desc())
                .fetch().map((r) -> {
            UserPunchStatusCount userPunchStatusCount = new UserPunchStatusCount();
            userPunchStatusCount.setUserId(r.value1());
            userPunchStatusCount.setStatus(r.value2());
            userPunchStatusCount.setCount(r.value3());
            result.add(userPunchStatusCount);
            return null;
        });
        return result;

    }

    @Override
    public List<PunchDayLogDTO> listPunchDayLogs(Long companyId, String startDay, String endDay) {
        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record5<Long, Date, Byte, Byte, Time>> step = context.select(Tables.EH_PUNCH_DAY_LOGS.USER_ID, Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE, Tables.EH_PUNCH_DAY_LOGS.STATUS, Tables.EH_PUNCH_EXCEPTION_APPROVALS.APPROVAL_STATUS, Tables.EH_PUNCH_DAY_LOGS.WORK_TIME).from(Tables.EH_PUNCH_DAY_LOGS);
//		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
        step.leftOuterJoin(Tables.EH_PUNCH_EXCEPTION_APPROVALS).on(Tables.EH_PUNCH_EXCEPTION_APPROVALS.USER_ID.eq(Tables.EH_PUNCH_DAY_LOGS.USER_ID))
                .and(Tables.EH_PUNCH_EXCEPTION_APPROVALS.ENTERPRISE_ID.eq(Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID)).and(Tables.EH_PUNCH_EXCEPTION_APPROVALS.PUNCH_DATE.eq(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE));

        Condition condition = (Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.equal(companyId));
        if (!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
            Date startDate = Date.valueOf(startDay);
            Date endDate = Date.valueOf(endDay);
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.between(startDate).and(endDate));
        }
        List<PunchDayLogDTO> result = new ArrayList<PunchDayLogDTO>();
        step.where(condition).orderBy(Tables.EH_PUNCH_DAY_LOGS.ID.desc())
                .fetch().map((r) -> {
            PunchDayLogDTO dto = new PunchDayLogDTO();
            dto.setUserId(r.value1());
            dto.setPunchTime(r.value2());
            dto.setStatus(r.value3());
            dto.setApprovalStatus((r.value4()));
            dto.setWorkTime((r.value5().getTime()));

            result.add(dto);
            return null;
        });
        return result;
    }

    @Override
    public List<PunchRule> findPunchRules(String ownerType, Long ownerId, Long timeRuleId, Long locationRuleId, Long wifiRuleId,
                                          Long workdayRuleId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhPunchRulesRecord> query = context
                .selectQuery(Tables.EH_PUNCH_RULES);

        Condition condition = Tables.EH_PUNCH_RULES.ID.ne(-1L);
        if (null != ownerType)
            condition = condition.and(Tables.EH_PUNCH_RULES.OWNER_TYPE.eq(ownerType));
        if (null != ownerId)
            condition = condition.and(Tables.EH_PUNCH_RULES.OWNER_ID.eq(ownerId));
        if (null != timeRuleId)
            condition = condition.and(Tables.EH_PUNCH_RULES.TIME_RULE_ID.eq(timeRuleId));
        if (null != locationRuleId)
            condition = condition.and(Tables.EH_PUNCH_RULES.LOCATION_RULE_ID.eq(locationRuleId));
        if (null != wifiRuleId)
            condition = condition.and(Tables.EH_PUNCH_RULES.WIFI_RULE_ID.eq(wifiRuleId));
        if (null != workdayRuleId)
            condition = condition.and(Tables.EH_PUNCH_RULES.WORKDAY_RULE_ID.eq(workdayRuleId));
        query.addConditions(condition);
        List<PunchRule> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, PunchRule.class));
            return null;
        });
        if (null != result && result.size() > 0)
            return result;
        return null;
    }

    @Override
    public List<PunchGeopoint> listPunchGeopointsByRuleId(String ownerType, Long ownerId, Long ruleId) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_GEOPOINTS);
        Condition condition = Tables.EH_PUNCH_GEOPOINTS.LOCATION_RULE_ID.equal(ruleId);
//				.and( Tables.EH_PUNCH_GEOPOINTS.OWNER_ID.eq(ownerId))
//				.and( Tables.EH_PUNCH_GEOPOINTS.OWNER_TYPE.eq(ownerType));
        step.where(condition);
        List<PunchGeopoint> result = step
                .orderBy(Tables.EH_PUNCH_GEOPOINTS.ID.desc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, PunchGeopoint.class);
                });

        if (null != result && result.size() > 0)
            return result;
        return null;
    }

    @Override
    public List<PunchTimeRule> queryPunchTimeRuleList(String ownerType, Long ownerId, CrossShardListingLocator locator, int pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhPunchTimeRulesRecord> query = context
                .selectQuery(Tables.EH_PUNCH_TIME_RULES);

        Condition condition = Tables.EH_PUNCH_TIME_RULES.ID.ne(-1L);
        if (null != ownerType)
            condition = condition.and(Tables.EH_PUNCH_TIME_RULES.OWNER_TYPE.eq(ownerType));
        if (null != ownerId)
            condition = condition.and(Tables.EH_PUNCH_TIME_RULES.OWNER_ID.eq(ownerId));
        if (null != locator && locator != null && locator.getAnchor() != null)
            condition = condition.and(Tables.EH_PUNCH_TIME_RULES.ID.gt(locator.getAnchor()));
        query.addConditions(condition);
        query.addLimit(pageSize);
        query.addOrderBy(Tables.EH_PUNCH_TIME_RULES.ID.asc());
        List<PunchTimeRule> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, PunchTimeRule.class));
            return null;
        });
        if (null != result && result.size() > 0)
            return result;
        return null;
    }


    @Override
    public List<PunchTimeRule> queryPunchTimeRuleList(Long startTimeLong, Long endTimeLong) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhPunchTimeRulesRecord> query = context
                .selectQuery(Tables.EH_PUNCH_TIME_RULES);

        Condition condition = Tables.EH_PUNCH_TIME_RULES.ID.ne(-1L);
        condition = condition.and(Tables.EH_PUNCH_TIME_RULES.START_LATE_TIME_LONG.between(startTimeLong, endTimeLong));
        query.addConditions(condition);
        query.addOrderBy(Tables.EH_PUNCH_TIME_RULES.ID.asc());
        List<PunchTimeRule> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, PunchTimeRule.class));
            return null;
        });
        if (null != result && result.size() > 0)
            return result;
        return null;
    }

    @Override
    public List<PunchTimeRule> queryPunchTimeRuleList(String ownerType, Long ownerId, String targetType, Long targetId, CrossShardListingLocator locator, int pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhPunchTimeRulesRecord> query = context
                .selectQuery(Tables.EH_PUNCH_TIME_RULES);

        Condition condition = Tables.EH_PUNCH_TIME_RULES.ID.ne(-1L);
        if (null != ownerType)
            condition = condition.and(Tables.EH_PUNCH_TIME_RULES.OWNER_TYPE.eq(ownerType));
        if (null != ownerId)
            condition = condition.and(Tables.EH_PUNCH_TIME_RULES.OWNER_ID.eq(ownerId));
        if (null != targetType)
            condition = condition.and(Tables.EH_PUNCH_TIME_RULES.TARGET_TYPE.eq(targetType));
        if (null != targetId)
            condition = condition.and(Tables.EH_PUNCH_TIME_RULES.TARGET_ID.eq(targetId));
        if (null != locator && locator != null && locator.getAnchor() != null)
            condition = condition.and(Tables.EH_PUNCH_TIME_RULES.ID.gt(locator.getAnchor()));
        query.addConditions(condition);
        query.addLimit(pageSize);
        query.addOrderBy(Tables.EH_PUNCH_TIME_RULES.ID.asc());
        List<PunchTimeRule> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, PunchTimeRule.class));
            return null;
        });
        if (null != result && result.size() > 0)
            return result;
        return null;
    }

    @Override
    public Long createPunchLocationRule(PunchLocationRule obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchLocationRules.class);
        long id = sequenceProvider.getNextSequence(key);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        EhPunchLocationRulesDao dao = new EhPunchLocationRulesDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updatePunchLocationRule(PunchLocationRule obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchLocationRulesDao dao = new EhPunchLocationRulesDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deletePunchLocationRule(PunchLocationRule obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchLocationRulesDao dao = new EhPunchLocationRulesDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public PunchLocationRule getPunchLocationRuleById(Long id) {
        try {
            PunchLocationRule[] result = new PunchLocationRule[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

            result[0] = context.select().from(Tables.EH_PUNCH_LOCATION_RULES)
                    .where(Tables.EH_PUNCH_LOCATION_RULES.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, PunchLocationRule.class);
                    });

            return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<PunchLocationRule> queryPunchLocationRules(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhPunchLocationRulesRecord> query = context.selectQuery(Tables.EH_PUNCH_LOCATION_RULES);
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if (locator != null && locator != null && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_PUNCH_LOCATION_RULES.ID.gt(locator.getAnchor()));
        }

        query.addOrderBy(Tables.EH_PUNCH_LOCATION_RULES.ID.asc());
        query.addLimit(count);
        List<PunchLocationRule> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, PunchLocationRule.class);
        });

//        if(objs.size() >= count) {
//            locator.setAnchor(objs.get(objs.size() - 1).getId());
//        } else {
//            locator.setAnchor(null);
//        }
        if (null == objs || objs.isEmpty())
            return null;
        return objs;
    }

    @Override
    public Long createPunchWifi(PunchWifi obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchWifis.class);
        long id = sequenceProvider.getNextSequence(key);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhPunchWifisDao dao = new EhPunchWifisDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updatePunchWifi(PunchWifi obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchWifisDao dao = new EhPunchWifisDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deletePunchWifi(PunchWifi obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchWifisDao dao = new EhPunchWifisDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public PunchWifi getPunchWifiById(Long id) {
        try {
            PunchWifi[] result = new PunchWifi[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

            result[0] = context.select().from(Tables.EH_PUNCH_WIFIS)
                    .where(Tables.EH_PUNCH_WIFIS.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, PunchWifi.class);
                    });

            return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<PunchWifi> queryPunchWifis(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhPunchWifisRecord> query = context.selectQuery(Tables.EH_PUNCH_WIFIS);
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if (locator != null && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_PUNCH_WIFIS.ID.gt(locator.getAnchor()));
        }

        query.addOrderBy(Tables.EH_PUNCH_WIFIS.ID.asc());
        query.addLimit(count);
        List<PunchWifi> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, PunchWifi.class);
        });

//        if(objs.size() >= count) {
//            locator.setAnchor(objs.get(objs.size() - 1).getId());
//        } else {
//            locator.setAnchor(null);
//        }
        if (null == objs || objs.isEmpty())
            return null;
        return objs;
    }

    private void prepareObj(PunchWifi obj) {
    }

    @Override
    public Long createPunchWifiRule(PunchWifiRule obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchWifiRules.class);
        long id = sequenceProvider.getNextSequence(key);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhPunchWifiRulesDao dao = new EhPunchWifiRulesDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updatePunchWifiRule(PunchWifiRule obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchWifiRulesDao dao = new EhPunchWifiRulesDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deletePunchWifiRule(PunchWifiRule obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchWifiRulesDao dao = new EhPunchWifiRulesDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public void deletePunchWifiRuleByOwnerAndId(String ownerType, Long ownerId, Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        DeleteWhereStep<EhPunchWifiRulesRecord> step = context
                .delete(Tables.EH_PUNCH_WIFI_RULES);
        Condition condition = Tables.EH_PUNCH_WIFI_RULES.ID.equal(id)
                .and(Tables.EH_PUNCH_WIFI_RULES.OWNER_ID.equal(ownerId))
                .and(Tables.EH_PUNCH_WIFI_RULES.OWNER_TYPE.equal(ownerType));
        step.where(condition);
        step.execute();
    }

    @Override
    public PunchWifiRule getPunchWifiRuleById(Long id) {
        try {
            PunchWifiRule[] result = new PunchWifiRule[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

            result[0] = context.select().from(Tables.EH_PUNCH_WIFI_RULES)
                    .where(Tables.EH_PUNCH_WIFI_RULES.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, PunchWifiRule.class);
                    });

            return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<PunchWifiRule> queryPunchWifiRules(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhPunchWifiRulesRecord> query = context.selectQuery(Tables.EH_PUNCH_WIFI_RULES);
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if (locator != null && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_PUNCH_WIFI_RULES.ID.gt(locator.getAnchor()));
        }

        query.addOrderBy(Tables.EH_PUNCH_WIFI_RULES.ID.asc());
        query.addLimit(count);
        List<PunchWifiRule> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, PunchWifiRule.class);
        });

//        if(objs.size() >= count) {
//            locator.setAnchor(objs.get(objs.size() - 1).getId());
//        } else {
//            locator.setAnchor(null);
//        }
        if (null == objs || objs.isEmpty())
            return null;
        return objs;
    }

    private void prepareObj(PunchWifiRule obj) {
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindHolidayByDate", allEntries = true)})
    public Long createPunchHoliday(PunchHoliday obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchHolidays.class);
        long id = sequenceProvider.getNextSequence(key);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhPunchHolidaysDao dao = new EhPunchHolidaysDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindHolidayByDate", allEntries = true)})
    public void updatePunchHoliday(PunchHoliday obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchHolidaysDao dao = new EhPunchHolidaysDao(context.configuration());
        dao.update(obj);
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindHolidayByDate", allEntries = true)})
    public void deletePunchHoliday(PunchHoliday obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchHolidaysDao dao = new EhPunchHolidaysDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindHolidayByDate", allEntries = true)})
    public void deletePunchHolidayByOwnerAndId(String ownerType, Long ownerId, Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        DeleteWhereStep<EhPunchHolidaysRecord> step = context
                .delete(Tables.EH_PUNCH_HOLIDAYS);
        Condition condition = Tables.EH_PUNCH_HOLIDAYS.ID.equal(id)
                .and(Tables.EH_PUNCH_HOLIDAYS.OWNER_ID.equal(ownerId))
                .and(Tables.EH_PUNCH_HOLIDAYS.OWNER_TYPE.equal(ownerType));
        step.where(condition);
        step.execute();
    }

    @Override
    public PunchHoliday getPunchHolidayById(Long id) {
        try {
            PunchHoliday[] result = new PunchHoliday[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

            result[0] = context.select().from(Tables.EH_PUNCH_HOLIDAYS)
                    .where(Tables.EH_PUNCH_HOLIDAYS.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, PunchHoliday.class);
                    });

            return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<PunchHoliday> queryPunchHolidays(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhPunchHolidaysRecord> query = context.selectQuery(Tables.EH_PUNCH_HOLIDAYS);
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if (locator != null && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_PUNCH_HOLIDAYS.ID.gt(locator.getAnchor()));
        }

        query.addOrderBy(Tables.EH_PUNCH_HOLIDAYS.ID.asc());
        query.addLimit(count);
        List<PunchHoliday> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, PunchHoliday.class);
        });

//        if(objs.size() >= count) {
//            locator.setAnchor(objs.get(objs.size() - 1).getId());
//        } else {
//            locator.setAnchor(null);
//        }
        if (null == objs || objs.isEmpty())
            return null;
        return objs;
    }

    private void prepareObj(PunchHoliday obj) {
    }

    @Override
    public Long createPunchWorkdayRule(PunchWorkdayRule obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchWorkdayRules.class);
        long id = sequenceProvider.getNextSequence(key);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhPunchWorkdayRulesDao dao = new EhPunchWorkdayRulesDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updatePunchWorkdayRule(PunchWorkdayRule obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchWorkdayRulesDao dao = new EhPunchWorkdayRulesDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deletePunchWorkdayRule(PunchWorkdayRule obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchWorkdayRulesDao dao = new EhPunchWorkdayRulesDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public void deletePunchWorkdayRuleByOwnerAndId(String ownerType, Long ownerId, Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        DeleteWhereStep<EhPunchWorkdayRulesRecord> step = context
                .delete(Tables.EH_PUNCH_WORKDAY_RULES);
        Condition condition = Tables.EH_PUNCH_WORKDAY_RULES.ID.equal(id)
                .and(Tables.EH_PUNCH_WORKDAY_RULES.OWNER_ID.equal(ownerId))
                .and(Tables.EH_PUNCH_WORKDAY_RULES.OWNER_TYPE.equal(ownerType));
        step.where(condition);
        step.execute();
    }

    @Override
    public PunchWorkdayRule getPunchWorkdayRuleById(Long id) {
        try {
            PunchWorkdayRule[] result = new PunchWorkdayRule[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

            result[0] = context.select().from(Tables.EH_PUNCH_WORKDAY_RULES)
                    .where(Tables.EH_PUNCH_WORKDAY_RULES.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, PunchWorkdayRule.class);
                    });

            return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<PunchWorkdayRule> queryPunchWorkdayRules(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhPunchWorkdayRulesRecord> query = context.selectQuery(Tables.EH_PUNCH_WORKDAY_RULES);
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if (locator != null && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_PUNCH_WORKDAY_RULES.ID.gt(locator.getAnchor()));
        }

        query.addOrderBy(Tables.EH_PUNCH_WORKDAY_RULES.ID.asc());
        query.addLimit(count);
        List<PunchWorkdayRule> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, PunchWorkdayRule.class);
        });

//        if(objs.size() >= count) {
//            locator.setAnchor(objs.get(objs.size() - 1).getId());
//        } else {
//            locator.setAnchor(null);
//        }
        if (null == objs || objs.isEmpty())
            return null;
        return objs;
    }

    private void prepareObj(PunchWorkdayRule obj) {
    }

    @Override
    public Long createPunchRuleOwnerMap(PunchRuleOwnerMap obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchRuleOwnerMap.class);
        long id = sequenceProvider.getNextSequence(key);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhPunchRuleOwnerMapDao dao = new EhPunchRuleOwnerMapDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updatePunchRuleOwnerMap(PunchRuleOwnerMap obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchRuleOwnerMapDao dao = new EhPunchRuleOwnerMapDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deletePunchRuleOwnerMap(PunchRuleOwnerMap obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchRuleOwnerMapDao dao = new EhPunchRuleOwnerMapDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public void deletePunchRuleOwnerMapByOwnerAndId(String ownerType, Long ownerId, Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        DeleteWhereStep<EhPunchRuleOwnerMapRecord> step = context
                .delete(Tables.EH_PUNCH_RULE_OWNER_MAP);
        Condition condition = Tables.EH_PUNCH_RULE_OWNER_MAP.ID.equal(id)
                .and(Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_ID.equal(ownerId))
                .and(Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_TYPE.equal(ownerType));
        step.where(condition);
        step.execute();
    }

    @Override
    public PunchRuleOwnerMap getPunchRuleOwnerMapByOwnerAndTarget(String ownerType, Long ownerId, String targetType, Long targetId) {
        try {
            //TODO
            PunchRuleOwnerMap[] result = new PunchRuleOwnerMap[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

            result[0] = context.select().from(Tables.EH_PUNCH_RULE_OWNER_MAP)
                    .where(Tables.EH_PUNCH_RULE_OWNER_MAP.TARGET_ID.eq(targetId))
                    .and(Tables.EH_PUNCH_RULE_OWNER_MAP.TARGET_TYPE.eq(targetType))

                    .and(Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_ID.eq(ownerId))
                    .and(Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_TYPE.eq(ownerType))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, PunchRuleOwnerMap.class);
                    });

            return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public PunchRuleOwnerMap getPunchRuleOwnerMapByTarget(String targetType, Long targetId) {
        try {
            PunchRuleOwnerMap[] result = new PunchRuleOwnerMap[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

            result[0] = context.select().from(Tables.EH_PUNCH_RULE_OWNER_MAP)
                    .where(Tables.EH_PUNCH_RULE_OWNER_MAP.TARGET_ID.eq(targetId))
                    .and(Tables.EH_PUNCH_RULE_OWNER_MAP.TARGET_TYPE.eq(targetType))
                    .orderBy(Tables.EH_PUNCH_RULE_OWNER_MAP.ID.desc())
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, PunchRuleOwnerMap.class);
                    });

            return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<PunchRuleOwnerMap> queryPunchRuleOwnerMaps(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhPunchRuleOwnerMapRecord> query = context.selectQuery(Tables.EH_PUNCH_RULE_OWNER_MAP);
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if (locator != null && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.ID.gt(locator.getAnchor()));
        }

        query.addOrderBy(Tables.EH_PUNCH_RULE_OWNER_MAP.ID.asc());
        query.addLimit(count);
        List<PunchRuleOwnerMap> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, PunchRuleOwnerMap.class);
        });

//        if(objs.size() >= count) {
//            locator.setAnchor(objs.get(objs.size() - 1).getId());
//        } else {
//            locator.setAnchor(null);
//        }
        if (null == objs || objs.isEmpty())
            return null;
        return objs;
    }

    @Override
    public List<PunchRuleOwnerMap> queryPunchRuleOwnerMaps(String ownerType, Long ownerId, String listType) {
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhPunchRuleOwnerMapRecord> query = context
                .selectQuery(Tables.EH_PUNCH_RULE_OWNER_MAP);
        query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_TYPE.eq(ownerType));
        if (ListTargetType.APPROVAL.getCode().equals(listType)) {
            query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.REVIEW_RULE_ID.isNotNull());
        }
        if (ListTargetType.PUNCH.getCode().equals(listType)) {
            query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.PUNCH_RULE_ID.isNotNull());
        }
        List<PunchRuleOwnerMap> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, PunchRuleOwnerMap.class));
            return null;
        });
        if (null != result && result.size() > 0)
            return result;
        return null;
    }

    private void prepareObj(PunchRuleOwnerMap obj) {
    }

    @Override
    public List<PunchLocationRule> queryPunchLocationRulesByName(String ownerType, Long ownerId, String name) {
        List<PunchLocationRule> result = queryPunchLocationRules(null, Integer.MAX_VALUE, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_PUNCH_LOCATION_RULES.OWNER_ID.eq(ownerId));
                query.addConditions(Tables.EH_PUNCH_LOCATION_RULES.OWNER_TYPE.eq(ownerType));
                query.addConditions(Tables.EH_PUNCH_LOCATION_RULES.NAME.eq(name));
                return query;
            }

        });
        return result;
    }

    @Override
    public void deletePunchGeopointsByRuleId(Long ruleId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        DeleteWhereStep<EhPunchGeopointsRecord> step = context
                .delete(Tables.EH_PUNCH_GEOPOINTS);
        Condition condition = Tables.EH_PUNCH_GEOPOINTS.LOCATION_RULE_ID.equal(ruleId);
        step.where(condition);
        step.execute();
    }

    @Override
    public void deletePunchLocationRuleByOwnerAndId(String ownerType, Long ownerId, Long id) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        DeleteWhereStep<EhPunchLocationRulesRecord> step = context
                .delete(Tables.EH_PUNCH_LOCATION_RULES);
        Condition condition = Tables.EH_PUNCH_LOCATION_RULES.ID.equal(id)
                .and(Tables.EH_PUNCH_LOCATION_RULES.OWNER_ID.equal(ownerId))
                .and(Tables.EH_PUNCH_LOCATION_RULES.OWNER_TYPE.equal(ownerType));
        step.where(condition);
        step.execute();
    }

    @Override
    public List<PunchLocationRule> queryPunchLocationRuleList(String ownerType, Long ownerId, CrossShardListingLocator locator, int i) {

        List<PunchLocationRule> result = queryPunchLocationRules(locator, i, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_PUNCH_LOCATION_RULES.OWNER_ID.eq(ownerId));
                query.addConditions(Tables.EH_PUNCH_LOCATION_RULES.OWNER_TYPE.eq(ownerType));
                return query;
            }

        });
        return result;
    }

    @Override
    public List<PunchWifiRule> queryPunchWiFiRulesByName(String ownerType, Long ownerId, String name) {
        List<PunchWifiRule> result = queryPunchWifiRules(null, Integer.MAX_VALUE, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_PUNCH_WIFI_RULES.OWNER_ID.eq(ownerId));
                query.addConditions(Tables.EH_PUNCH_WIFI_RULES.OWNER_TYPE.eq(ownerType));
                query.addConditions(Tables.EH_PUNCH_WIFI_RULES.NAME.eq(name));
                return query;
            }

        });
        return result;
    }

    @Override
    public void deletePunchWifisByRuleId(Long ruleId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        DeleteWhereStep<EhPunchWifisRecord> step = context
                .delete(Tables.EH_PUNCH_WIFIS);
        Condition condition = Tables.EH_PUNCH_WIFIS.WIFI_RULE_ID.equal(ruleId);
        step.where(condition);
        step.execute();
    }

    @Override
    public List<PunchWifiRule> queryPunchWifiRuleList(String ownerType, Long ownerId, CrossShardListingLocator locator, int i) {
        List<PunchWifiRule> result = queryPunchWifiRules(locator, i, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_PUNCH_WIFI_RULES.OWNER_ID.eq(ownerId));
                query.addConditions(Tables.EH_PUNCH_WIFI_RULES.OWNER_TYPE.eq(ownerType));
                return query;
            }

        });
        return result;
    }

    @Override
    public List<PunchWifi> listPunchWifisByRuleId(String ownerType, Long ownerId, Long ruleId) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_WIFIS);
        Condition condition = Tables.EH_PUNCH_WIFIS.WIFI_RULE_ID.equal(ruleId)
                .and(Tables.EH_PUNCH_WIFIS.OWNER_ID.eq(ownerId))
                .and(Tables.EH_PUNCH_WIFIS.OWNER_TYPE.eq(ownerType));
        step.where(condition);
        List<PunchWifi> result = step
                .orderBy(Tables.EH_PUNCH_WIFIS.ID.desc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, PunchWifi.class);
                });

        if (null != result && result.size() > 0)
            return result;
        return null;
    }

    @Override
    public List<PunchWorkdayRule> queryPunchWorkdayRulesByName(String ownerType, Long ownerId, String name) {
        List<PunchWorkdayRule> result = queryPunchWorkdayRules(null, Integer.MAX_VALUE, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_PUNCH_WORKDAY_RULES.OWNER_ID.eq(ownerId));
                query.addConditions(Tables.EH_PUNCH_WORKDAY_RULES.OWNER_TYPE.eq(ownerType));
                query.addConditions(Tables.EH_PUNCH_WORKDAY_RULES.NAME.eq(name));
                return query;
            }

        });
        return result;
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindHolidayByDate", allEntries = true)})
    public void deletePunchHolidayByRuleId(Long ruleId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        DeleteWhereStep<EhPunchHolidaysRecord> step = context
                .delete(Tables.EH_PUNCH_HOLIDAYS);
        Condition condition = Tables.EH_PUNCH_HOLIDAYS.WORKDAY_RULE_ID.equal(ruleId);
        step.where(condition);
        step.execute();
    }

    @Override
    public List<PunchWorkdayRule> queryPunchWorkdayRuleList(String ownerType, Long ownerId, CrossShardListingLocator locator, int i) {

        List<PunchWorkdayRule> result = queryPunchWorkdayRules(locator, i, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_PUNCH_WORKDAY_RULES.OWNER_ID.eq(ownerId));
                query.addConditions(Tables.EH_PUNCH_WORKDAY_RULES.OWNER_TYPE.eq(ownerType));
                return query;
            }

        });
        return result;
    }

    @Override
    public List<PunchHoliday> listPunchHolidaysByRuleId(String ownerType, Long ownerId, Long ruleId) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_HOLIDAYS);
        Condition condition = Tables.EH_PUNCH_HOLIDAYS.WORKDAY_RULE_ID.equal(ruleId)
                .and(Tables.EH_PUNCH_HOLIDAYS.OWNER_ID.eq(ownerId))
                .and(Tables.EH_PUNCH_HOLIDAYS.OWNER_TYPE.eq(ownerType));
        step.where(condition);
        List<PunchHoliday> result = step
                .orderBy(Tables.EH_PUNCH_HOLIDAYS.ID.desc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, PunchHoliday.class);
                });

        if (null != result && result.size() > 0)
            return result;
        return null;
    }

    @Override
    public List<PunchRule> queryPunchRulesByName(String ownerType, Long ownerId, String name) {
        List<PunchRule> result = queryPunchRules(null, Integer.MAX_VALUE, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_PUNCH_RULES.OWNER_ID.eq(ownerId));
                query.addConditions(Tables.EH_PUNCH_RULES.OWNER_TYPE.eq(ownerType));
                query.addConditions(Tables.EH_PUNCH_RULES.NAME.eq(name));
                return query;
            }

        });
        return result;
    }

    @Override
    public List<PunchRuleOwnerMap> queryPunchRuleOwnerMapsByRuleId(String ownerType, Long ownerId, Long ruleId) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_RULE_OWNER_MAP);
        Condition condition = Tables.EH_PUNCH_RULE_OWNER_MAP.PUNCH_RULE_ID.equal(ruleId)
                .and(Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_ID.eq(ownerId))
                .and(Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_TYPE.eq(ownerType));
        step.where(condition);
        List<PunchRuleOwnerMap> result = step
                .orderBy(Tables.EH_PUNCH_RULE_OWNER_MAP.ID.desc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, PunchRuleOwnerMap.class);
                });

        if (null != result && result.size() > 0)
            return result;
        return null;
    }

    @Override
    public List<PunchRule> queryPunchRuleList(String ownerType, Long ownerId, CrossShardListingLocator locator, int i) {

        List<PunchRule> result = queryPunchRules(locator, i, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_PUNCH_RULES.OWNER_ID.eq(ownerId));
                query.addConditions(Tables.EH_PUNCH_RULES.OWNER_TYPE.eq(ownerType));
                return query;
            }

        });
        return result;
    }

    @Override
    public List<PunchRuleOwnerMap> queryPunchRuleOwnerMapList(String ownerType, Long ownerId, String targetType, Long targetId, List<Long> userIds,
                                                              CrossShardListingLocator locator, int i) {

        List<PunchRuleOwnerMap> result = queryPunchRuleOwnerMaps(locator, i, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_ID.eq(ownerId));
                query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_TYPE.eq(ownerType));
                if (null != targetId)
                    query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.TARGET_ID.eq(targetId));
                if (null != userIds) {
                    query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.TARGET_ID.in(userIds));
                }
                if (StringUtils.isNotBlank(targetType))
                    query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.TARGET_TYPE.eq(targetType));
                return query;
            }

        });
        return result;
    }

    @Override
    public List<PunchRuleOwnerMap> queryPunchRuleOwnerMapList(String ownerType, Long ownerId, String targetType, Long targetId,
                                                              CrossShardListingLocator locator, int i) {

        List<PunchRuleOwnerMap> result = queryPunchRuleOwnerMaps(locator, i, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_ID.eq(ownerId));
                query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_TYPE.eq(ownerType));
                if (null != targetId)
                    query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.TARGET_ID.eq(targetId));
                if (StringUtils.isNotBlank(targetType))
                    query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.TARGET_TYPE.eq(targetType));
                return query;
            }

        });
        return result;
    }

    @Override
    public List<PunchHoliday> queryPunchHolidaysByStatus(String ownerType, Long ownerId, Long workdayRuleId, byte code) {

        List<PunchHoliday> result = queryPunchHolidays(null, Integer.MAX_VALUE, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_PUNCH_HOLIDAYS.OWNER_ID.eq(ownerId));
                query.addConditions(Tables.EH_PUNCH_HOLIDAYS.OWNER_TYPE.eq(ownerType));
                query.addConditions(Tables.EH_PUNCH_HOLIDAYS.STATUS.eq(code));
                return query;
            }

        });
        return result;
    }

    @Override
    public List<Long> queryPunchOrganizationsFromRules() {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record1<Long>> step = context.selectDistinct(Tables.EH_PUNCH_RULES.OWNER_ID).from(
                Tables.EH_PUNCH_RULES);
        Condition condition = Tables.EH_PUNCH_RULES.OWNER_TYPE.equal(PunchOwnerType.ORGANIZATION.getCode());
        step.where(condition);
        List<Long> result = new ArrayList<Long>();
        step.fetch().map((r) -> {
            result.add(r.value1());
            return null;
        });
        return result;
    }


    @Override
    public Long createPunchStatistic(PunchStatistic obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchStatistics.class);
        long id = sequenceProvider.getNextSequence(key);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhPunchStatisticsDao dao = new EhPunchStatisticsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updatePunchStatistic(PunchStatistic obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchStatisticsDao dao = new EhPunchStatisticsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deletePunchStatistic(PunchStatistic obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchStatisticsDao dao = new EhPunchStatisticsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public PunchStatistic getPunchStatisticById(Long id) {
        try {
            PunchStatistic[] result = new PunchStatistic[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

            result[0] = context.select().from(Tables.EH_PUNCH_STATISTICS)
                    .where(Tables.EH_PUNCH_STATISTICS.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, PunchStatistic.class);
                    });

            return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    private void prepareObj(PunchStatistic obj) {
    }

    @Override
    public List<PunchStatistic> queryPunchStatistics(String ownerType, Long ownerId, List<String> months, Byte exceptionStatus, List<Long> detailIds, Integer pageOffset, Integer pageSize) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhPunchStatisticsRecord> query = context.selectQuery(Tables.EH_PUNCH_STATISTICS);
        query.addConditions(Tables.EH_PUNCH_STATISTICS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_PUNCH_STATISTICS.OWNER_ID.eq(ownerId));
        if (exceptionStatus != null) {
            query.addConditions(Tables.EH_PUNCH_STATISTICS.EXCEPTION_STATUS.eq(exceptionStatus));
        }
        if (null != detailIds)
            query.addConditions(Tables.EH_PUNCH_STATISTICS.DETAIL_ID.in(detailIds));
        if (null != months)
            query.addConditions(Tables.EH_PUNCH_STATISTICS.PUNCH_MONTH.in(months));

        query.addOrderBy(Tables.EH_PUNCH_STATISTICS.PUNCH_MONTH.desc(), Tables.EH_PUNCH_STATISTICS.DEPT_ID.asc(), Tables.EH_PUNCH_STATISTICS.DETAIL_ID.asc());

        if (pageOffset != null && pageSize != null) {
            query.addLimit(pageOffset, pageSize);
        }

        List<PunchStatistic> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, PunchStatistic.class);
        });

        if (null != objs && objs.size() > 0)
            return objs;
        return null;
    }

    @Override
    public void deletePunchStatisticByUser(String ownerType, List<Long> ownerId, String punchMonth, Long detailId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        DeleteWhereStep<EhPunchStatisticsRecord> step = context.delete(Tables.EH_PUNCH_STATISTICS);
        Condition condition = Tables.EH_PUNCH_STATISTICS.PUNCH_MONTH.equal(punchMonth)
                .and(Tables.EH_PUNCH_STATISTICS.OWNER_ID.in(ownerId))
                .and(Tables.EH_PUNCH_STATISTICS.DETAIL_ID.equal(detailId))
                .and(Tables.EH_PUNCH_STATISTICS.OWNER_TYPE.equal(ownerType));
        step.where(condition);
        step.execute();
    }

    @Override
    public PunchRuleOwnerMap getPunchRuleOwnerMapById(Long id) {
        try {
            PunchRuleOwnerMap[] result = new PunchRuleOwnerMap[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

            result[0] = context.select().from(Tables.EH_PUNCH_RULE_OWNER_MAP)
                    .where(Tables.EH_PUNCH_RULE_OWNER_MAP.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, PunchRuleOwnerMap.class);
                    });

            return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<PunchDayLog> listPunchDayLogs(List<Long> userIds, Long ownerId, List<Long> detailIds, List<Long> dptIds, String startDay, String endDay,
                                              Byte arriveTimeCompareFlag, Time arriveTime, Byte leaveTimeCompareFlag, Time leaveTime, Byte workTimeCompareFlag,
                                              Time workTime, Byte exceptionStatus, Integer pageOffset, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_PUNCH_DAY_LOGS);
        Condition condition = (Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.equal(ownerId));
        if (CollectionUtils.isNotEmpty(userIds))
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.USER_ID.in(userIds));
        if (null != detailIds) {
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.DETAIL_ID.in(detailIds));
        }
        if (CollectionUtils.isNotEmpty(dptIds))
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.DEPT_ID.in(dptIds));
        if (exceptionStatus != null)
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.EXCEPTION_STATUS.eq(exceptionStatus));
        if (!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
            Date startDate = Date.valueOf(startDay);
            Date endDate = Date.valueOf(endDay);
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.between(startDate).and(endDate));
        }
        if (null != arriveTimeCompareFlag && arriveTime != null) {
            if (arriveTimeCompareFlag.equals(TimeCompareFlag.LESSOREQUAL.getCode())) {
                condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME.lessOrEqual(arriveTime));
            } else {
                condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME.greaterOrEqual(arriveTime));
            }
        }
        if (null != leaveTimeCompareFlag && null != leaveTime) {
            if (leaveTimeCompareFlag.equals(TimeCompareFlag.LESSOREQUAL.getCode())) {
                condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.LEAVE_TIME.lessOrEqual(leaveTime));
            } else {
                condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.LEAVE_TIME.greaterOrEqual(leaveTime));
            }
        }
        if (null != workTimeCompareFlag && null != workTime) {
            if (workTimeCompareFlag.equals(TimeCompareFlag.LESSOREQUAL.getCode())) {
                condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.WORK_TIME.lessOrEqual(workTime));
            } else {
                condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.WORK_TIME.greaterOrEqual(workTime));
            }
        }

        if (null != pageOffset && null != pageSize)
            step.limit(pageOffset, pageSize);
        List<PunchDayLog> result = new ArrayList<PunchDayLog>();
        step.where(condition).orderBy(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.desc(), Tables.EH_PUNCH_DAY_LOGS.DEPT_ID.asc(),
                Tables.EH_PUNCH_DAY_LOGS.PUNCH_ORGANIZATION_ID.asc(), Tables.EH_PUNCH_DAY_LOGS.DETAIL_ID.asc()).fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, PunchDayLog.class));
            return null;
        });
        if (result == null || result.isEmpty())
            return null;
        return result;
    }

    //用于查询是否有过异常申请
    @Override
    public PunchExceptionRequest findPunchExceptionRequest(Long userId, Long ownerId, Long punchDate,
                                                           Integer intervalNo) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record> step = context.select().from(Tables.EH_PUNCH_EXCEPTION_REQUESTS)
                .where(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.eq(userId))
                .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.eq(ownerId))
                .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_INTERVAL_NO.eq(intervalNo))
                .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.eq(new Date(punchDate)))
                .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
        Record record = step.limit(1).fetchOne();

        if (record != null) {
            return ConvertHelper.convert(record, PunchExceptionRequest.class);
        }

        return null;
    }

    @Override
    public PunchExceptionRequest findPunchExceptionRequestByRequestId(Long ownerId, Long creatorUid, Long requestId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Record record = context.select().from(Tables.EH_PUNCH_EXCEPTION_REQUESTS)
                .where(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.eq(creatorUid))
                .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.eq(ownerId))
                .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.REQUEST_ID.eq(requestId))
//				.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
                .limit(1)
                .fetchOne();

        if (record != null) {
            return ConvertHelper.convert(record, PunchExceptionRequest.class);
        }

        return null;
    }

    @Override
    public void deletePunchExceptionRequest(PunchExceptionRequest punchExceptionRequest) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchExceptionRequestsDao dao = new EhPunchExceptionRequestsDao(context.configuration());
        dao.delete(punchExceptionRequest);
    }

    @Override
    public PunchExceptionApproval findPunchExceptionApproval(Long userId, Long ownerId, Date date) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Record record = context.select().from(Tables.EH_PUNCH_EXCEPTION_APPROVALS)
                .where(Tables.EH_PUNCH_EXCEPTION_APPROVALS.USER_ID.eq(userId))
                .and(Tables.EH_PUNCH_EXCEPTION_APPROVALS.ENTERPRISE_ID.eq(ownerId))
                .and(Tables.EH_PUNCH_EXCEPTION_APPROVALS.PUNCH_DATE.eq(date))
                .limit(1)
                .fetchOne();

        if (record != null) {
            return ConvertHelper.convert(record, PunchExceptionApproval.class);
        }
        return null;
    }

    @Override
    public PunchDayLog findPunchDayLog(Long userId, Long enterpriseId, Date punchDate) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Record record = context.select().from(Tables.EH_PUNCH_DAY_LOGS)
                .where(Tables.EH_PUNCH_DAY_LOGS.USER_ID.eq(userId))
                .and(Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.eq(enterpriseId))
                .and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.eq(punchDate))
                .limit(1)
                .fetchOne();

        if (record != null) {
            return ConvertHelper.convert(record, PunchDayLog.class);
        }
        return null;
    }

    @Override
    public Integer countPunchLogDevice(Long userId, Long companyId,
                                       Date beginDate, Date punchDate) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectDistinct(Tables.EH_PUNCH_LOGS.IDENTIFICATION).from(Tables.EH_PUNCH_LOGS)
                .where(Tables.EH_PUNCH_LOGS.USER_ID.eq(userId))
                .and(Tables.EH_PUNCH_LOGS.ENTERPRISE_ID.eq(companyId))
                .and(Tables.EH_PUNCH_LOGS.PUNCH_DATE.eq(punchDate))
                .and(Tables.EH_PUNCH_LOGS.DEVICE_CHANGE_FLAG.eq(NormalFlag.NEED.getCode()))
                .fetchCount();
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindPunchTimeRuleById", allEntries = true)})
    public void deletePunchTimeRulesByOwnerAndTarget(String ownerType, Long ownerId,
                                                     String targetType, Long targetId) {

//        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
//		DeleteWhereStep<EhPunchTimeRulesRecord> step = context.delete(Tables.EH_PUNCH_TIME_RULES);
//		Condition condition = Tables.EH_PUNCH_TIME_RULES.TARGET_ID.equal(targetId)
//				.and(Tables.EH_PUNCH_TIME_RULES.TARGET_TYPE.equal(targetType))
//				.and(Tables.EH_PUNCH_TIME_RULES.OWNER_ID.equal(ownerId))
//				.and(Tables.EH_PUNCH_TIME_RULES.OWNER_TYPE.equal(ownerType)) ; 
//		step.where(condition);
//		step.execute();
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "ListPunchTimeIntervalByTimeRuleId", key = "#ptInterval.timeRuleId")})
    public void createPunchTimeInterval(PunchTimeInterval ptInterval) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchTimeIntervals.class);
        long id = sequenceProvider.getNextSequence(key);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        ptInterval.setId(id);
        EhPunchTimeIntervalsDao dao = new EhPunchTimeIntervalsDao(context.configuration());
        dao.insert(ptInterval);
    }


    @Override
    @Caching(evict = {@CacheEvict(value = "ListPunchSpecailDaysByOrgId", key = "#psd.punchOrganizationId")})
    public void createPunchSpecialDay(PunchSpecialDay psd) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchSpecialDays.class);
        long id = sequenceProvider.getNextSequence(key);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        psd.setId(id);
        EhPunchSpecialDaysDao dao = new EhPunchSpecialDaysDao(context.configuration());
        dao.insert(psd);

    }

    @Override
    public void deletePunchGeopointsByOwnerId(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteWhereStep<EhPunchGeopointsRecord> step = context.delete(Tables.EH_PUNCH_GEOPOINTS);
        Condition condition = Tables.EH_PUNCH_GEOPOINTS.OWNER_ID.equal(id);
        step.where(condition);
        step.execute();
    }

    @Override
    public void deletePunchWifisByOwnerId(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteWhereStep<EhPunchWifisRecord> step = context.delete(Tables.EH_PUNCH_WIFIS);
        Condition condition = Tables.EH_PUNCH_WIFIS.OWNER_ID.equal(id);
        step.where(condition);
        step.execute();

    }

    @Override
    @Cacheable(value = "GetPunchRuleByPunchOrgId", key = "#id", unless = "#result == null")
    public PunchRule getPunchruleByPunchOrgId(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Record record = context.select().from(Tables.EH_PUNCH_RULES)
                .where(Tables.EH_PUNCH_RULES.PUNCH_ORGANIZATION_ID.eq(id))
                .fetchOne();
        if (record != null) {
            return ConvertHelper.convert(record, PunchRule.class);
        }
        return null;
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindPunchTimeRuleById", allEntries = true)})
    public void deletePunchTimeRuleByPunchOrgId(Long punchOrgId) {
//		 DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
//			DeleteWhereStep<EhPunchTimeRulesRecord> step = context.delete(Tables.EH_PUNCH_TIME_RULES);
//			Condition condition = Tables.EH_PUNCH_TIME_RULES.OWNER_ID.equal(punchOrgId); 
//			step.where(condition);
//			step.execute();
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "ListPunchSpecailDaysByOrgId", key = "#id")})
    public void deletePunchSpecialDaysByPunchOrgId(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteWhereStep<EhPunchSpecialDaysRecord> step = context.delete(Tables.EH_PUNCH_SPECIAL_DAYS);
        Condition condition = Tables.EH_PUNCH_SPECIAL_DAYS.PUNCH_ORGANIZATION_ID.equal(id);
        step.where(condition);
        step.execute();
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "ListPunchTimeIntervalByTimeRuleId", allEntries = true)})
    public void deletePunchTimeIntervalByPunchRuleId(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteWhereStep<EhPunchTimeIntervalsRecord> step = context.delete(Tables.EH_PUNCH_TIME_INTERVALS);
        Condition condition = Tables.EH_PUNCH_TIME_INTERVALS.PUNCH_RULE_ID.equal(id);
        step.where(condition);
        step.execute();

    }
    @Override
	public List<PunchTimeRule> listActivePunchTimeRuleByOwnerAndStatusList(String ownerType, Long ownerId, List<Byte> statusList) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_TIME_RULES);
        Condition condition = Tables.EH_PUNCH_TIME_RULES.OWNER_TYPE.equal(ownerType)
                .and(Tables.EH_PUNCH_TIME_RULES.OWNER_ID.equal(ownerId))
                .and(Tables.EH_PUNCH_TIME_RULES.STATUS.in(statusList));
        step.where(condition);
        List<PunchTimeRule> result = step
                .orderBy(Tables.EH_PUNCH_TIME_RULES.ID.asc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, PunchTimeRule.class);
                });
        return result;
    }
    
    @Override
    public List<PunchTimeRule> listActivePunchTimeRuleByOwner(String ownerType, Long ownerId, Byte status) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_TIME_RULES);
        Condition condition = Tables.EH_PUNCH_TIME_RULES.OWNER_TYPE.equal(ownerType)
                .and(Tables.EH_PUNCH_TIME_RULES.OWNER_ID.equal(ownerId))
                .and(Tables.EH_PUNCH_TIME_RULES.STATUS.eq(status));
        step.where(condition);
        List<PunchTimeRule> result = step
                .orderBy(Tables.EH_PUNCH_TIME_RULES.ID.asc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, PunchTimeRule.class);
                });
        return result;
    }

    @Override
    @Cacheable(value = "ListPunchTimeIntervalByTimeRuleId", key = "#timeRuleId", unless = "#result.size()==0")
    public List<PunchTimeInterval> listPunchTimeIntervalByTimeRuleId(Long timeRuleId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_TIME_INTERVALS);
        Condition condition = Tables.EH_PUNCH_TIME_INTERVALS.TIME_RULE_ID.equal(timeRuleId);
        step.where(condition);
        List<PunchTimeInterval> result = step
                .orderBy(Tables.EH_PUNCH_TIME_INTERVALS.ARRIVE_TIME_LONG.asc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, PunchTimeInterval.class);
                });
        return result;
    }

    @Override
    public List<PunchGeopoint> listPunchGeopointsByOwner(String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_GEOPOINTS);
        Condition condition = Tables.EH_PUNCH_GEOPOINTS.OWNER_TYPE.equal(ownerType)
                .and(Tables.EH_PUNCH_GEOPOINTS.OWNER_ID.equal(ownerId));
        step.where(condition);
        List<PunchGeopoint> result = step
                .orderBy(Tables.EH_PUNCH_GEOPOINTS.ID.asc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, PunchGeopoint.class);
                });
        return result;
    }

    @Override
    public List<PunchWifi> listPunchWifsByOwner(String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_WIFIS);
        Condition condition = Tables.EH_PUNCH_WIFIS.OWNER_TYPE.equal(ownerType)
                .and(Tables.EH_PUNCH_WIFIS.OWNER_ID.equal(ownerId));
        step.where(condition);
        List<PunchWifi> result = step
                .orderBy(Tables.EH_PUNCH_WIFIS.ID.asc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, PunchWifi.class);
                });
        return result;
    }

    @Override
    @Cacheable(value="ListPunchSpecailDaysByOrgId",key="#punchOrganizationId",unless = "#result.size()==0")
    public List<PunchSpecialDay> listPunchSpecailDaysByOrgId(Long punchOrganizationId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_SPECIAL_DAYS);
        Condition condition = Tables.EH_PUNCH_SPECIAL_DAYS.PUNCH_ORGANIZATION_ID.equal(punchOrganizationId);
        step.where(condition);
        List<PunchSpecialDay> result = step
                .orderBy(Tables.EH_PUNCH_SPECIAL_DAYS.ID.asc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, PunchSpecialDay.class);
                });
        return result;
    }

    @Override
    public PunchSpecialDay findSpecialDayByDateAndOrgId(Long punchOrganizationId,
                                                        java.util.Date date) {
        try {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

            PunchSpecialDay result = context.select().from(Tables.EH_PUNCH_SPECIAL_DAYS)
                    .where(Tables.EH_PUNCH_SPECIAL_DAYS.PUNCH_ORGANIZATION_ID.eq(punchOrganizationId))
                    .and(Tables.EH_PUNCH_SPECIAL_DAYS.RULE_DATE.eq(new java.sql.Date(date.getTime())))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, PunchSpecialDay.class);
                    });

            return result;
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    @Cacheable(value = "FindHolidayByDate", key = "#punchDate", unless = "#result==null")
    public PunchHoliday findHolidayByDate(Date punchDate) {
        try {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

            PunchHoliday result = context.select().from(Tables.EH_PUNCH_HOLIDAYS)
                    .where(Tables.EH_PUNCH_HOLIDAYS.RULE_DATE.eq(new java.sql.Date(punchDate.getTime())))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, PunchHoliday.class);
                    });
            return result;
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<PunchTimeRule> listPunchTimeRulesBySplitTime(long beginTime, long endTime) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_TIME_RULES);
        Condition condition = Tables.EH_PUNCH_TIME_RULES.DAY_SPLIT_TIME_LONG.greaterOrEqual(beginTime)
                .and(Tables.EH_PUNCH_TIME_RULES.DAY_SPLIT_TIME_LONG.lessOrEqual(endTime));
        step.where(condition);
        List<PunchTimeRule> result = step
                .orderBy(Tables.EH_PUNCH_TIME_RULES.ID.asc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, PunchTimeRule.class);
                });
        return result;
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindPunchTimeRuleById", allEntries = true)})
    public void deletePunchTimeRuleByRuleId(Long id) {
//		DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
//		DeleteWhereStep<EhPunchTimeRulesRecord> step = context.delete(Tables.EH_PUNCH_TIME_RULES);
//		Condition condition = Tables.EH_PUNCH_TIME_RULES.PUNCH_RULE_ID.eq(id);
//		step.where(condition);
//		step.execute();
    }

    @Override
    public Integer approveAbnormalPunch(Long userId, Date punchDate, Integer punchIntervalNo, Byte punchType) {

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        return context.update(Tables.EH_PUNCH_LOGS).set(Tables.EH_PUNCH_LOGS.APPROVAL_STATUS, PunchStatus.NORMAL.getCode())
        		.set(Tables.EH_PUNCH_LOGS.UPDATE_DATE, new java.sql.Date(DateHelper.currentGMTTime().getTime()))
                .where(Tables.EH_PUNCH_LOGS.PUNCH_DATE.eq(punchDate))
                .and(Tables.EH_PUNCH_LOGS.PUNCH_INTERVAL_NO.eq(punchIntervalNo))
                .and(Tables.EH_PUNCH_LOGS.PUNCH_TYPE.eq(punchType))
                .and(Tables.EH_PUNCH_LOGS.USER_ID.eq(userId)).execute();
    }

    @Override
    public List<PunchExceptionRequest> listPunchExceptionRequestBetweenBeginAndEndTime(Long userId, Long enterpriseId, Timestamp dayStart, Timestamp dayEnd) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select(Tables.EH_PUNCH_EXCEPTION_REQUESTS.fields())
                .from(Tables.EH_PUNCH_EXCEPTION_REQUESTS);
        Condition condition = (Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.eq(enterpriseId));
        if(null != dayStart && null != dayEnd)
	        condition = condition.and((Tables.EH_PUNCH_EXCEPTION_REQUESTS.BEGIN_TIME.lessOrEqual(dayEnd)
	                .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.BEGIN_TIME.greaterOrEqual(dayStart)))
	                .or(Tables.EH_PUNCH_EXCEPTION_REQUESTS.END_TIME.lessOrEqual(dayEnd)
	                        .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.END_TIME.greaterOrEqual(dayStart)))
	                .or(Tables.EH_PUNCH_EXCEPTION_REQUESTS.END_TIME.greaterOrEqual(dayEnd)
	                        .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.BEGIN_TIME.lessOrEqual(dayStart))));
        if(null != userId)
        	condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.eq(userId));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.ne(ApprovalStatus.REJECTION.getCode()));

        List<EhPunchExceptionRequestsRecord> resultRecord = step.where(condition)
                .orderBy(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ID.desc()).fetch()
                .map(new EhPunchExceptionRequestMapper());

        List<PunchExceptionRequest> result = resultRecord.stream().map((r) -> {
            return ConvertHelper.convert(r, PunchExceptionRequest.class);
        }).collect(Collectors.toList());
        if (result == null || result.size() == 0) {
            return new ArrayList<>();
        }
        return new ArrayList<>(result);
    }

    @Override
    public List<PunchExceptionRequestStatisticsItemDTO> countPunchExceptionRequestGroupByMonth(Long userId, Long enterpriseId, String punchMonth) {
        if (userId == null || userId == 0) {
            return null;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date dayStart = new Date(dateFormat.parse(punchMonth + "01").getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dayStart);
            calendar.add(Calendar.MONTH, 1);
            Date dayEnd = new Date(calendar.getTimeInMillis());
            DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
            SelectJoinStep<Record2<String, Integer>> query = context.select(Tables.EH_PUNCH_EXCEPTION_REQUESTS.APPROVAL_ATTRIBUTE, Tables.EH_PUNCH_EXCEPTION_REQUESTS.ID.count())
                    .from(Tables.EH_PUNCH_EXCEPTION_REQUESTS);
            Condition condition = Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.eq(enterpriseId)
                    .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.eq(userId))
                    .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.BEGIN_TIME.lt(new Timestamp(dayEnd.getTime())))
                    .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.END_TIME.greaterOrEqual(new Timestamp(dayStart.getTime())))
                    .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.ne(ApprovalStatus.REJECTION.getCode()));

            List<PunchExceptionRequestStatisticsItemDTO> result = query.where(condition)
                    .groupBy(Tables.EH_PUNCH_EXCEPTION_REQUESTS.APPROVAL_ATTRIBUTE).fetch()
                    .map(record2 -> {
                        PunchExceptionRequestStatisticsItemDTO dto = new PunchExceptionRequestStatisticsItemDTO();
                        dto.setItemName(record2.value1());
                        dto.setNum(record2.value2());
                        return dto;
                    });

            return result;
        } catch (ParseException e) {
            LOGGER.error("punchMonth invalid", e);
            return null;
        }
    }

    @Override
    public List<PunchExceptionRequest> listpunchexceptionRequestByDate(Long userId, Long enterpriseId, Date punchDate) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select(Tables.EH_PUNCH_EXCEPTION_REQUESTS.fields()).from(Tables.EH_PUNCH_EXCEPTION_REQUESTS);
        Condition condition = (Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.eq(enterpriseId));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.eq(punchDate));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.eq(userId));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.ne(ApprovalStatus.REJECTION.getCode()));
        
        List<EhPunchExceptionRequestsRecord> resultRecord = step.where(condition)
                .orderBy(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ID.desc()).fetch()
                .map(new EhPunchExceptionRequestMapper());

        List<PunchExceptionRequest> result = resultRecord.stream().map(r -> ConvertHelper.convert(r, PunchExceptionRequest.class)).collect(Collectors.toList());

        if (result == null || result.size() == 0) {
            return null;
        }
        return result;
    }

    @Override
    public List<PunchExceptionRequest> listpunchexceptionRequestByDate(Long userId, Long enterpriseId, Date startDate ,Date endDate) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select(Tables.EH_PUNCH_EXCEPTION_REQUESTS.fields()).from(Tables.EH_PUNCH_EXCEPTION_REQUESTS);
        Condition condition = (Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.eq(enterpriseId));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.between(startDate, endDate));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.ne(ApprovalStatus.REJECTION.getCode()));
        if(null != userId)
        	condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.eq(userId));

        List<EhPunchExceptionRequestsRecord> resultRecord = step.where(condition)
                .orderBy(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ID.desc()).fetch()
                .map(new EhPunchExceptionRequestMapper());

        List<PunchExceptionRequest> result = resultRecord.stream().map(r -> ConvertHelper.convert(r, PunchExceptionRequest.class)).collect(Collectors.toList());

        if (result == null || result.size() == 0) {
            return null;
        }
        return new ArrayList<>(result);
    }

    @Override
    public Integer countExceptionRequests(Long userId, Long ownerId, String punchMonth, List<Byte> statusList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date beginDate;
        try {
            beginDate = new Date(dateFormat.parse(punchMonth + "01").getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(beginDate);
            calendar.add(Calendar.MONTH, 1);
            Date endDate = new Date(calendar.getTimeInMillis());
            SelectConditionStep<Record1<Integer>> step = getReadOnlyContext().select(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ID.count()).from(Tables.EH_PUNCH_EXCEPTION_REQUESTS)
                    .where(Tables.EH_PUNCH_EXCEPTION_REQUESTS.CREATOR_UID.eq(userId))
                    .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.eq(ownerId))
                    .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.greaterOrEqual(beginDate))
                    .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.lt(endDate))
                    .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.APPROVAL_ATTRIBUTE.eq(GeneralApprovalAttribute.ABNORMAL_PUNCH.getCode()))
                    .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.in(statusList));
            return step.fetchOneInto(Integer.class);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public Map<Long, Integer> countAbonormalExceptionRequestGroupByPunchDate(Long organizationId, Long userId, String month, List<Byte> statusList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date beginDate;
        try {
            beginDate = new Date(dateFormat.parse(month + "01").getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(beginDate);
            calendar.add(Calendar.MONTH, 1);
            Date endDate = new Date(calendar.getTimeInMillis());
            SelectHavingStep<Record2<Date, Integer>> step = getReadOnlyContext().
                    select(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE, Tables.EH_PUNCH_EXCEPTION_REQUESTS.ID.count()).from(Tables.EH_PUNCH_EXCEPTION_REQUESTS)
                    .where(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.eq(organizationId))
                    .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.eq(userId))
                    .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.greaterOrEqual(beginDate))
                    .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.lt(endDate))
                    .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.APPROVAL_ATTRIBUTE.eq(GeneralApprovalAttribute.ABNORMAL_PUNCH.getCode()))
                    .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.in(statusList)).groupBy(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE);
            Result<Record2<Date, Integer>> records = step.fetch();
            if (null != records && records.size() > 0) {
                Map<Long, Integer> result = new HashMap<>();
                for (Record2<Date, Integer> record : records) {
                    if (record.value1() != null) {
                        result.put(record.value1().getTime(), record.value2());
                    }
                }
                return result;
            }
        } catch (ParseException e) {
            LOGGER.error("listAbonormalExceptionRequestByOwnerAndMonth error : \n", e);
            return new HashMap<>();
        }
        return new HashMap<>();
    }

    @Override
    public List<ExtDTO> listAskForLeaveExtDTOs(Long userId, String ownerType, Long ownerId, String punchMonth, Map<Long, String> approvalCategoryIdNames) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Timestamp beginDate;
        try {
            beginDate = new Timestamp(dateFormat.parse(punchMonth + "01").getTime());
            List<ExtDTO> result = null;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(beginDate);
            calendar.add(Calendar.MONTH, 1);
            Timestamp endDate = new Timestamp(calendar.getTimeInMillis());
            SelectHavingStep<Record2<String, BigDecimal>> step = getReadOnlyContext()
                    .select(Tables.EH_APPROVAL_CATEGORIES.CATEGORY_NAME, Tables.EH_PUNCH_EXCEPTION_REQUESTS.DURATION_DAY.sum().round(4))
                    .from(Tables.EH_APPROVAL_CATEGORIES).leftOuterJoin(Tables.EH_PUNCH_EXCEPTION_REQUESTS)
                    .on(Tables.EH_APPROVAL_CATEGORIES.ID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.CATEGORY_ID))
                    .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.CREATOR_UID.eq(userId))
                    .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.eq(ownerId))
                    .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.END_TIME.greaterOrEqual(beginDate))
                    .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.BEGIN_TIME.lt(endDate))
                    .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.APPROVAL_ATTRIBUTE.eq(GeneralApprovalAttribute.ASK_FOR_LEAVE.getCode()))
                    .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.eq(ApprovalStatus.AGREEMENT.getCode()))
                    .where(Tables.EH_APPROVAL_CATEGORIES.ID.in(approvalCategoryIdNames.keySet()))
                    .groupBy(Tables.EH_APPROVAL_CATEGORIES.CATEGORY_NAME);

            result = step.orderBy(Tables.EH_APPROVAL_CATEGORIES.DEFAULT_ORDER).fetch().map((r) -> {
                ExtDTO dto = new ExtDTO();
                dto.setName(r.value1());
                if (r.value2() == null)
                    dto.setTimeCount("0");
                else
                    dto.setTimeCount(r.value2().toString());

                return dto;
            });
            if (null == result || result.size() == 0) {
                return null;
            }
            return result;
        } catch (ParseException e) {
            return null;
        }
    }

    private EhApprovalRequestsDao getReadWriteDao() {
        return getDao(getReadWriteContext());
    }

    private EhApprovalRequestsDao getReadOnlyDao() {
        return getDao(getReadOnlyContext());
    }

    private EhApprovalRequestsDao getDao(DSLContext context) {
        return new EhApprovalRequestsDao(context.configuration());
    }

    private DSLContext getReadWriteContext() {
        return getContext(AccessSpec.readWrite());
    }

    private DSLContext getReadOnlyContext() {
        return getContext(AccessSpec.readOnly());
    }

    private DSLContext getContext(AccessSpec accessSpec) {
        return dbProvider.getDslContext(accessSpec);
    }

    @Override
    public List<PunchRule> listPunchRulesByStatus(List<Byte> statusList) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhPunchRulesRecord> query = context
                .selectQuery(Tables.EH_PUNCH_RULES);

        Condition condition = Tables.EH_PUNCH_RULES.STATUS.in(statusList);
        query.addConditions(condition);
        List<PunchRule> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, PunchRule.class));
            return null;
        });
        if (null != result && result.size() > 0)
            return result;
        return null;
    }

    @Override
    public List<PunchRule> listPunchRulesByOwnerAndRuleType(String ownerType, Long ownerId, byte ruleType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhPunchRulesRecord> query = context
                .selectQuery(Tables.EH_PUNCH_RULES);

        Condition condition = Tables.EH_PUNCH_RULES.OWNER_ID.eq(ownerId)
                .and(Tables.EH_PUNCH_RULES.RULE_TYPE.eq(ruleType));

        query.addConditions(condition);
        List<PunchRule> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, PunchRule.class));
            return null;
        });
        if (null != result && result.size() > 0)
            return result;
        return null;
    }

    @Override
    public PunchExceptionRequest findPunchExceptionRequest(Long userId, Long enterpriseId, Date punchDate, Integer punchIntervalNo, Byte punchType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select(Tables.EH_PUNCH_EXCEPTION_REQUESTS.fields())
                .from(Tables.EH_PUNCH_EXCEPTION_REQUESTS);
        Condition condition = (Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.eq(enterpriseId));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.eq(userId));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.eq(punchDate));
        if (null != punchIntervalNo) {
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_INTERVAL_NO.eq(punchIntervalNo));
        }
        if (null != punchType) {
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_TYPE.eq(punchType));
        }
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.APPROVAL_ATTRIBUTE.eq(GeneralApprovalAttribute.ABNORMAL_PUNCH.getCode()));

        List<EhPunchExceptionRequestsRecord> resultRecord = step.where(condition)
                .orderBy(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ID.desc()).fetch()
                .map(new EhPunchExceptionRequestMapper());

        List<PunchExceptionRequest> result = resultRecord.stream().map((r) -> {
            return ConvertHelper.convert(r, PunchExceptionRequest.class);
        }).collect(Collectors.toList());
        if (result == null || result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public PunchExceptionRequest findPunchExceptionRequest(Long userId, Long enterpriseId, Date punchDate, Byte status) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select(Tables.EH_PUNCH_EXCEPTION_REQUESTS.fields())
                .from(Tables.EH_PUNCH_EXCEPTION_REQUESTS);
        Condition condition = (Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.eq(enterpriseId));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.eq(userId));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.eq(punchDate));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.eq(status));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.APPROVAL_ATTRIBUTE.eq(GeneralApprovalAttribute.ABNORMAL_PUNCH.getCode()));

        List<EhPunchExceptionRequestsRecord> resultRecord = step.where(condition)
                .orderBy(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ID.desc()).fetch()
                .map(new EhPunchExceptionRequestMapper());

        List<PunchExceptionRequest> result = resultRecord.stream().map((r) -> {
            return ConvertHelper.convert(r, PunchExceptionRequest.class);
        }).collect(Collectors.toList());
        if (result == null || result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public PunchLog findPunchLog(Long organizationId, Long applyUserId, Date punchDate, Byte punchType, Integer punchIntervalNo) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Record record = context.select().from(Tables.EH_PUNCH_LOGS)
                .where(Tables.EH_PUNCH_LOGS.USER_ID.eq(applyUserId))
                .and(Tables.EH_PUNCH_LOGS.ENTERPRISE_ID.eq(organizationId))
                .and(Tables.EH_PUNCH_LOGS.PUNCH_DATE.eq(punchDate))
                .and(Tables.EH_PUNCH_LOGS.PUNCH_TYPE.eq(punchType))
                .and(Tables.EH_PUNCH_LOGS.PUNCH_INTERVAL_NO.eq(punchIntervalNo))
                .limit(1)
                .fetchOne();

        if (record != null) {
            return ConvertHelper.convert(record, PunchLog.class);
        }
        return null;
    }

    @Override
    public void updatePunchLog(PunchLog punchLog) {

        assert (punchLog.getId() == null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchLogsDao dao = new EhPunchLogsDao(
                context.configuration());
        dao.update(punchLog);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchLogs.class,
                punchLog.getId());

    }

    @Override
    public List<PunchLog> listPunchLogs(Long ownerId, List<Long> userIds, Long startDay, Long endDay) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_LOGS);
        Condition condition = Tables.EH_PUNCH_LOGS.PUNCH_DATE.greaterOrEqual(new Date(startDay));
        Condition condition2 = Tables.EH_PUNCH_LOGS.PUNCH_DATE.lessOrEqual(new Date(endDay));
        Condition condition3 = Tables.EH_PUNCH_LOGS.ENTERPRISE_ID.equal(ownerId);
        Condition condition4 = Tables.EH_PUNCH_LOGS.PUNCH_STATUS.equal(ClockCode.SUCESS.getCode());
        condition = condition.and(condition2);
        condition = condition.and(condition3);
        condition = condition.and(condition4);
        if(null != userIds){
        	Condition condition5 = Tables.EH_PUNCH_LOGS.USER_ID.in(userIds);
            condition = condition.and(condition5);
        }
        step.where(condition);
        List<PunchLog> result = step.orderBy(
                Tables.EH_PUNCH_LOGS.PUNCH_DATE.desc(), Tables.EH_PUNCH_LOGS.USER_ID.asc(),Tables.EH_PUNCH_LOGS.PUNCH_INTERVAL_NO.asc(),
                Tables.EH_PUNCH_LOGS.PUNCH_TYPE.asc(), Tables.EH_PUNCH_LOGS.PUNCH_TIME.asc())
                .fetch().map((r) -> {
                    return ConvertHelper.convert(r, PunchLog.class);
                });
        return result;

    }

    @Override
    public List<PunchLog> listPunchLogsForOpenApi(Long ownerId, List<Long> userIds, Long startDay, Long endDay) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_LOGS);
        Condition condition = Tables.EH_PUNCH_LOGS.PUNCH_DATE.between(new Date(startDay), new Date(endDay))
        		.or(Tables.EH_PUNCH_LOGS.UPDATE_DATE.between(new Date(startDay), new Date(endDay))); 
        Condition condition3 = Tables.EH_PUNCH_LOGS.ENTERPRISE_ID.equal(ownerId);
        Condition condition4 = Tables.EH_PUNCH_LOGS.PUNCH_STATUS.equal(ClockCode.SUCESS.getCode()); 
        condition = condition.and(condition3);
        condition = condition.and(condition4);
        if(null != userIds){
        	Condition condition5 = Tables.EH_PUNCH_LOGS.USER_ID.in(userIds);
            condition = condition.and(condition5);
        }
        step.where(condition);
        List<PunchLog> result = step.orderBy(
                Tables.EH_PUNCH_LOGS.PUNCH_DATE.desc(), Tables.EH_PUNCH_LOGS.USER_ID.asc(),Tables.EH_PUNCH_LOGS.PUNCH_INTERVAL_NO.asc(),
                Tables.EH_PUNCH_LOGS.PUNCH_TYPE.asc(), Tables.EH_PUNCH_LOGS.PUNCH_TIME.asc())
                .fetch().map((r) -> {
                    return ConvertHelper.convert(r, PunchLog.class);
                });
        return result;

    }

    @Override
    public void deletePunchLogs(Long ownerId, Date monthBegin, Date monthEnd){
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	context.delete(Tables.EH_PUNCH_LOG_FILES)
    	.where(Tables.EH_PUNCH_LOG_FILES.ENTERPRISE_ID.eq(ownerId)
    					.and(Tables.EH_PUNCH_LOG_FILES.PUNCH_DATE.between(monthBegin, monthEnd))).execute();
    }

    @Override
    public void filePunchLogs(Long ownerId, Date monthBegin, Date monthEnd, PunchMonthReport report){
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	
    	context.insertInto(Tables.EH_PUNCH_LOG_FILES,
    			Tables.EH_PUNCH_LOG_FILES.ID,
    			Tables.EH_PUNCH_LOG_FILES.USER_ID,
    			Tables.EH_PUNCH_LOG_FILES.ENTERPRISE_ID,
    			Tables.EH_PUNCH_LOG_FILES.LONGITUDE,
    			Tables.EH_PUNCH_LOG_FILES.LATITUDE,
    			Tables.EH_PUNCH_LOG_FILES.PUNCH_DATE,
    			Tables.EH_PUNCH_LOG_FILES.PUNCH_TIME,
    			Tables.EH_PUNCH_LOG_FILES.PUNCH_STATUS,
    			Tables.EH_PUNCH_LOG_FILES.IDENTIFICATION,
    			Tables.EH_PUNCH_LOG_FILES.PUNCH_TYPE,
    			Tables.EH_PUNCH_LOG_FILES.PUNCH_INTERVAL_NO,
    			Tables.EH_PUNCH_LOG_FILES.RULE_TIME,
    			Tables.EH_PUNCH_LOG_FILES.STATUS,
    			Tables.EH_PUNCH_LOG_FILES.APPROVAL_STATUS,
    			Tables.EH_PUNCH_LOG_FILES.SMART_ALIGNMENT,
    			Tables.EH_PUNCH_LOG_FILES.WIFI_INFO,
                Tables.EH_PUNCH_LOG_FILES.LOCATION_INFO,
                Tables.EH_PUNCH_LOG_FILES.DEVICE_CHANGE_FLAG,
    			Tables.EH_PUNCH_LOG_FILES.SHOULD_PUNCH_TIME)
    	.select(context.select(Tables.EH_PUNCH_LOGS.ID,
    			Tables.EH_PUNCH_LOGS.USER_ID,
    			Tables.EH_PUNCH_LOGS.ENTERPRISE_ID,
    			Tables.EH_PUNCH_LOGS.LONGITUDE,
    			Tables.EH_PUNCH_LOGS.LATITUDE,
    			Tables.EH_PUNCH_LOGS.PUNCH_DATE,
    			Tables.EH_PUNCH_LOGS.PUNCH_TIME,
    			Tables.EH_PUNCH_LOGS.PUNCH_STATUS,
    			Tables.EH_PUNCH_LOGS.IDENTIFICATION,
    			Tables.EH_PUNCH_LOGS.PUNCH_TYPE,
    			Tables.EH_PUNCH_LOGS.PUNCH_INTERVAL_NO,
    			Tables.EH_PUNCH_LOGS.RULE_TIME,
    			Tables.EH_PUNCH_LOGS.STATUS,
    			Tables.EH_PUNCH_LOGS.APPROVAL_STATUS,
    			Tables.EH_PUNCH_LOGS.SMART_ALIGNMENT,
    			Tables.EH_PUNCH_LOGS.WIFI_INFO,
                Tables.EH_PUNCH_LOGS.LOCATION_INFO,
                Tables.EH_PUNCH_LOGS.DEVICE_CHANGE_FLAG,
                Tables.EH_PUNCH_LOGS.SHOULD_PUNCH_TIME).from(Tables.EH_PUNCH_LOGS)
    			.where(Tables.EH_PUNCH_LOGS.ENTERPRISE_ID.eq(ownerId)
    					.and(Tables.EH_PUNCH_LOGS.PUNCH_DATE.between(monthBegin, monthEnd))))
    					.execute();
    	
    	context.update(Tables.EH_PUNCH_LOG_FILES).set(Tables.EH_PUNCH_LOG_FILES.FILE_TIME,report.getFileTime())
    	.set(Tables.EH_PUNCH_LOG_FILES.FILER_NAME,report.getFilerName())
    	.set(Tables.EH_PUNCH_LOG_FILES.FILER_UID,report.getFilerUid())
    	.set(Tables.EH_PUNCH_LOG_FILES.MONTH_REPORT_ID,report.getId())
    	.where(Tables.EH_PUNCH_LOG_FILES.ENTERPRISE_ID.eq(ownerId)
    					.and(Tables.EH_PUNCH_LOG_FILES.PUNCH_DATE.between(monthBegin, monthEnd))).execute();
    }

	@Override
	public void deletePunchDayLogs(Long ownerId, Date monthBegin, Date monthEnd) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	context.delete(Tables.EH_PUNCH_DAY_LOG_FILES)
    	.where(Tables.EH_PUNCH_DAY_LOG_FILES.ENTERPRISE_ID.eq(ownerId)
    					.and(Tables.EH_PUNCH_DAY_LOG_FILES.PUNCH_DATE.between(monthBegin, monthEnd))).execute();
	}

	@Override
	public void filePunchDayLogs(Long ownerId, Date monthBegin, Date monthEnd,
			PunchMonthReport report) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	
    	context.insertInto(Tables.EH_PUNCH_DAY_LOG_FILES,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.ID,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.USER_ID,
                Tables.EH_PUNCH_DAY_LOG_FILES.DETAIL_ID,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.ENTERPRISE_ID,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.PUNCH_DATE,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.ARRIVE_TIME,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.LEAVE_TIME,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.WORK_TIME,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.STATUS,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.CREATOR_UID,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.CREATE_TIME,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.VIEW_FLAG,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.MORNING_STATUS,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.AFTERNOON_STATUS,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.PUNCH_TIMES_PER_DAY,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.NOON_LEAVE_TIME,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.AFTERNOON_ARRIVE_TIME,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.EXCEPTION_STATUS,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.DEVICE_CHANGE_FLAG,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.STATUS_LIST,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.PUNCH_COUNT,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.PUNCH_ORGANIZATION_ID,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.RULE_TYPE,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.TIME_RULE_NAME,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.TIME_RULE_ID,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.APPROVAL_STATUS_LIST,
                Tables.EH_PUNCH_DAY_LOG_FILES.BELATE_TIME_TOTAL,
                Tables.EH_PUNCH_DAY_LOG_FILES.LEAVE_EARLY_TIME_TOTAL,
                Tables.EH_PUNCH_DAY_LOG_FILES.OVERTIME_TOTAL_WORKDAY,
                Tables.EH_PUNCH_DAY_LOG_FILES.OVERTIME_TOTAL_RESTDAY,
                Tables.EH_PUNCH_DAY_LOG_FILES.OVERTIME_TOTAL_LEGAL_HOLIDAY,
    		    Tables.EH_PUNCH_DAY_LOG_FILES.SMART_ALIGNMENT,
                Tables.EH_PUNCH_DAY_LOG_FILES.REST_FLAG,
                Tables.EH_PUNCH_DAY_LOG_FILES.ABSENT_FLAG,
                Tables.EH_PUNCH_DAY_LOG_FILES.NORMAL_FLAG,
                Tables.EH_PUNCH_DAY_LOG_FILES.BELATE_COUNT,
                Tables.EH_PUNCH_DAY_LOG_FILES.LEAVE_EARLY_COUNT,
                Tables.EH_PUNCH_DAY_LOG_FILES.FORGOT_PUNCH_COUNT_ON_DUTY,
                Tables.EH_PUNCH_DAY_LOG_FILES.FORGOT_PUNCH_COUNT_OFF_DUTY,
                Tables.EH_PUNCH_DAY_LOG_FILES.ASK_FOR_LEAVE_REQUEST_COUNT,
                Tables.EH_PUNCH_DAY_LOG_FILES.GO_OUT_REQUEST_COUNT,
                Tables.EH_PUNCH_DAY_LOG_FILES.BUSINESS_TRIP_REQUEST_COUNT,
                Tables.EH_PUNCH_DAY_LOG_FILES.OVERTIME_REQUEST_COUNT,
                Tables.EH_PUNCH_DAY_LOG_FILES.PUNCH_EXCEPTION_REQUEST_COUNT,
                Tables.EH_PUNCH_DAY_LOG_FILES.DEPT_ID)
    	.select(context.select(Tables.EH_PUNCH_DAY_LOGS.ID,
    		    Tables.EH_PUNCH_DAY_LOGS.USER_ID,
                Tables.EH_PUNCH_DAY_LOGS.DETAIL_ID,
    		    Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID,
    		    Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE,
    		    Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME,
    		    Tables.EH_PUNCH_DAY_LOGS.LEAVE_TIME,
    		    Tables.EH_PUNCH_DAY_LOGS.WORK_TIME,
    		    Tables.EH_PUNCH_DAY_LOGS.STATUS,
    		    Tables.EH_PUNCH_DAY_LOGS.CREATOR_UID,
    		    Tables.EH_PUNCH_DAY_LOGS.CREATE_TIME,
    		    Tables.EH_PUNCH_DAY_LOGS.VIEW_FLAG,
    		    Tables.EH_PUNCH_DAY_LOGS.MORNING_STATUS,
    		    Tables.EH_PUNCH_DAY_LOGS.AFTERNOON_STATUS,
    		    Tables.EH_PUNCH_DAY_LOGS.PUNCH_TIMES_PER_DAY,
    		    Tables.EH_PUNCH_DAY_LOGS.NOON_LEAVE_TIME,
    		    Tables.EH_PUNCH_DAY_LOGS.AFTERNOON_ARRIVE_TIME,
    		    Tables.EH_PUNCH_DAY_LOGS.EXCEPTION_STATUS,
    		    Tables.EH_PUNCH_DAY_LOGS.DEVICE_CHANGE_FLAG,
    		    Tables.EH_PUNCH_DAY_LOGS.STATUS_LIST,
    		    Tables.EH_PUNCH_DAY_LOGS.PUNCH_COUNT,
    		    Tables.EH_PUNCH_DAY_LOGS.PUNCH_ORGANIZATION_ID,
    		    Tables.EH_PUNCH_DAY_LOGS.RULE_TYPE,
    		    Tables.EH_PUNCH_DAY_LOGS.TIME_RULE_NAME,
    		    Tables.EH_PUNCH_DAY_LOGS.TIME_RULE_ID,
    		    Tables.EH_PUNCH_DAY_LOGS.APPROVAL_STATUS_LIST,
                Tables.EH_PUNCH_DAY_LOGS.BELATE_TIME_TOTAL,
                Tables.EH_PUNCH_DAY_LOGS.LEAVE_EARLY_TIME_TOTAL,
                Tables.EH_PUNCH_DAY_LOGS.OVERTIME_TOTAL_WORKDAY,
                Tables.EH_PUNCH_DAY_LOGS.OVERTIME_TOTAL_RESTDAY,
                Tables.EH_PUNCH_DAY_LOGS.OVERTIME_TOTAL_LEGAL_HOLIDAY,
                Tables.EH_PUNCH_DAY_LOGS.SMART_ALIGNMENT,
                Tables.EH_PUNCH_DAY_LOGS.REST_FLAG,
                Tables.EH_PUNCH_DAY_LOGS.ABSENT_FLAG,
                Tables.EH_PUNCH_DAY_LOGS.NORMAL_FLAG,
                Tables.EH_PUNCH_DAY_LOGS.BELATE_COUNT,
                Tables.EH_PUNCH_DAY_LOGS.LEAVE_EARLY_COUNT,
                Tables.EH_PUNCH_DAY_LOGS.FORGOT_PUNCH_COUNT_ON_DUTY,
                Tables.EH_PUNCH_DAY_LOGS.FORGOT_PUNCH_COUNT_OFF_DUTY,
                Tables.EH_PUNCH_DAY_LOGS.ASK_FOR_LEAVE_REQUEST_COUNT,
                Tables.EH_PUNCH_DAY_LOGS.GO_OUT_REQUEST_COUNT,
                Tables.EH_PUNCH_DAY_LOGS.BUSINESS_TRIP_REQUEST_COUNT,
                Tables.EH_PUNCH_DAY_LOGS.OVERTIME_REQUEST_COUNT,
                Tables.EH_PUNCH_DAY_LOGS.PUNCH_EXCEPTION_REQUEST_COUNT,
                Tables.EH_PUNCH_DAY_LOGS.DEPT_ID)
    		    .from(Tables.EH_PUNCH_DAY_LOGS)
    			.where(Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.eq(ownerId)
    					.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.between(monthBegin, monthEnd))))
    					.execute();
    	
    	context.update(Tables.EH_PUNCH_DAY_LOG_FILES).set(Tables.EH_PUNCH_DAY_LOG_FILES.FILE_TIME,report.getFileTime())
    	.set(Tables.EH_PUNCH_DAY_LOG_FILES.FILER_NAME,report.getFilerName())
    	.set(Tables.EH_PUNCH_DAY_LOG_FILES.FILER_UID,report.getFilerUid())
    	.set(Tables.EH_PUNCH_DAY_LOG_FILES.MONTH_REPORT_ID,report.getId())
    	.where(Tables.EH_PUNCH_DAY_LOG_FILES.ENTERPRISE_ID.eq(ownerId)
    					.and(Tables.EH_PUNCH_DAY_LOG_FILES.PUNCH_DATE.between(monthBegin, monthEnd))).execute();
	}

	@Override
	public void deletePunchDayLogs(Long ownerId, String punchMonth) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	context.delete(Tables.EH_PUNCH_STATISTIC_FILES)
    	.where(Tables.EH_PUNCH_STATISTIC_FILES.OWNER_ID.eq(ownerId)
    					.and(Tables.EH_PUNCH_STATISTIC_FILES.PUNCH_MONTH.eq(punchMonth))).execute();
	}

	@Override
	public void filePunchDayLogs(Long ownerId, String punchMonth, PunchMonthReport report) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	
    	context.insertInto(Tables.EH_PUNCH_STATISTIC_FILES,Tables.EH_PUNCH_STATISTIC_FILES.ID,
    		    Tables.EH_PUNCH_STATISTIC_FILES.PUNCH_MONTH,
    		    Tables.EH_PUNCH_STATISTIC_FILES.OWNER_TYPE,
    		    Tables.EH_PUNCH_STATISTIC_FILES.OWNER_ID,
    		    Tables.EH_PUNCH_STATISTIC_FILES.USER_ID,
    		    Tables.EH_PUNCH_STATISTIC_FILES.USER_NAME,
    		    Tables.EH_PUNCH_STATISTIC_FILES.DEPT_ID,
    		    Tables.EH_PUNCH_STATISTIC_FILES.DEPT_NAME,
    		    Tables.EH_PUNCH_STATISTIC_FILES.WORK_DAY_COUNT,
    		    Tables.EH_PUNCH_STATISTIC_FILES.WORK_COUNT,
    		    Tables.EH_PUNCH_STATISTIC_FILES.BELATE_COUNT,
    		    Tables.EH_PUNCH_STATISTIC_FILES.LEAVE_EARLY_COUNT,
    		    Tables.EH_PUNCH_STATISTIC_FILES.BLANDLE_COUNT,
    		    Tables.EH_PUNCH_STATISTIC_FILES.UNPUNCH_COUNT,
    		    Tables.EH_PUNCH_STATISTIC_FILES.ABSENCE_COUNT,
    		    Tables.EH_PUNCH_STATISTIC_FILES.SICK_COUNT,
    		    Tables.EH_PUNCH_STATISTIC_FILES.EXCHANGE_COUNT,
    		    Tables.EH_PUNCH_STATISTIC_FILES.OUTWORK_COUNT,
    		    Tables.EH_PUNCH_STATISTIC_FILES.OVER_TIME_SUM,
    		    Tables.EH_PUNCH_STATISTIC_FILES.PUNCH_TIMES_PER_DAY,
    		    Tables.EH_PUNCH_STATISTIC_FILES.EXCEPTION_STATUS,
    		    Tables.EH_PUNCH_STATISTIC_FILES.DESCRIPTION,
    		    Tables.EH_PUNCH_STATISTIC_FILES.CREATOR_UID,
    		    Tables.EH_PUNCH_STATISTIC_FILES.CREATE_TIME,
    		    Tables.EH_PUNCH_STATISTIC_FILES.EXTS,
    		    Tables.EH_PUNCH_STATISTIC_FILES.USER_STATUS,
    		    Tables.EH_PUNCH_STATISTIC_FILES.PUNCH_ORG_NAME,
    		    Tables.EH_PUNCH_STATISTIC_FILES.DETAIL_ID,
    		    Tables.EH_PUNCH_STATISTIC_FILES.EXCEPTION_DAY_COUNT,
    		    Tables.EH_PUNCH_STATISTIC_FILES.ANNUAL_LEAVE_BALANCE,
    		    Tables.EH_PUNCH_STATISTIC_FILES.OVERTIME_COMPENSATION_BALANCE,
    		    Tables.EH_PUNCH_STATISTIC_FILES.DEVICE_CHANGE_COUNTS,
    		    Tables.EH_PUNCH_STATISTIC_FILES.EXCEPTION_REQUEST_COUNTS,
    		    Tables.EH_PUNCH_STATISTIC_FILES.BELATE_TIME,
                Tables.EH_PUNCH_STATISTIC_FILES.LEAVE_EARLY_TIME,
                Tables.EH_PUNCH_STATISTIC_FILES.FORGOT_PUNCH_COUNT_ON_DUTY,
                Tables.EH_PUNCH_STATISTIC_FILES.FORGOT_PUNCH_COUNT_OFF_DUTY,
                Tables.EH_PUNCH_STATISTIC_FILES.OVERTIME_TOTAL_WORKDAY,
                Tables.EH_PUNCH_STATISTIC_FILES.OVERTIME_TOTAL_RESTDAY,
                Tables.EH_PUNCH_STATISTIC_FILES.OVERTIME_TOTAL_LEGAL_HOLIDAY,
                Tables.EH_PUNCH_STATISTIC_FILES.REST_DAY_COUNT,
                Tables.EH_PUNCH_STATISTIC_FILES.FULL_NORMAL_FLAG,
                Tables.EH_PUNCH_STATISTIC_FILES.ASK_FOR_LEAVE_REQUEST_COUNT,
                Tables.EH_PUNCH_STATISTIC_FILES.GO_OUT_REQUEST_COUNT,
                Tables.EH_PUNCH_STATISTIC_FILES.BUSINESS_TRIP_REQUEST_COUNT,
                Tables.EH_PUNCH_STATISTIC_FILES.OVERTIME_REQUEST_COUNT,
                Tables.EH_PUNCH_STATISTIC_FILES.PUNCH_EXCEPTION_REQUEST_COUNT,
    		    Tables.EH_PUNCH_STATISTIC_FILES.STATUS_LIST )
    	.select(context.select(Tables.EH_PUNCH_STATISTICS.ID,
    		    Tables.EH_PUNCH_STATISTICS.PUNCH_MONTH,
    		    Tables.EH_PUNCH_STATISTICS.OWNER_TYPE,
    		    Tables.EH_PUNCH_STATISTICS.OWNER_ID,
    		    Tables.EH_PUNCH_STATISTICS.USER_ID,
    		    Tables.EH_PUNCH_STATISTICS.USER_NAME,
    		    Tables.EH_PUNCH_STATISTICS.DEPT_ID,
    		    Tables.EH_PUNCH_STATISTICS.DEPT_NAME,
    		    Tables.EH_PUNCH_STATISTICS.WORK_DAY_COUNT,
    		    Tables.EH_PUNCH_STATISTICS.WORK_COUNT,
    		    Tables.EH_PUNCH_STATISTICS.BELATE_COUNT,
    		    Tables.EH_PUNCH_STATISTICS.LEAVE_EARLY_COUNT,
    		    Tables.EH_PUNCH_STATISTICS.BLANDLE_COUNT,
    		    Tables.EH_PUNCH_STATISTICS.UNPUNCH_COUNT,
    		    Tables.EH_PUNCH_STATISTICS.ABSENCE_COUNT,
    		    Tables.EH_PUNCH_STATISTICS.SICK_COUNT,
    		    Tables.EH_PUNCH_STATISTICS.EXCHANGE_COUNT,
    		    Tables.EH_PUNCH_STATISTICS.OUTWORK_COUNT,
    		    Tables.EH_PUNCH_STATISTICS.OVER_TIME_SUM,
    		    Tables.EH_PUNCH_STATISTICS.PUNCH_TIMES_PER_DAY,
    		    Tables.EH_PUNCH_STATISTICS.EXCEPTION_STATUS,
    		    Tables.EH_PUNCH_STATISTICS.DESCRIPTION,
    		    Tables.EH_PUNCH_STATISTICS.CREATOR_UID,
    		    Tables.EH_PUNCH_STATISTICS.CREATE_TIME,
    		    Tables.EH_PUNCH_STATISTICS.EXTS,
    		    Tables.EH_PUNCH_STATISTICS.USER_STATUS,
    		    Tables.EH_PUNCH_STATISTICS.PUNCH_ORG_NAME,
    		    Tables.EH_PUNCH_STATISTICS.DETAIL_ID,
    		    Tables.EH_PUNCH_STATISTICS.EXCEPTION_DAY_COUNT,
    		    Tables.EH_PUNCH_STATISTICS.ANNUAL_LEAVE_BALANCE,
    		    Tables.EH_PUNCH_STATISTICS.OVERTIME_COMPENSATION_BALANCE,
    		    Tables.EH_PUNCH_STATISTICS.DEVICE_CHANGE_COUNTS,
    		    Tables.EH_PUNCH_STATISTICS.EXCEPTION_REQUEST_COUNTS,
    		    Tables.EH_PUNCH_STATISTICS.BELATE_TIME,
    		    Tables.EH_PUNCH_STATISTICS.LEAVE_EARLY_TIME,
    		    Tables.EH_PUNCH_STATISTICS.FORGOT_PUNCH_COUNT_ON_DUTY,
                Tables.EH_PUNCH_STATISTICS.FORGOT_PUNCH_COUNT_OFF_DUTY,
                Tables.EH_PUNCH_STATISTICS.OVERTIME_TOTAL_WORKDAY,
                Tables.EH_PUNCH_STATISTICS.OVERTIME_TOTAL_RESTDAY,
                Tables.EH_PUNCH_STATISTICS.OVERTIME_TOTAL_LEGAL_HOLIDAY,
                Tables.EH_PUNCH_STATISTICS.REST_DAY_COUNT,
                Tables.EH_PUNCH_STATISTICS.FULL_NORMAL_FLAG,
                Tables.EH_PUNCH_STATISTICS.ASK_FOR_LEAVE_REQUEST_COUNT,
                Tables.EH_PUNCH_STATISTICS.GO_OUT_REQUEST_COUNT,
                Tables.EH_PUNCH_STATISTICS.BUSINESS_TRIP_REQUEST_COUNT,
                Tables.EH_PUNCH_STATISTICS.OVERTIME_REQUEST_COUNT,
                Tables.EH_PUNCH_STATISTICS.PUNCH_EXCEPTION_REQUEST_COUNT,
    		    Tables.EH_PUNCH_STATISTICS.STATUS_LIST).from(Tables.EH_PUNCH_STATISTICS)
    			.where(Tables.EH_PUNCH_STATISTICS.OWNER_ID.eq(ownerId)
				.and(Tables.EH_PUNCH_STATISTICS.PUNCH_MONTH.eq(punchMonth))))
    					.execute();
    	
    	context.update(Tables.EH_PUNCH_STATISTIC_FILES).set(Tables.EH_PUNCH_STATISTIC_FILES.FILE_TIME,report.getFileTime())
    	.set(Tables.EH_PUNCH_STATISTIC_FILES.FILER_NAME,report.getFilerName())
    	.set(Tables.EH_PUNCH_STATISTIC_FILES.MONTH_REPORT_ID,report.getId())
    	.set(Tables.EH_PUNCH_STATISTIC_FILES.FILER_UID,report.getFilerUid())
    	.where(Tables.EH_PUNCH_STATISTIC_FILES.OWNER_ID.eq(ownerId))
				.and(Tables.EH_PUNCH_STATISTIC_FILES.PUNCH_MONTH.eq(punchMonth)).execute();
		
	}

	@Override
	public PunchLog findLastPunchLog(Long userId, Long enterpriseId, Timestamp punchTime) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
    	Record record = context.select().from(Tables.EH_PUNCH_LOGS)
    	.where(Tables.EH_PUNCH_LOGS.USER_ID.eq(userId))
		.and(Tables.EH_PUNCH_LOGS.ENTERPRISE_ID.eq(enterpriseId))
		.and(Tables.EH_PUNCH_LOGS.PUNCH_TIME.lt(punchTime))
		.orderBy(Tables.EH_PUNCH_LOGS.PUNCH_TIME.desc())
		.limit(1).fetchAny();
    	if(null == record)
    		return null;
    	
		return record.map(r->{ return ConvertHelper.convert(r, PunchLog.class);});
	}

    @Override
    public PunchOvertimeRule getPunchOvertimeRuleById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhPunchOvertimeRulesRecord> query = context.selectQuery(Tables.EH_PUNCH_OVERTIME_RULES);
        query.addConditions(Tables.EH_PUNCH_OVERTIME_RULES.ID.eq(id));
        query.addConditions(Tables.EH_PUNCH_OVERTIME_RULES.STATUS.ne((byte) 0));

        EhPunchOvertimeRulesRecord record = query.fetchOne();
        return ConvertHelper.convert(record, PunchOvertimeRule.class);
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindPunchOvertimeRulesByPunchRuleId", allEntries = true)})
    public Long createPunchOvertimeRule(PunchOvertimeRule punchOvertimeRule) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchOvertimeRules.class);
        long id = sequenceProvider.getNextSequence(key);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPunchOvertimeRules.class));
        punchOvertimeRule.setId(id);
        punchOvertimeRule.setCreateTime(DateUtils.currentTimestamp());
        punchOvertimeRule.setCreatorUid(UserContext.currentUserId());
        EhPunchOvertimeRulesDao dao = new EhPunchOvertimeRulesDao(context.configuration());
        dao.insert(punchOvertimeRule);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPunchOvertimeRules.class, null);
        return id;
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindPunchOvertimeRulesByPunchRuleId", allEntries = true)})
    public Long updatePunchOvertimeRule(PunchOvertimeRule punchOvertimeRule) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPunchOvertimeRules.class));
        punchOvertimeRule.setUpdateTime(DateUtils.currentTimestamp());
        punchOvertimeRule.setOperatorUid(UserContext.currentUserId());
        EhPunchOvertimeRulesDao dao = new EhPunchOvertimeRulesDao(context.configuration());
        dao.update(punchOvertimeRule);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchOvertimeRules.class, punchOvertimeRule.getId());
        return punchOvertimeRule.getId();
    }

    @Override
    @Cacheable(value = "FindPunchOvertimeRulesByPunchRuleId", key = "{#punchRuleId,#status}")
    public List<PunchOvertimeRule> findPunchOvertimeRulesByPunchRuleId(Long punchRuleId, Byte status) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhPunchOvertimeRulesRecord> query = context.selectQuery(Tables.EH_PUNCH_OVERTIME_RULES);
        query.addConditions(Tables.EH_PUNCH_OVERTIME_RULES.PUNCH_RULE_ID.eq(punchRuleId));
        if (status != null) {
            query.addConditions(Tables.EH_PUNCH_OVERTIME_RULES.STATUS.eq(status));
        }

        Result<EhPunchOvertimeRulesRecord> result = query.fetch();

        if (result == null || result.isEmpty()) {
            return Collections.emptyList();
        }

        return result.map((r) -> {
            return ConvertHelper.convert(r, PunchOvertimeRule.class);
        });
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindPunchOvertimeRulesByPunchRuleId", allEntries = true)})
    public void deletePunchOvertimeRulesByPunchRuleId(Long punchRuleId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPunchOvertimeRules.class));
        DeleteQuery<EhPunchOvertimeRulesRecord> deleteQuery = context.deleteQuery(Tables.EH_PUNCH_OVERTIME_RULES);
        deleteQuery.addConditions(Tables.EH_PUNCH_OVERTIME_RULES.PUNCH_RULE_ID.eq(punchRuleId));
        deleteQuery.execute();
    }

	@Override
	public List<PunchDayLog> listPunchDayLogsByItemTypeAndDeptIds(Long orgId, List<Long> deptIds,
		String startDay, String endDay, PunchStatusStatisticsItemType itemType, Integer pageOffset, Integer pageSize) {
	 	DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());  
        SelectJoinStep<Record> step =context.select().from(Tables.EH_PUNCH_DAY_LOGS); 
        Condition condition = (Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.eq(orgId));
        if (deptIds != null)
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.DEPT_ID.in(deptIds)); 
        if (!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
            Date startDate = Date.valueOf(startDay);
            Date endDate = Date.valueOf(endDay);
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.between(startDate).and(endDate));
        }
        condition = getPDLStatusItemTypeCondition(itemType, condition);

        Integer offset = pageOffset == null ? 1 : (pageOffset - 1) * pageSize;
        if (null != pageOffset && null != pageSize)
            step.limit(offset, pageSize);
        List<PunchDayLog> result = new ArrayList<PunchDayLog>();
        step.where(condition)
                .orderBy(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.desc(),Tables.EH_PUNCH_DAY_LOGS.PUNCH_ORGANIZATION_ID.asc(), Tables.EH_PUNCH_DAY_LOGS.USER_ID.desc()).fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, PunchDayLog.class));
            return null;
        });
        if (result == null || result.isEmpty())
            return null;
        return result;
	}

    private Condition getPDLStatusItemTypeCondition(PunchStatusStatisticsItemType itemType, Condition condition) {
        if(itemType != null){
        	switch(itemType){
        		case BELATE:
        			condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.BELATE_COUNT.gt(0));
        			break;
        		case LEAVE_EARLY:
        			condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.LEAVE_EARLY_COUNT.gt(0));
        			break;
        		case NORMAL:
        			condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.NORMAL_FLAG.eq(com.everhomes.rest.techpark.punch.NormalFlag.YES.getCode()));
        			break;
        		case REST:
        			condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.REST_FLAG.eq(com.everhomes.rest.techpark.punch.NormalFlag.YES.getCode()));
        			break;
                case UN_ARRIVED:
                    condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.TIME_RULE_ID.gt(0L).and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_COUNT.eq(0)));
                    break;
        		case ABSENT:
        			condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ABSENT_FLAG.eq(com.everhomes.rest.techpark.punch.NormalFlag.YES.getCode()))
        				.and(Tables.EH_PUNCH_DAY_LOGS.SPLIT_DATE_TIME.lt(new Timestamp(DateHelper.currentGMTTime().getTime())));
        			break;
        		case CHECKING:
        			condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ABSENT_FLAG.eq(com.everhomes.rest.techpark.punch.NormalFlag.YES.getCode()))
        				.and(Tables.EH_PUNCH_DAY_LOGS.SPLIT_DATE_TIME.gt(new Timestamp(DateHelper.currentGMTTime().getTime())));
        			break;
                case FORGOT_PUNCH:
                    condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.FORGOT_PUNCH_COUNT_ON_DUTY.gt(0).or(Tables.EH_PUNCH_DAY_LOGS.FORGOT_PUNCH_COUNT_OFF_DUTY.gt(0)));
                    break;
                case ARRIVED:
                    condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.TIME_RULE_ID.gt(0L).and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_COUNT.gt(0)));
                    break;
                case GO_OUT:
                    condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.GO_OUT_PUNCH_FLAG.eq(com.everhomes.rest.techpark.punch.NormalFlag.YES.getCode()));
                    break;
    			default:
    				break;
        	}
        }
        return condition;
    }

    public List<PunchStatistic> listPunchSatisticsByItemTypeAndDeptIds(Long organizationId, List<Long> deptIds, String queryByMonth,
                                PunchStatusStatisticsItemType itemType, Integer pageOffset, int pageSize){
		 	DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());  
	        SelectJoinStep<Record> step =context.select().from(Tables.EH_PUNCH_STATISTICS); 
	        Condition condition = (Tables.EH_PUNCH_STATISTICS.OWNER_ID.eq(organizationId));
	        if (deptIds != null)
	            condition = condition.and(Tables.EH_PUNCH_STATISTICS.DEPT_ID.in(deptIds)); 
	        if (!StringUtils.isEmpty(queryByMonth)) { 
	            condition = condition.and(Tables.EH_PUNCH_STATISTICS.PUNCH_MONTH.eq(queryByMonth));
	        }
	        if(itemType != null){
	        	switch(itemType){ 
	        		case BELATE:
	        			condition = condition.and(Tables.EH_PUNCH_STATISTICS.BELATE_COUNT.gt(0));
	        			break;
	        		case LEAVE_EARLY:
	        			condition = condition.and(Tables.EH_PUNCH_STATISTICS.LEAVE_EARLY_COUNT.gt(0));
	        			break;
	        		case NORMAL:
	        			condition = condition.and(Tables.EH_PUNCH_STATISTICS.FULL_NORMAL_FLAG.eq(NormalFlag.NEED.getCode()));
	        			break;
	        		case REST:
	        			condition = condition.and(Tables.EH_PUNCH_STATISTICS.REST_DAY_COUNT.gt(0));
	        			break;
	        		case ABSENT:
	        			condition = condition.and(Tables.EH_PUNCH_STATISTICS.ABSENCE_COUNT.gt(0.0));
	        			break;
                    case FORGOT_PUNCH:
                        condition = condition.and(Tables.EH_PUNCH_STATISTICS.FORGOT_PUNCH_COUNT_ON_DUTY.gt(0).or(Tables.EH_PUNCH_STATISTICS.FORGOT_PUNCH_COUNT_OFF_DUTY.gt(0)));
                        break; 
                    case GO_OUT:
	        			condition = condition.and(Tables.EH_PUNCH_STATISTICS.GO_OUT_PUNCH_DAY_COUNT.gt(0));
                        break;
	    			default:
	    				break;
	        	}
	        }

	        Integer offset = pageOffset == null ? 1 : (pageOffset - 1) * pageSize;
	        step.limit(offset, pageSize);
	        List<PunchStatistic> result = new ArrayList<>();
	        step.where(condition)
	                .orderBy(Tables.EH_PUNCH_STATISTICS.PUNCH_ORG_NAME.asc(), Tables.EH_PUNCH_STATISTICS.USER_ID.asc()).fetch().map((r) -> {
	            result.add(ConvertHelper.convert(r, PunchStatistic.class));
	            return null;
	        });
	        if (result == null || result.isEmpty())
	            return null;
	        return result;
	}

    @Override
    public List<OrganizationMemberDetails> listExceptionMembersByDate(Long organizationId, Long departmentId, Date startDate, Date endDate, GeneralApprovalAttribute approvalAttribute, Integer pageOffset, int pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return archivesService.queryArchivesEmployees(new ListingLocator(), organizationId, departmentId, (locator, query) -> {
            if(null != approvalAttribute) {
                SelectConditionStep<Record1<Long>> condition = context.select(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID)
                        .from(Tables.EH_PUNCH_EXCEPTION_REQUESTS).where(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.eq(organizationId))
                        .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.APPROVAL_ATTRIBUTE.eq(approvalAttribute.getCode()));
                switch(approvalAttribute){
                    case ABNORMAL_PUNCH:
                        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.between(startDate, endDate));
                        break;
                    default:
                        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.BEGIN_TIME.lt(new Timestamp(endDate.getTime())))
                                .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.END_TIME.gt(new Timestamp(startDate.getTime())));
                        break;
                }
                query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.TARGET_ID.in(condition));
            }
            query.addOrderBy(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.desc());
            Integer offset = pageOffset == null ? 1 : (pageOffset - 1) * pageSize;
            query.addLimit(offset, pageSize);
            return query;
        });
    }

    @Override
    public MonthlyStatisticsByMemberRecordMapper monthlyStatisticsByMember(Long organizationId, String punchMonth, Long detailId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record18<Long, String, String, Long, Long, Long, Double, Integer, Integer, Integer, Double, Integer, Integer, Integer, Integer, Integer, Integer, Integer>> query = context.select(
                Tables.EH_PUNCH_STATISTICS.ID.as("id"),
                Tables.EH_PUNCH_STATISTICS.PUNCH_MONTH.as("punchMonth"),
                Tables.EH_PUNCH_STATISTICS.OWNER_TYPE.as("ownerType"),
                Tables.EH_PUNCH_STATISTICS.OWNER_ID.as("ownerId"),
                Tables.EH_PUNCH_STATISTICS.USER_ID.as("userId"),
                Tables.EH_PUNCH_STATISTICS.DETAIL_ID.as("detailId"),
                Tables.EH_PUNCH_STATISTICS.WORK_COUNT.as("workCount"),
                Tables.EH_PUNCH_STATISTICS.REST_DAY_COUNT.as("restDayCount"),
                Tables.EH_PUNCH_STATISTICS.BELATE_COUNT.as("belateCount"),
                Tables.EH_PUNCH_STATISTICS.LEAVE_EARLY_COUNT.as("leaveEarlyCount"),
                Tables.EH_PUNCH_STATISTICS.ABSENCE_COUNT.as("absenceCount"),
                Tables.EH_PUNCH_STATISTICS.FORGOT_PUNCH_COUNT_ON_DUTY.add(Tables.EH_PUNCH_STATISTICS.FORGOT_PUNCH_COUNT_OFF_DUTY).as("forgotCount"),
                Tables.EH_PUNCH_STATISTICS.ASK_FOR_LEAVE_REQUEST_COUNT.as("askForLeaveRequestCount"),
                Tables.EH_PUNCH_STATISTICS.GO_OUT_REQUEST_COUNT.as("goOutRequestCount"),
                Tables.EH_PUNCH_STATISTICS.BUSINESS_TRIP_REQUEST_COUNT.as("businessTripRequestCount"),
                Tables.EH_PUNCH_STATISTICS.OVERTIME_REQUEST_COUNT.as("overtimeRequestCount"),
                Tables.EH_PUNCH_STATISTICS.PUNCH_EXCEPTION_REQUEST_COUNT.as("punchExceptionRequestCount"),
                Tables.EH_PUNCH_STATISTICS.GO_OUT_PUNCH_DAY_COUNT.as("goOutPunchDayCount")).from(Tables.EH_PUNCH_STATISTICS);
        Condition condition = Tables.EH_PUNCH_STATISTICS.OWNER_TYPE.eq("organization").and(Tables.EH_PUNCH_STATISTICS.OWNER_ID.eq(organizationId)).and(Tables.EH_PUNCH_STATISTICS.PUNCH_MONTH.eq(punchMonth)).and(Tables.EH_PUNCH_STATISTICS.DETAIL_ID.eq(detailId));
        Record18<Long, String, String, Long, Long, Long, Double, Integer, Integer, Integer, Double, Integer, Integer, Integer, Integer, Integer, Integer, Integer> record = query.where(condition).limit(1).fetchOne();
        if (record == null) {
            return new MonthlyStatisticsByMemberRecordMapper();
        }
        return record.map(new MonthlyStatisticsByMemberRecordMapper());
    }

    @Override
    public MonthlyStatisticsByDepartmentRecordMapper monthlyStatisticsByDepartment(Long organizationId, String statisticsMonth, List<Long> deptIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record11<BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal>> query = context.select(
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.BELATE_COUNT.gt(0), 1).otherwise(0).sum().as("belateMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.LEAVE_EARLY_COUNT.gt(0), 1).otherwise(0).sum().as("leaveEarlyMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.ABSENCE_COUNT.gt(0D), 1).otherwise(0).sum().as("absenceMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.FORGOT_PUNCH_COUNT_ON_DUTY.gt(0).or(Tables.EH_PUNCH_STATISTICS.FORGOT_PUNCH_COUNT_OFF_DUTY.gt(0)), 1).otherwise(0).sum().as("forgotMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.FULL_NORMAL_FLAG.eq((byte) 1), 1).otherwise(0).sum().as("normalMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.ASK_FOR_LEAVE_REQUEST_COUNT.gt(0), 1).otherwise(0).sum().as("askForLeaveRequestMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.GO_OUT_REQUEST_COUNT.gt(0), 1).otherwise(0).sum().as("goOutRequestMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.BUSINESS_TRIP_REQUEST_COUNT.gt(0), 1).otherwise(0).sum().as("businessTripRequestMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.OVERTIME_REQUEST_COUNT.gt(0), 1).otherwise(0).sum().as("overtimeRequestMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.PUNCH_EXCEPTION_REQUEST_COUNT.gt(0), 1).otherwise(0).sum().as("punchExceptionRequestCount"),
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.GO_OUT_PUNCH_DAY_COUNT.gt(0), 1).otherwise(0).sum().as("goOutPunchDayCount")).from(Tables.EH_PUNCH_STATISTICS);
        Condition condition = Tables.EH_PUNCH_STATISTICS.OWNER_TYPE.eq("organization").and(Tables.EH_PUNCH_STATISTICS.OWNER_ID.eq(organizationId)).and(Tables.EH_PUNCH_STATISTICS.PUNCH_MONTH.eq(statisticsMonth)).and(Tables.EH_PUNCH_STATISTICS.DEPT_ID.in(deptIds));
        Record11<BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal> record = query.where(condition).fetchOne();
        if (record == null) {
            return new MonthlyStatisticsByDepartmentRecordMapper();
        }
        return record.map(new MonthlyStatisticsByDepartmentRecordMapper());
    }

    @Override
    public DailyStatisticsByDepartmentBaseRecordMapper dailyStatisticsByDepartment(Long organizationId, Date statisticsDate, List<Long> deptIds, boolean isToday) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record14<BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal>> query = context.select(
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.REST_FLAG.eq((byte) 1), 1).otherwise(0).sum().as("restMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.TIME_RULE_ID.gt(0L), 1).otherwise(0).sum().as("shouldArrivedMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.TIME_RULE_ID.gt(0L).and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_COUNT.gt(0)), 1).otherwise(0).sum().as("actArrivedMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.TIME_RULE_ID.gt(0L).and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_COUNT.eq(0)), 1).otherwise(0).sum().as("unArrivedMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.BELATE_COUNT.gt(0), 1).otherwise(0).sum().as("belateMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.LEAVE_EARLY_COUNT.gt(0), 1).otherwise(0).sum().as("leaveEarlyMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.ABSENT_FLAG.eq((byte) 1), 1).otherwise(0).sum().as("absenceMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.FORGOT_PUNCH_COUNT_ON_DUTY.gt(0).or(Tables.EH_PUNCH_DAY_LOGS.FORGOT_PUNCH_COUNT_OFF_DUTY.gt(0)), 1).otherwise(0).sum().as("forgotPunchMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.ASK_FOR_LEAVE_REQUEST_COUNT.gt(0), 1).otherwise(0).sum().as("askForLeaveRequestMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.GO_OUT_REQUEST_COUNT.gt(0), 1).otherwise(0).sum().as("goOutRequestMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.BUSINESS_TRIP_REQUEST_COUNT.gt(0), 1).otherwise(0).sum().as("businessTripRequestMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.OVERTIME_REQUEST_COUNT.gt(0), 1).otherwise(0).sum().as("overtimeRequestMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.PUNCH_EXCEPTION_REQUEST_COUNT.gt(0), 1).otherwise(0).sum().as("punchExceptionRequestCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.GO_OUT_PUNCH_FLAG.eq((byte) 1), 1).otherwise(0).sum().as("goOutPunchDayCount"))

                .from(Tables.EH_PUNCH_DAY_LOGS);

        Condition condition = Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.eq(organizationId).and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.eq(statisticsDate)).and(Tables.EH_PUNCH_DAY_LOGS.DEPT_ID.in(deptIds));
        Record14<BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal> record = query.where(condition).fetchOne();

        if (record == null) {
            return isToday ? new DailyStatisticsByDepartmentTodayRecordMapper() : new DailyStatisticsByDepartmentHistoryRecordMapper();
        }
        if (isToday) {
            return record.map(new DailyStatisticsByDepartmentTodayRecordMapper());
        } else {
            return record.map(new DailyStatisticsByDepartmentHistoryRecordMapper());
        }
    }

    @Override
    public DailyPunchStatusStatisticsTodayRecordMapper dailyPunchStatusMemberCountsTodayByDepartment(Long organizationId, Date statisticsDate, List<Long> deptIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record6<BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal>> query = context.select(
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.TIME_RULE_ID.gt(0L).and(Tables.EH_PUNCH_DAY_LOGS.ABSENT_FLAG.eq((byte) 1)), 1).otherwise(0).sum().as("unArrivedMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.BELATE_COUNT.gt(0), 1).otherwise(0).sum().as("belateMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.LEAVE_EARLY_COUNT.gt(0), 1).otherwise(0).sum().as("leaveEarlyMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.REST_FLAG.eq((byte) 1), 1).otherwise(0).sum().as("restMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.NORMAL_FLAG.eq((byte) 1), 1).otherwise(0).sum().as("normalMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.GO_OUT_PUNCH_FLAG.eq((byte) 1), 1).otherwise(0).sum().as("goOutPunchDayCount")).from(Tables.EH_PUNCH_DAY_LOGS);
        Condition condition = Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.eq(organizationId).and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.eq(statisticsDate)).and(Tables.EH_PUNCH_DAY_LOGS.DEPT_ID.in(deptIds));
        Record6<BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal> record = query.where(condition).fetchOne();
        if (record == null) {
            return new DailyPunchStatusStatisticsTodayRecordMapper();
        }
        return record.map(new DailyPunchStatusStatisticsTodayRecordMapper());
    }

    @Override
    public DailyPunchStatusStatisticsHistoryRecordMapper dailyPunchStatusMemberCountsHistoryByDepartment(Long organizationId, Date statisticsDate, List<Long> deptIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record8<BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal>> query = context.select(
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.REST_FLAG.eq((byte) 1), 1).otherwise(0).sum().as("restMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.NORMAL_FLAG.eq((byte) 1), 1).otherwise(0).sum().as("normalMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.BELATE_COUNT.gt(0), 1).otherwise(0).sum().as("belateMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.LEAVE_EARLY_COUNT.gt(0), 1).otherwise(0).sum().as("leaveEarlyMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.ABSENT_FLAG.eq((byte) 1), 1).otherwise(0).sum().as("absenceMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.ABSENT_FLAG.eq((byte) 1).and(Tables.EH_PUNCH_DAY_LOGS.SPLIT_DATE_TIME.gt(new Timestamp(System.currentTimeMillis()))), 1).otherwise(0).sum().as("checkingMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.FORGOT_PUNCH_COUNT_ON_DUTY.gt(0).or(Tables.EH_PUNCH_DAY_LOGS.FORGOT_PUNCH_COUNT_OFF_DUTY.gt(0)), 1).otherwise(0).sum().as("forgotPunchMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.GO_OUT_PUNCH_FLAG.eq((byte) 1), 1).otherwise(0).sum().as("goOutPunchDayCount")).from(Tables.EH_PUNCH_DAY_LOGS);
        Condition condition = Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.eq(organizationId).and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.eq(statisticsDate)).and(Tables.EH_PUNCH_DAY_LOGS.DEPT_ID.in(deptIds));
        Record8<BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal> record = query.where(condition).fetchOne();
        if (record == null) {
            return new DailyPunchStatusStatisticsHistoryRecordMapper();
        }
        return record.map(new DailyPunchStatusStatisticsHistoryRecordMapper());
    }

    @Override
    public MonthlyPunchStatusStatisticsRecordMapper monthlyPunchStatusMemberCountsByDepartment(Long organizationId, String statisticsMonth, List<Long> deptIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record6<BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal>> query = context.select(
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.BELATE_COUNT.gt(0), 1).otherwise(0).sum().as("belateMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.LEAVE_EARLY_COUNT.gt(0), 1).otherwise(0).sum().as("leaveEarlyMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.ABSENCE_COUNT.gt(0D), 1).otherwise(0).sum().as("absenceMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.GO_OUT_PUNCH_DAY_COUNT.gt(0), 1).otherwise(0).sum().as("goOutPunchDayCount"),
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.FORGOT_PUNCH_COUNT_ON_DUTY.gt(0).or(Tables.EH_PUNCH_STATISTICS.FORGOT_PUNCH_COUNT_OFF_DUTY.gt(0)), 1).otherwise(0).sum().as("forgotPunchMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.FULL_NORMAL_FLAG.eq((byte) 1), 1).otherwise(0).sum().as("normalMemberCount")
        ).from(Tables.EH_PUNCH_STATISTICS);
        Condition condition = Tables.EH_PUNCH_STATISTICS.OWNER_TYPE.eq("organization").and(Tables.EH_PUNCH_STATISTICS.OWNER_ID.eq(organizationId)).and(Tables.EH_PUNCH_STATISTICS.PUNCH_MONTH.eq(statisticsMonth)).and(Tables.EH_PUNCH_STATISTICS.DEPT_ID.in(deptIds));
        Record6<BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal> record = query.where(condition).fetchOne();
        if (record == null) {
            return new MonthlyPunchStatusStatisticsRecordMapper();
        }
        return record.map(new MonthlyPunchStatusStatisticsRecordMapper());
    }

    @Override
    public PunchExceptionRequestStatisticsRecordMapper dailyPunchExceptionRequestMemberCountsByDepartment(Long organizationId, Date statisticsDate, List<Long> deptIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record5<BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal>> query = context.select(
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.ASK_FOR_LEAVE_REQUEST_COUNT.gt(0), 1).otherwise(0).sum().as("askForLeaveRequestMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.GO_OUT_REQUEST_COUNT.gt(0), 1).otherwise(0).sum().as("goOutRequestMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.BUSINESS_TRIP_REQUEST_COUNT.gt(0), 1).otherwise(0).sum().as("businessTripRequestMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.OVERTIME_REQUEST_COUNT.gt(0), 1).otherwise(0).sum().as("overtimeRequestMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_DAY_LOGS.PUNCH_EXCEPTION_REQUEST_COUNT.gt(0), 1).otherwise(0).sum().as("punchExceptionRequestCount")).from(Tables.EH_PUNCH_DAY_LOGS);
        Condition condition = Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.eq(organizationId).and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.eq(statisticsDate)).and(Tables.EH_PUNCH_DAY_LOGS.DEPT_ID.in(deptIds));
        Record5<BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal> record=query.where(condition).fetchOne();
        if (record == null) {
            return new PunchExceptionRequestStatisticsRecordMapper();
        }
        return record.map(new PunchExceptionRequestStatisticsRecordMapper());
    }

    @Override
    public PunchExceptionRequestStatisticsRecordMapper monthlyPunchExceptionRequestMemberCountsByDepartment(Long organizationId, String statisticsMonth, List<Long> deptIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record5<BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal>> query = context.select(
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.ASK_FOR_LEAVE_REQUEST_COUNT.gt(0), 1).otherwise(0).sum().as("askForLeaveRequestMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.GO_OUT_REQUEST_COUNT.gt(0), 1).otherwise(0).sum().as("goOutRequestMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.BUSINESS_TRIP_REQUEST_COUNT.gt(0), 1).otherwise(0).sum().as("businessTripRequestMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.OVERTIME_REQUEST_COUNT.gt(0), 1).otherwise(0).sum().as("overtimeRequestMemberCount"),
                DSL.decode().when(Tables.EH_PUNCH_STATISTICS.PUNCH_EXCEPTION_REQUEST_COUNT.gt(0), 1).otherwise(0).sum().as("punchExceptionRequestCount")).from(Tables.EH_PUNCH_STATISTICS);
        Condition condition = Tables.EH_PUNCH_STATISTICS.OWNER_TYPE.eq("organization").and(Tables.EH_PUNCH_STATISTICS.OWNER_ID.eq(organizationId)).and(Tables.EH_PUNCH_STATISTICS.PUNCH_MONTH.eq(statisticsMonth)).and(Tables.EH_PUNCH_STATISTICS.DEPT_ID.in(deptIds));
        Record5<BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal> record = query.where(condition).fetchOne();
        if (record == null) {
            return new PunchExceptionRequestStatisticsRecordMapper();
        }
        return record.map(new PunchExceptionRequestStatisticsRecordMapper());
    }

    @Override
    public List<PunchDayLog> listPunchDayLogsByApprovalAttributeAndDeptIds(Long organizationId, List<Long> deptIds,
                                                                           Date queryDate, PunchExceptionRequestStatisticsItemType itemType, Integer pageOffset, int pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step =context.select(Tables.EH_PUNCH_DAY_LOGS.fields()).from(Tables.EH_PUNCH_DAY_LOGS)
        		.leftOuterJoin(Tables.EH_UNIONGROUP_MEMBER_DETAILS).on(Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID.eq(Tables.EH_PUNCH_DAY_LOGS.DETAIL_ID))
        		.and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.VERSION_CODE.eq(0));
        Condition condition = (Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.eq(organizationId));
        if (deptIds != null)
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.DEPT_ID.in(deptIds));
        condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.eq(queryDate));

        if(itemType != null){
            switch(itemType){
                case ASK_FOR_LEAVE:
                    condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ASK_FOR_LEAVE_REQUEST_COUNT.gt(0));
                    break;
                case GO_OUT:
                    condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.GO_OUT_REQUEST_COUNT.gt(0));
                    break;
                case BUSINESS_TRIP:
                    condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.BUSINESS_TRIP_REQUEST_COUNT.gt(0));
                    break;
                case OVERTIME:
                    condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.OVERTIME_REQUEST_COUNT.gt(0));
                    break;
                case PUNCH_EXCEPTION:
                    condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_EXCEPTION_REQUEST_COUNT.gt(0));
                    break;
                default:
                    break;
            }
        }

        Integer offset = pageOffset == null ? 1 : (pageOffset - 1) * pageSize;
        step.limit(offset, pageSize);
        List<PunchDayLog> result = step.where(condition)
                .orderBy(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID.asc(), Tables.EH_PUNCH_DAY_LOGS.DEPT_ID.asc(), Tables.EH_PUNCH_DAY_LOGS.USER_ID.desc()).fetch().map(new PunchDayLogRecordMapper());
         
        if (result == null || result.isEmpty())
            return null;
        return result;
    }

    @Override
    public List<PunchStatistic> listPunchSatisticsByExceptionItemTypeAndDeptIds(Long organizationId, List<Long> deptIds, String queryByMonth, PunchExceptionRequestStatisticsItemType itemType, Integer pageOffset, int pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step =context.select(Tables.EH_PUNCH_STATISTICS.fields()).from(Tables.EH_PUNCH_STATISTICS)
        		.leftOuterJoin(Tables.EH_UNIONGROUP_MEMBER_DETAILS).on(Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID.eq(Tables.EH_PUNCH_STATISTICS.DETAIL_ID))
        		.and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.VERSION_CODE.eq(0));
        Condition condition = (Tables.EH_PUNCH_STATISTICS.OWNER_ID.eq(organizationId));
        if (deptIds != null)
            condition = condition.and(Tables.EH_PUNCH_STATISTICS.DEPT_ID.in(deptIds));
        if (!StringUtils.isEmpty(queryByMonth)) {
            condition = condition.and(Tables.EH_PUNCH_STATISTICS.PUNCH_MONTH.eq(queryByMonth));
        }
        if(itemType != null){
            switch(itemType){
                case ASK_FOR_LEAVE:
                    condition = condition.and(Tables.EH_PUNCH_STATISTICS.ASK_FOR_LEAVE_REQUEST_COUNT.gt(0));
                    break;
                case GO_OUT:
                    condition = condition.and(Tables.EH_PUNCH_STATISTICS.GO_OUT_REQUEST_COUNT.gt(0));
                    break;
                case BUSINESS_TRIP:
                    condition = condition.and(Tables.EH_PUNCH_STATISTICS.BUSINESS_TRIP_REQUEST_COUNT.gt(0));
                    break;
                case OVERTIME:
                    condition = condition.and(Tables.EH_PUNCH_STATISTICS.OVERTIME_REQUEST_COUNT.gt(0));
                    break;
                case PUNCH_EXCEPTION:
                    condition = condition.and(Tables.EH_PUNCH_STATISTICS.PUNCH_EXCEPTION_REQUEST_COUNT.gt(0));
                    break;
                default:
                    break;
            }
        }

        Integer offset = pageOffset == null ? 1 : (pageOffset - 1) * pageSize;
        step.limit(offset, pageSize);
         List<PunchStatistic> result = step.where(condition)
                .orderBy(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID.asc(), Tables.EH_PUNCH_STATISTICS.DEPT_ID.desc()).fetch().map(new PunchStatisticRecordMapper());
        if (result == null || result.isEmpty())
            return null;
        return result;
    }

    @Override
    public List<PunchDayLog> listPunchDayLogsByItemTypeAndUserId(Long organizationId, Long userId, Date startDay, Date endDay, PunchStatusStatisticsItemType itemType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step =context.select(Tables.EH_PUNCH_DAY_LOGS.fields()).from(Tables.EH_PUNCH_DAY_LOGS) 
        		.leftOuterJoin(Tables.EH_UNIONGROUP_MEMBER_DETAILS).on(Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID.eq(Tables.EH_PUNCH_DAY_LOGS.DETAIL_ID))
        		.and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.VERSION_CODE.eq(0));
        Condition condition = (Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.eq(organizationId));
        if (userId != null)
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.USER_ID.eq(userId));
        if (null != startDay && null != endDay) {
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.between(startDay).and(endDay));
        }
        condition = getPDLStatusItemTypeCondition(itemType, condition);

        List<PunchDayLog> result = step.where(condition)
                .orderBy(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.desc(), Tables.EH_PUNCH_DAY_LOGS.PUNCH_ORGANIZATION_ID.asc(), Tables.EH_PUNCH_DAY_LOGS.USER_ID.asc()).fetch().map(new PunchDayLogRecordMapper());
        if (result == null || result.isEmpty())
            return null;
        return result;
    }

    @Override
    public List<PunchExceptionRequest> listExceptionRequestsByItemTypeAndDate(Long userId, Long organizationId, Date startDate, Date endDate, GeneralApprovalAttribute approvalAttribute) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select(Tables.EH_PUNCH_EXCEPTION_REQUESTS.fields())
                .from(Tables.EH_PUNCH_EXCEPTION_REQUESTS);
        Condition condition = (Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.eq(organizationId));
        switch(approvalAttribute){
            case ABNORMAL_PUNCH:
                condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.between(startDate, endDate));
                break;
            default:
                condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.BEGIN_TIME.lessOrEqual(new Timestamp(endDate.getTime())))
                        .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.END_TIME.greaterOrEqual(new Timestamp(startDate.getTime())));
                break;
        }
        if(null != userId)
            condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.eq(userId));
        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.ne(ApprovalStatus.REJECTION.getCode()));

        condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.APPROVAL_ATTRIBUTE.eq(approvalAttribute.getCode()));

        List<EhPunchExceptionRequestsRecord> resultRecord = step.where(condition)
                .orderBy(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ID.desc()).fetch()
                .map(new EhPunchExceptionRequestMapper());

        List<PunchExceptionRequest> result = resultRecord.stream().map((r) -> {
            return ConvertHelper.convert(r, PunchExceptionRequest.class);
        }).collect(Collectors.toList());
        if (result == null || result.size() == 0) {
            return new ArrayList<>();
        }
        return new ArrayList<>(result);
    }

    @Override
    public void setPunchTimeRuleStatus(Long prId, Byte targetStatus) {

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPunchOvertimeRules.class));

        UpdateConditionStep<EhPunchTimeRulesRecord> step = context.update(Tables.EH_PUNCH_TIME_RULES)
                .set(Tables.EH_PUNCH_TIME_RULES.STATUS, targetStatus).where(Tables.EH_PUNCH_TIME_RULES.PUNCH_RULE_ID.eq(prId))
                .and(Tables.EH_PUNCH_TIME_RULES.STATUS.eq(PunchRuleStatus.ACTIVE.getCode()));
        step.execute();
    }

    @Override
    public void setPunchSchedulingsStatus(Long prId, Byte targetStatus, Date beginDate) {

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPunchOvertimeRules.class));

        UpdateConditionStep<EhPunchSchedulingsRecord> step = context.update(Tables.EH_PUNCH_SCHEDULINGS)
                .set(Tables.EH_PUNCH_SCHEDULINGS.STATUS, targetStatus).where(Tables.EH_PUNCH_SCHEDULINGS.PUNCH_RULE_ID.eq(prId))
                .and(Tables.EH_PUNCH_SCHEDULINGS.STATUS.eq(PunchRuleStatus.ACTIVE.getCode()))
                .and(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.greaterOrEqual(beginDate));
        step.execute();

    }

	@Override
	public Integer countDeviceChanges(Date theFirstDate, Date theLastDate, Long userId, Long ownerId) { 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		
		SelectJoinStep<Record1<Integer>> step = context.selectCount().from(Tables.EH_PUNCH_LOGS); 
		Condition condition = (Tables.EH_PUNCH_LOGS.ENTERPRISE_ID.equal(ownerId));
		condition = condition.and(Tables.EH_PUNCH_LOGS.USER_ID.eq(userId)); 
		condition = condition.and(Tables.EH_PUNCH_LOGS.PUNCH_DATE.between(theFirstDate).and(theLastDate));  
		condition = condition.and(Tables.EH_PUNCH_LOGS.DEVICE_CHANGE_FLAG.eq(NormalFlag.NEED.getCode())); 
		return step.where(condition).fetchOneInto(Integer.class); 
	}

    @Override
    public void batchCreatePunchNotifications(List<EhPunchNotifications> punchNotifications) {
        if (CollectionUtils.isEmpty(punchNotifications)) {
            return;
        }
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhPunchNotifications.class), punchNotifications.size());
        for (EhPunchNotifications notification : punchNotifications) {
            notification.setId(id++);
            notification.setCreateTime(now);
        }

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhPunchNotifications.class));
        EhPunchNotificationsDao dao = new EhPunchNotificationsDao(context.configuration());
        dao.insert(punchNotifications);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPunchNotifications.class, null);
    }

    @Override
    public int countPunchNotifications(Integer namespaceId, Long organizationId, Long punchRuleId, Date punchDate) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record1<Integer>> query = context.selectCount().from(Tables.EH_PUNCH_NOTIFICATIONS).where(
                Tables.EH_PUNCH_NOTIFICATIONS.NAMESPACE_ID.eq(namespaceId)
                        .and(Tables.EH_PUNCH_NOTIFICATIONS.ENTERPRISE_ID.eq(organizationId))
                        .and(Tables.EH_PUNCH_NOTIFICATIONS.PUNCH_RULE_ID.eq(punchRuleId))
                        .and(Tables.EH_PUNCH_NOTIFICATIONS.PUNCH_DATE.eq(punchDate)));
        Record1<Integer> result = query.fetchOne();
        if (result == null || result.value1() == null) {
            return 0;
        }
        return result.value1();
    }

    @Override
    public List<PunchNotification> findPunchNotificationList(QueryPunchNotificationCondition condition) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record> query = context.select().from(Tables.EH_PUNCH_NOTIFICATIONS).where(
                Tables.EH_PUNCH_NOTIFICATIONS.NAMESPACE_ID.eq(condition.getNamespaceId())
                        .and(Tables.EH_PUNCH_NOTIFICATIONS.ENTERPRISE_ID.eq(condition.getOrganizationId()))
                        .and(Tables.EH_PUNCH_NOTIFICATIONS.PUNCH_RULE_ID.eq(condition.getPunchRuleId()))
                        .and(Tables.EH_PUNCH_NOTIFICATIONS.PUNCH_DATE.eq(condition.getPunchDate()))
                        .and(Tables.EH_PUNCH_NOTIFICATIONS.PUNCH_TYPE.eq(condition.getPunchType().getCode()))
                        .and(Tables.EH_PUNCH_NOTIFICATIONS.EXCEPT_REMIND_TIME.gt(condition.getRemindTimeBetweenFrom()))
                        .and(Tables.EH_PUNCH_NOTIFICATIONS.EXCEPT_REMIND_TIME.le(condition.getRemindTimeBetweenTo()))
                        .and(Tables.EH_PUNCH_NOTIFICATIONS.INVALID_FLAG.eq((byte) 0))
                        .and(Tables.EH_PUNCH_NOTIFICATIONS.PUNCH_INTERVAL_NO.eq(condition.getPunchIntervalNo())));
        Result<Record> result = query.fetch();
        if (result == null || result.size() == 0) {
            return new ArrayList<>();
        }
        return result.map(n -> {
            return ConvertHelper.convert(n, PunchNotification.class);
        });
    }

    @Override
    public int invalidPunchNotificationList(QueryPunchNotificationCondition condition) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhPunchNotifications.class));
        UpdateQuery<EhPunchNotificationsRecord> updateQuery = context.updateQuery(Tables.EH_PUNCH_NOTIFICATIONS);
        updateQuery.addConditions(Tables.EH_PUNCH_NOTIFICATIONS.NAMESPACE_ID.eq(condition.getNamespaceId()));
        updateQuery.addConditions(Tables.EH_PUNCH_NOTIFICATIONS.ENTERPRISE_ID.eq(condition.getOrganizationId()));
        updateQuery.addConditions(Tables.EH_PUNCH_NOTIFICATIONS.PUNCH_RULE_ID.eq(condition.getPunchRuleId()));
        updateQuery.addConditions(Tables.EH_PUNCH_NOTIFICATIONS.PUNCH_DATE.eq(condition.getPunchDate()));
        updateQuery.addConditions(Tables.EH_PUNCH_NOTIFICATIONS.PUNCH_TYPE.eq(condition.getPunchType().getCode()));
        updateQuery.addConditions(Tables.EH_PUNCH_NOTIFICATIONS.EXCEPT_REMIND_TIME.gt(condition.getRemindTimeBetweenFrom()));
        updateQuery.addConditions(Tables.EH_PUNCH_NOTIFICATIONS.EXCEPT_REMIND_TIME.le(condition.getRemindTimeBetweenTo()));
        updateQuery.addConditions(Tables.EH_PUNCH_NOTIFICATIONS.PUNCH_INTERVAL_NO.eq(condition.getPunchIntervalNo()));
        updateQuery.addConditions(Tables.EH_PUNCH_NOTIFICATIONS.INVALID_FLAG.eq((byte) 0));
        updateQuery.addValue(Tables.EH_PUNCH_NOTIFICATIONS.UPDATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()));
        updateQuery.addValue(Tables.EH_PUNCH_NOTIFICATIONS.ACT_REMIND_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()));
        updateQuery.addValue(Tables.EH_PUNCH_NOTIFICATIONS.INVALID_FLAG, (byte) 1);

        return updateQuery.execute();
    }

    @Override
    public PunchNotification findPunchNotification(Integer namespaceId, Long organizationId, Long detailId, Date punchDate, PunchType punchType, Integer punchIntervalNo) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record> query = context.select().from(Tables.EH_PUNCH_NOTIFICATIONS).where(
                Tables.EH_PUNCH_NOTIFICATIONS.NAMESPACE_ID.eq(namespaceId)
                        .and(Tables.EH_PUNCH_NOTIFICATIONS.ENTERPRISE_ID.eq(organizationId))
                        .and(Tables.EH_PUNCH_NOTIFICATIONS.DETAIL_ID.eq(detailId))
                        .and(Tables.EH_PUNCH_NOTIFICATIONS.PUNCH_DATE.eq(punchDate))
                        .and(Tables.EH_PUNCH_NOTIFICATIONS.PUNCH_TYPE.eq(punchType.getCode()))
                        .and(Tables.EH_PUNCH_NOTIFICATIONS.PUNCH_INTERVAL_NO.eq(punchIntervalNo)));
        Record record = query.limit(1).fetchOne();
        if (record == null) {
            return null;
        }
        return ConvertHelper.convert(record, PunchNotification.class);
    }

    @Override
    public List<PunchNotification> findPunchNotificationList(Integer namespaceId, Long organizationId, Long userId, Date punchDate) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record> query = context.select().from(Tables.EH_PUNCH_NOTIFICATIONS).where(
                Tables.EH_PUNCH_NOTIFICATIONS.NAMESPACE_ID.eq(namespaceId)
                        .and(Tables.EH_PUNCH_NOTIFICATIONS.ENTERPRISE_ID.eq(organizationId))
                        .and(Tables.EH_PUNCH_NOTIFICATIONS.USER_ID.eq(userId))
                        .and(Tables.EH_PUNCH_NOTIFICATIONS.PUNCH_DATE.eq(punchDate))
                        .and(Tables.EH_PUNCH_NOTIFICATIONS.INVALID_FLAG.eq((byte) 0)));
        Result<Record> result = query.fetch();
        if (result == null || result.size() == 0) {
            return new ArrayList<>();
        }
        return result.map(n -> {
            return ConvertHelper.convert(n, PunchNotification.class);
        });
    }

    @Override
    public void updatePunchNotification(PunchNotification punchNotification) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhPunchNotifications.class));
        EhPunchNotificationsDao dao = new EhPunchNotificationsDao(context.configuration());
        punchNotification.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(punchNotification);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchNotifications.class, punchNotification.getId());
    }

    @Override
    public int deleteAllPunchNotificationsBeforeDate(Date beforePunchDate) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhPunchNotifications.class));
        DeleteQuery<EhPunchNotificationsRecord> deleteQuery = context.deleteQuery(Tables.EH_PUNCH_NOTIFICATIONS);
        deleteQuery.addConditions(Tables.EH_PUNCH_NOTIFICATIONS.PUNCH_DATE.lt(beforePunchDate));
        return deleteQuery.execute();
    }

	@Override
	public Integer countPunchSatisticsByItemTypeAndDeptIds(Long organizationId, List<Long> deptIds,
			String queryByMonth) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());  
        SelectJoinStep<Record> step =context.select().from(Tables.EH_PUNCH_STATISTICS); 
        Condition condition = (Tables.EH_PUNCH_STATISTICS.OWNER_ID.eq(organizationId));
        if (deptIds != null)
            condition = condition.and(Tables.EH_PUNCH_STATISTICS.DEPT_ID.in(deptIds)); 
        if (!StringUtils.isEmpty(queryByMonth)) { 
            condition = condition.and(Tables.EH_PUNCH_STATISTICS.PUNCH_MONTH.eq(queryByMonth));
        }
        return step.where(condition).fetchCount();
	}

	@Override
	public Integer countPunchDayLogsByItemTypeAndDeptIds(Long organizationId, List<Long> deptIds,
			java.util.Date queryDate) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());  
		SelectJoinStep<Record> step =context.select().from(Tables.EH_PUNCH_DAY_LOGS); 
        Condition condition = (Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.eq(organizationId));
        if (deptIds != null)
            condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.DEPT_ID.in(deptIds));  
        condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.eq(new java.sql.Date(queryDate.getTime()))); 
        return step.where(condition).fetchCount();
	}

    @Override
    public void createPUnchGoOutLog(PunchGoOutLog log) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchGoOutLogs.class);
        long id = sequenceProvider.getNextSequence(key);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        log.setId(id);
        EhPunchGoOutLogsDao dao = new EhPunchGoOutLogsDao(context.configuration());
        dao.insert(log);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPunchGoOutLogs.class, null);
    }

    @Override
    public PunchGoOutLog findPunchGoOutLogById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPunchGoOutLogsDao dao = new EhPunchGoOutLogsDao(context.configuration());
        EhPunchGoOutLogs log = dao.findById(id);
        return ConvertHelper.convert(log, PunchGoOutLog.class);
    }

    @Override
    public void updatePunchGoOutLog(PunchGoOutLog log) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPunchGoOutLogsDao dao = new EhPunchGoOutLogsDao(context.configuration());
        dao.update(log);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchGoOutLogs.class, log.getId());

    }

    @Override
    public Byte processGoOutPunchFlag(Date punchDate, Long targetId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        int punchCount = context.select().from(Tables.EH_PUNCH_GO_OUT_LOGS)
                .where(Tables.EH_PUNCH_GO_OUT_LOGS.PUNCH_DATE.eq(punchDate))
                .and(Tables.EH_PUNCH_GO_OUT_LOGS.USER_ID.eq(targetId))
                .fetchCount();
        return punchCount > 0 ? com.everhomes.rest.techpark.punch.NormalFlag.YES.getCode() : com.everhomes.rest.techpark.punch.NormalFlag.NO.getCode();
    }

	@Override
	public List<PunchGoOutLog> listPunchGoOutLogs(Long userId, Long enterpriseId, Date pDate) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_PUNCH_GO_OUT_LOGS);
        Condition condition = Tables.EH_PUNCH_GO_OUT_LOGS.PUNCH_DATE.equal(pDate);
        Condition condition2 = Tables.EH_PUNCH_GO_OUT_LOGS.USER_ID.equal(userId);
        Condition condition3 = Tables.EH_PUNCH_GO_OUT_LOGS.ORGANIZATION_ID.equal(enterpriseId); 
        condition = condition.and(condition2);
        condition = condition.and(condition3); 
        step.where(condition);
        List<PunchGoOutLog> result = step.orderBy(Tables.EH_PUNCH_GO_OUT_LOGS.USER_ID.asc(),
                Tables.EH_PUNCH_GO_OUT_LOGS.PUNCH_DATE.asc())
                .fetch().map((r) -> {
                    return ConvertHelper.convert(r, PunchGoOutLog.class);
                });
        return result;
	}
}

