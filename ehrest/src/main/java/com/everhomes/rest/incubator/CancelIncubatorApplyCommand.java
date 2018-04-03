package com.everhomes.rest.incubator;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 *     <li>id: id</li>
 * </ul>
 */
public class CancelIncubatorApplyCommand {

	Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
