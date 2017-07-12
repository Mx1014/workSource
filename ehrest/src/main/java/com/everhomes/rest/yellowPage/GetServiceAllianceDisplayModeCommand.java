package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li> parentId: 服务联盟父分类id</li>
 *  <li> destination: 输出终端 1：客户端 2：浏览器</li>
 * </ul>
 */
public class GetServiceAllianceDisplayModeCommand {

	private Long parentId;

    private Byte destination;

    public Byte getDestination() {
        return destination;
    }

    public void setDestination(Byte destination) {
        this.destination = destination;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
