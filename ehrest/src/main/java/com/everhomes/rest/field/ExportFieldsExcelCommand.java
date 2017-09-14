//@formatter:off
package com.everhomes.rest.field;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/13.
 */

public class ExportFieldsExcelCommand {
    private String moduleName;

    private Integer namespaceId;
    private Long customerId;

    private Byte customerType;
    private List<String> includedParentSheetNames;

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

    public List<String> getIncludedParentSheetNames() {
        return includedParentSheetNames;
    }

    public void setIncludedParentSheetNames(List<String> includedParentSheetNames) {
        this.includedParentSheetNames = includedParentSheetNames;
    }
}
