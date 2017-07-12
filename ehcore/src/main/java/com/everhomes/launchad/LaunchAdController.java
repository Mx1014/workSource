// @formatter:off
package com.everhomes.launchad;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.rest.launchad.LaunchAdDTO;
import com.everhomes.rest.launchad.SetLaunchAdCommand;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * App启动广告模块
 * Created by xq.tian on 2016/12/9.
 */
@RestDoc(value = "Launch advertisement controller", site = "core")
@RequestMapping("/launchad")
@RestController
public class LaunchAdController extends ControllerBase {

    @Autowired
    private LaunchAdService launchAdService;

    /**
     * <b>URL: /launchad/getLaunchad</b>
     * <p>获取启动广告数据</p>
     */
    @RequestMapping("getLaunchad")
    @RestReturn(value = LaunchAdDTO.class)
    @RequireAuthentication(false)
    public RestResponse geLaunchAd() {
        return response(launchAdService.getLaunchAd());
    }

    /**
     * <b>URL: /launchad/setLaunchAd</b>
     * <p>设置启动广告信息</p>
     */
    @RequestMapping("setLaunchAd")
    @RestReturn(value = LaunchAdDTO.class)
    public RestResponse setLaunchAd(SetLaunchAdCommand cmd) {
        return response(launchAdService.setLaunchAd(cmd));
    }

    /**
     * <b>URL: /launchad/uploadLaunchAdFile</b>
     * <p>上传广告附件</p>
     */
    @RequestMapping("uploadLaunchAdFile")
    @RestReturn(value = UploadCsFileResponse.class)
    public RestResponse uploadLaunchAdFile(@RequestParam("attachment") MultipartFile [] files) {
        return response(launchAdService.uploadLaunchAdFile(files));
    }

    /*private RestResponse success() {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }*/

    private RestResponse response(Object o) {
        RestResponse response = new RestResponse(o);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }
}
