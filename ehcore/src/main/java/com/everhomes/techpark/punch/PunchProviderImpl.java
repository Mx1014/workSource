package com.everhomes.techpark.punch;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.JoinType;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.pm.CommunityPmMember;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhPunchExceptionRequests;
import com.everhomes.server.schema.tables.pojos.EhPunchGeopoints;
import com.everhomes.server.schema.tables.pojos.EhPunchRules;
import com.everhomes.server.schema.tables.pojos.EhPunchWorkday;
import com.everhomes.server.schema.tables.daos.EhPunchGeopointsDao;
import com.everhomes.server.schema.tables.daos.EhPunchLogsDao;
import com.everhomes.server.schema.tables.daos.EhPunchRulesDao;
import com.everhomes.server.schema.tables.daos.EhVersionUpgradeRulesDao;
import com.everhomes.server.schema.tables.pojos.EhPunchLogs;
import com.everhomes.server.schema.tables.records.EhOrganizationMembersRecord;
import com.everhomes.server.schema.tables.records.EhPunchExceptionRequestsRecord;
import com.everhomes.server.schema.tables.records.EhPunchGeopointsRecord;
import com.everhomes.server.schema.tables.records.EhPunchRulesRecord;
import com.everhomes.server.schema.tables.records.EhPunchWorkdayRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.version.VersionUpgradeRule;

@Component
public class PunchProviderImpl implements PunchProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	// @Cacheable(value="PunchLogs-List", key="{#queryDate,#userId,#companyId}",
	// unless="#result.size() == 0")
	@Override
	public List<PunchLog> listPunchLogsByDate(Long userId, Long companyId,
			String queryDate, byte clockCode) {
		Date date = java.sql.Date.valueOf(queryDate);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_PUNCH_LOGS);
		Condition condition = Tables.EH_PUNCH_LOGS.PUNCH_DATE.equal(date);
		Condition condition2 = Tables.EH_PUNCH_LOGS.USER_ID.equal(userId);
		Condition condition3 = Tables.EH_PUNCH_LOGS.COMPANY_ID.equal(companyId);
		condition = condition.and(condition2);
		condition = condition.and(condition3);
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record1<Date>> step = context.selectDistinct(
				Tables.EH_PUNCH_LOGS.PUNCH_DATE).from(Tables.EH_PUNCH_LOGS);
		Condition condition3 = Tables.EH_PUNCH_LOGS.PUNCH_DATE.between(
				beginSqlDate, endSqlDate);
		Condition condition2 = Tables.EH_PUNCH_LOGS.USER_ID.equal(userId);
		Condition condition = Tables.EH_PUNCH_LOGS.COMPANY_ID.equal(companyId);
		condition = condition.and(condition2);
		condition = condition.and(condition3);
		step.where(condition);
		List<Date> result = step
				.orderBy(Tables.EH_PUNCH_LOGS.PUNCH_DATE.desc()).fetch()
				.map(r -> r.getValue(Tables.EH_PUNCH_LOGS.PUNCH_DATE));
		return result;
	}

	@Override
	public PunchRule getPunchRuleByCompanyId(Long companyId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_PUNCH_RULES);
		Condition condition = Tables.EH_PUNCH_RULES.COMPANY_ID.equal(companyId);
		step.where(condition);
		List<PunchRule> result = step.orderBy(Tables.EH_PUNCH_RULES.ID.desc())
				.fetch().map((r) -> {
					return ConvertHelper.convert(r, PunchRule.class);
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
		DSLContext context = this.dbProvider.getDslContext(AccessSpec
				.readWrite());
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhPunchLogs.class));
		punchLog.setId(id);
		EhPunchLogsDao dao = new EhPunchLogsDao(context.configuration());
		dao.insert(punchLog);

	}

	@Override
	public List<PunchGeopoint> listPunchGeopointsByCompanyId(Long companyId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_PUNCH_GEOPOINTS);
		Condition condition = Tables.EH_PUNCH_GEOPOINTS.COMPANY_ID
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
	public void createPunchRule(PunchRule punchRule) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhPunchRules.class));
		punchRule.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPunchRulesRecord record = ConvertHelper.convert(punchRule,
				EhPunchRulesRecord.class);
		InsertQuery<EhPunchRulesRecord> query = context
				.insertQuery(Tables.EH_PUNCH_RULES);
		query.setRecord(record);
		query.execute();

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPunchRules.class, null);

	}

	@Override
	public void updatePunchRule(PunchRule punchRule) {
		assert (punchRule.getId() == null);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPunchRulesDao dao = new EhPunchRulesDao(context.configuration());
		dao.update(punchRule);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchRules.class,
				punchRule.getId());
	}

	@Override
	public void deletePunchRule(PunchRule punchRule) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPunchRulesDao dao = new EhPunchRulesDao(context.configuration());
		dao.deleteById(punchRule.getId());

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchRules.class,
				punchRule.getId());
	}

	@Override
	public void deletePunchRuleById(Long id) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPunchRulesDao dao = new EhPunchRulesDao(context.configuration());
		dao.deleteById(id);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchRules.class, id);
	}

	@Override
	public PunchRule findPunchRuleById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhPunchRulesDao dao = new EhPunchRulesDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), PunchRule.class);
	}

	@Override
	public PunchRule findPunchRuleByCompanyId(Long companyId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectQuery<EhPunchRulesRecord> query = context
				.selectQuery(Tables.EH_PUNCH_RULES);
		query.addConditions(Tables.EH_PUNCH_RULES.COMPANY_ID.eq(companyId));

		List<PunchRule> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, PunchRule.class));
			return null;
		});
		if (null != result && result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	public void createPunchGeopoint(PunchGeopoint punchGeopoint) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhPunchGeopoints.class));
		punchGeopoint.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
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
		// query.setReturning(Tables.EH_PUNCH_RULES.ID);
		query.execute();
		// punchRule.setId(query.getReturnedRecord().getId());
		DaoHelper
				.publishDaoAction(DaoAction.CREATE, EhPunchWorkday.class, null);

	}

	@Override
	public List<PunchWorkday> listWorkdays(DateStatus dateStatus) {
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_PUNCH_EXCEPTION_REQUESTS);
		Condition condition = Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE
				.equal(date);
		Condition condition2 = Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.equal(userId);
		Condition condition3 = Tables.EH_PUNCH_EXCEPTION_REQUESTS.COMPANY_ID.equal(companyId);
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
		// TODO Auto-generated method stub
		Date date = java.sql.Date.valueOf(logDay);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_PUNCH_EXCEPTION_APPROVALS);
		Condition condition = Tables.EH_PUNCH_EXCEPTION_APPROVALS.PUNCH_DATE
				.equal(date);
		Condition condition2 = Tables.EH_PUNCH_EXCEPTION_APPROVALS.USER_ID.equal(userId);
		Condition condition3 = Tables.EH_PUNCH_EXCEPTION_APPROVALS.COMPANY_ID.equal(companyId);
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
	public Integer countExceptionRequests(String keyword, Long companyId, String startDay, String endDay, byte status,
			byte processCode) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_PUNCH_EXCEPTION_REQUESTS);
		step.join(Tables.EH_GROUP_CONTACTS, JoinType.JOIN).connectBy(Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
		
	
		Condition condition = (Tables.EH_PUNCH_EXCEPTION_REQUESTS.COMPANY_ID.equal(companyId));
		if(keyword != null)
			condition = condition.and(Tables.EH_GROUP_CONTACTS.CONTACT_NAME.like("%"+keyword+"%").
					or(Tables.EH_GROUP_CONTACTS.CONTACT_TOKEN.like("%"+keyword+"%").or(Tables.EH_GROUP_CONTACTS.STRING_TAG1.like("%"+keyword+"%"))));

		if(!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
			Date startDate = Date.valueOf(startDay);
			Date endDate = Date.valueOf(endDay);
			condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.between(startDate).and(endDate));
		}
		if(status != 0){
			condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.eq(status));
		}
		if(processCode != 0){
			condition = condition.and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PROCESS_CODE.eq(processCode));
		}
		return step.where(condition).fetchOneInto(Integer.class);
	
	}
	
	@Override
	public List<PunchExceptionRequest> listExceptionRequests(String keyword, Long companyId, String startDay,String endDay,
			byte status, byte processCode,Integer pageOffset,Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<PunchExceptionRequest> result  = new ArrayList<PunchExceptionRequest>();
		SelectQuery<EhPunchExceptionRequestsRecord> query = context.selectQuery(Tables.EH_PUNCH_EXCEPTION_REQUESTS);
		query.addJoin(Tables.EH_GROUP_CONTACTS, Tables.EH_GROUP_CONTACTS.CONTACT_UID.eq(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID));
		query.addConditions(Tables.EH_PUNCH_EXCEPTION_REQUESTS.COMPANY_ID.equal(companyId));
		if(keyword != null)
			query.addConditions(Tables.EH_GROUP_CONTACTS.CONTACT_NAME.like("%"+keyword+"%").
					or(Tables.EH_GROUP_CONTACTS.CONTACT_TOKEN.like("%"+keyword+"%").or(Tables.EH_GROUP_CONTACTS.STRING_TAG1.like("%"+keyword+"%"))));

		if(!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
			Date startDate = Date.valueOf(startDay);
			Date endDate = Date.valueOf(endDay);
			query.addConditions(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.between(startDate).and(endDate));
		}
		if(status != 0){
			query.addConditions(Tables.EH_PUNCH_EXCEPTION_REQUESTS.STATUS.eq(status));
		}
		if(processCode != 0){
			query.addConditions(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PROCESS_CODE.eq(processCode));
		}
		Integer offset = pageOffset == null ? 1 : (pageOffset - 1 ) * pageSize;
		query.addOrderBy(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ID.asc());
		query.addLimit(offset, pageSize);
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, PunchExceptionRequest.class));
			return null;
		});
		return result;
	}
}

