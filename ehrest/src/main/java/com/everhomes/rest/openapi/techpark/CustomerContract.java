package com.everhomes.rest.openapi.techpark;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class CustomerContract {
    private String contractNumber;
    private String contractEndDate;
    @ItemType(CustomerContractBuilding.class)
    private List<CustomerContractBuilding> buildings;
    private Boolean dealed;

    public Boolean getDealed() {
		return dealed;
	}

	public void setDealed(Boolean dealed) {
		this.dealed = dealed;
	}

	public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(String contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public List<CustomerContractBuilding> getBuildings() {
		return buildings;
	}

	public void setBuildings(List<CustomerContractBuilding> buildings) {
		this.buildings = buildings;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
