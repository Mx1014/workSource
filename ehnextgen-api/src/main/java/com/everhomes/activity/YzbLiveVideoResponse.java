package com.everhomes.activity;

import com.everhomes.util.StringHelper;

// {"retval": "EOK", "retinfo":{"dsthostname":"118.26.134.12"
// , "dsthostport":1935, "dstprotocol":"rtmp"
// , "dstexkey":"rtmp://118.26.134.12:1935/live/AKAauzzOeZu636", "video_enable":1, "audio_enable":1}}
public class YzbLiveVideoResponse {
    private String retval;
    private String reterr;
    private YzbLiveVideoInfo retinfo;

    public String getReterr() {
        return reterr;
    }

    public void setReterr(String reterr) {
        this.reterr = reterr;
    }

    public String getRetval() {
        return retval;
    }

    public void setRetval(String retval) {
        this.retval = retval;
    }

    public YzbLiveVideoInfo getRetinfo() {
        return retinfo;
    }

    public void setRetinfo(YzbLiveVideoInfo retinfo) {
        this.retinfo = retinfo;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
