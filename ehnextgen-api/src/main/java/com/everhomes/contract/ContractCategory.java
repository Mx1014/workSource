package com.everhomes.contract;

import com.everhomes.server.schema.tables.pojos.EhContractCategories;
import com.everhomes.util.StringHelper;

public class ContractCategory extends EhContractCategories  {

	/**
	 * EhContractCategories
	 */
	private static final long serialVersionUID = 1796117121187200850L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
