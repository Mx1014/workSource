//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/8/19.
 */

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>targetId:客户的id</li>
 * <li>targetType:客户的类型，eh_user为认证的个人，eh_organization为认证的企业</li>
 * <li>targetName:客户名称</li>
 * <li>userIdentifier:个人客户的登录手机号</li>
 * <li>contractId:合同id</li>
 *</ul>
 */
public class TargetDTO {
    private Long targetId;
    private String targetType;
    private String targetName;
    private String userIdentifier;
    private Long contractId;

    public String getTargetName() {
        return targetName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public TargetDTO() {
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
}
