// @formatter:off
package com.everhomes.rest.visitorsys.ui;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.visitorsys.VisitorsysFormConfig;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
  *<ul>
 * <li>formConfig: (选填)表单配置列表，{@link com.everhomes.rest.general_approval.GeneralFormFieldDTO}</li>
  *</ul>
  */

public class GetEnterpriseFormResponse {
    @ItemType(GeneralFormFieldDTO.class)
    private List<GeneralFormFieldDTO> formConfig;

    public List<GeneralFormFieldDTO> getFormConfig() {
        return formConfig;
    }

    public void setFormConfig(List<GeneralFormFieldDTO> formConfig) {
        this.formConfig = formConfig;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
