package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>meterQRCode: 表的二维码</li>
 *     <li>namespaceId: 域空间</li>
 * </ul>
 * Created by ying.xiong on 2017/6/26.
 */
public class FindEnergyMeterByQRCodeCommand {

    private Long meterQRCode;
    
    private Integer namespaceId;

    public Long getMeterQRCode() {
        return meterQRCode;
    }

    public void setMeterQRCode(Long meterQRCode) {
        this.meterQRCode = meterQRCode;
    }
    
    public Integer getNamespaceId() {
		return namespaceId;
	}

    public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
