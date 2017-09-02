//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/9/1.
 */

/**
 *<ul>
 * <li>targeType:客户的类型，个人:eh_user----default; 企业:eh_organization---park_tourist</li>
 * <li>targetId:客户id</li>
 *</ul>
 */
public class FindUserInfoForPaymentCommand {
    private String targeType;
    private Long targetId;

    public String getTargeType() {
        return targeType;
    }

    public void setTargeType(String targeType) {
        this.targeType = targeType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
}
