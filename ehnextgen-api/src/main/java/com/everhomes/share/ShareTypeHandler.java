package com.everhomes.share;

import com.everhomes.rest.share.ShareType;

/**
 * Created by xq.tian on 2017/12/18.
 */
public interface ShareTypeHandler {

    ShareType init();

    void execute(String shareData);
}
