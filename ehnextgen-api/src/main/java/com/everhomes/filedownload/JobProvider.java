package com.everhomes.filedownload;

import java.util.List;

public interface JobProvider {
    void createJob(Job job);

    void updateJob(Job job);

    Job findById(Long id);

    List<Job> listJobs(Integer namespaceId, Long communityId, Long orgId, Long userId, Byte type, Byte status, Long pageAnchor, int count);
}
