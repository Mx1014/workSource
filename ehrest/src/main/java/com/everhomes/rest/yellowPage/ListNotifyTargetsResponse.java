package com.everhomes.rest.yellowPage;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>dtos: 参考 {@link com.everhomes.rest.yellowPage.NotifyTargetDTO}</li>
 *  <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class ListNotifyTargetsResponse {

	@ItemType(NotifyTargetDTO.class)
	private List<NotifyTargetDTO> dtos;
	
	private Long nextPageAnchor;

	public List<NotifyTargetDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<NotifyTargetDTO> dtos) {
		this.dtos = dtos;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
