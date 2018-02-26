package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Date;
import java.util.List;

/**
 * <ul>
 * <li>contactName: 员工姓名</li>
 * <li>checkInTime: 入职时间</li>
 * <li>dismissTime: 离职时间</li>
 * <li>idNumber: 身份证号</li>
 * <li>salaryCardNumber: 工资卡号</li>
 * <li>salaryCardBank: 工资银行</li>
 * <li>categories:类型 {@link com.everhomes.rest.salary.EmployeeCategoryDTO}</li>
 * </ul>
 */
public class GetEmployeeEntitiesResponse {
    private String contactName;
    private Date checkInTime;
    private Date dismissTime;
    private String idNumber;
    private String salaryCardNumber;
    private String salaryCardBank;

    @ItemType(EmployeeCategoryDTO.class)
    private List<EmployeeCategoryDTO> categories;


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<EmployeeCategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<EmployeeCategoryDTO> categories) {
        this.categories = categories;
    }

    public Date getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Date getDismissTime() {
        return dismissTime;
    }

    public void setDismissTime(Date dismissTime) {
        this.dismissTime = dismissTime;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getSalaryCardBank() {
        return salaryCardBank;
    }

    public void setSalaryCardBank(String salaryCardBank) {
        this.salaryCardBank = salaryCardBank;
    }

    public String getSalaryCardNumber() {
        return salaryCardNumber;
    }

    public void setSalaryCardNumber(String salaryCardNumber) {
        this.salaryCardNumber = salaryCardNumber;
    }
}

