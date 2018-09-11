// @formatter:off
package com.everhomes.news;

import java.util.List;

public interface NewsProvider {

	Long createNews(News news);

	void updateNews(News news);

	News findNewsById(Long id);

	List<News> listNews(Long ownerId, List<Long> communityId, Long categoryId, Integer namespaceId, Long from, Integer pageSize, boolean isScene, Byte status);

	Long getMaxTopIndex(Integer namespaceId);

	void createNewsList(List<News> newsList);

	List<News> findAllActiveNewsByPage(Long from, Integer pageSize);

	NewsCategory findNewsCategoryById(Long categoryId);

	void deleteNewsCommunity(Long newsId);

	void createNewsCommunity(NewsCommunity newsCommunity);

	List<Long> listNewsCommunities(Long newsId);

	Boolean getCommentForbiddenFlag(Long categoryId, Integer namespaceId);

	void createNewsCategory(NewsCategory newsCategory);

	void updateNewsCategory(NewsCategory newsCategory);

	Long createNewsTag(NewsTag newsTag);

	void createNewsTagVals(NewsTagVals newsTagVals);

	void deletNewsTagVals(Long newsId);

	List<NewsTagVals> listNewsTagVals(Long newsId);

	NewsTag findNewsTagById(Long id);

	void updateNewsTag(NewsTag newsTag);

	List<NewsTag> listNewsTag(Integer namespaceId,String ownerType,Long ownerId,Byte isSearch,Long parentId,Long pageAnchor,Integer pageSize, Long categoryId);

	void increaseViewCount(Long newsId, Long nViewCount);

	NewsCategory getCategoryIdByEntryId(Integer entryId,Integer namespaceId);

	News findNewsByNamespaceAndId(Integer namespaceId, Long id);

	Long createNewPreview(News news);

	News findNewPreview(Long id);


	List<NewsTag> listParentTags(String ownerType, Long ownerId, Long categoryId);

	void deleteProjectNewsTags(Long projectId, Long categoryId);
}
