package com.everhomes.rest.pushmessagelog;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
* <ul>
* <li>dtos: 后台返回数组 ，参考{@link com.everhomes.rest.pushmessagelog.PushMessageLogDTO}</li>
* <li>nextPageAnchor: 下一页开始锚点</li>
* </ul>
*/
public class PushMessageLogReturnDTO {
	private List<PushMessageLogDTO> dtos;
	
    private Long nextPageAnchor;
    


	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}


	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<PushMessageLogDTO> getDtos() {
		return dtos;
	}


	public void setDtos(List<PushMessageLogDTO> dtos) {
		this.dtos = dtos;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
