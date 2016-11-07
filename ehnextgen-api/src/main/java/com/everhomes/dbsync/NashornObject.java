package com.everhomes.dbsync;

public interface NashornObject {
    long getId();
    void setId(long id);
    long getCreateTime();
    long getTimeout();
    void onError(Exception ex);
    void onComplete();
    String getJSFunc();
}
