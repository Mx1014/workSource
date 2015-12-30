package com.everhomes.rest.banner;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: banner click id</li>
 * <li>uuid: banner click 唯一标示</li>
 * <li>bannerId: banner Id</li>
 * <li>uid: 用户id</li>
 * <li>familyId: 用户家庭id</li>
 * <li>clickCount: 点击次数</li>
 * <li>lastClickTime: 最近一次点击时间</li>
 * <li>createTime: 创建时间</li>
 * </ul>
 */
public class BannerClickDTO {
    private Long     id;
    private String   uuid;
    private Long     bannerId;
    private Long     uid;
    private Long     familyId;
    private Long     clickCount;
    private Timestamp lastClickTime;
    private Timestamp createTime;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public Long getBannerId() {
        return bannerId;
    }
    public void setBannerId(Long bannerId) {
        this.bannerId = bannerId;
    }
    public Long getUid() {
        return uid;
    }
    public void setUid(Long uid) {
        this.uid = uid;
    }
    public Long getFamilyId() {
        return familyId;
    }
    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }
    public Long getClickCount() {
        return clickCount;
    }
    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }
    public Timestamp getLastClickTime() {
        return lastClickTime;
    }
    public void setLastClickTime(Timestamp lastClickTime) {
        this.lastClickTime = lastClickTime;
    }
    public Timestamp getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
