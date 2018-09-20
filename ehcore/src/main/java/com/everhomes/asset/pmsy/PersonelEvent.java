package com.everhomes.asset.pmsy;

import java.util.EventObject;

/**
 * Created by Administrator on 2017/7/18.
 */
public class PersonelEvent extends EventObject {
    private static final long serialVersionUID = 1L;
    private Object source;//事件源

    public PersonelEvent(Object source){
        super(source);
        this.source = source;
    }

    @Override
    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}
