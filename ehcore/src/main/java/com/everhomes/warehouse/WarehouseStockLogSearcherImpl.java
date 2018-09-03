package com.everhomes.warehouse;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.acl.PrivilegeServiceErrorCode;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.warehouse.SearchWarehouseStockLogsCommand;
import com.everhomes.rest.warehouse.SearchWarehouseStockLogsResponse;
import com.everhomes.rest.warehouse.WarehouseStockLogDTO;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SearchUtils;
import com.everhomes.search.WarehouseStockLogSearcher;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2017/5/16.
 */
@Component
public class WarehouseStockLogSearcherImpl extends AbstractElasticSearch implements WarehouseStockLogSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseStockLogSearcherImpl.class);

    @Autowired
    private WarehouseProvider warehouseProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private PortalService portalService;

    @Autowired
	private UserProvider userProvider;

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<WarehouseStockLogs> logs) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (WarehouseStockLogs log : logs) {

            XContentBuilder source = createDoc(log);
            if(null != source) {
                LOGGER.info("warehouse stock log id:" + log.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(log.getId().toString()).source(source));
            }

        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public void feedDoc(WarehouseStockLogs log) {
        XContentBuilder source = createDoc(log);

        feedDoc(log.getId().toString(), source);
    }

    @Override
    public void syncFromDb() {
        int pageSize = 200;
        this.deleteAll();

        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<WarehouseStockLogs> logs = warehouseProvider.listWarehouseStockLogs(locator, pageSize);

            if(logs.size() > 0) {
                this.bulkUpdate(logs);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();

        LOGGER.info("sync for warehouse stock logs ok");
    }

    @Override
    public SearchWarehouseStockLogsResponse query(SearchWarehouseStockLogsCommand cmd) {
        //checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(),PrivilegeConstants.WAREHOUSE_REPO_MAINTAIN_SEARCH,cmd.getOwnerId());
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
        FilterBuilder fb = FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId());
//        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerId", cmd.getOwnerId()));
        //fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerType", cmd.getOwnerType()));
        //新增， 兼容性还没有
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("communityId", cmd.getCommunityId()));

        if(cmd.getWarehouseId() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("warehouseId", cmd.getWarehouseId()));
        }

        if(cmd.getMaterialId() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("materialId", cmd.getMaterialId()));
        }

        if(cmd.getRequestType() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("requestType", cmd.getRequestType()));
        }

        if(cmd.getMaterialNumber() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("materialNumber", cmd.getMaterialNumber()));
        }

        if(cmd.getRequestName() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("requestName", cmd.getRequestName()));
        }
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
//        Long anchor = 1l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
//
        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);

        if(cmd.getMaterialName() == null || cmd.getMaterialName().isEmpty()) {
            builder.addSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC).ignoreUnmapped(true));
        }

        SearchResponse rsp = builder.execute().actionGet();

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("query warehouse stock logs :{}, rsp :{}", builder, rsp);
        }

        List<Long> ids = getIds(rsp);
        Long total = getTotal(rsp);
        SearchWarehouseStockLogsResponse response = new SearchWarehouseStockLogsResponse();
//        List<Long> ids = warehouseProvider.findAllMaterialLogIds(
//                cmd.getWarehouseOrderId(),anchor,pageSize,response);
//        if(ids.size() > pageSize) {
//            response.setNextPageAnchor(anchor + 1);
//            ids.remove(ids.size() - 1);
//        } else {
//            response.setNextPageAnchor(null);
//        }

        List<WarehouseStockLogDTO> logDTOs = new ArrayList<WarehouseStockLogDTO>();
        for(Long id : ids) {
            WarehouseStockLogs log = warehouseProvider.findWarehouseStockLogs(id, cmd.getOwnerType(), cmd.getOwnerId());
            WarehouseStockLogDTO dto = ConvertHelper.convert(log, WarehouseStockLogDTO.class);

            if(log.getRequestUid() != null) {
                List<OrganizationMember> requests = organizationProvider.listOrganizationMembers(log.getRequestUid());
                if(requests != null && requests.size() > 0) {
                    dto.setRequestUserName(requests.get(0).getContactName());
                }
            }
            if (dto.getDeliveryUid() != null) {
				//用户可能不在组织架构中 所以用nickname
				User user = userProvider.findUserById(dto.getDeliveryUid());
				if(user != null) {
					dto.setDeliveryUserName(user.getNickName());
				}
			}

            List<OrganizationMember> deliveries = organizationProvider.listOrganizationMembers(log.getDeliveryUid());
            if(deliveries != null && deliveries.size() > 0) {
                dto.setDeliveryUserName(deliveries.get(0).getContactName());
            }

            Warehouses warehouse = warehouseProvider.findWarehouse(dto.getWarehouseId(), cmd.getOwnerType(), cmd.getOwnerId(),cmd.getCommunityId());
            if(warehouse != null) {
                dto.setWarehouseName(warehouse.getName());
            }

            WarehouseMaterials material = warehouseProvider.findWarehouseMaterials(dto.getMaterialId(), cmd.getOwnerType(), cmd.getOwnerId(),cmd.getCommunityId());
            if(material != null) {
                dto.setMaterialName(material.getName());
                dto.setMaterialNumber(material.getMaterialNumber());
                dto.setUnitId(material.getUnitId());
                dto.setSupplierName(material.getSupplierName());

                WarehouseUnits unit = warehouseProvider.findWarehouseUnits(material.getUnitId(), cmd.getOwnerType(), cmd.getOwnerId());
                if(unit != null) {
                    dto.setUnitName(unit.getName());
                }
            }
            logDTOs.add(dto);
        }
        // sort logDTOs on create time asc -- by vincent.wang 2018/3/21 on planet earth
        logDTOs = logDTOs.stream().sorted(Comparator.comparing(WarehouseStockLogDTO::getCreateTime))
                .collect(Collectors.toList());
        response.setStockLogDTOs(logDTOs);
        return response;
    }

    @Override
    public SearchWarehouseStockLogsResponse queryByOrder(SearchWarehouseStockLogsCommand cmd) {
        //checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(),PrivilegeConstants.WAREHOUSE_REPO_MAINTAIN_SEARCH,cmd.getOwnerId());
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
//        Long anchor = 0l;
        Long anchor = 1l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
        SearchWarehouseStockLogsResponse response = new SearchWarehouseStockLogsResponse();
        List<Long> ids = warehouseProvider.findAllMaterialLogIds(
                cmd.getWarehouseOrderId(),anchor,pageSize,response);
        if(ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        } else {
            response.setNextPageAnchor(null);
        }

        List<WarehouseStockLogDTO> logDTOs = new ArrayList<WarehouseStockLogDTO>();
        for(Long id : ids) {
            WarehouseStockLogs log = warehouseProvider.findWarehouseStockLogs(id, cmd.getOwnerType(), cmd.getOwnerId(), cmd.getMaterialName());
            WarehouseStockLogDTO dto = ConvertHelper.convert(log, WarehouseStockLogDTO.class);

            if(log.getRequestUid() != null) {
                List<OrganizationMember> requests = organizationProvider.listOrganizationMembers(log.getRequestUid());
                if(requests != null && requests.size() > 0) {
                    dto.setRequestUserName(requests.get(0).getContactName());
                }
            }

            List<OrganizationMember> deliveries = organizationProvider.listOrganizationMembers(log.getDeliveryUid());
            if(deliveries != null && deliveries.size() > 0) {
                dto.setDeliveryUserName(deliveries.get(0).getContactName());
            }

            Warehouses warehouse = warehouseProvider.findWarehouse(dto.getWarehouseId(), cmd.getOwnerType(), cmd.getOwnerId(),cmd.getCommunityId());
            if(warehouse != null) {
                dto.setWarehouseName(warehouse.getName());
            }

            WarehouseMaterials material = warehouseProvider.findWarehouseMaterials(dto.getMaterialId(), cmd.getOwnerType(), cmd.getOwnerId(),cmd.getCommunityId());
            if(material != null) {
                dto.setMaterialName(material.getName());
                dto.setMaterialNumber(material.getMaterialNumber());
                dto.setUnitId(material.getUnitId());
                dto.setSupplierName(material.getSupplierName());

                WarehouseUnits unit = warehouseProvider.findWarehouseUnits(material.getUnitId(), cmd.getOwnerType(), cmd.getOwnerId());
                if(unit != null) {
                    dto.setUnitName(unit.getName());
                }
            }
            logDTOs.add(dto);
        }
        response.setStockLogDTOs(logDTOs);
        return response;
    }

    private XContentBuilder createDoc(WarehouseStockLogs log){
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("namespaceId", log.getNamespaceId());
            b.field("ownerId", log.getOwnerId());
            b.field("ownerType", log.getOwnerType());
            b.field("warehouseId", log.getWarehouseId());
            b.field("materialId", log.getMaterialId());
            b.field("requestType", log.getRequestType());
            b.field("createTime", log.getCreateTime());
            b.field("communityId", log.getCommunityId());

            if(log.getRequestUid() != null) {
                b.field("requestUid", log.getRequestUid());
                List<OrganizationMember> members = organizationProvider.listOrganizationMembers(log.getRequestUid());
                if(members != null && members.size() > 0) {
                    b.field("requestName", members.get(0).getContactName());
                } else {
                    b.field("requestName", "");
                }
            } else {
                b.field("requestUid", "");
                b.field("requestName", "");
            }

            WarehouseMaterials material = warehouseProvider.findWarehouseMaterials(log.getMaterialId(), log.getOwnerType(), log.getOwnerId(),log.getCommunityId());
            if(material != null) {
                b.field("materialName", material.getName());
                b.field("materialNumber", material.getMaterialNumber());
            } else {
                b.field("materialName", "");
                b.field("materialNumber", "");
            }



            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create warehouse stock log  " + log.getId() + " error");
            return null;
        }
    }

    private Long getTotal(SearchResponse rsp) {
        List<Long> results = new ArrayList<Long>();
        Long totalHits= rsp.getHits().getTotalHits();
        return totalHits;
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

    @Override
    public String getIndexType() {
        return SearchUtils.WAREHOUSE_STOCK_LOG;
    }
}
