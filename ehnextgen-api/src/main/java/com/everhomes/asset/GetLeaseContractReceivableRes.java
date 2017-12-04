//@formatter:off
package com.everhomes.asset;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/11/23.
 */

public class GetLeaseContractReceivableRes extends AssetZjhResponse {
    private List<GetLeaseContractReceivableData> data;

    public List<GetLeaseContractReceivableData> getData() {
        return data;
    }

    public void setData(List<GetLeaseContractReceivableData> data) {
        this.data = data;
    }
}
