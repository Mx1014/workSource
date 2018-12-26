// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>indexFlag: 是否开启主页签标志，请参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class GetIndexFlagResponse {

    private Byte indexFlag;

    public Byte getIndexFlag() {
        return indexFlag;
    }

    public void setIndexFlag(Byte indexFlag) {
        this.indexFlag = indexFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
