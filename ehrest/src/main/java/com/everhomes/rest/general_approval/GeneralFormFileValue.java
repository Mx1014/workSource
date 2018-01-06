package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.flow.FlowCaseFileDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

public class GeneralFormFileValue {

    @ItemType(GeneralFormFileValueDTO.class)
    private List<GeneralFormFileValueDTO> files;

    public List<GeneralFormFileValueDTO> getFiles() {
        return files;
    }

    public void setFiles(List<GeneralFormFileValueDTO> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
