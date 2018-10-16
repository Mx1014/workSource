package com.everhomes.rest.filemanagement;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>contents: (List)要删除的文件/文件夹列表 参考 {@link com.everhomes.rest.filemanagement.FileContentDTO}</li>
 * </ul>
 */
public class DeleteFileContentCommand {
 
    private List<FileContentDTO> contents; 
    public DeleteFileContentCommand() {
    }
 

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public List<FileContentDTO> getContents() {
		return contents;
	}


	public void setContents(List<FileContentDTO> contents) {
		this.contents = contents;
	}
 
}
