package com.everhomes.rest.contentserver;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul> 上传文件控件的文件详细信息
 * <li>uploadId: 上传会话 ID </li>
 * <li>title: 标题</li>
 * <li>limitCount: 文件数限制 </li>
 * <li>limitPerSize: 每个文件大小的限制 </li>
 * <li>fileExtension: 文件拓展名 </li>
 * <li>userToken: 用户登录之后的 token </li>
 * <li>contentServer: 内容服务器链接地址 </li>
 * <li>infos: 文件列表 {@link com.everhomes.rest.contentserver.UploadFileInfo}</li>
 * </ul>
 * @author janson
 *
 */
public class UploadFileInfoCommand {
    @NotNull
    private String uploadId;
    
    private Integer limitCount;
    private Integer limitPerSize;
    private String fileExtension;
    private String title;
    private String userToken;
    private String contentServer;
    
    @ItemType(UploadFileInfo.class)
    private List<UploadFileInfo> infos;

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public Integer getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(Integer limitCount) {
        this.limitCount = limitCount;
    }

    public Integer getLimitPerSize() {
        return limitPerSize;
    }

    public void setLimitPerSize(Integer limitPerSize) {
        this.limitPerSize = limitPerSize;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getContentServer() {
        return contentServer;
    }

    public void setContentServer(String contentServer) {
        this.contentServer = contentServer;
    }

    public List<UploadFileInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<UploadFileInfo> infos) {
        this.infos = infos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
