package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>details: 按时间排序的详细信息,参考{@link com.everhomes.rest.techpark.punch.PunchExceptionRequestItemDetailDTO}</li>
 * <li>itemNum: 项目数量</li>
 * </ul>
 */
public class ListPunchExceptionRequestItemDetailResponse {
    private List<PunchExceptionRequestItemDetailDTO> details;
    private Integer itemNum;
    public List<PunchExceptionRequestItemDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<PunchExceptionRequestItemDetailDTO> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Integer getItemNum() {
        return itemNum;
    }

    public void setItemNum(Integer itemNum) {
        this.itemNum = itemNum;
    }
}
