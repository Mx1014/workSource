package com.everhomes.rest.field;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>items: 字段选择项信息， 参考{@link ScopeFieldItemInfo}</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class UpdateFieldItemsCommand {
    @ItemType(ScopeFieldItemInfo.class)
    private List<ScopeFieldItemInfo> items;

    public List<ScopeFieldItemInfo> getItems() {
        return items;
    }

    public void setItems(List<ScopeFieldItemInfo> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
