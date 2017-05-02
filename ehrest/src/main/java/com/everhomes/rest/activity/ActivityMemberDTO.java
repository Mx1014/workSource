// @formatter:off
package com.everhomes.rest.activity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * 
 * @author elians
 *<ul>
 *<li>id:登记ID</li>
 *<li>uid:用户ID</li>
 *<li>userName:用户名</li>
 *<li>userAvatar:用户头像</li>
 *<li>familyName:家庭名</li>
 *<li>familyId:家庭id</li>
 *<li>adultCount:成人数</li>
 *<li>childCount:小孩数</li>
 *<li>checkInFlag:签到标记</li>
 *<li>checkInTime:签到时间</li>
 *<li>confirmFlag:确认标记</li>
 *<li>creatorFlag:创建人标识</li>
 *<li>lotteryWinnerFlag:中奖标识</li>
 *<li>lotteryWonTime:中奖时间</li>
 *<li>phone:用户电话号码</li>
 *<li>payFlag:是否已支付</li>
 *</ul>
 */
public class ActivityMemberDTO {
    private Long id;
    private Long uid;
    private String userName;
    private String userAvatar;
    private String familyName;
    private Long familyId;
    private Integer adultCount;
    private Integer childCount;
    private Integer checkinFlag;
    private String checkinTime;
    private Integer confirmFlag;
    private String confirmTime;
    private Integer creatorFlag;
    private Integer lotteryWinnerFlag;
    private String lotteryWonTime;
    private Byte payFlag;
    
    @ItemType(String.class)
    private List<String> phone;
    
    public List<String> getPhone() {
		return phone;
	}

	public void setPhone(List<String> phone) {
		this.phone = phone;
	}

	public ActivityMemberDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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


    public Integer getCheckinFlag() {
        return checkinFlag;
    }

    public void setCheckinFlag(Integer checkinFlag) {
        this.checkinFlag = checkinFlag;
    }

    public String getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
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
    
    public Byte getPayFlag() {
		return payFlag;
	}

	public void setPayFlag(Byte payFlag) {
		this.payFlag = payFlag;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
