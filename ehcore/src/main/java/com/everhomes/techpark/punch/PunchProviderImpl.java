package com.everhomes.techpark.punch;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPunchLogsDao;
import com.everhomes.server.schema.tables.daos.EhVersionUpgradeRulesDao;
import com.everhomes.server.schema.tables.pojos.EhPunchLogs;
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
	public List<PunchLogs> listPunchLogsByDate(Long userId, Long companyId,String queryDate) { 
		Date date =java.sql.Date.valueOf(queryDate);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_PUNCH_LOGS);
        Condition condition = Tables.EH_PUNCH_LOGS.PUNCH_DATE.equal(date);
        Condition condition2 = Tables.EH_PUNCH_LOGS.USER_ID.equal(userId);
        Condition condition3 = Tables.EH_PUNCH_LOGS.COMPANY_ID.equal(companyId);
        condition = condition.and(condition2);
        condition = condition.and(condition3);
        step.where(condition);
        List<PunchLogs> result = step.orderBy(Tables.EH_PUNCH_LOGS.ID.desc()).
                fetch().map((r) ->{ return ConvertHelper.convert(r, PunchLogs.class);});
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
	public PunchRules getPunchRuleByCompanyId(Long companyId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_PUNCH_RULES);
        Condition condition = Tables.EH_PUNCH_RULES.COMPANY_ID.equal(companyId);
        step.where(condition);
        List<PunchRules> result = step.orderBy(Tables.EH_PUNCH_RULES.ID.desc()).
                fetch().map((r) ->{ return ConvertHelper.convert(r,  PunchRules.class);});
        if(null!=result && result.size()>0 )
        	return result.get(0);
        return null;
	}
	
//	@Caching(evict = {
//	        @CacheEvict(value="PunchLogs-List", key="{#punchLog.punchDate,#punchLog.userId,#punchLog.companyId}")
//	    })
	@Override
	public void createPunchLog(PunchLogs punchLog) {
		// TODO Auto-generated method stub
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPunchLogs.class));
		punchLog.setId(id);
		EhPunchLogsDao dao = new EhPunchLogsDao(context.configuration());
		dao.insert(punchLog);
		
		
	}
	  
 
	    
	  
}
