package com.everhomes.rest.activity;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>tagName: 标签名称</li>
 *     <li>signPeopleCount: 报名人数总数</li>
 *     <li>signPeopleRate: 报名人数比例</li>
 *     <li>createActivityCount:创建活动总数</li>
 *     <li>createActivityRate:创建活动比例</li>
 * </ul>
 */
public class StatisticsTagDTO {

	private String tagName;
	
	private Integer signPeopleCount;
	
	private Double signPeopleRate;
	
	private Integer createActivityCount;
	
	private Double createActivityRate;
	
	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Integer getSignPeopleCount() {
		return signPeopleCount;
	}

	public void setSignPeopleCount(Integer signPeopleCount) {
		this.signPeopleCount = signPeopleCount;
	}

	public Double getSignPeopleRate() {
		return signPeopleRate;
	}

	public void setSignPeopleRate(Double signPeopleRate) {
		this.signPeopleRate = signPeopleRate;
	}

	public Integer getCreateActivityCount() {
		return createActivityCount;
	}

	public void setCreateActivityCount(Integer createActivityCount) {
		this.createActivityCount = createActivityCount;
	}

	public Double getCreateActivityRate() {
		return createActivityRate;
	}

	public void setCreateActivityRate(Double createActivityRate) {
		this.createActivityRate = createActivityRate;
	}

	@Override
	public String toString() {
        return StringHelper.toJsonString(this);
    }
}
