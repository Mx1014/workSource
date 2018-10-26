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
import com.everhomes.rest.warehouse.SearchWarehousesCommand;
import com.everhomes.rest.warehouse.SearchWarehousesResponse;
import com.everhomes.rest.warehouse.WarehouseDTO;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SearchUtils;
import com.everhomes.search.WarehouseSearcher;
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
public class WarehouseSearcherImpl extends AbstractElasticSearch implements WarehouseSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseSearcherImpl.class);

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
    public void bulkUpdate(List<Warehouses> warehouses) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (Warehouses warehouse : warehouses) {

            XContentBuilder source = createDoc(warehouse);
            if(null != source) {
                LOGGER.info("warehouse id:" + warehouse.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(warehouse.getId().toString()).source(source));
            }

        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public String getIndexType() {
        return SearchUtils.WAREHOUSE;
    }

    @Override
    public void feedDoc(Warehouses warehouse) {
        XContentBuilder source = createDoc(warehouse);

        feedDoc(warehouse.getId().toString(), source);
    }

    @Override
    public void syncFromDb() {
        int pageSize = 200;
        this.deleteAll();

        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<Warehouses> warehouses = warehouseProvider.listWarehouses(locator, pageSize);

            if(warehouses.size() > 0) {
                this.bulkUpdate(warehouses);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();

        LOGGER.info("sync for warehouses ok");
    }

    @Override
    public SearchWarehousesResponse query(SearchWarehousesCommand cmd) {
    	//界面报空指针，加入参数校验
    	if (cmd.getOwnerId() == null || cmd.getCommunityId() == null) {
    		throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_ORGIDORCOMMUNITYID_IS_EMPTY,
                    "OrgIdorCommunityId user privilege error");
		}
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        QueryBuilder qb = null;
        if(cmd.getName() == null || cmd.getName().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            /*qb = QueryBuilders.multiMatchQuery(cmd.getName())
                    .field("name", 5.0f)
                    .field("name.pinyin_prefix", 2.0f)
                    .field("name.pinyin_gram", 1.0f);
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("name");*/
        	String pattern = "*" + cmd.getName() + "*";
            qb = QueryBuilders.boolQuery()
            					.should(QueryBuilders.wildcardQuery("name", pattern));

            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("name");

        }

        FilterBuilder fb = FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId());
        // adapt to zuolin base remove searching with ownerId and ownerType by jiarui
//fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerId", cmd.getOwnerId()));
        //fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerType", cmd.getOwnerType().toLowerCase()));
        //增加园区id的筛选 by wentian
        fb = FilterBuilders.andFilter(fb,FilterBuilders.termFilter("communityId", cmd.getCommunityId()));

        if(cmd.getStatus() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("status", cmd.getStatus()));
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

        SearchResponse rsp = builder.execute().actionGet();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("query warehouse :{}", builder);
        }

        List<Long> ids = getIds(rsp);

        SearchWarehousesResponse response = new SearchWarehousesResponse();
        if(ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        } else {
            response.setNextPageAnchor(null);
        }

        List<WarehouseDTO> warehouseDTOs = new ArrayList<WarehouseDTO>();
        for(Long id : ids) {
            Warehouses warehouse = warehouseProvider.findWarehouse(id, cmd.getOwnerType(), cmd.getOwnerId(),cmd.getCommunityId());
            WarehouseDTO dto = ConvertHelper.convert(warehouse, WarehouseDTO.class);
            warehouseDTOs.add(dto);
        }
        response.setWarehouseDTOs(warehouseDTOs);

        return response;
    }

    private XContentBuilder createDoc(Warehouses warehouse){
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("namespaceId", warehouse.getNamespaceId());
            b.field("ownerId", warehouse.getOwnerId());
            b.field("ownerType", warehouse.getOwnerType());
            b.field("status", warehouse.getStatus());
            b.field("name", warehouse.getName());
            b.field("communityId",warehouse.getCommunityId());


            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create warehouse " + warehouse.getId() + " error");
            return null;
        }
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
