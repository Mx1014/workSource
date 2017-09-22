package com.everhomes.rest.contract;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 园区id</li>
 *     <li>customerName: 客户名</li>
 *     <li>targetId: 机构id或用户id</li>
 *     <li>targetType: 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 * </ul>
 * Created by ying.xiong on 2017/8/22.
 */
public class ListCustomerContractsCommand {

    private String customerName;

    private Long targetId;

    private Byte targetType;

    private Long communityId;

    private Integer namespaceId;

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Byte getTargetType() {
        return targetType;
    }

    public void setTargetType(Byte targetType) {
        this.targetType = targetType;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
}
