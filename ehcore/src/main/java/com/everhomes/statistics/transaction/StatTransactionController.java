// @formatter:off
package com.everhomes.statistics.transaction;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.business.BusinessDTO;
import com.everhomes.rest.statistics.transaction.ExecuteTaskCommand;
import com.everhomes.rest.statistics.transaction.ListStatServiceSettlementAmountsCommand;
import com.everhomes.rest.statistics.transaction.ListStatShopTransactionsResponse;
import com.everhomes.rest.statistics.transaction.ListStatTransactionCommand;
import com.everhomes.rest.statistics.transaction.StatServiceSettlementResultDTO;
import com.everhomes.rest.statistics.transaction.StatShopTransactionDTO;

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
    private StatTransactionService statTransactionService;
    
    /**
     * <b>URL: /stat/transaction/executeStatTask</b>
     * <p>執行任務</p>
     */
    @RequestMapping("executeStatTask")
    @RestReturn(value=String.class)
    public RestResponse executeStatTask(@Valid ExecuteTaskCommand cmd) {
        RestResponse response = new RestResponse(statTransactionService.excuteSettlementTask(cmd.getStartDate(), cmd.getEndDate()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/transaction/listStatServiceSettlementAmounts</b>
     * <p>統計服務結算金額</p>
     */
    @RequestMapping("listStatServiceSettlementAmounts")
    @RestReturn(value=StatServiceSettlementResultDTO.class, collection=true)
    public RestResponse listStatServiceSettlementAmounts(@Valid ListStatServiceSettlementAmountsCommand cmd) {
        RestResponse response = new RestResponse(statTransactionService.listStatServiceSettlementAmounts(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /stat/transaction/listStatServiceSettlementAmountDetails</b>
     * <p>統計服務結算明細金額</p>
     */
    @RequestMapping("listStatServiceSettlementAmountDetails")
    @RestReturn(value=StatServiceSettlementResultDTO.class, collection=true)
    public RestResponse listStatServiceSettlementAmountDetails(@Valid ListStatServiceSettlementAmountsCommand cmd) {
        RestResponse response = new RestResponse(statTransactionService.listStatServiceSettlementAmountDetails(cmd));
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
    public RestResponse exportStatServiceSettlementAmounts(@Valid ListStatServiceSettlementAmountsCommand cmd, HttpServletResponse res) {
    	statTransactionService.exportStatServiceSettlementAmounts(cmd, res);
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /stat/transaction/listStatShopTransactions</b>
     * <p>查询商铺订单</p>
     */
    @RequestMapping("listStatShopTransactions")
    @RestReturn(value=ListStatShopTransactionsResponse.class)
    public RestResponse listStatShopTransactions(@Valid ListStatTransactionCommand cmd) {
    	RestResponse response = new RestResponse(statTransactionService.listStatShopTransactions(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /stat/transaction/exportStatShopTransactions</b>
     * <p>导出商铺订单</p>
     */
    @RequestMapping("exportStatShopTransactions")
    @RestReturn(value=String.class)
    public RestResponse exportStatShopTransactions(@Valid ListStatTransactionCommand cmd, HttpServletResponse res) {
    	statTransactionService.exportStatShopTransactions(cmd, res);
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /stat/transaction/listZuoLinBusinesses</b>
     * <p>左邻店铺</p>
     */
    @RequestMapping("listZuoLinBusinesses")
    @RestReturn(value=BusinessDTO.class, collection = true)
    public RestResponse listZuoLinBusinesses() {
    	RestResponse response = new RestResponse(statTransactionService.listZuoLinBusinesses());
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
