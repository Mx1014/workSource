// @formatter:off
package com.everhomes.qrcode;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.forum.NewTopicCommand;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.qrcode.GetQRCodeImageCommand;
import com.everhomes.rest.qrcode.GetQRCodeInfoCommand;
import com.everhomes.rest.qrcode.NewQRCodeCommand;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.rest.region.ListRegionCommand;
import com.everhomes.rest.region.RegionAdminStatus;
import com.everhomes.rest.region.RegionDTO;
import com.everhomes.rest.region.RegionScope;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.FileHelper;
import com.everhomes.util.QRCodeConfig;
import com.everhomes.util.QRCodeEncoder;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;
import com.everhomes.util.RequireAuthentication;

@RestController
@RequestMapping("/qrcode")
public class QRCodeController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(QRCodeController.class);

    @Autowired
    private QRCodeService qrcodeService;
    
    /**
     * <b>URL: /qrcode/newQRCode</b>
     * <p>创建新二维码</p>
     */
    @RequestMapping("newQRCode")
    @RestReturn(value=QRCodeDTO.class)
    public RestResponse newQRCode(@Valid NewQRCodeCommand cmd) {
        QRCodeDTO qrcodeDto = this.qrcodeService.createQRCode(cmd);
        
        RestResponse response = new RestResponse(qrcodeDto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /qrcode/getQRCodeInfo</b>
     * <p>获取二维码信息</p>
     */
    @RequestMapping("getQRCodeInfo")
    @RestReturn(value=QRCodeDTO.class)
    public RestResponse getQRCodeInfo(@Valid GetQRCodeInfoCommand cmd) {
        QRCodeDTO qrcodeDto = this.qrcodeService.getQRCodeInfo(cmd);
        
        RestResponse response = new RestResponse(qrcodeDto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /qrcode/getQRCodeImage</b>
     * <p>获取二维码图片</p>
     */
    @RequireAuthentication(false)
    @RequestMapping(value="getQRCodeImage")
    public ModelAndView getQRCodeImage(GetQRCodeImageCommand cmd, 
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        String qrid = cmd.getQrid();
        QRCodeDTO qrcode = this.qrcodeService.getQRCodeInfoById(qrid);
        
        BufferedOutputStream bos = null;
        ByteArrayOutputStream out = null;
        try {
            Integer width = cmd.getWidth();
            if(width == null) {
                width = 100;
            }
            Integer height = cmd.getHeight();
            if(height == null) {
                height = 100;
            }
            BufferedImage image = QRCodeEncoder.createQrCode(qrcode.getUrl(), width, height, qrcode.getLogoUrl());
            out = new ByteArrayOutputStream();
            ImageIO.write(image, QRCodeConfig.FORMAT_PNG, out);
            
            String fileName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + "." + QRCodeConfig.FORMAT_PNG;
            response.setContentType("application/octet-stream;");
            response.setHeader("Content-disposition", "attachment; filename="
                    + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
            response.setHeader("Content-Length", String.valueOf(out.size()));  
            
            bos = new BufferedOutputStream(response.getOutputStream());
            ImageIO.write(image, QRCodeConfig.FORMAT_PNG, bos);
        } catch (Exception e) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Failed to download the package file");
        } finally {
            FileHelper.closeOuputStream(out);
            FileHelper.closeOuputStream(bos);
        }          

        return null;
    }
}
