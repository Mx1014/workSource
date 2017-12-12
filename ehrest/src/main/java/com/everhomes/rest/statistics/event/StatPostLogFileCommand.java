// @formatter:off
package com.everhomes.rest.statistics.event;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>files: 日志文件uri列表{@link com.everhomes.rest.statistics.event.StatPostLogFileDTO}</li>
 * </ul>
 */
public class StatPostLogFileCommand {

    @ItemType(StatPostLogFileDTO.class)
    private List<StatPostLogFileDTO> files;

    public List<StatPostLogFileDTO> getFiles() {
        return files;
    }

    public void setFiles(List<StatPostLogFileDTO> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}