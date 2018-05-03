// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
  *<ul>
 * <li>formConfig: (选填)表单配置，{@link VisitorsysFormConfig}</li>
  *</ul>
  */

public class GetEnterpriseFormForWebResponse {
    private VisitorsysFormConfig formConfig;

    public VisitorsysFormConfig getFormConfig() {
        return formConfig;
    }

    public void setFormConfig(VisitorsysFormConfig formConfig) {
        this.formConfig = formConfig;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
