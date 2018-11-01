package com.everhomes.general_form;

import com.everhomes.rest.general_approval.GeneralFormSubFormValueDTO;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostApprovalFormSubformItemValue;

public interface GeneralFormFieldValueProcessor {
    PostApprovalFormItem processDropBoxField(PostApprovalFormItem formVal, String jsonVal, Byte originFieldFlag);

    PostApprovalFormItem processMultiLineTextField(PostApprovalFormItem formVal, String jsonVal, Byte originFieldFlag);

    PostApprovalFormItem processImageField(PostApprovalFormItem formVal, String jsonVal);

    PostApprovalFormItem processFileField(PostApprovalFormItem formVal, String jsonVal);

    PostApprovalFormItem processIntegerTextField(PostApprovalFormItem formVal, String jsonVal, Byte originFieldFlag);

    PostApprovalFormItem processSubFormField(PostApprovalFormItem formVal, String fieldExtra, String jsonVal, Byte originFieldFlag);

    GeneralFormSubFormValueDTO processSubFormItemField(String extraJson, PostApprovalFormSubformItemValue value, Byte originFieldFlag);
}
