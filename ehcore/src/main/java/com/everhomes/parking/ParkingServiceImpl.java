// @formatter:off
package com.everhomes.parking;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.acl.AclProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.parking.CreateParkingRechargeRateCommand;
import com.everhomes.rest.parking.ListParkingCardsCommand;
import com.everhomes.rest.parking.ListParkingLotsCommand;
import com.everhomes.rest.parking.ListParkingRechargeRatesCommand;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingLotDTO;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class ParkingServiceImpl implements ParkingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingServiceImpl.class);

    @Autowired
    private AclProvider aclProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private ParkingProvider parkingProvider;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Autowired
    private CoordinationProvider coordinationProvider;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private OrganizationProvider organizationProvider;
    
    @Override
    public List<ParkingCardDTO> listParkingCards(ListParkingCardsCommand cmd) {
        Long parkingLotId = cmd.getParkingLotId();
        // TODO: 检查停车场ID是否为null
        if(null == parkingLotId){
        	LOGGER.error("parkingLotId is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"parkingLotId is null.");
        }
           
        List<ParkingCardDTO> cardList = null; 
        ParkingLot parkingLot = parkingProvider.findParkingLotById(parkingLotId);
        if(parkingLot != null) {
            // TODO: 检查参数里的ownerType和ownerId是否与查出来停车场里的匹配
            if(cmd.getOwnerId() != parkingLot.getOwnerId() 
            		|| !parkingLot.getOwnerType().equalsIgnoreCase(cmd.getOwnerType())){
            	LOGGER.error("ownerId or ownertype is not match with parking ownerId.");
    			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
    					"ownerId or ownertype is not match with parking ownerId.");
            }
            
            String venderName = parkingLot.getName();
            ParkingVendorHandler handler = getParkingVendorHandler(venderName);
            cardList = handler.getParkingCardsByPlate(cmd.getOwnerType(), cmd.getOwnerId(), parkingLotId, cmd.getPlateNumber());
        }
        
        return cardList;
    }
    
    @Override
    public List<ParkingRechargeRateDTO> listParkingRechargeRates(ListParkingRechargeRatesCommand cmd){
    	Long parkingLotId = cmd.getParkingLotId();
        // TODO: 检查停车场ID是否为null
        if(null == parkingLotId){
        	LOGGER.error("parkingLotId is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"parkingLotId is null.");
        }
           
        List<ParkingRechargeRateDTO> parkingRechargeRateList = null; 
        ParkingLot parkingLot = parkingProvider.findParkingLotById(parkingLotId);
        if(parkingLot != null) {
            // TODO: 检查参数里的ownerType和ownerId是否与查出来停车场里的匹配
            if(cmd.getOwnerId() != parkingLot.getOwnerId() 
            		|| !parkingLot.getOwnerType().equalsIgnoreCase(cmd.getOwnerType())){
            	LOGGER.error("ownerId or ownertype is not match with parking ownerId.");
    			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
    					"ownerId or ownertype is not match with parking ownerId.");
            }
            
            String venderName = parkingLot.getName();
            ParkingVendorHandler handler = getParkingVendorHandler(venderName);
            parkingRechargeRateList = handler.getParkingRechargeRates(cmd.getOwnerType(), cmd.getOwnerId(), parkingLotId);
        }
        
        return parkingRechargeRateList;
    }
    
    private ParkingVendorHandler getParkingVendorHandler(String vendorName) {
        ParkingVendorHandler handler = null;
        
        if(vendorName != null && vendorName.length() > 0) {
            String handlerPrefix = ParkingVendorHandler.PARKING_VENDOR_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + vendorName);
        }
        
        return handler;
    }
    

    @Override
    public List<ParkingLotDTO> listParkingLots(ListParkingLotsCommand cmd){
    	List<ParkingLotDTO> parkingLotList = null;
    	if(cmd.getOwnerId() == null || StringUtils.isBlank(cmd.getOwnerType())){
    		LOGGER.error("ownerId or ownertype is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"ownerId or ownertype is null.");
    	}
    	
    	List<ParkingLot> list = parkingProvider.listParkingLots(cmd.getOwnerType(), cmd.getOwnerId());
    	parkingLotList = list.stream().map(r -> ConvertHelper.convert(r, ParkingLotDTO.class))
    		.collect(Collectors.toList());
    	return parkingLotList;
    }
    
    public ParkingRechargeRateDTO createParkingRechargeRate(CreateParkingRechargeRateCommand cmd){
    	ParkingRechargeRateDTO parkingRechargeRateDTO = null;
    	
    	return parkingRechargeRateDTO;
    }
}
