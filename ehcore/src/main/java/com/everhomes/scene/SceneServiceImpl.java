// @formatter:off
package com.everhomes.scene;

import java.util.ArrayList;
import java.util.List;


import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



import com.everhomes.rest.scene.ListSceneTypesCommand;
import com.everhomes.rest.scene.SceneTypeInfoDTO;import com.everhomes.util.ConvertHelper;


@Component
public class SceneServiceImpl implements SceneService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SceneServiceImpl.class);
        
    @Autowired
    private SceneProvider sceneProvider;

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
 }
