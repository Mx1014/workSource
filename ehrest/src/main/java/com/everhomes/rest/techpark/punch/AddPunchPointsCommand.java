package com.everhomes.rest.techpark.punch;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul> 
 * <li>qrToken：二维码扫描到的token</li>
 * <li>punchGeoPoints: 地点规则 {@link com.everhomes.rest.techpark.punch.PunchGeoPointDTO}</li>
 * </ul>
 */
public class AddPunchPointsCommand {
	private String qrToken;
	@ItemType(PunchGeoPointDTO.class)
	private  List<PunchGeoPointDTO>  punchGeoPoints;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public List<PunchGeoPointDTO> getPunchGeoPoints() {
		return punchGeoPoints;
	}
	public void setPunchGeoPoints(List<PunchGeoPointDTO> punchGeoPoints) {
		this.punchGeoPoints = punchGeoPoints;
	}


	public String getQrToken() {
		return qrToken;
	}

	public void setQrToken(String qrToken) {
		this.qrToken = qrToken;
	}
}
