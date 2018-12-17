package com.everhomes.general_form;

import com.everhomes.server.schema.tables.pojos.EhGeneralFormFieldsConfig;
import com.everhomes.util.StringHelper;

/**
 * @author huqi
 */
public class GeneralFormFieldsConfig extends EhGeneralFormFieldsConfig {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
