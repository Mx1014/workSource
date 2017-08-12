// @formatter:off
package com.everhomes.barcode;

import com.everhomes.BarCodeService.BarcodeService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.qrcode.QRCodeService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.barcode.BarcodeDTO;
import com.everhomes.rest.barcode.CheckBarcodeCommand;
import com.everhomes.rest.qrcode.GetQRCodeImageCommand;
import com.everhomes.rest.qrcode.GetQRCodeInfoCommand;
import com.everhomes.rest.qrcode.NewQRCodeCommand;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/barcode")
public class BarcodeController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(BarcodeController.class);

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
