// @formatter:off
package com.everhomes.scene;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public interface SceneProvider {
    SceneTypeInfo findSceneTypeById(long id);
    List<SceneTypeInfo>  findSceneTypeByName(Integer namespaceId, String name);
    List<SceneTypeInfo> listSceneTypes(Integer namespaceId);
 }
