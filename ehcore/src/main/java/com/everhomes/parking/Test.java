package com.everhomes.parking;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Administrator on 2017/5/3.
 */
public class Test {
    public static void main(String[] args) {
//        Thread a = new Thread();  a.start();a.start();

        Test t = new Test();
        try {
            t.wait(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t.notify();
//        for (int i = 0; i < 20; i++) {
////            double r = Math.random();
////            System.out.println(r);
////            System.out.println((int)(r *1000));
////            System.out.println(generateRandomNumber(3));
//            System.out.println(createOrderNo3());;
//        }
////        testDate();
//        System.out.println(Math.pow(10,2));
//        System.out.println(generateRandomNumber(3));
    }

    private static Long createOrderNo3() {
        String bill = String.valueOf(System.currentTimeMillis()) + generateRandomNumber1(3);
        return Long.valueOf(bill);
    }

    /**
     *
     * @param n 创建n位随机数
     * @return
     */
    private static  long generateRandomNumber1(int n){
        return (long)((Math.random() * 9 + 1) * Math.pow(10, n-1));
    }

    private static Long createOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String prefix = sdf.format(new Date());
        String suffix = String.valueOf(generateRandomNumber(3));
        return Long.valueOf(prefix + suffix);
    }

    private static Long createOrderNo(Long time) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//		String prefix = sdf.format(new Date());
        String suffix = String.valueOf(generateRandomNumber(3));

        return Long.valueOf(String.valueOf(time) + suffix);
    }

    protected static long generateRandomNumber(int n){
        return (long)((Math.random() * 9 + 1) * Math.pow(10, n-1));
    }
    public static void testDate()
    {
        Date date = new Date();
        long timeMill = date.getTime();
        System.out.println(timeMill);
        Random rand = new Random(timeMill);
        for(int i = 0; i < 20; i++)
        {
            System.out.println(rand.nextInt(50));
        }
    }
}
