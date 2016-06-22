// @formatter:off
package com.everhomes.ui.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.news.NewsService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.news.AddNewsCommentBySceneCommand;
import com.everhomes.rest.news.DeleteNewsCommentBySceneCommand;
import com.everhomes.rest.news.ListNewsBySceneCommand;
import com.everhomes.rest.news.NewsCommentDTO;
import com.everhomes.rest.news.NewsListResponse;
import com.everhomes.rest.news.SetNewsLikeFlagBySceneCommand;

/**
 * <ul>
 * <li>客户端的新闻相关api</li>
 * </ul>
 */
@RestDoc(value="NewsUi controller", site="newsUi")
@RestController
@RequestMapping("/ui/news")
public class NewsUiController {

	@Autowired
	private NewsService newsService;
	
	/**
	 * <b>URL: /ui/news/listNewsByScene</b>
	 * <p>
	 * APP端查询新闻列表
	 * </p>
	 */
	@RequestMapping("listNewsByScene")
	@RestReturn(NewsListResponse.class)
	public RestResponse listNewsByScene(ListNewsBySceneCommand cmd) {
		NewsListResponse newsListResponse = newsService.listNewsByScene(cmd);

		RestResponse response = new RestResponse(newsListResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /ui/news/setNewsLikeFlagByScene</b>
	 * <p>APP点赞/取消点赞</p>
	 */
	@RequestMapping("setNewsLikeFlagByScene")
	@RestReturn(String.class)
	public RestResponse setNewsLikeFlagByScene(SetNewsLikeFlagBySceneCommand cmd){
		newsService.setNewsLikeFlagByScene(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	
	/**
	 * <b>URL: /ui/news/addNewsCommentByScene</b>
	 * <p>APP添加一条评论</p>
	 */
	@RequestMapping("addNewsCommentByScene")
	@RestReturn(NewsCommentDTO.class)
	public RestResponse addNewsCommentByScene(AddNewsCommentBySceneCommand cmd){
		NewsCommentDTO newsCommentDTO = newsService.addNewsCommentByScene(cmd);

		RestResponse response = new RestResponse(newsCommentDTO);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
		
	/**
	 * <b>URL: /ui/news/deleteNewsCommentByScene</b>
	 * <p>APP删除评论</p>
	 */
	@RequestMapping("deleteNewsCommentByScene")
	@RestReturn(String.class)
	public RestResponse deleteNewsCommentByScene(DeleteNewsCommentBySceneCommand cmd){
		newsService.deleteNewsCommentByScene(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	
}
