package com.everhomes.rest.warehouse;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 *     <li>id: 物品分类id</li>
 *     <li>ownerType: 物品分类所属类型 eg：EhOrganizations</li>
 *     <li>ownerId: 物品分类所属类型id</li>
 *     <li>parentId: 上级分类id</li>
 *     <li>parentCategoryNumber: 上级分类编码</li>
 *     <li>parentCategoryName: 上级分类名称</li>
 *     <li>name: 物品分类名称</li>
 *     <li>categoryNumber: 物品分类编码</li>
 *     <li>updateTime: 最后更新时间</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class WarehouseMaterialCategoryDTO {

    private Long id;

    private String ownerType;

    private Long ownerId;

    private Long parentId;

    private String parentCategoryNumber;

    private String parentCategoryName;

    private String name;

    private String categoryNumber;

    private Timestamp updateTime;

    @ItemType(WarehouseMaterialCategoryDTO.class)
    private List<WarehouseMaterialCategoryDTO> childrens;

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getParentCategoryName() {
        return parentCategoryName;
    }

    public void setParentCategoryName(String parentCategoryName) {
        this.parentCategoryName = parentCategoryName;
    }

    public String getCategoryNumber() {
        return categoryNumber;
    }

    public void setCategoryNumber(String categoryNumber) {
        this.categoryNumber = categoryNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getParentCategoryNumber() {
        return parentCategoryNumber;
    }

    public void setParentCategoryNumber(String parentCategoryNumber) {
        this.parentCategoryNumber = parentCategoryNumber;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<WarehouseMaterialCategoryDTO> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<WarehouseMaterialCategoryDTO> childrens) {
        this.childrens = childrens;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
