package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
	* 
	* <ul>
	* <li>namespaceId : 域空间id</li>
	* <li>ownerType: 所属类型</li>
	* <li>ownerId: 所属项目ID</li>
	* <li>flowCaseId: 工作流ID isByFlow为1时传此值</li>
	* <li>type: 服务联盟类型值 isByFlow为1时非必传，isByFlow为0时必传</li>
	* <li>categoryId: 服务类型id</li>
	* <li>keyword: key值，可以查询服务商名称，联系人，联系电话</li>
	* <li>pageSize: 获取的数据条数，如需所有传空</li>
	* <li>anchor: 锚点</li>
	* <li>isByFlow: 是否根据工作流获取，用于客户端 1-根据工作流获取 0-不根据工作流获取</li>
	* <li>appId: 后台校验权限时需要添加应用id</li>
	* <li>currentPMId: 后台校验权限时需要添加管理公司id</li>
	* </ul>
	*  @author
	*  huangmingbo 2018年5月17日
**/
public class ListServiceAllianceProvidersCommand {
	
	@NotNull
	private Integer namespaceId;
    @NotNull
    private String ownerType;
    @NotNull
    private Long ownerId;
    
    private Long flowCaseId;
    
    private Long type;
    
    private Long categoryId;
    
    private String keyword;
    
    private Integer pageSize;
    
    private Long anchor;
    
    private Byte isByFlow;
    
	private Long appId;	
	
	private Long currentPMId;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getAnchor() {
		return anchor;
	}

	public void setAnchor(Long anchor) {
		this.anchor = anchor;
	}

	public Long getFlowCaseId() {
		return flowCaseId;
	}

	public void setFlowCaseId(Long flowCaseId) {
		this.flowCaseId = flowCaseId;
	}

	public Byte getIsByFlow() {
		return isByFlow;
	}

	public void setIsByFlow(Byte isByFlow) {
		this.isByFlow = isByFlow;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getCurrentPMId() {
		return currentPMId;
	}

	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}

}
