// @formatter:off
package com.everhomes.news;

import org.springframework.web.multipart.MultipartFile;

import com.everhomes.rest.news.AddNewsCommentCommand;
import com.everhomes.rest.news.AddNewsCommentForWebCommand;
import com.everhomes.rest.news.AddNewsCommentResponse;
import com.everhomes.rest.news.CreateNewsCommand;
import com.everhomes.rest.news.CreateNewsResponse;
import com.everhomes.rest.news.DeleteNewsCommand;
import com.everhomes.rest.news.DeleteNewsCommentCommand;
import com.everhomes.rest.news.GetNewsContentCommand;
import com.everhomes.rest.news.GetNewsContentResponse;
import com.everhomes.rest.news.GetNewsDetailInfoCommand;
import com.everhomes.rest.news.GetNewsDetailInfoResponse;
import com.everhomes.rest.news.ImportNewsCommand;
import com.everhomes.rest.news.ListNewsCommand;
import com.everhomes.rest.news.ListNewsCommentCommand;
import com.everhomes.rest.news.ListNewsCommentResponse;
import com.everhomes.rest.news.ListNewsResponse;
import com.everhomes.rest.news.SearchNewsCommand;
import com.everhomes.rest.news.SearchNewsResponse;
import com.everhomes.rest.news.SetNewsLikeFlagCommand;
import com.everhomes.rest.news.SetNewsLikeFlagForWebCommand;
import com.everhomes.rest.news.SetNewsTopFlagCommand;
import com.everhomes.rest.news.SyncNewsCommand;
import com.everhomes.rest.ui.news.AddNewsCommentBySceneCommand;
import com.everhomes.rest.ui.news.AddNewsCommentBySceneResponse;
import com.everhomes.rest.ui.news.DeleteNewsCommentBySceneCommand;
import com.everhomes.rest.ui.news.ListNewsBySceneCommand;
import com.everhomes.rest.ui.news.ListNewsBySceneResponse;
import com.everhomes.rest.ui.news.SetNewsLikeFlagBySceneCommand;
import com.everhomes.rest.ui.user.SearchContentsBySceneCommand;
import com.everhomes.rest.ui.user.SearchContentsBySceneReponse;

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
