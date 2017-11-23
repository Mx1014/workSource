// @formatter:off
package com.everhomes.sms;

import com.everhomes.server.schema.tables.pojos.EhYzxSmsLogs;
import com.everhomes.util.StringHelper;

/**
 * Created by xq.tian on 2017/7/11.
 */
public class YzxSmsLog extends EhYzxSmsLogs {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
