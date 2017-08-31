// @formatter:off
package com.everhomes.namespace;

import com.everhomes.community.CommunityProvider;
import com.everhomes.community.CommunityService;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.launchpad.LaunchPadItem;
import com.everhomes.launchpad.LaunchPadProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.community.CommunityAuthPopupConfigDTO;
import com.everhomes.rest.community.GetCommunityAuthPopupConfigCommand;
import com.everhomes.rest.namespace.*;
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

@Component
public class NamespaceResourceServiceImpl implements NamespaceResourceService {
	private static final Logger LOGGER = LoggerFactory.getLogger(NamespaceResourceServiceImpl.class);
	ExecutorService pool = Executors.newFixedThreadPool(3);

	//蒙版启用参数
	private final static Integer MASK_ENABLE = 0;
	private final static Integer MASK_DISABLE = 1;

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

	@Autowired
	private NamespacesProvider namespacesProvider;

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
        
		//读取蒙版的配置项 默认打开
		Integer maskFlag = this.configurationProvider.getIntValue(namespaceId, "mask.key", MASK_DISABLE);
		detailDto.setMaskFlag(maskFlag);

		if(maskFlag == 0){
			//从配置中读取MaskDTO
			List<MaskDTO> masks = this.namespacesProvider.listNamespaceMasks(namespaceId);
			if(masks != null){
				masks.forEach(r->{
					//在圖標表中查找
					LaunchPadItem item = this.launchPadProvider.searchLaunchPadItemsByItemName(namespaceId, r.getSceneType(), r.getItemName());
					if(item != null){
						//有效圖標
						r.setId(item.getId());
					}else{
						//無效圖標
						r.setId(0L);
						r.setTips("cannot found item");
					}
				});
			}
			if(masks != null && masks.size()  > 0){
				detailDto.setPmMasks(masks);
			}
		}else{
			detailDto.setMaskFlag(MASK_DISABLE);
		}

		return detailDto;
	}

}
