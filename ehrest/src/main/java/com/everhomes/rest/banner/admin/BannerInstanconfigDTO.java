// @formatter:off
package com.everhomes.rest.banner.admin;

import com.everhomes.util.StringHelper;

public class BannerInstanconfigDTO {

    private Long widthRatio;

    private Long heightRatio;

    public Long getWidthRatio() {
        return widthRatio;
    }

    public void setWidthRatio(Long widthRatio) {
        this.widthRatio = widthRatio;
    }

    public Long getHeightRatio() {
        return heightRatio;
    }

    public void setHeightRatio(Long heightRatio) {
        this.heightRatio = heightRatio;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
