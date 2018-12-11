package com.everhomes.aclink;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.everhomes.app.App;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.openapi.AppNamespaceMapping;
import com.everhomes.openapi.AppNamespaceMappingProvider;
import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.rest.aclink.AclinkLogCreateCommand;
import com.everhomes.rest.aclink.AclinkLogDTO;
import com.everhomes.rest.aclink.AclinkLogEventType;
import com.everhomes.rest.aclink.AclinkLogItem;
import com.everhomes.rest.aclink.AclinkLogListResponse;
import com.everhomes.rest.aclink.AclinkQueryLogCommand;
import com.everhomes.rest.aclink.AclinkQueryLogResponse;
import com.everhomes.rest.aclink.AclinkServiceErrorCode;
import com.everhomes.rest.aclink.DoorAccessOwnerType;
import com.everhomes.rest.aclink.DoorStatisticByTimeCommand;
import com.everhomes.rest.aclink.DoorStatisticByTimeDTO;
import com.everhomes.rest.aclink.DoorStatisticByTimeResponse;
import com.everhomes.rest.aclink.DoorStatisticCommand;
import com.everhomes.rest.aclink.DoorStatisticDTO;
import com.everhomes.rest.aclink.DoorStatisticResponse;
import com.everhomes.rest.aclink.OpenAclinkLogDTO;
import com.everhomes.rest.aclink.OpenQueryLogCommand;
import com.everhomes.rest.aclink.OpenQueryLogResponse;
import com.everhomes.rest.aclink.TempStatisticByTimeCommand;
import com.everhomes.rest.aclink.TempStatisticByTimeDTO;
import com.everhomes.rest.aclink.TempStatisticByTimeResponse;
import com.everhomes.rest.techpark.punch.CreateType;
import com.everhomes.rest.techpark.punch.ThirdPartPunchClockCommand;
import com.everhomes.rest.techpark.punch.ThirdPartPunchClockUerDTO;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.techpark.punch.PunchService;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class AclinkLogServiceImpl implements AclinkLogService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AclinkLogServiceImpl.class);
	
	@Autowired
	private DoorAccessProvider doorAccessProvider;

    @Autowired
    private DoorAccessService doorAccessService;
	
    @Autowired
    private DoorAuthProvider doorAuthProvider;
    
    @Autowired
    private AclinkLogProvider aclinkLogProvider;
    
    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    AppNamespaceMappingProvider appNamespaceMappingProvider;
    
	@Autowired
	private PunchService punchService;

    @Override
    public AclinkLogListResponse createAclinkLog(AclinkLogCreateCommand cmds) {
        AclinkLogListResponse resp = new AclinkLogListResponse();
        resp.setDtos(new ArrayList<AclinkLogDTO>());
        
        List<Long> qryAuthIds = cmds.getItems().stream().map(r -> r.getAuthId()).collect(Collectors.toList());
        List<DoorAuth> listAuth = doorAuthProvider.queryDoorAuth(new CrossShardListingLocator(), 0, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_AUTH.ID.in(qryAuthIds));
                return query;
            }
        });
        
        List<Long> qryDoorIds = listAuth.stream().map(r -> r.getDoorId()).collect(Collectors.toList());
        List<DoorAccess> listDoors = doorAccessProvider.queryDoorAccesss(new CrossShardListingLocator(), 0, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_DOOR_ACCESS.ID.in(qryDoorIds));
				return query;
			}
		});
        
        List<AclinkLog> listAclinkLog = new ArrayList<AclinkLog>();
        for(int i = 0; i < cmds.getItems().size(); i++) {
            AclinkLogItem cmd = cmds.getItems().get(i);
            AclinkLog aclinkLog = ConvertHelper.convert(cmd, AclinkLog.class);
            
            try {
            	DoorAuth doorAuth = listAuth.stream().filter(r -> r.getId().equals(aclinkLog.getAuthId())).findAny().get();
            	DoorAccess door = listDoors.stream().filter(r -> r.getId().equals(aclinkLog.getDoorId())).findAny().get();
            	aclinkLog.setUserName(doorAuth.getNickname());
            	aclinkLog.setUserIdentifier(doorAuth.getPhone());
            	aclinkLog.setUserId(doorAuth.getUserId());
                aclinkLog.setDoorName(door.getDisplayNameNotEmpty());
                aclinkLog.setHardwareId(door.getHardwareId());
                aclinkLog.setOwnerId(door.getOwnerId());
                aclinkLog.setOwnerType(door.getOwnerType());
                aclinkLog.setDoorType(door.getDoorType());
                
                listAclinkLog.add(aclinkLog);
                AclinkLogDTO dto = ConvertHelper.convert(aclinkLog, AclinkLogDTO.class);
                if(dto != null) {
                    resp.getDtos().add(dto);
                }
            } catch(Exception ex) {
                LOGGER.error("aclinklog error i=" + i, ex);
                continue;
            }
        }
        aclinkLogProvider.createAclinkLogBatch(listAclinkLog);
        //临时授权消息提示
        listAclinkLog.stream().forEach(aclinkLog -> {
            doorAccessService.sendMessageToAuthCreator(aclinkLog.getAuthId());
        });
        
        //调用打卡
        doThirdPartPunchClock(listAuth, cmds.getItems(), AclinkLogEventType.PHONE_QR_OPEN.getCode(), CreateType.DOOR_PUNCH.getCode());
        
        return resp;
    }

	
	@Override
	public AclinkLogListResponse createAclinkLogByLocalServer(AclinkLogCreateCommand cmd) {
		AclinkLogListResponse resp = createAclinkLog(cmd);
		return resp;
	}
	
	@Override
	public void recordFaceRecognitionResult(AclinkLogCreateCommand cmd) {
		//暂不考虑失效的门禁,门禁若已失效,应在上层判断是否调用该方法
		List<Long> qryAuthIds = cmd.getItems().stream().map(r -> r.getAuthId()).collect(Collectors.toList());
        List<DoorAuth> listAuth = doorAuthProvider.queryDoorAuth(new CrossShardListingLocator(), 0, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_AUTH.ID.in(qryAuthIds));
                return query;
            }
        });
        
        doThirdPartPunchClock(listAuth, cmd.getItems(), AclinkLogEventType.FACE_OPEN.getCode(), CreateType.FACE_PUNCH.getCode());
        
	} 
	
	//根据开门类型调用打卡,listAuth和AclinkLogEventType作为限制条件
	private void doThirdPartPunchClock(List<DoorAuth> listAuth, List<AclinkLogItem> listLog, Long logType, Byte creteType){
        long now = DateHelper.currentGMTTime().getTime();
        //批量传识别结果,可能包含多个门禁
        for(DoorAuth auth : listAuth){
        	if(DoorAccessOwnerType.ENTERPRISE.getCode() == auth.getOwnerType()){
        		ThirdPartPunchClockCommand tpCmd = new ThirdPartPunchClockCommand();
        		tpCmd.setCreateType(creteType);
        		tpCmd.setIdentification(String.valueOf(auth.getDoorId()));
        		tpCmd.setEnterpriseId(auth.getOwnerId());
        		tpCmd.setNamespaceId(auth.getNamespaceId());
				tpCmd.setUsers(listLog.stream()
						.filter(r -> logType == r.getEventType() 
								&& auth.getId().equals(r.getAuthId())
								&& r.getDoorId().equals(auth.getDoorId()) 
								&& now - 10 * 60 * 1000 < r.getLogTime())
						.map(r -> {
							ThirdPartPunchClockUerDTO tpDto = new ThirdPartPunchClockUerDTO();
							tpDto.setPunchTime(r.getLogTime());
							tpDto.setUserId(r.getUserId());
        			return tpDto;
        		}).collect(Collectors.toList()));
				if(tpCmd.getUsers().size() > 0){
					try {
						punchService.thirdPartPunchClock(tpCmd);
					} catch (Exception e) {
						LOGGER.warn(
								String.format("error in thirdPartPunchClock by aclinkLog, cmd:{}", tpCmd.toString()));
						LOGGER.error(e.getMessage());
					}
				}
        	}
        }
	}
	
    //add by liqingyan 日志导出
    @Override
    public void exportAclinkLogsXls(AclinkQueryLogCommand cmd, HttpServletResponse httpResponse){
        ByteArrayOutputStream output = null;
        XSSFWorkbook wb = this.createLogsXSSFWorkbook(cmd);
        try {
            output = new ByteArrayOutputStream();
            wb.write(output);
            DownloadUtil.download(output, httpResponse);
        } catch (Exception e) {
            LOGGER.error("export error, e = {}", e);
        } finally{
            try {
                wb.close();
                output.close();
            } catch (IOException e) {
                LOGGER.error("close error", e);
            }
        }
    }
    
  //add by liqingyan 待翻译
    /**
     * 创建导出日志excel
     * @param cmd
     * @return
     */
    private XSSFWorkbook createLogsXSSFWorkbook(AclinkQueryLogCommand cmd){
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());

        XSSFWorkbook wb = new XSSFWorkbook();
        String sheetName = "门禁日志列表";
        XSSFSheet sheet = wb.createSheet(sheetName);
        XSSFCellStyle style = wb.createCellStyle();// 样式对象
        Font font = wb.createFont();
        font.setFontHeightInPoints((short)20);
        font.setFontName("Courier New");

        style.setFont(font);

        XSSFCellStyle titleStyle = wb.createCellStyle();// 样式对象
        titleStyle.setFont(font);
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        int rowNum = 0;

        XSSFRow row1 = sheet.createRow(rowNum ++);
        row1.setRowStyle(style);
        int cellN = 0;
        row1.createCell(cellN ++).setCellValue("姓名");
        row1.createCell(cellN ++).setCellValue("手机号码");
        row1.createCell(cellN ++).setCellValue("门禁名称");
        row1.createCell(cellN ++).setCellValue("授权类型");
        row1.createCell(cellN ++).setCellValue("开门方式");
        row1.createCell(cellN ++).setCellValue("开门时间");

        cmd.setPageSize(1000);
        while (true){
            AclinkQueryLogResponse userRes = this.queryLogs(cmd);
            for (AclinkLogDTO user: userRes.getDtos()) {
                cellN = 0;
                XSSFRow row = sheet.createRow(rowNum ++);
                row.setRowStyle(style);
                row.createCell(cellN ++).setCellValue(user.getUserName());
                row.createCell(cellN ++).setCellValue(user.getUserIdentifier());
                row.createCell(cellN ++).setCellValue(user.getDoorName());
                row.createCell(cellN ++).setCellValue(user.getAuthType() > 0 ? "常规授权" : "临时授权");
                row.createCell(cellN ++).setCellValue(user.getEventType().equals(0L) ? "蓝牙开门": equals(1L)? "二维码开门": equals(2L)? "远程开门":equals(3L)? "人脸开门":"");
                row.createCell(cellN ++).setCellValue(DateUtil.dateToStr(new Date(user.getLogTime()), DateUtil.DATE_TIME_LINE));
            }

            if(null == userRes.getNextPageAnchor()){
                break;
            }
            cmd.setPageAnchor(userRes.getNextPageAnchor());
        }

        return wb;
    }
    
    @Override
    public AclinkQueryLogResponse queryLogs(AclinkQueryLogCommand cmd) {
        //添加权限 by liqingyan
//        if (cmd.getOwnerType() != null && cmd.getOwnerType() == 1 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
//            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4112041122L, cmd.getAppId(), null, cmd.getCurrentProjectId());
//        }
        if (cmd.getOwnerType() != null && cmd.getOwnerType() == 0 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4111041112L, cmd.getAppId(), null, cmd.getCurrentProjectId());
        }
        AclinkQueryLogResponse resp = new AclinkQueryLogResponse();
        resp.setDtos(new ArrayList<AclinkLogDTO>());
        int count = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        //issue-42656 选门禁组查门禁组下所有门禁 by liuyilin 20181119
        List<DoorAccess> doors = doorAccessProvider.listDoorAccessByGroupId(cmd.getDoorId() != null? cmd.getDoorId():cmd.getGroupId() , 0);
		List<Long> doorIds = new ArrayList<Long>();
		// TODO 3.0,门禁组类型需要传,根据组类型判断是否添加doorId
		if(cmd.getDoorId() != null){
			doorIds.add(cmd.getDoorId());
		}
		if (doors != null && doors.size() > 0) {
			for (DoorAccess door : doors) {
				doorIds.add(door.getId());
			}
		}
		
        List<AclinkLogDTO> objs = aclinkLogProvider.queryAclinkLogDTOsByTime(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                if(cmd.getOwnerType() != null) {
                    query.addConditions(Tables.EH_ACLINK_LOGS.OWNER_TYPE.eq(cmd.getOwnerType()));
                }
                if(cmd.getOwnerId() != null) {
                    query.addConditions(Tables.EH_ACLINK_LOGS.OWNER_ID.eq(cmd.getOwnerId()));
                }
                if(cmd.getEventType() != null) {
                    query.addConditions(Tables.EH_ACLINK_LOGS.EVENT_TYPE.eq(cmd.getEventType()));
                }
                if(cmd.getKeyword() != null) {
                    query.addConditions(Tables.EH_ACLINK_LOGS.USER_IDENTIFIER.like("%" + cmd.getKeyword() + "%")
                            .or(Tables.EH_ACLINK_LOGS.USER_NAME.like("%" + cmd.getKeyword()+"%")));
                }
                query.addConditions(Tables.EH_ACLINK_LOGS.DOOR_ID.in(doorIds));
                //时间比较
                if(cmd.getStartTime() != null && cmd.getEndTime() != null ) {
                    cmd.setStartTime(DateHelper.parseDataString(DateHelper.getDateDisplayString(TimeZone.getTimeZone("GMT+:08:00"), cmd.getStartTime()),"yyyy-MM-dd").getTime());
                    cmd.setEndTime(DateHelper.parseDataString(DateHelper.getDateDisplayString(TimeZone.getTimeZone("GMT+:08:00"), cmd.getEndTime()),"yyyy-MM-dd").getTime() + 24*60*60*1000);
                    query.addConditions(Tables.EH_ACLINK_LOGS.CREATE_TIME.between(new Timestamp(cmd.getStartTime()), new Timestamp(cmd.getEndTime())));
                }
                return query;
            }
            
        });
        resp.setDtos(objs);
        resp.setNextPageAnchor(locator.getAnchor());
        return resp;
    }

    @Override
    public OpenQueryLogResponse openQueryLogs(OpenQueryLogCommand cmd){
    	AppNamespaceMapping appNamespaceMapping = appNamespaceMappingProvider.findAppNamespaceMappingByAppKey(cmd.getAppKey());
		if (appNamespaceMapping == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"not exist app namespace mapping");
		}
		Integer namespaceId = appNamespaceMapping.getNamespaceId();
		
    	OpenQueryLogResponse resp = new OpenQueryLogResponse();
        resp.setDtos(new ArrayList<OpenAclinkLogDTO>());
        if(CollectionUtils.isEmpty(cmd.getMacAddresses())){
			return resp;
		}
        
		//TODO 上海华润对接调试用,待appSecret与公司关联实现后补完 by liuyilin 20180802
		App app = UserContext.current().getCallerApp();
		if(app == null || app.getName() == null){
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "auth error");
		}
		Long ownerId = Long.valueOf(app.getName());
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(DoorAccessOwnerType.ENTERPRISE.getCode());

        int count = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<AclinkLog> objs = aclinkLogProvider.queryAclinkLogsByTime(locator, count, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                if(cmd.getOwnerType() != null) {
                    query.addConditions(Tables.EH_ACLINK_LOGS.OWNER_TYPE.eq(cmd.getOwnerType()));
                }
                if(cmd.getOwnerId() != null) {
                    query.addConditions(Tables.EH_ACLINK_LOGS.OWNER_ID.eq(cmd.getOwnerId()));
                }
                if(cmd.getEventType() != null) {
                    query.addConditions(Tables.EH_ACLINK_LOGS.EVENT_TYPE.eq(cmd.getEventType()));
                }
                if(cmd.getKeyword() != null) {
                    query.addConditions(Tables.EH_ACLINK_LOGS.USER_IDENTIFIER.like(cmd.getKeyword() + "%")
                            .or(Tables.EH_ACLINK_LOGS.USER_NAME.like(cmd.getKeyword()+"%")));
                }
                if(cmd.getValidEndMs() != null){
                	query.addConditions(Tables.EH_ACLINK_LOGS.LOG_TIME.lt(cmd.getValidEndMs()));
                }
                if(cmd.getValidFromMs() != null){
                	query.addConditions(Tables.EH_ACLINK_LOGS.LOG_TIME.gt(cmd.getValidFromMs()));
                }
                
                query.addConditions(Tables.EH_ACLINK_LOGS.HARDWARE_ID.in(cmd.getMacAddresses()));
                //namespace可能有误,已用owner限制 by liuyilin 20181113
//                query.addConditions(Tables.EH_ACLINK_LOGS.NAMESPACE_ID.eq(namespaceId));
                return query;
            }

        });

        for(AclinkLog obj : objs) {
        	OpenAclinkLogDTO dto = ConvertHelper.convert(obj, OpenAclinkLogDTO.class);
        	dto.setMacAddress(obj.getHardwareId());
            resp.getDtos().add(dto);
        }
        resp.setNextPageAnchor(locator.getAnchor());
        return resp;
    }
    
    //add by liqingyan
   @Override
   public DoorStatisticResponse doorStatistic (DoorStatisticCommand cmd){
       //添加权限
//       if (cmd.getOwnerType() != null && cmd.getOwnerType() == 1 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
//           userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4112041123L, cmd.getAppId(), null, cmd.getCurrentProjectId());
//       }
       if (cmd.getOwnerType() != null && cmd.getOwnerType() == 0 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
           userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4111041113L, cmd.getAppId(), null, cmd.getCurrentProjectId());
       }
       DoorStatisticResponse resp = new DoorStatisticResponse();
       resp.setDtos(new DoorStatisticDTO());
       DoorStatisticDTO objs =aclinkLogProvider.queryDoorStatistic(cmd);
       resp.setDtos(objs);
       return resp;
   }
   
   //add by liqingyan
   @Override
   public DoorStatisticByTimeResponse doorStatisticByTime (DoorStatisticByTimeCommand cmd){
       //添加权限
//       if (cmd.getOwnerType() != null && cmd.getOwnerType() == 1 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
//           userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4112041123L, cmd.getAppId(), null, cmd.getCurrentProjectId());
//       }
       if (cmd.getOwnerType() != null && cmd.getOwnerType() == 0 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
           userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4111041113L, cmd.getAppId(), null, cmd.getCurrentProjectId());
       }
       DoorStatisticByTimeResponse resp = new DoorStatisticByTimeResponse();
       resp.setDtos(new ArrayList<DoorStatisticByTimeDTO>());
       List<DoorStatisticByTimeDTO> objs = aclinkLogProvider.queryDoorStatisticByTime(cmd);
       resp.setDtos(objs);
       return resp;
   }

   //add by liqingyan
   @Override
   public TempStatisticByTimeResponse tempStatisticByTime (TempStatisticByTimeCommand cmd){
       //添加权限
//       if (cmd.getOwnerType() != null && cmd.getOwnerType() == 1 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
//           userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4112041123L, cmd.getAppId(), null, cmd.getCurrentProjectId());
//       }
       if (cmd.getOwnerType() != null && cmd.getOwnerType() == 0 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
           userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4111041113L, cmd.getAppId(), null, cmd.getCurrentProjectId());
       }
       TempStatisticByTimeResponse resp = new TempStatisticByTimeResponse();
       resp.setDtos(new ArrayList<TempStatisticByTimeDTO>());
       List<TempStatisticByTimeDTO> objs = aclinkLogProvider.queryTempStatisticByTime(cmd);
       resp.setDtos(objs);
       return resp;
   }

}
