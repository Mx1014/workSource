package com.everhomes.varField;

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
    List<FieldItemDTO> listFieldItems(ListFieldItemCommand cmd);

    void exportFieldsExcel(ExportFieldsExcelCommand cmd, HttpServletResponse response);
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

    void importFieldsExcel(ImportFieldExcelCommand cmd, MultipartFile file);

    ScopeFieldItem findScopeFieldItemByFieldItemId(Integer namespaceId, Long communityId, Long itemId);




    void exportExcelTemplate(ListFieldGroupCommand cmd,HttpServletResponse response);
    ScopeFieldItem findScopeFieldItemByDisplayName(Integer namespaceId, Long communityId, String moduleName, String displayName);

    List<SystemFieldGroupDTO> listSystemFieldGroups(ListSystemFieldGroupCommand cmd);
    List<SystemFieldDTO> listSystemFields(ListSystemFieldCommand cmd);
    List<SystemFieldItemDTO> listSystemFieldItems(ListSystemFieldItemCommand cmd);


}
