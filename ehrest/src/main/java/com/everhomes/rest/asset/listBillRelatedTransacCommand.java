//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2018/4/25.
 */
/**
 *<ul>
 * <li>billId:账单id</li>
 * <li>pageAnchor:锚点</li>
 * <li>pageSize:页大小</li>
 * <li>communityId:园区id</li>
 * <li>userId:收款方id</li>
 * <li>userType:收款方类型</li>
 *</ul>
 */
public class listBillRelatedTransacCommand {
    private Long billId;
    private Long pageAnchor;
    private Long pageSize;
    private Long communityId;
    private Long userId;
    private String userType;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }
}
