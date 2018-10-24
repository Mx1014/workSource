//@formatter:off
package com.everhomes.asset;

/**
 * Created by Wentian on 2017/10/11.
 */
public interface PaymentConstants {
    public final String OWER_TYPE_COMMUNITY = "community";
    public final String OWNER_TYPE_ADMIN = "PM";
    
    // 连接营销/订单系统所需要的配置项 by lqs 20181006
    public final String KEY_ORDER_SERVER_CONNECTION_URL = "prmt.server.connect_url";
    public final String KEY_ORDER_SERVER_APP_KEY = "prmt.server.app_key";
    public final String KEY_ORDER_SERVER_APP_SECRET = "prmt.server.app_secret";
    public final String KEY_ORDER_DEFAULT_PERSONAL_BIND_PHONE = "prmt.default.personal_bind_phone";
    public final String KEY_SYSTEM_ID = "prmt.system_id";
}
