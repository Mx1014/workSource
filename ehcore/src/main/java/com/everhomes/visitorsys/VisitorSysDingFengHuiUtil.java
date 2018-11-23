package com.everhomes.visitorsys;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.user.UserContext;
import com.everhomes.visitorsys.jsst.JsstEntity;
import com.everhomes.visitorsys.jsst.JsstRequestParams;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.xmlbeans.impl.jam.JSourcePosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.everhomes.util.StringHelper.toHexString;

@Component
public class VisitorSysDingFengHuiUtil {

    @Autowired
    VisitorSysVisitReasonProvider visitReasonProvider;

    public Object getInviteInfo(VisitorSysVisitor visitor){

        String cid = "880075500000001";
        String parkCode = "0010028888";

        JsstRequestParams params = new JsstRequestParams();
        params.setServiceId("3c.visitor.invite");
        JSONObject attr = new JSONObject();
        attr.put("areaCode", parkCode);
        attr.put("businesserCode",cid);
        attr.put("parkCode",parkCode);
        String personCode = getPersonCode(UserContext.current().getUser().getIdentifierToken());
        attr.put("personCode",personCode);

        Timestamp time = visitor.getPlannedVisitTime();
        DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        DateFormat edFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String sd = sdFormat.format(time);
        String ed = edFormat.format(time);
        JSONObject timeDesc = new JSONObject();
        timeDesc.put("sd",sd);
        timeDesc.put("ed",ed);
        timeDesc.put("w","0000000");
        timeDesc.put("st","00:00");
        timeDesc.put("et","00:00");
        attr.put("timeDesc",timeDesc);

        attr.put("visitorContent","");
        VisitorSysVisitReason reason = visitReasonProvider.findVisitorSysVisitReasonById(visitor.getVisitReasonId());

        attr.put("visitorType",reason.getId());

        params.setAttributes(attr);

        List<JSONObject> dataItems = new ArrayList<>();
        JSONObject visitorParam = new JSONObject();
        visitorParam.put("visitorName",visitor.getVisitorName());
        visitorParam.put("visitorTel",visitor.getVisitorPhone());
        visitorParam.put("carNo",visitor.getPlateNo());
        dataItems.add(visitorParam);

        params.setDataItems(dataItems);

        String json = post(params.toString());

        return "";
    }

    public String getPersonCode(String phone){
        String personCode = "";
        String parkCode = "0010028888";
        JsstRequestParams params = new JsstRequestParams();
        params.setServiceId("3c.base.querypersonsbytel");
        JSONObject attr = new JSONObject();
        attr.put("areaCode",parkCode);
        attr.put("telephone",phone);
        params.setAttributes(attr);

        String json = post(params.toString());

        JsstEntity entity = JSONObject.parseObject(json,JsstEntity.class);
        if(entity.getResultCode() == 0){
            List<JSONObject> list = entity.getDataItems();
            if(list != null && list.size() > 0){
                personCode = list.get(0).getString("personCode");
            }
        }

        return personCode;
    }

    public String login(){
//      接口地址
        String url = "http://syx.jslife.com.cn/jsaims/login";
//      客户号
        String cid = "880075500000001";
//      帐号
        String usr = "880075500000001";
//      密码
        String psw = "888888";

        String token = "";

        try {
//      构造参数
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            ArrayList<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("cid", cid));
            list.add(new BasicNameValuePair("usr", usr));
            list.add(new BasicNameValuePair("psw",psw));
            HttpEntity en = new UrlEncodedFormEntity(list, HTTP.UTF_8);
            post.setEntity(en);

//          发送消息和处理结果
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                System.out.println("执行成功！");
                String s = EntityUtils.toString(response.getEntity());
                System.out.println(s);
                JSONObject result = (JSONObject) JSONObject.parse(s);
                if("0".equals(result.getString("resultCode"))){
                    token = result.getString("token");
                }
            } else {
                System.out.println("执行失败！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return token;

    }


    public String post(String params){
//      接口地址
        String url = "http://syx.jslife.com.cn/jsaims/as";
//      客户号
        String cid = "880075500000001";
//      接口版本号
        String v = "2";
//      参数
//        String p = "{\"serviceId\":\"common.queryuserinfo\", \"userId\":\"00001\"}";
//      签名signKey
        String signKey = "7ac3e2ee1075bf4bb6b816c1e80126c0";

        String token = login();

        if(StringUtils.isBlank(token)){
            return "";
        }

//      生成MD5签名
        String sn = null;
        try {
            MessageDigest md5Tool = MessageDigest.getInstance("MD5");
            byte[] bytes = (params + signKey).getBytes("UTF-8");
            System.out.print("签名前序列:");
            for(byte b : bytes){
                System.out.print(Byte.toString(b));
            }
            System.out.println();
            byte[] md5Data = md5Tool.digest((params + signKey).getBytes("UTF-8"));
            sn = toHexString(md5Data);
            sn = StringUtils.upperCase(sn);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("签名:" + sn);

        String result = "";

        try {
//          构造参数
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("cid", cid));
            list.add(new BasicNameValuePair("v", v));
            list.add(new BasicNameValuePair("p", params));
            list.add(new BasicNameValuePair("sn", sn));
            list.add(new BasicNameValuePair("tn", token));// 取token
            HttpEntity en = new UrlEncodedFormEntity(list, HTTP.UTF_8);
            post.setEntity(en);

//          发送消息和处理结果
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                System.out.println("执行成功！");
                result = EntityUtils.toString(response.getEntity());
                System.out.println(result);
            } else {
                System.out.println("执行失败！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
//        VisitorSysDingFengHuiUtil bean = new VisitorSysDingFengHuiUtil();
//
//        String params = "{\"attributes\":{\"carNo\":\"粤-BMP525\",\"parkCode\":\"0010028888\"},\"requestType\":\"DATA\",\"serviceId\":\"3c.park.bookparkspace\"}";
//      人员编码查询
//        String params = "{\"attributes\":{\"areaCode\":\"0010028888\",\"telephone\":\"13823665629\"},\"requestType\":\"DATA\",\"serviceId\":\"3c.base.querypersonsbytel\"}";
//      3.17 发起邀访
//        String params = "{\"attributes\":{\"areaCode\":\"0010028888\",\"businesserCode\":\"880075500000001\",\"parkCode\":\"0010028888\",\"personCode\":\"SZ000098\",\"timeDesc\":\"{\\\"sd\\\":\\\"2018-11-30 00:00:00\\\",\\\"ed\\\":\\\"2018-11-30 23:59:59\\\",\\\"st\\\":\\\"15:00\\\",\\\"et\\\":\\\"17:30\\\",\\\"w\\\":\\\"0\\\"}\",\"visitorContent\":\"6666\",\"visitorType\":0},\"dataItems\":[{\"attributes\":{\"carNo\":\"粤-12345\",\"cardNo\":\"4343434\",\"visitorName\":\"张三\",\"visitorTel\":\"13800138000\"},\"objectId\":\"\",\"operateType\":\"READ\",\"subItems\":[]}],\"requestType\":\"DATA\",\"serviceId\":\"3c.visitor.invite\"}";
//        bean.post(params);
//        bean.login();
        System.out.println("" + 1 + 3L);
    }

}
