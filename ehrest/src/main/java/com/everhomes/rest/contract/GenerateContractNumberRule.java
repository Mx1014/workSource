package com.everhomes.rest.contract;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

public class GenerateContractNumberRule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String constantVar; //常量
	private String dateVar;//时间 
	private String Sparefield;//备用字段

	public String getConstantVar() {
		return constantVar;
	}

	public void setConstantVar(String constantVar) {
		this.constantVar = constantVar;
	}

	public String getDateVar() {
		return dateVar;
	}

	public void setDateVar(String dateVar) {
		this.dateVar = dateVar;
	}

	public String getSparefield() {
		return Sparefield;
	}

	public void setSparefield(String sparefield) {
		Sparefield = sparefield;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
