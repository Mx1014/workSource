package com.everhomes.rest.qrcode;

public interface QRCodeServiceErrorCode {
    static final String SCOPE = "qrcode";
    
    static final int ERROR_ACTION_TYPE_INVALID = 10001;
    static final int ERROR_QR_CODE_EXPIRED = 10002;
}
