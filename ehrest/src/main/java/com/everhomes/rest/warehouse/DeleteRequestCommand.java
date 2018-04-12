//@formatter:off
package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * Created by Wentian Wang on 2018/3/2.
 */
/**
 *<ul>
 * <li>requestId:领用申请的id</li>
 *</ul>
 */
public class DeleteRequestCommand {
    private Long requestId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
}
