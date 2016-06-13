// @formatter:off
package com.everhomes.wifi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.wifi.CreateWifiSettingCommand;
import com.everhomes.rest.wifi.DeleteWifiSettingCommand;
import com.everhomes.rest.wifi.ListWifiSettingCommand;
import com.everhomes.rest.wifi.EditWifiSettingCommand;
import com.everhomes.rest.wifi.ListWifiSettingResponse;
import com.everhomes.rest.wifi.VerifyWifiCommand;
import com.everhomes.rest.wifi.VerifyWifiDTO;
import com.everhomes.rest.wifi.WifiSettingDTO;

@RestController
@RequestMapping("/wifi")
public class WifiController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(WifiController.class);

	@Autowired
	private WifiService wifiService;

	/**
	 * <b>URL: /wifi/listWifiSetting</b>
	 * <p>获取wifi列表</p>
	 */
	@RequestMapping("listWifiSetting")
	@RestReturn(value=ListWifiSettingResponse.class)
	public RestResponse listWifiSetting(ListWifiSettingCommand cmd) {
		ListWifiSettingResponse resp = wifiService.listWifiSetting(cmd);
		RestResponse response = new RestResponse(resp);

		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /wifi/createWifiSetting</b>
	 * <p>新增wifi</p>
	 */
	@RequestMapping("createWifiSetting")
	@RestReturn(value=WifiSettingDTO.class)
	public RestResponse createWifiSetting(CreateWifiSettingCommand cmd) {
		WifiSettingDTO dto = wifiService.createWifiSetting(cmd);
		RestResponse response = new RestResponse(dto);

		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /wifi/deleteWifiSetting</b>
	 * <p>删除wifi</p>
	 */
	@RequestMapping("deleteWifiSetting")
	@RestReturn(value=WifiSettingDTO.class)
	public RestResponse deleteWifiSetting(DeleteWifiSettingCommand cmd) {
		wifiService.deleteWifiSetting(cmd);
		RestResponse response = new RestResponse();

		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /wifi/editWifiSetting</b>
	 * <p>编辑wifi</p>
	 */
	@RequestMapping("editWifiSetting")
	@RestReturn(value=WifiSettingDTO.class)
	public RestResponse editWifiSetting(EditWifiSettingCommand cmd) {
		WifiSettingDTO dto = wifiService.editWifiSetting(cmd);
		RestResponse response = new RestResponse(dto);

		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /wifi/verifyWifi</b>
	 * <p>判断该wifi是否在配置列表当中</p>
	 */
	@RequestMapping("verifyWifi")
	@RestReturn(value=VerifyWifiDTO.class)
	public RestResponse verifyWifi(VerifyWifiCommand cmd) {
		VerifyWifiDTO dto = wifiService.verifyWifi(cmd);
		RestResponse response = new RestResponse(dto);

		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

}
