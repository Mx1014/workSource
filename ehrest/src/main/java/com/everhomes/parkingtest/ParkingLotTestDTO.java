// @formatter:off
package com.everhomes.parkingtest;

import com.everhomes.util.StringHelper;

/**
 * 返回结果参数列表，返回的字段在这里定义
/ul>
 */
public class ParkingLotTestDTO {
    private Long id;
    private String name;
    private Integer age;
    
    private Long nextPageAnchor;

    public Long getNextPageAnchor() {
		return nextPageAnchor;
	}


	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Integer getAge() {
		return age;
	}


	public void setAge(Integer age) {
		this.age = age;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
