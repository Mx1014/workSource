//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/10/31.
 */
/**
 *<ul>
 * <li>failCause:失败或成功的描述</li>
 *</ul>
 */
public class DeleteChargingItemForBillGroupResponse {
    private String failCause;

    public String getFailCause() {
        return failCause;
    }

    public void setFailCause(String failCause) {
        this.failCause = failCause;
    }
}
