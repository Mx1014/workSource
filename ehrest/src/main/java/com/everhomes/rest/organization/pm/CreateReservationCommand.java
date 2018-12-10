//@formatter:off
package com.everhomes.rest.organization.pm;

import java.sql.Timestamp;

/**
 * Created by Wentian Wang on 2018/6/12.
 */
/**
 *<ul>
 * <li>addressId: 门牌id</li>
 * <li>enterpriseCustomerId: 企业客户id</li>
 * <li>startTime: 开始时间, 时间戳，非精确选择的时间字段需要置为0，例如传来的时间最多精确到分钟，那么秒钟到0，如果有业务场景不满足再更改</li>
 * <li>endTime：结束时间</li>
 * <li>communityId</li>
 * <li>namespaceId</li>
 * <li>organizationId</li>
 *</ul>
 */
public class CreateReservationCommand {
    private Long addressId;
    private Long enterpriseCustomerId;
    private String startTime;
    private String endTime;
    private Long communityId;
    private Integer namespaceId;
    private Long organizationId;

    public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

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

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getEnterpriseCustomerId() {
        return enterpriseCustomerId;
    }

    public void setEnterpriseCustomerId(Long enterpriseCustomerId) {
        this.enterpriseCustomerId = enterpriseCustomerId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
