// @formatter:off
package com.everhomes.news;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.news.CreateNewsCommand;
import com.everhomes.rest.news.ImportNewsCommand;
import com.everhomes.rest.news.ListNewsAdminResponse;
import com.everhomes.rest.news.ListNewsAppResponse;
import com.everhomes.rest.news.ListNewsCommand;
import com.everhomes.rest.news.QueryNewsContentCommand;
import com.everhomes.rest.news.QueryNewsContentResponse;
import com.everhomes.rest.news.QueryNewsDetailCommand;
import com.everhomes.rest.news.QueryNewsDetailResponse;

@RestController
@RequestMapping("/news")
public class NewsController  extends ControllerBase{
	
    /**
     * <b>URL: /news/createNews</b>
     * <p>创建一条新闻</p>
     */
	@RequestMapping("createNews")
	@RestReturn(String.class)
	public RestResponse createNews(CreateNewsCommand cmd){
		return new RestResponse();
	}
	
	/**
	 * 
	 * <b>URL: /news/importNews<b>
	 * <p>批量导入新闻</p>
	 */
	@RequestMapping("importNews")
	@RestReturn(String.class)
	public RestResponse importNews(ImportNewsCommand cmd, @RequestParam("attachment") MultipartFile[] files){
		return new RestResponse();
	}
	
	/**
	 * 
	 * <b>URL: /news/listNewsAdmin<b>
	 * <p>后台查询新闻列表</p>
	 */
	@RequestMapping("listNewsAdmin")
	@RestReturn(ListNewsAdminResponse.class)
	public RestResponse listNewsAdmin(ListNewsCommand cmd){
		return new RestResponse();
	}
	
	/**
	 * 
	 * <b>URL: /news/listNewsApp<b>
	 * <p>APP端查询新闻列表</p>
	 */
	@RequestMapping("listNewsApp")
	@RestReturn(ListNewsAppResponse.class)
	public RestResponse listNewsApp(ListNewsCommand cmd){
		return new RestResponse();
	}
	
	/**
	 * 
	 * <b>URL: /news/queryNewsDetail<b>
	 * <p>查询新闻详情（共用）</p>
	 */
	@RequestMapping("queryNewsDetail")
	@RestReturn(QueryNewsDetailResponse.class)
	public RestResponse queryNewsDetail(QueryNewsDetailCommand cmd){
		return new RestResponse();
	}
	
	/**
	 * 
	 * <b>URL: /news/queryNewsContent<b>
	 * <p>查询新闻正文</p>
	 */
	@RequestMapping("queryNewsContent")
	@RestReturn(QueryNewsContentResponse.class)
	public RestResponse queryNewsContent(QueryNewsContentCommand cmd){
		return new RestResponse();
	}
	
	/**
	 * 
	 * <b>URL: /news/stickNews<b>
	 * <p>置顶新闻</p>
	 */
	@RequestMapping("stickNews")
	@RestReturn(String.class)
	public RestResponse stickNews(QueryNewsDetailCommand cmd){
		return new RestResponse();
	}
	
	/**
	 * 
	 * <b>URL: /news/unstickNews<b>
	 * <p>取消置顶新闻</p>
	 */
	@RequestMapping("unstickNews")
	@RestReturn(String.class)
	public RestResponse unstickNews(QueryNewsDetailCommand cmd){
		return new RestResponse();
	}
}
