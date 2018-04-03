// @formatter:off
package com.everhomes.rest.portal;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ranks: 调整item的分类集合</li>
 * </ul>
 */
public class RankPortalItemCategoryCommand {

	@ItemType(PortalItemCategoryRank.class)
	private List<PortalItemCategoryRank> ranks;

	public RankPortalItemCategoryCommand() {

	}

	public RankPortalItemCategoryCommand(List<PortalItemCategoryRank> ranks) {
		super();
		this.ranks = ranks;
	}

	public List<PortalItemCategoryRank> getRanks() {
		return ranks;
	}

	public void setRanks(List<PortalItemCategoryRank> ranks) {
		this.ranks = ranks;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
