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

    public Byte getMyPublishFlag() {
        return myPublishFlag;
    }

    public void setMyPublishFlag(Byte myPublishFlag) {
        this.myPublishFlag = myPublishFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
