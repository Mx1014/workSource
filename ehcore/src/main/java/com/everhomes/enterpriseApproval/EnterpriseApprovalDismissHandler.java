package com.everhomes.enterpriseApproval;

import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseDetail;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.archives.ArchivesOperationType;
import com.everhomes.rest.general_approval.GeneralFormReminderDTO;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(EnterpriseApprovalDismissHandler.ENTERPRISE_APPROVAL_DISMISS_HANDLER_NAME)
public class EnterpriseApprovalDismissHandler implements EnterpriseApprovalHandler {

    static final String ENTERPRISE_APPROVAL_DISMISS_HANDLER_NAME = EnterpriseApprovalHandler.ENTERPRISE_APPROVAL_PREFIX + "DISMISS_APPLICATION";

    @Autowired
    private EnterpriseApprovalService enterpriseApprovalService;

    @Override
    public void onApprovalCreated(FlowCase flowCase) {

    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState flowCase) {

    }


    @Override
    public GeneralFormReminderDTO getGeneralFormReminder(GetTemplateBySourceIdCommand cmd) {
        Long userId = UserContext.currentUserId();
        return enterpriseApprovalService.checkArchivesApproval(userId, cmd.getOwnerId(), cmd.getSourceId(),
                ArchivesOperationType.DISMISS.getCode());
    }
}
