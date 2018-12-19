package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>label: 门户导航栏名称</li>
 * <li>description: 门户导航栏描述</li>
 * <li>type: 对象id</li>
 * <li>configJson: 对象类型</li>
 * <li>iconUrl: 导航的icon图片url</li>
 * <li>selectedIconUrl: 选中导航的icon图片url</li>
 *  <li>createTime: 创建时间</li>
 * <li>updateTime: 修改时间</li>
 * <li>creatorUid: 创建人id</li>
 * <li>creatorUName: 创建人名称</li>
 * <li>operatorUid: 操作人id</li>
 * <li>operatorUName: 操作人名称</li>
 * <li>topBarStyle: 顶栏样式，请参考{@link com.everhomes.rest.launchpadbase.indexconfigjson.TopBarStyle}</li>
 * </ul>
 */
public class PortalNavigationBarDTO {
    private Long id;
    private String label;
    private String contentName;
    private String description;
    private Byte type;
    private String configJson;
    private String iconUrl;
    private String iconUri;
    private String selectedIconUrl;
    private String selectedIconUri;
    private Long createTime;
    private Long updateTime;
    private Long operatorUid;
    private Long creatorUid;
    private String creatorUName;
    private String operatorUName;
    private Byte topBarStyle;

    public Byte getTopBarStyle() {
        return topBarStyle;
    }

    public void setTopBarStyle(Byte topBarStyle) {
        this.topBarStyle = topBarStyle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(String configJson) {
        this.configJson = configJson;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getSelectedIconUrl() {
        return selectedIconUrl;
    }

    public void setSelectedIconUrl(String selectedIconUrl) {
        this.selectedIconUrl = selectedIconUrl;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getSelectedIconUri() {
        return selectedIconUri;
    }

    public void setSelectedIconUri(String selectedIconUri) {
        this.selectedIconUri = selectedIconUri;
    }
}
