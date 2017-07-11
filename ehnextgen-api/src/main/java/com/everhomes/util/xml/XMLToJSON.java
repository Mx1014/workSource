package com.everhomes.util.xml;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sfyan on 2017/3/17.
 */
public class XMLToJSON {

    public static String convertElementJson(String xmlStr) throws SAXException,
            IOException, XMLToJSONException {
        return convertElementJson(new InputSource(new StringReader(xmlStr)));
    }

    public static String convertElementJson(File file) throws SAXException,
            IOException, XMLToJSONException {
        return convertElementJson(new InputSource(new FileInputStream(file)));
    }

    public static String convertElementJson(InputSource inputSource) throws SAXException,
            IOException, XMLToJSONException {
        ToJsonSAXHandler handler = new ToJsonSAXHandler();
        //创建一个 SAX 解析器 ,并设置这个解析器的内容事件处理器 和 错误事件处理器 为 handler
        XMLReader reader = XMLReaderFactory.createXMLReader();
        reader.setContentHandler(handler);
        reader.setErrorHandler(handler);
        //用 SAX 解析器解析XML输入源
        reader.parse(inputSource);
        //返回 ToJsonSAXHandler 中保存的 json字符串
        return handler.getJsonString();
    }



    public static Map<String, Object> convertMap(String xmlJson, boolean isStandard){
        Map<String, Object> childElementMap = new HashMap<>();
        Map xmlMap = (Map)JSONObject.parse(xmlJson);
        String key = "";
        Object value = null;
        for (Object map : xmlMap.entrySet()){
            if(((Map.Entry)map).getKey().equals("localName")){
                key = ((Map.Entry)map).getValue().toString();
                if(isStandard){
                    key = getStandardKey(key);
                }
            }

            if(((Map.Entry)map).getKey().equals("childElements")){
                List<Object> childElements = JSONArray.parseArray(((Map.Entry)map).getValue().toString(),Object.class);
                if(childElements.size() > 1){
                    String preChildKey = "";
                    Object preChildValue = "";
                    List<Object> childList = null;
                    Map<String, Object> childMap = null;
                    for(int i = 1; i < childElements.size(); i = i + 2 ){
                        String elementJson = childElements.get(i).toString();
                        String elementKey = getElementKey(elementJson);
                        if(isStandard){
                            elementKey = getStandardKey(elementKey);
                        }
                        Map<String, Object> elementMap = convertMap(elementJson, isStandard);
                        if(preChildKey.equals(elementKey)){
                            if(null == childList){
                                childList = new ArrayList<>();
                                childList.add(preChildValue);
                            }
                            childList.add(elementMap.get(elementKey));
                        }else{
                            if(null == childMap){
                                childMap = new HashMap<>();
                            }
                            childMap.putAll(elementMap);
                        }
                        preChildKey = elementKey;
                        preChildValue = elementMap.get(elementKey);
                    }

                    if(null == childList){
                        value = childMap;
                    }else{
                        value = childList;
                    }
                }else{
                    if(1 == childElements.size()){
                        value = childElements.get(0);
                    }else{
                        value = "";
                    }

                }
            }
        }
        childElementMap.put(key, value);
        return childElementMap;
    }


    /**
     * xml转换成map 即key不做任何处理 是最原始的值
     * 其中map里面key值对应xml中的标签名
     * @param xmlStr
     * @return
     */
    public static Map<String, Object> convertOriginalMap(String xmlStr){
        try{
            String xmlJson = convertElementJson(xmlStr);
            return convertMap(xmlJson, false);
        }catch (Exception e){
            throw new RuntimeException("XML String convert map errer", e);
        }
    }

    /**
     * xml file转换成map 即key不做任何处理 是最原始的值
     * 其中map里面key值对应xml中的标签名
     * @param file
     * @return
     */
    public static Map<String, Object> convertOriginalMap(File file){
        try{
            String xmlJson = convertElementJson(file);
            return convertMap(xmlJson, false);
        }catch (Exception e){
            throw new RuntimeException("XML String convert map errer", e);
        }
    }

    /**
     * xml转换成map 即key会做处理，变成标准的值
     * 其中map里面的key值做了转换，xml中如果包含的名称有下划线，则会把下划线去掉，下划线后面一个字母变成大写
     * @param xmlStr
     * @return
     */
    public static Map<String, Object> convertStandardMap(String xmlStr){
        try{
            String xmlJson = convertElementJson(xmlStr);
            return convertMap(xmlJson, true);
        }catch (Exception e){
            throw new RuntimeException("XML String convert map errer", e);
        }
    }

    /**
     * xml file转换成map 即key会做处理，变成标准的值
     * 其中map里面的key值做了转换，xml中如果包含的名称有下划线，则会把下划线去掉，下划线后面一个字母变成大写
     * @param file
     * @return
     */
    public static Map<String, Object> convertStandardMap(File file){
        try{
            String xmlJson = convertElementJson(file);
            return convertMap(xmlJson, true);
        }catch (Exception e){
            throw new RuntimeException("XML String convert map errer", e);
        }
    }

    /**
     * xml转换成json 即key会做处理，变成标准的值
     * 其中json里面的key值做了转换，xml中如果包含的名称有下划线，则会把下划线去掉，下划线后面一个字母变成大写
     * @param xmlStr
     * @return
     */
    public static String convertStandardJson(String xmlStr){
        Map<String, Object> elementMap = convertStandardMap(xmlStr);
        return JSONObject.toJSONString(elementMap);
    }

    /**
     * xml file转换成json 即key会做处理，变成标准的值
     * 其中json里面的key值做了转换，xml中如果包含的名称有下划线，则会把下划线去掉，下划线后面一个字母变成大写
     * @param file
     * @return
     */
    public static String convertStandardJson(File file){
        Map<String, Object> elementMap = convertStandardMap(file);
        return JSONObject.toJSONString(elementMap);
    }

    /**
     * xml file转换成json 即key不做任何处理 是最原始的值
     * 其中json里面key值对应xml中的标签名
     * @param file
     * @return
     */
    public static String convertOriginalJson(File file){
        Map<String, Object> elementMap = convertOriginalMap(file);
        return JSONObject.toJSONString(elementMap);
    }

    protected static String getElementKey(String xmlJson){
        Map xmlMap = (Map)JSONObject.parse(xmlJson);
        String key = "";
        for (Object map : xmlMap.entrySet()){
            if(((Map.Entry)map).getKey().equals("localName")){
                key = ((Map.Entry)map).getValue().toString();
            }
        }
        return key;
    }

    private static String getStandardKey(String elementKey){
        return getName(elementKey, elementKey);
    }

    private static String getName(String name,String  anotherName) {
        name=anotherName;
        //如果最后一个是_ 不做转换
        if(name.indexOf("_")>0&&name.length()!=name.indexOf("_")+1){
            int lengthPlace=name.indexOf("_");
            name=name.replaceFirst("_", "");
            String s=name.substring(lengthPlace, lengthPlace+1);
            s=s.toUpperCase();
            anotherName=name.substring(0,lengthPlace)+s+name.substring(lengthPlace+1);
        }else{
            return  anotherName;
        }
        return getName(name,anotherName);
    }
}
