package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>label: 门户导航栏名称</li>
 * <li>description: 门户itemGroup描述</li>
 * <li>actionType: 内容类型</li>
 * <li>actionData: 内容需要的参数，类型 我：无参数 门户：门户id(例如：{'layoutId':1})，业务应用：应用id(例如：{'moduleAppId':1}</li>
 *  <li>createTime: 创建时间</li>
 * <li>updateTime: 修改时间</li>
 * <li>creatorUid: 创建人id</li>
 * <li>creatorUName: 创建人名称</li>
 * <li>operatorUid: 操作人id</li>
 * <li>operatorUName: 操作人名称</li>
 * </ul>
 */
public class PortalNavigationBarDTO {
    private String label;
    private String contentName;
    private String description;
    private Long createTime;
    private Long updateTime;
    private Long operatorUid;
    private Long creatorUid;
    private String creatorUName;
    private String operatorUName;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(Long operatorUid) {
        this.operatorUid = operatorUid;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public String getCreatorUName() {
        return creatorUName;
    }

    public void setCreatorUName(String creatorUName) {
        this.creatorUName = creatorUName;
    }

    public String getOperatorUName() {
        return operatorUName;
    }

    public void setOperatorUName(String operatorUName) {
        this.operatorUName = operatorUName;
    }
}
