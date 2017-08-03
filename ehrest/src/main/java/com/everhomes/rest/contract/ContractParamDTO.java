package com.everhomes.rest.contract;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 园区id</li>
 *     <li>expiringPeriod: 合同到期日前多久为即将到期合同</li>
 *     <li>expiringUnit: 单位 参考{@link com.everhomes.rest.contract.PeriodUnit}</li>
 *     <li>notifyPeriod: 提醒时间</li>
 *     <li>notifyUnit: 单位 参考{@link com.everhomes.rest.contract.PeriodUnit}</li>
 *     <li>expiredPeriod: 审批通过合同转为过期的时间</li>
 *     <li>expiredUnit: 单位 参考{@link com.everhomes.rest.contract.PeriodUnit}</li>
 * </ul>
 * Created by ying.xiong on 2017/8/2.
 */
public class ContractParamDTO {

    private Long id;
    private Integer namespaceId;
    private Long communityId;
    private Integer expiringPeriod;
    private Byte expiringUnit;
    private Integer notifyPeriod;
    private Byte notifyUnit;
    private Integer expiredPeriod;
    private Byte expiredUnit;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getExpiredPeriod() {
        return expiredPeriod;
    }

    public void setExpiredPeriod(Integer expiredPeriod) {
        this.expiredPeriod = expiredPeriod;
    }

    public Byte getExpiredUnit() {
        return expiredUnit;
    }

    public void setExpiredUnit(Byte expiredUnit) {
        this.expiredUnit = expiredUnit;
    }

    public Integer getExpiringPeriod() {
        return expiringPeriod;
    }

    public void setExpiringPeriod(Integer expiringPeriod) {
        this.expiringPeriod = expiringPeriod;
    }

    public Byte getExpiringUnit() {
        return expiringUnit;
    }

    public void setExpiringUnit(Byte expiringUnit) {
        this.expiringUnit = expiringUnit;
    }

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

    public Integer getNotifyPeriod() {
        return notifyPeriod;
    }

    public void setNotifyPeriod(Integer notifyPeriod) {
        this.notifyPeriod = notifyPeriod;
    }

    public Byte getNotifyUnit() {
        return notifyUnit;
    }

    public void setNotifyUnit(Byte notifyUnit) {
        this.notifyUnit = notifyUnit;
    }
}
