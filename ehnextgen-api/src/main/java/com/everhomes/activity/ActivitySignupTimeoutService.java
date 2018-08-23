// @formatter:off
package com.everhomes.activity;

import com.everhomes.forum.Post;

public interface ActivitySignupTimeoutService {

    void pushTimeout(Post post);
    void cancelTimeoutActivity(Long postId);
}
