package com.everhomes.warehouse;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.warehouse.SearchWarehouseMaterialsCommand;
import com.everhomes.rest.warehouse.SearchWarehouseMaterialsResponse;
import com.everhomes.rest.warehouse.WarehouseMaterialDTO;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SearchUtils;
import com.everhomes.search.WarehouseMaterialSearcher;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
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
public class WarehouseMaterialSearcherImpl extends AbstractElasticSearch implements WarehouseMaterialSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseMaterialSearcherImpl.class);

    @Autowired
    private WarehouseProvider warehouseProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<WarehouseMaterials> materials) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (WarehouseMaterials material : materials) {

            XContentBuilder source = createDoc(material);
            if(null != source) {
                LOGGER.info("material id:" + material.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(material.getId().toString()).source(source));
            }

        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public void feedDoc(WarehouseMaterials material) {
        XContentBuilder source = createDoc(material);

        feedDoc(material.getId().toString(), source);
    }

    @Override
    public void syncFromDb() {
        int pageSize = 200;
        this.deleteAll();

        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<WarehouseMaterials> materials = warehouseProvider.listWarehouseMaterials(locator, pageSize);

            if(materials.size() > 0) {
                this.bulkUpdate(materials);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();

        LOGGER.info("sync for warehouse materials ok");
    }

    @Override
    public SearchWarehouseMaterialsResponse query(SearchWarehouseMaterialsCommand cmd) {
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

        FilterBuilder fb = FilterBuilders.termFilter("namespaceId", UserContext.getCurrentNamespaceId());
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerId", cmd.getOwnerId()));
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerType", cmd.getOwnerType()));

        if(cmd.getCategoryId() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("categoryId", cmd.getCategoryId()));
        }
        if(cmd.getMaterialNumber() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("materialNumber", cmd.getMaterialNumber()));
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
            LOGGER.debug("SearchWarehouseMaterials query : {}", builder);
        }
        SearchResponse rsp = builder.execute().actionGet();

        List<Long> ids = getIds(rsp);

        SearchWarehouseMaterialsResponse response = new SearchWarehouseMaterialsResponse();
        if(ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        } else {
            response.setNextPageAnchor(null);
        }

        List<WarehouseMaterialDTO> materialDTOs = new ArrayList<WarehouseMaterialDTO>();
        for(Long id : ids) {
            WarehouseMaterials material = warehouseProvider.findWarehouseMaterials(id, cmd.getOwnerType(), cmd.getOwnerId());
            WarehouseMaterialDTO dto = ConvertHelper.convert(material, WarehouseMaterialDTO.class);

            WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategories(dto.getCategoryId(), dto.getOwnerType(), dto.getOwnerId());
            if(category != null) {
                dto.setCategoryName(category.getName());
            }

            WarehouseUnits unit = warehouseProvider.findWarehouseUnits(dto.getUnitId(), dto.getOwnerType(), dto.getOwnerId());
            if(unit != null) {
                dto.setUnitName(unit.getName());
            }

            materialDTOs.add(dto);
        }
        response.setMaterialDTOs(materialDTOs);

        return response;
    }

    @Override
    public String getIndexType() {
        return SearchUtils.WAREHOUSE_MATERIAL;
    }

    private XContentBuilder createDoc(WarehouseMaterials material){
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("namespaceId", material.getNamespaceId());
            b.field("ownerId", material.getOwnerId());
            b.field("ownerType", material.getOwnerType());
            b.field("name", material.getName());
            b.field("materialNumber", material.getMaterialNumber());
            b.field("categoryId", material.getCategoryId());
            b.field("updateTime", material.getUpdateTime());

            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create warehouse material " + material.getId() + " error");
            return null;
        }
    }
}
