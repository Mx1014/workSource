package com.everhomes.filedownload;

import com.everhomes.rest.filedownload.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TaskService {

    /**
     * 创建任务
     * @param name 任务名称
     * @param type 任务类型 参考{@link TaskType}
     * @param taskClass 实现了JobHandler的任务类
     * @param params 任务的业务参数
     * @param repeatFlag 是否支持重复执行 参考{@link TaskRepeatFlag}
     * @param startTime 执行时间，为空时立刻加入任务队列
     * @return 任务id
     */
    Long createTask(String name, Byte type, Class taskClass, Map<String, Object> params, Byte repeatFlag, Date startTime);

    /**
     * 取消任务
     * @param taskId 任务进度
     */
    void cancelTask(Long taskId);

    /**
     * 更新任务状态及错误信息
     * @param taskId 任务id
     * @param status 任务状态 参考{@link TaskStatus}
     * @param errorDesc 错误信息
     */
    void updateTaskStatus(Long taskId, Byte status, String errorDesc);

    /**
     * 更新任务进度比例到redis和mysql
     * @param taskId 任务id
     * @param process 任务进度百分数
     */
    void updateTaskProcess(Long taskId, Integer process);

    List<Task> listTasks(Integer namespaceId, Long communityId, Long orgId, Long userId, Byte type, Byte status, Long pageAnchor, Integer count);

    List<Long> listWaitingTaskIds();
}
