package com.everhomes.parkingtest;

import javax.validation.constraints.NotNull;

/**
 * @author sw on 2017/8/23.
 */
public class ParkingRechargeTestConfig {

   
    private Long id;
    
    private String name;

	private Integer age;

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
	

    
}
