package com.everhomes.warehouse;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.acl.PrivilegeServiceErrorCode;
import com.everhomes.rest.contract.ContractErrorCode;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.warehouse.SearchWarehouseStocksCommand;
import com.everhomes.rest.warehouse.SearchWarehouseStocksResponse;
import com.everhomes.rest.warehouse.Status;
import com.everhomes.rest.warehouse.WarehouseStockDTO;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SearchUtils;
import com.everhomes.search.WarehouseStockSearcher;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
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
public class WarehouseStockSearcherImpl extends AbstractElasticSearch implements WarehouseStockSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseStockSearcherImpl.class);

    @Autowired
    private WarehouseProvider warehouseProvider;

    @Autowired
    private ConfigurationProvider configProvider;
    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private PortalService portalService;

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<WarehouseStocks> stocks) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (WarehouseStocks stock : stocks) {

            XContentBuilder source = createDoc(stock);
            if(null != source) {
                LOGGER.info("warehouse stock id:" + stock.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(stock.getId().toString()).source(source));
            }

        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public void feedDoc(WarehouseStocks stock) {
        XContentBuilder source = createDoc(stock);

        feedDoc(stock.getId().toString(), source);
    }

    @Override
    public void syncFromDb() {
        int pageSize = 200;
        this.deleteAll();

        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<WarehouseStocks> stocks = warehouseProvider.listWarehouseStocks(locator, pageSize);

            if(stocks.size() > 0) {
                this.bulkUpdate(stocks);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();

        LOGGER.info("sync for warehouse stocks ok");
    }

    @Override
    public SearchWarehouseStocksResponse query(SearchWarehouseStocksCommand cmd) {
    	if (cmd.getOwnerId() == null || cmd.getCommunityId() == null) {
    		throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_ORGIDORCOMMUNITYID_IS_EMPTY,
                    "OrgIdorCommunityId user privilege error");
		}
        //checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(), PrivilegeConstants.WAREHOUSE_REPO_MAINTAIN_SEARCH,cmd.getOwnerId());
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
        if(cmd.getMaterialName() == null || cmd.getMaterialName().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getMaterialName())
                    .field("name", 5.0f)
                    .field("name.pinyin_prefix", 2.0f)
                    .field("name.pinyin_gram", 1.0f);
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("name");

        }

        FilterBuilder fb = FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId());
//        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerId", cmd.getOwnerId()));
        //fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerType", cmd.getOwnerType()));
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("communityId", cmd.getCommunityId()));
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("status", Status.ACTIVE.getCode()));

        if(cmd.getWarehouseId() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("warehouseId", cmd.getWarehouseId()));
        }

        if(cmd.getWarehouseStatus() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("warehouseStatus", cmd.getWarehouseStatus()));
        }

        if(cmd.getMaterialNumber() != null && cmd.getMaterialNumber().length()>0) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("materialNumber", cmd.getMaterialNumber()));
        }

        if(cmd.getCategoryId() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("categoryId", cmd.getCategoryId()));
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

        SearchResponse rsp = builder.execute().actionGet();

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("query warehouse stocks :{}", rsp);
        }

        List<Long> ids = getIds(rsp);

        SearchWarehouseStocksResponse response = new SearchWarehouseStocksResponse();
        if(ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        } else {
            response.setNextPageAnchor(null);
        }

        List<WarehouseStockDTO> stockDTOs = new ArrayList<WarehouseStockDTO>();
        for(Long id : ids) {
            WarehouseStocks stock = warehouseProvider.findWarehouseStocks(id, cmd.getOwnerType(), cmd.getOwnerId());
            if(stock == null){
                continue;
            }
            WarehouseStockDTO dto = ConvertHelper.convert(stock, WarehouseStockDTO.class);
            Warehouses warehouse = warehouseProvider.findWarehouse(dto.getWarehouseId(), cmd.getOwnerType(), cmd.getOwnerId(),cmd.getCommunityId());
            if(warehouse != null) {
                dto.setWarehouseName(warehouse.getName());
            }

            WarehouseMaterials material = warehouseProvider.findWarehouseMaterials(dto.getMaterialId(), cmd.getOwnerType(), cmd.getOwnerId(),cmd.getCommunityId());
            if(material != null) {
                dto.setMaterialName(material.getName());
                dto.setMaterialNumber(material.getMaterialNumber());
                dto.setCategoryId(material.getCategoryId());
                dto.setUnitId(material.getUnitId());
                dto.setSupplierName(material.getSupplierName());

                WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategories(material.getCategoryId(), cmd.getOwnerType(), cmd.getOwnerId());
                if(category != null) {
                    dto.setCategoryName(category.getName());
                }

                WarehouseUnits unit = warehouseProvider.findWarehouseUnits(material.getUnitId(), cmd.getOwnerType(), cmd.getOwnerId());
                if(unit != null) {
                    dto.setUnitName(unit.getName());
                }
            }
            stockDTOs.add(dto);
        }
        response.setStockDTOs(stockDTOs);

        return response;
    }

    private XContentBuilder createDoc(WarehouseStocks stock){
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("namespaceId", stock.getNamespaceId());
            b.field("ownerId", stock.getOwnerId());
            b.field("ownerType", stock.getOwnerType());
            b.field("warehouseId", stock.getWarehouseId());
            b.field("materialId", stock.getMaterialId());
            b.field("updateTime", stock.getUpdateTime());
            b.field("status", stock.getStatus());
            b.field("communityId", stock.getCommunityId());

            Warehouses warehouse = warehouseProvider.findWarehouse(stock.getWarehouseId(), stock.getOwnerType(), stock.getOwnerId(),stock.getCommunityId());
            if(warehouse != null) {
                b.field("warehouseStatus", warehouse.getStatus());
            } else {
                b.field("warehouseStatus", "");
            }


            WarehouseMaterials material = warehouseProvider.findWarehouseMaterials(stock.getMaterialId(), stock.getOwnerType(), stock.getOwnerId(),stock.getCommunityId());
            if(material != null) {
                b.field("name", material.getName());
                b.field("materialNumber", material.getMaterialNumber());
                b.field("categoryId", material.getCategoryId());
            } else {
                b.field("name", "");
                b.field("materialNumber", "");
                b.field("categoryId", "");
            }

            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create warehouse stock  " + stock.getId() + " error");
            return null;
        }
    }

    @Override
    public String getIndexType() {
        return SearchUtils.WAREHOUSE_STOCK;
    }

    private void checkAssetPriviledgeForPropertyOrg(Long communityId, Long priviledgeId, Long OrganizationId) {
        ListServiceModuleAppsCommand cmd1 = new ListServiceModuleAppsCommand();
        cmd1.setActionType((byte)13);
        cmd1.setModuleId(PrivilegeConstants.WAREHOUSE_MODULE_ID);
        cmd1.setNamespaceId(UserContext.getCurrentNamespaceId());
        ListServiceModuleAppsResponse res = portalService.listServiceModuleAppsWithConditon(cmd1);
        Long appId = res.getServiceModuleApps().get(0).getOriginId();
        if(!userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), EntityType.ORGANIZATIONS.getCode(), OrganizationId, OrganizationId,priviledgeId , appId, null,communityId )){
            throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_CHECK_APP_PRIVILEGE,
                    "check app privilege error");
        }
    }
}
