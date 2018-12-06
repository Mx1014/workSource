package com.everhomes.contract.template;

import com.everhomes.openapi.Contract;

public interface ContractTemplateStrategy {
	
	/**
	 * 确认关键字是否合法
	 * @param contract
	 * @param segments
	 * @return
	 */
	boolean isValid(Contract contract,String[] segments);
	
	/**
	 * 通过相应的关键字获取值
	 * @param contract
	 * @param segments
	 * @return
	 */
	String getValue(Contract contract,String[] segments);
}
