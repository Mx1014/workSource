// @formatter:off
package com.everhomes.rest.launchpad;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>name: item的类别名称</li>
 * <li>iconUrl: 类别icon</li>
 * <li>order: 排序</li>
 * <li>align: 类别标题位置 @link{com.everhomes.rest.launchpad.ItemServiceCategryAlign}</li>
 * </ul>
 */
public class CreateItemServiceCategryCommand {
    private String name;
    private String iconUri;
    private Integer order;
    private Byte align;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
