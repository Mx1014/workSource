package com.everhomes.gogs;

import com.everhomes.util.StringHelper;

public class GogsData {

    private Object data;
    private boolean ok;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
