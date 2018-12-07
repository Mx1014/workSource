package com.everhomes.xfyun;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.xfyun.QueryRoutersCommand;
import com.everhomes.rest.xfyun.QueryRoutersResponse;

public interface XunfeiYunService {
	QueryRoutersResponse queryRouters(QueryRoutersCommand cmd);

	void afterDeal(HttpServletRequest req, HttpServletResponse resp);
}
