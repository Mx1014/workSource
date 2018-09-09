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
import com.everhomes.rest.asset.CreateBillCommand;
import com.everhomes.rest.asset.CreateGeneralBillCommand;
import com.everhomes.rest.asset.CreateOrUpdateAssetMappingCmd;
import com.everhomes.rest.asset.ListBillGroupsDTO;
import com.everhomes.rest.asset.ListBillsDTO;
import com.everhomes.rest.asset.ListChargingItemsDTO;
import com.everhomes.rest.asset.OwnerIdentityCommand;
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
	
	/**
	 * <p>业务应用新增缴费映射关系</p>
	 * <b>URL: /test/createOrUpdateAssetMapping</b>
	 */
//	@RequestMapping("createOrUpdateAssetMapping")
//	@RestReturn(value = AssetModuleAppMappingDTO.class, collection = false)
//	public RestResponse createOrUpdateAssetMapping(CreateOrUpdateAssetMappingCmd cmd) {
//		AssetModuleAppMapping assetModuleAppMapping = ConvertHelper.convert(cmd, AssetModuleAppMapping.class);
//		AssetModuleAppMapping dto = assetService.createOrUpdateAssetMapping(assetModuleAppMapping);
//		AssetModuleAppMapping assetModuleAppMapping = ConvertHelper.convert(cmd, AssetModuleAppMapping.class);
//	    RestResponse response = new RestResponse(dto);
//	    response.setErrorDescription("OK");
//	    response.setErrorCode(ErrorCodes.SUCCESS);
//	    return response;
//	}
	
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

}
