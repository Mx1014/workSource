package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.util.StringHelper;
import com.sun.javafx.image.impl.General;

import java.util.List;

public class ExportArchivesEmployeesDTO {

    @ItemType(GeneralFormFieldDTO.class)
    private List<GeneralFormFieldDTO> fields;

    public ExportArchivesEmployeesDTO() {
    }

    public List<GeneralFormFieldDTO> getFields() {
        return fields;
    }

    public void setFields(List<GeneralFormFieldDTO> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
