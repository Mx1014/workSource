// @formatter:off
package com.everhomes.visitorsys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/5/11 20:25
 */
public class VisitorSysUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(VisitorSysUtils.class);

    public static Timestamp getStartOfDay(long now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        return new Timestamp(calendar.getTimeInMillis());

    }

    public static Timestamp getEndOfDay(long now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 拷贝除了namespaceId，ownerId,ownerType属性到target中
     * @param source
     * @param target
     * @param <T>
     * @return
     */
     public static <T>T copyNotNullProperties( Object source, T target) {
        String[] s = {"getNamespaceId","getOwnerType","getOwnerId","setNamespaceId","setOwnerType","setOwnerId"};
        List<String> ignoreProperties = new ArrayList<>(Arrays.asList(s));
        return copyNotNullProperties(source,target,ignoreProperties);
    }

    /**
     * 拷贝source中非所有空属性,到target中对应属性中
     * @param source
     * @param target
     * @param <T>
     * @return
     */
     public static <T>T copyAllNotNullProperties( Object source, T target) {
        return copyNotNullProperties( source,target,null);
    }

    /**
     * 拷贝source中非空不在ignoreProperties中的属性,到target中对应属性中
     * @param source
     * @param target
     * @param ignoreProperties
     * @param <T>
     * @return
     */
     public static <T>T copyNotNullProperties( Object source, T target, List<String> ignoreProperties) {
        if(source==null)
            return target;
        Map<String,Method> getSetMethodMap = new HashMap();
        List<Method> getMethodList = new ArrayList();
        for (Method method : source.getClass().getMethods()) {
            String methodName = method.getName();
            if((methodName.startsWith("get")
                    ||  methodName.startsWith("set"))
                    && !"getClass".equals(methodName)
                    && (ignoreProperties==null || !ignoreProperties.contains(methodName))){
                if(methodName.startsWith("get")){
                    getMethodList.add(method);
                }
                getSetMethodMap.put(methodName,method);
            }
        }
        for (Method method : getMethodList) {
            String sourceGetMethodName = method.getName();
            String targetSetMethodName = sourceGetMethodName.replace("get","set");
            try {
                Object result = method.invoke(source);
                if(result==null){
                    continue;
                }
                if(isNeedPropertiesCopy(result)){
                    //如果需要深拷贝，这里先获取到目标类的需要深拷贝的属性
                    Method targetGetMethod = target.getClass().getMethod(sourceGetMethodName);
                    Object subtarget = targetGetMethod.invoke(target);
                    if(subtarget!=null) {
                        result = copyNotNullProperties(result, subtarget,null);
                    }
                }
                Method sourceSetMethod = getSetMethodMap.get(targetSetMethodName);
                Method targetMethod = target.getClass().getMethod(targetSetMethodName, sourceSetMethod.getParameterTypes());
                targetMethod.invoke(target, result);
            } catch (Exception e) {
                LOGGER.trace("source Method = {}, targetMethod = {}, failed", sourceGetMethodName, targetSetMethodName,e);
            }
        }
        return target;
    }

    /**
     * 生成大小写加数字的混合随机字符串
     * @return
     */
    public static String generateLetterNumCode(int length) {
        String randomCode = "";
        Random rand = new Random();
        for(int i=0;i<length;i++){
            int num = rand.nextInt(3);
            switch(num){
                case 0:
                    char c1 = (char)(rand.nextInt(26)+'a');//生成随机小写字母
                    randomCode += c1;
                    break;
                case 1:
                    char c2 = (char)(rand.nextInt(26)+'A');//生成随机大写字母
                    randomCode += c2;
                    break;
                case 2:
                    randomCode += rand.nextInt(10);//生成随机数字
            }
        }
        return randomCode;
    }
    /**
     * 生成固定长度的数字
     * @param codeLength
     * @return
     */
    public static String generateNumCode(int codeLength) {
        String code = "";
        while(code.length()<codeLength){
            int i = (int) (Math.random() * 10);
            code +=i;
        }
        while(code.length()>codeLength){
            code = code.substring(1, code.length());
        }
        return code;
    }

     public static boolean isNeedPropertiesCopy(Object result){
        return !(result instanceof Byte
                || result instanceof Short
                || result instanceof Integer
                || result instanceof Long
                || result instanceof Float
                || result instanceof Double
                || result instanceof Character
                || result instanceof Boolean
                || result instanceof String
                || result instanceof Timestamp
                || result instanceof Collection);
    }

}
