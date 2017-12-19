package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>name: name</li>
 *     <li>posterUri: posterUri</li>
 *     <li>actionType: actionType</li>
 *     <li>actionData: actionData</li>
 * </ul>
 */
public class UpdatePointBannerCommand {

    @NotNull
    private Long id;
    private String name;
    private String posterUri;
    private Byte actionType;
    private String actionData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
