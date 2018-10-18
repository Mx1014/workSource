package com.everhomes.qrcode;

import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.rest.qrcode.QRCodeHandler;
import com.everhomes.rest.qrcode.QRCodeSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xq.tian on 2017/11/15.
 */
@Deprecated
@Component
public class QRCodeListenerManagerImpl implements QRCodeListenerManager, ApplicationListener<ContextRefreshedEvent> {

    @Autowired(required = false)
    private List<QRCodeModuleListener> listenerList;

    private Map<QRCodeHandler, QRCodeModuleListener> listenerMap;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null && listenerList != null) {
            listenerMap = new HashMap<>();
            for (QRCodeModuleListener listener : listenerList) {
                listenerMap.put(listener.init(), listener);
            }
        }
    }

    @Override
    public void onQRCodeCreating(String handler, QRCode qrcode) {
        QRCodeHandler qrCodeHandler = QRCodeHandler.fromCode(handler);
        if (qrCodeHandler != null) {
            QRCodeModuleListener listener = listenerMap.get(qrCodeHandler);
            listener.onQRCodeCreating(qrcode);
        }
    }

    @Override
    public void onQRCodeCreated(String handler, QRCode qrcode) {
        QRCodeHandler qrCodeHandler = QRCodeHandler.fromCode(handler);
        if (qrCodeHandler != null) {
            QRCodeModuleListener listener = listenerMap.get(qrCodeHandler);
            listener.onQRCodeCreated(qrcode);
        }
    }

    @Override
    public void onGetQRCodeInfo(QRCodeDTO qrCode, String source) {
        QRCodeHandler qrCodeHandler = QRCodeHandler.fromCode(qrCode.getHandler());
        QRCodeSource qrCodeSource = QRCodeSource.fromCode(source);
        if (qrCodeHandler != null) {
            QRCodeModuleListener listener = listenerMap.get(qrCodeHandler);
            listener.onGetQRCodeInfo(qrCode, qrCodeSource);
        }
    }
}
