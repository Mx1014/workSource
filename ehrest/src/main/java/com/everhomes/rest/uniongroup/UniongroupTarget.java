package com.everhomes.rest.uniongroup;

/**
 * <ul>用于构造可用的uniongroup的命令对象
 * <li>id: 目标Id,Long</li>
 * <li>type: 目标类型,String</li>
 * <li>name: 目标名称,String</li>
 * </ul>
 */
public class UniongroupTarget {
    public UniongroupTarget() {

    }

    public UniongroupTarget(Long id, String type, String name) {
        super();
        this.id = id;
        this.type = type;
        this.name = name;
    }

    private Long id;
    private String type;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
