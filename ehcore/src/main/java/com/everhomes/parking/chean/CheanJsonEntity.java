package com.everhomes.parking.chean;

import java.util.List;

public class CheanJsonEntity<T> {
    private Boolean status;
    private String message;
    private Integer code;
    private String time;
    private T data;
    private String rowindex;
    private Integer total;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getRowindex() {
        return rowindex;
    }

    public void setRowindex(String rowindex) {
        this.rowindex = rowindex;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
