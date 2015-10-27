// @formatter:off
// generated at 2015-11-03 16:20:54
// generated at 2015-10-21 17:44:18
// generated at 2015-10-26 15:50:45
// generated at 2015-10-27 13:49:25
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
