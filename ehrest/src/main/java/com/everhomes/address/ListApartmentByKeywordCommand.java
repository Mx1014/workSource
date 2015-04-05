// @formatter:off
package com.everhomes.address;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class ListApartmentByKeywordCommand {
    @NotNull
    private Long communitId;

    @NotNull
    private String buildingName;
    
    @NotNull
    private String keyword;

    public ListApartmentByKeywordCommand() {
    }

    public Long getCommunitId() {
        return communitId;
    }

    public void setCommunitId(Long communitId) {
        this.communitId = communitId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
