package com.everhomes.rest.rentalv2;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：id</li>
 * <li>rentalType: 价格类型，参考{@link com.everhomes.rest.rentalv2.RentalType}</li>
 * </ul>
 */
public class FindRentalSiteByIdCommand {
    private Long id;

    private String sceneToken;

	public String getSceneToken() {
        return sceneToken;
    }

    public void setSceneToken(String sceneToken) {
        this.sceneToken = sceneToken;
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
