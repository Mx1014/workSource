package com.everhomes.techpark.park;

import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.namespace.QName;

import com.alibaba.fastjson.JSONObject;
import com.bosigao.cxf.rest.BosigaoCardInfo;
import com.everhomes.rest.parking.*;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bosigao.cxf.Service1;
import com.bosigao.cxf.Service1Soap;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.organization.pm.pay.ResultHolder;
import com.everhomes.parking.ParkingCardRequest;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingRechargeRate;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.techpark.onlinePay.OnlinePayBillCommand;
import com.everhomes.rest.techpark.onlinePay.PayStatus;
import com.everhomes.rest.techpark.onlinePay.RechargeStatus;
import com.everhomes.rest.techpark.park.ApplyParkCardDTO;
import com.everhomes.rest.techpark.park.ApplyParkCardList;
import com.everhomes.rest.techpark.park.ApplyParkingCardStatus;
import com.everhomes.rest.techpark.park.CreateParkingChargeCommand;
import com.everhomes.rest.techpark.park.CreateRechargeOrderCommand;
import com.everhomes.rest.techpark.park.DeleteParkingChargeCommand;
import com.everhomes.rest.techpark.park.FetchCardCommand;
import com.everhomes.rest.techpark.park.FetchStatus;
import com.everhomes.rest.techpark.park.GetAllCardDescriptDTO;
import com.everhomes.rest.techpark.park.ListCardTypeCommand;
import com.everhomes.rest.techpark.park.ListCardTypeResponse;
import com.everhomes.rest.techpark.park.OfferCardCommand;
import com.everhomes.rest.techpark.park.ParkNotificationTemplateCode;
import com.everhomes.rest.techpark.park.ParkResponseList;
import com.everhomes.rest.techpark.park.ParkResponseListCommand;
import com.everhomes.rest.techpark.park.ParkingChargeDTO;
import com.everhomes.rest.techpark.park.ParkingServiceErrorCode;
import com.everhomes.rest.techpark.park.PaymentRankingCommand;
import com.everhomes.rest.techpark.park.PlateInfo;
import com.everhomes.rest.techpark.park.PlateNumberCommand;
import com.everhomes.rest.techpark.park.PreferentialRulesDTO;
import com.everhomes.rest.techpark.park.QryPreferentialRuleByCommunityIdCommand;
import com.everhomes.rest.techpark.park.RechargeInfoDTO;
import com.everhomes.rest.techpark.park.RechargeOrderDTO;
import com.everhomes.rest.techpark.park.RechargeRecordDTO;
import com.everhomes.rest.techpark.park.RechargeRecordList;
import com.everhomes.rest.techpark.park.RechargeRecordListCommand;
import com.everhomes.rest.techpark.park.RechargeSuccessResponse;
import com.everhomes.rest.techpark.park.SearchApplyCardCommand;
import com.everhomes.rest.techpark.park.SearchRechargeRecordCommand;
import com.everhomes.rest.techpark.park.SetPreferentialRuleCommand;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.onlinePay.OnlinePayProvider;
import com.everhomes.techpark.onlinePay.OnlinePayService;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;


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
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private AppProvider appProvider;
	
	@Autowired
    private ParkingProvider parkingProvider;
	
	private static final QName SERVICE_NAME = new QName("http://tempuri.org/", "Service1");
	
	
	@Override
	public void addCharge(CreateParkingChargeCommand cmd) {
		User user = UserContext.current().getUser();
//		Long parkId = user.getCommunityId();
		
		ParkCharge parkCharge = new ParkCharge();
		parkCharge.setCardType(cmd.getCardType());
		parkCharge.setMonths(cmd.getMonths());
		parkCharge.setAmount(cmd.getAmount());
		parkCharge.setCommunityId(cmd.getCommunityId());
		
		parkProvider.addCharge(parkCharge);
		
	}

	@Override
	public void deleteCharge(DeleteParkingChargeCommand cmd) {
		
		User user = UserContext.current().getUser();
//		Long parkId = user.getCommunityId();
		
		ParkCharge parkCharge =  parkProvider.findParkingChargeById(cmd.getId());
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
		List<ParkCharge> parkCharge = parkProvider.listParkingChargeByEnterpriseCommunityId(cmd.getCommunityId(),cmd.getCardType(), offset, pageSize);
		
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
		order.setCardType( cmd.getCardType());
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

		BosigaoCardInfo card = getCardInfo(order.getPlateNumber());
		String oldValidEnd = card.getValidEnd();
		Long validEnd = strToLong(oldValidEnd);
		order.setOldValidityperiod(addDays(validEnd, 1));
		order.setNewValidityperiod(addMonth(validEnd, cmd.getMonths()));

		this.parkProvider.createRechargeOrder(order);
		//向2.0版本的表中添加充值数据
		ParkingRechargeOrder parkingRechargeOrder = new ParkingRechargeOrder();		
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		
		Long parkingLotId = configProvider.getLongValue("parking.parkingLotId", 10001L);
		ParkingLot parkingLot = parkingProvider.findParkingLotById(parkingLotId);
		try{
		parkingRechargeOrder.setOwnerType(ParkingOwnerType.COMMUNITY.getCode());
		Long ownerId = configProvider.getLongValue("parking.communityId", 240111044331048623L);
		parkingRechargeOrder.setOwnerId(ownerId);
		parkingRechargeOrder.setParkingLotId(parkingLot.getId());
		parkingRechargeOrder.setPlateNumber(cmd.getPlateNumber());
		parkingRechargeOrder.setPlateOwnerName(cmd.getOwnerName());
		parkingRechargeOrder.setPlateOwnerPhone(null);
		parkingRechargeOrder.setPayerEnterpriseId(null);
		parkingRechargeOrder.setPayerUid(user.getId());
		parkingRechargeOrder.setPayerPhone(userIdentifier.getIdentifierToken());
		parkingRechargeOrder.setVendorName(parkingLot.getVendorName());
		ParkingRechargeRate rate = parkProvider.findParkingRechargeRates(ParkingOwnerType.COMMUNITY.getCode(), ownerId, parkingLotId,
				new BigDecimal(cmd.getMonths()), new BigDecimal(cmd.getAmount()));
		//parkingRechargeOrder.setCardNumber(cmd.getCardNumber());
		parkingRechargeOrder.setRateToken(String.valueOf(rate.getId()));
		parkingRechargeOrder.setRateName(rate.getRateName());
		parkingRechargeOrder.setMonthCount(new BigDecimal(cmd.getMonths()));
		parkingRechargeOrder.setPrice(new BigDecimal(cmd.getAmount()));
		parkingRechargeOrder.setStatus(ParkingRechargeOrderStatus.UNPAID.getCode());
		parkingRechargeOrder.setRechargeStatus(ParkingRechargeOrderRechargeStatus.UNRECHARGED.getCode());
		parkingRechargeOrder.setCreatorUid(user.getId());
		parkingRechargeOrder.setCreateTime(new Timestamp(time));
		parkingRechargeOrder.setOrderNo(bill);
//		parkingRechargeOrder.setNewExpiredTime(addMonth(cmd.getValidityPeriod(), cmd.getMonths()));
//		parkingRechargeOrder.setOldExpiredTime(addDays(cmd.getValidityPeriod(), 1));
		parkingRechargeOrder.setRechargeType(ParkingRechargeType.MONTHLY.getCode());
		
		parkingProvider.createParkingRechargeOrder(parkingRechargeOrder);
		}catch(Exception e) {
			LOGGER.error("createParkingRechargeOrder is fail. {}",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"createParkingRechargeOrder is fail.");
		}
		
		RechargeOrderDTO dto = new RechargeOrderDTO();
		dto.setBillId(order.getBillId());
		dto.setAmount(order.getRechargeAmount());
		dto.setName(order.getRechargeUsername()+"的订单");
		dto.setDescription("为"+order.getPlateNumber()+"充值"+order.getRechargeMonth()+"个月");
		//签名
		this.setSignatureParam(dto);
		return dto;
	}
	
	private void setSignatureParam(RechargeOrderDTO dto) {
		String appKey = configurationProvider.getValue("pay.appKey", "7bbb5727-9d37-443a-a080-55bbf37dc8e1");
		Long timestamp = System.currentTimeMillis();
		Integer randomNum = (int) (Math.random()*1000);
		App app = appProvider.findAppByKey(appKey);
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("appKey",appKey);
		map.put("timestamp",timestamp+"");
		map.put("randomNum",randomNum+"");
		map.put("amount",dto.getAmount().doubleValue()+"");
		String signature = SignatureHelper.computeSignature(map, app.getSecretKey());
		dto.setAppKey(appKey);
		dto.setRandomNum(randomNum);
		dto.setSignature(URLEncoder.encode(signature));
		dto.setTimestamp(timestamp);
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

	private Timestamp addDays(Long oldPeriod, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(oldPeriod);
		calendar.add(Calendar.DATE, days);
		Timestamp time = new Timestamp(calendar.getTimeInMillis());

		return time;
	}

	private Timestamp addMonth(Long oldPeriod, int month) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(oldPeriod);

		int day = calendar.get(Calendar.DAY_OF_MONTH);
		if(day == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){
			calendar.add(Calendar.MONTH, month);
			int d = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, d);
		}else{
			calendar.add(Calendar.MONTH, month-1);
			int d = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, d);
		}

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

		LOGGER.error("Not support card request", cmd);
		throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_APPLY_CARD,
				"Not support card request");
		
//		if(cmd.getPlateNumber() == null || cmd.getPlateNumber().length() != 7) {
//			LOGGER.error("the length of plateNumber is wrong.");
//			throw RuntimeErrorException.errorWith(ParkingServiceErrorCode.SCOPE, ParkingServiceErrorCode.ERROR_PLATE_LENGTH,
//					localeStringService.getLocalizedString(String.valueOf(ParkingServiceErrorCode.SCOPE),
//							String.valueOf(ParkingServiceErrorCode.ERROR_PLATE_LENGTH),
//							UserContext.current().getUser().getLocale(),"the length of plateNumber is wrong."));
//		}
//		PlateInfo info = verifyRechargedPlate(cmd);
//
//		if(info != null && "true".equals(info.getIsValid())){
//			LOGGER.error("the plateNumber is already have a card.");
//			throw RuntimeErrorException.errorWith(ParkingServiceErrorCode.SCOPE, ParkingServiceErrorCode.ERROR_PLATE_EXIST,
//					localeStringService.getLocalizedString(String.valueOf(ParkingServiceErrorCode.SCOPE),
//							String.valueOf(ParkingServiceErrorCode.ERROR_PLATE_EXIST),
//							UserContext.current().getUser().getLocale(),"the plateNumber is already have a card."));
//		}
//
//		if(parkProvider.isApplied(cmd.getPlateNumber())) {
//			LOGGER.error("the plateNumber is already applied.");
//			throw RuntimeErrorException.errorWith(ParkingServiceErrorCode.SCOPE, ParkingServiceErrorCode.ERROR_PLATE_APPLIED,
//					localeStringService.getLocalizedString(String.valueOf(ParkingServiceErrorCode.SCOPE),
//							String.valueOf(ParkingServiceErrorCode.ERROR_PLATE_APPLIED),
//							UserContext.current().getUser().getLocale(),"the plateNumber is already applied."));
//		}
//
////		User user = UserContext.current().getUser();
////		List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(user.getId());
////        List<String> phones = identifiers.stream().filter((r)-> { return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.MOBILE; })
////            .map((r) -> { return r.getIdentifierToken(); })
////            .collect(Collectors.toList());
//		try {
//			ParkApplyCard apply = new ParkApplyCard();
//			apply.setApplierId(cmd.getUserId());
//			apply.setApplierName(cmd.getUserName());
//			apply.setApplierPhone(cmd.getPhoneNumber());
//			apply.setCompanyName(cmd.getCompanyName());
//			apply.setApplyStatus(ApplyParkingCardStatus.WAITING.getCode());
//			apply.setApplyTime(new Timestamp(System.currentTimeMillis()));
//			apply.setFetchStatus(FetchStatus.NO.getCode());
//			apply.setPlateNumber(cmd.getPlateNumber());
//			apply.setCommunityId(cmd.getCommunityId());
//
//			parkProvider.applyParkingCard(apply);
//			//兼容1.0版本，想2.0版本中添加申请数据
//			ParkingCardRequest parkingCardRequest = new ParkingCardRequest();
//			User user = UserContext.current().getUser();
//			Long parkingLotId = configProvider.getLongValue("parking.parkingLotId", 10001L);
//			ParkingLot parkingLot = parkingProvider.findParkingLotById(parkingLotId);
//
//			Long ownerId = configProvider.getLongValue("parking.communityId", 240111044331048623L);
//			parkingCardRequest.setOwnerId(ownerId);
//			parkingCardRequest.setOwnerType(ParkingOwnerType.COMMUNITY.getCode());
//			parkingCardRequest.setParkingLotId(parkingLot.getId());
//			//parkingCardRequest.setRequestorEnterpriseId(cmd.getRequestorEnterpriseId());
//			parkingCardRequest.setPlateNumber(cmd.getPlateNumber());
//			parkingCardRequest.setPlateOwnerEntperiseName(cmd.getCompanyName());
//			parkingCardRequest.setPlateOwnerName(cmd.getUserName());
//			parkingCardRequest.setPlateOwnerPhone(cmd.getPhoneNumber());
//			parkingCardRequest.setRequestorUid(user.getId());
//			//设置一些初始状态
//			parkingCardRequest.setIssueFlag(ParkingCardIssueFlag.UNISSUED.getCode());
//			parkingCardRequest.setStatus(ParkingCardRequestStatus.QUEUEING.getCode());
//			parkingCardRequest.setCreatorUid(user.getId());
//			parkingCardRequest.setCreateTime(new Timestamp(System.currentTimeMillis()));
//
//			parkingProvider.requestParkingCard(parkingCardRequest);
//			Integer count = parkingProvider.waitingCardCount(ParkingOwnerType.COMMUNITY.getCode(),
//					ownerId, parkingLot.getId(), parkingCardRequest.getCreateTime());
//			//String count = parkProvider.waitingCardCount(cmd.getCommunityId()) - 1 + "";
//			return String.valueOf(count);
//		} catch(Exception e) {
//			throw RuntimeErrorException.errorWith(ParkingServiceErrorCode.SCOPE, ParkingServiceErrorCode.ERROR_PLATE_APPLIED_SERVER,
//					localeStringService.getLocalizedString(String.valueOf(ParkingServiceErrorCode.SCOPE),
//							String.valueOf(ParkingServiceErrorCode.ERROR_PLATE_APPLIED_SERVER),
//							UserContext.current().getUser().getLocale(),"the server is busy."));
//		}
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
//			end = addDays(cmd.getEndDay(), 1);
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
		
		List<RechargeInfo> recordInfo = parkProvider.searchRechargeRecord(cmd.getStartTime(), cmd.getEndTime(), cmd.getRechargeStatus(), cmd.getCommunityId(), 
																			cmd.getOwnerName(), cmd.getRechargePhone(), cmd.getPlateNumber(), locator, pageSize + 1);
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
		if(info.getRechargeStatus() != RechargeStatus.SUCCESS.getCode()) {
			if(cmd.getPayStatus().toLowerCase().equals("fail")) {
				LOGGER.error("pay failed.orderNo ="+cmd.getOrderNo());
			}
			else {
				String carNumber = info.getPlateNumber();
				String cost = (int)(info.getRechargeAmount()*100) +"";
				String flag = "2"; //停车场系统接口的传入参数，2表示是车牌号
				String payTime = info.getRechargeTime().toString();
//				String validStart = timestampToStr(info.getOldValidityperiod());
//				String validEnd = timestampToStr(info.getNewValidityperiod());

				BosigaoCardInfo card = getCardInfo(carNumber);
				String oldValidEnd = card.getValidEnd();
				Long time = strToLong(oldValidEnd);

				String validStart = timestampToStr(addDays(time, 1));
				String validEnd = timestampToStr(addMonth(time, info.getRechargeMonth().intValue()));

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
			}
		}
		
		RechargeSuccessResponse rechargeResponse = getRechargeStatus(Long.valueOf(cmd.getOrderNo()));
		//PayStatus.WAITING_FOR_PAY.getCode();
		//RechargeStatus.HANDING.getCode();
		ParkingRechargeOrder order = parkProvider.findParkingRechargeOrderByOrderNo(Long.valueOf(cmd.getOrderNo()));
		//1.0订单
		RechargeInfo order_1_0 = onlinePayProvider.findRechargeInfoByOrderId(Long.valueOf(cmd.getOrderNo()));
		Timestamp payTimeStamp = new Timestamp(System.currentTimeMillis());
		order.setStatus(order_1_0.getPaymentStatus());
		if(order_1_0.getRechargeStatus().equals(RechargeStatus.INACTIVE.getCode())){
			order.setRechargeStatus(ParkingRechargeOrderRechargeStatus.NONE.getCode());
		}
		if(order_1_0.getRechargeStatus().equals(RechargeStatus.HANDING.getCode())){
			order.setRechargeStatus(ParkingRechargeOrderRechargeStatus.UNRECHARGED.getCode());
		}
		if(order_1_0.getRechargeStatus().equals(RechargeStatus.UPDATING.getCode())){
			order.setRechargeStatus(ParkingRechargeOrderRechargeStatus.UNRECHARGED.getCode());
		}
		if(order_1_0.getRechargeStatus().equals(RechargeStatus.SUCCESS.getCode())){
			order.setRechargeStatus(ParkingRechargeOrderRechargeStatus.RECHARGED.getCode());
		}
		order.setPaidTime(payTimeStamp);
		order.setPaidType(cmd.getVendorType());
		order.setRechargeTime(payTimeStamp);
		//order.setPaidTime(cmd.getPayTime());
		parkingProvider.updateParkingRechargeOrder(order);
		return rechargeResponse;
		
	}

	private Long strToLong(String str) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		Long ts = null;
		try {
			ts = sdf.parse(str).getTime();
		} catch (ParseException e) {
			LOGGER.error("validityPeriod data format is not yyyymmdd.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"validityPeriod data format is not yyyymmdd.");
		}

		return ts;
	}

	private BosigaoCardInfo getCardInfo(String plateNumber){

		URL wsdlURL = Service1.WSDL_LOCATION;
		Service1 ss = new Service1(wsdlURL, Service1.SERVICE);
		Service1Soap port = ss.getService1Soap12();

		String json = port.getCardInfo("", plateNumber, "2", "sign");

		if(LOGGER.isDebugEnabled())
			LOGGER.debug("Parking bosigao json={}", json);

		ResultHolder resultHolder = JSONObject.parseObject(json, ResultHolder.class);
		this.checkResultHolderIsNull(resultHolder, plateNumber);

		BosigaoCardInfo card = null;

		if(resultHolder.isSuccess()){
			String cardJson = JSONObject.parseObject(resultHolder.getData().toString()).get("card").toString();
			if(LOGGER.isDebugEnabled())
				LOGGER.debug("Parking bosigao cardJson={}", cardJson);

			card = JSONObject.parseObject(cardJson, BosigaoCardInfo.class);
		}
		return card;
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

		PlateInfo response = new PlateInfo();
		if(resultHolder.isSuccess()){
			Map<String,Object> data = (Map<String, Object>) resultHolder.getData();
			Map<String,Object> card = (Map<String, Object>) data.get("card");
			Boolean validStatus =  (Boolean) card.get("valid");
			this.checkValidStatusIsNull(validStatus,cmd.getPlateNumber());

			if(LOGGER.isDebugEnabled())
				LOGGER.error("validStatus="+validStatus);

			if(!validStatus){
				response.setIsValid("false");
				
				return response;
			}
			else if(validStatus){
				String ownerName = (String) card.get("userName");
				String plateNumber = (String) card.get("carNumber");
				
				String validEnd = (String) card.get("validEnd");
				String cardCode = (String) card.get("cardCode");
				String cardType = (String) card.get("cardDescript"); 
				Timestamp validityPeriod = strToTimestamp(validEnd);

				
				response.setOwnerName(ownerName);
				response.setPlateNumber(plateNumber);
				response.setValidityPeriod(validityPeriod);
				response.setCardType(cardType);
				response.setCardCode(cardCode);
				response.setIsValid("true");
				List<ParkCharge> parkCharge = parkProvider.listParkingChargeByEnterpriseCommunityId(cmd.getCommunityId(),cardType, null, null);
				response.setParkingCharge(parkCharge.stream().map(r->{
					ParkingChargeDTO dto = ConvertHelper.convert(r, ParkingChargeDTO.class);
					return dto;
				}).collect(Collectors.toList()));
				
				if(LOGGER.isDebugEnabled())
					LOGGER.error("successcommand="+response.toString());

				return response;
			}
		}
		
		return response;
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
		
		Timestamp begin = strToTimestamp(configurationProvider.getValue(ConfigConstants.PARKING_PREFERENTIAL_STARTTIME, "000000"));
		Long billId = Long.valueOf(orderNo);
		RechargeInfo rechargeInfo = onlinePayProvider.findRechargeInfoByOrderId(billId);
		if(rechargeInfo == null) {
			LOGGER.error("the bill id is not in list.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the bill id is not in list.");
		}
		if(rechargeInfo.getRechargeStatus() != RechargeStatus.SUCCESS.getCode()) {
			return -1;
		}
		Timestamp payTime = rechargeInfo.getRechargeTime();
		Timestamp end = strToTimestamp(configurationProvider.getValue(ConfigConstants.PARKING_PREFERENTIAL_ENDTIME, "000000"));
		
		if(payTime.after(end)) {
			return 0;
		}
		
		int count = parkProvider.getPaymentRanking(payTime, begin);
		
		return count;
	}

	@Override
	public String rechargeTop(PaymentRankingCommand cmd) {
		
		if(cmd.getPayStatus().toLowerCase().equals("fail")) {
			return "payFailed";
		}
		if(cmd.getPayStatus().toLowerCase().equals("success")) {
			
			int ranking = getPaymentRanking(cmd.getOrderNo());
			
			int range = Integer.valueOf(configurationProvider.getValue(ConfigConstants.PARKING_PREFERENTIAL_RANGE, "0"));
			
			if(ranking == 0)
				return "the activity was finished";
			
			if(ranking == -1)
				return "recharge unfinished";
			
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

	@Override
	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
		ListCardTypeResponse response = new ListCardTypeResponse();
//		response.setCardTypes(new ArrayList<String>());
		URL wsdlURL = Service1.WSDL_LOCATION;
 
		Service1 ss = new Service1(wsdlURL, SERVICE_NAME);
        Service1Soap port = ss.getService1Soap12();
        LOGGER.info("verifyRechargedPlate");
        String json = port.getAllCardDescript();
        
        GetAllCardDescriptDTO cardDescriptDTO = GsonUtil.fromJson(json, GetAllCardDescriptDTO.class);
        
        
        if(LOGGER.isDebugEnabled())
			LOGGER.error("cardDescriptDTO="+cardDescriptDTO.isSuccess());

		if(cardDescriptDTO.isSuccess()){
			response.setCardTypes(cardDescriptDTO.getCardDescript());
		}
		return response;
	}
	
	@Override
	public PreferentialRulesDTO qryPreferentialRuleByCommunityId(QryPreferentialRuleByCommunityIdCommand cmd){
		if(org.springframework.util.StringUtils.isEmpty(cmd.getOwnerType())){
			cmd.setOwnerType(EntityType.COMMUNITY.getCode());
		}
		
		int namespaceId = UserContext.getCurrentNamespaceId(null);
		
		//兼容
		if(null == cmd.getOwnerId()){
			List<Community> communities = communityProvider.listCommunitiesByNamespaceId(namespaceId);
			if(null != communities && 0 != communities.size()){
				cmd.setOwnerId(communities.get(0).getId());
			}
		}
		
		PreferentialRule preferentialRule = parkProvider.findPreferentialRuleByCommunityId(cmd.getOwnerType(), cmd.getOwnerId());
		PreferentialRulesDTO dto = ConvertHelper.convert(preferentialRule, PreferentialRulesDTO.class);
		if(null != preferentialRule){
			dto.setStartTime(null == preferentialRule.getStartTime() ? new Date().getTime() : preferentialRule.getStartTime().getTime());
			dto.setEndTime(null == preferentialRule.getEndTime() ? new Date().getTime() : preferentialRule.getEndTime().getTime());
		}
		return dto;
	}
	
	@Override
	public void setPreferentialRule(SetPreferentialRuleCommand cmd){
		if(org.springframework.util.StringUtils.isEmpty(cmd.getOwnerType())){
			cmd.setOwnerType(EntityType.COMMUNITY.getCode());
		}
		
		int namespaceId = UserContext.getCurrentNamespaceId(null);
		
		//兼容
		if(null == cmd.getOwnerId()){
			List<Community> communities = communityProvider.listCommunitiesByNamespaceId(namespaceId);
			if(null != communities && 0 != communities.size()){
				cmd.setOwnerId(communities.get(0).getId());
			}
		}
		
		PreferentialRule preferentialRule = parkProvider.findPreferentialRuleByCommunityId(cmd.getOwnerType(), cmd.getOwnerId());
		
		if(null != preferentialRule){
			preferentialRule.setStartTime(new Timestamp(cmd.getStartTime()));
			preferentialRule.setEndTime(new Timestamp(cmd.getEndTime()));
			preferentialRule.setBeforeNember(cmd.getBeforeNember());
			parkProvider.updatePreferentialRuleById(preferentialRule);
		}
		
	}
	

}
