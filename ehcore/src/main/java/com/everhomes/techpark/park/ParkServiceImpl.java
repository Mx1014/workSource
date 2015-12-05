package com.everhomes.techpark.park;

import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;






































import com.bosigao.cxf.Service1;
import com.bosigao.cxf.Service1Soap;
import com.everhomes.app.AppConstants;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessageBodyType;
import com.everhomes.messaging.MessageChannel;
import com.everhomes.messaging.MessageDTO;
import com.everhomes.messaging.MessagingConstants;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.organization.pm.pay.ResultHolder;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.onlinePay.OnlinePayBillCommand;
import com.everhomes.techpark.onlinePay.OnlinePayProvider;
import com.everhomes.techpark.onlinePay.OnlinePayService;
import com.everhomes.techpark.onlinePay.PayStatus;
import com.everhomes.techpark.onlinePay.RechargeStatus;
import com.everhomes.techpark.rental.RentalServiceErrorCode;
import com.everhomes.user.IdentifierType;
import com.everhomes.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;


@Component
public class ParkServiceImpl implements ParkService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParkServiceImpl.class);

	@Autowired
	private ParkProvider parkProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private UserProvider userProvider;

	@Autowired
	private OnlinePayProvider onlinePayProvider;
	
	@Autowired
    private ConfigurationProvider configProvider;
	
	@Autowired
	private LocaleTemplateService localeTemplateService;
	
	@Autowired
	private MessagingService messagingService;
	
	@Autowired
	private OnlinePayService onlinePayService;
	
	@Autowired
	private LocaleStringService localeStringService;
	
	private static final QName SERVICE_NAME = new QName("http://tempuri.org/", "Service1");
	
	
	@Override
	public void addCharge(CreateParkingChargeCommand cmd) {
		User user = UserContext.current().getUser();
//		Long parkId = user.getCommunityId();
		
		ParkCharge parkCharge = new ParkCharge();
		parkCharge.setMonths(cmd.getMonths());
		parkCharge.setAmount(cmd.getAmount());
		parkCharge.setCommunityId(cmd.getCommunityId());
		
		parkProvider.addCharge(parkCharge);
		
	}

	@Override
	public void deleteCharge(DeleteParkingChargeCommand cmd) {
		
		User user = UserContext.current().getUser();
//		Long parkId = user.getCommunityId();
		
		ParkCharge parkCharge = new ParkCharge();
		parkCharge.setId(cmd.getId());
		parkCharge.setMonths(cmd.getMonths());
		parkCharge.setAmount(cmd.getAmount());
		parkCharge.setCommunityId(cmd.getCommunityId());
		
		parkProvider.deleteCharge(parkCharge);
	}

	@Override
	public ParkResponseList listParkingCharge(ParkResponseListCommand cmd) {
		
		User user = UserContext.current().getUser();
//		Long parkId = user.getCommunityId();
		
		Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		int pageOffset = cmd.getPageOffset() == null ? 1: cmd.getPageOffset();
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);
        
		ParkResponseList response = new ParkResponseList();
		List<ParkCharge> parkCharge = parkProvider.listParkingChargeByEnterpriseCommunityId(cmd.getCommunityId(), offset, pageSize);
		
		if(parkCharge != null && parkCharge.size() == pageSize){
			response.setNextPageOffset(pageOffset + 1);
		}
		
		response.setParkingCharge(parkCharge.stream().map(r->{
			ParkingChargeDTO dto = ConvertHelper.convert(r, ParkingChargeDTO.class);
			return dto;
		}).collect(Collectors.toList()));
		
		return response;
	}

	@Override
	public RechargeOrderDTO createRechargeOrder(CreateRechargeOrderCommand cmd) {
		
		if(cmd.getAmount() == null || cmd.getPlateNumber() == null){
			LOGGER.error("amount or plateNumber is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"amount or plateNumber is null.");
		}
		
		User user = UserContext.current().getUser();
		List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(user.getId());
        List<String> phones = identifiers.stream().filter((r)-> { return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.MOBILE; })
            .map((r) -> { return r.getIdentifierToken(); })
            .collect(Collectors.toList());
        
		RechargeInfo order = new RechargeInfo();
		if(phones != null)
			order.setRechargePhone(phones.get(0));
		order.setNumberType((byte) 0);
		order.setOwnerName(cmd.getOwnerName());
		order.setPlateNumber(cmd.getPlateNumber());
		order.setRechargeAmount(cmd.getAmount());
		order.setRechargeMonth(cmd.getMonths());
		order.setRechargeUserid(user.getId());
		order.setRechargeUsername(user.getNickName());
		Long time = System.currentTimeMillis();
		order.setRechargeTime(new Timestamp(time));
		Long bill = onlinePayService.createBillId(time);
		order.setBillId(bill);
		order.setPaymentStatus(PayStatus.WAITING_FOR_PAY.getCode());
		order.setRechargeStatus(RechargeStatus.HANDING.getCode());
		order.setCommunityId(cmd.getCommunityId());
		order.setOldValidityperiod(addDays(cmd.getValidityPeriod(), 1));
		order.setNewValidityperiod(addMonth(cmd.getValidityPeriod(), cmd.getMonths()));;
		
		this.parkProvider.createRechargeOrder(order);
		
		RechargeOrderDTO dto = new RechargeOrderDTO();
		dto.setBillId(order.getBillId());
		dto.setAmount(order.getRechargeAmount());
		dto.setName(order.getRechargeUsername()+"的订单");
		dto.setDescription("为"+order.getPlateNumber()+"充值"+order.getRechargeMonth()+"个月");
		return dto;
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
	
	private Timestamp addDays(String oldPeriod, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(strToTimestamp(oldPeriod));
		calendar.add(Calendar.DATE, days);
		Timestamp time = new Timestamp(calendar.getTimeInMillis());
		
		return time;
	}
	
	private Timestamp addMonth(String oldPeriod, int month) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(strToTimestamp(oldPeriod));
		calendar.add(Calendar.DATE, 1);
		calendar.add(Calendar.MONTH, month);
		Timestamp newPeriod = new Timestamp(calendar.getTimeInMillis());
		
		return newPeriod;
	}

	@Override
	public RechargeSuccessResponse getRechargeStatus(Long billId) {
		
		RechargeInfo rechargeInfo = onlinePayProvider.findRechargeInfoByOrderId(billId);
		
		if(rechargeInfo == null) {
			LOGGER.error("the bill id is not in list.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the bill id is not in list.");
		}
		
		RechargeSuccessResponse rechargeResponse = new RechargeSuccessResponse();
		rechargeResponse.setBillId(rechargeInfo.getBillId());
		rechargeResponse.setPayStatus(rechargeInfo.getPaymentStatus());
		rechargeResponse.setRechargeStatus(rechargeInfo.getRechargeStatus());
		if(rechargeInfo.getRechargeStatus().byteValue() == RechargeStatus.SUCCESS.getCode()) {
			rechargeResponse.setValidityPeriod(rechargeInfo.getNewValidityperiod());
		}
		else {
			rechargeResponse.setValidityPeriod(rechargeInfo.getOldValidityperiod());
		}
		return rechargeResponse;
	}

	@Override
	public RechargeRecordList listRechargeRecord(RechargeRecordListCommand cmd) {
		
		User user = UserContext.current().getUser();
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor() == null ? 0L : cmd.getPageAnchor());
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		
		List<RechargeInfo> recordInfo = parkProvider.listRechargeRecord(cmd.getCommunityId(), user.getId(), locator, pageSize + 1);
		List<RechargeRecordDTO> rechargeRecord = new ArrayList<RechargeRecordDTO>();
		
		recordInfo.forEach(record -> {
			RechargeRecordDTO r = ConvertHelper.convert(record, RechargeRecordDTO.class);
			if(r.getRechargeStatus().byteValue() == RechargeStatus.SUCCESS.getCode()) {
				r.setValidityperiod(record.getNewValidityperiod());
			} else {
				r.setValidityperiod(record.getOldValidityperiod());
			}
			rechargeRecord.add(r);
		});
		
		RechargeRecordList list = new RechargeRecordList();
		
		if(recordInfo.size() > pageSize) {
			list.setRechargeRecord(rechargeRecord.subList(0, rechargeRecord.size()-1));
			list.setNextPageAnchor(locator.getAnchor());
		} else {
			list.setRechargeRecord(rechargeRecord);
			list.setNextPageAnchor(null);
		}
		return list;
	}

	@Override
	public String applyParkingCard(PlateNumberCommand cmd) {
		
		
		if(cmd.getPlateNumber() == null || cmd.getPlateNumber().length() != 7) {
			LOGGER.error("the length of plateNumber is wrong.");
			throw RuntimeErrorException.errorWith(ParkingServiceErrorCode.SCOPE, ParkingServiceErrorCode.ERROR_PLATE_LENGTH,
					localeStringService.getLocalizedString(String.valueOf(ParkingServiceErrorCode.SCOPE), 
							String.valueOf(ParkingServiceErrorCode.ERROR_PLATE_LENGTH),
							UserContext.current().getUser().getLocale(),"the length of plateNumber is wrong."));
		}
		PlateInfo info = verifyRechargedPlate(cmd);
		
		if(info != null && "true".equals(info.getIsValid())){
			LOGGER.error("the plateNumber is already have a card.");
			throw RuntimeErrorException.errorWith(ParkingServiceErrorCode.SCOPE, ParkingServiceErrorCode.ERROR_PLATE_EXIST,
					localeStringService.getLocalizedString(String.valueOf(ParkingServiceErrorCode.SCOPE), 
							String.valueOf(ParkingServiceErrorCode.ERROR_PLATE_EXIST),
							UserContext.current().getUser().getLocale(),"the plateNumber is already have a card."));
		}
		
		if(parkProvider.isApplied(cmd.getPlateNumber())) {
			LOGGER.error("the plateNumber is already applied.");
			throw RuntimeErrorException.errorWith(ParkingServiceErrorCode.SCOPE, ParkingServiceErrorCode.ERROR_PLATE_APPLIED,
					localeStringService.getLocalizedString(String.valueOf(ParkingServiceErrorCode.SCOPE), 
							String.valueOf(ParkingServiceErrorCode.ERROR_PLATE_APPLIED),
							UserContext.current().getUser().getLocale(),"the plateNumber is already applied."));
		}
			
		User user = UserContext.current().getUser();
		List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(user.getId());
        List<String> phones = identifiers.stream().filter((r)-> { return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.MOBILE; })
            .map((r) -> { return r.getIdentifierToken(); })
            .collect(Collectors.toList());
		ParkApplyCard apply = new ParkApplyCard();
		apply.setApplierId(user.getId());
		apply.setApplierName(user.getNickName());
		apply.setApplierPhone(phones.get(0));
		apply.setApplyStatus(ApplyParkingCardStatus.WAITING.getCode());
		apply.setApplyTime(new Timestamp(System.currentTimeMillis()));
		apply.setFetchStatus(FetchStatus.NO.getCode());
		apply.setPlateNumber(cmd.getPlateNumber());
		apply.setCommunityId(cmd.getCommunityId());
		
		parkProvider.applyParkingCard(apply);
		String count = parkProvider.waitingCardCount(cmd.getCommunityId()) - 1 + "";
		return count;
	}

	@Override
	public ApplyParkCardList searchApplyCardList(SearchApplyCardCommand cmd) {
		
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor() == null ? 0L : cmd.getPageAnchor());
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		Timestamp begin = null;
		Timestamp end = null;
		if(!StringUtils.isEmpty(cmd.getBeginDay())) {
			begin = strToTimestamp(cmd.getBeginDay());
		}
		if(!StringUtils.isEmpty(cmd.getEndDay())) {
			Timestamp time = strToTimestamp(cmd.getEndDay());
			end = addDays(cmd.getEndDay(), 1);
		}
 		List<ParkApplyCard> appliers = parkProvider.searchApply(cmd.getCommunityId(),cmd.getApplierName(), cmd.getApplierPhone(), cmd.getPlateNumber(), cmd.getApplyStatus(), begin, end, locator, pageSize + 1);
		List<ApplyParkCardDTO> applyDto = new ArrayList<ApplyParkCardDTO>();
		
		appliers.forEach(apply -> {
			applyDto.add(ConvertHelper.convert(apply, ApplyParkCardDTO.class));
		});
		
		ApplyParkCardList list = new ApplyParkCardList();
		
		if(appliers.size() > pageSize) {
			list.setApplyCard(applyDto.subList(0, applyDto.size()-1));
			list.setNextPageAnchor(locator.getAnchor());
		} else {
			list.setApplyCard(applyDto);
			list.setNextPageAnchor(null);
		}
		return list;
	}

	@Override
	public RechargeRecordList searchRechargeRecord(
			SearchRechargeRecordCommand cmd) {
		User user = UserContext.current().getUser();
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor() == null ? 0L : cmd.getPageAnchor());
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		
		List<RechargeInfo> recordInfo = parkProvider.searchRechargeRecord(cmd.getCommunityId(), cmd.getOwnerName(), cmd.getRechargePhone(), cmd.getPlateNumber(), locator, pageSize + 1);
		List<RechargeRecordDTO> rechargeRecord = new ArrayList<RechargeRecordDTO>();
		
		recordInfo.forEach(record -> {
			RechargeRecordDTO r = ConvertHelper.convert(record, RechargeRecordDTO.class);
			if(r.getRechargeStatus().byteValue() == RechargeStatus.SUCCESS.getCode()) {
				r.setValidityperiod(record.getNewValidityperiod());
			} else {
				r.setValidityperiod(record.getOldValidityperiod());
			}
			rechargeRecord.add(r);
		});
		
		RechargeRecordList list = new RechargeRecordList();
		
		if(recordInfo.size() > pageSize) {
			list.setRechargeRecord(rechargeRecord.subList(0, rechargeRecord.size()-1));
			list.setNextPageAnchor(locator.getAnchor());
		} else {
			list.setRechargeRecord(rechargeRecord);
			list.setNextPageAnchor(null);
		}
		return list;
		
	}

	@Override
	public ApplyParkCardList offerCard(OfferCardCommand cmd) {
		
		if(cmd == null || cmd.getAmount() == null || cmd.getAmount() == 0 || "".equals(cmd.getAmount())) {
			LOGGER.error(" offering cards number is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"offering cards number is null.");
		}
		
		int count = parkProvider.waitingCardCount(cmd.getCommunityId());
		
		if(cmd.getAmount() > count) {
			LOGGER.error(" offering cards number is greater than waiting people, there are only " + count + " people waiting for cards.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"offering cards number is greater than waiting people, there are only " + count + " people waiting for cards.");
		}
		
		List<ParkApplyCard> topAppliers = parkProvider.searchTopAppliers(cmd.getAmount(), cmd.getCommunityId());
		
		Map<String, Object> map = new HashMap<String, Object>();
		String deadline = deadline();
	    map.put("deadline", deadline);
		String scope = ParkNotificationTemplateCode.SCOPE;
		int code = ParkNotificationTemplateCode.USER_APPLY_CARD;
		String locale = "zh_CN";
		String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
		topAppliers.forEach(applier -> {
			sendMessageToUser(applier.getApplierId(), notifyTextForApplicant);
			applier.setApplyStatus(ApplyParkingCardStatus.NOTIFIED.getCode());
			applier.setDeadline(deadlineTimestamp(deadline));
			parkProvider.updateParkApplyCard(applier);
		});
		
		SearchApplyCardCommand search = new SearchApplyCardCommand();
		ApplyParkCardList list = searchApplyCardList(search);
		
		return list;
	}
	
	private Timestamp deadlineTimestamp(String str) {
		
		String time =str + " 18:05:00";   
		Timestamp ts = Timestamp.valueOf(time);
		
		return ts;
	}
	
	private String deadline() {
		long time = System.currentTimeMillis();

		Timestamp ts = new Timestamp(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(ts);
		Byte day = 3;
		calendar.add(Calendar.DATE, day);
		return sdf.format(calendar.getTime());
	}
	
	private void sendMessageToUser(Long userId, String content) {
//		User user = UserContext.current().getUser();
		MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
        
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
	}

	@Override
	public ApplyParkCardDTO fetchCard(FetchCardCommand cmd) {
		
		ParkApplyCard applier = parkProvider.findApplierByPhone(cmd.getApplierPhone(), cmd.getCommunityId());
		
		if(applier == null) {
			LOGGER.error("the applier is unable to fetch card now");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the applier is unable to fetch card now");
		}
		
		applier.setFetchStatus(FetchStatus.YES.getCode());
		applier.setApplyStatus(ApplyParkingCardStatus.FETCHED.getCode());
		parkProvider.updateParkApplyCard(applier);
		
		ApplyParkCardDTO result = ConvertHelper.convert(applier, ApplyParkCardDTO.class);
		
		return result;
	}

	@Scheduled(cron="0 0 2 * * ? ")
	@Override
	public void invalidApplier() {
		LOGGER.info("update invalid appliers.");
		parkProvider.updateInvalidAppliers();
	}

	@Override
	public void updateRechargeOrder(Long billId) {
		RechargeInfo order = onlinePayProvider.findRechargeInfoByOrderId(billId);
		order.setRechargeStatus(RechargeStatus.SUCCESS.getCode());
		onlinePayProvider.updateRehargeInfo(order);
	}

	private String timestampToStr(Timestamp time) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String str = sdf.format(time);
		return str;
	}
	@Override
	public RechargeSuccessResponse refreshParkingSystem(
			OnlinePayBillCommand cmd) {

		RechargeInfoDTO info = onlinePayService.onlinePayBill(cmd);
		String carNumber = info.getPlateNumber();
		String cost = (int)(info.getRechargeAmount()*100) +"";
		String flag = "2"; //停车场系统接口的传入参数，2表示是车牌号
		String payTime = info.getRechargeTime().toString();
//		response.setSign("sign");
		String validStart = timestampToStr(info.getOldValidityperiod());
		String validEnd = timestampToStr(info.getNewValidityperiod());
		
		URL wsdlURL = Service1.WSDL_LOCATION;
		
		Service1 ss = new Service1(wsdlURL, SERVICE_NAME);
        Service1Soap port = ss.getService1Soap12();
        LOGGER.info("refreshParkingSystem");
        
        String json = port.cardPayMoney("", carNumber, flag, cost, validStart, validEnd, payTime, "sign");
		
		ResultHolder resultHolder = GsonUtil.fromJson(json, ResultHolder.class);
		this.checkResultHolderIsNull(resultHolder,carNumber);
		
		if(resultHolder.isSuccess()){
			updateRechargeOrder(Long.valueOf(cmd.getOrderNo()));
		}
	
		RechargeSuccessResponse rechargeResponse = getRechargeStatus(Long.valueOf(cmd.getOrderNo()));
		return rechargeResponse;
		
	}

	@Override
	public Set<String> getRechargedPlate() {
		User user = UserContext.current().getUser();
		Set<String> plateNumber = parkProvider.getRechargedPlate(user.getId());
		return plateNumber;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PlateInfo verifyRechargedPlate(PlateNumberCommand cmd) {
		
		URL wsdlURL = Service1.WSDL_LOCATION;
		
		Service1 ss = new Service1(wsdlURL, SERVICE_NAME);
        Service1Soap port = ss.getService1Soap12();
        LOGGER.info("verifyRechargedPlate");
        String json = port.getCardInfo("", cmd.getPlateNumber(), "2", "sign");
        
        ResultHolder resultHolder = GsonUtil.fromJson(json, ResultHolder.class);
        this.checkResultHolderIsNull(resultHolder,cmd.getPlateNumber());
        
        if(LOGGER.isDebugEnabled())
			LOGGER.error("resultHolder="+resultHolder.isSuccess());

		if(resultHolder.isSuccess()){
			Map<String,Object> data = (Map<String, Object>) resultHolder.getData();
			Map<String,Object> card = (Map<String, Object>) data.get("card");
			Boolean validStatus =  (Boolean) card.get("valid");
			this.checkValidStatusIsNull(validStatus,cmd.getPlateNumber());

			if(LOGGER.isDebugEnabled())
				LOGGER.error("validStatus="+validStatus);

			if(!validStatus){
				PlateInfo command = new PlateInfo();
				command.setIsValid("false");
				
				return command;
			}
			else if(validStatus){
				String ownerName = (String) card.get("userName");
				String plateNumber = (String) card.get("carNumber");
				
				String validEnd = (String) card.get("validEnd");
				Timestamp validityPeriod = strToTimestamp(validEnd);

				PlateInfo command = new PlateInfo();
				command.setOwnerName(ownerName);
				command.setPlateNumber(plateNumber);
				command.setValidityPeriod(validityPeriod);
				command.setIsValid("true");

				if(LOGGER.isDebugEnabled())
					LOGGER.error("successcommand="+command.toString());

				return command;
			}
		}
		return null;
	}
	
	private void checkResultHolderIsNull(ResultHolder resultHolder,String plateNo) {
		if(resultHolder == null){
			LOGGER.error("remote search pay order return null.plateNo="+plateNo);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"remote search pay order return null.");
		}
	}
	
	private void checkValidStatusIsNull(Boolean validStatus,String plateNo) {
		if(validStatus == null){
			LOGGER.error("validStatus is null.plateNo="+plateNo);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"validStatus is null.");
		}
	}

	private Integer getPaymentRanking(String orderNo) {
		
		Timestamp begin = strToTimestamp(ConfigConstants.PARKING_PREFERENTIAL_STARTTIME);
		Long billId = Long.valueOf(orderNo);
		RechargeInfo rechargeInfo = onlinePayProvider.findRechargeInfoByOrderId(billId);
		if(rechargeInfo == null) {
			LOGGER.error("the bill id is not in list.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the bill id is not in list.");
		}
		Timestamp payTime = rechargeInfo.getRechargeTime();
		Timestamp end = strToTimestamp(ConfigConstants.PARKING_PREFERENTIAL_ENDTIME);
		
		if(payTime.after(end)) {
			return 0;
		}
		
		int count = parkProvider.getPaymentRanking(payTime, begin);
		
		return count;
	}

	@Override
	public String rechargeTop(OnlinePayBillCommand cmd) {
		RechargeInfoDTO info = onlinePayService.onlinePayBill(cmd);
		
		if(cmd.getPayStatus().toLowerCase().equals("fail")) {
			return "payFailed";
		}
		if(cmd.getPayStatus().toLowerCase().equals("success")) {
			String carNumber = info.getPlateNumber();
			String cost = (int)(info.getRechargeAmount()*100) +"";
			String flag = "2"; //停车场系统接口的传入参数，2表示是车牌号
			String payTime = info.getRechargeTime().toString();
			String validStart = timestampToStr(info.getOldValidityperiod());
			String validEnd = timestampToStr(info.getNewValidityperiod());
			
			URL wsdlURL = Service1.WSDL_LOCATION;
			
			Service1 ss = new Service1(wsdlURL, SERVICE_NAME);
	        Service1Soap port = ss.getService1Soap12();
	        LOGGER.info("refreshParkingSystem");
	        
	        String json = port.cardPayMoney("", carNumber, flag, cost, validStart, validEnd, payTime, "sign");
			
			ResultHolder resultHolder = GsonUtil.fromJson(json, ResultHolder.class);
			this.checkResultHolderIsNull(resultHolder,carNumber);
			
			if(resultHolder.isSuccess()){
				updateRechargeOrder(Long.valueOf(cmd.getOrderNo()));
			}
			
			int ranking = getPaymentRanking(cmd.getOrderNo());
			
			int range = Integer.valueOf(configurationProvider.getValue(ConfigConstants.PARKING_PREFERENTIAL_RANGE, "0"));
			
			if(ranking == 0)
				return "the activity was finished";
			
			if(ranking <= range)
				return "in the range";
			
		}
		return "out of range";
	}
	
	@Scheduled(cron="0 0 2 * * ? ")
	@Override
	public void refreshRechargeStatus() {
		LOGGER.info("refresh recharge status.");
		List<RechargeInfo> rechargeInfo = parkProvider.findPaysuccessAndWaitingrefreshInfo();
		rechargeInfo.stream().map(info -> {
			String carNumber = info.getPlateNumber();
			String cost = (int)(info.getRechargeAmount()*100) +"";
			String flag = "2"; //停车场系统接口的传入参数，2表示是车牌号
			String payTime = info.getRechargeTime().toString();
			String validStart = timestampToStr(info.getOldValidityperiod());
			String validEnd = timestampToStr(info.getNewValidityperiod());
			
			URL wsdlURL = Service1.WSDL_LOCATION;
			
			Service1 ss = new Service1(wsdlURL, SERVICE_NAME);
	        Service1Soap port = ss.getService1Soap12();
	        LOGGER.info("refreshParkingSystem");
	        
	        String json = port.cardPayMoney("", carNumber, flag, cost, validStart, validEnd, payTime, "sign");
			
			ResultHolder resultHolder = GsonUtil.fromJson(json, ResultHolder.class);
			this.checkResultHolderIsNull(resultHolder,carNumber);
			
			if(resultHolder.isSuccess()){
				updateRechargeOrder(info.getBillId());
			}
			return null;
		});
		
	}
}
