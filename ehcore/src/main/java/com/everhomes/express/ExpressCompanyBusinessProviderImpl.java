// @formatter:off
package com.everhomes.express;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectConditionStep;
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
import com.everhomes.server.schema.tables.daos.EhExpressCompanyBusinessesDao;
import com.everhomes.server.schema.tables.pojos.EhExpressCompanyBusinesses;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class ExpressCompanyBusinessProviderImpl implements ExpressCompanyBusinessProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createExpressCompanyBusiness(ExpressCompanyBusiness expressCompanyBusiness) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhExpressCompanyBusinesses.class));
		expressCompanyBusiness.setId(id);
		expressCompanyBusiness.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		expressCompanyBusiness.setCreatorUid(UserContext.current().getUser().getId());
		expressCompanyBusiness.setUpdateTime(expressCompanyBusiness.getCreateTime());
		expressCompanyBusiness.setOperatorUid(expressCompanyBusiness.getCreatorUid());
		getReadWriteDao().insert(expressCompanyBusiness);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhExpressCompanyBusinesses.class, null);
	}

	@Override
	public void updateExpressCompanyBusiness(ExpressCompanyBusiness expressCompanyBusiness) {
		assert (expressCompanyBusiness.getId() != null);
		expressCompanyBusiness.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		expressCompanyBusiness.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(expressCompanyBusiness);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhExpressCompanyBusinesses.class, expressCompanyBusiness.getId());
	}

	@Override
	public ExpressCompanyBusiness findExpressCompanyBusinessById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ExpressCompanyBusiness.class);
	}
	
	@Override
	public List<ExpressCompanyBusiness> listExpressCompanyBusiness() {
		return getReadOnlyContext().select().from(Tables.EH_EXPRESS_COMPANY_BUSINESSES)
				.orderBy(Tables.EH_EXPRESS_COMPANY_BUSINESSES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ExpressCompanyBusiness.class));
	}
	
	private EhExpressCompanyBusinessesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhExpressCompanyBusinessesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhExpressCompanyBusinessesDao getDao(DSLContext context) {
		return new EhExpressCompanyBusinessesDao(context.configuration());
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

	@Override
	public List<ExpressCompanyBusiness> listExpressSendTypesByOwner(int namespaceId, String ownerType, Long ownerId,
			Long expressCompanyId) {
		SelectConditionStep<?> query = getReadOnlyContext().select().from(Tables.EH_EXPRESS_COMPANY_BUSINESSES)
		.where(Tables.EH_EXPRESS_COMPANY_BUSINESSES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
		.and(Tables.EH_EXPRESS_COMPANY_BUSINESSES.NAMESPACE_ID.eq(namespaceId));
		if (ownerType != null && ownerId != null) {
			query.and(Tables.EH_EXPRESS_COMPANY_BUSINESSES.OWNER_TYPE.eq(ownerType));
			query.and(Tables.EH_EXPRESS_COMPANY_BUSINESSES.OWNER_ID.eq(ownerId));
		}
		if(expressCompanyId !=null ){
			query.and(Tables.EH_EXPRESS_COMPANY_BUSINESSES.EXPRESS_COMPANY_ID.eq(expressCompanyId));
		}
		return query.fetch().map(r->ConvertHelper.convert(r, ExpressCompanyBusiness.class));
	}

	@Override
	public ExpressCompanyBusiness getExpressCompanyBusinessByOwner(int namespaceId, String ownerType, Long ownerId,
			Byte sendType) {
		SelectConditionStep<?> query = getReadOnlyContext().select().from(Tables.EH_EXPRESS_COMPANY_BUSINESSES)
				.where(Tables.EH_EXPRESS_COMPANY_BUSINESSES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.and(Tables.EH_EXPRESS_COMPANY_BUSINESSES.NAMESPACE_ID.eq(namespaceId));
		query.and(Tables.EH_EXPRESS_COMPANY_BUSINESSES.OWNER_TYPE.eq(ownerType));
		query.and(Tables.EH_EXPRESS_COMPANY_BUSINESSES.OWNER_ID.eq(ownerId));
		query.and(Tables.EH_EXPRESS_COMPANY_BUSINESSES.SEND_TYPE.eq(Long.valueOf(sendType)));
		List list = query.fetch();
		if(list != null && list.size()>0){
			return ConvertHelper.convert(list.get(0),ExpressCompanyBusiness.class);
		}
		return null;
	}


}
