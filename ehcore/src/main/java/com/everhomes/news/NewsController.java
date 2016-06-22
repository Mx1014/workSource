// @formatter:off
package com.everhomes.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.news.AddNewsCommentCommand;
import com.everhomes.rest.news.BriefNewsDTO;
import com.everhomes.rest.news.CreateNewsCommand;
import com.everhomes.rest.news.DeleteNewsCommand;
import com.everhomes.rest.news.DeleteNewsCommentCommand;
import com.everhomes.rest.news.ImportNewsCommand;
import com.everhomes.rest.news.ListNewsCommand;
import com.everhomes.rest.news.ListNewsCommentCommand;
import com.everhomes.rest.news.ListNewsCommentResponse;
import com.everhomes.rest.news.NewsCommentDTO;
import com.everhomes.rest.news.NewsContentDTO;
import com.everhomes.rest.news.NewsDTO;
import com.everhomes.rest.news.NewsDetailInfoCommand;
import com.everhomes.rest.news.NewsListResponse;
import com.everhomes.rest.news.SearchNewsCommand;
import com.everhomes.rest.news.SetNewsLikeFlagCommand;
import com.everhomes.rest.news.SetNewsTopFlagCommand;

@RestDoc(value = "News Controller", site = "core")
@RestController
@RequestMapping("/news")
public class NewsController extends ControllerBase {

	@Autowired
	private NewsService newsService;

	/**
	 * <b>URL: /news/createNews</b>
	 * <p>
	 * 创建一条新闻
	 * </p>
	 */
	@RequestMapping("createNews")
	@RestReturn(BriefNewsDTO.class)
	public RestResponse createNews(CreateNewsCommand cmd) {
		BriefNewsDTO briefNewsDTO = newsService.createNews(cmd);

		RestResponse response = new RestResponse(briefNewsDTO);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
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
		newsService.importNews(cmd,files);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
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
		NewsListResponse newsListResponse = newsService.listNews(cmd);

		RestResponse response = new RestResponse(newsListResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /news/searchNews</b>
	 * <p>
	 * 后台关键字搜索新闻列表
	 * </p>
	 */
	@RequestMapping("searchNews")
	@RestReturn(NewsListResponse.class)
	public RestResponse searchNews(SearchNewsCommand cmd) {
		NewsListResponse newsListResponse = newsService.searchNews(cmd);

		RestResponse response = new RestResponse(newsListResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /news/getNewsDetailInfo</b>
	 * <p>
	 * 查询新闻详情（共用）
	 * </p>
	 */
	@RequestMapping("getNewsDetailInfo")
	@RestReturn(NewsDTO.class)
	public RestResponse getNewsDetailInfo(NewsDetailInfoCommand cmd) {
		NewsDTO newsDTO = newsService.getNewsDetailInfo(cmd);

		RestResponse response = new RestResponse(newsDTO);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /news/setNewsTopFlag</b>
	 * <p>
	 * 新闻置顶/取消置顶
	 * </p>
	 */
	@RequestMapping("setNewsTopFlag")
	@RestReturn(String.class)
	public RestResponse setNewsTopFlag(SetNewsTopFlagCommand cmd) {
		newsService.setNewsTopFlag(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /news/getNewsContent</b>
	 * <p>
	 * 查询新闻正文（共用）
	 * </p>
	 */
	@RequestMapping("getNewsContent")
	@RestReturn(NewsContentDTO.class)
	public RestResponse getNewsContent(NewsDetailInfoCommand cmd) {
		NewsContentDTO newsContentDTO = newsService.getNewsContent(cmd);

		RestResponse response = new RestResponse(newsContentDTO);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /news/deleteNews</b>
	 * <p>
	 * 删除一条新闻
	 * </p>
	 */
	@RequestMapping("deleteNews")
	@RestReturn(String.class)
	public RestResponse deleteNews(DeleteNewsCommand cmd) {
		newsService.deleteNews(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /news/setNewsLikeFlag</b>
	 * <p>
	 * 点赞/取消点赞
	 * </p>
	 */
	@RequestMapping("setNewsLikeFlag")
	@RestReturn(String.class)
	public RestResponse setNewsLikeFlag(SetNewsLikeFlagCommand cmd) {
		newsService.setNewsLikeFlag(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /news/addNewsComment</b>
	 * <p>
	 * 添加一条评论
	 * </p>
	 */
	@RequestMapping("addNewsComment")
	@RestReturn(NewsCommentDTO.class)
	public RestResponse addNewsComment(AddNewsCommentCommand cmd) {
		NewsCommentDTO newsCommentDTO = newsService.addNewsComment(cmd);

		RestResponse response = new RestResponse(newsCommentDTO);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /news/listNewsComment</b>
	 * <p>
	 * 评论列表（共用）
	 * </p>
	 */
	@RequestMapping("listNewsComment")
	@RestReturn(ListNewsCommentResponse.class)
	public RestResponse listNewsComment(ListNewsCommentCommand cmd) {
		ListNewsCommentResponse listNewsCommentResponse = newsService.listNewsComment(cmd);

		RestResponse response = new RestResponse(listNewsCommentResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /news/deleteNewsComment</b>
	 * <p>
	 * 删除评论
	 * </p>
	 */
	@RequestMapping("deleteNewsComment")
	@RestReturn(String.class)
	public RestResponse deleteNewsComment(DeleteNewsCommentCommand cmd) {
		newsService.deleteNewsComment(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
