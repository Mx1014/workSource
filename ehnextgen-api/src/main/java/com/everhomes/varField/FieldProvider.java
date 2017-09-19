package com.everhomes.varField;

import java.util.List;

/**
 * Created by ying.xiong on 2017/8/3.
 */
public interface FieldProvider {
    List<ScopeFieldGroup> listScopeFieldGroups(Integer namespaceId, Long communityId, String moduleName);
    List<FieldGroup> listFieldGroups(List<Long> ids);
    List<FieldGroup> listFieldGroups(String moduleName);
    List<ScopeField> listScopeFields(Integer namespaceId, Long communityId, String moduleName, String groupPath);
    List<Field> listFields(List<Long> ids);
    List<Field> listFields(String moduleName, String groupPath);
    List<FieldItem> listFieldItems(List<Long> fieldIds);
    List<ScopeFieldItem> listScopeFieldItems(List<Long> fieldIds, Integer namespaceId);
    List<ScopeFieldItem> listScopeFieldItems(Long fieldId, Integer namespaceId, Long communityId);
    ScopeFieldItem findScopeFieldItemByFieldItemId(Integer namespaceId, Long communityId, Long itemId);
    ScopeFieldItem findScopeFieldItemByDisplayName(Integer namespaceId, Long communityId, String moduleName, String displayName);
    ScopeField findScopeField(Integer namespaceId, Long communityId, Long fieldId);
}
