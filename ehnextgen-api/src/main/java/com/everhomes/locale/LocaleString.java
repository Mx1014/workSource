// @formatter:off
package com.everhomes.locale;

import com.everhomes.server.schema.tables.pojos.EhLocaleStrings;
import com.everhomes.util.StringHelper;

public class LocaleString extends EhLocaleStrings {
    private static final long serialVersionUID = -1760364542066209765L;

    public LocaleString() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
