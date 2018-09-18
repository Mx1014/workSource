package com.everhomes.rest.contentserver;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

public class UploadFileCommand {

    private String fileName;

    private String url;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
