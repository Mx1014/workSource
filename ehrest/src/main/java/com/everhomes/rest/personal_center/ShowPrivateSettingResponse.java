// @formatter:off
package com.everhomes.rest.personal_center;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>showPrivateSetting: 是否显示隐私设置，1:是，0：否 请参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class ShowPrivateSettingResponse {

    private Byte showPrivateSetting;

    public Byte getShowPrivateSetting() {
        return showPrivateSetting;
    }

    public void setShowPrivateSetting(Byte showPrivateSetting) {
        this.showPrivateSetting = showPrivateSetting;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
