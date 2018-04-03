package com.everhomes.rest.warehouse;

import com.everhomes.discover.ItemType;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>ownerType: 所属类型 eg：EhOrganizations</li>
 *     <li>ownerId: 所属类型id</li>
 *     <li>requestUid: 申请人id</li>
 *     <li>requestUserName: 申请人姓名</li>
 *     <li>requestUserContact: 申请人联系方式</li>
 *     <li>requestOrganizationId: 申请人所在机构id</li>
 *     <li>requestOrganizationName: 申请人所在机构名称</li>
 *     <li>remark: 申请说明</li>
 *     <li>requestType: 申请类型 参考{@link com.everhomes.rest.warehouse.WarehouseStockRequestType}</li>
 *     <li>reviewResult： 审阅结果 参考{@link com.everhomes.rest.warehouse.WarehouseRequestReviewResult}</li>
 *     <li>createTime: 申请时间</li>
 *     <li>updateTime: 更新时间</li>
 *     <li>materialDetailDTOs: 申请物品列表 参考{@link com.everhomes.rest.warehouse.WarehouseRequestMaterialDetailDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class WarehouseRequestDetailsDTO {
    private Long id;

    private String ownerType;

    private Long ownerId;

    private Long requestUid;

    private String requestUserName;

    private String requestUserContact;

    private Long requestOrganizationId;

    private String requestOrganizationName;

    private String remark;

    private Byte requestType;

    private Byte reviewResult;

    private Timestamp createTime;

    private Timestamp updateTime;

    @ItemType(WarehouseRequestMaterialDetailDTO.class)
    private List<WarehouseRequestMaterialDetailDTO> materialDetailDTOs;

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getRequestUserContact() {
        return requestUserContact;
    }

    public void setRequestUserContact(String requestUserContact) {
        this.requestUserContact = requestUserContact;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<WarehouseRequestMaterialDetailDTO> getMaterialDetailDTOs() {
        return materialDetailDTOs;
    }

    public void setMaterialDetailDTOs(List<WarehouseRequestMaterialDetailDTO> materialDetailDTOs) {
        this.materialDetailDTOs = materialDetailDTOs;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getRequestOrganizationId() {
        return requestOrganizationId;
    }

    public void setRequestOrganizationId(Long requestOrganizationId) {
        this.requestOrganizationId = requestOrganizationId;
    }

    public String getRequestOrganizationName() {
        return requestOrganizationName;
    }

    public void setRequestOrganizationName(String requestOrganizationName) {
        this.requestOrganizationName = requestOrganizationName;
    }

    public Byte getRequestType() {
        return requestType;
    }

    public void setRequestType(Byte requestType) {
        this.requestType = requestType;
    }

    public Long getRequestUid() {
        return requestUid;
    }

    public void setRequestUid(Long requestUid) {
        this.requestUid = requestUid;
    }

    public String getRequestUserName() {
        return requestUserName;
    }

    public void setRequestUserName(String requestUserName) {
        this.requestUserName = requestUserName;
    }

    public Byte getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(Byte reviewResult) {
        this.reviewResult = reviewResult;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
