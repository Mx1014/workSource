// @formatter:off
package com.everhomes.reserve;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.parking.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class ReserveProviderImpl implements ReserveProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReserveProviderImpl.class);


	@Autowired
    private SequenceProvider sequenceProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Override
    public ReserveRule findReserveRuleById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhReserveRules.class));
		EhReserveRulesDao dao = new EhReserveRulesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ReserveRule.class);
    }

	@Override
	public void createReserveRule(ReserveRule reserveRule){

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhReserveRules.class));
		reserveRule.setId(id);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhReserveRulesDao dao = new EhReserveRulesDao(context.configuration());
		dao.insert(reserveRule);

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhReserveRules.class, null);

	}

	@Override
	public void updateReserveRule(ReserveRule reserveRule) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhReserveRulesDao dao = new EhReserveRulesDao(context.configuration());
		dao.update(reserveRule);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhReserveRules.class, null);

	}

	@Override
	public ReserveDiscountUser findReserveDiscountUserById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhReserveDiscountUsers.class));
		EhReserveDiscountUsersDao dao = new EhReserveDiscountUsersDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), ReserveDiscountUser.class);
	}

	@Override
	public void createReserveDiscountUser(ReserveDiscountUser reserveDiscountUser){

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhReserveDiscountUsers.class));
		reserveDiscountUser.setId(id);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhReserveDiscountUsersDao dao = new EhReserveDiscountUsersDao(context.configuration());
		dao.insert(reserveDiscountUser);

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhReserveDiscountUsers.class, null);

	}

	@Override
	public void updateReserveDiscountUser(ReserveDiscountUser reserveDiscountUser) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhReserveDiscountUsersDao dao = new EhReserveDiscountUsersDao(context.configuration());
		dao.update(reserveDiscountUser);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhReserveDiscountUsers.class, null);

	}

	@Override
	public ReserveOrderRule findReserveOrderRuleById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhReserveOrderRules.class));
		EhReserveOrderRulesDao dao = new EhReserveOrderRulesDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), ReserveOrderRule.class);
	}

	@Override
	public void createReserveOrderRule(ReserveOrderRule reserveOrderRule){

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhReserveOrderRules.class));
		reserveOrderRule.setId(id);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhReserveOrderRulesDao dao = new EhReserveOrderRulesDao(context.configuration());
		dao.insert(reserveOrderRule);

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhReserveOrderRules.class, null);

	}

	@Override
	public void updateReserveOrderRule(ReserveOrderRule reserveOrderRule) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhReserveOrderRulesDao dao = new EhReserveOrderRulesDao(context.configuration());
		dao.update(reserveOrderRule);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhReserveOrderRules.class, null);

	}

	@Override
	public void createReserveCloseDate(ReserveCloseDate reserveCloseDate){

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhReserveCloseDates.class));
		reserveCloseDate.setId(id);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhReserveCloseDatesDao dao = new EhReserveCloseDatesDao(context.configuration());
		dao.insert(reserveCloseDate);

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhReserveCloseDates.class, null);

	}

	@Override
	public void deleteReserveCloseDate(ReserveCloseDate reserveCloseDate) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhReserveCloseDatesDao dao = new EhReserveCloseDatesDao(context.configuration());
		dao.delete(reserveCloseDate);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhReserveCloseDates.class, null);

	}

	@Override
	public ReserveOrder findReserveOrderById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhReserveOrders.class));
		EhReserveOrdersDao dao = new EhReserveOrdersDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), ReserveOrder.class);
	}

	@Override
	public void createReserveOrder(ReserveOrder reserveOrder){

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhReserveOrders.class));
		reserveOrder.setId(id);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhReserveOrdersDao dao = new EhReserveOrdersDao(context.configuration());
		dao.insert(reserveOrder);

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhReserveOrders.class, null);

	}

	@Override
	public void updateReserveOrder(ReserveOrder reserveOrder) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhReserveOrdersDao dao = new EhReserveOrdersDao(context.configuration());
		dao.update(reserveOrder);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhReserveOrders.class, null);

	}

//	@Override
//	public ParkingRechargeOrder findParkingRechargeOrderByOrderNo(Long orderNo) {
//		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingRechargeOrders.class));
//		SelectQuery<EhParkingRechargeOrdersRecord> query = context.selectQuery(Tables.EH_PARKING_RECHARGE_ORDERS);
//
//		query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.ORDER_NO.eq(orderNo));
//		return ConvertHelper.convert(query.fetchAny(), ParkingRechargeOrder.class);
//	}

    @Override
    public List<ReserveOrder> searchReserveOrders(Integer namespaceId, String ownerType, Long ownerId,
												  String resourceType, Long resourceId, Long startTime, Long endTime,
												  String spaceNo, String applicantEnterpriseName, String keyword, Byte status,
												  Long pageAnchor, Integer pageSize){

    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhReserveOrdersRecord> query = context.selectQuery(Tables.EH_RESERVE_ORDERS);
        
        if (null != pageAnchor && pageAnchor != 0) {
			query.addConditions(Tables.EH_RESERVE_ORDERS.CREATE_TIME.gt(new Timestamp(pageAnchor)));
		}
        if(StringUtils.isNotBlank(ownerType)) {
			query.addConditions(Tables.EH_RESERVE_ORDERS.OWNER_TYPE.eq(ownerType));
		}
        if(null != ownerId) {
			query.addConditions(Tables.EH_RESERVE_ORDERS.OWNER_ID.eq(ownerId));
		}
        if(StringUtils.isNotBlank(resourceType)) {
			query.addConditions(Tables.EH_RESERVE_ORDERS.RESOURCE_TYPE.eq(resourceType));
		}
        if(null != resourceId) {
			query.addConditions(Tables.EH_RESERVE_ORDERS.RESOURCE_ID.eq(resourceId));
		}
		if(null != startTime) {
			query.addConditions(Tables.EH_RESERVE_ORDERS.CREATE_TIME.ge(new Timestamp(startTime)));
		}
		if(null != endTime) {
			query.addConditions(Tables.EH_RESERVE_ORDERS.CREATE_TIME.le(new Timestamp(endTime)));
		}
        if(StringUtils.isNotBlank(applicantEnterpriseName)){
			query.addConditions(Tables.EH_RESERVE_ORDERS.APPLICANT_ENTERPRISE_NAME.like("%" + applicantEnterpriseName + "%"));
		}
        if(StringUtils.isNotBlank(keyword)) {
			query.addConditions(Tables.EH_RESERVE_ORDERS.APPLICANT_NAME.like("%" + keyword + "%")
					.or(Tables.EH_RESERVE_ORDERS.APPLICANT_PHONE.like("%" + keyword + "%"))
					.or(Tables.EH_RESERVE_ORDERS.STRING_TAG1.like("%" + keyword + "%")));
		}
        if(null != status) {
			query.addConditions(Tables.EH_RESERVE_ORDERS.STATUS.eq(status));
		}
        if(null != pageSize) {
			query.addLimit(pageSize);
		}

    	return query.fetch().map(r -> ConvertHelper.convert(r, ReserveOrder.class));
    }
    

}
