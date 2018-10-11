package com.everhomes.pmtask.archibus;

import com.everhomes.util.StringHelper;

public class ArchibusArea {
    private String pk_area;
    private String area_code;
    private String area_name;
    private String area_memo;
    private String pk_crop;

    public String getPk_area() {
        return pk_area;
    }

    public void setPk_area(String pk_area) {
        this.pk_area = pk_area;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getArea_memo() {
        return area_memo;
    }

    public void setArea_memo(String area_memo) {
        this.area_memo = area_memo;
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
