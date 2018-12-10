package com.everhomes.meeting;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestDoc(value = "Meeting controller", site = "core")
@RestController
@RequestMapping("/meeting")
public class MeetingController extends ControllerBase {
    @Autowired
    private MeetingService meetingService;

    /**
     * <b>URL: /meeting/getMeetingRoomSimpleInfo</b>
     * <p>获取会议室简单信息，管理后台使用</p>
     */
    @RequestMapping("getMeetingRoomSimpleInfo")
    @RestReturn(MeetingRoomSimpleInfoDTO.class)
    public RestResponse getMeetingRoomSimpleInfo(GetMeetingRoomSimpleInfoCommand cmd) {
        MeetingRoomSimpleInfoDTO dto = meetingService.getMeetingRoomSimpleInfo(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/getMeetingRoomDetailInfo</b>
     * <p>获取会议室被预定的时间情况</p>
     */
    @RequestMapping("getMeetingRoomDetailInfo")
    @RestReturn(MeetingRoomDetailInfoDTO.class)
    public RestResponse getMeetingRoomDetailInfo(GetMeetingRoomDetailCommand cmd) {
        MeetingRoomDetailInfoDTO dto = meetingService.getMeetingRoomDetail(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/createOrUpdateMeetingRoom</b>
     * <p>新增或更新会议室</p>
     */
    @RequestMapping("createOrUpdateMeetingRoom")
    @RestReturn(Long.class)
    public RestResponse createOrUpdateMeetingRoom(CreateOrUpdateMeetingRoomCommand cmd) {
        Long id = meetingService.createOrUpdateMeetingRoom(cmd);
        RestResponse response = new RestResponse(id);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/deleteMeetingRoom</b>
     * <p>删除会议室</p>
     */
    @RequestMapping("deleteMeetingRoom")
    @RestReturn(String.class)
    public RestResponse deleteMeetingRoom(DeleteMeetingRoomCommand cmd) {
        meetingService.deleteMeetingRoom(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/listMeetingRoomSimpleInfos</b>
     * <p>查询会议室列表，没有预约信息，管理后台使用</p>
     */
    @RequestMapping("listMeetingRoomSimpleInfos")
    @RestReturn(ListMeetingRoomSimpleInfoResponse.class)
    public RestResponse listMeetingRoomSimpleInfos(ListMeetingRoomSimpleInfoCommand cmd) {
        ListMeetingRoomSimpleInfoResponse listMeetingRoomSimpleInfoResponse = meetingService.listMeetingRoomSimpleInfo(cmd);
        RestResponse response = new RestResponse(listMeetingRoomSimpleInfoResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/listMeetingRoomDetailInfosByDay</b>
     * <p>查询会议室列表详情，有时间预约信息，APP端使用</p>
     */
    @RequestMapping("listMeetingRoomDetailInfosByDay")
    @RestReturn(ListMeetingRoomDetailInfoResponse.class)
    public RestResponse listMeetingRoomDetailInfosByDay(ListMeetingRoomDetailInfoCommand cmd) {
        ListMeetingRoomDetailInfoResponse listMeetingRoomDetailInfoResponse = meetingService.listMeetingRoomDetailInfo(cmd);
        RestResponse response = new RestResponse(listMeetingRoomDetailInfoResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/getMeetingReservationSimpleInfoByTimeUnit</b>
     * <p>通过预订时间单元查询预订该时间的会议信息</p>
     */
    @RequestMapping("getMeetingReservationSimpleInfoByTimeUnit")
    @RestReturn(MeetingReservationSimpleDTO.class)
    public RestResponse getMeetingReservationSimpleInfoByTimeUnit(GetMeetingReservationSimpleByTimeUnitCommand cmd) {
        MeetingReservationSimpleDTO meetingReservationSimpleDTO = meetingService.getMeetingReservationSimpleInfoByTimeUnit(cmd);
        RestResponse response = new RestResponse(meetingReservationSimpleDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/getMeetingReservationDetail</b>
     * <p>获取会议预定详情</p>
     */
    @RequestMapping("getMeetingReservationDetail")
    @RestReturn(MeetingReservationDetailDTO.class)
    public RestResponse getMeetingReservationDetail(GetMeetingReservationDetailCommand cmd) {
        MeetingReservationDetailDTO meetingReservationDetailDTO = meetingService.getMeetingReservationDetail(cmd);
        RestResponse response = new RestResponse(meetingReservationDetailDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/meetingReservationLockTime</b>
     * <p>会议预订时间锁定/更新会议室时间锁定</p>
     */
    @RequestMapping("meetingReservationLockTime")
    @RestReturn(MeetingReservationDetailDTO.class)
    public RestResponse meetingReservationLockTime(MeetingReservationLockTimeCommand cmd) {
        MeetingReservationDetailDTO dto = meetingService.meetingReservationLockTime(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/abandonMeetingReservationLockTime</b>
     * <p>取消会议的时间锁定</p>
     */
    @RequestMapping("abandonMeetingReservationLockTime")
    @RestReturn(String.class)
    public RestResponse abandonMeetingReservationLockTime(AbandonMeetingReservationLockTimeCommand cmd) {
        meetingService.abandonMeetingReservationLockTime(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/updateMeetingReservation</b>
     * <p>完成、修改会议预约</p>
     */
    @RequestMapping("updateMeetingReservation")
    @RestReturn(MeetingReservationDetailDTO.class)
    public RestResponse updateMeetingReservation(UpdateMeetingReservationCommand cmd) {
        MeetingReservationDetailDTO dto = meetingService.updateMeetingReservation(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/endMeetingReservation</b>
     * <p>(提前)结束会议</p>
     */
    @RequestMapping("endMeetingReservation")
    @RestReturn(String.class)
    public RestResponse endMeetingReservation(EndMeetingReservationCommand cmd) {
        meetingService.endMeetingReservation(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/cancelMeetingReservation</b>
     * <p>取消会议</p>
     */
    @RequestMapping("cancelMeetingReservation")
    @RestReturn(String.class)
    public RestResponse cancelMeetingReservation(CancelMeetingReservationCommand cmd) {
        meetingService.cancelMeetingReservation(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/listMyMeetings</b>
     * <p>列出我的会议列表，发起的或者参与的</p>
     */
    @RequestMapping("listMyMeetings")
    @RestReturn(ListMyMeetingsResponse.class)
    public RestResponse listMyMeetings(ListMyMeetingsCommand cmd) {
        ListMyMeetingsResponse listMyMeetingsResponse = meetingService.listMyMeetings(cmd);
        RestResponse response = new RestResponse(listMyMeetingsResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/getMeetingRecordDetail</b>
     * <p>获取会议纪要详情</p>
     */
    @RequestMapping("getMeetingRecordDetail")
    @RestReturn(MeetingRecordDetailInfoDTO.class)
    public RestResponse getMeetingRecordDetail(GetMeetingRecordDetailCommand cmd) {
        MeetingRecordDetailInfoDTO meetingRecordDetailInfoDTO = meetingService.getMeetingRecordDetail(cmd);
        RestResponse response = new RestResponse(meetingRecordDetailInfoDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/createMeetingRecord</b>
     * <p>创建会议纪要</p>
     */
    @RequestMapping("createMeetingRecord")
    @RestReturn(MeetingRecordDetailInfoDTO.class)
    public RestResponse createMeetingRecord(CreateMeetingRecordCommand cmd) {
        MeetingRecordDetailInfoDTO dto = meetingService.createMeetingRecord(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/updateMeetingRecord</b>
     * <p>更新会议纪要</p>
     */
    @RequestMapping("updateMeetingRecord")
    @RestReturn(MeetingRecordDetailInfoDTO.class)
    public RestResponse updateMeetingRecord(UpdateMeetingRecordCommand cmd) {
        MeetingRecordDetailInfoDTO dto = meetingService.updateMeetingRecord(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/deleteMeetingRecord</b>
     * <p>删除会议纪要</p>
     */
    @RequestMapping("deleteMeetingRecord")
    @RestReturn(String.class)
    public RestResponse deleteMeetingRecord(DeleteMeetingRecordCommand cmd) {
        meetingService.deleteMeetingRecord(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/listMyMeetingRecords</b>
     * <p>列出我的纪要列表</p>
     */
    @RequestMapping("listMyMeetingRecords")
    @RestReturn(ListMyMeetingRecordsResponse.class)
    public RestResponse listMyMeetingRecords(ListMyMeetingRecordsCommand cmd) {
        ListMyMeetingRecordsResponse listMyMeetingRecordsResponse = meetingService.listMyMeetingRecords(cmd);
        RestResponse response = new RestResponse(listMyMeetingRecordsResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/createOrUpdateMeetingTemplate</b>
     * <p>新增或编辑会议模板</p>
     */
    @RequestMapping("createOrUpdateMeetingTemplate")
    @RestReturn(String.class)
    public RestResponse createOrUpdateMeetingTemplate(CreateOrUpdateMeetingTemplateCommand cmd) {
        meetingService.createOrUpdateMeetingTemplate(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/deleteMeetingTemplate</b>
     * <p>删除会议模板</p>
     */
    @RequestMapping("deleteMeetingTemplate")
    @RestReturn(String.class)
    public RestResponse deleteMeetingTemplate(DeleteMeetingTemplateCommand cmd) {
        meetingService.deleteMeetingTemplate(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/listMyMeetingTemplates</b>
     * <p>我的会议模板</p>
     */
    @RequestMapping("listMyMeetingTemplates")
    @RestReturn(ListMyMeetingTemplateResponse.class)
    public RestResponse listMyMeetingTemplates(ListMyMeetingTemplateCommand cmd) {
        RestResponse response = new RestResponse(meetingService.listMyMeetingTemplates(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/getMeetingTemplateById</b>
     * <p>我的会议模板</p>
     */
    @RequestMapping("getMeetingTemplateById")
    @RestReturn(MeetingTemplateDTO.class)
    public RestResponse getMeetingTemplateById(GetMeetingTemplateCommand cmd) {
        RestResponse response = new RestResponse(meetingService.getMeetingTemplateById(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /meeting/listMyUnfinishedMeetings</b>
     * <p>列出我的未结束的会议</p>
     */
    @RequestMapping("listMyUnfinishedMeetings")
    @RestReturn(ListMyUnfinishedMeetingResponse.class)
    public RestResponse listMyUnfinishedMeetings(ListMyUnfinishedMeetingCommand cmd) {
        RestResponse response = new RestResponse(meetingService.listMyUnfinishedMeetings(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
