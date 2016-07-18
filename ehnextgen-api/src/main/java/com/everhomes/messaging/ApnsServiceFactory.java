package com.everhomes.messaging;

import com.notnoop.apns.ApnsService;

public interface ApnsServiceFactory {
    ApnsService getApnsService(String partner);
    void stopApnsServiceByName(String partner);
}
