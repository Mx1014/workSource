// @formatter:off
// generated at 2015-07-31 09:39:28
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
