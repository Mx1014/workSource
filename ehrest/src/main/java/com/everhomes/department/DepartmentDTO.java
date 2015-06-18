// @formatter:off
package com.everhomes.department;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：主键id</li>
 * <li>parentId：所在的区域id</li>
 * <li>name：名称</li>
 * <li>path：路径，含层次关系，如/父亲id/第一层孩子id/第二层孩子id/...</li>
 * <li>level：当前层级</li>
 * <li>scopeCode：成员类型：参考{@link com.everhomes.department.DepartmentScopeCode}</li>
 * <li>status：状态：参考{@link com.everhomes.department.DepartmentStatus}</li>
 * </ul>
 */
public class DepartmentDTO {
	private Long    id;
    private Long    parentId;
	private String  name;
	private String  path;
	private Integer level;
	private Byte    scopeCode;
	private Byte    status;
	public DepartmentDTO() {
    }
	
	
	public java.lang.Long getParentId() {
		return parentId;
	}


	public void setParentId(java.lang.Long parentId) {
		this.parentId = parentId;
	}


	public java.lang.String getName() {
		return name;
	}


	public void setName(java.lang.String name) {
		this.name = name;
	}


	public java.lang.String getPath() {
		return path;
	}


	public void setPath(java.lang.String path) {
		this.path = path;
	}


	public java.lang.Integer getLevel() {
		return level;
	}


	public void setLevel(java.lang.Integer level) {
		this.level = level;
	}


	public java.lang.Byte getScopeCode() {
		return scopeCode;
	}


	public void setScopeCode(java.lang.Byte scopeCode) {
		this.scopeCode = scopeCode;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
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
}
