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
import com.everhomes.server.schema.tables.daos.EhExpressCompaniesDao;
import com.everhomes.server.schema.tables.pojos.EhExpressCompanies;
import com.everhomes.util.ConvertHelper;

@Component
public class ExpressCompanyProviderImpl implements ExpressCompanyProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createExpressCompany(ExpressCompany expressCompany) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhExpressCompanies.class));
		expressCompany.setId(id);
		getReadWriteDao().insert(expressCompany);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhExpressCompanies.class, null);
	}

	@Override
	public void updateExpressCompany(ExpressCompany expressCompany) {
		assert (expressCompany.getId() != null);
		getReadWriteDao().update(expressCompany);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhExpressCompanies.class, expressCompany.getId());
	}

	@Override
	public ExpressCompany findExpressCompanyById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ExpressCompany.class);
	}
	
	@Override
	public List<ExpressCompany> listExpressCompany() {
		return getReadOnlyContext().select().from(Tables.EH_EXPRESS_COMPANIES)
				.orderBy(Tables.EH_EXPRESS_COMPANIES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ExpressCompany.class));
	}
	
	private EhExpressCompaniesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhExpressCompaniesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhExpressCompaniesDao getDao(DSLContext context) {
		return new EhExpressCompaniesDao(context.configuration());
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
