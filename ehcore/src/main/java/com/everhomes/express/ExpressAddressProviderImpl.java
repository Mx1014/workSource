// @formatter:off
package com.everhomes.express;

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
import com.everhomes.server.schema.tables.daos.EhExpressAddressesDao;
import com.everhomes.server.schema.tables.pojos.EhExpressAddresses;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class ExpressAddressProviderImpl implements ExpressAddressProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createExpressAddress(ExpressAddress expressAddress) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhExpressAddresses.class));
		expressAddress.setId(id);
		expressAddress.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		expressAddress.setCreatorUid(UserContext.current().getUser().getId());
		expressAddress.setUpdateTime(expressAddress.getCreateTime());
		expressAddress.setOperatorUid(expressAddress.getCreatorUid());
		getReadWriteDao().insert(expressAddress);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhExpressAddresses.class, null);
	}

	@Override
	public void updateExpressAddress(ExpressAddress expressAddress) {
		assert (expressAddress.getId() != null);
		expressAddress.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		expressAddress.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(expressAddress);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhExpressAddresses.class, expressAddress.getId());
	}

	@Override
	public ExpressAddress findExpressAddressById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ExpressAddress.class);
	}
	
	@Override
	public List<ExpressAddress> listExpressAddress() {
		return getReadOnlyContext().select().from(Tables.EH_EXPRESS_ADDRESSES)
				.orderBy(Tables.EH_EXPRESS_ADDRESSES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ExpressAddress.class));
	}
	
	private EhExpressAddressesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhExpressAddressesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhExpressAddressesDao getDao(DSLContext context) {
		return new EhExpressAddressesDao(context.configuration());
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
