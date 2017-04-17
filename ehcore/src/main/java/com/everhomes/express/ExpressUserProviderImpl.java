// @formatter:off
package com.everhomes.express;

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
import com.everhomes.server.schema.tables.daos.EhExpressUsersDao;
import com.everhomes.server.schema.tables.pojos.EhExpressUsers;
import com.everhomes.util.ConvertHelper;

@Component
public class ExpressUserProviderImpl implements ExpressUserProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createExpressUser(ExpressUser expressUser) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhExpressUsers.class));
		expressUser.setId(id);
		getReadWriteDao().insert(expressUser);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhExpressUsers.class, null);
	}

	@Override
	public void updateExpressUser(ExpressUser expressUser) {
		assert (expressUser.getId() != null);
		getReadWriteDao().update(expressUser);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhExpressUsers.class, expressUser.getId());
	}

	@Override
	public ExpressUser findExpressUserById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ExpressUser.class);
	}
	
	@Override
	public List<ExpressUser> listExpressUser() {
		return getReadOnlyContext().select().from(Tables.EH_EXPRESS_USERS)
				.orderBy(Tables.EH_EXPRESS_USERS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ExpressUser.class));
	}
	
	private EhExpressUsersDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhExpressUsersDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhExpressUsersDao getDao(DSLContext context) {
		return new EhExpressUsersDao(context.configuration());
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
