package com.everhomes.news.open;
import java.util.List;

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

public interface NewsOpenService {

	static final Long NEWS_MODULE_ID = 10800L;

	CreateNewsResponse createNews(CreateOpenNewsCommand cmd);

	void updateNews(UpdateOpenNewsCommand cmd);

	void deleteNews(DeleteNewsCommand cmd);

	ListOpenNewsResponse listNews(ListOpenNewsCommand cmd);

	GetOpenNewsDetailResponse getNewsDetail(GetOpenNewsDetailCommand cmd);

	List<IdNameDTO> listProjects();

	List<IdNameDTO> listApplications();

	List<TagDTO> listNewsTags(ListNewsTagsCommand cmd);
}
