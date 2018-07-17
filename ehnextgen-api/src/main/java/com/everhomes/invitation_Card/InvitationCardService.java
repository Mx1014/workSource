package com.everhomes.invitation_Card;

import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.invitation_card.GetInvitationFormCommond;
import com.everhomes.rest.invitation_card.UpdateInvitationActiveStatusCommond;
import com.everhomes.rest.invitation_card.UpdateInvitationApprovalActiveFormCommond;

public interface InvitationCardService {

    /**
     * <p> 根据前端传入的审批项ID : sourceId 和表单ID以及表单版本将该审批下的该表单设为启用 </p>
     */
    void updateInvitationApprovalActiveForm(UpdateInvitationApprovalActiveFormCommond cmd);


    /**
     * <p> 根据前端传入的审批项ID : 更新启用的审批项ID，如果之前有启用的审批项ID则将其停用后，将传入的审批项启用 </p>
     * @param cmd
     */
    void updateInvitationApprovalActiveStatus(UpdateInvitationActiveStatusCommond cmd);

    /**
     * <p> 前端传入模块ID，后台查找启用的审批流程并根据启用流程查找启用的表单，并返回  </p>
     * @param cmd
     * @return
     */
    GeneralFormDTO getInvitationForm(GetInvitationFormCommond cmd);
}
