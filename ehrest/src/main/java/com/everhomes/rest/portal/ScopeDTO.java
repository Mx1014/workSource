package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>id: 场景实体 id</li>
 * <li>name: 场景实体 名称</li>
 * </ul>
 */
public class ScopeDTO {

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
