// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>photoIds：图片id列表</li>
 * </ul>
 */
public class DeletePhotoByIdCommand {
	@NotEmpty
	private List<Long> photoIds;

	public List<Long> getPhotoIds() {
		return photoIds;
	}

	public void setPhotoIds(List<Long> photoIds) {
		this.photoIds = photoIds;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
