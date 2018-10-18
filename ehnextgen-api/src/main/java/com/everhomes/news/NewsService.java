// @formatter:off
package com.everhomes.news;

import com.everhomes.rest.news.*;
import com.everhomes.rest.news.open.CreateOpenNewsCommand;
import com.everhomes.rest.news.open.ListOpenNewsCommand;
import com.everhomes.rest.news.open.ListOpenNewsResponse;
import com.everhomes.rest.news.open.UpdateOpenNewsCommand;
import com.everhomes.rest.portal.ServiceModuleAppDTO;

import org.springframework.web.multipart.MultipartFile;

import com.everhomes.rest.ui.news.AddNewsCommentBySceneCommand;
import com.everhomes.rest.ui.news.AddNewsCommentBySceneResponse;
import com.everhomes.rest.ui.news.DeleteNewsCommentBySceneCommand;
import com.everhomes.rest.ui.news.ListNewsBySceneCommand;
import com.everhomes.rest.ui.news.ListNewsBySceneResponse;
import com.everhomes.rest.ui.news.SetNewsLikeFlagBySceneCommand;
import com.everhomes.rest.ui.user.SearchContentsBySceneCommand;
import com.everhomes.rest.ui.user.SearchContentsBySceneReponse;

import java.util.List;

public interface NewsService {

	CreateNewsResponse createNews(CreateNewsCommand cmd);

	void updateNews(UpdateNewsCommand cmd);

	void importNews(ImportNewsCommand cmd, MultipartFile[] files);

	ListNewsResponse listNews(ListNewsCommand cmd);

	GetNewsDetailInfoResponse getNewsDetailInfo(GetNewsDetailInfoCommand cmd);

	GetNewsDetailResponse getNewsDetail(GetNewsDetailInfoCommand cmd);

	void setNewsTopFlag(SetNewsTopFlagCommand cmd);

	GetNewsContentResponse getNewsContent(GetNewsContentCommand cmd);

	void deleteNews(DeleteNewsCommand cmd);

	void setNewsLikeFlag(SetNewsLikeFlagCommand cmd);

	AddNewsCommentResponse addNewsComment(AddNewsCommentCommand cmd);

	ListNewsCommentResponse listNewsComment(ListNewsCommentCommand cmd);
	
	GetNewsCommentResponse getNewsComment(GetNewsCommentCommand cmd);

	void deleteNewsComment(DeleteNewsCommentCommand cmd);

	void updateNewsTag(UpdateNewsTagCommand cmd);

	GetNewsTagResponse getNewsTag(GetNewsTagCommand cmd);

	ListNewsBySceneResponse listNewsByScene(ListNewsBySceneCommand cmd);

	void setNewsLikeFlagByScene(SetNewsLikeFlagBySceneCommand cmd);

	AddNewsCommentBySceneResponse addNewsCommentByScene(AddNewsCommentBySceneCommand cmd);

	void deleteNewsCommentByScene(DeleteNewsCommentBySceneCommand cmd);

	void syncNews(SyncNewsCommand cmd);

	ListNewsResponse listNewsForWeb(ListNewsCommand cmd);
	
	SearchContentsBySceneReponse searchNewsByScene(SearchContentsBySceneCommand cmd);

	AddNewsCommentResponse addNewsForWebComment(AddNewsCommentForWebCommand cmd);

	void setNewsLikeFlagForWeb(SetNewsLikeFlagForWebCommand cmd);

	GetCategoryIdByEntryIdResponse getCategoryIdByEntryId(GetCategoryIdByEntryIdCommand cmd);

	void publishNews(publishNewsCommand cmd);
	
	News findNewsById(Long userId, Long newsId);
	
	void syncNewsWhenDelete(Long id);
	
	Long checkNewsToken(Long userId, String newsToken);

	String getNewsQR(UpdateNewsCommand cmd);

	void enableSelfDefinedConfig(GetSelfDefinedStateCommand cmd);

	void disableSelfDefinedConfig(GetSelfDefinedStateCommand cmd);

	GetSelfDefinedStateResponse getSelfDefinedState(GetSelfDefinedStateCommand cmd);

	GetNewsDetailInfoResponse getNewsPreview(GetNewsContentCommand cmd);

	void deleteNews(Long userId, News news);

	News createNewsByOpenApi(Integer namespaceId, CreateOpenNewsCommand cmd);

	void updateNewsByOpenApi(News originNews, UpdateOpenNewsCommand cmd);

	ListOpenNewsResponse listNewsOpenApi(ListOpenNewsCommand cmd, Integer namespaceId);
}



