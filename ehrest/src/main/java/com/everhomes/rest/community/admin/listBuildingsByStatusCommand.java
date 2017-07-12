package com.everhomes.rest.community.admin;


/**
 * <ul>
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * <li>status: 楼栋状态,参考{@link com.everhomes.rest.address.CommunityAdminStatus}</li>
 * </ul>
 */
public class listBuildingsByStatusCommand {

	private Long pageAnchor;
    
    private Integer pageSize;
    
    private Byte status;

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

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
}
