// @formatter:off
package com.everhomes.rest.officecubicle.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>cityid1: 城市id</li>
 * <li>cityid2: 城市id<</li>
 * </ul>
 */
public class ReOrderCityCommand {
    private Long cityid1;
    private Long cityid2;

    public Long getCityid1() {
        return cityid1;
    }

    public void setCityid1(Long cityid1) {
        this.cityid1 = cityid1;
    }

    public Long getCityid2() {
        return cityid2;
    }

    public void setCityid2(Long cityid2) {
        this.cityid2 = cityid2;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
