//@formatter:off
package com.everhomes.rest.supplier;

/**
 * Created by Wentian Wang on 2018/1/10.
 */
/**
 *<ul>
 * <li>id:供应商id</li>
 * <li>identity:供应商编号</li>
 * <li>name:供应商名字</li>
 * <li>legalRepresentative:供应商法人代表</li>
 * <li>bizLicense:营业执照注册号</li>
 * <li>contactName:联系人</li>
 * <li>contactTel:联系电话</li>
 *</ul>
 */
public class ListSuppliersDTO {
    private Long id;
    private String identity;
    private String name;
    private String legalRepresentative;
    private String bizLicense;
    private String contactName;
    private String contactTel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
    }

    public String getBizLicense() {
        return bizLicense;
    }

    public void setBizLicense(String bizLicense) {
        this.bizLicense = bizLicense;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }
}
