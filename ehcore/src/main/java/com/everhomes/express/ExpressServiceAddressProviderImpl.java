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
import com.everhomes.server.schema.tables.daos.EhExpressServiceAddressesDao;
import com.everhomes.server.schema.tables.pojos.EhExpressServiceAddresses;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class ExpressServiceAddressProviderImpl implements ExpressServiceAddressProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createExpressServiceAddress(ExpressServiceAddress expressServiceAddress) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhExpressServiceAddresses.class));
		expressServiceAddress.setId(id);
		expressServiceAddress.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		expressServiceAddress.setCreatorUid(UserContext.current().getUser().getId());
		expressServiceAddress.setUpdateTime(expressServiceAddress.getCreateTime());
		expressServiceAddress.setOperatorUid(expressServiceAddress.getCreatorUid());
		getReadWriteDao().insert(expressServiceAddress);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhExpressServiceAddresses.class, null);
	}

	@Override
	public void updateExpressServiceAddress(ExpressServiceAddress expressServiceAddress) {
		assert (expressServiceAddress.getId() != null);
		expressServiceAddress.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		expressServiceAddress.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(expressServiceAddress);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhExpressServiceAddresses.class, expressServiceAddress.getId());
	}

	@Override
	public ExpressServiceAddress findExpressServiceAddressById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ExpressServiceAddress.class);
	}
	
	@Override
	public List<ExpressServiceAddress> listExpressServiceAddress() {
		return getReadOnlyContext().select().from(Tables.EH_EXPRESS_SERVICE_ADDRESSES)
				.orderBy(Tables.EH_EXPRESS_SERVICE_ADDRESSES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ExpressServiceAddress.class));
	}
	
	@Override
	public List<ExpressServiceAddress> listExpressServiceAddresseByOwner(ExpressOwner owner) {
		return getReadOnlyContext().select().from(Tables.EH_EXPRESS_SERVICE_ADDRESSES)
				.where(Tables.EH_EXPRESS_SERVICE_ADDRESSES.NAMESPACE_ID.eq(owner.getNamespaceId()))
				.and(Tables.EH_EXPRESS_SERVICE_ADDRESSES.OWNER_TYPE.eq(owner.getOwnerType().getCode()))
				.and(Tables.EH_EXPRESS_SERVICE_ADDRESSES.OWNER_ID.eq(owner.getOwnerId()))
				.and(Tables.EH_EXPRESS_SERVICE_ADDRESSES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.orderBy(Tables.EH_EXPRESS_SERVICE_ADDRESSES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ExpressServiceAddress.class));
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
