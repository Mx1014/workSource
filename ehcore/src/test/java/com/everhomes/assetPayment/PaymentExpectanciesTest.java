//@formatter:off
package com.everhomes.assetPayment;

import com.everhomes.asset.AssetService;
import com.everhomes.asset.AssetServiceImpl;
import com.everhomes.rest.asset.*;
import com.everhomes.util.CalculatorUtil;
import com.google.gson.Gson;
import org.junit.Test;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Wentian Wang on 2017/8/22.
 */

public class PaymentExpectanciesTest {

    @Test
    public void fun() throws ParseException, IllegalAccessException, IntrospectionException, InvocationTargetException {
//        Script
//        List<VariableIdAndValue> list = new ArrayList<>();
//        VariableIdAndValue v1 = new VariableIdAndValue();
//        v1.setVariableId("1");
//        v1.setVariableValue("80");
//        list.add(v1);
//        VariableIdAndValue v2 = new VariableIdAndValue();
//        v2.setVariableId("2");
//        v2.setVariableValue("2");
//        list.add(v2);
//        VariableIdAndValue v3 = new VariableIdAndValue();
//        v3.setVariableId("3");
//        v3.setVariableValue("60");
//        list.add(v3);
//        String formula = "(1*2-3)";
//        BigDecimal bigDecimal = calculateFee1(list, formula, 1);
//        System.out.println(bigDecimal);

        PaymentExpectanciesCommand cmd = new PaymentExpectanciesCommand();
        cmd.setTargetType("eh_organization");
        cmd.setTargetName("波塞冬集团");
        cmd.setTargetId(null);
        cmd.setPageSize(20);
        cmd.setPageOffset(0);
        cmd.setOwnerType("community");
        cmd.setOwnerId(240111044331055035l);
        cmd.setNoticeTel("15919770996");
        cmd.setNamesapceId(999985);
        List<FeeRules> list = new ArrayList<>();

        FeeRules feeRules = new FeeRules();
        feeRules.setChargingItemId(1l);
        feeRules.setChargingStandardId(1l);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date begin = sdf.parse("2017-01");
        Date end = sdf.parse("2017-10");
        feeRules.setDateStrBegin(begin);
        feeRules.setDateStrEnd(end);
        List<String> props = new ArrayList<>();
        props.add("240102032121838343934");
        props.add("KG28483BX21");
//        feeRules.setPropertyName(props);
        List<VariableIdAndValue> v = new ArrayList<>();
        VariableIdAndValue g = new VariableIdAndValue();
        g.setVariableId("ydj");
        g.setVariableValue("80");
        VariableIdAndValue g1 = new VariableIdAndValue();
        g1.setVariableId("mj");
        g1.setVariableValue("100");
        v.add(g);
        v.add(g1);
        feeRules.setVariableIdAndValueList(v);
        List<ContractProperty> var1 = new ArrayList<>();
        ContractProperty var2 = new ContractProperty();
        var2.setAddressId(24010203212183834l);
        var2.setApartmentName("西太平洋");
        var2.setBuldingName("波塞冬旋翼载具研发中心");
        var2.setPropertyName("24010203212183834");
        ContractProperty var3 = new ContractProperty();
        var3.setAddressId(null);
        var3.setApartmentName("古宇宙");
        var3.setBuldingName("万神墓");
        var3.setPropertyName("KG28483BX21");
        var1.add(var2);
        var1.add(var3);
        feeRules.setProperties(var1);

        list.add(feeRules);

        cmd.setFeesRules(list);
        cmd.setContractNum("KK2086");
//        cmd.setBuldingName("西太平洋03号standalone");
//        cmd.setApartmentName("旋翼载具研究所811");

        System.out.println(cmd);

//        Map map = convertBean(cmd);
//        System.out.println(map);


    }







    /**
     * 将一个 JavaBean 对象转化为一个  Map
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map convertBean(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }



    /**
     * 将一个 Map 对象转化为一个 JavaBean
     * @param type 要转化的类型
     * @param map 包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InstantiationException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings("rawtypes")
    public static Object convertMap(Class type, Map map)
            throws IntrospectionException, IllegalAccessException,
            InstantiationException, InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
        Object obj = type.newInstance(); // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();

            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                Object value = map.get(propertyName);

                Object[] args = new Object[1];
                args[0] = value;

                descriptor.getWriteMethod().invoke(obj, args);
            }
        }
        return obj;
    }


















    private BigDecimal calculateFee1(List<VariableIdAndValue> variableIdAndValueList, String formula, float duration) {
        Gson gson = new Gson();
        HashMap<String,BigDecimal> map = new HashMap();
        for(int i = 0; i < variableIdAndValueList.size(); i++){
            VariableIdAndValue variableIdAndValue = variableIdAndValueList.get(i);
            map.put((String)variableIdAndValue.getVariableId(),new BigDecimal(Float.parseFloat((String)variableIdAndValue.getVariableValue())));
        }
        char[] chars = formula.toCharArray();
        List<Character> ch = new ArrayList<>();
        for(int i = 0; i < chars.length; i++){
            ch.add(chars[i]);
        }
        List<Character> operators = new ArrayList<>();
        operators.add('*');
        operators.add('/');
        operators.add('+');
        operators.add('-');
        int begin = 0;
        int end = 0;
        outter:
        while(true){
            if(ch.get(end)=='('){

            }
            if(end == ch.size()-1){
                //最后的置换
                List<Character> characters = ch.subList(begin, end + 1);
                String variableId = "";
                for(int i = 0; i< characters.size();i++){
                    variableId += characters.get(i);
                }
                if(!map.containsKey(variableId)){
                    throw new RuntimeException("公式解析失败");
                }
                BigDecimal varibleValue = map.get(variableId);
                int value = varibleValue.intValue();
                String replaced = String.valueOf(value);
                char[] replacedChars = replaced.toCharArray();
                List<Character> target = new ArrayList<>();
                int len = replaced.length();
                for(int i = 0; i< replacedChars.length; i++){
                    target.add(replacedChars[i]);
                }
                int originaLen = end -begin;
                if(begin == end){
                    originaLen = 1;
                }
                ch.addAll(begin,target);
                int deleteIndex = begin+target.size();;
                for(int i = 0; i < originaLen; i++){
                    ch.remove(deleteIndex);
                }
                break outter;
            }
            char c = ch.get(end);
            if(operators.contains(c)){
                //替换 begin和end之间的数值，置begin为 begin+len(new)+1，end = begin--》continue来跳过这个运算符
                List<Character> characters = ch.subList(begin, end);
                String variableId = "";
                for(int i = 0; i< characters.size();i++){
                    variableId += characters.get(i);
                }

                if(!map.containsKey(variableId)){
                    throw new RuntimeException("公式解析失败");
                }
                BigDecimal varibleValue = map.get(variableId);
                int value = varibleValue.intValue();
                String replaced = String.valueOf(value);
                char[] replacedChars = replaced.toCharArray();
                List<Character> target = new ArrayList<>();
                int len = replaced.length();
                for(int i = 0; i< replacedChars.length; i++){
                    target.add(replacedChars[i]);
                }
                int originaLen = end -begin;
                ch.addAll(begin,target);
                int deleteIndex = begin+target.size();;
                for(int i = 0; i < originaLen; i++){
                    ch.remove(deleteIndex);
                }
                begin = begin+len+1;
                end = begin;
                continue;
            }
            end++;
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < ch.size(); i++){
            String s = ch.get(i).toString();
            sb.append(s);
        }
        BigDecimal response = CalculatorUtil.arithmetic(sb.toString());
        response.setScale(2);
        return response;
    }

}
