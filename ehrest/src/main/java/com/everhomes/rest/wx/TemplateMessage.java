package com.everhomes.rest.wx;

import com.everhomes.util.StringHelper;

import java.util.HashMap;


public class TemplateMessage {
    private String touser;
    private String template_id;
    private String url;
    private String topcolor;
    private String color;

    //{"xxxx", {"value": "xxx", "color": "xxxx"}}
    private HashMap<String, MessageItem> data;


    public TemplateMessage() {
        this.data = new HashMap<>();
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTopcolor() {
        return topcolor;
    }

    public void setTopcolor(String topcolor) {
        this.topcolor = topcolor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public HashMap<String, MessageItem> getData() {
        return data;
    }

    public void setData(HashMap<String, MessageItem> data) {
        this.data = data;
    }


    public void add(String key, String value, String color) {
        data.put(key, new MessageItem(value, color));
    }

    public void add(String key, MessageItem item) {
        data.put(key, item);
    }

    /**
     * 直接转化成jsonString
     *
     * @return {String}
     */
    public String build() {
        return StringHelper.toJsonString(this);
    }


}
