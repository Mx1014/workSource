package com.everhomes.search;

import com.everhomes.hotTag.HotTag;
import com.everhomes.rest.hotTag.SearchTagCommand;
import com.everhomes.rest.hotTag.SearchTagResponse;

public interface HotTagSearcher {
    void feedDoc(HotTag tag);
    SearchTagResponse query(SearchTagCommand cmd);
}
