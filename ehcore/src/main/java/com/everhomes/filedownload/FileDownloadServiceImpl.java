// @formatter:off
package com.everhomes.filedownload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileDownloadServiceImpl implements FileDownloadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDownloadServiceImpl.class);

    @Override
    public Long createJob(String fileName, List<Object> rows, List<CellMapper> cellMappers) {
        return null;
    }

    @Override
    public void cancelJob(Long jobId) {

    }
}
