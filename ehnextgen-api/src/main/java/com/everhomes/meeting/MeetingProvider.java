package com.everhomes.meeting;

import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.meeting.QueryMyMeetingTemplateCondition;
import com.everhomes.server.schema.tables.pojos.EhMeetingInvitations;
import com.everhomes.server.schema.tables.pojos.EhMeetingReservations;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

public interface MeetingProvider {

    MeetingRoom findMeetingRoomById(Integer namespaceId, Long organizationId, Long meetingRoomId);

    boolean checkMeetingRoomNameExist(CheckMeetingRoomNameExistCondition condition);

    Long createMeetingRoom(MeetingRoom meetingRoom);

    Long updateMeetingRoom(MeetingRoom meetingRoom);

    List<MeetingRoom> findMeetingRooms(Integer pageSize, Integer offset, ListingQueryBuilderCallback queryBuilderCallback);

    boolean shouldInitDefaultMeetingRoom(Integer namespaceId, Long organizationId);

    boolean checkMeetingTimeIsAvailable(CheckMeetingTimeAvailableCondition condition);

    List<MeetingReservation> findMeetingReservationByTimeUnit(Long meetingRoomId, Date meetingDate, Time beginTime, Time endTime);

    // 查找该会议室已预约的、还没开始的会议
    List<MeetingReservation> findNotBeginningMeetingReservationByRoomId(Long meetingRoomId);

    // 查找会议室在某个日期内的预定情况
    List<MeetingReservation> findMeetingRoomReservationDetails(List<Long> meetingRoomIds, Date queryBeginDate, Date queryEndDate);

    boolean checkMeetingReservationBeRecovery(Long meetingReservationId);

    MeetingReservation findMeetingReservationById(Integer namespaceId, Long organizationId, Long meetingReservationId);

    List<EhMeetingReservations> findLockStatusMeetingReservations(Timestamp beforeLockTime, int limitCount);

    List<EhMeetingReservations> findLockStatusMeetingReservationsBySponsorUserId(Long sponsorUserId, Long meetingRoomId);

    List<MeetingReservation> findComingSoonMeetingReservations(int comingSoonMinute, int limitCount);

    Long createMeetingReservation(MeetingReservation meetingReservation);

    Long updateMeetingReservation(MeetingReservation meetingReservation);

    void deleteMeetingReservation(MeetingReservation meetingReservation);

    void batchDeleteMeetingReservations(List<EhMeetingReservations> meetingReservations);

    void batchUpdateMeetingReservationRemindStatus(List<MeetingReservation> meetingReservations);

    List<MeetingReservation> findMeetingReservationsByDetailId(QueryMyMeetingsCondition condition);

    List<EhMeetingInvitations> findMeetingInvitationsByMeetingId(Long meetingReservationId, String... roleTypes);

    int countMeetingInvitation(Long meetingReservationId, String sourceType, Long sourceId);

    void batchCreateMeetingInvitations(List<EhMeetingInvitations> meetingInvitations);

    void batchDeleteMeetingInvitationsByMeetingId(Long meetingReservationId, String... roleTypes);

    void batchDeleteMeetingInvitations(List<EhMeetingInvitations> meetingInvitations);

    Long createMeetingRecord(MeetingRecord meetingRecord);

    Long updateMeetingRecord(MeetingRecord meetingRecord);

    void deleteMeetingRecord(MeetingRecord meetingRecord);

    MeetingRecord findMeetingRecordById(Long id);

    MeetingRecord findMeetingRecordByMeetingReservationId(Long meetingReservationId);

    List<MeetingRecord> findMeetingRecordsByDetailId(QueryMyMeetingRecordsCondition condition);

	List<MeetingAttachment> listMeetingAttachments(Long ownerId, String ownerType);

	void batchDeleteMeetingAttachments(List<MeetingAttachment> deleteAttachements);

	void batchCreateMeetingAttachments(List<MeetingAttachment> addAttachements);

    void deleteMeetingAttachmentsByOwnerId(Integer namespaceId, String ownerType, Long ownerId);

    MeetingTemplate findMeetingTemplateById(Long id, Integer namespaceId, Long organizationId, Long userId);

    Long createMeetingTemplate(MeetingTemplate template);

    Long updateMeetingTemplate(MeetingTemplate template);

    void deleteMeetingTemplate(MeetingTemplate template);

    List<MeetingTemplate> findMeetingTemplates(QueryMyMeetingTemplateCondition condition);

    List<MeetingInvitationTemplate> findMeetingInvitationTemplates(Integer namespaceId, Long organizationId, Long meetingTemplateId);

    void deleteMeetingInvitationTemplates(Integer namespaceId, Long organizationId, Long meetingTemplateId);

    void batchCreateMeetingInvitationTemplate(List<MeetingInvitationTemplate> invitationTemplates);

    void batchDeleteMeetingInvitationTemplate(List<MeetingInvitationTemplate> invitationTemplates);
}
