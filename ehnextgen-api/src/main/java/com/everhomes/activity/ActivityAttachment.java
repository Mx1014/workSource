package com.everhomes.activity;

import com.everhomes.server.schema.tables.pojos.EhActivityAttachments;
import com.everhomes.util.StringHelper;

/**
 * Created by Administrator on 2016/12/5.
 */
public class ActivityAttachment extends EhActivityAttachments {
    private static final long serialVersionUID = 7248284883608459890L;

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
