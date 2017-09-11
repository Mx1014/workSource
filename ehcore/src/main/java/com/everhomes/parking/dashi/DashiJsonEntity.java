package com.everhomes.parking.dashi;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/8/22.
 */
public class DashiJsonEntity<T> {
    private Integer RtnCode;
    private String RtnInfo;
    private List<T> data;

    public boolean isSuccess() {
        if (RtnCode == 0) {
            return true;
        }
        return false;
    }

    public Integer getRtnCode() {
        return RtnCode;
    }

    public void setRtnCode(Integer rtnCode) {
        RtnCode = rtnCode;
    }

    public String getRtnInfo() {
        return RtnInfo;
    }

    public void setRtnInfo(String rtnInfo) {
        RtnInfo = rtnInfo;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
