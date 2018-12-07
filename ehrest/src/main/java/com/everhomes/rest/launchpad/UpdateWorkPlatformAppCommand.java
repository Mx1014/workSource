// @formatter:off
package com.everhomes.rest.launchpad;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>id:id</li>
 *     <li>visibleFlag: 可见性，0不可见，1可见</li>
 * </ul>
 */
public class UpdateWorkPlatformAppCommand {
    private Long id;

    private Byte visibleFlag;

    public Byte getVisibleFlag() {
        return visibleFlag;
    }

    public void setVisibleFlag(Byte visibleFlag) {
        this.visibleFlag = visibleFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
