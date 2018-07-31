// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>photoIds:照片id列表</li>
 * </ul>
 *
 */
public class UpdateVistorSyncTimeCommand {
	
	private String photoIds;

	public String getPhotoIds() {
		return photoIds;
	}

	public void setPhotoIds(String photoIds) {
		this.photoIds = photoIds;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
