package com.everhomes.rest.filemanagement;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

public class SearchFileResponse {

    @ItemType(FileCatalogDTO.class)
    private List<FileCatalogDTO> catalogs;

    @ItemType(FileContentDTO.class)
    private List<FileContentDTO> folders;

    @ItemType(FileContentDTO.class)
    private List<FileContentDTO> files;

    public SearchFileResponse() {
    }

    public List<FileCatalogDTO> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<FileCatalogDTO> catalogs) {
        this.catalogs = catalogs;
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
