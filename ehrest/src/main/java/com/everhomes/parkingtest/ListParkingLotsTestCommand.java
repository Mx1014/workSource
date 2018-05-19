// @formatter:off
package com.everhomes.parkingtest;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 字段id主键</li>
 * <li>name: 名字</li>
 * <li>age: 年龄</li>
 * <li>nextPageAnchor: 下一页锚点</li>
 * <li>pageSize: 页大小</li>
 * <li>describe: 测试练习，前端需要上送的字段在这里</li>
 * </ul>
 */

public class ListParkingLotsTestCommand {

    private Long id;
    
    private String name;
    
    private Integer age;
    
    private Long pageAnchor;

    private Integer pageSize;

    public String getName() {
		return name;
	}


	public Long getPageAnchor() {
		return pageAnchor;
	}


	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}


	public void setName(String name) {
		this.name = name;
	}


	public ListParkingLotsTestCommand() {
    }


    public Integer getAge() {
		return age;
	}


	public void setAge(Integer age) {
		this.age = age;
	}


	public Integer getPageSize() {
		return pageSize;
	}


	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
