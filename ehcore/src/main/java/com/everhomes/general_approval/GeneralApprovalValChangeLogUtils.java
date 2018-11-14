package com.everhomes.general_approval;

import com.alibaba.fastjson.JSON;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.GeneralFormSubFormValue;
import com.everhomes.rest.general_approval.GeneralFormSubFormValueDTO;
import com.everhomes.rest.general_approval.PostApprovalFormFileDTO;
import com.everhomes.rest.general_approval.PostApprovalFormFileValue;
import com.everhomes.rest.general_approval.PostApprovalFormImageValue;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public final class GeneralApprovalValChangeLogUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralApprovalValChangeLogUtils.class);
    private static final List<String> DEFAULT_FIELDS = new ArrayList<>();

    static {
        for (GeneralFormDataSourceType value : GeneralFormDataSourceType.values()) {
            DEFAULT_FIELDS.add(value.getCode());
        }
    }

    public String changeLog(List<GeneralFormDTO> befores, List<GeneralFormDTO> afters, String contactName) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
            StringBuffer logContent = new StringBuffer(contactName + " 编辑于\n" + sdf.format(new Date()) + "\n\n");

            if (CollectionUtils.isEmpty(befores) || CollectionUtils.isEmpty(afters) || befores.size() != afters.size()) {
                return logContent.toString();
            }

            for (int i = 0; i < befores.size(); i++) {
                String log = changeLog(befores.get(i), afters.get(i));
                if (StringUtils.hasText(log)) {
                    logContent.append(String.format("%s（表%d）\n", befores.get(i).getFormName(), i + 1));
                    logContent.append(log);
                    logContent.append("\n");
                }
            }
            return logContent.toString();
        } catch (Exception e) {
            LOGGER.error(String.format(""),e);
            return "";
        }
    }

    private String changeLog(GeneralFormDTO before, GeneralFormDTO after) {
        StringBuffer logContent = new StringBuffer();
        Map<String, GeneralFormFieldDTO> afterFormFieldMap = generalFormFieldMap(after.getFormFields());
        for (GeneralFormFieldDTO beforeField : before.getFormFields()) {
            if (DEFAULT_FIELDS.contains(beforeField.getFieldName())) {
                continue;
            }
            String log = changeLog(beforeField, afterFormFieldMap.get(beforeField.getFieldName()));
            if (StringUtils.hasText(log)) {
                logContent.append(log);
            }
        }
        return logContent.toString();
    }

    private Map<String, GeneralFormFieldDTO> generalFormFieldMap(List<GeneralFormFieldDTO> fields) {
        if (CollectionUtils.isEmpty(fields)) {
            return new HashMap<>();
        }
        Map<String, GeneralFormFieldDTO> map = new HashMap<>();
        for (GeneralFormFieldDTO field : fields) {
            map.put(field.getFieldName(), field);
        }
        return map;
    }

    private String changeLog(GeneralFormFieldDTO before, GeneralFormFieldDTO after) {
        switch (GeneralFormFieldType.fromCode(before.getFieldType())) {
            case SINGLE_LINE_TEXT:
            case NUMBER_TEXT:
            case DATE:
            case DROP_BOX:
            case MULTI_LINE_TEXT:
            case INTEGER_TEXT:
                return diffSimpleValue(before.getFieldName(), before.getFieldValue(), after.getFieldValue());
            case IMAGE:
                return diffImageValue(before.getFieldName(), before.getFieldValue(), after.getFieldValue());
            case FILE:
                return diffFileValue(before.getFieldName(), before.getFieldValue(), after.getFieldValue());
            case SUBFORM:
                return diffSubForm(before.getFieldName(), before.getFieldValue(), after.getFieldValue());
        }
        return null;
    }

    private String changeLog(String fieldType, String fieldName, String oldValueJson, String postValueJson) {
        switch (GeneralFormFieldType.fromCode(fieldType)) {
            case SINGLE_LINE_TEXT:
            case NUMBER_TEXT:
            case DATE:
            case DROP_BOX:
            case MULTI_LINE_TEXT:
            case INTEGER_TEXT:
                return diffSimpleValue(fieldName, oldValueJson, postValueJson);
            case IMAGE:
                return diffImageValue(fieldName, oldValueJson, postValueJson);
            case FILE:
                return diffFileValue(fieldName, oldValueJson, postValueJson);
            case SUBFORM:
                return diffSubForm(fieldName, oldValueJson, postValueJson);
        }
        return null;
    }

    private String diffSimpleValue(String fieldName, String oldValueJson, String postValueJson) {
        String oldValue = StringUtils.hasText(oldValueJson) ? JSON.parseObject(oldValueJson, PostApprovalFormTextValue.class).getText() : null;
        String postValue = StringUtils.hasText(postValueJson) ? JSON.parseObject(postValueJson, PostApprovalFormTextValue.class).getText() : null;
        if (!StringUtils.hasText(oldValue)) {
            if (StringUtils.hasText(postValue)) {
                return String.format("%s : 设置为 \"%s\"\n", fieldName, postValue);
            }
        } else {
            if (!StringUtils.hasText(postValue)) {
                return String.format("%s : 删除 \"%s\"\n", fieldName, oldValue);
            }
            if (!oldValue.equals(postValue)) {
                return String.format("%s : 由\"%s\" 变更为 \"%s\"\n", fieldName, oldValue, postValue);
            }
        }
        return null;
    }

    private String diffImageValue(String fieldName, String oldValueJson, String postValueJson) {
        StringBuffer log = new StringBuffer();
        PostApprovalFormImageValue oldImages = !StringUtils.hasText(oldValueJson) ? null : JSON.parseObject(oldValueJson, PostApprovalFormImageValue.class);
        PostApprovalFormImageValue postImages = !StringUtils.hasText(postValueJson) ? null : JSON.parseObject(postValueJson, PostApprovalFormImageValue.class);
        if (oldImages != null && !CollectionUtils.isEmpty(oldImages.getUris())) {
            for (int i = 0; i < oldImages.getUris().size(); i++) {
                if (postImages == null || !postImages.getUris().contains(oldImages.getUris().get(i))) {
                    // 兼容老版本没有传imageNames的场景
                    if (!CollectionUtils.isEmpty(oldImages.getImageNames()) && oldImages.getImageNames().size() == oldImages.getUris().size()) {
                        log.append(String.format("%s : 删除 \"%s\"\n", fieldName, oldImages.getImageNames().get(i)));
                    }
                }
            }
        }
        if (postImages != null && !CollectionUtils.isEmpty(postImages.getUris())) {
            for (int i = 0; i < postImages.getUris().size(); i++) {
                if (oldImages == null || !oldImages.getUris().contains(postImages.getUris().get(i))) {
                    // 兼容老版本没有传imageNames的场景
                    if (!CollectionUtils.isEmpty(postImages.getImageNames()) && postImages.getImageNames().size() == postImages.getUris().size()) {
                        log.append(String.format("%s : 添加 \"%s\"\n", fieldName, postImages.getImageNames().get(i)));
                    }
                }
            }
        }
        return log.toString();
    }

    private String diffFileValue(String fieldName, String oldValueJson, String postValueJson) {
        PostApprovalFormFileValue oldFiles = !StringUtils.hasText(oldValueJson) ? null : JSON.parseObject(oldValueJson, PostApprovalFormFileValue.class);
        PostApprovalFormFileValue postFiles = !StringUtils.hasText(postValueJson) ? null : JSON.parseObject(postValueJson, PostApprovalFormFileValue.class);

        StringBuffer log = new StringBuffer();

        if (oldFiles != null && !CollectionUtils.isEmpty(oldFiles.getFiles())) {
            for (PostApprovalFormFileDTO file : oldFiles.getFiles()) {
                if (postFiles == null || !contains(postFiles.getFiles(), file)) {
                    log.append(String.format("%s : 删除 \"%s\"\n", fieldName, file.getFileName()));
                }
            }
        }
        if (postFiles != null && !CollectionUtils.isEmpty(postFiles.getFiles())) {
            for (PostApprovalFormFileDTO file : postFiles.getFiles()) {
                if (oldFiles == null || !contains(oldFiles.getFiles(), file)) {
                    log.append(String.format("%s : 添加 \"%s\"\n", fieldName, file.getFileName()));
                }
            }
        }

        return log.toString();
    }

    String diffSubForm(String fieldName, String oldValueJson, String postValueJson) {
        StringBuffer log = new StringBuffer();
        GeneralFormSubFormValue oldValue = StringUtils.hasText(oldValueJson) ? JSON.parseObject(oldValueJson, GeneralFormSubFormValue.class) : null;
        GeneralFormSubFormValue postValue = StringUtils.hasText(postValueJson) ? JSON.parseObject(postValueJson, GeneralFormSubFormValue.class) : null;
        List<GeneralFormSubFormValueDTO> oldSubForms = (oldValue == null || CollectionUtils.isEmpty(oldValue.getSubForms())) ? new ArrayList<>() : oldValue.getSubForms();
        List<GeneralFormSubFormValueDTO> postSubForms = (postValue == null || CollectionUtils.isEmpty(postValue.getSubForms())) ? new ArrayList<>() : postValue.getSubForms();
        int len = Math.max(oldSubForms.size(), postSubForms.size());
        for (int i = 0; i < len; i++) {
            List<GeneralFormFieldDTO> oldFormFields = oldSubForms.size() > i ? oldSubForms.get(i).getFormFields() : null;
            List<GeneralFormFieldDTO> postFormFields = postSubForms.size() > i ? postSubForms.get(i).getFormFields() : null;
            int len2 = Math.max(CollectionUtils.isEmpty(oldFormFields) ? 0 : oldFormFields.size(), CollectionUtils.isEmpty(postFormFields) ? 0 : postFormFields.size());
            for (int j = 0; j < len2; j++) {
                GeneralFormFieldDTO oldField = (oldFormFields != null && oldFormFields.size() > j) ? oldFormFields.get(j) : null;
                GeneralFormFieldDTO postField = (postFormFields != null && postFormFields.size() > j) ? postFormFields.get(j) : null;
                if (oldField == null && postField == null) {
                    continue;
                }
                String fieldType = oldField != null ? oldField.getFieldType() : postField.getFieldType();
                String subFieldName = String.format("%s%d-%s", fieldName, i + 1, oldField != null ? oldField.getFieldName() : postField.getFieldName());
                String line = changeLog(fieldType, subFieldName, oldField != null ? oldField.getFieldValue() : null, postField != null ? postField.getFieldValue() : null);
                if (StringUtils.hasText(line)) {
                    log.append(line);
                }
            }
        }
        return log.toString();
    }

    private boolean contains(List<PostApprovalFormFileDTO> files, PostApprovalFormFileDTO file) {
        if (CollectionUtils.isEmpty(files)) {
            return false;
        }
        for (PostApprovalFormFileDTO dto : files) {
            if (dto.getUri().equals(file.getUri())) {
                return true;
            }
        }
        return false;
    }
}
