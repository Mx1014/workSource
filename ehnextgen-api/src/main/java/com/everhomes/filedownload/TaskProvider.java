package com.everhomes.filedownload;

import java.util.List;

public interface TaskProvider {
    void createTask(Task task);

    void updateTask(Task task);

    Task findById(Long id);

    List<Task> listTask(Integer namespaceId, Long communityId, Long orgId, Long userId, Byte type, Byte status, Long pageAnchor, Integer count);

    List<Long> listWaitingTaskIds();
}
