//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/10/12.
 */
/**
 *<ul>
 * <li>failCause:回馈消息</li>
 *</ul>
 */
public class DeleteChargingStandardDTO {
    private String failCause;

    public String getFailCause() {
        return failCause;
    }

    public void setFailCause(String failCause) {
        this.failCause = failCause;
    }
}
