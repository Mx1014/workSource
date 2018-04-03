// @formatter:off
package com.everhomes.sms;

import com.everhomes.server.schema.tables.pojos.EhSmsBlackLists;
import com.everhomes.util.StringHelper;

/**
 * Created by xq.tian on 2017/7/4.
 */
public class SmsBlackList extends EhSmsBlackLists {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
