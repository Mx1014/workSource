package com.everhomes.rest.techpark.punch;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>punchCountList：考勤统计信息列表，参考{@link com.everhomes.rest.techpark.punch.PunchCountDTO}</li>
 * </ul>
 */

public class ListPunchCountCommandResponse{
 


    
    @ItemType(PunchCountDTO.class)
    private List<PunchCountDTO> punchCountList;
 
    
	public List<PunchCountDTO> getPunchCountList() {
		return punchCountList;
	}


	public void setPunchCountList(List<PunchCountDTO> punchCountList) {
		this.punchCountList = punchCountList;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
 }
