package com.everhomes.payment.zhuzong;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.payment.*;
import com.everhomes.rest.payment.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component(PaymentCardVendorHandler.PAYMENTCARD_VENDOR_PREFIX + "ZHUZONG")
public class ZhuzongPaymentCardVendorHandler implements PaymentCardVendorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZhuzongPaymentCardVendorHandler.class);

    @Autowired
    ConfigurationProvider configProvider;
    @Autowired
    private PaymentCardProvider paymentCardProvider;

    private static final String ACCOUNT_QUERY_TYPE = "1";
    private static CloseableHttpClient httpClient = HttpClients.createDefault();
    @Override
    public List<CardInfoDTO> getCardInfoByVendor(ListCardInfoCommand cmd) {
        User user = UserContext.current().getUser();
        List<PaymentCard> cardList = paymentCardProvider.listPaymentCard(cmd.getOwnerId(),cmd.getOwnerType(),user.getId());
        List<CardInfoDTO> result = new ArrayList<CardInfoDTO>();
        if (cardList == null || cardList.size() == 0)
            return null;
        PaymentCard card = cardList.get(0);
        JSONObject jo = new JSONObject();
        jo.put("FunctionID", "1");
        ZhuzongVendorDate vendorDate = (ZhuzongVendorDate) StringHelper.fromJsonString(card.getVendorCardData(), ZhuzongVendorDate.class);
        jo.put("UserID", vendorDate.getUserId());
        String response = postToZhuzong(jo.toJSONString());
        ZhuzongUserCardInfo cardInfo = (ZhuzongUserCardInfo) StringHelper.fromJsonString(response, ZhuzongUserCardInfo.class);
        if ("1".equals(cardInfo.getResultID())){//返回报错
            LOGGER.error("paymentCard getCardInfoByVendor error, param={} response = {}", jo,response);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "paymentCard getCardInfoByVendor error");
        }
        if (!"0".equals(cardInfo.getResultID()))
            return null;
        CardInfoDTO cardInfoDTO = new CardInfoDTO();
        cardInfoDTO.setBalance(new BigDecimal(cardInfo.getBalance()));
        cardInfoDTO.setMobile(card.getMobile());
        cardInfoDTO.setStatus(cardInfo.getStateID());
        cardInfoDTO.setCardNo(cardInfo.getCardID());
        vendorDate = new ZhuzongVendorDate();
        vendorDate.setDeptName(cardInfo.getDeptName());
        vendorDate.setUserId(cardInfo.getUserID());
        vendorDate.setUserName(cardInfo.getUserName());
        cardInfoDTO.setVendorCardData(StringHelper.toJsonString(vendorDate));
        cardInfoDTO.setCardId(card.getId());
        result.add(cardInfoDTO);

        return result;
    }

    @Override
    public CardInfoDTO applyCard(ApplyCardCommand cmd, PaymentCardIssuer cardIssuer) {
        JSONObject jo = new JSONObject();
        jo.put("FunctionID", "0");
        jo.put("UserName ",cmd.getName());
        jo.put("UserID ",cmd.getUserId());


        return null;
    }

    @Override
    public List<CardTransactionOfMonth> listCardTransactions(ListCardTransactionsCommand cmd, PaymentCard card) {
        return null;
    }

    @Override
    public void rechargeCard(PaymentCardRechargeOrder order, PaymentCard card) {

    }

    public String postToZhuzong(String params) {
        String url = configProvider.getValue("paymentCard.zhuzong.url", "");
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        String json = null;

        try {
            httpPost.setEntity(new StringEntity(params));
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if(status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    json = EntityUtils.toString(entity, "utf8");
                }
            }

        }catch (IOException e) {
            LOGGER.error("paymentCard request error, param={}", params, e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
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
        ZhuzongVendorDate vendorDate = (ZhuzongVendorDate) StringHelper.fromJsonString(card.getVendorCardData(), ZhuzongVendorDate.class);
        JSONObject jo = new JSONObject();
        jo.put("FunctionID", "4");
        jo.put("UserID",vendorDate.getUserId());
        jo.put("LossType",cmd.getLossType().toString());
        postToZhuzong(jo.toJSONString());

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
        String body = "{\"FunctionId\":\"0\",\"UserId\":\"0000001\",\"UserName\":\"张三\"}";
        try {
            httpPost.setEntity(new StringEntity(body));
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

