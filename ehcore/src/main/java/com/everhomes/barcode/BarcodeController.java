// @formatter:off
package com.everhomes.barcode;

import com.everhomes.BarCodeService.BarcodeService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.barcode.BarcodeDTO;
import com.everhomes.rest.barcode.CheckBarcodeCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/barcode")
public class BarcodeController extends ControllerBase {

    @Autowired
    private BarcodeService barcodeService;
    
    /**
     * <b>URL: /barcode/checkBarcode</b>
     * <p>校验一维码</p>
     */
    @RequestMapping("checkBarcode")
    @RestReturn(value=BarcodeDTO.class)
    public RestResponse checkBarcode(CheckBarcodeCommand cmd) {
        BarcodeDTO barcodeDTO = this.barcodeService.checkBarcode(cmd);
        
        RestResponse response = new RestResponse(barcodeDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


}
