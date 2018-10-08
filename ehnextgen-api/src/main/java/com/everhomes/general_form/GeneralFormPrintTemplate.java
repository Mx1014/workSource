// @formatter:off
package com.everhomes.general_form;

import com.everhomes.server.schema.tables.pojos.EhGeneralFormPrintTemplates;
import com.everhomes.util.StringHelper;

public class GeneralFormPrintTemplate extends EhGeneralFormPrintTemplates{
    private static final long serialVersionUID = 4342070191667305585L;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    public String gogsPath() {
        return this.getName() + ".txt";
    }
}
