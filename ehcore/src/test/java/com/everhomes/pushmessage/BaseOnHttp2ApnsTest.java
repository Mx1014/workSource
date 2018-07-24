package com.everhomes.pushmessage;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import com.everhomes.apnshttp2.builder.ApnsClient;
import com.everhomes.apnshttp2.builder.impl.ApnsClientBuilder;
import com.everhomes.apnshttp2.notifiction.Notification;
import com.everhomes.apnshttp2.notifiction.NotificationResponse;

/**
 * 基于 http/2实现 后端与 APNs 连接的 service类(用于测试连接)
 * @author huanglm(motto)
 *
 */
public class BaseOnHttp2ApnsTest {
	
	
	private static final  String AUTHKEY =
										  "MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgexxG8QrtsI2+Xuwf           "+
										  "75baYuZBQYBDzSBfEPijhC2GOmagCgYIKoZIzj0DAQehRANCAAT4qnM82JvBxnJ/        "+
										  "R3ESPFgylVgU4st8QlQdf1QEYQtU3lVNStrg8W6DcLvYyL/7I/Tc0HFXm+8YQijz        "+
										  "7ayWDOp7  ";
										 
	//建立连接及推送所需的参数
	//static final String HostDevelopment_url = "https://api.development.push.apple.com";
	//static final String HostProduction_url = "https://api.push.apple.com";
	//static final String deviceToken = "3c3e31318ee86003a371bea84a1f3fa7ea855d12d88a54aba8cda22e06548340";//设备
	//static final String deviceToken = "633a3f1c3777415071785884add2a9a30572d266faaa35aee5226bfb449e10ab";//设备
	static final String deviceToken = "1add69b3f502e607d27e5cec7a5b65f8e266937480d5dc620b113286d30008e2";//设备
	
	//开发者帐号信息
	static final String authKey = AUTHKEY;//token证书
	static final String authKeyId ="Q77V8W78L2";//ntoken id
	static final String teamId = "4H44DAN2YD";//关联开发者账号
	
	static final String bundleId = "com.techpark.ios.zuolin"; //关联应用
	static final String endpoint = "https://api.push.apple.com";//推送服务器，关联应用provisionFile类型
  	   
    public static void main(String[] args){
    	connectest();
    }
    /**
     * 测试连接
     */
    public static void connectest(){    	
		try {

		    ApnsClient client;

		    client = new ApnsClientBuilder()
	        .inSynchronousMode()
	        .withProductionGateway(true)
	        .withApnsAuthKey(AUTHKEY)
	        .withTeamID(teamId)
	        .withKeyID(authKeyId)
	        .withDefaultTopic(bundleId)
	        .build();
		    
		    Notification n = new Notification.Builder(deviceToken)
            .alertBody("我在测试http2-APNs 连接，请忽略！").build();
		    NotificationResponse result = client.push(n);
		    System.out.println(result);
    
		} catch (UnrecoverableKeyException e) {
			
			e.printStackTrace();
		} catch (KeyManagementException e) {
			
			e.printStackTrace();
		} catch (CertificateException e) {
			
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		} catch (KeyStoreException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

       
    }

}
