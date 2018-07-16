package com.everhomes.invitation_card;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.PostGeneralFormValCommand;
import com.everhomes.rest.invitation_card.GetInvitationFormCommond;
import com.everhomes.rest.invitation_card.ListInvitationFormResponse;
import com.everhomes.rest.invitation_card.UpdateInvitationActiveStatusCommond;
import com.everhomes.rest.invitation_card.UpdateInvitationApprovalActiveFormCommond;
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
     * <b>URL: /InvitationCard/updateInvitationApprovalActiveForm</b>
     * <p> 根据前端传入的审批项ID : sourceId 和表单ID以及表单版本将该审批下的该表单设为启用 </p>
     * @param cmd
     * @return
     */
    @RequestMapping("updateInvitationApprovalActiveForm")
    @RestReturn(value=String.class)
    public RestResponse updateInvitationApprovalActiveForm(UpdateInvitationApprovalActiveFormCommond cmd) {
        //ListGeneralFormValResponse dto = listInvitationCardFormBySourceId
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /InvitationCard/updateInvitationApprovalActiveStatus</b>
     * <p> 根据前端传入的审批项ID : 更新启用的审批项ID，如果之前有启用的审批项ID则将其停用后，将传入的审批项启用 </p>
     */
    @RequestMapping("updateInvitationApprovalActiveStatus")
    @RestReturn(value=String.class)
    public RestResponse updateInvitationApprovalActiveStatus(UpdateInvitationActiveStatusCommond cmd) {
        //ListGeneralFormValResponse dto = listInvitationCardFormBySourceId
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /InvitationCard/getInvitationForm</b>
     * <p> 前端传入模块ID，后台查找启用的审批流程并根据启用流程查找启用的表单，并返回  </p>
     * @param cmd
     * @return
     */
    @RequestMapping("getInvitationForm")
    @RestReturn(value=GeneralFormDTO.class)
    public RestResponse getInvitationForm(GetInvitationFormCommond cmd) {
        //ListGeneralFormValResponse dto = listInvitationCardFormBySourceId
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }





}
