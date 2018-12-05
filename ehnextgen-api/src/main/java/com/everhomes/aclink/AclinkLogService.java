package com.everhomes.aclink;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.aclink.AclinkLogCreateCommand;
import com.everhomes.rest.aclink.AclinkLogListResponse;
import com.everhomes.rest.aclink.AclinkQueryLogCommand;
import com.everhomes.rest.aclink.AclinkQueryLogResponse;
import com.everhomes.rest.aclink.DoorStatisticByTimeCommand;
import com.everhomes.rest.aclink.DoorStatisticByTimeResponse;
import com.everhomes.rest.aclink.DoorStatisticCommand;
import com.everhomes.rest.aclink.DoorStatisticResponse;
import com.everhomes.rest.aclink.OpenQueryLogCommand;
import com.everhomes.rest.aclink.OpenQueryLogResponse;
import com.everhomes.rest.aclink.TempStatisticByTimeCommand;
import com.everhomes.rest.aclink.TempStatisticByTimeResponse;

public interface AclinkLogService {

	AclinkLogListResponse createAclinkLog(AclinkLogCreateCommand cmd);
	
	AclinkLogListResponse createAclinkLogByLocalServer(AclinkLogCreateCommand cmd);

	AclinkQueryLogResponse queryLogs(AclinkQueryLogCommand cmd);
	
    OpenQueryLogResponse openQueryLogs(OpenQueryLogCommand cmd);
	
    //add by liqingyan
    void exportAclinkLogsXls(AclinkQueryLogCommand cmd, HttpServletResponse httpResponse);

    //add by liqingyan
    DoorStatisticResponse doorStatistic(DoorStatisticCommand cmd);
    
    DoorStatisticByTimeResponse doorStatisticByTime(DoorStatisticByTimeCommand cmd);
    
    TempStatisticByTimeResponse tempStatisticByTime(TempStatisticByTimeCommand cmd);

	void recordFaceRecognitionResult(AclinkLogCreateCommand cmd);
}
