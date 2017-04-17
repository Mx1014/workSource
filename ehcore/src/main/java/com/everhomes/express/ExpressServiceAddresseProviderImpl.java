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
import com.everhomes.server.schema.tables.daos.EhExpressServiceAddressesDao;
import com.everhomes.server.schema.tables.pojos.EhExpressServiceAddresses;
import com.everhomes.util.ConvertHelper;

@Component
public class ExpressServiceAddresseProviderImpl implements ExpressServiceAddresseProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createExpressServiceAddresse(ExpressServiceAddresse expressServiceAddresse) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhExpressServiceAddresses.class));
		expressServiceAddresse.setId(id);
		getReadWriteDao().insert(expressServiceAddresse);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhExpressServiceAddresses.class, null);
	}

	@Override
	public void updateExpressServiceAddresse(ExpressServiceAddresse expressServiceAddresse) {
		assert (expressServiceAddresse.getId() != null);
		getReadWriteDao().update(expressServiceAddresse);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhExpressServiceAddresses.class, expressServiceAddresse.getId());
	}

	@Override
	public ExpressServiceAddresse findExpressServiceAddresseById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ExpressServiceAddresse.class);
	}
	
	@Override
	public List<ExpressServiceAddresse> listExpressServiceAddresse() {
		return getReadOnlyContext().select().from(Tables.EH_EXPRESS_SERVICE_ADDRESSES)
				.orderBy(Tables.EH_EXPRESS_SERVICE_ADDRESSES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ExpressServiceAddresse.class));
	}
	
	private EhExpressServiceAddressesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhExpressServiceAddressesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhExpressServiceAddressesDao getDao(DSLContext context) {
		return new EhExpressServiceAddressesDao(context.configuration());
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
