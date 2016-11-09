package com.everhomes.dbsync;

public interface NashornProcessService {
    void push(NashornObject obj);
    void stop();
    void putProcessJob(NashornObject obj);
}
