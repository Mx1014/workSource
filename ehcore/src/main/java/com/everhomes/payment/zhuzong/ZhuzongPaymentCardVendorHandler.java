package com.everhomes.payment.zhuzong;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.payment.*;
import com.everhomes.rest.payment.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component(PaymentCardVendorHandler.PAYMENTCARD_VENDOR_PREFIX + "ZHUZONG")
public class ZhuzongPaymentCardVendorHandler implements PaymentCardVendorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZhuzongPaymentCardVendorHandler.class);

    @Autowired
    ConfigurationProvider configProvider;
    @Autowired
    private PaymentCardProvider paymentCardProvider;

    private static final String APPLT_CARD_TYPE = "0";
    private static final String ACCOUNT_QUERY_TYPE = "1";
    private static final String RECHARGE_TYPE = "3";
    private static final String FREEZE_ACCOUNT_TYPE = "4";
    private static final String CONSUME_TRANSACTION_TYPE = "6";
    private static final String RECHARGE_TRANSACTION_TYPE = "5";
    private static final String UNBUNDLE_CARD_TYPE = "7";

    public static final String vendorName = "ZHUZONG";
    private static CloseableHttpClient httpClient = HttpClients.createDefault();
    @Override
    public List<CardInfoDTO> getCardInfoByVendor(ListCardInfoCommand cmd) {
        User user = UserContext.current().getUser();
        List<PaymentCard> cardList = paymentCardProvider.listPaymentCard(cmd.getOwnerId(),cmd.getOwnerType(),user.getId());
        List<CardInfoDTO> result = new ArrayList<CardInfoDTO>();
        if (cardList == null || cardList.size() == 0)
            return result;
        PaymentCard card = cardList.get(0);
        result.add(getCardInfo(card));

        return result;
    }

    @Override
    public CardInfoDTO getCardInfo(PaymentCard paymentCard) {
        if (paymentCard == null)
            return null;
        JSONObject jo = new JSONObject();
        jo.put("FunctionID", ACCOUNT_QUERY_TYPE);
        jo.put("UserID", paymentCard.getCardNo());
        String response = postToZhuzong(jo.toJSONString());
        ZhuzongUserCardInfo cardInfo = (ZhuzongUserCardInfo) StringHelper.fromJsonString(response, ZhuzongUserCardInfo.class);
        if ("4".equals(cardInfo.getResultID())) {//销户
            CardInfoDTO cardInfoDTO = new CardInfoDTO();
            cardInfoDTO.setStatus("2");
            cardInfoDTO.setCardId(paymentCard.getId());
            return cardInfoDTO;
        }
        if (!"0".equals(cardInfo.getResultID())){//返回报错
            LOGGER.error("paymentCard getCardInfoByVendor error, param={} response = {}", jo,response);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "paymentCard getCardInfoByVendor error");
        }

        CardInfoDTO cardInfoDTO = new CardInfoDTO();
        cardInfoDTO.setBalance(new BigDecimal(cardInfo.getBalance()));
        cardInfoDTO.setMobile(paymentCard.getMobile());
        cardInfoDTO.setStatus(cardInfo.getStateID());
        cardInfoDTO.setCardNo(cardInfo.getCardID());
        ZhuzongVendorDate vendorDate = new ZhuzongVendorDate();
        vendorDate.setDeptName(cardInfo.getDeptName());
        vendorDate.setUserId(cardInfo.getUserID());
        vendorDate.setUserName(cardInfo.getUserName());
        cardInfoDTO.setVendorCardData(StringHelper.toJsonString(vendorDate));
        cardInfoDTO.setCardId(paymentCard.getId());
        return cardInfoDTO;
    }


    @Override
    public CardInfoDTO applyCard(ApplyCardCommand cmd, PaymentCardIssuer cardIssuer) {
        JSONObject jo = new JSONObject();
        jo.put("FunctionID", APPLT_CARD_TYPE);
        jo.put("UserName",cmd.getName());
        jo.put("UserID",cmd.getUserId());
        String response = postToZhuzong(jo.toJSONString());
        ZhuzongApplyCard applyCard = (ZhuzongApplyCard)StringHelper.fromJsonString(response, ZhuzongApplyCard.class);
        if ("1".equals(applyCard.getResultID())) //失败
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 1001,
                    "绑定账户失败");
        if ("2".equals(applyCard.getResultID())) //已绑定
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 1001,
                    "该账户已被绑定");
        if ("3".equals(applyCard.getResultID())) //已销户
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 1001,
                    "该账户已被销户");
        if ("4".equals(applyCard.getResultID())) //不存在
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,1001,
                    "该账户不存在");
        if (!"0".equals(applyCard.getResultID()))
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 1001,
                    "查询第三方接口失败");
        User user = UserContext.current().getUser();
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setOwnerId(cmd.getOwnerId());
        paymentCard.setOwnerType(cmd.getOwnerType());
        paymentCard.setNamespaceId(user.getNamespaceId());
        paymentCard.setIssuerId(cardIssuer.getId());
        paymentCard.setUserName(cmd.getName());
        paymentCard.setMobile(cmd.getMobile());
        paymentCard.setCardNo(applyCard.getUserID());
        paymentCard.setUserId(user.getId());
        paymentCard.setCreateTime(new Timestamp(System.currentTimeMillis()));
        paymentCard.setUpdateTime(paymentCard.getCreateTime());
        paymentCard.setCreatorUid(user.getId());
        paymentCard.setStatus(PaymentCardStatus.ACTIVE.getCode());
        paymentCard.setVendorName(vendorName);
        ZhuzongVendorDate vendorDate = new ZhuzongVendorDate();
        vendorDate.setUserId(cmd.getUserId());
        paymentCard.setVendorCardData(StringHelper.toJsonString(vendorDate));
        paymentCardProvider.createPaymentCard(paymentCard);
        CardInfoDTO cardInfoDTO = ConvertHelper.convert(paymentCard, CardInfoDTO.class);
        return cardInfoDTO;
    }

    @Override
    public List<CardTransactionOfMonth> listCardTransactions(ListCardTransactionsCommand cmd, PaymentCard card) {
        if ("1".equals(cmd.getTransactionType()))
            return listRechargeTransactions(cmd,card);
        else
            return listConsumeTransactions(cmd,card);
    }

    private List<CardTransactionOfMonth> listConsumeTransactions(ListCardTransactionsCommand cmd, PaymentCard card){
        JSONObject jo = new JSONObject();
        jo.put("FunctionID", CONSUME_TRANSACTION_TYPE);
        jo.put("UserID",card.getCardNo());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (cmd.getStartTime() != null) {
            jo.put("StartDate", format.format(new Date(cmd.getStartTime())));
            jo.put("EndDate", format.format(new Date(cmd.getEndTime())));
            jo.put("DayNum", "");
        }else{
            jo.put("DayNum", "0");
            jo.put("StartDate", "");
            jo.put("EndDate", "");
        }
        jo.put("PageIndex",cmd.getPageAnchor() != null ? cmd.getPageAnchor().toString():"1");
        jo.put("PageSize",cmd.getPageSize() != null ? cmd.getPageSize().toString():"10");
        String response = postToZhuzong(jo.toJSONString());
        ZhuzongConsumeResponse res = (ZhuzongConsumeResponse)StringHelper.fromJsonString(response, ZhuzongConsumeResponse.class);
        List<CardTransactionOfMonth> transactions = new ArrayList<>();
        CardTransactionOfMonth month = new CardTransactionOfMonth();
        month.setRequests(new ArrayList<>());
        transactions.add(month);
        if ("2".equals(res.getResultID())) //无记录
            return transactions;
        if (!"0".equals(res.getResultID()))
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 1001,
                    "查询第三方接口失败");

        List<ZhuzongConsumeDate> dataList = res.getDataList();
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (ZhuzongConsumeDate date : dataList){
            CardTransactionFromVendorDTO dto = new CardTransactionFromVendorDTO();
            dto.setItemName("消费");
            dto.setAmount(new BigDecimal(date.getConsumeValue()));
            dto.setTransactionType("2");
            String time = date.getHappenDate() + " " + date.getHappenTime();
            try {
                dto.setTransactionTime(format.parse(time).getTime());
            } catch (ParseException e) {
                LOGGER.error("paymentCard parse error, time={} ", time);
            }
            month.getRequests().add(dto);
        }

        return transactions;
    }

    private List<CardTransactionOfMonth> listRechargeTransactions(ListCardTransactionsCommand cmd, PaymentCard card){
        JSONObject jo = new JSONObject();
        jo.put("FunctionID", RECHARGE_TRANSACTION_TYPE);
        jo.put("UserID",card.getCardNo());
        jo.put("DepositType","");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (cmd.getStartTime() != null) {
            jo.put("StartDate", format.format(new Date(cmd.getStartTime())));
            jo.put("EndDate", format.format(new Date(cmd.getEndTime())));
            jo.put("DayNum", "");
        }else{
            jo.put("DayNum", "0");
            jo.put("StartDate", "");
            jo.put("EndDate", "");
        }
        jo.put("PageIndex",cmd.getPageAnchor() != null ? cmd.getPageAnchor().toString():"1");
        jo.put("PageSize",cmd.getPageSize() != null ? cmd.getPageSize().toString():"10");
        String response = postToZhuzong(jo.toJSONString());
        ZhuzongRechargeResponse res = (ZhuzongRechargeResponse)StringHelper.fromJsonString(response, ZhuzongRechargeResponse.class);

        List<CardTransactionOfMonth> transactions = new ArrayList<>();
        CardTransactionOfMonth month = new CardTransactionOfMonth();
        month.setRequests(new ArrayList<>());
        transactions.add(month);
        if ("2".equals(res.getResultID())) //无记录
            return transactions;
        if (!"0".equals(res.getResultID()))
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 1001,
                    "查询第三方接口失败");

        List<ZhuzongRechargeDate> dataList = res.getDataList();
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (ZhuzongRechargeDate date : dataList){
            CardTransactionFromVendorDTO dto = new CardTransactionFromVendorDTO();
            dto.setItemName("充值·充值成功");
            dto.setAmount(new BigDecimal(date.getDepositValue()));
            dto.setTransactionType("1");
            String time = date.getHappenDate() + " " + date.getHappenTime();
            try {
                dto.setTransactionTime(format.parse(time).getTime());
            } catch (ParseException e) {
                LOGGER.error("paymentCard parse error, time={} ", time);
            }
            dto.setPaidType(date.getDepositType());
            month.getRequests().add(dto);
        }

        return transactions;
    }

    @Override
    public void rechargeCard(PaymentCardRechargeOrder order, PaymentCard card) {
        JSONObject jo = new JSONObject();
        jo.put("FunctionID", RECHARGE_TYPE);
        jo.put("OrderID", order.getOrderNo().toString());
        jo.put("UserID",card.getCardNo());
        jo.put("DepositValue",order.getAmount().setScale(2));
        jo.put("DepositType","10001".equals(order.getPaidType())?"2":"1");
        String response = postToZhuzong(jo.toJSONString());
        ZhuzongUserCardInfo cardInfo = (ZhuzongUserCardInfo) StringHelper.fromJsonString(response, ZhuzongUserCardInfo.class);
        if ("0".equals(cardInfo.getResultID())){//返回正常
            order.setRechargeStatus(CardRechargeStatus.RECHARGED.getCode());
            order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
            paymentCardProvider.updatePaymentCardRechargeOrder(order);
        }else{ //返回错误
            order.setRechargeStatus(CardRechargeStatus.FAIL.getCode());
            order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
            paymentCardProvider.updatePaymentCardRechargeOrder(order);
        }
    }

    public String postToZhuzong(String params) {
        String url = configProvider.getValue("paymentCard.zhuzong.url", "");
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        String json = null;

        try {
            httpPost.setEntity(new StringEntity(params,"UTF-8"));
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if(status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    json = EntityUtils.toString(entity, "utf8");
                }
            }else{
                LOGGER.error("paymentCard request error, param={} response = {}", params, response);
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 1001,
                        "paymentCard request error.");
            }

        }catch (IOException e) {
            LOGGER.error("paymentCard request error, param={}", params, e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 1001,
                    "paymentCard request error.");
        }finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("paymentCard close instream, response error, param={}", params, e);
                }
            }

        }
        if(LOGGER.isDebugEnabled())
            LOGGER.debug("Data from zhuzong, param={}, json={}", params, json);
        return json;
    }

    @Override
    public void freezeCard(FreezeCardCommand cmd) {
        PaymentCard card = paymentCardProvider.findPaymentCardById(cmd.getCardId());
        if (card == null )
            return ;
        JSONObject jo = new JSONObject();
        jo.put("FunctionID", FREEZE_ACCOUNT_TYPE);
        jo.put("UserID",card.getCardNo());
        jo.put("LossType",cmd.getLossType().toString());
        postToZhuzong(jo.toJSONString());

        card.setUpdateTime(new Timestamp(System.currentTimeMillis()));

    }

    @Override
    public void unbundleCard(PaymentCard paymentCard) {
        if (paymentCard == null )
            return ;
        ZhuzongVendorDate vendorDate = (ZhuzongVendorDate) StringHelper.fromJsonString(paymentCard.getVendorCardData(), ZhuzongVendorDate.class);
        JSONObject jo = new JSONObject();
        jo.put("FunctionID", UNBUNDLE_CARD_TYPE);
        jo.put("UserID",vendorDate.getUserId());
        jo.put("UserName",paymentCard.getUserName());
        String response = postToZhuzong(jo.toJSONString());
        ZhuzongUserCardInfo cardInfo = (ZhuzongUserCardInfo) StringHelper.fromJsonString(response, ZhuzongUserCardInfo.class);
        if (!"1".equals(cardInfo.getResultID())){//没有失败时
            paymentCard.setStatus(PaymentCardStatus.INACTIVE.getCode());
            paymentCard.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            paymentCardProvider.updatePaymentCard(paymentCard);
        }
    }

    public static void main (String[] args){
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost("http://111.207.114.167:9010");
        CloseableHttpResponse response = null;
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("FunctionID", "1"));
        nameValuePairs.add(new BasicNameValuePair("UserID", "1"));

        MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
        //String body = "{\"FunctionID\":\"1\",\"UserID\":\"1\"}";
        String body = "{\"FunctionID\":\"0\",\"UserID\":\"1\",\"UserName\":\"张三\"}";
        try {
            httpPost.setEntity(new StringEntity(body,"UTF-8"));
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            // httpPost.setEntity(stringEntity);
            response = httpclient.execute(httpPost);
            //System.out.println(Arrays.toString(response.getAllHeaders()));
            InputStream inputStream = response.getEntity().getContent();
            //System.out.println(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
            System.out.println(EntityUtils.toString(response.getEntity(),"UTF-8"));

        }catch (Exception e){

        }
    }
}

