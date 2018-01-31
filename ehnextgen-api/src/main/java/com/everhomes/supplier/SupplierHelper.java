//@formatter:off
package com.everhomes.supplier;

/**
 * Created by Wentian Wang on 2018/1/23.
 */

public class SupplierHelper {
    static String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
    public static String getIdentity() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 7; i++) {
            char a = chars.charAt((int) (Math.random() * 36));
            sb.append(a);
        }
        return sb.toString();
    }
}
