package com.everhomes.search;

import java.util.List;

import com.everhomes.hotTag.HotTags;
import com.everhomes.rest.hotTag.SearchTagCommand;
import com.everhomes.rest.hotTag.SearchTagResponse;

public interface HotTagSearcher {
    void feedDoc(HotTags tag);
    SearchTagResponse query(SearchTagCommand cmd);
}
