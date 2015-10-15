// @formatter:off
// generated at 2015-10-14 12:36:35
package com.everhomes.statistics.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.statistics.admin.ListStatisticsByDateDTO;

public class StatisticsRestResponse extends RestResponseBase {

    private List<ListStatisticsByDateDTO> response;

    public StatisticsRestResponse () {
    }

    public List<ListStatisticsByDateDTO> getResponse() {
        return response;
    }

    public void setResponse(List<ListStatisticsByDateDTO> response) {
        this.response = response;
    }
}
