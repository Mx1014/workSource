package com.everhomes.configurations.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.configurations.ConfigurationsService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.configurations.admin.ConfigurationsAdminCommand;
import com.everhomes.rest.configurations.admin.ConfigurationsAdminDTO;
import com.everhomes.rest.configurations.admin.ConfigurationsCreateAdminCommand;
import com.everhomes.rest.configurations.admin.ConfigurationsIdAdminCommand;
import com.everhomes.rest.configurations.admin.ConfigurationsIdAdminDTO;
import com.everhomes.rest.configurations.admin.ConfigurationsUpdateAdminCommand;

/**
 * configurations management Controller
 * @author huanglm
 *
 */
@RestDoc(value="Configurations Admin controller", site="core")
@RestController
@RequestMapping("/admin/configurations")
public class ConfigurationsAdminController extends ControllerBase{
	
	@Autowired
    private ConfigurationsService configurationsService;
	

	/**
     * <b>URL: /admin/configurations/listConfigurations</b>
     * <p>查询配置项信息</p>
     */
	@RequestMapping("listConfigurations")
    @RestReturn(value=ConfigurationsAdminDTO.class)
	public RestResponse listConfigurations(ConfigurationsAdminCommand cmd) {
		
		ConfigurationsAdminDTO resultDTO = configurationsService.listConfigurations(cmd);
        RestResponse response = new RestResponse(resultDTO);
        setResponseSuccess(response);
        return response;
	}

	/**
     * <b>URL: /admin/configurations/getConfigurationById</b>
     * <p>通过主键查询配置项信息</p>
     */
	@RequestMapping("getConfigurationById")
    @RestReturn(value=ConfigurationsIdAdminDTO.class)
	public RestResponse getConfigurationById(ConfigurationsIdAdminCommand cmd) {
		
		ConfigurationsIdAdminDTO resultDTO = configurationsService.getConfigurationById(cmd);
		RestResponse response = new RestResponse(resultDTO);
        setResponseSuccess(response);
        return response;
	}

	/**
     * <b>URL: /admin/configurations/crteateConfiguration</b>
     * <p>创建配置项信息</p>
     */
	@RequestMapping("crteateConfiguration")
    @RestReturn(value=String.class)
	public RestResponse crteateConfiguration(ConfigurationsCreateAdminCommand cmd) {
		
		configurationsService.crteateConfiguration(cmd);
		RestResponse response = new RestResponse();
        setResponseSuccess(response);
        return response;
	}


	/**
     * <b>URL: /admin/configurations/updateConfiguration</b>
     * <p>修改配置项信息</p>
     */
	@RequestMapping("updateConfiguration")
    @RestReturn(value=String.class)
	public RestResponse updateConfiguration(ConfigurationsUpdateAdminCommand cmd) {
		
		configurationsService.updateConfiguration(cmd);
		RestResponse response = new RestResponse();
        setResponseSuccess(response);
        return response;
	}


	/**
     * <b>URL: /admin/configurations/deleteConfiguration</b>
     * <p>删除配置项信息</p>
     */
	@RequestMapping("deleteConfiguration")
    @RestReturn(value=String.class)
	public RestResponse deleteConfiguration(ConfigurationsIdAdminCommand cmd) {
		
		configurationsService.deleteConfiguration(cmd);
		RestResponse response = new RestResponse();
        setResponseSuccess(response);
        return response;
	}
	
	/**
	 * <p>设置response 成功信息</p>
	 * @param response
	 */
	private void setResponseSuccess(RestResponse response){
		if(response == null ) return ;
		
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
	}
}
