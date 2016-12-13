// @formatter:off
package com.everhomes.openapi;

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
import com.everhomes.server.schema.tables.daos.EhTechparkSyncdataLogDao;
import com.everhomes.server.schema.tables.pojos.EhTechparkSyncdataLog;
import com.everhomes.util.ConvertHelper;

@Component
public class TechparkSyncdataLogProviderImpl implements TechparkSyncdataLogProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createTechparkSyncdataLog(TechparkSyncdataLog techparkSyncdataLog) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhTechparkSyncdataLog.class));
		techparkSyncdataLog.setId(id);
		getReadWriteDao().insert(techparkSyncdataLog);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhTechparkSyncdataLog.class, null);
	}

	@Override
	public void updateTechparkSyncdataLog(TechparkSyncdataLog techparkSyncdataLog) {
		assert (techparkSyncdataLog.getId() != null);
		getReadWriteDao().update(techparkSyncdataLog);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhTechparkSyncdataLog.class, techparkSyncdataLog.getId());
	}

	@Override
	public TechparkSyncdataLog findTechparkSyncdataLogById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), TechparkSyncdataLog.class);
	}
	
	@Override
	public List<TechparkSyncdataLog> listTechparkSyncdataLog() {
		return getReadOnlyContext().select().from(Tables.EH_TECHPARK_SYNCDATA_LOG)
				.orderBy(Tables.EH_TECHPARK_SYNCDATA_LOG.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, TechparkSyncdataLog.class));
	}
	
	private EhTechparkSyncdataLogDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhTechparkSyncdataLogDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhTechparkSyncdataLogDao getDao(DSLContext context) {
		return new EhTechparkSyncdataLogDao(context.configuration());
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
