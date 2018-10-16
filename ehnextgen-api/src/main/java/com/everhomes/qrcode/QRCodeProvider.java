// @formatter:off
package com.everhomes.qrcode;

@Deprecated
public interface QRCodeProvider {
    void createQRCode(QRCode qrcode);
    void updateQRCode(QRCode qrcode);
    void deleteQRCode(QRCode qrcode);
    void deleteQRCodeById(long qrcodeId);
    QRCode findQRCodeById(long qrcodeId);
}
