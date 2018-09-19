package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageOffset: 下一页页码</li>
 * <li>total: 总记录数</li>
 * <li>punchMemberDTOS: 考勤人员信息，参考{@link com.everhomes.rest.techpark.punch.PunchMemberDTO}</li>
 * </ul>
 */
public class ListPunchMembersResponse {
    private Integer nextPageOffset;
    private Integer total;
    private List<PunchMemberDTO> punchMemberDTOS;

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
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
