package com.everhomes.openapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.asset.PaymentBills;
import com.everhomes.asset.bill.AssetBillService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.asset.ListBillsCommand;
import com.everhomes.rest.asset.bill.ChangeChargeStatusCommand;
import com.everhomes.rest.asset.bill.ListBillsDTO;
import com.everhomes.rest.asset.bill.ListBillsResponse;
import com.everhomes.rest.asset.bill.NotifyThirdSignCommand;

/**
 * @author created by ycx
 * @date 下午3:23:42
 */
@RestDoc(value = "Asset open Constroller", site = "core")
@RestController
@RequestMapping("/openapi/asset")
public class AssetOpenController extends ControllerBase {
    //private static final Logger LOGGER = LoggerFactory.getLogger(AssetOpenController.class);
    
    @Autowired
    private AssetBillService assetBillService;
    
    /**
     * <b>URL: /openapi/asset/listBills</b>
     * <p>物业缴费V7.5（中天-资管与财务EAS系统对接）：查看账单列表（只传租赁账单） </p>
     */
    @RequestMapping("listBills")
    @RestReturn(value = ListBillsResponse.class)
	public RestResponse listBills(ListBillsCommand cmd) {
		ListBillsResponse listBillsResponse = assetBillService.listOpenBills(cmd);
		RestResponse response = new RestResponse(listBillsResponse);
		response.setErrorDescription("OK");
		response.setErrorCode(ErrorCodes.SUCCESS);
		return response;
	}
    
    /**
     * <b>URL: /openapi/asset/changeChargeStatus</b>
     * <p>物业缴费V7.5（中天-资管与财务EAS系统对接）：EAS系统收到款项录入凭证，将收款状态回传至左邻</p>
     */
    @RequestMapping("changeChargeStatus")
    @RestReturn(value = ListBillsDTO.class)
	public RestResponse changeChargeStatus(ChangeChargeStatusCommand cmd) {
    	ListBillsDTO dto = assetBillService.changeChargeStatus(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorDescription("OK");
		response.setErrorCode(ErrorCodes.SUCCESS);
		return response;
	}
    
    /**
     * <b>URL: /openapi/asset/notifyThirdSign</b>
     * <p>物业缴费V7.4(瑞安项目-资产管理对接CM系统) ： 一个特殊error标记给左邻系统，左邻系统以此标记判断该条数据下一次同步不再传输 </p>
     */
    @RequestMapping("notifyThirdSign")
    @RestReturn(value = RestResponse.class)
	public RestResponse notifyThirdSign(NotifyThirdSignCommand cmd) {
		assetBillService.notifyThirdSign(cmd);
		RestResponse response = new RestResponse();
		response.setErrorDescription("OK");
		response.setErrorCode(ErrorCodes.SUCCESS);
		return response;
	}

}
