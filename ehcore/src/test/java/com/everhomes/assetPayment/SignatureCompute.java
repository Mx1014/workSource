//@formatter:off
package com.everhomes.assetPayment;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.Timestamp;
import java.util.*;

/**
 * Created by Wentian Wang on 2017/8/11.
 */

public class SignatureCompute {
    @Test
    public void fun(){
        HashMap<String,String> params = new HashMap<>();
        params.put("communityName","创业公寓");
//        params.put("communityName","863基地");
//        params.put("companyName","矽光光电科技（上海）有限公司");
        params.put("organizationName","矽光光电科技（上海）有限公司");
//        params.put("buildingName","张衡路200号1幢全幢");
//        params.put("apartmentName","2层-211");
//        params.put("communityName","创业公寓");
//        params.put("organizationName","上海玮舟微电子科技有限公司");
        params.put("offset","1");
        params.put("pageSize","4");
        params.put("payFlag","1");
        params.put("sdateFrom","2016-04");
        params.put("sdateTo","2016-06");
        params.put("appKey","ee4c8905-9aa4-4d45-973c-ede4cbb3cf21");
        Long l = System.currentTimeMillis();
        params.put("timestamp","1498097655000");
        Random r = new Random();
        int i = r.nextInt(1000);
        params.put("nonce","54256");
        params.put("crypto","sssss");
        String secretKey = "2CQ7dgiGCIfdKyHfHzO772IltqC50e9w7fswbn6JezdEAZU+x4+VHsBE/RKQ5BCkz/irj0Kzg6te6Y9JLgAvbQ==";
        String signature = computeSignature(params,secretKey );
        System.out.println(verifySignature(params,secretKey,signature));
        System.out.println("=================================================================");
        params.put("signature",signature);
        for(Map.Entry entry : params.entrySet()) {
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }
    public static String computeSignature(Map<String, String> params, String secretKey) {
        assert(params != null);
        assert(secretKey != null);

        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            byte[] rawKey = Base64.decodeBase64(secretKey);

            SecretKeySpec keySpec = new SecretKeySpec(rawKey, "HmacSHA1");
            mac.init(keySpec);

            List<String> keyList = new ArrayList<String>();
            CollectionUtils.addAll(keyList, params.keySet().iterator());
            Collections.sort(keyList);

            for(String key : keyList) {
                mac.update(key.getBytes("UTF-8"));
                String val = params.get(key);
                if(val != null && !val.isEmpty())
                    mac.update(val.getBytes("UTF-8"));
            }

            byte[] encryptedBytes = mac.doFinal();
            String signature = Base64.encodeBase64String(encryptedBytes);

            return signature;
        } catch(InvalidKeyException e) {
            throw new InvalidParameterException("Invalid secretKey for signing");
        } catch(NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException for HmacSHA1", e);
        } catch(UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException for UTF-8", e);
        }
    }

    public static boolean verifySignature(Map<String, String> params, String secretKey, String signatureToVerify) {
        String signature = computeSignature(params, secretKey);

        if(signature.equals(signatureToVerify))
            return true;

        return false;
    }

}
