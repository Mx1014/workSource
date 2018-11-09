package com.everhomes.openapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.asset.bill.AssetBillService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.asset.ListBillsCommand;
import com.everhomes.rest.asset.bill.ListBillsResponse;

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
    	//写死中天的域空间ID
    	cmd.setNamespaceId(999944);
    	cmd.setOwnerId(cmd.getCommunityId());
		ListBillsResponse listBillsResponse = assetBillService.listOpenBills(cmd);
		RestResponse response = new RestResponse(listBillsResponse);
		response.setErrorDescription("OK");
		response.setErrorCode(ErrorCodes.SUCCESS);
		return response;
	}
    
    
    

}
