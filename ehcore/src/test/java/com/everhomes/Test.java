package com.everhomes;

import com.everhomes.util.StringHelper;

import java.util.*;

/**
 * 100个字组成一个序列，让你写一个程序，从第一个字开始，依次往后数，
 * 编号数到7的倍数从从序列中剔除（注意：是从序列中删除而非将该字置为空串），
 * 数到最后一个字后回到第一个字，如此往复，直到全部删完。请写出程序
 */
public class Test {
    public static void main(String[] args) {
//        String testStr = "ewr";
//        System.out.println(testStr.indexOf("/", 0));
        String a = "YYYYMM";
        System.out.println(a.substring(0,3)+"年"+a.substring(4,5));

        String testStr = "//dddd/sdf/cvd//df///44//3/a";
        String[] result = split2(testStr);
        System.out.println(testStr+"转换成了"+StringHelper.toJsonString(result));
//        Map<String, String> map = new HashMap<String, String>(16);
//        while (true) {
//            if(array)
//        }
//        cycle(100, 7);
    }

    //
    public void test(Object a, Object b) {
        synchronized (a){
            try {
                a.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (b) {
                a.notify();
            }
        }
    }
    public static String[] split2(String str) {
        List<String> arrayList = new ArrayList<String>();
//        return str.split("/+");
        char[] sa = str.toCharArray();
        int fromIndex=0;
        int endIndex=0;
        for(int i = 0;i<str.length();i++) {
            if('/'==sa[i]){
                endIndex=i;
                if(endIndex-fromIndex>1){
                    arrayList.add(str.substring(fromIndex+1, endIndex));
                }
                fromIndex=i;
            }else{
            }
        }
        String[] a = new String[arrayList.size()];
        return   arrayList.toArray(a);
    }

    public static String[] split1(String str) {
        List<String> arrayList = new ArrayList<String>();
        int off = 0;
        int index =0;
        while (true) {
            index = str.indexOf("/", off);
            if (index == -1) {
                if (off < str.length()) {
                    arrayList.add(str.substring(off));
                }
                break;
            }else{
                if (index - off > 0) {
                    arrayList.add(str.substring(off, index));
                }
                off = index + 1;
            }

        }
        String[] a = new String[arrayList.size()];
        return   arrayList.toArray(a);
    }
    /**
     *
     * @param total 总共有多少个元素
     * @param k 数到第几个剔除
     * @return
     */
    public static synchronized int cycle(int total, int k) {
        List<Integer> dataList = new ArrayList<>(); // 创建链表对象
        for (int i = 0; i < total; i++)                  // 添加数据元素
            dataList.add(new Integer(i + 1));
        int index = -1;    // 定义下标，模拟已经去掉一个元素，因此从-1开始
        int loopTime = 1;
        while (dataList.size() > 1) {
            index += k;// 一直循环去除数据，直到只剩下一个元素
            int i  = index % dataList.size();          // 得到应该出局的下标
            Integer removeNum = dataList.remove(i);
            System.out.println("数到第"+(index+loopTime++)+"个元素"+removeNum+",list是"+ StringHelper.toJsonString(dataList));
            index-- ;// 去除元素
        }

        return ((Integer) dataList.get(0)).intValue();      // 返回它的值
    }



}
