package com.everhomes.rest.activity;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>tagName: 标签名称</li>
 *     <li>signPeopleCount: 报名人数总数</li>
 *     <li>signPeopleRateText: 报名人数比例</li>
 *     <li>createActivityCount:创建活动总数</li>
 *     <li>createActivityRateText:创建活动比例</li>
 * </ul>
 */
public class ExportTagDTO {

	private String tagName;
	
	private Integer signPeopleCount;
	
	private String signPeopleRateText;
	
	private Integer createActivityCount;
	
	private String createActivityRateText;
	
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

	public Integer getCreateActivityCount() {
		return createActivityCount;
	}

	public void setCreateActivityCount(Integer createActivityCount) {
		this.createActivityCount = createActivityCount;
	}

	public String getSignPeopleRateText() {
		return signPeopleRateText;
	}

	public void setSignPeopleRateText(String signPeopleRateText) {
		this.signPeopleRateText = signPeopleRateText;
	}

	public String getCreateActivityRateText() {
		return createActivityRateText;
	}

	public void setCreateActivityRateText(String createActivityRateText) {
		this.createActivityRateText = createActivityRateText;
	}

	@Override
	public String toString() {
        return StringHelper.toJsonString(this);
    }
}
