package com.everhomes.flow;

import com.everhomes.rest.user.UserInfo;

import java.util.List;

/**
 * Created by xq.tian on 2017/11/13.
 */
public interface FlowCaseProcessorsProcessor {

    List<Long> getProcessorIdList();

    List<Long> getProcessorsIdList(FlowCaseState ctx);

    List<UserInfo> getProcessorsInfoList();

    List<UserInfo> getProcessorsInfoList(FlowCaseState ctx);
}
