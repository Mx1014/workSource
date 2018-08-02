package com.everhomes.rest.pushmessagelog;

/**
* <ul>
* <li>namespaceId: 域空间</li>
* <li>operatorId: 操作者ID</li>
* <li>pageSize: 页数据量</li>
* <li>pageAnchor: 本页开始的锚点</li>
* </ul>
*/
public class PushMessageListCommand {

	private Integer namespaceId ;
	private Integer operatorId ;
    private Integer  pageSize;    
    private Long pageAnchor;
    
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Integer getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
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
    
    
}
