// @formatter:off
package com.everhomes.statistics.transaction;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.statistics.transaction.ExecuteTaskCommand;
import com.everhomes.rest.statistics.transaction.ListStatServiceSettlementAmountsCommand;
import com.everhomes.rest.statistics.transaction.StatTransactionSettlementDTO;

/**
 * <ul>
 * <li>交易統計api</li>
 * </ul>
 */
@RestDoc(value="Stat transaction controller", site="statTransaction")
@RestController
@RequestMapping("/stat/transaction")
public class StatTransactionController extends ControllerBase {
	
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private OrganizationService organizationService;
    
    /**
     * <b>URL: /stat/transaction/executeTask</b>
     * <p>執行任務</p>
     */
    @RequestMapping("executeTask")
    @RestReturn(value=String.class)
    public RestResponse executeTask(@Valid ExecuteTaskCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/transaction/listStatServiceSettlementAmounts</b>
     * <p>統計服務結算金額</p>
     */
    @RequestMapping("listStatServiceSettlementAmounts")
    @RestReturn(value=StatTransactionSettlementDTO.class, collection=true)
    public RestResponse listStatServiceSettlementAmounts(@Valid ListStatServiceSettlementAmountsCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /stat/transaction/listStatServiceSettlementAmountDetails</b>
     * <p>統計服務結算明細金額</p>
     */
    @RequestMapping("listStatServiceSettlementAmountDetails")
    @RestReturn(value=StatTransactionSettlementDTO.class, collection=true)
    public RestResponse listStatServiceSettlementAmountDetails(@Valid ListStatServiceSettlementAmountsCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /stat/transaction/exportStatServiceSettlementAmounts</b>
     * <p>导出结算数据</p>
     */
    @RequestMapping("exportStatServiceSettlementAmounts")
    @RestReturn(value=String.class)
    public RestResponse exportStatServiceSettlementAmounts(@Valid ListStatServiceSettlementAmountsCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}
