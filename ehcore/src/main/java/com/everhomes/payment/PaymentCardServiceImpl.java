package com.everhomes.payment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.pay.order.SourceType;
import com.everhomes.paySDK.PayUtil;
import com.everhomes.rest.activity.ActivityLocalStringCode;
import com.everhomes.rest.gorder.order.BusinessPayerType;
import com.everhomes.rest.gorder.order.CreateRefundOrderCommand;
import com.everhomes.rest.order.*;
import com.everhomes.rest.payment.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.order.OrderUtil;
import com.everhomes.payment.taotaogu.AESCoder;
import com.everhomes.payment.taotaogu.NotifyEntity;
import com.everhomes.payment.taotaogu.TaotaoguTokenCacheItem;
import com.everhomes.payment.taotaogu.TaotaoguVendorConstant;
import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.EncryptionUtils;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RandomGenerator;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import com.google.gson.Gson;

@Component
public class PaymentCardServiceImpl implements PaymentCardService{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentCardServiceImpl.class);
    private SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final String BIZ_ACCOUNT_PRE = "NS";//账号前缀
    @Autowired
    private PaymentCardProvider paymentCardProvider;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
	private OrderUtil commonOrderUtil;
    
    @Autowired
    private ConfigurationProvider configProvider;

	@Autowired
	private UserPrivilegeMgr userPrivilegeMgr;
    @Autowired
	private SmsProvider smsProvider;
    @Autowired
    BigCollectionProvider bigCollectionProvider;
    @Autowired
	private LocaleStringService localeStringService;
    @Autowired
    private CoordinationProvider coordinationProvider;
    @Autowired
	private PaymentCardPayService payService;
	@Autowired
	private PaymentCardOrderEmbeddedV2Handler paymentCardOrderEmbeddedV2Handler;

    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    @Override
    public List<CardInfoDTO> listCardInfo(ListCardInfoCommand cmd){
    	checkParam(cmd.getOwnerType(), cmd.getOwnerId());

    	List<PaymentCardIssuer> cardIssuerList = paymentCardProvider.listPaymentCardIssuer(cmd.getOwnerId(),cmd.getOwnerType());

		List<CardInfoDTO> result = new ArrayList<CardInfoDTO>();
		for(PaymentCardIssuer cardIssuer:cardIssuerList){
			PaymentCardVendorHandler handler = getPaymentCardVendorHandler(cardIssuer.getVendorName());
			List<CardInfoDTO> cardList = handler.getCardInfoByVendor(cmd);
			result.addAll(cardList);
		}
    	return result;
    }
    @Override
    public List<CardIssuerDTO> listCardIssuer(ListCardIssuerCommand cmd){
    	checkParam(cmd.getOwnerType(), cmd.getOwnerId());
    	List<CardIssuerDTO> result = new ArrayList<>();
    	List<PaymentCardIssuer> list = paymentCardProvider.listPaymentCardIssuer(cmd.getOwnerId(),cmd.getOwnerType());
    	result = list.stream().map(r -> ConvertHelper.convert(r, CardIssuerDTO.class)).collect(Collectors.toList());
    	return result;
    }
    @Override
    public CardInfoDTO applyCard(ApplyCardCommand cmd){
    	checkParam(cmd.getOwnerType(), cmd.getOwnerId());
    	User user = UserContext.current().getUser();
    	Integer count = paymentCardProvider.countPaymentCard(cmd.getOwnerId(), cmd.getOwnerType(), user.getId());
    	if(count > 0){
//    		LOGGER.error("plateNumber cannot be null.");
//        	throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PLATE_NULL,
//					localeStringService.getLocalizedString(String.valueOf(ParkingErrorCode.SCOPE), 
//							String.valueOf(ParkingErrorCode.ERROR_PLATE_NULL),
//							UserContext.current().getUser().getLocale(),"plateNumber cannot be null."));
    		LOGGER.error("Already open card.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 1001,
					"不能重复绑定");
    	}
    	PaymentCardIssuer cardIssuer = paymentCardProvider.findPaymentCardIssuerById(cmd.getIssuerId());
    	PaymentCardVendorHandler handler = getPaymentCardVendorHandler(cardIssuer.getVendorName());
		CardInfoDTO dto = handler.applyCard(cmd,cardIssuer);
    	return dto;
    }
    @Override
    public CommonOrderDTO rechargeCard(RechargeCardCommand cmd){
    	checkParam(cmd.getOwnerType(), cmd.getOwnerId());
    	PaymentCard paymentCard = checkPaymentCard(cmd.getCardId());
    	checkPaymentCardIsNull(paymentCard,cmd.getCardId());
    	User user = UserContext.current().getUser();
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());

    	PaymentCardRechargeOrder paymentCardRechargeOrder = new PaymentCardRechargeOrder();
    	paymentCardRechargeOrder.setOwnerType(cmd.getOwnerType());
    	paymentCardRechargeOrder.setOwnerId(cmd.getOwnerId());
    	paymentCardRechargeOrder.setNamespaceId(user.getNamespaceId());
    	paymentCardRechargeOrder.setOrderNo(createOrderNo());
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
    	orderCmd.setOrderType(OrderType.OrderTypeEnum.PAYMENTCARD.getPycode());
    	orderCmd.setSubject("一卡通充值订单");
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
	public PreOrderDTO rechargeCardV2(RechargeCardCommand cmd) {
		checkParam(cmd.getOwnerType(), cmd.getOwnerId());
		PaymentCard paymentCard = checkPaymentCard(cmd.getCardId());
		checkPaymentCardIsNull(paymentCard,cmd.getCardId());
		User user = UserContext.current().getUser();
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());

		PaymentCardRechargeOrder paymentCardRechargeOrder = new PaymentCardRechargeOrder();
		paymentCardRechargeOrder.setOwnerType(cmd.getOwnerType());
		paymentCardRechargeOrder.setOwnerId(cmd.getOwnerId());
		paymentCardRechargeOrder.setNamespaceId(user.getNamespaceId());
		paymentCardRechargeOrder.setOrderNo(createOrderNo());
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

		PreOrderCommand preOrderCommand = new PreOrderCommand();

		preOrderCommand.setClientAppName(cmd.getClientAppName());
		preOrderCommand.setOrderType(OrderType.OrderTypeEnum.PAYMENTCARD.getPycode());
		preOrderCommand.setOrderId(paymentCardRechargeOrder.getId());
		preOrderCommand.setAmount(cmd.getAmount().multiply(new BigDecimal(100)).longValue());
		preOrderCommand.setCommunityId(cmd.getOwnerId());
		preOrderCommand.setPayerId(user.getId());
		preOrderCommand.setNamespaceId(user.getNamespaceId());


		PreOrderDTO callBack = payService.createPreOrder(preOrderCommand,paymentCardRechargeOrder);
		return callBack;
	}

	@Override
	public void refundOrderV2(Long orderId) {
		PaymentCardRechargeOrder order = paymentCardProvider.findPaymentCardRechargeOrderById(orderId);
		payService.refundOrder(order);
	}

	@Override
    public GetCardPaidQrCodeDTO getCardPaidQrCode(GetCardPaidQrCodeCommand cmd){
    	checkParam(cmd.getOwnerType(), cmd.getOwnerId());
    	GetCardPaidQrCodeDTO dto = new GetCardPaidQrCodeDTO();
    	PaymentCard paymentCard = checkPaymentCard(cmd.getCardId());
    	checkPaymentCardIsNull(paymentCard,cmd.getCardId());
    	
//    	CachePool cachePool = CachePool.getInstance();
//    	CacheItem entity = cachePool.getCacheItem(CacheConstant.GET_VERIFY_CODE_TIME+cmd.getCardId());
//    	//防止请求过于频繁
//    	if(entity == null){
//    		//缓存保持24小时
//    		long now = System.currentTimeMillis();
//    		cachePool.putCacheItem(CacheConstant.GET_VERIFY_CODE_TIME+cmd.getCardId(), now, 24 * 60 *60 * 1000);
//    	}else{
//    		long time = (long) entity.getEntity();
//    		long now = System.currentTimeMillis();
//    		if(now < (30 * 1000 + time) ){
//    			LOGGER.error("the get Code request is frequently,the time is less than 50s.");
//    			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//    					"the get Code request is frequently,the time is less than 50s.");
//    		}
//    		entity.setEntity(now);
//    	}
    	PaymentCardVendorHandler handler = getPaymentCardVendorHandler(paymentCard.getVendorName());
		String code = handler.getCardPaidQrCodeByVendor(paymentCard);
		dto.setCode(code);
    	return dto;
    }
    @Override
    public GetCardPaidResultDTO getCardPaidResult(GetCardPaidResultCommand cmd){
    	checkParam(cmd.getOwnerType(), cmd.getOwnerId());
    	GetCardPaidResultDTO dto = null;
    	PaymentCard paymentCard = checkPaymentCard(cmd.getCardId());
    	checkPaymentCardIsNull(paymentCard,cmd.getCardId());
    	boolean flag = true;
    	int i = 1;
    	while(flag&&i<=10){
    		i++;
    		PaymentCardTransaction paymentCardTransaction = paymentCardProvider.findPaymentCardTransactionByCondition(cmd.getCode(),paymentCard.getCardNo());
        	if(paymentCardTransaction != null){
        		dto = new GetCardPaidResultDTO();
        		dto.setAmount(paymentCardTransaction.getAmount());
        		dto.setDisAmount(paymentCardTransaction.getAmount());
        		dto.setMerchantName(paymentCardTransaction.getMerchantName());
        		dto.setMerchantNo(paymentCardTransaction.getMerchantNo());
        		dto.setTransactionTime(paymentCardTransaction.getTransactionTime());
        		dto.setStatus(paymentCardTransaction.getStatus().equals(CardTransactionStatus.PAIDED.getCode())
        				?PaidResultStatus.SUCCESS.getCode():PaidResultStatus.FAIL.getCode());
        		flag = false;
        		break;
        	}
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				continue;
			}
    	}
    	return dto;
    }
    
    @Override
    public SendCardVerifyCodeDTO sendCardVerifyCode(SendCardVerifyCodeCommand cmd){
    	checkParam(cmd.getOwnerType(), cmd.getOwnerId());
    	SendCardVerifyCodeDTO dto = new SendCardVerifyCodeDTO();
    	String verifyCode = RandomGenerator.getRandomDigitalString(6);
    	User user = UserContext.current().getUser();
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());

		sendVerificationCodeSms(userIdentifier.getNamespaceId(), userIdentifier.getIdentifierToken(),verifyCode);
		dto.setVerifyCode(verifyCode);
		//丢到缓存中
		this.coordinationProvider.getNamedLock(CoordinationLocks.PAYMENT_CARD.getCode()).enter(()-> {
			String key = userIdentifier.getIdentifierToken();
	        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
	        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
	      
	        TaotaoguTokenCacheItem cacheItem = new TaotaoguTokenCacheItem();
	        cacheItem.setCreateTime(new Date());
	        cacheItem.setExpireTime(10 * 60 * 1000);
	        cacheItem.setVerifyCode(verifyCode);
	        redisTemplate.opsForValue().set(key,StringHelper.toJsonString(cacheItem));
	        redisTemplate.expire(key, 10, TimeUnit.MINUTES);
            return null;
        });
		
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
    	checkParam(cmd.getOwnerType(), cmd.getOwnerId());
    	PaymentCard paymentCard = checkPaymentCard(cmd.getCardId());
    	checkPaymentCardIsNull(paymentCard,cmd.getCardId());
    	if(!paymentCard.getPassword().equals(EncryptionUtils.hashPassword(cmd.getOldPassword()))){
    		LOGGER.error("the old password is not correctly.");
			throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_OLD_PASSWORD,
					"the old password is not correctly.");
    	}
    		
    	PaymentCardVendorHandler handler = getPaymentCardVendorHandler(paymentCard.getVendorName());
		handler.setCardPassword(cmd,paymentCard);
    }
    
    @Override
    public void resetCardPassword(ResetCardPasswordCommand cmd){
    	checkParam(cmd.getOwnerType(), cmd.getOwnerId());
    	if(StringUtils.isBlank(cmd.getVerifyCode())){
    		LOGGER.error("verifyCode cannot be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"verifyCode cannot be null.");
    	}
    	Object result = this.coordinationProvider.getNamedLock(CoordinationLocks.PAYMENT_CARD.getCode()).enter(()-> {
			String key = cmd.getMobile();
	        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
	        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
	      
	        Object obj = redisTemplate.opsForValue().get(key);
            return obj;
        }).first();

    	
    	if(result == null){
    		LOGGER.error("verifyCode is not exists.");
			throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_VERIFY_CODE,
					"verifyCode is not exists.");
    	}
    	Object o =  StringHelper.fromJsonString(result.toString(), Object.class);
    	TaotaoguTokenCacheItem cacheItem = (TaotaoguTokenCacheItem) StringHelper.fromJsonString(o.toString(),TaotaoguTokenCacheItem.class);
    	if(!(cacheItem.getVerifyCode()).equals(cmd.getVerifyCode())){
    		LOGGER.error("verifyCode is not correctly.");
    		throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_VERIFY_CODE,
					"verifyCode is not correctly.");
    	}
    	if(cacheItem.isExpired()){
    		LOGGER.error("verifyCode is expired.");
    		throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_VERIFY_CODE,
					"verifyCode is expired.");
    	}
    	PaymentCard paymentCard = checkPaymentCard(cmd.getCardId());
    	checkPaymentCardIsNull(paymentCard,cmd.getCardId());
    		
    	PaymentCardVendorHandler handler = getPaymentCardVendorHandler(paymentCard.getVendorName());
		handler.resetCardPassword(cmd,paymentCard);
		this.coordinationProvider.getNamedLock(CoordinationLocks.PAYMENT_CARD.getCode()).enter(()-> {
			String key = cmd.getMobile();
	        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
	        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
	      
	        redisTemplate.delete(key);
            return null;
        });
    }
    @Override
    public ListCardTransactionsResponse listCardTransactions(ListCardTransactionsCommand cmd){
    	checkParam(cmd.getOwnerType(), cmd.getOwnerId());
    	if(cmd.getPageAnchor() == null)
    		cmd.setPageAnchor(1L);
    	User user = UserContext.current().getUser();
    	ListCardTransactionsResponse response = new ListCardTransactionsResponse();
    	List<PaymentCard> cardList = paymentCardProvider.listPaymentCard(cmd.getOwnerId(),cmd.getOwnerType(),user.getId());
    	if(CollectionUtils.isEmpty(cardList)) {
    		
    	}
		//for(PaymentCard card:cardList){
    	PaymentCard card = cardList.get(0);
			PaymentCardVendorHandler handler = getPaymentCardVendorHandler(card.getVendorName());
			List<CardTransactionOfMonth> list = handler.listCardTransactions(cmd,card);
		//}
			
			response.setRequests(list);
			if(list.size()==0)
				response.setNextPageAnchor(null);
			else
				response.setNextPageAnchor(cmd.getPageAnchor()+1);
		return response;
    }
    
    @Override
    public SearchCardUsersResponse searchCardUsers(SearchCardUsersCommand cmd){
    	if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4120041210L, cmd.getAppId(), null,cmd.getCurrentProjectId());//开卡用户权限
		}

    	SearchCardUsersResponse response = new SearchCardUsersResponse();
		
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		//User user = UserContext.current().getUser();
		List<PaymentCard> list = paymentCardProvider.searchCardUsers(cmd.getOwnerId(),cmd.getOwnerType(),
				cmd.getKeyword(),cmd.getStatus(),cmd.getPageAnchor(), pageSize);
		response.setRequests(list.stream().map(r -> ConvertHelper.convert(r, CardUserDTO.class))
				.collect(Collectors.toList()));
    	if(list.size() > 0){
    		
    		if(pageSize != null && list.size() != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getCreateTime().getTime());
        	}
    	}
    	
		return response;
    }
    @Override
    public GetCardUserStatisticsDTO getCardUserStatistics(GetCardUserStatisticsCommand cmd){
    	if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4120041210L, cmd.getAppId(), null,cmd.getCurrentProjectId());//开卡用户权限
		}
    	checkParam(cmd.getOwnerType(), cmd.getOwnerId());
    	GetCardUserStatisticsDTO dto = new GetCardUserStatisticsDTO();
    	User user = UserContext.current().getUser();
		int communityUserCount = userProvider.countUserByNamespaceId(user.getNamespaceId(), null);
		Integer cardUserCount = paymentCardProvider.countCardUsers(cmd.getOwnerId(), cmd.getOwnerType(), user.getNamespaceId());
		dto.setCardUserCount(cardUserCount);
		dto.setTotalCount(communityUserCount);
		dto.setNormalUserCount(communityUserCount - cardUserCount);
    	return dto;
    }
    @Override
    public SearchCardRechargeOrderResponse searchCardRechargeOrder(SearchCardRechargeOrderCommand cmd){
    	if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4120041230L, cmd.getAppId(), null,cmd.getCurrentProjectId());//充值记录权限
		}
    	SearchCardRechargeOrderResponse response = new SearchCardRechargeOrderResponse();
		Timestamp startDate = null;
		Timestamp endDate = null;
		if(cmd.getStartDate() != null)
			startDate = new Timestamp(cmd.getStartDate());
		if(cmd.getEndDate() != null)
			endDate = new Timestamp(cmd.getEndDate());
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		//User user = UserContext.current().getUser();
		List<PaymentCardRechargeOrder> list = paymentCardProvider.searchCardRechargeOrder(cmd.getOwnerType(),
				cmd.getOwnerId(), startDate,endDate,cmd.getRechargeType(), cmd.getRechargeStatus(), cmd.getKeyword(),
				cmd.getPageAnchor(), pageSize);
		response.setRequests(list.stream().map(r -> ConvertHelper.convert(r, CardRechargeOrderDTO.class))
				.collect(Collectors.toList()));
    	if(list.size() > 0){
    		
    		if(pageSize != null && list.size() != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getCreateTime().getTime());
        	}
    	}
    	
		return response;
    }
    @Override
    public SearchCardTransactionsResponse searchCardTransactions(SearchCardTransactionsCommand cmd){
    	if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4120041240L, cmd.getAppId(), null,cmd.getCurrentProjectId());//消费记录权限
		}
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
		response.setRequests(list.stream().map(r -> ConvertHelper.convert(r, CardTransactionDTO.class))
				.collect(Collectors.toList()));
    	if(list.size() > 0){
    		
    		if(pageSize != null && list.size() != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getTransactionTime().getTime());
        	}
    	}
    	
		return response;
    }
    
    @Override
    public void updateCardRechargeOrder(UpdateCardRechargeOrderCommand cmd){
    	if(cmd.getId() == null){
    		LOGGER.error("order id cannot be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"order id cannot be null.");
    	}
    	PaymentCardRechargeOrder order = paymentCardProvider.findPaymentCardRechargeOrderById(cmd.getId());
    	order.setRechargeStatus(cmd.getRechargeStatus());
    	paymentCardProvider.updatePaymentCardRechargeOrder(order);
    }
    
    @Override
    public void exportCardUsers(SearchCardUsersCommand cmd,HttpServletResponse response){
    	if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4120041220L, cmd.getAppId(), null,cmd.getCurrentProjectId());//开卡用户权限
		}
    	Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		//User user = UserContext.current().getUser();
		List<PaymentCard> list = paymentCardProvider.searchCardUsers(cmd.getOwnerId(),cmd.getOwnerType(),
				cmd.getKeyword(),cmd.getStatus(),cmd.getPageAnchor(), pageSize);
		Workbook wb = new XSSFWorkbook();
		
		Font font = wb.createFont();   
		font.setFontName("黑体");   
		font.setFontHeightInPoints((short) 16);
		CellStyle style = wb.createCellStyle();
		style.setFont(font);
		
		Sheet sheet = wb.createSheet("cardUsers");
		sheet.setDefaultColumnWidth(20);  
		sheet.setDefaultRowHeightInPoints(20); 
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("手机号");
		row.createCell(1).setCellValue("姓名");
		row.createCell(2).setCellValue("最后修改时间");
		row.createCell(3).setCellValue("卡号");
		row.createCell(4).setCellValue("状态");
		for(int i=0;i<list.size();i++){
			Row tempRow = sheet.createRow(i + 1);
			PaymentCard paymentCard = list.get(i);
			tempRow.createCell(0).setCellValue(paymentCard.getMobile());
			tempRow.createCell(1).setCellValue(paymentCard.getUserName());
			tempRow.createCell(2).setCellValue(datetimeSF.format(paymentCard.getUpdateTime()));
			tempRow.createCell(3).setCellValue(paymentCard.getCardNo());
			tempRow.createCell(4).setCellValue(paymentCard.getStatus() == PaymentCardStatus.ACTIVE.getCode() ? "已绑定" : "已解绑");
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			wb.write(out);
			DownloadUtil.download(out, response);
		} catch (IOException e) {
			LOGGER.error("exportCardUsers is fail. {}",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"exportCardUsers is fail.");
		}
		
	}
    
	@Override
	public void exportCardRechargeOrder(SearchCardRechargeOrderCommand cmd,
			HttpServletResponse response) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4120041230L, cmd.getAppId(), null,cmd.getCurrentProjectId());//充值记录权限
		}
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
		Workbook wb = new XSSFWorkbook();
		
		Font font = wb.createFont();   
		font.setFontName("黑体");   
		font.setFontHeightInPoints((short) 16);
		CellStyle style = wb.createCellStyle();
		style.setFont(font);
		
		Sheet sheet = wb.createSheet("cardUsers");
		sheet.setDefaultColumnWidth(20);  
		sheet.setDefaultRowHeightInPoints(20); 
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("手机号");
		row.createCell(1).setCellValue("姓名");
		row.createCell(2).setCellValue("卡号");
		row.createCell(3).setCellValue("充值金额");
		row.createCell(4).setCellValue("充值时间");
		row.createCell(5).setCellValue("支付方式");
		row.createCell(6).setCellValue("处理状态");
		for(int i=0;i<list.size();i++){
			Row tempRow = sheet.createRow(i + 1);
			PaymentCardRechargeOrder order = list.get(i);
			tempRow.createCell(0).setCellValue(order.getMobile());
			tempRow.createCell(1).setCellValue(order.getUserName());
			tempRow.createCell(2).setCellValue(order.getCardNo());
			tempRow.createCell(3).setCellValue(order.getAmount().doubleValue());
			tempRow.createCell(4).setCellValue(order.getRechargeTime()!=null?datetimeSF.format(order.getRechargeTime()):"");
			tempRow.createCell(5).setCellValue("10001".equals(order.getPaidType())?"支付宝":"微信");
			tempRow.createCell(6).setCellValue(convertOrderStatus(CardRechargeStatus.fromCode(order.getRechargeStatus()).getCode()));
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			wb.write(out);
			DownloadUtil.download(out, response);
		} catch (IOException e) {
			LOGGER.error("exportCardUsers is fail. {}",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"exportCardUsers is fail.");
		}
		
	}
	private String convertOrderStatus(Byte b){
		switch (b) {
		case 0:
			return "充值失败";
		case 1:
			return "处理中";
		case 2:
			return "充值成功";
		case 3:
			return "处理完成";
		case 4:
			return "已退款";
		default:
			return "";
		}
	}
	@Override
	public void exportCardTransactions(SearchCardTransactionsCommand cmd,
			HttpServletResponse response) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4120041240L, cmd.getAppId(), null,cmd.getCurrentProjectId());//消费记录权限
		}
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
		Workbook wb = new XSSFWorkbook();
		
		Font font = wb.createFont();   
		font.setFontName("黑体");   
		font.setFontHeightInPoints((short) 16);
		CellStyle style = wb.createCellStyle();
		style.setFont(font);
		
		Sheet sheet = wb.createSheet("cardUsers");
		sheet.setDefaultColumnWidth(20);  
		sheet.setDefaultRowHeightInPoints(20); 
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("手机号");
		row.createCell(1).setCellValue("姓名");
		row.createCell(2).setCellValue("卡号");
		row.createCell(3).setCellValue("消费类型");
		row.createCell(4).setCellValue("商品");
		row.createCell(5).setCellValue("订单号");
		row.createCell(6).setCellValue("交易时间");
		row.createCell(7).setCellValue("交易金额");
		row.createCell(8).setCellValue("处理状态");
		for(int i=0;i<list.size();i++){
			Row tempRow = sheet.createRow(i + 1);
			PaymentCardTransaction transaction = list.get(i);
			tempRow.createCell(0).setCellValue(transaction.getMobile());
			tempRow.createCell(1).setCellValue(transaction.getUserName());
			tempRow.createCell(2).setCellValue(transaction.getCardNo());
			tempRow.createCell(3).setCellValue(transaction.getConsumeType());
			tempRow.createCell(4).setCellValue(transaction.getItemName());
			tempRow.createCell(5).setCellValue(transaction.getOrderNo());
			tempRow.createCell(6).setCellValue(datetimeSF.format(transaction.getTransactionTime()));
			tempRow.createCell(7).setCellValue(transaction.getAmount().doubleValue());
			tempRow.createCell(8).setCellValue(convertTransactionStatus(CardTransactionStatus.fromCode(transaction.getStatus()).getCode()));
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			wb.write(out);
			DownloadUtil.download(out, response);
		} catch (IOException e) {
			LOGGER.error("ExportCardTransactions failed",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"ExportCardTransactions failed");
		}
		
	}
    
	private String convertTransactionStatus(Byte b){
		switch (b) {
		case 0:
			return "失败";
		case 1:
			return "过期";
		case 2:
			return "已撤销";
		case 3:
			return "支付成功";
		case 4:
			return "未支付";
		default:
			return "";
		}
	}
	
    @Override
    public NotifyEntityDTO notifyPaidResult(NotifyEntityCommand cmd){
    	NotifyEntityDTO dto = new NotifyEntityDTO();
    	PaymentCardTransaction transaction = new PaymentCardTransaction();
    	if(StringUtils.isBlank(cmd.getToken())){
			dto.setMsg_sn("invalid parameter");
			dto.setReturn_code("01");
			return dto;
		}
    	PaymentCardIssuer issuer = paymentCardProvider.findPaymentCardIssuerByToken(cmd.getToken());
    	if(issuer == null){
    		LOGGER.error("Token not found, user may be not login, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Token not found, user may be not login");
    	}
    	
    	String key = "taotaogu-token-" + issuer.getId();
    	Object cache = this.coordinationProvider.getNamedLock(CoordinationLocks.PAYMENT_CARD.getCode()).enter(()-> {
			
	        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
	        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
	      
	        Object obj = redisTemplate.opsForValue().get(key);
            return obj;
        }).first();
    	
    	if(cache == null){
    		LOGGER.error("Token and aesKey is not found in cache, key={}, cmd={}", key, cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Token and aeskey is not found");
    	}
    	Object o =  StringHelper.fromJsonString(cache.toString(), Object.class);
    	TaotaoguTokenCacheItem cacheItem = (TaotaoguTokenCacheItem) StringHelper.fromJsonString(o.toString(),TaotaoguTokenCacheItem.class);
		String aesKey = cacheItem.getAesKey();
		String token = cacheItem.getToken();
    	Gson gson = new Gson();
		String msg = null;
		
		try {
			if(StringUtils.isBlank(cmd.getToken()) || StringUtils.isBlank(cmd.getSign()) ||
					StringUtils.isBlank(cmd.getMsg()) || !cmd.getToken().equals(token)){
				dto.setMsg_sn("invalid parameter");
				dto.setReturn_code("01");
				return dto;
			}
			msg = new String (AESCoder.decrypt(Base64.decodeBase64(cmd.getMsg()), aesKey.getBytes()), "GBK");
			NotifyEntity result = gson.fromJson(msg, NotifyEntity.class);
			
			User user = UserContext.current().getUser();
			PaymentCard paymentCard = paymentCardProvider.findPaymentCardByCardNo(result.getCard_id(), TaotaoguVendorConstant.TAOTAOGU);
			transaction.setOwnerId(paymentCard.getOwnerId());
			transaction.setOwnerType(paymentCard.getOwnerType());
			transaction.setNamespaceId(user.getNamespaceId());
			transaction.setPayerUid(paymentCard.getUserId());
			transaction.setUserName(paymentCard.getUserName());
			transaction.setMobile(paymentCard.getMobile());
			transaction.setItemName("");
			transaction.setMerchantNo(result.getMerch_id());
			transaction.setMerchantName(result.getMerch_name());
			transaction.setAmount(new BigDecimal(result.getReal_amt()));
			transaction.setTransactionNo(result.getAcct_sn());
			transaction.setTransactionTime(StrTotimestamp(result.getFinal_time()));
			transaction.setCardId(paymentCard.getId());
			transaction.setStatus(convertTransaction(result.getTrade_status()));
			transaction.setCreatorUid(user.getId());
			transaction.setCreateTime(new Timestamp(System.currentTimeMillis()));
			transaction.setVendorName(TaotaoguVendorConstant.TAOTAOGU);
			String extraData = TaotaoguVendorConstant.TAOTAOGU_WEB_CARD_TRANSACTION_STATUS_JSON;
			transaction.setVendorResult(extraData);
			transaction.setToken(result.getToken());
			transaction.setCardNo(result.getCard_id());
			//transaction.setOrderNo(createOrderNo());
			paymentCardProvider.createPaymentCardTransaction(transaction);
    	
			dto.setMsg_sn(result.getMsg_sn());
			dto.setReturn_code("00");
		} catch (Exception e) {
			dto.setReturn_code("01");
		}
    	
    	return dto;
    }
    //转换一下淘淘谷的状态
    private Byte convertTransaction(String status){
    	Byte result = null;
    	switch (status) {
		case "00": result = 3;
			break;
		case "01": result = 4;
			break;
		case "02": result = 0;
			break;
		case "03": result = 1;
			break;
		case "04": result = 2;
			break;
		default:
			break;
		}
    	return result;
    }
    private PaymentCardVendorHandler getPaymentCardVendorHandler(String vendorName) {
    	PaymentCardVendorHandler handler = null;
        
        if(vendorName != null && vendorName.length() > 0) {
            String handlerPrefix = PaymentCardVendorHandler.PAYMENTCARD_VENDOR_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + vendorName);
        }
        
        return handler;
    }
    
    private Long createOrderNo() {
		String bill = String.valueOf(System.currentTimeMillis()) + generateRandomNumber(3);
		return Long.valueOf(bill);
	}

	/**
	 *
	 * @param n 创建n位随机数
	 * @return
	 */
	private long generateRandomNumber(int n){
		return (long)((Math.random() * 9 + 1) * Math.pow(10, n-1));
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
    
    private Timestamp StrTotimestamp(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
		return new Timestamp(d.getTime());
	}
    
    private void checkPaymentCardIsNull(PaymentCard paymentCard,Long id){
    	if(paymentCard == null){
    		LOGGER.error("paymentCard is not exists {}.",id);
    		throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_NOT_EXISTS_CARD,
					localeStringService.getLocalizedString(String.valueOf(PaymentCardErrorCode.SCOPE), 
							String.valueOf(PaymentCardErrorCode.ERROR_NOT_EXISTS_CARD),
							UserContext.current().getUser().getLocale(),"paymentCard is not exists ."));
    	}
    }
    private void checkParam(String ownerType,Long ownerId){
    	if(ownerId == null ) {
        	LOGGER.error("ownerId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"ownerId cannot be null.");
        }
    	
    	if(StringUtils.isBlank(ownerType)) {
        	LOGGER.error("ownerType cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"ownerType cannot be null.");
        }
    }

	@Override
	public void payNotify(OrderPaymentNotificationCommand cmd) {
		if (!PayUtil.verifyCallbackSignature(cmd))
		throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_CREATE_FAIL,
				"signature wrong");
		SrvOrderPaymentNotificationCommand cmd2 = new SrvOrderPaymentNotificationCommand();
		cmd2.setBizOrderNum(cmd.getBizOrderNum());
		cmd2.setOrderId(cmd.getOrderId());
		cmd2.setPaymentType(cmd.getPaymentType());
		paymentCardOrderEmbeddedV2Handler.paySuccess(cmd2);
	}

	@Override
	public void refundNotify(OrderPaymentNotificationCommand cmd) {
		if (!PayUtil.verifyCallbackSignature(cmd))
			throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_CREATE_FAIL,
					"signature wrong");
		SrvOrderPaymentNotificationCommand cmd2 = new SrvOrderPaymentNotificationCommand();
		cmd2.setBizOrderNum(cmd.getBizOrderNum());
		cmd2.setOrderId(cmd.getOrderId());
		paymentCardOrderEmbeddedV2Handler.refundSuccess(cmd2);
	}

	@Override
	public PaymentCardHotlineDTO getHotline(GetHotlineCommand cmd) {
		checkParam(cmd.getOwnerType(), cmd.getOwnerId());
		List<PaymentCardIssuerCommunity> hotlines = paymentCardProvider.listPaymentCardIssuerCommunity(cmd.getOwnerId(), cmd.getOwnerType());
		if (hotlines == null || hotlines.size() == 0)
			return null;
		PaymentCardHotlineDTO dto = ConvertHelper.convert(hotlines.get(0),PaymentCardHotlineDTO.class);
		return dto;
	}

	@Override
	public void updateHotline(UpdateHotlineCommand cmd) {
		checkParam(cmd.getOwnerType(), cmd.getOwnerId());
		List<PaymentCardIssuerCommunity> hotlines = paymentCardProvider.listPaymentCardIssuerCommunity(cmd.getOwnerId(), cmd.getOwnerType());
		if (hotlines == null || hotlines.size() == 0)
			return ;
		PaymentCardIssuerCommunity hotline = hotlines.get(0);
		hotline.setHotline(cmd.getHotline());
		paymentCardProvider.updatePaymentCardIssuerCommunity(hotline);
	}

	@Override
	public void freezeCard(FreezeCardCommand cmd) {
		PaymentCard paymentCard = checkPaymentCard(cmd.getCardId());
		PaymentCardVendorHandler handler = getPaymentCardVendorHandler(paymentCard.getVendorName());
		handler.freezeCard(cmd);
	}

	@Override
	public void unbunleCard(Long cardId) {
		PaymentCard paymentCard = checkPaymentCard(cardId);
		PaymentCardVendorHandler handler = getPaymentCardVendorHandler(paymentCard.getVendorName());
		handler.unbundleCard(paymentCard);
	}

	@Override
	public CardInfoDTO getCardInfo(Long cardId) {
		PaymentCard paymentCard = checkPaymentCard(cardId);
		PaymentCardVendorHandler handler = getPaymentCardVendorHandler(paymentCard.getVendorName());
		return handler.getCardInfo(paymentCard);
	}

}
