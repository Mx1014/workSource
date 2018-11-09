// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>id: 门禁或门禁组ID </li>
 * <li>type: 类型：1门禁 2门禁组</li>
 * <li>name: 门禁（组）名称</li>
 * <li>status：状态： 0失效 1有效</li>
 * </ul>
 *
 */
public class DoorsAndGroupsDTO {
	private Long id;
	private Byte type;
	private String name;
	private Byte Status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getStatus() {
		return Status;
	}

	public void setStatus(Byte status) {
		Status = status;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
