package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>name: 名称</li>
 *     <li>description: 描述</li>
 *     <li>script: 脚本内容</li>
 * </ul>
 */
public class UpdateFlowScriptCommand {

    @NotNull
    private Long id;
    private String name;
    private String description;
    private String script;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
