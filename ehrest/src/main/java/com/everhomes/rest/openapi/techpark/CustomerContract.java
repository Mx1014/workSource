package com.everhomes.rest.openapi.techpark;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class CustomerContract {
    /** 合同编号 */
    private String contractNumber;
    
    /** 合同到期日 */
    private String contractEndDate;
    
    /** 楼栋门牌列表 */
    @ItemType(CustomerBuilding.class)
    private List<CustomerBuilding> buildings;

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

    public List<CustomerBuilding> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<CustomerBuilding> buildings) {
        this.buildings = buildings;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
