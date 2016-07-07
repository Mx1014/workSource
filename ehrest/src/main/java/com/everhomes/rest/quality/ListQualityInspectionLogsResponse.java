package com.everhomes.rest.quality;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>dtos: 参考com.everhomes.rest.quality.QualityInspectionLogDTO</li>
 *  <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class ListQualityInspectionLogsResponse {
	
	@ItemType(QualityInspectionLogDTO.class)
	private List<QualityInspectionLogDTO> dtos;
	
	private Long nextPageAnchor;
	
	public List<QualityInspectionLogDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<QualityInspectionLogDTO> dtos) {
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
