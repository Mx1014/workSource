// @formatter:off
package com.everhomes.contract;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.contract.*;
import com.everhomes.search.ContractSearcher;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/contract")
public class ContractController extends ControllerBase {
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	@Autowired
	private ContractService contractService;

	@Autowired
	private ContractSearcher contractSearcher;
	
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
	public RestResponse generateContractNumber(){
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		return new RestResponse(contractService.generateContractNumber());
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
	@RestReturn(ContractDTO.class)
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
	 * <p>设置合同参数</p>
	 * <b>URL: /contract/setContractParam</b>
	 */
	@RequestMapping("setContractParam")
	@RestReturn(String.class)
	public RestResponse setContractParam(SetContractParamCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
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
		Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		return new RestResponse(contractService.getContractParam(cmd));
	}

	/**
	 * <p>合同发起审批/合同报废</p>
	 * <b>URL: /contract/reviewContract</b>
	 */
	@RequestMapping("reviewContract")
	@RestReturn(String.class)
	public RestResponse reviewContract(ReviewContractCommand cmd){
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		ContractService contractService = getContractService(namespaceId);
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
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		ContractService contractService = getContractService(namespaceId);
		contractService.entryContract(cmd);
		return new RestResponse();
	}

	private ContractService getContractService(Integer namespaceId) {
		String handler = configurationProvider.getValue(namespaceId, "contractService", "");
		return PlatformContext.getComponent(ContractService.CONTRACT_PREFIX + handler);
	}

}