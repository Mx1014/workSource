package com.everhomes.investment;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.general_approval.GeneralFormValDTO;
import com.everhomes.rest.general_approval.GetGeneralFormValCommand;
import com.everhomes.rest.investment.CreateContactCommand;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestDoc(value="investment enterprise controller", site="core")
@RestController
@RequestMapping("/investment")
public class InvestmentEnterpriseController {

    /**
     * <b>URL: /investment/createContact</b>
     * <p> 新建联系人 </p>
     */
    @RequestMapping("createContact")
    @RestReturn(value=Long.class)
    public RestResponse createContact(CreateContactCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
