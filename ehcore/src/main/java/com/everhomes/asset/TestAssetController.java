package com.everhomes.asset;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.asset.CreateGeneralBillCommand;
import com.everhomes.rest.asset.CreateOrUpdateAssetMappingCmd;
import com.everhomes.rest.asset.ListBillGroupsDTO;
import com.everhomes.rest.asset.ListBillsDTO;
import com.everhomes.rest.asset.ListChargingItemsDTO;
import com.everhomes.rest.asset.OwnerIdentityCommand;
import com.everhomes.rest.asset.PaymentExpectanciesCommand;
import com.everhomes.rest.asset.TestLateFineCommand;
import com.everhomes.rest.contract.CMSyncObject;
import com.everhomes.rest.portal.AssetModuleAppMappingDTO;
import com.everhomes.rest.portal.AssetServiceModuleAppDTO;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

@RestDoc(value = "Asset Controller", site = "core")
@RestController
@RequestMapping("/test")
public class TestAssetController extends ControllerBase {
	
	@Autowired
	private AssetService assetService;
	
	/**
     * <p>物业缴费V6.6统一账单：获取缴费应用列表接口</p>
     * <b>URL: /test/listAssetModuleApps</b>
     */
	@RequestMapping("listAssetModuleApps")
	@RestReturn(value=AssetServiceModuleAppDTO.class, collection = true)
    public RestResponse listAssetModuleApps(ListServiceModuleAppsCommand cmd) {
    	List<AssetServiceModuleAppDTO> dtos = assetService.listAssetModuleApps(cmd.getNamespaceId());
        RestResponse response = new RestResponse(dtos);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }
	
	/**
	 * <p>物业缴费V6.6统一账单：展示账单组列表</p>
	 * <b>URL: /test/listBillGroups</b>
	 */
	@RequestMapping("listBillGroups")
	@RestReturn(value = ListBillGroupsDTO.class, collection = true)
	public RestResponse listBillGroups(OwnerIdentityCommand cmd) {
	    List<ListBillGroupsDTO> list = assetService.listBillGroups(cmd);
	    RestResponse response = new RestResponse(list);
	    response.setErrorDescription("OK");
	    response.setErrorCode(ErrorCodes.SUCCESS);
	    return response;
	}
	
	/**
	 * <p>获取账单组启用的收费项目列表</p>
	 * <b>URL: /test/listChargingItems</b>
	 */
	@RequestMapping("listChargingItems")
	@RestReturn(value = ListChargingItemsDTO.class, collection = true)
	public RestResponse listChargingItems(OwnerIdentityCommand cmd) {
	    List<ListChargingItemsDTO> list = assetService.listAvailableChargingItems(cmd);
	    RestResponse response = new RestResponse(list);
	    response.setErrorDescription("OK");
	    response.setErrorCode(ErrorCodes.SUCCESS);
	    return response;
	}
	
	/**
	 * <p>业务应用新增缴费映射关系</p>
	 * <b>URL: /test/createOrUpdateAssetMapping</b>
	 */
	@RequestMapping("createOrUpdateAssetMapping")
	@RestReturn(value = AssetModuleAppMappingDTO.class, collection = false)
	public RestResponse createOrUpdateAssetMapping(CreateOrUpdateAssetMappingCmd cmd) {
		AssetModuleAppMapping assetModuleAppMapping = ConvertHelper.convert(cmd, AssetModuleAppMapping.class);
		AssetModuleAppMapping dto = assetService.createOrUpdateAssetMapping(assetModuleAppMapping);
		AssetModuleAppMappingDTO assetModuleAppMappingDTO = ConvertHelper.convert(dto, AssetModuleAppMappingDTO.class);
	    RestResponse response = new RestResponse(assetModuleAppMappingDTO);
	    response.setErrorDescription("OK");
	    response.setErrorCode(ErrorCodes.SUCCESS);
	    return response;
	}
	
	/**
	 * <p>创建统一账单接口</p>
	 * <b>URL: /test/createGeneralBill</b>
	 */
	@RequestMapping("createGeneralBill")
	@RestReturn(value = ListBillsDTO.class, collection = true)
	public RestResponse createGeneralBill(CreateGeneralBillCommand cmd) {
		List<ListBillsDTO> dto = assetService.createGeneralBill(cmd);
	    RestResponse response = new RestResponse(dto);
	    response.setErrorDescription("OK");
	    response.setErrorCode(ErrorCodes.SUCCESS);
	    return response;
	}
	
	/**
	 * <p>手动修改系统时间，从而触发滞纳金产生（仅用于测试）</p>
	 * <b>URL: /test/testLateFine</b>
	 * @throws ParseException
	 */
	@RequestMapping("testLateFine")
	@RestReturn(value = String.class)
	public RestResponse testLateFine(TestLateFineCommand cmd) {
		assetService.testLateFine(cmd);
		RestResponse response = new RestResponse();
		response.setErrorDescription("OK");
		response.setErrorCode(ErrorCodes.SUCCESS);
		return response;
	}
	
	/**
	 * <p>仅用于手动测试能耗数据</p>
	 * <b>URL: /test/testEnergy</b>
	 */
	@RequestMapping("testEnergy")
	@RestReturn(value = String.class)
	public RestResponse testEnergy(PaymentExpectanciesCommand cmd) {
		assetService.paymentExpectanciesCalculate(cmd);
		RestResponse response = new RestResponse();
		response.setErrorDescription("OK");
		response.setErrorCode(ErrorCodes.SUCCESS);
		return response;
	}

	/**
	 * <p>仅用于测试手动测试欠费天数</p>
	 * <b>URL: /test/testUpdateBillDueDayCountOnTime</b>
	 */
	@RequestMapping("testUpdateBillDueDayCountOnTime")
	@RestReturn(value = String.class)
	public RestResponse testUpdateBillDueDayCountOnTime(TestLateFineCommand cmd) {
		assetService.testUpdateBillDueDayCountOnTime(cmd);
		RestResponse response = new RestResponse();
		response.setErrorDescription("OK");
		response.setErrorCode(ErrorCodes.SUCCESS);
		return response;
	}
	
	/**
	 * <p>仅用于测试瑞安CM对接</p>
	 * <b>URL: /test/testSyncRuiAnCMBillToZuolin</b>
	 */
	@RequestMapping("testSyncRuiAnCMBillToZuolin")
	@RestReturn(value = String.class)
	public RestResponse testSyncRuiAnCMBillToZuolin(HttpServletResponse response) {
		String testString = "{\r\n" + 
				"    \"errorcode\": \"0\",\r\n" + 
				"    \"total\": \"19\",\r\n" + 
				"    \"currentpage\": \"1\",\r\n" + 
				"    \"totalpage\": \"1\",\r\n" + 
				"    \"data\": [\r\n" + 
				"        {\r\n" + 
				"            \"ContractHeader\": {\r\n" + 
				"                \"RentalID\": \"27483\",\r\n" + 
				"                \"RentalType\": \"新租\",\r\n" + 
				"                \"PropertyID\": \"443\",\r\n" + 
				"                \"DebtorID\": \"14961\",\r\n" + 
				"                \"DebtorAcct\": \"zzs17182\",\r\n" + 
				"                \"AccountID\": \"12057\",\r\n" + 
				"                \"OA_AccountID\": \"\",\r\n" + 
				"                \"AccountName\": \"巴罗克（上海）服饰有限公司\",\r\n" + 
				"                \"Connector\": \"\",\r\n" + 
				"                \"ConnectorPhone\": \"\",\r\n" + 
				"                \"ContractNo\": \"20180823 - port5test001\",\r\n" + 
				"                \"Mail\": \"\",\r\n" + 
				"                \"MoveinTime\": \"\",\r\n" + 
				"                \"GFA\": \"\",\r\n" + 
				"                \"NFA\": \"\",\r\n" + 
				"                \"LFA\": \"211.41\",\r\n" + 
				"                \"MailingAddress\": \"address001\",\r\n" + 
				"                \"StartDate\": \"2018-01-01\",\r\n" + 
				"                \"EndDate\": \"2020-12-31\",\r\n" + 
				"                \"StampingDate\": \"\",\r\n" + 
				"                \"TerminateDate\": \"2018-03-31\",\r\n" + 
				"                \"ContractAmt\": \"144065.34\",\r\n" + 
				"                \"Recordstatus\": \"已结束\",\r\n" + 
				"                \"ModifyDate\": \"08/23/2018 07:55:11\"\r\n" + 
				"            },\r\n" + 
				"            \"ContractUnit\": [\r\n" + 
				"                {\r\n" + 
				"                    \"RentalID\": \"27483\",\r\n" + 
				"                    \"UnitID\": \"34559\",\r\n" + 
				"                    \"GFA\": \"211.41\",\r\n" + 
				"                    \"NAF\": \"211.41\",\r\n" + 
				"                    \"LFA\": \"211.41\",\r\n" + 
				"                    \"ModifyDate\": \"08/23/2018 15:42:59\"\r\n" + 
				"                }\r\n" + 
				"            ],\r\n" + 
				"            \"ContractFee\": [\r\n" + 
				"                {\r\n" + 
				"                    \"RentalID\": \"27483\",\r\n" + 
				"                    \"BillItemName\": \"租金\",\r\n" + 
				"                    \"StartDate\": \"2018-01-01\",\r\n" + 
				"                    \"EndDate\": \"2020-12-31\",\r\n" + 
				"                    \"CalculateMethod\": \"天单价\",\r\n" + 
				"                    \"DocumentAmt\": \"11.55\",\r\n" + 
				"                    \"ChargeAmt\": \"11.00\",\r\n" + 
				"                    \"TaxAmt\": \"0.55\",\r\n" + 
				"                    \"ModifyDate\": \"08/23/2018 15:45:29\"\r\n" + 
				"                }\r\n" + 
				"            ],\r\n" + 
				"            \"RentalOption\": [],\r\n" + 
				"            \"Insurance\": [\r\n" + 
				"                {\r\n" + 
				"                    \"RentalID\": \"27483\",\r\n" + 
				"                    \"InsuranceType\": \"默认\",\r\n" + 
				"                    \"InsuranceMoney\": \"\",\r\n" + 
				"                    \"ModifyDate\": \"08/23/2018 15:47:00\"\r\n" + 
				"                }\r\n" + 
				"            ],\r\n" + 
				"            \"Bill\": [\r\n" + 
				"                {\r\n" + 
				"                    \"PropertyID\": \"443\",\r\n" + 
				"                    \"BillScheduleID\": \"9352862\",\r\n" + 
				"                    \"DebtorID\": \"14961\",\r\n" + 
				"                    \"RentalID\": \"27483\",\r\n" + 
				"                    \"BillID\": \"447882\",\r\n" + 
				"                    \"BillType\": \"租金账单\",\r\n" + 
				"                    \"BillItemName\": \"租金\",\r\n" + 
				"                    \"DocumentDate\": \"2017-12-31\",\r\n" + 
				"                    \"StartDate\": \"2018-01-01\",\r\n" + 
				"                    \"EndDate\": \"2018-01-31\",\r\n" + 
				"                    \"Status\": \"已出账单\",\r\n" + 
				"                    \"DocumentAmt\": \"75695.35\",\r\n" + 
				"                    \"BalanceAmt\": \"0.00\",\r\n" + 
				"                    \"ModifyDate\": \"\"\r\n" + 
				"                },\r\n" + 
				"                {\r\n" + 
				"                    \"PropertyID\": \"443\",\r\n" + 
				"                    \"BillScheduleID\": \"9352863\",\r\n" + 
				"                    \"DebtorID\": \"14961\",\r\n" + 
				"                    \"RentalID\": \"27483\",\r\n" + 
				"                    \"BillID\": \"447884\",\r\n" + 
				"                    \"BillType\": \"租金账单\",\r\n" + 
				"                    \"BillItemName\": \"租金\",\r\n" + 
				"                    \"DocumentDate\": \"2018-01-31\",\r\n" + 
				"                    \"StartDate\": \"2018-02-01\",\r\n" + 
				"                    \"EndDate\": \"2018-02-28\",\r\n" + 
				"                    \"Status\": \"已出账单\",\r\n" + 
				"                    \"DocumentAmt\": \"68369.99\",\r\n" + 
				"                    \"BalanceAmt\": \"0.00\",\r\n" + 
				"                    \"ModifyDate\": \"01/31/2018 00:00:00\"\r\n" + 
				"                },\r\n" + 
				"                {\r\n" + 
				"                    \"PropertyID\": \"443\",\r\n" + 
				"                    \"BillScheduleID\": \"9352864\",\r\n" + 
				"                    \"DebtorID\": \"14961\",\r\n" + 
				"                    \"RentalID\": \"27483\",\r\n" + 
				"                    \"BillID\": \"447885\",\r\n" + 
				"                    \"BillType\": \"租金账单\",\r\n" + 
				"                    \"BillItemName\": \"租金\",\r\n" + 
				"                    \"DocumentDate\": \"2018-02-28\",\r\n" + 
				"                    \"StartDate\": \"2018-03-01\",\r\n" + 
				"                    \"EndDate\": \"2018-03-31\",\r\n" + 
				"                    \"Status\": \"已出账单\",\r\n" + 
				"                    \"DocumentAmt\": \"75695.35\",\r\n" + 
				"                    \"BalanceAmt\": \"0.00\",\r\n" + 
				"                    \"ModifyDate\": \"01/31/2018 00:00:00\"\r\n" + 
				"                },\r\n" + 
				"                {\r\n" + 
				"                    \"PropertyID\": \"443\",\r\n" + 
				"                    \"BillScheduleID\": \"9352898\",\r\n" + 
				"                    \"DebtorID\": \"14961\",\r\n" + 
				"                    \"RentalID\": \"27483\",\r\n" + 
				"                    \"BillID\": \"447883\",\r\n" + 
				"                    \"BillType\": \"租金账单\",\r\n" + 
				"                    \"BillItemName\": \"租金\",\r\n" + 
				"                    \"DocumentDate\": \"2017-12-31\",\r\n" + 
				"                    \"StartDate\": \"2018-01-01\",\r\n" + 
				"                    \"EndDate\": \"2018-01-31\",\r\n" + 
				"                    \"Status\": \"已出账单\",\r\n" + 
				"                    \"DocumentAmt\": \"-75695.35\",\r\n" + 
				"                    \"BalanceAmt\": \"0.00\",\r\n" + 
				"                    \"ModifyDate\": \"\"\r\n" + 
				"                }\r\n" + 
				"            ]\r\n" + 
				"        }\r\n" + 
				"    ]\r\n" + 
				"}";
		Map result = JSONObject.parseObject(testString);
        CMSyncObject cmSyncObject =
                (CMSyncObject) StringHelper.fromJsonString(testString, CMSyncObject.class);
        cmSyncObject.getData().get(0).setCommunityId(240111044332063578L);
        cmSyncObject.getData().get(0).setCustomerId(1041502L);
        
		assetService.syncRuiAnCMBillToZuolin(cmSyncObject, 999929, 3L);
		RestResponse restResponse = new RestResponse();
		restResponse.setErrorDescription("OK");
		restResponse.setErrorCode(ErrorCodes.SUCCESS);
		return restResponse;
	}
	

}
