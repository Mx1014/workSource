package com.everhomes.rest.enterpriseApproval;

import com.everhomes.rest.archives.ArchivesOperationalConfigurationDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>flag: 0-无 1-人事 2-审批 參考{@link com.everhomes.rest.archives.ArchivesOperationCheckFlag}</li>
 * <li>info: 补充信息 参考{@link com.everhomes.rest.archives.ArchivesOperationalConfigurationDTO}</li>
 * </ul>
 */
public class CheckArchivesApprovalResponse {

    private Byte flag;

    private ArchivesOperationalConfigurationDTO info;

    public CheckArchivesApprovalResponse() {
    }

    public Byte getFlag() {
        return flag;
    }

    public void setFlag(Byte flag) {
        this.flag = flag;
    }

    public ArchivesOperationalConfigurationDTO getInfo() {
        return info;
    }

    public void setInfo(ArchivesOperationalConfigurationDTO info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
