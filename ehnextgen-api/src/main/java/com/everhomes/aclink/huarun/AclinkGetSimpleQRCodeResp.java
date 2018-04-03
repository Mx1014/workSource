package com.everhomes.aclink.huarun;

import com.everhomes.util.StringHelper;

public class AclinkGetSimpleQRCodeResp {
	private String status;
	private String qrcode;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
