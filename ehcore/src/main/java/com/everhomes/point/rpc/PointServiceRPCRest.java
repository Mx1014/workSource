package com.everhomes.point.rpc;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.point.GetUserPointCommand;
import com.everhomes.rest.point.ListPointLogsCommand;
import com.everhomes.rest.point.ListPointLogsResponse;
import com.everhomes.rest.point.PointLogDTO;
import com.everhomes.rest.point.PointScoreDTO;
import com.everhomes.util.ConvertHelper;

@Service
public class PointServiceRPCRest extends PointServerRPCRestService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PointServiceRPCRest.class);
	
	public PointScoreDTO getUserPoint(GetUserPointCommand cmd) {  
   	
    	RestResponse resp =
                call(URIConstants.POINTSCORE_GETUSERPOINT_URL, cmd, RestResponse.class);
    	if(resp == null){
    		return null ;
    	}
    	Map<String ,Object> map = (Map<String ,Object>)resp.getResponseObject();
    	if(map !=null){
    		//LOGGER.info(resp.getResponseObject().toString());
    		PointScoreDTO dto  = new PointScoreDTO();
    		Integer id = map.get("id")==null?null:(Integer)map.get("id") ;
        	dto.setId(id==null?null:id.longValue());
        	Integer namespaceId = map.get("namespaceId")==null?null:(Integer)map.get("namespaceId") ;
        	dto.setNamespaceId(namespaceId);
        	Integer score = map.get("score")==null?null:(Integer)map.get("score") ;
        	dto.setScore(score==null?null:score.longValue());
        	Integer userId = map.get("userId")==null?null:(Integer)map.get("userId") ;
        	dto.setUserId(userId==null?null:userId.longValue());      
        	return dto ;
    	}
    	
    	return null ;
    }

	public ListPointLogsResponse getUserPointLogs(ListPointLogsCommand cmd) {  
	   	
    	RestResponse resp =
                call(URIConstants.POINTLOGS_LISTUSERPOINTLOGS_URL, cmd, RestResponse.class);
    	if(resp == null){
    		return null ;
    	}
    	Map<String ,Object> map = (Map<String ,Object>)resp.getResponseObject();
    	if(map !=null){
    		//LOGGER.info(resp.getResponseObject().toString());
    		ListPointLogsResponse response  = new ListPointLogsResponse();
    		Integer nextPageAnchor =map.get("nextPageAnchor")==null?null:(Integer)map.get("nextPageAnchor");
    		response.setNextPageAnchor(nextPageAnchor==null?null:nextPageAnchor.longValue());
    		response.setLogs(null);
    		
    		List<PointLogDTO> logs = map.get("logs")==null?null:(List<PointLogDTO>)map.get("logs");
    		if(logs != null){
    			response.setLogs(logs);
    		}
    		
        	return response ;
    	}
    	
    	return null ;
    }


}
