// @formatter:off
package com.everhomes.news;

import java.util.List;

public interface NewsProvider {

	void createNews(News news);

	void updateNews(News news);

	News findNewsById(Long id);

	List<News> listNews(Long communityId, Long categoryId, Integer namespaceId, Long from, Integer pageSize);

	Long getMaxTopIndex(Integer namespaceId);

	void createNewsList(List<News> newsList);

	List<News> findAllActiveNewsByPage(Long from, Integer pageSize);

	NewsCategory findNewsCategoryById(Long categoryId);

	void deleteNewsCommunity(Long newsId);

	void createNewsCommunity(NewsCommunity newsCommunity);

	List<Long> listNewsCommunities(Long newsId);

	Boolean getCommentForbiddenFlag(Long categoryId, Integer namespaceId);
}
