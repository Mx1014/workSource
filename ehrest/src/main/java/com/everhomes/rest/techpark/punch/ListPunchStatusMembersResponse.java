package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageOffset: 下一页页码</li>
 * <li>currentItem: 当前tab，参考{@link com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemDTO}</li>
 * <li>allItems: 所有tab项，参考{@link com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemDTO}</li>
 * <li>punchMemberDTOS: 考勤人员信息，参考{@link com.everhomes.rest.techpark.punch.PunchMemberDTO}</li>
 * </ul>
 */
public class ListPunchStatusMembersResponse {
    private Integer nextPageOffset;
    private PunchStatusStatisticsItemDTO currentItem;
    private List<PunchStatusStatisticsItemDTO> allItems;
    private List<PunchMemberDTO> punchMemberDTOS;

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public PunchStatusStatisticsItemDTO getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(PunchStatusStatisticsItemDTO currentItem) {
        this.currentItem = currentItem;
    }

    public List<PunchMemberDTO> getPunchMemberDTOS() {
        return punchMemberDTOS;
    }

    public void setPunchMemberDTOS(List<PunchMemberDTO> punchMemberDTOS) {
        this.punchMemberDTOS = punchMemberDTOS;
    }

    public List<PunchStatusStatisticsItemDTO> getAllItems() {
        return allItems;
    }

    public void setAllItems(List<PunchStatusStatisticsItemDTO> allItems) {
        this.allItems = allItems;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
