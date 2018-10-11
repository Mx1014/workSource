package com.everhomes.rest.flow_statistics;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>isAll: 是否全部删除重新处理 ,值为1表示是，其他值表示否</li>
 * <li>namespaceId: 域空间ID</li>
 * </ul>
 */
public class StatisticsHanldeLogCommand {

    private Byte isAll ;
    private Integer namespaceId ;

    public Byte getIsAll() {
        return isAll;
    }

    public void setIsAll(Byte isAll) {
        this.isAll = isAll;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
