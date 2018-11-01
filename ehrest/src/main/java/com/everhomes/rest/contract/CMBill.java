package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CMBill {

    private String PropertyID;
    
    private String BillScheduleID;//定期账单ID（唯一标识）

    private String DebtorID;

    private String RentalID;

    private String BillID;

    private String BillType;

    private String BillItemName;

    private String DocumentDate;

    private String StartDate;

    private String EndDate;

    private String Status;

    private String DocumentAmt;
    
    private String ChargeAmt;
    
    private String TaxAmt;
    
    private String TaxRate;

    private String BalanceAmt;

    private String ModifyDate;

    public String getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(String modifyDate) {
        ModifyDate = modifyDate;
    }

    public String getPropertyID() {
        return PropertyID;
    }

    public void setPropertyID(String propertyID) {
        PropertyID = propertyID;
    }

    public String getDebtorID() {
        return DebtorID;
    }

    public void setDebtorID(String debtorID) {
        DebtorID = debtorID;
    }

    public String getRentalID() {
        return RentalID;
    }

    public void setRentalID(String rentalID) {
        RentalID = rentalID;
    }

    public String getBillID() {
        return BillID;
    }

    public void setBillID(String billID) {
        BillID = billID;
    }

    public String getBillType() {
        return BillType;
    }

    public void setBillType(String billType) {
        BillType = billType;
    }

    public String getBillItemName() {
        return BillItemName;
    }

    public void setBillItemName(String billItemName) {
        BillItemName = billItemName;
    }

    public String getDocumentDate() {
        return DocumentDate;
    }

    public void setDocumentDate(String documentDate) {
        DocumentDate = documentDate;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDocumentAmt() {
        return DocumentAmt;
    }

    public void setDocumentAmt(String documentAmt) {
        DocumentAmt = documentAmt;
    }

    public String getBalanceAmt() {
        return BalanceAmt;
    }

    public void setBalanceAmt(String balanceAmt) {
        BalanceAmt = balanceAmt;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getBillScheduleID() {
		return BillScheduleID;
	}

	public void setBillScheduleID(String billScheduleID) {
		BillScheduleID = billScheduleID;
	}

	public String getChargeAmt() {
		return ChargeAmt;
	}

	public void setChargeAmt(String chargeAmt) {
		ChargeAmt = chargeAmt;
	}

	public String getTaxAmt() {
		return TaxAmt;
	}

	public void setTaxAmt(String taxAmt) {
		TaxAmt = taxAmt;
	}

	public String getTaxRate() {
		return TaxRate;
	}

	public void setTaxRate(String taxRate) {
		TaxRate = taxRate;
	}
}
