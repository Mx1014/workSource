package com.everhomes.varField;

import java.util.List;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/8/3.
 */
public interface FieldProvider {
    Map<Long, ScopeFieldGroup> listScopeFieldGroups(Integer namespaceId, Long ownerId,Long communityId, String moduleName, Long categoryId);
    Map<Long, ScopeFieldGroup> listScopeFieldGroups(Integer namespaceId, Long communityId, String moduleName);
    List<FieldGroup> listFieldGroups(List<Long> ids);
    FieldGroup findFieldGroup(Long id);
    List<FieldGroup> listFieldGroups(String moduleName);
    List<Long> listFieldGroupRanges(String moduleName,String moduleType);
    Map<Long, ScopeField> listScopeFields(Integer namespaceId,Long ownerId, Long communityId, String moduleName, String groupPath, Long categoryId);
    List<Field> listFields(List<Long> ids);
    List<Long> listFieldRanges(String moduleName,String moduleType,String groupPath);
    List<Field> listFields(String moduleName, String groupPath);



    void saveFieldGroups(String customerType, Long customerId, List<Object> objects, String simpleName);

    String findClassNameByGroupDisplayName(String groupDisplayName);
    FieldGroup findGroupByGroupDisplayName(String groupDisplayName);
    FieldGroup findGroupByGroupLogicName(String groupLogicName);

    List<FieldItem> listFieldItems(Long fieldId);
    List<ScopeFieldItem> listScopeFieldItems(Long fieldId, Integer namespaceId, Long communityId,Long ownerId, Long categoryId);
    Map<Long, ScopeFieldItem> listScopeFieldsItems(List<Long> fieldIds,Long ownerId, Integer namespaceId, Long communityId, Long categoryId,String moduleName);
    Map<Long, ScopeFieldItem> listScopeFieldsItems(List<Long> fieldIds, Integer namespaceId, Long communityId, Long categoryId, String moduleName);
    ScopeFieldItem findScopeFieldItemByFieldItemId(Integer namespaceId,Long ownerId, Long communityId, Long itemId);
    ScopeFieldItem findScopeFieldItemByDisplayName(Integer namespaceId, Long communityId,Long ownerId, String moduleName, String displayName);
    ScopeFieldItem findScopeFieldItemByDisplayName(Integer namespaceId, Long ownerId,Long communityId, String moduleName, Long fieldId, String displayName);
    ScopeFieldItem findScopeFieldItemByBusinessValue(Integer namespaceId,Long ownerId,String ownerType, Long communityId, String moduleName, Long fieldId, Byte businessValue);
    ScopeField findScopeField(Integer namespaceId, Long communityId, Long fieldId);

    List<FieldItem> listFieldItems(List<Long> fieldIds);

    void createScopeFieldGroup(ScopeFieldGroup scopeGroup);
    void updateScopeFieldGroup(ScopeFieldGroup scopeGroup);
    ScopeFieldGroup findScopeFieldGroup(Long id, Integer namespaceId, Long communityId, Long categoryId);

    void createScopeField(ScopeField scopeField);
    void updateScopeField(ScopeField scopeField);
    ScopeField findScopeField(Long id, Integer namespaceId,Long ownerId, Long communityId, Long categoryId);

    void createScopeFieldItem(ScopeFieldItem scopeFieldItem);
    void updateScopeFieldItem(ScopeFieldItem scopeFieldItem);
    ScopeFieldItem findScopeFieldItem(Long id, Integer namespaceId, Long communityId, Long categoryId);

    void createFieldItem(FieldItem item);
    ScopeField findScopeField(Integer namespaceId, Long communityId, Long groupId, String fieldDisplayName);
    Field findField(Long groupId, String fieldName);
    Field findField(String moduleName, String name, String groupPath);
    //add by tangcen
	FieldItem findFieldItemByBusinessValue(Long fieldId, Byte businessValue);

    List<Field> listMandatoryFields(String moduleName, Long  groupId);

    List <Long>checkCustomerField(Integer namespaceId,Long ownerId, Long communityId, String moduleName);

    Field findFieldById(Long fieldId);

    FieldItem findFieldItemByItemId(Long itemId);

    void changeFilterStatus(Integer namespaceId, Long communityId, String moduleName, Long userId, String groupPath);;

    void createFieldScopeFilter(VarFieldScopeFilter filter);

    List<VarFieldScopeFilter> listFieldScopeFilter(Integer namespaceId, Long communityId, String moduleName, Long userId, String groupPath);
}
