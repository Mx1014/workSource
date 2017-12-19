package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>name: name</li>
 *     <li>posterPath: posterPath</li>
 *     <li>actionType: actionType</li>
 *     <li>actionData: actionData</li>
 * </ul>
 */
public class UpdatePointBannerCommand {

    @NotNull
    private Long id;
    private String name;
    private String posterPath;
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

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
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
