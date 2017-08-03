package com.everhomes.varField;

import com.everhomes.rest.varField.FieldDTO;
import com.everhomes.rest.varField.FieldGroupDTO;
import com.everhomes.rest.varField.ListFieldCommand;
import com.everhomes.rest.varField.ListFieldGroupCommand;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by ying.xiong on 2017/8/3.
 */
public class FieldServiceImpl implements FieldService {

    @Autowired
    private FieldProvider fieldProvider;
    @Override
    public List<FieldDTO> listFields(ListFieldCommand cmd) {
        return null;
    }

    @Override
    public List<FieldGroupDTO> listFieldGroups(ListFieldGroupCommand cmd) {

        return null;
    }
}
