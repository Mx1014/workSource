package com.everhomes.rest.techpark.punch;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.punch.admin.PunchWiFiDTO;
import com.everhomes.util.StringHelper;
/**
 * <ul> 
 * <li>qrToken：二维码扫描到的token</li>
 * <li>wifis: wifi规则{@link com.everhomes.rest.techpark.punch.admin.PunchWiFiDTO}</li>
 * </ul>
 */
public class AddPunchWifisCommand {
	private String qrToken;
	@ItemType(PunchWiFiDTO.class)
	private  List<PunchWiFiDTO>  wifis;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


	public List<PunchWiFiDTO> getWifis() {
		return wifis;
	}

	public void setWifis(List<PunchWiFiDTO> wifis) {
		this.wifis = wifis;
	}


	public String getQrToken() {
		return qrToken;
	}

	public void setQrToken(String qrToken) {
		this.qrToken = qrToken;
	}
}
