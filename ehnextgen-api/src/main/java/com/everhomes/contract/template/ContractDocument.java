package com.everhomes.contract.template;

import com.everhomes.server.schema.tables.pojos.EhContractDocuments;
import com.everhomes.util.StringHelper;

public class ContractDocument extends EhContractDocuments{
	
	private static final long serialVersionUID = 1L;

	public String gogsPath() {
        return this.getName() + "_" + this.getContractNumber() +".txt";
    }
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
