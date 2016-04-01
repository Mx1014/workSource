// @formatter:off
package com.everhomes.parking;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.parking.CreateParkingRechargeRateCommand;
import com.everhomes.rest.parking.ListParkingCardsCommand;
import com.everhomes.rest.parking.ListParkingLotsCommand;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.rest.techpark.park.ApplyParkingCardStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhParkingLotsDao;
import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.server.schema.tables.pojos.EhParkCharge;
import com.everhomes.server.schema.tables.pojos.EhParkingCardRequests;
import com.everhomes.server.schema.tables.pojos.EhParkingLots;
import com.everhomes.server.schema.tables.pojos.EhParkingRechargeRates;
import com.everhomes.server.schema.tables.records.EhParkChargeRecord;
import com.everhomes.server.schema.tables.records.EhParkingCardRequestsRecord;
import com.everhomes.server.schema.tables.records.EhParkingLotsRecord;
import com.everhomes.server.schema.tables.records.EhParkingRechargeRatesRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;

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
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null, 
                (DSLContext context, Object reducingContext)-> {
                	Condition condition = Tables.EH_PARK_APPLY_CARD.APPLY_STATUS.equal(ApplyParkingCardStatus.WAITING.getCode());
                	condition = condition.or(Tables.EH_PARK_APPLY_CARD.APPLY_STATUS.equal(ApplyParkingCardStatus.NOTIFIED.getCode()));
                    count[0] = context.selectCount().from(Tables.EH_PARK_APPLY_CARD)
                            .where(condition)
                            .and(Tables.EH_PARK_APPLY_CARD.PLATE_NUMBER.equal(plateNumber))
                    .fetchOneInto(Integer.class);
                    return true;
                });
		if(count[0] > 0) {
			return true;
		}
		return false;
	}
    
 }
