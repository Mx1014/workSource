//@formatter:off
package com.everhomes.rest.requisition;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * Created by Wentian Wang on 2018/1/20.
 */
/**
 *<ul>
 * <li>ownerType:所属者类型</li>
 * <li>ownerId:所属者id</li>
 * <li>namespaceId:域名id</li>
 * <li>theme:主题</li>
 * <li>applicantName:申请人姓名</li>
 * <li>applicantDepartment:申请人所属部门</li>
 * <li>requisitionTypeId:请示类型id</li>
 * <li>amount:金额</li>
 * <li>description:描述</li>
 * <li>attachmentUrl:附件的地址</li>
 *</ul>
 */
public class CreateRequisitionCommand {
    private String ownerType;
    private Long ownerId;
    private Integer namespaceId;
    private String theme;
    private String applicantName;
    private String applicantDepartment;
    private BigDecimal amount;
    private String description;
    private Long requisitionTypeId;
    private String attachmentUrl;

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public Long getRequisitionTypeId() {
        return requisitionTypeId;
    }

    public void setRequisitionTypeId(Long requisitionTypeId) {
        this.requisitionTypeId = requisitionTypeId;
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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantDepartment() {
        return applicantDepartment;
    }

    public void setApplicantDepartment(String applicantDepartment) {
        this.applicantDepartment = applicantDepartment;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
