// @formatter:off
// generated at 2015-11-03 16:20:54
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
