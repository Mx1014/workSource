package com.everhomes.varField;


import com.everhomes.rest.asset.ImportFieldsExcelResponse;
import com.everhomes.rest.dynamicExcel.DynamicImportResponse;
import com.everhomes.rest.field.ExportFieldsExcelCommand;
import com.everhomes.rest.varField.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by ying.xiong on 2017/8/3.
 */
public interface FieldService {

    List<FieldDTO> listFields(ListFieldCommand cmd);
    List<FieldGroupDTO> listFieldGroups(ListFieldGroupCommand cmd);

    List<FieldDTO> listActiveNoUsedItem(ListActiveNoUsedItemCommand cmd);

    List<FieldItemDTO> listFieldItems(ListFieldItemCommand cmd);
    List<FieldItemDTO> listScopeFieldItems(ListScopeFieldItemCommand cmd);

    /**
     * 更新动态组、字段、选项的方式：
     * 1、查出所有符合的map列表
     * 2、处理 没有id的增加，有的在数据库中查询找到则更新,且在列表中去掉对应的，没找到则增加
     * 3、将map列表中剩下的置为inactive
     * @param cmd
     */
    void updateFields(UpdateFieldsCommand cmd);
    void updateFieldGroups(UpdateFieldGroupsCommand cmd);
    void updateFieldItems(UpdateFieldItemsCommand cmd);



    ImportFieldsExcelResponse importFieldsExcel(ImportFieldExcelCommand cmd, MultipartFile file);


    void exportFieldsExcel(ExportFieldsExcelCommand cmd, HttpServletResponse response);

    void exportExcelTemplate(ListFieldGroupCommand cmd,HttpServletResponse response);
    List<FieldGroupDTO> getAllGroups(ExportFieldsExcelCommand cmd,boolean onlyLeaf, boolean filter);



    ScopeFieldItem findScopeFieldItemByFieldItemId(Integer namespaceId,Long ownerId, Long communityId, Long itemId);
    ScopeFieldItem findScopeFieldItemByDisplayName(Integer namespaceId,Long ownerId, Long communityId, String moduleName, String displayName);

    List<SystemFieldGroupDTO> listSystemFieldGroups(ListSystemFieldGroupCommand cmd);
    List<SystemFieldDTO> listSystemFields(ListSystemFieldCommand cmd);
    List<SystemFieldItemDTO> listSystemFieldItems(ListSystemFieldItemCommand cmd);


    void exportDynamicExcelTemplate(ListFieldGroupCommand cmd, HttpServletResponse response);

    void exportDynamicExcel(ExportFieldsExcelCommand cmd, HttpServletResponse response);

    List<List<String>> getDataOnFields(FieldGroupDTO group, Long customerId, Byte customerType,List<FieldDTO> fields,Long communityId,Integer namespaceId,String moduleName, Long orgId, Object params);

    DynamicImportResponse importDynamicExcel(ImportFieldExcelCommand cmd, MultipartFile file);

    void createDynamicScopeItems(Integer namespaceId, String instanceConfig, String appName);
    ScopeFieldItem findScopeFieldItemByDisplayNameAndFieldId(Integer namespaceId,Long ownerId, Long communityId, String moduleName, String displayName, Long fieldId);


    void saveFieldScopeFilter(SaveFieldScopeFilterCommand cmd);

    List<FieldDTO> listFieldScopeFilter(ListFieldScopeFilterCommand cmd);
}
