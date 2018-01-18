package com.everhomes.rest.filemanagement;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>contendIds: (List)内容ids</li>
 * </ul>
 */
public class DeleteFileContentCommand {

    @ItemType(Long.class)
    private List<Long> contendIds;

    public DeleteFileContentCommand() {
    }

    public List<Long> getContendIds() {
        return contendIds;
    }

    public void setContendIds(List<Long> contendIds) {
        this.contendIds = contendIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
