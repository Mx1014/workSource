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
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

@Component(ServiceAllianceApprovalFormHandler.SERVICE_ALLIANCE_APPROVAL_FROM)
public class ServiceAllianceApprovalFormHandler implements GeneralApprovalFormHandler {

    static final String SERVICE_ALLIANCE_APPROVAL_FROM = GeneralApprovalFormHandler.GENERAL_APPROVAL_FORM_PREFIX + "service_alliance";

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
        FlowCase fc = dbProvider.execute((TransactionStatus status) -> {

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
        // 把command作为json传到content里，给flowcase的listener进行处理
        cmd21.setContent(JSON.toJSONString(cmd));
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
            flowCase = flowService.createFlowCase(cmd21);
        }
            return flowCase;
        });


        GetTemplateByApprovalIdResponse res = new GetTemplateByApprovalIdResponse();
        res.setFlowCaseId(fc.getId());
        PostGeneralFormDTO dto = new PostGeneralFormDTO();
        List<PostApprovalFormItem> items = new ArrayList<>();
        PostApprovalFormItem item = new PostApprovalFormItem();
        item.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        item.setFieldName(GeneralFormDataSourceType.CUSTOM_DATA.getCode());
        item.setFieldValue(JSONObject.toJSONString(res));
        items.add(item);
        dto.setValues(items);
        return dto;
    }

    @Override
    public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {
        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(cmd
                .getSourceId());

        GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(ga
                .getFormOriginId());
        if(form == null )
            throw RuntimeErrorException.errorWith(GeneralApprovalServiceErrorCode.SCOPE,
                    GeneralApprovalServiceErrorCode.ERROR_FORM_NOTFOUND, "form not found");
        form.setFormVersion(form.getFormVersion());
        List<GeneralFormFieldDTO> fieldDTOs = new ArrayList<GeneralFormFieldDTO>();
        fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
        //增加一个隐藏的field 用于存放sourceId
        GeneralFormFieldDTO sourceIdField = new GeneralFormFieldDTO();
        sourceIdField.setDataSourceType(GeneralFormDataSourceType.SOURCE_ID.getCode());
        sourceIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        sourceIdField.setFieldName(GeneralFormDataSourceType.SOURCE_ID.getCode());
        sourceIdField.setRequiredFlag(NormalFlag.NEED.getCode());
        sourceIdField.setDynamicFlag(NormalFlag.NEED.getCode());
        sourceIdField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
        sourceIdField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
        fieldDTOs.add(sourceIdField);

        GeneralFormFieldDTO organizationIdField = new GeneralFormFieldDTO();
        organizationIdField.setDataSourceType(GeneralFormDataSourceType.ORGANIZATION_ID.getCode());
        organizationIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        organizationIdField.setFieldName(GeneralFormDataSourceType.ORGANIZATION_ID.getCode());
        organizationIdField.setRequiredFlag(NormalFlag.NEED.getCode());
        organizationIdField.setDynamicFlag(NormalFlag.NEED.getCode());
        organizationIdField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
        organizationIdField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
        fieldDTOs.add(organizationIdField);

        GeneralFormFieldDTO customField = new GeneralFormFieldDTO();
        customField.setFieldName(GeneralFormDataSourceType.CUSTOM_DATA.getCode());
        customField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        customField.setRequiredFlag(NormalFlag.NONEED.getCode());
        customField.setDynamicFlag(NormalFlag.NONEED.getCode());
        customField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
        customField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
        customField.setDataSourceType(GeneralFormDataSourceType.CUSTOM_DATA.getCode());
        customField.setFieldValue(JSONObject.toJSONString(ga));
        fieldDTOs.add(customField);

        GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
        dto.setFormFields(fieldDTOs);
        return dto;
    }

    @Override
    public GeneralFormReminderDTO getGeneralFormReminder(GeneralFormReminderCommand cmd) {
        return new GeneralFormReminderDTO(TrueOrFalseFlag.FALSE.getCode());
    }
}
