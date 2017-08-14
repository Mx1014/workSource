package com.everhomes.rest.openapi.shenzhou;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.contract.BuildingApartmentDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ying.xiong on 2017/8/14.
 */
public class ZJContract {
    private String contractNumber;
    private String name;
    private String contractAttribute;
    private String contractStartDate;
    private String contractEndDate;
    @ItemType(BuildingApartmentDTO.class)
    private List<BuildingApartmentDTO> apartments;
    private BigDecimal amount;
    private String status;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public List<BuildingApartmentDTO> getApartments() {
        return apartments;
    }

    public void setApartments(List<BuildingApartmentDTO> apartments) {
        this.apartments = apartments;
    }

    public String getContractAttribute() {
        return contractAttribute;
    }

    public void setContractAttribute(String contractAttribute) {
        this.contractAttribute = contractAttribute;
    }

    public String getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(String contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(String contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
