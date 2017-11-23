// @formatter:off
package com.everhomes.rest.launchpad;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>id: itemId</li>
 * <li>namespaceId: 命名空间</li>
 * <li>name: 名字</li>
 * <li>layoutJson: 服务市场版本风格</li>
 * <li>versionCode: 当前版本</li>
 * <li>minVersionCode: 最小支持版本</li>
 * <li>status: 状态</li>
 * <li>createTime: 创建时间</li>
 * <li>sceneType: 场景类型</li>
 * </ul>
 */
public class LaunchPadLayoutDTO {

    private Long     id;
    private Integer  namespaceId;
    private String   name;
    private String   layoutJson;
    private Long     versionCode;
    //private Long     minVersionCode;
    private Byte     status;
    private Timestamp createTime;
    private String sceneType;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLayoutJson() {
        return layoutJson;
    }

    public void setLayoutJson(String layoutJson) {
        this.layoutJson = layoutJson;
    }

    public Long getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Long versionCode) {
        this.versionCode = versionCode;
    }

//    public Long getMinVersionCode() {
//        return minVersionCode;
//    }
//
//    public void setMinVersionCode(Long minVersionCode) {
//        this.minVersionCode = minVersionCode;
//    }

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

}
