package com.everhomes.general_approval;

import com.alibaba.fastjson.JSON;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.general_form.GeneralFormVal;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.general_approval.PostApprovalFormImageValue;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowCaseFileDTO;
import com.everhomes.rest.flow.FlowCaseFileValue;
import com.everhomes.rest.general_approval.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GeneralApprovalFieldProcessorImpl implements GeneralApprovalFieldProcessor {

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private LocaleStringService localeStringService;

    @Override
    public void processMultiLineTextField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(JSON.parseObject(jsonVal, PostApprovalFormTextValue.class).getText());
        entities.add(e);
    }

    @Override
    public void processDropBoxField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(JSON.parseObject(jsonVal, PostApprovalFormTextValue.class).getText());
        entities.add(e);
    }

    @Override
    public void processImageField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        e.setEntityType(FlowCaseEntityType.IMAGE.getCode());
        PostApprovalFormImageValue imageValue = JSON.parseObject(jsonVal, PostApprovalFormImageValue.class);
        for (String uriString : imageValue.getUris()) {
            String url = this.contentServerService.parserUri(uriString, EntityType.USER.getCode(), UserContext.current().getUser().getId());
            e.setValue(url);
            FlowCaseEntity e2 = ConvertHelper.convert(e, FlowCaseEntity.class);
            entities.add(e2);
        }
    }

    @Override
    public void processFileField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        e.setEntityType(FlowCaseEntityType.FILE.getCode());
        PostApprovalFormFileValue fileValue = JSON.parseObject(jsonVal, PostApprovalFormFileValue.class);
        if (null == fileValue || fileValue.getFiles() == null)
            return;
        List<FlowCaseFileDTO> files = new ArrayList<>();
        for (PostApprovalFormFileDTO dto2 : fileValue.getFiles()) {
            FlowCaseFileDTO fileDTO = new FlowCaseFileDTO();
            String url = this.contentServerService.parserUri(dto2.getUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
            ContentServerResource resource = contentServerService.findResourceByUri(dto2.getUri());
            fileDTO.setUrl(url);
            fileDTO.setFileName(dto2.getFileName());
            fileDTO.setFileSize(resource.getResourceSize());
            files.add(fileDTO);
        }
        FlowCaseFileValue value = new FlowCaseFileValue();
        value.setFiles(files);
        e.setValue(JSON.toJSONString(value));
        entities.add(e);
    }

    @Override
    public void processIntegerTextField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(JSON.parseObject(jsonVal, PostApprovalFormTextValue.class).getText());
        entities.add(e);
    }

    @Override
    public void processSubFormField(List<FlowCaseEntity> entities, GeneralFormFieldDTO dto, String jsonVal){
        PostApprovalFormSubformValue subFormValue = JSON.parseObject(jsonVal, PostApprovalFormSubformValue.class);
        //取出设置的子表单fields
        GeneralFormSubformDTO subFromExtra = JSON.parseObject(dto.getFieldExtra(), GeneralFormSubformDTO.class);
        //给子表单计数从1开始
        int formCount = 1;
        //循环取出每一个子表单值
        for (PostApprovalFormSubformItemValue subForm1 : subFormValue.getForms()) {
            FlowCaseEntity e = new FlowCaseEntity();
            e.setKey(dto.getFieldDisplayName() == null ? dto.getFieldName() : dto.getFieldDisplayName());
            e.setEntityType(FlowCaseEntityType.LIST.getCode());
            e.setValue(formCount++ + "");
            entities.add(e);
            List<GeneralFormVal> subVals = new ArrayList<>();
            //循环取出一个子表单的每一个字段值
            for (PostApprovalFormItem val : subForm1.getValues()) {
                GeneralFormVal subVal = new GeneralFormVal();
                subVal.setFieldName(val.getFieldName());
                subVal.setFieldType(val.getFieldType());
                subVal.setFieldValue(val.getFieldValue());
                subVals.add(subVal);
            }
//            List<FlowCaseEntity> subSingleEntities = new ArrayList<>();
//            processFlowEntities(subSingleEntities, subVals, subFromExtra.getFormFields());
            entities.addAll(processSubFormItemField(subVals, subFromExtra.getFormFields()));
        }
    }

    private List<FlowCaseEntity> processSubFormItemField(List<GeneralFormVal> subVals, List<GeneralFormFieldDTO> fieldDTOs) {
        List<FlowCaseEntity> entities = new ArrayList<>();
        for (GeneralFormVal subVal : subVals) {
            FlowCaseEntity e = new FlowCaseEntity();
            GeneralFormFieldDTO dto = getFieldDTO(subVal.getFieldName(), fieldDTOs);
            if (null == dto) {
                continue;
            }
            e.setKey(dto.getFieldDisplayName() == null ? dto.getFieldName() : dto.getFieldDisplayName());
            //  just process the subForm value (there is no more subForm)
            switch (GeneralFormFieldType.fromCode(subVal.getFieldType())) {
                case SINGLE_LINE_TEXT:
                case NUMBER_TEXT:
                case DATE:
                case DROP_BOX:
                    processDropBoxField(entities, e, subVal.getFieldValue());
                    break;
                case MULTI_LINE_TEXT:
                    processMultiLineTextField(entities, e, subVal.getFieldValue());
                    break;
                case IMAGE:
                    processImageField(entities, e, subVal.getFieldValue());
                    break;
                case FILE:
                    processFileField(entities, e, subVal.getFieldValue());
                    break;
                case INTEGER_TEXT:
                    processIntegerTextField(entities, e, subVal.getFieldValue());
                    break;
                default:
                    break;
            }
        }
        return entities;
    }

    private GeneralFormFieldDTO getFieldDTO(String fieldName, List<GeneralFormFieldDTO> fieldDTOs) {
        for (GeneralFormFieldDTO val : fieldDTOs) {
            if (val.getFieldName().equals(fieldName))
                return val;
        }
        return null;
    }

    @Override
    public void processContactField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        PostApprovalFormContactValue contactValue = JSON.parseObject(jsonVal, PostApprovalFormContactValue.class);
        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.contact.key", "1", "zh_CN", "企业"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(contactValue.getEnterpriseName());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.contact.key", "2", "zh_CN", "楼栋-门牌"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        String addresses = "";

        if (null != contactValue.getAddresses() && contactValue.getAddresses().size() > 0)
            addresses = StringUtils.join(contactValue.getAddresses(), ",");
        e.setValue(addresses);
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.contact.key", "3", "zh_CN", "联系人"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(contactValue.getContactName());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.contact.key", "4", "zh_CN", "联系方式"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(contactValue.getContactNumber());
        entities.add(e);
    }
}
