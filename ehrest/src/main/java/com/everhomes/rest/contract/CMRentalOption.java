package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

public class CMRentalOption {

    private String RentalID;

    private String Title;

    private String Description;

    private String Remark;

    private String Modifydate;

    public String getModifydate() {
        return Modifydate;
    }

    public void setModifydate(String modifydate) {
        Modifydate = modifydate;
    }

    public String getRentalID() {
        return RentalID;
    }

    public void setRentalID(String rentalID) {
        RentalID = rentalID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
