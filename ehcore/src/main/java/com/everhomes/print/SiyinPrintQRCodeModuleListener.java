package com.everhomes.print;

import org.springframework.stereotype.Component;
import com.everhomes.qrcode.QRCodeModuleListener;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.rest.qrcode.QRCodeHandler;
import com.everhomes.rest.qrcode.QRCodeSource;

/**
 * 
 * <ul>
 * <li> : </li>
 * </ul>
 *
 *  @author:dengs 2017年12月26日
 */
@Component
public class SiyinPrintQRCodeModuleListener implements QRCodeModuleListener {
    @Override
    public QRCodeHandler init() {
        return QRCodeHandler.PRINT;
    }

    @Override
    public void onGetQRCodeInfo(QRCodeDTO qrCode, QRCodeSource source) {
    	//actionData={"url":"http://printtest.zuolin.com/cloud-print/build/index.html?qrid=123213123123#/home#sign_suffix"}
    	String actionData = qrCode.getActionData();
    	actionData = actionData.replace("index.html#/home#sign_suffix", "index.html?qrid="+qrCode.getQrid()+"#/home#sign_suffix");
    	qrCode.setActionData(actionData);
    }
}