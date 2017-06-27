// @formatter:off
package com.everhomes.parking;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.server.schema.tables.daos.*;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.parking.ParkingOrderDeleteFlag;
import com.everhomes.rest.parking.ParkingCardRequestStatus;
import com.everhomes.rest.parking.ParkingRechargeOrderStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhParkingAttachments;
import com.everhomes.server.schema.tables.pojos.EhParkingCarSeries;
import com.everhomes.server.schema.tables.pojos.EhParkingCardRequests;
import com.everhomes.server.schema.tables.pojos.EhParkingFlow;
import com.everhomes.server.schema.tables.pojos.EhParkingLots;
import com.everhomes.server.schema.tables.pojos.EhParkingRechargeOrders;
import com.everhomes.server.schema.tables.pojos.EhParkingRechargeRates;
import com.everhomes.server.schema.tables.pojos.EhParkingStatistics;
import com.everhomes.server.schema.tables.pojos.EhParkingVendors;
import com.everhomes.server.schema.tables.records.EhParkingAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhParkingCarSeriesRecord;
import com.everhomes.server.schema.tables.records.EhParkingCardRequestsRecord;
import com.everhomes.server.schema.tables.records.EhParkingFlowRecord;
import com.everhomes.server.schema.tables.records.EhParkingLotsRecord;
import com.everhomes.server.schema.tables.records.EhParkingRechargeOrdersRecord;
import com.everhomes.server.schema.tables.records.EhParkingRechargeRatesRecord;
import com.everhomes.server.schema.tables.records.EhParkingStatisticsRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class ParkingProviderImpl implements ParkingProvider {
    @Autowired 
    private SequenceProvider sequenceProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
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
        return ConvertHelper.convert(dao.findById(id), ParkingLot.class);
    }
    
    @Override
    public ParkingRechargeOrder findParkingRechargeOrderById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingRechargeOrders.class));
        EhParkingRechargeOrdersDao dao = new EhParkingRechargeOrdersDao(context.configuration());
        
        return ConvertHelper.convert(dao.findById(id), ParkingRechargeOrder.class);
    }
    
    @Override
    public List<ParkingLot> listParkingLots(String ownerType, Long ownerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingLots.class));
        
        SelectQuery<EhParkingLotsRecord> query = context.selectQuery(Tables.EH_PARKING_LOTS);
        if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PARKING_LOTS.OWNER_TYPE.eq(ownerType));
        if(null != ownerId)
        	query.addConditions(Tables.EH_PARKING_LOTS.OWNER_ID.eq(ownerId));
        
        return query.fetch().map(r -> ConvertHelper.convert(r, ParkingLot.class));
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
        	p.setFlowVersion(r.getValue(Tables.EH_PARKING_CARD_REQUESTS.FLOW_VERSION));
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
        if(null != flowId)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.FLOW_ID.eq(flowId));

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
    		Byte rechargeType, String paidType, String cardNumber, Byte status, Long pageAnchor, Integer pageSize) {
    	
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
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.CREATE_TIME.gt(startDate));
        if(null != endDate)
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.CREATE_TIME.lt(endDate));
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
    public BigDecimal countParkingRechargeOrders(String ownerType, Long ownerId, Long parkingLotId,
    		String plateNumber, String plateOwnerName, String payerPhone, Timestamp startDate, Timestamp endDate,
    		Byte rechargeType, String paidType) {
    	
    	final BigDecimal[] count = new BigDecimal[1];
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhParkingRechargeOrders.class), null, 
                (DSLContext context, Object reducingContext)-> {
                	
                    SelectJoinStep<Record1<BigDecimal>> query = context.select(Tables.EH_PARKING_RECHARGE_ORDERS.PRICE.sum())
                    		.from(Tables.EH_PARKING_RECHARGE_ORDERS);
                    
                    Condition condition = Tables.EH_PARKING_RECHARGE_ORDERS.OWNER_TYPE.eq(ownerType);
                    condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.OWNER_ID.eq(ownerId));
                    condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.PARKING_LOT_ID.eq(parkingLotId));
                    condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.IS_DELETE.eq(ParkingOrderDeleteFlag.NORMAL.getCode()));
                    condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.STATUS.eq(ParkingRechargeOrderStatus.RECHARGED.getCode()));
                    
                    if(StringUtils.isNotBlank(plateNumber))
                    	condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.PLATE_NUMBER.eq(plateNumber));
                    if(StringUtils.isNotBlank(plateOwnerName))
                    	condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.PLATE_OWNER_NAME.eq(plateOwnerName));
                    if(StringUtils.isNotBlank(payerPhone))
                    	condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.PAYER_PHONE.eq(payerPhone));
                    if(StringUtils.isNotBlank(paidType))
                    	condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.PAID_TYPE.eq(paidType));
                    if(null != rechargeType)
                    	condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.RECHARGE_TYPE.eq(rechargeType));
                    if(null != startDate)
                    	condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.CREATE_TIME.gt(startDate));
                    if(null != endDate)
                    	condition = condition.and(Tables.EH_PARKING_RECHARGE_ORDERS.CREATE_TIME.lt(endDate));
                    
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
                                                              Byte status, String carBrand, String carSerieName, String plateOwnerEntperiseName, Long flowId,
                                                              SortField order, Long pageAnchor, Integer pageSize){

    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhParkingCardRequestsRecord> query = context.selectQuery(Tables.EH_PARKING_CARD_REQUESTS);
        
        if (null != pageAnchor && pageAnchor != 0)
			query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.CREATE_TIME.gt(new Timestamp(pageAnchor)));
        if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.OWNER_TYPE.eq(ownerType));
        if(null != ownerId)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.OWNER_ID.eq(ownerId));
        if(null != parkingLotId)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.PARKING_LOT_ID.eq(parkingLotId));
        if(null != flowId)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.FLOW_ID.eq(flowId));
        if(StringUtils.isNotBlank(plateNumber))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.PLATE_NUMBER.eq(plateNumber));
        if(StringUtils.isNotBlank(plateOwnerName))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.PLATE_OWNER_NAME.eq(plateOwnerName));
        if(StringUtils.isNotBlank(plateOwnerEntperiseName))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.PLATE_OWNER_ENTPERISE_NAME.eq(plateOwnerEntperiseName));
        if(StringUtils.isNotBlank(plateOwnerPhone))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.PLATE_OWNER_PHONE.eq(plateOwnerPhone));
        if(StringUtils.isNotBlank(carBrand))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.CAR_BRAND.eq(carBrand));
        if(StringUtils.isNotBlank(carSerieName))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.CAR_SERIE_NAME.eq(carSerieName));
        if(null != status)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.STATUS.eq(status));
        if(null != startDate)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.CREATE_TIME.ge(startDate));
        if(null != endDate)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.CREATE_TIME.le(endDate));

        if (null != order) {
            query.addOrderBy(order);
        }
        if(null != pageSize)
        	query.addLimit(pageSize);
        
        List<ParkingCardRequest> resultList = query.fetch().map(r -> ConvertHelper.convert(r, ParkingCardRequest.class));
        
    	return resultList;
    }
    
	@Override
	public void setParkingLotConfig(ParkingLot parkingLot) {
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
	    if(null != parkingLotId)
	    	query.addConditions(Tables.EH_PARKING_FLOW.FLOW_ID.eq(flowId));
	     
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
                	if(null != flowId)
                    	condition = condition.and(Tables.EH_PARKING_CARD_REQUESTS.FLOW_ID.eq(flowId));

                    count[0] = query.where(condition).fetchOneInto(Integer.class);
                    return true;
                });
		return count[0];
	}
}
