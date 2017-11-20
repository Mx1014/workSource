package com.everhomes.rest.common;

import com.everhomes.util.StringHelper;

import java.io.Serializable;


/**
 * <ul>
 * </ul>
 */
public class GuildActionData implements Serializable {

    private static final long serialVersionUID = -304678700143142196L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
