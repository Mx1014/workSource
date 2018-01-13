package com.everhomes.rest.filemanagement;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>downloadPermission: 下载权限 0-拒绝下载 1-允许下载</li>
 * <li>folders: 文件夹列表 {@link com.everhomes.rest.filemanagement.FileContentDTO}</li>
 * <li>files: 文件列表 {@link com.everhomes.rest.filemanagement.FileContentDTO}</li>
 * </ul>
 */
public class ListFileContentResponse {

    private Integer nextPageOffset;

    private Byte downloadPermission;

    @ItemType(FileContentDTO.class)
    private List<FileContentDTO> folders;

    @ItemType(FileContentDTO.class)
    private List<FileContentDTO> files;


    public ListFileContentResponse() {
    }

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public Byte getDownloadPermission() {
        return downloadPermission;
    }

    public void setDownloadPermission(Byte downloadPermission) {
        this.downloadPermission = downloadPermission;
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
}
