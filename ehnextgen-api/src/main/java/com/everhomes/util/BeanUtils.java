package com.everhomes.util;


import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * 获取私有属性值JavaBean的帮助类
 */
public class BeanUtils {
    private PropertyDescriptor dp = null;
    private String propertyName;
    private Object obj;
    /**
     * 构造方法
     * @param objName 属性名称
     * @param obj  要操作的对象
     */
    public BeanUtils(String objName,Object obj){
        try{
            this.propertyName = objName;
            this.obj = obj;
            dp = new PropertyDescriptor(propertyName, obj.getClass());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 获取javabean私有属性的值
     * @return
     * @throws Exception
     */
    public Object getProperty() throws Exception {
        //获取对象属性方法
        Method method = dp.getReadMethod();
        //获取属性对应的值
        Object retValue = method.invoke(obj);

        return retValue;
    }
    /**
     * 设置私有属性的值
     * @param setValue
     * @throws Exception
     */
    public void setProperty(Object setValue) throws Exception {
        Method method = dp.getWriteMethod();
        method.invoke(obj, setValue);
    }


}
