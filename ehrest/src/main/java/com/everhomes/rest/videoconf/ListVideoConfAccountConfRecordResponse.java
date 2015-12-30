package com.everhomes.rest.videoconf;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>confCount：开会总次数</li>
 * <li>confTimeCount：开会总时长</li>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>confRecords: 开会记录信息，参考{@link com.everhomes.rest.videoconf.ConfRecordDTO}</li>
 * </ul>
 */
public class ListVideoConfAccountConfRecordResponse {
	
	private Integer confCount;
	
	private Long confTimeCount;
	
	private Long nextPageAnchor;
	
	@ItemType(ConfRecordDTO.class)
	private List<ConfRecordDTO> confRecords;
	

	public Integer getConfCount() {
		return confCount;
	}


	public void setConfCount(Integer confCount) {
		this.confCount = confCount;
	}


	public Long getConfTimeCount() {
		return confTimeCount;
	}


	public void setConfTimeCount(Long confTimeCount) {
		this.confTimeCount = confTimeCount;
	}


	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}


	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}


	public List<ConfRecordDTO> getConfRecords() {
		return confRecords;
	}


	public void setConfRecords(List<ConfRecordDTO> confRecords) {
		this.confRecords = confRecords;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
