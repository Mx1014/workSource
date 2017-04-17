// @formatter:off
package com.everhomes.express;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.express.ListExpressOrderCondition;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhExpressOrdersDao;
import com.everhomes.server.schema.tables.pojos.EhExpressOrders;
import com.everhomes.util.ConvertHelper;

@Component
public class ExpressOrderProviderImpl implements ExpressOrderProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createExpressOrder(ExpressOrder expressOrder) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhExpressOrders.class));
		expressOrder.setId(id);
		getReadWriteDao().insert(expressOrder);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhExpressOrders.class, null);
	}

	@Override
	public void updateExpressOrder(ExpressOrder expressOrder) {
		assert (expressOrder.getId() != null);
		getReadWriteDao().update(expressOrder);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhExpressOrders.class, expressOrder.getId());
	}

	@Override
	public ExpressOrder findExpressOrderById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ExpressOrder.class);
	}
	
	@Override
	public List<ExpressOrder> listExpressOrder() {
		return getReadOnlyContext().select().from(Tables.EH_EXPRESS_ORDERS)
				.orderBy(Tables.EH_EXPRESS_ORDERS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ExpressOrder.class));
	}
	
	@Override
	public List<ExpressOrder> listExpressOrderByCondition(ListExpressOrderCondition condition) {
		SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_EXPRESS_ORDERS)
				.where(Tables.EH_EXPRESS_ORDERS.NAMESPACE_ID.eq(condition.getNamespaceId()))
				.and(Tables.EH_EXPRESS_ORDERS.OWNER_TYPE.eq(condition.getOwnerType()))
				.and(Tables.EH_EXPRESS_ORDERS.OWNER_ID.eq(condition.getOwnerId()));
		
		if (condition.getServiceAddressId() != null) {
//			step.and(Tables.EH_EXPRESS_ORDERS.ser);/
		}
		
		
		
		
//		private Long serviceAddressId;
//
//		private Long expressCompanyId;
//
//		private Byte status;
//
//		private String keyword;
//
//		private Long pageAnchor;
//
//		private Integer pageSize;
		
		return null;
	}

	private EhExpressOrdersDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhExpressOrdersDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhExpressOrdersDao getDao(DSLContext context) {
		return new EhExpressOrdersDao(context.configuration());
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
