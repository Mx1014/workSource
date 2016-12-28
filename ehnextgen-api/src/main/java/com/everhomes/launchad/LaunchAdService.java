// @formatter:off
package com.everhomes.launchad;

import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.rest.launchad.LaunchAdDTO;
import com.everhomes.rest.launchad.SetLaunchAdCommand;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by xq.tian on 2016/12/9.
 */
public interface LaunchAdService {

    /**
     * 获取启动广告
     */
    LaunchAdDTO getLaunchAd();

    /**
     * 设置启动广告数据
     */
    LaunchAdDTO setLaunchAd(SetLaunchAdCommand cmd);

    /**
     * 上传附件
     */
    UploadCsFileResponse uploadLaunchAdFile(MultipartFile[] files);
}
