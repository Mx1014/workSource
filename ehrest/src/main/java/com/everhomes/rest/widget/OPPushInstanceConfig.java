//@formatter:off
package com.everhomes.rest.widget;

import com.everhomes.util.StringHelper;

import java.io.Serializable;

/**
 * <ul>
 *     <li>itemGroup: itemGroup</li>
 *     <li>entityCount: 之前用的，现在又不用了，暂时没有意义</li>
 *     <li>subjectHeight: 标题显示行数</li>
 *     <li>descriptionHeight: 描述显示行数</li>
 *     <li>newsSize: 显示多少条</li>
 *     <li>recommendType: 推荐类型</li>
 *     <li>moduleId: moduleId</li>
 *     <li>appId: appId</li>
 *     <li>actionType: actionType</li>
 * </ul>
 */
public class OPPushInstanceConfig implements Serializable {

    private String itemGroup;
    private Integer entityCount;
    private Integer subjectHeight;
    private Integer descriptionHeight;
    private Integer newsSize;
    private Byte recommendType;
    private Long moduleId;
    private Long appId;
    private Byte actionType;
    //private Object appConfig;

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public Integer getEntityCount() {
        return entityCount;
    }

    public void setEntityCount(Integer entityCount) {
        this.entityCount = entityCount;
    }

    public Integer getSubjectHeight() {
        return subjectHeight;
    }

    public void setSubjectHeight(Integer subjectHeight) {
        this.subjectHeight = subjectHeight;
    }

    public Integer getDescriptionHeight() {
        return descriptionHeight;
    }

    public void setDescriptionHeight(Integer descriptionHeight) {
        this.descriptionHeight = descriptionHeight;
    }

    public Integer getNewsSize() {
        return newsSize;
    }

    public void setNewsSize(Integer newsSize) {
        this.newsSize = newsSize;
    }

    public Byte getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(Byte recommendType) {
        this.recommendType = recommendType;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Byte getActionType() {
        return actionType;
    }

    public void setActionType(Byte actionType) {
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
