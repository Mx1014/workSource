// @formatter:off
package com.everhomes.BarCodeService;

import com.everhomes.rest.barcode.BarcodeDTO;
import com.everhomes.rest.barcode.CheckBarcodeCommand;

public interface BarcodeService {
    /**
     * 检查条形码对应的对象，
     * 1、手写发http请求的类型
     * 2、同一个服务的其他模块，则使用注册-轮询的方式 注册接口参考{@link BarcodeModuleListener}
     * @param cmd
     * @return
     */
    BarcodeDTO checkBarcode(CheckBarcodeCommand cmd);
}
