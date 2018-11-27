package com.everhomes.meeting;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.filemanagement.FileService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.mail.MailHandler;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.meeting.AbandonMeetingReservationLockTimeCommand;
import com.everhomes.rest.meeting.AttachmentOwnerType;
import com.everhomes.rest.meeting.CancelMeetingReservationCommand;
import com.everhomes.rest.meeting.CreateMeetingRecordCommand;
import com.everhomes.rest.meeting.CreateOrUpdateMeetingRoomCommand;
import com.everhomes.rest.meeting.DeleteMeetingRecordCommand;
import com.everhomes.rest.meeting.DeleteMeetingRoomCommand;
import com.everhomes.rest.meeting.EndMeetingReservationCommand;
import com.everhomes.rest.meeting.GetMeetingRecordDetailCommand;
import com.everhomes.rest.meeting.GetMeetingReservationDetailCommand;
import com.everhomes.rest.meeting.GetMeetingReservationSimpleByTimeUnitCommand;
import com.everhomes.rest.meeting.GetMeetingRoomDetailCommand;
import com.everhomes.rest.meeting.GetMeetingRoomSimpleInfoCommand;
import com.everhomes.rest.meeting.ListMeetingRoomDetailInfoCommand;
import com.everhomes.rest.meeting.ListMeetingRoomDetailInfoResponse;
import com.everhomes.rest.meeting.ListMeetingRoomSimpleInfoCommand;
import com.everhomes.rest.meeting.ListMeetingRoomSimpleInfoResponse;
import com.everhomes.rest.meeting.ListMyMeetingRecordsCommand;
import com.everhomes.rest.meeting.ListMyMeetingRecordsResponse;
import com.everhomes.rest.meeting.ListMyMeetingsCommand;
import com.everhomes.rest.meeting.ListMyMeetingsResponse;
import com.everhomes.rest.meeting.MeetingAttachmentDTO;
import com.everhomes.rest.meeting.MeetingGeneralFlag;
import com.everhomes.rest.meeting.MeetingInvitationDTO;
import com.everhomes.rest.meeting.MeetingMemberSourceType;
import com.everhomes.rest.meeting.MeetingRecordDetailActionData;
import com.everhomes.rest.meeting.MeetingRecordDetailInfoDTO;
import com.everhomes.rest.meeting.MeetingRecordSimpleInfoDTO;
import com.everhomes.rest.meeting.MeetingReservationDetailActionData;
import com.everhomes.rest.meeting.MeetingReservationDetailDTO;
import com.everhomes.rest.meeting.MeetingReservationLockTimeCommand;
import com.everhomes.rest.meeting.MeetingReservationShowStatus;
import com.everhomes.rest.meeting.MeetingReservationSimpleDTO;
import com.everhomes.rest.meeting.MeetingRoomDetailInfoDTO;
import com.everhomes.rest.meeting.MeetingRoomReservationDTO;
import com.everhomes.rest.meeting.MeetingRoomReservationTimeDTO;
import com.everhomes.rest.meeting.MeetingRoomSimpleInfoDTO;
import com.everhomes.rest.meeting.MeetingRoomStatus;
import com.everhomes.rest.meeting.UpdateMeetingRecordCommand;
import com.everhomes.rest.meeting.UpdateMeetingReservationCommand;
import com.everhomes.rest.messaging.ChannelType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessageMetaConstant;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.messaging.MetaObjectType;
import com.everhomes.rest.messaging.RouterMetaObject;
import com.everhomes.scheduler.MeetingRemindScheduleJob;
import com.everhomes.scheduler.MeetingRoomRecoveryScheduleJob;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhMeetingAttachments;
import com.everhomes.server.schema.tables.pojos.EhMeetingInvitations;
import com.everhomes.server.schema.tables.pojos.EhMeetingReservations;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.punch.PunchProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.hp.hpl.sparta.xpath.ThisNodeTest;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class MeetingServiceImpl implements MeetingService, ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeetingServiceImpl.class);
    private static final String CHINESE_COMMA = "、";
    private static final int MEETING_ROOM_RECOVERY_FETCH_SIZE = 300;
    private static final int MEETING_COMING_SOON_REMIND_FETCH_SIZE = 300;
    private static final int MEETING_COMING_SOON_MINUTE = 10;
    private static final String LOCALE = "zh_CN";

    @Autowired
    private FileService fileService;
    @Autowired
    private MeetingProvider meetingProvider;
    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private LocaleStringService localeStringService;
    @Autowired
    private LocaleTemplateService localeTemplateService;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private MessagingService messagingService;
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private NamespaceProvider namespaceProvider;
    @Autowired
    private CoordinationProvider coordinationProvider;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private ContentServerService contentServerService;
    @Autowired
    private ScheduleProvider scheduleProvider;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            setup();
        }
    }

    public void setup() {
        if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            String triggerName1 = MeetingRoomRecoveryScheduleJob.MEETING_ROOM_RECOVERY_SCHEDULE + System.currentTimeMillis();
            String jobName1 = triggerName1;
            String cronExpression1 = MeetingRoomRecoveryScheduleJob.CRON_EXPRESSION;
            //启动定时任务-会议室资源超时回收
            scheduleProvider.scheduleCronJob(triggerName1, jobName1, cronExpression1, MeetingRoomRecoveryScheduleJob.class, null);

            String triggerName2 = MeetingRemindScheduleJob.MEETING_REMIND_SCHEDULE + System.currentTimeMillis();
            String jobName2 = triggerName2;
            String cronExpression2 = MeetingRemindScheduleJob.CRON_EXPRESSION;
            //启动定时任务-会议前10分钟提醒
            scheduleProvider.scheduleCronJob(triggerName2, jobName2, cronExpression2, MeetingRemindScheduleJob.class, null);
        }
    }

    @Override
    public MeetingRoomSimpleInfoDTO getMeetingRoomSimpleInfo(GetMeetingRoomSimpleInfoCommand cmd) {
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));
        MeetingRoom meetingRoom = getAndCheckMeetingRoomById(cmd.getOrganizationId(), cmd.getMeetingRoomId());
        return convertToMeetingRoomSimpleInfoDTO(meetingRoom);
    }

    private MeetingRoom getAndCheckMeetingRoomById(Long organizationId, Long meetingRoomId) {
        MeetingRoom meetingRoom = meetingProvider.findMeetingRoomById(UserContext.getCurrentNamespaceId(), organizationId, meetingRoomId);
        if (meetingRoom == null) {
            LOGGER.error("Unable to find the meeting room.Parameters:namespaceId={},organizationId={},meetingRoomId={}", UserContext.getCurrentNamespaceId(), organizationId, meetingRoomId);
            throw errorWithMeetingRoomNoExist();
        }
        return meetingRoom;
    }

    private MeetingRoomSimpleInfoDTO convertToMeetingRoomSimpleInfoDTO(MeetingRoom meetingRoom) {
        MeetingRoomSimpleInfoDTO simpleInfoDTO = new MeetingRoomSimpleInfoDTO();
        simpleInfoDTO.setMeetingRoomId(meetingRoom.getId());
        simpleInfoDTO.setName(meetingRoom.getName());
        simpleInfoDTO.setSeatCount(meetingRoom.getSeatCount());
        return simpleInfoDTO;
    }

    @Override
    public MeetingRoomDetailInfoDTO getMeetingRoomDetail(GetMeetingRoomDetailCommand cmd) {
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));
        MeetingRoom meetingRoom = getAndCheckMeetingRoomById(cmd.getOrganizationId(), cmd.getMeetingRoomId());
        Map<Long, List<MeetingReservation>> groupByMeetingRoomMap = listMeetingRoomReservationDetailsGroupByRoomId(Collections.singletonList(meetingRoom.getId()), new Date(cmd.getQueryStartDate()), new Date(cmd.getQueryEndDate()));
        return convertToMeetingRoomDetailInfoDTO(meetingRoom, groupByMeetingRoomMap.get(meetingRoom.getId()));
    }

    private Map<Long, List<MeetingReservation>> listMeetingRoomReservationDetailsGroupByRoomId(List<Long> meetingRoomIds, Date queryBeginDate, Date queryEndDate) {
        List<MeetingReservation> meetingReservations = meetingProvider.findMeetingRoomReservationDetails(meetingRoomIds, queryBeginDate, queryEndDate);
        final Map<Long, List<MeetingReservation>> groupByMeetingRoomMap = new HashMap<>();
        if (CollectionUtils.isEmpty(meetingReservations)) {
            return groupByMeetingRoomMap;
        }
        meetingReservations.forEach(meetingReservation -> {
            List<MeetingReservation> reservationsThisRoom = groupByMeetingRoomMap.get(meetingReservation.getMeetingRoomId());
            if (reservationsThisRoom == null) {
                reservationsThisRoom = new ArrayList<>();
                groupByMeetingRoomMap.put(meetingReservation.getMeetingRoomId(), reservationsThisRoom);
            }
            reservationsThisRoom.add(meetingReservation);
        });

        return groupByMeetingRoomMap;
    }

    private MeetingRoomDetailInfoDTO convertToMeetingRoomDetailInfoDTO(MeetingRoom meetingRoom, List<MeetingReservation> meetingReservationsThisRoom) {
        MeetingRoomDetailInfoDTO meetingRoomDetailInfoDTO = ConvertHelper.convert(meetingRoom, MeetingRoomDetailInfoDTO.class);
        meetingRoomDetailInfoDTO.setSplitTimeUnitCount(getSplitTimeUnitCount());
        meetingRoomDetailInfoDTO.setOpenBeginTime(getTimeInMillis(meetingRoom.getOpenBeginTime()));
        meetingRoomDetailInfoDTO.setOpenEndTime(getTimeInMillis(meetingRoom.getOpenEndTime()));
        meetingRoomDetailInfoDTO.setOperateTime(meetingRoom.getOperateTime().getTime());

        if (CollectionUtils.isEmpty(meetingReservationsThisRoom)) {
            return meetingRoomDetailInfoDTO;
        }

        Map<Long, MeetingRoomReservationDTO> details = new TreeMap<>();
        // 按预订的开会时间顺序排序
        Collections.sort(meetingReservationsThisRoom, new Comparator<MeetingReservation>() {
            @Override
            public int compare(MeetingReservation o1, MeetingReservation o2) {
                return Long.valueOf(o1.getLockBeginTime().getTime()).compareTo(Long.valueOf(o2.getLockBeginTime().getTime()));
            }
        });
        meetingReservationsThisRoom.forEach(meetingReservation -> {
            Long meetingDateLong = getTimeInMillis(meetingReservation.getMeetingDate());
            MeetingRoomReservationDTO meetingRoomReservationDTO = details.get(meetingDateLong);
            if (meetingRoomReservationDTO == null) {
                meetingRoomReservationDTO = new MeetingRoomReservationDTO();
                meetingRoomReservationDTO.setMeetingDate(meetingDateLong);
                meetingRoomReservationDTO.setReservationTimeDTOS(new ArrayList<>());
                details.put(meetingDateLong, meetingRoomReservationDTO);
            }
            meetingRoomReservationDTO.getReservationTimeDTOS().add(
                    new MeetingRoomReservationTimeDTO(meetingReservation.getId(), meetingDateLong, getTimeInMillis(new Time(meetingReservation.getLockBeginTime().getTime())), getTimeInMillis(new Time(meetingReservation.getLockEndTime().getTime()))));
        });
        List<MeetingRoomReservationDTO> meetingRoomReservationDTOS = calculateBookUpStatus(meetingRoom, new ArrayList<>(details.values()));
        meetingRoomDetailInfoDTO.setMeetingRoomReservationDTOs(meetingRoomReservationDTOS);
        return meetingRoomDetailInfoDTO;
    }

    // 计算该会议室这一天是否已订满
    private List<MeetingRoomReservationDTO> calculateBookUpStatus(MeetingRoom meetingRoom, List<MeetingRoomReservationDTO> meetingRoomReservationDTOS) {
        if (CollectionUtils.isEmpty(meetingRoomReservationDTOS)) {
            return Collections.emptyList();
        }
        // 总可预订时长
        long total = getTimeInMillis(meetingRoom.getOpenEndTime()) - getTimeInMillis(meetingRoom.getOpenBeginTime());
        for (MeetingRoomReservationDTO reservationDate : meetingRoomReservationDTOS) {
            if (CollectionUtils.isEmpty(reservationDate.getReservationTimeDTOS())) {
                continue;
            }
            // 已被预订时长
            long totalReservationTimeLong = 0L;
            for (MeetingRoomReservationTimeDTO reservationTime : reservationDate.getReservationTimeDTOS()) {
                // 原则上不会有null值
                if (reservationTime.getBeginTime() == null || reservationTime.getEndTime() == null) {
                    continue;
                }
                totalReservationTimeLong += (reservationTime.getEndTime() - reservationTime.getBeginTime());
            }
            reservationDate.setBookedUpFlag(total <= totalReservationTimeLong ? MeetingGeneralFlag.ON.getCode() : MeetingGeneralFlag.OFF.getCode());
        }
        return meetingRoomReservationDTOS;
    }

    @Override
    public Long createOrUpdateMeetingRoom(CreateOrUpdateMeetingRoomCommand cmd) {
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));
        cmd.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        // v1.0 的会议室没有指定归属分公司，所以设置为总公司ID
        cmd.setOwnerId(cmd.getOrganizationId());

        checkMeetingRoomNameRepeat(cmd);

        if (cmd.getMeetingRoomId() == null) {
            return createMeetingRoom(cmd);
        } else {
            return updateMeetingRoom(cmd);
        }
    }

    private Long createMeetingRoom(CreateOrUpdateMeetingRoomCommand cmd) {
        String operatorName = getContractNameByUserId(UserContext.currentUserId(), cmd.getOrganizationId());

        MeetingRoom meetingRoom = new MeetingRoom();
        meetingRoom.setNamespaceId(UserContext.getCurrentNamespaceId());
        meetingRoom.setOrganizationId(cmd.getOrganizationId());
        meetingRoom.setOwnerType(cmd.getOwnerType());
        meetingRoom.setOwnerId(cmd.getOwnerId());
        meetingRoom.setName(cmd.getName());
        meetingRoom.setSeatCount(cmd.getSeatCount());
        meetingRoom.setDescription(cmd.getDescription());
        meetingRoom.setOpenBeginTime(getMeetingRoomOpenBeginTime());
        meetingRoom.setOpenEndTime(getMeetingRoomOpenEndTime());
        meetingRoom.setOperatorName(operatorName);
        meetingRoom.setStatus(MeetingRoomStatus.OPENING.getCode());
        return meetingProvider.createMeetingRoom(meetingRoom);
    }

    private Long updateMeetingRoom(CreateOrUpdateMeetingRoomCommand cmd) {
        MeetingRoom updateMeetingRoom = getAndCheckMeetingRoomById(cmd.getOrganizationId(), cmd.getMeetingRoomId());
        String operatorName = getContractNameByUserId(UserContext.currentUserId(), cmd.getOrganizationId());

        updateMeetingRoom.setName(cmd.getName());
        updateMeetingRoom.setSeatCount(cmd.getSeatCount());
        updateMeetingRoom.setDescription(cmd.getDescription());
        updateMeetingRoom.setOperatorName(operatorName);
        dbProvider.execute(transactionStatus -> {
            meetingProvider.updateMeetingRoom(updateMeetingRoom);
            return null;
        });

        return updateMeetingRoom.getId();
    }

    @Override
    public void deleteMeetingRoom(DeleteMeetingRoomCommand cmd) {
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));
        MeetingRoom meetingRoom = meetingProvider.findMeetingRoomById(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), cmd.getMeetingRoomId());
        if (meetingRoom == null) {
            return;
        }
        List<MeetingReservation> meetingReservationCanceledList = dbProvider.execute(transactionStatus -> {
            meetingRoom.setStatus(MeetingRoomStatus.DELETED.getCode());
            meetingRoom.setOperatorName(getContractNameByUserId(UserContext.currentUserId(), cmd.getOrganizationId()));
            meetingProvider.updateMeetingRoom(meetingRoom);
            // 1、这里要自动取消预订了该会议室的未开始的所有会议
            // 2、不影响进行中/已结束的会议
            List<MeetingReservation> meetingReservations = meetingProvider.findNotBeginningMeetingReservationByRoomId(meetingRoom.getId());
            if (CollectionUtils.isEmpty(meetingReservations)) {
                return null;
            }
            for (MeetingReservation meetingReservation : meetingReservations) {
                meetingReservation.setStatus(MeetingReservationStatus.CANCELED.getCode());
                meetingProvider.updateMeetingReservation(meetingReservation);
            }
            return meetingReservations;
        });

        sendMessageAfterMeetingCanceled(meetingReservationCanceledList, meetingRoom.getOperatorName());
    }

    @Override
    public ListMeetingRoomSimpleInfoResponse listMeetingRoomSimpleInfo(ListMeetingRoomSimpleInfoCommand cmd) {
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));

        // 系统设置默认会议室
        initDefaultMeetingRoom(cmd.getOrganizationId());

        List<MeetingRoom> results = this.meetingProvider.findMeetingRooms(null, null, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_MEETING_ROOMS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
                query.addConditions(Tables.EH_MEETING_ROOMS.ORGANIZATION_ID.eq(cmd.getOrganizationId()));
                return query;
            }
        });

        ListMeetingRoomSimpleInfoResponse response = new ListMeetingRoomSimpleInfoResponse();

        if (CollectionUtils.isEmpty(results)) {
            return response;
        }
        List<MeetingRoomSimpleInfoDTO> dtos = new ArrayList<>(results.size());
        for (MeetingRoom meetingRoom : results) {
            dtos.add(convertToMeetingRoomSimpleInfoDTO(meetingRoom));
        }

        response.setDtos(dtos);
        return response;
    }

    @Override
    public ListMeetingRoomDetailInfoResponse listMeetingRoomDetailInfo(ListMeetingRoomDetailInfoCommand cmd) {
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        int pageOffset = 1;
        if (cmd.getPageOffset() != null && cmd.getPageOffset() > 1) {
            pageOffset = cmd.getPageOffset();
        }
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);


        initDefaultMeetingRoom(cmd.getOrganizationId());
        List<MeetingRoom> results = this.meetingProvider.findMeetingRooms(pageSize + 1, offset, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_MEETING_ROOMS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
                query.addConditions(Tables.EH_MEETING_ROOMS.ORGANIZATION_ID.eq(cmd.getOrganizationId()));
                query.addConditions(Tables.EH_MEETING_ROOMS.STATUS.eq(MeetingRoomStatus.OPENING.getCode()));
                return query;
            }
        });

        ListMeetingRoomDetailInfoResponse response = new ListMeetingRoomDetailInfoResponse();

        if (CollectionUtils.isEmpty(results)) {
            return response;
        }
        List<Long> meetingRoomIds = new ArrayList<>();
        if (results.size() > pageSize) {
            results.remove(pageSize);
            response.setNextPageOffset(pageOffset + 1);
        }
        for (MeetingRoom meetingRoom : results) {
            meetingRoomIds.add(meetingRoom.getId());
        }
        Map<Long, List<MeetingReservation>> groupByMeetingRoomMap = listMeetingRoomReservationDetailsGroupByRoomId(meetingRoomIds, new Date(cmd.getQueryDate()), new Date(cmd.getQueryDate()));
        List<MeetingRoomDetailInfoDTO> dtos = new ArrayList<>(results.size());
        for (MeetingRoom meetingRoom : results) {
            dtos.add(convertToMeetingRoomDetailInfoDTO(meetingRoom, groupByMeetingRoomMap.get(meetingRoom.getId())));
        }

        response.setDtos(dtos);
        return response;
    }

    @Override
    public MeetingReservationDetailDTO getMeetingReservationDetail(GetMeetingReservationDetailCommand cmd) {
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));
        MeetingReservation meetingReservation = meetingProvider.findMeetingReservationById(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), cmd.getMeetingReservationId());
        if (meetingReservation == null) {
            throw errorWithMeetingReservationNoExist();
        }
        if (MeetingReservationStatus.CANCELED == MeetingReservationStatus.fromCode(meetingReservation.getStatus())) {
            throw errorWithMeetingReservationCanceled();
        }
        if (!haveMeetingReservationReadPermission(meetingReservation)) {
            throw errorWithMeetingReservationNoReadPermission();
        }
        List<EhMeetingInvitations> meetingInvitations = meetingProvider.findMeetingInvitationsByMeetingId(meetingReservation.getId(), null);
        MeetingRecord meetingRecord = meetingProvider.findMeetingRecordByMeetingReservationId(meetingReservation.getId());

        MeetingRecordDetailInfoDTO meetingRecordDetailInfoDTO = convertToMeetingRecordDetailInfoDTO(meetingReservation, meetingRecord, selectMeetingInvitationDTOs(meetingInvitations, MeetingInvitationRoleType.CC));
        MeetingReservationDetailDTO meetingReservationDetailDTO = convertToMeetingReservationDTO(meetingReservation);
        meetingReservationDetailDTO.setMeetingInvitationDTOS(getUserAvatar(selectMeetingInvitationDTOs(meetingInvitations, MeetingInvitationRoleType.ATTENDEE)));
        meetingReservationDetailDTO.setMemberNamesSummary(buildMeetingReservationMemberNamesSummary(meetingReservation, meetingInvitations, false));
        meetingReservationDetailDTO.setMeetingRecordDetailInfoDTO(meetingRecordDetailInfoDTO);
        return meetingReservationDetailDTO;
    }

    private List<MeetingInvitationDTO> selectMeetingInvitationDTOs(List<EhMeetingInvitations> meetingInvitations, MeetingInvitationRoleType roleType) {
        if (CollectionUtils.isEmpty(meetingInvitations)) {
            return Collections.emptyList();
        }
        final List<MeetingInvitationDTO> meetingInvitationDTOS = new ArrayList<>();
        meetingInvitations.forEach(meetingInvitation -> {
            if (roleType == null || roleType == MeetingInvitationRoleType.fromCode(meetingInvitation.getRoleType())) {
                meetingInvitationDTOS.add(ConvertHelper.convert(meetingInvitation, MeetingInvitationDTO.class));
            }
        });
        return meetingInvitationDTOS;
    }

    private MeetingRecordDetailInfoDTO convertToMeetingRecordDetailInfoDTO(MeetingReservation meetingReservation, MeetingRecord meetingRecord, List<MeetingInvitationDTO> meetingInvitations) {
        if (meetingRecord == null) {
            return null;
        }
        MeetingRecordDetailInfoDTO meetingRecordDetailInfoDTO = new MeetingRecordDetailInfoDTO();
        meetingRecordDetailInfoDTO.setId(meetingRecord.getId());
        meetingRecordDetailInfoDTO.setMeetingReservationId(meetingReservation.getId());
        meetingRecordDetailInfoDTO.setSubject(meetingReservation.getSubject());
        meetingRecordDetailInfoDTO.setMeetingSponsorUserId(meetingReservation.getMeetingSponsorUserId());
        meetingRecordDetailInfoDTO.setMeetingSponsorDetailId(meetingReservation.getMeetingSponsorDetailId());
        meetingRecordDetailInfoDTO.setMeetingSponsorName(meetingReservation.getMeetingSponsorName());
        meetingRecordDetailInfoDTO.setMeetingDate(getTimeInMillis(meetingReservation.getMeetingDate()));
        meetingRecordDetailInfoDTO.setBeginTime(getTimeInMillis(new Time(meetingReservation.getExpectBeginTime().getTime())));
        meetingRecordDetailInfoDTO.setEndTime(getTimeInMillis(new Time(meetingReservation.getExpectEndTime().getTime())));
        meetingRecordDetailInfoDTO.setOperateDateTime(meetingRecord.getOperateTime().getTime());
        meetingRecordDetailInfoDTO.setOperatorUid(meetingRecord.getOperatorUid());
        meetingRecordDetailInfoDTO.setOperatorName(meetingRecord.getOperatorName());
        meetingRecordDetailInfoDTO.setMemberNamesSummary(buildMeetingRecordMemberNamesSummary(meetingInvitations));
        meetingRecordDetailInfoDTO.setMeetingRecordShareDTOS(getUserAvatar(meetingInvitations));
        meetingRecordDetailInfoDTO.setContent(meetingRecord.getContent());
        meetingRecordDetailInfoDTO.setRecordWordLimit(getRecordWordLimit());
        meetingRecordDetailInfoDTO.setMeetingAttachments(listMeetingAttachments(meetingRecord.getId(), AttachmentOwnerType.EhMeetingRecords));
        return meetingRecordDetailInfoDTO;
    }

    @Override
    public MeetingReservationSimpleDTO getMeetingReservationSimpleInfoByTimeUnit(GetMeetingReservationSimpleByTimeUnitCommand cmd) {
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));
        if (cmd.getCellBeginTime() == null || cmd.getCellEndTime() == null || cmd.getCellBeginTime() > cmd.getCellEndTime()) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "ERROR_INVALID_PARAMETER");
        }
        getAndCheckMeetingRoomById(cmd.getOrganizationId(), cmd.getMeetingRoomId());
        List<MeetingReservation> meetingReservations = meetingProvider.findMeetingReservationByTimeUnit(cmd.getMeetingRoomId(), new Date(cmd.getMeetingDate()), getTimeFromMillis(cmd.getCellBeginTime()), getTimeFromMillis(cmd.getCellEndTime()));
        if (CollectionUtils.isEmpty(meetingReservations)) {
            return null;
        }
        MeetingReservation meetingReservation = meetingReservations.get(0);
        MeetingReservationSimpleDTO meetingReservationSimpleDTO = convertToMeetingReservationSimpleDTO(meetingReservation);
        if (MeetingReservationStatus.TIME_LOCK == MeetingReservationStatus.fromCode(meetingReservation.getStatus())) {
            meetingReservationSimpleDTO.setStatus(MeetingReservationShowStatus.BE_LOCK_UP.getCode());
            meetingReservationSimpleDTO.setShowStatus(MeetingReservationShowStatus.BE_LOCK_UP.toString());
        } else {
            meetingReservationSimpleDTO.setStatus(MeetingReservationShowStatus.BE_OCCUPIED.getCode());
            meetingReservationSimpleDTO.setShowStatus(MeetingReservationShowStatus.BE_OCCUPIED.toString());
        }
        return meetingReservationSimpleDTO;
    }

    private MeetingReservationSimpleDTO convertToMeetingReservationSimpleDTO(MeetingReservation meetingReservation) {
        MeetingReservationSimpleDTO meetingReservationSimpleDTO = new MeetingReservationSimpleDTO();
        meetingReservationSimpleDTO.setId(meetingReservation.getId());
        meetingReservationSimpleDTO.setSubject(meetingReservation.getSubject());
        meetingReservationSimpleDTO.setMeetingRoomId(meetingReservation.getMeetingRoomId());
        meetingReservationSimpleDTO.setMeetingRoomName(meetingReservation.getMeetingRoomName());
        meetingReservationSimpleDTO.setExpectBeginTime(getTimeInMillis(new Time(meetingReservation.getExpectBeginTime().getTime())));
        meetingReservationSimpleDTO.setExpectEndTime(getTimeInMillis(new Time(meetingReservation.getExpectEndTime().getTime())));
        meetingReservationSimpleDTO.setSponsorUserId(meetingReservation.getMeetingSponsorUserId());
        meetingReservationSimpleDTO.setSponsorDetailId(meetingReservation.getMeetingSponsorDetailId());
        meetingReservationSimpleDTO.setSponsorName(meetingReservation.getMeetingSponsorName());
        meetingReservationSimpleDTO.setMeetingDate(meetingReservation.getMeetingDate().getTime());
        meetingReservationSimpleDTO.setAttachmentFlag(meetingReservation.getAttachmentFlag());
        return meetingReservationSimpleDTO;
    }

    @Override
    public MeetingReservationDetailDTO meetingReservationLockTime(MeetingReservationLockTimeCommand cmd) {
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));
        if (cmd.getBeginTime() == null || cmd.getEndTime() == null || cmd.getBeginTime() > cmd.getEndTime()) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "ERROR_INVALID_PARAMETER");
        }

        MeetingRoom meetingRoom = getAndCheckMeetingRoomById(cmd.getOrganizationId(), cmd.getMeetingRoomId());
        if (MeetingRoomStatus.OPENING != MeetingRoomStatus.fromCode(meetingRoom.getStatus())) {
            throw errorWithMeetingRoomClosed();
        }

        Date meetingDate = new Date(cmd.getMeetingDate());
        LocalTime beginLocalTime = getTimeFromMillis(cmd.getBeginTime()).toLocalTime();
        LocalTime endLocalTime = getTimeFromMillis(cmd.getEndTime()).toLocalTime();
        Timestamp beginDateTime = new Timestamp(LocalDateTime.of(meetingDate.toLocalDate(), beginLocalTime).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        Timestamp endDateTime = new Timestamp(LocalDateTime.of(meetingDate.toLocalDate(), endLocalTime).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        if (new Timestamp(DateHelper.currentGMTTime().getTime()).after(beginDateTime)) {
            throw errorWithMeetingTimeExpired();
        }

        clearMyMeetingRoomLockHistories(meetingRoom.getId());

        checkMeetingTimeIsAvailable(cmd, meetingDate, beginDateTime, endDateTime);
        if (cmd.getMeetingReservationId() == null) {
            return createMeetingReservationLockTime(cmd, meetingRoom, beginDateTime, endDateTime);
        } else {
            return updateMeetingReservationLockTime(cmd, meetingRoom, beginDateTime, endDateTime);
        }
    }

    private void clearMyMeetingRoomLockHistories(Long meetingRoomId) {
        // 确保同一个人同一个会议室内只有一个时间段被锁定，因此在新锁定时间时先回收之前的时间锁定
        List<EhMeetingReservations> myLockStatusMeetingReservations = meetingProvider.findLockStatusMeetingReservationsBySponsorUserId(UserContext.currentUserId(), meetingRoomId);
        meetingProvider.batchDeleteMeetingReservations(myLockStatusMeetingReservations);
    }

    private MeetingReservation buildLockTimeMeetingReservation(MeetingReservationLockTimeCommand cmd, MeetingRoom meetingRoom, Timestamp beginDateTime, Timestamp endDateTime) {
        MeetingReservation meetingReservation = new MeetingReservation();
        meetingReservation.setNamespaceId(UserContext.getCurrentNamespaceId());
        meetingReservation.setOrganizationId(cmd.getOrganizationId());
        meetingReservation.setMeetingDate(new Date(cmd.getMeetingDate()));
        meetingReservation.setMeetingRoomId(meetingRoom.getId());
        meetingReservation.setMeetingRoomName(meetingRoom.getName());
        meetingReservation.setMeetingRoomSeatCount(meetingRoom.getSeatCount());
        meetingReservation.setExpectBeginTime(beginDateTime);
        meetingReservation.setExpectEndTime(endDateTime);
        meetingReservation.setLockBeginTime(beginDateTime);
        meetingReservation.setLockEndTime(endDateTime);
        meetingReservation.setStatus(MeetingReservationStatus.TIME_LOCK.getCode());
        meetingReservation.setSystemMessageFlag(MeetingGeneralFlag.ON.getCode());
        meetingReservation.setEmailMessageFlag(MeetingGeneralFlag.OFF.getCode());
        OrganizationMember organizationMember = organizationProvider.findActiveOrganizationMemberByOrgIdAndUId(UserContext.currentUserId(), cmd.getOrganizationId());
        if (organizationMember != null) {
            meetingReservation.setMeetingSponsorUserId(organizationMember.getTargetId());
            meetingReservation.setMeetingSponsorDetailId(organizationMember.getDetailId());
            meetingReservation.setMeetingSponsorName(organizationMember.getContactName());
        } else {
            meetingReservation.setMeetingSponsorUserId(0L);
            meetingReservation.setMeetingSponsorDetailId(0L);
            meetingReservation.setMeetingSponsorName("");
        }
        return meetingReservation;
    }

    private MeetingReservationDetailDTO createMeetingReservationLockTime(MeetingReservationLockTimeCommand cmd, MeetingRoom meetingRoom, Timestamp beginDateTime, Timestamp endDateTime) {
        // 锁定时间，10分钟没有完成预订将被回收MeetingRoomRecoveryScheduleJob
        MeetingReservation meetingReservation = buildLockTimeMeetingReservation(cmd, meetingRoom, beginDateTime, endDateTime);
        // 加锁防止预定时间被重用
        this.coordinationProvider.getNamedLock(CoordinationLocks.MEETING_ROOM_TIME_LOCK.getCode() + meetingRoom.getId()).enter(() -> {
            // 获得锁后二次判断
            checkMeetingTimeIsAvailable(cmd, new Date(cmd.getMeetingDate()), beginDateTime, endDateTime);
            dbProvider.execute(transactionStatus -> {
                meetingProvider.createMeetingReservation(meetingReservation);
                return null;
            });
            return null;
        });
        List<EhMeetingInvitations> meetingInvitations = buildDefaultMeetingInvitations(meetingReservation);
        MeetingReservationDetailDTO meetingReservationDetailDTO = convertToMeetingReservationDTO(meetingReservation);
        meetingReservationDetailDTO.setMeetingInvitationDTOS(getUserAvatar(selectMeetingInvitationDTOs(meetingInvitations, MeetingInvitationRoleType.ATTENDEE)));
        meetingReservationDetailDTO.setMemberNamesSummary(buildMeetingReservationMemberNamesSummary(meetingReservation, meetingInvitations, false));
        return meetingReservationDetailDTO;
    }

    private List<EhMeetingInvitations> buildDefaultMeetingInvitations(MeetingReservation meetingReservation) {
        List<EhMeetingInvitations> meetingInvitations = new ArrayList<>();
        EhMeetingInvitations sponsor = new EhMeetingInvitations();
        sponsor.setRoleType(MeetingInvitationRoleType.ATTENDEE.getCode());
        sponsor.setMeetingReservationId(meetingReservation.getId());
        sponsor.setSourceName(meetingReservation.getMeetingSponsorName());
        sponsor.setSourceId(meetingReservation.getMeetingSponsorDetailId());
        sponsor.setSourceType(MeetingMemberSourceType.MEMBER_DETAIL.getCode());
        meetingInvitations.add(sponsor);
        return meetingInvitations;
    }

    private MeetingReservationDetailDTO updateMeetingReservationLockTime(MeetingReservationLockTimeCommand cmd, MeetingRoom meetingRoom, Timestamp beginDateTime, Timestamp endDateTime) {
        MeetingReservation meetingReservation = meetingProvider.findMeetingReservationById(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), cmd.getMeetingReservationId());

        checkWhenUpdateMeetingReservation(meetingReservation);

        // 加锁防止预定时间重用
        this.coordinationProvider.getNamedLock(CoordinationLocks.MEETING_ROOM_TIME_LOCK.getCode() + meetingRoom.getId()).enter(() -> {
            // 获得锁后二次判断
            checkMeetingTimeIsAvailable(cmd, new Date(cmd.getMeetingDate()), beginDateTime, endDateTime);
            dbProvider.execute(transactionStatus -> {
                meetingReservation.setMeetingRoomId(meetingRoom.getId());
                meetingReservation.setMeetingRoomName(meetingRoom.getName());
                meetingReservation.setMeetingRoomSeatCount(meetingRoom.getSeatCount());
                meetingReservation.setMeetingDate(new Date(cmd.getMeetingDate()));
                meetingReservation.setExpectBeginTime(beginDateTime);
                meetingReservation.setExpectEndTime(endDateTime);
                meetingReservation.setLockBeginTime(beginDateTime);
                meetingReservation.setLockEndTime(endDateTime);
                meetingProvider.updateMeetingReservation(meetingReservation);
                return null;
            });
            return null;
        });
        List<EhMeetingInvitations> meetingInvitations = meetingProvider.findMeetingInvitationsByMeetingId(meetingReservation.getId(), MeetingInvitationRoleType.ATTENDEE.getCode());
        if (CollectionUtils.isEmpty(meetingInvitations)) {
            meetingInvitations = buildDefaultMeetingInvitations(meetingReservation);
        }

        MeetingReservationDetailDTO meetingReservationDetailDTO = convertToMeetingReservationDTO(meetingReservation);
        meetingReservationDetailDTO.setMeetingInvitationDTOS(getUserAvatar(selectMeetingInvitationDTOs(meetingInvitations, MeetingInvitationRoleType.ATTENDEE)));
        meetingReservationDetailDTO.setMemberNamesSummary(buildMeetingReservationMemberNamesSummary(meetingReservation, meetingInvitations, false));
        sendMessageAfterMeetingUpdated(meetingInvitations, meetingReservation, meetingReservationDetailDTO.getMemberNamesSummary());
        return meetingReservationDetailDTO;
    }

    private void checkMeetingTimeIsAvailable(MeetingReservationLockTimeCommand cmd, Date meetingDate, Timestamp beginDateTime, Timestamp endDateTime) {
        CheckMeetingTimeAvailableCondition condition = new CheckMeetingTimeAvailableCondition();
        condition.setMeetingRoomId(cmd.getMeetingRoomId());
        condition.setMeetingDate(meetingDate);
        condition.setMeetingReservationId(cmd.getMeetingReservationId());
        condition.setBeginDateTime(beginDateTime);
        condition.setEndDateTime(endDateTime);
        boolean isTimeAvailable = meetingProvider.checkMeetingTimeIsAvailable(condition);
        if (!isTimeAvailable) {
            throw errorWithMeetingTimeBeOccupied();
        }
    }

    private MeetingReservationDetailDTO convertToMeetingReservationDTO(MeetingReservation meetingReservation) {
        MeetingReservationDetailDTO meetingReservationDetailDTO = ConvertHelper.convert(meetingReservation, MeetingReservationDetailDTO.class);
        meetingReservationDetailDTO.setSystemMessageFlag(meetingReservation.getSystemMessageFlag() != null ? meetingReservation.getSystemMessageFlag() : MeetingGeneralFlag.OFF.getCode());
        meetingReservationDetailDTO.setEmailMessageFlag(meetingReservation.getEmailMessageFlag() != null ? meetingReservation.getEmailMessageFlag() : MeetingGeneralFlag.OFF.getCode());
        meetingReservationDetailDTO.setMeetingDate(meetingReservation.getMeetingDate().getTime());
        meetingReservationDetailDTO.setExpectBeginTime(getTimeInMillis(new Time(meetingReservation.getExpectBeginTime().getTime())));
        meetingReservationDetailDTO.setExpectEndTime(getTimeInMillis(new Time(meetingReservation.getExpectEndTime().getTime())));
        meetingReservationDetailDTO.setLockBeginTime(getTimeInMillis(new Time(meetingReservation.getLockBeginTime().getTime())));
        meetingReservationDetailDTO.setLockEndTime(getTimeInMillis(new Time(meetingReservation.getLockEndTime().getTime())));
        if (meetingReservation.getActBeginTime() != null) {
            meetingReservationDetailDTO.setActBeginDateTime(meetingReservation.getActBeginTime().getTime());
        }
        if (meetingReservation.getActEndTime() != null) {
            meetingReservationDetailDTO.setActEndDateTime(meetingReservation.getActEndTime().getTime());
        }
        meetingReservationDetailDTO.setRecordWordLimit(getRecordWordLimit());
        MeetingReservationShowStatus status = buildMeetingReservationShowStatus(meetingReservation);
        meetingReservationDetailDTO.setStatus(status.getCode());
        meetingReservationDetailDTO.setShowStatus(status.toString());
        meetingReservationDetailDTO.setMeetingAttachments(listMeetingAttachments(meetingReservation.getId(),AttachmentOwnerType.EhMeetingReservations));
        return meetingReservationDetailDTO;
    }

    private List<MeetingAttachmentDTO> listMeetingAttachments(Long id,
			AttachmentOwnerType attachmentOwnerType) {
    	if(null == attachmentOwnerType)
    		return null;
    	List<MeetingAttachment> attachments = meetingProvider.listMeetingAttachements(id, attachmentOwnerType.getCode());
    	if(null != attachments){
    		return attachments.stream().map(r -> {
    			return processAttachment2DTO(r);
    		}).collect(Collectors.toList());
    	}
		return null;
	}

	private MeetingAttachmentDTO processAttachment2DTO(MeetingAttachment r) {
		MeetingAttachmentDTO dto = ConvertHelper.convert(r , MeetingAttachmentDTO.class);
		dto.setContentUrl(contentServerService.parserUri(dto.getContentUri()));
		dto.setContentIconUrl(contentServerService.parserUri(dto.getContentIconUri()));
		return dto;
	}

	private MeetingReservationShowStatus buildMeetingReservationShowStatus(MeetingReservation meetingReservation) {
        MeetingReservationShowStatus status = MeetingReservationShowStatus.COMING_SOON;
        if (MeetingReservationStatus.CANCELED == MeetingReservationStatus.fromCode(meetingReservation.getStatus())) {
            status = MeetingReservationShowStatus.CANCELED;
        } else if (MeetingReservationStatus.TIME_LOCK == MeetingReservationStatus.fromCode(meetingReservation.getStatus())) {
            status = MeetingReservationShowStatus.EDIT;
        } else {
            Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
            if (now.after(meetingReservation.getExpectBeginTime()) || meetingReservation.getActBeginTime() != null) {
                status = MeetingReservationShowStatus.STARTING;
            }
            if (now.after(meetingReservation.getExpectEndTime()) || meetingReservation.getActEndTime() != null) {
                status = MeetingReservationShowStatus.COMPLETED;
            }
        }
        return status;
    }

    @Override
    public MeetingReservationDetailDTO updateMeetingReservation(UpdateMeetingReservationCommand cmd) {
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));

        cmd.setMeetingInvitations(getAndCheckMeetingInvitationDTOs(cmd.getMeetingInvitations()));

        MeetingReservation meetingReservation = meetingProvider.findMeetingReservationById(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), cmd.getMeetingReservationId());
        if (meetingReservation == null) {
            // 判断时间锁定是否因超时被释放，如果是则重新尝试锁定时间
            if (meetingProvider.checkMeetingReservationBeRecovery(cmd.getMeetingReservationId())) {
                MeetingReservationLockTimeCommand lockTimeCommand = ConvertHelper.convert(cmd, MeetingReservationLockTimeCommand.class);
                lockTimeCommand.setMeetingReservationId(null);
                MeetingReservationDetailDTO newLockTime = meetingReservationLockTime(lockTimeCommand);
                meetingReservation = meetingProvider.findMeetingReservationById(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), newLockTime.getId());
                cmd.setMeetingReservationId(newLockTime.getId());
            }
        }
        if (meetingReservation == null) {
            throw errorWithMeetingReservationNoExist();
        }
        // 验证一下被锁定的时间是否已经过期
        if (MeetingReservationStatus.TIME_LOCK == MeetingReservationStatus.fromCode(meetingReservation.getStatus())
                && new Timestamp(DateHelper.currentGMTTime().getTime()).after(meetingReservation.getExpectBeginTime())) {
            meetingProvider.deleteMeetingReservation(meetingReservation);
            throw errorWithMeetingTimeExpired();
        }
        boolean isSubjectUpdate = !(cmd.getSubject().equals(meetingReservation.getSubject()) && stringEqual(cmd.getContent(),meetingReservation.getContent()));
        final MeetingReservation updateMeetingReservation = meetingReservation;
        checkWhenUpdateMeetingReservation(updateMeetingReservation);

        updateMeetingReservation.setSubject(cmd.getSubject());
        updateMeetingReservation.setContent(cmd.getContent());
        updateMeetingReservation.setSystemMessageFlag(cmd.getSystemMessageFlag());
        updateMeetingReservation.setEmailMessageFlag(cmd.getEmailMessageFlag());
        updateMeetingReservation.setStatus(MeetingReservationStatus.NORMAL.getCode());

        List<OrganizationMemberDetails> memberDetails = getOrganizationMembersByInvitationSourceIds(buildEhMeetingInvitations(cmd.getMeetingInvitations(), updateMeetingReservation.getId(), MeetingInvitationRoleType.ATTENDEE));
        updateMeetingReservation.setInvitationUserCount(memberDetails != null ? memberDetails.size() : 0);

        List<EhMeetingInvitations> deleteMeetingInvitations = new ArrayList<>();
        List<MeetingInvitationDTO> addMeetingInvitations = new ArrayList<>();
        List<MeetingInvitationDTO> existMeetingInvitations = new ArrayList<>();
        List<EhMeetingInvitations> unChangedMeetingInvitations = new ArrayList<>();
        List<EhMeetingInvitations> meetingInvitations = meetingProvider.findMeetingInvitationsByMeetingId(updateMeetingReservation.getId(), MeetingInvitationRoleType.ATTENDEE.getCode());
        if (!CollectionUtils.isEmpty(meetingInvitations)) {
            for (EhMeetingInvitations ehMeetingInvitation : meetingInvitations) {
                boolean shouldDelete = true;
                for (MeetingInvitationDTO meetingInvitationDTO : cmd.getMeetingInvitations()) {
                    if (ehMeetingInvitation.getSourceType().equals(meetingInvitationDTO.getSourceType()) && ehMeetingInvitation.getSourceId().compareTo(meetingInvitationDTO.getSourceId()) == 0) {
                        unChangedMeetingInvitations.add(ehMeetingInvitation);
                        existMeetingInvitations.add(meetingInvitationDTO);
                        shouldDelete = false;
                    }
                }
                if (shouldDelete) {
                    deleteMeetingInvitations.add(ehMeetingInvitation);
                }
            }
        }
        addMeetingInvitations.addAll(cmd.getMeetingInvitations());
        addMeetingInvitations.removeAll(existMeetingInvitations);
        
        //附件
        List<MeetingAttachment> oldAttachements = meetingProvider.listMeetingAttachements(meetingReservation.getId(), AttachmentOwnerType.EhMeetingReservations.getCode());
        List<MeetingAttachment> newAttachements = convertDTO2MeetingAttachment(cmd.getMeetingAttachments(), meetingReservation);
        List<MeetingAttachment> existsAttachements = new ArrayList<>();
        List<MeetingAttachment> deleteAttachements = findDeleteAttachments(oldAttachements, newAttachements, existsAttachements);
        List<MeetingAttachment> addAttachements = findAddAttachments(newAttachements, existsAttachements);
        if(!CollectionUtils.isEmpty(cmd.getMeetingAttachments())){
        	meetingReservation.setAttachmentFlag(MeetingGeneralFlag.ON.getCode());
        }else{
            meetingReservation.setAttachmentFlag(MeetingGeneralFlag.OFF.getCode());
        }
        dbProvider.execute(transactionStatus -> {
            Long id = meetingProvider.updateMeetingReservation(updateMeetingReservation);
            meetingProvider.batchDeleteMeetingInvitations(deleteMeetingInvitations);
            List<EhMeetingInvitations> addMeetingInvitationList = buildEhMeetingInvitations(addMeetingInvitations, id, MeetingInvitationRoleType.ATTENDEE);
            meetingProvider.batchCreateMeetingInvitations(addMeetingInvitationList);

            meetingProvider.batchDeleteMeetingAttachments(deleteAttachements);
            meetingProvider.batchCreateMeetingAttachments(addAttachements);
            
            return null;
        });

        List<EhMeetingInvitations> currentMeetingInvitations = meetingProvider.findMeetingInvitationsByMeetingId(updateMeetingReservation.getId(), MeetingInvitationRoleType.ATTENDEE.getCode());
        String memberNames = buildMeetingReservationMemberNamesSummary(updateMeetingReservation, currentMeetingInvitations, false);
        sendMessageAfterMeetingCanceled(deleteMeetingInvitations, updateMeetingReservation, memberNames);
        sendMessageAfterMeetingInvited(buildEhMeetingInvitations(addMeetingInvitations, updateMeetingReservation.getId(), MeetingInvitationRoleType.ATTENDEE), updateMeetingReservation, memberNames);
        if (isSubjectUpdate) {
            sendMessageAfterMeetingUpdated(unChangedMeetingInvitations, updateMeetingReservation, memberNames);
        }

        MeetingReservationDetailDTO meetingReservationDetailDTO = convertToMeetingReservationDTO(updateMeetingReservation);
        meetingReservationDetailDTO.setMeetingInvitationDTOS(getUserAvatar(selectMeetingInvitationDTOs(currentMeetingInvitations, MeetingInvitationRoleType.ATTENDEE)));
        meetingReservationDetailDTO.setMemberNamesSummary(memberNames);
        return meetingReservationDetailDTO;
    }

    private boolean stringEqual(String content, String content2) { 
		return content == null ? (content2 == null ? true : false) : content.equals(content2);
	}

	private List<MeetingAttachment> findDeleteAttachments(List<MeetingAttachment> oldAttachements,
			List<MeetingAttachment> newAttachements, List<MeetingAttachment> existsAttachements) {
    	if(CollectionUtils.isEmpty(oldAttachements))
			return Collections.emptyList();
    	if(CollectionUtils.isEmpty(newAttachements))
			return oldAttachements;
    	List<MeetingAttachment> deleteAttachements = new ArrayList<>();
		for (MeetingAttachment oldAttachment : oldAttachements) {
            boolean shouldDelete = true;
            for (MeetingAttachment newAttachment : newAttachements) {
                if (newAttachment.getContentName().equals(oldAttachment.getContentName()) &&
                		newAttachment.getContentUri().equals(oldAttachment.getContentUri()) ) {
                    shouldDelete = false;
                    existsAttachements.add(newAttachment);
                    break;
                }
            }
            if (shouldDelete) {
            	deleteAttachements .add(oldAttachment);
            }
        }
		return deleteAttachements;
	}

	private List<MeetingAttachment> findAddAttachments(List<MeetingAttachment> newAttachements,
			List<MeetingAttachment> existsAttachements) {
		if(CollectionUtils.isEmpty(newAttachements))
			return Collections.emptyList();
		List<MeetingAttachment> addAttachements = new ArrayList<>();
		addAttachements.addAll(newAttachements);
		addAttachements.removeAll(existsAttachements);
		return addAttachements;
	}

	private List<MeetingAttachment> convertDTO2MeetingAttachment(
			List<MeetingAttachmentDTO> meetingAttachments, MeetingReservation meetingReservation) {
		if(meetingAttachments == null){
			return null;
		}
		Map<String, String> fileIconUriMap = fileService.getFileIconUrl();
		return meetingAttachments.stream().map(r->{
			MeetingAttachment attachment = ConvertHelper.convert(r, MeetingAttachment.class);
			attachment.setNamespaceId(meetingReservation.getNamespaceId());
			attachment.setOwnerType(AttachmentOwnerType.EhMeetingReservations.getCode());
			attachment.setOwnerId(meetingReservation.getId());
			attachment.setContentIconUri(processIconUri(fileIconUriMap,r.getContentName())); 
			return attachment;
		}).collect(Collectors.toList());
	}

	private String processIconUri(Map<String, String> fileIconUriMap, String fileName) {
		String suffix = "other";
		if(null != fileName){
			suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		}
		return fileIconUriMap.get(suffix) == null ? fileIconUriMap.get("other") : fileIconUriMap.get(suffix);
	}

	private void checkWhenUpdateMeetingReservation(MeetingReservation meetingReservation) {
        if (meetingReservation == null) {
            throw errorWithMeetingReservationNoExist();
        }
        if (MeetingReservationStatus.CANCELED == MeetingReservationStatus.fromCode(meetingReservation.getStatus())) {
            throw errorWithMeetingReservationCanceled();
        }
        // 只有会议发起人才有修改权限
        if (!haveMeetingReservationWritePermission(meetingReservation)) {
            throw errorWithNoPermissionUpdateMeetingReservation();
        }
        if (checkIsMeetingBegin(meetingReservation)) {
            throw errorWithUpdateABeginMeetingReservation();
        }
        if (checkIsMeetingEnd(meetingReservation)) {
            throw errorWithUpdateAEndMeetingReservation();
        }
    }

    @Override
    public void abandonMeetingReservationLockTime(AbandonMeetingReservationLockTimeCommand cmd) {
        if (cmd.getMeetingReservationId() == null) {
            // 没有锁定时间，则直接返回
            return;
        }
        dbProvider.execute(transactionStatus -> {
            MeetingReservation lockTimeMeetingReservation = meetingProvider.findMeetingReservationById(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), cmd.getMeetingReservationId());
            if (lockTimeMeetingReservation != null && MeetingReservationStatus.TIME_LOCK == MeetingReservationStatus.fromCode(lockTimeMeetingReservation.getStatus())) {
                if (!haveMeetingReservationWritePermission(lockTimeMeetingReservation)) {
                    throw errorWithNoPermissionUpdateMeetingReservation();
                }
                meetingProvider.deleteMeetingReservation(lockTimeMeetingReservation);
            }
            return null;
        });
    }

    private String buildMeetingReservationMemberNamesSummary(MeetingReservation meetingReservation, List<EhMeetingInvitations> meetingInvitations, boolean skipSponsor) {
        StringBuffer memberNamesSummary = new StringBuffer();
        if (!skipSponsor) {
            memberNamesSummary.append(meetingReservation.getMeetingSponsorName());
            memberNamesSummary.append("（发起人）");
            if (CollectionUtils.isEmpty(meetingInvitations)) {
                return memberNamesSummary.toString();
            }
            memberNamesSummary.append(CHINESE_COMMA);
        }

        for (EhMeetingInvitations meetingInvitationDTO : meetingInvitations) {
            if (MeetingInvitationRoleType.ATTENDEE == MeetingInvitationRoleType.fromCode(meetingInvitationDTO.getRoleType())) {
                // 发起人前面已经拼接过了，就不需要重复
                if (MeetingMemberSourceType.MEMBER_DETAIL == MeetingMemberSourceType.fromCode(meetingInvitationDTO.getSourceType())
                        && meetingInvitationDTO.getSourceId().compareTo(meetingReservation.getMeetingSponsorDetailId()) != 0) {
                    memberNamesSummary.append(meetingInvitationDTO.getSourceName());
                    memberNamesSummary.append(CHINESE_COMMA);
                }
            }
        }
        return memberNamesSummary.substring(0, memberNamesSummary.length() - 1);
    }

    private String buildMeetingRecordMemberNamesSummary(List<MeetingInvitationDTO> meetingRecordReceivers) {
        if (CollectionUtils.isEmpty(meetingRecordReceivers)) {
            return "";
        }
        StringBuffer memberNamesSummary = new StringBuffer();
        for (MeetingInvitationDTO receiver : meetingRecordReceivers) {
            memberNamesSummary.append(receiver.getSourceName());
            memberNamesSummary.append(CHINESE_COMMA);
        }
        return memberNamesSummary.substring(0, memberNamesSummary.length() - 1);
    }

    private List<EhMeetingInvitations> buildEhMeetingInvitations(List<MeetingInvitationDTO> addMeetingInvitations, Long meetingReservationId, MeetingInvitationRoleType roleType) {
        if (CollectionUtils.isEmpty(addMeetingInvitations)) {
            return Collections.emptyList();
        }
        List<EhMeetingInvitations> addMeetingInvitationList = new ArrayList<>();
        for (MeetingInvitationDTO meetingInvitationDTO : addMeetingInvitations) {
            EhMeetingInvitations ehMeetingInvitation = new EhMeetingInvitations();
            ehMeetingInvitation.setMeetingReservationId(meetingReservationId);
            ehMeetingInvitation.setSourceType(meetingInvitationDTO.getSourceType());
            ehMeetingInvitation.setSourceId(meetingInvitationDTO.getSourceId());
            ehMeetingInvitation.setSourceName(meetingInvitationDTO.getSourceName());
            ehMeetingInvitation.setRoleType(roleType.getCode());
            addMeetingInvitationList.add(ehMeetingInvitation);
        }
        return addMeetingInvitationList;
    }

    @Override
    public void endMeetingReservation(EndMeetingReservationCommand cmd) {
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));
        MeetingReservation meetingReservation = meetingProvider.findMeetingReservationById(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), cmd.getMeetingReservationId());
        if (meetingReservation == null) {
            throw errorWithMeetingReservationNoExist();
        }
        if (!haveMeetingReservationWritePermission(meetingReservation)) {
            throw errorWithNoPermissionUpdateMeetingReservation();
        }
        if (MeetingReservationStatus.CANCELED == MeetingReservationStatus.fromCode(meetingReservation.getStatus())) {
            throw errorWithMeetingReservationCanceled();
        }
        if (!checkIsMeetingBegin(meetingReservation)) {
            throw errorWithMeetingReservationCompletedBeforeBegin();
        }
        if (checkIsMeetingEnd(meetingReservation)) {
            return;
        }
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        if (now.before(meetingReservation.getLockEndTime())) {
            // 提前结束会议需要把占用的多余的时间释放出来
            meetingReservation.setLockEndTime(generateNewLockEndTime(meetingReservation.getLockEndTime(), now));
        }
        meetingReservation.setActEndTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        meetingProvider.updateMeetingReservation(meetingReservation);
    }

    private Timestamp generateNewLockEndTime(Timestamp originLockEndTime, Timestamp now) {
        int splitMinuteCount = getSplitMinuteCount();
        int dayOfMonth = originLockEndTime.toLocalDateTime().getDayOfMonth();
        Calendar newLockEndTime = Calendar.getInstance();
        newLockEndTime.setTime(now);
        newLockEndTime.add(Calendar.MINUTE, splitMinuteCount);
        // 验证加了一个时间单元以后时间是同一天，次日则不修改
        if (dayOfMonth != newLockEndTime.get(Calendar.DAY_OF_MONTH)) {
            return originLockEndTime;
        }
        // 修正为一个时间单元的整数倍
        newLockEndTime.set(Calendar.MINUTE, newLockEndTime.get(Calendar.MINUTE) - newLockEndTime.get(Calendar.MINUTE) % splitMinuteCount);
        newLockEndTime.set(Calendar.SECOND, 0);
        newLockEndTime.set(Calendar.MILLISECOND, 0);
        Timestamp newLockTime = new Timestamp(newLockEndTime.getTimeInMillis());
        if (newLockTime.before(originLockEndTime)) {
            return newLockTime;
        } else {
            return originLockEndTime;
        }
    }

    @Override
    public void cancelMeetingReservation(CancelMeetingReservationCommand cmd) {
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));
        MeetingReservation meetingReservation = meetingProvider.findMeetingReservationById(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), cmd.getMeetingReservationId());
        if (meetingReservation == null) {
            return;
        }
        if (!haveMeetingReservationWritePermission(meetingReservation)) {
            throw errorWithNoPermissionUpdateMeetingReservation();
        }
        if (MeetingReservationStatus.CANCELED == MeetingReservationStatus.fromCode(meetingReservation.getStatus())) {
            return;
        }
        if (checkIsMeetingEnd(meetingReservation)) {
            throw errorWithCancelABeginOrEndMeetingReservation();
        }
        meetingReservation.setStatus(MeetingReservationStatus.CANCELED.getCode());
        meetingProvider.updateMeetingReservation(meetingReservation);

        List<EhMeetingInvitations> meetingInvitations = meetingProvider.findMeetingInvitationsByMeetingId(meetingReservation.getId(), MeetingInvitationRoleType.ATTENDEE.getCode());
        String memberNames = buildMeetingReservationMemberNamesSummary(meetingReservation, meetingInvitations, false);
        sendMessageAfterMeetingCanceled(meetingInvitations, meetingReservation, memberNames);
    }

    @Override
    public ListMyMeetingsResponse listMyMeetings(ListMyMeetingsCommand cmd) {
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        int pageOffset = (cmd.getPageOffset() != null && cmd.getPageOffset() > 1) ? cmd.getPageOffset() : 1;
        if (cmd.getPageSize() == null) {
            // 如果pageSize没有传值，则不分页查询
            pageSize = Integer.MAX_VALUE - 1;
            pageOffset = 0;
        }

        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);

        OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(UserContext.currentUserId(), cmd.getOrganizationId());
        QueryMyMeetingsCondition condition = new QueryMyMeetingsCondition(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(),
                MeetingGeneralFlag.ON == MeetingGeneralFlag.fromCode(cmd.getEndFlag()),
                member.getDetailId(), pageSize + 1, offset);
        List<MeetingReservation> meetingReservations = meetingProvider.findMeetingReservationsByDetailId(condition);
        ListMyMeetingsResponse response = new ListMyMeetingsResponse();
        if (CollectionUtils.isEmpty(meetingReservations)) {
            return response;
        }
        if (meetingReservations.size() > pageSize) {
            meetingReservations.remove(pageSize);
            response.setNextPageOffset(pageOffset + 1);
        }

        List<MeetingReservationSimpleDTO> dtos = new ArrayList<>();
        meetingReservations.forEach(meetingReservation -> {
            MeetingReservationSimpleDTO dto = new MeetingReservationSimpleDTO();
            dto.setId(meetingReservation.getId());
            dto.setSubject(meetingReservation.getSubject());
            dto.setMeetingRoomId(meetingReservation.getMeetingRoomId());
            dto.setMeetingRoomName(meetingReservation.getMeetingRoomName());
            dto.setMeetingDate(getTimeInMillis(meetingReservation.getMeetingDate()));
            dto.setExpectBeginTime(getTimeInMillis(new Time(meetingReservation.getExpectBeginTime().getTime())));
            dto.setExpectEndTime(getTimeInMillis(new Time(meetingReservation.getExpectEndTime().getTime())));
            dto.setSponsorUserId(meetingReservation.getMeetingSponsorUserId());
            dto.setSponsorDetailId(meetingReservation.getMeetingSponsorDetailId());
            dto.setSponsorName(meetingReservation.getMeetingSponsorName());
            dto.setAttachmentFlag(meetingReservation.getAttachmentFlag());
            MeetingReservationShowStatus status = buildMeetingReservationShowStatus(meetingReservation);
            dto.setStatus(status.getCode());
            dto.setShowStatus(status.toString());
            dtos.add(dto);
        });
        Collections.sort(dtos, new Comparator<MeetingReservationSimpleDTO>() {
            @Override
            public int compare(MeetingReservationSimpleDTO o1, MeetingReservationSimpleDTO o2) {
                MeetingReservationShowStatus s2 = MeetingReservationShowStatus.fromCode(o2.getStatus());
                MeetingReservationShowStatus s1 = MeetingReservationShowStatus.fromCode(o1.getStatus());
                // 这边空值判断是为了避免意外情况造成空指针，理论上不存在空值
                if (s2 == null) {
                    s2 = MeetingReservationShowStatus.COMING_SOON;
                }
                if (s1 == null) {
                    s1 = MeetingReservationShowStatus.COMING_SOON;
                }
                return s1.ordinal() - s2.ordinal();
            }
        });
        response.setDtos(dtos);
        return response;
    }

    @Override
    public MeetingRecordDetailInfoDTO getMeetingRecordDetail(GetMeetingRecordDetailCommand cmd) {
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));

        MeetingRecord meetingRecord = meetingProvider.findMeetingRecordById(cmd.getMeetingRecordId());
        if (meetingRecord == null) {
            throw errorWithMeetingRecordNoExist();
        }
        MeetingReservation meetingReservation = meetingProvider.findMeetingReservationById(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), meetingRecord.getMeetingReservationId());
        if (meetingReservation == null) {
            throw errorWithMeetingReservationNoExist();
        }
        if (!haveMeetingReservationReadPermission(meetingReservation)) {
            throw errorWithMeetingRecordNoReadPermission();
        }
        List<EhMeetingInvitations> meetingInvitations = meetingProvider.findMeetingInvitationsByMeetingId(meetingReservation.getId(), MeetingInvitationRoleType.CC.getCode());
        return convertToMeetingRecordDetailInfoDTO(meetingReservation, meetingRecord, selectMeetingInvitationDTOs(meetingInvitations, MeetingInvitationRoleType.CC));
    }

    @Override
    public MeetingRecordDetailInfoDTO createMeetingRecord(CreateMeetingRecordCommand cmd) {
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));
        cmd.setMeetingRecordShareDTOS(getAndCheckMeetingInvitationDTOs(cmd.getMeetingRecordShareDTOS()));

        MeetingReservation meetingReservation = meetingProvider.findMeetingReservationById(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), cmd.getMeetingReservationId());
        if (meetingReservation == null) {
            throw errorWithMeetingReservationNoExist();
        }
        if (!haveMeetingReservationWritePermission(meetingReservation)) {
            throw errorWithNoPermissionUpdateMeetingRecord();
        }

        MeetingRecord existMeetingRecord = meetingProvider.findMeetingRecordByMeetingReservationId(meetingReservation.getId());
        // 防止重复创建
        if (existMeetingRecord != null) {
            throw errorWithMeetingRecordDuplicated();
        }

        MeetingReservationShowStatus status = buildMeetingReservationShowStatus(meetingReservation);
        if (MeetingReservationShowStatus.COMPLETED != status) {
            throw errorWithMeetingRecordBeforeMeetingNoCompleted();
        }

        String operatorName = getContractNameByUserId(UserContext.currentUserId(), cmd.getOrganizationId());
        MeetingRecord meetingRecord = new MeetingRecord();
        meetingRecord.setMeetingReservationId(meetingReservation.getId());
        meetingRecord.setMeetingSubject(meetingReservation.getSubject());
        meetingRecord.setOperatorName(operatorName);
        meetingRecord.setContent(cmd.getContent());

        if(!CollectionUtils.isEmpty(cmd.getMeetingAttachments())){
        	meetingRecord.setAttachmentFlag(MeetingGeneralFlag.ON.getCode());
        }else{
            meetingRecord.setAttachmentFlag(MeetingGeneralFlag.OFF.getCode());
        }
        
        List<EhMeetingInvitations> recordReceivers = buildEhMeetingInvitations(cmd.getMeetingRecordShareDTOS(), meetingReservation.getId(), MeetingInvitationRoleType.CC);
        dbProvider.execute(transactionStatus -> {
            meetingProvider.createMeetingRecord(meetingRecord);
            //附件 
            List<MeetingAttachment> newAttachements = convertDTO2MeetingAttachment(cmd.getMeetingAttachments(), meetingRecord); 
            meetingProvider.batchCreateMeetingAttachments(newAttachements);
            meetingProvider.batchCreateMeetingInvitations(recordReceivers);
            return null;
        });

        List<EhMeetingInvitations> allRecordReceivers = meetingProvider.findMeetingInvitationsByMeetingId(meetingReservation.getId(), null);
        sendMessageAfterMeetingRecordReceived(meetingReservation, meetingRecord, allRecordReceivers, false);

        List<MeetingInvitationDTO> meetingInvitationDTOS = selectMeetingInvitationDTOs(recordReceivers, MeetingInvitationRoleType.CC);
        return convertToMeetingRecordDetailInfoDTO(meetingReservation, meetingRecord, meetingInvitationDTOS);
    }

    private List<MeetingAttachment> convertDTO2MeetingAttachment(
			List<MeetingAttachmentDTO> meetingAttachments, MeetingRecord meetingRecord) {

		if(meetingAttachments == null){
			return null;
		}
		Map<String, String> fileIconUriMap = fileService.getFileIconUrl();
		return meetingAttachments.stream().map(r->{
			MeetingAttachment attachment = ConvertHelper.convert(r, MeetingAttachment.class);
			attachment.setNamespaceId(UserContext.getCurrentNamespaceId());
			attachment.setOwnerType(AttachmentOwnerType.EhMeetingRecords.getCode());
			attachment.setOwnerId(meetingRecord.getId());
			attachment.setContentIconUri(processIconUri(fileIconUriMap,r.getContentName()));
			return attachment;
		}).collect(Collectors.toList());
	}

	@Override
    public MeetingRecordDetailInfoDTO updateMeetingRecord(UpdateMeetingRecordCommand cmd) {
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));
        cmd.setMeetingRecordShareDTOS(getAndCheckMeetingInvitationDTOs(cmd.getMeetingRecordShareDTOS()));
        MeetingRecord meetingRecord = meetingProvider.findMeetingRecordById(cmd.getMeetingRecordId());
        if (meetingRecord == null) {
            throw errorWithMeetingRecordNoExist();
        }

        MeetingReservation meetingReservation = meetingProvider.findMeetingReservationById(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), meetingRecord.getMeetingReservationId());
        if (meetingReservation == null) {
            throw errorWithMeetingReservationNoExist();
        }
        if (!haveMeetingReservationWritePermission(meetingReservation)) {
            throw errorWithNoPermissionUpdateMeetingRecord();
        }
        boolean isContentUpdate = !cmd.getContent().equals(meetingRecord.getContent());
        String operatorName = getContractNameByUserId(UserContext.currentUserId(), cmd.getOrganizationId());
        meetingRecord.setMeetingSubject(meetingReservation.getSubject());
        meetingRecord.setOperatorName(operatorName);
        meetingRecord.setContent(cmd.getContent());

        List<EhMeetingInvitations> deleteMeetingInvitations = new ArrayList<>();
        List<MeetingInvitationDTO> addMeetingInvitations = new ArrayList<>();
        List<MeetingInvitationDTO> existMeetingInvitations = new ArrayList<>();
        List<EhMeetingInvitations> unChangedMeetingInvitations = new ArrayList<>();
        List<EhMeetingInvitations> meetingInvitations = meetingProvider.findMeetingInvitationsByMeetingId(meetingReservation.getId(), null);
        if (!CollectionUtils.isEmpty(meetingInvitations)) {
            for (EhMeetingInvitations ehMeetingInvitation : meetingInvitations) {
                if (MeetingInvitationRoleType.ATTENDEE == MeetingInvitationRoleType.fromCode(ehMeetingInvitation.getRoleType())) {
                    unChangedMeetingInvitations.add(ehMeetingInvitation);
                    continue;
                }
                boolean shouldDelete = true;
                for (MeetingInvitationDTO meetingRecordShareDTO : cmd.getMeetingRecordShareDTOS()) {
                    if (ehMeetingInvitation.getSourceType().equals(meetingRecordShareDTO.getSourceType()) && ehMeetingInvitation.getSourceId().compareTo(meetingRecordShareDTO.getSourceId()) == 0) {
                        unChangedMeetingInvitations.add(ehMeetingInvitation);
                        existMeetingInvitations.add(meetingRecordShareDTO);
                        shouldDelete = false;
                    }
                }
                if (shouldDelete) {
                    deleteMeetingInvitations.add(ehMeetingInvitation);
                }
            }
        }
        addMeetingInvitations.addAll(cmd.getMeetingRecordShareDTOS());
        addMeetingInvitations.removeAll(existMeetingInvitations);

        //附件
        List<MeetingAttachment> oldAttachements = meetingProvider.listMeetingAttachements(meetingRecord.getId(), AttachmentOwnerType.EhMeetingRecords.getCode());
        List<MeetingAttachment> newAttachements = convertDTO2MeetingAttachment(cmd.getMeetingAttachments(), meetingRecord);
        List<MeetingAttachment> existsAttachements = new ArrayList<>();
		List<MeetingAttachment> deleteAttachements = findDeleteAttachments(oldAttachements, newAttachements, existsAttachements);
        List<MeetingAttachment> addAttachements = findAddAttachments(newAttachements, existsAttachements);
        if(!CollectionUtils.isEmpty(cmd.getMeetingAttachments())){
        	meetingRecord.setAttachmentFlag(MeetingGeneralFlag.ON.getCode());
        }else{
            meetingRecord.setAttachmentFlag(MeetingGeneralFlag.OFF.getCode());
        }
        List<EhMeetingInvitations> newRecordReceivers = buildEhMeetingInvitations(addMeetingInvitations, meetingReservation.getId(), MeetingInvitationRoleType.CC);
        dbProvider.execute(transactionStatus -> {
            meetingProvider.updateMeetingRecord(meetingRecord);
            meetingProvider.batchDeleteMeetingInvitations(deleteMeetingInvitations);
            meetingProvider.batchCreateMeetingInvitations(newRecordReceivers);
            meetingProvider.batchDeleteMeetingAttachments(deleteAttachements);
            meetingProvider.batchCreateMeetingAttachments(addAttachements);
            return null;
        });
        List<EhMeetingInvitations> newAllMeetingInvitations = meetingProvider.findMeetingInvitationsByMeetingId(meetingRecord.getMeetingReservationId(), MeetingInvitationRoleType.CC.getCode());
        if (isContentUpdate) {
            sendMessageAfterMeetingRecordReceived(meetingReservation, meetingRecord, unChangedMeetingInvitations, true);
        }
        sendMessageAfterMeetingRecordReceived(meetingReservation, meetingRecord, newRecordReceivers, false);
        return convertToMeetingRecordDetailInfoDTO(meetingReservation, meetingRecord, selectMeetingInvitationDTOs(newAllMeetingInvitations, MeetingInvitationRoleType.CC));
    }

    @Override
    public void deleteMeetingRecord(DeleteMeetingRecordCommand cmd) {
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));
        MeetingRecord meetingRecord = meetingProvider.findMeetingRecordById(cmd.getMeetingRecordId());
        if (meetingRecord == null) {
            return;
        }
        MeetingReservation meetingReservation = meetingProvider.findMeetingReservationById(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), meetingRecord.getMeetingReservationId());
        if (meetingReservation == null) {
            throw errorWithMeetingReservationNoExist();
        }
        if (!haveMeetingReservationWritePermission(meetingReservation)) {
            throw errorWithNoPermissionDeleteMeetingRecord();
        }
        List<MeetingAttachment> deleteAttachements = meetingProvider.listMeetingAttachements(meetingRecord.getId(), AttachmentOwnerType.EhMeetingRecords.getCode());
        List<EhMeetingInvitations> recordReceivers = meetingProvider.findMeetingInvitationsByMeetingId(meetingReservation.getId(), MeetingInvitationRoleType.CC.getCode());
        dbProvider.execute(transactionStatus -> {
            meetingProvider.deleteMeetingRecord(meetingRecord);
            meetingProvider.batchDeleteMeetingInvitations(recordReceivers);
            meetingProvider.batchDeleteMeetingAttachments(deleteAttachements);
            return null;
        });
    }

    @Override
    public ListMyMeetingRecordsResponse listMyMeetingRecords(ListMyMeetingRecordsCommand cmd) {
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        int pageOffset = (cmd.getPageOffset() == null || cmd.getPageOffset() == 0) ? 1 : cmd.getPageOffset();
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);
        OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(UserContext.currentUserId(), cmd.getOrganizationId());
        QueryMyMeetingRecordsCondition condition = new QueryMyMeetingRecordsCondition(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), member.getDetailId(), pageSize + 1, offset);
        List<MeetingRecord> meetingRecords = meetingProvider.findMeetingRecordsByDetailId(condition);
        ListMyMeetingRecordsResponse response = new ListMyMeetingRecordsResponse();
        if (CollectionUtils.isEmpty(meetingRecords)) {
            return response;
        }
        if (meetingRecords.size() > pageSize) {
            meetingRecords.remove(pageSize);
            response.setNextPageOffset(pageOffset + 1);
        }

        List<MeetingRecordSimpleInfoDTO> dtos = new ArrayList<>();
        meetingRecords.forEach(meetingRecord -> {
            MeetingRecordSimpleInfoDTO dto = new MeetingRecordSimpleInfoDTO();
            dto.setMeetingRecordId(meetingRecord.getId());
            dto.setReceiveTime(meetingRecord.getOperateTime().getTime());
            dto.setRecorderName(meetingRecord.getOperatorName());
            dto.setShowTitle(meetingRecord.getMeetingSubject() + "-会议纪要");
            dto.setAttachmentFlag(meetingRecord.getAttachmentFlag());
            if (meetingRecord.getContent() != null && meetingRecord.getContent().length() > 100) {
                dto.setSummary(meetingRecord.getContent().substring(0, 100));
            } else {
                dto.setSummary(meetingRecord.getContent());
            }
            dtos.add(dto);
        });
        response.setDtos(dtos);
        return response;
    }

    @Override
    public void recoveryMeetingRoomResource() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateHelper.currentGMTTime());
        calendar.add(Calendar.MINUTE, -10);
        Timestamp beforeLockTime = new Timestamp(calendar.getTimeInMillis());
        this.coordinationProvider.getNamedLock(CoordinationLocks.MEETING_ROOM_RESOURCE_RECOVERY.getCode()).tryEnter(() -> {
            LOGGER.info("recoveryMeetingRoomResource begin");
            List<EhMeetingReservations> lockStatusMeetingReservations = meetingProvider.findLockStatusMeetingReservations(beforeLockTime, MEETING_ROOM_RECOVERY_FETCH_SIZE);
            boolean isStop = CollectionUtils.isEmpty(lockStatusMeetingReservations);
            int count = 0;
            while (!isStop) {
                count += lockStatusMeetingReservations.size();
                meetingProvider.batchDeleteMeetingReservations(lockStatusMeetingReservations);
                lockStatusMeetingReservations = meetingProvider.findLockStatusMeetingReservations(beforeLockTime, MEETING_ROOM_RECOVERY_FETCH_SIZE);
                isStop = CollectionUtils.isEmpty(lockStatusMeetingReservations);
            }
            LOGGER.info("recoveryMeetingRoomResource end,recovery count = {}", count);
        });
    }

    @Override
    public void meetingRemind() {
        this.coordinationProvider.getNamedLock(CoordinationLocks.MEETING_REMIND.getCode()).tryEnter(() -> {
            LOGGER.info("meetingRemind begin");
            List<MeetingReservation> comingSoonMeetingReservations = meetingProvider.findComingSoonMeetingReservations(MEETING_COMING_SOON_MINUTE, MEETING_COMING_SOON_REMIND_FETCH_SIZE);
            boolean isStop = CollectionUtils.isEmpty(comingSoonMeetingReservations);
            int count = 0;
            while (!isStop) {
                count += comingSoonMeetingReservations.size();
                meetingProvider.batchUpdateMeetingReservationRemindStatus(comingSoonMeetingReservations);
                comingSoonMeetingReservations.forEach(meetingReservation -> {
                    try {
                        sendMessageWhenMeetingComingSoon(meetingReservation);
                    } catch (Exception e) {
                        LOGGER.error("meetingRemind error,meetingReservationId={}", meetingReservation.getId(), e);
                    }
                });
                comingSoonMeetingReservations = meetingProvider.findComingSoonMeetingReservations(MEETING_COMING_SOON_MINUTE, MEETING_COMING_SOON_REMIND_FETCH_SIZE);
                isStop = CollectionUtils.isEmpty(comingSoonMeetingReservations);
            }
            LOGGER.info("meetingRemind end,remind count = {}", count);
        });
    }

    private List<OrganizationMemberDetails> getOrganizationMembersByInvitationSourceIds(List<EhMeetingInvitations> meetingInvitations) {
        Set<Long> detailIds = new HashSet<>();
        for (EhMeetingInvitations invitation : meetingInvitations) {
            if (MeetingMemberSourceType.MEMBER_DETAIL == MeetingMemberSourceType.fromCode(invitation.getSourceType())) {
                detailIds.add(invitation.getSourceId());
            }
        }
        if (detailIds.isEmpty()) {
            return Collections.emptyList();
        }
        return organizationProvider.findDetailInfoListByIdIn(new ArrayList<>(detailIds));
    }

    // 受邀参加会议的消息通知
    private void sendMessageAfterMeetingInvited(List<EhMeetingInvitations> meetingInvitations, MeetingReservation meetingReservation, String memberNames) {
        if (CollectionUtils.isEmpty(meetingInvitations)) {
            return;
        }
        String routeUrl = RouterBuilder.build(Router.MEETING_RESERVATION_DETAIL, new MeetingReservationDetailActionData(meetingReservation.getId(), meetingReservation.getOrganizationId()));
        String locale = UserContext.current().getUser().getLocale();
        String account = configurationProvider.getValue(0, "mail.smtp.account", "zuolin@zuolin.com");
        UserLogin login = UserContext.current().getLogin();
        ExecutorUtil.submit(new Runnable() {
            @Override
            public void run() {
            	if(UserContext.current().getLogin()==null){
            		UserContext.current().setLogin(login);
            	}
                List<OrganizationMemberDetails> memberDetails = getOrganizationMembersByInvitationSourceIds(meetingInvitations);
                Map<String, String> model = new HashMap<>();
                model.put("meetingSponsorName", meetingReservation.getMeetingSponsorName());
                String subject = localeTemplateService.getLocaleTemplateString(MeetingMessageLocaleConstants.SCOPE, MeetingMessageLocaleConstants.BE_INVITED_A_MEETING_MESSAGE_TITLE, locale, model, "");
                String messageContent = buildMeetingReservationMessageContent(meetingReservation, locale);
                String emailContent = buildMeetingReservationMailMessageContent(meetingReservation, memberNames, MeetingMessageLocaleConstants.MEETING_MAIL_MESSAGE_WITH_APP_NAME_BODY, locale);
                List<File> attachmentFiles = buildMeetingReservationAttachemntPaths(meetingReservation.getId());
                List<String> stringAttementList = new ArrayList<String>();
                attachmentFiles.stream().forEach(file->{stringAttementList.add(file.getAbsolutePath());});
                sendNotifications(meetingReservation, memberDetails, account, subject, messageContent, emailContent, routeUrl, CollectionUtils.isEmpty(stringAttementList)? null : stringAttementList);
                attachmentFiles.stream().forEach(file->{file.delete();});
            }
        });
    }

    protected List<File> buildMeetingReservationAttachemntPaths(Long meetingReservationId) {
    	List<MeetingAttachmentDTO> attachmentList = listMeetingAttachments(meetingReservationId,AttachmentOwnerType.EhMeetingReservations);
		return buildAttachemntPaths(attachmentList);
	}

	private List<File> buildAttachemntPaths(List<MeetingAttachmentDTO> attachmentList) {
		if(CollectionUtils.isEmpty(attachmentList))
			return new ArrayList<>();
		return attachmentList.stream().map(r->contentUrlToLocalFile(r)).filter(r -> r != null).collect(Collectors.toList());
	}

	private File contentUrlToLocalFile(MeetingAttachmentDTO r) {
		// TODO Auto-generated method stub
		List<File> list = new ArrayList<File>();
	    StringBuffer tmpdirBuffer = new StringBuffer(System.getProperty("java.io.tmpdir"));
	    Long currentMillisecond = System.currentTimeMillis();
	    tmpdirBuffer.append(File.separator);
	    tmpdirBuffer.append(currentMillisecond);
	    //附件目录
	    String tmpdir= tmpdirBuffer.toString();
	    File baseDir = new File(tmpdirBuffer.toString());
	    if(!baseDir.exists()){
	    	baseDir.mkdirs();
	    }
	    tmpdirBuffer.append(File.separator);
	    tmpdirBuffer.append(r.getContentName());
	    if(r.getContentType()!=null && !r.getContentName().endsWith(r.getContentType())){
		    tmpdirBuffer.append(".");
		    tmpdirBuffer.append(r.getContentType());
	    }
	    String tempName = tmpdirBuffer.toString();
	    //附件
	    File file = new File(tempName);
	    if(naiveDownloadPicture(file, r.getContentUrl())){
	    	return file;
	    }
		return null;
	}
	 public static boolean naiveDownloadPicture(File file,String urlstr) {
        URL url = null;
        DataInputStream dataInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            //生成图片链接的url类
            url = new URL(urlstr);
            //将url链接下的图片以字节流的形式存储到 DataInputStream类中
            dataInputStream = new DataInputStream(url.openStream());
            //为file生成对应的文件输出流
            fileOutputStream = new FileOutputStream(file);
            //定义字节数组大小
            byte[] buffer = new byte[1024];
            //从所包含的输入流中读取[buffer.length()]的字节，并将它们存储到缓冲区数组buffer 中。
            //dataInputStream.read()会返回写入到buffer的实际长度,若已经读完 则返回-1                  
            while (dataInputStream.read(buffer) > 0) { 
                 fileOutputStream.write(buffer);//将buffer中的字节写入文件中区
            }
            fileOutputStream.flush();
            return true;
        } catch (MalformedURLException e) {
        	LOGGER.error("获取contentserver的资源到本地文件时出错: URL [{}] 有问题 ", urlstr, e);
        } catch (IOException e) {
        	LOGGER.error("获取contentserver的资源到本地文件时出错:  io 有问题 ", e);
        } finally{
            try {
            	if(fileOutputStream != null){            		
            		fileOutputStream.close(); 
            	}
			}  catch (IOException e) {
	        	LOGGER.error("关闭 fileOutputStream 时出错:  io 有问题 ", e);
	        }

            try {
            	if(dataInputStream != null){
            		dataInputStream.close(); 
            	}
			}  catch (IOException e) {
	        	LOGGER.error("关闭 dataInputStream 时出错:  io 有问题 ", e);
	        }
        }
        return false;
	}
	 
	private void sendNotifications(MeetingReservation meetingReservation, List<OrganizationMemberDetails> memberDetails, String account, String subject, String messageContent, String emailContent, String routeUrl, List<String> attachementPaths) {
        if (CollectionUtils.isEmpty(memberDetails)) {
            return;
        }

        for (OrganizationMemberDetails memberDetail : memberDetails) {
            // 发起人不用接收消息
            if (memberDetail.getId().compareTo(meetingReservation.getMeetingSponsorDetailId()) == 0) {
                continue;
            }
            if (MeetingGeneralFlag.ON == MeetingGeneralFlag.fromCode(meetingReservation.getSystemMessageFlag()) && memberDetail.getTargetId() != null && memberDetail.getTargetId() > 0) {
                sendMessage(memberDetail.getTargetId(), messageContent, routeUrl, subject);
            }
            if (MeetingGeneralFlag.ON == MeetingGeneralFlag.fromCode(meetingReservation.getEmailMessageFlag()) && StringUtils.hasText(memberDetail.getWorkEmail())) {
                sendEmail(account, memberDetail.getWorkEmail(), subject, emailContent, attachementPaths);
            }
        }
    }

    // 会议信息更新的消息通知
    private void sendMessageAfterMeetingUpdated(List<EhMeetingInvitations> meetingInvitations, MeetingReservation meetingReservation, String memberNames) {
        if (CollectionUtils.isEmpty(meetingInvitations)) {
            return;
        }
        String routeUrl = RouterBuilder.build(Router.MEETING_RESERVATION_DETAIL, new MeetingReservationDetailActionData(meetingReservation.getId(), meetingReservation.getOrganizationId()));
        String locale = UserContext.current().getUser().getLocale();
        String account = configurationProvider.getValue(0, "mail.smtp.account", "zuolin@zuolin.com");
        UserLogin login = UserContext.current().getLogin();
        ExecutorUtil.submit(new Runnable() {
            @Override
            public void run() {
            	if(UserContext.current().getLogin()==null){
            		UserContext.current().setLogin(login);
            	}
                List<OrganizationMemberDetails> memberDetails = getOrganizationMembersByInvitationSourceIds(meetingInvitations);
                String subject = localeStringService.getLocalizedString(MeetingMessageLocaleConstants.SCOPE, String.valueOf(MeetingMessageLocaleConstants.UPDATE_A_MEETING_MESSAGE_TITLE), locale, null);
                String messageContent = buildMeetingReservationMessageContent(meetingReservation, locale);
                String emailContent = buildMeetingReservationMailMessageContent(meetingReservation, memberNames, MeetingMessageLocaleConstants.MEETING_MAIL_MESSAGE_WITH_APP_NAME_BODY, locale);
                List<File> attachmentFiles = buildMeetingReservationAttachemntPaths(meetingReservation.getId());
                List<String> stringAttementList = new ArrayList<String>();
                attachmentFiles.stream().forEach(file->{stringAttementList.add(file.getAbsolutePath());});
                sendNotifications(meetingReservation, memberDetails, account, subject, messageContent, emailContent, routeUrl, CollectionUtils.isEmpty(stringAttementList)? null : stringAttementList);
                attachmentFiles.stream().forEach(file->{file.delete();});
            }
        });
    }

    // 会议取消的消息通知
    private void sendMessageAfterMeetingCanceled(List<MeetingReservation> meetingReservationList, String adminContactName) {
        if (CollectionUtils.isEmpty(meetingReservationList)) {
            return;
        }
        String locale = UserContext.current().getUser().getLocale();
        ExecutorUtil.submit(new Runnable() {
            @Override
            public void run() {
                for (MeetingReservation meetingReservation : meetingReservationList) {
                    if (MeetingGeneralFlag.ON != MeetingGeneralFlag.fromCode(meetingReservation.getSystemMessageFlag())) {
                        continue;
                    }
                    List<EhMeetingInvitations> meetingInvitations = meetingProvider.findMeetingInvitationsByMeetingId(meetingReservation.getId(), MeetingInvitationRoleType.ATTENDEE.getCode());
                    List<OrganizationMemberDetails> memberDetails = getOrganizationMembersByInvitationSourceIds(meetingInvitations);
                    String subject = localeStringService.getLocalizedString(MeetingMessageLocaleConstants.SCOPE, String.valueOf(MeetingMessageLocaleConstants.CANCEL_A_MEETING_MESSAGE_TITLE), locale, null);
                    Map<String, String> model = new HashMap<>();
                    model.put("adminContactName", adminContactName);
                    String messageContent = localeTemplateService.getLocaleTemplateString(MeetingMessageLocaleConstants.SCOPE, MeetingMessageLocaleConstants.MEETING_ROOM_DELETE_BY_ADMIN, locale, model, null);
                    if (CollectionUtils.isEmpty(memberDetails)) {
                        return;
                    }
                    for (OrganizationMemberDetails memberDetail : memberDetails) {
                        if (memberDetail.getTargetId() != null && memberDetail.getTargetId() > 0) {
                            sendMessage(memberDetail.getTargetId(), messageContent, null, subject);
                        }
                    }
                }
            }
        });
    }

    // 会议取消的消息通知
    private void sendMessageAfterMeetingCanceled(List<EhMeetingInvitations> meetingInvitations, MeetingReservation meetingReservation, String memberNamesSummary) {
        if (CollectionUtils.isEmpty(meetingInvitations)) {
            return;
        }

        String locale = UserContext.current().getUser().getLocale();
        String account = configurationProvider.getValue(0, "mail.smtp.account", "zuolin@zuolin.com");
        UserLogin login = UserContext.current().getLogin();
        ExecutorUtil.submit(new Runnable() {
            @Override
            public void run() {
            	if(UserContext.current().getLogin()==null){
            		UserContext.current().setLogin(login);
            	}
                List<OrganizationMemberDetails> memberDetails = getOrganizationMembersByInvitationSourceIds(meetingInvitations);
                String subject = localeStringService.getLocalizedString(MeetingMessageLocaleConstants.SCOPE, String.valueOf(MeetingMessageLocaleConstants.CANCEL_A_MEETING_MESSAGE_TITLE), locale, null);
                String messageContent = buildMeetingReservationMessageContent(meetingReservation, locale);
                String emailContent = buildMeetingReservationMailMessageContent(meetingReservation, memberNamesSummary, MeetingMessageLocaleConstants.MEETING_MAIL_MESSAGE_BODY, locale);
                List<File> attachmentFiles = buildMeetingReservationAttachemntPaths(meetingReservation.getId());
                List<String> stringAttementList = new ArrayList<String>();
                attachmentFiles.stream().forEach(file->{stringAttementList.add(file.getAbsolutePath());});
                sendNotifications(meetingReservation, memberDetails, account, subject, messageContent, emailContent, emailContent, CollectionUtils.isEmpty(stringAttementList)? null : stringAttementList);
                attachmentFiles.stream().forEach(file->{file.delete();});
            }
        });
    }

    // 会议纪要的消息通知
    private void sendMessageAfterMeetingRecordReceived(MeetingReservation meetingReservation, MeetingRecord meetingRecord, List<EhMeetingInvitations> meetingRecordReceivers, boolean isModified) {
        if (CollectionUtils.isEmpty(meetingRecordReceivers)) {
            return;
        }
        String routeUrl = RouterBuilder.build(Router.MEETING_RECORD_DETAIL, new MeetingRecordDetailActionData(meetingRecord.getId(), meetingReservation.getOrganizationId()));
        String locale = UserContext.current().getUser().getLocale();
        ExecutorUtil.submit(new Runnable() {
            @Override
            public void run() {
                List<OrganizationMemberDetails> memberDetails = getOrganizationMembersByInvitationSourceIds(meetingRecordReceivers);
                int code = MeetingMessageLocaleConstants.RECEIVE_A_MEETING_RECORD_MESSAGE_TITLE;
                if (isModified) {
                    code = MeetingMessageLocaleConstants.RECEIVE_A_MODIFY_MEETING_RECORD_MESSAGE_TITLE;
                }
                Map<String, String> model = new HashMap<>();
                model.put("meetingRecorderName", meetingRecord.getOperatorName());
                String messageSubject = localeTemplateService.getLocaleTemplateString(MeetingMessageLocaleConstants.SCOPE, code, locale, model, null);
                String content = buildMeetingRecordMessageContent(meetingReservation, locale);
                for (OrganizationMemberDetails memberDetail : memberDetails) {
                    // 发起人不用接收消息
                    if (memberDetail.getId().compareTo(meetingReservation.getMeetingSponsorDetailId()) == 0) {
                        continue;
                    }
                    if (memberDetail.getTargetId() != null && memberDetail.getTargetId() > 0) {
                        sendMessage(memberDetail.getTargetId(), content, routeUrl, messageSubject);
                    }
                }
            }
        });
    }

    // 会议即将开始的消息通知
    private void sendMessageWhenMeetingComingSoon(MeetingReservation meetingReservation) {
        if (MeetingGeneralFlag.OFF == MeetingGeneralFlag.fromCode(meetingReservation.getSystemMessageFlag())) {
            return;
        }
        String routeUrl = RouterBuilder.build(Router.MEETING_RESERVATION_DETAIL, new MeetingReservationDetailActionData(meetingReservation.getId(), meetingReservation.getOrganizationId()));
        List<EhMeetingInvitations> meetingInvitations = meetingProvider.findMeetingInvitationsByMeetingId(meetingReservation.getId(), MeetingInvitationRoleType.ATTENDEE.getCode());
        List<OrganizationMemberDetails> memberDetails = getOrganizationMembersByInvitationSourceIds(meetingInvitations);
        String subject = localeStringService.getLocalizedString(MeetingMessageLocaleConstants.SCOPE, String.valueOf(MeetingMessageLocaleConstants.MEETING_COMING_SOON_MESSAGE_TITLE), LOCALE, null);
        String messageContent = buildMeetingReservationMessageContent(meetingReservation, LOCALE);
        for (OrganizationMemberDetails memberDetail : memberDetails) {
            if (memberDetail.getTargetId() != null && memberDetail.getTargetId() > 0) {
                sendMessage(memberDetail.getTargetId(), messageContent, routeUrl, subject);
            }
        }
    }

    private void sendEmail(String account, String email, String subject, String content, List<String> attachementPaths) {
        if (!StringUtils.hasText(email)) {
            return;
        }
        String handlerName = MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP;
        MailHandler handler = PlatformContext.getComponent(handlerName);

        handler.sendMail(0, account, email, subject, content, attachementPaths);
    }

    private void sendMessage(Long receiveUserId, String content, String url, String messageSubject) {
        MessageDTO message = new MessageDTO();
        message.setBodyType(MessageBodyType.TEXT.getCode());
        message.setBody(content.toString());
        message.setMetaAppId(AppConstants.APPID_DEFAULT);
        message.setChannels(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(receiveUserId)));

        Map<String, String> meta = new HashMap<>();
        meta.put(MessageMetaConstant.MESSAGE_SUBJECT, messageSubject);
        if (StringUtils.hasText(url)) {
            //  set the route
            RouterMetaObject metaObject = new RouterMetaObject();
            metaObject.setUrl(url);
            meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
            meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
        }

        message.setMeta(meta);

        //  send the message
        messagingService.routeMessage(
                User.SYSTEM_USER_LOGIN,
                AppConstants.APPID_MESSAGING,
                ChannelType.USER.getCode(),
                String.valueOf(receiveUserId),
                message,
                MessagingConstants.MSG_FLAG_STORED.getCode()
        );
    }

    private String buildMeetingReservationMessageContent(MeetingReservation meetingReservation, String locale) {
        Map<String, String> model = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 EEEE HH:mm", Locale.SIMPLIFIED_CHINESE);

        model.put("meetingBeginTime", sdf.format(meetingReservation.getExpectBeginTime()));
        model.put("meetingRoomName", meetingReservation.getMeetingRoomName());
        model.put("meetingSubject", meetingReservation.getSubject());
        String content = localeTemplateService.getLocaleTemplateString(MeetingMessageLocaleConstants.SCOPE, MeetingMessageLocaleConstants.MEETING_SYSTEM_MESSAGE_BODY, locale, model, "");
        return content.replace("|", "\n");
    }

    private String buildMeetingReservationMailMessageContent(MeetingReservation meetingReservation, String invitationNames, int code, String locale) {
        Map<String, String> model = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 EEEE HH:mm", Locale.SIMPLIFIED_CHINESE);
        model.put("meetingBeginTime", sdf.format(meetingReservation.getExpectBeginTime()));
        model.put("meetingRoomName", meetingReservation.getMeetingRoomName());
        model.put("meetingSubject", meetingReservation.getSubject());
        model.put("meetingSponsorName", meetingReservation.getMeetingSponsorName());
        model.put("meetingUserList", invitationNames);
        model.put("content", meetingReservation.getContent() == null ? "" : meetingReservation.getContent());
        String content = localeTemplateService.getLocaleTemplateString(MeetingMessageLocaleConstants.SCOPE, code, locale, model, "");
        return content.replace("|", "\n");
    }

    private String buildMeetingRecordMessageContent(MeetingReservation meetingReservation, String locale) {
        Map<String, String> model = new HashMap<>();
        model.put("meetingSubject", meetingReservation.getSubject());
        return localeTemplateService.getLocaleTemplateString(MeetingMessageLocaleConstants.SCOPE, MeetingMessageLocaleConstants.MEETING_RECORD_MESSAGE_BODY, locale, model, "");
    }

    private Long getTopEnterpriseId(Long organizationId) {
        if (organizationId == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "organizationId can not be null");
        }
        Organization organization = organizationProvider.findOrganizationById(organizationId);
        if (organization == null) {
            LOGGER.error("Unable to find the organization.organizationId = {}", organizationId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Unable to find the organization.");
        }
        if (organization.getParentId() == null || organization.getParentId() == 0)
            return organizationId;
        else {
            return Long.valueOf(organization.getPath().split("/")[1]);
        }
    }

    private String getContractNameByUserId(Long userId, Long organizationId) {
        OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, organizationId);
        if (member != null) {
            return member.getContactName();
        }
        return UserContext.current().getUser().getNickName();
    }

    private void checkMeetingRoomNameRepeat(CreateOrUpdateMeetingRoomCommand cmd) {
        CheckMeetingRoomNameExistCondition condition = new CheckMeetingRoomNameExistCondition();
        condition.setNamespaceId(UserContext.getCurrentNamespaceId());
        condition.setOrganizationId(cmd.getOrganizationId());
        condition.setOwnerType(cmd.getOwnerType());
        condition.setOwnerId(cmd.getOwnerId());
        condition.setName(cmd.getName());
        condition.setId(cmd.getMeetingRoomId());
        boolean isNameExist = meetingProvider.checkMeetingRoomNameExist(condition);
        if (isNameExist) {
            throw RuntimeErrorException.errorWith(
                    MeetingErrorCodeConstants.SCOPE,
                    MeetingErrorCodeConstants.MEETING_ROOM_NAME_REPEAT_ERROR,
                    localeStringService.getLocalizedString(
                            String.valueOf(MeetingErrorCodeConstants.SCOPE),
                            String.valueOf(MeetingErrorCodeConstants.MEETING_ROOM_NAME_REPEAT_ERROR),
                            UserContext.current().getUser().getLocale(),
                            "Ensure that the names are not duplicated"));
        }
    }

    private RuntimeErrorException errorWithMeetingRoomNoExist() {
        return RuntimeErrorException.errorWith(
                MeetingErrorCodeConstants.SCOPE,
                MeetingErrorCodeConstants.MEETING_ROOM_NO_EXIST_ERROR,
                localeStringService.getLocalizedString(
                        String.valueOf(MeetingErrorCodeConstants.SCOPE),
                        String.valueOf(MeetingErrorCodeConstants.MEETING_ROOM_NO_EXIST_ERROR),
                        UserContext.current().getUser().getLocale(),
                        "The meeting room does not exist"));
    }

    private RuntimeErrorException errorWithMeetingRoomClosed() {
        return RuntimeErrorException.errorWith(
                MeetingErrorCodeConstants.SCOPE,
                MeetingErrorCodeConstants.MEETING_ROOM_UNAVAILABLE_ERROR,
                localeStringService.getLocalizedString(
                        String.valueOf(MeetingErrorCodeConstants.SCOPE),
                        String.valueOf(MeetingErrorCodeConstants.MEETING_ROOM_UNAVAILABLE_ERROR),
                        UserContext.current().getUser().getLocale(),
                        "The meeting room is currently unavailable"));
    }

    private RuntimeErrorException errorWithMeetingTimeExpired() {
        return RuntimeErrorException.errorWith(
                MeetingErrorCodeConstants.SCOPE,
                MeetingErrorCodeConstants.MEETING_TIME_HAS_EXPIRED_ERROR,
                localeStringService.getLocalizedString(
                        String.valueOf(MeetingErrorCodeConstants.SCOPE),
                        String.valueOf(MeetingErrorCodeConstants.MEETING_TIME_HAS_EXPIRED_ERROR),
                        UserContext.current().getUser().getLocale(),
                        "Time has expired"));
    }

    private RuntimeErrorException errorWithMeetingTimeBeOccupied() {
        return RuntimeErrorException.errorWith(
                MeetingErrorCodeConstants.SCOPE,
                MeetingErrorCodeConstants.MEETING_TIME_BE_OCCUPIED_ERROR,
                localeStringService.getLocalizedString(
                        String.valueOf(MeetingErrorCodeConstants.SCOPE),
                        String.valueOf(MeetingErrorCodeConstants.MEETING_TIME_BE_OCCUPIED_ERROR),
                        UserContext.current().getUser().getLocale(),
                        "The meeting time is currently unavailable"));
    }

    private RuntimeErrorException errorWithMeetingReservationNoExist() {
        return RuntimeErrorException.errorWith(
                MeetingErrorCodeConstants.SCOPE,
                MeetingErrorCodeConstants.MEETING_RESERVATION_NO_EXIST_ERROR,
                localeStringService.getLocalizedString(
                        String.valueOf(MeetingErrorCodeConstants.SCOPE),
                        String.valueOf(MeetingErrorCodeConstants.MEETING_RESERVATION_NO_EXIST_ERROR),
                        UserContext.current().getUser().getLocale(),
                        "The meeting reservation does not exist"));
    }

    private RuntimeErrorException errorWithMeetingReservationCanceled() {
        return RuntimeErrorException.errorWith(
                MeetingErrorCodeConstants.SCOPE,
                MeetingErrorCodeConstants.MEETING_RESERVATION_HAS_BEEN_CANCELED_ERROR,
                localeStringService.getLocalizedString(
                        String.valueOf(MeetingErrorCodeConstants.SCOPE),
                        String.valueOf(MeetingErrorCodeConstants.MEETING_RESERVATION_HAS_BEEN_CANCELED_ERROR),
                        UserContext.current().getUser().getLocale(),
                        "The meeting reservation has been canceled"));
    }

    private RuntimeErrorException errorWithMeetingRecordNoExist() {
        return RuntimeErrorException.errorWith(
                MeetingErrorCodeConstants.SCOPE,
                MeetingErrorCodeConstants.MEETING_RECORD_NO_EXIST_ERROR,
                localeStringService.getLocalizedString(
                        String.valueOf(MeetingErrorCodeConstants.SCOPE),
                        String.valueOf(MeetingErrorCodeConstants.MEETING_RECORD_NO_EXIST_ERROR),
                        UserContext.current().getUser().getLocale(),
                        "The meeting record does not exist"));
    }

    private RuntimeErrorException errorWithMeetingRecordDuplicated() {
        return RuntimeErrorException.errorWith(
                MeetingErrorCodeConstants.SCOPE,
                MeetingErrorCodeConstants.MEETING_RECORD_DUPLICATED_ERROR,
                localeStringService.getLocalizedString(
                        String.valueOf(MeetingErrorCodeConstants.SCOPE),
                        String.valueOf(MeetingErrorCodeConstants.MEETING_RECORD_DUPLICATED_ERROR),
                        UserContext.current().getUser().getLocale(),
                        "param error"));
    }

    private RuntimeErrorException errorWithMeetingRecordBeforeMeetingNoCompleted() {
        return RuntimeErrorException.errorWith(
                MeetingErrorCodeConstants.SCOPE,
                MeetingErrorCodeConstants.MEETING_RECORD_MEETING_NO_COMPLETE_ERROR,
                localeStringService.getLocalizedString(
                        String.valueOf(MeetingErrorCodeConstants.SCOPE),
                        String.valueOf(MeetingErrorCodeConstants.MEETING_RECORD_MEETING_NO_COMPLETE_ERROR),
                        UserContext.current().getUser().getLocale(),
                        "param error"));
    }

    private RuntimeErrorException errorWithNoPermissionDeleteMeetingRecord() {
        return RuntimeErrorException.errorWith(
                MeetingErrorCodeConstants.SCOPE,
                MeetingErrorCodeConstants.MEETING_RECORD_DELETE_PERMISSION_DENY_ERROR,
                localeStringService.getLocalizedString(
                        String.valueOf(MeetingErrorCodeConstants.SCOPE),
                        String.valueOf(MeetingErrorCodeConstants.MEETING_RECORD_DELETE_PERMISSION_DENY_ERROR),
                        UserContext.current().getUser().getLocale(),
                        "Permission denied"));
    }

    private RuntimeErrorException errorWithNoPermissionUpdateMeetingRecord() {
        return RuntimeErrorException.errorWith(
                MeetingErrorCodeConstants.SCOPE,
                MeetingErrorCodeConstants.MEETING_RECORD_CREATE_UPDATE_PERMISSION_ERROR,
                localeStringService.getLocalizedString(
                        String.valueOf(MeetingErrorCodeConstants.SCOPE),
                        String.valueOf(MeetingErrorCodeConstants.MEETING_RECORD_CREATE_UPDATE_PERMISSION_ERROR),
                        UserContext.current().getUser().getLocale(),
                        "Permission denied"));
    }

    private RuntimeErrorException errorWithNoPermissionUpdateMeetingReservation() {
        return RuntimeErrorException.errorWith(
                MeetingErrorCodeConstants.SCOPE,
                MeetingErrorCodeConstants.MEETING_RESERVATION_UPDATE_PERMISSION_ERROR,
                localeStringService.getLocalizedString(
                        String.valueOf(MeetingErrorCodeConstants.SCOPE),
                        String.valueOf(MeetingErrorCodeConstants.MEETING_RESERVATION_UPDATE_PERMISSION_ERROR),
                        UserContext.current().getUser().getLocale(),
                        "Permission denied"));
    }

    private RuntimeErrorException errorWithCancelABeginOrEndMeetingReservation() {
        return RuntimeErrorException.errorWith(
                MeetingErrorCodeConstants.SCOPE,
                MeetingErrorCodeConstants.MEETING_BEGIN_OR_END_CANNOT_CANCEL_ERROR,
                localeStringService.getLocalizedString(
                        String.valueOf(MeetingErrorCodeConstants.SCOPE),
                        String.valueOf(MeetingErrorCodeConstants.MEETING_BEGIN_OR_END_CANNOT_CANCEL_ERROR),
                        UserContext.current().getUser().getLocale(),
                        "Permission denied"));
    }

    private RuntimeErrorException errorWithMeetingReservationCompletedBeforeBegin() {
        return RuntimeErrorException.errorWith(
                MeetingErrorCodeConstants.SCOPE,
                MeetingErrorCodeConstants.MEETING_COMPLETE_BEFORE_BEGIN_ERROR,
                localeStringService.getLocalizedString(
                        String.valueOf(MeetingErrorCodeConstants.SCOPE),
                        String.valueOf(MeetingErrorCodeConstants.MEETING_COMPLETE_BEFORE_BEGIN_ERROR),
                        UserContext.current().getUser().getLocale(),
                        "Status error"));
    }

    private RuntimeErrorException errorWithUpdateAEndMeetingReservation() {
        return RuntimeErrorException.errorWith(
                MeetingErrorCodeConstants.SCOPE,
                MeetingErrorCodeConstants.MEETING_RESERVATION_IS_END_ERROR,
                localeStringService.getLocalizedString(
                        String.valueOf(MeetingErrorCodeConstants.SCOPE),
                        String.valueOf(MeetingErrorCodeConstants.MEETING_RESERVATION_IS_END_ERROR),
                        UserContext.current().getUser().getLocale(),
                        "Permission denied"));
    }

    private RuntimeErrorException errorWithUpdateABeginMeetingReservation() {
        return RuntimeErrorException.errorWith(
                MeetingErrorCodeConstants.SCOPE,
                MeetingErrorCodeConstants.MEETING_RESERVATION_IS_BEGIN_ERROR,
                localeStringService.getLocalizedString(
                        String.valueOf(MeetingErrorCodeConstants.SCOPE),
                        String.valueOf(MeetingErrorCodeConstants.MEETING_RESERVATION_IS_BEGIN_ERROR),
                        UserContext.current().getUser().getLocale(),
                        "Permission denied"));
    }

    private RuntimeErrorException errorWithMeetingReservationNoReadPermission() {
        return RuntimeErrorException.errorWith(
                MeetingErrorCodeConstants.SCOPE,
                MeetingErrorCodeConstants.MEETING_RESERVATION_HAS_NO_READ_PERMISSION_ERROR,
                localeStringService.getLocalizedString(
                        String.valueOf(MeetingErrorCodeConstants.SCOPE),
                        String.valueOf(MeetingErrorCodeConstants.MEETING_RESERVATION_HAS_NO_READ_PERMISSION_ERROR),
                        UserContext.current().getUser().getLocale(),
                        "Permission denied"));
    }

    private RuntimeErrorException errorWithMeetingRecordNoReadPermission() {
        return RuntimeErrorException.errorWith(
                MeetingErrorCodeConstants.SCOPE,
                MeetingErrorCodeConstants.MEETING_RECORD_HAS_NO_READ_PERMISSION_ERROR,
                localeStringService.getLocalizedString(
                        String.valueOf(MeetingErrorCodeConstants.SCOPE),
                        String.valueOf(MeetingErrorCodeConstants.MEETING_RECORD_HAS_NO_READ_PERMISSION_ERROR),
                        UserContext.current().getUser().getLocale(),
                        "Permission denied"));
    }

    private boolean checkIsMeetingEnd(MeetingReservation meetingReservation) {
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        if (meetingReservation.getLockEndTime().before(now)) {
            return true;
        }
        if (meetingReservation.getActEndTime() != null) {
            return true;
        }
        return false;
    }

    private boolean checkIsMeetingBegin(MeetingReservation meetingReservation) {
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        if (meetingReservation.getLockBeginTime().before(now)) {
            return true;
        }
        if (meetingReservation.getActBeginTime() != null) {
            return true;
        }
        return false;
    }

    private boolean haveMeetingReservationReadPermission(MeetingReservation meetingReservation) {
        if (haveMeetingReservationWritePermission(meetingReservation)) {
            return true;
        }
        OrganizationMember organizationMember = organizationProvider.findActiveOrganizationMemberByOrgIdAndUId(UserContext.currentUserId(), meetingReservation.getOrganizationId());
        return meetingProvider.countMeetingInvitation(meetingReservation.getId(), MeetingMemberSourceType.MEMBER_DETAIL.getCode(), organizationMember.getDetailId()) > 0;
    }

    private boolean haveMeetingReservationWritePermission(MeetingReservation meetingReservation) {
        if (meetingReservation.getMeetingSponsorUserId() != null && UserContext.currentUserId().compareTo(meetingReservation.getMeetingSponsorUserId()) == 0) {
            return true;
        }
        if (meetingReservation.getMeetingRecorderUserId() != null && UserContext.currentUserId().compareTo(meetingReservation.getMeetingRecorderUserId()) == 0) {
            return true;
        }
        return false;
    }

    private Time getMeetingRoomOpenBeginTime() {
        String openBeginTimeStr = configurationProvider.getValue(ConfigConstants.MEETING_ROOM_OPEN_BEGIN_TIME, "08:00:00");
        return Time.valueOf(openBeginTimeStr);
    }

    private Time getMeetingRoomOpenEndTime() {
        String openEndStr = configurationProvider.getValue(ConfigConstants.MEETING_ROOM_OPEN_END_TIME, "20:00:00");
        return Time.valueOf(openEndStr);
    }

    private int getSplitTimeUnitCount() {
        return configurationProvider.getIntValue(ConfigConstants.MEETING_ROOM_OPEN_TIME_SPLIT_COUNT, 48);
    }

    private int getSplitMinuteCount() {
        return configurationProvider.getIntValue(ConfigConstants.MEETING_ROOM_OPEN_TIME_SPLIT_MINUTE_COUNT, 15);
    }

    private int getRecordWordLimit() {
        return configurationProvider.getIntValue(ConfigConstants.MEETING_RECORD_WORD_LIMIT, 500);
    }

    private void initDefaultMeetingRoom(Long organizationId) {
        if (organizationId == null || organizationId < 0) {
            return;
        }
        if (!meetingProvider.shouldInitDefaultMeetingRoom(UserContext.getCurrentNamespaceId(), organizationId)) {
            return;
        }
        this.coordinationProvider.getNamedLock(CoordinationLocks.MEETING_ROOM_DEFAULT_INIT.getCode() + organizationId).enter(() -> {
            // 获得锁后进行二次判断
            if (meetingProvider.shouldInitDefaultMeetingRoom(UserContext.getCurrentNamespaceId(), organizationId)) {
                meetingProvider.createMeetingRoom(defaultMeetingRoom(organizationId, "洽谈室", 10));
                meetingProvider.createMeetingRoom(defaultMeetingRoom(organizationId, "大会议室", 20));
            }
            return null;
        });
    }

    private MeetingRoom defaultMeetingRoom(Long organizationId, String name, Integer seatCount) {
        MeetingRoom meetingRoom = new MeetingRoom();
        meetingRoom.setNamespaceId(UserContext.getCurrentNamespaceId());
        meetingRoom.setOrganizationId(organizationId);
        meetingRoom.setOwnerType(EntityType.ORGANIZATIONS.getCode());
        meetingRoom.setOwnerId(organizationId);
        meetingRoom.setName(name);
        meetingRoom.setSeatCount(seatCount);
        meetingRoom.setDescription("系统默认");
        meetingRoom.setOpenBeginTime(getMeetingRoomOpenBeginTime());
        meetingRoom.setOpenEndTime(getMeetingRoomOpenEndTime());
        meetingRoom.setOperatorName("SYSTEM");
        meetingRoom.setStatus(MeetingRoomStatus.OPENING.getCode());
        meetingRoom.setCreatorUid(Long.valueOf(0));
        meetingRoom.setOperatorUid(Long.valueOf(0));
        return meetingRoom;
    }

    private Long getTimeInMillis(Time time) {
        LocalTime localTime = time.toLocalTime();
        return localTime.getHour() * 3600 * 1000L + localTime.getMinute() * 60 * 1000L + localTime.getSecond() * 1000L;
    }

    private Time getTimeFromMillis(Long millSecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateHelper.currentGMTTime());
        calendar.set(Calendar.HOUR_OF_DAY, (int) (millSecond % (3600 * 1000)));
        calendar.set(Calendar.MINUTE, (int) (millSecond / (60 * 1000)));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Time(calendar.getTime().getTime());
    }

    private Long getTimeInMillis(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    private List<MeetingInvitationDTO> getAndCheckMeetingInvitationDTOs(List<MeetingInvitationDTO> meetingInvitationDTOS) {
        if (CollectionUtils.isEmpty(meetingInvitationDTOS)) {
            return Collections.emptyList();
        }
        meetingInvitationDTOS.forEach(dto -> {
            if (!StringUtils.hasText(dto.getSourceType())) {
                dto.setSourceType(MeetingMemberSourceType.MEMBER_DETAIL.getCode());
            }
        });
        return meetingInvitationDTOS;
    }

    private List<MeetingInvitationDTO> getUserAvatar(List<MeetingInvitationDTO> meetingInvitationDTOS) {
        if (CollectionUtils.isEmpty(meetingInvitationDTOS)) {
            return meetingInvitationDTOS;
        }
        meetingInvitationDTOS.forEach(dto -> {
            if (MeetingMemberSourceType.MEMBER_DETAIL == MeetingMemberSourceType.fromCode(dto.getSourceType())) {
                OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(dto.getSourceId());
                if (memberDetail != null && memberDetail.getTargetId() > 0) {
                    dto.setContactAvatar(getUserAvatar(memberDetail.getTargetId()));
                }
            }
        });
        return meetingInvitationDTOS;
    }

    public String getUserAvatar(Long userId) {
        User user = userProvider.findUserById(userId);
        if (null != user) {
            return contentServerService.parserUri(user.getAvatar());
        }
        return "";
    }
}
