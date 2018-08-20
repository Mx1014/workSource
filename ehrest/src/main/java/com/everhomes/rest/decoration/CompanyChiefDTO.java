package com.everhomes.rest.decoration;


/**
 * <ul>
 * <li>id</li>
 * <li>uid</li>
 * <li>companyId</li>
 * <li>name</li>
 * <li>phone</li>
 * </ul>
 */
public class CompanyChiefDTO {

    private Long id;
    private Long uid;
    private Long companyId;
    private String name;
    private String phone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
