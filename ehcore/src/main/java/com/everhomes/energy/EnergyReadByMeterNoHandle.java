package com.everhomes.energy;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rui.Jia  2018/4/16 11 :01
 */
@Component(EnergyAutoReadHandler.AUTO_PREFIX + EnergyAutoReadHandler.ZHI_FU_HUI)
public class EnergyReadByMeterNoHandle implements EnergyAutoReadHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnergyReadByMeterNoHandle.class);
    private static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    @Override
    public String readMeterautomatically(String meterName, String serverUrl, String publicKey, String clientId) {
        String access_token = getToken(publicKey, clientId);
        Map<String, String> params = new HashMap<>();
        params.put("meterNo", meterName);
        params.put("access_token", access_token);
        return sendPost(serverUrl + "/joy/readByMeterNo.do", params);
    }

    /**
     * 向服务器发送post请求
     */
    private static String sendPost(String url, Map<String, String> params) {
        long st = new Date().getTime();
        if (url == null || url.isEmpty() || params == null || params.isEmpty()) {
            return "";
        }
        String result = "";
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // httpPost.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        try {
            List<NameValuePair> list = new ArrayList<>();
            params.forEach((k, v) -> list.add(new BasicNameValuePair(k, v)));
            httpPost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            LOGGER.error("post exception", e);
        } finally {
            try {
                httpPost.releaseConnection();
                if (response != null) {
                    response.close();
                }
                EntityUtils.consume(entity);
                //httpClient.close();
            } catch (IOException e) {
                LOGGER.error("post exception", e);
            }
        }
        LOGGER.debug("response elapse: " + (new Date().getTime() - st) / 1000 + " s");
        return result;
    }

    /**
     * @return @throws Exception
     */
    private static String getToken(String publicKey, String inputString) {
        try {
            String inputStr = String.format("{'client_id':'%s','datetime':'%s'}", inputString, new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            LOGGER.debug("before encrypted: " + inputStr);
            byte[] data = inputStr.getBytes();
            byte[] encodedData = encryptByPublicKey(data, publicKey);
            return Base64.encodeBase64URLSafeString(encodedData);

        } catch (Exception ex) {
            LOGGER.error("get token error ", ex);
        }
        return "";
    }

    /**
     * 加密
     * 用公钥加密
     */
    private static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
        // 对公钥解密
        byte[] keyBytes = Base64.decodeBase64(key);
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }
}
