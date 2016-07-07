package com.everhomes.payment.taotaogu;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.cert.Cert;
import com.everhomes.cert.CertProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.payment.PaymentCard;
import com.everhomes.payment.PaymentCardIssuer;
import com.everhomes.payment.PaymentCardProvider;
import com.everhomes.payment.PaymentCardRechargeOrder;
import com.everhomes.payment.PaymentCardVendorHandler;
import com.everhomes.rest.payment.ApplyCardCommand;
import com.everhomes.rest.payment.CardInfoDTO;
import com.everhomes.rest.payment.CardRechargeStatus;
import com.everhomes.rest.payment.CardTransactionFromVendorDTO;
import com.everhomes.rest.payment.CardTransactionOfMonth;
import com.everhomes.rest.payment.CardTransactionTypeStatus;
import com.everhomes.rest.payment.ListCardInfoCommand;
import com.everhomes.rest.payment.ListCardTransactionsCommand;
import com.everhomes.rest.payment.PaymentCardErrorCode;
import com.everhomes.rest.payment.PaymentCardStatus;
import com.everhomes.rest.payment.ResetCardPasswordCommand;
import com.everhomes.rest.payment.SetCardPasswordCommand;
import com.everhomes.user.EncryptionUtils;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

@Component(PaymentCardVendorHandler.PAYMENTCARD_VENDOR_PREFIX + "TAOTAOGU")
public class TaotaoguPaymentCardVendorHandler implements PaymentCardVendorHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(TaotaoguPaymentCardVendorHandler.class);
	
	private static final String CARD_QUERY_TYPE = "1020";
	private static final String ACCOUNT_QUERY_TYPE = "1010";
	private static final String QUERY_TRANSACTION_TYPE = "1030";
	private static final String CARD_CREATE_TYPE = "0000";
	private static final String CARD_SALE_TYPE = "0010";
	private static final String CHANGE_PASSWORD_TYPE = "0050";
	private static final String RESET_PASSWORD_TYPE = "0040";
	private static final String CARD_RECHARGE_TYPE = "0020";
	private static final String BUSINESS_QUERY_TYPE = "9990";
	@Autowired
    private PaymentCardProvider paymentCardProvider;
	@Autowired
    private ConfigurationProvider configProvider;
	@Autowired
	private CertProvider certProvider;
	@Autowired
	private LocaleStringService localeStringService;
	@Autowired
    BigCollectionProvider bigCollectionProvider;
	@Autowired
    private CoordinationProvider coordinationProvider;
	
	private static CloseableHttpClient httpClient = HttpClients.createDefault();

	@Override
	public List<CardInfoDTO> getCardInfoByVendor(ListCardInfoCommand cmd) {
		User user = UserContext.current().getUser();
		List<CardInfoDTO> result = new ArrayList<CardInfoDTO>();
		List<PaymentCard> cardList = paymentCardProvider.listPaymentCard(cmd.getOwnerId(),cmd.getOwnerType(),user.getId());
		for(PaymentCard card:cardList){
			PaymentCardIssuer issuer = paymentCardProvider.findPaymentCardIssuerById(card.getIssuerId());
			String vendorData = issuer.getVendorData();

			TaotaoguVendorData taotaoguVendorData = (TaotaoguVendorData) StringHelper.fromJsonString(vendorData, TaotaoguVendorData.class);
			String brandCode = taotaoguVendorData.getBranchCode();
			CardInfoDTO cardInfo = new CardInfoDTO();

			try {
				Map<String, Object> cardQuertParam = new HashMap<String, Object>();
				cardQuertParam.put("BranchCode", brandCode);
				cardQuertParam.put("CardId", card.getCardNo());
				Map<String, Object> queryCardResultMap = queryCard(taotaoguVendorData, brandCode, card.getCardNo());
				Map<String, Object> queryAccountResultMap = queryAccount(taotaoguVendorData, brandCode, card.getCardNo());
			
				if(queryCardResultMap != null){
					cardInfo.setCardId(card.getId());
					cardInfo.setCardNo((String)queryCardResultMap.get("CardId"));
					cardInfo.setCardType((String)queryCardResultMap.get("CardSubClass"));
					String effDate = (String)queryCardResultMap.get("EffDate");
					String expirDate = (String)queryCardResultMap.get("ExpirDate");
					cardInfo.setActivedTime(strTotimestamp(effDate));
					cardInfo.setExpiredTime(strTotimestamp(expirDate));
					String cardStatus = (String)queryCardResultMap.get("CardStatus");
					cardInfo.setStatus(cardStatus);
					cardInfo.setMobile(card.getMobile());
					cardInfo.setVendorCardData(card.getVendorCardData());
				}
				if(queryAccountResultMap != null){
					cardInfo.setCardId(card.getId());
					List list = (List) queryAccountResultMap.get("Row");
					for(int i=0;i<list.size();i++){
						Map map = (Map) list.get(i);
						if("fund".equals(((String)map.get("SubAcctType")).trim())){
							cardInfo.setBalance(new BigDecimal(map.get("AvlbBal").toString()));
							break;
						}
					}
				}
			} catch (Exception e) {
				LOGGER.error("the getCardInfo request of taotaogu failed card={}.",card,e);
				throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
						"the getCardInfo request of taotaogu failed.");
			}
			result.add(cardInfo);
		}
		return result;
	}
	
	@Override
	public CardInfoDTO applyCard(ApplyCardCommand cmd,PaymentCardIssuer cardIssuer) {
		CardInfoDTO cardInfoDTO = new CardInfoDTO();
		String vendorData = cardIssuer.getVendorData();
		TaotaoguVendorData taotaoguVendorData = (TaotaoguVendorData) StringHelper.fromJsonString(vendorData, TaotaoguVendorData.class);

		String brandCode = taotaoguVendorData.getBranchCode();
		String cardPatternid = taotaoguVendorData.getCardPatternid();
		Map<String, Object> createCardResultMap = createCard(taotaoguVendorData, brandCode, cardPatternid, cmd.getMobile());
		try {
				String cardId = (String)createCardResultMap.get("StrCardId");
				Map<String, Object> saleCardResultMap = saleCard(taotaoguVendorData, brandCode, cardId);
				String initPassword = taotaoguVendorData.getInitPassword();
				changePassword(taotaoguVendorData, brandCode, cardId, initPassword, cmd.getPassword());
				Map<String,Object> queryCardResultMap = queryCard(taotaoguVendorData, brandCode, cardId);
				
				if(queryCardResultMap != null){
					String effDate = (String)queryCardResultMap.get("EffDate");
					String expirDate = (String)queryCardResultMap.get("ExpirDate");
				
					User user = UserContext.current().getUser();
					PaymentCard paymentCard = new PaymentCard();
					paymentCard.setOwnerId(cmd.getOwnerId());
					paymentCard.setOwnerType(cmd.getOwnerType());
					paymentCard.setNamespaceId(user.getNamespaceId());
					paymentCard.setIssuerId(cardIssuer.getId());
					paymentCard.setUserName(user.getNickName());
					paymentCard.setMobile(cmd.getMobile());
					paymentCard.setCardNo(cardId);
					paymentCard.setBalance(new BigDecimal(0));
					paymentCard.setPassword(EncryptionUtils.hashPassword(cmd.getPassword()));
					paymentCard.setUserId(user.getId());
					paymentCard.setCreateTime(new Timestamp(System.currentTimeMillis()));
					paymentCard.setActivateTime(strTotimestamp(effDate));
					paymentCard.setExpiredTime(strTotimestamp(expirDate));
					paymentCard.setCreatorUid(user.getId());
					paymentCard.setStatus(PaymentCardStatus.ACTIVE.getCode());
					paymentCard.setVendorName(TaotaoguVendorConstant.TAOTAOGU);
					paymentCard.setVendorCardData(TaotaoguVendorConstant.TAOTAOGU_CARD__STATUS_JSON);
					paymentCardProvider.createPaymentCard(paymentCard);
					cardInfoDTO = ConvertHelper.convert(paymentCard, CardInfoDTO.class);
				}
		} catch (Exception e) {
			LOGGER.error("the apply card request of taotaogu failed cmd={},cardIssuerId={}.", cmd, cardIssuer.getId(), e);
			throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
					"the apply card request of taotaogu failed.");
		}
		return cardInfoDTO;
	}
	public void setCardPassword(SetCardPasswordCommand cmd,PaymentCard paymentCard){
		PaymentCardIssuer issuer = paymentCardProvider.findPaymentCardIssuerById(paymentCard.getIssuerId());
		String vendorData = issuer.getVendorData();
		TaotaoguVendorData taotaoguVendorData = (TaotaoguVendorData) StringHelper.fromJsonString(vendorData, TaotaoguVendorData.class);
		String brandCode = taotaoguVendorData.getBranchCode();
		
		changePassword(taotaoguVendorData, brandCode, paymentCard.getCardNo(), cmd.getOldPassword(),cmd.getNewPassword());
		
		paymentCard.setPassword(EncryptionUtils.hashPassword(cmd.getNewPassword()));
		paymentCardProvider.updatePaymentCard(paymentCard);
			
	}

	@Override
	public void resetCardPassword(ResetCardPasswordCommand cmd, PaymentCard paymentCard) {
		PaymentCardIssuer issuer = paymentCardProvider.findPaymentCardIssuerById(paymentCard.getIssuerId());
		String vendorData = issuer.getVendorData();
		TaotaoguVendorData taotaoguVendorData = (TaotaoguVendorData) StringHelper.fromJsonString(vendorData, TaotaoguVendorData.class);
		String brandCode = taotaoguVendorData.getBranchCode();

		Map<String, Object> resetPasswordResultMap = resetPassword(taotaoguVendorData, brandCode, paymentCard.getCardNo());
		changePassword(taotaoguVendorData, brandCode, paymentCard.getCardNo(), taotaoguVendorData.getInitPassword(), cmd.getNewPassword());
		paymentCard.setPassword(EncryptionUtils.hashPassword(cmd.getNewPassword()));
		paymentCardProvider.updatePaymentCard(paymentCard);
		
	}
	@Override
	public List<CardTransactionOfMonth> listCardTransactions(ListCardTransactionsCommand cmd,PaymentCard card){
		PaymentCardIssuer issuer = paymentCardProvider.findPaymentCardIssuerById(card.getIssuerId());
		String vendorData = issuer.getVendorData();
		TaotaoguVendorData taotaoguVendorData = (TaotaoguVendorData) StringHelper.fromJsonString(vendorData, TaotaoguVendorData.class);
		String brandCode = taotaoguVendorData.getBranchCode();
		//203 充值类型
		Map<String, Object> rechargeTransactionResultMap = queryTransaction(taotaoguVendorData, brandCode, card.getCardNo(), "203", cmd.getPageAnchor(), cmd.getPageSize());
		//101 消费类型
		Map<String, Object> consumeTransactionResultMap = queryTransaction(taotaoguVendorData, brandCode, card.getCardNo(), "101", cmd.getPageAnchor(), cmd.getPageSize());

		List<CardTransactionOfMonth> resultList = new ArrayList<>();
		if(rechargeTransactionResultMap != null && consumeTransactionResultMap != null){
			
			String count = rechargeTransactionResultMap.get("Count").toString();
			List<Map<String,Object>> rechargeList = (List<Map<String, Object>>) rechargeTransactionResultMap.get("Row");
			List<Map<String,Object>> consumeList = (List<Map<String, Object>>) consumeTransactionResultMap.get("Row");
			rechargeList.addAll(consumeList);
			outer:
			for(Map<String,Object> m:rechargeList){
				
				BigDecimal consumeAmount = new BigDecimal(0);
				BigDecimal rechargeAmount = new BigDecimal(0);
				
				CardTransactionFromVendorDTO dto = new CardTransactionFromVendorDTO();
				dto.setVendorResult(TaotaoguVendorConstant.TAOTAOGU_APP_CARD_TRANSACTION_STATUS_JSON);
				dto.setMerchant((String)m.get("MerchId"));
				BigDecimal amount = null;
				
				String transactionType = (String)m.get("TransType");
				if("101".equals(transactionType)){
					dto.setTransactionType(CardTransactionTypeStatus.CONSUME.getCode());
					amount = new BigDecimal(m.get("ChdrPdpAmt").toString());
					dto.setAmount(amount);
					String status = (String)m.get("ProcStatus");
					dto.setStatus(status);
					if("01".equals(status))
						consumeAmount = consumeAmount.add(amount);
				}else if("203".equals(transactionType)){
					dto.setTransactionType(CardTransactionTypeStatus.RECHARGE.getCode());
					amount = new BigDecimal(m.get("TransAmt").toString());
					dto.setAmount(amount);
					String status = (String)m.get("ProcStatus");
					dto.setStatus(status);
					if("01".equals(status))
						rechargeAmount = rechargeAmount.add(amount);
				}else
					continue outer;
				
				
				String recvTime = (String)m.get("RecvTime");
				dto.setTransactionTime(strToLong(recvTime));
				
				for(CardTransactionOfMonth ctm:resultList){
					if(ctm.getDate().equals(strToDate(recvTime))){
						List<CardTransactionFromVendorDTO> requests = ctm.getRequests();
						ctm.setConsumeAmount(ctm.getConsumeAmount().add(consumeAmount));
						ctm.setRechargeAmount(ctm.getRechargeAmount().add(rechargeAmount));
						requests.add(dto);
						continue outer;
					}
				}
				CardTransactionOfMonth  cardTransactionOfMonth = new CardTransactionOfMonth();
				List<CardTransactionFromVendorDTO> cardTransactionList = new ArrayList<>();
				cardTransactionList.add(dto);
				cardTransactionOfMonth.setRequests(cardTransactionList);
				cardTransactionOfMonth.setDate(strToDate(recvTime));
				cardTransactionOfMonth.setConsumeAmount(consumeAmount);
				cardTransactionOfMonth.setRechargeAmount(rechargeAmount);
				resultList.add(cardTransactionOfMonth);
			}
		}
		//按时间排序
		for( CardTransactionOfMonth cardTransactionOfMonth:resultList) {
			Collections.sort(cardTransactionOfMonth.getRequests());
		}
		Collections.sort(resultList);
		return resultList;
	}
	//制卡
		private Map<String,Object> createCard(TaotaoguVendorData taotaoguVendorData, String brandCode, String cardPatternid, String mobile) {
			Map<String, Object> createCardParam = new HashMap<String, Object>();
			createCardParam.put("BranchCode", brandCode);
			createCardParam.put("CardPatternid", cardPatternid);
			createCardParam.put("CardNum", "1");
			createCardParam.put("DeliveryUser", "");
			createCardParam.put("DeliveryContact", mobile);
			Map<String,Object> result = null;
			result = post(createRequestParam(taotaoguVendorData, CARD_CREATE_TYPE, createCardParam));
			return result;
		}
		//售卡
		private Map<String,Object> saleCard(TaotaoguVendorData taotaoguVendorData, String brandCode, String cardId) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("BranchCode", brandCode);
			param.put("CardId", cardId);
			param.put("EndCardId", "");
			param.put("SaleNum", "");
			param.put("SaleType", "1");
			param.put("Amt", "0.00");
			param.put("Expenses", "0.00");
			param.put("RealAmt", "");
			param.put("PayAccNo", "");
			param.put("PayAccName", "");
			param.put("SaleProxyUserId", "");
			param.put("ActivityId", "");
			param.put("DiscountAmt", "");
			param.put("GiveAmt", "");
			param.put("SaleUser", "");
			Map<String, Object> result = post(createRequestParam(taotaoguVendorData,CARD_SALE_TYPE,param));
			
			return result;
		}
		//修改密码
		private Map<String,Object> changePassword(TaotaoguVendorData taotaoguVendorData, String brandCode, String cardId,String oldPassword, String newPassword) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("BranchCode", brandCode);
			param.put("CardId", cardId);
			Cert cert = certProvider.findCertByName(configProvider.getValue(TaotaoguVendorConstant.PIN3_CRT, TaotaoguVendorConstant.PIN3_CRT));
			if(cert == null){
				LOGGER.error("taotaogu.pin3.crt is null.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"taotaogu.pin3.crt is null.");
			}
			InputStream in = new ByteArrayInputStream(cert.getData());
			
			byte[] oldpsd;
			try {
				oldpsd = CertCoder.encryptByPublicKey(oldPassword.getBytes(), in);
				param.put("OrigPassWord", ByteTools.BytesToHexStr(oldpsd));
				byte[] newpsd = CertCoder.encryptByPublicKey(newPassword.getBytes(), in);
				param.put("NewPassWord", ByteTools.BytesToHexStr(newpsd));
			} catch (Exception e) {
				LOGGER.error("the encryptByPublicKey password failed cardId={},oldPassword={},newPassword={}.", cardId, oldPassword, newPassword, e);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"the apply card request of taotaogu failed.");
			}
			param.put("Remark", "");
			Map<String,Object> result = post(createRequestParam(taotaoguVendorData, CHANGE_PASSWORD_TYPE, param));
			return result;
		}
		//重置密码
		private Map<String,Object> resetPassword(TaotaoguVendorData taotaoguVendorData, String brandCode, String cardId) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("BranchCode", brandCode);
			param.put("CardId", cardId);
			param.put("Remark", "");
			Map<String, Object> result = post(createRequestParam(taotaoguVendorData,RESET_PASSWORD_TYPE,param));
			return result;
		}
		//查询卡信息
		private Map<String,Object> queryCard(TaotaoguVendorData taotaoguVendorData, String brandCode, String cardId) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("BranchCode", brandCode);
			param.put("CardId", cardId);
			Map<String,Object> result = post(createRequestParam(taotaoguVendorData, CARD_QUERY_TYPE, param));
			return result;
		}
		//查询账户信息
		private Map<String,Object> queryAccount(TaotaoguVendorData taotaoguVendorData, String brandCode, String cardId) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("BranchCode", brandCode);
			param.put("CardId", cardId);
			param.put("AcctType", "00");
			param.put("SubAcctType", "");
			Map<String,Object> result = post(createRequestParam(taotaoguVendorData, ACCOUNT_QUERY_TYPE, param));
			return result;
		}
	
	private Map<String,Object> queryTransaction(TaotaoguVendorData taotaoguVendorData, String brandCode, String cardId
			,String transType,Long pageAnchor,Integer pageSize){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("BranchCode", brandCode);
		param.put("CardId", cardId);
		param.put("EndCardId", "");
		param.put("MerchId", "");
		param.put("TerminalId", "");
		param.put("StartDate", "");
		param.put("Enddate", "");
		param.put("TransType", transType);
		param.put("PageNumber", pageAnchor==null?"1":String.valueOf(pageAnchor));
		param.put("PageSize", pageSize==null?"10":String.valueOf(pageSize));
		Map<String, Object> result = post(createRequestParam(taotaoguVendorData, QUERY_TRANSACTION_TYPE, param));
		return result;
	}
	private Timestamp strTotimestamp(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
		return new Timestamp(d.getTime());
	}
 
	private Long strToLong(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
		return d.getTime();
	}
	
	private Long strToDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime().getTime();
	}

	@Override
	public String getCardPaidQrCodeByVendor(PaymentCard paymentCard) {
		PaymentCardIssuer issuer = paymentCardProvider.findPaymentCardIssuerById(paymentCard.getIssuerId());
		String vendorData = issuer.getVendorData();
		TaotaoguVendorData taotaoguVendorData = (TaotaoguVendorData) StringHelper.fromJsonString(vendorData, TaotaoguVendorData.class);
		String result = null;
		TaotaoguTokenCacheItem cacheItem = getToken(paymentCard.getIssuerId(),taotaoguVendorData);
		String aesKey = cacheItem.getAesKey();
		String token = cacheItem.getToken();
		Map<String,Object> codeMap = getCardPaidQrCode(taotaoguVendorData, paymentCard.getCardNo(), paymentCard.getIssuerId(),token,aesKey);
		if(codeMap != null){
			String returnCode = (String) codeMap.get("return_code");
			if("00".equals(returnCode)){
				result = (String) codeMap.get("token");
			}else{
				cacheItem = getRefreshToken(paymentCard.getIssuerId(), taotaoguVendorData);
				aesKey = cacheItem.getAesKey();
				token = cacheItem.getToken();
				codeMap = getCardPaidQrCode(taotaoguVendorData, paymentCard.getCardNo(), paymentCard.getIssuerId(),token,aesKey);
				returnCode = (String) codeMap.get("return_code");
				if("00".equals(returnCode)){
					result = (String) codeMap.get("token");
				}else{
					LOGGER.error("the getCardPaidQrCode request of taotaogu failed codeMap={},token={},aesKey={}.",codeMap,token,aesKey);
					throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_GET_CARD_CODE,
							"the getCardPaidQrCode request of taotaogu failed .");
				}
			}
		}
		return result;
	}
	
	private Map<String,Object> getCardPaidQrCode(TaotaoguVendorData taotaoguVendorData,String cardId,Long issuerId,String token,String aesKey){
		
		JSONObject json = new JSONObject();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// 可以方便地修改日期格式
		String timeStr = dateFormat.format(now);
		String chnl_type = taotaoguVendorData.getChnlType();
		String chnl_id = taotaoguVendorData.getChnlId();
		json.put("chnl_type", chnl_type);
		json.put("chnl_id", chnl_id);
		json.put("chnl_sn", System.currentTimeMillis());
		json.put("card_id", cardId);
		json.put("reserved", "");
		json.put("request_time", timeStr);
		
		if(LOGGER.isDebugEnabled())
			LOGGER.debug(" getCardPaidQrCodeByVendor json={},token={},aesKey={}",json,token,aesKey);
		Map codeMap = post("/iips2/order/tokenrequest",token, aesKey, json);
		return codeMap;
	}
	
    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    private TaotaoguTokenCacheItem getToken(Long issuerId,TaotaoguVendorData taotaoguVendorData) {
    	return this.coordinationProvider.getNamedLock(CoordinationLocks.PAYMENT_CARD.getCode()).enter(()-> {
    		Object obj = getTokenFromCache(issuerId);
    		TaotaoguTokenCacheItem cacheItem = null;
            if(obj != null) {
            	cacheItem = (TaotaoguTokenCacheItem) StringHelper.fromJsonString(obj.toString(), TaotaoguTokenCacheItem.class);
                // 如果过期
                if(cacheItem.isExpired()){
                	refreshToken(issuerId,taotaoguVendorData);
                	cacheItem = (TaotaoguTokenCacheItem) StringHelper.fromJsonString(getTokenFromCache(issuerId).toString(),
                			TaotaoguTokenCacheItem.class);
                }
                // 没过期就直接使用
                
            } else {
               refreshToken(issuerId,taotaoguVendorData);
               cacheItem = (TaotaoguTokenCacheItem) StringHelper.fromJsonString(getTokenFromCache(issuerId).toString(),
           			TaotaoguTokenCacheItem.class);
            }
            return cacheItem;
        }).first();
    	
    }
    
    private TaotaoguTokenCacheItem getRefreshToken(Long issuerId,TaotaoguVendorData taotaoguVendorData) {
    	this.coordinationProvider.getNamedLock(CoordinationLocks.PAYMENT_CARD.getCode()).enter(()-> {
    		refreshToken(issuerId,taotaoguVendorData);
            return null;
        });
    	return getToken(issuerId, taotaoguVendorData);
    }
    
    private void refreshToken(Long issuerId,TaotaoguVendorData taotaoguVendorData) {
        String key = getTokenKey(issuerId);
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
      
        Map map = login(taotaoguVendorData);
        TaotaoguTokenCacheItem cacheItem = new TaotaoguTokenCacheItem();
        cacheItem.setCreateTime(new Date());
        cacheItem.setExpireTime(20 * 60 * 60 * 1000);
        cacheItem.setAesKey((String) map.get("aes_key"));
        cacheItem.setToken((String) map.get("token"));
        redisTemplate.opsForValue().set(key,StringHelper.toJsonString(cacheItem));
        redisTemplate.expire(key, 20, TimeUnit.HOURS);
    }
    
    private Object getTokenFromCache(Long issuerId) {
        String key = getTokenKey(issuerId);
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
      
        Object value = redisTemplate.opsForValue().get(key);
        
        Object obj = null;
        if(value != null) {
            obj = StringHelper.fromJsonString(value.toString(), Object.class);    
        }
        return obj;
    }
    
    private String getTokenKey(Long issuerId) {
        return "taotaogu-token-" + issuerId;
    }
	
	@Override
	public void rechargeCard(PaymentCardRechargeOrder order, PaymentCard card) {
		PaymentCardIssuer issuer = paymentCardProvider.findPaymentCardIssuerById(card.getIssuerId());
		String vendorData = issuer.getVendorData();
		TaotaoguVendorData taotaoguVendorData = (TaotaoguVendorData) StringHelper.fromJsonString(vendorData, TaotaoguVendorData.class);
		String brandCode = taotaoguVendorData.getBranchCode();
		TaotaoguResponseEntiy rechargeCardResult = rechargeCard(taotaoguVendorData, brandCode, card.getCardNo(), order.getAmount());
		try {
			if(rechargeCardResult.isSuccess()){
				order.setRechargeStatus(CardRechargeStatus.RECHARGED.getCode());
				order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
				paymentCardProvider.updatePaymentCardRechargeOrder(order);
				return;
			}else if("A0".equals(rechargeCardResult.getRespCode())){
				boolean flag = true;
				int i = 1;
				while(flag&&i<=10){
					i++;
					Map<String, Object>  map = queryBusiness(taotaoguVendorData, brandCode, CARD_RECHARGE_TYPE, rechargeCardResult.getMsgID());
						String status = (String) map.get("ProcStatus");
						if("01".equals(status)){
							flag = false;
							order.setRechargeStatus(CardRechargeStatus.RECHARGED.getCode());
							order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
							paymentCardProvider.updatePaymentCardRechargeOrder(order);
							return;
						}if("06".equals(status)){
							flag = false;
							order.setRechargeStatus(CardRechargeStatus.FAIL.getCode());
							order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
							paymentCardProvider.updatePaymentCardRechargeOrder(order);
							return;
						}
					Thread.sleep(5000);
				}
				if(flag){
					order.setRechargeStatus(CardRechargeStatus.FAIL.getCode());
					order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
					paymentCardProvider.updatePaymentCardRechargeOrder(order);
				}
			}else{
				order.setRechargeStatus(CardRechargeStatus.FAIL.getCode());
				order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
				paymentCardProvider.updatePaymentCardRechargeOrder(order);
				return;
			}
			
		}catch (Exception e) {
			LOGGER.error("the recharge request of taotaogu failed.",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"the recharge request of taotaogu failed.");
		}
		
	}
	//充值
	private TaotaoguResponseEntiy rechargeCard(TaotaoguVendorData taotaoguVendorData, String brandCode, String cardId,BigDecimal amount
			){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("BranchCode", brandCode);
		param.put("CardId", cardId);
		param.put("EndCardId", "");
		param.put("RechargeNum", "");
		param.put("RechargeType", "0");
		param.put("RechargeAcctType", "fund");
		param.put("Amt", amount.toString());
		param.put("RealAmt", amount.toString());
		param.put("PayAccNo", "");
		param.put("PayAccName", "");
		param.put("ActivityId", "");
		param.put("DiscountAmt", "");
		param.put("GiveAmt", "");
		param.put("SaleUser", "");
		TaotaoguResponseEntiy result = postForTaotaoguResponseEntiy(createRequestParam(taotaoguVendorData, CARD_RECHARGE_TYPE, param));
		return result;
	}
	//查询业务
		private Map<String,Object> queryBusiness(TaotaoguVendorData taotaoguVendorData, String brandCode, String queryType,
				String origMsgId){
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("QueryType", queryType);
			param.put("OrigMsgId", origMsgId);
			Map<String, Object> result = post(createRequestParam(taotaoguVendorData, BUSINESS_QUERY_TYPE, param));
			return result;
		}
	//创建登录参数
	private JSONObject createLoginParam(TaotaoguVendorData taotaoguVendorData){
		JSONObject json = new JSONObject();
		String chnl_type = taotaoguVendorData.getChnlType();
		String chnl_id = taotaoguVendorData.getChnlId();
		String merch_id = taotaoguVendorData.getMerchId();
		String termnl_id = taotaoguVendorData.getTermnlId();
		json.put("chnl_type", chnl_type);
		json.put("chnl_id", chnl_id);
		json.put("chnl_sn", System.currentTimeMillis());
		json.put("merch_id", merch_id);
		json.put("termnl_id", termnl_id);
		return json;
	}
	//卡信息接口，创建请求参数方法
		private String createRequestParam(TaotaoguVendorData taotaoguVendorData,String msgType,Map<String, Object> param){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			
			Map<String, Object> requestParam = new HashMap<String, Object>();
			String appName = taotaoguVendorData.getAppName();
			String version = taotaoguVendorData.getVersion();
			String dstId = taotaoguVendorData.getDstId();
			String brandCode = (String) taotaoguVendorData.getBranchCode();
			requestParam.put("AppName", appName);
			requestParam.put("Version",version);
			requestParam.put("ClientDt",sdf.format(new Date()));
			requestParam.put("SrcId",brandCode);
			requestParam.put("DstId",dstId);
			requestParam.put("MsgType",msgType);
			requestParam.put("MsgID",brandCode + StringUtils.leftPad(String.valueOf(System.currentTimeMillis()), 24, "0"));
			requestParam.put("Sign", "");

			requestParam.put("Param",param);
			byte[] data = StringHelper.toJsonString(requestParam).getBytes();
			try {
				Cert cert = certProvider.findCertByName(configProvider.getValue(TaotaoguVendorConstant.KEY_STORE, TaotaoguVendorConstant.KEY_STORE));
				if(cert == null){
					LOGGER.error("taotaogu.keystore is null.");
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
							"taotaogu.keystore is null.");
				}
				InputStream in = new ByteArrayInputStream(cert.getData());
				String pass = cert.getCertPass();
				String[] passArr = pass.split(",");
				byte[] sign = null;
				sign = CertCoder.sign(data, in,passArr[0], passArr[1], passArr[2]);
				requestParam.put("Sign",ByteTools.BytesToHexStr(sign));
			} catch (Exception e) {
				LOGGER.error("createRequestParam failed taotaoguVendorData={},msgType={},param={}.",taotaoguVendorData,msgType,StringHelper.toJsonString(param),e);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"createRequestParam failed.");
			}
			return StringHelper.toJsonString(requestParam);
		}
	//卡信息，http请求方法，返回解析responseEntity之后的结果
	private Map<String,Object> post(String msg){
		String cardUrl = configProvider.getValue("taotaogu.card.url", "");
		HttpPost request = new HttpPost(cardUrl);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		try{
			pairs.add(new BasicNameValuePair("msg", msg));
			request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
			HttpResponse rsp = httpClient.execute(request);
			StatusLine status = rsp.getStatusLine();
			
			if(status.getStatusCode() == 200){
				String rspText = EntityUtils.toString(rsp.getEntity(), "UTF-8");
				if(LOGGER.isDebugEnabled())
					LOGGER.debug("post(String msg),rspText={}, status={}.",rspText,status);
				TaotaoguResponseEntiy resp = (TaotaoguResponseEntiy) StringHelper.fromJsonString(rspText, TaotaoguResponseEntiy.class);
				if(!resp.isSuccess()) {
					LOGGER.error("post(String msg) of taotaogu failed,msg={}, result={}.",msg,rspText);
					throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
							"post(String msg) of taotaogu failed.");
				}
				return resp.getData();
			}else{
				LOGGER.error("post(String msg) http request of taotaogu status is not 200,msg={}, status={}.",msg,status);
				throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
						"post(String msg) http request of taotaogu status is not 200.");
			}
		}catch(Exception e){
			LOGGER.error("post(String msg) http request of taotaogu exception.",e);
			throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
					"post(String msg) http request of taotaogu exception.");
		}
	}
	//卡信息接口，直接返回淘淘谷responseEntity信息
	private TaotaoguResponseEntiy postForTaotaoguResponseEntiy(String msg){
		String cardUrl = configProvider.getValue("taotaogu.card.url", "");
		HttpPost request = new HttpPost(cardUrl);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		try{
			pairs.add(new BasicNameValuePair("msg", msg));
			request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
			HttpResponse rsp = httpClient.execute(request);
			StatusLine status = rsp.getStatusLine();
			
			if(status.getStatusCode() == 200){
				String rspText = EntityUtils.toString(rsp.getEntity(), "UTF-8");
				if(LOGGER.isDebugEnabled())
					LOGGER.debug("postForTaotaoguResponseEntiy,rspText={},status={}.",rspText,status);
				TaotaoguResponseEntiy resp = (TaotaoguResponseEntiy) StringHelper.fromJsonString(rspText, TaotaoguResponseEntiy.class);
				if(!resp.isSuccess()) {
					LOGGER.error("postForTaotaoguResponseEntiy failed,msg={}, result={}.",msg,rspText);
					throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
							"postForTaotaoguResponseEntiy failed.");
				}
				return resp;
			}else{
				LOGGER.error("postForTaotaoguResponseEntiy http status is {},msg={}, status={}.",status.getStatusCode(),msg,status);
				throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
						"postForTaotaoguResponseEntiy http status is not 200.");
			}
		}catch(Exception e){
			LOGGER.error("postForTaotaoguResponseEntiy post request exception.",e);
			throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
					"postForTaotaoguResponseEntiy post request exception.");
		}
	}
	//订单登录方法，因为参数，和返回结果解析和其他普通订单方法不一样，所以单独写一个方法，获取token和aesKey
	private Map<String,Object> login(TaotaoguVendorData taotaoguVendorData){
		Map<String,Object> result = null;
		String rspText = null;
		JSONObject json = createLoginParam(taotaoguVendorData);
		try{
			String orderUrl = configProvider.getValue("taotaogu.order.url", "");
			HttpPost request = new HttpPost(orderUrl+"/iips2/order/login");
			
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			Cert serverCer = certProvider.findCertByName(configProvider.getValue(TaotaoguVendorConstant.SERVER_CER, TaotaoguVendorConstant.SERVER_CER));
			if(serverCer == null){
				LOGGER.error("taotaogu.server.cer is null.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"taotaogu.server.cer is null.");
			}
			InputStream serverCerIn = new ByteArrayInputStream(serverCer.getData());
			Cert clientPfx = certProvider.findCertByName(configProvider.getValue(TaotaoguVendorConstant.CLIENT_PFX, TaotaoguVendorConstant.CLIENT_PFX));
			if(clientPfx == null){
				LOGGER.error("taotaogu.server.cer is null.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"taotaogu.server.cer is null.");
			}
			InputStream clientPfxIn = new ByteArrayInputStream(clientPfx.getData());
			
			String msg = json.toString();
			msg = Base64.encodeBase64String(OrderCertCoder.encryptByPublicKey(msg.getBytes(), serverCerIn));
			
			byte[] r=  OrderCertCoder.sign(msg.getBytes(), clientPfxIn,null, clientPfx.getCertPass());
			String sign = Base64.encodeBase64String(r);
			
			pairs.add(new BasicNameValuePair("msg", msg));
			pairs.add(new BasicNameValuePair("sign", sign));
			
			request.setEntity(new UrlEncodedFormEntity(pairs, "GBK"));
			HttpResponse rsp = httpClient.execute(request);
			StatusLine status = rsp.getStatusLine();
			rspText = EntityUtils.toString(rsp.getEntity(), "GBK");
			if(LOGGER.isDebugEnabled())
				LOGGER.debug("login info :rspText={}",rspText);   
			int a = rspText.indexOf("msg=");
			int b = rspText.indexOf("&sign=");
			String r1 = null; //结果字符串
			if(b != -1){
				msg = rspText.substring(a + 4, b);
				sign = rspText.substring(b + 6);
				boolean bSign = OrderCertCoder.verifySign(msg.getBytes(), Base64.decodeBase64(sign), serverCerIn);
				r = OrderCertCoder.decryptByPrivateKey(Base64.decodeBase64(msg), clientPfxIn, null, clientPfx.getCertPass());
				r1 = new String(r, "GBK");
			}else{
				r1 = rspText.substring(a + 4);
			}
			result = (Map<String,Object>) StringHelper.fromJsonString(r1, Map.class);
		}catch(Exception e){
			LOGGER.error("the login request of taotaogu failed rspText={},json={}.",rspText,json,e);
			throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
					"the login request of taotaogu failed.");
		}
		return result;
	}
	//订单接口，http请求方法
	private Map<String,Object> post(String method,String token,String aesKey,JSONObject json){
		String orderUrl = configProvider.getValue("taotaogu.order.url", "");
		Map<String,Object> result = null;
		String rspText = null;
		try{
			HttpPost request = new HttpPost(orderUrl+method);
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				
			pairs.add(new BasicNameValuePair("token", token));
			String msg = Base64.encodeBase64String(AESCoder.encrypt(json.toString().getBytes("GBK"), aesKey.getBytes()));
			pairs.add(new BasicNameValuePair("msg", msg));
			pairs.add(new BasicNameValuePair("sign", SHA1.EnCodeSHA1(msg + aesKey + token)));
			request.setEntity(new UrlEncodedFormEntity(pairs, "GBK"));
			HttpResponse rsp = httpClient.execute(request);
			
			StatusLine status = rsp.getStatusLine();
			if(LOGGER.isDebugEnabled())
				LOGGER.debug("the login request of taotaogu info method={},token={},aesKey={},param={},rspText={},status={}.",
						method,token,aesKey,json,rspText,status);
			if(status.getStatusCode() == 200){
				rspText = EntityUtils.toString(rsp.getEntity(), "GBK");
				result = resolveOrderResult(rspText, aesKey);
				return result;
			}else{
				LOGGER.error("the http request of taotaogu status is not 200 method={},token={},aesKey={},param={},rspText={}.",
						method,token,aesKey,json,rspText);
				throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
						"the http request of taotaogu status is not 200.");
			}
		}catch(Exception e){
			LOGGER.error("the http request of taotaogu exception method={},token={},aesKey={},param={},rspText={}.",
					method,token,aesKey,json,rspText,e);
			throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
					"the http request of taotaogu exception.");
		}
	}
	//订单接口，解析返回结果
	private Map<String,Object> resolveOrderResult(String rspText,String aesKey){
		String msg = "";
		Map<String,Object> result = null;
		try{
			int a = rspText.indexOf("msg=");
			String data1 = null; //结果字符串
			if(a != -1){
				int b = rspText.indexOf("&sign=");
				if(b != -1){
					msg = rspText.substring(a + 4, b);
					String sign = rspText.substring(b + 6);
					Map<String,Object> map = (Map<String,Object>) StringHelper.fromJsonString(msg, Map.class);
					String data = (String) map.get("data");
					data1 = new String (AESCoder.decrypt(Base64.decodeBase64(data), aesKey.getBytes()), "GBK");
				}else{
					data1 = rspText.substring(a + 4);
				}
				result = (Map<String,Object>) StringHelper.fromJsonString(data1, Map.class);
			}
		}catch(Exception e){
			LOGGER.error("resolveOrderResult failed rspText={},aesKey={}.",
					rspText,aesKey,e);
			throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
					"resolveOrderResult failed.");
		}
		return result;
	}
}
