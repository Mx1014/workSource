//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/9/1.
 */

/**
 *<ul>
 * <li>targetType:客户的类型，个人:eh_user----default; 企业:eh_organization---park_tourist</li>
 * <li>targetId:客户id</li>
 * <li>communityId:园区id</li>
 *</ul>
 */
public class FindUserInfoForPaymentCommand {
    private String targetType;
    private Long targetId;
    private Long communityId;

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
}
