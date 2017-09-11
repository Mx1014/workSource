package com.everhomes.rest.varField;

/**
 * <ul>
 *     <li>text: 单行文本框</li>
 *     <li>multiText: 多行文本框</li>
 *     <li>select: 单选列表</li>
 *     <li>radio: 单选框</li>
 *     <li>checkbox: 多选框</li>
 *     <li>datetime: 时间</li>
 *     <li>file: 文件</li>
 *     <li>image: 图片</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public enum FieldParamType {

    TEXT("text"), MULTI_TEXT("multiText"), SELECT("select"), RADIO("radio"), CHECKBOX("checkbox"),DATETIME("datetime"),
    FILE("file"), IMAGE("image");

    private String name;

    private FieldParamType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static FieldParamType fromName(String name) {
        if(name != null) {
            FieldParamType[] values = FieldParamType.values();
            for(FieldParamType value : values) {
                if(value.equals(name)) {
                    return value;
                }
            }
        }

        return null;
    }
}
