package com.everhomes.invitation_Card;

import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.rest.general_approval.GeneralFormDTO;

public interface InvitationCardProvider {

    /**
     * 根据域空间和moudleId获取正在运行的审批
     * @param moduleId
     * @param namespaceId
     * @return
     */
    GeneralApproval getRunnintInvitationApproval(Long moduleId, Integer namespaceId);

}
