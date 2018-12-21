package com.everhomes.relocation;

import com.everhomes.rest.relocation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author sw on 2017/11/20.
 */
public interface RelocationService {
    SearchRelocationRequestsResponse searchRelocationRequests(SearchRelocationRequestsCommand cmd);

    RelocationRequestDTO getRelocationRequestDetail(GetRelocationRequestDetailCommand cmd);

    RelocationInfoDTO getRelocationUserInfo(GetRelocationUserInfoCommand cmd);

    RelocationRequestDTO requestRelocation(RequestRelocationCommand cmd);

    QueryRelocationStatisticsResponse queryRelocationStatistics(QueryRelocationStatisticsCommand cmd);

    void exportRelocationRequests(SearchRelocationRequestsCommand cmd, HttpServletResponse response);
}
