package com.everhomes.enterpriseApproval;

import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseDetail;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.approval.TrueOrFalseFlag;
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
    private FlowCaseProvider flowCaseProvider;

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

        GeneralFormReminderDTO dto = new GeneralFormReminderDTO();

        dto.setFlag(TrueOrFalseFlag.FALSE.getCode());
        Long userId = UserContext.currentUserId();
        List<FlowCaseDetail> details = enterpriseApprovalService.listActiveFlowCasesByApprovalId(cmd);
        if (details != null && details.size() > 0) {
            dto.setFlag(TrueOrFalseFlag.TRUE.getCode());
            dto.setTitle("还有审批中的人事申请");
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
