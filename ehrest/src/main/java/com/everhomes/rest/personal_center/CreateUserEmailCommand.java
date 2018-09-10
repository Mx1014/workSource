// @formatter:off
package com.everhomes.rest.personal_center;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>email: 邮箱</li>
 * </ul>
 */
public class CreateUserEmailCommand {
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
