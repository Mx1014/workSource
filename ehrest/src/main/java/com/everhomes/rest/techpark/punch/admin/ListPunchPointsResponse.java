package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.punch.PunchGeoPointDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出考勤点
 * <li>nextPageAnchor: 分页，下一页锚点</li>
 * <li>points: 结果{@link com.everhomes.rest.techpark.punch.admin.PunchGeoPointDTO}</li>
 * </ul>
 */
public class ListPunchPointsResponse {
	private Long nextPageAnchor;

	@ItemType(PunchGeoPointDTO.class)
	private List<PunchGeoPointDTO> points;

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

	public List<PunchGeoPointDTO> getPoints() {
		return points;
	}

	public void setPoints(List<PunchGeoPointDTO> points) {
		this.points = points;
	} 

}
