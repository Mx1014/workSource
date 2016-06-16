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
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
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
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.order.OrderUtil;
import com.everhomes.payment.taotaogu.AESCoder;
import com.everhomes.payment.taotaogu.NotifyEntity;
import com.everhomes.payment.taotaogu.PaidResultThread;
import com.everhomes.payment.taotaogu.PaidResultThreadPool;
import com.everhomes.payment.taotaogu.SHA1;
import com.everhomes.payment.util.CacheItem;
import com.everhomes.payment.util.CachePool;
import com.everhomes.payment.util.Util;
import com.everhomes.rest.order.CommonOrderCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.payment.ApplyCardCommand;
import com.everhomes.rest.payment.CardInfoDTO;
import com.everhomes.rest.payment.CardIssuerDTO;
import com.everhomes.rest.payment.CardOrderStatus;
import com.everhomes.rest.payment.CardRechargeOrderDTO;
import com.everhomes.rest.payment.CardRechargeStatus;
import com.everhomes.rest.payment.CardTransactionDTO;
import com.everhomes.rest.payment.CardTransactionOfMonth;
import com.everhomes.rest.payment.CardUserDTO;
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
import com.everhomes.rest.payment.NotifyEntityCommand;
import com.everhomes.rest.payment.NotifyEntityDTO;
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
import com.google.gson.Gson;

@Component
public class PaymentCardServiceImpl implements PaymentCardService{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentCardServiceImpl.class);
    private SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
    	User user = UserContext.current().getUser();
    	Integer count = paymentCardProvider.countPaymentCard(cmd.getOwnerId(), cmd.getOwnerType(), user.getId());
    	if(count > 0){
//    		LOGGER.error("plateNumber cannot be null.");
//        	throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PLATE_NULL,
//					localeStringService.getLocalizedString(String.valueOf(ParkingErrorCode.SCOPE), 
//							String.valueOf(ParkingErrorCode.ERROR_PLATE_NULL),
//							UserContext.current().getUser().getLocale(),"plateNumber cannot be null."));
    		LOGGER.error("Already open card.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Already open card.");
    	}
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
    	paymentCardRechargeOrder.setNamespaceId(user.getNamespaceId());
    	paymentCardRechargeOrder.setOrderNo(createOrderNo(user.getId()));
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
    	PaymentCard paymentCard = checkPaymentCard(cmd.getCardId());
    	if(paymentCard == null){
    		LOGGER.error("card id can not be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"card id can not be null.");
    	}
    	PaymentCardVendorHandler handler = getPaymentCardVendorHandler(paymentCard.getVendorName());
		String code = handler.getCardPaidQrCodeByVendor(paymentCard);
		dto.setCode(code);
    	return dto;
    }
    @Override
    public GetCardPaidResultDTO getCardPaidResult(GetCardPaidResultCommand cmd){
    	GetCardPaidResultDTO dto = new GetCardPaidResultDTO();
    	PaymentCard paymentCard = checkPaymentCard(cmd.getCardId());
    	if(paymentCard == null){
    		LOGGER.error("card id can not be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"card id can not be null.");
    	}
    	dto.setToken(cmd.getToken());
    	ExecutorService service = PaidResultThreadPool.getInstance();
    	PaidResultThread thread = new PaidResultThread(dto);
    	service.execute(thread);
    	synchronized (dto) {
			try {
				dto.wait();
			} catch (InterruptedException e) {
				LOGGER.error("card id can not be null.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"card id can not be null.");
			}
		}
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
		//丢到缓存中
		CachePool cachePool = CachePool.getInstance();
		cachePool.putCacheItem(userIdentifier.getIdentifierToken(), verifyCode,10*60*1000);
		
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
//			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PLATE_NULL,
//					localeStringService.getLocalizedString(String.valueOf(ParkingErrorCode.SCOPE), 
//							String.valueOf(ParkingErrorCode.ERROR_PLATE_NULL),
//							UserContext.current().getUser().getLocale(),"plateNumber cannot be null."));
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
		CachePool cachePool = CachePool.getInstance();

    	CacheItem entity = cachePool.getCacheItem(cmd.getMobile());
    	if(entity == null){
    		LOGGER.error("verifyCode is not exists.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"verifyCode is not exists.");
    	}
    	if(!((String)entity.getEntity()).equals(cmd.getVerifyCode())){
    		LOGGER.error("verifyCode is not correctly.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"verifyCode is not correctly.");
    	}
    	if(entity.isExpired()){
    		LOGGER.error("verifyCode is expired.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"verifyCode is expired.");
    	}
    	cachePool.removeCacheItem(cmd.getMobile());
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
    	if(cmd.getPageAnchor() == null)
    		cmd.setPageAnchor(1L);
    	User user = UserContext.current().getUser();
    	ListCardTransactionsResponse response = new ListCardTransactionsResponse();
    	List<PaymentCard> cardList = paymentCardProvider.listPaymentCard(cmd.getOwnerId(),cmd.getOwnerType(),user.getId());
		//for(PaymentCard card:cardList){
    	PaymentCard card = cardList.get(0);
			PaymentCardVendorHandler handler = getPaymentCardVendorHandler(card.getVendorName());
			List<CardTransactionOfMonth> list = handler.listCardTransactions(cmd,card);
		//}
			//按时间排序
			Collections.sort(list);
			response.setRequests(list);
			if(list.size()==0)
				response.setNextPageAnchor(null);
			else
				response.setNextPageAnchor(cmd.getPageAnchor()+1);
		return response;
    }
    
    @Override
    public SearchCardUsersResponse searchCardUsers(SearchCardUsersCommand cmd){
    	SearchCardUsersResponse response = new SearchCardUsersResponse();
		
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		//User user = UserContext.current().getUser();
		List<PaymentCard> list = paymentCardProvider.searchCardUsers(cmd.getOwnerId(),cmd.getOwnerType(),
				cmd.getKeyword(),cmd.getPageAnchor(), pageSize);
    	if(list.size() > 0){
    		response.setRequests(list.stream().map(r -> ConvertHelper.convert(r, CardUserDTO.class))
    				.collect(Collectors.toList()));
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
    
    @Override
    public void exportCardUsers(SearchCardUsersCommand cmd,HttpServletResponse response){
    	Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		//User user = UserContext.current().getUser();
		List<PaymentCard> list = paymentCardProvider.searchCardUsers(cmd.getOwnerId(),cmd.getOwnerType(),
				cmd.getKeyword(),cmd.getPageAnchor(), pageSize);
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
		row.createCell(2).setCellValue("开卡时间");
		row.createCell(3).setCellValue("卡号");
		for(int i=0;i<list.size();i++){
			Row tempRow = sheet.createRow(i + 1);
			PaymentCard paymentCard = list.get(i);
			tempRow.createCell(0).setCellValue(paymentCard.getMobile());
			tempRow.createCell(1).setCellValue(paymentCard.getUserName());
			tempRow.createCell(2).setCellValue(datetimeSF.format(paymentCard.getCreateTime()));
			tempRow.createCell(3).setCellValue(paymentCard.getCardNo());
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			wb.write(out);
			Util.download(out, response);
		} catch (IOException e) {
			LOGGER.error("exportCardUsers is fail. {}",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"exportCardUsers is fail.");
		}
		
	}
    
	@Override
	public void exportCardRechargeOrder(SearchCardRechargeOrderCommand cmd,
			HttpServletResponse response) {
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
			tempRow.createCell(5).setCellValue(order.getPaidType());
			tempRow.createCell(6).setCellValue(order.getRechargeStatus());
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			wb.write(out);
			Util.download(out, response);
		} catch (IOException e) {
			LOGGER.error("exportCardUsers is fail. {}",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"exportCardUsers is fail.");
		}
		
	}
	@Override
	public void exportCardTransactions(SearchCardTransactionsCommand cmd,
			HttpServletResponse response) {
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
			tempRow.createCell(3).setCellValue("");
			tempRow.createCell(4).setCellValue(transaction.getItemName());
			tempRow.createCell(5).setCellValue(transaction.getId());
			tempRow.createCell(6).setCellValue(datetimeSF.format(transaction.getTransactionTime()));
			tempRow.createCell(7).setCellValue(transaction.getAmount().doubleValue());
			tempRow.createCell(8).setCellValue(transaction.getStatus());
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			wb.write(out);
			Util.download(out, response);
		} catch (IOException e) {
			LOGGER.error("exportCardUsers is fail. {}",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"exportCardUsers is fail.");
		}
		
	}
    
    @Override
    public NotifyEntityDTO notifyPaidResult(NotifyEntityCommand cmd){
    	PaymentCardTransaction transaction = new PaymentCardTransaction();
    	CachePool cachePool = CachePool.getInstance();
		String aesKey = cachePool.getStringValue(VendorConstant.TAOTAOGU_AESKEY);
		String token = cachePool.getStringValue(VendorConstant.TAOTAOGU_TOKEN);
    	Gson gson = new Gson();
		Map map = gson.fromJson(cmd.getMsg(), Map.class);
		String data = (String) map.get("data");
		String data1 = null;
		try {
			data1 = new String (AESCoder.decrypt(Base64.decodeBase64(data), aesKey.getBytes()), "GBK");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		NotifyEntity result = gson.fromJson(data1, NotifyEntity.class);
       	User user = UserContext.current().getUser();
    	UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
    	PaymentCard paymentCard = paymentCardProvider.findPaymentCardByCardNo(result.getCard_id(), VendorConstant.TAOTAOGU);
    	transaction.setOwnerId(paymentCard.getOwnerId());
    	transaction.setOwnerType(paymentCard.getOwnerType());
    	transaction.setNamespaceId(user.getNamespaceId());
    	transaction.setItemName("");
    	transaction.setMerchant(result.getMerch_name());
    	transaction.setAmount(new BigDecimal(result.getTrade_amt()));
    	transaction.setTransactionNo(result.getAcct_sn());
    	transaction.setTransactionTime(StrTotimestamp(result.getFinal_time()));
    	transaction.setCardId(paymentCard.getId());
    	transaction.setStatus(convertTransaction(result.getTrade_status()));
    	transaction.setCreatorUid(user.getId());
    	transaction.setCreateTime(new Timestamp(System.currentTimeMillis()));
    	transaction.setVendorName(VendorConstant.TAOTAOGU);
    	transaction.setVendorResult(paymentCard.getVendorCardData());
    	transaction.setToken(result.getToken());
    	transaction.setCardNo(result.getCard_id());
    	transaction.setOrderNo(createOrderNo(user.getId()));
    	
    	paymentCardProvider.createPaymentCardTransaction(transaction);
    	NotifyEntityDTO dto = new NotifyEntityDTO();
    	dto.setMsg_sn(result.getMsg_sn());
    	dto.setReturn_code("00");
    	return dto;
    }

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
    private Long createOrderNo(Long id) {
		String bill = String.valueOf(System.currentTimeMillis()) + (int) (Math.random()*1000);
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
    
}
