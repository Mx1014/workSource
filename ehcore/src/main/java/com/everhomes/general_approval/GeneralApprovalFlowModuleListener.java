package com.everhomes.general_approval;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.flow.FlowModuleListener;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GeneralApprovalFlowModuleListener implements FlowModuleListener {
    private static List<String> DEFAULT_FIELDS = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralApprovalFlowModuleListener.class);
    @Autowired
    protected GeneralApprovalFieldProcessor generalApprovalFieldProcessor;
    @Autowired
    protected ContentServerService contentServerService;
    @Autowired
    protected ServiceModuleProvider serviceModuleProvider;
    @Autowired
    protected GeneralApprovalProvider generalApprovalProvider;
    @Autowired
    protected GeneralApprovalValProvider generalApprovalValProvider;
    @Autowired
    protected GeneralFormService generalFormService;
    @Autowired
    protected GeneralFormProvider generalFormProvider;
    @Autowired
    protected LocaleStringService localeStringService;
    @Autowired
    protected OrganizationProvider organizationProvider;

    public GeneralApprovalFlowModuleListener() {
        for (GeneralFormDataSourceType value : GeneralFormDataSourceType.values()) {
            DEFAULT_FIELDS.add(value.getCode());
        }
    }

    protected PostApprovalFormItem getFormFieldDTO(String string, List<PostApprovalFormItem> values) {
        for (PostApprovalFormItem val : values) {
            if (val.getFieldName().equals(string))
                return val;
        }
        return null;
    }

    protected GeneralFormFieldDTO getFieldDTO(String fieldName, List<GeneralFormFieldDTO> fieldDTOs) {
        for (GeneralFormFieldDTO val : fieldDTOs) {
            if (val.getFieldName().equals(fieldName))
                return val;
        }
        return null;
    }

    /**
     * 组装自定义字符串
     */
    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        List<FlowCaseEntity> entities = new ArrayList<>();
        entities.addAll(onFlowCaseCustomDetailRender(flowCase, flowUserType));
        return entities;
    }

    @Override
    public void onFlowButtonFired(FlowCaseState ctx) {

    }

    protected List<FlowCaseEntity> onFlowCaseCustomDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        List<FlowCaseEntity> entities = new ArrayList<>();
        if (flowCase.getReferType().equals(FlowReferType.APPROVAL.getCode())) {
            List<GeneralApprovalVal> vals = this.generalApprovalValProvider
                    .queryGeneralApprovalValsByFlowCaseId(flowCase.getId());
            GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(
                    vals.get(0).getFormOriginId(), vals.get(0).getFormVersion());
            // 模板设定的字段DTOs
            List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(),
                    GeneralFormFieldDTO.class);
            processEntities(entities, vals, fieldDTOs);

        }
        return entities;
    }

    private void processEntities(
            List<FlowCaseEntity> entities, List<GeneralApprovalVal> vals, List<GeneralFormFieldDTO> fieldDTOs) {

        for (GeneralApprovalVal val : vals) {
            try {
                if (!DEFAULT_FIELDS.contains(val.getFieldName())) {
                    // 不在默认fields的就是自定义字符串，组装这些
                    FlowCaseEntity e = new FlowCaseEntity();
                    GeneralFormFieldDTO dto = getFieldDTO(val.getFieldName(), fieldDTOs);
                    if (null == dto) {
                        //  just process the subForm value (there is no more subForm)
                        continue;
                    }
                    e.setKey(dto.getFieldDisplayName() == null ? dto.getFieldName() : dto.getFieldDisplayName());
                    switch (GeneralFormFieldType.fromCode(val.getFieldType())) {
                        case SINGLE_LINE_TEXT:
                        case NUMBER_TEXT:
                        case DATE:
                        case DROP_BOX:
                            generalApprovalFieldProcessor.processDropBoxField(entities, e, val.getFieldStr3());
                            break;
                        case MULTI_LINE_TEXT:
                            generalApprovalFieldProcessor.processMultiLineTextField(entities, e, val.getFieldStr3());
                            break;
                        case IMAGE:
                            generalApprovalFieldProcessor.processImageField(entities, e, val.getFieldStr3());
                            break;
                        case FILE:
                            generalApprovalFieldProcessor.processFileField(entities, e, val.getFieldStr3());
                            break;
                        case INTEGER_TEXT:
                            generalApprovalFieldProcessor.processIntegerTextField(entities, e, val.getFieldStr3());
                            break;
                        case SUBFORM:
                            generalApprovalFieldProcessor.processSubFormField(entities, dto, val.getFieldStr3());
                            break;
                        case CONTACT:
                            //企业联系人
                            generalApprovalFieldProcessor.processContactField(entities, e, val.getFieldStr3());
                            break;
                    }
                }
            } catch (NullPointerException e) {
                LOGGER.error(" ********** 空指针错误  val = " + JSON.toJSONString(val), e);
            } catch (Exception e) {
                LOGGER.error(" ********** 这是什么错误  = " + JSON.toJSONString(val), e);

            }
        }
    }

    @Override
    public FlowModuleInfo initModule() {
        return new FlowModuleInfo();
    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {

    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase, FlowUserType flowUserType) {
        return null;
    }

}
