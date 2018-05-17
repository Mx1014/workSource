package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>namespaceId</li>
 * <li>companyName</li>
 * </ul>
 */
public class ListDecorationCompaniesCommand {

    private Long namespaceId;
    private String companyName;

    public Long getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Long namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
