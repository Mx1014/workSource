package com.everhomes.rest.community;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListApartmentsInCommunityResponse {
	
	private Integer totalApartmentNumber;
	private Integer totalRelatedContractNumber;
	private Double totalAreaSize;
	private Double totalRentArea;
	private Double totalFreeArea;
	private Double totalChargeArea;
	private Double totalRent;
	private Double totalAreaAveragePrice;
	@ItemType(ApartmentInfoDTO.class)
	private List<ApartmentInfoDTO> apartments;
	private Long nextPageAnchor;
	
	public Integer getTotalRelatedContractNumber() {
		return totalRelatedContractNumber;
	}
	public void setTotalRelatedContractNumber(Integer totalRelatedContractNumber) {
		this.totalRelatedContractNumber = totalRelatedContractNumber;
	}
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public Double getTotalRent() {
		return totalRent;
	}
	public void setTotalRent(Double totalRent) {
		this.totalRent = totalRent;
	}
	public Integer getTotalApartmentNumber() {
		return totalApartmentNumber;
	}
	public void setTotalApartmentNumber(Integer totalApartmentNumber) {
		this.totalApartmentNumber = totalApartmentNumber;
	}
	public Double getTotalAreaSize() {
		return totalAreaSize;
	}
	public void setTotalAreaSize(Double totalAreaSize) {
		this.totalAreaSize = totalAreaSize;
	}
	public Double getTotalRentArea() {
		return totalRentArea;
	}
	public void setTotalRentArea(Double totalRentArea) {
		this.totalRentArea = totalRentArea;
	}
	public Double getTotalFreeArea() {
		return totalFreeArea;
	}
	public void setTotalFreeArea(Double totalFreeArea) {
		this.totalFreeArea = totalFreeArea;
	}
	public Double getTotalChargeArea() {
		return totalChargeArea;
	}
	public void setTotalChargeArea(Double totalChargeArea) {
		this.totalChargeArea = totalChargeArea;
	}
	public Double getTotalAreaAveragePrice() {
		return totalAreaAveragePrice;
	}
	public void setTotalAreaAveragePrice(Double totalAreaAveragePrice) {
		this.totalAreaAveragePrice = totalAreaAveragePrice;
	}
	public List<ApartmentInfoDTO> getApartments() {
		return apartments;
	}
	public void setApartments(List<ApartmentInfoDTO> apartments) {
		this.apartments = apartments;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
