// @formatter:off
package com.everhomes.scene;

import java.util.List;

import org.springframework.stereotype.Component;

import com.everhomes.rest.scene.ListSceneTypesCommand;
import com.everhomes.rest.scene.SceneTypeInfoDTO;

@Component
public interface SceneService {
    /**
     * 根据ID获取场景类型
     * @param id 场景类型ID
     * @return 场景类型
     */
    SceneTypeInfo getSceneTypeById(Long id);
    
    /**
     * 根据名称获取场景类型
     * @param namespaceId 域名空间
     * @param name 场景名称
     * @return 场景类型
     */
    List<SceneTypeInfo> getSceneTypeByName(Integer namespaceId, String name);
    
    /**
     * 根据名称获取基础场景类型，场景类型可继承，指定名称后获取最顶层父亲场景
     * @param namespaceId 域名空间
     * @param name 场景名称
     * @return 场景类型
     */
    SceneTypeInfo getBaseSceneTypeByName(Integer namespaceId, String name);
    
    /**
     * 列出场景类型
     * @param cmd 域名空间等信息
     * @return
     */
    List<SceneTypeInfoDTO> listSceneTypes(ListSceneTypesCommand cmd);
 }
