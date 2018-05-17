package com.everhomes.rest.visitorsys;

import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>fieldType: 字段类型 {@link com.everhomes.rest.general_approval.GeneralFormFieldType}</li>
 * <li>fieldName: 字段名字</li>
 * <li>fieldDisplayName: 字段展示名字</li>
 * <li>fieldValue: 提交的数据
 * 数字值：{@link com.everhomes.rest.general_approval.PostApprovalFormTextValue}
 * 文本值：{@link com.everhomes.rest.general_approval.PostApprovalFormTextValue}
 * 日期值：{@link com.everhomes.rest.general_approval.PostApprovalFormTextValue} 
 * 文本值：{@link com.everhomes.rest.general_approval.PostApprovalFormTextValue}
 * 下拉框值：{@link com.everhomes.rest.general_approval.PostApprovalFormTextValue}
 * 图片值：{@link com.everhomes.rest.general_approval.PostApprovalFormImageValue}
 * 文件值：{@link com.everhomes.rest.general_approval.PostApprovalFormFileValue}
 * 子表单值：{@link com.everhomes.rest.general_approval.PostApprovalFormSubformValue}
 * <li>fieldAttribute: 字段属性 比如：系统字段 {@link com.everhomes.rest.general_approval.GeneralFormFieldAttribute}</li>
 * </li>
 * </ul>
 * @author janson
 *
 */
public class VisitorsysApprovalFormItem extends GeneralFormFieldDTO{
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
