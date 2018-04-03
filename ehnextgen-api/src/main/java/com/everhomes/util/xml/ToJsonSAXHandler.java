package com.everhomes.util.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by sfyan on 2017/3/17.
 */
public class ToJsonSAXHandler extends DefaultHandler {

    //jsonStringBuilder 保存解析XML时生成的json字符串
    private StringBuilder jsonStringBuilder ;

    /*
     *  isProcessing 表示 是否正在解析一个XML
     *      startDocument 事件发生时设置 isProcessing = true
     *      startDocument 事件发生时设置 isProcessing = false
     */
    private boolean isProcessing;

    /*
     *  justProcessStartElement 表示 是否刚刚处理完一个 startElement事件
     *  引入这个标记的作用是为了判断什么时候输出逗号
     */
    private boolean justProcessStartElement;

    public ToJsonSAXHandler(){
        jsonStringBuilder = new StringBuilder();
    }

    @Override
    public void startDocument() throws SAXException {
        /*
         * 开始解析XML文档时 设定一些解析状态
         *     设置isProcessing为true，表示XML正在被解析
         *     设置justProcessStartElement为true，表示刚刚没有处理过 startElement事件
         */
        isProcessing = true;
        justProcessStartElement = true;
        //清空 jsonStringBuilder 中的字符
        this.jsonStringBuilder.delete(0, this.jsonStringBuilder.length());
    }

    @Override
    public void endDocument() throws SAXException {
        isProcessing = false;
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attrs) throws SAXException {
        /*
         * 是否刚刚处理完一个startElement事件
         *     如果是 则表示这个元素是父元素的第一个元素 。
         *     如果不是 则表示刚刚处理完一个endElement事件，即这个元素不是父元素的第一个元素
         */
        if(!justProcessStartElement){
            jsonStringBuilder.append(',');
            justProcessStartElement = true;
        }
        jsonStringBuilder.append("{");
        jsonStringBuilder.append("localName:").append('\"').append(localName).append('\"').append(',');
        jsonStringBuilder.append("uri:").append('\"').append(uri).append('\"').append(',');
        jsonStringBuilder.append("qName:").append('\"').append(qName).append('\"').append(',');
        //将解析出来的元素属性添加到JSON字符串中
        jsonStringBuilder.append("attrs:{");
        if(attrs.getLength() > 0){
            jsonStringBuilder.append(attrs.getLocalName(0)).append(":")
                    .append(attrs.getValue(0));
            for(int i = 1 ; i < attrs.getLength() ; i++){
                jsonStringBuilder.append(',').append(attrs.getLocalName(i)).append(":")
                        .append(attrs.getValue(i));
            }
        }
        jsonStringBuilder.append("},");
        //将解析出来的元素的子元素列表添加到JSON字符串中
        jsonStringBuilder.append("childElements:[").append('\n');
    }

    @Override
    public void endElement(String uri,String localName,String qName)
            throws SAXException {
        justProcessStartElement = false;
        jsonStringBuilder.append("]}").append('\n');
    }

    @Override
    public void characters(char[] ch, int begin, int length)
            throws SAXException {
        /*
         * 是否刚刚处理完一个startElement事件
         *     如果是 则表示这个元素是父元素的第一个元素 。
         *     如果不是 则表示刚刚处理完一个endElement事件，即这个元素不是父元素的第一个元素
         */
        if(!justProcessStartElement){
            jsonStringBuilder.append(',');
        }else
            justProcessStartElement = false;

        jsonStringBuilder.append('\"');
        for(int i = begin ; i < begin+length ; i++){
            switch(ch[i]){
                case '\'':jsonStringBuilder.append("\\'");break;
                case '\"':jsonStringBuilder.append("\\\"");break;
                case '\n':jsonStringBuilder.append("\\n");break;
                case '\t':jsonStringBuilder.append("\\t");break;
                case '\r':jsonStringBuilder.append("\\r");break;
                default:  jsonStringBuilder.append(ch[i]);break;
            }
        }
        jsonStringBuilder.append('\"').append('\n');
    }

    public String getJsonString() throws XMLToJSONException{
        if(this.isProcessing)
            throw new XMLToJSONException("getJsonString before resolution is finished");
        else
            return jsonStringBuilder.toString();
    }
}
