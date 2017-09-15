package com.everhomes.varField;

import com.everhomes.rest.varField.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by ying.xiong on 2017/8/3.
 */
public interface FieldService {

    List<FieldDTO> listFields(ListFieldCommand cmd);
    List<FieldGroupDTO> listFieldGroups(ListFieldGroupCommand cmd);
    List<FieldItemDTO> listFieldItems(ListFieldItemCommand cmd);


}
