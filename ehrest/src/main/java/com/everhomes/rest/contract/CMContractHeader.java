package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

//瑞安合同表头 黄鹏宇 2018年8月21日
public class CMContractHeader {

    private String RentalID;

    private String RentalType;

    private String PropertyID;

    private String DebtorID;

    private String DebtorAcct;

    private String AccountID;

    private String OA_AccountID;

    private String AccountName;

    private String Connector;

    private String ConnectorPhone;

    private String ContractNo;

    private String Mail;

    private String MoveinTime;

    private String GFA;

    private String NFA;

    private String LFA;

    private String MailingAddress;

    private String StartDate;

    private String EndDate;

    private String StampingDate;

    private String TerminateDate;

    private String ContractAmt;

    private String Recordstatus;
    
    private String SignDate;

    private String ModifyDate;
    
    private String CreateUserName;

    public String getCreateUserName() {
		return CreateUserName;
	}

	public void setCreateUserName(String createUserName) {
		CreateUserName = createUserName;
	}

	public String getOA_AccountID() {
        return OA_AccountID;
    }

    public void setOA_AccountID(String OA_AccountID) {
        this.OA_AccountID = OA_AccountID;
    }

    public String getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(String modifyDate) {
        ModifyDate = modifyDate;
    }

    public String getSignDate() {
		return SignDate;
	}

	public void setSignDate(String signDate) {
		SignDate = signDate;
	}

	public String getRentalID() {
        return RentalID;
    }

    public void setRentalID(String rentalID) {
        RentalID = rentalID;
    }

    public String getRentalType() {
        return RentalType;
    }

    public void setRentalType(String rentalType) {
        RentalType = rentalType;
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

    public String getDebtorAcct() {
        return DebtorAcct;
    }

    public void setDebtorAcct(String debtorAcct) {
        DebtorAcct = debtorAcct;
    }

    public String getAccountID() {
        return AccountID;
    }

    public void setAccountID(String accountID) {
        AccountID = accountID;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getConnector() {
        return Connector;
    }

    public void setConnector(String connector) {
        Connector = connector;
    }

    public String getConnectorPhone() {
        return ConnectorPhone;
    }

    public void setConnectorPhone(String connectorPhone) {
        ConnectorPhone = connectorPhone;
    }

    public String getContractNo() {
        return ContractNo;
    }

    public void setContractNo(String contractNo) {
        ContractNo = contractNo;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public String getMoveinTime() {
        return MoveinTime;
    }

    public void setMoveinTime(String moveinTime) {
        MoveinTime = moveinTime;
    }

    public String getGFA() {
        return GFA;
    }

    public void setGFA(String GFA) {
        this.GFA = GFA;
    }

    public String getNFA() {
        return NFA;
    }

    public void setNFA(String NFA) {
        this.NFA = NFA;
    }

    public String getLFA() {
        return LFA;
    }

    public void setLFA(String LFA) {
        this.LFA = LFA;
    }

    public String getMailingAddress() {
        return MailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        MailingAddress = mailingAddress;
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

    public String getStampingDate() {
        return StampingDate;
    }

    public void setStampingDate(String stampingDate) {
        StampingDate = stampingDate;
    }

    public String getTerminateDate() {
        return TerminateDate;
    }

    public void setTerminateDate(String terminateDate) {
        TerminateDate = terminateDate;
    }

    public String getContractAmt() {
        return ContractAmt;
    }

    public void setContractAmt(String contractAmt) {
        ContractAmt = contractAmt;
    }

    public String getRecordstatus() {
        return Recordstatus;
    }

    public void setRecordstatus(String recordstatus) {
        Recordstatus = recordstatus;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
