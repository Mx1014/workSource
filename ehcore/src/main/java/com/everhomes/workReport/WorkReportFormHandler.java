package com.everhomes.workReport;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.general_form.*;
import com.everhomes.rest.general_approval.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + "WORK_REPORT")
public class WorkReportFormHandler implements GeneralFormModuleHandler {

    @Autowired
    private WorkReportProvider workReportProvider;

    @Autowired
    private WorkReportValProvider workReportValProviderl;

    @Autowired
    private GeneralFormProvider generalFormProvider;

    @Autowired
    private GeneralFormValProvider generalFormValProvider;

    private final String WORK_REPORT_VAL = "work_report_val";

    @Override
    public PostGeneralFormDTO postGeneralForm(PostGeneralFormCommand cmd) {

        //  find the corresponding report and the form
        WorkReportVal reportVal = workReportValProviderl.getWorkReportValById(cmd.getSourceId());
        WorkReport report = workReportProvider.getWorkReportById(reportVal.getReportId());
        GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(report.getFormOriginId());
        if (cmd.getValues() != null && cmd.getValues().size() > 0) {
            for (PostApprovalFormItem val :  cmd.getValues()) {
                GeneralFormVal obj = new GeneralFormVal();
                obj.setNamespaceId(UserContext.getCurrentNamespaceId());
                obj.setOrganizationId(cmd.getCurrentOrganizationId());
                obj.setOwnerId(cmd.getOwnerId());
                obj.setOwnerType(cmd.getOwnerType());
                obj.setModuleId(report.getModuleId());
                obj.setModuleType(report.getModuleType());
                obj.setSourceId(reportVal.getId());
                obj.setSourceType(WORK_REPORT_VAL);
                obj.setFormOriginId(form.getFormOriginId());
                obj.setFormVersion(form.getFormVersion());
                obj.setFieldName(val.getFieldName());
                obj.setFieldType(val.getFieldType());
                obj.setFieldValue(val.getFieldValue());
                generalFormValProvider.createGeneralFormVal(obj);
            }
        }
        PostGeneralFormDTO dto = ConvertHelper.convert(cmd, PostGeneralFormDTO.class);
        return dto;
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
}
