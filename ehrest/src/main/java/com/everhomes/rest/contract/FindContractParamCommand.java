package com.everhomes.rest.contract;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/2.
 */
public class FindContractParamCommand {

    private Integer namespaceId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
}
