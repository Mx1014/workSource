package com.everhomes.rest.techpark.punch;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset：下一页的页码（如果没有则为空）</li>
 * <li>exceptionRequestList：打卡异常申报信息列表，参考{@link com.everhomes.rest.techpark.punch.PunchExceptionRequestDTO}</li>
 * </ul>
 */

public class ListPunchExceptionRequestCommandResponse{
 

	private Integer nextPageOffset;
    
    @ItemType(PunchExceptionRequestDTO.class)
    private List<PunchExceptionRequestDTO> exceptionRequestList;
 
    
    public Integer getNextPageOffset() {
		return nextPageOffset;
	}


	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}


	public List<PunchExceptionRequestDTO> getExceptionRequestList() {
		return exceptionRequestList;
	}


	public void setExceptionRequestList(List<PunchExceptionRequestDTO> exceptionRequestList) {
		this.exceptionRequestList = exceptionRequestList;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
 }
