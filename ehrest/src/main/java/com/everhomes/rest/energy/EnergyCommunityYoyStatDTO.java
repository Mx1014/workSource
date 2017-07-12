package com.everhomes.rest.energy;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>年度水电用量收支对比表
 *     <li>communityId: 小区id </li>
 *     <li>communityName: 小区名字 --如果小区名为"汇总" 小区id 为null 则是汇总结果</li>
 *     <li>areaSize: 小区面积</li>
 *     <li>statlist: 某月的汇总记录 {@link com.everhomes.rest.energy.EnergyStatByYearDTO}</li>  
 * </ul>
 */
public class EnergyCommunityYoyStatDTO {
	private Long communityId;
	private String communityName;
    private Double areaSize;
	@ItemType(EnergyStatByYearDTO.class)
	private List<EnergyStatByYearDTO> statlist;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public List<EnergyStatByYearDTO> getStatlist() {
		return statlist;
	}
	public void setStatlist(List<EnergyStatByYearDTO> statlist) {
		this.statlist = statlist;
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public Double getAreaSize() {
		return areaSize;
	}
	public void setAreaSize(Double areaSize) {
		this.areaSize = areaSize;
	}
}
