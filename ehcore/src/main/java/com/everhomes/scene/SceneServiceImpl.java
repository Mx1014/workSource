// @formatter:off
package com.everhomes.scene;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.community.CommunityType;
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
    private CommunityProvider communityProvider;

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
		if(cmd.getCommunityId() == null) {
			LOGGER.error("Community id may not be null, userId={}, namespaceId={}, cmd={}", userId, namespaceId, cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Community id may not be null");
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null) {
			LOGGER.error("The community id is invalid, userId={}, namespaceId={}, cmd={}", userId, namespaceId, cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "The community id is invalid");
		}
		CommunityType communityType = CommunityType.fromCode(community.getCommunityType());
		List<SceneTypeInfo> sceneTypeList = new ArrayList<>();
		if(communityType == CommunityType.COMMERCIAL) {
			sceneTypeList.addAll(sceneProvider.findSceneTypeByName(0, SceneType.PARK_TOURIST.getCode()));
			sceneTypeList.addAll(sceneProvider.findSceneTypeByName(0, SceneType.PM_ADMIN.getCode()));
		} else if(communityType == CommunityType.RESIDENTIAL) {
            sceneTypeList.addAll(sceneProvider.findSceneTypeByName(0, SceneType.DEFAULT.getCode()));
            sceneTypeList.addAll(sceneProvider.findSceneTypeByName(0, SceneType.PM_ADMIN.getCode()));
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
