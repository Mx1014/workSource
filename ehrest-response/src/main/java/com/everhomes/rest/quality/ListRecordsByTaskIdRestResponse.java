// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.quality;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.quality.QualityInspectionTaskRecordsDTO;

public class ListRecordsByTaskIdRestResponse extends RestResponseBase {

    private List<QualityInspectionTaskRecordsDTO> response;

    public ListRecordsByTaskIdRestResponse () {
    }

    public List<QualityInspectionTaskRecordsDTO> getResponse() {
        return response;
    }

    public void setResponse(List<QualityInspectionTaskRecordsDTO> response) {
        this.response = response;
    }
}
