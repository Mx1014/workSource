package com.everhomes.rest.reserve;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * <li>title: 标题</li>
 * <li>contents: 订单内容 {@link com.everhomes.rest.reserve.ReserveOrderContentEntity}</li>
 * </ul>
 */
public class ReserveOrderBlockEntity {
    private String title;

    @ItemType(ReserveOrderContentEntity.class)
    private List<ReserveOrderContentEntity> contents;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ReserveOrderContentEntity> getContents() {
        return contents;
    }

    public void setContents(List<ReserveOrderContentEntity> contents) {
        this.contents = contents;
    }
}
