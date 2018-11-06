// @formatter:off
package com.everhomes.news;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.news.*;
import com.everhomes.util.StringHelper;

import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.jooq.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.RequireAuthentication;

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
	@RestReturn(CreateNewsResponse.class)
	public RestResponse createNews(CreateNewsCommand cmd) {
		CreateNewsResponse createNewsResponse = newsService.createNews(cmd);

		RestResponse response = new RestResponse(createNewsResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /news/publishNews</b>
	 * <p>
	 * 发布一条新闻
	 * </p>
	 */
	@RequestMapping("publishNews")
	@RestReturn(String.class)
	public RestResponse publishNews(publishNewsCommand cmd) {
		newsService.publishNews(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /news/publi</b>
	 * <p>
	 * 修改一条新闻
	 * </p>
	 */
	@RequestMapping("updateNews")
	@RestReturn(String.class)
	public RestResponse updateNews(UpdateNewsCommand cmd) {
		newsService.updateNews(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

//	/**
//	 * <b>URL: /news/importNews</b>
//	 * <p>
//	 * 批量导入新闻
//	 * </p>
//	 */
//	@RequestMapping("importNews")
//	@RestReturn(String.class)
//	public RestResponse importNews(ImportNewsCommand cmd, @RequestParam("attachment") MultipartFile[] files) {
//		newsService.importNews(cmd,files);
//
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}

	/**
	 * <b>URL: /news/listNews</b>
	 * <p>
	 * 后台查询新闻列表
	 * </p>
	 */
	@RequestMapping("listNews")
	@RestReturn(ListNewsResponse.class)
	public RestResponse listNews(ListNewsCommand cmd) {
//		cmd.setCheckPrivilegeFlag(TrueOrFalseFlag.TRUE.getCode());
		ListNewsResponse listNewsResponse = newsService.listNews(cmd);

		RestResponse response = new RestResponse(listNewsResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /news/listNewsForWeb</b>
	 * <p>
	 * 给web的APP端查询新闻列表
	 * </p>
	 */
	@RequestMapping("listNewsForWeb")
	@RestReturn(ListNewsResponse.class)
	@RequireAuthentication(false)
	public RestResponse listNewsForWeb(ListNewsCommand cmd) {
		ListNewsResponse listNewsResponse = newsService.listNewsForWeb(cmd);

		RestResponse response = new RestResponse(listNewsResponse);
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
	@RestReturn(GetNewsDetailInfoResponse.class)
	@RequireAuthentication(false)
	public RestResponse getNewsDetailInfo(GetNewsDetailInfoCommand cmd) {
		GetNewsDetailInfoResponse getNewsDetailInfoResponse = newsService.getNewsDetailInfo(cmd);

		RestResponse response = new RestResponse(getNewsDetailInfoResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /news/getNewsDetail</b>
	 * <p>
	 * 查询新闻详情（编辑用）
	 * </p>
	 */
	@RequestMapping("getNewsDetail")
	@RestReturn(GetNewsDetailResponse.class)
	@RequireAuthentication(false)
	public RestResponse getNewsDetail(GetNewsDetailInfoCommand cmd) {
		GetNewsDetailResponse getNewsDetailResponse = newsService.getNewsDetail(cmd);

		RestResponse response = new RestResponse(getNewsDetailResponse);
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
	@RestReturn(GetNewsContentResponse.class)
	@RequireAuthentication(false)
	public RestResponse getNewsContent(GetNewsContentCommand cmd) {
		GetNewsContentResponse newsContentDTO = newsService.getNewsContent(cmd);

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
	 * <b>URL: /news/setNewsLikeFlagForWeb</b>
	 * <p>
	 * 点赞/取消点赞
	 * </p>
	 */
	@RequestMapping("setNewsLikeFlagForWeb")
	@RestReturn(String.class)
	public RestResponse setNewsLikeFlagForWeb(SetNewsLikeFlagForWebCommand cmd) {
		newsService.setNewsLikeFlagForWeb(cmd);

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
	@RestReturn(AddNewsCommentResponse.class)
	public RestResponse addNewsComment(AddNewsCommentCommand cmd) {
		AddNewsCommentResponse newsCommentDTO = newsService.addNewsComment(cmd);

		RestResponse response = new RestResponse(newsCommentDTO);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /news/addNewsCommentForWeb</b>
	 * <p>
	 * 添加一条评论 给WEB做的功能用
	 * </p>
	 */
	@RequestMapping("addNewsCommentForWeb")
	@RestReturn(AddNewsCommentResponse.class)
	public RestResponse addNewsCommentForWeb(AddNewsCommentForWebCommand cmd) {
		AddNewsCommentResponse newsCommentDTO = newsService.addNewsForWebComment(cmd);

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
	@RequireAuthentication(false)
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

	/**
	 * <b>URL: /news/updateNewsTag</b>
	 * <p>
	 * 新建/修改标签
	 * </p>
	 */
	@RequestMapping("updateNewsTag")
	@RestReturn(String.class)
	public RestResponse updateNewsTag(UpdateNewsTagCommand cmd){
		newsService.updateNewsTag(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /news/getNewsTag</b>
	 * <p>
	 * 查询标签
	 * </p>
	 */
	@RequestMapping("getNewsTag")
	@RestReturn(GetNewsTagResponse.class)
	@RequireAuthentication(false)
	public RestResponse getNewsTag(GetNewsTagCommand cmd){
		GetNewsTagResponse getNewsTagResponse = newsService.getNewsTag(cmd);

		RestResponse response = new RestResponse(getNewsTagResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /news/syncNews</b>
	 * <p>
	 * 同步新闻
	 * </p>
	 */
	@RequestMapping("syncNews")
	@RestReturn(String.class)
	public RestResponse syncNews(SyncNewsCommand cmd){
		newsService.syncNews(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /news/getCategoryIdByEntryId</b>
	 * <p>
	 * 根据入口id获取新闻分类id
	 * </p>
	 */
	@RequestMapping("getCategoryIdByEntryId")
	@RestReturn(GetCategoryIdByEntryIdResponse.class)	
	@RequireAuthentication(false)
	public RestResponse getCategoryIdByEntryId(GetCategoryIdByEntryIdCommand cmd){
		RestResponse response = new RestResponse(newsService.getCategoryIdByEntryId(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /news/getNewsQR</b>
	 * <p>
	 * 返回预览二维码
	 * </p>
	 */
	@RequestMapping("getNewsQR")
	@RestReturn(String.class)
	public RestResponse getNewsQR(UpdateNewsCommand cmd) {
		String QRCode = newsService.getNewsQR(cmd);
		RestResponse response = new RestResponse(QRCode);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /news/getNewsPreview</b>
	 * <p>
	 * 返回预览内容
	 * </p>
	 */
	@RequestMapping("getNewsPreview")
	@RestReturn(GetNewsDetailInfoResponse.class)
	@RequireAuthentication(false)
	public RestResponse getNewsPreview(GetNewsContentCommand cmd) {
		RestResponse response = new RestResponse(newsService.getNewsPreview(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}



	/**
	 * <b>URL: /news/enableSelfDefinedConfig</b>
	 * <p>
	 * 开启自定义配置
	 * </p>
	 */
	@RequestMapping("enableSelfDefinedConfig")
	@RestReturn(String.class)
	public RestResponse enableSelfDefinedConfig(GetSelfDefinedStateCommand cmd) {
		newsService.enableSelfDefinedConfig(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /news/disableSelfDefinedConfig</b>
	 * <p>
	 * 关闭自定义配置
	 * </p>
	 */
	@RequestMapping("disableSelfDefinedConfig")
	@RestReturn(String.class)
	public RestResponse disableSelfDefinedConfig(GetSelfDefinedStateCommand cmd) {
		newsService.disableSelfDefinedConfig(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /news/getSelfDefinedState</b>
	 * <p>
	 * 获取自定义配置状态
	 * </p>
	 */
	@RequestMapping("getSelfDefinedState")
	@RestReturn(GetSelfDefinedStateResponse.class)
	public RestResponse getSelfDefinedState(GetSelfDefinedStateCommand cmd) {
		RestResponse response = new RestResponse(newsService.getSelfDefinedState(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
//@Aspect
//@Component
// class MyPoxy {
//		
//	    @Before("execution(public * com.everhomes.news.NewsController.*(..))")
//	    public void beforMethod(JoinPoint point){
//	        String methodName = point.getSignature().getName();
//	        List<Object> args = Arrays.asList(point.getArgs());
//	        System.out.println("调用前连接点方法为：" + methodName + ",参数为：" + args);
//	    }
//}
