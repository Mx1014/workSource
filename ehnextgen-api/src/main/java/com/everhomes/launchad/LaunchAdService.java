// @formatter:off
package com.everhomes.launchad;

import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.rest.launchad.CreateOrUpdateLaunchAdCommand;
import com.everhomes.rest.launchad.GetLaunchAdCommand;
import com.everhomes.rest.launchad.LaunchAdDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by xq.tian on 2016/12/9.
 */
public interface LaunchAdService {

    /**
     * 获取启动广告
     * @param cmd
     */
    LaunchAdDTO getLaunchAd(GetLaunchAdCommand cmd);

    /**
     * 设置启动广告数据
     */
    LaunchAdDTO createOrUpdateLaunchAd(CreateOrUpdateLaunchAdCommand cmd);

    /**
     * 上传附件
     */
    UploadCsFileResponse uploadLaunchAdFile(MultipartFile[] files);

}
