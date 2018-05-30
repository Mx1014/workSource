package com.everhomes.yellowPage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.general_approval.*;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.general_approval.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

@Component(ServiceAllianceApprovalPostHandler.SERVICE_ALLIANCE_APPROVAL_POST)
public class ServiceAllianceApprovalPostHandler implements ApprovalPostValHandler {

    static final String SERVICE_ALLIANCE_APPROVAL_POST = ApprovalPostValHandler.APPROVAL_POST_PREFIX + "service_alliance";

    @Autowired
    private GeneralApprovalProvider generalApprovalProvider;

    @Autowired
    private GeneralApprovalValProvider generalApprovalValProvider;

    @Autowired
    private GeneralFormProvider generalFormProvider;

    @Autowired
    private FlowService flowService;

    @Autowired
    private FlowCaseProvider flowCaseProvider;

    @Autowired
    private YellowPageProvider yellowPageProvider;

    @Autowired
    private DbProvider dbProvider;

    @Override
    public PostGeneralFormDTO postApprovalForm(PostApprovalFormCommand cmd) {
        User user = UserContext.current().getUser();
        GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd.getApprovalId());
        GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(ga.getFormOriginId());
        dbProvider.execute((TransactionStatus status) -> {

            if (form.getStatus().equals(GeneralFormStatus.CONFIG.getCode())) {
            // 使用表单/审批 注意状态 config
            form.setStatus(GeneralFormStatus.RUNNING.getCode());
            this.generalFormProvider.updateGeneralForm(form);
        }
        Flow flow = flowService.getEnabledFlow(ga.getNamespaceId(), 40500L,
                FlowModuleType.NO_MODULE.getCode(), ga.getId(), FlowOwnerType.GENERAL_APPROVAL.getCode());
        CreateFlowCaseCommand cmd21 = new CreateFlowCaseCommand();
        cmd21.setApplyUserId(user.getId());
        cmd21.setReferType(FlowReferType.APPROVAL.getCode());
        cmd21.setReferId(ga.getId());
        cmd21.setProjectType(ga.getOwnerType());
        cmd21.setProjectId(ga.getOwnerId());
        cmd21.setCurrentOrganizationId(cmd.getOrganizationId());
        cmd21.setApplierOrganizationId(cmd.getOrganizationId());
        cmd21.setTitle(ga.getApprovalName());
        ServiceAllianceCategories category = yellowPageProvider.findCategoryById(ga.getModuleId());
        if (category != null) {
            cmd21.setServiceType(category.getName());
        }
        Long flowCaseId = flowService.getNextFlowCaseId();

        // 把values 存起来
        for (PostApprovalFormItem val : cmd.getValues()) {
            GeneralApprovalVal obj = ConvertHelper.convert(ga, GeneralApprovalVal.class);
            obj.setApprovalId(ga.getId());
            obj.setFormVersion(form.getFormVersion());
            obj.setFlowCaseId(flowCaseId);
            obj.setFieldName(val.getFieldName());
            obj.setFieldType(val.getFieldType());
            obj.setFieldStr3(val.getFieldValue());
            this.generalApprovalValProvider.createGeneralApprovalVal(obj);
        }

        FlowCase flowCase;
        if (null == flow) {
            // 给他一个默认哑的flow
            GeneralModuleInfo gm = ConvertHelper.convert(ga, GeneralModuleInfo.class);
            gm.setOwnerId(ga.getId());
            gm.setOwnerType(FlowOwnerType.GENERAL_APPROVAL.getCode());
            cmd21.setFlowCaseId(flowCaseId);
            flowCase = flowService.createDumpFlowCase(gm, cmd21);
            flowCase.setStatus(FlowCaseStatus.FINISHED.getCode());
            flowCaseProvider.updateFlowCase(flowCase);
        } else {
            cmd21.setFlowMainId(flow.getFlowMainId());
            cmd21.setFlowVersion(flow.getFlowVersion());
            cmd21.setFlowCaseId(flowCaseId);
            flowService.createFlowCase(cmd21);
        }
            return null;
        });

        PostGeneralFormDTO dto = ConvertHelper.convert(cmd, PostGeneralFormDTO.class);
        List<PostApprovalFormItem> items = new ArrayList<>();
        PostApprovalFormItem item = new PostApprovalFormItem();
        item.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        item.setFieldName(GeneralFormDataSourceType.CUSTOM_DATA.getCode());
        items.add(item);
        dto.setValues(items);
        return dto;
    }
}
