package com.everhomes.controller;

import com.everhomes.discover.RestMethod;

public class ExtendRestMethod extends RestMethod {
    private XssExclude xssExclude;

    public XssExclude getXssExclude() {
        return xssExclude;
    }

    public void setXssExclude(XssExclude xssExclude) {
        this.xssExclude = xssExclude;
    }
}
