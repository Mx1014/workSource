// @formatter:off
package com.everhomes.news;

import com.everhomes.rest.news.*;
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

	void importNews(ImportNewsCommand cmd, MultipartFile[] files);

	ListNewsResponse listNews(ListNewsCommand cmd);

	SearchNewsResponse searchNews(SearchNewsCommand cmd);

	GetNewsDetailInfoResponse getNewsDetailInfo(GetNewsDetailInfoCommand cmd);

	void setNewsTopFlag(SetNewsTopFlagCommand cmd);

	GetNewsContentResponse getNewsContent(GetNewsContentCommand cmd);

	void deleteNews(DeleteNewsCommand cmd);

	void setNewsLikeFlag(SetNewsLikeFlagCommand cmd);

	AddNewsCommentResponse addNewsComment(AddNewsCommentCommand cmd);

	ListNewsCommentResponse listNewsComment(ListNewsCommentCommand cmd);

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
	
}
