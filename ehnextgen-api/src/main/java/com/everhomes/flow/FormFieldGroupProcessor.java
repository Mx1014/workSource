package com.everhomes.flow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * <p>用于组件表单字段</p>
 */
public interface FormFieldGroupProcessor extends FormFieldProcessor {
    default String parseFieldName(Flow flow, String fieldName, String extra) {
        try {
            JSONObject extraObject = JSON.parseObject(extra);
            return (String) extraObject.get("fieldName");
        } catch (Exception e) {
            return fieldName;
        }
    }
}
