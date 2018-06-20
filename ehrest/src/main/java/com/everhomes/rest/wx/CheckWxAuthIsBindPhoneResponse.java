// @formatter:off
package com.everhomes.rest.wx;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>bindType: 微信是否绑定手机号，请参考{@link com.everhomes.rest.wx.WxAuthBindPhoneType}</li>
 * </ul>
 */
public class CheckWxAuthIsBindPhoneResponse {

    private Byte bindType;

    public Byte getBindType() {
        return bindType;
    }

    public void setBindType(Byte bindType) {
        this.bindType = bindType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
