// @formatter:off
package com.everhomes.group;

import com.everhomes.server.schema.tables.pojos.EhGroupVisibleScopes;
import com.everhomes.util.StringHelper;

public class GroupVisibilityScope extends EhGroupVisibleScopes {
    private static final long serialVersionUID = 1467045088361297623L;

    public GroupVisibilityScope() {
    }
    
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
