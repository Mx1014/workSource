package com.everhomes.activity;

import com.everhomes.util.StringHelper;

public class ActivityMemberDTO {
    private Long id;
    private String userName;
    private String userAvatar;
    private String familyName;
    private Long familyId;
    private Integer adultCount;
    private Integer childCount;
    private Integer signupFlag;
    private String signupTime;
    private Integer confirmFlag;
    private String confirmTime;
    private Integer creatorFlag;
    private Integer lotteryWinnerFlag;
    private String lotteryWonTime;
    
    public ActivityMemberDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public Integer getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(Integer adultCount) {
        this.adultCount = adultCount;
    }

    public Integer getChildCount() {
        return childCount;
    }

    public void setChildCount(Integer childCount) {
        this.childCount = childCount;
    }

    public Integer getSignupFlag() {
        return signupFlag;
    }

    public void setSignupFlag(Integer signupFlag) {
        this.signupFlag = signupFlag;
    }

    public String getSignupTime() {
        return signupTime;
    }

    public void setSignupTime(String signupTime) {
        this.signupTime = signupTime;
    }

    public Integer getConfirmFlag() {
        return confirmFlag;
    }

    public void setConfirmFlag(Integer confirmFlag) {
        this.confirmFlag = confirmFlag;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Integer getCreatorFlag() {
        return creatorFlag;
    }

    public void setCreatorFlag(Integer creatorFlag) {
        this.creatorFlag = creatorFlag;
    }

    public Integer getLotteryWinnerFlag() {
        return lotteryWinnerFlag;
    }

    public void setLotteryWinnerFlag(Integer lotteryWinnerFlag) {
        this.lotteryWinnerFlag = lotteryWinnerFlag;
    }

    public String getLotteryWonTime() {
        return lotteryWonTime;
    }

    public void setLotteryWonTime(String lotteryWonTime) {
        this.lotteryWonTime = lotteryWonTime;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
