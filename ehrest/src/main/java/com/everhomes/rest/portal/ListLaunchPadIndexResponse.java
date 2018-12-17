// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.launchpad.LaunchPadIndexDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>launchPadIndexs: 主页签列表，请参考{@link com.everhomes.rest.launchpad.LaunchPadIndexDTO}</li>
 *     <li>indexFlag: 是否使用主页签，请参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class ListLaunchPadIndexResponse {

    @ItemType(LaunchPadIndexDTO.class)
    private List<LaunchPadIndexDTO> launchPadIndexs;

    private Byte indexFlag;

    public Byte getIndexFlag() {
        return indexFlag;
    }

    public void setIndexFlag(Byte indexFlag) {
        this.indexFlag = indexFlag;
    }

    public List<LaunchPadIndexDTO> getLaunchPadIndexs() {
        return launchPadIndexs;
    }

    public void setLaunchPadIndexs(List<LaunchPadIndexDTO> launchPadIndexs) {
        this.launchPadIndexs = launchPadIndexs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
