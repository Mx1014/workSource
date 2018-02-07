//@formatter:off
package com.everhomes.rest.purchase;

/**
 * Created by Wentian Wang on 2018/1/10.
 */

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 *<ul>
 * <li>purchaseRequestId:采购单id</li>
 * <li>supplierId:供应商id</li>
 * <li>deliveryDate:交付日期</li>
 * <li>contactName:联系人</li>
 * <li>contactTel:联系电话</li>
 * <li>remark:备注</li>
 * <li>approvalSheetId:关联的请示单的id</li>
 * <li>dtos:采购物品列表，参考{@link com.everhomes.rest.purchase.PurchaseMaterialDTO}</li>
 * <li>xxxx</li>
 * <li>xxxx</li>
 * <li>xxxx</li>
 *</ul>
 */
public class CreateOrUpdatePurchaseOrderCommand {
    private Long purchaseRequestId;
    private Long supplierId;
    private Timestamp deliveryDate;
    private String remark;
    private Long approvalSheetId;
    private List<PurchaseMaterialDTO> dtos;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Long communityId;
    private String contactTel;
    private String contactName;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getPurchaseRequestId() {
        return purchaseRequestId;
    }

    public void setPurchaseRequestId(Long purchaseRequestId) {
        this.purchaseRequestId = purchaseRequestId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getApprovalSheetId() {
        return approvalSheetId;
    }

    public void setApprovalSheetId(Long approvalSheetId) {
        this.approvalSheetId = approvalSheetId;
    }

    public List<PurchaseMaterialDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<PurchaseMaterialDTO> dtos) {
        this.dtos = dtos;
    }
}
