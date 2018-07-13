//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;
/**
 * @author created by yangcx
 * @date 2018年5月22日----上午10:57:58
 */
/**
 *<ul>
 * <li>listBillsDTOS: 已出账单的集合，参考{@link ListBillsDTOForEnt}</li>
 * <li>nextPageAnchor: 下一次锚点</li>
 *</ul>
 */
public class ListBillsResponseForEnt {
    @ItemType(ListBillsDTOForEnt.class)
    private List<ListBillsDTOForEnt> listBillsDTOS;
    private Long nextPageAnchor;

    public ListBillsResponseForEnt() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

	public List<ListBillsDTOForEnt> getListBillsDTOS() {
		return listBillsDTOS;
	}

	public void setListBillsDTOS(List<ListBillsDTOForEnt> listBillsDTOS) {
		this.listBillsDTOS = listBillsDTOS;
	}
    
}
