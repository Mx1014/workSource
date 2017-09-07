//@formatter:off
package com.everhomes.assetPayment;

import com.everhomes.util.StringHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wentian Wang on 2017/8/19.
 */

public class UtilTest {
    @Test
    public void fun(){
        List<container> list = new ArrayList<>();
        container x1 = new container();
        container x2 = new container();
        x1.setA("1");
        x1.setB(new BigDecimal("80"));
        x2.setA("2");
        x2.setB(new BigDecimal("0"));
        list.add(x1);
        list.add(x2);
        Gson gson = new Gson();
        Map<String,String> map = new LinkedHashMap<>();
        map.put("1","80");
        map.put("2","0");
        String s = gson.toJson(map);
//        String kick = "[{"1":"80"},{"2":"0"}]";
        String kick="";
        System.out.println(s);
        System.out.println("============");
//        List<container> o = gson.fromJson(s, new TypeToken<List<Map>>() {
//        }.getType());
        Map map1 = gson.fromJson(s, Map.class);
        System.out.println(map1);
//        BigDecimal b = new BigDecimal((double)o.get(0).getB());
//        System.out.println(b);
    }
}
class container{
    private Object a;
    private Object b;

    public Object getA() {
        return a;
    }

    public void setA(Object a) {
        this.a = a;
    }

    public Object getB() {
        return b;
    }

    public void setB(Object b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
