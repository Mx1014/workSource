package com.everhomes.news;

import com.everhomes.server.schema.tables.pojos.EhNewsTagVals;

/**
 * Created by Administrator on 2017/9/12.
 */
public class NewsTagVals extends EhNewsTagVals {
    private static final long serialVersionUID = 3794196114833158911L;

    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
