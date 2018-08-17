// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>payeeAccountStatus: 付款方账号状态，请参考{@link com.everhomes.rest.activity.ActivityPayeeStatusType}</li>
 * </ul>
 */
public class CheckPayeeIsUsefulResponse {

    private Byte payeeAccountStatus;

    public Byte getPayeeAccountStatus() {
        return payeeAccountStatus;
    }

    public void setPayeeAccountStatus(Byte payeeAccountStatus) {
        this.payeeAccountStatus = payeeAccountStatus;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
