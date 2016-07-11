// @formatter:off
package com.everhomes.namespace;

import com.everhomes.server.schema.tables.pojos.EhNamespaceDetails;
import com.everhomes.util.StringHelper;

public class NamespaceDetail extends EhNamespaceDetails {
    private static final long serialVersionUID = 759557813540590358L;

    public NamespaceDetail() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
