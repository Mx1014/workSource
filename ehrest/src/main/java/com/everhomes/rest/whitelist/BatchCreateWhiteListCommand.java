// @formatter:off
package com.everhomes.rest.whitelist;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>createWhiteListCommandList: 新增白名单列表{@link com.everhomes.rest.whitelist.CreateWhiteListCommand}</li>
 * </ul>
 */
public class BatchCreateWhiteListCommand {

    @NotNull
    @ItemType(CreateWhiteListCommand.class)
    private List<CreateWhiteListCommand> createWhiteListCommandList;

    public List<CreateWhiteListCommand> getCreateWhiteListCommandList() {
        return createWhiteListCommandList;
    }

    public void setCreateWhiteListCommandList(List<CreateWhiteListCommand> createWhiteListCommandList) {
        this.createWhiteListCommandList = createWhiteListCommandList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
