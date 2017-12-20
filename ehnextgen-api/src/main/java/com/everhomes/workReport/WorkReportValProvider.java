package com.everhomes.workReport;

import com.everhomes.rest.workReport.ListWorkReportsValCommand;
import org.jooq.Record;
import org.jooq.RecordMapper;

import java.util.List;

public interface WorkReportValProvider {

    Long createWorkReportVal(WorkReportVal val);

    void deleteWorkReportVal(WorkReportVal val);

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

    Integer countUnReadWorkReportsVal(Integer namespaceId, Long receiverId);

    void markWorkReportsValReading(Integer namespaceId, Long receiverId);

    Long createWorkReportValComment(WorkReportValComment comment);

    void deleteWorkReportValComment(WorkReportValComment comment);

    WorkReportValComment getWorkReportValCommentById(Long commentId);

    List<WorkReportValComment> listWorkReportValComments(Integer namespaceId, Long reportValId, Long pageAnchor,Integer pageSize);

    void createWorkReportValCommentAttachment(WorkReportValCommentAttachment attachment);

    void deleteCommentAttachmentsByCommentId(Integer namespaceId, Long commentId);

    List<WorkReportValCommentAttachment> listWorkReportValCommentAttachments(Integer namespaceId, List<Long> commentIds);
}
