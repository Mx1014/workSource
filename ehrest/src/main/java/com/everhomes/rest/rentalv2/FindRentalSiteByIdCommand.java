package com.everhomes.rest.rentalv2;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：id</li>
 <li>resourceType: resourceType {@link RentalV2ResourceType}</li>
 * </ul>
 */
public class FindRentalSiteByIdCommand {

    private String resourceType;
    private Long id;

    private String sceneType;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
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
