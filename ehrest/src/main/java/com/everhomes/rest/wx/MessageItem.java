package com.everhomes.rest.wx;


public class MessageItem {

    private Object value;
    private String color;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public MessageItem(Object value, String color) {
        this.value = value;
        this.color = color;
    }
}