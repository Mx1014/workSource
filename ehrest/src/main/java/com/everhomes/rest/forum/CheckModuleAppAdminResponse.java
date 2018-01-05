// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>flag: flag 参考{@link TrueOrFalseFlag}</li>
 * </ul>
 */
public class CheckModuleAppAdminResponse {

    private Byte flag;

    public Byte getFlag() {
        return flag;
    }

    public void setFlag(Byte flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
