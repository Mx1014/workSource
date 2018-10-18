// @formatter:off
package com.everhomes.rest.launchpad;



import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 广场id</li>
 * </ul>
 */
public class GetLaunchPadLayoutCommand {
    @NotNull
    private Long id;

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
