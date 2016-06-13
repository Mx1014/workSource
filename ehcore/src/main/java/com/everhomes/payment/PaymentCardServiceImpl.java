package com.everhomes.payment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.order.OrderUtil;
import com.everhomes.payment.util.VerifyCodeEntity;
import com.everhomes.rest.order.CommonOrderCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.payment.ApplyCardCommand;
import com.everhomes.rest.payment.CardInfoDTO;
import com.everhomes.rest.payment.CardIssuerDTO;
import com.everhomes.rest.payment.CardOrderStatus;
import com.everhomes.rest.payment.CardRechargeOrderDTO;
import com.everhomes.rest.payment.CardRechargeStatus;
import com.everhomes.rest.payment.CardTransactionDTO;
import com.everhomes.rest.payment.CardTransactionOfMonth;
import com.everhomes.rest.payment.GetCardPaidQrCodeCommand;
import com.everhomes.rest.payment.GetCardPaidQrCodeDTO;
import com.everhomes.rest.payment.GetCardPaidResultCommand;
import com.everhomes.rest.payment.GetCardPaidResultDTO;
import com.everhomes.rest.payment.GetCardUserStatisticsCommand;
import com.everhomes.rest.payment.GetCardUserStatisticsDTO;
import com.everhomes.rest.payment.ListCardInfoCommand;
import com.everhomes.rest.payment.ListCardIssuerCommand;
import com.everhomes.rest.payment.ListCardTransactionsCommand;
import com.everhomes.rest.payment.ListCardTransactionsResponse;
import com.everhomes.rest.payment.RechargeCardCommand;
import com.everhomes.rest.payment.ResetCardPasswordCommand;
import com.everhomes.rest.payment.SearchCardRechargeOrderCommand;
import com.everhomes.rest.payment.SearchCardRechargeOrderResponse;
import com.everhomes.rest.payment.SearchCardTransactionsCommand;
import com.everhomes.rest.payment.SearchCardTransactionsResponse;
import com.everhomes.rest.payment.SearchCardUsersCommand;
import com.everhomes.rest.payment.SearchCardUsersResponse;
import com.everhomes.rest.payment.SendCardVerifyCodeCommand;
import com.everhomes.rest.payment.SendCardVerifyCodeDTO;
import com.everhomes.rest.payment.SetCardPasswordCommand;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.EncryptionUtils;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RandomGenerator;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;

@Component
public class PaymentCardServiceImpl implements PaymentCardService{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentCardServiceImpl.class);
    private static ConcurrentHashMap<String,VerifyCodeEntity> threadMap = new ConcurrentHashMap();  
    @Autowired
    private PaymentCardProvider paymentCardProvider;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
	private OrderUtil commonOrderUtil;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Autowired
	private SmsProvider smsProvider;
    
    @Override
    public List<CardInfoDTO> listCardInfo(ListCardInfoCommand cmd){
    	User user = UserContext.current().getUser();
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());

		List<CardInfoDTO> result = new ArrayList<CardInfoDTO>();
		List<PaymentCard> cardList = paymentCardProvider.listPaymentCard(cmd.getOwnerId(),cmd.getOwnerType(),user.getId());
		for(PaymentCard card:cardList){
			PaymentCardVendorHandler handler = getPaymentCardVendorHandler(card.getVendorName());
			CardInfoDTO dto = handler.getCardInfoByVendor(card);
			if(null != dto)
				result.add(dto);
		}
    	
    	return result;
    }
    @Override
    public List<CardIssuerDTO> listCardIssuer(ListCardIssuerCommand cmd){
    	List<CardIssuerDTO> result = new ArrayList<>();
    	List<PaymentCardIssuer> list = paymentCardProvider.listPaymentCardIssuer(cmd.getOwnerId(),cmd.getOwnerType());
    	result = list.stream().map(r -> ConvertHelper.convert(r, CardIssuerDTO.class)).collect(Collectors.toList());
    	return result;
    }
    @Override
    public CardInfoDTO applyCard(ApplyCardCommand cmd){
    	PaymentCardIssuer cardIssuer = paymentCardProvider.findPaymentCardIssuerById(cmd.getIssuerId());
    	PaymentCardVendorHandler handler = getPaymentCardVendorHandler(cardIssuer.getVendorName());
		CardInfoDTO dto = handler.applyCard(cmd,cardIssuer);
    	return dto;
    }
    @Override
    public CommonOrderDTO rechargeCard(RechargeCardCommand cmd){
    	
    	PaymentCard paymentCard = checkPaymentCard(cmd.getCardId());
    	if(paymentCard == null){
    		LOGGER.error("card id can not be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"card id can not be null.");
    	}
    	User user = UserContext.current().getUser();
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());

    	PaymentCardRechargeOrder paymentCardRechargeOrder = new PaymentCardRechargeOrder();
    	paymentCardRechargeOrder.setOwnerType(cmd.getOwnerType());
    	paymentCardRechargeOrder.setOwnerId(cmd.getOwnerId());
    	paymentCardRechargeOrder.setOrderNo(createOrderNo(System.currentTimeMillis()));
    	paymentCardRechargeOrder.setUserId(user.getId());
    	paymentCardRechargeOrder.setUserName(user.getNickName());
    	paymentCardRechargeOrder.setMobile(userIdentifier.getIdentifierToken());
    	paymentCardRechargeOrder.setCardNo(paymentCard.getCardNo());
    	paymentCardRechargeOrder.setCardId(paymentCard.getId());
    	paymentCardRechargeOrder.setAmount(cmd.getAmount());
    	paymentCardRechargeOrder.setPayerUid(user.getId());
    	paymentCardRechargeOrder.setPayerName(user.getNickName());
    	paymentCardRechargeOrder.setPayStatus(CardOrderStatus.UNPAID.getCode());
    	paymentCardRechargeOrder.setRechargeStatus(CardRechargeStatus.UNRECHARGED.getCode());
    	paymentCardRechargeOrder.setCreatorUid(user.getId());
    	paymentCardRechargeOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
    	paymentCardProvider.createPaymentCardRechargeOrder(paymentCardRechargeOrder);
    	
    	//调用统一处理订单接口，返回统一订单格式
    	CommonOrderCommand orderCmd = new CommonOrderCommand();
    	orderCmd.setBody(paymentCardRechargeOrder.getMobile());
    	orderCmd.setOrderNo(paymentCardRechargeOrder.getId().toString());
    	orderCmd.setOrderType(OrderType.OrderTypeEnum.PARKING.getPycode());
    	orderCmd.setSubject("一卡通充值订单简要描述");
    	orderCmd.setTotalFee(paymentCardRechargeOrder.getAmount());
    	CommonOrderDTO dto = null;
    	try {
    		dto = commonOrderUtil.convertToCommonOrderTemplate(orderCmd);
    	} catch (Exception e) {
    		LOGGER.error("convertToCommonOrder is fail. {}",e);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"convertToCommonOrder is fail.");
    	}
    	return dto;
    }
    
    @Override
    public GetCardPaidQrCodeDTO getCardPaidQrCode(GetCardPaidQrCodeCommand cmd){
    	GetCardPaidQrCodeDTO dto = new GetCardPaidQrCodeDTO();
    	
    	return dto;
    }
    @Override
    public GetCardPaidResultDTO getCardPaidResult(GetCardPaidResultCommand cmd){
    	GetCardPaidResultDTO dto = new GetCardPaidResultDTO();
    	
    	return dto;
    }
    
    @Override
    public SendCardVerifyCodeDTO sendCardVerifyCode(SendCardVerifyCodeCommand cmd){
    	SendCardVerifyCodeDTO dto = new SendCardVerifyCodeDTO();
    	String verifyCode = RandomGenerator.getRandomDigitalString(6);
    	User user = UserContext.current().getUser();
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());

		sendVerificationCodeSms(userIdentifier.getNamespaceId(), userIdentifier.getIdentifierToken(),verifyCode);
		dto.setVerifyCode(verifyCode);
		VerifyCodeEntity verifyCodeEntity = new VerifyCodeEntity();
		verifyCodeEntity.setCreateTime(System.currentTimeMillis());
		verifyCodeEntity.setVerifyCode(verifyCode);
		threadMap.put(userIdentifier.getIdentifierToken(),verifyCodeEntity);
    	return dto;
    }
    private void sendVerificationCodeSms(Integer namespaceId, String phoneNumber, String verificationCode){
	    List<Tuple<String, Object>> variables = smsProvider.toTupleList(SmsTemplateCode.KEY_VCODE, verificationCode);
	    String templateScope = SmsTemplateCode.SCOPE;
	    int templateId = SmsTemplateCode.VERIFICATION_CODE;
	    String templateLocale = UserContext.current().getUser().getLocale();
	    smsProvider.sendSms(namespaceId, phoneNumber, templateScope, templateId, templateLocale, variables);
	}
    @Override
    public void setCardPassword(SetCardPasswordCommand cmd){
    	PaymentCard paymentCard = checkPaymentCard(cmd.getCardId());
    	if(paymentCard == null){
    		LOGGER.error("card id can not be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"card id can not be null.");
    	}
    	if(!paymentCard.getPassword().equals(EncryptionUtils.hashPassword(cmd.getOldPassword()))){
    		LOGGER.error("the old password is not correctly.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the old password is not correctly.");
    	}
    		
    	PaymentCardVendorHandler handler = getPaymentCardVendorHandler(paymentCard.getVendorName());
		handler.setCardPassword(cmd,paymentCard);
    }
    
    @Override
    public void resetCardPassword(ResetCardPasswordCommand cmd){
    	if(StringUtils.isBlank(cmd.getVerifyCode())){
    		LOGGER.error("verifyCode cannot be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"verifyCode cannot be null.");
    	}
    	VerifyCodeEntity entity = threadMap.get(cmd.getMobile());
    	if(!entity.getVerifyCode().equals(cmd.getVerifyCode())){
    		LOGGER.error("verifyCode is not correctly.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"verifyCode is not correctly.");
    	}
    	if(entity.isExpired()){
    		LOGGER.error("verifyCode is expired.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"verifyCode is nexpired.");
    	}
    	PaymentCard paymentCard = checkPaymentCard(cmd.getCardId());
    	if(paymentCard == null){
    		LOGGER.error("card id cannot be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"card id cannot be null.");
    	}
    		
    	PaymentCardVendorHandler handler = getPaymentCardVendorHandler(paymentCard.getVendorName());
		handler.resetCardPassword(cmd,paymentCard);
    }
    
    @Override
    public ListCardTransactionsResponse listCardTransactions(ListCardTransactionsCommand cmd){
    	User user = UserContext.current().getUser();
    	ListCardTransactionsResponse response = new ListCardTransactionsResponse();
    	List<PaymentCard> cardList = paymentCardProvider.listPaymentCard(cmd.getOwnerId(),cmd.getOwnerType(),user.getId());
		for(PaymentCard card:cardList){
			PaymentCardVendorHandler handler = getPaymentCardVendorHandler(card.getVendorName());
			List<CardTransactionOfMonth> list = handler.listCardTransactions(cmd,card);
//			if(null != dto)
//				result.add(dto);
		}
		return response;
    }
    
    @Override
    public SearchCardUsersResponse searchCardUsers(SearchCardUsersCommand cmd){
    	
    	return null;
    }
    @Override
    public GetCardUserStatisticsDTO getCardUserStatistics(GetCardUserStatisticsCommand cmd){
    	
    	return null;
    }
    @Override
    public SearchCardRechargeOrderResponse searchCardRechargeOrder(SearchCardRechargeOrderCommand cmd){
    	SearchCardRechargeOrderResponse response = new SearchCardRechargeOrderResponse();
		Timestamp startDate = null;
		Timestamp endDate = null;
		if(cmd.getStartDate() != null)
			startDate = new Timestamp(cmd.getStartDate());
		if(cmd.getEndDate() != null)
			new Timestamp(cmd.getEndDate());
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		//User user = UserContext.current().getUser();
		List<PaymentCardRechargeOrder> list = paymentCardProvider.searchCardRechargeOrder(cmd.getOwnerType(),
				cmd.getOwnerId(), startDate,endDate,cmd.getRechargeType(), cmd.getRechargeStatus(), cmd.getKeyword(),
				cmd.getPageAnchor(), pageSize);
    	if(list.size() > 0){
    		response.setRequests(list.stream().map(r -> ConvertHelper.convert(r, CardRechargeOrderDTO.class))
    				.collect(Collectors.toList()));
    		if(pageSize != null && list.size() != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getRechargeTime().getTime());
        	}
    	}
    	
		return response;
    }
    @Override
    public SearchCardTransactionsResponse searchCardTransactions(SearchCardTransactionsCommand cmd){
    	SearchCardTransactionsResponse response = new SearchCardTransactionsResponse();
		Timestamp startDate = null;
		Timestamp endDate = null;
		if(cmd.getStartDate() != null)
			startDate = new Timestamp(cmd.getStartDate());
		if(cmd.getEndDate() != null)
			new Timestamp(cmd.getEndDate());
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		//User user = UserContext.current().getUser();
		List<PaymentCardTransaction> list = paymentCardProvider.searchCardTransactions(cmd.getOwnerType(),
				cmd.getOwnerId(), startDate,endDate,cmd.getConsumeType(), cmd.getStatus(), cmd.getKeyword(),
				cmd.getPageAnchor(), pageSize);
    	if(list.size() > 0){
    		response.setRequests(list.stream().map(r -> ConvertHelper.convert(r, CardTransactionDTO.class))
    				.collect(Collectors.toList()));
    		if(pageSize != null && list.size() != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getTransactionTime().getTime());
        	}
    	}
    	
		return response;
    }
    
    private PaymentCardVendorHandler getPaymentCardVendorHandler(String vendorName) {
    	PaymentCardVendorHandler handler = null;
        
        if(vendorName != null && vendorName.length() > 0) {
            String handlerPrefix = PaymentCardVendorHandler.PAYMENTCARD_VENDOR_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + vendorName);
        }
        
        return handler;
    }
    private Long createOrderNo(Long time) {
		String bill = String.valueOf(time) + (int) (Math.random()*1000);
		return Long.valueOf(bill);
	}
    
    private PaymentCard checkPaymentCard(Long cardId){
    	if(cardId == null){
    		LOGGER.error("card id can not be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"card id can not be null.");
    	}
    	PaymentCard paymentCard = paymentCardProvider.findPaymentCardById(cardId);
    	return paymentCard;
    }
}
