package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>systemId: systemId</li>
 *     <li>name: name</li>
 *     <li>posterUri: posterUri</li>
 *     <li>actionType: actionType</li>
 *     <li>actionData: actionData</li>
 *     <li>defaultOrder: defaultOrder</li>
 * </ul>
 */
public class CreatePointBannerCommand {

    @NotNull
    private Long systemId;
    private String name;
    private String posterUri;
    private Byte actionType;
    private String actionData;
    private Integer defaultOrder;

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosterUri() {
        return posterUri;
    }

    public void setPosterUri(String posterUri) {
        this.posterUri = posterUri;
    }

    public Byte getActionType() {
        return actionType;
    }

    public void setActionType(Byte actionType) {
        this.actionType = actionType;
    }

    public String getActionData() {
        return actionData;
    }

    public void setActionData(String actionData) {
        this.actionData = actionData;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
