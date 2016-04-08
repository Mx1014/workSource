package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>enterpriseId: 企业id</li>
 * <li>status: 有效状态</li>
 * <li>keyword: 关键字：姓名 手机号 部门 企业名称</li>
 * <li>isAssigned: 是否只显示已分配的账号 0-是 1-否</li>
 * <li>pageAnchor: 本页开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListEnterpriseVideoConfAccountCommand {
	
	private Long enterpriseId;
	
	private Byte status;
	
	private String keyword;
	
	private Byte isAssigned;
	
	private Long pageAnchor;
	
    private Integer pageSize;

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	
	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Byte getIsAssigned() {
		return isAssigned;
	}

	public void setIsAssigned(Byte isAssigned) {
		this.isAssigned = isAssigned;
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
