package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * 年度水电用量收支对比表
 * <li>dateStr: 日期字符串格式 201606</li>
 * <li>waterReceivableAmount: 水表应收数量</li>
 * <li>waterPayableAmount: 水表应付量</li>
 * <li>waterBurdenAmount: 水表负担数量</li>
 * <li>waterAverageAmount: 水表每平负担数量</li>
 * <li>waterReceivableCost: 水表应收费用</li>
 * <li>waterPayableCost: 水表应付费用</li>
 * <li>waterBurdenCost: 水表实际负担费用</li>
 * <li>electricReceivableAmount: 电表应收数量</li>
 * <li>electricPayableAmount: 电表应付量</li>
 * <li>electricBurdenAmount: 电表负担数量</li>
 * <li>electricAverageAmount: 电表每平负担数量</li>
 * <li>electricReceivableCost: 电表应收费用</li>
 * <li>electricPayableCost: 电表应付费用</li>
 * <li>electricBurdenCost: 电表实际负担费用</li>
 * </ul>
 */
public class EnergyStatByYearDTO {

	private String dateStr;
	private BigDecimal waterReceivableAmount;
	private BigDecimal waterPayableAmount;
	private BigDecimal waterBurdenAmount;
	private BigDecimal waterAverageAmount;

	private BigDecimal waterReceivableCost;
	private BigDecimal waterPayableCost;
	private BigDecimal waterBurdenCost;

	private BigDecimal electricReceivableAmount;
	private BigDecimal electricPayableAmount;
	private BigDecimal electricBurdenAmount;
	private BigDecimal electricAverageAmount;

	private BigDecimal electricReceivableCost;
	private BigDecimal electricPayableCost;
	private BigDecimal electricBurdenCost;

	public EnergyStatByYearDTO() {
		this.waterReceivableAmount = new BigDecimal(0);
		this.waterPayableAmount = new BigDecimal(0);
		this.waterBurdenAmount = new BigDecimal(0);
		this.waterAverageAmount = new BigDecimal(0);
		this.waterReceivableCost = new BigDecimal(0);
		this.waterPayableCost = new BigDecimal(0);
		this.waterBurdenCost = new BigDecimal(0);
		this.electricReceivableAmount = new BigDecimal(0);
		this.electricPayableAmount = new BigDecimal(0);
		this.electricBurdenAmount = new BigDecimal(0);
		this.electricAverageAmount = new BigDecimal(0);
		this.electricReceivableCost = new BigDecimal(0);
		this.electricPayableCost = new BigDecimal(0);
		this.electricBurdenCost = new BigDecimal(0);
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public BigDecimal getWaterReceivableAmount() {
		return waterReceivableAmount;
	}

	public void setWaterReceivableAmount(BigDecimal waterReceivableAmount) {
		this.waterReceivableAmount = waterReceivableAmount;
	}

	public BigDecimal getWaterPayableAmount() {
		return waterPayableAmount;
	}

	public void setWaterPayableAmount(BigDecimal waterPayableAmount) {
		this.waterPayableAmount = waterPayableAmount;
	}

	public BigDecimal getWaterBurdenAmount() {
		return waterBurdenAmount;
	}

	public void setWaterBurdenAmount(BigDecimal waterBurdenAmount) {
		this.waterBurdenAmount = waterBurdenAmount;
	}

	public BigDecimal getWaterReceivableCost() {
		return waterReceivableCost;
	}

	public void setWaterReceivableCost(BigDecimal waterReceivableCost) {
		this.waterReceivableCost = waterReceivableCost;
	}

	public BigDecimal getWaterPayableCost() {
		return waterPayableCost;
	}

	public void setWaterPayableCost(BigDecimal waterPayableCost) {
		this.waterPayableCost = waterPayableCost;
	}

	public BigDecimal getWaterBurdenCost() {
		return waterBurdenCost;
	}

	public void setWaterBurdenCost(BigDecimal waterBurdenCost) {
		this.waterBurdenCost = waterBurdenCost;
	}

	public BigDecimal getElectricReceivableAmount() {
		return electricReceivableAmount;
	}

	public void setElectricReceivableAmount(BigDecimal electricReceivableAmount) {
		this.electricReceivableAmount = electricReceivableAmount;
	}

	public BigDecimal getElectricPayableAmount() {
		return electricPayableAmount;
	}

	public void setElectricPayableAmount(BigDecimal electricPayableAmount) {
		this.electricPayableAmount = electricPayableAmount;
	}

	public BigDecimal getElectricBurdenAmount() {
		return electricBurdenAmount;
	}

	public void setElectricBurdenAmount(BigDecimal electricBurdenAmount) {
		this.electricBurdenAmount = electricBurdenAmount;
	}

	public BigDecimal getElectricReceivableCost() {
		return electricReceivableCost;
	}

	public void setElectricReceivableCost(BigDecimal electricReceivableCost) {
		this.electricReceivableCost = electricReceivableCost;
	}

	public BigDecimal getElectricPayableCost() {
		return electricPayableCost;
	}

	public void setElectricPayableCost(BigDecimal electricPayableCost) {
		this.electricPayableCost = electricPayableCost;
	}

	public BigDecimal getElectricBurdenCost() {
		return electricBurdenCost;
	}

	public void setElectricBurdenCost(BigDecimal electricBurdenCost) {
		this.electricBurdenCost = electricBurdenCost;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public BigDecimal getWaterAverageAmount() {
		return waterAverageAmount;
	}

	public void setWaterAverageAmount(BigDecimal waterAverageAmount) {
		this.waterAverageAmount = waterAverageAmount;
	}

	public BigDecimal getElectricAverageAmount() {
		return electricAverageAmount;
	}

	public void setElectricAverageAmount(BigDecimal electricAverageAmount) {
		this.electricAverageAmount = electricAverageAmount;
	}
}
