package com.everhomes.filedownload;

import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.banner.BannerScope;
import com.everhomes.rest.banner.BannerStatus;
import com.everhomes.rest.launchpad.ApplyPolicy;

import java.util.List;
import java.util.Map;


public interface FileDownloadProvider {
    void createFileDownloadJob(FileDownloadJob job);

    void updateFileDownloadJob(FileDownloadJob job);

    FileDownloadJob findById(Long id);

    List<FileDownloadJob> listFileDownloadJobsByOwnerId(Long ownerId, Long pageAnchor, int count);
}
