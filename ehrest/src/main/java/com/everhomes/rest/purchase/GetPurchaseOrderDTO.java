//@formatter:off
package com.everhomes.rest.purchase;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Wentian Wang on 2018/1/10.
 */
/**
 *<ul>
 * <li>purchaseRequestId:采购单id</li>
 * <li>supplierId:供应商id</li>
 * <li>supplierName:供应商名字</li>
 * <li>contact:供应商联系人</li>
 * <li>contactTel:联系电话</li>
 * <li>deliveryDate:交付日期</li>
 * <li>remark:备注</li>
 * <li>approvalSheetId:关联的请示单的id</li>
 * <li>dtos:采购物品列表，参考{@link com.everhomes.rest.purchase.PurchaseMaterialDetailDTO}</li>
 *</ul>
 */
public class GetPurchaseOrderDTO {
    private Long purchaseRequestId;
    private Long supplierId;
    private String supplierName;
    private String contact;
    private String contactTel;
    private String deliveryDate;
    private String remark;
    private Long approvalSheetId;
    private List<PurchaseMaterialDetailDTO> dtos;

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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
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

    public List<PurchaseMaterialDetailDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<PurchaseMaterialDetailDTO> dtos) {
        this.dtos = dtos;
    }
}
