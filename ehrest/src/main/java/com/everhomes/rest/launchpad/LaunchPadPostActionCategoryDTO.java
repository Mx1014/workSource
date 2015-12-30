// @formatter:off
package com.everhomes.rest.launchpad;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: if</li>
 * <li>itemName: 名称</li>
 * <li>itemLabel: 显示标签</li>
 * <li>forumId: 论坛id</li>
 * <li>contentCategory: 发帖动作的大类，如物业，家政等</li>
 * <li>actionCategory: 发帖动作的小类，如投诉，建议等</li>
 * </ul>
 */
public class LaunchPadPostActionCategoryDTO {
    private Long    id;
    private String  itemName;
    private String  itemLabel;
    private Long    forumId;
    private Long    contentCategory;
    private Long    actionCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemLabel() {
        return itemLabel;
    }

    public void setItemLabel(String itemLabel) {
        this.itemLabel = itemLabel;
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getContentCategory() {
        return contentCategory;
    }

    public void setContentCategory(Long contentCategory) {
        this.contentCategory = contentCategory;
    }

    public Long getActionCategory() {
        return actionCategory;
    }

    public void setActionCategory(Long actionCategory) {
        this.actionCategory = actionCategory;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
