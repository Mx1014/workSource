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
import com.everhomes.rest.asset.ListBillGroupsDTO;
import com.everhomes.rest.asset.OwnerIdentityCommand;
import com.everhomes.rest.portal.AssetServiceModuleAppDTO;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;

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
	

}
