// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属者类型，参考{@link com.everhomes.rest.express.ExpressOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>pageSize: 页面大小</li>
 * <li>pageAnchor: 锚点</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public class ListExpressHotlinesCommand {
	private String ownerType;
	private Long ownerId;
	private Integer pageSize;
	private Long pageAnchor;

	public ListExpressHotlinesCommand() {
	}

	public ListExpressHotlinesCommand(String ownerType, Long ownerId, Integer pageSize, Long pageAnchor) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.pageSize = pageSize;
		this.pageAnchor = pageAnchor;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
