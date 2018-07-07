// @formatter:off
package com.everhomes.openapi;

import com.everhomes.server.schema.tables.pojos.EhContractTemplates;
import com.everhomes.util.StringHelper;

public class ContractTemplate extends EhContractTemplates {

	private static final long serialVersionUID = 7451788240342013832L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	public String gogsPath() {
        return this.getName() + ".txt";
    }
}