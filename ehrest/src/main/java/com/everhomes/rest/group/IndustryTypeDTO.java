// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;


/**
 * <ul>
 *     <li>id: id</li>
 *     <li>uuid: uuid</li>
 *     <li>namespace_id: namespace_id</li>
 *     <li>name: name</li>
 *     <li>create_time: create_time</li>
 * </ul>
 */
public class IndustryTypeDTO {
    private Long id;
    private String uuid;
    private Integer namespace_id;
    private String name;
    private Timestamp create_time;

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

    public Integer getNamespace_id() {
        return namespace_id;
    }

    public void setNamespace_id(Integer namespace_id) {
        this.namespace_id = namespace_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
