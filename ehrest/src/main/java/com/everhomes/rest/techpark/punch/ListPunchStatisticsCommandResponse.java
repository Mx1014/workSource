package com.everhomes.rest.techpark.punch;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset：下一页的页码（如果没有则为空）</li>
 * <li>punchList：打卡统计信息列表，参考{@link com.everhomes.rest.techpark.punch.PunchStatisticsDTO}</li>
 * </ul>
 */

public class ListPunchStatisticsCommandResponse{
 

	private Integer nextPageOffset;
    
    @ItemType(PunchStatisticsDTO.class)
    private List<PunchStatisticsDTO> punchList;
 
    
    public Integer getNextPageOffset() {
		return nextPageOffset;
	}


	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}



	public List<PunchStatisticsDTO> getPunchList() {
		return punchList;
	}


	public void setPunchList(List<PunchStatisticsDTO> punchList) {
		this.punchList = punchList;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
 }
