package com.everhomes.invitation_card;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.general_approval.PostGeneralFormValCommand;
import com.everhomes.rest.invitation_card.ListInvitationFormResponse;
import com.everhomes.rest.invitation_card.updateInvitationApprovalActiveFormCommond;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestDoc(value="Invitation Card controller", site="core")
@RestController
@RequestMapping("/InvitationCard")
public class InvitationCardController {

    /**
     * <b>URL: /InvitationCard/listInvitationCardFormBySourceId </b>
     * <p> 根据审批信息列出所有请示单表单,返回的表单列表带有启用队列 </p>
     */
    @RequestMapping("listInvitationFormBySourceId")
    @RestReturn(value=ListInvitationFormResponse.class)
    public RestResponse listInvitationFormBySourceId(PostGeneralFormValCommand cmd) {
        //ListInvitationFormResponse dto = listInvitationCardFormBySourceId
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: 更新审批项启用的表单</b>
     * <p> 根据前端传入的审批项ID : sourceId 和表单ID以及表单版本将该审批下的该表单设为启用 </p>
     * @param cmd
     * @return
     */
    @RequestMapping("updateInvitationApprovalActiveForm")
    @RestReturn(value=String.class)
    public RestResponse updateInvitationApprovalActiveForm(updateInvitationApprovalActiveFormCommond cmd) {
        //ListGeneralFormValResponse dto = listInvitationCardFormBySourceId
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


}
