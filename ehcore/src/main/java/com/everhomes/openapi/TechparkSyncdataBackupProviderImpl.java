// @formatter:off
package com.everhomes.openapi;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhTechparkSyncdataBackupDao;
import com.everhomes.server.schema.tables.pojos.EhTechparkSyncdataBackup;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class TechparkSyncdataBackupProviderImpl implements TechparkSyncdataBackupProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createTechparkSyncdataBackup(TechparkSyncdataBackup techparkSyncdataBackup) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhTechparkSyncdataBackup.class));
		techparkSyncdataBackup.setId(id);
		getReadWriteDao().insert(techparkSyncdataBackup);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhTechparkSyncdataBackup.class, null);
	}

	@Override
	public void updateTechparkSyncdataBackup(TechparkSyncdataBackup techparkSyncdataBackup) {
		assert (techparkSyncdataBackup.getId() != null);
		getReadWriteDao().update(techparkSyncdataBackup);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhTechparkSyncdataBackup.class, techparkSyncdataBackup.getId());
	}

	@Override
	public TechparkSyncdataBackup findTechparkSyncdataBackupById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), TechparkSyncdataBackup.class);
	}
	
	@Override
	public List<TechparkSyncdataBackup> listTechparkSyncdataBackup() {
		return getReadOnlyContext().select().from(Tables.EH_TECHPARK_SYNCDATA_BACKUP)
				.orderBy(Tables.EH_TECHPARK_SYNCDATA_BACKUP.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, TechparkSyncdataBackup.class));
	}
	
	@Override
	public List<TechparkSyncdataBackup> listTechparkSyncdataBackupByParam(Integer namespaceId, Byte dataType,
			Byte allFlag) {
		return getReadOnlyContext().select().from(Tables.EH_TECHPARK_SYNCDATA_BACKUP)
				.where(Tables.EH_TECHPARK_SYNCDATA_BACKUP.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_TECHPARK_SYNCDATA_BACKUP.DATA_TYPE.eq(dataType))
				.and(Tables.EH_TECHPARK_SYNCDATA_BACKUP.ALL_FLAG.eq(allFlag))
				.and(Tables.EH_TECHPARK_SYNCDATA_BACKUP.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.fetch().map(r -> ConvertHelper.convert(r, TechparkSyncdataBackup.class));
	}

	@Override
	public void updateTechparkSyncdataBackupInactive(List<TechparkSyncdataBackup> backupList) {
		backupList.forEach(b->{
			b.setStatus(CommonStatus.INACTIVE.getCode()); 
			b.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			updateTechparkSyncdataBackup(b);
		});
				
	}

	private EhTechparkSyncdataBackupDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhTechparkSyncdataBackupDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhTechparkSyncdataBackupDao getDao(DSLContext context) {
		return new EhTechparkSyncdataBackupDao(context.configuration());
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
