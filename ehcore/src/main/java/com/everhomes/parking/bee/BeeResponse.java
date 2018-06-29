// @formatter:off
package com.everhomes.parking.bee;

import com.everhomes.util.StringHelper;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/5/29 15:24
 */
public class BeeResponse<T> {
    private String message;
    private String ngis;
    private T outList;
    private String runtime;
    private String state;
    private String ts;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNgis() {
        return ngis;
    }

    public void setNgis(String ngis) {
        this.ngis = ngis;
    }

    public T getOutList() {
        return outList;
    }

    public void setOutList(T outList) {
        this.outList = outList;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public boolean isSuccess() {
        return state!=null && "1".equals(state);
    }
}
