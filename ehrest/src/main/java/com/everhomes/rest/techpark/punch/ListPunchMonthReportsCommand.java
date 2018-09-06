// @formatter:off
package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * <li>ownerType: 所属类型,填organization</li>
 * <li>ownerId: 公司id</li>
 * <li>punchYear: 查询年份</li>
 * </ul>
 */
public class ListPunchMonthReportsCommand {

	private Long pageAnchor;

	private Integer pageSize;

	private String ownerType;

	private Long ownerId;
	
	private String punchYear;

	public ListPunchMonthReportsCommand() {

	}

	public ListPunchMonthReportsCommand(Long pageAnchor, Integer pageSize, String ownerType, Long ownerId, String punchYear) {
		super();
		this.pageAnchor = pageAnchor;
		this.pageSize = pageSize;
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.setPunchYear(punchYear);
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getPunchYear() {
		return punchYear;
	}

	public void setPunchYear(String punchYear) {
		this.punchYear = punchYear;
	}

}
