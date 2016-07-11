package com.everhomes.search;

import java.util.Date;
import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;

public interface QueryMaker {
    int getPageSize();
    int getPageNumber();
    QueryMaker setPageInfo(int pageNum, int pageSize);
    QueryMaker addQueryTerm(String term);
    String getQueryString();
    QueryMaker setQueryString(String queryString);
    QueryMaker includeFilter(String term, List objs);
    QueryMaker excludeFilter(String term, List objs);
    QueryMaker dateFrom(Date date);
    QueryMaker dateTo(Date date);
    void makeQueryBuilder(SearchRequestBuilder builder);
}
