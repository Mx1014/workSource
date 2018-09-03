package com.everhomes.rest.filemanagement;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>id: 内容id</li>
 * <li>catalogId: 所属目录id</li>
 * <li>name: 内容全称</li>
 * <li>size: 大小</li>
 * <li>parentId: 父级id</li>
 * <li>locationName: 所在位置</li>
 * <li>contentType: 内容类型，file-文件,folder-文件夹 参考{@link com.everhomes.rest.filemanagement.FileContentType}</li>
 * <li>contentName: 内容名称</li>
 * <li>contentSuffix: 后缀名称</li>
 * <li>contentUri: 内容uri</li>
 * <li>contentUrl: 内容url(下载链接)</li>
 * <li>iconUrl: 图标url</li>
 * <li>path: 路径 格式: /目录/文件夹1/文件夹2.../文件夹n</li>
 * <li>contents: 可见范围列表 参考{@link com.everhomes.rest.filemanagement.FileContentDTO}</li>
 * <li>createTime: 创建时间</li>
 * <li>operatorName: 更新人</li>
 * <li>updateTime: 更新时间</li>
 * </ul>
 */
public class FileContentDTO {

    private Long id;

    private Long catalogId;

    private String name;

    private Integer size;

    private Long parentId;
    
    private Long locationName;

    private String contentType;

    private String contentName;

    private String contentSuffix;

    private String contentUri;

    private String contentUrl;

    private String iconUrl;

    private String operatorName;

    private Timestamp updateTime;

    private Timestamp createTime;

    private String path;
    private List<FileContentDTO> contents;

    private String identify;

    public FileContentDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getLocationName() {
		return locationName;
	}

	public void setLocationName(Long locationName) {
		this.locationName = locationName;
	}

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<FileContentDTO> getContents() {
        return contents;
    }

    public void setContents(List<FileContentDTO> contents) {
        this.contents = contents;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }
}
