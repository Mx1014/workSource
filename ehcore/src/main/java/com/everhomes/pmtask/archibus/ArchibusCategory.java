package com.everhomes.pmtask.archibus;

import com.everhomes.util.StringHelper;

public class ArchibusCategory {

    private String pk_class_id;
    private String class_code;
    private String class_name;
    private String sort;
    private String whether_visit;
    private String whether_auto_task;
    private String project_attribute;
    private String pk_crop;

    public String getPk_class_id() {
        return pk_class_id;
    }

    public void setPk_class_id(String pk_class_id) {
        this.pk_class_id = pk_class_id;
    }

    public String getClass_code() {
        return class_code;
    }

    public void setClass_code(String class_code) {
        this.class_code = class_code;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getWhether_visit() {
        return whether_visit;
    }

    public void setWhether_visit(String whether_visit) {
        this.whether_visit = whether_visit;
    }

    public String getWhether_auto_task() {
        return whether_auto_task;
    }

    public void setWhether_auto_task(String whether_auto_task) {
        this.whether_auto_task = whether_auto_task;
    }

    public String getProject_attribute() {
        return project_attribute;
    }

    public void setProject_attribute(String project_attribute) {
        this.project_attribute = project_attribute;
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
