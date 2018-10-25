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
import com.everhomes.rest.organization.pm.reportForm.GetTotalBuildingStaticsCommand;
import com.everhomes.rest.organization.pm.reportForm.GetTotalCommunityStaticsCommand;
import com.everhomes.rest.organization.pm.reportForm.ListBuildingReportFormResponse;
import com.everhomes.rest.organization.pm.reportForm.ListCommunityReportFormResponse;
import com.everhomes.rest.organization.pm.reportForm.TotalBuildingStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.TotalCommunityStaticsDTO;

/**
 * 此类用于产生资产（园区、楼宇、房源）相关的报表
 * @author steve
 */
@RestController
@RequestMapping("/pm/reportForm")
public class PropertyReportFormController extends ControllerBase{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyReportFormController.class);

	@Autowired
	private PropertyReportFormService propertyReportFormService;
	
	private PropertyReportFormJob propertyReportFormJob;
	
	/**
     * <b>URL: /pm/reportForm/getCommunityReportForm</b>
     * <p>获取项目信息汇总表</p>
     */
    @RequestMapping("getCommunityReportForm")
    @RestReturn(value=ListCommunityReportFormResponse.class)
    public RestResponse getCommunityReportForm(GetCommunityReportFormCommand cmd) {
    	ListCommunityReportFormResponse result = propertyReportFormService.getCommunityReportForm(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/reportForm/getTotalCommunityStatics</b>
     * <p>获取项目信息汇总表总计</p>
     */
    @RequestMapping("getTotalCommunityStatics")
    @RestReturn(value=TotalCommunityStaticsDTO.class)
    public RestResponse getTotalCommunityStatics(GetTotalCommunityStaticsCommand cmd) {
    	TotalCommunityStaticsDTO result = propertyReportFormService.getTotalCommunityStatics(cmd);
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
    public RestResponse exportCommunityReportForm(GetTotalCommunityStaticsCommand cmd) {
    	propertyReportFormService.exportCommunityReportForm(cmd);
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
    @RestReturn(value=ListBuildingReportFormResponse.class)
    public RestResponse getBuildingReportForm(GetBuildingReportFormCommand cmd) {
    	ListBuildingReportFormResponse result = propertyReportFormService.getBuildingReportForm(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/reportForm/getTotalBuildingStatics</b>
     * <p>获取楼宇信息汇总表总计</p>
     */
    @RequestMapping("getTotalBuildingStatics")
    @RestReturn(value=TotalBuildingStaticsDTO.class)
    public RestResponse getTotalBuildingStatics(GetTotalBuildingStaticsCommand cmd) {
    	TotalBuildingStaticsDTO result = propertyReportFormService.getTotalBuildingStatics(cmd);
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
    public RestResponse exportBuildingReportForm(GetTotalBuildingStaticsCommand cmd) {
    	propertyReportFormService.exportBuildingReportForm(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/reportForm/testReportFormJob</b>
     * <p>测试定时任务</p>
     */
    @RequestMapping("testReportFormJob")
    @RestReturn(value=String.class)
    public RestResponse testReportFormJob() {
    	propertyReportFormJob.generateReportFormStatics();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    
    
}
