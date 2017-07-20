package com.everhomes.rest.portal;


import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * <li>id: 门户item的id</li>
 * <li>itemGroupId: 门户itemGroup的id</li>
 * <li>label: 门户item名称</li>
 * <li>contentName: 配置内容名称</li>
 * <li>description: 门户item描述</li>
 * <li>itemWidth: 栏目占列比</li>
 * <li>iconUrl: icon图片的url</li>
 * <li>bgcolor: 底版颜色</li>
 * <li>status: 状态</li>
 * <li>actionType: 跳转类型</li>
 * <li>actionData: 跳转参数，无：无参数，门户：门户id(例如：{'layoutId':1})，业务应用：应用id(例如：{'moduleAppId':1})，电商：还未定义，更多/全部：类型(例如：{'type':'more'}或者{'type':'all'}</li>
 * <li>createTime: 创建时间</li>
 * <li>updateTime: 修改时间</li>
 * <li>creatorUid: 创建人id</li>
 * <li>creatorUName: 创建人名称</li>
 * <li>operatorUid: 操作人id</li>
 * <li>operatorUName: 操作人名称</li>
 * <li>scopes: item范围</li>
 * </ul>
 */
public class PortalItemDTO {

    private Long id;
    private Long itemGroupId;
    private String label;
    private String contentName;
    private String iconUri;
    private String iconUrl;
    private Integer itemWidth;
    private Integer itemHeight;
    private Byte status;
    private String actionType;
    private String actionData;
    private Integer defaultOrder;
    private Byte displayFlag;
    private String selectediconUri;
    private String selectediconUrl;
    private Integer moreOrder;
    private Long itemCategryId;
    private String description;
    private Long createTime;
    private Long updateTime;
    private Long operatorUid;
    private Long creatorUid;
    private String creatorUName;
    private String operatorUName;

    @ItemType(PortalContentScopeDTO.class)
    private List<PortalContentScopeDTO> scopes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getItemGroupId() {
        return itemGroupId;
    }

    public void setItemGroupId(Long itemGroupId) {
        this.itemGroupId = itemGroupId;
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

    public Integer getItemWidth() {
        return itemWidth;
    }

    public void setItemWidth(Integer itemWidth) {
        this.itemWidth = itemWidth;
    }

    public Integer getItemHeight() {
        return itemHeight;
    }

    public void setItemHeight(Integer itemHeight) {
        this.itemHeight = itemHeight;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionData() {
        return actionData;
    }

    public void setActionData(String actionData) {
        this.actionData = actionData;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public Byte getDisplayFlag() {
        return displayFlag;
    }

    public void setDisplayFlag(Byte displayFlag) {
        this.displayFlag = displayFlag;
    }

    public String getSelectediconUrl() {
        return selectediconUrl;
    }

    public void setSelectediconUrl(String selectediconUrl) {
        this.selectediconUrl = selectediconUrl;
    }

    public Integer getMoreOrder() {
        return moreOrder;
    }

    public void setMoreOrder(Integer moreOrder) {
        this.moreOrder = moreOrder;
    }

    public Long getItemCategryId() {
        return itemCategryId;
    }

    public void setItemCategryId(Long itemCategryId) {
        this.itemCategryId = itemCategryId;
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

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getSelectediconUri() {
        return selectediconUri;
    }

    public void setSelectediconUri(String selectediconUri) {
        this.selectediconUri = selectediconUri;
    }

    public List<PortalContentScopeDTO> getScopes() {
        return scopes;
    }

    public void setScopes(List<PortalContentScopeDTO> scopes) {
        this.scopes = scopes;
    }
}
