package com.everhomes.enterpriseApproval;

import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseDetail;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalTemplateCode;
import com.everhomes.rest.flow.FlowCaseSearchType;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.SearchFlowCaseCommand;
import com.everhomes.rest.general_approval.GeneralFormReminderDTO;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(EnterpriseApprovalEmployHandler.ENTERPRISE_APPROVAL_EMPLOY_HANDLER_NAME)
public class EnterpriseApprovalEmployHandler implements EnterpriseApprovalHandler{

    static final String ENTERPRISE_APPROVAL_EMPLOY_HANDLER_NAME = EnterpriseApprovalHandler.ENTERPRISE_APPROVAL_PREFIX + "EMPLOY_APPLICATION";

    @Autowired
    private EnterpriseApprovalService enterpriseApprovalService;

    @Autowired
    private LocaleStringService localeStringService;

    @Autowired
    private GeneralApprovalProvider generalApprovalProvider;

    @Override
    public void onApprovalCreated(FlowCase flowCase) {

    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState flowCase) {

    }

    @Override
    public GeneralFormReminderDTO getGeneralFormReminder(GetTemplateBySourceIdCommand cmd) {

        GeneralFormReminderDTO dto = new GeneralFormReminderDTO();
        dto.setFlag(TrueOrFalseFlag.FALSE.getCode());

        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(cmd.getSourceId());
        Long userId = UserContext.currentUserId();
        List<FlowCaseDetail> details = enterpriseApprovalService.listActiveFlowCasesByApprovalId(cmd.getOwnerId(), cmd.getSourceId());
        if (details != null && details.size() > 0) {
            dto.setFlag(TrueOrFalseFlag.TRUE.getCode());
            dto.setTitle(localeStringService.getLocalizedString(EnterpriseApprovalTemplateCode.SCOPE, EnterpriseApprovalTemplateCode.APPROVAL_TITLE, "zh_CN", "Remind"));
            dto.setContent("您的转正申请正在审批中\n" +
                    "\n" +
                    "现在发起申请将使该申请作废\n" +
                    "\n" +
                    "确定仍要提交吗？");
            return dto;
        }
        return dto;
    }
}
