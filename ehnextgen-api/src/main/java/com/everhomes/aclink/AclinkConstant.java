package com.everhomes.aclink;

public interface AclinkConstant {
    public static final String HOME_URL = "home.url";
    public static final String SMS_VISITOR_USER = "username";
    public static final String SMS_VISITOR_DOOR = "doorname";
    public static final String SMS_VISITOR_LINK = "link";
    public static final String SMS_VISITOR_ID = "id";
    public static final String ACLINK_DRIVER_TYPE = "aclink.qr_driver_type";			// 二维码驱动类型 令令 华润 左邻 左邻v2
    public static final String ACLINK_VISITOR_SHORTS = "aclink.visitor_shorts";     // 访客授权短句，比如
    public static final String ACLINK_QR_TIMEOUTS = "aclink.qr_timeout"; 				// 令令二维码有效时间
    public static final String ACLINK_VISITOR_CNT = "aclink.qr_visitor_cnt"; 			// 令令访问限制 
    public static final String ACLINK_QR_DRIVER_EXT = "aclink.qr_driver_ext"; 		// 二维码驱动拓展类型，比如给保安扫描的二维码
    public static final String ACLINK_QR_DRIVER_ZUOLIN_INNER = "aclink.qr_driver_zuolin_inner"; // 左邻内部驱动类型，比如 zuolin zuolinv2
    public static final String ACLINK_USERKEY_TIMEOUTS = "aclink.user_key_timeout";  // 用户钥匙的超时时间
    public static final String ACLINK_NEW_USER_AUTO_AUTH = "aclink.new_user_auto_auth";//注册用户自动授权，目前暂时是基于域空间配置
    public static final String ACLINK_JOIN_COMPANY_AUTO_AUTH = "aclink.join_company_auto_auth";//加入公司自动授权，目前暂时是基于域空间配置，未来应该有更好的管理界面
    public static final String ACLINK_QR_IMAGE_TIMEOUTS = "aclink.qr_image_timeout";
    public static final String ACLINK_QR_IMAGE_INTRO = "aclink.qr_image_intro";
    
    public static final String QR_INTRO_URL = "http://core.zuolin.com/entrance-guard/build/index.html?hideNavigationBar=1#/instructions";
    
    public static final String ALI_APP_ID = "2017101309284970";
    public static final String ALI_APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCmFAnegdMrW2FUTnu7zn0QnJm10TpYvlBFGDXbexdD5xeqJzEQa38RouJ/2pIy+pUV3S7iN2Od0SY79t2lYGX8mnD+cukFgdPWbyScz/pJnKCxz2/cw1sKUTGqXF/CWiB47pCWFXionKzSSecuiQehX8qQBT2OWPqvU6Y1dulgx4VWpGuQUw/YPJH2b/7reBLNwGOu5gbQP/PWVXHnAeqcWgGhHcghaY1RXxwhFNdzJFUxoyzTUPF0o4nweCW66wF7nFINeST6R3B3By5HkmniUnsLKnltyRx11hJ913PsbNshtrby1qU5Vw1Ad3v/vLj3CEEa7Mfb3R4fL6l3pWyjAgMBAAECggEBAJ+Pfw/lIXJ1gCRUpzKAJ85u9oslsu4SIuLPaOcu8uJNQ4I51zzUWqgiG4UFBROvhs8Ty3iSCMZWbGqOOtMps7Km20rMMi8VVeaer8ltVCWnMwjCqNGsa2qVKadoyqPanXoykn1vsZYluTDsygz+oHMdqmsGbea2fFs4fea+EWjlnFY3O22ryTVS88OrrhF2nI6N3VexZsQZf+ZpNUJOGuQ+ubQ3wrlthv3X8Xg2gPylAPKRTvEHQaGeOZLQaGUptDpJL3o2Hbwc9JPM1g3DPLOSYm8+paoGMKF7aIVoq8C6FzjJBa32VOooT6WxqK7ooMnxyNSYbtf6kyXOF1RWhNkCgYEA0yyfT8s+prCuiy4wFxORFTR4qMnok/FcXfblDp5cLw/RxbmfMnSqUftgKchErfcdsPorBZWl81zbqyqVZq2CJF/oCRHBxTxCP9tSdgpIbuPc+2DvFQNjtDbw+TcNe4d6mfQtrkjS6APXCZcCwRPggNPWdM9eE2aTfY2pW2O147cCgYEAyVTczl7blZF9yJDJc3gDS9oBVxLwKPQJt9ak9IwB3JoiN2VFSUllMauNPyX4lTr3QRdirhN6Ehz4HyuWJIPS/6BhVSFPVObsSMrUk0/T32BI33vpGxt+IN1YXvKuExODltHBCJprGFUpx9BmixCsoYLjRNr1n6Cs8/e4ajHidnUCgYEAkqzbusCgs4bktSdPn7enfeMEK7iSIq/ySBHE53J29Pk36u4S9SizZF4FXQANNAgAR3x7hVV7/pzwtuLNApRhYIJ82E/NFwHTwibED9j0lpEOXEvnw+Cmz/WZSoBCjRp8vZIsaHGMjDqekbqh3xm/RbvdqSSfYzpMF1jd2OxY2eECgYBw5BDb9vyRpX8uU2FiSc0vXxKZTJk1nYFFgCoGper1U8gT38k2Ct2fOF5/Jj95DhNOgFpo2ar9NRk8ASizWOebRsbNAVyGcuXS+0ivQXfALTEvfz96X7QdoX/H3bQWJj8G9p6SwGOExVwkGoslNzhVaBsfpNdNpr7UHfdeNJKTcQKBgDLkqAnn256AJWmswKhY8Ly2cPMpYedkFIrrHq1l+81tLFL2bPAOyUtACz/o15Kb8Ki9ehyzTSToD/JA7gfguIRUY3AXR7if8tmVNyQddZ1BcyNqe0ZJSoDzXeRxL9Xz3fKxuMh0v5vxkpgftuYYIIrw8v5IWUivq/BQZk8q58gX";
    public static final String ALI_APP_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAphmpO+yCACAtcE724TRl9n1WGKSeXN3iaXGR5s1L72MSS0hZdS6yQaM3ZCYCEUZT0d6awCnovXrNzudzOAZ7pFW+n1WU7tgIg/1n8lv99rgFCzjm8R2qOxeI6j7k3UsS8cUHLsRnj37dr+lQyTHMLGlOS0VBe4EqwfRgq7UQS++zINcwQI05orUuv38mKwR6Eth6BOL4E+1YiAmIS35YX2KawLmeYfnOVWZ9q2l6KKQ9faIVDTdw0C8uX6yYn9ltdiB9sZJJ2Ir/Uxf8q4mk74yRjNc32k+iOg7tBwpqJdvd0ktbdKjKxoNvXKwbZiNQaKw7NuH9ql9d9Kr2gXVYFQIDAQAB";
}
