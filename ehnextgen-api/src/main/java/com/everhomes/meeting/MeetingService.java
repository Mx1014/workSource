package com.everhomes.meeting;

import com.everhomes.rest.meeting.AbandonMeetingReservationLockTimeCommand;
import com.everhomes.rest.meeting.CancelMeetingReservationCommand;
import com.everhomes.rest.meeting.CreateMeetingRecordCommand;
import com.everhomes.rest.meeting.CreateOrUpdateMeetingRoomCommand;
import com.everhomes.rest.meeting.CreateOrUpdateMeetingTemplateCommand;
import com.everhomes.rest.meeting.DeleteMeetingRecordCommand;
import com.everhomes.rest.meeting.DeleteMeetingRoomCommand;
import com.everhomes.rest.meeting.DeleteMeetingTemplateCommand;
import com.everhomes.rest.meeting.EndMeetingReservationCommand;
import com.everhomes.rest.meeting.GetMeetingRecordDetailCommand;
import com.everhomes.rest.meeting.GetMeetingReservationDetailCommand;
import com.everhomes.rest.meeting.GetMeetingReservationSimpleByTimeUnitCommand;
import com.everhomes.rest.meeting.GetMeetingRoomDetailCommand;
import com.everhomes.rest.meeting.GetMeetingRoomSimpleInfoCommand;
import com.everhomes.rest.meeting.GetMeetingTemplateCommand;
import com.everhomes.rest.meeting.ListMeetingRoomDetailInfoCommand;
import com.everhomes.rest.meeting.ListMeetingRoomDetailInfoResponse;
import com.everhomes.rest.meeting.ListMeetingRoomSimpleInfoCommand;
import com.everhomes.rest.meeting.ListMeetingRoomSimpleInfoResponse;
import com.everhomes.rest.meeting.ListMyMeetingRecordsCommand;
import com.everhomes.rest.meeting.ListMyMeetingRecordsResponse;
import com.everhomes.rest.meeting.ListMyMeetingTemplateCommand;
import com.everhomes.rest.meeting.ListMyMeetingTemplateResponse;
import com.everhomes.rest.meeting.ListMyMeetingsCommand;
import com.everhomes.rest.meeting.ListMyMeetingsResponse;
import com.everhomes.rest.meeting.ListMyUnfinishedMeetingCommand;
import com.everhomes.rest.meeting.ListMyUnfinishedMeetingResponse;
import com.everhomes.rest.meeting.MeetingRecordDetailInfoDTO;
import com.everhomes.rest.meeting.MeetingReservationDetailDTO;
import com.everhomes.rest.meeting.MeetingReservationLockTimeCommand;
import com.everhomes.rest.meeting.MeetingReservationSimpleDTO;
import com.everhomes.rest.meeting.MeetingRoomDetailInfoDTO;
import com.everhomes.rest.meeting.MeetingRoomSimpleInfoDTO;
import com.everhomes.rest.meeting.MeetingTemplateDTO;
import com.everhomes.rest.meeting.UpdateMeetingRecordCommand;
import com.everhomes.rest.meeting.UpdateMeetingReservationCommand;

public interface MeetingService {

    MeetingRoomSimpleInfoDTO getMeetingRoomSimpleInfo(GetMeetingRoomSimpleInfoCommand cmd);

    MeetingRoomDetailInfoDTO getMeetingRoomDetail(GetMeetingRoomDetailCommand cmd);

    Long createOrUpdateMeetingRoom(CreateOrUpdateMeetingRoomCommand cmd);

    void deleteMeetingRoom(DeleteMeetingRoomCommand cmd);

    ListMeetingRoomSimpleInfoResponse listMeetingRoomSimpleInfo(ListMeetingRoomSimpleInfoCommand cmd);

    ListMeetingRoomDetailInfoResponse listMeetingRoomDetailInfo(ListMeetingRoomDetailInfoCommand cmd);

    MeetingReservationDetailDTO getMeetingReservationDetail(GetMeetingReservationDetailCommand cmd);

    MeetingReservationSimpleDTO getMeetingReservationSimpleInfoByTimeUnit(GetMeetingReservationSimpleByTimeUnitCommand cmd);

    MeetingReservationDetailDTO meetingReservationLockTime(MeetingReservationLockTimeCommand cmd);

    MeetingReservationDetailDTO updateMeetingReservation(UpdateMeetingReservationCommand cmd);

    void abandonMeetingReservationLockTime(AbandonMeetingReservationLockTimeCommand cmd);

    void endMeetingReservation(EndMeetingReservationCommand cmd);

    void cancelMeetingReservation(CancelMeetingReservationCommand cmd);

    ListMyMeetingsResponse listMyMeetings(ListMyMeetingsCommand cmd);

    MeetingRecordDetailInfoDTO getMeetingRecordDetail(GetMeetingRecordDetailCommand cmd);

    MeetingRecordDetailInfoDTO createMeetingRecord(CreateMeetingRecordCommand cmd);

    MeetingRecordDetailInfoDTO updateMeetingRecord(UpdateMeetingRecordCommand cmd);

    void deleteMeetingRecord(DeleteMeetingRecordCommand cmd);

    ListMyMeetingRecordsResponse listMyMeetingRecords(ListMyMeetingRecordsCommand cmd);

    void createOrUpdateMeetingTemplate(CreateOrUpdateMeetingTemplateCommand cmd);

    void deleteMeetingTemplate(DeleteMeetingTemplateCommand cmd);

    ListMyMeetingTemplateResponse listMyMeetingTemplates(ListMyMeetingTemplateCommand cmd);

    MeetingTemplateDTO getMeetingTemplateById(GetMeetingTemplateCommand cmd);

    ListMyUnfinishedMeetingResponse listMyUnfinishedMeetings(ListMyUnfinishedMeetingCommand cmd);

    // 回收会议室资源
    void recoveryMeetingRoomResource();
    // 会议即将开始提醒
    void meetingRemind();

}
