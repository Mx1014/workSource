package com.everhomes.enterprise_approval;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.enterprise_approval.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/enterprise_approval")
public class EnterpriseApprovalController extends ControllerBase{

    @Autowired
    private EnterpriseApprovalService enterpriseApprovalService;

    /**
     * <b>URL: /enterprise_approval/listEnterpriseApprovals</b>
     * <p>OA审批列表</p>
     */
    @RequestMapping("listEnterpriseApprovals")
    @RestReturn(value = ListEnterpriseApprovalsResponse.class)
    public RestResponse addWorkReport(@Valid ListEnterpriseApprovalsCommand cmd){
        ListEnterpriseApprovalsResponse res = enterpriseApprovalService.listEnterpriseApprovals(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
