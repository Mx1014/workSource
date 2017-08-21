package com.everhomes.rest.techpark.punch;

/**
 * <ul>
 * <li>codeType：二维码类型 {@link com.everhomes.rest.techpark.punch.QRType}</li>
 * <li>qrToken：二维码的token--时间戳+可以唯一确定这个考勤组的信息</li> 
 * </ul>
 */
import com.everhomes.util.StringHelper;

public class GetPunchQRCodeCommand {

	private Byte codeType ;
	
	private String qrToken;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Byte getCodeType() {
		return codeType;
	}

	public void setCodeType(Byte codeType) {
		this.codeType = codeType;
	}

	public String getQrToken() {
		return qrToken;
	}

	public void setQrToken(String qrToken) {
		this.qrToken = qrToken;
	}
	
	
}
