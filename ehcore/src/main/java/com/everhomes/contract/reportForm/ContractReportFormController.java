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
