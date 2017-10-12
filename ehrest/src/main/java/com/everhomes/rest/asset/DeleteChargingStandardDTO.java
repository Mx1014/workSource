//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/10/12.
 */
/**
 *<ul>
 * <li>message:回馈消息，success:成功; failed:失败</li>
 *</ul>
 */
public class DeleteChargingStandardDTO {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
