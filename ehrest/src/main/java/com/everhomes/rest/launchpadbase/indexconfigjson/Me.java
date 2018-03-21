package com.everhomes.rest.launchpadbase.indexconfigjson;

import com.everhomes.rest.user.MyPublishFlag;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>myPublishFlag: "我"-"我的发布"是否显示，参考{@link MyPublishFlag}</li>
 * </ul>
 */
public class Me {

    private Byte myPublishFlag;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
