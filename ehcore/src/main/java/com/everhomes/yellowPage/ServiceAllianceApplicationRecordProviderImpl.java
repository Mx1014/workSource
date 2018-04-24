// @formatter:off
package com.everhomes.yellowPage;

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
import com.everhomes.server.schema.tables.daos.EhServiceAllianceApplicationRecordsDao;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceApplicationRecords;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class ServiceAllianceApplicationRecordProviderImpl implements ServiceAllianceApplicationRecordProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createServiceAllianceApplicationRecord(ServiceAllianceApplicationRecord serviceAllianceApplicationRecord) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAllianceApplicationRecords.class));
		serviceAllianceApplicationRecord.setId(id);
		serviceAllianceApplicationRecord.setCreatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().insert(serviceAllianceApplicationRecord);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceAllianceApplicationRecords.class, null);
	}

	@Override
	public void updateServiceAllianceApplicationRecord(ServiceAllianceApplicationRecord serviceAllianceApplicationRecord) {
		assert (serviceAllianceApplicationRecord.getId() != null);
		getReadWriteDao().update(serviceAllianceApplicationRecord);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceAllianceApplicationRecords.class, serviceAllianceApplicationRecord.getId());
	}

	@Override
	public ServiceAllianceApplicationRecord findServiceAllianceApplicationRecordById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ServiceAllianceApplicationRecord.class);
	}
	
	@Override
	public List<ServiceAllianceApplicationRecord> listServiceAllianceApplicationRecord(Long pageAnchor, Integer pageSize) {
		return getReadOnlyContext().select().from(Tables.EH_SERVICE_ALLIANCE_APPLICATION_RECORDS)
				.where(Tables.EH_SERVICE_ALLIANCE_APPLICATION_RECORDS.ID.gt(pageAnchor))
				.orderBy(Tables.EH_SERVICE_ALLIANCE_APPLICATION_RECORDS.ID.asc()).limit(pageSize)
				.fetch().map(r -> ConvertHelper.convert(r, ServiceAllianceApplicationRecord.class));
	}
	
	@Override
	public List<ServiceAllianceApplicationRecord> listServiceAllianceApplicationRecordByEnterpriseId(Long enterpriseId,Long pageAnchor, Integer pageSize) {
		return getReadOnlyContext().select().from(Tables.EH_SERVICE_ALLIANCE_APPLICATION_RECORDS)
				.where(Tables.EH_SERVICE_ALLIANCE_APPLICATION_RECORDS.ID.gt(pageAnchor))
				.and(Tables.EH_SERVICE_ALLIANCE_APPLICATION_RECORDS.CREATOR_ORGANIZATION_ID.eq(enterpriseId))
				.orderBy(Tables.EH_SERVICE_ALLIANCE_APPLICATION_RECORDS.ID.asc()).limit(pageSize)
				.fetch().map(r -> ConvertHelper.convert(r, ServiceAllianceApplicationRecord.class));
	}
	
	private EhServiceAllianceApplicationRecordsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhServiceAllianceApplicationRecordsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhServiceAllianceApplicationRecordsDao getDao(DSLContext context) {
		return new EhServiceAllianceApplicationRecordsDao(context.configuration());
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
