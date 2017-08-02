// @formatter:off
package com.everhomes.user;

import com.everhomes.server.schema.tables.pojos.EhUserAppealLogs;
import com.everhomes.util.StringHelper;

/**
 * Created by xq.tian on 2017/6/27.
 */
public class UserAppealLog extends EhUserAppealLogs {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
