package com.everhomes.asset.pmsy;

import java.util.EventListener;

/**
 * Created by Administrator on 2017/7/18.
 */
public class PersonelEventListener implements EventListener{
    //事件发生后的回调方法
    public void handleEvent(PersonelEvent e){
        EventSourceObject object = (EventSourceObject)e.getSource();
        System.out.println("My name has been changed!");
        System.out.println("I got a new name,named \""+object.getName()+"\"");    }
}
