// @formatter:off
package com.everhomes.rest.statistics.event;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>logType: 需要上传的日志类型 {@link com.everhomes.rest.statistics.event.StatEventLogType}</li>
 *     <li>environments: environments {@link com.everhomes.rest.statistics.event.StatLogUploadStrategyEnvironmentDTO}</li>
 * </ul>
 */
public class StatLogUploadStrategyDTO {

    private Byte logType;
    @ItemType(StatLogUploadStrategyEnvironmentDTO.class)
    private List<StatLogUploadStrategyEnvironmentDTO> environments;

    public Byte getLogType() {
        return logType;
    }

    public void setLogType(Byte logType) {
        this.logType = logType;
    }

    public List<StatLogUploadStrategyEnvironmentDTO> getEnvironments() {
        return environments;
    }

    public void setEnvironments(List<StatLogUploadStrategyEnvironmentDTO> environments) {
        this.environments = environments;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}