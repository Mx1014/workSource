package com.everhomes.xfyun;

import com.everhomes.rest.xfyun.QueryRoutersCommand;
import com.everhomes.rest.xfyun.QueryRoutersResponse;

public interface XunfeiYunService {
	QueryRoutersResponse queryRouters(QueryRoutersCommand cmd);
}
