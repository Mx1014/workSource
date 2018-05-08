package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>dtos: 日程列表,参考{@link com.everhomes.rest.remind.RemindDTO}</li>
 * <li>nextPageOffset: 下一页页码</li>
 * </ul>
 */
public class ListRemindResponse {
    private List<RemindDTO> dtos;
    private Integer nextPageOffset;

    public List<RemindDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<RemindDTO> dtos) {
        this.dtos = dtos;
    }

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
