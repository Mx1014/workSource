// @formatter:off
package com.everhomes.parking;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.order.PaymentOrderRecord;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.rest.general.order.GorderPayType;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.OwnerType;
import com.everhomes.rest.parking.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ParkingProviderImpl implements ParkingProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParkingProviderImpl.class);


	@Autowired
    private SequenceProvider sequenceProvider;
    
    @Autowired
    private DbProvider dbProvider;

	@Autowired
	public com.everhomes.paySDK.api.PayService sdkPayService;
    @Override
    public ParkingVendor findParkingVendorByName(String name) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingVendors.class));
        EhParkingVendorsDao dao = new EhParkingVendorsDao(context.configuration());
        return ConvertHelper.convert(dao.fetchOneByName(name), ParkingVendor.class);
    }
    
    @Override
    public ParkingLot findParkingLotById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingLots.class));
        EhParkingLotsDao dao = new EhParkingLotsDao(context.configuration());

		ParkingLot parkingLot = ConvertHelper.convert(dao.findById(id), ParkingLot.class);

		if(parkingLot!=null) {
			populateParkingConfigInfo(parkingLot);
		}

        return parkingLot;
    }

	@Override
	public ParkingCardRequestType findParkingCardTypeByTypeId(String ownerType, Long ownerId, Long parkingLotId, String cardTypeId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingCardTypes.class));

		SelectQuery<EhParkingCardTypesRecord> query = context.selectQuery(Tables.EH_PARKING_CARD_TYPES);
		query.addConditions(Tables.EH_PARKING_CARD_TYPES.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_PARKING_CARD_TYPES.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_PARKING_CARD_TYPES.PARKING_LOT_ID.eq(parkingLotId));
		query.addConditions(Tables.EH_PARKING_CARD_TYPES.CARD_TYPE_ID.eq(cardTypeId));

		return ConvertHelper.convert(query.fetchOne(), ParkingCardRequestType.class);
	}

	@Override
	public ParkingInvoiceType findParkingInvoiceTypeById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingInvoiceTypes.class));
		EhParkingInvoiceTypesDao dao = new EhParkingInvoiceTypesDao(context.configuration());

		return ConvertHelper.convert(dao.findById(id), ParkingInvoiceType.class);
	}

	@Override
	public List<ParkingInvoiceType> listParkingInvoiceTypes(String ownerType, Long ownerId, Long parkingLotId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingInvoiceTypes.class));

		SelectQuery<EhParkingInvoiceTypesRecord> query = context.selectQuery(Tables.EH_PARKING_INVOICE_TYPES);
		if (null != parkingLotId) {
			query.addConditions(Tables.EH_PARKING_INVOICE_TYPES.PARKING_LOT_ID.eq(parkingLotId));
		}
		if(StringUtils.isNotBlank(ownerType)) {
			query.addConditions(Tables.EH_PARKING_INVOICE_TYPES.OWNER_TYPE.eq(ownerType));
		}
		if(null != ownerId) {
			query.addConditions(Tables.EH_PARKING_INVOICE_TYPES.OWNER_ID.eq(ownerId));
		}
		query.addConditions(Tables.EH_PARKING_INVOICE_TYPES.STATUS.eq(ParkingCommonStatus.ACTIVE.getCode()));
		query.addOrderBy(Tables.EH_PARKING_INVOICE_TYPES.ID.asc());
		return query.fetch().map(r -> ConvertHelper.convert(r, ParkingInvoiceType.class));
	}

	@Override
	public List<ParkingCardRequestType> listParkingCardTypes(String ownerType, Long ownerId, Long parkingLotId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingCardTypes.class));

		SelectQuery<EhParkingCardTypesRecord> query = context.selectQuery(Tables.EH_PARKING_CARD_TYPES);
		if (null != parkingLotId) {
			query.addConditions(Tables.EH_PARKING_CARD_TYPES.PARKING_LOT_ID.eq(parkingLotId));
		}
		if(StringUtils.isNotBlank(ownerType)) {
			query.addConditions(Tables.EH_PARKING_CARD_TYPES.OWNER_TYPE.eq(ownerType));
		}
		if(null != ownerId) {
			query.addConditions(Tables.EH_PARKING_CARD_TYPES.OWNER_ID.eq(ownerId));
		}

		query.addConditions(Tables.EH_PARKING_CARD_TYPES.STATUS.eq(ParkingCommonStatus.ACTIVE.getCode()));

		return query.fetch().map(r -> ConvertHelper.convert(r, ParkingCardRequestType.class));
	}
    
    @Override
    public ParkingRechargeOrder findParkingRechargeOrderById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingRechargeOrders.class));
        EhParkingRechargeOrdersDao dao = new EhParkingRechargeOrdersDao(context.configuration());
        
        return ConvertHelper.convert(dao.findById(id), ParkingRechargeOrder.class);
    }

	@Override
	public ParkingRechargeOrder findParkingRechargeOrderByBizOrderNum(String bizOrderNum) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingRechargeOrders.class));
		SelectQuery<EhParkingRechargeOrdersRecord> query = context.selectQuery(Tables.EH_PARKING_RECHARGE_ORDERS);

		query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.BIZ_ORDER_NO.eq(bizOrderNum));
		return ConvertHelper.convert(query.fetchAny(), ParkingRechargeOrder.class);
	}

	@Override
	public ParkingRechargeOrder findParkingRechargeOrderByGeneralOrderId(Long gorderId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingRechargeOrders.class));
		SelectQuery<EhParkingRechargeOrdersRecord> query = context.selectQuery(Tables.EH_PARKING_RECHARGE_ORDERS);

		query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.GENERAL_ORDER_ID.eq(String.valueOf(gorderId)));
		return ConvertHelper.convert(query.fetchAny(), ParkingRechargeOrder.class);
	}

	
	@Override
	public ParkingRechargeOrder findParkingRechargeOrderByOrderNo(Long orderNo) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingRechargeOrders.class));
		SelectQuery<EhParkingRechargeOrdersRecord> query = context.selectQuery(Tables.EH_PARKING_RECHARGE_ORDERS);

		query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.ORDER_NO.eq(orderNo));
		return ConvertHelper.convert(query.fetchAny(), ParkingRechargeOrder.class);
	}
    
    @Override
    public List<ParkingLot> listParkingLots(String ownerType, Long ownerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingLots.class));
        
        SelectQuery<EhParkingLotsRecord> query = context.selectQuery(Tables.EH_PARKING_LOTS);
        if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PARKING_LOTS.OWNER_TYPE.eq(ownerType));
        if(null != ownerId)
        	query.addConditions(Tables.EH_PARKING_LOTS.OWNER_ID.eq(ownerId));
        
        return query.fetch().map(r -> {
			ParkingLot parkingLot = ConvertHelper.convert(r, ParkingLot.class);
			populateParkingConfigInfo(parkingLot);

			return parkingLot;
		});
    }

    private void populateParkingConfigInfo(ParkingLot parkingLot) {
		String configJson = parkingLot.getConfigJson();
		ParkingLotConfig temp = JSONObject.parseObject(configJson, ParkingLotConfig.class);
		ParkingLotConfig initTemp = new ParkingLotConfig();
		BeanUtils.copyProperties(temp, initTemp);
		BeanUtils.copyProperties(initTemp, parkingLot);

//		parkingLot.setTempfeeFlag(temp.getTempfeeFlag());
//		parkingLot.setRateFlag(temp.getRateFlag());
//		parkingLot.setLockCarFlag(temp.getLockCarFlag());
//		parkingLot.setSearchCarFlag(temp.getSearchCarFlag());
//		parkingLot.setCurrentInfoType(temp.getCurrentInfoType());
//		parkingLot.setContact(temp.getContact());

		String rechargeJson = parkingLot.getRechargeJson();
		if (null != rechargeJson) {
			ParkingRechargeConfig config = JSONObject.parseObject(rechargeJson, ParkingRechargeConfig.class);
			BeanUtils.copyProperties(config, parkingLot);
//			parkingLot.setExpiredRechargeFlag(config.getExpiredRechargeFlag());
//			parkingLot.setExpiredRechargeMonthCount(config.getExpiredRechargeMonthCount());
//			parkingLot.setExpiredRechargeType(config.getExpiredRechargeType());
//			parkingLot.setMaxExpiredDay(config.getMaxExpiredDay());
		}else{
			//默认不开启
			parkingLot.setExpiredRechargeFlag(ParkingConfigFlag.NOTSUPPORT.getCode());
			parkingLot.setMonthlyDiscountFlag(ParkingConfigFlag.NOTSUPPORT.getCode());
			parkingLot.setTempFeeDiscountFlag(ParkingConfigFlag.NOTSUPPORT.getCode());
		}
	}

    @Override
    public List<ParkingRechargeRate> listParkingRechargeRates(String ownerType, Long ownerId, Long parkingLotId,
    		String cardType) {
    	
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingRechargeRates.class));
        
        SelectQuery<EhParkingRechargeRatesRecord> query = context.selectQuery(Tables.EH_PARKING_RECHARGE_RATES);
        
        query.addConditions(Tables.EH_PARKING_RECHARGE_RATES.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_PARKING_RECHARGE_RATES.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_PARKING_RECHARGE_RATES.PARKING_LOT_ID.eq(parkingLotId));
        	
        if(StringUtils.isNotBlank(cardType))
        	query.addConditions(Tables.EH_PARKING_RECHARGE_RATES.CARD_TYPE.eq(cardType));
        
        List<ParkingRechargeRate> result = query.fetch().map(r -> ConvertHelper.convert(r, ParkingRechargeRate.class));
         
        return result;
    }

	@Override
	public ParkingRechargeRate findParkingRechargeRateByMonthCount(String ownerType, Long ownerId, Long parkingLotId,
															  String cardType, BigDecimal monthCount) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingRechargeRates.class));

		SelectQuery<EhParkingRechargeRatesRecord> query = context.selectQuery(Tables.EH_PARKING_RECHARGE_RATES);

		query.addConditions(Tables.EH_PARKING_RECHARGE_RATES.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_PARKING_RECHARGE_RATES.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_PARKING_RECHARGE_RATES.PARKING_LOT_ID.eq(parkingLotId));
		query.addConditions(Tables.EH_PARKING_RECHARGE_RATES.CARD_TYPE.eq(cardType));
		query.addConditions(Tables.EH_PARKING_RECHARGE_RATES.MONTH_COUNT.eq(monthCount));

		LOGGER.debug(query.getSQL());
		LOGGER.debug(JSONObject.toJSONString(query.getBindValues()));

		return ConvertHelper.convert(query.fetchOne(), ParkingRechargeRate.class);
	}
    
    @Override
    public void createParkingRechargeRate(ParkingRechargeRate parkingRechargeRate){
    	
    	long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhParkingRechargeRates.class));
    	parkingRechargeRate.setId(id);
    	
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhParkingRechargeRatesDao dao = new EhParkingRechargeRatesDao(context.configuration());
		dao.insert(parkingRechargeRate);
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhParkingRechargeRates.class, null);
    	
    }
    
    @Override
    public void requestParkingCard(ParkingCardRequest parkingCardRequest){
    	
    	long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhParkingCardRequests.class));
    	parkingCardRequest.setId(id);
    	
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhParkingCardRequestsDao dao = new EhParkingCardRequestsDao(context.configuration());
		dao.insert(parkingCardRequest);
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhParkingCardRequests.class, null);
    	
    }
    
    @Override
    public List<ParkingCardRequest> listParkingCardRequests(Long userId, String ownerType, Long ownerId, Long parkingLotId,
    		String plateNumber, String order, Long pageAnchor, Integer pageSize){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingCardRequests.class));
        
        StringBuilder sb = new StringBuilder("");
        StringBuilder conditionSb = new StringBuilder("");
        StringBuilder condition2 = new StringBuilder("");
        //status =1 时 统计排队
        sb.append("select case e1.status when 2 then count(*) else 0 end as ranking,e1.* from eh_parking_card_requests e1 left join (select * from eh_parking_card_requests e3 where e3.status =2 ");
       // case e1.status when  1 then count(*) else 0 end
        if(userId != null)
        	conditionSb.append(" and e1.REQUESTOR_UID = ").append(userId);
        if (pageAnchor != null && pageAnchor != 0)
        	conditionSb.append(" and e1.create_time < '").append(new Timestamp(pageAnchor)).append("'");
        if(StringUtils.isNotBlank(ownerType)) {
        	conditionSb.append(" and e1.OWNER_TYPE = '").append(ownerType).append("'");
        	condition2.append(" and e3.OWNER_TYPE = '").append(ownerType).append("'");
        }
        if(ownerId != null) {
        	conditionSb.append(" and e1.OWNER_ID = ").append(ownerId);
        	condition2.append(" and e3.OWNER_ID = ").append(ownerId);
        }
        if(parkingLotId != null) {
        	conditionSb.append(" and e1.PARKING_LOT_ID = ").append(parkingLotId);
        	condition2.append(" and e3.PARKING_LOT_ID = ").append(parkingLotId);
        }
        if(StringUtils.isNotBlank(plateNumber)) {
        	conditionSb.append(" and e1.PLATE_NUMBER = '").append(plateNumber).append("'");
        	conditionSb.append(" and e1.status != 0");
        	condition2.append(" and e3.PLATE_NUMBER = '").append(plateNumber).append("'");
        }
        if(!condition2.toString().equals("")){
        	sb.append(condition2.toString());
        }
        sb.append(" ) e2 on e1.create_time >= e2.create_time ");
        if(!conditionSb.toString().equals("")){
        	sb.append(" where ").append(conditionSb.replace(0, 4, "").toString());
        }
        sb.append(" group by e1.id ");
        if(order != null)
        	sb.append(" order by ").append(order);
        else
        	sb.append(" order by e1.create_time desc");
        if(pageSize != null)
        	sb.append(" limit ").append(pageSize);
        List<ParkingCardRequest> resultList = context.fetch(sb.toString()).stream().map(r -> {
        	ParkingCardRequest p = new ParkingCardRequest();
        	p.setId(r.getValue(Tables.EH_PARKING_CARD_REQUESTS.ID));
        	p.setCreateTime(r.getValue(Tables.EH_PARKING_CARD_REQUESTS.CREATE_TIME));
        	p.setCreatorUid(r.getValue(Tables.EH_PARKING_CARD_REQUESTS.CREATOR_UID));
        	p.setIssueFlag(r.getValue(Tables.EH_PARKING_CARD_REQUESTS.ISSUE_FLAG));
        	p.setIssueTime(r.getValue(Tables.EH_PARKING_CARD_REQUESTS.ISSUE_TIME));
        	p.setOwnerId(r.getValue(Tables.EH_PARKING_CARD_REQUESTS.OWNER_ID));
        	p.setOwnerType(r.getValue(Tables.EH_PARKING_CARD_REQUESTS.OWNER_TYPE));
        	p.setParkingLotId(r.getValue(Tables.EH_PARKING_CARD_REQUESTS.PARKING_LOT_ID));
        	p.setPlateNumber(r.getValue(Tables.EH_PARKING_CARD_REQUESTS.PLATE_NUMBER));
        	p.setPlateOwnerEntperiseName(r.getValue(Tables.EH_PARKING_CARD_REQUESTS.PLATE_OWNER_ENTPERISE_NAME));
        	p.setPlateOwnerName(r.getValue(Tables.EH_PARKING_CARD_REQUESTS.PLATE_OWNER_NAME));
        	p.setPlateOwnerPhone(r.getValue(Tables.EH_PARKING_CARD_REQUESTS.PLATE_OWNER_PHONE));
        	p.setRanking(((Long) r.getValue("ranking")).intValue());
        	p.setRequestorEnterpriseId(r.getValue(Tables.EH_PARKING_CARD_REQUESTS.REQUESTOR_ENTERPRISE_ID));
        	p.setRequestorUid(r.getValue(Tables.EH_PARKING_CARD_REQUESTS.REQUESTOR_UID));
        	p.setStatus(r.getValue(Tables.EH_PARKING_CARD_REQUESTS.STATUS));
        	
        	p.setFlowCaseId(r.getValue(Tables.EH_PARKING_CARD_REQUESTS.FLOW_CASE_ID));
        	p.setFlowId(r.getValue(Tables.EH_PARKING_CARD_REQUESTS.FLOW_ID));
        	return p;
        }).collect(Collectors.toList());
        
    	return resultList;
    }
    
    @Override
    public List<ParkingCardRequest> listParkingCardRequests(Long userId, String ownerType, Long ownerId,
    		Long parkingLotId, String plateNumber, Byte requestStatus, Byte unRequestStatus, Long flowId, 
    		Long pageAnchor, Integer pageSize){

    	DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingCardRequests.class));
        SelectQuery<EhParkingCardRequestsRecord> query = context.selectQuery(Tables.EH_PARKING_CARD_REQUESTS);
        
        if(null != userId)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.REQUESTOR_UID.eq(userId));
        if(null != pageAnchor && pageAnchor != 0)
			query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.CREATE_TIME.gt(new Timestamp(pageAnchor)));
        if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.OWNER_TYPE.eq(ownerType));
        if(null != ownerId)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.OWNER_ID.eq(ownerId));
        if(null != parkingLotId)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.PARKING_LOT_ID.eq(parkingLotId));
        if(StringUtils.isNotBlank(plateNumber))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.PLATE_NUMBER.eq(plateNumber));
        if(null != requestStatus)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.STATUS.eq(requestStatus));
        if(null != unRequestStatus)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.STATUS.ne(unRequestStatus));
//        if(null != flowId)
//        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.FLOW_ID.eq(flowId));

        query.addOrderBy(Tables.EH_PARKING_CARD_REQUESTS.CREATE_TIME.asc());
        if(null != pageSize)
        	query.addLimit(pageSize);
        
        List<ParkingCardRequest> resultList = query.fetch().map(r -> ConvertHelper.convert(r, ParkingCardRequest.class));
        
    	return resultList;
    }
    
    @Override
    public Integer waitingCardCount(String ownerType, Long ownerId, Long parkingLotId, Timestamp createTime){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingCardRequests.class));
    	SelectJoinStep<Record1<Integer>> query = context.selectCount().from(Tables.EH_PARKING_CARD_REQUESTS);
        
        Condition condition = Tables.EH_PARKING_CARD_REQUESTS.OWNER_TYPE.eq(ownerType);
        condition = condition.and(Tables.EH_PARKING_CARD_REQUESTS.OWNER_ID.eq(ownerId));
        condition = condition.and(Tables.EH_PARKING_CARD_REQUESTS.PARKING_LOT_ID.eq(parkingLotId));
        condition = condition.and(Tables.EH_PARKING_CARD_REQUESTS.CREATE_TIME.lt(createTime));
        condition = condition.and(Tables.EH_PARKING_CARD_REQUESTS.STATUS.eq(ParkingCardRequestStatus.QUEUEING.getCode()));
    	return query.where(condition).fetchOneInto(Integer.class);
    }
    
    @Override
    public List<ParkingRechargeOrder> listParkingRechargeOrders(String ownerType, Long ownerId, Long parkingLotId,
    		String plateNumber, Long userId, Long pageAnchor, Integer pageSize) {
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingRechargeOrders.class));
        SelectQuery<EhParkingRechargeOrdersRecord> query = context.selectQuery(Tables.EH_PARKING_RECHARGE_ORDERS);
        
        query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.PARKING_LOT_ID.eq(parkingLotId));
        query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.IS_DELETE.eq(ParkingOrderDeleteFlag.NORMAL.getCode()));
        query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.CREATOR_UID.eq(userId));
        query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.STATUS.ge(ParkingRechargeOrderStatus.PAID.getCode()));
        
        if (pageAnchor != null && pageAnchor != 0)
			query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.CREATE_TIME.lt(new Timestamp(pageAnchor)));
        if(StringUtils.isNotBlank(plateNumber))
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.PLATE_NUMBER.eq(plateNumber));
        
        query.addOrderBy(Tables.EH_PARKING_RECHARGE_ORDERS.CREATE_TIME.desc());
        if(pageSize != null)
        	query.addLimit(pageSize);
        
        List<ParkingRechargeOrder> resultList = query.fetch().map(r -> ConvertHelper.convert(r, ParkingRechargeOrder.class));
        
    	return resultList;
    }
    
    @Override
    public List<ParkingRechargeOrder> searchParkingRechargeOrders(String ownerType, Long ownerId, Long parkingLotId,
																  String plateNumber, String plateOwnerName, String payerPhone, Timestamp startDate, Timestamp endDate,
																  Byte rechargeType, String paidType, String cardNumber, Byte status, String paySource, String keyWords, 
																  Long pageAnchor, Integer pageSize,Byte payMode) {
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingRechargeOrders.class));
        SelectQuery<EhParkingRechargeOrdersRecord> query = context.selectQuery(Tables.EH_PARKING_RECHARGE_ORDERS);
        
        query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.PARKING_LOT_ID.eq(parkingLotId));
        query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.IS_DELETE.eq(ParkingOrderDeleteFlag.NORMAL.getCode()));

        if (null != pageAnchor && pageAnchor != 0)
			query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.CREATE_TIME.lt(new Timestamp(pageAnchor)));
        if(StringUtils.isNotBlank(plateNumber))
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.PLATE_NUMBER.eq(plateNumber));
        if(StringUtils.isNotBlank(plateOwnerName))
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.PLATE_OWNER_NAME.eq(plateOwnerName));
        if(StringUtils.isNotBlank(payerPhone))
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.PAYER_PHONE.eq(payerPhone));
        if(StringUtils.isNotBlank(paidType))
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.PAID_TYPE.eq(paidType));
        if(StringUtils.isNotBlank(cardNumber))
            query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.CARD_NUMBER.like("%" + cardNumber + "%"));
        if(null != rechargeType)
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.RECHARGE_TYPE.eq(rechargeType));
        if(null != startDate)
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.RECHARGE_TIME.gt(startDate));
        if(null != endDate)
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.RECHARGE_TIME.lt(endDate));
        if(paySource !=null){
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.PAY_SOURCE.eq(paySource));
		}
		if(keyWords!=null){
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.PLATE_OWNER_NAME.like("%" + keyWords + "%")
			.or(Tables.EH_PARKING_RECHARGE_ORDERS.PLATE_NUMBER.like("%" + keyWords + "%"))
			.or(Tables.EH_PARKING_RECHARGE_ORDERS.PAYER_PHONE.like("%" + keyWords + "%")));
		}
		if(payMode!=null){
			if (payMode == GorderPayType.ENTERPRISE_PAY.getCode()){
				List<Byte> modes = new ArrayList();
				modes.add(GorderPayType.ENTERPRISE_PAY.getCode());
				modes.add(GorderPayType.WAIT_FOR_ENTERPRISE_PAY.getCode());
				query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.PAY_MODE.in(modes));
			} else
				query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.PAY_MODE.eq(payMode));
		}
        if (null != status) {
            query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.STATUS.eq(status));
        }else {
            query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.STATUS.ge(ParkingRechargeOrderStatus.PAID.getCode()));
        }

        query.addOrderBy(Tables.EH_PARKING_RECHARGE_ORDERS.CREATE_TIME.desc());
        if(null != pageSize)
        	query.addLimit(pageSize);
        
    	return query.fetch().map(r -> ConvertHelper.convert(r, ParkingRechargeOrder.class));
    }

	@Override
	public ParkingRechargeOrder getParkingRechargeTempOrder(String ownerType, Long ownerId, Long parkingLotId,
																  String plateNumber, Timestamp startDate, Timestamp endDate) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingRechargeOrders.class));
		SelectQuery<EhParkingRechargeOrdersRecord> query = context.selectQuery(Tables.EH_PARKING_RECHARGE_ORDERS);

		query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.PARKING_LOT_ID.eq(parkingLotId));
		query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.IS_DELETE.eq(ParkingOrderDeleteFlag.NORMAL.getCode()));

		query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.PLATE_NUMBER.eq(plateNumber));

		query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.CREATE_TIME.gt(startDate));
		query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.CREATE_TIME.lt(endDate));

		query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.RECHARGE_TYPE.eq(ParkingRechargeType.TEMPORARY.getCode()));
		query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.STATUS.ge(ParkingRechargeOrderStatus.RECHARGED.getCode()));

		query.addOrderBy(Tables.EH_PARKING_RECHARGE_ORDERS.CREATE_TIME.desc());
		query.addLimit(1);

		return ConvertHelper.convert(query.fetchOne(), ParkingRechargeOrder.class);
	}

    @Override
    public BigDecimal countParkingRechargeOrders(String ownerType, Long ownerId, Long parkingLotId,
												 String plateNumber, String plateOwnerName, String payerPhone, Timestamp startDate, Timestamp endDate,
												 Byte rechargeType, String paidType,String cardNumber, Byte status, String paySource, String keyWords) {
    	
    	final BigDecimal[] count = new BigDecimal[1];
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhParkingRechargeOrders.class), null, 
                (DSLContext context, Object reducingContext)-> {
                	
                    SelectJoinStep<Record1<BigDecimal>> query = context.select(Tables.EH_PARKING_RECHARGE_ORDERS.PRICE.sum())
                    		.from(Tables.EH_PARKING_RECHARGE_ORDERS);
                    
                    Condition condition = Tables.EH_PARKING_RECHARGE_ORDERS.OWNER_TYPE.eq(ownerType);
                    condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.OWNER_ID.eq(ownerId));
                    condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.PARKING_LOT_ID.eq(parkingLotId));
                    condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.IS_DELETE.eq(ParkingOrderDeleteFlag.NORMAL.getCode()));
                    
                    if(StringUtils.isNotBlank(plateNumber))
                    	condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.PLATE_NUMBER.eq(plateNumber));
                    if(StringUtils.isNotBlank(plateOwnerName))
                    	condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.PLATE_OWNER_NAME.eq(plateOwnerName));
                    if(StringUtils.isNotBlank(payerPhone))
                    	condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.PAYER_PHONE.eq(payerPhone));
                    if(StringUtils.isNotBlank(paidType))
                    	condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.PAID_TYPE.eq(paidType));
					if(StringUtils.isNotBlank(cardNumber))
						condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.CARD_NUMBER.like("%" + cardNumber + "%"));
                    if(null != rechargeType)
                    	condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.RECHARGE_TYPE.eq(rechargeType));
                    if(null != startDate)
                    	condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.CREATE_TIME.gt(startDate));
                    if(null != endDate)
                    	condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.CREATE_TIME.lt(endDate));
					if(paySource !=null){
						condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.PAY_SOURCE.eq(paySource));
					}
					if(keyWords!=null){
						condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.PLATE_OWNER_NAME.like("%" + keyWords + "%")
								.or(Tables.EH_PARKING_RECHARGE_ORDERS.PLATE_NUMBER.like("%" + keyWords + "%"))
								.or(Tables.EH_PARKING_RECHARGE_ORDERS.PAYER_PHONE.like("%" + keyWords + "%")));
					}
					if (null != status) {
						condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.STATUS.eq(status));
					}else {
						condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.STATUS.ge(ParkingRechargeOrderStatus.PAID.getCode()));
					}

					count[0] = query.where(condition).fetchOneInto(BigDecimal.class);
                	
                    return true;
                });
		return count[0];
    	
    }
    
    @Override
    public void createParkingRechargeOrder(ParkingRechargeOrder parkingRechargeOrder){
    	long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhParkingRechargeOrders.class));
    	parkingRechargeOrder.setId(id);

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhParkingRechargeOrdersDao dao = new EhParkingRechargeOrdersDao(context.configuration());
		dao.insert(parkingRechargeOrder);
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhParkingRechargeOrders.class, null);
    }
    
    @Override
    public void deleteParkingRechargeRate(ParkingRechargeRate parkingRechargeRate){
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhParkingRechargeRatesDao dao = new EhParkingRechargeRatesDao(context.configuration());
    	dao.delete(parkingRechargeRate);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhParkingRechargeRates.class, parkingRechargeRate.getId());
    }
    
    @Override
    public ParkingRechargeRate findParkingRechargeRatesById(Long id){
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhParkingRechargeRatesDao dao = new EhParkingRechargeRatesDao(context.configuration());
    	
    	return ConvertHelper.convert(dao.fetchOneById(id), ParkingRechargeRate.class);
    }
    
    @Override
    public List<ParkingCardRequest> searchParkingCardRequests(String ownerType, Long ownerId, Long parkingLotId,
                                                              String plateNumber, String plateOwnerName, String plateOwnerPhone, Timestamp startDate, Timestamp endDate,
                                                              Byte status, String carBrand, String carSeriesName, String plateOwnerEnterpriseName, Long flowId,TableField field,
                                                              int order, String cardTypeId, String ownerKeyWords,  Long pageAnchor, Integer pageSize){

    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhParkingCardRequestsRecord> query = context.selectQuery(Tables.EH_PARKING_CARD_REQUESTS);
        
        if (null != pageAnchor && pageAnchor != 0) {
        	if (order > 0)
				query.addConditions(field.gt(new Timestamp(pageAnchor)));
        	else
				query.addConditions(field.lt(new Timestamp(pageAnchor)));
		}
        if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.OWNER_TYPE.eq(ownerType));
        if(null != ownerId)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.OWNER_ID.eq(ownerId));
        if(null != parkingLotId)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.PARKING_LOT_ID.eq(parkingLotId));
        //工作流id可能会变化，去掉工作流id的条件
//        if(null != flowId)
//        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.FLOW_ID.eq(flowId));
        if(StringUtils.isNotBlank(plateNumber))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.PLATE_NUMBER.eq(plateNumber));
        if(StringUtils.isNotBlank(plateOwnerName))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.PLATE_OWNER_NAME.eq(plateOwnerName));
        if(StringUtils.isNotBlank(plateOwnerEnterpriseName))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.PLATE_OWNER_ENTPERISE_NAME.eq(plateOwnerEnterpriseName));
        if(StringUtils.isNotBlank(plateOwnerPhone))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.PLATE_OWNER_PHONE.eq(plateOwnerPhone));
        if(StringUtils.isNotBlank(carBrand))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.CAR_BRAND.eq(carBrand));
        if(StringUtils.isNotBlank(carSeriesName))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.CAR_SERIE_NAME.eq(carSeriesName));
        if(null != status)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.STATUS.eq(status));
        if(null != startDate)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.CREATE_TIME.ge(startDate));
        if(null != endDate)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.CREATE_TIME.le(endDate));
		if (StringUtils.isNotBlank(cardTypeId)) {
			query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.CARD_TYPE_ID.eq(cardTypeId));
		}
		if(StringUtils.isNotBlank(ownerKeyWords)){
			query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.PLATE_OWNER_NAME.like("%"+ownerKeyWords+"%")
			.or(Tables.EH_PARKING_CARD_REQUESTS.PLATE_OWNER_PHONE.like("%"+ownerKeyWords+"%")));
		}

        if (null != field) {
			if (order > 0)
           		 query.addOrderBy(field.asc());
			else
				query.addOrderBy(field.desc());
        }
        if(null != pageSize)
        	query.addLimit(pageSize);
        
        List<ParkingCardRequest> resultList = query.fetch().map(r -> {
			ParkingCardRequest convert = ConvertHelper.convert(r, ParkingCardRequest.class);
			if (field != null)
				convert.setAnchor((Timestamp) r.getValue(field));
			return convert;
		});
        
    	return resultList;
    }
    
	@Override
	public void updateParkingLot(ParkingLot parkingLot) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhParkingLotsDao dao = new EhParkingLotsDao(context.configuration());
		dao.update(parkingLot);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhParkingLots.class, parkingLot.getId());

	}
	
	@Override
    public ParkingCardRequest findParkingCardRequestById(Long id) {
		
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingCardRequests.class));
        EhParkingCardRequestsDao dao = new EhParkingCardRequestsDao(context.configuration());
        
        return ConvertHelper.convert(dao.fetchOneById(id), ParkingCardRequest.class);
    }
	
	@Override
    public void updateParkingCardRequest(List<ParkingCardRequest> list) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhParkingCardRequestsDao dao = new EhParkingCardRequestsDao(context.configuration());
        
        dao.update( list.stream().map(r -> ConvertHelper.convert(r, EhParkingCardRequests.class)).collect(Collectors.toList()));
    }
	
	@Override
    public void updateParkingCardRequest(ParkingCardRequest parkingCardRequest) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhParkingCardRequestsDao dao = new EhParkingCardRequestsDao(context.configuration());
        
        dao.update(parkingCardRequest);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhParkingCardRequests.class, parkingCardRequest.getId());
    }
	
	@Override
    public void updateParkingRechargeOrder(ParkingRechargeOrder order) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhParkingRechargeOrdersDao dao = new EhParkingRechargeOrdersDao(context.configuration());
        
        dao.update(order);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhParkingRechargeOrders.class, order.getId());

    }
	
    @Override
    public List<ParkingRechargeOrder> listParkingRechargeOrders(Integer pageSize, Timestamp startDate, Timestamp endDate, 
    		List<Byte> statuses, CrossShardListingLocator locator){
    	
 	    List<ParkingRechargeOrder> results = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingRechargeOrders.class));
		SelectQuery<EhParkingRechargeOrdersRecord> query = context.selectQuery(Tables.EH_PARKING_RECHARGE_ORDERS);
	        //带上逻辑删除条件
		query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.IS_DELETE.eq(ParkingOrderDeleteFlag.NORMAL.getCode()));
		if(null != locator.getAnchor())
			query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.ID.gt(locator.getAnchor()));
		if(null != startDate)
			query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.CREATE_TIME.ge(startDate));
		if(null != endDate)
			query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.CREATE_TIME.lt(endDate));
		if(null != statuses && statuses.size() > 0){
			query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.STATUS.in(statuses));
		}
		query.addOrderBy(Tables.EH_PARKING_RECHARGE_ORDERS.ID.asc());
		query.addLimit(pageSize + 1);
		query.fetch().map(r -> {
			results.add(ConvertHelper.convert(r, ParkingRechargeOrder.class));
	    	return null;
		});
	        
		locator.setAnchor(null);
		if(results.size() > pageSize){
			results.remove(results.size() - 1);
			locator.setAnchor(results.get(results.size() -1).getId());
		}
		return results;
	}
	 
	@Override
    public List<ParkingCarSerie> listParkingCarSeries(Long parentId, Long pageAnchor, Integer pageSize) {
		 
		if(null == parentId)
	    	parentId = 0L;
		 
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingCarSeries.class));
		SelectQuery<EhParkingCarSeriesRecord> query = context.selectQuery(Tables.EH_PARKING_CAR_SERIES);
	     
	    query.addConditions(Tables.EH_PARKING_CAR_SERIES.PARENT_ID.eq(parentId));
	    if(null != pageAnchor)
	    	query.addConditions(Tables.EH_PARKING_CAR_SERIES.ID.gt(pageAnchor));
	     
	    query.addOrderBy(Tables.EH_PARKING_CAR_SERIES.ID.asc());
	    if(null != pageSize)
	    	query.addLimit(pageSize);
	     
	    return query.fetch().map(r -> ConvertHelper.convert(r, ParkingCarSerie.class));
    }
	 
	@Override
    public ParkingFlow getParkingRequestCardConfig(String ownerType, Long ownerId, Long parkingLotId, Long flowId) {
		 
	    DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingFlow.class));
		SelectQuery<EhParkingFlowRecord> query = context.selectQuery(Tables.EH_PARKING_FLOW);
	     
		if(StringUtils.isNotBlank(ownerType))
			query.addConditions(Tables.EH_PARKING_FLOW.OWNER_TYPE.eq(ownerType));
	    if(null != ownerId)
	    	query.addConditions(Tables.EH_PARKING_FLOW.OWNER_ID.eq(ownerId));
	    if(null != parkingLotId)
	    	query.addConditions(Tables.EH_PARKING_FLOW.PARKING_LOT_ID.eq(parkingLotId));
	    //by dengs,如果切换工作流，这里就查不出来原来的配置，现在改成与工作流配置无关
//	    if(null != parkingLotId)
//	    	query.addConditions(Tables.EH_PARKING_FLOW.FLOW_ID.eq(flowId));
	     
	    return ConvertHelper.convert(query.fetchAny(), ParkingFlow.class);
	}
	
	@Override
    public ParkingFlow findParkingRequestCardConfig(Long id) {
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingFlow.class));
		 
		EhParkingFlowDao dao = new EhParkingFlowDao(context.configuration());
		
		return ConvertHelper.convert(dao.findById(id), ParkingFlow.class);
    }
	
	@Override
    public void updatetParkingRequestCardConfig(ParkingFlow parkingFlow) {
		 
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		 
		EhParkingFlowDao dao = new EhParkingFlowDao(context.configuration());
		dao.update(parkingFlow);
		
	    DaoHelper.publishDaoAction(DaoAction.MODIFY, EhParkingFlow.class, parkingFlow.getId());
	}
	 
	@Override
    public void createParkingRequestCardConfig(ParkingFlow parkingFlow) {
		 long id = sequenceProvider.getNextSequence(NameMapper
					.getSequenceDomainFromTablePojo(EhParkingFlow.class));
		 parkingFlow.setId(id);
		 DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		 
		 EhParkingFlowDao dao = new EhParkingFlowDao(context.configuration());
		 dao.insert(parkingFlow);
		
		 DaoHelper.publishDaoAction(DaoAction.CREATE, EhParkingFlow.class, parkingFlow.getId());
    }
	
	@Override
    public void createParkingStatistic(ParkingStatistic parkingStatistic) {
		 long id = sequenceProvider.getNextSequence(NameMapper
					.getSequenceDomainFromTablePojo(EhParkingStatistics.class));
		 parkingStatistic.setId(id);
		 DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		 
		 EhParkingStatisticsDao dao = new EhParkingStatisticsDao(context.configuration());
		 dao.insert(parkingStatistic);
		
		 DaoHelper.publishDaoAction(DaoAction.CREATE, EhParkingStatistics.class, parkingStatistic.getId());
    }
	
	@Override
    public List<ParkingStatistic> listParkingStatistics(String ownerType, Long ownerId, Long parkingLotId, Timestamp dateStr) {
		 
	    DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingFlow.class));
		SelectQuery<EhParkingStatisticsRecord> query = context.selectQuery(Tables.EH_PARKING_STATISTICS);
	     
		if(StringUtils.isNotBlank(ownerType))
			query.addConditions(Tables.EH_PARKING_STATISTICS.OWNER_TYPE.eq(ownerType));
	    if(null != ownerId)
	    	query.addConditions(Tables.EH_PARKING_STATISTICS.OWNER_ID.eq(ownerId));
	    if(null != parkingLotId)
	    	query.addConditions(Tables.EH_PARKING_STATISTICS.PARKING_LOT_ID.eq(parkingLotId));
	    if(null != dateStr)
	    	query.addConditions(Tables.EH_PARKING_STATISTICS.DATE_STR.eq(dateStr));

	    return query.fetch().map(r -> ConvertHelper.convert(r, ParkingStatistic.class));
	}
	
	@Override
    public ParkingCarSerie findParkingCarSerie(Long id) {
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingCarSeries.class));
		 
		EhParkingCarSeriesDao dao = new EhParkingCarSeriesDao(context.configuration());
		
		return ConvertHelper.convert(dao.findById(id), ParkingCarSerie.class);
    }
	
	@Override
	public void createParkingAttachment(ParkingAttachment parkingAttachment){
    	long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhParkingAttachments.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhParkingAttachmentsDao dao = new EhParkingAttachmentsDao(context.configuration());
    	parkingAttachment.setId(id);
    	dao.insert(parkingAttachment);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhParkingAttachments.class, null);
    }
	
	@Override
	public List<ParkingAttachment> listParkingAttachments(Long ownerId, String ownerType){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingAttachments.class));
        
        SelectQuery<EhParkingAttachmentsRecord> query = context.selectQuery(Tables.EH_PARKING_ATTACHMENTS);
        
        query.addConditions(Tables.EH_PARKING_ATTACHMENTS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_PARKING_ATTACHMENTS.OWNER_TYPE.eq(ownerType));
        
        List<ParkingAttachment> result = query.fetch().map(r -> ConvertHelper.convert(r, ParkingAttachment.class));
        
        return result;
	}
	
	@Override
	public Integer countParkingCardRequest(String ownerType, Long ownerId, Long parkingLotId, Long flowId, Byte geStatus, Byte status) {
        //DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTasks.class));
        final Integer[] count = new Integer[1];
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhParkingCardRequests.class), null, 
                (DSLContext context, Object reducingContext)-> {
                	
                	SelectJoinStep<Record1<Integer>> query = context.selectCount()
                			.from(Tables.EH_PARKING_CARD_REQUESTS);
                	
                	Condition condition = Tables.EH_PARKING_CARD_REQUESTS.OWNER_TYPE.equal(ownerType);
                	condition = condition.and(Tables.EH_PARKING_CARD_REQUESTS.OWNER_ID.equal(ownerId));
                	if(null != geStatus)
                		condition = Tables.EH_PARKING_CARD_REQUESTS.STATUS.ge(geStatus);
                	if(null != status)
                		condition = Tables.EH_PARKING_CARD_REQUESTS.STATUS.eq(status);
                	if(null != parkingLotId)
                    	condition = condition.and(Tables.EH_PARKING_CARD_REQUESTS.PARKING_LOT_ID.eq(parkingLotId));
//                	if(null != flowId)
//                    	condition = condition.and(Tables.EH_PARKING_CARD_REQUESTS.FLOW_ID.eq(flowId));

                    count[0] = query.where(condition).fetchOneInto(Integer.class);
                    return true;
                });
		return count[0];
	}

	@Override
	public ParkingUserInvoice findParkingUserInvoiceByUserId(String ownerType, Long ownerId, Long parkingLotId, Long userId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingUserInvoices.class));
		SelectQuery<EhParkingUserInvoicesRecord> query = context.selectQuery(Tables.EH_PARKING_USER_INVOICES);

		query.addConditions(Tables.EH_PARKING_USER_INVOICES.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_PARKING_USER_INVOICES.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_PARKING_USER_INVOICES.PARKING_LOT_ID.eq(parkingLotId));
		query.addConditions(Tables.EH_PARKING_USER_INVOICES.USER_ID.eq(userId));

		return ConvertHelper.convert(query.fetchOne(), ParkingUserInvoice.class);
	}

	@Override
	public void updateParkingUserInvoice(ParkingUserInvoice parkingUserInvoice) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhParkingUserInvoicesDao dao = new EhParkingUserInvoicesDao(context.configuration());

		parkingUserInvoice.setUpdateUid(UserContext.currentUserId());
		parkingUserInvoice.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		dao.update(parkingUserInvoice);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhParkingUserInvoices.class, null);

	}

	@Override
	public void createParkingUserInvoice(ParkingUserInvoice parkingUserInvoice) {

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhParkingUserInvoices.class));

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhParkingUserInvoicesDao dao = new EhParkingUserInvoicesDao(context.configuration());

		parkingUserInvoice.setId(id);
		parkingUserInvoice.setCreatorUid(UserContext.currentUserId());
		parkingUserInvoice.setCreateTime(new Timestamp(System.currentTimeMillis()));

		dao.insert(parkingUserInvoice);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhParkingUserInvoices.class, null);

	}

	@Override
	public List<ParkingCarVerification> searchParkingCarVerifications(String ownerType, Long ownerId, Long parkingLotId,
																	  String plateNumber, String plateOwnerName, String plateOwnerPhone,
																	  Timestamp startDate, Timestamp endDate, Byte status,
																	  String requestorEnterpriseName, String ownerKeyWords, Long pageAnchor, Integer pageSize) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingCarVerifications.class));
		SelectQuery<EhParkingCarVerificationsRecord> query = context.selectQuery(Tables.EH_PARKING_CAR_VERIFICATIONS);

		query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.PARKING_LOT_ID.eq(parkingLotId));
		query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.SOURCE_TYPE.eq(ParkingCarVerificationSourceType.CAR_VERIFICATION.getCode()));

		if (null != pageAnchor && pageAnchor != 0L) {
			query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.CREATE_TIME.lt(new Timestamp(pageAnchor)));
		}
		if (StringUtils.isNotBlank(plateNumber)) {
			query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.PLATE_NUMBER.like("%" + plateNumber + "%"));
		}
		if (StringUtils.isNotBlank(plateOwnerName)) {
			query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.PLATE_OWNER_NAME.like("%" + plateOwnerName + "%"));
		}
		if (StringUtils.isNotBlank(plateOwnerPhone)) {
			query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.PLATE_OWNER_PHONE.like("%" + plateOwnerPhone + "%"));
		}
		if (null != startDate) {
			query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.CREATE_TIME.ge(startDate));
		}
		if (null != endDate) {
			query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.CREATE_TIME.le(endDate));
		}

		if (null != status) {
			query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.STATUS.eq(status));
		}else {
			query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.STATUS.ne(ParkingCarVerificationStatus.INACTIVE.getCode())
				.and(Tables.EH_PARKING_CAR_VERIFICATIONS.STATUS.ne(ParkingCarVerificationStatus.UN_AUTHORIZED.getCode())));
		}

		if (StringUtils.isNotBlank(requestorEnterpriseName)) {
			query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.REQUESTOR_ENTERPRISE_NAME.like("%" + requestorEnterpriseName + "%"));
		}
		if (StringUtils.isNotBlank(ownerKeyWords)) {
			query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.PLATE_OWNER_PHONE.like("%" + ownerKeyWords + "%")
					.or(Tables.EH_PARKING_CAR_VERIFICATIONS.PLATE_OWNER_NAME.like("%" + ownerKeyWords + "%")));
		}

		query.addOrderBy(Tables.EH_PARKING_CAR_VERIFICATIONS.CREATE_TIME.desc());
		if (null != pageSize) {
			query.addLimit(pageSize);
		}
		return query.fetch().map(r -> ConvertHelper.convert(r, ParkingCarVerification.class));
	}

	@Override
	public List<ParkingCarVerification> listParkingCarVerifications(String ownerType, Long ownerId, Long parkingLotId,
																	  Long requestorUid, Byte sourceType, Long pageAnchor, Integer pageSize) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingCarVerifications.class));
		SelectQuery<EhParkingCarVerificationsRecord> query = context.selectQuery(Tables.EH_PARKING_CAR_VERIFICATIONS);

		query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.PARKING_LOT_ID.eq(parkingLotId));

		if (null != pageAnchor && pageAnchor != 0L) {
			query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.CREATE_TIME.lt(new Timestamp(pageAnchor)));
		}
		if (requestorUid != null) {
			query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.REQUESTOR_UID.eq(requestorUid));
		}
		if (null != sourceType) {
			query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.SOURCE_TYPE.eq(sourceType));
		}

		query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.STATUS.ne(ParkingCarVerificationStatus.INACTIVE.getCode()));

		query.addOrderBy(Tables.EH_PARKING_CAR_VERIFICATIONS.CREATE_TIME.desc());
		if (null != pageSize) {
			query.addLimit(pageSize);
		}
		return query.fetch().map(r -> ConvertHelper.convert(r, ParkingCarVerification.class));
	}

	@Override
	public ParkingCarVerification findParkingCarVerificationByUserId(String ownerType, Long ownerId, Long parkingLotId, String plateNumber,
																	 Long userId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingCarVerifications.class));
		SelectQuery<EhParkingCarVerificationsRecord> query = context.selectQuery(Tables.EH_PARKING_CAR_VERIFICATIONS);

		query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.PARKING_LOT_ID.eq(parkingLotId));
		query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.PLATE_NUMBER.eq(plateNumber));
		query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.REQUESTOR_UID.eq(userId));

		query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.STATUS.ne(ParkingCarVerificationStatus.INACTIVE.getCode()));
		query.addConditions(Tables.EH_PARKING_CAR_VERIFICATIONS.STATUS.ne(ParkingCarVerificationStatus.FAILED.getCode()));

		return ConvertHelper.convert(query.fetchOne(), ParkingCarVerification.class);

	}

	@Override
	public ParkingCarVerification findParkingCarVerificationById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhParkingCarVerificationsDao dao = new EhParkingCarVerificationsDao(context.configuration());

		return ConvertHelper.convert(dao.findById(id), ParkingCarVerification.class);

	}

	@Override
	public void updateParkingCarVerification(ParkingCarVerification parkingCarVerification) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhParkingCarVerificationsDao dao = new EhParkingCarVerificationsDao(context.configuration());

		dao.update(parkingCarVerification);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhParkingCarVerifications.class, null);

	}

	@Override
	public void createParkingCarVerification(ParkingCarVerification parkingCarVerification) {

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhParkingCarVerifications.class));

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhParkingCarVerificationsDao dao = new EhParkingCarVerificationsDao(context.configuration());

		parkingCarVerification.setId(id);

		dao.insert(parkingCarVerification);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhParkingCarVerifications.class, null);

	}

	@Override
	public ParkingSpace findParkingSpaceById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhParkingSpacesDao dao = new EhParkingSpacesDao(context.configuration());

		return ConvertHelper.convert(dao.findById(id), ParkingSpace.class);

	}

	@Override
	public ParkingSpace findParkingSpaceBySpaceNo(String spaceNo) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		List<ParkingSpace> fetch = context.select().from(Tables.EH_PARKING_SPACES)
				.where(Tables.EH_PARKING_SPACES.SPACE_NO.eq(spaceNo))
				.and(Tables.EH_PARKING_SPACES.STATUS.notEqual(ParkingSpaceStatus.DELETED.getCode()))
				.fetch().map(r->ConvertHelper.convert(r, ParkingSpace.class));
		if (fetch!=null && fetch.size()>0)
			return fetch.get(0);
		else
			return null;

	}

	@Override
	public ParkingSpace findParkingSpaceByLockId(String lockId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingSpaces.class));
		List<ParkingSpace> fetch = context.select().from(Tables.EH_PARKING_SPACES)
				.where(Tables.EH_PARKING_SPACES.LOCK_ID.eq(lockId))
				.and(Tables.EH_PARKING_SPACES.STATUS.notEqual(ParkingSpaceStatus.DELETED.getCode()))
				.fetch().map(r->ConvertHelper.convert(r, ParkingSpace.class));
		if (fetch!=null && fetch.size()>0)
			return fetch.get(0);
		else
			return null;

	}

	@Override
	public Integer countParkingSpace(Integer namespaceId, String ownerType, Long ownerId, Long parkingLotId, List<String> spaces) {
		final Integer[] count = new Integer[1];
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhParkingSpaces.class), null,
				(DSLContext context, Object reducingContext)-> {

					SelectJoinStep<Record1<Integer>> query = context.selectCount()
							.from(Tables.EH_PARKING_SPACES);

					Condition condition = Tables.EH_PARKING_SPACES.OWNER_TYPE.equal(ownerType);
					condition = condition.and(Tables.EH_PARKING_SPACES.OWNER_ID.equal(ownerId));
					condition = condition.and(Tables.EH_PARKING_SPACES.NAMESPACE_ID.eq(namespaceId));
					condition = condition.and(Tables.EH_PARKING_SPACES.PARKING_LOT_ID.eq(parkingLotId));
					condition = condition.and(Tables.EH_PARKING_SPACES.STATUS.eq(ParkingSpaceStatus.OPEN.getCode())
												.or(Tables.EH_PARKING_SPACES.STATUS.eq(ParkingSpaceStatus.IN_USING.getCode())));
					if (spaces!=null && spaces.size()>0)
						condition = condition.and(Tables.EH_PARKING_SPACES.SPACE_NO.notIn(spaces));

					count[0] = query.where(condition).fetchOneInto(Integer.class);
					return true;
				});
		return count[0];
	}

	@Override
	public ParkingSpace getAnyParkingSpace(Integer namespaceId, String ownerType, Long ownerId, Long parkingLotId, List<String> spaces) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingSpaces.class));
		SelectQuery<EhParkingSpacesRecord> query = context.selectQuery(Tables.EH_PARKING_SPACES);

		query.addConditions(Tables.EH_PARKING_SPACES.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_PARKING_SPACES.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_PARKING_SPACES.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_PARKING_SPACES.PARKING_LOT_ID.eq(parkingLotId));
		query.addConditions(Tables.EH_PARKING_SPACES.STATUS.eq(ParkingSpaceStatus.OPEN.getCode())
			.or(Tables.EH_PARKING_SPACES.STATUS.eq(ParkingSpaceStatus.IN_USING.getCode())));
		if(spaces!=null && spaces.size()>0){
			query.addConditions(Tables.EH_PARKING_SPACES.SPACE_NO.notIn(spaces));
		}
		//排序 优先取 空余的车位分配
		query.addOrderBy(Tables.EH_PARKING_SPACES.STATUS.asc());
		query.addLimit(1);

		return ConvertHelper.convert(query.fetchAny(), ParkingSpace.class);

	}

	@Override
	public ParkingSpace getAnyFreeParkingSpace(Integer namespaceId, String ownerType, Long ownerId, Long parkingLotId) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingSpaces.class));
		SelectQuery<EhParkingSpacesRecord> query = context.selectQuery(Tables.EH_PARKING_SPACES);

		query.addConditions(Tables.EH_PARKING_SPACES.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_PARKING_SPACES.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_PARKING_SPACES.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_PARKING_SPACES.PARKING_LOT_ID.eq(parkingLotId));
		query.addConditions(Tables.EH_PARKING_SPACES.STATUS.eq(ParkingSpaceStatus.OPEN.getCode()));
		//排序 优先取 空余的车位分配
		query.addLimit(1);

		return ConvertHelper.convert(query.fetchAny(), ParkingSpace.class);

	}

	@Override
	public void updateParkingSpace(ParkingSpace parkingSpace) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhParkingSpacesDao dao = new EhParkingSpacesDao(context.configuration());

		parkingSpace.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		parkingSpace.setUpdateUid(UserContext.currentUserId());

		dao.update(parkingSpace);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhParkingSpaces.class, null);

	}

	@Override
	public void createParkingSpace(ParkingSpace parkingSpace) {

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhParkingSpaces.class));

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhParkingSpacesDao dao = new EhParkingSpacesDao(context.configuration());

		parkingSpace.setId(id);
		parkingSpace.setCreateTime(new Timestamp(System.currentTimeMillis()));
		parkingSpace.setCreatorUid(UserContext.currentUserId());

		dao.insert(parkingSpace);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhParkingSpaces.class, null);

	}

	@Override
	public void createParkingSpaceLog(ParkingSpaceLog log) {

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhParkingSpaceLogs.class));

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhParkingSpaceLogsDao dao = new EhParkingSpaceLogsDao(context.configuration());

		log.setId(id);

		dao.insert(log);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhParkingSpaceLogs.class, null);

	}

	@Override
	public List<ParkingSpace> searchParkingSpaces(Integer namespaceId, String ownerType, Long ownerId, Long parkingLotId,
												  String keyword, String lockStatus,Long parkingHubsId, Long pageAnchor, Integer pageSize) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingSpaces.class));
		SelectQuery<EhParkingSpacesRecord> query = context.selectQuery(Tables.EH_PARKING_SPACES);

		query.addConditions(Tables.EH_PARKING_SPACES.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_PARKING_SPACES.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_PARKING_SPACES.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_PARKING_SPACES.PARKING_LOT_ID.eq(parkingLotId));

		if (null != pageAnchor && pageAnchor != 0L) {
			query.addConditions(Tables.EH_PARKING_SPACES.ID.gt(pageAnchor));
		}
		if (StringUtils.isNotBlank(keyword)) {
			query.addConditions(Tables.EH_PARKING_SPACES.SPACE_NO.like("%" + keyword + "%")
			.or(Tables.EH_PARKING_SPACES.LOCK_ID.like("%" + keyword + "%")));
		}
		if (StringUtils.isNotBlank(lockStatus)) {
			query.addConditions(Tables.EH_PARKING_SPACES.LOCK_STATUS.eq(lockStatus));
		}
		if (parkingHubsId!=null) {
			query.addConditions(Tables.EH_PARKING_SPACES.PARKING_HUBS_ID.eq(parkingHubsId));
		}

		query.addConditions(Tables.EH_PARKING_SPACES.STATUS.ne(ParkingSpaceStatus.DELETED.getCode()));

		query.addOrderBy(Tables.EH_PARKING_SPACES.ID.asc());
		if (null != pageSize) {
			query.addLimit(pageSize);
		}
		return query.fetch().map(r -> ConvertHelper.convert(r, ParkingSpace.class));
	}

	@Override
	public List<ParkingSpaceLog> listParkingSpaceLogs(String spaceNo, Long startTime, Long endTime, Long pageAnchor, Integer pageSize) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingSpaceLogs.class));
		SelectQuery<EhParkingSpaceLogsRecord> query = context.selectQuery(Tables.EH_PARKING_SPACE_LOGS);

		query.addConditions(Tables.EH_PARKING_SPACE_LOGS.SPACE_NO.eq(spaceNo));

		if (null != pageAnchor && pageAnchor != 0L) {
			query.addConditions(Tables.EH_PARKING_SPACE_LOGS.OPERATE_TIME.lt(new Timestamp(pageAnchor)));
		}

		if (null != startTime) {
			query.addConditions(Tables.EH_PARKING_SPACE_LOGS.OPERATE_TIME.ge(new Timestamp(startTime)));
		}
		if (null != endTime) {
			query.addConditions(Tables.EH_PARKING_SPACE_LOGS.OPERATE_TIME.le(new Timestamp(endTime)));
		}
		query.addOrderBy(Tables.EH_PARKING_SPACE_LOGS.OPERATE_TIME.desc());
		if (null != pageSize) {
			query.addLimit(pageSize);
		}
		return query.fetch().map(r -> ConvertHelper.convert(r, ParkingSpaceLog.class));
	}

	@Override
	@Cacheable(value = "createPersonalPayUserIfAbsent", key="{#userId, #accountCode}", unless="#result == null")
	public ListBizPayeeAccountDTO createPersonalPayUserIfAbsent(String userId, String accountCode,String userIdenify, String tag1, String tag2, String tag3) {
		String payerid = OwnerType.USER.getCode()+userId;
		LOGGER.info("createPersonalPayUserIfAbsent payerid = {}, accountCode = {}, userIdenify={}",payerid,accountCode,userIdenify);
		PayUserDTO payUserList = sdkPayService.createPersonalPayUserIfAbsent(payerid, accountCode);
		if(payUserList==null){
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_CREATE_USER_ACCOUNT,
					"");
		}
		String s = sdkPayService.bandPhone(payUserList.getId(), userIdenify);
		ListBizPayeeAccountDTO dto = new ListBizPayeeAccountDTO();
		dto.setAccountId(payUserList.getId());
		dto.setAccountType(payUserList.getUserType()==2? OwnerType.ORGANIZATION.getCode():OwnerType.USER.getCode());//帐号类型，1-个人帐号、2-企业帐号
		dto.setAccountName(payUserList.getUserName());
		dto.setAccountAliasName(payUserList.getUserAliasName());
		if(payUserList.getRegisterStatus()!=null) {
			dto.setAccountStatus(Byte.valueOf(payUserList.getRegisterStatus() + ""));
		}
		return dto;
	}

	@Override
	public List<PaymentOrderRecord> listParkingPaymentOrderRecords(Long pageAnchor, Integer pageSize) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPaymentOrderRecords.class));
		return context.select()
				.from(Tables.EH_PAYMENT_ORDER_RECORDS)
				.where(Tables.EH_PAYMENT_ORDER_RECORDS.ORDER_TYPE.eq("parking"))
				.and(Tables.EH_PAYMENT_ORDER_RECORDS.ID.gt(pageAnchor))
				.orderBy(Tables.EH_PAYMENT_ORDER_RECORDS.ID)
				.limit(pageSize)
				.fetch().map(r->ConvertHelper.convert(r,PaymentOrderRecord.class));
	}

	@Override
	public List<ParkingRechargeOrder> listParkingRechargeOrdersByUserId(Long userId, Long startCreateTime,Long endCreateTime,Integer pageSize, Long pageAnchor) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Condition timeCondition = DSL.trueCondition();
		if(startCreateTime!=null){
			timeCondition = timeCondition.and(Tables.EH_PARKING_RECHARGE_ORDERS.CREATE_TIME.gt(new Timestamp(startCreateTime)));
		}
		if(endCreateTime!=null){
			timeCondition = timeCondition.and(Tables.EH_PARKING_RECHARGE_ORDERS.CREATE_TIME.lt(new Timestamp(endCreateTime)));
		}
		return context.select()
				.from(Tables.EH_PARKING_RECHARGE_ORDERS)
				.where(Tables.EH_PARKING_RECHARGE_ORDERS.CREATOR_UID.eq(userId))
				.and(Tables.EH_PARKING_RECHARGE_ORDERS.INVOICE_STATUS.eq((byte)0).or(Tables.EH_PARKING_RECHARGE_ORDERS.INVOICE_STATUS.isNull()))
				.and(Tables.EH_PARKING_RECHARGE_ORDERS.STATUS.in(new ArrayList<>(
						Arrays.asList(ParkingRechargeOrderStatus.PAID.getCode(),
								ParkingRechargeOrderStatus.RECHARGED.getCode(),
								ParkingRechargeOrderStatus.FAILED.getCode()))))
				.and(timeCondition)
				.orderBy(Tables.EH_PARKING_RECHARGE_ORDERS.ID.desc())
				.limit(pageSize)
				.offset(Integer.valueOf("" + (pageAnchor * pageSize)))
				.fetch().map(r->ConvertHelper.convert(r,ParkingRechargeOrder.class));
	}

	@Override
	public Long ParkingRechargeOrdersByUserId(Long userId,Long startCreateTime,Long endCreateTime) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Condition timeCondition = DSL.trueCondition();
		if(startCreateTime!=null){
			timeCondition = timeCondition.and(Tables.EH_PARKING_RECHARGE_ORDERS.CREATE_TIME.gt(new Timestamp(startCreateTime)));
		}
		if(endCreateTime!=null){
			timeCondition = timeCondition.and(Tables.EH_PARKING_RECHARGE_ORDERS.CREATE_TIME.lt(new Timestamp(endCreateTime)));
		}
		return Long.valueOf(context.selectCount()
				.from(Tables.EH_PARKING_RECHARGE_ORDERS)
				.where(Tables.EH_PARKING_RECHARGE_ORDERS.CREATOR_UID.eq(userId))
				.and(Tables.EH_PARKING_RECHARGE_ORDERS.INVOICE_STATUS.eq((byte)0).or(Tables.EH_PARKING_RECHARGE_ORDERS.INVOICE_STATUS.isNull()))
				.and(timeCondition)
				.and(Tables.EH_PARKING_RECHARGE_ORDERS.STATUS.in(new ArrayList<>(
						Arrays.asList(ParkingRechargeOrderStatus.PAID.getCode(),
								ParkingRechargeOrderStatus.RECHARGED.getCode(),
								ParkingRechargeOrderStatus.FAILED.getCode()))))
				.fetchOneInto(Integer.class));
	}

	public List<ParkingSpace> listParkingSpaceByParkingHubsId(Integer namespaceId, String ownerType, Long ownerId, Long parkingLotId, Long parkingHubsId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingSpaces.class));
		SelectQuery<EhParkingSpacesRecord> query = context.selectQuery(Tables.EH_PARKING_SPACES);

		query.addConditions(Tables.EH_PARKING_SPACES.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_PARKING_SPACES.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_PARKING_SPACES.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_PARKING_SPACES.PARKING_LOT_ID.eq(parkingLotId));
		query.addConditions(Tables.EH_PARKING_SPACES.STATUS.ne(ParkingSpaceStatus.DELETED.getCode()));
		query.addConditions(Tables.EH_PARKING_SPACES.PARKING_HUBS_ID.eq(parkingHubsId));
		query.addOrderBy(Tables.EH_PARKING_SPACES.ID.asc());
		return query.fetch().map(r -> ConvertHelper.convert(r, ParkingSpace.class));
	}


	@Override
	public List<ParkingLot> findParkingLotByIdHash(String parkingLotToken) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingLots.class));

		SelectQuery<EhParkingLotsRecord> query = context.selectQuery(Tables.EH_PARKING_LOTS);
		if(StringUtils.isNotBlank(parkingLotToken))
			query.addConditions(Tables.EH_PARKING_LOTS.ID_HASH.like(parkingLotToken+"%"));

		return query.fetch().map(r -> {
			ParkingLot parkingLot = ConvertHelper.convert(r, ParkingLot.class);
			populateParkingConfigInfo(parkingLot);

			return parkingLot;
		});
	}

	@Override
	public ParkingRechargeOrderDTO parkingRechargeOrdersByOrderNo(long orderNo) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingLots.class));
		SelectConditionStep<Record> sql = context.select().from(Tables.EH_PARKING_RECHARGE_ORDERS)
				.where(Tables.EH_PARKING_RECHARGE_ORDERS.ORDER_NO.eq(orderNo));
		Record record = sql.fetchOne();
		return ConvertHelper.convert(record, ParkingRechargeOrderDTO.class);
	}
	
    @Override
	public String findParkingLotNameByVendorName(Integer namespaceId, String vendorName) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingLots.class));
		SelectQuery<EhParkingLotsRecord> query = context.selectQuery(Tables.EH_PARKING_LOTS);
		query.addConditions(Tables.EH_PARKING_LOTS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_PARKING_LOTS.VENDOR_NAME.eq(vendorName));
		ParkingLot lot =  query.fetchOneInto(ParkingLot.class);
		return lot == null ? null : lot.getName();
	}
}
