//@formatter:off
package com.everhomes.asset;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/11/24.
 */

public class GetLeaseContractReceivableGroupForStatisticsRes extends AssetZjhResponse{
    private List<GetLeaseContractReceivableGroupForStatisticsData> data;

    public List<GetLeaseContractReceivableGroupForStatisticsData> getData() {
        return data;
    }

    public void setData(List<GetLeaseContractReceivableGroupForStatisticsData> data) {
        this.data = data;
    }
}
