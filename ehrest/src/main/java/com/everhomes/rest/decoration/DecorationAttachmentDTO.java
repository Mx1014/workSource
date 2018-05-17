package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>attachmentType：附件类型  参考{@link com.everhomes.rest.decoration.DecorationAttachmentType}</li>
 * <li>name</li>
 * <li>size</li>
 * <li>fileUri</li>
 * <li>fileUrl</li>
 * </ul>
 */
public class DecorationAttachmentDTO {

    private String name;
    private String attachmentType;
    private String size;
    private String fileUri;
    private String fileUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
