package com.everhomes.rest.yellowPage;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>id: tag id</li>
 * <li>parentId: 父标签id</li>
 * <li>value: 名称</li>
 * <li>isDefault: 是否是默认选项</li>
 * <li>deleteFlag: 删除符号 0 未删除 1 删除</li>
 * <li>defaultOrder: 顺序</li>
 * </ul>
 */
public class AllianceTagDTO {
	
	private Long id;
	private Long parentId;
	private String value;
	private Byte isDefault;
	private Byte deleteFlag;
	private Byte defaultOrder;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Byte getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Byte isDefault) {
		this.isDefault = isDefault;
	}

	public Byte getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Byte deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Byte getDefaultOrder() {
		return defaultOrder;
	}

	public void setDefaultOrder(Byte defaultOrder) {
		this.defaultOrder = defaultOrder;
	}

}
