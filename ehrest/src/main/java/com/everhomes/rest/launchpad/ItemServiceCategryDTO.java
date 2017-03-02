// @formatter:off
package com.everhomes.rest.launchpad;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * <ul>
 * <li>id: id</li>
 * <li>name: item的类别名称</li>
 * <li>iconUrl: 类别icon</li>
 * <li>order: 排序</li>
 * <li>align: 类别标题位置 {@link com.everhomes.rest.launchpad.ItemServiceCategryAlign}</li>
 * <li>status: item类别状态 {@link com.everhomes.rest.launchpad.ItemServiceCategryStatus}</li>
 * </ul>
 */
public class ItemServiceCategryDTO {
    private Long    id;
    private String name;
    private String iconUri;
    private String iconUrl;
    private Integer order;
    private Byte align;
    private Byte status;

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
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

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Byte getAlign() {
        return align;
    }

    public void setAlign(Byte align) {
        this.align = align;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
