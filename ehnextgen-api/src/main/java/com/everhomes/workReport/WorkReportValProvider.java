package com.everhomes.workReport;

import java.util.List;

public interface WorkReportValProvider {

    Long createWorkReportVal(WorkReportVal val);

    void updateWorkReportVal(WorkReportVal val);

    WorkReportVal getWorkReportValById(Long id);

    void createWorkReportValReceiverMap(WorkReportValReceiverMap receiver);

    void deleteReportValReceiverByValId(Long valId);

    List<WorkReportValReceiverMap> listReportValReceiversByValId(Long reportValId);

}
