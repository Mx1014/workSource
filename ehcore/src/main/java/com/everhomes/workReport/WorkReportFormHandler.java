package com.everhomes.workReport;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.general_form.*;
import com.everhomes.rest.general_approval.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + "WORK_REPORT")
public class WorkReportFormHandler implements GeneralFormModuleHandler {

    @Autowired
    private WorkReportProvider workReportProvider;

    @Autowired
    private WorkReportValProvider workReportValProvider;

    @Autowired
    private GeneralFormProvider generalFormProvider;

    @Autowired
    private GeneralFormValProvider generalFormValProvider;

    private final String WORK_REPORT_VAL = "work_report_val";

    @Override
    public PostGeneralFormDTO postGeneralFormVal(PostGeneralFormValCommand cmd) {
        //  find the corresponding report and the form
        WorkReportVal reportVal = workReportValProvider.getWorkReportValById(cmd.getSourceId());
        WorkReport report = workReportProvider.getWorkReportById(reportVal.getReportId());
        GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(report.getFormOriginId());
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        if (cmd.getValues() != null && cmd.getValues().size() > 0) {
            if (form.getStatus().equals(GeneralFormStatus.CONFIG.getCode())) {
                // take care of the form's status, change it before update it.
                form.setStatus(GeneralFormStatus.RUNNING.getCode());
                generalFormProvider.updateGeneralForm(form);
            }
            for (PostApprovalFormItem value : cmd.getValues()) {
                GeneralFormVal obj = packageGeneralFormVal(namespaceId, cmd.getCurrentOrganizationId(),
                        report.getOwnerId(), report.getOwnerType(), report, reportVal, form, value);
                generalFormValProvider.createGeneralFormVal(obj);
            }
        }
        PostGeneralFormDTO dto = ConvertHelper.convert(cmd, PostGeneralFormDTO.class);
        return dto;
    }

    private GeneralFormVal packageGeneralFormVal(
            Integer namespaceId, Long organizationId, Long ownerId, String ownerType, WorkReport report,
            WorkReportVal reportVal, GeneralForm form, PostApprovalFormItem value) {
        GeneralFormVal obj = new GeneralFormVal();
        obj.setNamespaceId(namespaceId);
        obj.setOrganizationId(organizationId);
        obj.setOwnerId(ownerId);
        obj.setOwnerType(ownerType);
        obj.setModuleId(report.getModuleId());
        obj.setModuleType(report.getModuleType());
        obj.setSourceId(reportVal.getId());
        obj.setSourceType(WORK_REPORT_VAL);
        obj.setFormOriginId(form.getFormOriginId());
        obj.setFormVersion(form.getFormVersion());
        obj.setFieldName(value.getFieldName());
        obj.setFieldType(value.getFieldType());
        obj.setFieldValue(value.getFieldValue());
        return obj;
    }

    @Override
    public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {
        WorkReport report = workReportProvider.getWorkReportById(cmd.getSourceId());
        GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(report.getFormOriginId());
        form.setFormVersion(form.getFormVersion());
        List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
        GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
        dto.setFormFields(fieldDTOs);
        return dto;
    }

    @Override
    public PostGeneralFormDTO updateGeneralFormVal(PostGeneralFormValCommand cmd) {
        //  find the corresponding report and the form
        WorkReportVal reportVal = workReportValProvider.getWorkReportValById(cmd.getSourceId());
        WorkReport report = workReportProvider.getWorkReportById(reportVal.getReportId());
        GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(report.getFormOriginId());
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        //  record those fields.
        if (cmd.getValues() != null && cmd.getValues().size() > 0) {
            List<String> fieldNameScope = new ArrayList<>();
            for (PostApprovalFormItem value : cmd.getValues()) {
                GeneralFormVal obj = generalFormValProvider.getGeneralFormValBySourceIdAndName(cmd.getSourceId(), WORK_REPORT_VAL, value.getFieldName());
                if (obj == null) {
                    //  1.create new fields
                    obj = packageGeneralFormVal(namespaceId, cmd.getCurrentOrganizationId(),
                            report.getOwnerId(), report.getOwnerType(), report, reportVal, form, value);
                    generalFormValProvider.createGeneralFormVal(obj);
                } else {
                    //  2.update old fields
                    obj.setFieldValue(value.getFieldValue());
                    obj.setFormOriginId(form.getFormOriginId());
                    obj.setFormVersion(form.getFormVersion());
                    generalFormValProvider.updateGeneralFormVal(obj);
                }
                fieldNameScope.add(value.getFieldName());
            }

            //  3.delete fields which not contain in the scope
            generalFormValProvider.deleteGeneralFormValNotInFieldNameScope(reportVal.getId(), WORK_REPORT_VAL, fieldNameScope);
        }
        PostGeneralFormDTO dto = ConvertHelper.convert(cmd, PostGeneralFormDTO.class);
        return dto;
    }
}
