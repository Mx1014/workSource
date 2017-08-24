// @formatter:off
package com.everhomes.namespace;

import com.everhomes.launchpad.LaunchPadItem;
import com.everhomes.launchpad.LaunchPadProvider;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.community.GetCommunityAuthPopupConfigCommand;
import com.everhomes.rest.namespace.*;
import org.elasticsearch.common.jackson.dataformat.yaml.snakeyaml.events.Event;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.CommunityService;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.community.CommunityAuthPopupConfigDTO;
import com.everhomes.rest.namespace.ListCommunityByNamespaceCommand;
import com.everhomes.rest.namespace.NamespaceDetailDTO;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class NamespaceResourceServiceImpl implements NamespaceResourceService {
	private static final Logger LOGGER = LoggerFactory.getLogger(NamespaceResourceServiceImpl.class);
	ExecutorService pool = Executors.newFixedThreadPool(3);
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private NamespaceResourceProvider namespaceResourceProvider;

	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private LaunchPadProvider launchPadProvider;

    @Autowired
    private CommunityService communityService;

    @Override
    public ListCommunityByNamespaceCommandResponse listCommunityByNamespace(ListCommunityByNamespaceCommand cmd) {
	    ListCommunityByNamespaceCommandResponse response = new ListCommunityByNamespaceCommandResponse();
	    
	    // TODO: 暂时先不分页查，后面再补
	    int namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
//	    List<NamespaceResource> result = namespaceResourceProvider.listResourceByNamespace(namespaceId, NamespaceResourceType.COMMUNITY);
//	    
//	    List<CommunityDTO> communityList = new ArrayList<CommunityDTO>();
//        if(result != null && result.size() > 0){
//            CommunityDTO dto = null;
//            for (NamespaceResource resource : result) {
//                NamespaceResourceType type = NamespaceResourceType.fromCode(resource.getResourceType());
//                if(type == NamespaceResourceType.COMMUNITY) {
//                    Community community = communityProvider.findCommunityById(resource.getResourceId());
//                    if(community != null) {
//                        dto = ConvertHelper.convert(community, CommunityDTO.class);
//                        communityList.add(dto);
//                    } else {
//                        LOGGER.error("Community not found, namespaceId=" + resource.getNamespaceId() 
//                            + ", communityId=" + resource.getResourceId() + ", cmd=" + cmd);
//                    }
//                } else {
//                    LOGGER.error("Unsupported namespace resource type, namespaceId=" + resource.getNamespaceId() 
//                        + ", type=" + type + ", communityId=" + resource.getResourceId() + ", cmd=" + cmd);
//                }
//            }
//        }
	    
	    // 先从namespaceResource查询域下面的全部的小区，然后再从community筛选类型  by sfyan 20160524
	    int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
	    List<NamespaceResource> resources = namespaceResourceProvider.listResourceByNamespace(namespaceId, NamespaceResourceType.COMMUNITY);
	    List<Long> communityIds = new ArrayList<Long>();
	    for (NamespaceResource resource : resources) {
	    	communityIds.add(resource.getResourceId());
		}
	    CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());		
		List<CommunityDTO> communityList = communityProvider.listCommunitiesByType(namespaceId, communityIds, cmd.getCommunityType(), locator, pageSize);
        response.setCommunities(communityList);
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("List communtiies by namespace, namespaceId={}, communityIds={}, cmd={}", namespaceId, communityIds, cmd);
        }
        
        return response;
    }
	
	@Override
	public NamespaceDetailDTO getNamespaceDetail(GetNamespaceDetailCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
	    NamespaceDetailDTO detailDto = null;
	    
	    NamespaceDetail namespaceDetail = namespaceResourceProvider.findNamespaceDetailByNamespaceId(cmd.getNamespaceId());
        if(namespaceDetail != null) {
            detailDto = ConvertHelper.convert(namespaceDetail, NamespaceDetailDTO.class);

            // 用户认证弹窗设置 add by xq.tian  2017/08/09
            GetCommunityAuthPopupConfigCommand cmd1 = new GetCommunityAuthPopupConfigCommand();
            cmd1.setNamespaceId(cmd.getNamespaceId());
            CommunityAuthPopupConfigDTO communityAuthPopupConfig = communityService.getCommunityAuthPopupConfig(cmd1);
            detailDto.setAuthPopupConfig(communityAuthPopupConfig.getStatus());
        }
        
		//需要蒙版的信息

		//从配置中读取

		List<LaunchPadItem> items_default = this.launchPadProvider.searchLaunchPadItemsByItemName(namespaceId,null,"Talent Apartment");
		List masks_pm = getMasksFromItemInfo(items_default, "快速切换至园区主页");
		if(masks_pm != null){
			detailDto.setPmMasks(masks_pm);
		}

		return detailDto;
	}

	//从item信息中获得蒙版信息
	private List<MaskDTO> getMasksFromItemInfo(List<LaunchPadItem> items, String tips){
		if(items.size() != 0){
			List masks = items.stream().map(r->{
				MaskDTO pmMask = new MaskDTO();
				pmMask.setId(r.getId());
				pmMask.setIconName(r.getItemName());
				pmMask.setTips(tips);
				return pmMask;
			}).collect(Collectors.toList());
			return masks;
		}
		return null;
	}
}
