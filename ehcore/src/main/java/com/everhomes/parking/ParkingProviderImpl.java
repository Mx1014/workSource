// @formatter:off
package com.everhomes.parking;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.SelectQuery;
import org.jooq.SortField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.parking.ParkingCardRequestStatus;
import com.everhomes.rest.parking.SearchParkingCardRequestsCommand;
import com.everhomes.rest.parking.SearchParkingRechargeOrdersCommand;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhParkingCardRequestsDao;
import com.everhomes.server.schema.tables.daos.EhParkingLotsDao;
import com.everhomes.server.schema.tables.daos.EhParkingRechargeRatesDao;
import com.everhomes.server.schema.tables.daos.EhParkingVendorsDao;
import com.everhomes.server.schema.tables.pojos.EhParkingCardRequests;
import com.everhomes.server.schema.tables.pojos.EhParkingLots;
import com.everhomes.server.schema.tables.pojos.EhParkingRechargeOrders;
import com.everhomes.server.schema.tables.pojos.EhParkingRechargeRates;
import com.everhomes.server.schema.tables.records.EhParkingCardRequestsRecord;
import com.everhomes.server.schema.tables.records.EhParkingLotsRecord;
import com.everhomes.server.schema.tables.records.EhParkingRechargeOrdersRecord;
import com.everhomes.server.schema.tables.records.EhParkingRechargeRatesRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class ParkingProviderImpl implements ParkingProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingProviderImpl.class);
    
    @Autowired 
    private SequenceProvider sequenceProvider;
    
    @Autowired
    private CoordinationProvider coordinator;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private CoordinationProvider coordinationProvider;
    
    @Override
    public ParkingVendor findParkingVendorByName(String name) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(ParkingVendor.class));
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
    public List<ParkingLot> listParkingLots(String ownerType,Long ownerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingLots.class));
        //EhParkingLotsDao dao = new EhParkingLotsDao(context.configuration());
        
        SelectQuery<EhParkingLotsRecord> query = context.selectQuery(Tables.EH_PARKING_LOTS);
        if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PARKING_LOTS.OWNER_TYPE.eq(ownerType));
        if(ownerId != null)
        	query.addConditions(Tables.EH_PARKING_LOTS.OWNER_ID.eq(ownerId));
        
        List<ParkingLot> result = new ArrayList<ParkingLot>();
        
        result = query.fetch().map(
        		r -> ConvertHelper.convert(r, ParkingLot.class));
        return result;
    }
    
    @Override
    public List<ParkingRechargeRate> listParkingRechargeRates(String ownerType,Long ownerId,Long parkingLotId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(ParkingRechargeRate.class));
        
        SelectQuery<EhParkingRechargeRatesRecord> query = context.selectQuery(Tables.EH_PARKING_RECHARGE_RATES);
        if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PARKING_RECHARGE_RATES.OWNER_TYPE.eq(ownerType));
        if(ownerId != null)
        	query.addConditions(Tables.EH_PARKING_RECHARGE_RATES.OWNER_ID.eq(ownerId));
        if(parkingLotId != null)
        	query.addConditions(Tables.EH_PARKING_RECHARGE_RATES.PARKING_LOT_ID.eq(parkingLotId));
        
        List<ParkingRechargeRate> result = new ArrayList<ParkingRechargeRate>();
        
        result = query.fetch().map(
        		r -> ConvertHelper.convert(r, ParkingRechargeRate.class));
        return result;
    }
    
    @Override
    public int createParkingRechargeRate(ParkingRechargeRate parkingRechargeRate){
    	
    	long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhParkingRechargeRates.class));
    	parkingRechargeRate.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhParkingRechargeRatesRecord record = ConvertHelper.convert(parkingRechargeRate, EhParkingRechargeRatesRecord.class);
		InsertQuery<EhParkingRechargeRatesRecord> query =  context.insertQuery(Tables.EH_PARKING_RECHARGE_RATES);
		
		query.setRecord(record);
		int result = query.execute();
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhParkingRechargeRates.class, null);
    	
    	return result;
    }
    
    @Override
    public int requestParkingCard(ParkingCardRequest parkingCardRequest){
    	
    	long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhParkingCardRequests.class));
    	parkingCardRequest.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhParkingCardRequestsRecord record = ConvertHelper.convert(parkingCardRequest, EhParkingCardRequestsRecord.class);
		InsertQuery<EhParkingCardRequestsRecord> query =  context.insertQuery(Tables.EH_PARKING_CARD_REQUESTS);
		
		query.setRecord(record);
		int result = query.execute();
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhParkingCardRequests.class, null);
    	
    	return result;
    }
    
    @Override
	public boolean isApplied(String plateNumber,Long parkingLotId) {
		
		final Integer[] count = new Integer[1];
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhParkingCardRequests.class), null, 
                (DSLContext context, Object reducingContext)-> {
                	Condition condition = Tables.EH_PARKING_CARD_REQUESTS.STATUS.notEqual(ParkingCardRequestStatus.INACTIVE.getCode());
                	//condition = condition.or(Tables.EH_PARKING_CARD_REQUESTS.STATUS.equal(ApplyParkingCardStatus.NOTIFIED.getCode()));
                    count[0] = context.selectCount().from(Tables.EH_PARKING_CARD_REQUESTS)
                            .where(condition)
                            .and(Tables.EH_PARKING_CARD_REQUESTS.PLATE_NUMBER.equal(plateNumber))
                            .and(Tables.EH_PARKING_CARD_REQUESTS.PARKING_LOT_ID.eq(parkingLotId))
                    .fetchOneInto(Integer.class);
                    return true;
                });
		if(count[0] > 0) {
			return true;
		}
		return false;
	}
    
    @Override
    public List<ParkingCardRequest> listParkingCardRequests(String ownerType,Long ownerId
    		,Long parkingLotId,String plateNumber,ParkingCardRequestStatus status,SortField<?> order,Long pageAnchor,Integer pageSize){
    	List<ParkingCardRequest> resultList = null;
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhParkingCardRequestsRecord> query = context.selectQuery(Tables.EH_PARKING_CARD_REQUESTS);
        
        if (pageAnchor != null && pageAnchor != 0)
			query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.ID.gt(pageAnchor));
        if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.OWNER_TYPE.eq(ownerType));
        if(ownerId != null)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.OWNER_ID.eq(ownerId));
        if(parkingLotId != null)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.PARKING_LOT_ID.eq(parkingLotId));
        if(StringUtils.isNotBlank(plateNumber))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.PLATE_NUMBER.eq(plateNumber));
        if(status != null)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.STATUS.eq(status.getCode()));
        if(order != null)
        	query.addOrderBy(order);
        if(pageSize != null)
        	query.addLimit(pageSize);
        
        resultList = query.fetch().map(r -> 
			ConvertHelper.convert(r, ParkingCardRequest.class));
        
    	return resultList;
    }
    
    @Override
    public List<ParkingRechargeOrder> listParkingRechargeOrders(String ownerType,Long ownerId
    		,Long parkingLotId,String plateNumber,Long pageAnchor,Integer pageSize){
    	List<ParkingRechargeOrder> resultList = null;
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhParkingRechargeOrdersRecord> query = context.selectQuery(Tables.EH_PARKING_RECHARGE_ORDERS);
        
        if (pageAnchor != null && pageAnchor != 0)
			query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.ID.gt(pageAnchor));
        if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.OWNER_TYPE.eq(ownerType));
        if(ownerId != null)
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.OWNER_ID.eq(ownerId));
        if(parkingLotId != null)
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.PARKING_LOT_ID.eq(parkingLotId));
        if(StringUtils.isNotBlank(plateNumber))
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.PLATE_NUMBER.eq(plateNumber));
        query.addOrderBy(Tables.EH_PARKING_RECHARGE_ORDERS.ID.asc());
        if(pageSize != null)
        	query.addLimit(pageSize);
        
        resultList = query.fetch().map(r -> 
			ConvertHelper.convert(r, ParkingRechargeOrder.class));
        
    	return resultList;
    	
    }
    
    @Override
    public List<ParkingRechargeOrder> listParkingRechargeOrders(SearchParkingRechargeOrdersCommand cmd){
    	List<ParkingRechargeOrder> resultList = null;
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhParkingRechargeOrdersRecord> query = context.selectQuery(Tables.EH_PARKING_RECHARGE_ORDERS);
        
        if (cmd.getPageAnchor() != null && cmd.getPageAnchor() != 0)
			query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.ID.gt(cmd.getPageAnchor()));
        if(StringUtils.isNotBlank(cmd.getOwnerType()))
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.OWNER_TYPE.eq(cmd.getOwnerType()));
        if(cmd.getOwnerId() != null)
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.OWNER_ID.eq(cmd.getOwnerId()));
        if(cmd.getParkingLotId() != null)
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.PARKING_LOT_ID.eq(cmd.getParkingLotId()));
        if(StringUtils.isNotBlank(cmd.getPlateNumber()))
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.PLATE_NUMBER.eq(cmd.getPlateNumber()));
        if(StringUtils.isNotBlank(cmd.getPlateOwnerName()))
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.PLATE_OWNER_NAME.eq(cmd.getPlateOwnerName()));
        if(StringUtils.isNotBlank(cmd.getPlateOwnerPhone()))
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.PLATE_OWNER_PHONE.eq(cmd.getPlateOwnerPhone()));
        if(StringUtils.isNotBlank(cmd.getPayerPhone()))
        	query.addConditions(Tables.EH_PARKING_RECHARGE_ORDERS.PAYER_PHONE.eq(cmd.getPayerPhone()));
        
        if(StringUtils.isNotBlank(cmd.getPayerName())){
        	query.addJoin(Tables.EH_USERS, Tables.EH_USERS.ID.eq(Tables.EH_PARKING_RECHARGE_ORDERS.PAYER_UID));
        	query.addConditions(Tables.EH_USERS.NICK_NAME.eq(cmd.getPayerName()));
        }
        	
        
        query.addOrderBy(Tables.EH_PARKING_RECHARGE_ORDERS.ID.asc());
        if(cmd.getPageSize() != null)
        	query.addLimit(cmd.getPageSize());
        
        resultList = query.fetch().map(r -> 
			ConvertHelper.convert(r, ParkingRechargeOrder.class));
        
    	return resultList;
    	
    }
    
    @Override
    public int createParkingRechargeOrder(ParkingRechargeOrder parkingRechargeOrder){
    	long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhParkingRechargeOrders.class));
    	parkingRechargeOrder.setId(id);

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhParkingRechargeOrdersRecord record = ConvertHelper.convert(parkingRechargeOrder, EhParkingRechargeOrdersRecord.class);
		InsertQuery<EhParkingRechargeOrdersRecord> query = context.insertQuery(Tables.EH_PARKING_RECHARGE_ORDERS);
		query.setRecord(record);
		int result = query.execute();
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhParkingRechargeOrders.class, null);
		
		return result;
    }
    
    @Override
    public void deleteParkingRechargeRate(EhParkingRechargeRates ehParkingRechargeRates){
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhParkingRechargeRatesDao dao = new EhParkingRechargeRatesDao(context.configuration());
    	dao.delete(ehParkingRechargeRates);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhParkingRechargeRates.class, ehParkingRechargeRates.getId());
    }
    
    @Override
    public ParkingRechargeRate findParkingRechargeRatesById(Long id){
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhParkingRechargeRatesDao dao = new EhParkingRechargeRatesDao(context.configuration());
    	
    	return ConvertHelper.convert(dao.fetchOneById(id), ParkingRechargeRate.class);
    }
    
    @Override
    public List<ParkingCardRequest> searchParkingCardRequests(
			SearchParkingCardRequestsCommand cmd){
    	List<ParkingCardRequest> resultList = null;
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhParkingCardRequestsRecord> query = context.selectQuery(Tables.EH_PARKING_CARD_REQUESTS);
        
        if (cmd.getPageAnchor() != null && cmd.getPageAnchor() != 0)
			query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.ID.gt(cmd.getPageAnchor()));
        if(StringUtils.isNotBlank(cmd.getOwnerType()))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.OWNER_TYPE.eq(cmd.getOwnerType()));
        if(cmd.getOwnerId() != null)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.OWNER_ID.eq(cmd.getOwnerId()));
        if(cmd.getParkingLotId() != null)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.PARKING_LOT_ID.eq(cmd.getParkingLotId()));
        if(StringUtils.isNotBlank(cmd.getPlateNumber()))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.PLATE_NUMBER.eq(cmd.getPlateNumber()));
        if(StringUtils.isNotBlank(cmd.getPlateOwnerName()))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.PLATE_OWNER_NAME.eq(cmd.getPlateOwnerName()));
        if(StringUtils.isNotBlank(cmd.getPlateOwnerPhone()))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.PLATE_OWNER_PHONE.eq(cmd.getPlateOwnerPhone()));
        if(cmd.getStatus() != null)
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.STATUS.eq(cmd.getStatus()));
        if(StringUtils.isNotBlank(cmd.getStartDate())){
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.CREATE_TIME.ge(strToTimestamp(cmd.getStartDate())));
        }
        if(StringUtils.isNotBlank(cmd.getEndDate()))
        	query.addConditions(Tables.EH_PARKING_CARD_REQUESTS.CREATE_TIME.le(strToTimestamp(cmd.getEndDate())));

        query.addOrderBy(Tables.EH_PARKING_CARD_REQUESTS.ID.asc());
        if(cmd.getPageSize() != null)
        	query.addLimit(cmd.getPageSize());
        
        resultList = query.fetch().map(r -> 
			ConvertHelper.convert(r, ParkingCardRequest.class));
        
    	return resultList;
    }
    
    private Timestamp strToTimestamp(String str) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		Timestamp ts = null;
		try {
			ts = new Timestamp(sdf.parse(str).getTime());
		} catch (ParseException e) {
			LOGGER.error("validityPeriod data format is not yyyymmdd.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"validityPeriod data format is not yyyymmdd.");
		}
		
		return ts;
	}

	@Override
	public void setParkingCardReserveDays(ParkingLot parkingLot) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhParkingLotsDao dao = new EhParkingLotsDao(context.configuration());
		dao.update(parkingLot);
	}
	
	@Override
    public ParkingCardRequest findParkingCardRequestById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhParkingCardRequestsDao dao = new EhParkingCardRequestsDao(context.configuration());
        return ConvertHelper.convert(dao.fetchOneById(id), ParkingCardRequest.class);
    }
	
	@Override
    public void updateParkingCardRequest(List<ParkingCardRequest> list) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhParkingCardRequestsDao dao = new EhParkingCardRequestsDao(context.configuration());
        
        dao.update( list.stream().map(r -> ConvertHelper.convert(r, EhParkingCardRequests.class)).collect(Collectors.toList()));
    }
 }
