package com.everhomes.aclink.lingling;

public interface AclinkLinglingConstant {
    public static String LINGLING_SERVER = "aclink.lingling.server";
    public static String LINGLING_APPKEY = "aclink.lingling.appKey";
    public static String LINGLING_SIGNATURE = "aclink.lingling.signature";
    public static String LINGLING_TOKEN = "aclink.lingling.token";
    
    public static String ADD_DEVICE = "cgi-bin/device/addDevice";
    public static String DEL_DEVICE = "cgi-bin/device/delDevice";
    public static String UPDATE_DEVICE = "cgi-bin/device/updateDevice";
    public static String QUERY_DEVICE_LIST = "cgi-bin/device/queryDeviceList";
    public static String MAKE_SDK_KEY = "cgi-bin/key/makeSdkKey";
    public static String GET_LINGLING_ID = "cgi-bin/qrcode/getLingLingId";
    public static String ADD_LIFT_QR_CODE = "cgi-bin/qrcode/addLiftQrCode";
    public static String ADD_VISITOR_QR_CODE = "cgi-bin/qrcode/addVisitorQrCode";
    public static String DEL_VISITOR_QR_CODE = "cgi-bin/qrcode/delVisitorQrCode";
    public static String SELECT_OPEN_DOOR_LOG = "cgi-bin/openDoorLog/selectOpenDoorLog";
}
