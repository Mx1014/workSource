package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>namespaceId</li>
 * <li>companyName</li>
 * </ul>
 */
public class ListDecorationCompaniesCommand {

    private Integer namespaceId;
    private String companyName;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
