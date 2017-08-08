package com.everhomes.community_approve;

import com.everhomes.server.schema.tables.pojos.EhCommunityApprove;
import com.everhomes.util.StringHelper;

/**
 * Created by Administrator on 2017/7/19.
 */
public class CommunityApprove extends EhCommunityApprove {

    private static final long serialVersionUID = -3499235795372018396L;

    @Override
    public String toString() {
            return StringHelper.toJsonString(this);
    }
}
