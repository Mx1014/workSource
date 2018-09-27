// @formatter:off
package com.everhomes.qrcode;

import com.everhomes.rest.qrcode.GetQRCodeInfoCommand;
import com.everhomes.rest.qrcode.NewQRCodeCommand;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.user.sdk.SdkQRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 已经废弃，请使用 {@link com.everhomes.user.sdk.SdkQRCodeService}
 */
@Deprecated
@Component
public class QRCodeServiceImpl implements QRCodeService {

    @Autowired
    private SdkQRCodeService sdkDelegate;

    @Override
    public QRCodeDTO createQRCode(NewQRCodeCommand cmd) {
        return sdkDelegate.createQRCode(cmd);
    }

    @Override
    public QRCodeDTO getQRCodeInfo(GetQRCodeInfoCommand cmd) {
        return sdkDelegate.getQRCodeInfo(cmd);
    }

    @Override
    public QRCodeDTO getQRCodeInfoById(String qrid, String source) {
        GetQRCodeInfoCommand cmd = new GetQRCodeInfoCommand();
        cmd.setQrid(qrid);
        cmd.setSource(source);
        return this.getQRCodeInfo(cmd);
    }
}
