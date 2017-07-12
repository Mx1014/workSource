// @formatter:off
package com.everhomes.authorization;

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
import com.everhomes.server.schema.tables.daos.EhAuthorizationThirdPartyRecordsDao;
import com.everhomes.server.schema.tables.pojos.EhAuthorizationThirdPartyRecords;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class AuthorizationThirdPartyRecordProviderImpl implements AuthorizationThirdPartyRecordProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createAuthorizationThirdPartyRecord(AuthorizationThirdPartyRecord authorizationThirdPartyRecord) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAuthorizationThirdPartyRecords.class));
		authorizationThirdPartyRecord.setId(id);
		authorizationThirdPartyRecord.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		authorizationThirdPartyRecord.setCreatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().insert(authorizationThirdPartyRecord);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhAuthorizationThirdPartyRecords.class, null);
	}

	@Override
	public void updateAuthorizationThirdPartyRecord(AuthorizationThirdPartyRecord authorizationThirdPartyRecord) {
		assert (authorizationThirdPartyRecord.getId() != null);
		getReadWriteDao().update(authorizationThirdPartyRecord);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAuthorizationThirdPartyRecords.class, authorizationThirdPartyRecord.getId());
	}

	@Override
	public AuthorizationThirdPartyRecord findAuthorizationThirdPartyRecordById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), AuthorizationThirdPartyRecord.class);
	}
	
	@Override
	public List<AuthorizationThirdPartyRecord> listAuthorizationThirdPartyRecord() {
		return getReadOnlyContext().select().from(Tables.EH_AUTHORIZATION_THIRD_PARTY_RECORDS)
				.orderBy(Tables.EH_AUTHORIZATION_THIRD_PARTY_RECORDS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, AuthorizationThirdPartyRecord.class));
	}
	
	private EhAuthorizationThirdPartyRecordsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhAuthorizationThirdPartyRecordsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhAuthorizationThirdPartyRecordsDao getDao(DSLContext context) {
		return new EhAuthorizationThirdPartyRecordsDao(context.configuration());
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
