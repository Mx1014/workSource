package com.everhomes.asset.szwwyjf;

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
import com.everhomes.asset.szwwyjf.webservice.EASLogin.EASLoginProxy;
import com.everhomes.asset.szwwyjf.webservice.EASLogin.EASLoginProxyServiceLocator;
import com.everhomes.asset.szwwyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxy;
import com.everhomes.asset.szwwyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxyServiceLocator;
import com.everhomes.asset.szwwyjf.webservice.client.WSContext;
import com.everhomes.rest.asset.BillForClientV2;
import com.everhomes.rest.asset.ListAllBillsForClientDTO;
import com.everhomes.rest.asset.ShowBillDetailForClientDTO;
import com.everhomes.rest.asset.ShowBillDetailForClientResponse;
import com.everhomes.rest.asset.ShowBillForClientV2DTO;
import com.everhomes.util.StringHelper;

public class SZWQuery {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SZWQuery.class);
	
	public SZWQuery() {
		
	}
	
	private	Boolean login() {
		Boolean isLogin = false;
		try {
			//通过WebService登录EAS
			EASLoginProxyServiceLocator loginLocator = new EASLoginProxyServiceLocator();
			EASLoginProxy loginProxy = loginLocator.getEASLogin();
			WSContext context = loginProxy.login("mybay", "mybay", "eas", "cs200", "l2", 2);
			if(context.getSessionId() != null) {
				LOGGER.info("szw WebService login success，SessionID: " + context.getSessionId());
				isLogin = true;
			}else {
				LOGGER.error("szw webservice login fail");
			}
		} catch (Exception e) {
			LOGGER.error("szw webservice login fail");
		} 
		return isLogin;
	}
	
	public List<ShowBillForClientV2DTO> showBillForClientV2(String request) {
		List<ShowBillForClientV2DTO> response = new ArrayList<ShowBillForClientV2DTO>();
		//通过WebService登录EAS
		SZWQuery szyQuery = new SZWQuery();
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
						showBillForClientV2DTO.setBillGroupName((jsonObject.get("tenCustomerDes") != null ? jsonObject.get("tenCustomerDes").toString() : null) + "的全部账单");
						//showBillForClientV2DTO.setBillGroupId(jsonObject.get("number") != null ? jsonObject.get("number").toString() : null);
						showBillForClientV2DTO.setContractId(jsonObject.get("number") != null ? jsonObject.get("number").toString() : null);
						showBillForClientV2DTO.setContractNum(jsonObject.get("number") != null ? jsonObject.get("number").toString() : null);
						BigDecimal overAllAmountOwed = BigDecimal.ZERO;//待缴金额总计
						String addressStr = "";//包含的地址
						List<BillForClientV2> bills = new ArrayList<BillForClientV2>();
						JSONArray receDataJsonArray = (JSONArray) jsonObject.get("receData");
						for(int j = 0;j < receDataJsonArray.size();j++) {
							JSONObject receDataJSONObject = receDataJsonArray.getJSONObject(j);
							BillForClientV2 bill = new BillForClientV2();
							String billDuration = receDataJSONObject.get("startDate") + "~" + receDataJSONObject.get("endDate");  		
							//格式化账期
							billDuration = billDuration.replace(" 00:00:00", "");
							bill.setBillDuration(billDuration);
							bill.setAmountReceivable(receDataJSONObject.get("fappamount") != null ? receDataJSONObject.get("fappamount").toString() : null);
							//应付金额
							BigDecimal fappamount = new BigDecimal(receDataJSONObject.get("fappamount") != null ? Double.parseDouble(receDataJSONObject.get("fappamount").toString()): 0);
							//实付金额
							BigDecimal factMount = new BigDecimal(receDataJSONObject.get("factMount") != null ? Double.parseDouble(receDataJSONObject.get("factMount").toString()): 0);
							//待缴金额
							BigDecimal amountOwed = fappamount.subtract(factMount).setScale(2,BigDecimal.ROUND_HALF_DOWN);
							bill.setAmountOwed(amountOwed.toString());
							bill.setBillId(receDataJSONObject.get("fid") != null ? receDataJSONObject.get("fid").toString() : null);
							bills.add(bill);
							overAllAmountOwed = overAllAmountOwed.add(amountOwed);//待缴金额总计
							//去除重复地址
							if(!addressStr.contains(receDataJSONObject.get("roomNo") != null ? receDataJSONObject.get("roomNo").toString() : "")){
								addressStr += receDataJSONObject.get("roomNo") + "," ;//包含的地址
							}
						}
						showBillForClientV2DTO.setBills(bills);
						showBillForClientV2DTO.setOverAllAmountOwed(String.valueOf(overAllAmountOwed));
						addressStr = addressStr.substring(0, addressStr.length() - 1);
						showBillForClientV2DTO.setAddressStr(addressStr);
						response.add(showBillForClientV2DTO);
					}
				} catch (JSONException e) {
					LOGGER.error("Failed to parse json response, result={}", result, e);
				}
			} catch (Exception e) {
				LOGGER.error("Failed to call szw api, request={}", request, e);
			}
		}
		return response;
	}
	
	public List<ListAllBillsForClientDTO> listAllBillsForClient(String request, Byte chargeStatus) {
		List<ListAllBillsForClientDTO> response = new ArrayList<ListAllBillsForClientDTO>();
		//通过WebService登录EAS
		SZWQuery szyQuery = new SZWQuery();
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
							listAllBillsForClientDTO.setDateStrBegin(receDataJSONObject.get("startDate") != null ? receDataJSONObject.get("startDate").toString().replace(" 00:00:00", ""): null);
							listAllBillsForClientDTO.setDateStrEnd(receDataJSONObject.get("endDate") != null ? receDataJSONObject.get("endDate").toString().replace(" 00:00:00", "") : null);
							String dateStr = receDataJSONObject.get("startDate") + "~" + receDataJSONObject.get("endDate");
							listAllBillsForClientDTO.setDateStr(dateStr.replace(" 00:00:00", ""));
							listAllBillsForClientDTO.setAmountReceivable(receDataJSONObject.get("fappamount") != null ? receDataJSONObject.get("fappamount").toString() : null);
							//应付金额
							BigDecimal fappamount = new BigDecimal(receDataJSONObject.get("fappamount") != null ? Double.parseDouble(receDataJSONObject.get("fappamount").toString()): 0);
							//实付金额
							BigDecimal factMount = new BigDecimal(receDataJSONObject.get("factMount") != null ? Double.parseDouble(receDataJSONObject.get("factMount").toString()): 0);
							//待缴金额
							BigDecimal amountOwed = fappamount.subtract(factMount).setScale(2,BigDecimal.ROUND_HALF_DOWN);
							listAllBillsForClientDTO.setAmountOwed(amountOwed.toString());
							listAllBillsForClientDTO.setChargeStatus(chargeStatus);
							response.add(listAllBillsForClientDTO);
						}
					}
				} catch (JSONException e) {
					LOGGER.error("Failed to parse json response, result={}", result, e);
				}
			} catch (Exception e) {
				LOGGER.error("Failed to call szw api, request={}", request, e);
			}
		}
		return response;
	}
	
	public ShowBillDetailForClientResponse getBillDetailForClient(String request) {
		ShowBillDetailForClientResponse response = new ShowBillDetailForClientResponse();
		//通过WebService登录EAS
		SZWQuery szyQuery = new SZWQuery();
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
					BigDecimal fappamount = new BigDecimal(receDataJSONObject.get("fappamount").toString());
					response.setAmountReceivable(fappamount);
					//实付金额
					BigDecimal factMount = new BigDecimal(receDataJSONObject.get("factMount").toString());
					//待缴金额
					BigDecimal amountOwed = fappamount.subtract(factMount).setScale(2,BigDecimal.ROUND_HALF_DOWN);
					response.setAmountOwed(amountOwed);
					String dateStr = receDataJSONObject.get("startDate") + "~" + receDataJSONObject.get("endDate");
					//格式化日期
					dateStr = dateStr.replace(" 00:00:00", "");
					response.setDatestr(dateStr);
					List<ShowBillDetailForClientDTO> showBillDetailForClientDTOList = new ArrayList<ShowBillDetailForClientDTO>();
					ShowBillDetailForClientDTO showBillDetailForClientDTO = new ShowBillDetailForClientDTO();
					showBillDetailForClientDTO.setBillItemName("深圳湾物业缴费对接");
					showBillDetailForClientDTO.setAmountOwed(amountOwed);
					showBillDetailForClientDTO.setAddressName(receDataJSONObject.get("roomNo") != null ? receDataJSONObject.get("roomNo").toString() : null);
					showBillDetailForClientDTO.setAmountReceivable(fappamount);
					showBillDetailForClientDTO.setDateStrBegin(receDataJSONObject.get("startDate") != null ? receDataJSONObject.get("startDate").toString().replace(" 00:00:00", "") : null);
					showBillDetailForClientDTO.setDateStrEnd(receDataJSONObject.get("endDate") != null ? receDataJSONObject.get("endDate").toString().replace(" 00:00:00", "") : null);
					showBillDetailForClientDTO.setDateStr(dateStr);
					showBillDetailForClientDTOList.add(showBillDetailForClientDTO);
					response.setShowBillDetailForClientDTOList(showBillDetailForClientDTOList);
				} catch (JSONException e) {
					LOGGER.error("Failed to parse json response, result={}", result, e);
				}
			} catch (Exception e) {
				LOGGER.error("Failed to call szw api, request={}", request, e);
			}
		}
		return response;
	}
}
