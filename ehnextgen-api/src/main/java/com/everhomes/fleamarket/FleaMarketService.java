// @formatter:off
package com.everhomes.fleamarket;

import com.everhomes.forum.Post;
import com.everhomes.rest.fleamarket.FleaMarketPostCommand;
import com.everhomes.rest.fleamarket.FleaMarketUpdateCommand;

public interface FleaMarketService {
    Post postItemToForum(FleaMarketPostCommand cmd);
    void updateItem(FleaMarketUpdateCommand cmd);
}
