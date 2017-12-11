package com.everhomes;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class test {

    @Test
    public void test(){
        List<Long> list = new ArrayList<>();
        list.add(1023080L);
/*        list.add(1023081L);
        list.add(1023082L);
        list.add(1023083L);
        list.add(1023084L);*/

        Long a = 1023080L;
        String b = "/1023080/1023081/1023082";
        System.out.println(list.contains(a));
        System.out.println(list.contains(b));
        for(Long c : list){
            System.out.println(b.contains(String.valueOf(c)));
        }
    }
}
