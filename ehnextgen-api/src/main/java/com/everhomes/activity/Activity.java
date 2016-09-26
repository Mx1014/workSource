package com.everhomes.activity;

import com.everhomes.server.schema.tables.pojos.EhActivities;

public class Activity extends EhActivities {

    private static final long serialVersionUID = -7408570947629646190L;
    private Byte videoState;
    
    public Activity() {
    }

    public Byte getVideoState() {
        return videoState;
    }

    public void setVideoState(Byte videoState) {
        this.videoState = videoState;
    }

}
