// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>identifier: 手机号</li>
 * </ul>
 */
public class ListResetIdentifierCodeCommand {

    private String identifier;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
