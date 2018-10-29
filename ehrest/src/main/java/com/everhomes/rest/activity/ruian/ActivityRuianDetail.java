package com.everhomes.rest.activity.ruian;

public class ActivityRuianDetail {

    private Long ActivityID ;//活动ID
    private String Name ;//活动名称
    private String SubTitle ;//副标题
    private String Photo ;//头图
    private String Article ;//活动详情（富文本）
    private String Pact ;//活动规则
    private String StartTime ;//开始时间
    private String EndTime ;//结束时间
    private Integer Rank ;//排序权重
    private Long ActivityCategoryID ;//活动一级分类ID
    private Long ActivitySubCategoryID ;//活动二级分类ID
    private Boolean IsNeedSign ;//是否需要报名
    private Boolean IsAudit ;//报名是否需要审核（前提IsNeedSign为true）
    private String SignStartTime ;//报名开始时间（如果IsNeedSign为true,则一定返回）
    private String SignEndTime ;//报名结束时间（如果IsNeedSign为true,则一定返回）
    private String SignCondition ;//报名条件（前提IsNeedSign为true，以文本形式返回）
    private Boolean IsDeductionBonus ;//报名是否需要积分（前提IsNeedSign为true）
    private Double DeductionBonus ;//报名所需积分值（如果IsDeductionBonus为true，则一定返回）
    private Integer TotalCountLimit ;//活动名额
    private String PageUrl ;//猫酷对应的url
    private Byte State ;//状态（1|即将开始，2|进行中，3|已结束）

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

    public String getArticle() {
        return Article;
    }

    public void setArticle(String article) {
        Article = article;
    }

    public String getPact() {
        return Pact;
    }

    public void setPact(String pact) {
        Pact = pact;
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

    public Integer getRank() {
        return Rank;
    }

    public void setRank(Integer rank) {
        Rank = rank;
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

    public Boolean getNeedSign() {
        return IsNeedSign;
    }

    public void setNeedSign(Boolean needSign) {
        IsNeedSign = needSign;
    }

    public Boolean getAudit() {
        return IsAudit;
    }

    public void setAudit(Boolean audit) {
        IsAudit = audit;
    }

    public String getSignStartTime() {
        return SignStartTime;
    }

    public void setSignStartTime(String signStartTime) {
        SignStartTime = signStartTime;
    }

    public String getSignEndTime() {
        return SignEndTime;
    }

    public void setSignEndTime(String signEndTime) {
        SignEndTime = signEndTime;
    }

    public String getSignCondition() {
        return SignCondition;
    }

    public void setSignCondition(String signCondition) {
        SignCondition = signCondition;
    }

    public Boolean getDeductionBonus() {
        return IsDeductionBonus;
    }

    public void setDeductionBonus(Double deductionBonus) {
        DeductionBonus = deductionBonus;
    }

    public Integer getTotalCountLimit() {
        return TotalCountLimit;
    }

    public void setTotalCountLimit(Integer totalCountLimit) {
        TotalCountLimit = totalCountLimit;
    }

    public String getPageUrl() {
        return PageUrl;
    }

    public void setPageUrl(String pageUrl) {
        PageUrl = pageUrl;
    }

    public Byte getState() {
        return State;
    }

    public void setState(Byte state) {
        State = state;
    }

    public void setDeductionBonus(Boolean deductionBonus) {
        IsDeductionBonus = deductionBonus;
    }
}
