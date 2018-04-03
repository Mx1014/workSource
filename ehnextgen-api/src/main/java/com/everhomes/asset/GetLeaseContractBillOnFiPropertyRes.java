//@formatter:off
package com.everhomes.asset;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/11/22.
 */

public class GetLeaseContractBillOnFiPropertyRes extends AssetZjhResponse {
    private List<GetLeaseContractBillOnFiPropertyData> data;

    public List<GetLeaseContractBillOnFiPropertyData> getData() {
        return data;
    }

    public void setData(List<GetLeaseContractBillOnFiPropertyData> data) {
        this.data = data;
    }
}
