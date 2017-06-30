package com.everhomes.parking;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.parking.bosigao.BosigaoTempFee;
import com.everhomes.rest.parking.ParkingTempFeeDTO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Administrator on 2017/5/3.
 */
public class ParkingTest {

    public static BosigaoTempFee createTestTempFee() {
        String dataJson = "{\"result\":\"0\",\"data\":{\"PlateNumber\":\"粤B22222\",\"CardNo\":\"1294\",\"ParkingID\":\"8007c821-6c53-49a3-9248-a75c010f096b\",\"ParkName\":\"金融基地停车场\",\"EntranceDate\":\"20170630090842\",\"Pkorder\":{\"PayTime\":\"00010101000000\",\"ID\":0,\"RecordID\":\"0e70f22f-3901-4dfc-9895-5fc7a2a0a4eb\",\"TagID\":\"B8F97E01-3606-405E-8B5F-E11C3E6E320A\",\"OrderType\":1,\"OrderID\":\"17062411271376510024\",\"PayWay\":0,\"PayWayName\":\"\",\"Amount\":900,\"OutstandingAmount\":900,\"PayAmount\":0,\"Status\":0,\"OrderSource\":6,\"OrderSourceName\":\"\",\"OrderTime\":\"20170624112752\",\"UserID\":null,\"Operator\":null,\"Remark\":\"临停缴费\",\"OldUserulDate\":\"00010101000000\",\"NewUsefulDate\":\"00010101000000\",\"OldMoney\":0,\"NewMoney\":0,\"Update_Time\":\"20170624112752\",\"IsUpdate\":1,\"SystemID\":\"16073113864\",\"CompanyID\":\"cd231b3b-6df9-4fa4-9da5-a75c010e6fcf\",\"ParkingID\":\"8007c821-6c53-49a3-9248-a75c010f096b\",\"RState\":0,\"OnlineUserID\":null,\"OnlineOrderID\":null,\"CompanyName\":\"\",\"CardNo\":\"\",\"DiscountAmount\":0,\"CarderateID\":null,\"Owner\":\"\",\"PlatformName\":\"\",\"ParkingName\":\"\",\"OrderTypeName\":\"\",\"InParkid\":\"630669\",\"KeyName\":\"\",\"CashAmount\":0,\"OnLineAmount\":0,\"EntranceDate\":\"00010101000000\",\"ExitDate\":\"00010101000000\",\"PlateNumber\":\"\",\"LongTime\":\"\",\"CashTime\":\"00010101000000\",\"CashMoney\":0},\"OutTime\":15,\"isAdd\":false,\"Result\":0,\"PayDate\":\"20170624112713\"}}";
        BosigaoTempFee tempFee = JSONObject.parseObject(dataJson, BosigaoTempFee.class);


        return tempFee;
    }

    public static void main(String[] args) {
//        Thread a = new Thread();  a.start();a.start();

//        ParkingTest t = new ParkingTest();
//        try {
//            t.wait(0);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        t.notify();
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
