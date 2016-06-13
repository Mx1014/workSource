package com.everhomes.payment;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.payment.util.ByteTools;
import com.everhomes.payment.util.CertCoder;
import com.everhomes.payment.util.ResponseEntiy;
import com.everhomes.payment.util.TAOTAOGUHttpUtil;
import com.everhomes.rest.payment.ApplyCardCommand;
import com.everhomes.rest.payment.CardInfoDTO;
import com.everhomes.rest.payment.CardTransactionDTO;
import com.everhomes.rest.payment.CardTransactionOfMonth;
import com.everhomes.rest.payment.CardTransactionTypeStatus;
import com.everhomes.rest.payment.ListCardTransactionsCommand;
import com.everhomes.rest.payment.PaymentCardStatus;
import com.everhomes.rest.payment.ResetCardPasswordCommand;
import com.everhomes.rest.payment.SetCardPasswordCommand;
import com.everhomes.user.EncryptionUtils;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.google.gson.Gson;

public class TAOTAOGUPaymentCardVendorHandler implements PaymentCardVendorHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(TAOTAOGUPaymentCardVendorHandler.class);
	@Autowired
    private PaymentCardProvider paymentCardProvider;
	
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
			if(cardMap != null){
				cardInfo.setCardId(card.getId());
				cardInfo.setCardNo((String)cardMap.get("CardId"));
				cardInfo.setCardType((String)cardMap.get("CardSubClass"));
				String effDate = (String)cardMap.get("EffDate");
				String expirDate = (String)cardMap.get("ExpirDate");
				cardInfo.setActivedTime(StrTotimestamp(effDate));
				cardInfo.setExpiredTime(StrTotimestamp(expirDate));
				cardInfo.setMobile((String)cardMap.get("MobileNo"));
				String cardStatus = (String)cardMap.get("CardStatus");
				if("10".equals(cardStatus))
					cardInfo.setStatus("正常（已激活）");
				else
					cardInfo.setStatus("无效");
			}
		
			if(!accountResponseEntiy.isSuccess())
				return null;
			Map accountMap = accountResponseEntiy.getData();
			if(accountMap != null){
				cardInfo.setCardId(card.getId());
				cardInfo.setBalance(new BigDecimal((String)accountMap.get("AvlbBal")));
			}
		} catch (Exception e) {
			e.printStackTrace();
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
				byte[] oldpsd = CertCoder.encryptByPublicKey("111111".getBytes(), "E:\\pin3.crt");
				changePasswordParam.put("OrigPassWord", ByteTools.BytesToHexStr(oldpsd));
				byte[] newpsd = CertCoder.encryptByPublicKey(cmd.getPassword().getBytes(), "E:\\pin3.crt");
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
					paymentCard.setCategoryId(cardIssuer.getId());
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
					paymentCard.setVendorCardData(cardIssuer.getVendorData());
					paymentCardProvider.createPaymentCard(paymentCard);
					cardInfoDTO = ConvertHelper.convert(paymentCard, CardInfoDTO.class);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
			oldpsd = CertCoder.encryptByPublicKey(cmd.getOldPassword().getBytes(), "E:\\pin3.crt");		
			changePasswordParam.put("OrigPassWord", ByteTools.BytesToHexStr(oldpsd));
			newpsd = CertCoder.encryptByPublicKey(cmd.getNewPassword().getBytes(), "E:\\pin3.crt");
			changePasswordParam.put("NewPassWord", ByteTools.BytesToHexStr(newpsd));
			changePasswordParam.put("Remark", "");
			changePasswordResponseEntiy = TAOTAOGUHttpUtil.post(brandCode,"0050",changePasswordParam);
		} catch (Exception e) {
			LOGGER.error("the change password request of taotaogu is failed.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"the change password request of taotaogu is failed.");
		}
		if(!changePasswordResponseEntiy.isSuccess()){
			LOGGER.error("change password of paymentCard is failed.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"change password of paymentCard is failed.");
		}
			
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
			LOGGER.error("reset password of paymentCard is failed.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"reset password of paymentCard is failed.");
		}
		
		Map<String, Object> changePasswordParam = new HashMap<String, Object>();
		changePasswordParam.put("BranchCode", brandCode);
		changePasswordParam.put("CardId", paymentCard.getCardNo());
		byte[] oldpsd = null;
		byte[] newpsd = null;
		
		
			oldpsd = CertCoder.encryptByPublicKey("111111".getBytes(), "E:\\pin3.crt");		
			changePasswordParam.put("OrigPassWord", ByteTools.BytesToHexStr(oldpsd));
			newpsd = CertCoder.encryptByPublicKey(cmd.getNewPassword().getBytes(), "E:\\pin3.crt");
			changePasswordParam.put("NewPassWord", ByteTools.BytesToHexStr(newpsd));
			changePasswordParam.put("Remark", "");
			changePasswordResponseEntiy = TAOTAOGUHttpUtil.post(brandCode,"0050",changePasswordParam);
		} catch (Exception e) {
			LOGGER.error("the change password request of taotaogu is failed.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"the change password request of taotaogu is failed.");
		}
		if(!changePasswordResponseEntiy.isSuccess()){
			LOGGER.error("change password of paymentCard is failed.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"change password of paymentCard is failed.");
		}
		
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
		param.put("EndCardId", "");
		param.put("MerchId", "");
		param.put("TerminalId", "");
		param.put("StartDate", "");
		param.put("Enddate", "");
		param.put("TransType", "");
		param.put("PageNumber", cmd.getPageAnchor());
		param.put("PageSize", cmd.getPageSize());
		
		ResponseEntiy responseEntiy = null;
		try {
			responseEntiy = TAOTAOGUHttpUtil.post(brandCode,"0040",param);
		} catch (Exception e) {
			LOGGER.error("the listCardTransactions request of taotaogu is failed.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"the listCardTransactions request of taotaogu is failed.");
		}
		if(!responseEntiy.isSuccess()){
			LOGGER.error("the listCardTransactions request of taotaogu is failed.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"the listCardTransactions request of taotaogu is failed.");
		}
		List<CardTransactionDTO> cardTransactionList = new ArrayList<>();
		Map map = responseEntiy.getData();
		if(map != null){
			
			String count = (String)map.get("Count");
			List<Map<String,Object>> list = (List<Map<String, Object>>) map.get("Row");
			for(Map<String,Object> m:list){
				CardTransactionDTO dto = new CardTransactionDTO();
				dto.setMerchant((String)m.get("MerchId"));
				dto.setAmount(new BigDecimal((String)m.get("ChdrRvaAmt")));
				String transactionType = (String)m.get("TransType");
				if("101".equals(transactionType))
					dto.setTransactionType(CardTransactionTypeStatus.CONSUME.getCode());
				if("203".equals(transactionType))
					dto.setTransactionType(CardTransactionTypeStatus.RECHARGE.getCode());
				dto.setStatus((String)m.get("ProcStatus"));
				dto.setTransactionTime(StrTotimestamp2((String)m.get("RecvTime")));
				cardTransactionList.add(dto);
			}
		}
		return null;
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
}
