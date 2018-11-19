package com.everhomes.openapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.contract.ListContractsResponse;
import com.everhomes.rest.contract.OpenapiListContractsCommand;
import com.everhomes.search.ContractSearcher;
import com.everhomes.user.UserContext;

@RestDoc(value = "ZTContract Constroller", site = "core")
@RestController
@RequestMapping("/openapi/contract")
public class ZTContractController extends ControllerBase {
    
	@Autowired
	private ContractSearcher contractSearcher;

    /**
     * <b>URL: /openapi/contract/listContracts</b>
     * <p>中天对接，查看合同列表（只传租赁合同） </p>
     * @return
     */
    @RequestMapping("listContracts")
    @RestReturn(ListContractsResponse.class)
    public RestResponse listContracts(OpenapiListContractsCommand cmd){
        
        Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
        cmd.setPaymentFlag((byte)0);
        cmd.setNamespaceId(namespaceId);
        RestResponse response = new RestResponse(contractSearcher.openapiListContracts(cmd));
    	response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
