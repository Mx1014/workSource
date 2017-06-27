package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>meterQRCode: 表的二维码</li>
 * </ul>
 * Created by ying.xiong on 2017/6/26.
 */
public class FindEnergyMeterByQRCodeCommand {

    private Long meterQRCode;

    public Long getMeterQRCode() {
        return meterQRCode;
    }

    public void setMeterQRCode(Long meterQRCode) {
        this.meterQRCode = meterQRCode;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
