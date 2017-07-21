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
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.express.ExpressOwner;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhExpressCompaniesDao;
import com.everhomes.server.schema.tables.pojos.EhExpressCompanies;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

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
		expressCompany.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		expressCompany.setCreatorUid(UserContext.current().getUser().getId());
		expressCompany.setUpdateTime(expressCompany.getCreateTime());
		expressCompany.setOperatorUid(expressCompany.getCreatorUid());
		getReadWriteDao().insert(expressCompany);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhExpressCompanies.class, null);
	}

	@Override
	public void updateExpressCompany(ExpressCompany expressCompany) {
		assert (expressCompany.getId() != null);
		expressCompany.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		expressCompany.setOperatorUid(UserContext.current().getUser().getId());
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
	
	@Override
	public List<ExpressCompany> listExpressCompanyByOwner(ExpressOwner owner) {
		// TODO 这里根据namespace_id获取快递公司
		if (owner.getOwnerType() != null && owner.getOwnerId() != null) {
			return getReadOnlyContext().select().from(Tables.EH_EXPRESS_COMPANIES)
					.where(Tables.EH_EXPRESS_COMPANIES.NAMESPACE_ID.eq(owner.getNamespaceId()))
					.and(Tables.EH_EXPRESS_COMPANIES.OWNER_TYPE.eq(owner.getOwnerType().getCode()))
					.and(Tables.EH_EXPRESS_COMPANIES.OWNER_ID.eq(owner.getOwnerId()))
					.and(Tables.EH_EXPRESS_COMPANIES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
					.orderBy(Tables.EH_EXPRESS_COMPANIES.ID.asc())
					.fetch().map(r -> ConvertHelper.convert(r, ExpressCompany.class));
		}
		return getReadOnlyContext().select().from(Tables.EH_EXPRESS_COMPANIES)
				.where(Tables.EH_EXPRESS_COMPANIES.PARENT_ID.eq(0L))
				.and(Tables.EH_EXPRESS_COMPANIES.NAMESPACE_ID.eq(owner.getNamespaceId()))
				.and(Tables.EH_EXPRESS_COMPANIES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
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
