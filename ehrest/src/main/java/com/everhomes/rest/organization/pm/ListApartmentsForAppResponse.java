package com.everhomes.rest.organization.pm;

import java.util.List;

import com.everhomes.util.StringHelper;

public class ListApartmentsForAppResponse {
	
	private Integer totalApartmentCount;
	private Integer freeApartmentCount;
	private Integer rentApartmentCount;
	private Integer occupiedApartmentCount;
	private Integer livingApartmentCount;
	private Integer saledApartmentCount;
	private List<ApartmentForAPPDTO> results;
	
	public Integer getTotalApartmentCount() {
		return totalApartmentCount;
	}
	public void setTotalApartmentCount(Integer totalApartmentCount) {
		this.totalApartmentCount = totalApartmentCount;
	}
	public Integer getFreeApartmentCount() {
		return freeApartmentCount;
	}
	public void setFreeApartmentCount(Integer freeApartmentCount) {
		this.freeApartmentCount = freeApartmentCount;
	}
	public Integer getRentApartmentCount() {
		return rentApartmentCount;
	}
	public void setRentApartmentCount(Integer rentApartmentCount) {
		this.rentApartmentCount = rentApartmentCount;
	}
	public Integer getOccupiedApartmentCount() {
		return occupiedApartmentCount;
	}
	public void setOccupiedApartmentCount(Integer occupiedApartmentCount) {
		this.occupiedApartmentCount = occupiedApartmentCount;
	}
	public Integer getLivingApartmentCount() {
		return livingApartmentCount;
	}
	public void setLivingApartmentCount(Integer livingApartmentCount) {
		this.livingApartmentCount = livingApartmentCount;
	}
	public Integer getSaledApartmentCount() {
		return saledApartmentCount;
	}
	public void setSaledApartmentCount(Integer saledApartmentCount) {
		this.saledApartmentCount = saledApartmentCount;
	}
	public List<ApartmentForAPPDTO> getResults() {
		return results;
	}
	public void setResults(List<ApartmentForAPPDTO> results) {
		this.results = results;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
}
