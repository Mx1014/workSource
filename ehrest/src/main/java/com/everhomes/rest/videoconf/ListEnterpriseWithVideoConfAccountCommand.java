package com.everhomes.rest.videoconf;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>keyword: 搜索关键字</li>
 * <li>status: 状态 0-formally use 1-on trial 2-overdue</li>
 * <li>namespaceId: 命名空间</li>
 * <li>pageAnchor: 本页开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListEnterpriseWithVideoConfAccountCommand {
	
	private String keyword;
	
	private Byte status;
	
	private Integer namespaceId;
	
	private Long pageAnchor;
	
    private Integer pageSize;
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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
