// @formatter:off
package com.everhomes.qrcode;

import com.everhomes.rest.qrcode.GetQRCodeInfoCommand;
import com.everhomes.rest.qrcode.NewQRCodeCommand;
import com.everhomes.rest.qrcode.QRCodeDTO;

@Deprecated
public interface QRCodeService {

    QRCodeDTO createQRCode(NewQRCodeCommand cmd);

    QRCodeDTO getQRCodeInfo(GetQRCodeInfoCommand cmd);

    QRCodeDTO getQRCodeInfoById(String qrid, String source);

}
