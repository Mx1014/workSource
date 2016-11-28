// @formatter:off
package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>buildingName: 楼栋名称</li>
 * <li>keywords: 查询关键词</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListContractsCommand {

	private String buildingName;

	private String keywords;

	private Long pageAnchor;

	private Integer pageSize;

	public ListContractsCommand() {

	}

	public ListContractsCommand(String buildingName, String keywords, Long pageAnchor, Integer pageSize) {
		super();
		this.buildingName = buildingName;
		this.keywords = keywords;
		this.pageAnchor = pageAnchor;
		this.pageSize = pageSize;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
