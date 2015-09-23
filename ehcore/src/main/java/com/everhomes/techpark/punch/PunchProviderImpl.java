package com.everhomes.techpark.punch;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
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
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhPunchRules;
import com.everhomes.server.schema.tables.pojos.EhPunchWorkday;
import com.everhomes.server.schema.tables.daos.EhPunchLogsDao;
import com.everhomes.server.schema.tables.daos.EhPunchRulesDao;
import com.everhomes.server.schema.tables.daos.EhVersionUpgradeRulesDao;
import com.everhomes.server.schema.tables.pojos.EhPunchLogs;
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
     
//    @Cacheable(value="PunchLogs-List", key="{#queryDate,#userId,#companyId}", unless="#result.size() == 0")
	@Override
	public List<PunchLog> listPunchLogsByDate(Long userId, Long companyId,String queryDate) { 
		Date date =java.sql.Date.valueOf(queryDate);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_PUNCH_LOGS);
        Condition condition = Tables.EH_PUNCH_LOGS.PUNCH_DATE.equal(date);
        Condition condition2 = Tables.EH_PUNCH_LOGS.USER_ID.equal(userId);
        Condition condition3 = Tables.EH_PUNCH_LOGS.COMPANY_ID.equal(companyId);
        condition = condition.and(condition2);
        condition = condition.and(condition3);
        step.where(condition);
        List<PunchLog> result = step.orderBy(Tables.EH_PUNCH_LOGS.ID.desc()).
                fetch().map((r) ->{ return ConvertHelper.convert(r, PunchLog.class);});
        return result;
	}
	
	@Override
	public List<Date> listPunchLogsBwteenTwoDay(Long userId, Long companyId,String beginDate,String endDate) { 
		Date beginSqlDate =java.sql.Date.valueOf(beginDate);
		Date endSqlDate =java.sql.Date.valueOf(endDate);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record1<Date>> step = context.selectDistinct(Tables.EH_PUNCH_LOGS.PUNCH_DATE).from(Tables.EH_PUNCH_LOGS);
        Condition condition3 = Tables.EH_PUNCH_LOGS.PUNCH_DATE.between(beginSqlDate, endSqlDate);
        Condition condition2 = Tables.EH_PUNCH_LOGS.USER_ID.equal(userId);
        Condition condition = Tables.EH_PUNCH_LOGS.COMPANY_ID.equal(companyId);
        condition = condition.and(condition2);
        condition = condition.and(condition3);
        step.where(condition);
        List<Date> result = step.orderBy(Tables.EH_PUNCH_LOGS.PUNCH_DATE.desc()).
                fetch().map(r -> r.getValue(Tables.EH_PUNCH_LOGS.PUNCH_DATE));
        return result;
	}
	
	@Override
	public PunchRule getPunchRuleByCompanyId(Long companyId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_PUNCH_RULES);
        Condition condition = Tables.EH_PUNCH_RULES.COMPANY_ID.equal(companyId);
        step.where(condition);
        List<PunchRule> result = step.orderBy(Tables.EH_PUNCH_RULES.ID.desc()).
                fetch().map((r) ->{ return ConvertHelper.convert(r,  PunchRule.class);});
        if(null!=result && result.size()>0 )
        	return result.get(0);
        return null;
	}
	
//	@Caching(evict = {
//	        @CacheEvict(value="PunchLogs-List", key="{#punchLog.punchDate,#punchLog.userId,#punchLog.companyId}")
//	    })
	@Override
	public void createPunchLog(PunchLog punchLog) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPunchLogs.class));
		punchLog.setId(id);
		EhPunchLogsDao dao = new EhPunchLogsDao(context.configuration());
		dao.insert(punchLog);
		
		
	}

	@Override
	public List<PunchGeopoint> listPunchGeopointsByCompanyId(Long companyId) {
		// TODO Auto-generated method stub
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_PUNCH_GEOPOINTS);
        Condition condition = Tables.EH_PUNCH_GEOPOINTS.COMPANY_ID.equal(companyId);
        step.where(condition);
        List<PunchGeopoint> result = step.orderBy(Tables.EH_PUNCH_GEOPOINTS.ID.desc()).
                fetch().map((r) ->{ return ConvertHelper.convert(r,  PunchGeopoint.class);});
        return result;
	} 
 
	@Override
	public void createPunchRule(PunchRule punchRule) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhPunchRules.class));
		punchRule.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPunchRulesRecord record = ConvertHelper.convert(punchRule, EhPunchRulesRecord.class);
		InsertQuery<EhPunchRulesRecord> query = context.insertQuery(Tables.EH_PUNCH_RULES);
		query.setRecord(record);
//		query.setReturning(Tables.EH_PUNCH_RULES.ID);
		query.execute();

//		punchRule.setId(query.getReturnedRecord().getId());
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPunchRules.class, null); 

	}


	@Override
	public void updatePunchRule(PunchRule punchRule){
		assert(punchRule.getId() == null);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPunchRulesDao dao = new EhPunchRulesDao(context.configuration());
		dao.update(punchRule);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchRules.class, punchRule.getId());
	}


	@Override
	public void deletePunchRule(PunchRule punchRule){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPunchRulesDao dao = new EhPunchRulesDao(context.configuration());
		dao.deleteById(punchRule.getId());

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchRules.class, punchRule.getId());
	}


	@Override
	public void deletePunchRuleById(Long id){

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

		SelectQuery<EhPunchRulesRecord> query = context.selectQuery(Tables.EH_PUNCH_RULES);
		query.addConditions(Tables.EH_PUNCH_RULES.COMPANY_ID.eq(companyId));

		List<PunchRule> result = new ArrayList<>();
		query.fetch().map((r) ->{
				result.add(ConvertHelper.convert(r,  PunchRule.class));
				return null;
			});
	        if(null!=result && result.size()>0 )
	        	return result.get(0);
	        return null;
	}    
	  
	@Override
	public void createPunchWorkday(PunchWorkday punchWorkday) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhPunchWorkday.class));
		punchWorkday.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPunchWorkdayRecord record = ConvertHelper.convert(punchWorkday, EhPunchWorkdayRecord.class);
		InsertQuery<EhPunchWorkdayRecord> query = context.insertQuery(Tables.EH_PUNCH_WORKDAY);
		query.setRecord(record);
//		query.setReturning(Tables.EH_PUNCH_RULES.ID);
		query.execute();
//		punchRule.setId(query.getReturnedRecord().getId());
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPunchWorkday.class, null); 

	}

}
