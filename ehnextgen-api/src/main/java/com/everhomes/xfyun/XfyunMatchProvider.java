package com.everhomes.xfyun;

public interface XfyunMatchProvider {

	XfyunMatch findMatch(String vendor, String service, String intent);
}
