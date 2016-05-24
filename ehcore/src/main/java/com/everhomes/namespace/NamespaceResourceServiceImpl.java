// @formatter:off
package com.everhomes.namespace;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.namespace.GetNamespaceDetailCommand;
import com.everhomes.rest.namespace.ListCommunityByNamespaceCommand;
import com.everhomes.rest.namespace.ListCommunityByNamespaceCommandResponse;
import com.everhomes.rest.namespace.NamespaceCommunityType;
import com.everhomes.rest.namespace.NamespaceDetailDTO;
import com.everhomes.rest.namespace.NamespaceResourceType;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;

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
	
	@Override
    public ListCommunityByNamespaceCommandResponse listCommunityByNamespace(ListCommunityByNamespaceCommand cmd) {
	    ListCommunityByNamespaceCommandResponse response = new ListCommunityByNamespaceCommandResponse();
	    
	    // TODO: 暂时先不分页查，后面再补
	    int namespaceId = UserContext.getCurrentNamespaceId();
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
	    List<CommunityDTO> communityList = new ArrayList<CommunityDTO>();
	    List<Community> communities = communityProvider.listCommunitiesByNamespaceId(namespaceId);
	    if(communities != null && communities.size() > 0) {
	    	for (Community community : communities) {
				if(null != cmd.getCommunityType()){
					if(cmd.getCommunityType().equals(community.getCommunityType())){
						communityList.add(ConvertHelper.convert(community, CommunityDTO.class));
					}
				}else{
					communityList.add(ConvertHelper.convert(community, CommunityDTO.class));
				}
			}
	    }

        response.setCommunities(communityList);
        
        return response;
    }
	
	@Override
	public NamespaceDetailDTO getNamespaceDetail(GetNamespaceDetailCommand cmd) {
	    NamespaceDetailDTO detailDto = null;
	    
	    NamespaceDetail namespaceDetail = namespaceResourceProvider.findNamespaceDetailByNamespaceId(cmd.getNamespaceId());
        if(namespaceDetail != null) {
            detailDto = ConvertHelper.convert(namespaceDetail, NamespaceDetailDTO.class);
        }
        
        return detailDto;
	}
}
