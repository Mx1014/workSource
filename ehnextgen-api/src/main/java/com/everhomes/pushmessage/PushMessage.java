package com.everhomes.pushmessage;

import com.everhomes.server.schema.tables.pojos.EhPushMessages;
import com.everhomes.util.StringHelper;

/**
 * PushMessage
 * @author janson
 *
 */
public class PushMessage extends EhPushMessages {

    /**
     * 
     */
    private static final long serialVersionUID = -7104159589146648056L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
