package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>tableName:表名称</li>
 * <li>deleteIds:删除的id</li>
 * </ul>
 * Created by rui.jia  2018/1/16 15 :42
 */

public class OfflineDeleteTablesInfo {

    String tableName;
    @ItemType(Long.class)
    List<Long> deleteIds;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Long> getDeleteIds() {
        return deleteIds;
    }

    public void setDeleteIds(List<Long> deleteIds) {
        this.deleteIds = deleteIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
