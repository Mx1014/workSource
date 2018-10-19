package com.everhomes.organization.pm.reportForm;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.organization.pm.reportForm.BuildingReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.GetBuildingReportFormCommand;
import com.everhomes.rest.organization.pm.reportForm.GetCommunityReportFormCommand;

/**
 * 此类用于产生资产（园区、楼宇、房源）相关的报表
 * @author steve
 */
@RestController
@RequestMapping("/pm/reportForm")
public class AssetReportFormController extends ControllerBase{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetReportFormController.class);

	@Autowired
	private AssetReportFormService assetReportFormService;
	
	
	/**
     * <b>URL: /pm/reportForm/getCommunityReportForm</b>
     * <p>获取项目信息汇总表</p>
     */
    @RequestMapping("getCommunityReportForm")
    @RestReturn(value=CommunityReportFormDTO.class,collection=true)
    public RestResponse getCommunityReportForm(GetCommunityReportFormCommand cmd) {
    	List<CommunityReportFormDTO> result = assetReportFormService.getCommunityReportForm(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/reportForm/exportCommunityReportForm</b>
     * <p>导出项目信息汇总表</p>
     */
    @RequestMapping("exportCommunityReportForm")
    @RestReturn(value=String.class)
    public RestResponse exportCommunityReportForm(GetCommunityReportFormCommand cmd) {
    	assetReportFormService.exportCommunityReportForm(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/reportForm/getBuildingReportForm</b>
     * <p>获取楼宇信息汇总表</p>
     */
    @RequestMapping("getBuildingReportForm")
    @RestReturn(value=BuildingReportFormDTO.class,collection=true)
    public RestResponse getBuildingReportForm(GetBuildingReportFormCommand cmd) {
    	List<BuildingReportFormDTO> result = assetReportFormService.getBuildingReportForm(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/reportForm/exportBuildingReportForm</b>
     * <p>导出楼宇信息汇总表</p>
     */
    @RequestMapping("exportBuildingReportForm")
    @RestReturn(value=String.class)
    public RestResponse exportBuildingReportForm(GetBuildingReportFormCommand cmd) {
    	assetReportFormService.exportBuildingReportForm(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}
