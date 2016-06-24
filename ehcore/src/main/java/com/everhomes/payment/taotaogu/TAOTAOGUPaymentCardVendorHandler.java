package com.everhomes.payment.taotaogu;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.cert.Cert;
import com.everhomes.cert.CertProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.payment.PaymentCard;
import com.everhomes.payment.PaymentCardIssuer;
import com.everhomes.payment.PaymentCardProvider;
import com.everhomes.payment.PaymentCardRechargeOrder;
import com.everhomes.payment.PaymentCardVendorHandler;
import com.everhomes.payment.util.CachePool;
import com.everhomes.rest.payment.ApplyCardCommand;
import com.everhomes.rest.payment.CardInfoDTO;
import com.everhomes.rest.payment.CardRechargeStatus;
import com.everhomes.rest.payment.CardTransactionFromVendorDTO;
import com.everhomes.rest.payment.CardTransactionOfMonth;
import com.everhomes.rest.payment.CardTransactionTypeStatus;
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
import com.google.gson.Gson;

@Component(PaymentCardVendorHandler.PAYMENTCARD_VENDOR_PREFIX + "TAOTAOGU")
public class TAOTAOGUPaymentCardVendorHandler implements PaymentCardVendorHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(TAOTAOGUPaymentCardVendorHandler.class);
	
	@Autowired
    private PaymentCardProvider paymentCardProvider;
	@Autowired
    private ConfigurationProvider configProvider;
	@Autowired
	private CertProvider certProvider;
	@Autowired
	private LocaleStringService localeStringService;
	
	@SuppressWarnings("rawtypes")
	@Override
	public CardInfoDTO getCardInfoByVendor(PaymentCard card) {
		PaymentCardIssuer issuer = paymentCardProvider.findPaymentCardIssuerById(card.getIssuerId());
		String vendorData = issuer.getVendorData();
		Gson gson = new Gson();
		Map vendorDataMap = gson.fromJson(vendorData, Map.class);

		String brandCode = (String) vendorDataMap.get("BranchCode");
		CardInfoDTO cardInfo = new CardInfoDTO();

		try {
			Map<String, Object> getCardParam = new HashMap<String, Object>();
			getCardParam.put("BranchCode", brandCode);
			getCardParam.put("CardId", card.getCardNo());
		
			Map<String, Object> getAccountParam = new HashMap<String, Object>();
			getAccountParam.put("BranchCode", brandCode);
			getAccountParam.put("CardId", card.getCardNo());
			getAccountParam.put("AcctType", "00");
			getAccountParam.put("SubAcctType", "");
			ResponseEntiy cardResponseEntiy = post(vendorDataMap,"1020",getCardParam);
			ResponseEntiy accountResponseEntiy = post(vendorDataMap,"1010",getAccountParam);
		
			if(!cardResponseEntiy.isSuccess())
				return null;
			Map cardMap = cardResponseEntiy.getData();
			if(cardMap == null){
				LOGGER.error("the getCardInfo request of taotaogu is failed {}.",cardResponseEntiy.toString());
				throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
						localeStringService.getLocalizedString(String.valueOf(PaymentCardErrorCode.SCOPE), 
								String.valueOf(PaymentCardErrorCode.ERROR_SERVER_REQUEST),
								UserContext.current().getUser().getLocale(),"the getCardInfo request of taotaogu is failed."));
			}else{
				cardInfo.setCardId(card.getId());
				cardInfo.setCardNo((String)cardMap.get("CardId"));
				cardInfo.setCardType((String)cardMap.get("CardSubClass"));
				String effDate = (String)cardMap.get("EffDate");
				String expirDate = (String)cardMap.get("ExpirDate");
				cardInfo.setActivedTime(strTotimestamp(effDate));
				cardInfo.setExpiredTime(strTotimestamp(expirDate));
				
				String cardStatus = (String)cardMap.get("CardStatus");
				cardInfo.setStatus(cardStatus);
				cardInfo.setMobile(card.getMobile());
				cardInfo.setVendorCardData(card.getVendorCardData());
			}
		
			if(!accountResponseEntiy.isSuccess())
				return null;
			Map accountMap = accountResponseEntiy.getData();
			if(accountMap != null){
				cardInfo.setCardId(card.getId());
				List list = (List) accountMap.get("Row");
				for(int i=0;i<list.size();i++){
					Map map = (Map) list.get(i);
					if("fund".equals(((String)map.get("SubAcctType")).trim())){
						cardInfo.setBalance(new BigDecimal(map.get("AvlbBal").toString()));
						break;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("the getCardInfo request of taotaogu is failed {}.",e);
			throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
					localeStringService.getLocalizedString(String.valueOf(PaymentCardErrorCode.SCOPE), 
							String.valueOf(PaymentCardErrorCode.ERROR_SERVER_REQUEST),
							UserContext.current().getUser().getLocale(),"the getCardInfo request of taotaogu is failed."));
		}
		
		return cardInfo;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public CardInfoDTO applyCard(ApplyCardCommand cmd,PaymentCardIssuer cardIssuer) {
		CardInfoDTO cardInfoDTO = new CardInfoDTO();
		String vendorData = cardIssuer.getVendorData();
		Gson gson = new Gson();
		Map vendorDataMap = gson.fromJson(vendorData, Map.class);

		String brandCode = (String) vendorDataMap.get("BranchCode");
		String cardPatternid = (String) vendorDataMap.get("CardPatternid");
		Map<String, Object> applyCardParam = new HashMap<String, Object>();
		applyCardParam.put("BranchCode", brandCode);
		applyCardParam.put("CardPatternid", cardPatternid);
		applyCardParam.put("CardNum", "1");
		applyCardParam.put("DeliveryUser", "");
		applyCardParam.put("DeliveryContact", cmd.getMobile());
		
		try {
			ResponseEntiy applyCardResponseEntiy = post(vendorDataMap,"0000",applyCardParam);
		
			if(!applyCardResponseEntiy.isSuccess())
				return null;
			Map applyCardMap = applyCardResponseEntiy.getData();
			if(applyCardMap != null){	
				String cardId = (String)applyCardMap.get("StrCardId");
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
				ResponseEntiy saleCardResponseEntiy = post(vendorDataMap,"0010",param);
				if(!saleCardResponseEntiy.isSuccess())
					return null;
				
				Map<String, Object> changePasswordParam = new HashMap<String, Object>();
				changePasswordParam.put("BranchCode", brandCode);
				changePasswordParam.put("CardId", cardId);
				Cert cert = certProvider.findCertByName(configProvider.getValue(TAOTAOGUVendorConstant.PIN3_CRT, ""));
				InputStream in = new ByteArrayInputStream(cert.getData());
				
				String initPassword = (String) vendorDataMap.get(TAOTAOGUVendorConstant.INITIAL_PASSWORD);
				byte[] oldpsd = CertCoder.encryptByPublicKey(initPassword.getBytes(), in);
				changePasswordParam.put("OrigPassWord", ByteTools.BytesToHexStr(oldpsd));
				byte[] newpsd = CertCoder.encryptByPublicKey(cmd.getPassword().getBytes(), in);
				changePasswordParam.put("NewPassWord", ByteTools.BytesToHexStr(newpsd));
				changePasswordParam.put("Remark", "");
				ResponseEntiy changePasswordResponseEntiy = post(vendorDataMap,"0050",changePasswordParam);
				if(!changePasswordResponseEntiy.isSuccess())
					return null;
				
				Map<String, Object> getCardParam = new HashMap<String, Object>();
				getCardParam.put("BranchCode", brandCode);
				getCardParam.put("CardId", cardId);
				
				ResponseEntiy cardResponseEntiy = post(vendorDataMap,"1020",getCardParam);
				if(!cardResponseEntiy.isSuccess())
					return null;
				Map getCardMap = cardResponseEntiy.getData();
				if(getCardMap != null){
					String effDate = (String)getCardMap.get("EffDate");
					String expirDate = (String)getCardMap.get("ExpirDate");
				
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
					paymentCard.setVendorName(TAOTAOGUVendorConstant.TAOTAOGU);
					paymentCard.setVendorCardData(TAOTAOGUVendorConstant.CARD__STATUS_JSON);
					paymentCardProvider.createPaymentCard(paymentCard);
					cardInfoDTO = ConvertHelper.convert(paymentCard, CardInfoDTO.class);
				}
			}
		} catch (Exception e) {
			LOGGER.error("the apply card request of taotaogu is failed {}.",e);
			throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
					localeStringService.getLocalizedString(String.valueOf(PaymentCardErrorCode.SCOPE), 
							String.valueOf(PaymentCardErrorCode.ERROR_SERVER_REQUEST),
							UserContext.current().getUser().getLocale(),"the apply card request of taotaogu is failed."));
		}
		return cardInfoDTO;
	}
	
	public void setCardPassword(SetCardPasswordCommand cmd,PaymentCard paymentCard){
		PaymentCardIssuer issuer = paymentCardProvider.findPaymentCardIssuerById(paymentCard.getIssuerId());
		String vendorData = issuer.getVendorData();
		Gson gson = new Gson();
		Map vendorDataMap = gson.fromJson(vendorData, Map.class);

		String brandCode = (String) vendorDataMap.get("BranchCode");
		Map<String, Object> changePasswordParam = new HashMap<String, Object>();
		changePasswordParam.put("BranchCode", brandCode);
		changePasswordParam.put("CardId", paymentCard.getCardNo());
		byte[] oldpsd = null;
		byte[] newpsd = null;
		ResponseEntiy changePasswordResponseEntiy = null;
		try {
			Cert cert = certProvider.findCertByName(configProvider.getValue(TAOTAOGUVendorConstant.PIN3_CRT, ""));
			InputStream in = new ByteArrayInputStream(cert.getData());
			oldpsd = CertCoder.encryptByPublicKey(cmd.getOldPassword().getBytes(), in);		
			changePasswordParam.put("OrigPassWord", ByteTools.BytesToHexStr(oldpsd));
			newpsd = CertCoder.encryptByPublicKey(cmd.getNewPassword().getBytes(), in);
			changePasswordParam.put("NewPassWord", ByteTools.BytesToHexStr(newpsd));
			changePasswordParam.put("Remark", "");
			changePasswordResponseEntiy = post(vendorDataMap,"0050",changePasswordParam);
		} catch (Exception e) {
			LOGGER.error("the change password request of taotaogu is failed {}.",e);
			throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
					localeStringService.getLocalizedString(String.valueOf(PaymentCardErrorCode.SCOPE), 
							String.valueOf(PaymentCardErrorCode.ERROR_SERVER_REQUEST),
							UserContext.current().getUser().getLocale(),"the change password request of taotaogu is failed."));
		}
		if(!changePasswordResponseEntiy.isSuccess()){
			LOGGER.error("the change password request of taotaogu is failed {}.",changePasswordResponseEntiy.toString());
			throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
					localeStringService.getLocalizedString(String.valueOf(PaymentCardErrorCode.SCOPE), 
							String.valueOf(PaymentCardErrorCode.ERROR_SERVER_REQUEST),
							UserContext.current().getUser().getLocale(),"the change password request of taotaogu is failed."));
		}
		paymentCard.setPassword(EncryptionUtils.hashPassword(cmd.getNewPassword()));
		paymentCardProvider.updatePaymentCard(paymentCard);
			
	}

	@Override
	public void resetCardPassword(ResetCardPasswordCommand cmd,
			PaymentCard paymentCard) {
		PaymentCardIssuer issuer = paymentCardProvider.findPaymentCardIssuerById(paymentCard.getIssuerId());
		String vendorData = issuer.getVendorData();
		Gson gson = new Gson();
		Map vendorDataMap = gson.fromJson(vendorData, Map.class);

		String brandCode = (String) vendorDataMap.get("BranchCode");
		ResponseEntiy resetPasswordResponseEntiy = null;
		ResponseEntiy changePasswordResponseEntiy = null;
		try {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("BranchCode", brandCode);
		param.put("CardId", paymentCard.getCardNo());
		param.put("Remark", "");
		
		resetPasswordResponseEntiy = post(vendorDataMap,"0040",param);
		if(!resetPasswordResponseEntiy.isSuccess()){
			LOGGER.error("the reset password request of taotaogu is failed {}.",resetPasswordResponseEntiy.toString());
			throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
					localeStringService.getLocalizedString(String.valueOf(PaymentCardErrorCode.SCOPE), 
							String.valueOf(PaymentCardErrorCode.ERROR_SERVER_REQUEST),
							UserContext.current().getUser().getLocale(),"the reset password request of taotaogu is failed."));
		}
		
		Map<String, Object> changePasswordParam = new HashMap<String, Object>();
		changePasswordParam.put("BranchCode", brandCode);
		changePasswordParam.put("CardId", paymentCard.getCardNo());
		byte[] oldpsd = null;
		byte[] newpsd = null;
		Cert cert = certProvider.findCertByName(configProvider.getValue(TAOTAOGUVendorConstant.PIN3_CRT, ""));
		InputStream in = new ByteArrayInputStream(cert.getData());
			String initPassword = (String) vendorDataMap.get(TAOTAOGUVendorConstant.INITIAL_PASSWORD);
			oldpsd = CertCoder.encryptByPublicKey(initPassword.getBytes(), in);		
			changePasswordParam.put("OrigPassWord", ByteTools.BytesToHexStr(oldpsd));
			newpsd = CertCoder.encryptByPublicKey(cmd.getNewPassword().getBytes(), in);
			changePasswordParam.put("NewPassWord", ByteTools.BytesToHexStr(newpsd));
			changePasswordParam.put("Remark", "");
			changePasswordResponseEntiy = post(vendorDataMap,"0050",changePasswordParam);
		} catch (Exception e) {
			LOGGER.error("the change password request of taotaogu is failed {}.",e);
			throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
					localeStringService.getLocalizedString(String.valueOf(PaymentCardErrorCode.SCOPE), 
							String.valueOf(PaymentCardErrorCode.ERROR_SERVER_REQUEST),
							UserContext.current().getUser().getLocale(),"the change password request of taotaogu is failed."));
		}
		if(!changePasswordResponseEntiy.isSuccess()){
			LOGGER.error("the change password request of taotaogu is failed {}.",changePasswordResponseEntiy.toString());
			throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
					localeStringService.getLocalizedString(String.valueOf(PaymentCardErrorCode.SCOPE), 
							String.valueOf(PaymentCardErrorCode.ERROR_SERVER_REQUEST),
							UserContext.current().getUser().getLocale(),"the change password request of taotaogu is failed."));
		}
		paymentCard.setPassword(EncryptionUtils.hashPassword(cmd.getNewPassword()));
		paymentCardProvider.updatePaymentCard(paymentCard);
		
	}
	@Override
	public List<CardTransactionOfMonth> listCardTransactions(ListCardTransactionsCommand cmd,PaymentCard card){
		PaymentCardIssuer issuer = paymentCardProvider.findPaymentCardIssuerById(card.getIssuerId());
		String vendorData = issuer.getVendorData();
		Gson gson = new Gson();
		Map vendorDataMap = gson.fromJson(vendorData, Map.class);
		String brandCode = (String) vendorDataMap.get("BranchCode");
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("BranchCode", brandCode);
		param.put("CardId", card.getCardNo());
		param.put("EndCardId", "");
		param.put("MerchId", "");
		param.put("TerminalId", "");
		param.put("StartDate", "");
		param.put("Enddate", "");
		param.put("TransType", "203");
		param.put("PageNumber", cmd.getPageAnchor()==null?"1":String.valueOf(cmd.getPageAnchor()));
		param.put("PageSize", cmd.getPageSize()==null?"10":String.valueOf(cmd.getPageSize()));
		
		ResponseEntiy rechargeResponseEntiy = null;
		ResponseEntiy consumeResponseEntiy = null;
		try {
			rechargeResponseEntiy = post(vendorDataMap,"1030",param);
			param.put("TransType", "101");
			consumeResponseEntiy = post(vendorDataMap,"1030",param);
		} catch (Exception e) {
			LOGGER.error("the listCardTransactions request of taotaogu is failed {}.",e);
			throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
					localeStringService.getLocalizedString(String.valueOf(PaymentCardErrorCode.SCOPE), 
							String.valueOf(PaymentCardErrorCode.ERROR_SERVER_REQUEST),
							UserContext.current().getUser().getLocale(),"the listCardTransactions request of taotaogu is failed."));
		}
		if(!rechargeResponseEntiy.isSuccess() || !consumeResponseEntiy.isSuccess()){
			LOGGER.error("the listCardTransactions request of taotaogu is failed {}.",rechargeResponseEntiy.toString());
			throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
					localeStringService.getLocalizedString(String.valueOf(PaymentCardErrorCode.SCOPE), 
							String.valueOf(PaymentCardErrorCode.ERROR_SERVER_REQUEST),
							UserContext.current().getUser().getLocale(),"the listCardTransactions request of taotaogu is failed."));
		}
		Map rechargeMap = rechargeResponseEntiy.getData();
		Map consumeMap = consumeResponseEntiy.getData();
		List<CardTransactionOfMonth> resultList = new ArrayList<>();
		if(rechargeMap != null && consumeMap != null){
			
			String count = rechargeMap.get("Count").toString();
			List<Map<String,Object>> rechargeList = (List<Map<String, Object>>) rechargeMap.get("Row");
			List<Map<String,Object>> consumeList = (List<Map<String, Object>>) consumeMap.get("Row");
			rechargeList.addAll(consumeList);
			outer:
			for(Map<String,Object> m:rechargeList){
				
				BigDecimal consumeAmount = new BigDecimal(0);
				BigDecimal rechargeAmount = new BigDecimal(0);
				
				CardTransactionFromVendorDTO dto = new CardTransactionFromVendorDTO();
				dto.setVendorResult(TAOTAOGUVendorConstant.CARD_TRANSACTION_STATUS_JSON);
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
		return resultList;
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
		Gson gson = new Gson();
		Map vendorDataMap = gson.fromJson(vendorData, Map.class);
		String result = null;
		getToken(vendorDataMap);
		CachePool cachePool = CachePool.getInstance();
		String aesKey = cachePool.getStringValue(TAOTAOGUVendorConstant.TAOTAOGU_AESKEY);
		String token = cachePool.getStringValue(TAOTAOGUVendorConstant.TAOTAOGU_TOKEN);
		
		JSONObject json = new JSONObject();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// 可以方便地修改日期格式
		String timeStr = dateFormat.format(now);
		String chnl_type = (String) vendorDataMap.get(TAOTAOGUVendorConstant.CHNL_TYPE);
		String chnl_id = (String) vendorDataMap.get(TAOTAOGUVendorConstant.CHNL_ID);
		json.put("chnl_type", chnl_type);
		json.put("chnl_id", chnl_id);
		json.put("chnl_sn", System.currentTimeMillis());
		json.put("card_id", paymentCard.getCardNo());
		json.put("reserved", "");
		json.put("request_time", timeStr);
		
		Map codeMap = post("/iips2/order/tokenrequest",token, aesKey, json);
		if(codeMap != null){
			String returnCode = (String) codeMap.get("return_code");
			if("00".equals(returnCode)){
				result = (String) codeMap.get("token");
			}else{
				LOGGER.error("the getCardPaidQrCode request of taotaogu is failed codeMap={},json={},token={},aesKey={}.",codeMap,json,token,aesKey);
				throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_GET_CARD_CODE,
						localeStringService.getLocalizedString(String.valueOf(PaymentCardErrorCode.SCOPE), 
								String.valueOf(PaymentCardErrorCode.ERROR_GET_CARD_CODE),
								UserContext.current().getUser().getLocale(),"the getCardPaidQrCode request of taotaogu is failed ."));
			}
		}
		return result;
	}
	
	private void getToken(Map vendorDataMap){
		CachePool cachePool = CachePool.getInstance();
		String aesKey = cachePool.getStringValue(TAOTAOGUVendorConstant.TAOTAOGU_AESKEY);
		String token = cachePool.getStringValue(TAOTAOGUVendorConstant.TAOTAOGU_TOKEN);
		if(aesKey == null || token == null){
			//丢到缓存中
				Map map = null;
				map = orderLogin(vendorDataMap);
				if(map != null){
					cachePool.putCacheItem(TAOTAOGUVendorConstant.TAOTAOGU_AESKEY, (String) map.get("aes_key"), 24*60*60*1000);
					cachePool.putCacheItem(TAOTAOGUVendorConstant.TAOTAOGU_TOKEN, (String) map.get("token"), 24*60*60*1000);
				}
		}
	}
	
	@Override
	public void rechargeCard(PaymentCardRechargeOrder order, PaymentCard card) {
		PaymentCardIssuer issuer = paymentCardProvider.findPaymentCardIssuerById(card.getIssuerId());
		String vendorData = issuer.getVendorData();
		Gson gson = new Gson();
		Map vendorDataMap = gson.fromJson(vendorData, Map.class);
		String brandCode = (String) vendorDataMap.get("BranchCode");
		Map<String, Object> param = new HashMap<String, Object>();

		param.put("BranchCode", brandCode);
		param.put("CardId", card.getCardNo());
		param.put("EndCardId", "");
		param.put("RechargeNum", "");
		param.put("RechargeType", "0");
		param.put("RechargeAcctType", "fund");
		param.put("Amt", order.getAmount().toString());
		param.put("RealAmt", order.getAmount().toString());
		
		param.put("PayAccNo", "");
		param.put("PayAccName", "");
		param.put("ActivityId", "");
		param.put("DiscountAmt", "");
		param.put("GiveAmt", "");
		param.put("SaleUser", "");
		
		ResponseEntiy responseEntiy = null;
		try {
			responseEntiy = post(vendorDataMap,"0020",param);
			if(responseEntiy.isSuccess()){
				order.setRechargeStatus(CardRechargeStatus.RECHARGED.getCode());
				order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
				paymentCardProvider.updatePaymentCardRechargeOrder(order);
				return;
			}else if("A0".equals(responseEntiy.getRespCode())){
				boolean flag = true;
				int i = 1;
					
				Map<String, Object> queryParam = new HashMap<String, Object>();
				queryParam.put("QueryType", "0020");
				queryParam.put("OrigMsgId", responseEntiy.getMsgID());
				while(flag&&i<=10){
					i++;
					ResponseEntiy selectResponseEntiy = post(vendorDataMap,"9990",param);
					if(selectResponseEntiy.isSuccess()){
						
						Map map = selectResponseEntiy.getData();
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
			LOGGER.error("the recharge request of taotaogu is failed {}.",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"the recharge request of taotaogu is failed.");
		}
		
	}
	
	private Map orderLogin(Map vendorDataMap){
		Map result = null;
		String rspText = null;
		JSONObject json = new JSONObject();
		try{
			CloseableHttpClient httpClient = HttpClients.createDefault();
			Gson gson = new Gson();
			String url = configProvider.getValue("taotaogu.order.url", "");
			HttpPost request = new HttpPost(url+"/iips2/order/login");
			
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// 可以方便地修改日期格式
			String timeStr = dateFormat.format(now);
			
			String chnl_type = (String) vendorDataMap.get(TAOTAOGUVendorConstant.CHNL_TYPE);
			String chnl_id = (String) vendorDataMap.get(TAOTAOGUVendorConstant.CHNL_ID);
			String merch_id = (String) vendorDataMap.get(TAOTAOGUVendorConstant.MERCH_ID);
			String termnl_id = (String) vendorDataMap.get(TAOTAOGUVendorConstant.TERMNL_ID);
			json.put("chnl_type", chnl_type);
			json.put("chnl_id", chnl_id);
			json.put("chnl_sn", System.currentTimeMillis());
			json.put("merch_id", merch_id);
			json.put("termnl_id", termnl_id);
		
			CertProvider certProvider =  PlatformContext.getComponent("certProviderImpl");
			Cert serverCer = certProvider.findCertByName(configProvider.getValue(TAOTAOGUVendorConstant.SERVER_CER, ""));
			InputStream serverCerIn = new ByteArrayInputStream(serverCer.getData());
			Cert clientPfx = certProvider.findCertByName(configProvider.getValue(TAOTAOGUVendorConstant.CLIENT_PFX, ""));
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
			result = gson.fromJson(r1, Map.class);
		}catch(Exception e){
			LOGGER.error("the orderLogin request of taotaogu is failed rspText={}, e={},json={}.",rspText,e.toString(),json);
			throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
					localeStringService.getLocalizedString(String.valueOf(PaymentCardErrorCode.SCOPE), 
							String.valueOf(PaymentCardErrorCode.ERROR_SERVER_REQUEST),
							UserContext.current().getUser().getLocale(),"the orderLogin request of taotaogu is failed."));
		}
		return result;
	}

	private Map post(String method,String token,String aesKey,JSONObject json){
		CloseableHttpClient httpClient = HttpClients.createDefault();
		Gson gson = new Gson();
		Map result = null;
		String rspText = null;
		try{
			String url = configProvider.getValue("taotaogu.order.url", "");
			HttpPost request = new HttpPost(url+method);
			
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				
			pairs.add(new BasicNameValuePair("token", token));
			String msg = Base64.encodeBase64String(AESCoder.encrypt(json.toString().getBytes("GBK"), aesKey.getBytes()));
			pairs.add(new BasicNameValuePair("msg", msg));
			pairs.add(new BasicNameValuePair("sign", SHA1.EnCodeSHA1(msg + aesKey + token)));
			
			request.setEntity(new UrlEncodedFormEntity(pairs, "GBK"));
			HttpResponse rsp = httpClient.execute(request);
			@SuppressWarnings("unused")
			StatusLine status = rsp.getStatusLine();
			rspText = EntityUtils.toString(rsp.getEntity(), "GBK");
			
			int a = rspText.indexOf("msg=");
			int b = rspText.indexOf("&sign=");
			
			String data1 = null; //结果字符串
			if(b != -1){
				msg = rspText.substring(a + 4, b);
				String sign = rspText.substring(b + 6);
				Map map = gson.fromJson(msg, Map.class);
				String data = (String) map.get("data");
				data1 = new String (AESCoder.decrypt(Base64.decodeBase64(data), aesKey.getBytes()), "GBK");
			}else{
				data1 = rspText.substring(a + 4);
			}
			result = gson.fromJson(data1, Map.class);
		}catch(Exception e){
			LOGGER.error("the orderLogin request of taotaogu is failed rspText={}, e={}.",rspText,e.toString());
			throw RuntimeErrorException.errorWith(PaymentCardErrorCode.SCOPE, PaymentCardErrorCode.ERROR_SERVER_REQUEST,
					localeStringService.getLocalizedString(String.valueOf(PaymentCardErrorCode.SCOPE), 
							String.valueOf(PaymentCardErrorCode.ERROR_SERVER_REQUEST),
							UserContext.current().getUser().getLocale(),"the orderLogin request of taotaogu is failed."));
		}
		return result;
	}
	
	public ResponseEntiy post(Map vendorDataMap,String msgType,Map<String, Object> param) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String url = configProvider.getValue("taotaogu.card.url", "");
		HttpPost request = new HttpPost(url);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		String msg = getJson(vendorDataMap,msgType,param);
		pairs.add(new BasicNameValuePair("msg", msg));
		request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
		HttpResponse rsp = httpClient.execute(request);
		StatusLine status = rsp.getStatusLine();
		if(status.getStatusCode() == 200){
			String rspText = EntityUtils.toString(rsp.getEntity(), "UTF-8");
			Gson gson = new Gson();
			ResponseEntiy resp = gson.fromJson(rspText, ResponseEntiy.class);
			
			return resp;	
		}
		return null;
	}
	
	private String getJson(Map vendorDataMap,String msgType,Map<String, Object> param) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Gson gson = new Gson();
		
		Map<String, Object> requestParam = new HashMap<String, Object>();
		String appName = (String) vendorDataMap.get(TAOTAOGUVendorConstant.APP_NAME);
		String version = (String) vendorDataMap.get(TAOTAOGUVendorConstant.VERSION);
		String dstId = (String) vendorDataMap.get(TAOTAOGUVendorConstant.DSTID);
		String brandCode = (String) vendorDataMap.get(TAOTAOGUVendorConstant.BRANCH_CODE);
		requestParam.put("AppName", appName);
		requestParam.put("Version",version);
		requestParam.put("ClientDt",sdf.format(new Date()));
		requestParam.put("SrcId",brandCode);
		requestParam.put("DstId",dstId);
		requestParam.put("MsgType",msgType);
		requestParam.put("MsgID",brandCode + StringUtils.leftPad(String.valueOf(System.currentTimeMillis()), 24, "0"));
		requestParam.put("Sign", "");

		requestParam.put("Param",param);
		byte[] data = gson.toJson(requestParam).getBytes();
		
		CertProvider certProvider = PlatformContext.getComponent("certProviderImpl");
		
		Cert cert = certProvider.findCertByName(configProvider.getValue(TAOTAOGUVendorConstant.KEY_STORE, ""));
		InputStream in = new ByteArrayInputStream(cert.getData());
		
		String pass = cert.getCertPass();
		String[] passArr = pass.split(",");
		
		byte[] sign = CertCoder.sign(data, in,passArr[0], passArr[1], passArr[2]);
		requestParam.put("Sign",ByteTools.BytesToHexStr(sign));
		
		return gson.toJson(requestParam);
	}
}
