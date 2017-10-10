package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.util.StringHelper;
import com.sun.javafx.image.impl.General;

import java.util.List;

public class ExportArchivesEmployeesDTO {

    @ItemType(String.class)
    private List<String> titles;

    @ItemType(String.class)
    private List<String> vals;

    public ExportArchivesEmployeesDTO() {
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<String> getVals() {
        return vals;
    }

    public void setVals(List<String> vals) {
        this.vals = vals;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
