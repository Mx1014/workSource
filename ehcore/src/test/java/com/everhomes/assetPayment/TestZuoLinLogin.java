package com.everhomes.assetPayment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;

import com.alibaba.fastjson.JSONObject;

public class TestZuoLinLogin {
	
	/**  
     * 发送GET请求  
     *   
     * @param url  
     *            目的地址  
     * @param parameters  
     *            请求参数，Map类型。  
     * @return 远程响应结果  
     */    
    public static String sendGet(String url, Map<String, String> parameters) {   
        String result="";  
        BufferedReader in = null;// 读取响应输入流    
        StringBuffer sb = new StringBuffer();// 存储参数    
        String params = "";// 编码之后的参数  
        try {  
            // 编码请求参数    
            if(parameters.size()==1){  
                for(String name:parameters.keySet()){  
                    sb.append(name).append("=").append(  
                            java.net.URLEncoder.encode(parameters.get(name),    
                            "UTF-8"));  
                }  
                params=sb.toString();  
            }else{  
                for (String name : parameters.keySet()) {    
                    sb.append(name).append("=").append(    
                            java.net.URLEncoder.encode(parameters.get(name),    
                                    "UTF-8")).append("&");    
                }    
                String temp_params = sb.toString();    
                params = temp_params.substring(0, temp_params.length() - 1);    
            }  
            String full_url = url + "?" + params;   
            System.out.println(full_url);   
            // 创建URL对象    
            java.net.URL connURL = new java.net.URL(full_url);    
            // 打开URL连接    
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL    
                    .openConnection();    
            // 设置通用属性    
            httpConn.setRequestProperty("Accept", "*/*");    
            httpConn.setRequestProperty("Connection", "Keep-Alive");    
            httpConn.setRequestProperty("User-Agent",    
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");    
            // 建立实际的连接    
            httpConn.connect();    
            // 响应头部获取    
            Map<String, List<String>> headers = httpConn.getHeaderFields();    
            // 遍历所有的响应头字段    
            for (String key : headers.keySet()) {    
                System.out.println(key + "\t：\t" + headers.get(key));    
            }    
            // 定义BufferedReader输入流来读取URL的响应,并设置编码方式    
            in = new BufferedReader(new InputStreamReader(httpConn    
                    .getInputStream(), "UTF-8"));    
            String line;    
            // 读取返回的内容    
            while ((line = in.readLine()) != null) {    
                result += line;    
            }    
        } catch (Exception e) {  
            e.printStackTrace();  
        }finally{  
            try {    
                if (in != null) {    
                    in.close();    
                }    
            } catch (IOException ex) {    
                ex.printStackTrace();    
            }    
        }  
        return result ;  
    }    
    
    /**   
     * 发送POST请求   
     *    
     * @param url   
     *            目的地址   
     * @param parameters   
     *            请求参数，Map类型。   
     * @return 远程响应结果   
     */    
    public static String sendPost(String url, Map<String, String> parameters) {    
        String result = "";// 返回的结果    
        BufferedReader in = null;// 读取响应输入流    
        PrintWriter out = null;    
        StringBuffer sb = new StringBuffer();// 处理请求参数    
        String params = "";// 编码之后的参数    
        try {    
            // 编码请求参数    
            if (parameters.size() == 1) {    
                for (String name : parameters.keySet()) {    
                    sb.append(name).append("=").append(    
                            java.net.URLEncoder.encode(parameters.get(name),    
                                    "UTF-8"));    
                }    
                params = sb.toString();    
            } else {    
                for (String name : parameters.keySet()) {    
                    sb.append(name).append("=").append(    
                            java.net.URLEncoder.encode(parameters.get(name),    
                                    "UTF-8")).append("&");    
                }    
                String temp_params = sb.toString();    
                params = temp_params.substring(0, temp_params.length() - 1);    
            }    
            // 创建URL对象    
            java.net.URL connURL = new java.net.URL(url);    
            // 打开URL连接    
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL    
                    .openConnection();    
            // 设置通用属性    
            httpConn.setRequestProperty("Accept", "*/*");    
            httpConn.setRequestProperty("Connection", "Keep-Alive");    
            httpConn.setRequestProperty("User-Agent",    
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");    
            // 设置POST方式    
            httpConn.setDoInput(true);    
            httpConn.setDoOutput(true);    
            // 获取HttpURLConnection对象对应的输出流    
            out = new PrintWriter(httpConn.getOutputStream());    
            // 发送请求参数    
            out.write(params);    
            // flush输出流的缓冲    
            out.flush();    
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式    
            in = new BufferedReader(new InputStreamReader(httpConn    
                    .getInputStream(), "UTF-8"));    
            String line;    
            // 读取返回的内容    
            while ((line = in.readLine()) != null) {    
                result += line;    
            }    
        } catch (Exception e) {    
            e.printStackTrace();    
        } finally {    
            try {    
                if (out != null) {    
                    out.close();    
                }    
                if (in != null) {    
                    in.close();    
                }    
            } catch (IOException ex) {    
                ex.printStackTrace();    
            }    
        }    
        return result;    
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
    
    /**   
     * 主函数，测试请求   
     *    
     * @param args   
     */    
    public static void main(String[] args) {    
        Map<String, String> parameters = new HashMap<String, String>();    
        parameters.put("appKey", "03c9d78c-5369-4269-8a46-2058d1c54696");    
        parameters.put("nonce", "1536076079");
        parameters.put("contentType", "text");
        parameters.put("content", "欢迎您使用XXX服务");
        parameters.put("timestamp", "1498641124142");
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "0");
        jsonObject.put("cusName", "深圳湾");
        parameters.put("customJson", jsonObject.toString());
        
        parameters.put("namespaceId", "999966");
        
        String signature = computeSignature(parameters, "S/IxJYKRq1y2Sk6Mh0jE3NVfNm7T91CL+V3cR8f9QC+p04XaJqZ5gsjtuWxTKe0B8/soqELcJfyul1FoES/P6w==");
        System.out.println("signature=" + signature);
        parameters.put("signature", signature);
        
        System.out.println(parameters.toString());
        
        String result =sendPost("https://parktest.szbay.com/evh/openapi/sendMessageToCustomUser", parameters);  
        //String result =sendPost("http://127.0.0.1:8080/evh/openapi/sendMessageToCustomUser", parameters);  
        System.out.println(result);   
    }    

}
