package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageOffset: 下一页页码</li>
 * <li>currentItem: 当前tab，参考{@link com.everhomes.rest.techpark.punch.PunchExceptionRequestStatisticsItemDTO}</li>
 * <li>allItems: 所有tab项，参考{@link com.everhomes.rest.techpark.punch.PunchExceptionRequestStatisticsItemDTO}</li>
 * <li>punchMemberDTOS: 考勤人员信息，参考{@link com.everhomes.rest.techpark.punch.PunchMemberDTO}</li>
 * </ul>
 */
public class ListPunchExceptionRequestMembersResponse {
    private Integer nextPageOffset;
    private PunchExceptionRequestStatisticsItemDTO currentItem;
    private List<PunchExceptionRequestStatisticsItemDTO> allItems;
    private List<PunchMemberDTO> punchMemberDTOS;

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public PunchExceptionRequestStatisticsItemDTO getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(PunchExceptionRequestStatisticsItemDTO currentItem) {
        this.currentItem = currentItem;
    }

    public List<PunchExceptionRequestStatisticsItemDTO> getAllItems() {
        return allItems;
    }

    public void setAllItems(List<PunchExceptionRequestStatisticsItemDTO> allItems) {
        this.allItems = allItems;
    }

    public List<PunchMemberDTO> getPunchMemberDTOS() {
        return punchMemberDTOS;
    }

    public void setPunchMemberDTOS(List<PunchMemberDTO> punchMemberDTOS) {
        this.punchMemberDTOS = punchMemberDTOS;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
