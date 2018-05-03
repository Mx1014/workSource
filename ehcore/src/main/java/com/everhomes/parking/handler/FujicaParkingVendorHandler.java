package com.everhomes.parking.handler;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.parking.fujica.Base64;
import com.everhomes.rest.parking.ListCardTypeCommand;
import com.everhomes.rest.parking.ListCardTypeResponse;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "FU_JI_CA")
public class FujicaParkingVendorHandler extends DefaultParkingVendorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(FujicaParkingVendorHandler.class);
    @Autowired
    ConfigurationProvider configProvider;
    @Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
        return null;
    }

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot, String plateNumber, String cardNo) {
        return null;
    }

    @Override
    public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
        return null;
    }

    @Override
    public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
        return null;
    }

    @Override
    public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {

    }

    private  String doJsonPost(String urlPath, String Json) {
        String result = "";
        BufferedReader reader = null;
        String appid = configProvider.getValue("parking.fujica.appid","fujica_dacfe36d3f15ae51");
        String appSecret = configProvider.getValue("parking.fujica.appSecret","69416bc8b9e44d56b7672d18b1106668");
        String privateKey = configProvider.getValue(
                "parking.fujica.privateKey","MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALATu5WNNx+XcrAS" +
                "NJieGRsY7VryQjcs+pABXGh4BOCqAW3FgAAc21aIG0uxvYHdQUw1I7J80RiQJ506" +
                "aIktGTmZKJTjez7JNF7GO4ieGmLMI3ds8iXyJrfUMp/1U3Cq6vja8vuRIKYVQCxZ" +
                "e8un98Nr03F7oMlKpBydJQKVdlAHAgMBAAECgYBPcqfqhAyCWbCrF5vZ3URQwL+g" +
                "kL0l7kqknaiXjsgMo0j/weTOqDaj5cgDMJDkvvPOsg+IYt9qKOlm/Urb0piVb9v9" +
                "iAnp6gIppcyHrPUesgYRSbqR/HhdGtnzn/4f45YpkXA6URMfc04xBrIUCw5Dmnqo" +
                "nJZJkjd6jjXiL65rYQJBAN8eg99yFH45D2cdlPBTN0LVWXgqy3N22byYh0j6kgo2" +
                "5WOPMcuIZPM/XdSoTfhYeHixvEwrmCAkCmSkf524prECQQDKBnzZRj3eCij1z2SZ" +
                "JCRZhRQUhFmK0AYYTXw9R0DfEaZNrEAe8PEypS94lYROvNV3TnSnK2FHKeH8YhHX" +
                "EoA3AkBRHzMrRrsUuYJUJ3lDd74b2p5RBp46OPgpjfuCGTiH5jW44RNlwQ2TM3LW" +
                "IutWZDRJDbY8q40AApqUxQpxOfXBAkA8bB5RGZoNW7qOcj3jM5UPlSbBUCg7xSXd" +
                "hOdAqJv1W6ECoB75YhSxkggVp5pPtlid+0AWc3n/v74QLwCo86aXAkEAlxY3hoxQ" +
                "jJiCjD2PbL0GLkMKNBqJgLcDWqeoHcal+iY2Fpr3U/iCJZNoBuovC2Qvc5J1y61P" +
                "ojrvFNyFMEkWTQ==");
        String time = getTimestamp();
        TreeMap<String, String> map = new TreeMap();
        map.put("param", Json);
        map.put("secret", appSecret);
        map.put("timestamp", time);
        String sign = signWithoutMD5(map);
        sign = sign(sign, privateKey);

        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("appid", appid);
            conn.setRequestProperty("appSecret", appSecret);
            conn.setRequestProperty("sign", sign);
            conn.setRequestProperty("timestamp", time);
            // 设置文件类型:
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            // 设置接收类型否则返回415错误
            conn.setRequestProperty("accept", "*/*");// 此处为暴力方法设置接受所有类型，以此来防范返回415;
            conn.setRequestProperty("accept", "application/json");
            // 往服务器里面发送数据
            if (Json != null && !("").equals(Json)) {
                byte[] writebytes = Json.getBytes();
                // 设置文件长度
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = (OutputStream) conn.getOutputStream();
                outwritestream.write(Json.getBytes());
                outwritestream.flush();
                outwritestream.close();

            }
            if (conn.getResponseCode() == 200) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                result = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String getTimestamp() {
        String time = "";
        long timestamp = System.currentTimeMillis();
        time = String.valueOf(timestamp);
        time = time.substring(0, 10);
        return time;
    }

    public static String signWithoutMD5(SortedMap<String, String> params) {
        String restul = "";
        if (params == null) {
            return restul;
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> me : params.entrySet()) {

            sb.append(me.getKey());
            sb.append('=');
            sb.append(me.getValue());
            sb.append('&');

        }
        int lastIndexOf = sb.lastIndexOf("&");
        if (lastIndexOf != -1) {
            sb.deleteCharAt(lastIndexOf);
        }
        restul = new String(sb);

        return restul;
    }

    private static final String ALGORITHM = "RSA";
    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    private static final String DEFAULT_CHARSET = "UTF-8";
    public static String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);

            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(DEFAULT_CHARSET));

            byte[] signed = signature.sign();

            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
