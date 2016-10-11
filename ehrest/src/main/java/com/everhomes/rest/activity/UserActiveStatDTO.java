package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * 
 * @author wh
 *<ul>
 *<li>id:id</li>
 *<li>statDate: 统计时间</li>
 *<li>namespaceId: 域空间</li>
 *<li>activeCount: 活跃人数</li>
 *<li>totalCount: 总人数</li>
 *</ul>
 */
public class UserActiveStatDTO {
    private Long id;
    private Long statDate;
    private Integer namespaceId;
    private Integer activeCount;
    private Integer totalCount;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStatDate() {
		return statDate;
	}
	public void setStatDate(Long statDate) {
		this.statDate = statDate;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Integer getActiveCount() {
		return activeCount;
	}
	public void setActiveCount(Integer activeCount) {
		this.activeCount = activeCount;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
