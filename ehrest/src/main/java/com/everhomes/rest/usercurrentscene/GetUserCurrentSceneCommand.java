package com.everhomes.rest.usercurrentscene;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>uid: 人员  ID </li>
 * <li>namespaceId: 域空间id</li>
 * </ul>
 */
public class GetUserCurrentSceneCommand {

   private Integer namespaceId ;
   private Long uid ;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
