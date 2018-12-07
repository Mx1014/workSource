package com.everhomes.contract.template;

import com.everhomes.rest.contract.ContractDetailDTO;

public interface ContractTemplateHandler {
	
	String CONTRACTTEMPLATE_PREFIX = "ContractTemplate-";
	
	/**
	 * 确认关键字是否合法
	 * @param contract
	 * @param segments
	 * @return
	 */
	boolean isValid(ContractDetailDTO contract,String[] segments);
	
	/**
	 * 通过相应的关键字获取值
	 * @param contract
	 * @param segments
	 * @return
	 */
	String getValue(ContractDetailDTO contract,String[] segments);
}
