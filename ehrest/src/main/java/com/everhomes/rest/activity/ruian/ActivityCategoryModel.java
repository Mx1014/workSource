package com.everhomes.rest.activity.ruian;

/**
 * 对接瑞安方的类，别人的的字段名都是大写开头的，为方便转按只能按别人的写了，不是我的锅．
 */
public class ActivityCategoryModel {

    private Long ActivityCategoryID ;//活动分类ID
    private String Name ;//分类名称
    private String TypeIcon ;//图标

    public Long getActivityCategoryID() {
        return ActivityCategoryID;
    }

    public void setActivityCategoryID(Long activityCategoryID) {
        ActivityCategoryID = activityCategoryID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTypeIcon() {
        return TypeIcon;
    }

    public void setTypeIcon(String typeIcon) {
        TypeIcon = typeIcon;
    }
}
