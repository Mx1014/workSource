package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>systemId: 积分系统id</li>
 *     <li>categoryId: 积分模块id</li>
 *     <li>categoryName: 积分模块名称</li>
 *     <li>ruleId: 积分规则id</li>
 *     <li>ruleName: 积分规则名称</li>
 *     <li>arithmeticType: arithmeticType {@link PointArithmeticType}</li>
 *     <li>points: 积分数量</li>
 *     <li>targetUid: 用户id</li>
 *     <li>targetName: 用户昵称</li>
 *     <li>targetPhone: 用户phone</li>
 *     <li>operatorType: 系统操作还是手动操作 {@link PointOperatorType}</li>
 *     <li>operatorUid: 操作用户id</li>
 *     <li>operatorName: 操作用户昵称</li>
 *     <li>operatorPhone: 操作用户电话</li>
 *     <li>description: 描述</li>
 *     <li>status: status</li>
 *     <li>createTime: createTime</li>
 *     <li>extra: extra</li>
 *     <li>discountPrice: 给</li>
 * </ul>
 */
public class PointLogDTO {

    private Long id;
    private Integer namespaceId;
    private Long systemId;
    private Long categoryId;
    private String categoryName;
    private Long ruleId;
    private String ruleName;
    private Byte arithmeticType;
    private Long points;
    private Long targetUid;
    private String targetName;
    private String targetPhone;
    private Byte operatorType;
    private Long operatorUid;
    private String operatorName;
    private String operatorPhone;
    private String description;
    private Byte status;
    private Timestamp createTime;
    private String extra;
    private String appName;

    private Float discountPrice;

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

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Byte getArithmeticType() {
        return arithmeticType;
    }

    public void setArithmeticType(Byte arithmeticType) {
        this.arithmeticType = arithmeticType;
    }

    public Byte getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(Byte operatorType) {
        this.operatorType = operatorType;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public Long getTargetUid() {
        return targetUid;
    }

    public void setTargetUid(Long targetUid) {
        this.targetUid = targetUid;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetPhone() {
        return targetPhone;
    }

    public void setTargetPhone(String targetPhone) {
        this.targetPhone = targetPhone;
    }

    public Long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(Long operatorUid) {
        this.operatorUid = operatorUid;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorPhone() {
        return operatorPhone;
    }

    public void setOperatorPhone(String operatorPhone) {
        this.operatorPhone = operatorPhone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Float getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Float discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
