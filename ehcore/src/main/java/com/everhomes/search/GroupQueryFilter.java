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

@SuppressWarnings("rawtypes")
public class GroupQueryFilter implements QueryMaker {
    public static final String TERM_ID = "id";
    public static final String TERM_NAME = "name";
    public static final String TERM_TAG = "tags.tag.name";
    public static final String TERM_TAG_UNTOUCH = "tags.tag.untouched";
    public static final String TERM_CATEGORY_PATH = "category";
    public static final String TERM_CREATORUID = "creatorUid";
    public static final String TERM_CATEGORY_ID = "categoryId";
    public static final String TERM_NAMESPACE_ID = "namespaceId";
    
    private static final Map<String, Float> termQueryMap;
    static
    {
        termQueryMap = new HashMap<String, Float>();
        termQueryMap.put(TERM_NAME, 1.2f);
        termQueryMap.put(TERM_TAG, 1.0f);
        termQueryMap.put(TERM_TAG_UNTOUCH, 1.1f);
        //termQueryMap.put(TERM_CATEGORY_PATH, 1.0f);
    }
    
    Map<String, List> inTerms = new HashMap<String, List>();
    Map<String, List> notTerms = new HashMap<String, List>();
    List<String> queryTerms = new ArrayList<String>();
    String queryString;
    int pageSize = 0;
    int pageNumber = 0;
    
    public GroupQueryFilter() {
        this(0, 30);
    }
    
    public GroupQueryFilter(int pageNum, int pageSize) {
        setPageInfo(pageNum, pageSize);
    }
    
    @Override
    public GroupQueryFilter setPageInfo(int pageNum, int pageSize) {
        this.pageSize = pageSize;
        this.pageNumber = pageNum;
        
        return this;
    }
    
    @Override
    public GroupQueryFilter addQueryTerm(String term) {
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
    public GroupQueryFilter setQueryString(String queryString) {
        this.queryString = queryString;
        return this;
    }

    @Override
    public GroupQueryFilter includeFilter(String term, List objs) {
        inTerms.put(term, objs);
        return this;
    }
    
    @Override
    public GroupQueryFilter excludeFilter(String term, List objs) {
        notTerms.put(term, objs);
        return this;
    }
    
    private QueryBuilder getQueryBuilder() {
        QueryBuilder qb = null;
        
        if((queryString == null) || (queryString.isEmpty()) || (queryTerms.size() == 0) ) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            
            PrefixQueryBuilder prefix_b = null;
            if(queryTerms.contains(TERM_CATEGORY_PATH)) {
                prefix_b = QueryBuilders.prefixQuery(TERM_CATEGORY_PATH, queryString);
                }
            queryTerms.remove(TERM_CATEGORY_PATH);
            
            MultiMatchQueryBuilder mqb = QueryBuilders.multiMatchQuery(queryString);
            for(String term : queryTerms) {
                Float f = termQueryMap.get(term);
                if(null != f) {
                    mqb = mqb.field(term, f.floatValue());
                    }
                }
          
            if(prefix_b != null) {
                if(queryTerms.size() == 0) {
                    qb = prefix_b;
                } else {
                    qb = QueryBuilders.boolQuery().should(mqb).should(prefix_b);     
                    }
            } else {
                qb = mqb;   
                }
            }
         
        return qb;
    }
    
    private FilterBuilder boolInFilter(String term, List values) {
        List<FilterBuilder> ors = new ArrayList<FilterBuilder>();
        for(Object l : values) {
            ors.add(FilterBuilders.termFilter(term, l));
        }
        if(ors.size() == 1) {
            return ors.get(0);
        }
        
        return FilterBuilders.boolFilter().should(ors.toArray(new FilterBuilder[ors.size()]));
    }
    
    private FilterBuilder boolNotFilter(String term, List values) {
        List<FilterBuilder> ors = new ArrayList<FilterBuilder>();
        for(Object l : values) {
            ors.add(FilterBuilders.termFilter(term, l));
            }
        if(ors.size() == 1) {
            return ors.get(0);
        }
        
        return FilterBuilders.notFilter(FilterBuilders.boolFilter().should(ors.toArray(new FilterBuilder[ors.size()])));
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
        
        for(Entry<String, List> entry: inTerms.entrySet()) {
            String term = entry.getKey();
            List v = entry.getValue();
            if(v.size() > 0) {
                allBuilders.add(boolInFilter(term,v));
                }
           }
        
        for(Entry<String, List> entry: notTerms.entrySet()){
            String term = entry.getKey();
            List v = entry.getValue();
            if(v.size() > 0) {
                allBuilders.add(boolNotFilter(term,v));
                }
            }
        
        if(allBuilders.size() == 0) {
            return null;
        } else  if(allBuilders.size() == 1) {
            return allBuilders.get(0);
        } else {
            return FilterBuilders.boolFilter().must(allBuilders.toArray(new FilterBuilder[allBuilders.size()])); 
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
        
        if(!queryString.isEmpty()) {
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("name");//<font color="#1fa24d"></font>
        }
        
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

    @Override
    public QueryMaker dateFrom(Date date) {
        return this;
    }

    @Override
    public QueryMaker dateTo(Date date) {
        return this;
    }
    
}
