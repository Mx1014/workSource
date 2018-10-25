package com.everhomes.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.everhomes.asset.PaymentConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.gorder.sdk.order.GeneralOrderService;
import com.everhomes.rest.promotion.http.RestClientSettings;

@Component
public class    OrderSdkProxyImpl implements ApplicationListener<ContextRefreshedEvent>{
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderSdkProxyImpl.class);
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private GeneralOrderService orderService;
    
    private RestClientSettings setting;
    
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            initRestClientSettings();
        	orderService.init(setting);
        }
    }
    
    /**
     * 每5分钟检查一下连接统一订单服务器的配置是否有变化，
     * 当配置有变化时，重新初始化连接；
     */
    @Scheduled(cron="0 0/5 * * * ?")
    protected void reflashRestClient() {
        boolean isChanged = initRestClientSettings();
        LOGGER.debug("Reflash rest client, isChanged={}", isChanged);
        if(isChanged) {
            orderService.init(setting);
        }
    }
    
    private boolean initRestClientSettings() {
        boolean isChanged = false;
        String appKey = configurationProvider.getValue(0, PaymentConstants.KEY_ORDER_SERVER_APP_KEY, "");
        String appSecret = configurationProvider.getValue(0, PaymentConstants.KEY_ORDER_SERVER_APP_SECRET, "");
        String connectUrl = configurationProvider.getValue(0, PaymentConstants.KEY_ORDER_SERVER_CONNECTION_URL, "");
        
        if(this.setting == null) {
            isChanged = true;
        } else {
            String oldAppKey = this.setting.getAppKey();
            String oldAppSecret = this.setting.getAppSecret();
            String oldConnectUrl = this.setting.getConnectUrl();
            if(appKey != null && !appKey.equals(oldAppKey)) {
                isChanged = true;
            }
            if(appSecret != null && !appSecret.equals(oldAppSecret)) {
                isChanged = true;
            }
            if(connectUrl != null && !connectUrl.equals(oldConnectUrl)) {
                isChanged = true;
            }
        }

        if(isChanged) {
            this.setting = new RestClientSettings(appKey, appSecret, connectUrl);
        }
        
        return isChanged;
    }
}
