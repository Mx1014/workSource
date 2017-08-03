package com.everhomes.techpark.punch;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DeleteWhereStep;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Record5;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.SelectHavingStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.ExceptionRequestType;
import com.everhomes.rest.approval.ListTargetType;
import com.everhomes.rest.techpark.punch.DateStatus;
import com.everhomes.rest.techpark.punch.PunchDayLogDTO;
import com.everhomes.rest.techpark.punch.PunchOwnerType;
import com.everhomes.rest.techpark.punch.PunchRquestType;
import com.everhomes.rest.techpark.punch.PunchStatus;
import com.everhomes.rest.techpark.punch.TimeCompareFlag;
import com.everhomes.rest.techpark.punch.UserPunchStatusCount;
import com.everhomes.rest.techpark.punch.ViewFlags;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPunchDayLogsDao;
import com.everhomes.server.schema.tables.daos.EhPunchExceptionApprovalsDao;
import com.everhomes.server.schema.tables.daos.EhPunchExceptionRequestsDao;
import com.everhomes.server.schema.tables.daos.EhPunchGeopointsDao;
import com.everhomes.server.schema.tables.daos.EhPunchHolidaysDao;
import com.everhomes.server.schema.tables.daos.EhPunchLocationRulesDao;
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
import com.everhomes.server.schema.tables.pojos.EhPunchHolidays;
import com.everhomes.server.schema.tables.pojos.EhPunchLocationRules;
import com.everhomes.server.schema.tables.pojos.EhPunchLogs;
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
import com.everhomes.server.schema.tables.records.EhPunchRuleOwnerMapRecord;
import com.everhomes.server.schema.tables.records.EhPunchRulesRecord;
import com.everhomes.server.schema.tables.records.EhPunchSchedulingsRecord;
import com.everhomes.server.schema.tables.records.EhPunchStatisticsRecord;
import com.everhomes.server.schema.tables.records.EhPunchTimeRulesRecord;
import com.everhomes.server.schema.tables.records.EhPunchWifiRulesRecord;
import com.everhomes.server.schema.tables.records.EhPunchWifisRecord;
import com.everhomes.server.schema.tables.records.EhPunchWorkdayRecord;
import com.everhomes.server.schema.tables.records.EhPunchWorkdayRulesRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class PunchProviderImpl implements PunchProvider {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PunchServiceImpl.class);
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

    @Override
    public Long createPunchRule(PunchRule obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchRules.class);
				long id = sequenceProvider.getNextSequence(key);
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhPunchRulesDao dao = new EhPunchRulesDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updatePunchRule(PunchRule obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchRulesDao dao = new EhPunchRulesDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deletePunchRule(PunchRule obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchRulesDao dao = new EhPunchRulesDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public PunchRule getPunchRuleById(Long id) {
        try {
        PunchRule[] result = new PunchRule[1];
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

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
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhPunchRulesRecord> query = context.selectQuery(Tables.EH_PUNCH_RULES);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator != null && locator.getAnchor() != null)  {
            query.addConditions(Tables.EH_PUNCH_RULES.ID.gt(locator.getAnchor()));
            }
 
		query.addOrderBy(Tables.EH_PUNCH_RULES.ID.asc());
        query.addLimit(count);
        List<PunchRule> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, PunchRule.class);
        });

//        if(objs.size() >= count) {
//            locator.setAnchor(objs.get(objs.size() - 1).getId());
//        } else {
//            locator.setAnchor(null);
//        }
        if(null == objs || objs.isEmpty())
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
		List<PunchLog> result = step.orderBy(Tables.EH_PUNCH_LOGS.ID.desc())
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
	public PunchTimeRule getPunchTimeRuleByCompanyId(String ownerType ,Long companyId) {
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
		if(LOGGER.isDebugEnabled()) {
		    LOGGER.debug("line 139 : Create punch log, key=" + key + ", newId=" + id + ", enterpriseId=" + punchLog.getEnterpriseId());
		}

		punchLog.setId(id);
		EhPunchLogsRecord record = ConvertHelper.convert(punchLog,
				EhPunchLogsRecord.class);
		InsertQuery<EhPunchLogsRecord> query = context
				.insertQuery(Tables.EH_PUNCH_LOGS);
		query.setRecord(record);
		query.execute();

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPunchLogs.class, null);

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
	public void updatePunchTimeRule(PunchTimeRule punchTimeRule) {
		assert (punchTimeRule.getId() == null);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPunchTimeRulesDao dao = new EhPunchTimeRulesDao(context.configuration());
		dao.update(punchTimeRule);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchTimeRules.class,
				punchTimeRule.getId());
	}

	@Override
	public PunchTimeRule getPunchTimeRuleById(Long id) { 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPunchTimeRulesDao dao = new EhPunchTimeRulesDao(context.configuration());
		EhPunchTimeRules result = dao.findById(id) ;
		if(null == result)
			return null;
		return ConvertHelper.convert(result, PunchTimeRule.class) ;
	}
	@Override
	public void deletePunchTimeRule(PunchTimeRule punchTimeRule) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPunchTimeRulesDao dao = new EhPunchTimeRulesDao(context.configuration());
		dao.deleteById(punchTimeRule.getId());

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchTimeRules.class,
				punchTimeRule.getId());
	}

	@Override
	public void deletePunchTimeRuleById(Long id) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPunchTimeRulesDao dao = new EhPunchTimeRulesDao(context.configuration());
		dao.deleteById(id);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchTimeRules.class,id);
	}
	@Override
	public void deletePunchTimeRuleByOwnerAndId(String ownerType,Long ownerId,Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhPunchTimeRulesRecord> step = context
				.delete(Tables.EH_PUNCH_TIME_RULES);
		Condition condition = Tables.EH_PUNCH_TIME_RULES.ID.equal(id)
				.and(Tables.EH_PUNCH_TIME_RULES.OWNER_ID.equal(ownerId))
				.and(Tables.EH_PUNCH_TIME_RULES.OWNER_TYPE.equal(ownerType)) ; 
		step.where(condition);
		step.execute();
	}

	@Override
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
	public List<PunchTimeRule> queryPunchTimeRules(String ownerType,Long ownerId,String targetType , Long targetId,String name) {
	    // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
		// DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectQuery<EhPunchTimeRulesRecord> query = context
				.selectQuery(Tables.EH_PUNCH_TIME_RULES);
		if(null != name)
			query.addConditions(Tables.EH_PUNCH_TIME_RULES.NAME.eq(name));
		query.addConditions(Tables.EH_PUNCH_TIME_RULES.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_PUNCH_TIME_RULES.OWNER_TYPE.eq(ownerType));
		if(null != targetType)
			query.addConditions(Tables.EH_PUNCH_TIME_RULES.TARGET_TYPE.eq(targetType));
		if(null != targetId)
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
	public Integer countExceptionRequests(Long userId,List<Long> userIds, Long companyId, String startDay, String endDay, Byte status,
			Byte processCode,Byte requestType) {
	    // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
		// DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_PUNCH_EXCEPTION_REQUESTS);
//		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
//		step.join(Tables.).on(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
	 
		Condition condition = (Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.equal(companyId));
		condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.eq(userId));
		condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.REQUEST_TYPE.eq(requestType));
		if(userIds != null)
			condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.in(userIds));
		if(!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
			Date startDate = Date.valueOf(startDay);
			Date endDate = Date.valueOf(endDay);
			condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.between(startDate).and(endDate));
		}
		if(null!= status && status != 0){
			condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.eq(status));
		}
		if(null!= processCode &&  processCode != 0){
			condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.APPROVAL_STATUS.eq(processCode));
		}
		return step.where(condition).fetchOneInto(Integer.class);
	
	}
	
	@Override
	public List<PunchExceptionRequest> listExceptionRequests(Long userId,List<Long> userIds, Long companyId, String startDay,String endDay,
			Byte status, Byte processCode,Integer pageOffset,Integer pageSize,Byte requestType) {
		// 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
	    // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record>  step = context.select(Tables.EH_PUNCH_EXCEPTION_REQUESTS.fields()).from(Tables.EH_PUNCH_EXCEPTION_REQUESTS);
//		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
//		step.join(Tables.EH_GROUP_CONTACTS).on(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
	 
		Condition condition = (Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.equal(companyId));
		condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.eq(userId));
		condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.REQUEST_TYPE.eq(requestType));

		if(userIds != null)
			condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.in(userIds));
		if(!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
			Date startDate = Date.valueOf(startDay);
			Date endDate = Date.valueOf(endDay);
			condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.between(startDate).and(endDate));
		}
		if(null!= status && status != 0){
			condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.eq(status));
		}
		if(null!= processCode &&  processCode != 0){
			condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.APPROVAL_STATUS.eq(processCode));
		}
		 
		Integer offset = pageOffset == null ? 1 : (pageOffset - 1 ) * pageSize;
		step.limit(offset , pageSize);
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

		SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_PUNCH_EXCEPTION_REQUESTS);
//		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
//		step.join(Tables.EH_GROUP_CONTACTS).on(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
	 
		Condition condition = (Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.equal(companyId));
//		condition = condition.and(Tables.EH_GROUP_CONTACTS.OWNER_TYPE.eq(OwnerType.COMPANY.getCode()).and(Tables.EH_GROUP_CONTACTS.OWNER_ID.eq(companyId)));
		condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.REQUEST_TYPE.eq(requestType));
		if(null!=userIds){
		condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.in(userIds));}
	 
		if(!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
			Date startDate = Date.valueOf(startDay);
			Date endDate = Date.valueOf(endDay);
			condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.between(startDate).and(endDate));
		}
		if(null!= status && status != 0){
			condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.eq(status));
		}
		if(null!= processCode &&  processCode != 0){
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
		SelectJoinStep<Record>  step = context.select(Tables.EH_PUNCH_EXCEPTION_REQUESTS.fields()).from(Tables.EH_PUNCH_EXCEPTION_REQUESTS);
//		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
//		step.join(Tables.EH_GROUP_CONTACTS).on(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
	 
		Condition condition = (Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.equal(companyId));
//		condition = condition.and(Tables.EH_GROUP_CONTACTS.OWNER_TYPE.eq(OwnerType.COMPANY.getCode()).and(Tables.EH_GROUP_CONTACTS.OWNER_ID.eq(companyId)));
		condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.REQUEST_TYPE.eq(requestType));

		if(userIds != null)
			condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.in(userIds));
		if(!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
			Date startDate = Date.valueOf(startDay);
			Date endDate = Date.valueOf(endDay);
			condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.between(startDate).and(endDate));
		}
		if(null!= status && status != 0){
			condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.eq(status));
		}
		if(null!= processCode &&  processCode != 0){
			condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.APPROVAL_STATUS.eq(processCode));
		}
		Integer offset = pageOffset == null ? 1 : (pageOffset - 1 ) * pageSize;
		step.limit(offset , pageSize);
		List<EhPunchExceptionRequestsRecord> resultRecord = step.where(condition)
				.orderBy(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ID.desc()).fetch()
				.map(new EhPunchExceptionRequestMapper());
		
		List<PunchExceptionRequest> result = resultRecord.stream().map((r) -> {
            return ConvertHelper.convert(r, PunchExceptionRequest.class);
        }).collect(Collectors.toList());
		return result;
	}

	@Override
	public PunchExceptionApproval  getExceptionApproval(Long userId, Long companyId,
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
	public PunchDayLog getDayPunchLogByDate(Long userId, Long companyId,
			String format) {

		Date punchDate = Date.valueOf(format);
		// 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
		// DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_PUNCH_DAY_LOGS);
		Condition condition = (Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.equal(companyId));
		condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.USER_ID.eq(userId));
		condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.eq(punchDate));
		step.where(condition);
		List<PunchDayLog> result = step.orderBy(Tables.EH_PUNCH_DAY_LOGS.ID.desc())
				.fetch().map((r) -> {
					return ConvertHelper.convert(r, PunchDayLog.class);
				});
		if (null != result && result.size() > 0)
			return result.get(0);
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
		if(LOGGER.isDebugEnabled()) {
		    LOGGER.debug("line 645 : Create punch day log, key=" + key + ", newId=" + id + ", enterpriseId=" + punchDayLog.getEnterpriseId());
		}
		punchDayLog.setId(id);
		EhPunchDayLogsRecord record = ConvertHelper.convert(punchDayLog, EhPunchDayLogsRecord.class);
		InsertQuery<EhPunchDayLogsRecord> query = context.insertQuery(Tables.EH_PUNCH_DAY_LOGS);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE,EhPunchDayLogs.class, null);
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

		SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_PUNCH_DAY_LOGS);
//		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
//		step.join(Tables.EH_GROUP_CONTACTS).on(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_DAY_LOGS.USER_ID));
	 
		Condition condition = (Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.equal(companyId));
//		condition = condition.and(Tables.EH_GROUP_CONTACTS.OWNER_TYPE.eq(OwnerType.COMPANY.getCode()).and(Tables.EH_GROUP_CONTACTS.OWNER_ID.eq(companyId)));
		if(userIds != null)
			condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.USER_ID.in(userIds));
		if(!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
			Date startDate = Date.valueOf(startDay);
			Date endDate = Date.valueOf(endDay);
			condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.between(startDate).and(endDate));
		}
		if(null!= startTimeCompareFlag &&!StringUtils.isEmpty(startTime)){
			Time arriveTime = Time.valueOf(startTime);
			if(startTimeCompareFlag.equals(0)){
				condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME.lessOrEqual(arriveTime));
			}else{
				condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME.greaterOrEqual(arriveTime));
			}
		}
		if(null!= endTimeCompareFlag &&!StringUtils.isEmpty(endTime)){
			Time leaveTime = Time.valueOf(endTime);
			if(endTimeCompareFlag.equals(0)){
				condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME.lessOrEqual(leaveTime));
			}else{
				condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME.greaterOrEqual(leaveTime));
			}
		}
		if(null!= workTimeCompareFlag &&!StringUtils.isEmpty(workTime)){
			Time sqlWorkTime = Time.valueOf(workTime);
			if(workTimeCompareFlag.equals(0)){
				condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME.lessOrEqual(sqlWorkTime));
			}else{
				condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME.greaterOrEqual(sqlWorkTime));
			}
		}
		if(null!= status && status != 0){
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
			Integer pageSize){
	    // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
		// DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record>  step = context.select().from(Tables.EH_PUNCH_DAY_LOGS);
//		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
//		step.join(Tables.EH_GROUP_CONTACTS).on(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_DAY_LOGS.USER_ID));
	 
		Condition condition = (Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.equal(companyId));
//		condition = condition.and(Tables.EH_GROUP_CONTACTS.OWNER_TYPE.eq(OwnerType.COMPANY.getCode()).and(Tables.EH_GROUP_CONTACTS.OWNER_ID.eq(companyId)));
		if(userIds != null)
			condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.USER_ID.in(userIds));
		if(!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
			Date startDate = Date.valueOf(startDay);
			Date endDate = Date.valueOf(endDay);
			condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.between(startDate).and(endDate));
		}
		if(null!= arriveTimeCompareFlag &&!StringUtils.isEmpty(arriveTime)){
			Time sqlArriveTime = Time.valueOf(arriveTime);
			if(arriveTimeCompareFlag.equals(TimeCompareFlag.LESSOREQUAL.getCode())){
				condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME.lessOrEqual(sqlArriveTime));
			}else{
				condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME.greaterOrEqual(sqlArriveTime));
			}
		}
		if(null!= leaveTimeCompareFlag &&!StringUtils.isEmpty(leaveTime)){
			Time sqlLeaveTime = Time.valueOf(leaveTime);
			if(leaveTimeCompareFlag.equals(TimeCompareFlag.LESSOREQUAL.getCode())){
				condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.LEAVE_TIME.lessOrEqual(sqlLeaveTime));
			}else{
				condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.LEAVE_TIME.greaterOrEqual(sqlLeaveTime));
			}
		}
		if(null!= workTimeCompareFlag &&!StringUtils.isEmpty(workTime)){
			Time sqlWorkTime = Time.valueOf(workTime);
			if(workTimeCompareFlag.equals(TimeCompareFlag.LESSOREQUAL.getCode())){
				condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.WORK_TIME.lessOrEqual(sqlWorkTime));
			}else{
				condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.WORK_TIME.greaterOrEqual(sqlWorkTime));
			}
		}
		if(null!= status && status != 0){
			condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.STATUS.eq(status));
		}
		Integer offset = pageOffset == null ? 0 : (pageOffset - 1 ) * pageSize;
		step.limit(offset , pageSize);
//		List<EhPunchDayLogsRecord> resultRecord = step.where(condition)
//				.orderBy(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.desc(),Tables.EH_PUNCH_DAY_LOGS.USER_ID.desc()).fetch()
//				.map(new EhPunchDayLogMapper());
//		
//		List<PunchDayLog> result = resultRecord.stream().map((r) -> {
//            return ConvertHelper.convert(r, PunchDayLog.class);
//        }).collect(Collectors.toList());
		List<PunchDayLog> result  = new ArrayList<PunchDayLog>();
		step.where(condition)
				.orderBy(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.desc(),Tables.EH_PUNCH_DAY_LOGS.USER_ID.desc()).fetch().map((r) -> {
					result.add( ConvertHelper.convert(r, PunchDayLog.class));
			 return null;
		});
		return result;
	}

	@Override
	public List<PunchDayLog> listPunchDayExceptionLogs(Long userId,
			Long companyId, String startDay, String endDay) {
		// DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record>  step = context.select(Tables.EH_PUNCH_DAY_LOGS.fields()).from(Tables.EH_PUNCH_DAY_LOGS);
//		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
//		step.join(Tables.EH_GROUP_CONTACTS).on(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_DAY_LOGS.USER_ID));
//	 
		Condition condition = (Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.equal(companyId));
		condition= condition.and(Tables.EH_PUNCH_DAY_LOGS.USER_ID.equal(userId));
//		condition = condition.and(Tables.EH_GROUP_CONTACTS.OWNER_TYPE.eq(OwnerType.COMPANY.getCode()).and(Tables.EH_GROUP_CONTACTS.OWNER_ID.eq(companyId)));
		 
		if(!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
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
		condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.VIEW_FLAG.eq(ViewFlags.NOTVIEW.getCode()) );
		 
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
	public List<PunchDayLog> listPunchDayLogs(Long userId,
			Long companyId, String startDay, String endDay) {
		// DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record>  step = context.select().from(Tables.EH_PUNCH_DAY_LOGS);
//		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
//		step.join(Tables.EH_GROUP_CONTACTS).on(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_DAY_LOGS.USER_ID));
//	 
		Condition condition = (Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.equal(companyId));
		condition= condition.and(Tables.EH_PUNCH_DAY_LOGS.USER_ID.equal(userId));
//		condition = condition.and(Tables.EH_GROUP_CONTACTS.OWNER_TYPE.eq(OwnerType.COMPANY.getCode()).and(Tables.EH_GROUP_CONTACTS.OWNER_ID.eq(companyId)));
		 
		if(!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
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
	public List<PunchDayLog> listPunchDayLogsExcludeEndDay(Long userId,
														   Long companyId, String startDay, String endDay) {
		// DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record>  step = context.select().from(Tables.EH_PUNCH_DAY_LOGS);
		Condition condition = (Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.equal(companyId));
		condition= condition.and(Tables.EH_PUNCH_DAY_LOGS.USER_ID.equal(userId));
		if(!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
			Date startDate = Date.valueOf(startDay);
			Date endDate = Date.valueOf(endDay);
			condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.greaterOrEqual(startDate));
			condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.lt(endDate));
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
		SelectJoinStep<Record>  step = context.select(Tables.EH_PUNCH_EXCEPTION_REQUESTS.fields()).from(Tables.EH_PUNCH_EXCEPTION_REQUESTS);
//		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
//		step.join(Tables.EH_GROUP_CONTACTS).on(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
		Condition condition = (Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.equal(companyId));
		condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.eq(userId));
		condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.REQUEST_TYPE.eq(PunchRquestType.APPROVAL.getCode()));
		condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.VIEW_FLAG.eq(ViewFlags.NOTVIEW.getCode()) );
		if(!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
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
	        .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.eq(logDate)).execute() ;

			//update 日志表
	        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
	        // context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(com.everhomes.server.schema.tables.EhPunchDayLogs.class,companyId));
	        context = this.dbProvider.getDslContext(AccessSpec.readWrite());
	        context.update(Tables.EH_PUNCH_DAY_LOGS).set(Tables.EH_PUNCH_DAY_LOGS.VIEW_FLAG, ViewFlags.ISVIEW.getCode())
	        .where(Tables.EH_PUNCH_DAY_LOGS.USER_ID.eq(userId))
	        .and(Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.eq(companyId))
	        .and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.eq(logDate)).execute() ;

			//update 审批表
	        // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
	        // context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(com.everhomes.server.schema.tables.EhPunchExceptionApprovals.class,companyId));
	        context = this.dbProvider.getDslContext(AccessSpec.readWrite());
	        context.update(Tables.EH_PUNCH_EXCEPTION_APPROVALS).set(Tables.EH_PUNCH_EXCEPTION_APPROVALS.VIEW_FLAG, ViewFlags.ISVIEW.getCode())
	        .where(Tables.EH_PUNCH_EXCEPTION_APPROVALS.USER_ID.eq(userId))
	        .and(Tables.EH_PUNCH_EXCEPTION_APPROVALS.ENTERPRISE_ID.eq(companyId))
	        .and(Tables.EH_PUNCH_EXCEPTION_APPROVALS.PUNCH_DATE.eq(logDate)).execute() ;
	}
	
	@Override
	public List<UserPunchStatusCount> listUserStatusPunch(Long companyId,  String startDay,
			String endDay) {
	    // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
		// DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		
		Condition condition = Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.eq(companyId);
		if(!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
			Date startDate = Date.valueOf(startDay);
			Date endDate = Date.valueOf(endDay);
			condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.between(startDate).and(endDate));
		}     
		
		
		SelectHavingStep<Record3<Long, Byte,Integer>> step = context.select(Tables.EH_PUNCH_DAY_LOGS.USER_ID,Tables.EH_PUNCH_DAY_LOGS.STATUS,
				Tables.EH_PUNCH_DAY_LOGS.ID.count())
				.from(Tables.EH_PUNCH_DAY_LOGS)
				.where(condition)
				.groupBy(Tables.EH_PUNCH_DAY_LOGS.STATUS,Tables.EH_PUNCH_DAY_LOGS.USER_ID);

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
	public List<UserPunchStatusCount> listUserApprovalStatusPunch(Long companyId,  String startDay,
			String endDay) {
	    // 在公司与机构合并之前，打卡跟着eh_groups表走，合并之后打卡表为全局表 modify by lqs 20160722
		// DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		
		Condition condition = Tables.EH_PUNCH_EXCEPTION_APPROVALS.ENTERPRISE_ID.eq(companyId);
		if(!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
			Date startDate = Date.valueOf(startDay);
			Date endDate = Date.valueOf(endDay);
			condition = condition.and(Tables.EH_PUNCH_EXCEPTION_APPROVALS.PUNCH_DATE.between(startDate).and(endDate));
		}     
		
		
		SelectHavingStep<Record3<Long, Byte,Integer>> step = context.select(Tables.EH_PUNCH_EXCEPTION_APPROVALS.USER_ID,Tables.EH_PUNCH_EXCEPTION_APPROVALS.APPROVAL_STATUS,
				Tables.EH_PUNCH_EXCEPTION_APPROVALS.ID.count())
				.from(Tables.EH_PUNCH_EXCEPTION_APPROVALS)
				.where(condition)
				.groupBy(Tables.EH_PUNCH_EXCEPTION_APPROVALS.APPROVAL_STATUS,Tables.EH_PUNCH_EXCEPTION_APPROVALS.USER_ID);

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
		SelectJoinStep<Record5<Long, Date, Byte, Byte, Time>>  step = context.select(Tables.EH_PUNCH_DAY_LOGS.USER_ID,Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE,Tables.EH_PUNCH_DAY_LOGS.STATUS,Tables.EH_PUNCH_EXCEPTION_APPROVALS.APPROVAL_STATUS,Tables.EH_PUNCH_DAY_LOGS.WORK_TIME).from(Tables.EH_PUNCH_DAY_LOGS);
//		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
		step.leftOuterJoin(Tables.EH_PUNCH_EXCEPTION_APPROVALS).on(Tables.EH_PUNCH_EXCEPTION_APPROVALS.USER_ID.eq(Tables.EH_PUNCH_DAY_LOGS.USER_ID))
		.and(Tables.EH_PUNCH_EXCEPTION_APPROVALS.ENTERPRISE_ID.eq(Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID)).and(Tables.EH_PUNCH_EXCEPTION_APPROVALS.PUNCH_DATE.eq(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE));
	 
		Condition condition = (Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.equal(companyId));
		if(!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
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
		if(null != ownerType)
			condition = condition.and(Tables.EH_PUNCH_RULES.OWNER_TYPE.eq(ownerType));
		if(null != ownerId)
			condition = condition.and(Tables.EH_PUNCH_RULES.OWNER_ID.eq(ownerId));
		if(null != timeRuleId)
			condition = condition.and(Tables.EH_PUNCH_RULES.TIME_RULE_ID.eq(timeRuleId));
		if(null != locationRuleId)
			condition = condition.and(Tables.EH_PUNCH_RULES.LOCATION_RULE_ID.eq(locationRuleId));
		if(null != wifiRuleId)
			condition = condition.and(Tables.EH_PUNCH_RULES.WIFI_RULE_ID.eq(wifiRuleId));
		if(null != workdayRuleId)
			condition = condition.and(Tables.EH_PUNCH_RULES.WORKDAY_RULE_ID.eq(workdayRuleId));
		query.addConditions(condition);
		List<PunchRule> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, PunchRule.class));
			return null;
		});
		if (null != result && result.size() > 0)
			return result ;
		return null;
	}
	@Override
	public List<PunchGeopoint> listPunchGeopointsByRuleId(String ownerType ,Long ownerId,Long ruleId) {
	     
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
			return result ;
		return null;
	}
	@Override
	public List<PunchTimeRule> queryPunchTimeRuleList(String ownerType, Long ownerId, CrossShardListingLocator locator, int pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectQuery<EhPunchTimeRulesRecord> query = context
				.selectQuery(Tables.EH_PUNCH_TIME_RULES);
		 
		Condition condition = Tables.EH_PUNCH_TIME_RULES.ID.ne(-1L);
		if(null != ownerType)
			condition = condition.and(Tables.EH_PUNCH_TIME_RULES.OWNER_TYPE.eq(ownerType));
		if(null != ownerId)
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
			return result ;
		return null;
	}
	

	@Override
	public List<PunchTimeRule> queryPunchTimeRuleList(  Long startTimeLong, Long endTimeLong ) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectQuery<EhPunchTimeRulesRecord> query = context
				.selectQuery(Tables.EH_PUNCH_TIME_RULES);
		 
		Condition condition = Tables.EH_PUNCH_TIME_RULES.ID.ne(-1L); 
		condition = condition.and(Tables.EH_PUNCH_TIME_RULES.START_LATE_TIME_LONG.between(startTimeLong, endTimeLong) );
		query.addConditions(condition); 
		query.addOrderBy(Tables.EH_PUNCH_TIME_RULES.ID.asc());
		List<PunchTimeRule> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, PunchTimeRule.class));
			return null;
		});
		if (null != result && result.size() > 0)
			return result ;
		return null;
	}
	@Override
	public List<PunchTimeRule> queryPunchTimeRuleList(String ownerType, Long ownerId, String targetType, Long targetId,CrossShardListingLocator locator, int pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectQuery<EhPunchTimeRulesRecord> query = context
				.selectQuery(Tables.EH_PUNCH_TIME_RULES);
		 
		Condition condition = Tables.EH_PUNCH_TIME_RULES.ID.ne(-1L);
		if(null != ownerType)
			condition = condition.and(Tables.EH_PUNCH_TIME_RULES.OWNER_TYPE.eq(ownerType));
		if(null != ownerId)
			condition = condition.and(Tables.EH_PUNCH_TIME_RULES.OWNER_ID.eq(ownerId)); 
		if(null != targetType)
			condition = condition.and(Tables.EH_PUNCH_TIME_RULES.TARGET_TYPE.eq(targetType));
		if(null != targetId)
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
			return result ;
		return null;
	}

    @Override
    public Long createPunchLocationRule(PunchLocationRule obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchLocationRules.class);
		long id = sequenceProvider.getNextSequence(key);
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id); 
        EhPunchLocationRulesDao dao = new EhPunchLocationRulesDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updatePunchLocationRule(PunchLocationRule obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchLocationRulesDao dao = new EhPunchLocationRulesDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deletePunchLocationRule(PunchLocationRule obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchLocationRulesDao dao = new EhPunchLocationRulesDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public PunchLocationRule getPunchLocationRuleById(Long id) {
        try {
        PunchLocationRule[] result = new PunchLocationRule[1];
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

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
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhPunchLocationRulesRecord> query = context.selectQuery(Tables.EH_PUNCH_LOCATION_RULES);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator != null && locator != null && locator.getAnchor() != null) {
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
        if(null == objs || objs.isEmpty())
        	return null;
        return objs;
    }

    @Override
    public Long createPunchWifi(PunchWifi obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchWifis.class);
		long id = sequenceProvider.getNextSequence(key);
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhPunchWifisDao dao = new EhPunchWifisDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updatePunchWifi(PunchWifi obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchWifisDao dao = new EhPunchWifisDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deletePunchWifi(PunchWifi obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchWifisDao dao = new EhPunchWifisDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public PunchWifi getPunchWifiById(Long id) {
        try {
        PunchWifi[] result = new PunchWifi[1];
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

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
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhPunchWifisRecord> query = context.selectQuery(Tables.EH_PUNCH_WIFIS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator != null && locator.getAnchor() != null) {
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
        if(null == objs || objs.isEmpty())
        	return null;
        return objs;
    }

    private void prepareObj(PunchWifi obj) {
    }
    @Override
    public Long createPunchWifiRule(PunchWifiRule obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchWifiRules.class);
		long id = sequenceProvider.getNextSequence(key);
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhPunchWifiRulesDao dao = new EhPunchWifiRulesDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updatePunchWifiRule(PunchWifiRule obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchWifiRulesDao dao = new EhPunchWifiRulesDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deletePunchWifiRule(PunchWifiRule obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchWifiRulesDao dao = new EhPunchWifiRulesDao(context.configuration());
        dao.deleteById(obj.getId());
    }

	@Override
	public void deletePunchWifiRuleByOwnerAndId(String ownerType,Long ownerId,Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhPunchWifiRulesRecord> step = context
				.delete(Tables.EH_PUNCH_WIFI_RULES);
		Condition condition = Tables.EH_PUNCH_WIFI_RULES.ID.equal(id)
				.and(Tables.EH_PUNCH_WIFI_RULES.OWNER_ID.equal(ownerId))
				.and(Tables.EH_PUNCH_WIFI_RULES.OWNER_TYPE.equal(ownerType)) ; 
		step.where(condition);
		step.execute();
	}
    @Override
    public PunchWifiRule getPunchWifiRuleById(Long id) {
        try {
        PunchWifiRule[] result = new PunchWifiRule[1];
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

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
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhPunchWifiRulesRecord> query = context.selectQuery(Tables.EH_PUNCH_WIFI_RULES);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator != null && locator.getAnchor() != null) {
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
        if(null == objs || objs.isEmpty())
        	return null;
        return objs;
    }

    private void prepareObj(PunchWifiRule obj) {
    }

    @Override
    public Long createPunchHoliday(PunchHoliday obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchHolidays.class);
		long id = sequenceProvider.getNextSequence(key);
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhPunchHolidaysDao dao = new EhPunchHolidaysDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updatePunchHoliday(PunchHoliday obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchHolidaysDao dao = new EhPunchHolidaysDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deletePunchHoliday(PunchHoliday obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchHolidaysDao dao = new EhPunchHolidaysDao(context.configuration());
        dao.deleteById(obj.getId());
    }

	@Override
	public void deletePunchHolidayByOwnerAndId(String ownerType,Long ownerId,Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhPunchHolidaysRecord> step = context
				.delete(Tables.EH_PUNCH_HOLIDAYS);
		Condition condition = Tables.EH_PUNCH_HOLIDAYS.ID.equal(id)
				.and(Tables.EH_PUNCH_HOLIDAYS.OWNER_ID.equal(ownerId))
				.and(Tables.EH_PUNCH_HOLIDAYS.OWNER_TYPE.equal(ownerType)) ; 
		step.where(condition);
		step.execute();
	}
    @Override
    public PunchHoliday getPunchHolidayById(Long id) {
        try {
        PunchHoliday[] result = new PunchHoliday[1];
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

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
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhPunchHolidaysRecord> query = context.selectQuery(Tables.EH_PUNCH_HOLIDAYS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator != null && locator.getAnchor() != null) {
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
        if(null == objs || objs.isEmpty())
        	return null;
        return objs;
    }

    private void prepareObj(PunchHoliday obj) {
    }

    @Override
    public Long createPunchWorkdayRule(PunchWorkdayRule obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchWorkdayRules.class);
		long id = sequenceProvider.getNextSequence(key);
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhPunchWorkdayRulesDao dao = new EhPunchWorkdayRulesDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updatePunchWorkdayRule(PunchWorkdayRule obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchWorkdayRulesDao dao = new EhPunchWorkdayRulesDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deletePunchWorkdayRule(PunchWorkdayRule obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchWorkdayRulesDao dao = new EhPunchWorkdayRulesDao(context.configuration());
        dao.deleteById(obj.getId());
    }

	@Override
	public void deletePunchWorkdayRuleByOwnerAndId(String ownerType,Long ownerId,Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhPunchWorkdayRulesRecord> step = context
				.delete(Tables.EH_PUNCH_WORKDAY_RULES);
		Condition condition = Tables.EH_PUNCH_WORKDAY_RULES.ID.equal(id)
				.and(Tables.EH_PUNCH_WORKDAY_RULES.OWNER_ID.equal(ownerId))
				.and(Tables.EH_PUNCH_WORKDAY_RULES.OWNER_TYPE.equal(ownerType)) ; 
		step.where(condition);
		step.execute();
	}
    @Override
    public PunchWorkdayRule getPunchWorkdayRuleById(Long id) {
        try {
        PunchWorkdayRule[] result = new PunchWorkdayRule[1];
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

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
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhPunchWorkdayRulesRecord> query = context.selectQuery(Tables.EH_PUNCH_WORKDAY_RULES);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator != null && locator.getAnchor() != null) {
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
        if(null == objs || objs.isEmpty())
        	return null;
        return objs;
    }

    private void prepareObj(PunchWorkdayRule obj) {
    }

    @Override
    public Long createPunchRuleOwnerMap(PunchRuleOwnerMap obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchRuleOwnerMap.class);
		long id = sequenceProvider.getNextSequence(key);
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhPunchRuleOwnerMapDao dao = new EhPunchRuleOwnerMapDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updatePunchRuleOwnerMap(PunchRuleOwnerMap obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchRuleOwnerMapDao dao = new EhPunchRuleOwnerMapDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deletePunchRuleOwnerMap(PunchRuleOwnerMap obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchRuleOwnerMapDao dao = new EhPunchRuleOwnerMapDao(context.configuration());
        dao.deleteById(obj.getId());
    }

	@Override
	public void deletePunchRuleOwnerMapByOwnerAndId(String ownerType,Long ownerId,Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhPunchRuleOwnerMapRecord> step = context
				.delete(Tables.EH_PUNCH_RULE_OWNER_MAP);
		Condition condition = Tables.EH_PUNCH_RULE_OWNER_MAP.ID.equal(id)
				.and(Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_ID.equal(ownerId))
				.and(Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_TYPE.equal(ownerType)) ; 
		step.where(condition);
		step.execute();
	}
    @Override
    public PunchRuleOwnerMap getPunchRuleOwnerMapByOwnerAndTarget(String ownerType , Long ownerId,String targetType , Long targetId) {
        try {
			//TODO
        PunchRuleOwnerMap[] result = new PunchRuleOwnerMap[1];
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

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
    public PunchRuleOwnerMap getPunchRuleOwnerMapByTarget(String targetType , Long targetId) {
        try {
        PunchRuleOwnerMap[] result = new PunchRuleOwnerMap[1];
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

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
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhPunchRuleOwnerMapRecord> query = context.selectQuery(Tables.EH_PUNCH_RULE_OWNER_MAP);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator != null && locator.getAnchor() != null) {
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
        if(null == objs || objs.isEmpty())
        	return null;
        return objs;
    }
    @Override
    public List<PunchRuleOwnerMap> queryPunchRuleOwnerMaps(String ownerType,Long ownerId,String listType) { 
    	// DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
    			DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

    			SelectQuery<EhPunchRuleOwnerMapRecord> query = context
    					.selectQuery(Tables.EH_PUNCH_RULE_OWNER_MAP); 
    			query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_ID.eq(ownerId));
    			query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_TYPE.eq(ownerType));
    			if(ListTargetType.APPROVAL.getCode().equals(listType)){
    				query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.REVIEW_RULE_ID.isNotNull());
    			} 
    			if(ListTargetType.PUNCH.getCode().equals(listType)){
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
	public void deletePunchGeopointsByRuleId( Long ruleId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhPunchGeopointsRecord> step = context
				.delete(Tables.EH_PUNCH_GEOPOINTS);
		Condition condition = Tables.EH_PUNCH_GEOPOINTS.LOCATION_RULE_ID.equal(ruleId) ; 
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
		Condition condition = Tables.EH_PUNCH_WIFIS.WIFI_RULE_ID.equal(ruleId) ; 
		step.where(condition);
		step.execute();
	}

	@Override
	public List<PunchWifiRule> queryPunchWifiRuleList(String ownerType, Long ownerId, CrossShardListingLocator locator, int i) {
		List<PunchWifiRule>  result = queryPunchWifiRules(locator, i, new ListingQueryBuilderCallback() {
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
				.and( Tables.EH_PUNCH_WIFIS.OWNER_ID.eq(ownerId))
				.and( Tables.EH_PUNCH_WIFIS.OWNER_TYPE.eq(ownerType));
		step.where(condition);
		List<PunchWifi> result = step
				.orderBy(Tables.EH_PUNCH_WIFIS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, PunchWifi.class);
				});

		if (null != result && result.size() > 0)
			return result ;
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
	public void deletePunchHolidayByRuleId(Long ruleId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhPunchHolidaysRecord> step = context
				.delete(Tables.EH_PUNCH_HOLIDAYS);
		Condition condition = Tables.EH_PUNCH_HOLIDAYS.WORKDAY_RULE_ID.equal(ruleId) ; 
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
				.and( Tables.EH_PUNCH_HOLIDAYS.OWNER_ID.eq(ownerId))
				.and( Tables.EH_PUNCH_HOLIDAYS.OWNER_TYPE.eq(ownerType));
		step.where(condition);
		List<PunchHoliday> result = step
				.orderBy(Tables.EH_PUNCH_HOLIDAYS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, PunchHoliday.class);
				});

		if (null != result && result.size() > 0)
			return result ;
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
				.and( Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_ID.eq(ownerId))
				.and( Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_TYPE.eq(ownerType));
		step.where(condition);
		List<PunchRuleOwnerMap> result = step
				.orderBy(Tables.EH_PUNCH_RULE_OWNER_MAP.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, PunchRuleOwnerMap.class);
				});

		if (null != result && result.size() > 0)
			return result ;
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
	public List<PunchRuleOwnerMap> queryPunchRuleOwnerMapList(String ownerType, Long ownerId, String targetType, Long targetId, List<Long>	userIds,
															  CrossShardListingLocator locator, int i) {

		List<PunchRuleOwnerMap> result = queryPunchRuleOwnerMaps(locator, i, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
																SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_ID.eq(ownerId));
				query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_TYPE.eq(ownerType));
				if(null!=targetId)
					query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.TARGET_ID.eq(targetId));
				if (null != userIds) {
					query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.TARGET_ID.in(userIds));
				}
				if(StringUtils.isNotBlank(targetType))
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
                if(null!=targetId)
                    query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.TARGET_ID.eq(targetId));
                if(StringUtils.isNotBlank(targetType))
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
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhPunchStatisticsDao dao = new EhPunchStatisticsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updatePunchStatistic(PunchStatistic obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchStatisticsDao dao = new EhPunchStatisticsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deletePunchStatistic(PunchStatistic obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPunchStatisticsDao dao = new EhPunchStatisticsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public PunchStatistic getPunchStatisticById(Long id) {
        try {
        PunchStatistic[] result = new PunchStatistic[1];
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

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

    @Override
    public List<PunchStatistic> queryPunchStatistics(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhPunchStatisticsRecord> query = context.selectQuery(Tables.EH_PUNCH_STATISTICS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(null != locator && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_PUNCH_STATISTICS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<PunchStatistic> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, PunchStatistic.class);
        });

		if (null != objs && objs.size() > 0)
			return objs ;
		return null;
    }

    private void prepareObj(PunchStatistic obj) {
    }

	@Override
	public List<PunchStatistic> queryPunchStatistics(String ownerType, Long ownerId, List<String> months, Byte exceptionStatus,
			List<Long> userIds, CrossShardListingLocator locator, int i) {
		List<PunchStatistic> result = queryPunchStatistics(locator,  i, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_PUNCH_STATISTICS.OWNER_ID.eq(ownerId));
                query.addConditions(Tables.EH_PUNCH_STATISTICS.OWNER_TYPE.eq(ownerType));   
                if(null != exceptionStatus)
                	query.addConditions(Tables.EH_PUNCH_STATISTICS.EXCEPTION_STATUS.eq(exceptionStatus));   
                if(null != userIds )
                	query.addConditions(Tables.EH_PUNCH_STATISTICS.USER_ID.in(userIds));   
                if(null != months)
                	query.addConditions(Tables.EH_PUNCH_STATISTICS.PUNCH_MONTH.in(months));   
                	
                return query;
            }
            
        });
		return result;
	}

	@Override
	public void deletePunchStatisticByUser(String ownerType, List<Long> ownerId, String punchMonth, Long userId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhPunchStatisticsRecord> step = context
				.delete(Tables.EH_PUNCH_STATISTICS);
		Condition condition = Tables.EH_PUNCH_STATISTICS.PUNCH_MONTH.equal(punchMonth)
				.and(Tables.EH_PUNCH_STATISTICS.USER_ID.equal(userId))
				.and(Tables.EH_PUNCH_STATISTICS.OWNER_ID.in(ownerId))
				.and(Tables.EH_PUNCH_STATISTICS.OWNER_TYPE.equal(ownerType)) ; 
		step.where(condition);
		step.execute();
	}

	@Override
	public PunchRuleOwnerMap getPunchRuleOwnerMapById(Long id) {
	      try {
	    	  PunchRuleOwnerMap[] result = new PunchRuleOwnerMap[1];
	          DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

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
	public List<PunchDayLog> listPunchDayLogs(List<Long> userIds, Long ownerId, String startDay, String endDay,
			Byte arriveTimeCompareFlag, Time arriveTime, Byte leaveTimeCompareFlag, Time leaveTime, Byte workTimeCompareFlag,
			Time workTime, Byte exceptionStatus, Integer pageOffset,Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record>  step = context.select().from(Tables.EH_PUNCH_DAY_LOGS);
//		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
//		step.join(Tables.EH_GROUP_CONTACTS).on(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_DAY_LOGS.USER_ID));
	 
		Condition condition = (Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.equal(ownerId));
//		condition = condition.and(Tables.EH_GROUP_CONTACTS.OWNER_TYPE.eq(OwnerType.COMPANY.getCode()).and(Tables.EH_GROUP_CONTACTS.OWNER_ID.eq(companyId)));
		if(userIds != null)
			condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.USER_ID.in(userIds));
		if(exceptionStatus != null)
			condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.EXCEPTION_STATUS.eq(exceptionStatus));
		if(!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
			Date startDate = Date.valueOf(startDay);
			Date endDate = Date.valueOf(endDay);
			condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.between(startDate).and(endDate));
		}
		if(null!= arriveTimeCompareFlag && arriveTime != null){ 
			if(arriveTimeCompareFlag.equals(TimeCompareFlag.LESSOREQUAL.getCode())){
				condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME.lessOrEqual(arriveTime));
			}else{
				condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.ARRIVE_TIME.greaterOrEqual(arriveTime));
			}
		}
		if(null!= leaveTimeCompareFlag && null != leaveTime){ 
			if(leaveTimeCompareFlag.equals(TimeCompareFlag.LESSOREQUAL.getCode())){
				condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.LEAVE_TIME.lessOrEqual(leaveTime));
			}else{
				condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.LEAVE_TIME.greaterOrEqual(leaveTime));
			}
		}
		if(null!= workTimeCompareFlag && null != workTime){ 
			if(workTimeCompareFlag.equals(TimeCompareFlag.LESSOREQUAL.getCode())){
				condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.WORK_TIME.lessOrEqual(workTime));
			}else{
				condition = condition.and(Tables.EH_PUNCH_DAY_LOGS.WORK_TIME.greaterOrEqual(workTime));
			}
		}
		 

//		Integer offset = pageOffset == null ? 0 : (pageOffset - 1 ) * pageSize;
		if(null != pageOffset && null != pageSize)
			step.limit(pageOffset , pageSize);
		List<PunchDayLog> result  = new ArrayList<PunchDayLog>();
		step.where(condition)
				.orderBy(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.desc(),Tables.EH_PUNCH_DAY_LOGS.USER_ID.desc()).fetch().map((r) -> {
					result.add( ConvertHelper.convert(r, PunchDayLog.class));
			 return null;
		});
		if(result == null || result.isEmpty())
			return null;
		return result;
	}

	//用于查询是否有过异常申请
	@Override
	public PunchExceptionRequest findPunchExceptionRequest(Long userId, Long ownerId, Long punchDate,
			Byte exceptionRequestType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectConditionStep<Record> step = context.select().from(Tables.EH_PUNCH_EXCEPTION_REQUESTS)
				.where(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.eq(userId))
				.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.eq(ownerId))
				.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.eq(new Date(punchDate)))
				.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
				
		if (exceptionRequestType.byteValue() == ExceptionRequestType.ALL_DAY.getCode()) {
			step.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.APPROVAL_STATUS.eq(PunchStatus.NORMAL.getCode()));
		}else if (exceptionRequestType.byteValue() == ExceptionRequestType.MORNING.getCode()) {
			step.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.MORNING_APPROVAL_STATUS.eq(PunchStatus.NORMAL.getCode()));
		}else if (exceptionRequestType.byteValue() == ExceptionRequestType.AFTERNOON.getCode()) {
			step.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.AFTERNOON_APPROVAL_STATUS.eq(PunchStatus.NORMAL.getCode()));
		}
		
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
				.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
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
			Date beginDate, Date endDate) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record1<String>> records = context.selectDistinct(Tables.EH_PUNCH_LOGS.IDENTIFICATION).from(Tables.EH_PUNCH_LOGS)
				.where(Tables.EH_PUNCH_LOGS.USER_ID.eq(userId))
				.and(Tables.EH_PUNCH_LOGS.ENTERPRISE_ID.eq(companyId))
				.and(Tables.EH_PUNCH_LOGS.PUNCH_DATE.between(beginDate, endDate))
				.fetch();
		
		if (records != null) {
			return records.size();
		}
		return 0;
	}

	@Override
	public void deletePunchTimeRulesByOwnerAndTarget(String ownerType, Long ownerId,
			String targetType, Long targetId) {

        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
		DeleteWhereStep<EhPunchTimeRulesRecord> step = context.delete(Tables.EH_PUNCH_TIME_RULES);
		Condition condition = Tables.EH_PUNCH_TIME_RULES.TARGET_ID.equal(targetId)
				.and(Tables.EH_PUNCH_TIME_RULES.TARGET_TYPE.equal(targetType))
				.and(Tables.EH_PUNCH_TIME_RULES.OWNER_ID.equal(ownerId))
				.and(Tables.EH_PUNCH_TIME_RULES.OWNER_TYPE.equal(ownerType)) ; 
		step.where(condition);
		step.execute();
	}

	@Override
	public void createPunchTimeInterval(PunchTimeInterval ptInterval) {
		 String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchTimeIntervals.class);
			long id = sequenceProvider.getNextSequence(key);
		 DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
		 ptInterval.setId(id); 
		 EhPunchTimeIntervalsDao dao = new EhPunchTimeIntervalsDao(context.configuration());
		 dao.insert(ptInterval);  
	}


	@Override
	public void createPunchSpecialDay(PunchSpecialDay psd){
		 String key = NameMapper.getSequenceDomainFromTablePojo(EhPunchSpecialDays.class);
			long id = sequenceProvider.getNextSequence(key);
		 DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
		 psd.setId(id); 
		 EhPunchSpecialDaysDao dao = new EhPunchSpecialDaysDao(context.configuration());
		 dao.insert(psd);  
		
	};
	
}

