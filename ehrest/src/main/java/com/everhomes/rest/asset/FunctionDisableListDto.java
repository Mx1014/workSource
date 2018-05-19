//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/11/24.
 */
public class FunctionDisableListDto {
    private Byte hasPay;
    private Byte hasContractView;
    //是否显示上传凭证的按钮(add by tangcen)
    private Byte hasUploadCertificate;

    public Byte getHasPay() {
        return hasPay;
    }

    public void setHasPay(Byte hasPay) {
        this.hasPay = hasPay;
    }

    public Byte getHasContractView() {
        return hasContractView;
    }

    public void setHasContractView(Byte hasContractView) {
        this.hasContractView = hasContractView;
    }

	public Byte getHasUploadCertificate() {
		return hasUploadCertificate;
	}

	public void setHasUploadCertificate(Byte hasUploadCertificate) {
		this.hasUploadCertificate = hasUploadCertificate;
	}
    
}
