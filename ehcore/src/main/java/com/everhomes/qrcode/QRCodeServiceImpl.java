// @formatter:off
package com.everhomes.qrcode;

import com.everhomes.rest.qrcode.GetQRCodeInfoCommand;
import com.everhomes.rest.qrcode.NewQRCodeCommand;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.user.sdk.SdkQRCodeService;
import com.everhomes.util.ConvertHelper;
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
        com.everhomes.rest.user.qrcode.NewQRCodeCommand command = ConvertHelper.convert(cmd, com.everhomes.rest.user.qrcode.NewQRCodeCommand.class);
        return ConvertHelper.convert(sdkDelegate.createQRCode(command), QRCodeDTO.class);
    }

    @Override
    public QRCodeDTO getQRCodeInfo(GetQRCodeInfoCommand cmd) {
        com.everhomes.rest.user.qrcode.GetQRCodeInfoCommand command = ConvertHelper.convert(cmd, com.everhomes.rest.user.qrcode.GetQRCodeInfoCommand.class);
        return ConvertHelper.convert(sdkDelegate.getQRCodeInfo(command), QRCodeDTO.class);
    }

    @Override
    public QRCodeDTO getQRCodeInfoById(String qrid, String source) {
        GetQRCodeInfoCommand cmd = new GetQRCodeInfoCommand();
        cmd.setQrid(qrid);
        cmd.setSource(source);
        return this.getQRCodeInfo(cmd);
    }
}
