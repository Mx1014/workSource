// @formatter:off
package com.everhomes.contract;

import com.everhomes.rest.contract.*;
import com.everhomes.search.ContractSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestController
@RequestMapping("/contract")
public class ContractController extends ControllerBase {
	
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
		return new RestResponse(contractService.listContracts(cmd));
	}

	/**
	 * <p>2.获取某organization的合同列表</p>
	 * <b>URL: /contract/listContractsByOraganizationId</b>
	 */
	@RequestMapping("listContractsByOraganizationId")
	@RestReturn(ListContractsResponse.class)
	public RestResponse listContractsByOraganizationId(ListContractsByOraganizationIdCommand cmd){
		return new RestResponse(contractService.listContractsByOraganizationId(cmd));
	}

	/**
	 * <p>搜索合同</p>
	 * <b>URL: /contract/searchContracts</b>
	 */
	@RequestMapping("searchContracts")
	@RestReturn(ListContractsResponse.class)
	public RestResponse searchContracts(SearchContractCommand cmd){
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
	 * <p>创建合同</p>
	 * <b>URL: /contract/createContract</b>
	 */
	@RequestMapping("createContract")
	@RestReturn(ContractDetailDTO.class)
	public RestResponse createContract(CreateContractCommand cmd){
		return new RestResponse(contractService.createContract(cmd));
	}

	/**
	 * <p>修改合同</p>
	 * <b>URL: /contract/updateContract</b>
	 */
	@RequestMapping("updateContract")
	@RestReturn(ContractDTO.class)
	public RestResponse updateContract(UpdateContractCommand cmd){
		return new RestResponse(contractService.updateContract(cmd));
	}

	/**
	 * <p>退约合同</p>
	 * <b>URL: /contract/denunciationContract</b>
	 */
	@RequestMapping("denunciationContract")
	@RestReturn(String.class)
	public RestResponse denunciationContract(DenunciationContractCommand cmd){
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
		ContractDetailDTO detail = contractService.findContract(cmd);
		return new RestResponse(detail);
	}

	/**
	 * <p>设置合同参数</p>
	 * <b>URL: /contract/setContractParam</b>
	 */
	@RequestMapping("setContractParam")
	@RestReturn(String.class)
	public RestResponse setContractParam(SetContractParamCommand cmd){
		return new RestResponse();
	}

	/**
	 * <p>查看合同参数</p>
	 * <b>URL: /contract/findContractParam</b>
	 */
	@RequestMapping("findContractParam")
	@RestReturn(ContractParamDTO.class)
	public RestResponse findContractParam(FindContractParamCommand cmd){
		return new RestResponse();
	}

	/**
	 * <p>合同审批</p>
	 * <b>URL: /contract/reviewContract</b>
	 */
	@RequestMapping("reviewContract")
	@RestReturn(String.class)
	public RestResponse reviewContract(ReviewContractCommand cmd){
		return new RestResponse();
	}

	/**
	 * <p>合同入场</p>
	 * <b>URL: /contract/entryContract</b>
	 */
	@RequestMapping("entryContract")
	@RestReturn(String.class)
	public RestResponse entryContract(EntryContractCommand cmd){
		return new RestResponse();
	}
}