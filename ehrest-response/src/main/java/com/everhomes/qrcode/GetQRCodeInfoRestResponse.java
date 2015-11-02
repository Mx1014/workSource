// @formatter:off
// generated at 2015-10-30 14:21:35
package com.everhomes.qrcode;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.qrcode.QRCodeDTO;

public class GetQRCodeInfoRestResponse extends RestResponseBase {

    private QRCodeDTO response;

    public GetQRCodeInfoRestResponse () {
    }

    public QRCodeDTO getResponse() {
        return response;
    }

    public void setResponse(QRCodeDTO response) {
        this.response = response;
    }
}
