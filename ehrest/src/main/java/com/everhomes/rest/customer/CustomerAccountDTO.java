package com.everhomes.rest.customer;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>customerType: 客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId: 客户id</li>
 *     <li>bankName: 开户行名称</li>
 *     <li>branchName: 开户网点</li>
 *     <li>accountHolder: 开户人</li>
 *     <li>accountNumber: 账号</li>
 *     <li>accountNumberTypeId: 账号类型id</li>
 *     <li>accountNumberTypeName: 账号类型名称</li>
 *     <li>branchProvince: 开户行所在省</li>
 *     <li>branchCity: 开户行所在市</li>
 *     <li>accountTypeId: 账户类型id</li>
 *     <li>accountTypeName: 账户类型名称</li>
 *     <li>memo: 备注</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public class CustomerAccountDTO {
    private Long id;
    private Integer namespaceId;
    private Byte customerType;
    private Long customerId;
    private String bankName;
    private String branchName;
    private String accountHolder;
    private String accountNumber;
    private Long accountNumberTypeId;
    private String accountNumberTypeName;
    private String branchProvince;
    private String branchCity;
    private Long accountTypeId;
    private String accountTypeName;
    private Long contractId;
    private String contractName;
    private String memo;

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getAccountNumberTypeId() {
        return accountNumberTypeId;
    }

    public void setAccountNumberTypeId(Long accountNumberTypeId) {
        this.accountNumberTypeId = accountNumberTypeId;
    }

    public String getAccountNumberTypeName() {
        return accountNumberTypeName;
    }

    public void setAccountNumberTypeName(String accountNumberTypeName) {
        this.accountNumberTypeName = accountNumberTypeName;
    }

    public Long getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(Long accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public String getAccountTypeName() {
        return accountTypeName;
    }

    public void setAccountTypeName(String accountTypeName) {
        this.accountTypeName = accountTypeName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchCity() {
        return branchCity;
    }

    public void setBranchCity(String branchCity) {
        this.branchCity = branchCity;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchProvince() {
        return branchProvince;
    }

    public void setBranchProvince(String branchProvince) {
        this.branchProvince = branchProvince;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Byte getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
}
