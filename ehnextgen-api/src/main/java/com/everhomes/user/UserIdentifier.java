// @formatter:off
package com.everhomes.user;

import com.everhomes.server.schema.tables.pojos.EhUserIdentifiers;
import com.everhomes.util.StringHelper;

public class UserIdentifier extends EhUserIdentifiers {
    private static final long serialVersionUID = -5892650127110591232L;

    public UserIdentifier() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
