package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.parking.fujica.Base64;
import com.everhomes.parking.fujica.FujicaJsonParam;
import com.everhomes.parking.fujica.FujicaResponse;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.*;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.*;

@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "FU_JI_CA")
public class FujicaParkingVendorHandler extends DefaultParkingVendorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(FujicaParkingVendorHandler.class);

    private static final String PARKING_GET_PAY_INFO = "/Park/GetParkInfoByCarNo";//查询临时车缴费信息接口
    private static final String PARKING_PAY_PARKING = "/CalculationCost/ApiThirdPartyTemporaryCardPay";//临时车缴费接口

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    ConfigurationProvider configProvider;
    @Autowired
    ParkingProvider parkingProvider;
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
        if(order.getRechargeType().equals(ParkingRechargeType.TEMPORARY.getCode())){
            return payTempCardFee(order);
        }

        return false;
    }

    boolean payTempCardFee(ParkingRechargeOrder order){
        JSONObject jo = new JSONObject();
        jo.put("CarNo",order.getPlateNumber());
        jo.put("DealNo",order.getOrderNo().toString());
        jo.put("Amount",order.getPrice());
        jo.put("ActualAmount",order.getPrice());
        jo.put("DeductionAmount",new BigDecimal(0));
        jo.put("PayStyle",VendorType.WEI_XIN.getCode().equals(order.getPaidType())?"微信":"支付宝");
        String parkingCode = configProvider.getValue("parking.fujica.parkingCode","17000104590501");
        jo.put("ParkingCode",parkingCode);
        jo.put("PayCostMachineOrderId","");

        String result = doJsonPost(PARKING_PAY_PARKING,jo.toJSONString());
        FujicaResponse entity = JSONObject.parseObject(result, FujicaResponse.class);
        ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
        if(entity != null  && entity.isSuccess() ) {
            return true;
        }
        return false;
    }

    @Override
    public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
        return null;
    }

    @Override
    public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {

    }

    @Override
    public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
        JSONObject jo = new JSONObject();
        jo.put("CarNo",plateNumber);
        String parkingCode = configProvider.getValue("parking.fujica.parkingCode","17000104590501");
        jo.put("ParkingCode",parkingCode);
        String result = doJsonPost(PARKING_GET_PAY_INFO,jo.toJSONString());
        FujicaResponse entity = JSONObject.parseObject(result, FujicaResponse.class);
        ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
        if(entity != null  && entity.isSuccess() && entity.getDataType() == 3) {//3是临时车
            FujicaJsonParam param = JSONObject.parseObject(entity.getJsonParam(),FujicaJsonParam.class);
            dto.setPlateNumber(param.getCarNo());
            dto.setEntryTime(getTimestamp(param.getBeginTime()));
            long now = System.currentTimeMillis();
            dto.setPayTime(now);
            Long parkingTime = getTimestamp(param.getChargeableTime())-dto.getEntryTime();
            dto.setParkingTime((int)(parkingTime /1000/60));
            dto.setDelayTime(param.getOverTime());
            dto.setPrice(param.getActualAmount());
            dto.setOriginalPrice(param.getParkingFee());
            dto.setOrderToken(param.getParkingCode());
            if (param.getActualAmount().compareTo(new BigDecimal(0))==0){
                if (parkingTime/1000/60 < param.getFreeTime())
                     dto.setRemainingTime((int)(param.getFreeTime() - parkingTime/1000/60));
                else{
                    List<ParkingRechargeOrder> list = parkingProvider.searchParkingRechargeOrders(parkingLot.getOwnerType(),parkingLot.getOwnerId(),parkingLot.getId(),
                            plateNumber,null,null,null,null,null,null,
                            null,ParkingRechargeOrderStatus.PAID.getCode(), null, null, null,null,null);
                    if (list!=null && list.size()>0){
                        Long lastPaidTime = now - list.get(0).getPaidTime().getTime();
                        if (lastPaidTime/1000/60 < param.getOverTime())
                            dto.setRemainingTime((int)(param.getOverTime() - lastPaidTime/1000/60));
                    }
                }
            }

        }
        return dto;
    }



    public static void main (String[] args){

        String urlPath = "http://mops-test.fujica.com.cn:8021/Api/Park/GetParkInfoByCarNo";
       // String urlPath = "http://mops-test.fujica.com.cn:8021/Api/CalculationCost/ApiThirdPartyTemporaryCardPay";
        JSONObject jo = new JSONObject();
        jo.put("ParkingCode","17000104590501");
        jo.put("CarNo","湘A4YXPP");

//        jo.put("DealNo","sssasdasfaxcasdqwrx");
//        jo.put("Amount",new BigDecimal(1));
//        jo.put("ActualAmount",new BigDecimal(1));
//        jo.put("DeductionAmount",new BigDecimal(0));
//        jo.put("PayStyle","支付宝");
//        jo.put("PayCostMachineOrderId","");
        String json = jo.toJSONString();
        String result = "";
        BufferedReader reader = null;
        String appid = "fujica_dacfe36d3f15ae51";
        String appSecret = "69416bc8b9e44d56b7672d18b1106668";
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALATu5WNNx+XcrAS" +
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
                        "ojrvFNyFMEkWTQ==";
        String time = getTimestamp();
        TreeMap<String, String> map = new TreeMap();
        map.put("param", json);
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
            if (json != null && !("").equals(json)) {
                byte[] writebytes = json.getBytes();
                // 设置文件长度
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = (OutputStream) conn.getOutputStream();
                outwritestream.write(json.getBytes());
                outwritestream.flush();
                outwritestream.close();

            }
            if (conn.getResponseCode() == 200) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                result = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(result);
    }

    private  String doJsonPost(String urlPath, String json) {
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
        urlPath = configProvider.getValue("parking.fujica.url","http://mops-test.fujica.com.cn:8021/Api")+urlPath;
        String time = getTimestamp();
        TreeMap<String, String> map = new TreeMap();
        map.put("param", json);
        map.put("secret", appSecret);
        map.put("timestamp", time);
        String sign = signWithoutMD5(map);
        sign = sign(sign, privateKey);
        LOGGER.info("The request info, url={}, param={}", urlPath, json);
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
            if (json != null && !("").equals(json)) {
                byte[] writebytes = json.getBytes();
                // 设置文件长度
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = (OutputStream) conn.getOutputStream();
                outwritestream.write(json.getBytes());
                outwritestream.flush();
                outwritestream.close();

            }
            if (conn.getResponseCode() == 200) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                result = reader.readLine();
            }
        } catch (Exception e) {
            LOGGER.error("The request error, param={}", json, e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "The request error.");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        LOGGER.info("SendResult from third, url={}, result={}", urlPath, result);
        return result;
    }

    private static Long getTimestamp(String dayTime){
        try {
            Date date = format.parse(dayTime);
            return date.getTime();
        }catch (Exception e){
            LOGGER.error("parse time error ",e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "parse time error.");
        }

    }

    private static String getTimestamp() {
        String time = "";
        long timestamp = System.currentTimeMillis();
        time = String.valueOf(timestamp);
        time = time.substring(0, 10);
        return time;
    }

    private static String signWithoutMD5(SortedMap<String, String> params) {
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
    private static String sign(String content, String privateKey) {
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
