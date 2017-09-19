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

    void exportExcelTemplate(ListFieldGroupCommand cmd,HttpServletResponse response);

    void exportFieldsExcel(ExportFieldsExcelCommand cmd, HttpServletResponse response);

    void importFieldsExcel(ImportFieldExcelCommand cmd, MultipartFile file);
}
