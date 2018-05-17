// @formatter:off
package com.everhomes.rest.launchpad;

import com.everhomes.rest.launchpadbase.LayoutJsonDTO;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: 广场布局Id</li>
 *     <li>namespaceId: 命名空间</li>
 *     <li>name: 名字</li>
 *     <li>layoutJson: 服务市场版本风格，参考{@link LayoutJsonDTO}</li>
 *     <li>versionCode: 当前版本</li>
 *     <li>status: 状态</li>
 *     <li>createTime: 创建时间</li>
 *     <li>sceneType: 场景类型（以前的字段，标准版不用）</li>
 *     <li>bgImageUri: 背景图片（以前的字段，标准版不用）</li>
 *     <li>bgImageUrl: 背景图片（以前的字段，标准版不用）</li>
 *     <li>type: 类型 参考{@link com.everhomes.rest.launchpadbase.LayoutType}</li>
 *     <li>bgColor: 背景颜色</li>
 * </ul>
 */
public class LaunchPadLayoutDTO {

    private Long id;
    private Integer namespaceId;
    private String name;
    private String layoutJson;
    private Long versionCode;
    //private Long     minVersionCode;
    private Byte status;
    private Timestamp createTime;
    private String sceneType;

    private String bgImageUri;

    private String bgImageUrl;

    private Byte type;
    private String bgColor;

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

    public String getBgImageUri() {
        return bgImageUri;
    }

    public void setBgImageUri(String bgImageUri) {
        this.bgImageUri = bgImageUri;
    }

    public String getBgImageUrl() {
        return bgImageUrl;
    }

    public void setBgImageUrl(String bgImageUrl) {
        this.bgImageUrl = bgImageUrl;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
