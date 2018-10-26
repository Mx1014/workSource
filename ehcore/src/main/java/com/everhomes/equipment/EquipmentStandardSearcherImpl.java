package com.everhomes.equipment;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.portal.PortalService;
import com.everhomes.repeat.RepeatService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.equipment.EquipmentModelType;
import com.everhomes.rest.equipment.EquipmentStandardCommunity;
import com.everhomes.rest.equipment.EquipmentStandardStatus;
import com.everhomes.rest.equipment.EquipmentStandardsDTO;
import com.everhomes.rest.equipment.SearchEquipmentStandardsCommand;
import com.everhomes.rest.equipment.SearchEquipmentStandardsResponse;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EquipmentStandardSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class EquipmentStandardSearcherImpl extends AbstractElasticSearch implements EquipmentStandardSearcher{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentStandardSearcherImpl.class);

	@Autowired
	private EquipmentProvider equipmentProvider;
	
	@Autowired
	private ConfigurationProvider configProvider;
	
	@Autowired
	private RepeatService repeatService;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private PortalService portalService;

    @Autowired
    private CommunityProvider communityProvider;

    //add atomic offset for (all community)
    //private static AtomicInteger offset = new AtomicInteger(0);
    //private static ConcurrentHashMap<Long, Integer> offsetHashMap = new ConcurrentHashMap<>();

    @Override
	public void deleteById(Long id) {
		deleteById(id.toString());
	}

	@Override
	public void bulkUpdate(List<EquipmentInspectionStandards> standards) {
		BulkRequestBuilder brb = getClient().prepareBulk();
        for (EquipmentInspectionStandards standard : standards) {
        	
            XContentBuilder source = createDoc(standard);
            if(null != source) {
                LOGGER.info("equipment inspection standard id:" + standard.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(standard.getId().toString()).source(source));    
                }
            
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
	}

	@Override
	public void feedDoc(EquipmentInspectionStandards standard) {
		XContentBuilder source = createDoc(standard);
        
        feedDoc(standard.getId().toString(), source);
	}

	@Override
	public void syncFromDb() {
		int pageSize = 200;      
        this.deleteAll();
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<EquipmentInspectionStandards> standards = equipmentProvider.listEquipmentInspectionStandards(locator, pageSize);
            
            if(standards.size() > 0) {
                this.bulkUpdate(standards);
            }
            
            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();
        
        LOGGER.info("sync for equipment inspection standard ok");
	}

	@Override
	public SearchEquipmentStandardsResponse query(SearchEquipmentStandardsCommand cmd) {

        checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_STANDARD_LIST,cmd.getTargetId());

        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        QueryBuilder qb = null;
        if (cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
                    .field("standardName", 5.0f)
                    .field("standardNumber", 5.0f);

            builder.addHighlightedField("standardName")
                    .addHighlightedField("standardNumber");
        }

        FilterBuilder fb = null;
        FilterBuilder nfb = FilterBuilders.termFilter("status", EquipmentStandardStatus.INACTIVE.getCode());
        fb = FilterBuilders.notFilter(nfb);

        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId()));
        if (cmd.getTargetId() != null && cmd.getTargetId() != 0L) {
            // specific community data
            FilterBuilder tfb = FilterBuilders.termFilter("targetId", cmd.getTargetId());
            fb = FilterBuilders.andFilter(fb, tfb);
        } else if (cmd.getTargetIds() != null && cmd.getTargetIds().size() > 0) {
            // all communities data
            FilterBuilder tfb = FilterBuilders.termsFilter("targetId", cmd.getTargetIds());
            // global data means template
            fb =  FilterBuilders.orFilter(tfb, FilterBuilders.andFilter(FilterBuilders.termFilter("ownerId", cmd.getOwnerId()), FilterBuilders.termFilter("targetId", 0)));
        }

        if(cmd.getStatus() != null)
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("status", cmd.getStatus()));

        if(cmd.getInspectionCategoryId() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("inspectionCategoryId", cmd.getInspectionCategoryId()));

        if(cmd.getRepeatType() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("repeatType", cmd.getRepeatType()));

        //增加设备tab页面根据设备类型查找标准选项 V3.0.2
        /*if (cmd.getCategoryId() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("categoryId", cmd.getCategoryId()));
        }*/

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0L;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }

        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);

        if(LOGGER.isDebugEnabled())
            LOGGER.info("EquipmentStandardSearcherImpl query builder ："+builder);

        SearchResponse rsp = builder.execute().actionGet();

        if (LOGGER.isDebugEnabled())
            LOGGER.info("EquipmentStandardSearcherImpl query rsp ：" + rsp);

        List<Long> ids = getIds(rsp);

        List<EquipmentStandardsDTO> eqStandards = new ArrayList<>();

        Long nextPageAnchor = null;
        if (ids.size() > pageSize) {
            nextPageAnchor = anchor + 1;
            ids.remove(ids.size() - 1);
        }

        for (Long id : ids) {
            EquipmentInspectionStandards standard = equipmentProvider.findStandardById(id);

            if (cmd.getTargetId() == null || cmd.getTargetId() == 0L) {
                //全部里面
                if (standard != null) {
                    if (standard.getTargetId() == 0L) {
                        List<Long> communityIds = equipmentProvider.listModelCommunityMapByModelId(standard.getId(), EquipmentModelType.STANDARD.getCode());
                        List<EquipmentStandardCommunity> communities = new ArrayList<>();
                        if (communityIds != null && communityIds.size() > 0) {
                            communityIds.forEach((c) -> {
                                Community community = communityProvider.findCommunityById(c);
                                if (community != null) {
                                    EquipmentStandardCommunity standardCommunity = new EquipmentStandardCommunity();
                                    standardCommunity.setCommunityId(community.getId());
                                    standardCommunity.setCommunityName(community.getName());
                                    communities.add(standardCommunity);
                                }
                            });
                        }
                        standard.setCommunities(communities);
                    } else {
                        Community community = communityProvider.findCommunityById(standard.getTargetId());
                        if (community != null) {
                            EquipmentStandardCommunity standardCommunity = new EquipmentStandardCommunity();
                            standardCommunity.setCommunityName(community.getName());
                            standardCommunity.setCommunityId(standard.getTargetId());
                            standard.setCommunities(new ArrayList<>(Collections.singletonList(standardCommunity)));
                        }
                    }
                }
            }

            if (standard != null) {
                //processRepeatSetting(standard);
                processEquipmentCount(standard);
                EquipmentStandardsDTO dto = ConvertHelper.convert(standard, EquipmentStandardsDTO.class);
                dto.setDescription("");
                eqStandards.add(dto);
            }

        }

        if (nextPageAnchor == null) {
            addApplyStandards(cmd, eqStandards);
        }

        return new SearchEquipmentStandardsResponse(nextPageAnchor, eqStandards);
    }

    private void addApplyStandards(SearchEquipmentStandardsCommand cmd, List<EquipmentStandardsDTO> eqStandards) {
        if (cmd.getTargetId() == null || cmd.getTargetId() == 0L) {
            return;
        }
        List<EquipmentModelCommunityMap> maps = equipmentProvider.listModelCommunityMapByCommunityId(cmd.getTargetId(), EquipmentModelType.STANDARD.getCode());
        List<EquipmentInspectionStandards> standardsList = equipmentProvider.listEquipmentStandardWithReferId(cmd.getTargetId(), cmd.getTargetType());
        if (standardsList != null && standardsList.size() > 0) {
            standardsList.forEach((standard) -> {
                if (standard.getReferId() != null && standard.getReferId() != 0L) {
                    if (maps != null && maps.size() > 0)
                        maps.removeIf((s) -> Objects.equals(s.getModelId(), standard.getReferId()));
                }
            });
        }

        if (maps != null && maps.size() > 0) {
            for (EquipmentModelCommunityMap map : maps) {
                EquipmentInspectionStandards standard = equipmentProvider.findStandardById(map.getModelId());
                if (standard != null && EquipmentStandardStatus.ACTIVE.equals(EquipmentStandardStatus.fromStatus(standard.getStatus()))) {
                    //有效的才加  fix bug
                    if (cmd.getStatus() != null) {
                        if (!Objects.equals(standard.getStatus(), cmd.getStatus())) {
                            continue;
                        }
                    }
                    if (cmd.getStandardType() != null) {
                        LOGGER.info("map standard :"+cmd.getStandardType());
                        if (!Objects.equals(standard.getStandardType(), cmd.getStandardType())) {
                            continue;
                        }
                    }
                    if (cmd.getRepeatType() != null) {
                        if (!Objects.equals(standard.getRepeatType(), cmd.getRepeatType())) {
                            continue;
                        }
                    }
                    if (cmd.getInspectionCategoryId() != null) {
                        if (!Objects.equals(standard.getInspectionCategoryId(), cmd.getInspectionCategoryId())) {
                            continue;
                        }
                    }
                    // processRepeatSetting(standard);
                    EquipmentStandardsDTO dto = ConvertHelper.convert(standard, EquipmentStandardsDTO.class);
                    dto.setDescription("");
                    if (null != standard.getRepeat()) {
                        RepeatSettingsDTO rs = ConvertHelper.convert(standard.getRepeat(), RepeatSettingsDTO.class);
                        dto.setRepeat(rs);
                    }
                    eqStandards.add(dto);
                }
            }
        }
    }

    private void checkUserPrivilege(Long orgId, Long privilegeId, Long communityId) {
        /*ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
        listServiceModuleAppsCommand.setNamespaceId(UserContext.getCurrentNamespaceId());
        listServiceModuleAppsCommand.setModuleId(EquipmentConstant.EQUIPMENT_MODULE);
        ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);
        boolean flag = false;
        if (null != apps && null != apps.getServiceModuleApps() && apps.getServiceModuleApps().size() > 0) {
            flag = userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), EntityType.ORGANIZATIONS.getCode(),
                    orgId, orgId, privilegeId, apps.getServiceModuleApps().get(0).getId(), null, communityId);
            if (!flag) {
                LOGGER.error("Permission is denied, namespaceId={}, orgId={}, communityId={}," +
                        " privilege={}", UserContext.getCurrentNamespaceId(), orgId, communityId, privilegeId);
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                        "Insufficient privilege");
            }
        }*/
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), orgId, privilegeId, EquipmentConstant.EQUIPMENT_MODULE, null, null, null,communityId);


    }

    private void processEquipmentCount(EquipmentInspectionStandards standard) {
        List<EquipmentStandardMap> maps = equipmentProvider.findByStandardId(standard.getId());
        if (maps != null) {
            standard.setEquipmentsCount(maps.size());
        }
    }

   /* private void processRepeatSetting(EquipmentInspectionStandards standard) {
		if(null != standard.getRepeatSettingId() && standard.getRepeatSettingId() != 0) {
			RepeatSettings repeat = repeatService.findRepeatSettingById(standard.getRepeatSettingId());
			standard.setRepeat(repeat);
		}
	}*/
	
	@Override
	public String getIndexType() {
		return SearchUtils.EQUIPMENTSTANDARDINDEXTYPE;
	}
	
	private XContentBuilder createDoc(EquipmentInspectionStandards standard){
		try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("standardName", standard.getName()).field("index","not_analyzed");
           // b.field("standardNumber", standard.getStandardNumber());
            b.field("ownerId", standard.getOwnerId());
            b.field("ownerType", standard.getOwnerType());
            b.field("standardType", standard.getStandardType());
            b.field("inspectionCategoryId", standard.getInspectionCategoryId());
            b.field("status", standard.getStatus());
            b.field("repeatType", standard.getRepeatType());
            b.field("namespaceId", standard.getNamespaceId());

            if(standard.getTargetId() != null) {
                b.field("targetId", standard.getTargetId());
            } else {
                b.field("targetId", 0);
            }

            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create standard " + standard.getId() + " error");
            return null;
        }
    }

}
