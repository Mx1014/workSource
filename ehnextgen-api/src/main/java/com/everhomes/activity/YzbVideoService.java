package com.everhomes.activity;

public interface YzbVideoService {
    YzbLiveVideoResponse startLive(String deviceId);

    YzbLiveVideoResponse setContinue(String deviceId, int b);
}
