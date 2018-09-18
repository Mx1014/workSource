package com.everhomes.enterpriseApproval;

import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormReminderCommand;
import com.everhomes.rest.general_approval.GeneralFormReminderDTO;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.techpark.punch.PunchExceptionRequest;

import java.util.List;

public interface EnterpriseApprovalHandler {

    String ENTERPRISE_APPROVAL_PREFIX = "EnterpriseApprovalHandler_";

    /**
     * 访问审批前需要做提示，使用该方法
     */
    default GeneralFormReminderDTO getGeneralFormReminder(GeneralFormReminderCommand cmd){
        return new GeneralFormReminderDTO(TrueOrFalseFlag.FALSE.getCode());}

    /**
     * 返回审批表单内容需要做额外处理时，使用此方法
     */
    default void processFormFields(List<GeneralFormFieldDTO> fieldDTOs, GetTemplateBySourceIdCommand cmd){
    }

    /**
     * 审批创建之后触发调用
     */
    void onApprovalCreated(FlowCase flowCase);

    /**
     * 当用户触发<b>终止</b>按钮或者程序触发 flowCase 终止，使用该方法实现后续操作
     */
    void onFlowCaseAbsorted(FlowCaseState flowCase);

    /**
     * 当流程被删除，使用该方法处理业务数据
     */
    void onFlowCaseDeleted(FlowCase flowCase);

    //  add by wuhan.
    /**
     * 当用户触发按钮或者程序触发 flowCase 正常结束，会触发该方法的调用
     */
    default PunchExceptionRequest onFlowCaseEnd(FlowCase flowCase){return null;}

}
