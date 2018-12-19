package com.everhomes.rest.launchpad;

import com.everhomes.util.StringHelper;
import java.sql.Timestamp;

/**
 * <ul>
 * <li>name: 门户导航栏名称</li>
 * <li>description: 门户导航栏描述</li>
 * <li>type: 主页签类型，请参考{@link com.everhomes.rest.launchpadbase.IndexType}</li>
 * <li>configJson: 对象类型，如果type为门户，请参考{@link com.everhomes.rest.launchpadbase.indexconfigjson.Container}，
 *                  如果type为应用，请参考{@link com.everhomes.rest.launchpadbase.indexconfigjson.Application}</li>
 * <li>iconUrl: 导航的icon图片url</li>
 * <li>selectedIconUrl: 选中导航的icon图片url</li>
 *  <li>createTime: 创建时间</li>
 * <li>updateTime: 修改时间</li>
 * <li>creatorUid: 创建人id</li>
 * <li>creatorUName: 创建人名称</li>
 * <li>operatorUid: 操作人id</li>
 * <li>operatorUName: 操作人名称</li>
 * </ul>
 */
public class LaunchPadIndexDTO {
    private Byte     status;
    private Timestamp     updateTime;
    private Long     operatorUid;
    private String     name;
    private Long     creatorUid;
    private Timestamp     createTime;
    private Integer     namespaceId;
    private String     configJson;
    private String     iconUri;
    private String     iconUrl;
    private String     selectedIconUri;
    private String     selectedIconUrl;
    private Byte     type;
    private Long     id;
    private String     description;


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

    public Byte getStatus() {
        return status;
    }


    public void setStatus(Byte status) {
        this.status = status;
    }


    public Timestamp getUpdateTime() {
        return updateTime;
    }


    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }


    public Long getOperatorUid() {
        return operatorUid;
    }


    public void setOperatorUid(Long operatorUid) {
        this.operatorUid = operatorUid;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Long getCreatorUid() {
        return creatorUid;
    }


    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }


    public Timestamp getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }


    public Integer getNamespaceId() {
        return namespaceId;
    }


    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }


    public String getConfigJson() {
        return configJson;
    }


    public void setConfigJson(String configJson) {
        this.configJson = configJson;
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

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
