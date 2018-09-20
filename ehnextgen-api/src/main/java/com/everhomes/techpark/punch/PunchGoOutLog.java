package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchGoOutLogs;
import com.everhomes.util.StringHelper;

/**
 * Created by wuhan on 2018/9/20.
 */
public class PunchGoOutLog extends EhPunchGoOutLogs {

    /**
     * @author Wuhan
     */
    private static final long serialVersionUID = 4212289544277363636L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
