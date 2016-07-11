// @formatter:off
package com.everhomes.pkg;

import com.everhomes.server.schema.tables.pojos.EhClientPackages;
import com.everhomes.util.StringHelper;

public class ClientPackage extends EhClientPackages{
    private static final long serialVersionUID = 6655877917359446185L;

    public ClientPackage() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
