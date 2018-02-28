package com.everhomes.rest.messaging;

import com.everhomes.util.StringHelper;

/**
 * <li>需要额外的参数</li>
 */
public class GetSercetKeyForScanCommand {
    private String args;

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
