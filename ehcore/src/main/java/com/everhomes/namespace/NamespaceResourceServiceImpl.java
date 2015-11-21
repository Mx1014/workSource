// @formatter:off
package com.everhomes.namespace;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.address.CommunityDTO;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.db.DbProvider;
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
	    int namespaceId = (cmd.getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : cmd.getNamespaceId();
	    List<NamespaceResource> result = namespaceResourceProvider.listResourceByNamespace(namespaceId, NamespaceResourceType.COMMUNITY);
	    
	    List<CommunityDTO> communityList = new ArrayList<CommunityDTO>();
        if(result != null && result.size() > 0){
            CommunityDTO dto = null;
            for (NamespaceResource resource : result) {
                NamespaceResourceType type = NamespaceResourceType.fromCode(resource.getResourceType());
                if(type == NamespaceResourceType.COMMUNITY) {
                    Community community = communityProvider.findCommunityById(resource.getResourceId());
                    if(community != null) {
                        dto = ConvertHelper.convert(community, CommunityDTO.class);
                        communityList.add(dto);
                    } else {
                        LOGGER.error("Community not found, namespaceId=" + resource.getNamespaceId() 
                            + ", communityId=" + resource.getResourceId() + ", cmd=" + cmd);
                    }
                } else {
                    LOGGER.error("Unsupported namespace resource type, namespaceId=" + resource.getNamespaceId() 
                        + ", type=" + type + ", communityId=" + resource.getResourceId() + ", cmd=" + cmd);
                }
            }
        }
        response.setCommunities(communityList);
        
        return response;
    }
}
