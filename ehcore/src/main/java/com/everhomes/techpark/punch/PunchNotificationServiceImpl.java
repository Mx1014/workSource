package com.everhomes.techpark.punch;

import com.everhomes.listing.ListingLocator;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.general_approval.GeneralApprovalAttribute;
import com.everhomes.rest.organization.EmployeeStatus;
import com.everhomes.rest.techpark.punch.HommizationType;
import com.everhomes.rest.techpark.punch.NormalFlag;
import com.everhomes.rest.techpark.punch.PunchRuleStatus;
import com.everhomes.rest.techpark.punch.PunchRuleType;
import com.everhomes.rest.techpark.punch.PunchTimeIntervalDTO;
import com.everhomes.rest.techpark.punch.PunchType;
import com.everhomes.scheduler.PunchNotificationScheduleJob;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhPunchNotifications;
import com.everhomes.uniongroup.UniongroupConfigureProvider;
import com.everhomes.uniongroup.UniongroupMemberDetail;
import com.everhomes.uniongroup.UniongroupVersion;
import com.everhomes.util.ExecutorUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PunchNotificationServiceImpl implements PunchNotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PunchNotificationServiceImpl.class);
    private static final Long ONE_MINUTE_MS = 60 * 1000L;
    private static final Long ONE_DAY_MS = 24 * 3600 * 1000L;

    @Autowired
    private PunchProvider punchProvider;
    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private UniongroupConfigureProvider uniongroupConfigureProvider;
    @Autowired
    private ScheduleProvider scheduleProvider;
    @Autowired
    private PunchBaseService punchBaseService;

    /**
     * 功能说明：打卡提醒
     * 初始化时机：考勤规则生效以后执行，初始化打卡提醒记录，提醒发出以后会更新数据状态为无效，第二天删除，保证表数据量始终很小（数据仅保留一天）
     * 全量：首先考勤规则生效后全量初始化提醒数据
     * 增量：每次提前打卡完成或请假申请审批通过后增量修改对应打卡提醒记录为无效（不需要提醒）
     * 注意：当天的提醒数据不删除而置为INVALID,是为了出现异常提醒时，可以通过记录来查看问题原因，第二天再清除数据
     */
    @Override
    public void punchNotificationInitialize() {
        // step1 : 删除今天之前遗留的数据，避免数据堆积
        java.sql.Date punchDate = java.sql.Date.valueOf(LocalDate.now());
        punchProvider.deleteAllPunchNotificationsBeforeDate(punchDate);
        // step2 : 查找开启了打卡提醒功能的固定排班的考勤规则
        List<PunchRule> punchRules = punchProvider.queryPunchRules(new ListingLocator(), Integer.MAX_VALUE - 1, (locator, query) -> {
            query.addConditions(Tables.EH_PUNCH_RULES.STATUS.eq(PunchRuleStatus.ACTIVE.getCode()));
            query.addConditions(Tables.EH_PUNCH_RULES.RULE_TYPE.eq(PunchRuleType.GUDING.getCode()));
            query.addConditions(Tables.EH_PUNCH_RULES.PUNCH_REMIND_FLAG.eq(NormalFlag.YES.getCode()));
            return query;
        });
        if (CollectionUtils.isEmpty(punchRules)) {
            return;
        }
        // step3 : 遍历考勤规则，获取考勤规则下的成员，提前生成每个打卡时间的待提醒记录（当天的全量初始化操作，而后每一次请假单审批通过和打卡的动作会对该数据进行增量更新）
        for (PunchRule pr : punchRules) {
            try {
                punchNotificationInitializeByPunchRule(punchDate, pr);
            } catch (Exception e) {
                // 忽略单个错误，避免全局影响
                LOGGER.error(String.format("punch notification init error,ruleId = {}", pr.getId()), e);
            }
        }
    }

    private void punchNotificationInitializeByPunchRule(java.sql.Date punchDate, PunchRule pr) {
        PunchTimeRule ptr = punchBaseService.getPunchTimeRuleGuDing(pr, punchDate);
        if (ptr == null || ptr.getId() == null || ptr.getId() == 0) {
            return;
        }
        List<PunchTimeIntervalDTO> timeIntervals = punchBaseService.findPunchTimeIntervals(ptr);
        if (CollectionUtils.isEmpty(timeIntervals)) {
            return;
        }
        UniongroupVersion currentVersion = punchBaseService.getPunchGroupVersion(pr.getOwnerId());
        List<UniongroupMemberDetail> uniongroupMemberDetails = this.uniongroupConfigureProvider.listUniongroupMemberDetail(currentVersion.getNamespaceId(), pr.getPunchOrganizationId(), pr.getOwnerId(), currentVersion.getCurrentVersionCode());
        if (CollectionUtils.isEmpty(uniongroupMemberDetails)) {
            return;
        }
        // 防止重复创建（避免某些异常发生时'如定时任务被执行多次'时不会重复创建记录）
        boolean hasInit = punchProvider.countPunchNotifications(currentVersion.getNamespaceId(), pr.getOwnerId(), pr.getId(), punchDate) > 0;
        if (hasInit) {
            return;
        }
        // 动态创建对应各个打卡时间的调度任务
        createScheduleJobDynamic(pr, ptr, punchDate, currentVersion.getNamespaceId(), timeIntervals);

        List<Long> detailIds = new ArrayList<>();
        for (UniongroupMemberDetail detail : uniongroupMemberDetails) {
            detailIds.add(detail.getDetailId());
        }
        Map<Long, OrganizationMemberDetails> organizationMemberDetailsMap = getActiveAndTrackOrganizationMemberDetailsMap(pr.getOwnerId(), detailIds);
        Map<Long, List<PunchServiceImpl.TimeInterval>> agreementPunchExceptionRequestMapByUserId = agreementPunchExceptionRequestMapByUserId(pr.getOwnerId(), punchDate);
        List<EhPunchNotifications> punchNotifications = new ArrayList<>();
        for (UniongroupMemberDetail uniongroupMemberDetail : uniongroupMemberDetails) {
            OrganizationMemberDetails memberDetail = organizationMemberDetailsMap.get(uniongroupMemberDetail.getDetailId());
            if (memberDetail == null) {
                continue;
            }
            List<PunchServiceImpl.TimeInterval> agreementPunchExceptionRequests = agreementPunchExceptionRequestMapByUserId.get(memberDetail.getTargetId());
            int punchIntervalNo = 1;
            for (PunchTimeIntervalDTO timeIntervalDTO : timeIntervals) {
                punchNotifications.add(buildPunchNotification(punchDate, memberDetail, pr, ptr, timeIntervalDTO, PunchType.ON_DUTY, punchIntervalNo, timeIntervals.size(), agreementPunchExceptionRequests));
                punchNotifications.add(buildPunchNotification(punchDate, memberDetail, pr, ptr, timeIntervalDTO, PunchType.OFF_DUTY, punchIntervalNo, timeIntervals.size(), agreementPunchExceptionRequests));
                punchIntervalNo++;
            }
            if (punchNotifications.size() >= 100) {
                punchProvider.batchCreatePunchNotifications(punchNotifications);
                punchNotifications.clear();
            }
        }

        if (punchNotifications.size() > 0) {
            punchProvider.batchCreatePunchNotifications(punchNotifications);
        }
    }

    /**
     * 按照设置的打卡时间启动定时任务:
     * 无或弹性打卡:1、上班提醒时间 = 上班时间 - 提前时间   2、下班提醒时间 = 下班时间
     * 晚到晚走: 1、上班提醒时间 = 上班时间 + 晚到时间 - 提前时间 2、下班提醒时间 = 下班时间 + 晚到时间
     */
    private void createScheduleJobDynamic(PunchRule pr, PunchTimeRule ptr, java.sql.Date punchDate, Integer namespaceId, List<PunchTimeIntervalDTO> timeIntervals) {
        if (CollectionUtils.isEmpty(timeIntervals)) {
            return;
        }

        int punchIntervalNo = 1;
        int scheduleNo = 1;  // 避免任务重名
        for (PunchTimeIntervalDTO interval : timeIntervals) {
            String scheduleOnDuty = PunchNotificationScheduleJob.PUNCH_NOTIFICATION_SCHEDULE + pr.getId() + "-" + System.currentTimeMillis() + "-" + scheduleNo++;
            String scheduleOffDuty = PunchNotificationScheduleJob.PUNCH_NOTIFICATION_SCHEDULE + pr.getId() + "-" + System.currentTimeMillis() + "-" + scheduleNo++;
            Timestamp onDuty = new Timestamp(punchDate.getTime() + interval.getArriveTime());
            Timestamp offDuty = new Timestamp(punchDate.getTime() + interval.getLeaveTime());
            // 最晚上班时间(晚到晚走)
            Timestamp flexOnDuty = new Timestamp(onDuty.getTime());
            Timestamp onDutyRemindTime = new Timestamp(onDuty.getTime() - pr.getRemindMinutesOnDuty() * ONE_MINUTE_MS);
            Timestamp offDutyRemindTime = new Timestamp(offDuty.getTime());

            // 晚到是指第一次上班卡，晚走是指最后一次下班卡
            // 晚到晚走:上班提醒时间 = 上班时间 + 晚到时间 - 提前时间    下班提醒时间 = 下班时间 + 晚到时间
            if (HommizationType.LATEARRIVE == HommizationType.fromCode(ptr.getHommizationType()) && ptr.getFlexTimeLong() != null && ptr.getFlexTimeLong() > 0) {
                if (punchIntervalNo == 1) {
                    flexOnDuty = new Timestamp(onDuty.getTime() + ptr.getFlexTimeLong());
                    onDutyRemindTime = new Timestamp(onDuty.getTime() + ptr.getFlexTimeLong() - pr.getRemindMinutesOnDuty() * ONE_MINUTE_MS);
                }
                if (punchIntervalNo == timeIntervals.size()) {
                    offDutyRemindTime = new Timestamp(offDuty.getTime() + ptr.getFlexTimeLong());
                }
            }

            Map<String, Object> baseParams = new HashMap<>();
            baseParams.put("namespaceId", namespaceId);
            baseParams.put("organizationId", pr.getOwnerId());
            baseParams.put("punchRuleId", pr.getId());
            baseParams.put("punchDate", punchDate);

            Map<String, Object> paramsOnDuty = new HashMap<>();
            paramsOnDuty.putAll(baseParams);
            paramsOnDuty.put("punchRuleTime", onDuty);
            paramsOnDuty.put("flexOnDuty", flexOnDuty);
            paramsOnDuty.put("punchType", PunchType.ON_DUTY.getCode());
            paramsOnDuty.put("punchIntervalNo", punchIntervalNo);

            Map<String, Object> paramsOffDuty = new HashMap<>();
            paramsOffDuty.putAll(baseParams);
            paramsOffDuty.put("punchRuleTime", offDuty);
            paramsOffDuty.put("punchType", PunchType.OFF_DUTY.getCode());
            paramsOffDuty.put("punchIntervalNo", punchIntervalNo);

            scheduleProvider.scheduleSimpleJob(scheduleOnDuty, scheduleOnDuty, onDutyRemindTime, PunchNotificationScheduleJob.class, paramsOnDuty);
            scheduleProvider.scheduleSimpleJob(scheduleOffDuty, scheduleOffDuty, offDutyRemindTime, PunchNotificationScheduleJob.class, paramsOffDuty);

            punchIntervalNo++;
        }
    }

    private Map<Long, List<PunchServiceImpl.TimeInterval>> agreementPunchExceptionRequestMapByUserId(Long organizationId, java.sql.Date punchDate) {
        List<PunchExceptionRequest> punchExceptionRequests = punchProvider.listPunchExceptionRequestBetweenBeginAndEndTime(null, organizationId, new Timestamp(punchDate.getTime()), new Timestamp(punchDate.getTime() + ONE_DAY_MS));
        if (CollectionUtils.isEmpty(punchExceptionRequests)) {
            return new HashMap<>();
        }
        Map<Long, List<PunchServiceImpl.TimeInterval>> map = new HashMap<>();
        for (PunchExceptionRequest request : punchExceptionRequests) {
            if (com.everhomes.rest.approval.ApprovalStatus.AGREEMENT != com.everhomes.rest.approval.ApprovalStatus.fromCode(request.getStatus())) {
                continue;
            }
            if (GeneralApprovalAttribute.OVERTIME.getCode().equals(request.getApprovalAttribute())) {
                continue;
            }
            List<PunchServiceImpl.TimeInterval> timeIntervals = map.get(request.getUserId());
            if (timeIntervals == null) {
                timeIntervals = new ArrayList<>();
                map.put(request.getUserId(), timeIntervals);
            }
            PunchServiceImpl.TimeInterval timeInterval = new PunchServiceImpl.TimeInterval();
            timeInterval.setBeginTime(request.getBeginTime());
            timeInterval.setEndTime(request.getEndTime());
            timeIntervals.add(timeInterval);
        }
        return map;
    }

    private EhPunchNotifications buildPunchNotification(java.sql.Date punchDate, OrganizationMemberDetails memberDetail, PunchRule pr, PunchTimeRule ptr, PunchTimeIntervalDTO timeIntervalDTO, PunchType punchType, int punchIntervalNo, int punchIntervalCount, List<PunchServiceImpl.TimeInterval> exceptionRequests) {
        EhPunchNotifications punchNotification = new EhPunchNotifications();
        punchNotification.setNamespaceId(memberDetail.getNamespaceId());
        punchNotification.setEnterpriseId(memberDetail.getOrganizationId());
        punchNotification.setUserId(memberDetail.getTargetId());
        punchNotification.setDetailId(memberDetail.getId());
        punchNotification.setPunchDate(punchDate);
        punchNotification.setPunchIntervalNo(punchIntervalNo);
        punchNotification.setPunchType(punchType.getCode());
        punchNotification.setPunchRuleId(pr.getId());
        punchNotification.setInvalidFlag(NormalFlag.NO.getCode());
        if (PunchType.ON_DUTY == punchType) {
            // 提前几分钟提醒
            punchNotification.setRuleTime(new Timestamp(punchDate.getTime() + timeIntervalDTO.getArriveTime()));

            if (HommizationType.LATEARRIVE == HommizationType.fromCode(ptr.getHommizationType()) && ptr.getFlexTimeLong() != null && ptr.getFlexTimeLong() > 0) {
                // 晚到是指第一次上班卡: 上班提醒时间 = 上班时间 + 晚到时间 - 提前时间
                if (punchIntervalNo == 1) {
                    punchNotification.setExceptRemindTime(new Timestamp(punchNotification.getRuleTime().getTime() + ptr.getFlexTimeLong() - pr.getRemindMinutesOnDuty() * ONE_MINUTE_MS));
                }
            } else {
                punchNotification.setExceptRemindTime(new Timestamp(punchNotification.getRuleTime().getTime() - pr.getRemindMinutesOnDuty() * ONE_MINUTE_MS));
            }
        } else {
            // 准点提醒
            punchNotification.setRuleTime(new Timestamp(punchDate.getTime() + timeIntervalDTO.getLeaveTime()));

            if (HommizationType.LATEARRIVE == HommizationType.fromCode(ptr.getHommizationType()) && ptr.getFlexTimeLong() != null && ptr.getFlexTimeLong() > 0) {
                // 晚走是指最后一次下班卡:下班提醒时间 = 下班时间 + 晚到时间
                if (punchIntervalNo == punchIntervalCount) {
                    punchNotification.setExceptRemindTime(new Timestamp(punchNotification.getRuleTime().getTime() + ptr.getFlexTimeLong()));
                }
            } else {
                punchNotification.setExceptRemindTime(punchNotification.getRuleTime());
            }
        }
        if (isTimeIntervalFullCovered(punchNotification.getRuleTime(), punchNotification.getRuleTime(), exceptionRequests)) {
            punchNotification.setInvalidFlag(NormalFlag.YES.getCode());
            punchNotification.setInvalidReason("请假覆盖打卡时间，不需要提醒");
        }
        return punchNotification;
    }

    private Map<Long, OrganizationMemberDetails> getActiveAndTrackOrganizationMemberDetailsMap(Long organizationId, List<Long> detailIds) {
        List<OrganizationMemberDetails> details = organizationProvider.queryOrganizationMemberDetails(new ListingLocator(), organizationId, (locator, query) -> {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.in(detailIds));
            query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ORGANIZATION_ID.eq(organizationId));
            query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.EMPLOYEE_STATUS.ne(EmployeeStatus.DISMISSAL.getCode()));
            return query;
        });
        if (CollectionUtils.isEmpty(detailIds)) {
            return new HashMap<>();
        }
        Map<Long, OrganizationMemberDetails> result = new HashMap<>();
        for (OrganizationMemberDetails detail : details) {
            if (detail.getTargetId() != null && detail.getTargetId() > 0) {
                result.put(detail.getId(), detail);
            }
        }
        return result;
    }

    @Override
    public void setPunchNotificationInvalidBackground(OrganizationMemberDetails memberDetail, PunchLog punchLog, Long punchRuleId) {
        if (punchRuleId == null || punchRuleId == 0) {
            return;
        }
        ExecutorUtil.submit(() -> {
            PunchRule pr = punchProvider.getPunchRuleById(punchRuleId);
            if (pr == null || PunchRuleType.GUDING != PunchRuleType.fromCode(pr.getRuleType())) {
                return;
            }
            PunchNotification punchNotification = punchProvider.findPunchNotification(memberDetail.getNamespaceId(), memberDetail.getOrganizationId(), memberDetail.getId(), punchLog.getPunchDate(), PunchType.fromCode(punchLog.getPunchType()), punchLog.getPunchIntervalNo());
            if (punchNotification != null && NormalFlag.NO == NormalFlag.fromCode(punchNotification.getInvalidFlag())) {
                punchNotification.setInvalidReason("提前打卡，不需要提醒");
                punchNotification.setInvalidFlag(NormalFlag.YES.getCode());
                punchProvider.updatePunchNotification(punchNotification);
            }
        });
    }

    @Override
    public void setPunchNotificationInvalidBackground(PunchExceptionRequest request, Integer namespaceId, Date punchDate) {
        if (com.everhomes.rest.approval.ApprovalStatus.AGREEMENT != com.everhomes.rest.approval.ApprovalStatus.fromCode(request.getStatus())) {
            return;
        }
        if (GeneralApprovalAttribute.OVERTIME.getCode().equals(request.getApprovalAttribute())) {
            return;
        }
        ExecutorUtil.submit(() -> {
            List<PunchNotification> punchNotifications = punchProvider.findPunchNotificationList(namespaceId, request.getEnterpriseId(), request.getUserId(), new java.sql.Date(punchDate.getTime()));
            if (CollectionUtils.isEmpty(punchNotifications)) {
                return;
            }

            for (PunchNotification punchNotification : punchNotifications) {
                if (NormalFlag.YES == NormalFlag.fromCode(punchNotification.getInvalidFlag())) {
                    continue;
                }
                if (request.getBeginTime().compareTo(punchNotification.getRuleTime()) <= 0 && request.getEndTime().compareTo(punchNotification.getRuleTime()) >= 0) {
                    punchNotification.setInvalidFlag(NormalFlag.YES.getCode());
                    punchNotification.setInvalidReason("请假覆盖打卡时间，不需要提醒");
                    punchProvider.updatePunchNotification(punchNotification);
                }
            }
        });
    }

    private boolean isTimeIntervalFullCovered(Date beginTime, Date endTime, List<PunchServiceImpl.TimeInterval> tiDTOs) {
        if (beginTime == null || endTime == null) {
            return false;
        }
        if (CollectionUtils.isEmpty(tiDTOs)) {
            return false;
        }
        for (PunchServiceImpl.TimeInterval t : tiDTOs) {
            if (t.getBeginTime().compareTo(beginTime) <= 0 && t.getEndTime().compareTo(endTime) >= 0) {
                return true;
            }
        }
        return false;
    }
}
