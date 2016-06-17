package com.everhomes.ui.news;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.news.ListNewsBySceneCommand;
import com.everhomes.rest.news.NewsListResponse;

/**
 * <ul>
 * <li>客户端的新闻相关api</li>
 * </ul>
 */
@RestDoc(value="NewsUi controller", site="newsUi")
@RestController
@RequestMapping("/ui/news")
public class NewsUiController {

	/**
	 * <b>URL: /news/listNewsByScene<b>
	 * <p>
	 * APP端查询新闻列表
	 * </p>
	 */
	@RequestMapping("listNewsByScene")
	@RestReturn(NewsListResponse.class)
	public RestResponse listNewsByScene(ListNewsBySceneCommand cmd) {
		return new RestResponse();
	}
}
