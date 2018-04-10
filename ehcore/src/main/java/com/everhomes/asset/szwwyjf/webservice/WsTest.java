package com.everhomes.asset.szwwyjf.webservice;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.asset.AssetPaymentStrings;
import com.everhomes.asset.szwwyjf.webservice.EASLogin.EASLoginProxy;
import com.everhomes.asset.szwwyjf.webservice.EASLogin.EASLoginProxyServiceLocator;
import com.everhomes.asset.szwwyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxy;
import com.everhomes.asset.szwwyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxyServiceLocator;
import com.everhomes.asset.szwwyjf.webservice.client.WSContext;

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
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("cusName", "深圳市石榴裙餐饮有限公司");
			//由于查询的是全部，金蝶对接需要时间段，所以设置一个足够大的时间范围
			jsonObject.put("startDate", "1900-01-01");
			jsonObject.put("endDate", "2900-01-01");
			//判断是企业客户，还是个人(0：企业客户，1：个人（判断）
			jsonObject.put("type", "0");
			//查看全部账单 (1：已缴，0：未缴，2：全部)
			jsonObject.put("state", "2");
			String result = accountProxy.sync_TenancyContractData(jsonObject.toString());
			JSONObject resultJSONObject = JSON.parseObject(result);
			System.out.println(resultJSONObject.get("reason"));
			
			
			
			System.out.println(result);
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
