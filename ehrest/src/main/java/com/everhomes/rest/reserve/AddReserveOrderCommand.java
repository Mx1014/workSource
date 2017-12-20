package com.everhomes.rest.reserve;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.reserve.ReserveOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>resourceType: 资源类型  {@link com.everhomes.rest.reserve.ReserveResourceType}</li>
 * <li>resourceId: 具体资源id, 例如vip车位预约根据停车场做区分</li>
 * <li>resourceJson: 资源json数据</li>
 * <li>reserveStartTime: 预约开始时间</li>
 * <li>reserveEndTime: 预约结束时间</li>
 * <li>reserveCellCount: 预约单元格数量</li>
 * <li>applicantEnterpriseId: 预约人公司id</li>
 * <li>applicantEnterpriseName: 预约人公司名称</li>
 * <li>applicantPhone: 预约人手机号</li>
 * <li>applicantName: 预约人名称</li>
 * <li>addressId: 门牌id</li>
 * <li>totalAmount: 总数金额</li>
 * </ul>
 */
public class AddReserveOrderCommand {
    private String resourceType;
    private Long resourceId;
    private String resourceJson;
    private Long reserveStartTime;
    private Long reserveEndTime;
    private Double reserveCellCount;
    private Long applicantEnterpriseId;
    private String applicantEnterpriseName;
    private String applicantPhone;
    private String applicantName;
    private Long addressId;
    private BigDecimal totalAmount;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceJson() {
        return resourceJson;
    }

    public void setResourceJson(String resourceJson) {
        this.resourceJson = resourceJson;
    }

    public Long getReserveStartTime() {
        return reserveStartTime;
    }

    public void setReserveStartTime(Long reserveStartTime) {
        this.reserveStartTime = reserveStartTime;
    }

    public Long getReserveEndTime() {
        return reserveEndTime;
    }

    public void setReserveEndTime(Long reserveEndTime) {
        this.reserveEndTime = reserveEndTime;
    }

    public Double getReserveCellCount() {
        return reserveCellCount;
    }

    public void setReserveCellCount(Double reserveCellCount) {
        this.reserveCellCount = reserveCellCount;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
