// @formatter:off
package com.everhomes.whitelist;

import com.everhomes.server.schema.tables.pojos.EhPhoneWhiteList;
import com.everhomes.util.StringHelper;

public class PhoneWhiteList extends EhPhoneWhiteList{

    private static final long serialVersionUID = 1828405863222275142L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
