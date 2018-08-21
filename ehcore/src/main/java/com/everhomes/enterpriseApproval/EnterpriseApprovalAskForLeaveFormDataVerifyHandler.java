package com.everhomes.enterpriseApproval;

import com.alibaba.fastjson.JSON;
import com.everhomes.approval.ApprovalCategory;
import com.everhomes.approval.ApprovalCategoryProvider;
import com.everhomes.general_approval.GeneralApprovalFormDataVerifyHandler;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.approval.ApprovalCategoryReminderFlag;
import com.everhomes.rest.approval.ApprovalCategoryStatus;
import com.everhomes.rest.approval.ApprovalServiceErrorCode;
import com.everhomes.rest.enterpriseApproval.ComponentAskForLeaveValue;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.PostApprovalFormCommand;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.techpark.punch.PunchVacationBalance;
import com.everhomes.techpark.punch.PunchVacationBalanceProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(EnterpriseApprovalAskForLeaveFormDataVerifyHandler.ENTERPRISE_APPROVAL_FORM_DATA_VERIFY)
public class EnterpriseApprovalAskForLeaveFormDataVerifyHandler implements GeneralApprovalFormDataVerifyHandler {

    public static final String ENTERPRISE_APPROVAL_FORM_DATA_VERIFY = GeneralApprovalFormDataVerifyHandler.GENERAL_APPROVAL_FORM_DATA_VERIFY_PREFIX + "ASK_FOR_LEAVE";
    @Autowired
    private ApprovalCategoryProvider approvalCategoryProvider;
    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private PunchVacationBalanceProvider punchVacationBalanceProvider;
    @Autowired
    private LocaleStringService localeStringService;

    @Override
    public void verify(PostApprovalFormCommand cmd) {
        ComponentAskForLeaveValue value = null;
        for (PostApprovalFormItem formItem : cmd.getValues()) {
            if (GeneralFormFieldType.ASK_FOR_LEAVE.getCode().equals(formItem.getFieldType())) {
                value = JSON.parseObject(formItem.getFieldValue(), ComponentAskForLeaveValue.class);
                break;
            }
        }
        if (value == null) {
            throwError(ApprovalServiceErrorCode.APPROVAL_CATEGORY_FORM_DATA_ERROR);
        }
        ApprovalCategory a1 = approvalCategoryProvider.findApprovalCategoryById(value.getRestId());
        if (a1 == null) {
            throwError(ApprovalServiceErrorCode.APPROVAL_CATEGORY_NOT_EXIST);
        }
        ApprovalCategory a2 = null;
        if (a1.getNamespaceId() == 0) {
            a2 = approvalCategoryProvider.findApprovalCategoryByOriginId(value.getRestId(), cmd.getOrganizationId());
        }
        // 兼容旧版本APP,如果是旧版本，对应的是namespace_id=0的基础数据，因此需要通过findApprovalCategoryByOriginId获取到对应公司的类型
        ApprovalCategory approvalCategory = a2 != null ? a2 : a1;
        if (approvalCategory == null || ApprovalCategoryStatus.INACTIVE == ApprovalCategoryStatus.fromCode(approvalCategory.getStatus())
                || ApprovalCategoryStatus.DELETED == ApprovalCategoryStatus.fromCode(approvalCategory.getStatus())) {
            throwError(ApprovalServiceErrorCode.APPROVAL_CATEGORY_UNUSEABLE_ERROR);
        }
        if (ApprovalCategoryReminderFlag.ACTIVE != ApprovalCategoryReminderFlag.fromCode(approvalCategory.getRemainderFlag())) {
            // 没有开启余额关联，不需要校验余额
            return;
        }
        OrganizationMember organizationMember = organizationProvider.findActiveOrganizationMemberByOrgIdAndUId(UserContext.currentUserId(), cmd.getOrganizationId());
        PunchVacationBalance punchVacationBalance = punchVacationBalanceProvider.findPunchVacationBalanceByDetailId(organizationMember.getDetailId());
        if (punchVacationBalance == null) {
            throwError(ApprovalServiceErrorCode.APPROVAL_CATEGORY_VACATION_BALANCE_LIMIT_ERROR);
        }

        if ("ANNUAL_LEAVE".equals(approvalCategory.getHanderType())) {
            if (Double.compare(value.getDuration(), punchVacationBalance.getAnnualLeaveBalance()) > 0) {
                throwError(ApprovalServiceErrorCode.APPROVAL_CATEGORY_VACATION_BALANCE_LIMIT_ERROR);
            }
        }
        if ("WORKING_DAY_OFF".equals(approvalCategory.getHanderType())) {
            if (Double.compare(value.getDuration(), punchVacationBalance.getOvertimeCompensationBalance()) > 0) {
                throwError(ApprovalServiceErrorCode.APPROVAL_CATEGORY_VACATION_BALANCE_LIMIT_ERROR);
            }
        }
    }

    private void throwError(int errorCode) {
        throw RuntimeErrorException
                .errorWith(
                        ApprovalServiceErrorCode.SCOPE,
                        errorCode,
                        localeStringService.getLocalizedString(
                                String.valueOf(ApprovalServiceErrorCode.SCOPE),
                                String.valueOf(errorCode),
                                UserContext.current().getUser().getLocale(),
                                "parameter error"));
    }
}
