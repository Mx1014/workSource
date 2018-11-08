package com.everhomes.general_form;

import com.alibaba.fastjson.JSON;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.GeneralFormFileValue;
import com.everhomes.rest.general_approval.GeneralFormFileValueDTO;
import com.everhomes.rest.general_approval.GeneralFormImageValue;
import com.everhomes.rest.general_approval.GeneralFormSubFormValue;
import com.everhomes.rest.general_approval.GeneralFormSubFormValueDTO;
import com.everhomes.rest.general_approval.PostApprovalFormFileDTO;
import com.everhomes.rest.general_approval.PostApprovalFormFileValue;
import com.everhomes.rest.general_approval.PostApprovalFormImageValue;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostApprovalFormSubformItemValue;
import com.everhomes.rest.general_approval.PostApprovalFormSubformValue;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.user.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GeneralFormFieldValueProcessorImpl implements GeneralFormFieldValueProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralFormFieldValueProcessorImpl.class);
    @Autowired
    private ContentServerService contentServerService;

    @Override
    public PostApprovalFormItem processDropBoxField(PostApprovalFormItem formVal, String jsonVal, Byte originFieldFlag) {
        if (NormalFlag.NEED.getCode() == originFieldFlag) {
            formVal.setFieldValue(jsonVal);
        } else {
            formVal.setFieldValue(JSON.parseObject(jsonVal, PostApprovalFormTextValue.class).getText());
        }
        return formVal;
    }

    @Override
    public PostApprovalFormItem processMultiLineTextField(PostApprovalFormItem formVal, String jsonVal, Byte originFieldFlag) {
        if (NormalFlag.NEED.getCode() == originFieldFlag) {
            formVal.setFieldValue(jsonVal);
        } else {
            formVal.setFieldValue(JSON.parseObject(jsonVal, PostApprovalFormTextValue.class).getText());
        }
        return formVal;
    }

    @Override
    public PostApprovalFormItem processImageField(PostApprovalFormItem formVal, String jsonVal) {
        PostApprovalFormImageValue imageObj = JSON.parseObject(jsonVal, PostApprovalFormImageValue.class);
        if (null == imageObj || imageObj.getUris() == null)
            return null;
        GeneralFormImageValue imageValue = new GeneralFormImageValue();
        imageValue.setImageNames(imageObj.getImageNames());
        imageValue.setUris(imageObj.getUris());
        List<String> urls = new ArrayList<>();
        for (String uriString : imageObj.getUris()) {
            try {
                String url = this.contentServerService.parserUri(uriString, EntityType.USER.getCode(), UserContext.current().getUser().getId());
                urls.add(url);
            } catch (Exception ex) {
                LOGGER.error("parserUri error", ex);
            }
        }
        imageValue.setUrls(urls);
        formVal.setFieldValue(imageValue.toString());
        return formVal;
    }

    @Override
    public PostApprovalFormItem processFileField(PostApprovalFormItem formVal, String jsonVal) {
        PostApprovalFormFileValue fileObj = JSON.parseObject(jsonVal, PostApprovalFormFileValue.class);
        if (null == fileObj || fileObj.getFiles() == null)
            return null;
        List<GeneralFormFileValueDTO> files = new ArrayList<>();
        for (PostApprovalFormFileDTO dto2 : fileObj.getFiles()) {
            try {
                GeneralFormFileValueDTO fileDTO = new GeneralFormFileValueDTO();
                String url = this.contentServerService.parserUri(dto2.getUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
                ContentServerResource resource = contentServerService.findResourceByUri(dto2.getUri());
                fileDTO.setUri(dto2.getUri());
                fileDTO.setUrl(url);
                fileDTO.setFileName(dto2.getFileName());
                fileDTO.setFileSize(resource.getResourceSize());
                files.add(fileDTO);
            } catch (Exception ex) {
                LOGGER.error("parserUri error", ex);
            }
        }
        GeneralFormFileValue fileValue = new GeneralFormFileValue();
        fileValue.setFiles(files);
        formVal.setFieldValue(fileValue.toString());
        return formVal;
    }

    @Override
    public PostApprovalFormItem processIntegerTextField(PostApprovalFormItem formVal, String jsonVal, Byte originFieldFlag) {
        if (NormalFlag.NEED.getCode() == originFieldFlag) {
            formVal.setFieldValue(jsonVal);
        } else {
            formVal.setFieldValue(JSON.parseObject(jsonVal, PostApprovalFormTextValue.class).getText());
        }
        return formVal;
    }

    @Override
    public PostApprovalFormItem processSubFormField(PostApprovalFormItem formVal, String fieldExtra, String jsonVal, Byte originFieldFlag) {
        //  取出子表单字段值
        PostApprovalFormSubformValue postSubFormValue = JSON.parseObject(jsonVal, PostApprovalFormSubformValue.class);
        List<GeneralFormSubFormValueDTO> subForms = new ArrayList<>();
        //  解析子表单的值
        for (PostApprovalFormSubformItemValue itemValue : postSubFormValue.getForms()) {
            subForms.add(processSubFormItemField(fieldExtra, itemValue, originFieldFlag));
        }
        GeneralFormSubFormValue subFormValue = new GeneralFormSubFormValue();
        subFormValue.setSubForms(subForms);
        formVal.setFieldValue(subFormValue.toString());
        return formVal;
    }

    @Override
    public GeneralFormSubFormValueDTO processSubFormItemField(String extraJson, PostApprovalFormSubformItemValue value, Byte originFieldFlag) {
        //  1.取出子表单字段初始内容
        //  2.将子表单中的值解析
        //  3.将得到的Value放入原有的Extra类中，并组装放入fieldValue中
        GeneralFormSubFormValueDTO result = JSON.parseObject(extraJson, GeneralFormSubFormValueDTO.class);
        Map<String, String> fieldMap = new HashMap<>();

        for (PostApprovalFormItem formVal : value.getValues()) {
            switch (GeneralFormFieldType.fromCode(formVal.getFieldType())) {
                case SINGLE_LINE_TEXT:
                case NUMBER_TEXT:
                case DATE:
                case DROP_BOX:
                    processDropBoxField(formVal, formVal.getFieldValue(), originFieldFlag);
                    break;
                case MULTI_LINE_TEXT:
                    processMultiLineTextField(formVal, formVal.getFieldValue(), originFieldFlag);
                    break;
                case IMAGE:
                    processImageField(formVal, formVal.getFieldValue());
                    break;
                case FILE:
                    processFileField(formVal, formVal.getFieldValue());
                    break;
                case INTEGER_TEXT:
                    processIntegerTextField(formVal, formVal.getFieldValue(), originFieldFlag);
                    break;
                case SUBFORM:
                    break;
            }
            fieldMap.put(formVal.getFieldName(), formVal.getFieldValue());
        }
        for (GeneralFormFieldDTO dto : result.getFormFields())
            dto.setFieldValue(fieldMap.get(dto.getFieldName()));
        return result;
    }
}
