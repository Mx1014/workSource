package com.everhomes.asset;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import com.everhomes.rest.asset.NoticeTriggerCommand;
import com.everhomes.rest.asset.OwnerIdentityCommand;
import com.everhomes.rest.asset.PaymentExpectanciesCommand;
import com.everhomes.rest.asset.TestLateFineCommand;
import com.everhomes.rest.portal.AssetModuleAppMappingDTO;
import com.everhomes.rest.portal.AssetServiceModuleAppDTO;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.util.ConvertHelper;

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
	
//	/**
//	 * <p>业务应用新增缴费映射关系</p>
//	 * <b>URL: /test/createOrUpdateAssetMapping</b>
//	 */
//	@RequestMapping("createOrUpdateAssetMapping")
//	@RestReturn(value = AssetModuleAppMappingDTO.class, collection = false)
//	public RestResponse createOrUpdateAssetMapping(CreateOrUpdateAssetMappingCmd cmd) {
//		AssetModuleAppMapping assetModuleAppMapping = ConvertHelper.convert(cmd, AssetModuleAppMapping.class);
//		AssetModuleAppMapping dto = assetService.createOrUpdateAssetMapping(assetModuleAppMapping);
//		AssetModuleAppMappingDTO assetModuleAppMappingDTO = ConvertHelper.convert(dto, AssetModuleAppMappingDTO.class);
//	    RestResponse response = new RestResponse(assetModuleAppMappingDTO);
//	    response.setErrorDescription("OK");
//	    response.setErrorCode(ErrorCodes.SUCCESS);
//	    return response;
//	}
	
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
	 * <b>URL: /test/noticeTrigger</b>
	 * <p>启动自动催缴的定时任务</p>
	 */
	@RequestMapping("noticeTrigger")
	public RestResponse noticeTrigger(NoticeTriggerCommand cmd) {
		assetService.noticeTrigger(cmd.getNamespaceId());
		RestResponse restResponse = new RestResponse();
		restResponse.setErrorCode(ErrorCodes.SUCCESS);
		restResponse.setErrorDescription("OK");
		return restResponse;
	}
	

}
