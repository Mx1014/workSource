//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;
/**
 * @author created by ycx
 * @date 下午1:49:33
 */

/**
 *<ul>
 * <li>billGroupId:账单组Id</li>
 * <li>billGroupName:账单组名称</li>
 * <li>subItemDTOList:减免费项的集合，参考{@link com.everhomes.rest.asset.SubItemDTO}</li>
 *</ul>
 */
public class ShowCreateBillSubItemListDTO {
    private Long billGroupId;
    private String billGroupName;
    @ItemType(SubItemDTO.class)
    private List<SubItemDTO> subItemDTOList;
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public String getBillGroupName() {
        return billGroupName;
    }

    public void setBillGroupName(String billGroupName) {
        this.billGroupName = billGroupName;
    }

    public ShowCreateBillSubItemListDTO() {

    }

	public List<SubItemDTO> getSubItemDTOList() {
		return subItemDTOList;
	}

	public void setSubItemDTOList(List<SubItemDTO> subItemDTOList) {
		this.subItemDTOList = subItemDTOList;
	}
}
