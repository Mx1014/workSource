package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 任务id</li>
 * <li>type: 导入的任务类型</li>
 * <li>status: 状态</li>
 * </ul>
 */
public class ImportFileTaskDTO {
	
	private Long id;
	
	private String type;
	
	private Byte status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
