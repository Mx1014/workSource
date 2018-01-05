package com.everhomes.relocation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author sw on 2017/11/22.
 */
public class Test {
    public static void main(String[] args) {
//        String content = "申请人：${requestorName}  企业名称：${requestorEnterpriseName}\\r\\n搬迁物品：${items}\\r\\n搬迁时间：${relocationDate}";
//        int lineBreak = content.indexOf("\\r\\n");
//        content = content.substring(lineBreak + 4);
//
//        System.out.println(content);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            System.out.println(sdf.parse("2017-12-01").getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = DriverManager.getConnection("jdbc:mysql://10.1.10.102/ehcore", "ehcore", "ehcore");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
