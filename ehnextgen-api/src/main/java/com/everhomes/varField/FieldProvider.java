package com.everhomes.varField;

import java.util.List;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/8/3.
 */
public interface FieldProvider {
    Map<Long, ScopeFieldGroup> listScopeFieldGroups(Integer namespaceId, Long communityId, String moduleName);
    List<FieldGroup> listFieldGroups(List<Long> ids);
    FieldGroup findFieldGroup(Long id);
    List<FieldGroup> listFieldGroups(String moduleName);
    Map<Long, ScopeField> listScopeFields(Integer namespaceId, Long communityId, String moduleName, String groupPath);
    List<Field> listFields(List<Long> ids);
    List<Field> listFields(String moduleName, String groupPath);



    void saveFieldGroups(String customerType, Long customerId, List<Object> objects, String simpleName);

    String findClassNameByGroupDisplayName(String groupDisplayName);

    List<FieldItem> listFieldItems(Long fieldId);
    List<ScopeFieldItem> listScopeFieldItems(Long fieldId, Integer namespaceId, Long communityId);
    Map<Long, ScopeFieldItem> listScopeFieldsItems(List<Long> fieldIds, Integer namespaceId, Long communityId);
    ScopeFieldItem findScopeFieldItemByFieldItemId(Integer namespaceId, Long communityId, Long itemId);
    ScopeFieldItem findScopeFieldItemByDisplayName(Integer namespaceId, Long communityId, String moduleName, String displayName);
    ScopeField findScopeField(Integer namespaceId, Long communityId, Long fieldId);

    List<FieldItem> listFieldItems(List<Long> fieldIds);

    void createScopeFieldGroup(ScopeFieldGroup scopeGroup);
    void updateScopeFieldGroup(ScopeFieldGroup scopeGroup);
    ScopeFieldGroup findScopeFieldGroup(Long id, Integer namespaceId, Long communityId);

    void createScopeField(ScopeField scopeField);
    void updateScopeField(ScopeField scopeField);
    ScopeField findScopeField(Long id, Integer namespaceId, Long communityId);

    void createScopeFieldItem(ScopeFieldItem scopeFieldItem);
    void updateScopeFieldItem(ScopeFieldItem scopeFieldItem);
    ScopeFieldItem findScopeFieldItem(Long id, Integer namespaceId, Long communityId);

}
