package com.everhomes.wx.message;

import com.everhomes.util.StringHelper;

import java.util.HashMap;



public class TemplateMessage {
    private String touser;
    private String template_id;
    private String url;
    private String topcolor;

    //{"xxxx", {"value": "xxx", "color": "xxxx"}}
    private HashMap<String, Item> data;


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

    public HashMap<String, Item> getData() {
        return data;
    }

    public void setData(HashMap<String, Item> data) {
        this.data = data;
    }


    public void add(String key, String value, String color) {
        data.put(key, new Item(value, color));
    }

    public void add(String key, Item item) {
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
