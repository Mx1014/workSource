// @formatter:off
// generated at 2015-08-20 19:14:55
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
