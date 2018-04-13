//@formatter:off
package com.everhomes.rest.supplier;

/**
 * Created by Wentian Wang on 2018/1/10.
 */

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>id:供应商id，没有传递则认为是修改</li>
 * <li>name:供应商名称</li>
 * <li>bizLicense:营业执照注册号</li>
 * <li>legalRepresentative:法人代表名称</li>
 * <li>contactName:联系人</li>
 * <li>contactTel:联系电话</li>
 * <li>registerAddress:注册地址</li>
 * <li>email:邮箱</li>
 * <li>corpAddress:公司地址</li>
 * <li>corpIntroInfo:公司简介</li>
 * <li>industry:所属行业</li>
 * <li>bankOfDeposit:开户行</li>
 * <li>accountNumber:银行卡号</li>
 * <li>categoryOfEvaluation:评定类别</li>
 * <li>evaluationLeval:供应商等级</li>
 * <li>mainBizScope:主要经营范围</li>
 * <li>attachmentUrl:附件下载链接</li>
 *</ul>
 */
public class GetSupplierDetailDTO {
    private Long id;
    private String name;
    private String bizLicense;
    private String legalRepresentative;
    private String contactName;
    private String contactTel;
    private String registerAddress;
    private String email;
    private String corpAddress;
    private String corpIntroInfo;
    private String industry;
    private String bankOfDeposit;
    private String accountNumber;
    private Byte categoryOfEvaluation;
    private Byte evaluationLeval;
    private String mainBizScope;
    private String attachmentUrl;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBizLicense() {
        return bizLicense;
    }

    public void setBizLicense(String bizLicense) {
        this.bizLicense = bizLicense;
    }

    public String getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
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

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCorpAddress() {
        return corpAddress;
    }

    public void setCorpAddress(String corpAddress) {
        this.corpAddress = corpAddress;
    }

    public String getCorpIntroInfo() {
        return corpIntroInfo;
    }

    public void setCorpIntroInfo(String corpIntroInfo) {
        this.corpIntroInfo = corpIntroInfo;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getBankOfDeposit() {
        return bankOfDeposit;
    }

    public void setBankOfDeposit(String bankOfDeposit) {
        this.bankOfDeposit = bankOfDeposit;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Byte getCategoryOfEvaluation() {
        return categoryOfEvaluation;
    }

    public void setCategoryOfEvaluation(Byte categoryOfEvaluation) {
        this.categoryOfEvaluation = categoryOfEvaluation;
    }

    public Byte getEvaluationLeval() {
        return evaluationLeval;
    }

    public void setEvaluationLeval(Byte evaluationLeval) {
        this.evaluationLeval = evaluationLeval;
    }

    public String getMainBizScope() {
        return mainBizScope;
    }

    public void setMainBizScope(String mainBizScope) {
        this.mainBizScope = mainBizScope;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }
}
