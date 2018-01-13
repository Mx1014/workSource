package com.everhomes.rest.filemanagement;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageOffset: 下一页页码</li>
 * <li>contents: 内容列表，参考{@link com.everhomes.rest.filemanagement.FileContentDTO}</li>
 * </ul>
 */
public class ListFileContentResponse {

    private Integer nextPageOffset;

    @ItemType(FileContentDTO.class)
    private List<FileContentDTO> contents;

    public ListFileContentResponse() {
    }

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public List<FileContentDTO> getContents() {
        return contents;
    }

    public void setContents(List<FileContentDTO> contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
