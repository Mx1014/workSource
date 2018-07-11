package com.everhomes.asset.szwwyjf;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.asset.szwwyjf.webservice.EASLogin.EASLoginProxy;
import com.everhomes.asset.szwwyjf.webservice.EASLogin.EASLoginProxyServiceLocator;
import com.everhomes.asset.szwwyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxy;
import com.everhomes.asset.szwwyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxyServiceLocator;
import com.everhomes.asset.szwwyjf.webservice.client.WSContext;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.asset.BillForClientV2;
import com.everhomes.rest.asset.ListAllBillsForClientDTO;
import com.everhomes.rest.asset.ShowBillDetailForClientDTO;
import com.everhomes.rest.asset.ShowBillDetailForClientResponse;
import com.everhomes.rest.asset.ShowBillForClientV2DTO;
import com.everhomes.rest.contract.ContractDetailDTO;
import com.everhomes.rest.contract.FindContractCommand;
import com.everhomes.util.StringHelper;

@Component
public class SZWQuery {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SZWQuery.class);
	
	@Autowired
    private ConfigurationProvider configurationProvider;
	
	public Boolean login() {
		Boolean isLogin = false;
		try {
			//通过WebService登录EAS
			EASLoginProxyServiceLocator loginLocator = new EASLoginProxyServiceLocator();
			//String EASLogin_address = "http://192.168.1.200:6888/ormrpc/services/EASLogin";
			String EASLogin_address = configurationProvider.getValue(999966, ConfigConstants.ASSET_SHENZHENWAN_EASLOGIN_ADDRESS,"");
			loginLocator.setEASLoginEndpointAddress(EASLogin_address);
			EASLoginProxy loginProxy = loginLocator.getEASLogin();
			String username = configurationProvider.getValue(999966, ConfigConstants.ASSET_SHENZHENWAN_USERNAME,"");
			String password = configurationProvider.getValue(999966, ConfigConstants.ASSET_SHENZHENWAN_PASSWORD,"");
			String slnName = configurationProvider.getValue(999966, ConfigConstants.ASSET_SHENZHENWAN_SLNNAME,"");
			String dcName = configurationProvider.getValue(999966, ConfigConstants.ASSET_SHENZHENWAN_DCNAME,"");
			String language = configurationProvider.getValue(999966, ConfigConstants.ASSET_SHENZHENWAN_LANGUAGE,"");
			String dbType = configurationProvider.getValue(999966, ConfigConstants.ASSET_SHENZHENWAN_DBTYPE,"");
			//WSContext context = loginProxy.login("mybay", "mybay", "eas", "cs200", "l2", 2);
			WSContext context = loginProxy.login(username, password, slnName, dcName, language, Integer.parseInt(dbType));
			if(context.getSessionId() != null) {
				LOGGER.info("szw WebService login success，SessionID: " + context.getSessionId());
				isLogin = true;
			}else {
				LOGGER.error("szw webservice login fail");
			}
		} catch (Exception e) {
			LOGGER.error("szw webservice login fail : " + e);
		} 
		return isLogin;
	}
	
	public List<ShowBillForClientV2DTO> showBillForClientV2(String request) {
		List<ShowBillForClientV2DTO> response = new ArrayList<ShowBillForClientV2DTO>();
		//通过WebService登录EAS
		if(login()) {
			try {
				WSWSSyncMyBayFacadeSrvProxyServiceLocator accountLocator = new WSWSSyncMyBayFacadeSrvProxyServiceLocator();
				//String WSWSSyncMyBayFacade_address = "http://192.168.1.200:6888/ormrpc/services/WSWSSyncMyBayFacade";
				String WSWSSyncMyBayFacade_address = configurationProvider.getValue(999966, ConfigConstants.ASSET_SHENZHENWAN_WSWSSYNCMYBAYFACADE_ADDRESS,"");
				accountLocator.setWSWSSyncMyBayFacadeEndpointAddress(WSWSSyncMyBayFacade_address);
				WSWSSyncMyBayFacadeSrvProxy accountProxy = accountLocator.getWSWSSyncMyBayFacade();
				String result = accountProxy.sync_TenancyContractData(request);
				try {
					JSONArray jsonArray = JSON.parseArray(result);
					for(int i = 0;i < jsonArray.size();i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						ShowBillForClientV2DTO showBillForClientV2DTO = new ShowBillForClientV2DTO();
						showBillForClientV2DTO.setBillGroupName((jsonObject.get("tenCustomerDes") != null ? jsonObject.get("tenCustomerDes").toString() : null) + "的账单");
						//showBillForClientV2DTO.setBillGroupId(jsonObject.get("number") != null ? jsonObject.get("number").toString() : null);
						//合同编号默认给前端传0，因为深圳湾对接传的合同ID是字符串，我们是Long，无法转换
						showBillForClientV2DTO.setContractId("0");
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
		if(login()) {
			try {
				WSWSSyncMyBayFacadeSrvProxyServiceLocator accountLocator = new WSWSSyncMyBayFacadeSrvProxyServiceLocator();
				//String WSWSSyncMyBayFacade_address = "http://192.168.1.200:6888/ormrpc/services/WSWSSyncMyBayFacade";
				String WSWSSyncMyBayFacade_address = configurationProvider.getValue(999966, ConfigConstants.ASSET_SHENZHENWAN_WSWSSYNCMYBAYFACADE_ADDRESS,"");
				accountLocator.setWSWSSyncMyBayFacadeEndpointAddress(WSWSSyncMyBayFacade_address);
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
							listAllBillsForClientDTO.setBillId(receDataJSONObject.get("fid") != null ? receDataJSONObject.get("fid").toString() : null);
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
		if(login()) {
			try {
				WSWSSyncMyBayFacadeSrvProxyServiceLocator accountLocator = new WSWSSyncMyBayFacadeSrvProxyServiceLocator();
				//String WSWSSyncMyBayFacade_address = "http://192.168.1.200:6888/ormrpc/services/WSWSSyncMyBayFacade";
				String WSWSSyncMyBayFacade_address = configurationProvider.getValue(999966, ConfigConstants.ASSET_SHENZHENWAN_WSWSSYNCMYBAYFACADE_ADDRESS,"");
				accountLocator.setWSWSSyncMyBayFacadeEndpointAddress(WSWSSyncMyBayFacade_address);
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
	
	public ContractDetailDTO findContractForApp(String request) {
		ContractDetailDTO contractDetailDTO = new ContractDetailDTO();
		//通过WebService登录EAS
		if(login()) {
			try {
				WSWSSyncMyBayFacadeSrvProxyServiceLocator accountLocator = new WSWSSyncMyBayFacadeSrvProxyServiceLocator();
				//String WSWSSyncMyBayFacade_address = "http://192.168.1.200:6888/ormrpc/services/WSWSSyncMyBayFacade";
				String WSWSSyncMyBayFacade_address = configurationProvider.getValue(999966, ConfigConstants.ASSET_SHENZHENWAN_WSWSSYNCMYBAYFACADE_ADDRESS,"");
				accountLocator.setWSWSSyncMyBayFacadeEndpointAddress(WSWSSyncMyBayFacade_address);
				WSWSSyncMyBayFacadeSrvProxy accountProxy = accountLocator.getWSWSSyncMyBayFacade();
				String result = accountProxy.sync_TenancyContractInfo(request);
				try {
					JSONArray jsonArray = JSON.parseArray(result);
					JSONObject jsonObject = jsonArray.getJSONObject(0);
					contractDetailDTO.setContractNumber(jsonObject.get("number") != null ? jsonObject.get("number").toString() : null);
					contractDetailDTO.setName(jsonObject.get("tenancyName") != null ? jsonObject.get("tenancyName").toString() : null);
					contractDetailDTO.setCustomerName(jsonObject.get("tenCustomerDes") != null ? jsonObject.get("tenCustomerDes").toString() : null);
					contractDetailDTO.setCategoryItemName(jsonObject.get("tenancyType") != null ? jsonObject.get("tenancyType").toString() : null);
					contractDetailDTO.setParentContractNumber(jsonObject.get("oldTenancyBill") != null ? jsonObject.get("oldTenancyBill").toString() : null);
					//格式化日期
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Timestamp contractStartDate = new Timestamp(format.parse(jsonObject.get("startDate") != null ? jsonObject.get("startDate").toString() : null).getTime());
					Timestamp contractEndDate = new Timestamp(format.parse(jsonObject.get("endDate") != null ? jsonObject.get("endDate").toString() : null).getTime());
					contractDetailDTO.setContractStartDate(contractStartDate);
					contractDetailDTO.setContractEndDate(contractEndDate);
					Timestamp signedTime = new Timestamp(format.parse(jsonObject.get("tenancyDate") != null ? jsonObject.get("tenancyDate").toString() : null).getTime());
					contractDetailDTO.setSignedTime(signedTime);
					Integer rentCycle = Integer.parseInt(jsonObject.get("leaseCount") != null ? jsonObject.get("leaseCount").toString() : null);
					contractDetailDTO.setRentCycle(rentCycle);
					Double rentSize = Double.parseDouble(jsonObject.get("roomTotalRent") != null ? jsonObject.get("roomTotalRent").toString() : null);
					contractDetailDTO.setRentSize(rentSize);
					BigDecimal deposit = new BigDecimal(jsonObject.get("depositAmount") != null ? jsonObject.get("depositAmount").toString() : null);
					contractDetailDTO.setDeposit(deposit);
					Integer freeDays = Integer.parseInt(jsonObject.get("freeDays") != null ? jsonObject.get("freeDays").toString() : null);
					contractDetailDTO.setFreeDays(freeDays);
					BigDecimal downpayment = new BigDecimal(jsonObject.get("firstPayRent") != null ? jsonObject.get("firstPayRent").toString() : null);
					contractDetailDTO.setDownpayment(downpayment);
					Timestamp downpaymentTime = new Timestamp(format.parse(jsonObject.get("firstLeaseEndDate") != null ? jsonObject.get("firstLeaseEndDate").toString() : null).getTime());
					contractDetailDTO.setDownpaymentTime(downpaymentTime);
					contractDetailDTO.setPaidType(jsonObject.get("rentCountType") != null ? jsonObject.get("rentCountType").toString() : null);
					//合同状态：待第三方修改
					//contractDetailDTO.setStatus(jsonObject.get("tenancyState") != null ? jsonObject.get("tenancyState").toString() : null);
					//经办人：没有这个字段
					//备注
					contractDetailDTO.setRemark(jsonObject.get("description") != null ? jsonObject.get("description").toString() : null);
					//合同总额
					if(jsonObject.get("quitRoomDate") != null && !jsonObject.get("quitRoomDate").equals("")) {
						Timestamp denunciationTime = new Timestamp(format.parse(jsonObject.get("quitRoomDate").toString()).getTime());
						contractDetailDTO.setDenunciationTime(denunciationTime);
					}
				} catch (JSONException e) {
					LOGGER.error("findContractForApp Failed to parse json response, request={}, result={}", request, result, e);
				}
			} catch (Exception e) {
				LOGGER.error("findContractForApp Failed to call szw api, request={}", request, e);
			}
		}
		return contractDetailDTO;
	}
	
	
}
