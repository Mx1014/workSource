package com.everhomes.search;

import java.util.List;

import com.everhomes.community.Community;
import com.everhomes.rest.community.CommunityDoc;

public interface  CommunitySearcher {
    void syncDb();
    void bulkUpdate(List<Community> communities);
    void feedDoc(Community community);
    public List<CommunityDoc> searchDocs(String queryString, Byte communityType, Long cityId, Long regionId, int pageNum, int pageSize);
    void deleteById(Long id);
    List<CommunityDoc> searchEnterprise(String queryString,Byte communityType, Long regionId, int pageNum, int pageSize);
}
