// @formatter:off
package com.everhomes.parking;

import java.sql.Timestamp;
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
import com.everhomes.locale.LocaleStringService;
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
import com.everhomes.rest.parking.RequestParkingCardCommand;
import com.everhomes.rest.techpark.park.ApplyParkingCardStatus;
import com.everhomes.rest.techpark.park.FetchStatus;
import com.everhomes.rest.techpark.park.ParkingServiceErrorCode;
import com.everhomes.rest.techpark.park.PlateInfo;
import com.everhomes.rest.techpark.park.PlateNumberCommand;
import com.everhomes.techpark.park.ParkApplyCard;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
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
    
    @Autowired
	private LocaleStringService localeStringService;
    
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
            if(cmd.getOwnerId().longValue() != parkingLot.getOwnerId().longValue() 
            		|| !parkingLot.getOwnerType().equalsIgnoreCase(cmd.getOwnerType())){
            	LOGGER.error("ownerId or ownertype is not match with parking ownerId.");
    			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
    					"ownerId or ownertype is not match with parking ownerId.");
            }
            
            String venderName = parkingLot.getVendorName();
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
            if(cmd.getOwnerId().longValue() != parkingLot.getOwnerId().longValue() 
            		|| !parkingLot.getOwnerType().equalsIgnoreCase(cmd.getOwnerType())){
            	LOGGER.error("ownerId or ownertype is not match with parking ownerId.");
    			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
    					"ownerId or ownertype is not match with parking ownerId.");
            }
            
            String venderName = parkingLot.getVendorName();
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
    
    @Override
    public ParkingRechargeRateDTO createParkingRechargeRate(CreateParkingRechargeRateCommand cmd){
    	User user = UserContext.current().getUser();
    	
    	ParkingRechargeRate parkingRechargeRate = new ParkingRechargeRate();
    	parkingRechargeRate.setOwnerType(cmd.getOwnerType());
    	parkingRechargeRate.setOwnerId(cmd.getOwnerId());
    	parkingRechargeRate.setParkingLotId(cmd.getParkingLotId());
    	parkingRechargeRate.setRateName(cmd.getRateName());
    	parkingRechargeRate.setMonthCount(cmd.getMonthCount());
    	parkingRechargeRate.setPrice(cmd.getPrice());
    	parkingRechargeRate.setCreatorUid(user.getId());
    	if(parkingProvider.createParkingRechargeRate(parkingRechargeRate) > 0)
    		return ConvertHelper.convert(parkingRechargeRate, ParkingRechargeRateDTO.class);
    	
    	return null;
    }
    
    @Override
	public String requestParkingCard(RequestParkingCardCommand cmd) {
		
		
		if(cmd.getPlateNumber() == null || cmd.getPlateNumber().length() != 7) {
			LOGGER.error("the length of plateNumber is wrong.");
			throw RuntimeErrorException.errorWith(ParkingServiceErrorCode.SCOPE, ParkingServiceErrorCode.ERROR_PLATE_LENGTH,
					localeStringService.getLocalizedString(String.valueOf(ParkingServiceErrorCode.SCOPE), 
							String.valueOf(ParkingServiceErrorCode.ERROR_PLATE_LENGTH),
							UserContext.current().getUser().getLocale(),"the length of plateNumber is wrong."));
		}
		ListParkingCardsCommand listParkingCardsCommand = new ListParkingCardsCommand();
		listParkingCardsCommand.setOwnerId(cmd.getOwnerId());
		listParkingCardsCommand.setOwnerType(cmd.getOwnerType());
		listParkingCardsCommand.setParkingLotId(cmd.getParkingLotId());
		listParkingCardsCommand.setPlateNumber(cmd.getPlateNumber());
		List<ParkingCardDTO> parkingCardList = listParkingCards(listParkingCardsCommand);
		
		if(parkingCardList.size() != 0 && true == parkingCardList.get(0).getIsValid()){
			LOGGER.error("the plateNumber is already have a card.");
			throw RuntimeErrorException.errorWith(ParkingServiceErrorCode.SCOPE, ParkingServiceErrorCode.ERROR_PLATE_EXIST,
					localeStringService.getLocalizedString(String.valueOf(ParkingServiceErrorCode.SCOPE), 
							String.valueOf(ParkingServiceErrorCode.ERROR_PLATE_EXIST),
							UserContext.current().getUser().getLocale(),"the plateNumber is already have a card."));
		}
		
		if(parkingProvider.isApplied(cmd.getPlateNumber(),cmd.getParkingLotId())) {
			LOGGER.error("the plateNumber is already applied.");
			throw RuntimeErrorException.errorWith(ParkingServiceErrorCode.SCOPE, ParkingServiceErrorCode.ERROR_PLATE_APPLIED,
					localeStringService.getLocalizedString(String.valueOf(ParkingServiceErrorCode.SCOPE), 
							String.valueOf(ParkingServiceErrorCode.ERROR_PLATE_APPLIED),
							UserContext.current().getUser().getLocale(),"the plateNumber is already applied."));
		}
			
//		User user = UserContext.current().getUser();
//		List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(user.getId());
//        List<String> phones = identifiers.stream().filter((r)-> { return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.MOBILE; })
//            .map((r) -> { return r.getIdentifierToken(); })
//            .collect(Collectors.toList());
		try {
			ParkingCardRequest parkingCardRequest = new ParkingCardRequest();
			parkingCardRequest.setOwnerId(cmd.getOwnerId());
			parkingCardRequest.setOwnerType(cmd.getOwnerType());
			parkingCardRequest.setParkingLotId(cmd.getParkingLotId());
			parkingCardRequest.setRequestorEnterpriseId(cmd.getRequestorEnterpriseId());
			parkingCardRequest.setPlateNumber(cmd.getPlateNumber());
			parkingCardRequest.setPlateOwnerEntperiseName(cmd.getPlateOwnerEntperiseName());
			parkingCardRequest.setPlateOwnerName(cmd.getPlateOwnerName());
			parkingCardRequest.setPlateOwnerPhone(cmd.getPlateOwnerPhone());
			
			parkingProvider.requestParkingCard(parkingCardRequest);
//			String count = parkProvider.waitingCardCount(cmd.getCommunityId()) - 1 + "";
//			return count;
		} catch(Exception e) {
			throw RuntimeErrorException.errorWith(ParkingServiceErrorCode.SCOPE, ParkingServiceErrorCode.ERROR_PLATE_APPLIED_SERVER,
					localeStringService.getLocalizedString(String.valueOf(ParkingServiceErrorCode.SCOPE), 
							String.valueOf(ParkingServiceErrorCode.ERROR_PLATE_APPLIED_SERVER),
							UserContext.current().getUser().getLocale(),"the server is busy."));
		}
		return null;
	}
}
