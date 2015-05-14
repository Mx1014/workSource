package com.everhomes.search;

import java.util.List;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityDoc;

public interface CommunitySearcher {
    void syncDb();
    void bulkUpdate(List<Community> communities);
    void feedDoc(Community community);
    public List<CommunityDoc> searchDocs(String queryString, Long cityId, int pageNum, int pageSize);
    List<String> search(String keyword);
    List<String> search(String keyword, Long cityId);
    List<String> search(String keyword, Long cityId, int pageSize);
    void deleteById(Long id);
}
