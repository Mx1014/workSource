// @formatter:off
package com.everhomes.contract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.contract.ListContractsByOraganizationIdCommand;
import com.everhomes.rest.contract.ListContractsCommand;
import com.everhomes.rest.contract.ListContractsResponse;

@RestController
@RequestMapping("/contract")
public class ContractController extends ControllerBase {
	
	@Autowired
	private ContractService contractService;
	
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
}