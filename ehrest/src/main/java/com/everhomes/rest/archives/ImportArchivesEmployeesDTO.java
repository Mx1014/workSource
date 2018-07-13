package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

public class ImportArchivesEmployeesDTO {

    @ItemType(String.class)
    private List<String> values;

    public ImportArchivesEmployeesDTO() {
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
