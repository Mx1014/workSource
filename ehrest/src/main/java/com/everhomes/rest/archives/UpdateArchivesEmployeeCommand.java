package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.util.StringHelper;

import java.sql.Date;
import java.util.List;

/**
 * <ul>
 * <li>formOriginId: 表单id</li>
 * <li>detailId: 员工id</li>
 * <li>organizationId: 公司id</li>
 * <li>values: (List)被修改的字段及值 {@link com.everhomes.rest.general_approval.PostApprovalFormItem}</li>
 * </ul>
 */
public class UpdateArchivesEmployeeCommand {

    private Long formOriginId;

    private Long detailId;

    private Long organizationId;

    /*private String contactName;

    private String enName;

    private String gender;

    private Date birthday;

    private String maritalFlag;

    private Date procreative;

    private String ethnicity;

    private String politicalFlag;

    private String nativePlace;

    private String idType;

    private String idNumber;

    private Date idExpiryDate;

    private String degree;

    private String graduationSchool;

    private Date graduationTime;

    private String regionCode;

    private String contactToken;

    private String email;

    private String wechat;

    private String qq;

    private String address;

    private String emergencyName;

    private String emergencyRelationship;

    private String emergencyContact;

    private Date checkInTime;

    private String employeeType;

    private String employeeStatus;

    private Date employmentTime;

    private String department;

    private String jobPosition;

    private String reportTarget;

    private String employeeNo;

    private String contactShortToken;

    private String workEmail;

    private Long contractPartyId;

    private Date workStartTime;

    private Date contractStartTime;

    private Date contractEndTime;

    private String salaryCardNumber;

    private String salaryCardBank;

    private String socialSecurityNumber;

    private String providentFundNumber;

    private String regResidenceType;

    private String regResidence;

    private String idPhoto;

    private String visaPhoto;

    private String lifePhoto;

    private String entryForm;

    private String graduationCertificate;

    private String degreeCertificate;

    private String contractCertificate;*/

    @ItemType(PostApprovalFormItem.class)
    List<PostApprovalFormItem> values;

    public UpdateArchivesEmployeeCommand() {
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<PostApprovalFormItem> getValues() {
        return values;
    }

    public void setValues(List<PostApprovalFormItem> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
