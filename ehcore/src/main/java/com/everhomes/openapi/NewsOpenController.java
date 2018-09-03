package com.everhomes.openapi;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.news.open.NewsOpenService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.common.IdNameDTO;
import com.everhomes.rest.news.open.CreateOpenNewsCommand;
import com.everhomes.rest.news.open.CreateNewsResponse;
import com.everhomes.rest.news.open.DeleteNewsCommand;
import com.everhomes.rest.news.open.GetOpenNewsDetailCommand;
import com.everhomes.rest.news.open.GetOpenNewsDetailResponse;
import com.everhomes.rest.news.open.ListOpenNewsCommand;
import com.everhomes.rest.news.open.ListOpenNewsResponse;
import com.everhomes.rest.news.open.ListNewsTagsCommand;
import com.everhomes.rest.news.open.TagDTO;
import com.everhomes.rest.news.open.UpdateOpenNewsCommand;
import com.everhomes.util.ValidatorUtil;

@RestDoc(value = "News open Constroller", site = "core")
@RestController
@RequestMapping("/openapi/news")
public class NewsOpenController extends ControllerBase {

	@Autowired
	NewsOpenService newsOpenService;

	/**
	 * <b>URL: /openapi/news/createNews</b>
	 * <p>
	 * 发布快讯
	 * </p>
	 */
	@RequestMapping("createNews")
	@RestReturn(CreateNewsResponse.class)
	public RestResponse createNews(CreateOpenNewsCommand cmd) {
		ValidatorUtil.validate(cmd);
		RestResponse response = new RestResponse(newsOpenService.createNews(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /openapi/news/updateNews</b>
	 * <p>
	 * 更新快讯
	 * </p>
	 */
	@RequestMapping("updateNews")
	@RestReturn(String.class)
	public RestResponse updateNews(UpdateOpenNewsCommand cmd) {
		ValidatorUtil.validate(cmd);
		newsOpenService.updateNews(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /openapi/news/deleteNews</b>
	 * <p>
	 * 删除快讯
	 * </p>
	 */
	@RequestMapping("deleteNews")
	@RestReturn(String.class)
	public RestResponse deleteNews(DeleteNewsCommand cmd) {
		ValidatorUtil.validate(cmd);
		newsOpenService.deleteNews(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /openapi/news/listNews</b>
	 * <p>
	 * 查询快讯列表
	 * </p>
	 */
	@RequestMapping("listNews")
	@RestReturn(ListOpenNewsResponse.class)
	public RestResponse listNews(ListOpenNewsCommand cmd) {
		ValidatorUtil.validate(cmd);
		ListOpenNewsResponse resp = newsOpenService.listNews(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /openapi/news/getNewsDetail</b>
	 * <p>
	 * 查询快讯详情
	 * </p>
	 */
	@RequestMapping("getNewsDetail")
	@RestReturn(GetOpenNewsDetailResponse.class)
	public RestResponse getNewsDetail(GetOpenNewsDetailCommand cmd) {
		ValidatorUtil.validate(cmd);
		GetOpenNewsDetailResponse resp = newsOpenService.getNewsDetail(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /openapi/news/listProjects</b>
	 * <p>
	 * 查询所有项目
	 * </p>
	 */
	@RequestMapping("listProjects")
	@RestReturn(value = IdNameDTO.class, collection = true)
	public RestResponse listProjects() {
		List<IdNameDTO> resp = newsOpenService.listProjects();
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /openapi/news/listNewsTags</b>
	 * <p>
	 * 查询所有标签
	 * </p>
	 */
	@RequestMapping("listNewsTags") 
	@RestReturn(value = TagDTO.class, collection = true)
	public RestResponse listNewsTags(ListNewsTagsCommand cmd) {
		ValidatorUtil.validate(cmd);
		List<TagDTO> resp = newsOpenService.listNewsTags(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /openapi/news/listApplications</b>
	 * <p>
	 * 获取所有应用
	 * </p>
	 */
	@RequestMapping("listApplications")
	@RestReturn(value = IdNameDTO.class, collection = true)
	public RestResponse listApplications() {
		List<IdNameDTO> resp = newsOpenService.listApplications();
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
