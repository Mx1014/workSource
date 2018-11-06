package com.everhomes.pmtask.archibus;

import com.everhomes.util.StringHelper;

public class ArchibusProject {

    private String pk_project;
    private String project_code;
    private String project_name;
    private String project_name_zh;
    private String area_memo;
    private String pk_area;
    private String pk_crop;
    private String area_name;

    public String getPk_project() {
        return pk_project;
    }

    public void setPk_project(String pk_project) {
        this.pk_project = pk_project;
    }

    public String getProject_code() {
        return project_code;
    }

    public void setProject_code(String project_code) {
        this.project_code = project_code;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_name_zh() {
        return project_name_zh;
    }

    public void setProject_name_zh(String project_name_zh) {
        this.project_name_zh = project_name_zh;
    }

    public String getArea_memo() {
        return area_memo;
    }

    public void setArea_memo(String area_memo) {
        this.area_memo = area_memo;
    }

    public String getPk_area() {
        return pk_area;
    }

    public void setPk_area(String pk_area) {
        this.pk_area = pk_area;
    }

    public String getPk_crop() {
        return pk_crop;
    }

    public void setPk_crop(String pk_crop) {
        this.pk_crop = pk_crop;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
