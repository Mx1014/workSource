package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>id: siteid</li>
 * <li>spaceId: 空间id</li>
 * <li>rentType: 租赁类别:1-开放式（默认space_type 1）,2-办公室</li>  
 * <li>spaceType: 空间类别:1-工位,2-面积 </li>  
 * <li>size: 场所大小 - 对于工位是个数，对于面积是平米</li> 
 * </ul>
 */
public class OfficeCategoryDTO {
	private Long id;
	private Long spaceId;
    private Byte rentType;
    private Byte spaceType;
	private Integer size;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}

	public Byte getRentType() {
		return rentType;
	}

	public void setRentType(Byte rentType) {
		this.rentType = rentType;
	}

	public Byte getSpaceType() {
		return spaceType;
	}

	public void setSpaceType(Byte spaceType) {
		this.spaceType = spaceType;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
  
}
