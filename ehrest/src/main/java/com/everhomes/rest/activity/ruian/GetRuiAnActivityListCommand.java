package com.everhomes.rest.activity.ruian;

/**
 * 对接瑞安方的类，别人的的字段名都是大写开头的，为方便转按只能按别人的写了，不是我的锅．
 */
public class GetRuiAnActivityListCommand {
    private Long ActivityCategoryID ;//活动一级分类ID
    private Long ActivitySubCategoryID ;//活动二级分类ID
    private Byte State ;//状态（1|即将开始，2|进行中，3|已结束）
    private Integer PageSize ;//每页数量（默认10）
    private Integer PageIndex ;//当前页码（默认1）

    public Long getActivityCategoryID() {
        return ActivityCategoryID;
    }

    public void setActivityCategoryID(Long activityCategoryID) {
        ActivityCategoryID = activityCategoryID;
    }

    public Long getActivitySubCategoryID() {
        return ActivitySubCategoryID;
    }

    public void setActivitySubCategoryID(Long activitySubCategoryID) {
        ActivitySubCategoryID = activitySubCategoryID;
    }

    public Byte getState() {
        return State;
    }

    public void setState(Byte state) {
        State = state;
    }

    public Integer getPageSize() {
        return PageSize;
    }

    public void setPageSize(Integer pageSize) {
        PageSize = pageSize;
    }

    public Integer getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        PageIndex = pageIndex;
    }
}
