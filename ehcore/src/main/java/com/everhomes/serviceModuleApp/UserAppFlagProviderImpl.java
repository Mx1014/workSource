// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhUserAppFlagsDao;
import com.everhomes.server.schema.tables.pojos.EhUserAppFlags;
import com.everhomes.server.schema.tables.pojos.EhUserApps;
import com.everhomes.server.schema.tables.records.EhUserAppFlagsRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UserAppFlagProviderImpl implements UserAppFlagProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createUserAppFlag(UserAppFlag userAppFlag) {

		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserAppFlags.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserAppFlags.class));
		userAppFlag.setId(id);

		EhUserAppFlagsDao dao = new EhUserAppFlagsDao(context.configuration());
		dao.insert(userAppFlag);
	}

	@Override
	public UserAppFlag findById(Long id) {


		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserAppFlags.class));

		EhUserAppFlagsDao dao = new EhUserAppFlagsDao(context.configuration());
		EhUserAppFlags userAppFlag = dao.findById(id);

		return ConvertHelper.convert(userAppFlag, UserAppFlag.class);
	}

	@Override
	public UserAppFlag findUserAppFlag(Long userId, Byte locationType, Long locationTargetId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserAppFlags.class));
		SelectQuery<EhUserAppFlagsRecord> query = context.selectQuery(Tables.EH_USER_APP_FLAGS);
		query.addConditions(Tables.EH_USER_APP_FLAGS.USER_ID.eq(userId));
		query.addConditions(Tables.EH_USER_APP_FLAGS.LOCATION_TYPE.eq(locationType));
		query.addConditions(Tables.EH_USER_APP_FLAGS.LOCATION_TARGET_ID.eq(locationTargetId));

		UserAppFlag userAppFlag = query.fetchAnyInto(UserAppFlag.class);

		return userAppFlag;
	}

	@Override
	public void delete(Long id) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserAppFlags.class));
		EhUserAppFlagsDao dao = new EhUserAppFlagsDao(context.configuration());
		dao.deleteById(id);
	}


	@Override
	public void delete(Long userId, Byte locationType, Long locationTargetId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserApps.class));
		DeleteQuery<EhUserAppFlagsRecord> query = context.deleteQuery(Tables.EH_USER_APP_FLAGS);
		query.addConditions(Tables.EH_USER_APP_FLAGS.USER_ID.eq(userId));
		query.addConditions(Tables.EH_USER_APP_FLAGS.LOCATION_TYPE.eq(locationType));
		query.addConditions(Tables.EH_USER_APP_FLAGS.LOCATION_TARGET_ID.eq(locationTargetId));
		query.execute();
	}
}
