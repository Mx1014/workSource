// @formatter:off
package com.everhomes.locale;

import com.everhomes.server.schema.tables.pojos.EhLocaleTemplates;
import com.everhomes.util.StringHelper;

public class LocaleTemplate extends EhLocaleTemplates {
    private static final long serialVersionUID = -8311929035746438595L;

    public LocaleTemplate() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
