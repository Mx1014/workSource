package com.everhomes.techpark.punch;

import com.everhomes.approval.ApprovalCategory;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.techpark.punch.admin.BatchUpdateVacationBalancesCommand;
import com.everhomes.rest.techpark.punch.admin.ExportVacationBalancesCommand;
import com.everhomes.rest.techpark.punch.admin.ImportVacationBalancesCommand;
import com.everhomes.rest.techpark.punch.admin.ListVacationBalanceLogsCommand;
import com.everhomes.rest.techpark.punch.admin.ListVacationBalanceLogsResponse;
import com.everhomes.rest.techpark.punch.admin.ListVacationBalancesCommand;
import com.everhomes.rest.techpark.punch.admin.ListVacationBalancesResponse;
import com.everhomes.rest.techpark.punch.admin.UpdateVacationBalancesCommand;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.math.BigDecimal;

public interface PunchVacationBalanceService {

    void updateVacationBalancesAndSendMessage(UpdateVacationBalancesCommand cmd);

    PunchVacationBalance updateVacationBalances(UpdateVacationBalancesCommand cmd);

    void addVacationBalanceHistoryCount(Long userId, Long organizationId, BigDecimal approvalAnnualLeaveDays, BigDecimal approvalOvertimeDays);

    void batchUpdateVacationBalances(BatchUpdateVacationBalancesCommand cmd);

    ListVacationBalanceLogsResponse listVacationBalanceLogs(ListVacationBalanceLogsCommand cmd);

    ListVacationBalancesResponse listVacationBalances(ListVacationBalancesCommand cmd);

    void exportVacationBalances(ExportVacationBalancesCommand cmd);

    ImportFileTaskDTO importVacationBalances(MultipartFile[] files, ImportVacationBalancesCommand cmd);

    OutputStream getVacationBalanceOutputStream(Long ownerId, Long taskId);

    void sendMessageWhenVacationBalanceChanged(OrganizationMemberDetails memberDetail, String content);

    String buildVacationBalanceChangedNotificationContent(Double annualLeaveAdd, Double overtimeAdd, PunchExceptionRequest request, PunchVacationBalance balance, ApprovalCategory approvalCategory, Integer notificationCode);

}
