package com.everhomes.rest.filemanagement;


import com.everhomes.util.StringHelper;

/**
 *
 * <ul>返回值:
 * <li>fileType: 文件类型(后缀名)</li>
 * <li>iconUri: 图标url</li>
 * </ul>
 */
public class FileIconDTO {


    private String fileType;
    private String iconUri;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }
}
