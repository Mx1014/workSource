package com.everhomes.asset.szywyjf.webservice;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.everhomes.asset.szywyjf.webservice.EASLogin.EASLoginProxy;
import com.everhomes.asset.szywyjf.webservice.EASLogin.EASLoginProxyServiceLocator;
import com.everhomes.asset.szywyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxy;
import com.everhomes.asset.szywyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxyServiceLocator;
import com.everhomes.asset.szywyjf.webservice.client.WSContext;

public class WsTest {
	
	public static void main(String[] args) {
		try {
			//	通过WebService登录EAS
			EASLoginProxyServiceLocator loginLocator = new EASLoginProxyServiceLocator();
			EASLoginProxy loginProxy = loginLocator.getEASLogin();
			System.out.println("------ 开始登录服务器 .... ");
			WSContext context = loginProxy.login("mybay", "mybay", "eas", "cs200", "l2", 2);
			System.out.println("------ 登陆成功，SessionID：" + context.getSessionId());
			
			//	通过Kmye WevService获取信息
			/*WSKmyeFacadeSrvProxyServiceLocator accountLocator = new WSKmyeFacadeSrvProxyServiceLocator();
			WSKmyeFacadeSrvProxy accountProxy = accountLocator.getWSKmyeFacade();
			String result = accountProxy.getBasedata(null);
			System.out.println(result);
			result = accountProxy.getAccountBalance(null);
			System.out.println(result);*/
			
			
			WSWSSyncMyBayFacadeSrvProxyServiceLocator accountLocator = new WSWSSyncMyBayFacadeSrvProxyServiceLocator();
			WSWSSyncMyBayFacadeSrvProxy accountProxy = accountLocator.getWSWSSyncMyBayFacade();
			
			String result = accountProxy.sync_TenancyContractData("{" + 
					"	\"cusName\": \"122312\",\r\n" + 
					"	\"startDate\": \"2018-03-26\",\r\n" + 
					"	\"endDate\": \"2018-03-26\",\r\n" + 
					"	\"type\": \"0\",\r\n" + 
					"	\"state\": \"1\"\r\n" + 
					"}");
			System.out.println(result);
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
