// @formatter:off
package com.everhomes.rest.personal_center;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>showCompanyFlag: 是否展示我的公司，请参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class UpdateShowCompanyCommand {
    private Byte showCompanyFlag;

    public Byte getShowCompanyFlag() {
        return showCompanyFlag;
    }

    public void setShowCompanyFlag(Byte showCompanyFlag) {
        this.showCompanyFlag = showCompanyFlag;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
