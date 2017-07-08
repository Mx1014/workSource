package com.everhomes.rest.portal;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * <li>id: 分类id</li>
 * <li>name: 分类名称</li>
 * <li>iconUri: icon图片的url</li>
 * <li>items: items列表，参考{@link com.everhomes.rest.portal.PortalItemDTO}</li>
 * <li>scopes: item范围，参考{@link com.everhomes.rest.portal.PortalContentScopeDTO}</li>
 * </ul>
 */
public class PortalItemCategoryDTO {

    private Long id;
    private String name;
    private String iconUrl;
    private Integer defaultOrder;
    private String align;

    @ItemType(PortalItemDTO.class)
    private List<PortalItemDTO> items;

    @ItemType(PortalContentScopeDTO.class)
    private List<PortalContentScopeDTO> scopes;

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

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public List<PortalItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PortalItemDTO> items) {
        this.items = items;
    }

    public List<PortalContentScopeDTO> getScopes() {
        return scopes;
    }

    public void setScopes(List<PortalContentScopeDTO> scopes) {
        this.scopes = scopes;
    }
}
