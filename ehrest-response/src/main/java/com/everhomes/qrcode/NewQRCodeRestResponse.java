// @formatter:off
// generated at 2015-08-21 13:34:54
package com.everhomes.qrcode;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.qrcode.QRCodeDTO;

public class NewQRCodeRestResponse extends RestResponseBase {

    private QRCodeDTO response;

    public NewQRCodeRestResponse () {
    }

    public QRCodeDTO getResponse() {
        return response;
    }

    public void setResponse(QRCodeDTO response) {
        this.response = response;
    }
}
