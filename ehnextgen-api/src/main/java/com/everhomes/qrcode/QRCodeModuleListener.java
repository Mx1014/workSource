// @formatter:off
package com.everhomes.qrcode;

import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.rest.qrcode.QRCodeHandler;
import com.everhomes.rest.qrcode.QRCodeSource;

/**
 * 该接口已经废弃, 请勿使用
 */
@Deprecated
public interface QRCodeModuleListener {

    QRCodeHandler init();

    default void onQRCodeCreating(QRCode qrcode) { }

    default void onQRCodeCreated(QRCode qrcode) { }

    default void onGetQRCodeInfo(QRCodeDTO qrCode, QRCodeSource source) { }
}
