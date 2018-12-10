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
import com.everhomes.rest.contract.statistic.ContractStatisticSearchType;
import com.everhomes.rest.contract.statistic.GetTotalContractStaticsCommand;
import com.everhomes.rest.contract.statistic.ListCommunityContractReportFormResponse;
import com.everhomes.rest.contract.statistic.ListContractStaticsTimeDimensionResponse;
import com.everhomes.rest.contract.statistic.SearchContractStaticsListCommand;
import com.everhomes.rest.contract.statistic.TotalContractStaticsDTO;
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
     * <p>合同报表：合同信息汇总表（根据域空间查询所有园区的数据记录总和）</p>
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
     * <p>合同报表：导出合同信息汇总表（根据域空间查询所有园区的数据记录总和）</p>
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
     * <p>合同报表：本月度各项目合同信息汇总表</p>
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
     * <p>合同报表：获取项目在时间维度上的信息汇总表（历史合计列表）</p>
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
     * <p>合同报表：导出月度所有项目合同信息汇总表（月度年度导出计算总计）</p>
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
     * <p>合同报表：导出项目时间维度上的信息汇总表（所有项目的历史合计）</p>
     * <b>URL: /contract/reportForm/exportContractStaticsTimeDimension</b>
     */
    @RequestMapping("exportContractStaticsTimeDimension")
    @RestReturn(value=String.class)
	public RestResponse exportContractStaticsTimeDimension(SearchContractStaticsListCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		//所有项目的汇总历史记录 汇总
		cmd.setSearchType(ContractStatisticSearchType.SUMMARYRECORD.getCode());
		contractService.exportContractStaticsCommunityHistory(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}

    /**
     * <p>合同报表：导出项目时间维度上的信息汇总表加上园区维度（各项目的历史合计）</p>
     * <b>URL: /contract/reportForm/exportContractStaticsCommunityTotal</b>
     */
    @RequestMapping("exportContractStaticsCommunityTotal")
    @RestReturn(value=String.class)
	public RestResponse exportContractStaticsCommunityTotal(SearchContractStaticsListCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		//各项目的历史合计 明细
		cmd.setSearchType(ContractStatisticSearchType.DETAILRECORD.getCode());
		contractService.exportContractStaticsCommunityHistory(cmd);
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
