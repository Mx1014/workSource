// @formatter:off
package com.everhomes.news;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.news.BriefNewsDTO;
import com.everhomes.rest.news.CreateNewsCommand;
import com.everhomes.rest.news.ImportNewsCommand;
import com.everhomes.rest.news.ListNewsCommand;
import com.everhomes.rest.news.NewsContentDTO;
import com.everhomes.rest.news.NewsDTO;
import com.everhomes.rest.news.NewsDetailInfoCommand;
import com.everhomes.rest.news.NewsListResponse;
import com.everhomes.rest.news.SetNewsTopFlagCommand;

@RestDoc(value = "News Controller", site = "core")
@RestController
@RequestMapping("/news")
public class NewsController extends ControllerBase {

	/**
	 * <b>URL: /news/createNews</b>
	 * <p>
	 * 创建一条新闻
	 * </p>
	 */
	@RequestMapping("createNews")
	@RestReturn(BriefNewsDTO.class)
	public RestResponse createNews(CreateNewsCommand cmd) {
		return new RestResponse();
	}

	/**
	 * <b>URL: /news/importNews</b>
	 * <p>
	 * 批量导入新闻
	 * </p>
	 */
	@RequestMapping("importNews")
	@RestReturn(String.class)
	public RestResponse importNews(ImportNewsCommand cmd, @RequestParam("attachment") MultipartFile[] files) {
		return new RestResponse();
	}

	/**
	 * <b>URL: /news/listNews</b>
	 * <p>
	 * 后台查询新闻列表
	 * </p>
	 */
	@RequestMapping("listNews")
	@RestReturn(NewsListResponse.class)
	public RestResponse listNews(ListNewsCommand cmd) {
		return new RestResponse();
	}

	/**
	 * <b>URL: /news/getNewsDetailInfo</b>
	 * <p>
	 * 查询新闻详情
	 * </p>
	 */
	@RequestMapping("getNewsDetailInfo")
	@RestReturn(NewsDTO.class)
	public RestResponse getNewsDetailInfo(NewsDetailInfoCommand cmd) {
		return new RestResponse();
	}

	/**
	 * <b>URL: /news/setNewsTopFlag</b>
	 * <p>
	 * 置顶新闻
	 * </p>
	 */
	@RequestMapping("setNewsTopFlag")
	@RestReturn(String.class)
	public RestResponse setNewsTopFlag(SetNewsTopFlagCommand cmd) {
		return new RestResponse();
	}
	
	/**
	 * <b>URL: /news/getNewsContent</b>
	 * <p>
	 * 查询新闻正文
	 * </p>
	 */
	@RequestMapping("getNewsContent")
	@RestReturn(NewsContentDTO.class)
	public RestResponse getNewsContent(NewsDetailInfoCommand cmd) {
		return new RestResponse();
	}

}
