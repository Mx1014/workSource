package com.everhomes.contract.reportForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contract.ContractService;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.contract.GetTotalContractStaticsCommand;
import com.everhomes.rest.contract.ListCommunityContractReportFormResponse;
import com.everhomes.rest.contract.ListContractStaticsTimeDimensionResponse;
import com.everhomes.rest.contract.SearchContractStaticsListCommand;
import com.everhomes.rest.contract.TotalContractStaticsDTO;
import com.everhomes.user.UserContext;

/**
 * 此类用于产生合同相关的报表
 * @author djm
 */
@RestController
@RequestMapping("/contract/reportForm")
public class ContractReportFormController extends ControllerBase{
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	private ContractService getContractService(Integer namespaceId) {
		String handler = configurationProvider.getValue(namespaceId, "contractService", "");
		return PlatformContext.getComponent(ContractService.CONTRACT_PREFIX + handler);
	}
	/**
     * <p>合同报表：获取项目信息汇总表</p>
     * <b>URL: /contract/reportForm/searchContractStaticsList</b>
     */
    @RequestMapping("searchContractStaticsList")
    @RestReturn(value = ListCommunityContractReportFormResponse.class)
	public RestResponse searchContractStaticsList(SearchContractStaticsListCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		ListCommunityContractReportFormResponse contractStaticsList = contractService.searchContractStaticsList(cmd);
		RestResponse response = new RestResponse(contractStaticsList);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
    
    /**
     * <p>合同报表：获取项目信息汇总表</p>
     * <b>URL: /contract/reportForm/getTotalContractStatics</b>
     */
    @RequestMapping("getTotalContractStatics")
    @RestReturn(value = TotalContractStaticsDTO.class)
	public RestResponse getTotalContractStatics(GetTotalContractStaticsCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		TotalContractStaticsDTO contractStaticsList = contractService.getTotalContractStatics(cmd);
		RestResponse response = new RestResponse(contractStaticsList);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
    
	/**
     * <p>合同报表：获取项目在时间维度上的信息汇总表</p>
     * <b>URL: /contract/reportForm/contractStaticsListTimeDimension</b>
     */
    @RequestMapping("contractStaticsListTimeDimension")
    @RestReturn(value = ListContractStaticsTimeDimensionResponse.class)
	public RestResponse contractStaticsListTimeDimension(SearchContractStaticsListCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		ListContractStaticsTimeDimensionResponse contractStaticsList = contractService.contractStaticsListTimeDimension(cmd);
		RestResponse response = new RestResponse(contractStaticsList);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
    
    /**
     * <p>合同报表：导出项目信息汇总表</p>
     * <b>URL: /contract/reportForm/exportContractStaticsInfo</b>
     */
    @RequestMapping("exportContractStaticsInfo")
    @RestReturn(value=String.class)
	public RestResponse exportContractStaticsInfo(GetTotalContractStaticsCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		contractService.exportContractStaticsInfo(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}
    
    /**
     * <p>合同报表：导出项目时间维度上的信息汇总表</p>
     * <b>URL: /contract/reportForm/exportContractStaticsTimeDimension</b>
     */
    @RequestMapping("exportContractStaticsTimeDimension")
    @RestReturn(value=String.class)
	public RestResponse exportContractStaticsTimeDimension(SearchContractStaticsListCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		contractService.exportContractStaticsTimeDimension(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}
    
    /**
     * <p>合同报表：导出项目汇总表总的数据信息</p>
     * <b>URL: /contract/reportForm/exportContractStaticsTotal</b>
     */
    @RequestMapping("exportContractStaticsTotal")
    @RestReturn(value=String.class)
	public RestResponse exportContractStaticsTotal(GetTotalContractStaticsCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		contractService.exportContractStaticsTotal(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}
    /**
     * <p>合同报表：导出项目时间维度上的信息汇总表加上园区维度</p>
     * <b>URL: /contract/reportForm/exportContractStaticsCommunityTotal</b>
     */
    @RequestMapping("exportContractStaticsCommunityTotal")
    @RestReturn(value=String.class)
	public RestResponse exportContractStaticsCommunityTotal(SearchContractStaticsListCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		contractService.exportContractStaticsCommunityTotal(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}
    
	/**
     * <p>合同报表：定时任务手动</p>
     * <b>URL: /contract/reportForm/excuteContractReportFormJob</b>
     */
    @RequestMapping("excuteContractReportFormJob")
    @RestReturn(value=String.class)
    public RestResponse excuteDoorAccessSchedule(GetTotalContractStaticsCommand cmd) {
    	ContractService contractService = getContractService(1111111);
		contractService.generateReportFormStatics(cmd);
        RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
    }
    
}
