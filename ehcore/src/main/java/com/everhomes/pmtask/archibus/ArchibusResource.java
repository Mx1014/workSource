package com.everhomes.pmtask.archibus;

import com.everhomes.util.StringHelper;

public class ArchibusResource {
    private String pk_resources;
    private String resources_code;
    private String resources_name;
    private String resources_type;
    private String parent_id;
    private String project_id;
    private String pk_crop;

    public String getPk_resources() {
        return pk_resources;
    }

    public void setPk_resources(String pk_resources) {
        this.pk_resources = pk_resources;
    }

    public String getResources_code() {
        return resources_code;
    }

    public void setResources_code(String resources_code) {
        this.resources_code = resources_code;
    }

    public String getResources_name() {
        return resources_name;
    }

    public void setResources_name(String resources_name) {
        this.resources_name = resources_name;
    }

    public String getResources_type() {
        return resources_type;
    }

    public void setResources_type(String resources_type) {
        this.resources_type = resources_type;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getPk_crop() {
        return pk_crop;
    }

    public void setPk_crop(String pk_crop) {
        this.pk_crop = pk_crop;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
