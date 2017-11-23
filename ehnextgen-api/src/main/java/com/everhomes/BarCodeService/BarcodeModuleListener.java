package com.everhomes.BarCodeService;

import com.everhomes.rest.barcode.BarcodeDTO;
import com.everhomes.rest.barcode.CheckBarcodeCommand;

/**
 * Created by Administrator on 2017/8/12.
 */
public interface BarcodeModuleListener {

    BarcodeDTO checkBarCode(CheckBarcodeCommand cmd);

}
