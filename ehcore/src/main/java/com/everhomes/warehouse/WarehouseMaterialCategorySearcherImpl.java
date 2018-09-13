package com.everhomes.warehouse;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.warehouse.SearchWarehouseMaterialCategoriesCommand;
import com.everhomes.rest.warehouse.SearchWarehouseMaterialCategoriesResponse;
import com.everhomes.rest.warehouse.WarehouseMaterialCategoryDTO;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SearchUtils;
import com.everhomes.search.WarehouseMaterialCategorySearcher;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying.xiong on 2017/5/15.
 */
@Component
public class WarehouseMaterialCategorySearcherImpl extends AbstractElasticSearch implements WarehouseMaterialCategorySearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseMaterialCategorySearcherImpl.class);

    @Autowired
    private WarehouseProvider warehouseProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<WarehouseMaterialCategories> categories) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (WarehouseMaterialCategories category : categories) {

            XContentBuilder source = createDoc(category);
            if(null != source) {
                LOGGER.info("warehouse material category id:" + category.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(category.getId().toString()).source(source));
            }

        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public void feedDoc(WarehouseMaterialCategories category) {
        XContentBuilder source = createDoc(category);

        feedDoc(category.getId().toString(), source);
    }

    @Override
    public void syncFromDb() {
        int pageSize = 200;
        this.deleteAll();

        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<WarehouseMaterialCategories> categories = warehouseProvider.listWarehouseMaterialCategories(locator, pageSize);

            if(categories.size() > 0) {
                this.bulkUpdate(categories);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();

        LOGGER.info("sync for warehouse material category ok");
    }

    @Override
    public SearchWarehouseMaterialCategoriesResponse query(SearchWarehouseMaterialCategoriesCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        QueryBuilder qb = null;
        if(cmd.getName() == null || cmd.getName().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getName())
                    .field("name", 5.0f)
                    .field("name.pinyin_prefix", 2.0f)
                    .field("name.pinyin_gram", 1.0f);
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("name");

        }

        FilterBuilder fb = FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId());
        // adapt to zuolin base ,remove searching with ownerId conditions
//        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerId", cmd.getOwnerId()));
//        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerType", cmd.getOwnerType().toLowerCase()));

        if(cmd.getParentId() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("parentId", cmd.getParentId()));
        }

        if(cmd.getCategoryNumber() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("categoryNumber", cmd.getCategoryNumber()));
        }
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }

        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);

        if(cmd.getName() == null || cmd.getName().isEmpty()) {
            builder.addSort(SortBuilders.fieldSort("updateTime").order(SortOrder.DESC).ignoreUnmapped(true));
        }

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("SearchWarehouseMaterialCategories: {}", builder);
        }


        SearchResponse rsp = builder.execute().actionGet();

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("SearchWarehouseMaterialCategories response: {}", rsp);
        }

        List<Long> ids = getIds(rsp);

        SearchWarehouseMaterialCategoriesResponse response = new SearchWarehouseMaterialCategoriesResponse();
        if(ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        } else {
            response.setNextPageAnchor(null);
        }

        List<WarehouseMaterialCategoryDTO> categoryDTOs = new ArrayList<WarehouseMaterialCategoryDTO>();
        for(Long id : ids) {
            WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategories(id, cmd.getOwnerType(), cmd.getOwnerId());
            WarehouseMaterialCategoryDTO dto = ConvertHelper.convert(category, WarehouseMaterialCategoryDTO.class);
            if(dto.getParentId() != null) {
                WarehouseMaterialCategories parent = warehouseProvider.findWarehouseMaterialCategories(dto.getParentId(), dto.getOwnerType(), dto.getOwnerId());
                if(parent != null) {
                    dto.setParentCategoryNumber(parent.getCategoryNumber());
                    dto.setParentCategoryName(parent.getName());
                }
            }
            categoryDTOs.add(dto);
        }
        response.setCategoryDTOs(categoryDTOs);

        return response;
    }

    @Override
    public String getIndexType() {
        return SearchUtils.WAREHOUSE_MATERIAL_CATEGORY;
    }

    private XContentBuilder createDoc(WarehouseMaterialCategories category){
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("namespaceId", category.getNamespaceId());
            b.field("ownerId", category.getOwnerId());
            b.field("ownerType", category.getOwnerType());
            b.field("parentId", category.getParentId());
            b.field("name", category.getName());
            b.field("categoryNumber", category.getCategoryNumber());
            b.field("updateTime", category.getUpdateTime());


            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create warehouse material category " + category.getId() + " error");
            return null;
        }
    }
}
