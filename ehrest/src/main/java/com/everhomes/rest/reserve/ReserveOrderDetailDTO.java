package com.everhomes.rest.reserve;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>id: 订单id</li>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.reserve.ReserveOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>resourceType: 资源类型  {@link com.everhomes.rest.reserve.ReserveResourceType}</li>
 * <li>resourceId: 具体资源id, 例如vip车位预约根据停车场做区分</li>
 * <li>resourceJson: 资源json数据</li>
 * <li>orderNo: 订单编号</li>
 * <li>reserveStartTime: 预约开始时间</li>
 * <li>reserveEndTime: 预约结束时间</li>
 * <li>reserveCellCount: 预约单元格数量</li>
 * <li>actualStartTime: 实际开始使用时间</li>
 * <li>actualEndTime: 实际结束使用时间</li>
 * <li>applicantEnterpriseId: 预约人公司id</li>
 * <li>applicantEnterpriseName: 预约人公司名称</li>
 * <li>applicantPhone: 预约人手机号</li>
 * <li>applicantName: 预约人名称</li>
 * <li>addressId: 门牌id</li>
 * <li>address: 门牌地址 </li>
 * <li>overTime: 超时时间 </li>
 * <li>payTime: 付款时间</li>
 * <li>payType: 支付类型</li>
 * <li>paidAmount: 支付金额</li>
 * <li>owingAmount: 每页条数</li>
 * <li>totalAmount: 总数金额</li>
 * <li>status: 订单状态 {@link com.everhomes.rest.reserve.ReserveOrderStatus}</li>
 * <li>creatorUid: 创建人id</li>
 * <li>createTime: 创建时间</li>
 * <li>updateUid: 更新人id</li>
 * <li>updateTime: 更新时间</li>
 * <li>cancelUid: 取消人id</li>
 * <li>cancelTime: 取消时间</li>
 * <li>remainPayTime: 剩余支付时间</li>
 * <li>blockEntities: 订单详情块列表 {@link com.everhomes.rest.reserve.ReserveOrderBlockEntity}</li>
 * </ul>
 */
public class ReserveOrderDetailDTO {

    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String resourceType;
    private Long resourceId;
    private String resourceJson;
    private String orderNo;
    private Timestamp reserveStartTime;
    private Timestamp reserveEndTime;
    private Double reserveCellCount;
    private Timestamp actualStartTime;
    private Timestamp actualEndTime;
    private Long applicantEnterpriseId;
    private String applicantEnterpriseName;
    private String applicantPhone;
    private String applicantName;
    private Long addressId;
    private String address;
    private Long overTime;
    private Timestamp payTime;
    private String payType;
    private BigDecimal paidAmount;
    private BigDecimal owingAmount;
    private BigDecimal totalAmount;
    private Byte status;
    private Long creatorUid;
    private Timestamp createTime;
    private Long updateUid;
    private Timestamp updateTime;
    private Long cancelUid;
    private Timestamp cancelTime;

    private Integer remainPayTime;

    @ItemType(ReserveOrderBlockEntity.class)
    private List<ReserveOrderBlockEntity> blockEntities;

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

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceJson() {
        return resourceJson;
    }

    public void setResourceJson(String resourceJson) {
        this.resourceJson = resourceJson;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Timestamp getReserveStartTime() {
        return reserveStartTime;
    }

    public void setReserveStartTime(Timestamp reserveStartTime) {
        this.reserveStartTime = reserveStartTime;
    }

    public Timestamp getReserveEndTime() {
        return reserveEndTime;
    }

    public void setReserveEndTime(Timestamp reserveEndTime) {
        this.reserveEndTime = reserveEndTime;
    }

    public Double getReserveCellCount() {
        return reserveCellCount;
    }

    public void setReserveCellCount(Double reserveCellCount) {
        this.reserveCellCount = reserveCellCount;
    }

    public Timestamp getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(Timestamp actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public Timestamp getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(Timestamp actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public Long getApplicantEnterpriseId() {
        return applicantEnterpriseId;
    }

    public void setApplicantEnterpriseId(Long applicantEnterpriseId) {
        this.applicantEnterpriseId = applicantEnterpriseId;
    }

    public String getApplicantEnterpriseName() {
        return applicantEnterpriseName;
    }

    public void setApplicantEnterpriseName(String applicantEnterpriseName) {
        this.applicantEnterpriseName = applicantEnterpriseName;
    }

    public String getApplicantPhone() {
        return applicantPhone;
    }

    public void setApplicantPhone(String applicantPhone) {
        this.applicantPhone = applicantPhone;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getOverTime() {
        return overTime;
    }

    public void setOverTime(Long overTime) {
        this.overTime = overTime;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getOwingAmount() {
        return owingAmount;
    }

    public void setOwingAmount(BigDecimal owingAmount) {
        this.owingAmount = owingAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateUid() {
        return updateUid;
    }

    public void setUpdateUid(Long updateUid) {
        this.updateUid = updateUid;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCancelUid() {
        return cancelUid;
    }

    public void setCancelUid(Long cancelUid) {
        this.cancelUid = cancelUid;
    }

    public Timestamp getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Timestamp cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Integer getRemainPayTime() {
        return remainPayTime;
    }

    public void setRemainPayTime(Integer remainPayTime) {
        this.remainPayTime = remainPayTime;
    }

    public List<ReserveOrderBlockEntity> getBlockEntities() {
        return blockEntities;
    }

    public void setBlockEntities(List<ReserveOrderBlockEntity> blockEntities) {
        this.blockEntities = blockEntities;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
