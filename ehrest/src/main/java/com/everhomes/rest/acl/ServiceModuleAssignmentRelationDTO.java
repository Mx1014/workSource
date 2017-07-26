package com.everhomes.rest.acl;


import com.everhomes.discover.ItemType;
import com.everhomes.rest.module.AssignmentTarget;
import com.everhomes.rest.module.Project;
import com.everhomes.rest.module.ServiceModuleOut;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>id: 业务模块关系id</li>
 * <li>ownerType：范围类型，固定EhOrganizations，如果是左邻运营后台的域名可以定义一个类型  参考{@link com.everhomes.rest.common.EntityType}</li>
 * <li>ownerId：范围具体Id，域名对应的机构id，后面需要讨论是否直接通过域名来获取当前公司</li>
 * <li>allModuleFlag: 是否全部模块,{@link com.everhomes.rest.common.AllFlagType}</li>
 * <li>allProjectFlag: 是否全部模块,{@link com.everhomes.rest.common.AllFlagType}</li>
 * <li>targets: 分配的对象集，参考{@link AssignmentTarget}</li>
 * <li>modules: 业务模块信息，{@link com.everhomes.rest.acl.ServiceModuleDTO}</li>
 * <li>projects: 分配的项目范围集合，参考{@link Project}</li>
 * </ul>
 */
public class ServiceModuleAssignmentRelationDTO {

    private Long id;

    private String ownerType;

    private Long ownerId;

    private Byte allModuleFlag;

    private Byte allProjectFlag;

    @ItemType(AssignmentTarget.class)
    private List<AssignmentTarget> targets;

    @ItemType(ServiceModuleOut.class)
    private List<ServiceModuleOut> modules;

    @ItemType(Project.class)
    private List<Project> projects;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Byte getAllModuleFlag() {
        return allModuleFlag;
    }

    public void setAllModuleFlag(Byte allModuleFlag) {
        this.allModuleFlag = allModuleFlag;
    }

    public List<AssignmentTarget> getTargets() {
        return targets;
    }

    public void setTargets(List<AssignmentTarget> targets) {
        this.targets = targets;
    }

    public List<ServiceModuleOut> getModules() {
        return modules;
    }

    public void setModules(List<ServiceModuleOut> modules) {
        this.modules = modules;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public Byte getAllProjectFlag() {
        return allProjectFlag;
    }

    public void setAllProjectFlag(Byte allProjectFlag) {
        this.allProjectFlag = allProjectFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
