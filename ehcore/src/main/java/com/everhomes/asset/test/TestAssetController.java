package com.everhomes.asset.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.asset.AssetService;
import com.everhomes.asset.schedule.AssetSchedule;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.asset.PaymentExpectanciesCommand;
import com.everhomes.rest.contract.CMSyncObject;
import com.everhomes.util.StringHelper;

@RestDoc(value = "Asset Controller", site = "core")
@RestController
@RequestMapping("/test")
public class TestAssetController extends ControllerBase {
	
	@Autowired
	private AssetService assetService;
	
	@Autowired
	private AssetSchedule assetSchedule;
	
	/**
	 * <p>手动修改系统时间，从而触发滞纳金产生（仅用于测试）</p>
	 * <b>URL: /test/testLateFine</b>
	 * @throws ParseException
	 */
	@RequestMapping("testLateFine")
	@RestReturn(value = String.class)
	public RestResponse testLateFine() {
		assetSchedule.lateFineCal();
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
	public RestResponse testUpdateBillDueDayCountOnTime() {
		assetSchedule.updateBillDueDayCountOnTime();
		RestResponse response = new RestResponse();
		response.setErrorDescription("OK");
		response.setErrorCode(ErrorCodes.SUCCESS);
		return response;
	}
	
	/**
	 * <b>URL: /test/testAutoBillNotice</b>
	 * <p>启动自动催缴的定时任务</p>
	 */
	@RequestMapping("testAutoBillNotice")
	public RestResponse testAutoBillNotice() {
		assetSchedule.autoBillNotice();
		RestResponse restResponse = new RestResponse();
		restResponse.setErrorCode(ErrorCodes.SUCCESS);
		restResponse.setErrorDescription("OK");
		return restResponse;
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
				"    \"total\": \"21\",\r\n" + 
				"    \"currentpage\": \"1\",\r\n" + 
				"    \"totalpage\": \"2\",\r\n" + 
				"    \"data\": [\r\n" + 
				"        {\r\n" + 
				"            \"ContractHeader\": {\r\n" + 
				"                \"RentalID\": \"27481\",\r\n" + 
				"                \"RentalType\": \"新租\",\r\n" + 
				"                \"PropertyID\": \"443\",\r\n" + 
				"                \"DebtorID\": \"14960\",\r\n" + 
				"                \"DebtorAcct\": \"zzs17180\",\r\n" + 
				"                \"AccountID\": \"12059\",\r\n" + 
				"                \"OA_AccountID\": \"10000\",\r\n" + 
				"                \"AccountName\": \"百草传奇餐饮（上海）有限公司\",\r\n" + 
				"                \"Connector\": \"\",\r\n" + 
				"                \"ConnectorPhone\": \"\",\r\n" + 
				"                \"ContractNo\": \"20180822-app test001\",\r\n" + 
				"                \"Mail\": \"\",\r\n" + 
				"                \"MoveinTime\": \"\",\r\n" + 
				"                \"GFA\": \"\",\r\n" + 
				"                \"NFA\": \"\",\r\n" + 
				"                \"LFA\": \"181.75\",\r\n" + 
				"                \"MailingAddress\": \"123\",\r\n" + 
				"                \"StartDate\": \"2018-08-01\",\r\n" + 
				"                \"EndDate\": \"2020-07-31\",\r\n" + 
				"                \"StampingDate\": \"\",\r\n" + 
				"                \"TerminateDate\": \"2018-12-31\",\r\n" + 
				"                \"ContractAmt\": \"291981.39\",\r\n" + 
				"                \"Recordstatus\": \"已审批\",\r\n" + 
				"                \"ModifyDate\": \"08/22/2018 09:26:35\"\r\n" + 
				"            },\r\n" + 
				"            \"ContractUnit\": [\r\n" + 
				"                {\r\n" + 
				"                    \"RentalID\": \"27481\",\r\n" + 
				"                    \"UnitID\": \"34475\",\r\n" + 
				"                    \"GFA\": \"181.75\",\r\n" + 
				"                    \"NAF\": \"181.75\",\r\n" + 
				"                    \"LFA\": \"181.75\",\r\n" + 
				"                    \"ModifyDate\": \"08/22/2018 16:20:09\"\r\n" + 
				"                }\r\n" + 
				"            ],\r\n" + 
				"            \"ContractFee\": [\r\n" + 
				"                {\r\n" + 
				"                    \"RentalID\": \"27481\",\r\n" + 
				"                    \"BillItemName\": \"租金\",\r\n" + 
				"                    \"StartDate\": \"2018-08-01\",\r\n" + 
				"                    \"EndDate\": \"2020-07-31\",\r\n" + 
				"                    \"CalculateMethod\": \"天单价\",\r\n" + 
				"                    \"DocumentAmt\": \"10.50\",\r\n" + 
				"                    \"ChargeAmt\": \"10.00\",\r\n" + 
				"                    \"TaxAmt\": \"0.50\",\r\n" + 
				"                    \"ModifyDate\": \"08/22/2018 16:21:42\"\r\n" + 
				"                }\r\n" + 
				"            ],\r\n" + 
				"            \"RentalOption\": [],\r\n" + 
				"            \"Insurance\": [\r\n" + 
				"                {\r\n" + 
				"                    \"RentalID\": \"27481\",\r\n" + 
				"                    \"InsuranceType\": \"默认\",\r\n" + 
				"                    \"InsuranceMoney\": \"\",\r\n" + 
				"                    \"ModifyDate\": \"08/22/2018 16:22:16\"\r\n" + 
				"                }\r\n" + 
				"            ],\r\n" + 
				"            \"Bill\": [\r\n" + 
				"                {\r\n" + 
				"                    \"PropertyID\": \"443\",\r\n" + 
				"                    \"BillScheduleID\": \"9352753\",\r\n" + 
				"                    \"DebtorID\": \"14960\",\r\n" + 
				"                    \"RentalID\": \"27481\",\r\n" + 
				"                    \"BillID\": \"447877\",\r\n" + 
				"                    \"BillType\": \"租金账单\",\r\n" + 
				"                    \"BillItemName\": \"租金\",\r\n" + 
				"                    \"DocumentDate\": \"2018-07-31\",\r\n" + 
				"                    \"StartDate\": \"2018-08-01\",\r\n" + 
				"                    \"EndDate\": \"2018-08-31\",\r\n" + 
				"                    \"Status\": \"已出账单\",\r\n" + 
				"                    \"DocumentAmt\": \"59159.63\",\r\n" + 
				"                    \"ChargeAmt\": \"56342.50\",\r\n" + 
				"                    \"TaxAmt\": \"2817.13\",\r\n" + 
				"                    \"TaxRate\": \"0.05\",\r\n" + 
				"                    \"BalanceAmt\": \"0.0000000000\",\r\n" + 
				"                    \"ModifyDate\": \"08/22/2018 00:00:00\"\r\n" + 
				"                },\r\n" + 
				"                {\r\n" + 
				"                    \"PropertyID\": \"443\",\r\n" + 
				"                    \"BillScheduleID\": \"9352754\",\r\n" + 
				"                    \"DebtorID\": \"14960\",\r\n" + 
				"                    \"RentalID\": \"27481\",\r\n" + 
				"                    \"BillID\": \"447878\",\r\n" + 
				"                    \"BillType\": \"租金账单\",\r\n" + 
				"                    \"BillItemName\": \"租金\",\r\n" + 
				"                    \"DocumentDate\": \"2018-08-31\",\r\n" + 
				"                    \"StartDate\": \"2018-09-01\",\r\n" + 
				"                    \"EndDate\": \"2018-09-30\",\r\n" + 
				"                    \"Status\": \"已出账单\",\r\n" + 
				"                    \"DocumentAmt\": \"57251.25\",\r\n" + 
				"                    \"ChargeAmt\": \"54525.00\",\r\n" + 
				"                    \"TaxAmt\": \"2726.25\",\r\n" + 
				"                    \"TaxRate\": \"0.05\",\r\n" + 
				"                    \"BalanceAmt\": \"0.0000000000\",\r\n" + 
				"                    \"ModifyDate\": \"08/22/2018 00:00:00\"\r\n" + 
				"                },\r\n" + 
				"                {\r\n" + 
				"                    \"PropertyID\": \"443\",\r\n" + 
				"                    \"BillScheduleID\": \"9352755\",\r\n" + 
				"                    \"DebtorID\": \"14960\",\r\n" + 
				"                    \"RentalID\": \"27481\",\r\n" + 
				"                    \"BillID\": \"447879\",\r\n" + 
				"                    \"BillType\": \"租金账单\",\r\n" + 
				"                    \"BillItemName\": \"租金\",\r\n" + 
				"                    \"DocumentDate\": \"2018-09-30\",\r\n" + 
				"                    \"StartDate\": \"2018-10-01\",\r\n" + 
				"                    \"EndDate\": \"2018-10-31\",\r\n" + 
				"                    \"Status\": \"已出账单\",\r\n" + 
				"                    \"DocumentAmt\": \"59159.63\",\r\n" + 
				"                    \"ChargeAmt\": \"56342.50\",\r\n" + 
				"                    \"TaxAmt\": \"2817.13\",\r\n" + 
				"                    \"TaxRate\": \"0.05\",\r\n" + 
				"                    \"BalanceAmt\": \"0.0000000000\",\r\n" + 
				"                    \"ModifyDate\": \"08/31/2018 00:00:00\"\r\n" + 
				"                },\r\n" + 
				"                {\r\n" + 
				"                    \"PropertyID\": \"443\",\r\n" + 
				"                    \"BillScheduleID\": \"9352756\",\r\n" + 
				"                    \"DebtorID\": \"14960\",\r\n" + 
				"                    \"RentalID\": \"27481\",\r\n" + 
				"                    \"BillID\": \"\",\r\n" + 
				"                    \"BillType\": \"租金账单\",\r\n" + 
				"                    \"BillItemName\": \"租金\",\r\n" + 
				"                    \"DocumentDate\": \"2018-10-31\",\r\n" + 
				"                    \"StartDate\": \"2018-11-01\",\r\n" + 
				"                    \"EndDate\": \"2018-11-30\",\r\n" + 
				"                    \"Status\": \"未发单\",\r\n" + 
				"                    \"DocumentAmt\": \"57251.25\",\r\n" + 
				"                    \"ChargeAmt\": \"54525.00\",\r\n" + 
				"                    \"TaxAmt\": \"2726.25\",\r\n" + 
				"                    \"TaxRate\": \"0.05\",\r\n" + 
				"                    \"BalanceAmt\": \"0.0000000000\",\r\n" + 
				"                    \"ModifyDate\": \"\"\r\n" + 
				"                },\r\n" + 
				"                {\r\n" + 
				"                    \"PropertyID\": \"443\",\r\n" + 
				"                    \"BillScheduleID\": \"9352757\",\r\n" + 
				"                    \"DebtorID\": \"14960\",\r\n" + 
				"                    \"RentalID\": \"27481\",\r\n" + 
				"                    \"BillID\": \"\",\r\n" + 
				"                    \"BillType\": \"租金账单\",\r\n" + 
				"                    \"BillItemName\": \"租金\",\r\n" + 
				"                    \"DocumentDate\": \"2018-11-30\",\r\n" + 
				"                    \"StartDate\": \"2018-12-01\",\r\n" + 
				"                    \"EndDate\": \"2018-12-31\",\r\n" + 
				"                    \"Status\": \"已缴账单\",\r\n" + 
				"                    \"DocumentAmt\": \"59159.63\",\r\n" + 
				"                    \"ChargeAmt\": \"56342.50\",\r\n" + 
				"                    \"TaxAmt\": \"2817.13\",\r\n" + 
				"                    \"TaxRate\": \"0.05\",\r\n" + 
				"                    \"BalanceAmt\": \"0.0000000000\",\r\n" + 
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
        
        List<CMSyncObject> cmSyncObjectList = new ArrayList<CMSyncObject>();
        cmSyncObjectList.add(cmSyncObject);
		assetService.syncRuiAnCMBillToZuolin(cmSyncObjectList, 999929, 3L);
		RestResponse restResponse = new RestResponse();
		restResponse.setErrorDescription("OK");
		restResponse.setErrorCode(ErrorCodes.SUCCESS);
		return restResponse;
	}

}
