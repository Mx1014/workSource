package com.everhomes.pmNotify;

import com.everhomes.server.schema.tables.pojos.EhPmNotifyConfigurations;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/9/12.
 */
public class PmNotifyConfigurations extends EhPmNotifyConfigurations {
    private static final long serialVersionUID = -2887697987172878976L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
