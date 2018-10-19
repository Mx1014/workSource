package com.everhomes.pmtask.archibus;

import com.everhomes.util.StringHelper;

import java.util.List;

public class ArchibusListEntity<T> {

    private Integer code;
    private Long st;
    private String msg;
    private List<T> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Long getSt() {
        return st;
    }

    public void setSt(Long st) {
        this.st = st;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Boolean isSuccess() {
        if(code == 0)
            return true;
        return false;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
