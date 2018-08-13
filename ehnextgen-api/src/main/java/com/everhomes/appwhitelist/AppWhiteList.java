// @formatter:off
package com.everhomes.appwhitelist;

import com.everhomes.server.schema.tables.pojos.EhAppWhiteList;
import com.everhomes.util.StringHelper;

public class AppWhiteList extends EhAppWhiteList{

    private static final long serialVersionUID = 3588481890534796694L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
