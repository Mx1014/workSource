package com.everhomes.point.rpc;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.point.GetUserPointCommand;
import com.everhomes.rest.point.ListPointLogsCommand;
import com.everhomes.rest.point.ListPointLogsResponse;
import com.everhomes.rest.point.PointLogDTO;
import com.everhomes.rest.point.PointScoreDTO;
import com.everhomes.rest.point.PublishEventCommand;

@Service
public class PointServiceRPCRest extends PointServerRPCRestService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PointServiceRPCRest.class);
	
	/**
	 * 获取用户积分
	 * @param cmd
	 * @return
	 */
	public PointScoreDTO getUserPoint(GetUserPointCommand cmd) {
		//update by huangliangming 远程调时所发生的一切问题不能影响该接口的运行,只当是没取到积分而已.
		RestResponse resp = null ;
		try {
    		 resp =
                call(URIConstants.POINTSCORE_GETUSERPOINT_URL, cmd, RestResponse.class);
		}catch(Exception e){
			LOGGER.error("something error happen while RPC to point system . e:{}",e);
		}
    	if(resp == null){
			PointScoreDTO dto  = new PointScoreDTO();
    		return dto ;
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

	/**
	 * 查询用户积分获取
	 * @param cmd
	 * @return
	 */
	
	    public ListPointLogsResponse  getUserPointLogs(ListPointLogsCommand cmd){
	    	//update by huangliangming 远程调时所发生的一切问题不能影响该接口的运行,只当是没取到积分而已.
			RestResponse resp = null ;
			try {
				 resp =
						call(URIConstants.POINTSCORE_COSTUSERPOINTSCOREEVENT_URL, cmd, RestResponse.class);
			}catch(Exception e){
				LOGGER.error("something error happen while RPC to point system . e:{}",e);
			}
			ListPointLogsResponse response  = new ListPointLogsResponse();
			List<PointLogDTO> logs = new ArrayList<PointLogDTO>();
			response.setLogs(logs);
	    	if(resp == null){

	    		return response ;
	    	}
	    	/*Map<String ,Object> map = (Map<String ,Object>)resp.getResponseObject();
	    	if(map !=null){
	    		//LOGGER.info(resp.getResponseObject().toString());
	    		ListPointLogsResponse response  = new ListPointLogsResponse();
	    		Integer nextPageAnchor =map.get("nextPageAnchor")==null?null:(Integer)map.get("nextPageAnchor");
	    		response.setNextPageAnchor(nextPageAnchor==null?null:nextPageAnchor.longValue());
	    		
	    		List<PointLogDTO> logs = map.get("logs")==null?null:(List<PointLogDTO>)map.get("logs");
	    		if(logs != null){
	    			response.setLogs(logs);
	    		}*/
			String str = StringHelper.toJsonString(resp.getResponseObject());
			if(StringUtils.isNotBlank(str)){
			    response = (ListPointLogsResponse)StringHelper.fromJsonString(str,ListPointLogsResponse.class);
	        	return response ;
	    	}
	    	
	    	return response ;
	    }
	    
	    /**
	     * 积分消费
	     * @param cmd
	     * @return
	     */
	    public boolean publishPointCostEvent(PublishEventCommand cmd) {  
		   	
	    	RestResponse resp =
	                call(URIConstants.POINTSCORE_COSTUSERPOINTSCOREEVENT_URL, cmd, RestResponse.class);
	    	if(resp == null){
	    		return false ;
	    	}
	    	Map<String ,Object> map = (Map<String ,Object>)resp.getResponseObject();
	    	if(map !=null){
	    		try{
	    			Boolean result = (Boolean)map.get("result");
	    			LOGGER.info(map.toString());
	    			return result ;
	    		}catch(Exception e){
	    			LOGGER.info(map.toString());
	    			return false ;
	    		}   		   	    	
	    	}
	    	
	    	return false ;
	    }


}
