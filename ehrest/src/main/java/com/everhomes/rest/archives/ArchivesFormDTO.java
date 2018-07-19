package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>id: 表单id</li>
 * <li>formName: 表单名称</li>
 * <li>formFields: 表单字段 参考{@link com.everhomes.rest.general_approval.GeneralFormFieldDTO}</li>
 * <li>formGroups: 表单字段组 {@link com.everhomes.rest.archives.ArchivesFormGroupDTO}</li>
 * <li>operatorUid: 操作人id</li>
 * <li>operatorTime: 操作时间</li>
 * </ul>
 */
public class ArchivesFormDTO {
    private Long id;

    private String formName;

    @ItemType(GeneralFormFieldDTO.class)
    private List<GeneralFormFieldDTO> formFields;

    @ItemType(ArchivesFormGroupDTO.class)
    private List<ArchivesFormGroupDTO> formGroups;

    private Long operatorUid;

    private Timestamp operatorTime;

    public ArchivesFormDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public List<GeneralFormFieldDTO> getFormFields() {
        return formFields;
    }

    public void setFormFields(List<GeneralFormFieldDTO> formFields) {
        this.formFields = formFields;
    }

    public List<ArchivesFormGroupDTO> getFormGroups() {
        return formGroups;
    }

    public void setFormGroups(List<ArchivesFormGroupDTO> formGroups) {
        this.formGroups = formGroups;
    }

    public Long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(Long operatorUid) {
        this.operatorUid = operatorUid;
    }

    public Timestamp getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Timestamp operatorTime) {
        this.operatorTime = operatorTime;
    }
}
