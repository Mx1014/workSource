package com.everhomes.varField;

import com.everhomes.rest.varField.FieldDTO;
import com.everhomes.rest.varField.FieldGroupDTO;
import com.everhomes.rest.varField.ListFieldCommand;
import com.everhomes.rest.varField.ListFieldGroupCommand;

import java.util.List;

/**
 * Created by ying.xiong on 2017/8/3.
 */
public interface FieldService {

    List<FieldDTO> listFields(ListFieldCommand cmd);
    List<FieldGroupDTO> listFieldGroups(ListFieldGroupCommand cmd);
}
