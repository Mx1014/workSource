package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

import java.util.List;


public class CMDataObject {

    private CMContractHeader ContractHeader;

    private List<CMContractUnit> ContractUnit;

    private List<CMContractFee> ContractFee;

    private List<CMRentalOption> RentalOption;

    private List<CMInsurance> Insurance;

    private List<CMBill> Bill;


    public CMContractHeader getContractHeader() {
        return ContractHeader;
    }

    public void setContractHeader(CMContractHeader contractHeader) {
        ContractHeader = contractHeader;
    }

    public List<CMContractUnit> getContractUnit() {
        return ContractUnit;
    }

    public void setContractUnit(List<CMContractUnit> contractUnit) {
        ContractUnit = contractUnit;
    }

    public List<CMContractFee> getContractFee() {
        return ContractFee;
    }

    public void setContractFee(List<CMContractFee> contractFee) {
        ContractFee = contractFee;
    }

    public List<CMRentalOption> getRentalOption() {
        return RentalOption;
    }

    public void setRentalOption(List<CMRentalOption> rentalOption) {
        RentalOption = rentalOption;
    }

    public List<CMInsurance> getInsurance() {
        return Insurance;
    }

    public void setInsurance(List<CMInsurance> insurance) {
        Insurance = insurance;
    }

    public List<CMBill> getBill() {
        return Bill;
    }

    public void setBill(List<CMBill> bill) {
        this.Bill = bill;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
