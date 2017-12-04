package com.everhomes.flow.formfield;

import com.everhomes.rest.general_approval.GeneralFormFieldType;
import org.springframework.stereotype.Component;

/**
 * Created by xq.tian on 2017/11/3.
 */
@Component
public class FormFieldMultiLineTextProcessor extends FormFieldSingleLineTextProcessor {

    @Override
    public GeneralFormFieldType getSupportFieldType() {
        return GeneralFormFieldType.MULTI_LINE_TEXT;
    }
}
