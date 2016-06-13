package com.everhomes.techpark.punch;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record5;
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
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.techpark.punch.DateStatus;
import com.everhomes.rest.techpark.punch.PunchDayLogDTO;
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
import com.everhomes.server.schema.tables.daos.EhPunchRulesDao;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.server.schema.tables.pojos.EhPunchDayLogs;
import com.everhomes.server.schema.tables.pojos.EhPunchExceptionApprovals;
import com.everhomes.server.schema.tables.pojos.EhPunchExceptionRequests;
import com.everhomes.server.schema.tables.pojos.EhPunchGeopoints;
import com.everhomes.server.schema.tables.pojos.EhPunchLogs;
import com.everhomes.server.schema.tables.pojos.EhPunchRules;
import com.everhomes.server.schema.tables.pojos.EhPunchWorkday;
import com.everhomes.server.schema.tables.records.EhPunchDayLogsRecord;
import com.everhomes.server.schema.tables.records.EhPunchExceptionApprovalsRecord;
import com.everhomes.server.schema.tables.records.EhPunchExceptionRequestsRecord;
import com.everhomes.server.schema.tables.records.EhPunchGeopointsRecord;
import com.everhomes.server.schema.tables.records.EhPunchLogsRecord;
import com.everhomes.server.schema.tables.records.EhPunchRulesRecord;
import com.everhomes.server.schema.tables.records.EhPunchWorkdayRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class PunchProviderImpl implements PunchProvider {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PunchServiceImpl.class);
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
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
	public PunchRule getPunchRuleByCompanyId(Long companyId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_PUNCH_RULES);
		Condition condition = Tables.EH_PUNCH_RULES.ENTERPRISE_ID.equal(companyId);
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
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class,punchLog.getEnterpriseId() ));
//		long id = sequenceProvider.getNextSequence(NameMapper
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
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
	public void createPunchRule(PunchRule punchRule) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhPunchRules.class));
		punchRule.setId(id);
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class,punchRule.getEnterpriseId() ));
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
		EhPunchRulesDao dao = new EhPunchRulesDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), PunchRule.class);
	}

	@Override
	public PunchRule findPunchRuleByCompanyId(Long companyId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));

		SelectQuery<EhPunchRulesRecord> query = context
				.selectQuery(Tables.EH_PUNCH_RULES);
		query.addConditions(Tables.EH_PUNCH_RULES.ENTERPRISE_ID.eq(companyId));

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
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class,punchGeopoint.getEnterpriseId() ));
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
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
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class,punchExceptionRequest.getEnterpriseId() ));
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));

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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));

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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
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
		 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
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
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class,punchExceptionApproval.getEnterpriseId() ));
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
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
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class,punchDayLog.getEnterpriseId() ));
		punchDayLog.setViewFlag(ViewFlags.NOTVIEW.getCode());
//		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPunchDayLogs.class));
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));

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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
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
 				.map(new EhPunchDayLogMapper());
		
		List<PunchDayLog> result = resultRecord.stream().map((r) -> {
            return ConvertHelper.convert(r, PunchDayLog.class);
        }).collect(Collectors.toList());
		return result;
	}

	@Override
	public List<PunchExceptionRequest> listExceptionNotViewRequests(
			Long userId, Long companyId, String startDay, String endDay) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
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
		   DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(com.everhomes.server.schema.tables.EhPunchExceptionRequests.class,companyId));
	        context.update(Tables.EH_PUNCH_EXCEPTION_REQUESTS).set(Tables.EH_PUNCH_EXCEPTION_REQUESTS.VIEW_FLAG, ViewFlags.ISVIEW.getCode())
	        .where(Tables.EH_PUNCH_EXCEPTION_REQUESTS.USER_ID.eq(userId))
	        .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ENTERPRISE_ID.eq(companyId))
	        .and(Tables.EH_PUNCH_EXCEPTION_REQUESTS.PUNCH_DATE.eq(logDate)).execute() ;

			//update 日志表
	        context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(com.everhomes.server.schema.tables.EhPunchDayLogs.class,companyId));
	        context.update(Tables.EH_PUNCH_DAY_LOGS).set(Tables.EH_PUNCH_DAY_LOGS.VIEW_FLAG, ViewFlags.ISVIEW.getCode())
	        .where(Tables.EH_PUNCH_DAY_LOGS.USER_ID.eq(userId))
	        .and(Tables.EH_PUNCH_DAY_LOGS.ENTERPRISE_ID.eq(companyId))
	        .and(Tables.EH_PUNCH_DAY_LOGS.PUNCH_DATE.eq(logDate)).execute() ;

			//update 审批表
	        context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(com.everhomes.server.schema.tables.EhPunchExceptionApprovals.class,companyId));
	        context.update(Tables.EH_PUNCH_EXCEPTION_APPROVALS).set(Tables.EH_PUNCH_EXCEPTION_APPROVALS.VIEW_FLAG, ViewFlags.ISVIEW.getCode())
	        .where(Tables.EH_PUNCH_EXCEPTION_APPROVALS.USER_ID.eq(userId))
	        .and(Tables.EH_PUNCH_EXCEPTION_APPROVALS.ENTERPRISE_ID.eq(companyId))
	        .and(Tables.EH_PUNCH_EXCEPTION_APPROVALS.PUNCH_DATE.eq(logDate)).execute() ;
	}
	
	@Override
	public List<UserPunchStatusCount> listUserStatusPunch(Long companyId,  String startDay,
			String endDay) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
		
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
		
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
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
}

