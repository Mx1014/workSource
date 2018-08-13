// @formatter:off
package com.everhomes.rest.common;

import com.everhomes.util.StringHelper;

public class AccessControlActionData {

    private String hardwareId;

    private int isSupportSmart; 	//是否支持智能门禁，0不支持、1支持

    private int isSupportQR; 		//是否支持二维码门禁，0不支持，1支持
    
    private int isSupportFace;		//是否支持人脸识别,0否1是
    
    private int isSupportLongRange;	//是否支持远程开门,0否1是

    private String doorId;

    private int isSupportKeyShowing; //是否显示我的钥匙
    
    private int isHighlight; // 0: 不支持高亮，1 支持高亮

    public int getIsSupportSmart() {
        return isSupportSmart;
    }

    public void setIsSupportSmart(int isSupportSmart) {
        this.isSupportSmart = isSupportSmart;
    }

    public int getIsSupportQR() {
        return isSupportQR;
    }

    public void setIsSupportQR(int isSupportQR) {
        this.isSupportQR = isSupportQR;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getDoorId() {
        return doorId;
    }

    public void setDoorId(String doorId) {
        this.doorId = doorId;
    }

    public int getIsSupportKeyShowing() {
        return isSupportKeyShowing;
    }

    public void setIsSupportKeyShowing(int isSupportKeyShowing) {
        this.isSupportKeyShowing = isSupportKeyShowing;
    }

    public int getIsHighlight() {
        return isHighlight;
    }

    public void setIsHighlight(int isHighlight) {
        this.isHighlight = isHighlight;
    }

    public int getIsSupportFace() {
		return isSupportFace;
	}

	public void setIsSupportFace(int isSupportFace) {
		this.isSupportFace = isSupportFace;
	}

	public int getIsSupportLongRange() {
		return isSupportLongRange;
	}

	public void setIsSupportLongRange(int isSupportLongRange) {
		this.isSupportLongRange = isSupportLongRange;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
