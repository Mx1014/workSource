package com.everhomes.payment.zhuzong;

import com.everhomes.payment.PaymentCard;
import com.everhomes.payment.PaymentCardIssuer;
import com.everhomes.payment.PaymentCardRechargeOrder;
import com.everhomes.payment.PaymentCardVendorHandler;
import com.everhomes.rest.payment.*;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
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
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component(PaymentCardVendorHandler.PAYMENTCARD_VENDOR_PREFIX + "ZHUZONG")
public class ZhuzongPaymentCardVendorHandler implements PaymentCardVendorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZhuzongPaymentCardVendorHandler.class);

    private static final String ACCOUNT_QUERY_TYPE = "1";
    @Override
    public List<CardInfoDTO> getCardInfoByVendor(ListCardInfoCommand cmd) {
        return null;
    }

    @Override
    public CardInfoDTO applyCard(ApplyCardCommand cmd, PaymentCardIssuer cardIssuer) {
        return null;
    }

    @Override
    public void setCardPassword(SetCardPasswordCommand cmd, PaymentCard paymentCard) {

    }

    @Override
    public void resetCardPassword(ResetCardPasswordCommand cmd, PaymentCard paymentCard) {

    }

    @Override
    public List<CardTransactionOfMonth> listCardTransactions(ListCardTransactionsCommand cmd, PaymentCard card) {
        return null;
    }

    @Override
    public String getCardPaidQrCodeByVendor(PaymentCard paymentCard) {
        return null;
    }

    @Override
    public void rechargeCard(PaymentCardRechargeOrder order, PaymentCard card) {

    }

    public static void main (String[] args){
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost("http://111.207.114.167:9010");
        CloseableHttpResponse response = null;
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("FunctionID", "1"));
        nameValuePairs.add(new BasicNameValuePair("UserID", "1"));

        MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
        String body = "{\"FunctionID\":\"1\",\"UserID\":\"1\"}";
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

