package com.everhomes.rest.barcode;

/**
 * Created by Administrator on 2017/8/12.
 */

/**
 * <ul>
 *     <li>barcode: 条形码号码</li>
 * </ul>
 */
public class CheckBarcodeCommand {
    private String barcode;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
