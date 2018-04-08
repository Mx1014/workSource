package com.everhomes.asset.szywyjf;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.asset.szywyjf.webservice.EASLogin.EASLoginProxy;
import com.everhomes.asset.szywyjf.webservice.EASLogin.EASLoginProxyServiceLocator;
import com.everhomes.asset.szywyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxy;
import com.everhomes.asset.szywyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxyServiceLocator;
import com.everhomes.asset.szywyjf.webservice.client.WSContext;

public class SZYQuery {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SZYQuery.class);
	
	public SZYQuery() {
		
	}
	
	public String query(String request) {
		String result = null;
		try {
			//	通过WebService登录EAS
			EASLoginProxyServiceLocator loginLocator = new EASLoginProxyServiceLocator();
			EASLoginProxy loginProxy = loginLocator.getEASLogin();
			WSContext context = loginProxy.login("mybay", "mybay", "eas", "cs200", "l2", 2);
			if(context.getSessionId() != null) {
				LOGGER.info("深圳湾对接，WebService登陆成功，SessionID：" + context.getSessionId());
			}else {
				LOGGER.error("深圳湾对接，WebService登陆失败！");
			}
			WSWSSyncMyBayFacadeSrvProxyServiceLocator accountLocator = new WSWSSyncMyBayFacadeSrvProxyServiceLocator();
			WSWSSyncMyBayFacadeSrvProxy accountProxy = accountLocator.getWSWSSyncMyBayFacade();
			result = accountProxy.sync_TenancyContractData(request);
			
			
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return result;
	}
}
