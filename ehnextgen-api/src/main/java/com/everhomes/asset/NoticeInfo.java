//@formatter:off
package com.everhomes.asset;


import com.everhomes.rest.asset.NoticeConfig;
import com.everhomes.rest.asset.NoticeObj;
import com.everhomes.util.StringHelper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class NoticeInfo implements Serializable{
    private String phoneNums;
    private BigDecimal amountRecevable;
    private BigDecimal amountOwed;
    private String targetType;
    private Long targetId;
    private String appName;
    private String targetName;
    private String dateStr;

    private String ownerType;
    private Long ownerId;
    private Long appTemplateId;
    private Long msgTemplateId;
    private Boolean useTemplate;
    private Integer namespaceId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    //    private List<NoticeObj> noticeObjs;
    public Boolean isUseTemplate() {
        return useTemplate;
    }

    public void setUseTemplate(Boolean useTemplate) {
        this.useTemplate = useTemplate;
    }

    public Long getAppTemplateId() {
        return appTemplateId;
    }

    public void setAppTemplateId(Long appTemplateId) {
        this.appTemplateId = appTemplateId;
    }

    public Long getMsgTemplateId() {
        return msgTemplateId;
    }

    public void setMsgTemplateId(Long msgTemplateId) {
        this.msgTemplateId = msgTemplateId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public NoticeInfo() {
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getPhoneNums() {
        return phoneNums;
    }

    public void setPhoneNums(String phoneNums) {
        this.phoneNums = phoneNums;
    }

    public BigDecimal getAmountRecevable() {
        return amountRecevable;
    }

    public void setAmountRecevable(BigDecimal amountRecevable) {
        this.amountRecevable = amountRecevable;
    }

    public BigDecimal getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(BigDecimal amountOwed) {
        this.amountOwed = amountOwed;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Boolean getUseTemplate() {
        return useTemplate;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoticeInfo)) return false;

        NoticeInfo that = (NoticeInfo) o;

        if (getTargetType() != null ? !getTargetType().equals(that.getTargetType()) : that.getTargetType() != null)
            return false;
        return getTargetId() != null ? getTargetId().equals(that.getTargetId()) : that.getTargetId() == null;
    }

    @Override
    public int hashCode() {
        int result = getTargetType() != null ? getTargetType().hashCode() : 0;
        result = 31 * result + (getTargetId() != null ? getTargetId().hashCode() : 0);
        return result;
    }
}
