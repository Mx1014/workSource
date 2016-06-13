// @formatter:off
package com.everhomes.rest.link;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: link的Id</li>
 * </ul>
 */
public class FindLinkByIdCommand {
    @NotNull
    private Long id;

    public FindLinkByIdCommand() {
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
