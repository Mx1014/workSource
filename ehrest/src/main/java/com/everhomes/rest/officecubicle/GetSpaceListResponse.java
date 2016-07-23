package com.everhomes.rest.officecubicle;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.officecubicle.OfficeSpaceDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出工位预定的空间
 * <li>nextPageAnchor: 分页，下一页锚点</li>
 * <li>spaces: 空间列表list{@link com.everhomes.rest.officecubicle.OfficeSpaceDTO}</li> 
 * </ul>
 */
public class GetSpaceListResponse {

	private Long nextPageAnchor;
	
	@ItemType(OfficeSpaceDTO.class)
	private List<OfficeSpaceDTO> spaces;

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

	public List<OfficeSpaceDTO> getSpaces() {
		return spaces;
	}

	public void setSpaces(List<OfficeSpaceDTO> spaces) {
		this.spaces = spaces;
	}
	
}
