package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>ids: 账单id列表</li>
 *     <li>ownerId：账单所属物业公司id</li>
 *     <li>ownerType：账单所属物业公司类型</li>
 *     <li>targetId：账单所属园区id</li>
 *     <li>targetType：账单所属园区类型</li>
 * </ul>
 */
public class BillIdListCommand {

    @NotNull
    private Long ownerId;

    @NotNull
    private String ownerType;

    @NotNull
    private Long targetId;

    @NotNull
    private String targetType;

    @ItemType(Long.class)
    private List<Long> ids;

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

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
