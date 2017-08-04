package com.everhomes.rest.hotTag;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>keyword:关键字</li>
 * <li>serviceType:标签服务类型 参考{@link com.everhomes.rest.hotTag.HotTagServiceType}</li>
 * <li>pageAnchor: 本页开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 * <li>namespaceId: 域空间Id，不传则取当前域空间的。0为默认域空间即总列表，域空间的热门标签都应该从总列表中设置得到</li>
 *</ul>
 */
public class SearchTagCommand {

	private String keyword;
	
	private String serviceType;
	
	private Long pageAnchor;
	
    private Integer pageSize;

	private Integer namespaceId;
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
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

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
