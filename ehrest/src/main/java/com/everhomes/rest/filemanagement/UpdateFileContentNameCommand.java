package com.everhomes.rest.filemanagement;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>contentId: 内容id</li>
 * <li>contentName: 内容名称</li>
 * <li>contentSuffix: 后缀名</li>
 * <li>path: 路径 格式: /目录/文件夹1/文件夹2.../文件夹n</li>
 * </ul>
 */
public class UpdateFileContentNameCommand {

    private Long contentId;

    private String contentName;

    private String contentSuffix;

    private String path;

    public UpdateFileContentNameCommand() {
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContentSuffix() {
        return contentSuffix;
    }

    public void setContentSuffix(String contentSuffix) {
        this.contentSuffix = contentSuffix;
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
