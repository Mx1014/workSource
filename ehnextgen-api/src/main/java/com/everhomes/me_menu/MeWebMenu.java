// @formatter:off
package com.everhomes.me_menu;

import com.everhomes.server.schema.tables.pojos.EhMeWebMenus;
import com.everhomes.util.StringHelper;

public class MeWebMenu extends EhMeWebMenus {

    private static final long serialVersionUID = -4818739203436994920L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
