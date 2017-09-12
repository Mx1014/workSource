//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>billGroupName:账单组名称</li>
 * <li>showBillForClientDTOList:账单概况集合，参考{@link com.everhomes.rest.asset.ShowBillForClientDTO}</li>
 *</ul>
 */
public class ShowBillGroupForClientResponse {
    private String billGroupName;
    @ItemType(ShowBillForClientDTO.class)
    private List<ShowBillForClientDTO> showBillForClientDTOList;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<ShowBillForClientDTO> getShowBillForClientDTOList() {
        return showBillForClientDTOList;
    }

    public void setShowBillForClientDTOList(List<ShowBillForClientDTO> showBillForClientDTOList) {
        this.showBillForClientDTOList = showBillForClientDTOList;
    }

    public String getBillGroupName() {
        return billGroupName;
    }

    public void setBillGroupName(String billGroupName) {
        this.billGroupName = billGroupName;
    }

    public ShowBillGroupForClientResponse() {

    }
}
