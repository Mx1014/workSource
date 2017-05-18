// @formatter:off
package com.everhomes.user;

import com.everhomes.server.schema.tables.pojos.EhUserNotificationSettings;
import com.everhomes.util.StringHelper;

/**
 * Created by xq.tian on 2017/4/18.
 */
public class UserNotificationSetting extends EhUserNotificationSettings {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
