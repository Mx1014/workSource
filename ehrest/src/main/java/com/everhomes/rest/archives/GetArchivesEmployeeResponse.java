package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>form: 档案详情表单(包含字段名、组名及值) 参考{@link com.everhomes.rest.general_approval.GeneralFormDTO}</li>
 * <li>los: 档案记录 参考{@link com.everhomes.rest.archives.ArchivesLogDTO}</li>
 * </ul>
 */
public class GetArchivesEmployeeResponse {

    private GeneralFormDTO form;

    @ItemType(ArchivesLogDTO.class)
    public List<ArchivesLogDTO> los;

    public GetArchivesEmployeeResponse() {
    }

    public GeneralFormDTO getForm() {
        return form;
    }

    public void setForm(GeneralFormDTO form) {
        this.form = form;
    }

    public List<ArchivesLogDTO> getLos() {
        return los;
    }

    public void setLos(List<ArchivesLogDTO> los) {
        this.los = los;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
