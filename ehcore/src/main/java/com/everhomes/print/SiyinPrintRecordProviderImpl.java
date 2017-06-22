// @formatter:off
package com.everhomes.print;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSiyinPrintRecordsDao;
import com.everhomes.server.schema.tables.pojos.EhSiyinPrintRecords;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SiyinPrintRecordProviderImpl implements SiyinPrintRecordProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSiyinPrintRecord(SiyinPrintRecord siyinPrintRecord) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSiyinPrintRecords.class));
		siyinPrintRecord.setId(id);
		siyinPrintRecord.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		siyinPrintRecord.setCreatorUid(UserContext.current().getUser().getId());
//		siyinPrintRecord.setUpdateTime(siyinPrintRecord.getCreateTime());
		siyinPrintRecord.setOperatorUid(siyinPrintRecord.getCreatorUid());
		getReadWriteDao().insert(siyinPrintRecord);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSiyinPrintRecords.class, null);
	}

	@Override
	public void updateSiyinPrintRecord(SiyinPrintRecord siyinPrintRecord) {
		assert (siyinPrintRecord.getId() != null);
//		siyinPrintRecord.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		siyinPrintRecord.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(siyinPrintRecord);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSiyinPrintRecords.class, siyinPrintRecord.getId());
	}

	@Override
	public SiyinPrintRecord findSiyinPrintRecordById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SiyinPrintRecord.class);
	}
	
	@Override
	public List<SiyinPrintRecord> listSiyinPrintRecord() {
		return getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_RECORDS)
				.orderBy(Tables.EH_SIYIN_PRINT_RECORDS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SiyinPrintRecord.class));
	}
	
	private EhSiyinPrintRecordsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSiyinPrintRecordsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSiyinPrintRecordsDao getDao(DSLContext context) {
		return new EhSiyinPrintRecordsDao(context.configuration());
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
	public SiyinPrintRecord findSiyinPrintRecordByJobId(String jobId) {
		List<SiyinPrintRecord> list = getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_RECORDS)
			.where(Tables.EH_SIYIN_PRINT_RECORDS.JOB_ID.eq(jobId)).fetch().map(r->ConvertHelper.convert(r, SiyinPrintRecord.class));
		if(list!=null && list.size() > 0)
			return list.get(0);
		return null;
	}
}
