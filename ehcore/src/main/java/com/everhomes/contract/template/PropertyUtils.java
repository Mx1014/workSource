package com.everhomes.contract.template;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.mysql.fabric.xmlrpc.base.Array;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
 
public class PropertyUtils {
/*	
	public static PropertyDescriptor getPropertyDescriptor(Class<? extends Object> clazz, String propertyName) {
		StringBuffer sb = new StringBuffer();//构建一个可变字符串用来构建方法名称
		Method setMethod = null;
		Method getMethod = null;
		PropertyDescriptor pd = null;
		try {
			Field f = clazz.getDeclaredField(propertyName);//根据字段名来获取字段
			if (f!= null) {
				//构建方法的后缀
			   String methodEnd = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
			   sb.append("set" + methodEnd);//构建set方法
			   setMethod = clazz.getDeclaredMethod(sb.toString(), new Class[]{ f.getType() });
			   sb.delete(0, sb.length());//清空整个可变字符串
			   sb.append("get" + methodEnd);//构建get方法
			   getMethod = clazz.getDeclaredMethod(sb.toString(), new Class[]{ });
			   //构建一个属性描述器 把对应属性 propertyName 的 get 和 set 方法保存到属性描述器中
			   pd = new PropertyDescriptor(propertyName, getMethod, setMethod);
			}
		} catch (Exception ex) {
				ex.printStackTrace();
		}
		return pd;
	}
	*/
	public static void setProperty(Object obj,String propertyName,Object value){
		Class<? extends Object> clazz = obj.getClass();//获取对象的类型
		PropertyDescriptor pd;
		try {
			pd = new PropertyDescriptor(propertyName, clazz);
			Method setMethod = pd.getWriteMethod();
			setMethod.invoke(obj, new Object[]{value});
		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			e1.printStackTrace();
		}
	}
	
	public static Object getProperty(Object obj, String propertyName){
		Object value =null ;
		Class<? extends Object> clazz = obj.getClass();//获取对象的类型
		PropertyDescriptor pd;
		try {
			pd = new PropertyDescriptor(propertyName, clazz);
			Method getMethod = pd.getReadMethod();//从属性描述器中获取 get 方法
			value = getMethod.invoke(clazz, new Object[]{});//调用方法获取方法的返回值
		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			e1.printStackTrace();
		}
		return value;//返回值
	}
	
	/**
	 * 获取一个类声明的所有变量的变量名
	 * @param clazz
	 * @return
	 */
	public static List<String> getDeclaredFieldsName(Class<?> clazz){
		List<String> result = new ArrayList<>();
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			String name = field.getName();
			result.add(name);
		}
		return result;
	}
	
}
