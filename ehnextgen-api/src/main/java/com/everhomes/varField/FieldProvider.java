package com.everhomes.varField;

import java.util.List;

/**
 * Created by ying.xiong on 2017/8/3.
 */
public interface FieldProvider {
    List<ScopeFieldGroup> listScopeFieldGroups(Integer namespaceId, String moduleName);
    List<FieldGroup> listFieldGroups(List<Long> ids);
    List<FieldGroup> listFieldGroups(String moduleName);
    List<ScopeField> listScopeFields(Integer namespaceId, String moduleName, String groupPath);
    List<Field> listFields(List<Long> ids);
    List<Field> listFields(String moduleName, String groupPath);
    List<FieldItem> listFieldItems(List<Long> fieldIds);
    List<ScopeFieldItem> listScopeFieldItems(List<Long> fieldIds);
    ScopeFieldItem findScopeFieldItemByFieldItemId(Long itemId);
}
