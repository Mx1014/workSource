// @formatter:off
package com.everhomes.parkingtest;

import com.everhomes.util.StringHelper;

/**
 * 在这里继承创建的表
 * @author dingjianmin
 *
 */

public class ParkingLotTest /*extends EhTestInfos*/ {
    private static final long serialVersionUID = 4551895615872424333L;

    public ParkingLotTest() {
    }

    private Long id;
    private Integer age;
    private String name;


	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Integer getAge() {
		return age;
	}



	public void setAge(Integer age) {
		this.age = age;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
