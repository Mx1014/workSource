// @formatter:off
package com.everhomes.department;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>

 * <li>parentId：父机构id。没有填0</li>
 * <li>name：名称</li>
 * <li>path：路径，含层次关系，如/父亲id/第一层孩子id/第二层孩子id/...</li>
 * <li>level：当前层级。没有填0</li>
 * <li>scopeCode：成员类型：参考{@link com.everhomes.department.DepartmentScopeCode}</li>
 * </ul>
 */
public class CreateDepartmentCommand {
	private Long    parentId;
	@NotNull
	private String  name;
	private String  path;
	private Integer level;
	@NotNull
	private Byte    scopeCode;
	
	public CreateDepartmentCommand() {
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


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
