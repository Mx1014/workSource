package com.everhomes.rest.general_approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>files: 文件列表<@link com.everhomes.rest.general_approval.PostApprovalFormFileDTO}</li>
 * </ul>
 * @author janson
 *
 */
public class PostApprovalFormFileValue {
	@ItemType(PostApprovalFormFileDTO.class)
	private List<PostApprovalFormFileDTO> files;
	
	@ItemType(String.class)
	private List<String> urls;
 

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

	public List<PostApprovalFormFileDTO> getFiles() {
		return files;
	}

	public void setFiles(List<PostApprovalFormFileDTO> files) {
		this.files = files;
	}
}
