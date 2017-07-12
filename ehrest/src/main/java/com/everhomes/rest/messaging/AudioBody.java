package com.everhomes.rest.messaging;

import com.everhomes.util.StringHelper;

public class AudioBody {
    private String url;
    private String uri;
    private String filename;
    private String format;
    
    private Long fileSize;
    private String duration;
    
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    public Long getFileSize() {
        return fileSize;
    }
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
