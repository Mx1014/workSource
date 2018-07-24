package com.everhomes.rest.fixedasset;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>namespaceId: 域空间ID</li>
 * <li>ownerType: 默认EhOrganizations</li>
 * <li>ownerId: 公司ID，必填</li>
 * <li>fixedAssetCategoryId: 分类ID</li>
 * <li>statusList: 状态列表，参考{@link com.everhomes.rest.fixedasset.FixedAssetStatus}</li>
 * <li>keyWord: 资产编号或名称</li>
 * <li>buyDateStart: 开始购买日期，格式：yyyy-MM-dd</li>
 * <li>buyDateEnd: 结束购买日期，格式：yyyy-MM-dd</li>
 * <li>occupyDateStart: 开始领用日期，格式：yyyy-MM-dd</li>
 * <li>occupyDateEnd: 结束领用日期，格式：yyyy-MM-dd</li>
 * <li>occupiedDepartmentIds: 使用部门Id列表</li>
 * <li>occupiedMemberDetailIds: 使用人detailId列表</li>
 * <li>pageAnchor: 第一页时客户端不用传</li>
 * <li>pageSize: 每页查询记录数</li>
 * </ul>
 */
public class ListFixedAssetCommand {
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Integer fixedAssetCategoryId;
    @ItemType(Byte.class)
    private List<Byte> statusList;
    private String keyWord;
    private String buyDateStart;
    private String buyDateEnd;
    private String occupyDateStart;
    private String occupyDateEnd;
    private List<Long> occupiedDepartmentIds;
    private List<Long> occupiedMemberDetailIds;
    private Long pageAnchor;
    private Integer pageSize;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getFixedAssetCategoryId() {
        return fixedAssetCategoryId;
    }

    public void setFixedAssetCategoryId(Integer fixedAssetCategoryId) {
        this.fixedAssetCategoryId = fixedAssetCategoryId;
    }

    public List<Byte> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Byte> statusList) {
        this.statusList = statusList;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getBuyDateStart() {
        return buyDateStart;
    }

    public void setBuyDateStart(String buyDateStart) {
        this.buyDateStart = buyDateStart;
    }

    public String getBuyDateEnd() {
        return buyDateEnd;
    }

    public void setBuyDateEnd(String buyDateEnd) {
        this.buyDateEnd = buyDateEnd;
    }

    public String getOccupyDateStart() {
        return occupyDateStart;
    }

    public void setOccupyDateStart(String occupyDateStart) {
        this.occupyDateStart = occupyDateStart;
    }

    public String getOccupyDateEnd() {
        return occupyDateEnd;
    }

    public void setOccupyDateEnd(String occupyDateEnd) {
        this.occupyDateEnd = occupyDateEnd;
    }

    public List<Long> getOccupiedDepartmentIds() {
        return occupiedDepartmentIds;
    }

    public void setOccupiedDepartmentIds(List<Long> occupiedDepartmentIds) {
        this.occupiedDepartmentIds = occupiedDepartmentIds;
    }

    public List<Long> getOccupiedMemberDetailIds() {
        return occupiedMemberDetailIds;
    }

    public void setOccupiedMemberDetailIds(List<Long> occupiedMemberDetailIds) {
        this.occupiedMemberDetailIds = occupiedMemberDetailIds;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
