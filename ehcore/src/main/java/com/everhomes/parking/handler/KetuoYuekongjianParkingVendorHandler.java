// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.parking.fujica.FujicaResponse;
import com.everhomes.parking.ketuo.KetuoJsonEntity;
import com.everhomes.parking.ketuo.KetuoRequestConfig;
import com.everhomes.parking.ketuo.KetuoTempFee;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.ParkingRechargeType;
import com.everhomes.rest.parking.ParkingTempFeeDTO;
import com.everhomes.util.MD5Utils;
import com.everhomes.util.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * 广州越空间 临时停车对接
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "YUE_KONG_JIAN")
public class KetuoYuekongjianParkingVendorHandler extends KetuoParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(KetuoParkingVendorHandler.class);

	private static final String GET_TEMP_FEE = "/api/wec/GetParkingPaymentInfo";//临时车查询接口
	private static final String PAY_TEMP_FEE = "/api/wec/PayParkingFee2";//临时车缴费接口
	
    @Override
    public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
        if(order.getRechargeType().equals(ParkingRechargeType.TEMPORARY.getCode())){
            return payTempCardFee(order);
        }

        return false;
    }
    
    boolean payTempCardFee(ParkingRechargeOrder order){
		JSONObject param = new JSONObject();
		int appId = 1;
        Integer amount = order.getOriginalPrice().multiply(new BigDecimal(100)).intValue();
		param.put("appId", appId);
		param.put("orderNo", order.getOrderToken());
		param.put("amount", (order.getOriginalPrice().multiply(new BigDecimal(100))).intValue());
	    param.put("payType", VendorType.WEI_XIN.getCode().equals(order.getPaidType()) ? 4 : 5);
		String json = doJsonPostPay(order, PAY_TEMP_FEE);

        JSONObject jsonObject = JSONObject.parseObject(json);
		Object obj = jsonObject.get("resCode");
		if(null != obj ) {
			int resCode = (int) obj;
			if(resCode == 0) {
				return true;
			}
		}
		return false;
    }

    @Override
    public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
		KetuoTempFee tempFee = null;
		String json = doJsonPost(plateNumber,GET_TEMP_FEE);
        
		KetuoJsonEntity<KetuoTempFee> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KetuoTempFee>>(){});
		if(entity.isSuccess()){
			List<KetuoTempFee> list = entity.getData();
			if(null != list && !list.isEmpty()) {
				tempFee = list.get(0);
			}
		}
		ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
		if(null == tempFee) {
			return dto;
		}
		dto.setPlateNumber(plateNumber);
		dto.setEntryTime(strToLong(tempFee.getEntryTime()));
		dto.setPayTime(strToLong(tempFee.getPayTime()));
		dto.setParkingTime(tempFee.getElapsedTime());
		dto.setDelayTime(tempFee.getDelayTime());
		dto.setPrice(new BigDecimal(tempFee.getPayable()).divide(new BigDecimal(100), TEMP_FEE_RETAIN_DECIMAL, RoundingMode.HALF_UP));

		dto.setOrderToken(tempFee.getOrderNo());
		return dto;
    }
    
	@Override
	protected KetuoRequestConfig getKetuoRequestConfig() {
		String url = configProvider.getValue("parking.yuekongjian.url", "");
		String key = configProvider.getValue("parking.yuekongjian.key", "");
//		String user = configProvider.getValue("parking.yuekongjian.user", "");
//		String pwd = configProvider.getValue("parking.yuekongjian.pwd", "");

		KetuoRequestConfig config = new KetuoRequestConfig();
		config.setUrl(url);
		config.setKey(key);
//		config.setUser(user);
//		config.setPwd(pwd);

		return config;
	}
	
	private String doJsonPost(String plateNumber, String urlPath) {
		JSONObject param = new JSONObject();
		urlPath = configProvider.getValue("parking.yuekongjian.url", "")+urlPath;
		String appSecret = configProvider.getValue("parking.yuekongjian.key", "");
		String parkId = "1";
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        String iv = sdf2.format(new Date());
        plateNumber=plateNumber.substring(2);
        String p = parkId + plateNumber + iv + appSecret;
        String key = MD5Utils.getMD5(p);
		param.put("appId", "1");
		param.put("key", key);
		param.put("parkId", "1");
		param.put("plateNo", plateNumber);
		
        String json = param.toJSONString();
        String result = "";
        BufferedReader reader = null;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
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


    private String doJsonPostPay(ParkingRechargeOrder order, String urlPath) {
        JSONObject param = new JSONObject();
        urlPath = configProvider.getValue("parking.yuekongjian.url", "")+urlPath;
        String appSecret = configProvider.getValue("parking.yuekongjian.key", "");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        String iv = sdf2.format(new Date());
        Integer amount = (order.getOriginalPrice().multiply(new BigDecimal(100))).intValue();
        Integer payType = VendorType.WEI_XIN.getCode().equals(order.getPaidType()) ? 4 : 5;
        String payMethod = "0";
        String freeMoney = "0";
        String freeTime = "0";
        String p = order.getOrderToken() + amount + payType + payMethod + freeMoney + freeTime + iv + appSecret;
        String key = MD5Utils.getMD5(p);
        param.put("appId", "1");
        param.put("key", key);
        param.put("orderNo", order.getOrderToken());
        param.put("amount", amount);
        param.put("payType", payType);
        param.put("payMethod", payMethod);
        param.put("freeMoney", freeMoney);
        param.put("freeTime", freeTime);

        String json = param.toJSONString();
        String result = "";
        BufferedReader reader = null;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
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

    public static void main (String[] args){

        String urlPath = "http://220.160.111.118:8099/api/wec/PayParkingFee2";
       // String urlPath = "http://mops-test.fujica.com.cn:8021/Api/CalculationCost/ApiThirdPartyTemporaryCardPay";
        JSONObject jo = new JSONObject();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        String iv = sdf2.format(new Date());
        String freeDetailString = "";
        String p = "000120180911141352092CCC002" + "1" +"0000" +iv + "b20887292a374637b4a9d6e9f940b1e6";
        String key = MD5Utils.getMD5(p);
        jo.put("appId", "1");
        jo.put("key", key);
        jo.put("orderNo", "000120180911141352092CCC002");
        jo.put("amount", "1");
        jo.put("payType","0");
        jo.put("payMethod", "0");
        jo.put("freeMoney", "0");
        jo.put("freeTime", "0");
        //jo.put("freeDetail", freeDetailString);

        String json = jo.toJSONString();
        String result = "";
        BufferedReader reader = null;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
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
}