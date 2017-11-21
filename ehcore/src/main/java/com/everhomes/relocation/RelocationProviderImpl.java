// @formatter:off
package com.everhomes.relocation;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.parking.*;
import com.everhomes.rest.parking.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RelocationProviderImpl implements RelocationProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(RelocationProviderImpl.class);


	@Autowired
    private SequenceProvider sequenceProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Override
    public RelocationRequest findRelocationRequestById(Long id) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhRelocationRequests.class));
		EhRelocationRequestsDao dao = new EhRelocationRequestsDao(context.configuration());

        return ConvertHelper.convert(dao.findById(id), RelocationRequest.class);
    }

//	@Override
//	public ParkingCardRequestType findParkingCardTypeByTypeId(String ownerType, Long ownerId, Long parkingLotId, String cardTypeId) {
//		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingCardTypes.class));
//
//		SelectQuery<EhParkingCardTypesRecord> query = context.selectQuery(Tables.EH_PARKING_CARD_TYPES);
//		query.addConditions(Tables.EH_PARKING_CARD_TYPES.OWNER_TYPE.eq(ownerType));
//		query.addConditions(Tables.EH_PARKING_CARD_TYPES.OWNER_ID.eq(ownerId));
//		query.addConditions(Tables.EH_PARKING_CARD_TYPES.PARKING_LOT_ID.eq(parkingLotId));
//		query.addConditions(Tables.EH_PARKING_CARD_TYPES.CARD_TYPE_ID.eq(cardTypeId));
//
//		return ConvertHelper.convert(query.fetchOne(), ParkingCardRequestType.class);
//	}
    
    @Override
    public void createRelocationRequest(RelocationRequest request){
    	
    	long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRelocationRequests.class));
		request.setId(id);
    	request.setCreateTime(new Timestamp(System.currentTimeMillis()));
    	request.setCreatorUid(UserContext.currentUserId());

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRelocationRequestsDao dao = new EhRelocationRequestsDao(context.configuration());
		dao.insert(request);
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRelocationRequests.class, null);
    	
    }

	@Override
	public void updateRelocationRequest(RelocationRequest request){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRelocationRequestsDao dao = new EhRelocationRequestsDao(context.configuration());
		dao.update(request);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRelocationRequests.class, null);

	}

    @Override
    public List<ParkingRechargeOrder> searchRelocationRequests(Integer namespaceId, String keyword, Long startDate,
															   Long endDate, Byte status, Long pageAnchor, Integer pageSize) {
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingRechargeOrders.class));
        SelectQuery<EhParkingRechargeOrdersRecord> query = context.selectQuery(Tables.EH_PARKING_RECHARGE_ORDERS);
        
        query.addConditions(Tables.EH_RELOCATION_REQUESTS.NAMESPACE_ID.eq(namespaceId));

        if (null != pageAnchor && pageAnchor != 0) {
			query.addConditions(Tables.EH_RELOCATION_REQUESTS.CREATE_TIME.lt(new Timestamp(pageAnchor)));
		}
        if(StringUtils.isNotBlank(keyword)) {
			query.addConditions(Tables.EH_RELOCATION_REQUESTS.REQUESTOR_ENTPERISE_NAME.like("%" + keyword + "%")
									.or(Tables.EH_RELOCATION_REQUESTS.REQUESTOR_NAME.like("%" + keyword + "%"))
									.or(Tables.EH_RELOCATION_REQUESTS.CONTACT_PHONE.like("%" + keyword + "%")));
		}
        if(null != startDate) {
			query.addConditions(Tables.EH_RELOCATION_REQUESTS.CREATE_TIME.gt(new Timestamp(startDate)));
		}
        if(null != endDate)
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.CREATE_TIME.lt(new Timestamp(endDate)));
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
                                                              Byte status, String carBrand, String carSeriesName, String plateOwnerEnterpriseName, Long flowId,
                                                              SortField order, String cardTypeId,  Long pageAnchor, Integer pageSize){

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

        if (null != order) {
            query.addOrderBy(order);
        }
        if(null != pageSize)
        	query.addLimit(pageSize);
        
        List<ParkingCardRequest> resultList = query.fetch().map(r -> ConvertHelper.convert(r, ParkingCardRequest.class));
        
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
																	  String requestorEnterpriseName, Long pageAnchor, Integer pageSize) {
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
}
