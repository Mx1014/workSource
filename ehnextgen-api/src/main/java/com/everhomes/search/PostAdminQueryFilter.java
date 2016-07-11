package com.everhomes.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

@SuppressWarnings("rawtypes")
public class PostAdminQueryFilter implements QueryMaker {
    public static final String TERM_ID = "id";
    public static final String TERM_SUBJECT = "subject";
    public static final String TERM_CONTENT = "content";
    public static final String TERM_CREATORNICKNAME = "creatorNickName";
    public static final String TERM_CONTENTCATEGORY = "contentcategory";
    public static final String TERM_ACTIONCATEGORY = "actioncategory";
    public static final String TERM_IDENTIFY = "identify";
    public static final String TERM_APPID = "appId";
    public static final String TERM_FORUMID = "forumId";
    public static final String TERM_CATEGORYID = "categoryId";
    public static final String TERM_CREATORUID = "creatorUid";
    public static final String TERM_VISIBILITYSCOPEID = "visibilityScopeId";
    public static final String TERM_VISIBILITYSCOPE = "visibilityScope";
    public static final String TERM_VISIBLEREGIONTYPE = "visibleRegionType";
    public static final String TERM_VISIBLEREGIONID = "visibleRegionId";
    public static final String TERM_EMBEDDEDID = "embeddedId";
    public static final String TERM_SENDERNAME = "senderName";
    public static final String TERM_SENDERAVATAR = "senderAvatar";
    public static final String TERM_FORUMNAME = "forumName";
    public static final String TERM_DISPLAYNAME = "displayName";
    public static final String TERM_CREATETIME = "createTime";
    public static final String TERM_PARENTPOSTID = "parentPostId";
    
    
    private static final Map<String, Float> termQueryMap;
    static
    {
        termQueryMap = new HashMap<String, Float>();
        termQueryMap.put(TERM_SUBJECT, 1.2f);
        termQueryMap.put(TERM_CONTENT, 1.0f);
        termQueryMap.put(TERM_CREATORNICKNAME, 1.1f);
    }
    
    Date fromDate = null;
    Date toDate = null;
    Map<String, List> inTerms = new HashMap<String, List>();
    Map<String, List> notTerms = new HashMap<String, List>();
    List<String> queryTerms = new ArrayList<String>();
    String queryString;
    int pageSize = 0;
    int pageNumber = 0;
    
    public PostAdminQueryFilter() {
        this(0, 30);
    }
    
    public PostAdminQueryFilter(int pageNum, int pageSize) {
        setPageInfo(pageNum, pageSize);
    }
    
    @Override
    public PostAdminQueryFilter setPageInfo(int pageNum, int pageSize) {
        this.pageSize = pageSize;
        this.pageNumber = pageNum;
        
        return this;
    }
    
    @Override
    public PostAdminQueryFilter addQueryTerm(String term) {
        if(!queryTerms.contains(term)) {
            queryTerms.add(term);
        }
        
        return this;
    }
    
    @Override
    public String getQueryString() {
        return queryString;
    }

    @Override
    public PostAdminQueryFilter setQueryString(String queryString) {
        this.queryString = queryString;
        return this;
    }
    
    public PostAdminQueryFilter dateFrom(Date date) {
        this.fromDate = date;
        
        return this;
    }
    
    public PostAdminQueryFilter dateTo(Date date) {
        this.toDate = date;
        
        return this;
    }

    @Override
    public PostAdminQueryFilter includeFilter(String term, List objs) {
        inTerms.put(term, objs);
        return this;
    }
    
    @Override
    public PostAdminQueryFilter excludeFilter(String term, List objs) {
        notTerms.put(term, objs);
        return this;
    }
    
    private QueryBuilder getQueryBuilder() {
        QueryBuilder qb = null;
        
        if((queryString) == null || (queryString.isEmpty()) || (queryTerms.size() == 0) ) {
            qb = QueryBuilders.matchAllQuery();
        } else if(queryTerms.size() == 1) {
            qb = QueryBuilders.queryString(queryString).field(queryTerms.get(0));
        } else {
            MultiMatchQueryBuilder mqb = QueryBuilders.multiMatchQuery(queryString);
            for(String term : queryTerms) {
                Float f = termQueryMap.get(term);
                if(null != f) {
                    mqb = mqb.field(term, f.floatValue());
                    }
                }
            qb = mqb;
            }
         
        return qb;
    }
    
    private FilterBuilder getCommonFilter() {
        List<FilterBuilder> allBuilders = new ArrayList<FilterBuilder>();
        List ids = inTerms.get(TERM_ID);
        if(ids != null && ids.size() > 0) {
            FilterBuilder includeIds = FilterBuilders.idsFilter().addIds(Arrays.copyOf(ids.toArray(), ids.size(), String[].class));
            allBuilders.add(includeIds);
            }
        
        List noIds = notTerms.get(TERM_ID);
        if(noIds != null && noIds.size() > 0) {
            FilterBuilder excludeIds = FilterBuilders.notFilter(FilterBuilders.idsFilter().addIds(Arrays.copyOf(ids.toArray(), ids.size(), String[].class)));
            allBuilders.add(excludeIds);
            }
        
        inTerms.remove(TERM_ID);
        notTerms.remove(TERM_ID);
        
        List<FilterBuilder> andBuilders = new ArrayList<FilterBuilder>();
        for(Entry<String, List> entry: inTerms.entrySet()) {
            String term = entry.getKey();
            List v = entry.getValue();
            for(Object ov : v) {
                if(!ov.toString().equals("")) {
                    andBuilders.add(FilterBuilders.termFilter(term, ov));    
                    }
                
                }
           }
        if(andBuilders.size() > 0) {
            allBuilders.add(FilterBuilders.andFilter(andBuilders.toArray(new FilterBuilder[andBuilders.size()])));
            }
        
        RangeFilterBuilder dateFilter = null;
        if((null == fromDate) && (null != toDate)) {
            fromDate = new Date(0);
            }
  
        if(null != fromDate) {
            dateFilter = FilterBuilders.rangeFilter(TERM_CREATETIME).from(fromDate);
            }
        
        if(null != toDate) {
            if(null == dateFilter) {
                dateFilter = FilterBuilders.rangeFilter(TERM_CREATETIME).from(fromDate);
            } else {
                dateFilter.to(toDate);
                }
          }
        if(null != dateFilter) {
            allBuilders.add(dateFilter);
            }
        
        List<FilterBuilder> notBuilders = new ArrayList<FilterBuilder>();
        for(Entry<String, List> entry: notTerms.entrySet()){
            String term = entry.getKey();
            List v = entry.getValue();
            for(Object ov : v) {
                notBuilders.add(FilterBuilders.termFilter(term, ov));
                }
            }
        
        if(notBuilders.size() > 0) {
            allBuilders.add(FilterBuilders.notFilter(FilterBuilders.orFilter(notBuilders.toArray(new FilterBuilder[notBuilders.size()]))));
            }
        
        if(allBuilders.size() == 0) {
            return null;
        } else  if(allBuilders.size() == 1) {
            return allBuilders.get(0);
        } else {
            return FilterBuilders.andFilter(allBuilders.toArray(new FilterBuilder[allBuilders.size()])); 
            }
    }
    
    @Override
    public void makeQueryBuilder(SearchRequestBuilder builder) {
        QueryBuilder qb = getQueryBuilder();
        
        FilterBuilder commonFilter = getCommonFilter();
        if(commonFilter != null) {
            qb = QueryBuilders.filteredQuery(qb, commonFilter);    
            }
        
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        
        if(pageSize > 0) {
            builder.setFrom(pageNumber * pageSize).setSize(pageSize + 1);
            }
        builder.addSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));
        
//        if(!queryString.isEmpty()) {
//            builder.setHighlighterFragmentSize(60);
//            builder.setHighlighterNumOfFragments(8);
//            builder.addHighlightedField("name");
//        }
        
        builder.setQuery(qb);
    }

    @Override
    public int getPageSize() {
        return this.pageSize;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }
    
}
