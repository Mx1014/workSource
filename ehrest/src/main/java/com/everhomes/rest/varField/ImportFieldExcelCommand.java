//@formatter:off
package com.everhomes.rest.varField;

/**
 * Created by Wentian Wang on 2017/9/19.
 */

public class ImportFieldExcelCommand {
    private Long customerId;
    private String customerType;
    private String moduleName;
    private Integer namespaceId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
}
