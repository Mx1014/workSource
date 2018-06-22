//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author created by ycx
 * @date 下午3:30:35
 */

/**
 *<ul>
 * <li>billIdList:账单id列表</li>
 * <li>billGroupId:账单组id</li>
 * <li>subItemDTOList:减免费项的集合，参考{@link com.everhomes.rest.asset.SubItemDTO}</li>
 *</ul>
 */
public class BatchModifyNotSettledBillCommand {
    private List<Long> billIdList;
    private Long billGroupId;
    @ItemType(SubItemDTO.class)
    private List<SubItemDTO> subItemDTOList;
    
	public List<Long> getBillIdList() {
		return billIdList;
	}
	public void setBillIdList(List<Long> billIdList) {
		this.billIdList = billIdList;
	}
	public Long getBillGroupId() {
		return billGroupId;
	}
	public void setBillGroupId(Long billGroupId) {
		this.billGroupId = billGroupId;
	}
	public List<SubItemDTO> getSubItemDTOList() {
		return subItemDTOList;
	}
	public void setSubItemDTOList(List<SubItemDTO> subItemDTOList) {
		this.subItemDTOList = subItemDTOList;
	}
}
