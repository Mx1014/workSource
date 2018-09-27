// @formatter:off
package com.everhomes.qrcode;

import com.everhomes.rest.qrcode.QRCodeDTO;

@Deprecated
public interface QRCodeListenerManager {

    void onQRCodeCreating(String handler, QRCode qrcode);

    void onQRCodeCreated(String handler, QRCode qrcode);

    void onGetQRCodeInfo(QRCodeDTO qrCode, String source);
}
