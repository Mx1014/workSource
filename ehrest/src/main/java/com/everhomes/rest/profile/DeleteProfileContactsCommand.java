package com.everhomes.rest.profile;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * <li>detailIds: (List)成员 detailIds 列表</li>
 * </ul>
 */
public class DeleteProfileContactsCommand {

    @ItemType(Long.class)
    private List<Long> detailIds;

    public DeleteProfileContactsCommand() {
    }

    public List<Long> getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(List<Long> detailIds) {
        this.detailIds = detailIds;
    }
}
