// @formatter:off
package com.everhomes.rest.openapi.jindi;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>dataType: 数据类型</li>
 * <li>actionType: 行为类型，如果为客户或客房则此值为空</li>
 * <li>subActionType: 行为子类型，暂为空</li>
 * <li>beginTime: 取数据的时间戳</li>
 * <li>pageAnchor: 取数据的锚点，因为如果遇到同一个时间戳一次取不完，需要从此锚点继续取</li>
 * <li>pageSize: 每次取多少条数据</li>
 * </ul>
 */
public class JindiFetchDataCommand {
	private Integer namespaceId;
	private String dataType;
	private String actionType;
	private String subActionType;
	private Long beginTime;
	private Long pageAnchor;
	private Integer pageSize;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getSubActionType() {
		return subActionType;
	}

	public void setSubActionType(String subActionType) {
		this.subActionType = subActionType;
	}

	public Long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
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
