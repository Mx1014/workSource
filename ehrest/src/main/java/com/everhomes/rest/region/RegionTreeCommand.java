// @formatter:off
package com.everhomes.rest.region;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>parentId: 父亲区域的ID</li>
 * <li>scope: 范围，参考{@link RegionScope}</li>
 * <li>status: 状态，参考{@link RegionAdminStatus}</li>
 * <li>namespaceId: 域空间id 没有默认取用户的</li>
 * </ul>
 */
public class RegionTreeCommand {
    private Long parentId;
    private Byte scope;
    private Byte status;
    private Integer namespaceId;

    public RegionTreeCommand() {
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Byte getScope() {
        return scope;
    }

    public void setScope(Byte scope) {
        this.scope = scope;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

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
}
