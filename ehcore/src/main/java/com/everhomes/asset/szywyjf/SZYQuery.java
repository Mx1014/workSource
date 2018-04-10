package com.everhomes.asset.szywyjf;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.asset.szywyjf.webservice.EASLogin.EASLoginProxy;
import com.everhomes.asset.szywyjf.webservice.EASLogin.EASLoginProxyServiceLocator;
import com.everhomes.asset.szywyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxy;
import com.everhomes.asset.szywyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxyServiceLocator;
import com.everhomes.asset.szywyjf.webservice.client.WSContext;
import com.everhomes.rest.asset.BillForClientV2;
import com.everhomes.rest.asset.ListAllBillsForClientDTO;
import com.everhomes.rest.asset.ShowBillDetailForClientDTO;
import com.everhomes.rest.asset.ShowBillDetailForClientResponse;
import com.everhomes.rest.asset.ShowBillForClientV2DTO;

public class SZYQuery {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SZYQuery.class);
	
	public SZYQuery() {
		
	}
	
	private	Boolean login() {
		Boolean isLogin = false;
		try {
			//通过WebService登录EAS
			EASLoginProxyServiceLocator loginLocator = new EASLoginProxyServiceLocator();
			EASLoginProxy loginProxy = loginLocator.getEASLogin();
			WSContext context = loginProxy.login("mybay", "mybay", "eas", "cs200", "l2", 2);
			if(context.getSessionId() != null) {
				LOGGER.info("深圳湾对接，WebService登陆成功，SessionID：" + context.getSessionId());
				isLogin = true;
			}else {
				LOGGER.error("深圳湾对接，WebService登陆失败！");
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return isLogin;
	}
	
	public List<ShowBillForClientV2DTO> showBillForClientV2(String request) {
		List<ShowBillForClientV2DTO> response = new ArrayList<ShowBillForClientV2DTO>();
		//通过WebService登录EAS
		SZYQuery szyQuery = new SZYQuery();
		if(szyQuery.login()) {
			try {
				WSWSSyncMyBayFacadeSrvProxyServiceLocator accountLocator = new WSWSSyncMyBayFacadeSrvProxyServiceLocator();
				WSWSSyncMyBayFacadeSrvProxy accountProxy = accountLocator.getWSWSSyncMyBayFacade();
				String result = accountProxy.sync_TenancyContractData(request);
				try {
					JSONArray jsonArray = JSON.parseArray(result);
					for(int i = 0;i < jsonArray.size();i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						ShowBillForClientV2DTO showBillForClientV2DTO = new ShowBillForClientV2DTO();
						showBillForClientV2DTO.setBillGroupName(jsonObject.get("tenCustomerDes") != null ? jsonObject.get("tenCustomerDes").toString() : null + "的全部账单");
						//showBillForClientV2DTO.setBillGroupId(jsonObject.get("number") != null ? jsonObject.get("number").toString() : null);
						showBillForClientV2DTO.setContractId(jsonObject.get("number") != null ? jsonObject.get("number").toString() : null);
						showBillForClientV2DTO.setContractNum(jsonObject.get("number") != null ? jsonObject.get("number").toString() : null);
						double overAllAmountOwed = 0;//待缴金额总计
						String addressStr = "";//包含的地址
						List<BillForClientV2> bills = new ArrayList<BillForClientV2>();
						JSONArray receDataJsonArray = (JSONArray) jsonObject.get("receData");
						for(int j = 0;j < receDataJsonArray.size();j++) {
							JSONObject receDataJSONObject = receDataJsonArray.getJSONObject(j);
							BillForClientV2 bill = new BillForClientV2();
							String billDuration = receDataJSONObject.get("startDate") + "至" + receDataJSONObject.get("endDate");  		
							bill.setBillDuration(billDuration);
							bill.setAmountReceivable(receDataJSONObject.get("fappamount") != null ? receDataJSONObject.get("fappamount").toString() : null);
							//应付金额
							double fappamount = receDataJSONObject.get("fappamount") != null ? Double.parseDouble(receDataJSONObject.get("fappamount").toString()): 0;
							//实付金额
							double factMount = receDataJSONObject.get("factMount") != null ? Double.parseDouble(receDataJSONObject.get("factMount").toString()): 0;
							//待缴金额
							double amountOwed = fappamount - factMount;
							bill.setAmountOwed(String.valueOf(amountOwed));
							bill.setBillId(receDataJSONObject.get("fid") != null ? receDataJSONObject.get("fid").toString() : null);
							
							overAllAmountOwed += amountOwed;//待缴金额总计
							addressStr += "," + receDataJSONObject.get("roomNo");//包含的地址
						}
						showBillForClientV2DTO.setBills(bills);
						showBillForClientV2DTO.setOverAllAmountOwed(String.valueOf(overAllAmountOwed));
						showBillForClientV2DTO.setAddressStr(addressStr);
						response.add(showBillForClientV2DTO);
					}
				} catch (JSONException e) {
					// TODO: handle exception
					JSONObject jsonObject = JSON.parseObject(result);
					LOGGER.error("深圳湾物业缴费对接请求数据失败，失败原因：" + jsonObject.get("Reason"));
				}
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return response;
	}
	
	public List<ListAllBillsForClientDTO> listAllBillsForClient(String request, Byte chargeStatus) {
		List<ListAllBillsForClientDTO> response = new ArrayList<ListAllBillsForClientDTO>();
		//通过WebService登录EAS
		SZYQuery szyQuery = new SZYQuery();
		if(szyQuery.login()) {
			try {
				WSWSSyncMyBayFacadeSrvProxyServiceLocator accountLocator = new WSWSSyncMyBayFacadeSrvProxyServiceLocator();
				WSWSSyncMyBayFacadeSrvProxy accountProxy = accountLocator.getWSWSSyncMyBayFacade();
				String result = accountProxy.sync_TenancyContractData(request);
				try {
					JSONArray jsonArray = JSON.parseArray(result);
					for(int i = 0;i < jsonArray.size();i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						JSONArray receDataJsonArray = (JSONArray) jsonObject.get("receData");
						for(int j = 0;j < receDataJsonArray.size();j++) {
							JSONObject receDataJSONObject = receDataJsonArray.getJSONObject(j);
							ListAllBillsForClientDTO listAllBillsForClientDTO = new ListAllBillsForClientDTO();
							listAllBillsForClientDTO.setBillGroupName(receDataJSONObject.get("fmoneyDefine") != null ? receDataJSONObject.get("fmoneyDefine").toString() : null);
							listAllBillsForClientDTO.setDateStrBegin(receDataJSONObject.get("startDate") != null ? receDataJSONObject.get("startDate").toString() : null);
							listAllBillsForClientDTO.setDateStrEnd(receDataJSONObject.get("endDate") != null ? receDataJSONObject.get("endDate").toString() : null);
							String dateStr = receDataJSONObject.get("startDate") + "~" + receDataJSONObject.get("endDate");
							listAllBillsForClientDTO.setDateStr(dateStr);
							listAllBillsForClientDTO.setAmountReceivable(receDataJSONObject.get("fappamount") != null ? receDataJSONObject.get("fappamount").toString() : null);
							//应付金额
							double fappamount = receDataJSONObject.get("fappamount") != null ? Double.parseDouble(receDataJSONObject.get("fappamount").toString()): 0;
							//实付金额
							double factMount = receDataJSONObject.get("factMount") != null ? Double.parseDouble(receDataJSONObject.get("factMount").toString()): 0;
							//待缴金额
							double amountOwed = fappamount - factMount;
							listAllBillsForClientDTO.setAmountOwed(String.valueOf(amountOwed));
							listAllBillsForClientDTO.setChargeStatus(chargeStatus);
							response.add(listAllBillsForClientDTO);
						}
					}
				} catch (JSONException e) {
					// TODO: handle exception
					JSONObject jsonObject = JSON.parseObject(result);
					LOGGER.error("深圳湾物业缴费对接请求数据失败，失败原因：" + jsonObject.get("Reason"));
				}
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return response;
	}
	
	public ShowBillDetailForClientResponse getBillDetailForClient(String request) {
		ShowBillDetailForClientResponse response = new ShowBillDetailForClientResponse();
		//通过WebService登录EAS
		SZYQuery szyQuery = new SZYQuery();
		if(szyQuery.login()) {
			try {
				WSWSSyncMyBayFacadeSrvProxyServiceLocator accountLocator = new WSWSSyncMyBayFacadeSrvProxyServiceLocator();
				WSWSSyncMyBayFacadeSrvProxy accountProxy = accountLocator.getWSWSSyncMyBayFacade();
				String result = accountProxy.sync_TenancyContractDetailed(request);
				try {
					JSONArray jsonArray = JSON.parseArray(result);
					JSONObject jsonObject = jsonArray.getJSONObject(0);
					JSONArray receDataJsonArray = (JSONArray) jsonObject.get("receData");
					JSONObject receDataJSONObject = receDataJsonArray.getJSONObject(0);
					//应付金额
					BigDecimal fappamount = (BigDecimal) receDataJSONObject.get("fappamount");
					response.setAmountReceivable(fappamount);
					//实付金额
					BigDecimal factMount = (BigDecimal) receDataJSONObject.get("fappamount");
					//待缴金额
					BigDecimal amountOwed = fappamount.subtract(factMount);
					response.setAmountOwed(amountOwed);
					String dateStr = receDataJSONObject.get("startDate") + "~" + receDataJSONObject.get("endDate");
					response.setDatestr(dateStr);
					List<ShowBillDetailForClientDTO> showBillDetailForClientDTOList = new ArrayList<ShowBillDetailForClientDTO>();
					ShowBillDetailForClientDTO showBillDetailForClientDTO = new ShowBillDetailForClientDTO();
					showBillDetailForClientDTO.setBillItemName("深圳湾物业缴费对接");
					showBillDetailForClientDTO.setAmountOwed(amountOwed);
					showBillDetailForClientDTO.setAddressName(receDataJSONObject.get("roomNo") != null ? receDataJSONObject.get("roomNo").toString() : null);
					showBillDetailForClientDTO.setAmountReceivable(fappamount);
					showBillDetailForClientDTO.setDateStrBegin(receDataJSONObject.get("startDate") != null ? receDataJSONObject.get("startDate").toString() : null);
					showBillDetailForClientDTO.setDateStrEnd(receDataJSONObject.get("endDate") != null ? receDataJSONObject.get("endDate").toString() : null);
					showBillDetailForClientDTO.setDateStr(dateStr);
					showBillDetailForClientDTOList.add(showBillDetailForClientDTO);
					response.setShowBillDetailForClientDTOList(showBillDetailForClientDTOList);
				} catch (JSONException e) {
					// TODO: handle exception
					JSONObject jsonObject = JSON.parseObject(result);
					LOGGER.error("深圳湾物业缴费对接请求数据失败，失败原因：" + jsonObject.get("Reason"));
				}
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return response;
	}
}
