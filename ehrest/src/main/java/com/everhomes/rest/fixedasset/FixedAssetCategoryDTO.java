package com.everhomes.rest.fixedasset;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>id: 分类ID，非空</li>
 * <li>name: 分类名称,非空</li>
 * <li>subCategories: 子分类列表，可为空，参考 {@link com.everhomes.rest.fixedasset.FixedAssetCategoryDTO}</li>
 * </ul>
 */
public class FixedAssetCategoryDTO {
    private Integer id;
    private String name;
    @ItemType(FixedAssetCategoryDTO.class)
    private List<FixedAssetCategoryDTO> subCategories;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FixedAssetCategoryDTO> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<FixedAssetCategoryDTO> subCategories) {
        this.subCategories = subCategories;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
