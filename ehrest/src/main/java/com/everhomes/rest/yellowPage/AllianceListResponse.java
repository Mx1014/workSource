package com.everhomes.rest.yellowPage;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
* 
* <ul>
* <li>nextPageAnchor : 下一页锚点</li>
* <li>dtos : 获取的列表数据</li>
* </ul>
**/
public class AllianceListResponse<T> {
	
	private Long nextPageAnchor;
	private  List<T> dtos;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<T> getDtos() {
		return dtos;
	}

	public void setDtos(List<T> dtos) {
		this.dtos = dtos;
	}
	
}
