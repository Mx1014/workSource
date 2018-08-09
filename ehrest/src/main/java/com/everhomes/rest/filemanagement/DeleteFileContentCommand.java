package com.everhomes.rest.filemanagement;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>contendIds: (List)内容ids</li>
 * <li>path: 目标路径 (如果移动走就不删了)</li>
 * </ul>
 */
public class DeleteFileContentCommand {

    @ItemType(Long.class)
    private List<Long> contendIds;
    private String path;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
