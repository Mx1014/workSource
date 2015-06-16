// @formatter:off
package com.everhomes.community;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>cityId: 城市Id</li>
 * <li>name: 小区名字</li>
 * </ul>
 */
public class GetCommunitiesByNameAndCityIdCommand {
    @NotNull
    private Long cityId;
    @NotNull
    private String name;
    
    public GetCommunitiesByNameAndCityIdCommand() {
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
