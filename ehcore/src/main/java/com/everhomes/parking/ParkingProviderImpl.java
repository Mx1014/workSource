// @formatter:off
package com.everhomes.parking;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.parking.ListParkingCardsCommand;
import com.everhomes.rest.parking.ListParkingLotsCommand;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhParkingLotsDao;
import com.everhomes.server.schema.tables.pojos.EhParkingLots;
import com.everhomes.server.schema.tables.records.EhParkChargeRecord;
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
    
 }
