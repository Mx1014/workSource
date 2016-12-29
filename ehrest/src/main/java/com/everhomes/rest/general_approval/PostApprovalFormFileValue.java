package com.everhomes.rest.general_approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>uris: 文件的链接</li>
 * </ul>
 * @author janson
 *
 */
public class PostApprovalFormFileValue {
	@ItemType(String.class)
	private List<String> uris;

	public List<String> getUris() {
		return uris;
	}

	public void setUris(List<String> uris) {
		this.uris = uris;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
