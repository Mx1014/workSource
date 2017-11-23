// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;


/**
 * <ul>
 *     <li>id: id</li>
 *     <li>uuid: uuid</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>name: name</li>
 *     <li>createTime: createTime</li>
 * </ul>
 */
public class IndustryTypeDTO {
    private Long id;
    private String uuid;
    private Integer namespaceId;
    private String name;
    private Timestamp createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
