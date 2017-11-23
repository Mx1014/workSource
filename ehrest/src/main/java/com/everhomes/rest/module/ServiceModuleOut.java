package com.everhomes.rest.module;

/**
 * <p>
 * 业务模块輸出对象
 * </p>
 * <ul>
 * <li>id: 模块id</li>
 * <li>name: 模块名称</li>
 */

public class ServiceModuleOut {
	private Long id;

	private String name;

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

}
