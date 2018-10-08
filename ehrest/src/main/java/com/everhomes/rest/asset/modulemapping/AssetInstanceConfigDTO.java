package com.everhomes.rest.asset.modulemapping;

import java.util.List;

/**
 * @author created by ycx
 * @date 下午1:38:48
 */

public class AssetInstanceConfigDTO {
	private String url;
	private Long categoryId;
	private Long contractOriginId;
	private Byte contractChangeFlag;
	private Byte energyFlag;
	private List<PrintInstanceConfigDTO> printInstanceConfigDTOList;
	private List<RentalInstanceConfigDTO> rentalInstanceConfigDTOList;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getContractOriginId() {
		return contractOriginId;
	}
	public void setContractOriginId(Long contractOriginId) {
		this.contractOriginId = contractOriginId;
	}
	public Byte getContractChangeFlag() {
		return contractChangeFlag;
	}
	public void setContractChangeFlag(Byte contractChangeFlag) {
		this.contractChangeFlag = contractChangeFlag;
	}
	public Byte getEnergyFlag() {
		return energyFlag;
	}
	public void setEnergyFlag(Byte energyFlag) {
		this.energyFlag = energyFlag;
	}
	public List<PrintInstanceConfigDTO> getPrintInstanceConfigDTOList() {
		return printInstanceConfigDTOList;
	}
	public void setPrintInstanceConfigDTOList(List<PrintInstanceConfigDTO> printInstanceConfigDTOList) {
		this.printInstanceConfigDTOList = printInstanceConfigDTOList;
	}
	public List<RentalInstanceConfigDTO> getRentalInstanceConfigDTOList() {
		return rentalInstanceConfigDTOList;
	}
	public void setRentalInstanceConfigDTOList(List<RentalInstanceConfigDTO> rentalInstanceConfigDTOList) {
		this.rentalInstanceConfigDTOList = rentalInstanceConfigDTOList;
	}
}
