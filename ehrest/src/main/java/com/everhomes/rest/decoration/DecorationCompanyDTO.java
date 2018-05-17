package com.everhomes.rest.decoration;

import java.util.List;

/**
 * <ul>
 * <li>companyChiefs：装修公司负责人 List 参考{@link com.everhomes.rest.decoration.CompanyChiefDTO}</li>
 * <li>name：公司名</li>
 * <li>organizationId：公司id</li>
 * <li>id：装修公司id</li>
 * </ul>
 */
public class DecorationCompanyDTO {

    private Long id;
    private Long organizationId;
    private String name;
    private List<CompanyChiefDTO> companyChiefs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CompanyChiefDTO> getCompanyChiefs() {
        return companyChiefs;
    }

    public void setCompanyChiefs(List<CompanyChiefDTO> companyChiefs) {
        this.companyChiefs = companyChiefs;
    }
}
