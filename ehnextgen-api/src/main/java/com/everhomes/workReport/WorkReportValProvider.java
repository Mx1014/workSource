package com.everhomes.workReport;

import com.everhomes.rest.workReport.ListWorkReportsValCommand;

import java.sql.Timestamp;
import java.util.List;

public interface WorkReportValProvider {

    Long createWorkReportVal(WorkReportVal val);

    void updateWorkReportVal(WorkReportVal val);

    WorkReportVal getWorkReportValById(Long id);

    WorkReportVal getValidWorkReportValById(Long id);

    List<WorkReportVal> listWorkReportValsByApplierIds(Integer namespaceId, Integer pageOffset, Integer pageSize, Long ownerId, String ownerType, List<Long> applierIds);

    List<WorkReportVal> listWorkReportValsByReceiverId(Integer namespaceId, Integer pageOffset, Long receiverId, ListWorkReportsValCommand cmd);

    void createWorkReportValReceiverMap(WorkReportValReceiverMap receiver);

    void updateWorkReportValReceiverMap(WorkReportValReceiverMap receiver);

    void deleteReportValReceiverByValId(Long valId);

    WorkReportValReceiverMap getWorkReportValReceiverByReceiverId(Integer namespaceId, Long reportValId, Long receiverId);

    List<WorkReportValReceiverMap> listReportValReceiversByValId(Long reportValId);

    Integer countUnReadWorkReportsVal(Integer namespaceId, Long organizationId, Long receiverId);

    void markWorkReportsValReading(Integer namespaceId, Long organizationId, Long receiverId);

    Long createWorkReportValComment(WorkReportValComment comment);

    void deleteWorkReportValComment(WorkReportValComment comment);

    WorkReportValComment getWorkReportValCommentById(Long commentId);

    List<WorkReportValComment> listWorkReportValComments(Integer namespaceId, Long reportValId, Integer offset, Integer pageSize);

    Integer countWorkReportValComments(Integer namespaceId, Long reportValId);

    void createWorkReportValCommentAttachment(WorkReportValCommentAttachment attachment);

    void deleteCommentAttachmentsByCommentId(Integer namespaceId, Long commentId);

    List<WorkReportValCommentAttachment> listWorkReportValCommentAttachments(Integer namespaceId, List<Long> commentIds);

    Long createWorkReportValReceiverMsg(WorkReportValReceiverMsg msg);

    void deleteReportValReceiverMsgByValId(Long reportValId);

    void deleteReportValReceiverMsg();

    void updateWorkReportValReceiverMsg(WorkReportValReceiverMsg msg);

    List<WorkReportValReceiverMsg> listReportValReceiverMsgByTime(java.sql.Timestamp startTime, java.sql.Timestamp endTime);

    List<WorkReportValReceiverMsg> listReportValReceiverMsgByReportTime(Long reportId, java.sql.Date reportTime);

    List<WorkReportValReceiverMap> listWorkReportReceivers();

    List<WorkReportVal> listWorkReportVals();
}
