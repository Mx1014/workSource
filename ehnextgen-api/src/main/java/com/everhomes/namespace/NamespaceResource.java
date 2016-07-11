// @formatter:off
package com.everhomes.namespace;

import com.everhomes.server.schema.tables.pojos.EhNamespaceResources;
import com.everhomes.util.StringHelper;

public class NamespaceResource extends EhNamespaceResources {
    private static final long serialVersionUID = -1733028032033126228L;

    public NamespaceResource() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
