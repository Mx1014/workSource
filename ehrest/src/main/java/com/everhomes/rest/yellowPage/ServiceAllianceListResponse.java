package com.everhomes.rest.yellowPage;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>dtos: 参考 {@link com.everhomes.rest.yellowPage.ServiceAllianceDTO}</li>
 *  <li>skipType: 只有一个企业时是否跳过列表页，0 不跳； 1 跳过</li>
 *  <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class ServiceAllianceListResponse {
	
	@ItemType(ServiceAllianceDTO.class)
	private List<ServiceAllianceDTO> dtos;
	
	private Byte skipType;
	
	private Long nextPageAnchor;

	public List<ServiceAllianceDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<ServiceAllianceDTO> dtos) {
		this.dtos = dtos;
	}

	public Byte getSkipType() {
		return skipType;
	}

	public void setSkipType(Byte skipType) {
		this.skipType = skipType;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
