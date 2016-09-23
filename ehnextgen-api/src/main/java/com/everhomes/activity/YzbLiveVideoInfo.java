package com.everhomes.activity;

import com.everhomes.util.StringHelper;

public class YzbLiveVideoInfo {
    private String dsthostname;
    private Long dsthostport;
    private String dstprotocol;
    private String dstexkey;
    private Long video_enable;
    private Long audio_enable;
    public String getDsthostname() {
        return dsthostname;
    }
    public void setDsthostname(String dsthostname) {
        this.dsthostname = dsthostname;
    }
    public Long getDsthostport() {
        return dsthostport;
    }
    public void setDsthostport(Long dsthostport) {
        this.dsthostport = dsthostport;
    }
    public String getDstprotocol() {
        return dstprotocol;
    }
    public void setDstprotocol(String dstprotocol) {
        this.dstprotocol = dstprotocol;
    }
    public String getDstexkey() {
        return dstexkey;
    }
    public void setDstexkey(String dstexkey) {
        this.dstexkey = dstexkey;
    }
    public Long getVideo_enable() {
        return video_enable;
    }
    public void setVideo_enable(Long video_enable) {
        this.video_enable = video_enable;
    }
    public Long getAudio_enable() {
        return audio_enable;
    }
    public void setAudio_enable(Long audio_enable) {
        this.audio_enable = audio_enable;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
