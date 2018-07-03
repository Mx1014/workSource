package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>flag: 标志 0-无需弹窗 1-需要弹窗</li>
 * <li>results: 配置列表 参考{@link com.everhomes.rest.archives.ArchivesOperationalConfigurationDTO}</li>
 * </ul>
 */
public class CheckOperationResponse {

    private Byte flag;

    @ItemType(ArchivesOperationalConfigurationDTO.class)
    private List<ArchivesOperationalConfigurationDTO> results;

    public CheckOperationResponse() {
    }

    public Byte getFlag() {
        return flag;
    }

    public void setFlag(Byte flag) {
        this.flag = flag;
    }

    public List<ArchivesOperationalConfigurationDTO> getResults() {
        return results;
    }

    public void setResults(List<ArchivesOperationalConfigurationDTO> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
