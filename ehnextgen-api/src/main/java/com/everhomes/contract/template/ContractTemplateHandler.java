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
	
	/**
	 * 确认关键字是否合法
	 * 该方法是为了兼容调租和免租这种情况，它们使用的其实是同一个pojo类，但是在业务上又单独分开展示的
	 * @param contract
	 * @param segments
	 * @return
	 */
	boolean isValid(ContractDetailDTO contract,String[] segments,String type);
	
	/**
	 * 通过相应的关键字获取值
	 * 该方法是为了兼容调租和免租这种情况，它们使用的其实是同一个pojo类，但是在业务上又单独分开展示的
	 * @param contract
	 * @param segments
	 * @return
	 */
	String getValue(ContractDetailDTO contract,String[] segments,String type);
}
