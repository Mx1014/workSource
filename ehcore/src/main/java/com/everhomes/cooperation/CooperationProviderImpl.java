package com.everhomes.cooperation;

import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.banner.Banner;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhCooperationRequests;
import com.everhomes.server.schema.tables.records.EhCooperationRequestsRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class CooperationProviderImpl implements CooperationProvider {

	@Autowired
	private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;
    
    
	@Override
	public void newCooperation(CooperationRequests cooperationRequests) {
		//
		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCooperationRequests.class));
		cooperationRequests.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhCooperationRequestsRecord record = ConvertHelper.convert(cooperationRequests, EhCooperationRequestsRecord.class);
		
		InsertQuery<EhCooperationRequestsRecord> query = context.insertQuery(Tables.EH_COOPERATION_REQUESTS);
		query.setRecord(record);
//		query.setReturning(Tables.EH_COOPERATION_REQUESTS.ID);
		query.execute();

//		cooperationRequests.setId(query.getReturnedRecord().getId());
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhCooperationRequests.class, null); 
	}

	  @Override
	    public List<CooperationRequests> listCooperation(String keyword, String cooperationType,long offset, long pageSize){
	        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
	        SelectJoinStep<Record> step = context.select().from(Tables.EH_COOPERATION_REQUESTS);
	        Condition condition = Tables.EH_COOPERATION_REQUESTS.COOPERATION_TYPE.equal(cooperationType);
	        Condition condition2 = Tables.EH_COOPERATION_REQUESTS.COMMUNITY_NAMES.like("%" + keyword + "%");
	        condition2 = condition2.or(Tables.EH_COOPERATION_REQUESTS.NAME.like("%" + keyword + "%"));
	        step.where(condition.and(condition2));

	        List<CooperationRequests> result = step.orderBy(Tables.EH_COOPERATION_REQUESTS.ID.desc()).limit((int)pageSize).offset((int)offset).
	                fetch().map((r) ->{ return ConvertHelper.convert(r, CooperationRequests.class);});
	        return result;
	    }
	    
	  
}
