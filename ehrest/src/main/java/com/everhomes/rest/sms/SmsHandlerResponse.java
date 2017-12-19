package com.everhomes.rest.sms;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul> 返回所有的短信 Handlers
 * 
 * </ul>
 * @author janson
 *
 */
public class SmsHandlerResponse {
    @ItemType(String.class)
    private List<String> handlers;

    public List<String> getHandlers() {
        return handlers;
    }

    public void setHandlers(List<String> handlers) {
        this.handlers = handlers;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
