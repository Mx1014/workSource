// @formatter:off
package com.everhomes.parkingtest;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>

 */
public class SetListParkingLotsTestCommand {
	
    @NotNull
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


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
