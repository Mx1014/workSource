package com.everhomes.rest.statistics.terminal;



/**
 *<ul>
 *<li>name:名字<li>
 *<li>rate:占比<li>
 *<li>amount:总数量<li>
 *</ul>
 */
public class PieChartData {

	private String name;

	private Double rate;

	private Long amount;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}
}
