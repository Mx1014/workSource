package com.everhomes.warehouse;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.warehouse.QueryRequestCommand;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SearchUtils;
import com.everhomes.search.WarehouseRequestMaterialSearcher;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
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
import java.util.List;

/**
 * Created by ying.xiong on 2017/5/18.
 */
@Component
public class WarehouseRequestMaterialSearcherImpl extends AbstractElasticSearch implements WarehouseRequestMaterialSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseSearcherImpl.class);

    @Autowired
    private WarehouseProvider warehouseProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Override
    public String getIndexType() {
        return SearchUtils.WAREHOUSE_REQUEST_MATERIAL;
    }

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<WarehouseRequestMaterials> materials) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (WarehouseRequestMaterials material : materials) {

            XContentBuilder source = createDoc(material);
            if(null != source) {
                LOGGER.info("WarehouseRequestMaterials id:" + material.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(material.getId().toString()).source(source));
            }

        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public void feedDoc(WarehouseRequestMaterials material) {
        XContentBuilder source = createDoc(material);

        feedDoc(material.getId().toString(), source);
    }

    @Override
    public void syncFromDb() {
        int pageSize = 200;
        this.deleteAll();

        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<WarehouseRequestMaterials> materials = warehouseProvider.listWarehouseRequestMaterials(locator, pageSize);

            if(materials.size() > 0) {
                this.bulkUpdate(materials);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();

        LOGGER.info("sync for WarehouseRequestMaterials ok");
    }

    @Override
    public List<Long> query(QueryRequestCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        QueryBuilder qb = null;
        if(cmd.getMaterialName() == null || cmd.getMaterialName().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getMaterialName())
                    .field("materialName", 5.0f)
                    .field("materialName.pinyin_prefix", 2.0f)
                    .field("materialName.pinyin_gram", 1.0f);
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("materialName");

        }
        FilterBuilder fb = FilterBuilders.termFilter("namespaceId", UserContext.getCurrentNamespaceId());
//        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerId", cmd.getOwnerId()));
//        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerType", cmd.getOwnerType().toLowerCase()));
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("communityId", cmd.getCommunityId()));

        if(cmd.getReviewResult() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("reviewResult", cmd.getReviewResult()));
        }
        if(cmd.getDeliveryFlag() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("deliveryFlag", cmd.getDeliveryFlag()));
        }
        if(cmd.getWarehouseId() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("warehouseId", cmd.getWarehouseId()));
        }
        if(cmd.getRequestUid() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("requestUid", cmd.getRequestUid()));
        }
        if(cmd.getRequestUserName() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("requestUserName", cmd.getRequestUserName()));
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

        if(cmd.getMaterialName() == null || cmd.getMaterialName().isEmpty()) {
            builder.addSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC).ignoreUnmapped(true));
        }

        SearchResponse rsp = builder.execute().actionGet();
//        if(LOGGER.isDebugEnabled()) {
//            LOGGER.debug("query warehouse request material :{}", builder);
//            LOGGER.debug("query warehouse request material rsp :{}", rsp);
//        }

        List<Long> ids = getIds(rsp);

        return ids;
    }

    private XContentBuilder createDoc(WarehouseRequestMaterials material){
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("namespaceId", material.getNamespaceId());
            b.field("ownerId", material.getOwnerId());
            b.field("ownerType", material.getOwnerType());
            b.field("reviewResult", material.getReviewResult());
            b.field("deliveryFlag", material.getDeliveryFlag());
            b.field("warehouseId", material.getWarehouseId());
            b.field("communityId", material.getCommunityId());
            WarehouseMaterials warehouseMaterial = warehouseProvider.findWarehouseMaterials(material.getMaterialId(), material.getOwnerType(), material.getOwnerId(),material.getCommunityId());
            if(warehouseMaterial != null) {
                b.field("materialName", warehouseMaterial.getName());
            } else {
                b.field("materialName", "");
            }
            WarehouseRequests request = warehouseProvider.findWarehouseRequests(material.getRequestId(), material.getOwnerType(), material.getOwnerId(),material.getCommunityId());
            if(request != null && request.getRequestUid() != null) {
                b.field("requestUid", request.getRequestUid());
                b.field("createTime", request.getCreateTime());
                List<OrganizationMember> members = organizationProvider.listOrganizationMembers(request.getRequestUid());
                if(members != null && members.size() > 0) {
                    b.field("requestUserName", members.get(0).getContactName());
                } else {
                    b.field("requestUserName", "");
                }
            } else {
                b.field("requestUid", "");
                b.field("requestUserName", "");
                b.field("createTime", "");
            }

            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create WarehouseRequestMaterials " + material.getId() + " error");
            return null;
        }
    }
}
