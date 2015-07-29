package com.everhomes.cooperation;

import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

}
