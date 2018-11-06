// @formatter:off
package com.everhomes.contract;


import java.util.List;

import javax.validation.Valid;

import com.everhomes.rest.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.customer.SyncDataTaskService;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.contract.AddContractTemplateCommand;
import com.everhomes.rest.contract.CheckAdminCommand;
import com.everhomes.rest.contract.ContractDTO;
import com.everhomes.rest.contract.ContractDetailDTO;
import com.everhomes.rest.contract.ContractEventDTO;
import com.everhomes.rest.contract.ContractParamDTO;
import com.everhomes.rest.contract.CreateContractCommand;
import com.everhomes.rest.contract.DeleteContractCommand;
import com.everhomes.rest.contract.DeleteContractTemplateCommand;
import com.everhomes.rest.contract.DenunciationContractCommand;
import com.everhomes.rest.contract.EnterpriseContractCommand;
import com.everhomes.rest.contract.EnterpriseContractDTO;
import com.everhomes.rest.contract.DurationParamDTO;
import com.everhomes.rest.contract.EntryContractCommand;
import com.everhomes.rest.contract.FindContractCommand;
import com.everhomes.rest.contract.GenerateContractNumberCommand;
import com.everhomes.rest.contract.GetContractParamCommand;
import com.everhomes.rest.contract.GetContractTemplateDetailCommand;
import com.everhomes.rest.contract.GetDurationParamCommand;
import com.everhomes.rest.contract.ListApartmentContractsCommand;
import com.everhomes.rest.contract.ListContractEventsCommand;
import com.everhomes.rest.contract.ListContractTemplatesResponse;
import com.everhomes.rest.contract.ListContractsByOraganizationIdCommand;
import com.everhomes.rest.contract.ListContractsBySupplierCommand;
import com.everhomes.rest.contract.ListContractsBySupplierResponse;
import com.everhomes.rest.contract.ListContractsCommand;
import com.everhomes.rest.contract.ListContractsResponse;
import com.everhomes.rest.contract.ListCustomerContractsCommand;
import com.everhomes.rest.contract.ListEnterpriseCustomerContractsCommand;
import com.everhomes.rest.contract.ListIndividualCustomerContractsCommand;
import com.everhomes.rest.contract.PrintPreviewPrivilegeCommand;
import com.everhomes.rest.contract.ReviewContractCommand;
import com.everhomes.rest.contract.SearchContractCommand;
import com.everhomes.rest.contract.SetContractParamCommand;
import com.everhomes.rest.contract.SetPrintContractTemplateCommand;
import com.everhomes.rest.contract.SyncContractsFromThirdPartCommand;
import com.everhomes.rest.contract.UpdateContractCommand;
import com.everhomes.rest.contract.UpdateContractTemplateCommand;
import com.everhomes.rest.contract.listContractTemplateCommand;
import com.everhomes.search.ContractSearcher;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;

@RestController
@RequestMapping("/contract")
public class ContractController extends ControllerBase {
	
	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private ContractSearcher contractSearcher;

	@Autowired
	private SyncDataTaskService syncDataTaskService;
	
	/**
	 * <p>1.合同列表</p>
	 * <b>URL: /contract/listContracts</b>
	 */
	@RequestMapping("listContracts")
	@RestReturn(ListContractsResponse.class)
	public RestResponse listContracts(ListContractsCommand cmd){
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		return new RestResponse(contractService.listContracts(cmd));
	}

	/**
	 * <p>2.获取某organization的合同列表</p>
	 * <b>URL: /contract/listContractsByOraganizationId</b>
	 */
	@RequestMapping("listContractsByOraganizationId")
	@RestReturn(ListContractsResponse.class)
	public RestResponse listContractsByOraganizationId(ListContractsByOraganizationIdCommand cmd){
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		return new RestResponse(contractService.listContractsByOraganizationId(cmd));
	}

	/**
	 * <p>搜索合同</p>
	 * <b>URL: /contract/searchContracts</b>
	 */
	@RequestMapping("searchContracts")
	@RestReturn(ListContractsResponse.class)
	public RestResponse searchContracts(SearchContractCommand cmd){
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		if (namespaceId == 999971) {
			ContractService contractService = getContractService(namespaceId);
			ListContractsCommand command = ConvertHelper.convert(cmd, ListContractsCommand.class);
			return new RestResponse(contractService.listContracts(command));
		}
		cmd.setPaymentFlag((byte)0);
		return new RestResponse(contractSearcher.queryContracts(cmd));
	}

	/**
	 * <p>同步合同</p>
	 * <b>URL: /contract/syncContracts</b>
	 */
	@RequestMapping("syncContracts")
	@RestReturn(String.class)
	public RestResponse syncContracts(){
		contractSearcher.syncFromDb();
		return new RestResponse();
	}

	/**
	 * <p>生成合同编号</p>
	 * <b>URL: /contract/generateContractNumber</b>
	 */
	@RequestMapping("generateContractNumber")
	@RestReturn(String.class)
	public RestResponse generateContractNumber(GenerateContractNumberCommand cmd){
		ContractService contractService = getContractService(cmd.getNamespaceId());
		return new RestResponse(contractService.generateContractNumber(cmd));
	}

	/**
	 * <p>创建合同</p>
	 * <b>URL: /contract/createContract</b>
	 */
	@RequestMapping("createContract")
	@RestReturn(ContractDetailDTO.class)
	public RestResponse createContract(CreateContractCommand cmd){
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		return new RestResponse(contractService.createContract(cmd));
	}

	/**
	 * <p>修改合同</p>
	 * <b>URL: /contract/updateContract</b>
	 */
	@RequestMapping("updateContract")
	@RestReturn(ContractDetailDTO.class)
	public RestResponse updateContract(UpdateContractCommand cmd){
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		return new RestResponse(contractService.updateContract(cmd));
	}

	/**
	 * <p>删除合同</p>
	 * <b>URL: /contract/deleteContract</b>
	 */
	@RequestMapping("deleteContract")
	@RestReturn(String.class)
	public RestResponse deleteContract(DeleteContractCommand cmd){
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		contractService.deleteContract(cmd);
		return new RestResponse();
	}

	/**
	 * <p>退约合同</p>
	 * <b>URL: /contract/denunciationContract</b>
	 */
	@RequestMapping("denunciationContract")
	@RestReturn(String.class)
	public RestResponse denunciationContract(DenunciationContractCommand cmd){
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		contractService.denunciationContract(cmd);
		return new RestResponse();
	}

	/**
	 * <p>查看合同详情</p>
	 * <b>URL: /contract/findContract</b>
	 */
	@RequestMapping("findContract")
	@RestReturn(ContractDetailDTO.class)
	public RestResponse findContract(FindContractCommand cmd){
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		ContractDetailDTO detail = contractService.findContract(cmd);
		return new RestResponse(detail);
	}

	/**
	 * <p>查看合同详情</p>
	 * <b>URL: /contract/findContractForApp</b>
	 */
	@RequestMapping("findContractForApp")
	@RestReturn(ContractDetailDTO.class)
	public RestResponse findContractForApp(FindContractCommand cmd){
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		ContractDetailDTO detail = contractService.findContractForApp(cmd);
		return new RestResponse(detail);
	}

	/**
	 * <p>查看客户合同</p>
	 * <b>URL: /contract/listCustomerContracts</b>
	 */
	@RequestMapping("listCustomerContracts")
	@RestReturn(value = ContractDTO.class, collection = true)
	public RestResponse listCustomerContracts(ListCustomerContractsCommand cmd){
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		return new RestResponse(contractService.listCustomerContracts(cmd));
	}

	/**
	 * <p>查看企业客户合同</p>
	 * <b>URL: /contract/listEnterpriseCustomerContracts</b>
	 */
	@RequestMapping("listEnterpriseCustomerContracts")
	@RestReturn(value = ContractDTO.class, collection = true)
	public RestResponse listEnterpriseCustomerContracts(ListEnterpriseCustomerContractsCommand cmd){
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		return new RestResponse(contractService.listEnterpriseCustomerContracts(cmd));
	}

	/**
	 * <p>查看个人客户合同</p>
	 * <b>URL: /contract/listIndividualCustomerContracts</b>
	 */
	@RequestMapping("listIndividualCustomerContracts")
	@RestReturn(value = ContractDTO.class, collection = true)
	public RestResponse listIndividualCustomerContracts(ListIndividualCustomerContractsCommand cmd){
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		return new RestResponse(contractService.listIndividualCustomerContracts(cmd));
	}

	/**
	 * <p>查看门牌的合同</p>
	 * <b>URL: /contract/listApartmentContracts</b>
	 */
	@RequestMapping("listApartmentContracts")
	@RestReturn(value = ContractDTO.class, collection = true)
	public RestResponse listApartmentContracts(ListApartmentContractsCommand cmd){
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		return new RestResponse(contractService.listApartmentContracts(cmd));
	}

	/**
	 * <p>设置合同参数</p>
	 * <b>URL: /contract/setContractParam</b>
	 */
	@RequestMapping("setContractParam")
	@RestReturn(String.class)
	public RestResponse setContractParam(SetContractParamCommand cmd) {
//		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(cmd.getNamespaceId());
		contractService.setContractParam(cmd);
		return new RestResponse();
	}

	/**
	 * <p>查看合同参数</p>
	 * <b>URL: /contract/getContractParam</b>
	 */
	@RequestMapping("getContractParam")
	@RestReturn(ContractParamDTO.class)
	public RestResponse getContractParam(GetContractParamCommand cmd){
//		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(cmd.getNamespaceId());
		return new RestResponse(contractService.getContractParam(cmd));
	}

	/**
	 * <p>合同发起审批/合同报废</p>
	 * <b>URL: /contract/reviewContract</b>
	 */
	@RequestMapping("reviewContract")
	@RestReturn(String.class)
	public RestResponse reviewContract(ReviewContractCommand cmd){
//		Integer namespaceId = UserContext.getCurrentNamespaceId();
		ContractService contractService = getContractService(cmd.getNamespaceId());
		contractService.reviewContract(cmd);
		return new RestResponse();
	}

	/**
	 * <p>合同入场</p>
	 * <b>URL: /contract/entryContract</b>
	 */
	@RequestMapping("entryContract")
	@RestReturn(String.class)
	public RestResponse entryContract(EntryContractCommand cmd){
//		Integer namespaceId = UserContext.getCurrentNamespaceId();
		ContractService contractService = getContractService(cmd.getNamespaceId());
		contractService.entryContract(cmd);
		return new RestResponse();
	}

	/**
	 * <p>判断是否是管理员</p>
	 * <b>URL: /contract/checkAdmin</b>
	 */
	@RequestMapping("checkAdmin")
	@RestReturn(Boolean.class)
	public RestResponse checkAdmin(CheckAdminCommand cmd){
		ContractService contractService = getContractService(cmd.getNamespaceId());
		return new RestResponse(contractService.checkAdmin(cmd));
	}

	/**
	 * <b>URL: /contract/syncContractsFromThirdPart</b>
	 * <p>从第三方同步合同</p>
	 */
	@RequestMapping("syncContractsFromThirdPart")
	@RestReturn(value = String.class)
	public RestResponse syncContractsFromThirdPart(@Valid SyncContractsFromThirdPartCommand cmd) {
		ContractService contractService = getContractService(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));
		RestResponse response = new RestResponse(contractService.syncContractsFromThirdPart(cmd, false));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	private ContractService getContractService(Integer namespaceId) {
		String handler = configurationProvider.getValue(namespaceId, "contractService", "");
		return PlatformContext.getComponent(ContractService.CONTRACT_PREFIX + handler);
	}

	//通过供应商查看合同 -- by wentian
	/**
	 * <b>URL: /contract/listContractsBySupplier</b>
	 * <p>通过供应商查看合同列表</p>
	 */
	@RequestMapping("listContractsBySupplier")
	@RestReturn(value = ListContractsBySupplierResponse.class)
	public RestResponse listContractsBySupplier(ListContractsBySupplierCommand cmd) {
		ContractService contractService = getContractService(UserContext.getCurrentNamespaceId(0));
		ListContractsBySupplierResponse res = contractService.listContractsBySupplier(cmd);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
    /**
     * <b>URL: /contract/exportContractListByCommunityCategoryId</b>
     * <p>合同列表导出</p>
     */
	@RequestMapping("exportContractListByCommunityCategoryId")
    @RestReturn(value = String.class)
    public RestResponse exportContractListByCommunityCategoryId(SearchContractCommand cmd) {
    	ContractService contractService = getContractService(UserContext.getCurrentNamespaceId(0));
    	contractService.exportContractListByCommunityCategoryId(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
	/**
	 * <p>设置合同模板,新增模板,复制模板，修改通用模板</p>
	 * <b>URL: /contract/addContractTemplate</b>
	 */
	@RequestMapping("addContractTemplate")
	@RestReturn(String.class)
	public RestResponse addContractTemplate(AddContractTemplateCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		return new RestResponse(contractService.addContractTemplate(cmd));
		
	}
	
	/**
	 * <p>园区修改模板，园区模板版本递增</p>
	 * <b>URL: /contract/updateContractTemplate</b>
	 */
	@RequestMapping("updateContractTemplate")
	@RestReturn(String.class)
	public RestResponse updateContractTemplate(UpdateContractTemplateCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		
		return new RestResponse(contractService.updateContractTemplate(cmd));
		
	}
	
	/**
	 * <p>合同模板列表，关键字查询</p>
	 * <b>URL: /contract/searchContractTemplates</b>
	 */
	@RequestMapping("searchContractTemplates")
	@RestReturn(ListContractTemplatesResponse.class)
	public RestResponse searchContractTemplates(listContractTemplateCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		return new RestResponse(contractService.searchContractTemplates(cmd));
		
	}
	
	/**
	 * <p>没有关联合同模板，打印预览选择合同模板</p>
	 * <b>URL: /contract/setPrintContractTemplate</b>
	 */
	@RequestMapping("setPrintContractTemplate")
	@RestReturn(String.class)
	public RestResponse setPrintContractTemplate(SetPrintContractTemplateCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		
		return new RestResponse(contractService.setPrintContractTemplate(cmd));
		
	}
	
	/**
	 * <p>删除选择过的合同模板</p>
	 * <b>URL: /contract/setPrintContractTemplate</b>
	 */
	@RequestMapping("deletePrintContractTemplate")
	@RestReturn(String.class)
	public RestResponse deletePrintContractTemplate(SetPrintContractTemplateCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		contractService.deletePrintContractTemplate(cmd);
		return new RestResponse();
	}
	
	/**
	 * <p>获取合同详情，以及模板详情信息</p>
	 * <b>URL: /contract/getContractTemplateDetail</b>
	 */
	@RequestMapping("getContractTemplateDetail")
	@RestReturn(String.class)
	public RestResponse getContractTemplateDetail(GetContractTemplateDetailCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		
		return new RestResponse(contractService.getContractTemplateDetail(cmd));
		
	}
	/**
	 * <p>删除模板</p>
	 * <b>URL: /contract/deleteContractTemplate</b>
	 */
	@RequestMapping("deleteContractTemplate")
	@RestReturn(String.class)
	public RestResponse deleteContractTemplate(DeleteContractTemplateCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		contractService.deleteContractTemplate(cmd);
		
		return new RestResponse();
	}
	
	/**
	 * <p>校验打印预览权限</p>
	 * <b>URL: /contract/checkPrintPreviewprivilege</b>
	 */
	@RequestMapping("checkPrintPreviewprivilege")
	@RestReturn(value = Long.class, collection = true)
	public RestResponse checkPrintPreviewprivilege(PrintPreviewPrivilegeCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		return new RestResponse(contractService.checkPrintPreviewprivilege(cmd));
	}
	
	//查看合同日志  by tangcen
	/**
	 * <b>URL: /contract/getDuration</b>
	 * <p>查找合同截断时的账单时间段</p>
	 */
	@RequestMapping("getDuration")
	@RestReturn(value = DurationParamDTO.class)
	public RestResponse getDuration(GetDurationParamCommand cmd) {
		ContractService contractService = getContractService(UserContext.getCurrentNamespaceId(0));
		DurationParamDTO res = contractService.getDuration(cmd);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /contract/listContractEvents</b>
	 * <p>查看合同日志</p>
	 */
	@RequestMapping("listContractEvents")
	@RestReturn(value = ContractEventDTO.class,collection=true)
	public RestResponse listContractEvents(ListContractEventsCommand cmd) {
		ContractService contractService = getContractService(UserContext.getCurrentNamespaceId(0));
		List<ContractEventDTO> result = contractService.listContractEvents(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /contract/filterAptitudeCustomer</b>
	 */
	@RequestMapping("filterAptitudeCustomer")
	@RestReturn(value = String.class)
	public RestResponse filterAptitudeCustomer(FilterAptitudeCustomerCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		Byte AptitudeFlag = contractService.filterAptitudeCustomer(cmd);
		RestResponse response = new RestResponse(AptitudeFlag);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /contract/listSyncErrorMsg</b>
	 * <p>查看导入数据错误日志</p>
	 */
	@RequestMapping("listSyncErrorMsg")
	@RestReturn(value = ListSyncDataErrorMsgResponse.class,collection=true)
	public RestResponse listSyncErrorMsg(ListSyncErrorMsgCommand cmd) {
		ListSyncDataErrorMsgResponse result = syncDataTaskService.listSyncErrorMsg(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /contract/updateAptitudeCustomer</b>
	 */
	@RequestMapping("updateAptitudeCustomer")
	@RestReturn(value = AptitudeCustomerFlagDTO.class)
	public RestResponse updateAptitudeCustomer(UpdateContractAptitudeFlagCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		AptitudeCustomerFlagDTO flag = contractService.updateAptitudeCustomer(cmd);
		RestResponse response = new RestResponse(flag);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /contract/exportContractSyncErrorMsg</b>
	 * <p>导出数据错误日志</p>
	 */
	@RequestMapping("exportContractSyncErrorMsg")
	@RestReturn(value = String.class)
	public RestResponse exportContractSyncErrorMsg(@Valid SearchContractCommand cmd) {
		ContractService contractService = getContractService(cmd.getNamespaceId());
		contractService.exportContractListByContractList(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /contract/EnterpriseContractDetail</b>
	 * <p>企业客户APP获取合同详情</p>
	 */
	@RequestMapping("EnterpriseContractDetail")
	@RestReturn(EnterpriseContractDTO.class)
	public RestResponse EnterpriseContractDetail(EnterpriseContractCommand cmd){
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		EnterpriseContractDTO detail = contractService.EnterpriseContractDetail(cmd);
		return new RestResponse(detail);
	}
	
	/**
	 * <b>URL: /contract/getContractCategoryList</b>
	 * <p>获取合同应用的列表</p>
	 */
	@RequestMapping("getContractCategoryList")
	@RestReturn(value = ContractCategoryListDTO.class, collection = true)
	public RestResponse getContractCategoryList(ContractCategoryCommand cmd){
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		List<ContractCategoryListDTO> categoryList = contractService.getContractCategoryList(cmd);
		return new RestResponse(categoryList);
	}

	/**
	 * <b>URL: /contract/dealBillsGeneratedByDenunciationContract</b>
	 * <p>删除历史退约合同产生的多余账单</p>
	 */
	@RequestMapping("dealBillsGeneratedByDenunciationContract")
	@RestReturn(String.class)
	public RestResponse dealBillsGeneratedByDenunciationContract(DenunciationContractBillsCommand cmd){
		ContractService contractService = getContractService(cmd.getNamespaceId());
		contractService.dealBillsGeneratedByDenunciationContract(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

}