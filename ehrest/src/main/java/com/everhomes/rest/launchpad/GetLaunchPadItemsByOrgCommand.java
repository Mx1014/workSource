// @formatter:off
package com.everhomes.rest.launchpad;


import javax.validation.constraints.NotNull;

import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>itemLocation: item的路径</li>
 *  <li>itemGroup: 当前item归属哪个组</li>
 * 	<li>organizationId: 机构/公司id</li>
 * 	<li>siteUri: 链接</li>
 * 	<li>namespaceId: 域空间</li>
 * 	<li>sceneType: 场景类型，{@link com.everhomes.rest.ui.user.SceneType}</li>
 * 	<li>categryId: item类别</li>
 * </ul>
 */
public class GetLaunchPadItemsByOrgCommand {
    
    @NotNull
    private String    itemLocation;
    @NotNull
    private String    itemGroup;
    @NotNull
    private Long    organizationId;
    @NotNull
    private Integer namespaceId;
    
    private String sceneType;

    private Long categryId;

    private String categryName;

    public GetLaunchPadItemsByOrgCommand() {
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

	public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }
    
    public String getCurrentSceneType() {
        return (sceneType == null) ? SceneType.DEFAULT.getCode() : sceneType;
    }

    public Long getCategryId() {
        return categryId;
    }

    public void setCategryId(Long categryId) {
        this.categryId = categryId;
    }

    public String getCategryName() {
        return categryName;
    }

    public void setCategryName(String categryName) {
        this.categryName = categryName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
