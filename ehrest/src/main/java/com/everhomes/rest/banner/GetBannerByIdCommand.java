// @formatter:off
package com.everhomes.rest.banner;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: bannerId</li>
 * </ul>
 */
public class GetBannerByIdCommand {
    @NotNull
    private Long id;

    public GetBannerByIdCommand() {
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
