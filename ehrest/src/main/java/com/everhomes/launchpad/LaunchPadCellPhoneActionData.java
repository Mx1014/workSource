package com.everhomes.launchpad;

import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为cellphone时点击item需要的参数
 * <li>cellPhone: 电话号码</li>
 * <li>title: 文本内容</li>
 * </ul>
 */
public class LaunchPadCellPhoneActionData {
    //{"cellPhone":12345678,"title":"咨询电话"}
    private String cellPhone;
    private String title;
    
    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
