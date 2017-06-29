package com.everhomes.rest.salary;

import java.util.List;
import java.util.Map;

public class ImportSalaryEmployeeOriginValDTO {

//    private List<SalaryGroup>
//    private List<Map<String,String>> salaryEmployeeVal;

    private List<String> salaryEmployeeVal;

    public ImportSalaryEmployeeOriginValDTO() {
    }

/*    public List<Map<String, String>> getSalaryEmployeeVal() {
        return salaryEmployeeVal;
    }

    public void setSalaryEmployeeVal(List<Map<String, String>> salaryEmployeeVal) {
        this.salaryEmployeeVal = salaryEmployeeVal;
    }*/

    public List<String> getSalaryEmployeeVal() {
        return salaryEmployeeVal;
    }

    public void setSalaryEmployeeVal(List<String> salaryEmployeeVal) {
        this.salaryEmployeeVal = salaryEmployeeVal;
    }
}
