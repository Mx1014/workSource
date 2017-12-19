// @formatter:off
package com.everhomes.scene;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.CommunityService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.namespace.NamespaceDetail;
import com.everhomes.namespace.NamespaceResourceProvider;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.namespace.NamespaceCommunityType;
import com.everhomes.rest.scene.ListSceneTypesCommand;
import com.everhomes.rest.scene.SceneTypeInfoDTO;
import com.everhomes.rest.ui.user.ListScentTypeByOwnerCommand;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class SceneServiceImpl implements SceneService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SceneServiceImpl.class);
        
    @Autowired
    private SceneProvider sceneProvider;

    @Autowired
    private CommunityService communityService;
    
    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private NamespaceResourceProvider namespaceResourceProvider;

    @Override
    public SceneTypeInfo getSceneTypeById(Long id) {
        if(id != null) {
            return sceneProvider.findSceneTypeById(id);
        } else {
            return null;
        }
    }

    @Override
    public List<SceneTypeInfo> getSceneTypeByName(Integer namespaceId, String name) {
        return sceneProvider.findSceneTypeByName(namespaceId, name);
    }

    @Override
    public SceneTypeInfo getBaseSceneTypeByName(Integer namespaceId, String name) {
        SceneTypeInfo scene = null;
        List<SceneTypeInfo> sceneList = sceneProvider.findSceneTypeByName(namespaceId, name);
        if(sceneList != null && sceneList.size() > 0) {
            scene = sceneList.get(0);
            if(scene != null && scene.getParentId() != null && scene.getParentId() > 0) {
                scene = getSceneTypeById(scene.getParentId());
            }
        }
        
        return scene;
    }
    
    @Override
    public List<SceneTypeInfoDTO> listSceneTypes(ListSceneTypesCommand cmd) {
        Integer namespaceId = null;
        if(cmd != null) {
            namespaceId = cmd.getNamespaceId();
        }
        List<SceneTypeInfo> sceneList = sceneProvider.listSceneTypes(namespaceId);
        
        List<SceneTypeInfoDTO> result = null;
        if(sceneList != null && sceneList.size() != 0) {
            result = sceneList.stream().map((r) -> {
                return ConvertHelper.convert(r, SceneTypeInfoDTO.class);
            }).collect(Collectors.toList());
        }
        
        return result;
    }
    
    @Override
	public List<SceneTypeInfoDTO> listSceneTypeByOwner(ListScentTypeByOwnerCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		Integer namespaceId = UserContext.getCurrentNamespaceId();


        //加了全部，此时没有communityId edit by yanjun 20171211
//		if(cmd.getCommunityId() == null) {
//			LOGGER.error("Community id may not be null, userId={}, namespaceId={}, cmd={}", userId, namespaceId, cmd);
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//                    "Community id may not be null");
//		}
//
//		if(community == null) {
//			LOGGER.error("The community id is invalid, userId={}, namespaceId={}, cmd={}", userId, namespaceId, cmd);
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//                    "The community id is invalid");
//		}

        List<SceneTypeInfo> sceneTypeList = new ArrayList<>();

        Community community = null;
        if(cmd.getCommunityId() != null){
            community = communityProvider.findCommunityById(cmd.getCommunityId());
        }

        if(community == null){
            //全部园区
            NamespaceDetail namespaceDetail = namespaceResourceProvider.findNamespaceDetailByNamespaceId(namespaceId);
            switch (NamespaceCommunityType.fromCode(namespaceDetail.getResourceType())){
                case COMMUNITY_COMMERCIAL:
                    sceneTypeList.addAll(sceneProvider.findSceneTypeByName(0, SceneType.PARK_TOURIST.getCode()));
                    break;
                case COMMUNITY_RESIDENTIAL:
                    sceneTypeList.addAll(sceneProvider.findSceneTypeByName(0, SceneType.DEFAULT.getCode()));
                    break;
                case COMMUNITY_MIX:
                    sceneTypeList.addAll(sceneProvider.findSceneTypeByName(0, SceneType.PARK_TOURIST.getCode()));
                    sceneTypeList.addAll(sceneProvider.findSceneTypeByName(0, SceneType.DEFAULT.getCode()));
                    break;
            }

            sceneTypeList.addAll(sceneProvider.findSceneTypeByName(0, SceneType.PM_ADMIN.getCode()));

        }else {

            //单个园区
            CommunityType communityType = CommunityType.fromCode(community.getCommunityType());

            if(communityType == CommunityType.COMMERCIAL) {
                sceneTypeList.addAll(sceneProvider.findSceneTypeByName(0, SceneType.PARK_TOURIST.getCode()));
                sceneTypeList.addAll(sceneProvider.findSceneTypeByName(0, SceneType.PM_ADMIN.getCode()));
            } else if(communityType == CommunityType.RESIDENTIAL) {
                sceneTypeList.addAll(sceneProvider.findSceneTypeByName(0, SceneType.DEFAULT.getCode()));
                sceneTypeList.addAll(sceneProvider.findSceneTypeByName(0, SceneType.PM_ADMIN.getCode()));
            }
        }


		List<SceneTypeInfoDTO> dtos = new ArrayList<>();
		sceneTypeList.forEach(info -> {
			SceneTypeInfoDTO dto = new SceneTypeInfoDTO();
			dto.setName(info.getName());
			dto.setDisplayName(info.getDisplayName());
			dtos.add(dto);
		});
		return dtos;
	}
 }
