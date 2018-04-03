package com.everhomes.rest.yellowPage;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.user.RequestFieldDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>dtos: 申请内容 参考{@link com.everhomes.rest.user.RequestFieldDTO}</li>
 *  <li>createTime: 申请时间</li>
 * </ul>
 *
 */
public class GetRequestInfoResponse {
	
	@ItemType(RequestFieldDTO.class)
	private List<RequestFieldDTO> dtos;
	
	private Timestamp createTime;

	public List<RequestFieldDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<RequestFieldDTO> dtos) {
		this.dtos = dtos;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
