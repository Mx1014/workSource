package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>videoSupportType: 0: NO_SUPPORT, 1: VIDEO_ONLY, 2: VIDEO_BOTH {@link com.everhomes.rest.activity.VideoSupportType} </li>
 * </ul>
 * @author janson
 *
 */
public class VideoCapabilityResponse {
    private Byte videoSupportType;

    public Byte getVideoSupportType() {
        return videoSupportType;
    }

    public void setVideoSupportType(Byte videoSupportType) {
        this.videoSupportType = videoSupportType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
