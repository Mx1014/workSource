package com.everhomes.rest.filemanagement;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>folders: 文件夹列表 {@link com.everhomes.rest.filemanagement.FileContentDTO}</li>
 * <li>files: 文件列表 {@link com.everhomes.rest.filemanagement.FileContentDTO}</li>
 * <li>path: 路径 格式: /目录/文件夹1/文件夹2.../文件夹n</li>
 * </ul>
 */
public class ListFileContentResponse {

    private Integer nextPageOffset;

    @ItemType(FileContentDTO.class)
    private List<FileContentDTO> folders;

    @ItemType(FileContentDTO.class)
    private List<FileContentDTO> files;

    private String path;

    public ListFileContentResponse() {
    }

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public List<FileContentDTO> getFolders() {
        return folders;
    }

    public void setFolders(List<FileContentDTO> folders) {
        this.folders = folders;
    }

    public List<FileContentDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileContentDTO> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
