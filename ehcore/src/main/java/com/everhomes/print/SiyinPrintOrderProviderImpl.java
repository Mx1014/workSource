// @formatter:off
package com.everhomes.print;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.SelectConditionStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSiyinPrintOrdersDao;
import com.everhomes.server.schema.tables.pojos.EhSiyinPrintOrders;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SiyinPrintOrderProviderImpl implements SiyinPrintOrderProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSiyinPrintOrder(SiyinPrintOrder siyinPrintOrder) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSiyinPrintOrders.class));
		siyinPrintOrder.setId(id);
		siyinPrintOrder.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		siyinPrintOrder.setCreatorUid(UserContext.current().getUser().getId());
//		siyinPrintOrder.setUpdateTime(siyinPrintOrder.getCreateTime());
		siyinPrintOrder.setOperatorUid(siyinPrintOrder.getCreatorUid());
		getReadWriteDao().insert(siyinPrintOrder);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSiyinPrintOrders.class, null);
	}

	@Override
	public void updateSiyinPrintOrder(SiyinPrintOrder siyinPrintOrder) {
		assert (siyinPrintOrder.getId() != null);
//		siyinPrintOrder.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		siyinPrintOrder.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(siyinPrintOrder);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSiyinPrintOrders.class, siyinPrintOrder.getId());
	}

	@Override
	public SiyinPrintOrder findSiyinPrintOrderById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SiyinPrintOrder.class);
	}
	
	@Override
	public List<SiyinPrintOrder> listSiyinPrintOrder(Timestamp startTime, Timestamp endTime, List<String> ownerTypeList,
			List<Long> ownerIdList) {
		SelectConditionStep<?> query = getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_ORDERS)
				.where(Tables.EH_SIYIN_PRINT_ORDERS.CREATE_TIME.between(startTime, endTime));
		Condition condition = null;
		for (int i = 0; i < ownerTypeList.size(); i++) {
			if(condition == null)
				condition = Tables.EH_SIYIN_PRINT_ORDERS.OWNER_TYPE.eq(ownerTypeList.get(i))
					.and(Tables.EH_SIYIN_PRINT_ORDERS.OWNER_ID.eq(ownerIdList.get(i)));
			else
				condition = condition.or(Tables.EH_SIYIN_PRINT_ORDERS.OWNER_TYPE.eq(ownerTypeList.get(i))
				.and(Tables.EH_SIYIN_PRINT_ORDERS.OWNER_ID.eq(ownerIdList.get(i))));
		}
		return query.and(condition)
		.orderBy(Tables.EH_SIYIN_PRINT_ORDERS.ID.asc())
		.fetch().map(r -> ConvertHelper.convert(r, SiyinPrintOrder.class));
	}
	
	private EhSiyinPrintOrdersDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSiyinPrintOrdersDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSiyinPrintOrdersDao getDao(DSLContext context) {
		return new EhSiyinPrintOrdersDao(context.configuration());
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
