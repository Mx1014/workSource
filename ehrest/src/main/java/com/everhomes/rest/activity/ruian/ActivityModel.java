package com.everhomes.rest.activity.ruian;

import java.sql.Timestamp;

/**
 * 对接瑞安方的类，别人的的字段名都是大写开头的，为方便转按只能按别人的写了，不是我的锅．
 */
public class ActivityModel {
    private Long ActivityID ;//活动ID
    private String Name ;//名称
    private String SubTitle ;//副标题
    private String Photo ;//头图
    private Byte State ;//状态（1|即将开始，2|进行中，3|已结束）
    private String StartTime ;//开始时间
    private String EndTime ;//结束时间
    private Long ActivityCategoryID ;//活动一级分类ID
    private Long ActivitySubCategoryID ;//活动二级分类ID

    public Long getActivityID() {
        return ActivityID;
    }

    public void setActivityID(Long activityID) {
        ActivityID = activityID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSubTitle() {
        return SubTitle;
    }

    public void setSubTitle(String subTitle) {
        SubTitle = subTitle;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public Byte getState() {
        return State;
    }

    public void setState(Byte state) {
        State = state;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

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
}
