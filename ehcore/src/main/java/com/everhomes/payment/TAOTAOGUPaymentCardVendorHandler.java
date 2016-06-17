package com.everhomes.payment;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.cert.Cert;
import com.everhomes.cert.CertProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.payment.taotaogu.ByteTools;
import com.everhomes.payment.taotaogu.CertCoder;
import com.everhomes.payment.taotaogu.ResponseEntiy;
import com.everhomes.payment.taotaogu.TAOTAOGUHttpUtil;
import com.everhomes.payment.taotaogu.TAOTAOGUOrderHttpUtil;
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
		String vendorData = card.getVendorCardData();
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
			ResponseEntiy cardResponseEntiy = TAOTAOGUHttpUtil.post(brandCode,"1020",getCardParam);
			ResponseEntiy accountResponseEntiy = TAOTAOGUHttpUtil.post(brandCode,"1010",getAccountParam);
		
			if(!cardResponseEntiy.isSuccess())
				return null;
			Map cardMap = cardResponseEntiy.getData();
			if(cardMap == null){
				LOGGER.error("the getCardInfo request of taotaogu is failed {}.",cardResponseEntiy.toString());
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"the getCardInfo request of taotaogu is failed.");
			}else{
				cardInfo.setCardId(card.getId());
				cardInfo.setCardNo((String)cardMap.get("CardId"));
				cardInfo.setCardType((String)cardMap.get("CardSubClass"));
				String effDate = (String)cardMap.get("EffDate");
				String expirDate = (String)cardMap.get("ExpirDate");
				cardInfo.setActivedTime(StrTotimestamp(effDate));
				cardInfo.setExpiredTime(StrTotimestamp(expirDate));
				cardInfo.setMobile((String)cardMap.get("MobileNo"));
				String cardStatus = (String)cardMap.get("CardStatus");
				cardInfo.setStatus(cardStatus);
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
						cardInfo.setBalance(new BigDecimal((Double)map.get("AvlbBal")).setScale(2,RoundingMode.DOWN));
						break;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("the cardInfo request of taotaogu is failed {}.",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"the cardInfo request of taotaogu is failed.");
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
		Map<String, Object> applyCardParam = new HashMap<String, Object>();
		applyCardParam.put("BranchCode", brandCode);
		applyCardParam.put("CardPatternid", "887093");
		applyCardParam.put("CardNum", "1");
		applyCardParam.put("DeliveryUser", "");
		applyCardParam.put("DeliveryContact", cmd.getMobile());
		
		try {
			ResponseEntiy applyCardResponseEntiy = TAOTAOGUHttpUtil.post(brandCode,"0000",applyCardParam);
		
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
				ResponseEntiy saleCardResponseEntiy = TAOTAOGUHttpUtil.post(brandCode,"0010",param);
				if(!saleCardResponseEntiy.isSuccess())
					return null;
				
				Map<String, Object> changePasswordParam = new HashMap<String, Object>();
				changePasswordParam.put("BranchCode", brandCode);
				changePasswordParam.put("CardId", cardId);
				Cert cert = certProvider.findCertByName(configProvider.getValue(VendorConstant.PIN3_CRT, ""));
				InputStream in = new ByteArrayInputStream(cert.getData());
				
				byte[] oldpsd = CertCoder.encryptByPublicKey(VendorConstant.INITIAL_PASSWORD.getBytes(), in);
				changePasswordParam.put("OrigPassWord", ByteTools.BytesToHexStr(oldpsd));
				byte[] newpsd = CertCoder.encryptByPublicKey(cmd.getPassword().getBytes(), in);
				changePasswordParam.put("NewPassWord", ByteTools.BytesToHexStr(newpsd));
				changePasswordParam.put("Remark", "");
				ResponseEntiy changePasswordResponseEntiy = TAOTAOGUHttpUtil.post(brandCode,"0050",changePasswordParam);
				if(!changePasswordResponseEntiy.isSuccess())
					return null;
				
				Map<String, Object> getCardParam = new HashMap<String, Object>();
				getCardParam.put("BranchCode", brandCode);
				getCardParam.put("CardId", cardId);
				
				ResponseEntiy cardResponseEntiy = TAOTAOGUHttpUtil.post(brandCode,"1020",getCardParam);
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
					paymentCard.setActivateTime(StrTotimestamp(effDate));
					paymentCard.setExpiredTime(StrTotimestamp(expirDate));
					paymentCard.setCreatorUid(user.getId());
					paymentCard.setStatus(PaymentCardStatus.ACTIVE.getCode());
					paymentCard.setVendorName(VendorConstant.TAOTAOGU);
					paymentCard.setVendorCardData(mergeJson(cardIssuer.getVendorData(), VendorConstant.CARD__STATUS_JSON));
					paymentCardProvider.createPaymentCard(paymentCard);
					cardInfoDTO = ConvertHelper.convert(paymentCard, CardInfoDTO.class);
				}
			}
		} catch (Exception e) {
			LOGGER.error("the apply card request of taotaogu is failed {}.",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"the apply card request of taotaogu is failed.");
		}
		return cardInfoDTO;
	}
	
	public void setCardPassword(SetCardPasswordCommand cmd,PaymentCard paymentCard){
		String vendorData = paymentCard.getVendorCardData();
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
			Cert cert = certProvider.findCertByName(configProvider.getValue(VendorConstant.PIN3_CRT, ""));
			InputStream in = new ByteArrayInputStream(cert.getData());
			oldpsd = CertCoder.encryptByPublicKey(cmd.getOldPassword().getBytes(), in);		
			changePasswordParam.put("OrigPassWord", ByteTools.BytesToHexStr(oldpsd));
			newpsd = CertCoder.encryptByPublicKey(cmd.getNewPassword().getBytes(), in);
			changePasswordParam.put("NewPassWord", ByteTools.BytesToHexStr(newpsd));
			changePasswordParam.put("Remark", "");
			changePasswordResponseEntiy = TAOTAOGUHttpUtil.post(brandCode,"0050",changePasswordParam);
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
		String vendorData = paymentCard.getVendorCardData();
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
		
		resetPasswordResponseEntiy = TAOTAOGUHttpUtil.post(brandCode,"0040",param);
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
		Cert cert = certProvider.findCertByName(configProvider.getValue(VendorConstant.PIN3_CRT, ""));
		InputStream in = new ByteArrayInputStream(cert.getData());
			oldpsd = CertCoder.encryptByPublicKey(VendorConstant.INITIAL_PASSWORD.getBytes(), in);		
			changePasswordParam.put("OrigPassWord", ByteTools.BytesToHexStr(oldpsd));
			newpsd = CertCoder.encryptByPublicKey(cmd.getNewPassword().getBytes(), in);
			changePasswordParam.put("NewPassWord", ByteTools.BytesToHexStr(newpsd));
			changePasswordParam.put("Remark", "");
			changePasswordResponseEntiy = TAOTAOGUHttpUtil.post(brandCode,"0050",changePasswordParam);
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
		String vendorData = card.getVendorCardData();
		Gson gson = new Gson();
		Map vendorDataMap = gson.fromJson(vendorData, Map.class);
		String brandCode = (String) vendorDataMap.get("BranchCode");
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("BranchCode", brandCode);
		//param.put("CardId", "5882572900500000182");
		//param.put("CardId", "5882572900500005884");
		param.put("CardId", "");
		param.put("EndCardId", "");
		param.put("MerchId", "");
		param.put("TerminalId", "");
		param.put("StartDate", "");
		param.put("Enddate", "");
		param.put("TransType", "");
		param.put("PageNumber", cmd.getPageAnchor()==null?"":String.valueOf(cmd.getPageAnchor()));
		param.put("PageSize", cmd.getPageSize()==null?"":String.valueOf(cmd.getPageSize()));
		
		ResponseEntiy responseEntiy = null;
		try {
			responseEntiy = TAOTAOGUHttpUtil.post(brandCode,"1030",param);
		} catch (Exception e) {
			LOGGER.error("the listCardTransactions request of taotaogu is failed {}.",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"the listCardTransactions request of taotaogu is failed.");
		}
		if(!responseEntiy.isSuccess()){
			LOGGER.error("the listCardTransactions request of taotaogu is failed {}.",responseEntiy.toString());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"the listCardTransactions request of taotaogu is failed.");
		}
		Map map = responseEntiy.getData();
		List<CardTransactionOfMonth> resultList = new ArrayList<>();
		if(map != null){
			
			Double count = (Double)map.get("Count");
			List<Map<String,Object>> list = (List<Map<String, Object>>) map.get("Row");
			outer:
			for(Map<String,Object> m:list){
				
				BigDecimal consumeAmount = new BigDecimal(0);
				BigDecimal rechargeAmount = new BigDecimal(0);
				
				CardTransactionFromVendorDTO dto = new CardTransactionFromVendorDTO();
				dto.setMerchant((String)m.get("MerchId"));
				BigDecimal amount = new BigDecimal((Double)m.get("ChdrRvaAmt"));
				dto.setAmount(amount);
				String transactionType = (String)m.get("TransType");
				if("101".equals(transactionType)){
					dto.setTransactionType(CardTransactionTypeStatus.CONSUME.getCode());
					consumeAmount = consumeAmount.add(amount);
				}
				if("203".equals(transactionType)){
					dto.setTransactionType(CardTransactionTypeStatus.RECHARGE.getCode());
					rechargeAmount = rechargeAmount.add(amount);
				}
				dto.setStatus((String)m.get("ProcStatus"));
				String recvTime = (String)m.get("RecvTime");
				dto.setTransactionTime(StrTotimestamp2(recvTime));
				
				for(CardTransactionOfMonth ctm:resultList){
					if(ctm.getDate().getTime() == StrTotimestamp(recvTime).getTime()){
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
				cardTransactionOfMonth.setDate(StrTotimestamp(recvTime));
				cardTransactionOfMonth.setConsumeAmount(consumeAmount);
				cardTransactionOfMonth.setRechargeAmount(rechargeAmount);
				resultList.add(cardTransactionOfMonth);
			}
		}
		return resultList;
	}
	
	private Timestamp StrTotimestamp(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
		return new Timestamp(d.getTime());
	}
	   
	private Timestamp StrTotimestamp2(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
		return new Timestamp(d.getTime());
	}

	@Override
	public String getCardPaidQrCodeByVendor(PaymentCard paymentCard) {
		String result = null;
		getToken();
		CachePool cachePool = CachePool.getInstance();
		String aesKey = cachePool.getStringValue(VendorConstant.TAOTAOGU_AESKEY);
		String token = cachePool.getStringValue(VendorConstant.TAOTAOGU_TOKEN);
		
		JSONObject json = new JSONObject();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// 可以方便地修改日期格式
		String timeStr = dateFormat.format(now);
		//timeStr = "20160223152525";
		json.put("chnl_type", "WEB");
		json.put("chnl_id", "12345679");
		json.put("chnl_sn", System.currentTimeMillis());
		json.put("card_id", paymentCard.getCardNo());
		json.put("reserved", "");
		json.put("request_time", timeStr);	
		
		try {
			boolean getTokenFlag = true;
			while(getTokenFlag){
				Map codeMap = TAOTAOGUOrderHttpUtil.post("/iips2/order/tokenrequest",token, aesKey, json);
				if(codeMap != null){
					String returnCode = (String) codeMap.get("return_code");
					if("00".equals(returnCode)){
						getTokenFlag = false;
						result = (String) codeMap.get("token");
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("the cardPaidQrCode request of taotaogu is failed {}.",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"the cardPaidQrCode request of taotaogu is failed.");
		}
		return result;
	}
	
	private void getToken(){
		CachePool cachePool = CachePool.getInstance();
		String aesKey = cachePool.getStringValue(VendorConstant.TAOTAOGU_AESKEY);
		String token = cachePool.getStringValue(VendorConstant.TAOTAOGU_TOKEN);
		if(aesKey == null || token == null){
			boolean flag = true;
			//丢到缓存中
			while(flag){
				Map map = null;
				try {
					map = TAOTAOGUOrderHttpUtil.orderLogin();
				} catch (Exception e) {
					LOGGER.error("the orderLogin request of taotaogu is failed {}.",e);
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
							"the orderLogin request of taotaogu is failed.");
				}
				if(map != null){
					flag = false;
					cachePool.putCacheItem(VendorConstant.TAOTAOGU_AESKEY, (String) map.get("aes_key"), 24*60*60*1000);
					cachePool.putCacheItem(VendorConstant.TAOTAOGU_TOKEN, (String) map.get("token"), 24*60*60*1000);
				}
			}
		}
	}

	@Override
	public void rechargeCard(PaymentCardRechargeOrder order, PaymentCard card) {
		Gson gson = new Gson();
		Map vendorDataMap = gson.fromJson(card.getVendorCardData(), Map.class);
		String brandCode = (String) vendorDataMap.get("BranchCode");
		Map<String, Object> param = new HashMap<String, Object>();

		param.put("BranchCode", brandCode);
		param.put("CardId", card.getId());
		param.put("EndCardId", "");
		param.put("RechargeNum", "");
		param.put("RechargeType", "0");
		param.put("RechargeAcctType", "fund");
		param.put("Amt", order.getAmount().doubleValue());
		param.put("RealAmt", order.getAmount().doubleValue());
		
		param.put("PayAccNo", "");
		param.put("PayAccName", "");
		param.put("ActivityId", "");
		param.put("DiscountAmt", "");
		param.put("GiveAmt", "");
		param.put("SaleUser", "");
		
		ResponseEntiy responseEntiy = null;
		try {
			responseEntiy = TAOTAOGUHttpUtil.post(brandCode,"0020",param);
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
					ResponseEntiy selectResponseEntiy = TAOTAOGUHttpUtil.post(brandCode,"9990",param);
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
	
	 private String mergeJson(String json1,String json2){
	    	Gson gson = new Gson();
			Map map = gson.fromJson(json1, Map.class);
			Map map1 = gson.fromJson(json2, Map.class);
			map.putAll(map1);
			return gson.toJson(map);
	    }
}
