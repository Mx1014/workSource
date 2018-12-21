// @formatter:off
package com.everhomes.enterprisemoment;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterpriseMomentsDao;
import com.everhomes.server.schema.tables.records.EhEnterpriseMomentAccessRecordsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class EnterpriseMomentAccessRecordProviderImpl implements EnterpriseMomentAccessRecordProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void insertOrUpdateRecord(EnterpriseMomentAccessRecord accessRecord) {
		accessRecord.setId(sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(accessRecord.getClass())));
		EhEnterpriseMomentAccessRecordsRecord record = ConvertHelper.convert(accessRecord, EhEnterpriseMomentAccessRecordsRecord.class);
		InsertQuery<EhEnterpriseMomentAccessRecordsRecord> insertQuery = getReadWriteContext().insertQuery(Tables.EH_ENTERPRISE_MOMENT_ACCESS_RECORDS);
		insertQuery.addRecord(record);
		insertQuery.onDuplicateKeyUpdate(true);
		insertQuery.addValueForUpdate(Tables.EH_ENTERPRISE_MOMENT_ACCESS_RECORDS.LAST_VISIT_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()));
		insertQuery.execute();
	}

	private EhEnterpriseMomentsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhEnterpriseMomentsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhEnterpriseMomentsDao getDao(DSLContext context) {
		return new EhEnterpriseMomentsDao(context.configuration());
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
}
