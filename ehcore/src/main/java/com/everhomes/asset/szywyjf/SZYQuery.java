package com.everhomes.asset.szywyjf;

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
import com.everhomes.rest.asset.ListAllBillsForClientDTO;
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
				/*try {
					JSONArray jsonArray = JSON.parseArray(result);
					for(int i = 0;i < jsonArray.size();i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						ShowBillForClientSZYDTO showBillForClientSZYDTO = new ShowBillForClientSZYDTO();
						showBillForClientSZYDTO.setNumber(jsonObject.get("number") != null ? jsonObject.get("number").toString() : null);
						showBillForClientSZYDTO.setTenancyName(jsonObject.get("tenancyName") != null ? jsonObject.get("tenancyName").toString() : null);
						showBillForClientSZYDTO.setTenCustomerDes(jsonObject.get("tenCustomerDes") != null ? jsonObject.get("tenCustomerDes").toString() : null);
						showBillForClientSZYDTO.setTenancyType(jsonObject.get("tenancyType") != null ? jsonObject.get("tenancyType").toString() : null);
						showBillForClientSZYDTO.setOldTenancyBill(jsonObject.get("oldTenancyBill") != null ? jsonObject.get("oldTenancyBill").toString() : null);
						showBillForClientSZYDTO.setStartDate(jsonObject.get("startDate") != null ? jsonObject.get("startDate").toString() : null);
						showBillForClientSZYDTO.setEndDate(jsonObject.get("endDate") != null ? jsonObject.get("endDate").toString() : null);
						showBillForClientSZYDTO.setTenancyDate(jsonObject.get("tenancyDate") != null ? jsonObject.get("tenancyDate").toString() : null);
						showBillForClientSZYDTO.setLeaseCount(jsonObject.get("leaseCount") != null ? jsonObject.get("leaseCount").toString() : null);
						showBillForClientSZYDTO.setRoomTotalRent(jsonObject.get("roomTotalRent") != null ? jsonObject.get("roomTotalRent").toString() : null);
						showBillForClientSZYDTO.setDepositAmount(jsonObject.get("depositAmount") != null ? jsonObject.get("depositAmount").toString() : null);
						showBillForClientSZYDTO.setFreeDays(jsonObject.get("freeDays") != null ? jsonObject.get("freeDays").toString() : null);
						showBillForClientSZYDTO.setFirstPayRent(jsonObject.get("firstPayRent") != null ? jsonObject.get("firstPayRent").toString() : null);
						showBillForClientSZYDTO.setFirstLeaseEndDate(jsonObject.get("firstLeaseEndDate") != null ? jsonObject.get("firstLeaseEndDate").toString() : null);
						showBillForClientSZYDTO.setRentCountType(jsonObject.get("rentCountType") != null ? jsonObject.get("rentCountType").toString() : null);
						showBillForClientSZYDTO.setTenancyState(jsonObject.get("tenancyState") != null ? jsonObject.get("tenancyState").toString() : null);
						showBillForClientSZYDTO.setTenancyAdviser(jsonObject.get("tenancyAdviser") != null ? jsonObject.get("tenancyAdviser").toString() : null);
						showBillForClientSZYDTO.setDescription(jsonObject.get("description") != null ? jsonObject.get("description").toString() : null);
						showBillForClientSZYDTO.setDealTotalRent(jsonObject.get("dealTotalRent") != null ? jsonObject.get("dealTotalRent").toString() : null);
						showBillForClientSZYDTO.setQuitRoomDate(jsonObject.get("quitRoomDate") != null ? jsonObject.get("quitRoomDate").toString() : null);
						List<BillForClientSZY> bills = new ArrayList<BillForClientSZY>();
						JSONArray receDataJsonArray = (JSONArray) jsonObject.get("receData");
						for(int j = 0;j < receDataJsonArray.size();j++) {
							JSONObject receDataJSONObject = receDataJsonArray.getJSONObject(j);
							BillForClientSZY bill = new BillForClientSZY();
							bill.setStartDate(receDataJSONObject.get("startDate") != null ? receDataJSONObject.get("startDate").toString() : null);
							bill.setEndDate(receDataJSONObject.get("endDate") != null ? receDataJSONObject.get("endDate").toString() : null);
							bill.setFappamount(receDataJSONObject.get("fappamount") != null ? receDataJSONObject.get("fappamount").toString() : null);
							bill.setFactMount(receDataJSONObject.get("factMount") != null ? receDataJSONObject.get("factMount").toString() : null);
							bill.setFmoneyDefine(receDataJSONObject.get("fmoneyDefine") != null ? receDataJSONObject.get("fmoneyDefine").toString() : null);
							bill.setFid(receDataJSONObject.get("fid") != null ? receDataJSONObject.get("fid").toString() : null);
							bill.setRoomNo(receDataJSONObject.get("roomNo") != null ? receDataJSONObject.get("roomNo").toString() : null);
							bills.add(bill);
						}
						showBillForClientSZYDTO.setBills(bills);
						response.add(showBillForClientSZYDTO);
					}
				} catch (JSONException e) {
					// TODO: handle exception
					JSONObject jsonObject = JSON.parseObject(result);
					LOGGER.error("深圳湾物业缴费对接请求数据失败，失败原因：" + jsonObject.get("Reason"));
				}*/
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return response;
	}
	
	public List<ListAllBillsForClientDTO> listAllBillsForClient(String request) {
		List<ListAllBillsForClientDTO> response = new ArrayList<ListAllBillsForClientDTO>();
		//通过WebService登录EAS
		SZYQuery szyQuery = new SZYQuery();
		if(szyQuery.login()) {
			try {
				WSWSSyncMyBayFacadeSrvProxyServiceLocator accountLocator = new WSWSSyncMyBayFacadeSrvProxyServiceLocator();
				WSWSSyncMyBayFacadeSrvProxy accountProxy = accountLocator.getWSWSSyncMyBayFacade();
				String result = accountProxy.sync_TenancyContractData(request);
				/*try {
					JSONArray jsonArray = JSON.parseArray(result);
					for(int i = 0;i < jsonArray.size();i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						ListAllBillsForClientSZYDTO listAllBillsForClientSZYDTO = new ListAllBillsForClientSZYDTO();
						listAllBillsForClientSZYDTO.setNumber(jsonObject.get("number") != null ? jsonObject.get("number").toString() : null);
						listAllBillsForClientSZYDTO.setTenancyName(jsonObject.get("tenancyName") != null ? jsonObject.get("tenancyName").toString() : null);
						listAllBillsForClientSZYDTO.setTenCustomerDes(jsonObject.get("tenCustomerDes") != null ? jsonObject.get("tenCustomerDes").toString() : null);
						listAllBillsForClientSZYDTO.setTenancyType(jsonObject.get("tenancyType") != null ? jsonObject.get("tenancyType").toString() : null);
						listAllBillsForClientSZYDTO.setOldTenancyBill(jsonObject.get("oldTenancyBill") != null ? jsonObject.get("oldTenancyBill").toString() : null);
						listAllBillsForClientSZYDTO.setStartDate(jsonObject.get("startDate") != null ? jsonObject.get("startDate").toString() : null);
						listAllBillsForClientSZYDTO.setEndDate(jsonObject.get("endDate") != null ? jsonObject.get("endDate").toString() : null);
						listAllBillsForClientSZYDTO.setTenancyDate(jsonObject.get("tenancyDate") != null ? jsonObject.get("tenancyDate").toString() : null);
						listAllBillsForClientSZYDTO.setLeaseCount(jsonObject.get("leaseCount") != null ? jsonObject.get("leaseCount").toString() : null);
						listAllBillsForClientSZYDTO.setRoomTotalRent(jsonObject.get("roomTotalRent") != null ? jsonObject.get("roomTotalRent").toString() : null);
						listAllBillsForClientSZYDTO.setDepositAmount(jsonObject.get("depositAmount") != null ? jsonObject.get("depositAmount").toString() : null);
						listAllBillsForClientSZYDTO.setFreeDays(jsonObject.get("freeDays") != null ? jsonObject.get("freeDays").toString() : null);
						listAllBillsForClientSZYDTO.setFirstPayRent(jsonObject.get("firstPayRent") != null ? jsonObject.get("firstPayRent").toString() : null);
						listAllBillsForClientSZYDTO.setFirstLeaseEndDate(jsonObject.get("firstLeaseEndDate") != null ? jsonObject.get("firstLeaseEndDate").toString() : null);
						listAllBillsForClientSZYDTO.setRentCountType(jsonObject.get("rentCountType") != null ? jsonObject.get("rentCountType").toString() : null);
						listAllBillsForClientSZYDTO.setTenancyState(jsonObject.get("tenancyState") != null ? jsonObject.get("tenancyState").toString() : null);
						listAllBillsForClientSZYDTO.setTenancyAdviser(jsonObject.get("tenancyAdviser") != null ? jsonObject.get("tenancyAdviser").toString() : null);
						listAllBillsForClientSZYDTO.setDescription(jsonObject.get("description") != null ? jsonObject.get("description").toString() : null);
						listAllBillsForClientSZYDTO.setDealTotalRent(jsonObject.get("dealTotalRent") != null ? jsonObject.get("dealTotalRent").toString() : null);
						listAllBillsForClientSZYDTO.setQuitRoomDate(jsonObject.get("quitRoomDate") != null ? jsonObject.get("quitRoomDate").toString() : null);
						List<BillForClientSZY> bills = new ArrayList<BillForClientSZY>();
						JSONArray receDataJsonArray = (JSONArray) jsonObject.get("receData");
						for(int j = 0;j < receDataJsonArray.size();j++) {
							JSONObject receDataJSONObject = receDataJsonArray.getJSONObject(j);
							BillForClientSZY bill = new BillForClientSZY();
							bill.setStartDate(receDataJSONObject.get("startDate") != null ? receDataJSONObject.get("startDate").toString() : null);
							bill.setEndDate(receDataJSONObject.get("endDate") != null ? receDataJSONObject.get("endDate").toString() : null);
							bill.setFappamount(receDataJSONObject.get("fappamount") != null ? receDataJSONObject.get("fappamount").toString() : null);
							bill.setFactMount(receDataJSONObject.get("factMount") != null ? receDataJSONObject.get("factMount").toString() : null);
							bill.setFmoneyDefine(receDataJSONObject.get("fmoneyDefine") != null ? receDataJSONObject.get("fmoneyDefine").toString() : null);
							bill.setFid(receDataJSONObject.get("fid") != null ? receDataJSONObject.get("fid").toString() : null);
							bill.setRoomNo(receDataJSONObject.get("roomNo") != null ? receDataJSONObject.get("roomNo").toString() : null);
							bills.add(bill);
						}
						listAllBillsForClientSZYDTO.setBills(bills);
						response.add(listAllBillsForClientSZYDTO);
					}
				} catch (JSONException e) {
					// TODO: handle exception
					JSONObject jsonObject = JSON.parseObject(result);
					LOGGER.error("深圳湾物业缴费对接请求数据失败，失败原因：" + jsonObject.get("Reason"));
				}*/
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
				String result = accountProxy.sync_TenancyContractData(request);
				/*try {
					JSONArray jsonArray = JSON.parseArray(result);
					for(int i = 0;i < jsonArray.size();i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						ListAllBillsForClientSZYDTO listAllBillsForClientSZYDTO = new ListAllBillsForClientSZYDTO();
						listAllBillsForClientSZYDTO.setNumber(jsonObject.get("number") != null ? jsonObject.get("number").toString() : null);
						listAllBillsForClientSZYDTO.setTenancyName(jsonObject.get("tenancyName") != null ? jsonObject.get("tenancyName").toString() : null);
						listAllBillsForClientSZYDTO.setTenCustomerDes(jsonObject.get("tenCustomerDes") != null ? jsonObject.get("tenCustomerDes").toString() : null);
						listAllBillsForClientSZYDTO.setTenancyType(jsonObject.get("tenancyType") != null ? jsonObject.get("tenancyType").toString() : null);
						listAllBillsForClientSZYDTO.setOldTenancyBill(jsonObject.get("oldTenancyBill") != null ? jsonObject.get("oldTenancyBill").toString() : null);
						listAllBillsForClientSZYDTO.setStartDate(jsonObject.get("startDate") != null ? jsonObject.get("startDate").toString() : null);
						listAllBillsForClientSZYDTO.setEndDate(jsonObject.get("endDate") != null ? jsonObject.get("endDate").toString() : null);
						listAllBillsForClientSZYDTO.setTenancyDate(jsonObject.get("tenancyDate") != null ? jsonObject.get("tenancyDate").toString() : null);
						listAllBillsForClientSZYDTO.setLeaseCount(jsonObject.get("leaseCount") != null ? jsonObject.get("leaseCount").toString() : null);
						listAllBillsForClientSZYDTO.setRoomTotalRent(jsonObject.get("roomTotalRent") != null ? jsonObject.get("roomTotalRent").toString() : null);
						listAllBillsForClientSZYDTO.setDepositAmount(jsonObject.get("depositAmount") != null ? jsonObject.get("depositAmount").toString() : null);
						listAllBillsForClientSZYDTO.setFreeDays(jsonObject.get("freeDays") != null ? jsonObject.get("freeDays").toString() : null);
						listAllBillsForClientSZYDTO.setFirstPayRent(jsonObject.get("firstPayRent") != null ? jsonObject.get("firstPayRent").toString() : null);
						listAllBillsForClientSZYDTO.setFirstLeaseEndDate(jsonObject.get("firstLeaseEndDate") != null ? jsonObject.get("firstLeaseEndDate").toString() : null);
						listAllBillsForClientSZYDTO.setRentCountType(jsonObject.get("rentCountType") != null ? jsonObject.get("rentCountType").toString() : null);
						listAllBillsForClientSZYDTO.setTenancyState(jsonObject.get("tenancyState") != null ? jsonObject.get("tenancyState").toString() : null);
						listAllBillsForClientSZYDTO.setTenancyAdviser(jsonObject.get("tenancyAdviser") != null ? jsonObject.get("tenancyAdviser").toString() : null);
						listAllBillsForClientSZYDTO.setDescription(jsonObject.get("description") != null ? jsonObject.get("description").toString() : null);
						listAllBillsForClientSZYDTO.setDealTotalRent(jsonObject.get("dealTotalRent") != null ? jsonObject.get("dealTotalRent").toString() : null);
						listAllBillsForClientSZYDTO.setQuitRoomDate(jsonObject.get("quitRoomDate") != null ? jsonObject.get("quitRoomDate").toString() : null);
						List<BillForClientSZY> bills = new ArrayList<BillForClientSZY>();
						JSONArray receDataJsonArray = (JSONArray) jsonObject.get("receData");
						for(int j = 0;j < receDataJsonArray.size();j++) {
							JSONObject receDataJSONObject = receDataJsonArray.getJSONObject(j);
							BillForClientSZY bill = new BillForClientSZY();
							bill.setStartDate(receDataJSONObject.get("startDate") != null ? receDataJSONObject.get("startDate").toString() : null);
							bill.setEndDate(receDataJSONObject.get("endDate") != null ? receDataJSONObject.get("endDate").toString() : null);
							bill.setFappamount(receDataJSONObject.get("fappamount") != null ? receDataJSONObject.get("fappamount").toString() : null);
							bill.setFactMount(receDataJSONObject.get("factMount") != null ? receDataJSONObject.get("factMount").toString() : null);
							bill.setFmoneyDefine(receDataJSONObject.get("fmoneyDefine") != null ? receDataJSONObject.get("fmoneyDefine").toString() : null);
							bill.setFid(receDataJSONObject.get("fid") != null ? receDataJSONObject.get("fid").toString() : null);
							bill.setRoomNo(receDataJSONObject.get("roomNo") != null ? receDataJSONObject.get("roomNo").toString() : null);
							bills.add(bill);
						}
						listAllBillsForClientSZYDTO.setBills(bills);
						response.add(listAllBillsForClientSZYDTO);
					}
				} catch (JSONException e) {
					// TODO: handle exception
					JSONObject jsonObject = JSON.parseObject(result);
					LOGGER.error("深圳湾物业缴费对接请求数据失败，失败原因：" + jsonObject.get("Reason"));
				}*/
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return response;
	}
}
