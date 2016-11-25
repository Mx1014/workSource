// @formatter:off
package com.everhomes.statistics.terminal;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.business.BusinessDTO;
import com.everhomes.rest.statistics.transaction.*;
import com.everhomes.statistics.transaction.StatTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * <ul>
 * <li>终端統計api</li>
 * </ul>
 */
@RestDoc(value="Stat terminal controller", site="statTerminal")
@RestController
@RequestMapping("/stat/terminal")
public class StatTerminalController extends ControllerBase {
	
    @Autowired
    private StatTransactionService statTransactionService;
    
//    /**
//     * <b>URL: /stat/transaction/executeStatTask</b>
//     * <p>執行任務</p>
//     */
//    @RequestMapping("executeStatTask")
//    @RestReturn(value=String.class)
//    public RestResponse executeStatTask(@Valid ExecuteTaskCommand cmd) {
//        RestResponse response = new RestResponse(statTransactionService.excuteSettlementTask(cmd.getStartDate(), cmd.getEndDate()));
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }

}
