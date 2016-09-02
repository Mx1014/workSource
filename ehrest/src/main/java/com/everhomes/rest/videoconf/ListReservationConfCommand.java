package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>pageAnchor: 本页开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 * <li>enterpriseId: 公司id</li>
 * </ul>
 */
public class ListReservationConfCommand {
	
	private Long pageAnchor;
	
    private Integer pageSize;
    
    private Long enterpriseId;

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
    
	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
