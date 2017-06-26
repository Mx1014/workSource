// @formatter:off
package com.everhomes.print.demo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusOneshotSubscriber;
import com.everhomes.bus.LocalBusOneshotSubscriberBuilder;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.http.HttpUtils;
import com.everhomes.print.SiyinTaskLogScheduleJob;
import com.everhomes.rest.RestResponse;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.sequence.LocalSequenceGenerator;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.xml.XMLToJSON;

import sun.misc.BASE64Decoder;


@RestDoc(value="Print controller", site="print")
@RestController
@RequestMapping("/print")
public class PrintDemoController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrintDemoController.class);

    @Autowired
    private LocalBusOneshotSubscriberBuilder localBusSubscriberBuilder;

    @Autowired
    private LocalBus localBus;

    @Autowired
    private BigCollectionProvider bigCollectionProvider;

    private String siyinUrl = "http://siyin.zuolin.com:8119";

    @Autowired
    private ScheduleProvider scheduleProvider;

    @PostConstruct
    public void setup(){
        //启动定时任务
        scheduleProvider.scheduleCronJob("siyin", "siyin", "0 */10 * * * ?",SiyinTaskLogScheduleJob.class , null);
    }

    @RequestMapping("printLogon")
    @RestReturn(String.class)
    @RequireAuthentication(false)
    public DeferredResult<RestResponse> printLogon(@RequestParam(value="uid", required=true) int uid) {
        DeferredResult<RestResponse> deferredResult = new DeferredResult<>();
        RestResponse response =  new RestResponse();
        String subject = "print";
        localBusSubscriberBuilder.build(subject + "." + uid, new LocalBusOneshotSubscriber() {
            @Override
            public Action onLocalBusMessage(Object sender, String subject,
                                            Object logonResponse, String path) {

                //这里可以清掉redis的uid


                deferredResult.setResult((RestResponse)logonResponse);
                return null;
            }
            @Override
            public void onLocalBusListeningTimeout() {
                response.setResponseObject("print logon timed out");
                response.setErrorCode(408);
                deferredResult.setResult(response);
            }

        }).setTimeout(10000).create();

        return deferredResult;
    }

    @RequestMapping("informPrint")
    @RestReturn(String.class)
    @RequireAuthentication(false)
    public RestResponse informPrint(@RequestParam(value="uid", required=true) int uid){
        final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        String key = "print-uid" + uid;
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        RestResponse printResponse = new RestResponse();
        printResponse.setResponseObject("success");
        User user = UserContext.current().getUser();
        user.setId(10001l);
        if(null != valueOperations.get(key)){
            printResponse.setResponseObject(user);
            printResponse.setErrorCode(ErrorCodes.SUCCESS);
        }else{
            printResponse.setResponseObject("uid invalid");
            printResponse.setErrorCode(409);
        }

        String subject = "print";

        // 必须重启一个线程来发布通知
        ExecutorUtil.submit(new Runnable() {
            @Override
            public void run() {
                localBus.publish(null, subject + "." + uid, printResponse);
            }
        });

        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequestMapping("getPrintLogonUrl")
    @RestReturn(String.class)
    @RequireAuthentication(false)
    public RestResponse getPrintLogonUrl(){
        long uid = LocalSequenceGenerator.getNextSequence();
        final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        String key = "print-uid" + uid;
        String value = String.valueOf(uid);
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, 1, TimeUnit.MINUTES);
        String url = "http://printtest.zuolin.com/evh/print/informPrint?uid=" + uid;
        Map<String,Object> resObj = new HashMap<>();
        resObj.put("url", url);
        resObj.put("uid", uid);
        RestResponse response =  new RestResponse(resObj);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequestMapping("unlockPrint")
    @RestReturn(String.class)
    @RequireAuthentication(false)
    public RestResponse unlockPrint(@RequestParam(value="readerName", required=true) String readerName){
        RestResponse restResponse = new RestResponse();
        User user = UserContext.current().getUser();
        Long userId = 10001L;
//        if(null != user){
//            userId = user.getId();
//        }
        try{
            // 获取模块ip
            Map<String, String> params = new HashMap<>();
            params.put("reader_name", readerName);
            params.put("action", "QueryModule");
            String result = HttpUtils.post(siyinUrl + "/console/mfpModuleManager", params, 30);
            String siyinCode = getSiyinCode(result);
            if(!siyinCode.equals("OK")){
                restResponse.setResponseObject("siyin api:/console/mfpModuleManager result:" + result);
                restResponse.setErrorCode(ErrorCodes.SUCCESS);
                return restResponse;
            }
            String moduleIp = getSiyinData(result);

            //添加或者修改用户信息
            params = new HashMap<>();
            params.put("login_account", userId.toString());
            params.put("login_domain", "Sysprint_OAuth");
            params.put("language", "zh-cn");
            result = HttpUtils.post(siyinUrl + "/console/loginListener", params, 30);
        	result = result.replace("<email_address />", "<email_address>864313905@qq.com</email_address>");
        	result = result.replace("<group_name>___OAUTH___</group_name>", "<group_name>community_id</group_name>");
//        	result = result.replace("<scan_to_home_account />", "<scan_to_home_account>shuang.deng@zuolin.com</scan_to_home_account>");
//        	result = result.replace("<scan_to_home_pwd />", "<scan_to_home_pwd>dengs12345</scan_to_home_pwd>");
//        	result = result.replace("<mfp_direct_print>NO</mfp_direct_print>", "<mfp_direct_print>YES</mfp_direct_print>");
            	
            siyinCode = getSiyinCode(result);
            if(!siyinCode.equals("OK")){
                restResponse.setResponseObject("siyin api:/console/loginListener result:" + result);
                restResponse.setErrorCode(ErrorCodes.SUCCESS);
                return restResponse;
            }
            result = getSiyinData(result);

            String resultJson = XMLToJSON.convertStandardJson(result);
            LOGGER.info("siyin api:/console/loginListener resultJson:{}", resultJson);

            //机器是施乐的
            String modulePort = "8119";
            String context = "xeroxmfp";

            //登录
            params = new HashMap<>();
            params.put("reader_name", readerName);
            params.put("action", "QueryModule");
            params.put("login_data", result);
            result = HttpUtils.post("http://" + moduleIp  + ":" + modulePort + "/" + context + "/directLogin", params, 30);
            siyinCode = getSiyinCode(result);
            if(!siyinCode.equals("OK")){
                restResponse.setResponseObject("siyin api:/xeroxmfp/directLogin result:" + result);
                restResponse.setErrorCode(ErrorCodes.SUCCESS);
                return restResponse;
            }

        }catch (Exception e){
            LOGGER.error("解锁异常:{}", e);
            restResponse.setResponseObject("解锁异常");
            restResponse.setErrorCode(ErrorCodes.SUCCESS);
        }
        restResponse.setResponseObject("解锁打印机完成");
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        return restResponse;
    }

    @RequestMapping("jobLogNotification")
    @RestReturn(String.class)
    @RequireAuthentication(false)
    public RestResponse callbackTaskLog(@RequestParam(value="jobData", required=true) String jobData){
        RestResponse restResponse = new RestResponse();
        try{
            LOGGER.info("siyin params request:{}", jobData);
            BASE64Decoder decoder = new BASE64Decoder();
            jobData = new String(decoder.decodeBuffer(jobData));
            jobData = XMLToJSON.convertStandardJson(jobData);
            LOGGER.info("task log json:{}", jobData);
        }catch (Exception e){

        }

        return restResponse;
    }
    
    @RequestMapping("jobLogNotification/jobLogNotification")
    @RestReturn(String.class)
    @RequireAuthentication(false)
    public RestResponse jobLogNotification(@RequestParam(value="jobData", required=true) String jobData){
        RestResponse restResponse = new RestResponse();
        try{
            LOGGER.info("siyin params request:{}", jobData);
            BASE64Decoder decoder = new BASE64Decoder();
            String decodeJobData = "";
            decodeJobData = new String(decoder.decodeBuffer(jobData));
            decodeJobData = XMLToJSON.convertStandardJson(decodeJobData);
            Map<String, Object> object = XMLToJSON.convertOriginalMap(new String(decoder.decodeBuffer(jobData)));
            Map data = (Map)object.get("data");
            Object job = (Map)data.get("job");
            LOGGER.info("task log json:{}", jobData);
        }catch (Exception e){
        	e.printStackTrace();
        }

        return restResponse;
    }
    
    @RequestMapping("mfpLogNotification")
    @RestReturn(String.class)
    @RequireAuthentication(false)
    public RestResponse mfpLogNotification(@RequestParam(value="jobData", required=true) String jobData){
        RestResponse restResponse = new RestResponse();
        try{
            LOGGER.info("siyin mfpLogNotification request:{}", jobData);
            BASE64Decoder decoder = new BASE64Decoder();
            jobData = new String(decoder.decodeBuffer(jobData));
            jobData = XMLToJSON.convertStandardJson(jobData);
            LOGGER.info("task log json:{}", jobData);
        }catch (Exception e){

        }

        return restResponse;
    }
    
    @RequestMapping("externalLogin")
//    @RestReturn(String.class)
    @RequireAuthentication(false)
    public String externalLogin(@RequestParam(value="loginAccount", required=true) String loginAccount,
    		@RequestParam(value="loginPassword", required=true) String loginPassword,
    		@RequestParam(value="loginDomain", required=true) String loginDomain,
    		@RequestParam(value="loginCard", required=true) String loginCard,
    		@RequestParam(value="serialNumber", required=true) String serialNumber){
        StringBuffer buffer = new StringBuffer();
        LOGGER.info("siyin externalLogin request:loginAccount = {},loginPassword = {},loginDomain = {},loginCard = {},serialNumber = {}", 
        		loginAccount,loginPassword,loginDomain,loginCard,serialNumber);
        buffer.append("OK:<xml>")
        		.append("<user_name>10002</user_name>")
        		.append("<card_id>").append(loginCard).append("</card_id>")
        		.append("<group_name></group_name>")
        		.append("<email_address>shuang.deng@zuolin.com</email_address>")
        		.append("<feature_list>")
        		.append("<allow_print>Y</allow_print>")
        		.append("<allow_copy>Y</allow_copy>")
        		.append("<allow_color>Y</allow_color>")
        		.append("<allow_scan>Y</allow_scan>")
        		.append("<allow_fax>Y</allow_fax>")
        		.append("</feature_list>")
        		.append("</xml>");
//        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
//        LOGGER.info(encoder.encodeBuffer(buffer.toString().getBytes()));
        return  Base64.encodeBase64String(buffer.toString().getBytes());
    }


    public String getSiyinCode(String result){
        return result.substring(0, result.indexOf(":"));
    }

    public String getSiyinData(String result){
        return result.substring(result.indexOf(":") + 1);
    }

    public static void main(String[] args) {
    	new PrintDemoController().jobLogNotification("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPGRhdGE+CiAgPGpvYj4KICAgIDxqb2JfaWQ+MjAxNzA2MjItNzYzNC01NGQzZDBhOC00ZjY1LTRmMTgtYmUyNy0yYmZlNGJmY2RkMmQ8L2pvYl9pZD4KICAgIDxqb2Jfc3RhdHVzPkZpbmlzaEpvYjwvam9iX3N0YXR1cz4KICAgIDxmaW5hbF9yZXN1bHQ+MTwvZmluYWxfcmVzdWx0PgogICAgPGdyb3VwX25hbWU+X19fT0FVVEhfX188L2dyb3VwX25hbWU+CiAgICA8dXNlcl9uYW1lPjEwMDAxPC91c2VyX25hbWU+CiAgICA8dXNlcl9kaXNwbGF5X25hbWUgLz4KICAgIDxjbGllbnRfaXAgLz4KICAgIDxjbGllbnRfbmFtZSAvPgogICAgPGNsaWVudF9tYWMgLz4KICAgIDxkcml2ZXJfbmFtZSAvPgogICAgPGpvYl90eXBlPlBSSU5UPC9qb2JfdHlwZT4KICAgIDxqb2JfaW5fdGltZT4yMDE3LTA2LTIyIDE1OjMyOjA4PC9qb2JfaW5fdGltZT4KICAgIDxqb2Jfb3V0X3RpbWU+MjAxNy0wNi0yMiAxNTozMjoxNzwvam9iX291dF90aW1lPgogICAgPGRvY3VtZW50X25hbWU+Q1VzZXJzQWRtaW5pc3RyYXRvckRlc2t0b3DmiZPljbDmtYvor5XmlofmoaMuZG9jeDwvZG9jdW1lbnRfbmFtZT4KICAgIDxsZXZlbF9uYW1lPuWFrOW8gDwvbGV2ZWxfbmFtZT4KICAgIDxwcm9qZWN0X25hbWUgLz4KICAgIDxwcmludGVyX25hbWU+RlgtQXBlb3NQb3J0LVZJIEMzMzcwPC9wcmludGVyX25hbWU+CiAgICA8Y29sbGF0ZT4wPC9jb2xsYXRlPgogICAgPHBhcGVyX3NpemU+QTQ8L3BhcGVyX3NpemU+CiAgICA8cGFwZXJfaGVpZ2h0PjA8L3BhcGVyX2hlaWdodD4KICAgIDxwYXBlcl93aWR0aD4wPC9wYXBlcl93aWR0aD4KICAgIDxkdXBsZXg+MTwvZHVwbGV4PgogICAgPGNvcHlfY291bnQ+MTwvY29weV9jb3VudD4KICAgIDxzdXJmYWNlX2NvdW50PjI8L3N1cmZhY2VfY291bnQ+CiAgICA8Y29sb3Jfc3VyZmFjZV9jb3VudD4wPC9jb2xvcl9zdXJmYWNlX2NvdW50PgogICAgPG1vbm9fc3VyZmFjZV9jb3VudD4yPC9tb25vX3N1cmZhY2VfY291bnQ+CiAgICA8cGFnZV9jb3VudD4yPC9wYWdlX2NvdW50PgogICAgPGNvbG9yX3BhZ2VfY291bnQ+MDwvY29sb3JfcGFnZV9jb3VudD4KICAgIDxtb25vX3BhZ2VfY291bnQ+MjwvbW9ub19wYWdlX2NvdW50PgogICAgPHRvdGFsX2Nvc3Q+MC4yPC90b3RhbF9jb3N0PgogICAgPGNvbG9yX2Nvc3Q+MC4wPC9jb2xvcl9jb3N0PgogICAgPG1vbm9fY29zdD4wLjI8L21vbm9fY29zdD4KICA8L2pvYj4KPC9kYXRhPgo=");
    
        String result = "OK:asf";
        System.out.println(result = result.substring(0, result.indexOf(":")));
        
        StringBuffer buffer = new StringBuffer();
        buffer.append("OK:<xml>")
        		.append("<user_name>10001</user_name>")
        		.append("<card_id></card_id>")
        		.append("<group_name>zuolin</group_name>")
        		.append("<email_address>shuang.deng@zuolin.com</email_address>")
        		.append("<feature_list>")
        		.append("<allow_print>Y</allow_print>")
        		.append("<allow_copy>Y</allow_copy>")
        		.append("<allow_color>Y</allow_color>")
        		.append("<allow_scan>Y</allow_scan>")
        		.append("<allow_fax>Y</allow_fax>")
        		.append("</feature_list>")
        		.append("</xml>");
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        String encorderString =  encoder.encodeBuffer(buffer.toString().getBytes());
        System.out.println(encorderString);
        BASE64Decoder decoder = new BASE64Decoder();
        try {
			byte[] bytes = decoder.decodeBuffer(encorderString);
			System.out.println(String.valueOf(bytes));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}
