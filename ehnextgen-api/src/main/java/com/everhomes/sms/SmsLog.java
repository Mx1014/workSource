package com.everhomes.sms;

import com.everhomes.server.schema.tables.pojos.EhSmsLogs;
import com.everhomes.util.StringHelper;

/**
 * Created by Administrator on 2017/3/27.
 */
public class SmsLog extends EhSmsLogs {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
