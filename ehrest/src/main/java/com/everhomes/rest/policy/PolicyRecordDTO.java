package com.everhomes.rest.policy;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 所属类型</li>
 * <li>ownerId: 所属项目id</li>
 * <li>categoryId: 企业类型</li>
 * <li>creatorId: 用户Id</li>
 * <li>creatorName: 用户姓名</li>
 * <li>creatorPhone: 联系电话</li>
 * <li>creatorOrgId: 企业Id</li>
 * <li>creatorOrgName: 企业名称</li>
 * <li>turnover: 营业额</li>
 * <li>tax: 纳税总额</li>
 * <li>qualification: 单位资质</li>
 * <li>financing: A轮融资</li>
 * <li>createTime: 创建时间</li>
 * </ul>
 */

public class PolicyRecordDTO {
    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Long categoryId;
    private Long creatorId;
    private String creatorName;
    private String creatorPhone;
    private Long creatorOrgId;
    private String creatorOrgName;
    private String turnover;
    private String tax;
    private String qualification;
    private String financing;
    private Timestamp createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorPhone() {
        return creatorPhone;
    }

    public void setCreatorPhone(String creatorPhone) {
        this.creatorPhone = creatorPhone;
    }

    public Long getCreatorOrgId() {
        return creatorOrgId;
    }

    public void setCreatorOrgId(Long creatorOrgId) {
        this.creatorOrgId = creatorOrgId;
    }

    public String getCreatorOrgName() {
        return creatorOrgName;
    }

    public void setCreatorOrgName(String creatorOrgName) {
        this.creatorOrgName = creatorOrgName;
    }

    public String getTurnover() {
        return turnover;
    }

    public void setTurnover(String turnover) {
        this.turnover = turnover;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getFinancing() {
        return financing;
    }

    public void setFinancing(String financing) {
        this.financing = financing;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
