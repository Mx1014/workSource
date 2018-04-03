// @formatter:off
package com.everhomes.salary;

import com.everhomes.server.schema.tables.pojos.EhSalaryGroupEntities;
import com.everhomes.util.StringHelper;

public class SalaryGroupEntity extends EhSalaryGroupEntities {
	
	private static final long serialVersionUID = -4759610341287325926L;

	private Byte defaultFlag;

	public SalaryGroupEntity(){
	}

	public SalaryGroupEntity(String name){
		this.setName(name);
	}

	public Byte getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(Byte defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}