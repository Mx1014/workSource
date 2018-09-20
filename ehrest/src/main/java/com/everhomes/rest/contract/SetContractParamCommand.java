package com.everhomes.rest.contract;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

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
 *     <li>receivableDate: 合同费用清单应收日期</li>
 *     <li>receivableUnit: 合同费用清单应收日期单位 参考{@link com.everhomes.rest.contract.PeriodUnit}</li>
 *     <li>payorreceiveContractType: 合同类型 0 收款合同 1付款合同</li>
 *     <li>generateContractNumberRule: 合同编号规则</li>
 *     <li>categoryId: 合同类型categoryId，用于多入口</li>
 * </ul>
 * Created by ying.xiong on 2017/8/2.
 */
public class SetContractParamCommand {
    private Long id;
    private Integer namespaceId;
    private Long orgId;
    private Long communityId;
    private Integer expiringPeriod;
    private Byte expiringUnit;
    private Integer notifyPeriod;
    private Byte notifyUnit;
    private Integer expiredPeriod;
    private Byte expiredUnit;
    private Integer receivableDate;
    private Byte receivableUnit;

    private Integer paidPeriod;
    
    private Byte payorreceiveContractType;
    
    @ItemType(GenerateContractNumberRule.class)
    private GenerateContractNumberRule generateContractNumberRule;

    
    public GenerateContractNumberRule getGenerateContractNumberRule() {
		return generateContractNumberRule;
	}

	public void setGenerateContractNumberRule(GenerateContractNumberRule generateContractNumberRule) {
		this.generateContractNumberRule = generateContractNumberRule;
	}

	public Byte getPayorreceiveContractType() {
		return payorreceiveContractType;
	}

	public void setPayorreceiveContractType(Byte payorreceiveContractType) {
		this.payorreceiveContractType = payorreceiveContractType;
	}

	private Long categoryId;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
	@ItemType(ContractParamGroupMapDTO.class)
    private List<ContractParamGroupMapDTO> notifyGroups;
    @ItemType(ContractParamGroupMapDTO.class)
    private List<ContractParamGroupMapDTO> paidGroups;

    public List<ContractParamGroupMapDTO> getNotifyGroups() {
        return notifyGroups;
    }

    public void setNotifyGroups(List<ContractParamGroupMapDTO> notifyGroups) {
        this.notifyGroups = notifyGroups;
    }

    public List<ContractParamGroupMapDTO> getPaidGroups() {
        return paidGroups;
    }

    public void setPaidGroups(List<ContractParamGroupMapDTO> paidGroups) {
        this.paidGroups = paidGroups;
    }

    public Integer getPaidPeriod() {
        return paidPeriod;
    }

    public void setPaidPeriod(Integer paidPeriod) {
        this.paidPeriod = paidPeriod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
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

    public Integer getReceivableDate() {
        return receivableDate;
    }

    public void setReceivableDate(Integer receivableDate) {
        this.receivableDate = receivableDate;
    }

    public Byte getReceivableUnit() {
        return receivableUnit;
    }

    public void setReceivableUnit(Byte receivableUnit) {
        this.receivableUnit = receivableUnit;
    }

    @Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
